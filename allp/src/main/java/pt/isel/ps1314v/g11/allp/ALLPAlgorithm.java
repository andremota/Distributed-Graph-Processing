package pt.isel.ps1314v.g11.allp;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Vertex;
import pt.isel.ps1314v.g11.llp.LLPMessage;
import pt.isel.ps1314v.g11.llp.NeighboorLabelValues;

/**
 *Layered Label Propagation implementation 
 *
 */
public class ALLPAlgorithm extends Algorithm<LongWritable, ALLPVertexValue, NullWritable, LLPMessage>  implements Configurable{

	private final Logger LOG = Logger.getLogger(ALLPAlgorithm.class);
	
	public static final String GLOBAL_CHANGE_AGGREGATOR = "pt.isel.ps1314v.g11.llp.LLPAlgorithm.GLOBAL_CHANGE_AGGREGATOR";
	public static final String DECISION_FACTOR = "pt.isel.ps1314v.g11.llp.LLPAlgorithm.DECISION_FACTOR";
	public static final String COUNT_VERTEX_AS_OWN_NEIGHBOR = "pt.isel.ps1314v.g11.llp.LLPAlgorithm.COUNT_VERTEX_AS_OWN_NEIGHBOR";
	
	private final BigDecimal DEFAULT_DECISION_FACTOR = BigDecimal.ONE;
	private BigDecimal decisionFactor;
	
	private final boolean COUNT_VERTEX_AS_LI_DEFAULT = false;
	private boolean countVertexNg;
	
	private Configuration conf;
	
	@Override
	public void compute(
			Vertex<LongWritable, ALLPVertexValue, NullWritable> vertex,
			Iterable<LLPMessage> messages) {

		if(getSuperstep() == 0){
			//Give each vertex an unique label.
			vertex.setVertexValue(new ALLPVertexValue(vertex.getId().get(),vertex.getId().get()));
//			LOG.info("Vertex{"+vertex.getId()+"} set label "+vertex.getId());
		}
		
		int minorStep = (int)(getSuperstep()%3);
		
		switch(minorStep){
			case 0:
//				LOG.info("On minorstep 0.");
				updateAndSendToNeighborhood(vertex, messages);
				break;
			case 1:
//				LOG.info("On minorstep 1.");
				calculateLabelAndSendToHub(vertex, messages);
				break;
			case 2:
//				LOG.info("On minorstep 2");
				updateCommunity(vertex, messages);
				break;
		}
	}

	private void updateAndSendToNeighborhood(
			Vertex<LongWritable, ALLPVertexValue, NullWritable> vertex,
			Iterable<LLPMessage> messages) {
		
		//Update vi for this vertex label. 
		
		long vi = 1; //default vi is 1 for the first phase in superstep 0.
		
		if(getSuperstep() > 0){
			//in superstep != 0 it should always receive the updated vi.
			Iterator<LLPMessage> itHubMessage = messages.iterator();
			if(itHubMessage.hasNext()){
				//if it has a message then it means the Hub of this vertex label has sent an updated vi value.
				LLPMessage message = itHubMessage.next();
				vi = message.getVi();
				
//				LOG.info("Vertex{"+vertex.getId()+"} change to " + vertex.getVertexValue());
				if(itHubMessage.hasNext())
					throw new IllegalStateException("Vertex{"+vertex.getId()+"} has more than one message.");
			}else{
				throw new IllegalStateException("Vertex{"+vertex.getId()+"} has no messages.");
			}
		}
		

//		LOG.info("On vertex{"+vertex.getId()+"} and sending to neighbor label " + vertex.getVertexValue().get());
		//send this vertex current community and vi to the adjacent vertices.
		sendMessageToNeighbors(vertex, 
				new LLPMessage(vertex.getId().get(),
						vi,
						vertex.getVertexValue().getLabel()));
		
	}

	private void calculateLabelAndSendToHub(
			Vertex<LongWritable, ALLPVertexValue, NullWritable> vertex,
			Iterable<LLPMessage> messages) {


		ALLPVertexValue vertexVal = vertex.getVertexValue();
		
		if(vertexVal.shouldStop()){
			vertexVal.setShouldStop(false);
			vertex.voteToHalt();
			sendMessageToVertex(new LongWritable(vertex.getVertexValue().getLabel()),
					new LLPMessage(vertex.getId().get()));
			return;
		}
		
		//kis values for each label in the neighbor.
		HashMap<Long, NeighboorLabelValues> adjacentLabelsEntries = new HashMap<Long, NeighboorLabelValues>(); 
		//adjacentLabelsEntries.
		for(LLPMessage message : messages){

//			LOG.info("On vertex{"+vertex.getId()+"} and received  from neighbor label " + message.getLabeli());
			
			long labeli = message.getLabeli(); 
			NeighboorLabelValues val = adjacentLabelsEntries.get(labeli);
			if(val != null){
				val.setKi(val.getKi()+1); // Update the vertices in the adjacency with this label.
			}else{
				if(countVertexNg){
					val = new NeighboorLabelValues(
							vertexVal.getLabel() == labeli? 2 : 1, 
							message.getVi());
				}else{
					val = new NeighboorLabelValues(1, message.getVi());
				}
				
				//The added value has the most up to date vi value.
				adjacentLabelsEntries.put(labeli, val);
			}
		}
		
		//Calculate the maximal label in the adjacency.
		
		BigDecimal max;
		NeighboorLabelValues ownLabel = adjacentLabelsEntries.get(vertex.getVertexValue().getLabel());
		if(ownLabel != null){
			//calculate for this vertex community.
			max = calculateLabel(new BigDecimal(ownLabel.getVi()), new BigDecimal(ownLabel.getKi()), decisionFactor);
		}else{
			//if this vertex does not happen to be in a community from the neighbor.
			max = calculateLabel(BigDecimal.ONE, BigDecimal.ZERO, decisionFactor);
		}
		
//		LOG.info("Vertex{"+vertex.getId()+"} own community max = " + max );
		
		long newLabel = vertexVal.getLabel();
		
		for(Entry<Long, NeighboorLabelValues> adjacentLabelEntry : adjacentLabelsEntries.entrySet()){
			
			long labeli = adjacentLabelEntry.getKey();
			NeighboorLabelValues labelVal  = adjacentLabelEntry.getValue();
			
			BigDecimal calc = calculateLabel(
					new BigDecimal(labelVal.getVi()),
					new BigDecimal(labelVal.getKi()), decisionFactor);
			
//			LOG.info("Vertex{"+vertex.getId()+"} calculated for label " + labeli +
//					" the value " + calc+"(ki="+labelVal.getKi()+";vi="+labelVal.getVi()+")");
			
			if(calc.compareTo(max) > 0 || 
					calc.compareTo(max) == 0 && labeli < newLabel){
				max = calc;
				newLabel = labeli;
			}
		}

		boolean changed = false;
		if(newLabel != vertexVal.getOldLabel() && newLabel != vertexVal.getLabel()){
			vertexVal.changeLabel(newLabel);
			changed = true;
		}else if(newLabel == vertexVal.getOldLabel() && newLabel<vertexVal.getLabel()){
			//Cycle resolution
			vertexVal.setShouldStop(true);
		}
		
		/*
		 * Aggregate if the vertex has changed, so that the computation 
		 * can be stopped if needed in the next superstep.
		 */
		aggregateValue(GLOBAL_CHANGE_AGGREGATOR, new BooleanWritable(changed));

//		LOG.info("Vertex{"+vertex.getId()+"} has label/hub " + vertex.getVertexValue() +" (changed = "+changed+")");

		sendMessageToVertex(new LongWritable(vertex.getVertexValue().getLabel()),
				new LLPMessage(vertex.getId().get()));
	
		//only hubs need to be active in the next superstep.
		vertex.voteToHalt();
	}
	
	protected BigDecimal calculateLabel(BigDecimal vi, BigDecimal ki, BigDecimal decisionFactor){
		return ki.subtract(decisionFactor.multiply((vi.subtract(ki)))); // ki - gamma  (vi - ki)
	}
	
	private void updateCommunity(
			Vertex<LongWritable, ALLPVertexValue, NullWritable> vertex,
			Iterable<LLPMessage> messages) {
		
		//If not even one vertex changed in the previous superstep then we can stop the computation.
		if(!((BooleanWritable)getValueFromAggregator(GLOBAL_CHANGE_AGGREGATOR)).get()){
//			LOG.info("Vertex{"+vertex.getId()+"} will halt");
			vertex.voteToHalt();
			return;
		}

		long vi = 0;
		
		for(LLPMessage m : messages ){
			//aggregate the total number of vertices in this community. 
			++vi;
//			LOG.info("Vertex{"+vertex.getId()+"} received from " + m.getSourceVertex());
		}

		if(vi == 0){
			return;
		}

		LLPMessage toSend = new LLPMessage(vertex.getId().get(), vi);
		
//		LOG.info("Vertex{"+vertex.getId()+"} aggregated vi=" + vi);
		
		//send the new updated vi to the community members.
		for(LLPMessage message : messages){
			sendMessageToVertex(new LongWritable(message.getSourceVertex()),toSend);
//			LOG.info("Vertex{"+vertex.getId()+"} sent to " + "Vertex{"+message.getSourceVertex()+"} , new hub = " + newHub);
		}
		
	}


	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
		float fdecision = conf.getFloat(DECISION_FACTOR, DEFAULT_DECISION_FACTOR.floatValue());
		decisionFactor = new BigDecimal(Float.toString(fdecision));
		countVertexNg = conf.getBoolean(COUNT_VERTEX_AS_OWN_NEIGHBOR, COUNT_VERTEX_AS_LI_DEFAULT);
	}

	@Override
	public Configuration getConf() {
		return conf;
	}
	
}

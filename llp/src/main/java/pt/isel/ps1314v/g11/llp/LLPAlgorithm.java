package pt.isel.ps1314v.g11.llp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Vertex;

/**
 *Layered Label Propagation implementation 
 *
 */
public class LLPAlgorithm extends Algorithm<LongWritable, LongWritable, NullWritable, LLPMessage>{

	private final Logger LOG = Logger.getLogger(LLPAlgorithm.class);
	
	public static final String GLOBAL_CHANGE_AGGREGATOR = "pt.isel.ps1314v.g11.llp.LLPAlgorithm.GLOBAL_CHANGE_AGGREGATOR";
	
	private final BigDecimal DEFAULT_DECISION_FACTOR = BigDecimal.ONE;
	
	@Override
	public void compute(
			Vertex<LongWritable, LongWritable, NullWritable> vertex,
			Iterable<LLPMessage> messages) {
		
		if(getSuperstep() == 0){
			//Give each vertex an unique label.
			vertex.setVertexValue(new LongWritable(vertex.getId().get()));
		}
		
		int minorStep = (int)(getSuperstep()%4);
		
		switch(minorStep){
			case 0:
				LOG.info("On minorstep 0.");
				updateAndSendToNeighborhood(vertex, messages);
				break;
			case 1:
				LOG.info("On minorstep 1.");
				calculateLabelAndSendToHub(vertex, messages);
				break;
			case 2:
				LOG.info("On minorstep 2.");
				resolveCycles(vertex, messages);
				break;
			case 3:
				LOG.info("On minorstep 3");
				updateCommunity(vertex, messages);
				break;
		}
	}

	private void updateAndSendToNeighborhood(
			Vertex<LongWritable, LongWritable, NullWritable> vertex,
			Iterable<LLPMessage> messages) {
		
		//Update vi for this vertex label. 
		
		long vi = 1; //default vi is 1 for the first phase in superstep 0.
		
		//in superstep != 0 it should always receive the updated vi.
		Iterator<LLPMessage> itHubMessage = messages.iterator();
		if(itHubMessage.hasNext()){
			//if it has a message then it means the Hub of this vertex label has sent an updated vi value.
			LLPMessage message = itHubMessage.next();
			vi = message.getVi();
			
			vertex.getVertexValue().set(message.getSourceVertex());
			
			LOG.info("Vertex{"+vertex.getId()+"} change to " + vertex.getVertexValue());
		}
		
		//send this vertex current community and vi to the adjacent vertices.
		sendMessageToNeighbors(vertex, new LLPMessage(vertex.getId().get(), vi, vertex.getVertexValue().get()));
		
	}

	private void calculateLabelAndSendToHub(
			Vertex<LongWritable, LongWritable, NullWritable> vertex,
			Iterable<LLPMessage> messages) {

		//kis values for each label in the neighbor.
		HashMap<Long, NeighboorLabelValues> adjacentLabelsEntries = new HashMap<>(); 
		//adjacentLabelsEntries.
		for(LLPMessage message : messages){

			LOG.info("On vertex{"+vertex.getId()+"} and received  from neighbor label " + message.getLabeli());
			
			long labeli = message.getLabeli(); 
			NeighboorLabelValues val = adjacentLabelsEntries.get(labeli);
			if(val != null){
				val.setKi(val.getKi()+1); // Update the vertices in the adjacency with this label.
			}else{
				val = new NeighboorLabelValues(
						1, /*vertex.getVertexValue().get() == labeli ? message.getVi()-vertex.getNumEdges():*/
						message.getVi());
				
				//The added value has the most up to date vi value.
				adjacentLabelsEntries.put(labeli, val);
			}

			val.addSource(message.getSourceVertex());
		}
		
		//Calculate the maximal label in the adjacency.
		
		BigDecimal max;
		NeighboorLabelValues ownLabel = adjacentLabelsEntries.get(vertex.getVertexValue().get());
		if(ownLabel != null){
			max = calculateLabel(new BigDecimal(ownLabel.getVi()), new BigDecimal(ownLabel.getKi()), DEFAULT_DECISION_FACTOR);
		}else{
			max = calculateLabel(BigDecimal.ONE, BigDecimal.ZERO, DEFAULT_DECISION_FACTOR);
		}
		
		LOG.info("Vertex{"+vertex.getId()+"} own community max = " + max );
		
		boolean changed = false;
		for(Entry<Long, NeighboorLabelValues> adjacentLabelEntry : adjacentLabelsEntries.entrySet()){
			
			long labeli = adjacentLabelEntry.getKey();
			NeighboorLabelValues labelVal  = adjacentLabelEntry.getValue();
			
			BigDecimal calc = calculateLabel(
					new BigDecimal(labelVal.getVi()),
					new BigDecimal(labelVal.getKi()), DEFAULT_DECISION_FACTOR);
			
			LOG.info("Vertex{"+vertex.getId()+"} calculated for label " + labeli +
					" the value " + calc+"(ki="+labelVal.getKi()+";vi="+labelVal.getVi()+")");
			
			if(calc.compareTo(max) > 0 || 
					calc.compareTo(max) == 0 && labeli < vertex.getVertexValue().get()){
				LOG.info("Vertex{"+vertex.getId()+"} changed from " +vertex.getVertexValue().get() +
						" label to " + labeli);
				max = calc;
				vertex.getVertexValue().set(labeli);
				changed = true;
			}
		}
		
		aggregateValue(GLOBAL_CHANGE_AGGREGATOR, new BooleanWritable(changed));
		
		if(changed){
			//notify the vertices that cause this change, so they can look up for any cycle.
			NeighboorLabelValues joinedCommunity = adjacentLabelsEntries.get(vertex.getVertexValue().get());
			for(Long source : joinedCommunity.getSources()){
				sendMessageToVertex(new LongWritable(source),
						new LLPMessage(vertex.getId().get(),0,vertex.getVertexValue().get()));
				LOG.info("Vertex{"+vertex.getId()+"} sent to Vertex{"+source+"}");
			}
		}
	}
	
	protected BigDecimal calculateLabel(BigDecimal vi, BigDecimal ki, BigDecimal decisionFactor){
		return ki.subtract(decisionFactor.multiply((vi.subtract(ki)))); // ki - gamma  (vi - ki)
	}

	//All vertices will run here to look up for cycles.
	//A vertex receives messages from vertices that joined their community due to the vertex that is running here.
	private void resolveCycles(
			Vertex<LongWritable, LongWritable, NullWritable> vertex,
			Iterable<LLPMessage> messages) {
		
		
		//If not even one vertex changed in the previous superstep then we can stop the computation.
		if(!((BooleanWritable)getValueFromAggregator(GLOBAL_CHANGE_AGGREGATOR)).get()){
			vertex.voteToHalt();
			return;
		}
		
		//Looks for a cycle.
		for(LLPMessage message : messages){
			if(message.getSourceVertex() == vertex.getVertexValue().get() && vertex.getId().get() != message.getSourceVertex()){
				//if this happened, then it means there was a cycle.
				
				//in case of cycles choose the minor label.
				if(message.getLabeli() < vertex.getVertexValue().get()){
					vertex.getVertexValue().set(message.getLabeli());
				}
			}
		}

		LOG.info("Vertex{"+vertex.getId()+"} has label/hub " + vertex.getVertexValue());
		
		//Send a message to this community hub.
		sendMessageToVertex(new LongWritable(vertex.getVertexValue().get()),
				new LLPMessage(vertex.getId().get()));
		
		//Will halt, since we only need the communities hubs to run in the next superstep.
		vertex.voteToHalt();
		
	}
	
	private void updateCommunity(
			Vertex<LongWritable, LongWritable, NullWritable> vertex,
			Iterable<LLPMessage> messages) {
		
	//	boolean sendToSelf = false;
	//	long ignoreVi = Long.MIN_VALUE;
		
		long vi = 0;
		
		for(LLPMessage message : messages){
			++vi;
			LOG.info("Vertex{"+vertex.getId()+"} received from " + message.getSourceVertex());

			//if(message.getSourceVertex() == vertex.getVertexValue().get() && vertex.getId().get() != message.getSourceVertex()){
				
			//if(message.getLabeli() < vertex.getVertexValue().get()){
			//	vertex.getVertexValue().set(value);
			//}
				
			//if(message.getSourceVertex()<vertex.getId().get()){
			//	//ignore count and won't send to this vertex and updated vi of this community.
			//	ignoreVi = message.getSourceVertex();
			//	LOG.info("Ignored Vertex{"+ignoreVi+"}");
			//	continue;
			//}else{	
				/*
				 * Should send this community information to itself
				 * so it can change its community.
				 */
			//	sendToSelf = true;
			//	++vi;
			//}
			//}
		}
			
		//Send updated info to the members of this community.
		//if(sendToSelf){
		//	sendMessageToVertex(vertex.getId(), 
		//			new LLPMessage(vertex.getId().get(),vi));
		//	LOG.info("Vertex{"+vertex.getId()+"} sent to " + "Vertex{"+vertex.getId()+"} via sentToSelf");
		//}

		
		for(LLPMessage message : messages){
			//if(ignoreVi != message.getSourceVertex()){
			sendMessageToVertex(new LongWritable(message.getSourceVertex()),
					new LLPMessage(vertex.getId().get(),vi));
			LOG.info("Vertex{"+vertex.getId()+"} sent to " + "Vertex{"+message.getSourceVertex()+"}");
			//}
		}
		
	}
	
}

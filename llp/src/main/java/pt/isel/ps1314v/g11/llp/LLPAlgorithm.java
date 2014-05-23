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
			LOG.info("Vertex{"+vertex.getId()+"} set label "+vertex.getId());
		}
		
		int minorStep = (int)(getSuperstep()%3);
		
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
				LOG.info("On minorstep 2");
				updateCommunity(vertex, messages);
				break;
		}
	}

	private void updateAndSendToNeighborhood(
			Vertex<LongWritable, LongWritable, NullWritable> vertex,
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
				
				vertex.getVertexValue().set(message.getSourceVertex());
				
				LOG.info("Vertex{"+vertex.getId()+"} change to " + vertex.getVertexValue());
				if(itHubMessage.hasNext())
					throw new IllegalStateException("Vertex{"+vertex.getId()+"} has more than one message.");
			}else{
				throw new IllegalStateException("Vertex{"+vertex.getId()+"} has no messages.");
			}
		}
		

		LOG.info("On vertex{"+vertex.getId()+"} and sending to neighbor label " + vertex.getVertexValue().get());
		//send this vertex current community and vi to the adjacent vertices.
		sendMessageToNeighbors(vertex, 
				new LLPMessage(vertex.getId().get(),
						vi,
						vertex.getVertexValue().get()));
		
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
						vertex.getVertexValue().get() == labeli? 2 : 1, /*vertex.getVertexValue().get() == labeli ? message.getVi()-vertex.getNumEdges():*/
						message.getVi());
				
				//The added value has the most up to date vi value.
				adjacentLabelsEntries.put(labeli, val);
			}
		}
		
		//Calculate the maximal label in the adjacency.
		
		BigDecimal max;
		NeighboorLabelValues ownLabel = adjacentLabelsEntries.get(vertex.getVertexValue().get());
		if(ownLabel != null){
			//calculate for this vertex community.
			max = calculateLabel(new BigDecimal(ownLabel.getVi()), new BigDecimal(ownLabel.getKi()), DEFAULT_DECISION_FACTOR);
		}else{
			//if this vertex does not happen to be in a community from the neighbor.
			max = calculateLabel(BigDecimal.ONE, BigDecimal.ZERO, DEFAULT_DECISION_FACTOR);
		}
		
		LOG.info("Vertex{"+vertex.getId()+"} own community max = " + max );
		
		boolean changed = false;

		long myLabel = vertex.getVertexValue().get();
		long newLabel = myLabel;
		
		int cycleStep = (int) ((getSuperstep()/3)%2);
		
		for(Entry<Long, NeighboorLabelValues> adjacentLabelEntry : adjacentLabelsEntries.entrySet()){
			
			long labeli = adjacentLabelEntry.getKey();
			NeighboorLabelValues labelVal  = adjacentLabelEntry.getValue();
			
			BigDecimal calc = calculateLabel(
					new BigDecimal(labelVal.getVi()),
					new BigDecimal(labelVal.getKi()), DEFAULT_DECISION_FACTOR);
			
			LOG.info("Vertex{"+vertex.getId()+"} calculated for label " + labeli +
					" the value " + calc+"(ki="+labelVal.getKi()+";vi="+labelVal.getVi()+")");
			
			if(calc.compareTo(max) > 0 || 
					calc.compareTo(max) == 0 && labeli < newLabel){
				max = calc;
				newLabel = labeli;
			}
		}
		
		if(cycleStep == 0 && newLabel < myLabel){
			newLabel = myLabel;
		}
		
		if(newLabel != myLabel){
			vertex.getVertexValue().set(newLabel);
			changed = true;
		}
		
		//will resolve cycles from happening
		/*if(cycleStep == 0 && vertex.getVertexValue().get()>newLabel ||
				cycleStep != 0 && vertex.getVertexValue().get()<newLabel){
			LOG.info("Vertex{"+vertex.getId()+"} changed label from " +vertex.getVertexValue().get() +
					" to " + newLabel);
			vertex.getVertexValue().set(newLabel);
			changed = true;
			
		}*/
		
		/*
		 * Aggregate if the vertex has changed, so that the computation 
		 * can be stopped if needed in the next superstep.
		 */
		aggregateValue(GLOBAL_CHANGE_AGGREGATOR, new BooleanWritable(changed));

		LOG.info("Vertex{"+vertex.getId()+"} has label/hub " + vertex.getVertexValue());
		LOG.info("Vertex{"+vertex.getId()+"} changed="+changed);

		sendMessageToVertex(vertex.getVertexValue(),
				new LLPMessage(vertex.getId().get()));
	
		//only hubs need to be active in the next superstep.
		vertex.voteToHalt();
	}
	
	protected BigDecimal calculateLabel(BigDecimal vi, BigDecimal ki, BigDecimal decisionFactor){
		return ki.subtract(decisionFactor.multiply((vi.subtract(ki)))); // ki - gamma  (vi - ki)
	}
	
	private void updateCommunity(
			Vertex<LongWritable, LongWritable, NullWritable> vertex,
			Iterable<LLPMessage> messages) {
		
		//If not even one vertex changed in the previous superstep then we can stop the computation.
		if(!((BooleanWritable)getValueFromAggregator(GLOBAL_CHANGE_AGGREGATOR)).get()){
			LOG.info("Vertex{"+vertex.getId()+"} will halt");
			vertex.voteToHalt();
			return;
		}

		boolean sendToSelf = false;
		boolean belongs = false;
		
		long myId = vertex.getId().get();
		long myLabel = vertex.getVertexValue().get();

		long vi = 0;
		long minHub = Long.MAX_VALUE;
		
		for(LLPMessage message : messages){
			//aggregate the total number of vertices in this community. 
			
			if(message.getSourceVertex() == vertex.getId().get()){
				belongs = true;
			}

			minHub = Math.min(minHub, message.getSourceVertex());
			
			if(message.getLabeli() == myLabel &&
					message.getSourceVertex() != myId){
				if(myId > message.getSourceVertex()){
					++vi;
					sendToSelf = true;
				}else{
					continue;
				}
			}

			++vi;
			
			LOG.info("Vertex{"+vertex.getId()+"} received from " + message.getSourceVertex());
		}

		if(vi == 0){
			return;
		}

		long newHub = myLabel;
		if(sendToSelf){
			newHub = myId;
		}else if(!belongs){
			newHub = minHub;
		}

		LLPMessage toSend = new LLPMessage(newHub, vi);
		
		LOG.info("Vertex{"+vertex.getId()+"} aggregated vi=" + vi);
		
		//send the new updated vi to the community members.
		for(LLPMessage message : messages){
			sendMessageToVertex(new LongWritable(message.getSourceVertex()),toSend);
			LOG.info("Vertex{"+vertex.getId()+"} sent to " + "Vertex{"+message.getSourceVertex()+"} , new hub = " + newHub);
		}
		
		if(sendToSelf){
			sendMessageToVertex(new LongWritable(myId), toSend);
		}
		
	}
	
}

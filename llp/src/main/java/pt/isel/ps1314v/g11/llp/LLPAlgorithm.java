package pt.isel.ps1314v.g11.llp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Vertex;

/**
 *Layered Label Propagation implementation 
 *
 */
public class LLPAlgorithm extends Algorithm<LongWritable, LongWritable, NullWritable, LLPMessage>{

	public static final String SHOULD_STOP_AGGREGATOR = "pt.isel.ps1314v.g11.llp.LLPAlgorithm.SHOULD_STOP_AGGREGATOR";
	private final BigDecimal DEFAULT_DECISION_FACTOR = BigDecimal.ONE;
	
	private final LLPMessage message = new LLPMessage();
	
	@Override
	public void compute(
			Vertex<LongWritable, LongWritable, NullWritable> vertex,
			Iterable<LLPMessage> messages) {
		
		if(getSuperstep() == 0){
			//Give each vertex an unique label.
			vertex.setVertexValue(vertex.getId());
			return;
		}
		
		int minorStep = (int)(getSuperstep()%3);
		
		switch(minorStep){
			case 0:
				updateAndSendToNeighborhood(vertex, messages);
				break;
			case 1:
				calculateLabelAndSendToHub(vertex, messages);
				break;
			case 2:
				updateCommunityInHub(vertex, messages);
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
		}
		
		//send this vertex current community and vi to the adjacent vertices.
		sendMessageToNeighbors(vertex, message.setValues(vertex.getId().get(), vi, vertex.getVertexValue().get()));
	}

	private void calculateLabelAndSendToHub(
			Vertex<LongWritable, LongWritable, NullWritable> vertex,
			Iterable<LLPMessage> messages) {

		//kis values for each label in the neighboor.
		HashMap<Long, NeighboorLabelValues> adjacentLabelsEntries = new HashMap<>(); 
		for(LLPMessage message : messages){
			long labeli = message.getLabeli(); 
			
			NeighboorLabelValues val = adjacentLabelsEntries.get(labeli);
			if(val != null){
				val.setKi(val.getKi()+1); // Update the vertices in the adjacency with this label.
			}else{
				//The added value has the most up to date vi value.
				adjacentLabelsEntries.put(labeli, new NeighboorLabelValues(1, message.getVi()));
			}
		}
		
		//Calculate the maximal label in the adjacency.
		BigDecimal max = new BigDecimal("-1.0"); //Any value is >=1.0.	
		boolean changed = false;
		for(Entry<Long, NeighboorLabelValues> adjacentLabelEntry : adjacentLabelsEntries.entrySet()){
			
			long labeli = adjacentLabelEntry.getKey();
			NeighboorLabelValues labelVal  = adjacentLabelEntry.getValue();
			
			BigDecimal calc = calculateLabel(new BigDecimal(labelVal.getVi()),
					new BigDecimal(labelVal.getKi()), DEFAULT_DECISION_FACTOR);
			
			if(calc.compareTo(max) > 0 || 
					calc.compareTo(max) == 0 && labeli < vertex.getVertexValue().get()){
				vertex.setVertexValue(new LongWritable(labeli));
				changed = true;
			}
		}
		aggregateValue(SHOULD_STOP_AGGREGATOR, new BooleanWritable(changed));
		
		//send to hub this vertex info.
		sendMessageToVertex(vertex.getVertexValue(),
				message.setValues(vertex.getId().get(),
						adjacentLabelsEntries.get(vertex.getVertexValue()).getVi()+1));
		
		vertex.voteToHalt();
	}
	
	protected BigDecimal calculateLabel(BigDecimal vi, BigDecimal ki, BigDecimal decisionFactor){
		return ki.subtract(decisionFactor.multiply((vi.subtract(ki)))); // ki - gamma  (vi - ki)
	}

	private void updateCommunityInHub(
			Vertex<LongWritable, LongWritable, NullWritable> vertex,
			Iterable<LLPMessage> messages) {
		
		if(((BooleanWritable)getValueFromAggregator(SHOULD_STOP_AGGREGATOR)).get()){
			vertex.voteToHalt();
		}
		
		long vi = 0;
		
		long myLabel = vertex.getVertexValue().get();
		
		boolean sendToSelf = false;
		
		for(LLPMessage message : messages){

			if(message.getSourceVertex() == myLabel){
				//Cycles only happen in hubs, so they should be resolved here. 
				if(message.getSourceVertex()<vertex.getId().get()){
					//ignore count
					continue;
				}else{
					/*
					 * Should send this community information to itself
					 * so it can change its community.
					 */
					sendToSelf = true;
				}
			}
			vi += message.getVi();

		}
		
		
		//Send updated info to the members of this community.
		if(sendToSelf)
			sendMessageToVertex(vertex.getId(), 
					message.setValues(vertex.getId().get(), vi));
		
		for(LLPMessage message : messages){
			sendMessageToVertex(new LongWritable(message.getSourceVertex()),
					message.setValues(vertex.getId().get(), vi));
		}
		
	}
	
}

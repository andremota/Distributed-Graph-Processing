package pt.isel.ps1314v.g11.louvain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class LouvainAlgorithm extends Algorithm<LongWritable, LouvainValue, IntWritable, LouvainMessage>{

	public static final String AGG_M2 = "pt.isel.ps1314v.g11.louvain.m2";
	
	private static int getDegree(Vertex<LongWritable, LouvainValue, IntWritable> vertex){
		int deg = 0;
		for(Edge<LongWritable, IntWritable> edges: vertex.getVertexEdges()){
			int val;
			IntWritable edgeValue = edges.getValue();
			
			if(edgeValue == null ||( val = (edgeValue.get()) ) <= 0)
				val = 1;

			deg += val;
		}
		return deg;
	}
	
	@Override
	public void compute(Vertex<LongWritable, LouvainValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages) {
		
		int phase = (int) (getSuperstep() % 3);
		
		//TODO Maybe do this in inputreader
		if(getSuperstep() == 0){
			vertex.setVertexValue(new LouvainValue(vertex.getId().get(),getDegree(vertex)));
			vertex.getVertexValue().setTot(vertex.getVertexValue().getDeg());
			aggregateValue(AGG_M2, new LongWritable(vertex.getVertexValue().getDeg()));
		}
		
		if(getSuperstep() == 7) //TODO for now
			vertex.voteToHalt();
		
		switch(phase){
		case 0:
			updateAndSendMessages(vertex,messages);
			break;
		case 1: //TODO stop verification
			if(getSuperstep() == 1){ //TODO The first phase 1 of pass 1 should always do this
				LongWritable m2 = getValueFromAggregator(AGG_M2);
				vertex.getVertexValue().setM2(m2.get());
			}
				
			calculateBestCommunity(vertex,messages);
			break;
		case 2:
			updateTotals(vertex,messages);
			break;
		}
	}
	
	private void updateAndSendMessages(
			Vertex<LongWritable, LouvainValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages) {
		
		LouvainValue value = vertex.getVertexValue();
		int tot = 0;
		
		if(getSuperstep()>0){
			Iterator<LouvainMessage> it = messages.iterator();
			
			if(it.hasNext()){
				LouvainMessage message = it.next();
				tot = message.getTot();
				value.setHub(message.getHub());
			}//TODO throw exception?
		} else {
			tot = value.getDeg();
		}
		
		sendMessageToNeighbors(vertex,
				new LouvainMessage(vertex.getId().get(),tot,value.getDeg(),value.getHub()));
		
	}
	
	private class CommHolder{
		public int kiin;
		public int tot;
		
		public CommHolder(int f, int s){
			kiin = f;
			tot = s;
		}
	}
	private void calculateBestCommunity(
			Vertex<LongWritable, LouvainValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages) {
		
		
		Map<Long,CommHolder> comms = new HashMap<>();
		
		for(LouvainMessage message: messages){
			long hub = message.getHub();
			int deg = message.getDeg();
			CommHolder comm;
			
			if((comm = comms.get(hub)) != null){
				comm.kiin += deg;
			} else {
				comms.put(hub, new CommHolder(deg, message.getTot()));
			}
		}
		
		LouvainValue value = vertex.getVertexValue();
		long m2 = value.getM2();
		
		BigDecimal maxQ = new BigDecimal("0.0");
		long bestCommunity = value.getHub();
		for(Map.Entry<Long, CommHolder> entry: comms.entrySet()){
			
			BigDecimal q = q(entry.getValue().kiin, entry.getValue().tot , value.getDeg(),m2);
			if(q.compareTo(maxQ) > 0 || q.compareTo(maxQ) == 0 && entry.getKey() < bestCommunity){
				maxQ = q;
				bestCommunity = entry.getKey();
			}
		}
		
		
		if(bestCommunity != value.getHub()){
			value.setHub(bestCommunity);
			//TODO registrar globalmente
		}
		
		sendMessageToVertex(new LongWritable(bestCommunity), 
				new LouvainMessage(vertex.getId().get(),value.getDeg()));
		
		vertex.voteToHalt();
	}
	
	
	private BigDecimal q(int ki_in, int tot, int k_i, long m) {
		BigDecimal kiin = new BigDecimal(ki_in);
		BigDecimal sum_tot = new BigDecimal(tot);
		BigDecimal ki = new BigDecimal(k_i);
		BigDecimal m2 = new BigDecimal(m);
		
		//Q  =  Kiin - sum_tot * K_i / 2m
		return kiin.subtract(sum_tot.multiply(ki).divide(m2));
	}
	
	private void updateTotals(
			Vertex<LongWritable, LouvainValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages) {
		
		int tot = 0;
		long myHub = vertex.getVertexValue().getHub();
		
		long hub = vertex.getId().get();
		long myId = hub;
		
		boolean sendToSelf = false;
		
		for(LouvainMessage message: messages){
			//TODO maybe check if I'm a member of this community and choose a member of the community
			//as their hub instead of me.
			
			long id = message.getVertexId();
			if(id == myHub){ 
				// In this case two hubs decided to switch with each other 
				// This is a cycle and must be avoided
				if(id < myId) 
					// The hub with the greater id will ignore the one with the lowest
					continue;
				else { 
					// And the one with the lowest MUST add itself to the community list
					// and send a message to itself so it can get the updated values.
					sendToSelf = true;
					tot += vertex.getVertexValue().getDeg();
				}
					
			}
			if(!(id == myHub && id < myId))
				tot += message.getDeg();
		}
		
		
		for(LouvainMessage message: messages){
			sendMessageToVertex(new LongWritable(message.getVertexId()),
					new LouvainMessage(tot,hub));
		}
		
		if(sendToSelf){
			sendMessageToVertex(vertex.getId(),
					new LouvainMessage(tot, hub));
		}
		
	}
	
	

}

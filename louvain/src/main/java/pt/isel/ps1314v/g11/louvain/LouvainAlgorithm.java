package pt.isel.ps1314v.g11.louvain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

import com.sun.istack.logging.Logger;

public class LouvainAlgorithm extends Algorithm<LongWritable, LouvainVertexValue, IntWritable, LouvainMessage>{

	
	public static final String AGG_M2 = "pt.isel.ps1314v.g11.louvain.m2";
	public static final String CHANGE_AGG = "pt.isel.ps1314v.g11.louvain.change";
	
	
	private static final int FIRST_PASS = 0;
	private static final int SECOND_PASS = 1;
	
	private static final int SCALE = 20;
	
	private static final Logger LOG = Logger.getLogger(LouvainAlgorithm.class);


	
	
	private static int getDegree(Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex){
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
	public void compute(Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages) {		


		
		int phase = (int) (getSuperstep() % 3);
		long iteration = getSuperstep()/3;
		
		//TODO Maybe do this in inputreader
		if(getSuperstep() == 0){
			vertex.setVertexValue(new LouvainVertexValue(vertex.getId().get(),getDegree(vertex)));
			int deg = vertex.getVertexValue().getDeg();
				if(deg==0){
				//Vertexes with no edges always belong to their own community.
				vertex.voteToHalt();
				return;

			}
			vertex.getVertexValue().setTot(deg);
			vertex.getVertexValue().setPass(FIRST_PASS);
			aggregateValue(AGG_M2, new LongWritable(deg));
		}
		
		if(getSuperstep() == 1){
			LongWritable m2 = getValueFromAggregator(AGG_M2);
			vertex.getVertexValue().setM2(m2.get());
		}	
	
		
		LouvainVertexValue value = vertex.getVertexValue();
		//LOG.info("VERTEX "+vertex.getId()+" IS ON SUPERSTEP "+getSuperstep()+" AND PASS "+value.getPass());
		if(/*iteration%2!=0&&*/phase == 1){
			if(value.getIterationsPerPass() > 0){
				BooleanWritable bw = getValueFromAggregator(CHANGE_AGG);
				boolean globalChange = bw.get();
				
				//LOG.info("VERTEX "+vertex.getId()+" SEES GLOBALCHANGE AS "+globalChange);
				if(!globalChange){
					if(value.getIterationsPerPass() == 1){
						vertex.voteToHalt();
						return;
					}
					else{
						value.incPass();
						value.setIterationsPerPass(0);
					}
				}
			}
		}
		
		switch(phase){
		case 0:
			updateAndSendMessages(vertex,messages);
			
			if(value.getIterationsPerPass() > 0){ //First time in the first pass. No change to aggregate
				aggregateValue(CHANGE_AGG, new BooleanWritable(vertex.getVertexValue().hasChanged()));
				value.setChanged(false);
			}
				
			break;
		case 1:
			if(value.getPass()%2==FIRST_PASS){
				calculateBestCommunity(vertex,messages,iteration);
				value.incIterationsPerPass();
			}
			else{
				aggregateEdges(vertex,messages);
				vertex.voteToHalt();
			}
			break;
		case 2:
			if(value.getPass()%2==FIRST_PASS){
				if(iteration%2==0)
					updateTotals(vertex,messages);
				else
					updateTotalsWithCycleDetection(vertex,messages);
				
			}	
			else{
				finalizeAggregations(vertex,messages);
				value.setIterationsPerPass(0);
				value.incPass();
			}
				
			
			break;
		}
	}



	private void updateAndSendMessages(
			Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages) {
		
		LouvainVertexValue value = vertex.getVertexValue();
		int tot = 0;
		
		if(getSuperstep()>0){
			Iterator<LouvainMessage> it = messages.iterator();
			
			if(it.hasNext()){
				LouvainMessage message = it.next();
				tot = message.getTot();
				value.setTot(tot);
				value.setHub(message.getHub());
				//LOG.info("VERTEX "+vertex.getId()+" RECEIVED MESSAGE FROM "+value.getHub());
			} else {
				throw new IllegalStateException("Vertex "+vertex.getId() + " did not receive community"
						+ " info from it's hub");
			}
			
			if(it.hasNext())
				throw new IllegalStateException("Vertex "+vertex.getId() + " received too many messages."
						+ "This could mean that a cycle wasn't resolved");
		} else {
			tot = value.getTot();
		}
		
		long myId = vertex.getId().get();
		long hub = value.getHub();
		
		for(Edge<LongWritable,IntWritable> edge: vertex.getVertexEdges()){
			sendMessageToVertex(edge.getTargetVertexId()
					, new LouvainMessage(
							myId,
							tot,
							edge.getValue().get(),
							hub));
		}
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
			Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages, long iteration) {
		
		
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
		
		LouvainVertexValue value = vertex.getVertexValue();
		
		long m2 = value.getM2();
		BigDecimal maxQ = new BigDecimal("0.0");
		long bestCommunity = value.getHub();
		long myCommunity = bestCommunity;
		int ki = value.getDeg();
		
		for(Map.Entry<Long, CommHolder> entry: comms.entrySet()){
			
			long otherCommunity = entry.getKey();
			CommHolder commInfo = entry.getValue();
			BigDecimal q;
			
			int tot = myCommunity==otherCommunity?commInfo.tot-ki:commInfo.tot;
			
			if(myCommunity==otherCommunity && tot == 0)
				q = new BigDecimal("0.0");
			else
				q = q(commInfo.kiin,tot,ki,m2);
					
			if(q.compareTo(maxQ) > 0 || q.compareTo(maxQ) == 0 && otherCommunity < bestCommunity){
				maxQ = q;
				bestCommunity = otherCommunity;
			}
		}
		
		if(iteration%2 == 0 && bestCommunity<myCommunity){
			bestCommunity = myCommunity;
		}
			
		if(bestCommunity != value.getHub()){
			//LOG.info(" VERTEX "+vertex.getId()+" CHANGING FROM "+myCommunity +" TO "+bestCommunity);
			value.setHub(bestCommunity);
			value.setChanged(true);
		}
		
		sendMessageToVertex(
				new LongWritable(value.getHub()), 
				new LouvainMessage(vertex.getId().get(),value.getDeg()));
		
		vertex.voteToHalt();
	}
	
	
	private BigDecimal q(int ki_in, int tot, int k_i, long m) {
		BigDecimal kiin = new BigDecimal(ki_in);
		BigDecimal sum_tot = new BigDecimal(tot);
		BigDecimal ki = new BigDecimal(k_i);
		BigDecimal m2 = new BigDecimal(m);
		
		//Q  =  Kiin - sum_tot * K_i / 2m
		return kiin.subtract(sum_tot.multiply(ki).divide(m2,SCALE,RoundingMode.HALF_DOWN));
	}
	
	private void updateTotals(
			Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages){
		
		long hub = vertex.getId().get();
		int tot = 0;
		long otherHub = Long.MAX_VALUE;
		boolean belongs = false;
		long myId = vertex.getId().get();
		
		for(LouvainMessage message: messages){
			
			long messageId = message.getVertexId();
			LOG.info(" VERTEX "+vertex.getId()+" RECEIVED MESSAGE FROM "+messageId);
			tot+=message.getDeg();
			if(messageId==myId)
				belongs = true;
			otherHub = Math.min(otherHub, messageId);
		}
		
		if(!belongs)
			hub = otherHub;
		
		//LouvainMessage messageToSend = 
		for(LouvainMessage message: messages){
			LOG.info("VERTEX "+vertex.getId()+" SENDING MESSAGE TO "+message.getVertexId());
			sendMessageToVertex(
					new LongWritable(message.getVertexId()),
					new LouvainMessage(tot,hub));
		}
	}
	
	private void updateTotalsWithCycleDetection(
			Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages) {
		
		long hub = vertex.getVertexValue().getHub();
		int tot = 0;
		long otherHub = Long.MAX_VALUE;
		long myId = vertex.getId().get();
		
		for(LouvainMessage message: messages){
			
			long messageId = message.getVertexId();
			if(messageId==hub && myId!=messageId){
				
				if(myId > messageId){
					tot+=vertex.getVertexValue().getDeg();
					otherHub = myId;
				}
				else continue;
			}
			
			tot+=message.getDeg();
		}

		if(tot==0)
			return;
		
		if(otherHub!=Long.MAX_VALUE)
			hub = otherHub;
		
		LouvainMessage messageToSend = new LouvainMessage(tot,hub);
		
		for(LouvainMessage message: messages){
			sendMessageToVertex(
					new LongWritable(message.getVertexId()),
					messageToSend);
		}
		
		if(otherHub!=Long.MAX_VALUE){
			sendMessageToVertex(
					vertex.getId(), 
					messageToSend);
		}
	}
	
	
	private void aggregateEdges(
			Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex, Iterable<LouvainMessage> messages) {
		Map<Long,Integer> comms = new HashMap<>();
		
		for(LouvainMessage message: messages){
			putSum(comms, message.getHub(), message.getDeg());
		}
		
		vertex.setEdges(
				Collections.unmodifiableList(new ArrayList<Edge<LongWritable,IntWritable>>()));
		
		LOG.info("VERTEX "+vertex.getId()+" WILL HALT AND HAS HUB "+vertex.getVertexValue().getHub());
		sendMessageToVertex(
				new LongWritable(vertex.getVertexValue().getHub()), 
				new LouvainMessage(vertex.getId().get(),comms));
		
		if(vertex.getId().get()!=vertex.getVertexValue().getHub())
			vertex.voteToHalt();
	}
	
	private void finalizeAggregations(
			Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages) {
		Map<Long,Integer> comms = new HashMap<>();
		
		//LOG.info("VERTEX "+vertex.getId()+" IS A HUB AND HAS HUB "+vertex.getVertexValue().getHub());
		for(LouvainMessage message: messages){
			//LOG.info("IN FINALIZATION VERTEX "+vertex.getId()+" RECEIVED MESSAGE FROM "+message.getVertexId());
			for(Map.Entry<Long, Integer> entry: message.getCommunities().entrySet()){
				putSum(comms, entry.getKey(), entry.getValue());
			}
		}
		
		List<Edge<LongWritable,IntWritable>> edges = new ArrayList<>();
		for(Map.Entry<Long, Integer> entry: comms.entrySet()){
	
			edges.add(new Edge<LongWritable, IntWritable>(
						new LongWritable(entry.getKey()),
						new IntWritable(entry.getValue())
						)
					);
		}
		
		vertex.setEdges(edges);
		vertex.getVertexValue().setDeg(getDegree(vertex));

		sendMessageToVertex(vertex.getId(), 
				new LouvainMessage(vertex.getVertexValue().getTot(), vertex.getVertexValue().getHub()));
	}
	
	private void putSum(Map<Long,Integer> map, Long key, Integer value){
		Integer onMap = map.get(key);
		
		if(onMap != null)
			value+=onMap;
		
		map.put(key, value);
	}


	
	

}

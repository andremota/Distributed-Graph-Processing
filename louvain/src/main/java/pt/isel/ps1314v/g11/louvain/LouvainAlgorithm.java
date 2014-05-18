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
		
		if(vertex.getId().get()==0)
			LOG.info(" A WILD GHOST HAS APPEARED! IN SUPERSTEP "+vertex.getId());
		//LOG.info(" I AM VERTEX "+vertex.getId() + " AND THERE ARE "+getTotalVertices()+" IN SUPERSTEP "+getSuperstep());
		/*if(vertex.getVertexValue().getPass()==2){
			LOG.info("VERTEX "+vertex.getId() + " IS IN THE THIRD PASS AND THE NUMBER OF EDGES IS "
					+vertex.getNumEdges());
		}*/
		int phase = (int) (getSuperstep() % 3);
		
		//TODO Maybe do this in inputreader
		if(getSuperstep() == 0){
			vertex.setVertexValue(new LouvainVertexValue(vertex.getId().get(),getDegree(vertex)));
			vertex.getVertexValue().setTot(vertex.getVertexValue().getDeg());
			vertex.getVertexValue().setPass(FIRST_PASS);
			aggregateValue(AGG_M2, new LongWritable(vertex.getVertexValue().getDeg()));
		}
		
		if(getSuperstep() == 1){
			LongWritable m2 = getValueFromAggregator(AGG_M2);
			vertex.getVertexValue().setM2(m2.get());
		}

		/*if(getSuperstep() == 16){
			vertlong iteration = getSuperstep()/3;ex.voteToHalt();
			return;
		}*/
		
		long iteration = getSuperstep()/3;
		LouvainVertexValue value = vertex.getVertexValue();
		if(iteration%2!=0&&phase == 1){
			if(value.getIterationsPerPass() > 0){
				BooleanWritable bw = getValueFromAggregator(CHANGE_AGG);
				boolean globalChange = bw.get();
				
				
				LOG.info("IN ITERATION " + value.getIterationsPerPass() 
						+ " THE GLOBAL CHANGE WAS " +globalChange
						+ " AND THE VERTEX "+vertex.getId()
						+ " WITH THE DEG "+value.getDeg()
						+ " HAD THE HUB "+value.getHub()
						+ " WHICH HAD THE TOT "+value.getTot());
				if(!globalChange){
					if(value.getIterationsPerPass() == 1){
						vertex.voteToHalt();
						return;
					}
					else{
						/*LOG.info("VERTEX "+vertex.getId() 
								+ " WITH THE HUB "+vertex.getVertexValue().getHub()
								+ " WILL CHANGE TO THE SECOND PASS");*/
						value.incPass();
						//value.setPass(SECOND_PASS);
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
				calculateBestCommunity(vertex,messages);
				//vertex.voteToHalt();
				value.incIterationsPerPass();
			}
			else
				aggregateEdges(vertex,messages);
			break;
		case 2:
			if(value.getPass()%2==FIRST_PASS){
				updateTotals(vertex, messages);
				/*if(iteration%2==0)
					updateTotals(vertex,messages);
				else
					updateTotalsWithCycleDetection(vertex,messages);*/
				//value.incIterationsPerPass();
				
			}	
			else{
				finalizeAggregations(vertex,messages);
				value.setIterationsPerPass(0);
				value.incPass();
				//value.setPass(FIRST_PASS);
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
		
		//LOG.info("VERTEX "+vertex.getId() + " NOW HAS THE HUB "+value.getHub());
		for(Edge<LongWritable,IntWritable> edge: vertex.getVertexEdges()){
			/*LOG.info("VERTEX "+vertex.getId() + " EDGE " +edge.getTargetVertexId() + " - " +edge.getValue() 
					+" TOT "+tot);*/
			sendMessageToVertex(edge.getTargetVertexId()
					, new LouvainMessage(vertex.getId().get(),tot,edge.getValue().get(),value.getHub()));
		}
		/*sendMessageToNeighbors(vertex,
				new LouvainMessage(vertex.getId().get(),tot,value.getDeg(),value.getHub()));*/
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
		
		LouvainVertexValue value = vertex.getVertexValue();
		long m2 = value.getM2();
		//LOG.info("VERTEX " + vertex.getId() + " KNOWS " +comms.size() + " COMMUNITIES");
		BigDecimal maxQ = new BigDecimal("0.0");
		long bestCommunity = value.getHub();
		final long myCommunity = bestCommunity;
		for(Map.Entry<Long, CommHolder> entry: comms.entrySet()){
			
			long otherCommunity = entry.getKey();
			int ki = value.getDeg();
			CommHolder commInfo = entry.getValue();
			BigDecimal q;
			int tot = myCommunity==otherCommunity?commInfo.tot-ki:commInfo.tot;
			if(myCommunity==otherCommunity && tot == 0)
				q = new BigDecimal("0.0");
			else
			q = q(
					commInfo.kiin, 
					tot,
					ki,
					m2);

				LOG.info("VERTEX "+vertex.getId() + " WITH KIIN "+commInfo.kiin
						+" WITH TOT "+tot
						+" WITH DEG "+ ki
						+" WITH M2 "+m2
						+" WITH THE HUB "+myCommunity
						+" CHANGING TO "+otherCommunity);
				LOG.info("VERTEX "+vertex.getId() + " DELTAQ " +q + " MAXQ "+maxQ 
					+ " FROM "+value.getHub() + " TO " + entry.getKey());
		

			
			if(q.compareTo(maxQ) > 0 || q.compareTo(maxQ) == 0 && otherCommunity < bestCommunity){
				maxQ = q;
				bestCommunity = otherCommunity;
			}
		}
		/*
		long iteration = getSuperstep() / 3;
		
		if(iteration%2==0&&myCommunity>bestCommunity || iteration%2!=0&&myCommunity<bestCommunity){
			LOG.info("VERTEX "+vertex.getId() + " IN " +value.getHub() 
					+" TRIED TO CHANGE TO "+bestCommunity);
			bestCommunity = myCommunity;
			value.setChanged(true);
		}
		*/
		long iteration = getSuperstep()/3;
		if(iteration%2 == 0 && bestCommunity<myCommunity /*|| iteration%2 !=0 && bestCommunity > myCommunity*/){
			LOG.info("IN ITERATION "+iteration+" VERTEX "+vertex.getId() + " IN " +value.getHub() 
					+" TRIED TO CHANGE TO "+bestCommunity);
			bestCommunity = myCommunity;
			//value.setChanged(true);

		}
			

		if(bestCommunity != value.getHub()){
			LOG.info("VERTEX "+vertex.getId() + " IN " +value.getHub() +" CHANGING TO "+bestCommunity);
			value.setHub(bestCommunity);
			value.setChanged(true);
		}
		

		CommHolder holder = comms.get(bestCommunity);
		sendMessageToVertex(new LongWritable(value.getHub()), 
				new LouvainMessage(vertex.getId().get(),value.getDeg(),0));
		
		vertex.voteToHalt();
	}
	
	
	private BigDecimal q(int ki_in, int tot, int k_i, long m) {
		BigDecimal kiin = new BigDecimal(ki_in);
		BigDecimal sum_tot = new BigDecimal(tot);
		BigDecimal ki = new BigDecimal(k_i);
		BigDecimal m2 = new BigDecimal(m);
		
		BigDecimal sumTotTimesKi = sum_tot.multiply(ki);
		//Q  =  Kiin - sum_tot * K_i / 2m
		return kiin.subtract(sumTotTimesKi.divide(m2,SCALE,RoundingMode.HALF_DOWN));
	}
	
	private void updateTotals(
			Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages){
		
		long hub = vertex.getId().get();
		int tot = 0;
		long otherHub = Long.MAX_VALUE;
		boolean belongs = false;
		for(LouvainMessage message: messages){
			tot+=message.getDeg();
			if(message.getVertexId()==vertex.getId().get())
				belongs = true;
			otherHub = Math.min(otherHub, message.getVertexId());
		}
		
		if(!belongs)
			hub = otherHub;
		for(LouvainMessage message: messages){
			sendMessageToVertex(new LongWritable(message.getVertexId()),
					new LouvainMessage(tot,hub));
		}
	}
	
	//TODO GHOSTBUSTING!!
	private void updateTotalsWithCycleDetection(
			Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages) {
		
		long hub = vertex.getVertexValue().getHub();
		int tot = 0;
		long otherHub = Long.MAX_VALUE;
		boolean belongs = false;
		long myId = vertex.getId().get();
		for(LouvainMessage message: messages){
			LOG.info("VERTEX "+myId + " RECEIVED MESSAGE FROM "+message.getVertexId()
					+" AND HAD THE HUB "+hub);
			if(message.getVertexId()==hub && myId!=message.getVertexId())
				if(myId < message.getVertexId())
					otherHub = message.getHub();
				else continue;
			tot+=message.getDeg();
			/*if(message.getVertexId()==vertex.getId().get())
				belongs = true;
			otherHub = Math.min(otherHub, message.getVertexId());*/
		}
		
		if(tot==0)
			return;
		LOG.info("AT VERTEX "+vertex.getId());
		if(otherHub!=Long.MAX_VALUE)
			hub = otherHub;
		
		for(LouvainMessage message: messages){
			sendMessageToVertex(new LongWritable(message.getVertexId()),
					new LouvainMessage(tot,hub));
		}
		
		if(otherHub!=Long.MAX_VALUE)
			sendMessageToVertex(
					vertex.getId(), 
					new LouvainMessage(tot,hub));
	}
	
//	private void updateTotals(
//			Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex,
//			Iterable<LouvainMessage> messages) {
//		
//		int tot = 0;
//		long myHub = vertex.getVertexValue().getHub();
//		
//		long hub = vertex.getId().get();
//		long myId = hub;
//		long idToIgnore = Long.MAX_VALUE;
//		int kiins = 0;
//		
//		boolean sendToSelf = false;
//		
//		for(LouvainMessage message: messages){
//			
//			//TODO maybe check if I'm a member of this community and choose a member of the community
//			//as their hub instead of me.
//			
//			long id = message.getVertexId();
//			/*LOG.info("VERTEX "+vertex.getId() + " WITH THE HUB " +myHub +
//					" RECEIVED MESSAGE FROM "+id);*/
//			if(id == myHub && id != myId){ 
//				// In this case two hubs decided to switch with each other 
//				// This is a cycle and must be avoided
//				LOG.info("VERTEX "+vertex.getId()+ " FOUND A CYCLE");
//				if(id < myId){
//					idToIgnore = id;
//					//tot -= message.getDeg();
//					// The hub with the greater id will ignore the one with the lowest
//					continue;
//				}
//					
//				else { 
//					// And the one with the lowest MUST add itself to the community list
//					// and send a message to itself so it can get the updated values.
//					LOG.info("VERTEX "+vertex.getId()+" WILL SEND TO SELF");
//					sendToSelf = true;
//					//tot++;
//					tot += vertex.getVertexValue().getDeg() /*+ message.getKinn()*/;
//					
//					//continue;
//				}
//					
//			}
//			//tot++;
//			//if(!(id == myHub && id < myId))
//				tot += message.getDeg();
//				//tot += message.getDeg() + message.getKinn();
//				//kiins += message.getKinn();
//		}
//		/*if(sendToSelf){
//			Map<Long,Integer> edges = new HashMap<>();
//			for(Edge<LongWritable, IntWritable> edge: vertex.getVertexEdges()){
//				edges.put(edge.getTargetVertexId().get(), edge.getValue().get());
//			}
//			int myKiin = 0;
//			for(LouvainMessage message: messages){
//				Integer kiin = edges.get(message.getVertexId());
//				if(kiin!=null)
//					myKiin+=kiin;
//			}
//			
//			tot += vertex.getVertexValue().getDeg();
//			kiins+=myKiin;
//			//tot = tot - kiins;
//		} //else*/
//		tot = tot -(kiins/2);
//		if(tot==0)
//			return;
//		//tot/=2;
//		LOG.info("VERTEX "+vertex.getId() + " IS SOLVING CONFLICTS AND THE NEW TOT IS "+tot +" AND KIINS WAS "+kiins 
//				+" ORIGINAL TOT WAS " + (tot+(kiins/2)));
//		for(LouvainMessage message: messages){
//			if(/*idToIgnore==Long.MAX_VALUE ||idToIgnore!=Long.MAX_VALUE &&*/ message.getVertexId() != idToIgnore)
//				sendMessageToVertex(new LongWritable(message.getVertexId()),
//						new LouvainMessage(tot,hub));
//		}
//		
//		if(sendToSelf){
//			sendMessageToVertex(vertex.getId(),
//					new LouvainMessage(tot, hub));
//		}
//		
//	}
	
	private void aggregateEdges(
			Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex, Iterable<LouvainMessage> messages) {
		Map<Long,Integer> comms = new HashMap<>();
		
		for(LouvainMessage message: messages){
			//if(vertex.getVertexValue().getHub()==4)
			LOG.info("VERTEX "+vertex.getId() + " AGGREGATING EDGE " 
					+ message.getHub() + " - " +message.getDeg());
			putSum(comms, message.getHub(), message.getDeg());
			/*long hub = message.getHub();
			int deg = message.getDeg();
			Integer commDeg;
			if((commDeg=comms.get(hub))!=null){
				comms.put(hub, commDeg+deg);
			} else {
				comms.put(hub, deg);
			}*/
		}
		
		vertex.setEdges(
				Collections.unmodifiableList(new ArrayList<Edge<LongWritable,IntWritable>>()));
		
		//LOG.info("VERTEX "+vertex.getId() + " DONE AGGREGATING AND WILL SEND MESSAGE TO HUB "+vertex.getVertexValue().getHub());
		sendMessageToVertex(
				new LongWritable(vertex.getVertexValue().getHub()), 
				new LouvainMessage(comms));
		
		vertex.voteToHalt();
	}
	
	private void finalizeAggregations(
			Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex,
			Iterable<LouvainMessage> messages) {
		Map<Long,Integer> comms = new HashMap<>();
		
		for(LouvainMessage message: messages){
			for(Map.Entry<Long, Integer> entry: message.getCommunities().entrySet()){
				/*LOG.info("VERTEX "+vertex.getId() + " FINALIZE AGGREGATION EDGE "
						+ entry.getKey() + " - " +entry.getValue());*/
				putSum(comms, entry.getKey(), entry.getValue());
			}
		}
		
		List<Edge<LongWritable,IntWritable>> edges = new ArrayList<>();
		for(Map.Entry<Long, Integer> entry: comms.entrySet()){
			int value = /*entry.getKey()==vertex.getVertexValue().getHub()?entry.getValue()/2:*/entry.getValue();

			Edge<LongWritable, IntWritable> edge = new Edge<LongWritable, IntWritable>(
					new LongWritable(entry.getKey()),
					new IntWritable(value));
			LOG.info("VERTEX "+vertex.getId() + " NEW EDGE " + edge.getTargetVertexId() +" - "+edge.getValue());
			edges.add(edge);
		}
		
		vertex.setEdges(edges);
		vertex.getVertexValue().setDeg(getDegree(vertex));
		int tot = vertex.getVertexValue().getTot();
		//vertex.getVertexValue().setTot(vertex.getVertexValue().getDeg());
		LOG.info("VERTEX "+vertex.getId()
				+" FINISHED AGGREGATION AND IT'S NEW TOT IS "+vertex.getVertexValue().getTot()
				+" AND IT'S NEW DEG IS "+vertex.getVertexValue().getDeg());
		//LOG.info("VERTEX "+vertex.getId() + " SENDING MESSAGE TO SELF");
		sendMessageToVertex(vertex.getId(), 
				new LouvainMessage(vertex.getVertexValue().getTot(), vertex.getId().get()));
	}
	
	private void putSum(Map<Long,Integer> map, Long key, Integer value){
		Integer onMap = map.get(key);
		
		if(onMap != null)
			value+=onMap;
		
		map.put(key, value);
	}


	
	

}

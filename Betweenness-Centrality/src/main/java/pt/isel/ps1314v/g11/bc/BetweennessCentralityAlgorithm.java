package pt.isel.ps1314v.g11.bc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.bc.BetweennessVertexValue.Predecessors;
import pt.isel.ps1314v.g11.bc.BetweennessVertexValue.SymmetricTuple;
import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

import com.sun.istack.logging.Logger;

public class BetweennessCentralityAlgorithm 
	extends Algorithm<LongWritable, BetweennessVertexValue, IntWritable, BetweennessMessage>
	implements Configurable {//TODO we should change this to setup instead

	public static final String START_VERTEXES = "pt.isel.ps1314v.g11.bc.startVertexes";
	public static final String AGG_ENDED = "pt.isel.ps1314v.g11.bc.agg/ended";
	public static final String AGG_SP_TOTAL = "pt.isel.ps1314v.g11.bc.agg/sp_total";
	public static final String AGG_BC_TOTAL = "pt.isel.ps1314v.g11.bc.agg/bc_total";
	
	private Configuration conf;
	
	
	private static final Logger LOG = Logger.getLogger(BetweennessCentralityAlgorithm.class);




	
	private static class Tuple{
		
		public long start;
		public Predecessors preds;
		
		public Tuple(long start, Predecessors pred) {
			this.start = start;
			this.preds = pred;
		}
	}
	
	
	@Override
	public Configuration getConf() {
		return conf;
	}
	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
	}
	
	private boolean isStart(Vertex<LongWritable, BetweennessVertexValue, IntWritable> vertex){
		String[] v = getConf().getStrings(START_VERTEXES);
		Arrays.sort(v);
		return Arrays.binarySearch(v, Long.toString(vertex.getId().get()))>=0;
	}
	
	private int getNumberOfStartVertexes(){
		return getConf().getStrings(START_VERTEXES).length;
	}
	
	@Override
	public void compute(
			Vertex<LongWritable, BetweennessVertexValue, IntWritable> vertex,
			Iterable<BetweennessMessage> messages) {
		if(getSuperstep()==0){
			vertex.setVertexValue(new BetweennessVertexValue());
			
			//Map<Long,Map<Long,Integer>> mins = vertex.getVertexValue().getMinimums();
			
			if(isStart(vertex)){
				
				long start = vertex.getId().get();
				LOG.info("Vertex "+start+" is a start vertex");
				sendMessageToNeighbors(vertex, new BetweennessMessage(
						start,
						start,
						0));
			}
			/*for(Edge<LongWritable,IntWritable> e : vertex.getVertexEdges()){
				mins.put(e.getTargetVertexId().get(), new HashMap<Long, Integer>());
				sendMessageToVertex(
						e.getTargetVertexId(),
						);
			}*/
			
			return;
		}
		
		BetweennessVertexValue value = vertex.getVertexValue();
		
		BooleanWritable ended = getValueFromAggregator(AGG_ENDED);
		if(getSuperstep() > 1){
			if(ended.get()){
				LongWritable sp_total = getValueFromAggregator(AGG_SP_TOTAL);
				long total = sp_total.get();
				if(total == 0){
					value.setFinalBC(((DoubleWritable)getValueFromAggregator(AGG_BC_TOTAL)).get());	
					vertex.voteToHalt();
					return;
				}
				LOG.info("Vertex "+vertex.getId()+ " has "+value.getShortestPaths()+" SPs and sees total as "+total);
				double my_bc;
				if(total==0)
					my_bc = 0; 
				else
					my_bc = (double)value.getShortestPaths() / (double)sp_total.get();
				aggregateValue(AGG_ENDED, new BooleanWritable(true));
				aggregateValue(AGG_BC_TOTAL, new DoubleWritable(my_bc));
				LOG.info("The BC for vertex "+vertex.getId()+" is "+my_bc);
				return;
			}
		}
		
		boolean isStart = isStart(vertex); //TODO maybe save in vertex?
		
		List<Tuple> updateds = processMessages(vertex, messages, value, isStart);

		

		boolean sentProgress = false;
		long myId = vertex.getId().get();
		for(Tuple t: updateds){
			Set<Long> pred = t.preds.predecessors;
			int numberOfShortestPaths = pred.size();
			// We will send messages to our neighbors for every new shortest path
			for(Edge<LongWritable, IntWritable> edge: vertex.getVertexEdges()){
			
				long id = edge.getTargetVertexId().get();
				
				// Once again, we don't send back to the start
				if(id!=t.start){
					if(pred.contains(id)){
						// A neighbor being also a predecessor means
						// That it belongs to the shortest path between
						// start and this vertex
						
						sendMessageToVertex(
									edge.getTargetVertexId(),
									new BetweennessMessage(t.start,myId,numberOfShortestPaths,true));
						sentProgress =  true;
					} else {
						sentProgress = true;
						// Otherwise it will send a progress message.
						sendMessageToVertex(
								edge.getTargetVertexId(),
								new BetweennessMessage(t.start, myId,t.preds.cost+1));
					}
				}
			}
		}
		if(getSuperstep()==1||!ended.get()){
			int sz = isStart?getNumberOfStartVertexes()-1:getNumberOfStartVertexes();
			if(!sentProgress&&value.getMinimums().size() == sz){
				LOG.info("Vertex "+vertex.getId() + " will end and aggregate "+value.getShortestPaths());
				aggregateValue(AGG_ENDED, new BooleanWritable(true));
				aggregateValue(AGG_SP_TOTAL, new LongWritable(value.getShortestPaths()));
			} else {
				aggregateValue(AGG_ENDED, new BooleanWritable(false));
			}
		}
		
		
		//vertex.voteToHalt();
		
	}
	
	
	private List<Tuple> processMessages(Vertex<LongWritable, BetweennessVertexValue, IntWritable> vertex,
				Iterable<BetweennessMessage> messages, BetweennessVertexValue value, boolean isStart){
		List<Tuple> updateds = new ArrayList<>();
		Map<Long, Predecessors> mins = value.getMinimums();
		Set<SymmetricTuple> starts = value.getStarts();
		for(BetweennessMessage message: messages){
			if(!message.isShortestPathMessage()){
				LOG.info("Progress message to "+vertex.getId()
						+" from "+message.getFromVertex()
						+" start is "+message.getStartVertex()
						+" with the cost "+message.getCost()
						);
			} else {
				LOG.info("Shortest path message to "+vertex.getId()
						+" start is "+message.getStartVertex()
						+" from "+message.getFromVertex()
						);
			}
			
			
			long start = message.getStartVertex();
			long from = message.getFromVertex();
			int cost = message.getCost();
			if(message.isShortestPathMessage()){	
				
				/*	
				 * Shortest path messages serve only to tell the vertex that it belongs in a shortest path
				 * But this message must be sent back to my predecessors 
				 * so they know that they're part of a shortest path
				 */
				
				Predecessors preds = mins.get(start);
				
				SymmetricTuple st = new SymmetricTuple(start,from);
				
				// cost is used as the number of shortest paths.
				BetweennessMessage toSend = new BetweennessMessage(start,from, cost,true);
				if(!starts.contains(st)){
					//should replicate message
					
					//should verify if is symmetric or not!
					//if it's symmetric then ignore otherwise count it!!!
					
					//if someone receives 2 progress messages... it should send 2 shortest path messages
					
					value.incNShortestPaths();
					starts.add(st);
					aggregateValue(AGG_ENDED, new BooleanWritable(false));
					for(Long pred: preds.predecessors){
						/*if(vertex.getId().get() == 1){
							LOG.info("Vertex "+vertex.getId()
									+" replicating shortest path message from "
									+start
									+" to "+pred);
						}*/
						sendMessageToVertex(
								new LongWritable(pred),
								toSend);
					}
				}
				continue;
			}
			
			
			Predecessors preds = mins.get(start);	
			
			if(preds == null){
				// First time we received a message for a shortest path for this vertex
				preds = new Predecessors(message.getCost());
				// Add it to the shortest path maps
				mins.put(start, preds);
				// And add it to the list of new shortest paths
				updateds.add(new Tuple(start, preds));
			}
			

			// If it's the second time we receive a shortest path message 
			// for this vertex we know it isn't the shortest path
			// We also never send back to the start vertex 
			// because there the number of shortest paths between neighbours is always 0
			if(message.getCost() == preds.cost /*&& start!=from*/){
				//if(vertex.getId().get() == 1)
				/*LOG.info("Vertex "+vertex.getId()+ " will add the new predecessor "+from
						+" that started from "+start
						+" with the message cost "+message.getCost()
						+" and the preds cost "+preds.cost);*/
				if(start!=from)
					preds.predecessors.add(from);
			}
				
		}
		
		return updateds;
	}
}

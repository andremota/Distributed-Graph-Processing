package pt.isel.ps1314v.g11.bc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.bc.BetweennessVertexValue.Pair;
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
	
	private Configuration conf;
	
	
	private static final Logger LOG = Logger.getLogger(BetweennessCentralityAlgorithm.class);



	
	private static class Tuple{
		
		public long start;
		public Pair preds;
		
		public Tuple(long start, Pair pred) {
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
		if(ended.get()){
			LongWritable sp_total = getValueFromAggregator(AGG_SP_TOTAL);
			long total = sp_total.get();
			LOG.info("Vertex "+vertex.getId()+ " sees total as "+sp_total.get());
			double my_bc;
			if(total==0)
				my_bc = 0; 
			else
				my_bc = (double)value.getShortestPaths() / (double)sp_total.get();
			LOG.info("The BC for vertex "+vertex.getId()+" is "+my_bc);
			vertex.voteToHalt();
		}
		Map<Long, Pair> mins = value.getMinimums();
		
		List<Tuple> updateds = new ArrayList<>();
		
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
						);
			}
			
			
			long start = message.getStartVertex();
			if(message.isShortestPathMessage()){
				// Shortest path messages serve only to tell the vertex that it belongs in a shortest path
				value.incNShortestPaths();
				
				// But this message must be sent back to my predecessors 
				// so they know that they're part of a shortest path
				Pair preds = mins.get(start);
				BetweennessMessage toSend = new BetweennessMessage(start, true);
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
				continue;
			}
			
			
			Pair preds = mins.get(start);	
			
			if(preds == null){
				// First time we received a message for a shortest path for this vertex
				preds = new Pair(message.getCost());
				// Add it to the shortest path maps
				mins.put(start, preds);
				// And add it to the list of new shortest paths
				updateds.add(new Tuple(start, preds));
			}
			
			long from = message.getFromVertex();
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

		boolean sentProgress = false;
		long myId = vertex.getId().get();
		for(Tuple t: updateds){
			
			// We will send messages to our neighbours for every new shortest path
			for(Edge<LongWritable, IntWritable> edge: vertex.getVertexEdges()){
				Set<Long> pred = t.preds.predecessors;
				long id = edge.getTargetVertexId().get();
				
				// Once again, we don't send back to the start
				if(id!=t.start){
					if(pred.contains(id)){
						// A neighbour being also a predecessor means
						// That it belongs to the shortest path between
						// start and this vertex
						sendMessageToVertex(
								edge.getTargetVertexId(),
								new BetweennessMessage(t.start,true));
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
		int sz = isStart(vertex)?getNumberOfStartVertexes()-1:getNumberOfStartVertexes();
		if(!sentProgress&&mins.size() == sz){
			LOG.info("Vertex "+vertex.getId() + " will end and aggregate "+value.getShortestPaths());
			aggregateValue(AGG_ENDED, new BooleanWritable(true));
			aggregateValue(AGG_SP_TOTAL, new LongWritable(value.getShortestPaths()));
		} else {
			aggregateValue(AGG_ENDED, new BooleanWritable(false));
		}
		
		//vertex.voteToHalt();
		
	}
}

package pt.isel.ps1314v.g11.bc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		extends
		Algorithm<LongWritable, BetweennessVertexValue, IntWritable, BetweennessMessage>
		implements Configurable {// TODO we should change this to setup instead

	public static final String NORMALIZE = "pt.isel.ps1314v.g11.bc.normalize";
	public static final String START_VERTEXES = "pt.isel.ps1314v.g11.bc.startVertexes";
	public static final String AGG_ENDED = "pt.isel.ps1314v.g11.bc.agg/ended";
	
	public static final String AGG_MAX_BC = "pt.isel.ps1314v.g11.bc.agg/max_bc";
	public static final String AGG_MIN_BC = "pt.isel.ps1314v.g11.bc.agg/min_bc";

	private static final boolean DEFAULT_NORMALIZE = false;
	
	private Configuration conf;

	private static final Logger LOG = Logger
			.getLogger(BetweennessCentralityAlgorithm.class);

	private static class Tuple {

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

	private boolean isStart(
			Vertex<LongWritable, BetweennessVertexValue, IntWritable> vertex) {
		String[] v = getConf().getStrings(START_VERTEXES, new String[]{});
		if(v == null || v.length == 0)
			return true;
		Arrays.sort(v);
		return Arrays.binarySearch(v, Long.toString(vertex.getId().get())) >= 0;
	}
	
	private boolean normalize(){
		return conf.getBoolean(NORMALIZE, DEFAULT_NORMALIZE);
	}

	@Override
	public void compute(
			Vertex<LongWritable, BetweennessVertexValue, IntWritable> vertex,
			Iterable<BetweennessMessage> messages) {
		if (getSuperstep() == 0) {
			vertex.setVertexValue(new BetweennessVertexValue());

			if (isStart(vertex)) {

				long start = vertex.getId().get();
//				LOG.info("Vertex " + start + " is a start vertex");
				sendMessageToNeighbors(vertex, new BetweennessMessage(start,
						start, 0));
			}
			
			if(normalize()){
				aggregateValue(AGG_ENDED, new BooleanWritable(false));
			}
			
			vertex.voteToHalt();
			return;
		}

		BetweennessVertexValue value = vertex.getVertexValue();

		if(normalize()){
			BooleanWritable ended = getValueFromAggregator(AGG_ENDED);
			
			if (ended.get()) {
				//calculate normal betweenness centrality.
				double minBc = ((DoubleWritable)getValueFromAggregator(AGG_MIN_BC)).get();
				double maxBc = ((DoubleWritable)getValueFromAggregator(AGG_MAX_BC)).get();
				
				double myBc = value.getFinalBC();
				
//				System.out.println(minBc + " - "+ maxBc);
				if(maxBc == minBc){
					value.setFinalBC(1);
				}else{
					value.setFinalBC( (myBc- minBc) / (maxBc - minBc) );
				}
				
				vertex.voteToHalt();
				return;
			}
		}
		
		boolean isStart = isStart(vertex); // TODO maybe save in vertex?

		List<Tuple> updateds = processMessages(vertex, messages, value, isStart);

		boolean sentProgress = false;
		long myId = vertex.getId().get();
		for (Tuple t : updateds) {
			List<Long> pred = t.preds.predecessors;
			int numberOfShortestPaths = pred.size();
			// We will send messages to our neighbors for every new shortest
			// path
			
			if(getSuperstep() > 1){
				for(long l : pred){
					sentProgress = true;
					sendMessageToVertex(new LongWritable(l),
							new BetweennessMessage(t.start, myId,
									numberOfShortestPaths, true));
					for (Edge<LongWritable, IntWritable> edge : vertex.getVertexEdges()) {
						long id = edge.getTargetVertexId().get();

						// Once again, we don't send back to the start
						if (id != t.start && !pred.contains(id)) {
							// Otherwise it will send a progress message.
							sendMessageToVertex(edge.getTargetVertexId(),
									new BetweennessMessage(t.start, myId,
											t.preds.cost + 1));
						}
					}
				}
			}else{
				if(normalize()){
					aggregateValue(AGG_ENDED, new BooleanWritable(false));
				}
				
				for (Edge<LongWritable, IntWritable> edge : vertex.getVertexEdges()) {
					long id = edge.getTargetVertexId().get();
					// Once again, we don't send back to the start
					if (id != t.start) {
						// Otherwise it will send a progress message.
						sendMessageToVertex(edge.getTargetVertexId(),
								new BetweennessMessage(t.start, myId,
										t.preds.cost + 1));
					}
				}
			}
			
/*			
			for (Edge<LongWritable, IntWritable> edge : vertex.getVertexEdges()) {
				if (id != t.start && !pred.contains(id)) {
					//	sentProgress = true;
						//if(getSuperstep() == 1)
							sendMessageToVertex(edge.getTargetVertexId(),
									new BetweennessMessage(t.start, myId,
											t.preds.cost + 1));
						//else 
						//
							for(int i = 0; i<numberOfShortestPaths; ++i)
							sendMessageToVertex(edge.getTargetVertexId(),
									new BetweennessMessage(t.start, myId,
											t.preds.cost + 1));
					}
				}
			}
*/
		}
			
		if(normalize()){
			if(!sentProgress){
//				LOG.info("Vertex " + vertex.getId()
//						+ " will end and aggregate " + value.getShortestPaths());
				
				// Aggregate the values to calculate the normal betweenness centrality
				// in the next superstep.
				
				//aggregateValue(AGG_ENDED, new BooleanWritable(true));
				aggregateValue(AGG_MIN_BC,
						new DoubleWritable(value.getFinalBC()));
				aggregateValue(AGG_MAX_BC,
						new DoubleWritable(value.getFinalBC()));
			}else {
				aggregateValue(AGG_ENDED, new BooleanWritable(false));
			} 
		}else{
			vertex.voteToHalt();
		}
		
	}

	private List<Tuple> processMessages(
			Vertex<LongWritable, BetweennessVertexValue, IntWritable> vertex,
			Iterable<BetweennessMessage> messages,
			BetweennessVertexValue value, boolean isStart) {
		List<Tuple> updateds = new ArrayList<>();
		Map<Long, Predecessors> mins = value.getMinimums();
		Map<SymmetricTuple, SymmetricTuple> starts = value.getStarts();

		Map<SymmetricTuple, SymmetricTuple> pathsPerPair = new HashMap<>();

		for (BetweennessMessage message : messages) {
//			if (!message.isShortestPathMessage()) {
//				LOG.info("Progress message to " + vertex.getId() + " from "
//						+ message.getFromVertex() + " start is "
//						+ message.getStartVertex() + " with the cost "
//						+ message.getCost());
//			} else {
//				LOG.info("Shortest path message to " + vertex.getId()
//						+ " start is " + message.getStartVertex() + " from "
//						+ message.getFromVertex());
//			}

			long start = message.getStartVertex();
			long from = message.getFromVertex();
			int cost = message.getCost();
			if (message.isShortestPathMessage()) {

				/*
				 * Shortest path messages serve only to tell the vertex that it
				 * belongs in a shortest path But this message must be sent back
				 * to my predecessors so they know that they're part of a
				 * shortest path
				 */

				Predecessors preds = mins.get(start);

				SymmetricTuple st = new SymmetricTuple(start, from);

				// cost is used as the number of shortest paths.
				BetweennessMessage toSend = new BetweennessMessage(start, from,
						cost, true);

				SymmetricTuple foundTuple = starts.get(st);
				boolean isNewPath = false;

				if (foundTuple == null) {
					starts.put(st, st);
					isNewPath = true;
				} else {
					isNewPath = foundTuple.exactEquals(st);
				}

				if (isNewPath) {
					/*
					 * the SymmetricTuple is being used as: first = existent
					 * paths to the target vertex; second = paths that pass on
					 * this vertex
					 * 
					 * message cost is being used as the max shortest paths to
					 * target vertex.
					 */
					SymmetricTuple pathCount = pathsPerPair.get(st);
					pathsPerPair
							.put(st,
									new SymmetricTuple(
											pathCount != null ? pathCount.first
													: message.getCost(),
											(pathCount != null ? pathCount.second
													: 0) + 1));

					// counting the number of shortest paths that pass on a
					// vertex for debug only.
					value.incNShortestPaths();

					/*
					 * should verify if is symmetric or not!
					 * If it's symmetric then ignore otherwise count it!!!
					 */
					if(normalize()){
						aggregateValue(AGG_ENDED, new BooleanWritable(false));
					}
					
					for (Long pred : preds.predecessors) {
						/*
						 * if(vertex.getId().get() == 1){
						 * LOG.info("Vertex "+vertex.getId()
						 * +" replicating shortest path message from " +start
						 * +" to "+pred); }
						 */
						sendMessageToVertex(new LongWritable(pred), toSend);
					}
				}

				continue;
			}

			Predecessors preds = mins.get(start);

			if (preds == null) {
				// First time we received a message for a shortest path for this
				// vertex
				preds = new Predecessors(message.getCost());
				// Add it to the shortest path maps
				mins.put(start, preds);
				// And add it to the list of new shortest paths
				updateds.add(new Tuple(start, preds));
			}

			// If it's the second time we receive a shortest path message
			// for this vertex we know it isn't the shortest path
			// We also never send back to the start vertex
			// because there the number of shortest paths between neighbours is
			// always 0
			if (message.getCost() == preds.cost /* && start!=from */) {
				 if(vertex.getId().get() == 1)
				
//				  LOG.info("Vertex "+vertex.getId()+
//				  " will add the new predecessor "+from
//				 +" that started from "+start
//				 +" with the message cost "+message.getCost()
//				 +" and the preds cost "+preds.cost);
				 
				if (start != from)
					preds.predecessors.add(from);
			}

		}

		updateVertexBetweenness(vertex, pathsPerPair);

		return updateds;
	}

	private void updateVertexBetweenness(
			Vertex<LongWritable, BetweennessVertexValue, IntWritable> vertex,
			Map<SymmetricTuple, SymmetricTuple> pathsPerPair) {
		double toSum = 0;
		for (SymmetricTuple tuple : pathsPerPair.values()) {
//			if(vertex.getId().get()==1)
//				LOG.info("Vertex "+vertex.getId()+" in tupple ("+tuple.second+", "+tuple.first+")");
			toSum += (double) tuple.second / (double) tuple.first;
		}
		BetweennessVertexValue val = (BetweennessVertexValue) vertex
				.getVertexValue();
//
//		if(vertex.getId().get()==1)
//			LOG.info("Vertex "+vertex.getId()+" bc is " + toSum);
		
		val.setFinalBC(val.getFinalBC() + toSum);
	}
}

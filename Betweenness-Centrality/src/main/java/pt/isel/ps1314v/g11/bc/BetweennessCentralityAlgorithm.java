package pt.isel.ps1314v.g11.bc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.bc.BetweennessVertexValue.Pair;
import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class BetweennessCentralityAlgorithm extends Algorithm<LongWritable, BetweennessVertexValue, IntWritable, BetweennessMessage>{

	private static class Tuple{
		
		public long start;
		public Pair preds;
		
		public Tuple(long start, Pair pred) {
			this.start = start;
			this.preds = pred;
		}
	}
	@Override
	public void compute(
			Vertex<LongWritable, BetweennessVertexValue, IntWritable> vertex,
			Iterable<BetweennessMessage> messages) {
		if(getSuperstep()==0){
			vertex.setVertexValue(new BetweennessVertexValue());
			
			//Map<Long,Map<Long,Integer>> mins = vertex.getVertexValue().getMinimums();
			
			long start = vertex.getId().get();
			
			sendMessageToNeighbors(vertex, new BetweennessMessage(
					start,
					start));
			/*for(Edge<LongWritable,IntWritable> e : vertex.getVertexEdges()){
				mins.put(e.getTargetVertexId().get(), new HashMap<Long, Integer>());
				sendMessageToVertex(
						e.getTargetVertexId(),
						);
			}*/
			
			return;
		}
		
		BetweennessVertexValue value = vertex.getVertexValue();
		
		Map<Long, Pair> mins = value.getMinimums();
		List<Tuple> updateds = new ArrayList<>();
		Set<Long> toErase = new HashSet<>();
		
		for(BetweennessMessage message: messages){
			long start = message.getStartVertex();
			boolean secondTime = false;
			
			if(message.isShortestPathMessage()){
				// Shortest path messages serve only to tell the vertex that it belongs in a shortest path
				value.incNShortestPaths();
				
				// But this message must be sent back to my predecessors 
				// so they know that they're part of a shortest path
				Pair preds = mins.get(start);
				BetweennessMessage toSend = new BetweennessMessage(start, true);
				for(Long pred: preds.predecessors){
					sendMessageToVertex(
							new LongWritable(pred),
							toSend);
				}
				
				toErase.add(start);
				continue;
			}
			
			
			Pair preds = null;	
			
			if(!mins.containsKey(start)){
				// First time we received a message for a shortest path for this vertex
				preds = new Pair();
				// Add it to the shortest path maps
				mins.put(start, preds);
				// And add it to the list of new shortest paths
				updateds.add(new Tuple(start, preds));
			} else {
				secondTime = toErase.contains(start);
				preds = mins.get(start);
			}
			
			long from = message.getFromVertex();
			// If it's the second time we receive a shortest path message 
			// for this vertex we know it isn't the shortest path
			// We also never send back to the start vertex 
			// because there the number of shortest paths between neighbours is always 0
			if(!secondTime && start!=from)
				preds.predecessors.add(from);
				
		}
		
		//Erase to save memory
		for(Long start: toErase){
			mins.put(start, null);
		}
		
		long myId = vertex.getId().get();
		for(Tuple t: updateds){
			
			boolean sentProgress = false;
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
								new BetweennessMessage(t.start, myId));
					}
				}
			}
			
			// Erase pred to save memory only if no progress messages were sent
			// Which means that the vertex will not be waiting for a shortest path 
			// message so it can erase
			if(!sentProgress)
				mins.put(t.start, null);
		}
		
		vertex.voteToHalt();
		
	}


}

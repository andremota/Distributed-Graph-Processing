package pt.isel.ps1314v.g11.bc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class BetweennessCentralityAlgorithm extends Algorithm<LongWritable, BetweennessVertexValue, IntWritable, BetweennessMessage>{

	@Override
	public void compute(
			Vertex<LongWritable, BetweennessVertexValue, IntWritable> vertex,
			Iterable<BetweennessMessage> messages) {
		if(getSuperstep()==0){
			vertex.setVertexValue(new BetweennessVertexValue());
			
			Map<Long,Map<Long,Integer>> mins = vertex.getVertexValue().getMinimums();
			
			long start = vertex.getId().get();
			
			for(Edge<LongWritable,IntWritable> e : vertex.getVertexEdges()){
				mins.put(e.getTargetVertexId().get(), new HashMap<Long, Integer>());
				sendMessageToVertex(
						e.getTargetVertexId(),
						new BetweennessMessage(
								start,
								start,
								e.getValue().get()));
			}
			
			return;
		}
		
		BetweennessVertexValue value = vertex.getVertexValue();
		Map<Long, Map<Long, Integer>> mins = value.getMinimums();
		Set<Long> updateds = new HashSet<>();
		
		boolean newMin = false;
		for(BetweennessMessage message: messages){
			long start = message.getStartVertex();
			
			Integer min = mins.get(message.getFromVertex()).get(message.getStartVertex());
			
			int cost = message.getCost();
			
			if(min==null || cost<min){
				newMin = true;
				//mins.put(start, cost);
				updateds.add(start);
				continue;
			}
		}
		
		if(newMin || getSuperstep()==0){
			for(Edge<LongWritable, IntWritable> edge: vertex.getVertexEdges()){
				LongWritable id = edge.getTargetVertexId();
				for(Long start: updateds){
					//if(start!=id.get())
					
				}
				
			}
		}
		
		vertex.voteToHalt();
		
	}


}

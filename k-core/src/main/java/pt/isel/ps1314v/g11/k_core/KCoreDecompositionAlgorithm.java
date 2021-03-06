package pt.isel.ps1314v.g11.k_core;

import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class KCoreDecompositionAlgorithm
		extends
		Algorithm<LongWritable, KCoreDecompositionVertexValue, IntWritable, KCoreDecompositionMessage> {

	Logger LOG = Logger.getLogger(KCoreDecompositionAlgorithm.class);
	
	@Override
	public void compute(
			Vertex<LongWritable, KCoreDecompositionVertexValue, IntWritable> vertex,
			Iterable<KCoreDecompositionMessage> messages) {

		// Initialization: Each vertex has it's degree as it's core.
		// Also set up est now.
		if (getSuperstep() == 0) {
			
			vertex.setVertexValue(new KCoreDecompositionVertexValue());
			
			int core = 0;
			Map<Long,Integer> est = vertex.getVertexValue().getEst();
			for(Edge<LongWritable, IntWritable> edges: vertex.getVertexEdges()){
				int val;
				IntWritable edgeValue = edges.getValue();
				
				if(edgeValue == null ||( val = (edgeValue.get()) ) <= 0)
					val = 1;

				core += val;
				est.put(edges.getTargetVertexId().get(), Integer.MAX_VALUE);
			}
			
			
			vertex.getVertexValue().setCore(core);
			sendMessageToNeighbors(vertex, new KCoreDecompositionMessage(
													vertex.getId().get(),
													vertex.getVertexValue().getCore()			
											)
										);
			return;
		}
		
		
		// Update the new estimations that are lesser than known estimations
		KCoreDecompositionVertexValue vertexValue = vertex.getVertexValue();
		
		Map<Long,Integer> est = vertexValue.getEst();
		for(KCoreDecompositionMessage message: messages){
			if( message.getVertexCore() < est.get(message.getVertexId())){
				est.put(message.getVertexId(), message.getVertexCore());
			}
		}
		
		
		/*
		 * Optimization:
		 * Because we receive all messages at the same time 
		 * we can compute index just once
		 */
		int t = vertexValue.computeIndex();

		// Only send if the vertex changed it's estimation
		
		if(t < vertexValue.getCore()){
			vertexValue.setCore(t);
			vertexValue.setChanged(true);
		}
		
		if(!vertexValue.hasChanged()){
			vertex.voteToHalt();
			
			return;
		}
		
		int core = vertexValue.getCore();
		
		/*
		 * Optimization:
		 * Because we can send messages to a specific vertex
		 * we can send our new core only if core if our core is lower
		 * than the vertex's estimation. Or, in other words, we can 
		 * send our core if there's a possibility of it lowering 
		 * the coreness of the other vertex
		 */
		for(Edge<LongWritable, IntWritable> edges: vertex.getVertexEdges()){
			if(core < est.get(
					edges.getTargetVertexId().get()
					)
					){
				sendMessageToVertex(
						edges.getTargetVertexId(), 
						new KCoreDecompositionMessage(
								vertex.getId().get(), 
								core)
						);
			}
		}
		
		vertexValue.setChanged(false);
		

	}

}

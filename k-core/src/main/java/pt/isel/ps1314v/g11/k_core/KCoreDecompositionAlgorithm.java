package pt.isel.ps1314v.g11.k_core;

import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class KCoreDecompositionAlgorithm
		extends
		Algorithm<LongWritable, KCoreDecompositionVertexValue, LongWritable, KCoreDecompositionMessage> {

	Logger LOG = Logger.getLogger(KCoreDecompositionAlgorithm.class);
	
	@Override
	public void compute(
			Vertex<LongWritable, KCoreDecompositionVertexValue, LongWritable> vertex,
			Iterable<KCoreDecompositionMessage> messages) {

		LOG.info("NUM: "+vertex.getNumEdges());
		if (getSuperstep() == 0) {

			vertex.setVertexValue(
						new KCoreDecompositionVertexValue() //TODO This should be degree
					);
			
			Map<Long,Integer> est = vertex.getVertexValue().getEst();
			for(Edge<LongWritable, LongWritable> edges: vertex.getVertexEdges()){
				est.put(edges.getTargetVertexId().get(), Integer.MAX_VALUE);
			}
			LOG.info("SIZE :"+ est.size());
			sendMessageToNeighbors(vertex, new KCoreDecompositionMessage(
													vertex.getId().get(),
													vertex.getVertexValue().getCore()			
											)
										);
			return;
		}
		
		
		KCoreDecompositionVertexValue vertexValue = vertex.getVertexValue();
		
		Map<Long,Integer> est = vertexValue.getEst();
		for(KCoreDecompositionMessage message: messages){
			if( message.getVertexCore() < est.get(message.getVertexId())){
				est.put(message.getVertexId(), message.getVertexCore());
			}
		}
		LOG.info("VERTEX: "+vertex.getId());
		
		/*
		 * Optimization:
		 * Because we receive all messages at the same time 
		 * we can compute index just once
		 */
		int t = vertexValue.computeIndex();
		
		
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
		for(Edge<LongWritable, LongWritable> edges: vertex.getVertexEdges()){
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

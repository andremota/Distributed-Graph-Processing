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

		if (getSuperstep() == 0) {

			vertex.setVertexValue(
						new KCoreDecompositionVertexValue(vertex.getNumEdges()) //TODO This should be degree
					);
			
			Map<Long,Integer> est = vertex.getVertexValue().getEst();
			for(Edge<LongWritable, LongWritable> edges: vertex.getVertexEdges()){
				est.put(edges.getTargetVertexId().get(), Integer.MAX_VALUE);
			}
			
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
			if(est.get(message.getVertexId()) < message.getVertexCore())
				est.put(message.getVertexId(), message.getVertexCore());
		}
		
		
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
		LOG.info("EST IS "+est);
		for(Edge<LongWritable, LongWritable> edges: vertex.getVertexEdges()){
			if(core < est.get(
					edges.getTargetVertexId()
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

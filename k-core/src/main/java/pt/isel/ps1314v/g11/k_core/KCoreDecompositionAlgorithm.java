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
			LOG.info("num edges is "+vertex.getNumEdges());
			int i = 0;
			for(Edge<LongWritable, LongWritable> edges: vertex.getVertexEdges()){
				LOG.info("Edge id is "+ edges.getTargetVertexId().get());
				LOG.info("edge value is "+edges.getValue());
				est.put(edges.getTargetVertexId().get(), Integer.MAX_VALUE);
			}
			LOG.info("est size is " + est.size());
			sendMessageToNeighbors(vertex, new KCoreDecompositionMessage(
													vertex.getId().get(),
													vertex.getVertexValue().getCore()			
											)
										);
			return;
		}
		
		
		KCoreDecompositionVertexValue vertexValue = vertex.getVertexValue();
		
		Map<Long,Integer> est = vertexValue.getEst();
		LOG.info("VERTEX IS "+vertex.getId());
		LOG.info("EST SIZE IS "+est.size());
		LOG.info("EDGE NUM IS "+vertex.getNumEdges());
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
		//LOG.info("EST IS "+est);
		LOG.info("CORE IS "+core);

		for(Edge<LongWritable, LongWritable> edges: vertex.getVertexEdges()){
			/*LOG.info("EDGE IS " + edges);
			LOG.info("EDGE TARGET IS " +edges.getTargetVertexId());
			LOG.info("EST FOR TARGET IS "+est.get(edges.getTargetVertexId()));*/
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

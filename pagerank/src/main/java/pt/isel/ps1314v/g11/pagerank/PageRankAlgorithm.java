package pt.isel.ps1314v.g11.pagerank;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;
import pt.isel.ps1314v.g11.heatkernel.RandomWalkAlgorithm;

public class PageRankAlgorithm extends RandomWalkAlgorithm{

	private final Logger LOG = Logger.getLogger(PageRankAlgorithm.class);
	
	@Override
	public double recompute(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex,
			Iterable<DoubleWritable> messages) {
		
		double sum = 0;
		int nMessages = 0;
		for(DoubleWritable w : messages){
			sum += w.get();
			++nMessages;
		}
		
		LOG.info("Vertex " + vertex.getId() + " received " + nMessages + " messages.");
		
		return (1 - getJumpFactor() ) * sum + getJumpFactor() / getEdgeWeigth(vertex);
	}

	@Override
	public double contribution(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> v,
			Edge<LongWritable, DoubleWritable> toEdge) {
		
		return v.getVertexValue().get() / getEdgeWeigth(v);
	}

}

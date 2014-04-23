package pt.isel.ps1314v.g11.pagerank;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.graph.Vertex;
import pt.isel.ps1314v.g11.heatkernel.RandomWalkAlgorithm;

public class PageRankAlgorithm extends RandomWalkAlgorithm{

	@Override
	public double recompute(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex,
			Iterable<DoubleWritable> messages) {
		
		double sum = 0;
		for(DoubleWritable w : messages){
			sum += w.get();
		}
		
		return (1 - getJumpFactor() ) * sum + getJumpFactor() / getEdgeWeigth(vertex);
	}

}

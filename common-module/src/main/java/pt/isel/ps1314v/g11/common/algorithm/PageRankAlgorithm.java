package pt.isel.ps1314v.g11.common.algorithm;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.graph.Vertex;

public class PageRankAlgorithm extends RandomWalkAlgorithm{

	@Override
	public void recompute(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex,
			Iterable<DoubleWritable> messages) {
		
	}

}

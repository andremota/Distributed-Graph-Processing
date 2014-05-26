package pt.isel.ps1314v.g11.bc;

import org.apache.hadoop.io.NullWritable;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class BetweennessCentralityAlgorithm extends Algorithm<NullWritable, NullWritable, NullWritable, NullWritable>{

	@Override
	public void compute(
			Vertex<NullWritable, NullWritable, NullWritable> vertex,
			Iterable<NullWritable> messages) {
		
	}

}

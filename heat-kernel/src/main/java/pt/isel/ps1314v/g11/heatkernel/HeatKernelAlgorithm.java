package pt.isel.ps1314v.g11.heatkernel;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.graph.Vertex;

public class HeatKernelAlgorithm extends RandomWalkAlgorithm {

	@Override
	public void recompute(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex,
			Iterable<DoubleWritable> messages) {

	}

}

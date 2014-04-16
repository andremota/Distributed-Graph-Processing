package pt.isel.ps1314.giraph.example;

import java.io.IOException;

import org.apache.giraph.graph.BasicComputation;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

public class ExampleComputation extends BasicComputation<LongWritable, DoubleWritable, FloatWritable, DoubleWritable>{

	private final Logger LOG = Logger.getLogger(ExampleComputation.class);
	
	@Override
	public void compute(
			org.apache.giraph.graph.Vertex<LongWritable, DoubleWritable, FloatWritable> arg0,
			Iterable<DoubleWritable> arg1) throws IOException {
		arg0.voteToHalt();
	}
	
}

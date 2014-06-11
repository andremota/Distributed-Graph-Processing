package pt.isel.ps1314.giraph.example;

import java.io.IOException;

import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;

/**
 * 
 * A simple giraph computation example in which each vertex should have the
 * minor value in its adjacency. Is equivalent to SimpleVertex in hama-example.
 */
public class SimpleComputation extends
		BasicComputation<LongWritable, IntWritable, NullWritable, IntWritable> {
	@Override
	public void compute(Vertex<LongWritable, IntWritable, NullWritable> vertex,
			Iterable<IntWritable> messages) throws IOException {

		if (getSuperstep() == 1) {
			IntWritable writable = vertex.getValue();
			// Calculate the minimum value in the adjacency (only received
			// messages from adjacent vertices)
			for (IntWritable msg : messages) {
				writable.set(Math.min(writable.get(), msg.get()));
			}
			// halt the computation (this simple algorithm ends here.)
			vertex.voteToHalt();
			return;
		}
		// Send to adjacent vertices this vertex value.
		sendMessageToAllEdges(vertex, vertex.getValue());
	}
}

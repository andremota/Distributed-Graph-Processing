package pt.isel.ps1314v.g11.hama.example;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hama.graph.Vertex;

/**
 * 
 * A simple hama vertex computation example in which each vertex should have the minor value in its adjacency
 * Is equivalent to SimpleComputation in giraph-example.
 * 
 */
public class SimpleVertex extends Vertex<LongWritable, NullWritable, IntWritable>{
	public void compute(Iterable<IntWritable> messages) throws IOException {
		if(getSuperstepCount() == 1){
			IntWritable writable = getValue();
			//Calculate the minimum value in the adjacency (only received messages from adjacent vertices)
			for(IntWritable msg : messages){
				writable.set(Math.min(msg.get(), writable.get()));
			}
			// halt the computation (this simple algorithm ends here.)
			voteToHalt();
			return;
		}
		//Send to adjacent vertices this vertex value.
		sendMessageToNeighbors(getValue());
	}
}

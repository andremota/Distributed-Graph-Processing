package pt.isel.ps1314.giraph.example;

import java.io.IOException;

import org.apache.giraph.job.GiraphJob;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Vertex;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleConfiguration;

public class GiraphModuleExample {

	/*
	 * Normally algorithms should be implemented on the common-module, however
	 * this is just an example for the giraph-module.
	 */
	private static class ExampleAlgorithm extends
			Algorithm<LongWritable, IntWritable, IntWritable> {

		@Override
		public void compute(
				Vertex<LongWritable, IntWritable, IntWritable> vertex,
				Iterable<IntWritable> messages) {
			
			if (getSuperstep() == 2) {
				/*
				 * Will halt the computation in the second superstep.
				 */
				vertex.voteToHalt();
			}

		}

	}

	public void main(String... args) throws IOException {

		GiraphJob job = new GiraphJob("ExampleJob");

		CommonConfig commonConfig = new CommonConfig(
				new GiraphModuleConfiguration(job));

		/*
		 * Sets the common algorithm class and will set the Computation mapper class
		 * in the giraph configuration.
		 */
		commonConfig.setAlgorithmClass(ExampleAlgorithm.class);

		/*
		 * Run the job with the GiraphModuleConfiguration configs.
		 * 
		 * This will set all the needed mapper classes in the
		 * GiraphConfiguration and will run the job.
		 */
		commonConfig.run(true);
	}
}

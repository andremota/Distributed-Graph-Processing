package pt.isel.ps1314.giraph.example;

import java.io.IOException;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat;
import org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexOutputFormat;
import org.apache.giraph.job.GiraphJob;
import org.apache.giraph.utils.InternalVertexRunner;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

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

		private static final Logger LOG = Logger
				.getLogger(GiraphModuleExample.class);

		@Override
		public void compute(
				Vertex<LongWritable, IntWritable, IntWritable> vertex,
				Iterable<IntWritable> messages) {

			LOG.info("Superstep " + getSuperstep() + " on vertex with id "
					+ vertex.getId().get());

			if (getSuperstep() == 2) {
				/*
				 * Will halt the computation in the second superstep.
				 */
				vertex.voteToHalt();
			}

		}

	}

	public static void main(String... args) throws Exception {

		String[] graph = new String[] { 
				"[1,0,[[2,1],[3,1]]]",
				"[2,0,[[3,1],[4,1]]]", 
				"[3,0,[[4,1]]]", 
				"[4,0,[]]" };

		GiraphConfiguration conf = new GiraphConfiguration();
		
		conf.setVertexInputFormatClass(
		        JsonLongDoubleFloatDoubleVertexInputFormat.class);
		
		conf.setVertexOutputFormatClass(
		        JsonLongDoubleFloatDoubleVertexOutputFormat.class);

		CommonConfig commonConfig = new CommonConfig(
				new GiraphModuleConfiguration(conf));
		/*
		 * Sets the common algorithm class and will set the Computation mapper
		 * class in the giraph configuration.
		 */
		commonConfig.setAlgorithmClass(ExampleAlgorithm.class);
		
		/*
		 * This will set all the needed mapper classes in the
		 * GiraphConfiguration and will run the job.
		 */
		commonConfig.preparePlatformConfig();
		
		Iterable<String> its = InternalVertexRunner.run(conf, graph);
		
		for(String r : its){
			System.out.println(r);
		}
		
	}
}

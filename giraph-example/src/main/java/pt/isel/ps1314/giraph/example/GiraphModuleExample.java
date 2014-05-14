package pt.isel.ps1314.giraph.example;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat;
import org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexOutputFormat;
import org.apache.giraph.utils.InternalVertexRunner;

import pt.isel.ps1314v.g11.common.aggregator.BooleanAndAggregator;
import pt.isel.ps1314v.g11.common.aggregator.DoubleSumAggregator;
import pt.isel.ps1314v.g11.common.combiner.DoubleSumCombiner;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleConfiguration;

/**
 * This is just an example of the usage of the giraph-module.
 * 
 */
public class GiraphModuleExample {

	public static void main(String... args) throws Exception {
		
		GiraphConfiguration conf = new GiraphConfiguration();

		/*
		 * To run on the Local job Runner
		 */
		conf.set("giraph.SplitMasterWorker", "false");

		conf.setVertexInputFormatClass(JsonLongDoubleFloatDoubleVertexInputFormat.class);
		conf.setVertexOutputFormatClass(JsonLongDoubleFloatDoubleVertexOutputFormat.class);
		conf.setWorkerConfiguration(1, 1, 100);
		
		GiraphModuleConfiguration giraphConfig = new GiraphModuleConfiguration(conf);
		CommonConfig commonConfig = new CommonConfig(giraphConfig);
		
		/*
		 * Sets the common algorithm class and will set the Computation mapper
		 * class in the giraph configuration.
		 */
		commonConfig.setAlgorithmClass(ExampleAlgorithm.class);
		//commonConfig.setCombinerClass(DoubleSumCombiner.class);

		commonConfig.registerAggregator("Boolean", BooleanAndAggregator.class);
		commonConfig.registerAggregator("Double", DoubleSumAggregator.class);
		
		
		//commonConfig.registerAggregator("WHat", DoubleSumAggregator.class);
		
		/*
		 * This will finish setting up the Configuration for Apache Giraph. MUST
		 * BE CALLED before starting a job.
		 */
		commonConfig.preparePlatformConfig();
		
		// To run on the VertexRunner.
		
		String[] graph = new String[] { 
		  					"[1,0,[[2,1],[3,3]]]",
		  					"[2,0,[[3,1],[4,10]]]",
		  					"[3,0,[[4,2]]]",
							"[4,0,[]]" };
		  					
		 Iterable<String> its = InternalVertexRunner.run(conf, graph);
		 if (its != null)
		 	for (String r : its) {
		 		System.out.println(r);
		 	}
		
	}

}

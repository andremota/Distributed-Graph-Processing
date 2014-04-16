package pt.isel.ps1314.giraph.example;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat;
import org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexOutputFormat;
import org.apache.giraph.utils.InternalVertexRunner;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleConfiguration;

/**
 * This is just an example of the usage of the giraph-module.
 * 
 */
public class GiraphModuleExample {

	public static void main(String... args) throws Exception {

		String[] graph = new String[] { "[1,0,[[2,1],[3,3]]]",
				"[2,0,[[3,1],[4,10]]]", "[3,0,[[4,2]]]", "[4,0,[]]" };
		
		GiraphConfiguration conf = new GiraphConfiguration();

		conf.setVertexInputFormatClass(JsonLongDoubleFloatDoubleVertexInputFormat.class);
		conf.setVertexOutputFormatClass(JsonLongDoubleFloatDoubleVertexOutputFormat.class);
		
		CommonConfig commonConfig = new CommonConfig(
				new GiraphModuleConfiguration(conf));

		/*
		 * Sets the common algorithm class and will set the Computation mapper
		 * class in the giraph configuration.
		 */
		commonConfig.setAlgorithmClass(ExampleAlgorithm.class);

		/*
		 * This will finish setting up the Configuration for Apache Giraph.
		 * MUST BE CALLED before starting a job.
		 */
		commonConfig.preparePlatformConfig();

		Iterable<String> its = InternalVertexRunner.run(conf, graph);
		if (its != null)
			for (String r : its) {
				System.out.println(r);
			}
		

	}

}

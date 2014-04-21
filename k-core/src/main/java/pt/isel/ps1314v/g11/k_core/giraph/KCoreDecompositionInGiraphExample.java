package pt.isel.ps1314v.g11.k_core.giraph;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat;
import org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexOutputFormat;
import org.apache.giraph.utils.InternalVertexRunner;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleConfiguration;
import pt.isel.ps1314v.g11.k_core.KCoreDecompositionAlgorithm;

public class KCoreDecompositionInGiraphExample {

	public static void main(String[] args) throws Exception {
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
		
		commonConfig.setAlgorithmClass(KCoreDecompositionAlgorithm.class);
		
		commonConfig.preparePlatformConfig();
		
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

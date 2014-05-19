package pt.isel.ps1314v.g11.llp.giraph;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.IdWithValueTextOutputFormat;
import org.apache.giraph.utils.InternalVertexRunner;

import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleConfiguration;
import pt.isel.ps1314v.g11.llp.LLPAlgorithm;
import pt.isel.ps1314v.g11.llp.giraph.io.AdjacencyListWithValuesInputFormat;

public class LLPGiraphExample {
	public static void main(String[] args) throws Exception{
		GiraphConfiguration conf = new GiraphConfiguration();

		/*
		 * To run on the Local job Runner
		 */
		conf.set("giraph.SplitMasterWorker", "false");

		conf.setVertexInputFormatClass(AdjacencyListWithValuesInputFormat.class);
		conf.setVertexOutputFormatClass(IdWithValueTextOutputFormat.class);
		conf.setWorkerConfiguration(1, 1, 100);
		
		GiraphModuleConfiguration giraphConfig = new GiraphModuleConfiguration(conf);
		CommonConfig commonConfig = new CommonConfig(giraphConfig);
		
		commonConfig.registerAggregator(LLPAlgorithm.GLOBAL_CHANGE_AGGREGATOR, BooleanOrAggregator.class);
		commonConfig.setAlgorithmClass(LLPAlgorithm.class);
		
		commonConfig.preparePlatformConfig();
		
		String[] graph = new String[] { 
				"1 0 2 1 4 1",
				"2 0 1 1 3 1",
				"3 0 2 1 4 1",
				"4 0 1 1 3 1"};
		
		/*String[] graph = new String[]{
				"0 0 1 1 2 1",
				"1 0 0 1 3 1",
				"2 0 0 1 3 1 4 1",
				"3 0 1 1 2 2",
				"4 0 2 1"
		};*/
		
		/*String[] graph = new String[] { 
					"0 0 1 1 3 1",
					"1 0 0 1 2 1 3 1",
					"2 0 1 1 4 1",
					"3 0 0 1 1 1 4 1",
					"4 0 3 1 2 1"};*/
		
		/*String[] graph = new String[]{
				"0 0 1 1 2 1 3 1",
				"1 0 0 1 2 1 3 1",
				"2 0 1 1 0 1 3 1",
				"3 0 1 1 0 1 2 1 4 1",
				"4 0 5 1 3 1",
				"5 0 6 1 7 1 8 1 4 1",
				"6 0 5 1 7 1 8 1",
				"7 0 5 1 6 1 8 1",
				"8 0 5 1 6 1 7 1",
		};*/
		
		Iterable<String> its = InternalVertexRunner.run(conf, graph);
		 if (its != null)
		 	for (String r : its) {
		 		System.out.println(r);
		 	}
	}
}

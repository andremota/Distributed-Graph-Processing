package pt.isel.ps1314v.g11.allp.giraph;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.IdWithValueTextOutputFormat;
import org.apache.giraph.utils.InternalVertexRunner;

import pt.isel.ps1314v.g11.allp.ALLPAlgorithm;
import pt.isel.ps1314v.g11.allp.giraph.io.ALLPAdjacencyListWithValuesInputFormat;
import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleConfiguration;

public class ALLPGiraphExample {
	public static void main(String[] args) throws Exception {
		GiraphConfiguration conf = new GiraphConfiguration();

		/*
		 * To run on the Local job Runner
		 */
		conf.set("giraph.SplitMasterWorker", "false");
		conf.setWorkerConfiguration(1, 1, 100);

		conf.setVertexInputFormatClass(ALLPAdjacencyListWithValuesInputFormat.class);
		conf.setVertexOutputFormatClass(IdWithValueTextOutputFormat.class);
		
		conf.setFloat(ALLPAlgorithm.DECISION_FACTOR, 0f);
		conf.setBoolean(ALLPAlgorithm.COUNT_VERTEX_AS_OWN_NEIGHBOR, false);

		GiraphModuleConfiguration giraphConfig = new GiraphModuleConfiguration(
				conf);
		CommonConfig commonConfig = new CommonConfig(giraphConfig);

		commonConfig.registerAggregator(ALLPAlgorithm.GLOBAL_CHANGE_AGGREGATOR,
				BooleanOrAggregator.class);
		commonConfig.setAlgorithmClass(ALLPAlgorithm.class);

		commonConfig.preparePlatformConfig();

//		String[] graph = new String[] { "1 0 2 1 4 1", "2 0 1 1 3 1",
//				"3 0 2 1 4 1", "4 0 1 1 3 1" };

		/*
		 * String[] graph = new String[]{ "0 0 1 1 2 1", "1 0 0 1 3 1",
		 * "2 0 0 1 3 1 4 1", "3 0 1 1 2 2", "4 0 2 1" };
		 */

		/*
		 * String[] graph = new String[] { "0 0 1 1 3 1", "1 0 0 1 2 1 3 1",
		 * "2 0 1 1 4 1", "3 0 0 1 1 1 4 1", "4 0 3 1 2 1"};
		 */

		/*
		 * String[] graph = new String[]{ "0 0 1 1 2 1 3 1", "1 0 0 1 2 1 3 1",
		 * "2 0 1 1 0 1 3 1", "3 0 1 1 0 1 2 1 4 1", "4 0 5 1 3 1",
		 * "5 0 6 1 7 1 8 1 4 1", "6 0 5 1 7 1 8 1", "7 0 5 1 6 1 8 1",
		 * "8 0 5 1 6 1 7 1", };
		 */

//		 String[] graph = new String[]{
//		 "0 0 1 1 2 1 3 1",
//		 "1 0 0 1 2 1 3 1",
//		 "2 0 0 1 1 1 3 1",
//		 "3 0 0 1 1 1 2 1 4 1",
//		 "4 0 3 1 5 1 6 1",
//		 "5 0 4 1 7 1 8 1 9 1",
//		 "6 0 5 1 10 1 11 1 12 1 13 1",
//		 "7 0 5 1 8 1 9 1",
//		 "8 0 5 1 7 1 9 1",
//		 "9 0 5 1 7 1 8 1",
//		 "10 0 6 1 11 1 12 1 13 1",
//		 "11 0 6 1 10 1 12 1 13 1",
//		 "12 0 6 1 10 1 11 1 13 1",
//		 "13 0 6 1 10 1 11 1 12 1"
//		 };

		
//		  String[] graph = new String[]{ "0 0 1 1 5 1", "1 0 0 1 2 1",
//		  "2 0 1 1 3 1", "3 0 2 1 4 1", "4 0 3 1 5 1", "5 0 0 1 4 1" };
		 

		
		  String[] graph = new String[]{ "0 0 2 1 3 1 4 1 5 1",
		  "1 0 2 1 4 1 7 1", "2 0 0 1 1 1 4 1 5 1 6 1", "3 0 0 1 7 1",
		  "4 0 0 1 1 1 2 1 10 1", "5 0 0 1 2 1 7 1 11 1", "6 0 2 1 7 1 11 1",
		  "7 0 1 1 3 1 5 1 6 1", "8 0 9 1 10 1 11 1 14 1 15 1",
		  "9 0 8 1 12 1 14 1", "10 0 4 1 8 1 11 1 12 1 13 1 14 1",
		  "11 0 5 1 6 1 8 1 10 1 13 1", "12 0 9 1 10 1", "13 0 10 1 11 1",
		  "14 0 8 1 9 1 10 1", "15 0 8 1" };
		 

		Iterable<String> its = InternalVertexRunner.run(conf, graph);
		if (its != null) {
			for (String r : its) {
				System.out.println(r);
			}
		}

	}
}

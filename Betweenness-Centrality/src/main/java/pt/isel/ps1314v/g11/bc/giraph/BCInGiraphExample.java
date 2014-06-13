package pt.isel.ps1314v.g11.bc.giraph;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.utils.InternalVertexRunner;

import pt.isel.ps1314v.g11.bc.BetweennessCentralityAlgorithm;
import pt.isel.ps1314v.g11.bc.giraph.io.AdjacencyListBCInputFormat;
import pt.isel.ps1314v.g11.bc.giraph.io.JsonBCOutputFormat;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleConfiguration;
import pt.isel.ps1314v.g11.giraph.util.ExampleFileRunner;

public class BCInGiraphExample {
	public static void main(String[] args) throws Exception {
		GiraphConfiguration conf = new GiraphConfiguration();

		/*
		 * To run on the Local job Runner
		 */
		conf.set("giraph.SplitMasterWorker", "false");

		conf.setVertexInputFormatClass(AdjacencyListBCInputFormat.class);
		conf.setVertexOutputFormatClass(JsonBCOutputFormat.class);
		conf.setWorkerConfiguration(1, 1, 100);
		//conf.set("mapreduce.job.counters.limit", "240");
		
		GiraphModuleConfiguration giraphConfig = new GiraphModuleConfiguration(conf);
		CommonConfig commonConfig = new CommonConfig(giraphConfig);
		
		commonConfig.setAlgorithmClass(BetweennessCentralityAlgorithm.class);
		
		commonConfig.setStrings(BetweennessCentralityAlgorithm.START_VERTEXES, new String[]{
				"0",
				"1",
				"2",
				"3",
				"4",
//				"5",
//				"6",
//				"7",
//				"8",
//				"9",
//				"10",
//				"11",
//				"12",
//				"13",
//				"14",
//				"15"
		});
		
//		commonConfig.setBoolean(BetweennessCentralityAlgorithm.NORMALIZE, true);
		
		//Only needed when BetweennessCentralityAlgorithm.NORMALIZE is set to true.
//		commonConfig.registerAggregator(BetweennessCentralityAlgorithm.AGG_ENDED, BooleanAndAggregator.class);
//		commonConfig.registerAggregator(BetweennessCentralityAlgorithm.AGG_MIN_BC, DoubleMinAggregator.class);
//		commonConfig.registerAggregator(BetweennessCentralityAlgorithm.AGG_MAX_BC, DoubleMaxAggregator.class);
		
		commonConfig.preparePlatformConfig();
		
		String[] graph = new String[] {
				"0 0 1 1",
				"1 0 0 1 2 1",
				"2 0 1 1"
		};
		
		/*String[] graph = new String[]{
				"[0,0,[[1,1]]]",
				"[1,1,[[0,1],[2,1],[3,1]]]",
				"[2,1,[[1,1],[4,1]]]",
				"[3,1,[[1,1],[4,1]]]",
				"[4,1,[[2,1],[3,1]]]"
		};*/
		
//		String[] graph = new String[] {
//			"[0,0,[[1,1]]]",
//			"[1,0,[[0,1],[2,2]]]",
//			"[2,0,[[1,1],[3,3]]]",
//			"[3,0,[[2,2],[4,4]]]",
//			"[4,0,[[3,3]]]"
//		};
//		
//		String[] graph = new String[] {
//						"[0,0,[[1,1],[3,3]]]",
//						"[1,0,[[0,1],[2,2],[3,1]]]",
//						"[2,0,[[1,2],[4,4]]]",
//						"[3,0,[[0,3],[1,1],[4,4]]]",
//						"[4,0,[[3,4],[2,4]]]"
//		};
		
		/*
		String[] graph = new String[] { 
					"[1,0,[[2,1],[3,3]]]",
					"[2,0,[[1,1],[3,1],[4,10]]]",
					"[3,0,[[1,3],[2,1],[4,2]]]",
					"[4,0,[[2,10],[3,2]]]" };
		*/
//		String[] graph = new String[] { 
//				"[1,0,[[2,1],[4,1]]]",
//				"[2,0,[[1,1],[3,1]]]",
//				"[3,0,[[2,1],[4,1]]]",
//				"[4,0,[[1,1],[3,1]]]" };
		
//		String[] graph = new String[]{
//				"[0,0,[[2,1],[3,1],[4,1],[5,1]]]",
//				"[1,0,[[2,1],[4,1],[7,1]]]",
//				"[2,0,[[0,1],[1,1],[4,1],[5,1],[6,1]]]",
//				"[3,0,[[0,1],[7,1]]]",
//				"[4,0,[[0,1],[1,1],[2,1],[10,1]]]",
//				"[5,0,[[0,1],[2,1],[7,1],[11,1]]]",
//				"[6,0,[[2,1],[7,1],[11,1]]]",
//				"[7,0,[[1,1],[3,1],[5,1],[6,1]]]",
//				"[8,0,[[9,1],[10,1],[11,1],[14,1],[15,1]]]",
//				"[9,0,[[8,1],[12,1],[14,1]]]",
//				"[10,0,[[4,1],[8,1],[11,1],[12,1],[13,1],[14,1]]]",
//				"[11,0,[[5,1],[6,1],[8,1],[10,1],[13,1]]]",
//				"[12,0,[[9,1],[10,1]]]",
//				"[13,0,[[10,1],[11,1]]]",
//				"[14,0,[[8,1],[9,1],[10,1]]]",
//				"[15,0,[[8,1]]]"
//		};
		
	/*	
		String[] graph = new String[]{
				"[0,0,[[1,1],[2,1]]]",
				"[1,0,[[0,1],[3,1]]]",
				"[2,0,[[0,1],[3,1],[4,1]]]",
				"[3,0,[[1,1],[2,1]]]",
				"[4,0,[[2,1]]]"
		};
		*/
		/*String[] graph = new String[]{
				"[0,0,[[1,1],[2,1],[3,1]]]",
				"[1,0,[[0,1],[2,1],[3,1]]]",
				"[2,0,[[0,1],[1,1],[3,1]]]",
				"[3,0,[[0,1],[1,1],[2,1],[4,1]]]",
				"[4,0,[[3,1],[5,1],[6,1]]]",
				"[5,0,[[4,1],[7,1],[8,1],[9,1]]]",
				"[6,0,[[5,1],[10,1],[11,1],[12,1],[13,1]]]",
				"[7,0,[[5,1],[8,1],[9,1]]]",
				"[8,0,[[5,1],[7,1],[9,1]]]",
				"[9,0,[[5,1],[7,1],[8,1]]]",
				"[10,0,[[6,1],[11,1],[12,1],[13,1]]]",
				"[11,0,[[6,1],[10,1],[12,1],[13,1]]]",
				"[12,0,[[6,1],[10,1],[11,1],[13,1]]]",
				"[13,0,[[6,1],[10,1],[11,1],[12,1]]]"
		};
		*/
		/*String[] graph = new String[]{
				"[0,0,[[1,1],[5,1]]]",
				"[1,0,[[2,1],[0,1]]]",
				"[2,0,[[1,1],[3,1]]]",
				"[3,0,[[2,1],[4,1]]]",
				"[4,0,[[3,1],[5,1]]]",
				"[5,0,[[0,1],[4,1]]]"
		};*/
		
		if(args.length >= 2){
			ExampleFileRunner.run(args[0], args[1], conf);
		}else{
			Iterable<String> its = InternalVertexRunner.run(conf, graph);
			 if (its != null){
			 	for (String r : its) {
			 		System.out.println(r);
			 	}
			 }
		}
	}
}

package pt.isel.ps1314v.g11.pagerank.giraph;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.IdWithValueTextOutputFormat;
import org.apache.giraph.utils.InternalVertexRunner;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleConfiguration;
import pt.isel.ps1314v.g11.giraph.util.ExampleFileRunner;
import pt.isel.ps1314v.g11.heatkernel.RandomWalkAlgorithm;
import pt.isel.ps1314v.g11.heatkernel.giraph.io.AdjacencyListWithValuesInputFormat;
import pt.isel.ps1314v.g11.heatkernel.util.RandomWalkConfig;
import pt.isel.ps1314v.g11.pagerank.PageRankAlgorithm;

public class PageRankGiraphExample {
	
	public static void main(String[] args) throws Exception{
		GiraphConfiguration conf = new GiraphConfiguration();

		RandomWalkConfig argsConfig = RandomWalkConfig.parseArgs(args);
		/*
		 * To run on the Local job Runner
		 */
		conf.set("giraph.SplitMasterWorker", "false");
		
		conf.setVertexInputFormatClass(AdjacencyListWithValuesInputFormat.class);
		conf.setVertexOutputFormatClass(IdWithValueTextOutputFormat.class);
		conf.setWorkerConfiguration(1, 1, 100);
		
		GiraphModuleConfiguration giraphConfig = new GiraphModuleConfiguration(conf);
		CommonConfig commonConfig = new CommonConfig(giraphConfig);
		
		commonConfig.setAlgorithmClass(PageRankAlgorithm.class);
		
		commonConfig.preparePlatformConfig();

		commonConfig.setInt(RandomWalkAlgorithm.MAX_SUPERSTEPS_CONF, argsConfig.getNumberOfSupersteps());
		if(argsConfig.getFactor() != 0){
			conf.setFloat(RandomWalkAlgorithm.JUMP_FACTOR_CONF, argsConfig.getFactor());
		}
		
		String[] graph = new String[] { 
					"1 0 2 1 4 1 5 1",
					"2 0 5 1",
					"3 0 1 1",
					"4 0 3 1 5 1",
					"5 0 4 1"};
		
		if(args.length >= 2){
			ExampleFileRunner.run(argsConfig.getInputPath(), argsConfig.getOutputPath(), conf);
		}
		else {
			Iterable<String> its = InternalVertexRunner.run(conf, graph);
			if (its != null){
				for (String r : its) {
					System.out.println(r);
				}
			}
		}
		
	}
}

package pt.isel.ps1314v.g11.pagerank.hama;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;
import org.kohsuke.args4j.CmdLineException;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.hama.config.HamaModuleConfiguration;
import pt.isel.ps1314v.g11.heatkernel.RandomWalkAlgorithm;
import pt.isel.ps1314v.g11.heatkernel.hama.io.RandomWalkVertexInputReader;
import pt.isel.ps1314v.g11.heatkernel.util.RandomWalkConfig;
import pt.isel.ps1314v.g11.pagerank.PageRankAlgorithm;

public class PageRankHamaExample {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, CmdLineException{
		HamaConfiguration conf =  new HamaConfiguration();
		
		/*
		 * Some properties to make it easier to run 
		 * and debug locally.
		 * 
		 */
//		conf.set("bsp.master.address", "local");
//		conf.set("bsp.local.tasks.maximum", "2");
//		conf.set("fs.default.name", "file:///");
		
		GraphJob job = new GraphJob(conf, PageRankHamaExample.class);
		RandomWalkConfig argsConfig = RandomWalkConfig.parseArgs(args);
		job.setJobName("PageRankJob");
	    // Vertex reader
		job.setVertexInputReaderClass(RandomWalkVertexInputReader.class);
		
		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		
		job.setPartitioner(HashPartitioner.class);
		
		job.setInputPath(new Path(argsConfig.getInputPath()));
		job.setOutputPath(new Path(argsConfig.getOutputPath()));
		
		CommonConfig moduleConfig = new CommonConfig(
				new HamaModuleConfiguration(job));

		
		moduleConfig.setAlgorithmClass(PageRankAlgorithm.class);
		moduleConfig.setInt(RandomWalkAlgorithm.MAX_SUPERSTEPS_CONF, argsConfig.getNumberOfSupersteps());
		if(argsConfig.getFactor() != 0){
			conf.setFloat(RandomWalkAlgorithm.JUMP_FACTOR_CONF, argsConfig.getFactor());
		}
		
		moduleConfig.preparePlatformConfig();

		job.waitForCompletion(true);
	}
}

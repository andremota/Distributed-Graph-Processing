package pt.isel.ps1314v.g11.bc.hama;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;

import pt.isel.ps1314v.g11.bc.BetweennessCentralityAlgorithm;
import pt.isel.ps1314v.g11.bc.hama.io.BCVertexInputReader;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.hama.config.HamaModuleConfiguration;

public class BCHamaExample {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		HamaConfiguration conf =  new HamaConfiguration();
		GraphJob job = new GraphJob(conf, BCHamaExample.class);
		job.setJobName("PageRankJob");
	    // Vertex reader
		job.setVertexInputReaderClass(BCVertexInputReader.class);
		 
		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		
		job.setPartitioner(HashPartitioner.class);
		
		job.setInputPath(new Path(args[0]));
		job.setOutputPath(new Path(args[1]));
		
		CommonConfig moduleConfig = new CommonConfig(
				new HamaModuleConfiguration(job));

		
		moduleConfig.setAlgorithmClass(BetweennessCentralityAlgorithm.class);

		
		moduleConfig.preparePlatformConfig();

		job.waitForCompletion(true);
	}
}

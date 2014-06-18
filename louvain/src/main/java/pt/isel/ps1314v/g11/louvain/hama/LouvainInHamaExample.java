package pt.isel.ps1314v.g11.louvain.hama;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;

import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.aggregator.LongSumAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.hama.config.HamaModuleConfiguration;
import pt.isel.ps1314v.g11.louvain.LouvainAlgorithm;
import pt.isel.ps1314v.g11.louvain.hama.io.LouvainTextReader;

public class LouvainInHamaExample {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
			
			HamaConfiguration conf =  new HamaConfiguration();
	
			/*
			 * Some properties to make it easier to run 
			 * and debug locally.
			 * 
			 */
			conf.set("bsp.master.address", "local");
			conf.set("bsp.local.tasks.maximum", "2");
			conf.set("fs.default.name", "file:///");
			
			GraphJob job = new GraphJob(conf, LouvainInHamaExample.class);
			job.setJobName("ExampleJob");
		    // Vertex reader
			job.setVertexInputReaderClass(LouvainTextReader.class);
			
			/*job.setVertexIDClass(Text.class);
			job.setVertexValueClass(DoubleWritable.class);
			job.setEdgeValueClass(NullWritable.class);
	*/
			job.setInputFormat(TextInputFormat.class);
			//job.
			
			job.setPartitioner(HashPartitioner.class);
			job.setOutputFormat(TextOutputFormat.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(DoubleWritable.class);
		    
			job.setInputPath(new Path(args[0]));
			job.setOutputPath(new Path(args[1]));
			
			CommonConfig moduleConfig = new CommonConfig(
					new HamaModuleConfiguration(job));

			
			moduleConfig.setAlgorithmClass(LouvainAlgorithm.class);
			moduleConfig.registerAggregator(LouvainAlgorithm.AGG_M2, LongSumAggregator.class);
			moduleConfig.registerAggregator(LouvainAlgorithm.CHANGE_AGG, BooleanOrAggregator.class);

			
			moduleConfig.preparePlatformConfig();
	
			job.waitForCompletion(true);
	}
}

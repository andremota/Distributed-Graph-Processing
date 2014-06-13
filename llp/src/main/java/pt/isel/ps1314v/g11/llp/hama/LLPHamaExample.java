package pt.isel.ps1314v.g11.llp.hama;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;

import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.hama.config.HamaModuleConfiguration;
import pt.isel.ps1314v.g11.llp.LLPAlgorithm;
import pt.isel.ps1314v.g11.llp.hama.io.LLPVertexInputReader;

public class LLPHamaExample {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		HamaConfiguration conf = new HamaConfiguration();
		
		conf.set("bsp.master.address", "local");
		conf.set("bsp.local.tasks.maximum", "2");
		conf.set("fs.default.name", "file:///");
		
		GraphJob job = new GraphJob(conf, LLPHamaExample.class);
		job.setJobName("LLPJob");
		
		job.setVertexInputReaderClass(LLPVertexInputReader.class);
		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		
		job.setVertexIDClass(LongWritable.class);
		job.setVertexValueClass(LongWritable.class);
		job.setEdgeValueClass(NullWritable.class);
		
		job.setInputPath(new Path(args[0]));
		job.setOutputPath(new Path(args[1]));

		
		CommonConfig moduleConfig = new CommonConfig(
				new HamaModuleConfiguration(job));
		
		moduleConfig.setAlgorithmClass(LLPAlgorithm.class);
		
		moduleConfig.registerAggregator(LLPAlgorithm.GLOBAL_CHANGE_AGGREGATOR,
				BooleanOrAggregator.class);
		
		moduleConfig.preparePlatformConfig();

		job.waitForCompletion(true);
	}
}

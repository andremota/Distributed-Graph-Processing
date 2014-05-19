package pt.isel.ps1314v.g11.llp.hama;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.SequenceFileInputFormat;
import org.apache.hama.commons.io.TextArrayWritable;
import org.apache.hama.graph.Edge;
import org.apache.hama.graph.GraphJob;
import org.apache.hama.graph.VertexInputReader;

import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.hama.config.HamaModuleConfiguration;
import pt.isel.ps1314v.g11.llp.LLPAlgorithm;

public class LLPHamaExample {
	
	public static class LLPVertexInputReader 
				extends VertexInputReader<Text, TextArrayWritable, LongWritable, NullWritable, LongWritable>{

		@Override
		public boolean parseVertex(
				Text key,
				TextArrayWritable value,
				org.apache.hama.graph.Vertex<LongWritable, NullWritable, LongWritable> vertex)
				throws Exception {
			
				vertex.setVertexID(new LongWritable(Long.parseLong(key.toString())));

				for (Writable v : value.get()) {
					vertex.addEdge(
							new Edge<LongWritable, NullWritable>(
									new LongWritable(Long.parseLong(v.toString())),
									null));
				}

				return true;
		}
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		HamaConfiguration conf = new HamaConfiguration();
		
		conf.set("bsp.master.address", "local");
		conf.set("bsp.local.tasks.maximum", "2");
		conf.set("fs.default.name", "file:///");
		
		GraphJob job = new GraphJob(conf, LLPHamaExample.class);
		job.setJobName("LLPJob");
		
		job.setVertexInputReaderClass(LLPVertexInputReader.class);
		job.setInputFormat(SequenceFileInputFormat.class);
		
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

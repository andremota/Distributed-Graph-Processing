package pt.isel.ps1314v.g11.k_core.hama;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.SequenceFileInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.commons.io.TextArrayWritable;
import org.apache.hama.graph.Edge;
import org.apache.hama.graph.GraphJob;
import org.apache.hama.graph.Vertex;
import org.apache.hama.graph.VertexInputReader;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.hama.config.HamaModuleConfiguration;
import pt.isel.ps1314v.g11.k_core.KCoreDecompositionAlgorithm;
import pt.isel.ps1314v.g11.k_core.KCoreDecompositionVertexValue;

public class KCoreDecompositionInHamaExample {

	
	public static class KCoreSeqReader extends VertexInputReader<Text, TextArrayWritable,
	LongWritable, LongWritable, KCoreDecompositionVertexValue>{
		//private Logger LOG = Logger.getLogger(KCoreDecompositionInHamaExample.class);
		@Override
		public boolean parseVertex(
				Text key,
				TextArrayWritable value,
				Vertex<LongWritable, LongWritable, KCoreDecompositionVertexValue> vertex)
				throws Exception {
			
			//LOG.info("KEY: "+key.toString());
			vertex.setVertexID(new LongWritable(Long.parseLong(key.toString())));
			for(Writable w: value.get()){
				//LOG.info("Writable: " +w.toString());
				vertex.addEdge(new Edge<LongWritable, LongWritable>(
						new LongWritable(Long.parseLong(w.toString())),null));
			}
			return true;
		}
		
	}
	
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
		
		GraphJob job = new GraphJob(conf, KCoreDecompositionInHamaExample.class);
		job.setJobName("ExampleJob");
	    // Vertex reader
		job.setVertexInputReaderClass(KCoreSeqReader.class);

		/*job.setVertexIDClass(Text.class);
		job.setVertexValueClass(DoubleWritable.class);
		job.setEdgeValueClass(NullWritable.class);
*/
		job.setInputFormat(SequenceFileInputFormat.class);

		job.setPartitioner(HashPartitioner.class);
		job.setOutputFormat(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
	    
		job.setInputPath(new Path(args[0]));
		job.setOutputPath(new Path(args[1]));
		
		CommonConfig moduleConfig = new CommonConfig(
				new HamaModuleConfiguration(job));

		//moduleConfig.setAlgorithmClass(ExampleAlgorithm.class);
		
		moduleConfig.setAlgorithmClass(KCoreDecompositionAlgorithm.class);
		//moduleConfig.setCombinerClass(DoubleSumCombiner.class);
		
		//job.setAggregatorClass(DoubleAggregator.class, BooleanAggregator.class);
		//moduleConfig.setAggregatorClass(ExampleAggregator.class);
		//moduleConfig.setAggregatorClass(BooleanAndAggregator.class);
		
		//moduleConfig.preparePlatformConfig();

		job.waitForCompletion(true);
	}
}

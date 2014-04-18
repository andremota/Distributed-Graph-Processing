package pt.isel.ps1314v.g11.hama.example;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.SequenceFileInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.commons.io.TextArrayWritable;
import org.apache.hama.graph.AverageAggregator;
import org.apache.hama.graph.Edge;
import org.apache.hama.graph.GraphJob;
import org.apache.hama.graph.Vertex;
import org.apache.hama.graph.VertexInputReader;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.hama.config.HamaModuleConfiguration;

public class HamaModuleExample {
	  public static class PageRankVertex extends
      Vertex<Text, NullWritable, DoubleWritable> {

    static double DAMPING_FACTOR = 0.85;
    static double MAXIMUM_CONVERGENCE_ERROR = 0.001;

    @Override
    public void setup(HamaConfiguration conf) {
      String val = conf.get("hama.pagerank.alpha");
      if (val != null) {
        DAMPING_FACTOR = Double.parseDouble(val);
      }
      val = conf.get("hama.graph.max.convergence.error");
      if (val != null) {
        MAXIMUM_CONVERGENCE_ERROR = Double.parseDouble(val);
      }
    }

    @Override
    public void compute(Iterable<DoubleWritable> messages) throws IOException {
      // initialize this vertex to 1 / count of global vertices in this graph
      if (this.getSuperstepCount() == 0) {
        setValue(new DoubleWritable(1.0 / this.getNumVertices()));
      } else if (this.getSuperstepCount() >= 1) {
        double sum = 0;
        for (DoubleWritable msg : messages) {
          sum += msg.get();
        }
        double alpha = (1.0d - DAMPING_FACTOR) / this.getNumVertices();
        setValue(new DoubleWritable(alpha + (sum * DAMPING_FACTOR)));
        aggregate(0, this.getValue());
      }

      // if we have not reached our global error yet, then proceed.
      DoubleWritable globalError = getAggregatedValue(0);
      
      if (globalError != null && this.getSuperstepCount() > 2
          && MAXIMUM_CONVERGENCE_ERROR > globalError.get()) {
        voteToHalt();
      } else {
        // in each superstep we are going to send a new rank to our neighbours
        sendMessageToNeighbors(new DoubleWritable(this.getValue().get()
            / this.getEdges().size()));
      }
    }
  }
	
	/**
	 * Example VertexInputReader.
	 * @see <a href="https://hama.apache.org/hama_graph_tutorial.html">Hama Graph Tutorial</a>
	 */
	public static class PagerankSeqReader
			extends
			VertexInputReader<Text, TextArrayWritable, Text, NullWritable, DoubleWritable> {
		@Override
		public boolean parseVertex(Text key, TextArrayWritable value,
				Vertex<Text, NullWritable, DoubleWritable> vertex)
				throws Exception {
			vertex.setVertexID(key);

			for (Writable v : value.get()) {
				vertex.addEdge(new Edge<Text, NullWritable>((Text) v, null));
			}

			return true;
		}
	}

	public static void main(String... args) throws IOException,
			ClassNotFoundException, InterruptedException {
		
		HamaConfiguration conf =  new HamaConfiguration();

		/*
		 * Some properties to make it easier to run locally
		 */
		
		conf.set("bsp.master.address", "local");
		conf.set("bsp.local.tasks.maximum", "2");
		conf.set("fs.default.name", "file:///");
		
		GraphJob pageJob = new GraphJob(conf, HamaModuleExample.class);
	    pageJob.setJobName("Pagerank");

	    // set the defaults
	    pageJob.setMaxIteration(30);

	    // Vertex reader
	    pageJob.setVertexInputReaderClass(PagerankSeqReader.class);

	    pageJob.setVertexIDClass(Text.class);
	    pageJob.setVertexValueClass(DoubleWritable.class);
	    pageJob.setEdgeValueClass(NullWritable.class);

	    pageJob.setInputFormat(SequenceFileInputFormat.class);

	    pageJob.setPartitioner(HashPartitioner.class);
	    pageJob.setOutputFormat(TextOutputFormat.class);
	    pageJob.setOutputKeyClass(Text.class);
	    pageJob.setOutputValueClass(DoubleWritable.class);
	    
	    pageJob.setInputPath(new Path("/home/andre/randomgraph"));
	    pageJob.setOutputPath(new Path("/home/andre/result"));

		
		CommonConfig moduleConfig = new CommonConfig(
				new HamaModuleConfiguration(pageJob));

		moduleConfig.setAlgorithmClass(ExampleAlgorithm.class);
		moduleConfig.preparePlatformConfig();

	    pageJob.waitForCompletion(true);

	}
}

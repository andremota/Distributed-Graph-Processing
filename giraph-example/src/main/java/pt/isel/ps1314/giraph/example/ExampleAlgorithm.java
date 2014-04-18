package pt.isel.ps1314.giraph.example;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Vertex;

/*
 * Normally algorithms should be implemented on the common-module, however
 * this is just an example for the giraph-module.
 */

public class ExampleAlgorithm extends
		Algorithm<LongWritable, DoubleWritable, FloatWritable> {

	private static final Logger LOG = Logger
			.getLogger(GiraphModuleExample.class);

	@Override
	public void compute(
			Vertex<LongWritable, DoubleWritable, FloatWritable> vertex,
			Iterable<DoubleWritable> messages) {

		LOG.info("Superstep " + getSuperstep() + " on vertex with id "
				+ vertex.getId().get());

		if (getSuperstep() == 0) {
			
			for (pt.isel.ps1314v.g11.common.graph.Edge<LongWritable, FloatWritable> e : vertex
					.getVertexEdges()) {
				
				LOG.info("vertice " + vertex.getId() + " to "
						+ e.getTargetVertexId() + " with "  + e.getValue());
				
				sendMessage(e.getTargetVertexId(), new DoubleWritable(2));
			} 
		}

		if (getSuperstep() == 1) {
			/*
			 * Will halt the computation in the second superstep.
			 */
			vertex.voteToHalt();
		}

	}

}

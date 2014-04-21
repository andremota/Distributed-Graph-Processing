package pt.isel.ps1314.giraph.example;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.common.graph.BasicAlgorithm;
import pt.isel.ps1314v.g11.common.graph.Vertex;

/*
 * Normally algorithms should be implemented on the common-module, however
 * this is just an example for the giraph-module.
 */

public class ExampleAlgorithm extends
		BasicAlgorithm<LongWritable, DoubleWritable, FloatWritable> {

	private static final Logger LOG = Logger
			.getLogger(GiraphModuleExample.class);

	@Override
	public void compute(
			Vertex<LongWritable, DoubleWritable, FloatWritable> vertex,
			Iterable<DoubleWritable> messages) {

		LOG.info("Superstep " + getSuperstep() + " on vertex with id "
				+ vertex.getId().get());

		/*if (getSuperstep() == 0) {
			sendMessage(vertex.getId(), new DoubleWritable(2));
			sendMessage(vertex.getId(), new DoubleWritable(2));
			sendMessage(vertex.getId(), new DoubleWritable(2));
		}*/

		
		aggregateValue(0, new DoubleWritable(1));
		
		LOG.info("VALUE0="+getValueFromAggregator(0));
		LOG.info("VALUE1="+getValueFromAggregator(1));
		
		if(getSuperstep()==0)
			aggregateValue(1, new BooleanWritable(true));
		else if( getSuperstep() == 1 && vertex.getId().toString().equals("3"))
			aggregateValue(1, new BooleanWritable(false));
		if (getSuperstep() == 2) {
			/*
			 * Will halt the computation in the second superstep.
			 */
			for(DoubleWritable w :messages)
				LOG.info("VALUE = " + w);
			
			vertex.voteToHalt();
		}

	}

}

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

		
		if(getSuperstep() == 0){
			aggregateValue("Double", new DoubleWritable(1));
			aggregateValue("Boolean", new BooleanWritable(true));
		}
		/*if(getSuperstep()==0){
			vertex.setVertexValue(new DoubleWritable(Double.parseDouble(vertex.getId().toString())));
			sendMessageToVertex(vertex.getId(), new LongWritable(1));
			sendMessageToVertex(new Text(2+""), new LongWritable(2));
			sendMessageToVertex(new Text(3+""), new LongWritable(4));
	
		}*/

		
		
		
		DoubleWritable dW = getValueFromAggregator("Double");
		BooleanWritable bW = getValueFromAggregator("Boolean");
		LOG.info("VALUE0="+dW);
		LOG.info("VALUE1="+bW);
		

			
		if( getSuperstep() == 1 && vertex.getId().toString().equals("3"))
			aggregateValue("Boolean", new BooleanWritable(false));
		
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

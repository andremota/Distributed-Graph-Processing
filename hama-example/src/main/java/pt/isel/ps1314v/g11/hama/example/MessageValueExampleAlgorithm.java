package pt.isel.ps1314v.g11.hama.example;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class MessageValueExampleAlgorithm extends
		Algorithm<Text, DoubleWritable, NullWritable, LongWritable> {

	
	private static final Logger LOG = Logger
			.getLogger(MessageValueExampleAlgorithm.class);
	@Override
	public void compute(Vertex<Text, DoubleWritable, NullWritable> vertex,
			Iterable<LongWritable> messages) {
				LOG.info("Superstep " + getSuperstep() + " on vertex with id "
				+ vertex.getId());
		
				
		if(getSuperstep() == 0){
			aggregateValue("Double", new DoubleWritable(1));
			aggregateValue("Boolean", new BooleanWritable(false));
		}
		/*if(getSuperstep()==0){
			vertex.setVertexValue(new DoubleWritable(Double.parseDouble(vertex.getId().toString())));
			sendMessageToVertex(vertex.getId(), new LongWritable(1));
			sendMessageToVertex(new Text(2+""), new LongWritable(2));
			sendMessageToVertex(new Text(3+""), new LongWritable(4));
	
		}*/
		if(getSuperstep() != 0){
			DoubleWritable dW = getValueFromAggregator("Double");
			BooleanWritable bW = getValueFromAggregator("Boolean");
			LOG.info("VALUE0="+dW);
			LOG.info("VALUE1="+bW);
				
			if( getSuperstep() == 1 && vertex.getId().toString().equals("99"))
				aggregateValue("Boolean", new BooleanWritable(false));
		}
		
			
		if (getSuperstep() == 2) {
			
			/*DoubleWritable writable = vertex.getVertexValue();
			LOG.info("VERTEX VALUE = " + writable);
			int i = 0;
			for(LongWritable dw: messages){
				++i;
				LOG.info("FINAL VALUE: "+dw);
			}
				
			
			LOG.info("THERE ARE " +i + " MESSAGES");*/
			/*
			 * Will halt the computation in the third superstep.
			 */

			vertex.voteToHalt();
		}
	}

}

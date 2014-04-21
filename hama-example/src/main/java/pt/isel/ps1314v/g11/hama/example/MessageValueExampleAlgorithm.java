package pt.isel.ps1314v.g11.hama.example;

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
		
		if(getSuperstep()==0){
			sendMessageToVertex(vertex.getId(), new LongWritable(1));
			sendMessageToVertex(vertex.getId(), new LongWritable(2));
			sendMessageToVertex(vertex.getId(), new LongWritable(4));
		}

		vertex.setVertexValue(new DoubleWritable(1000));
		
		/*aggregateValue(0, new DoubleWritable(1));
		
		LOG.info("VALUE0="+getValueFromAggregator(0));
		LOG.info("VALUE1="+getValueFromAggregator(1));
		
		if(getSuperstep()==0)
			aggregateValue(1, new BooleanWritable(true));
		else if( getSuperstep() == 1 && vertex.getId().toString().equals("99"))
			aggregateValue(1, new BooleanWritable(false));
			*/
			

		if (getSuperstep() == 1) {
			
			LOG.info("VERTEX VALUE = " + vertex.getVertexValue());
			int i = 0;
			for(LongWritable dw: messages){
				++i;
				LOG.info("FINAL VALUE: "+dw);
			}
				
			
			LOG.info("THERE ARE " +i + " MESSAGES");
			/*
			 * Will halt the computation in the third superstep.
			 */

			vertex.voteToHalt();
		}
	}

}

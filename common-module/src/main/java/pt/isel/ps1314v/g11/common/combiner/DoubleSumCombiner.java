package pt.isel.ps1314v.g11.common.combiner;

import org.apache.hadoop.io.DoubleWritable;

import pt.isel.ps1314v.g11.common.graph.Combiner;
/**
 * A combiner that sums Double value messages.
 * Initial value is 0.
 *
 */
public class DoubleSumCombiner implements Combiner<DoubleWritable>{

	@Override
	public void combine(DoubleWritable originalMessage,
			DoubleWritable newMessage) {
		originalMessage.set(originalMessage.get()+newMessage.get());
		
	}

	@Override
	public DoubleWritable initialValue() {
		return new DoubleWritable(0d);
	}

}

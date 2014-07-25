package pt.isel.ps1314v.g11.common.combiner;

import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.graph.Combiner;

/**
 * A combiner that sums Long value messages.
 * Initial value is 0.
 *
 */
public class LongSumCombiner implements Combiner<LongWritable>{

	@Override
	public void combine(LongWritable originalMessage, LongWritable newMessage) {
		originalMessage.set(originalMessage.get()+newMessage.get());
	}

	@Override
	public LongWritable initialValue() {
		return new LongWritable(0l);
	}

}

package pt.isel.ps1314v.g11.common.combiner;

import org.apache.hadoop.io.IntWritable;

import pt.isel.ps1314v.g11.common.graph.Combiner;

public class IntSumCombiner implements Combiner<IntWritable>{

	@Override
	public void combine(IntWritable originalMessage, IntWritable newMessage) {
		originalMessage.set(originalMessage.get()+newMessage.get());
	}

	@Override
	public IntWritable initialValue() {
		return new IntWritable(0);
	}

}

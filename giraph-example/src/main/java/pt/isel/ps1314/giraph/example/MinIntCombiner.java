package pt.isel.ps1314.giraph.example;

import org.apache.giraph.combiner.MessageCombiner;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;

public class MinIntCombiner extends MessageCombiner<LongWritable, IntWritable>{
	@Override
	public void combine(LongWritable vertex, IntWritable originalMessage, IntWritable otherMessage) {
		originalMessage.set(Math.min(originalMessage.get(), otherMessage.get()));
	}
	@Override
	public IntWritable createInitialMessage() {
		return new IntWritable(Integer.MAX_VALUE);
	}
}

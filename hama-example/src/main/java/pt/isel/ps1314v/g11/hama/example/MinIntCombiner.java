package pt.isel.ps1314v.g11.hama.example;

import org.apache.hadoop.io.IntWritable;
import org.apache.hama.bsp.Combiner;

public class MinIntCombiner extends Combiner<IntWritable>{
	@Override
	public IntWritable combine(Iterable<IntWritable> messages) {
		IntWritable writable = new IntWritable(Integer.MAX_VALUE);
		for(IntWritable msg : messages){
			writable.set(Math.min(writable.get(), msg.get()));
		}	
		return writable;
	}
}

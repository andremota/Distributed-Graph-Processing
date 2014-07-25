package pt.isel.ps1314v.g11.common.aggregator;

import org.apache.hadoop.io.IntWritable;

/**
 * Aggregator that aggregates calculating the summing Integer values. 
 * Initial value is 0.
 */
public class IntSumAggregator extends BaseAggregator<IntWritable> {

	@Override
	public void aggregate(IntWritable value) {
		getValue().set(getValue().get()+value.get());
	}

	@Override
	public IntWritable initialValue() {
		return new IntWritable(0);
	}

}

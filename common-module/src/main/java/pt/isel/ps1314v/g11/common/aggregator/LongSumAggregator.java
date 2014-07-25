package pt.isel.ps1314v.g11.common.aggregator;

import org.apache.hadoop.io.LongWritable;

/**
 * Aggregator that aggregates calculating the summing Long values. 
 * Initial value is 0.
 */
public class LongSumAggregator extends BaseAggregator<LongWritable>{

	@Override
	public void aggregate(LongWritable value) {
		getValue().set(getValue().get()+value.get());
		
	}

	@Override
	public LongWritable initialValue() {
		return new LongWritable(0l);
	}

}

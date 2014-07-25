package pt.isel.ps1314v.g11.common.aggregator;

import org.apache.hadoop.io.DoubleWritable;

/**
 * Aggregator that aggregates calculating the max between Double values. 
 * Initial value is {@link Double#MIN_VALUE}.
 */
public class DoubleMaxAggregator extends BaseAggregator<DoubleWritable>{

	@Override
	public void aggregate(DoubleWritable value) {
		getValue().set(Math.max(value.get(), getValue().get()));
	}

	@Override
	public DoubleWritable initialValue() {
		return new DoubleWritable(Double.MIN_VALUE);
	}
}

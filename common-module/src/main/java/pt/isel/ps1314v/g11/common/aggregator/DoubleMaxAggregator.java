package pt.isel.ps1314v.g11.common.aggregator;

import org.apache.hadoop.io.DoubleWritable;

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

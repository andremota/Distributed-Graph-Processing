package pt.isel.ps1314v.g11.common.aggregator;

import org.apache.hadoop.io.DoubleWritable;

public class DoubleSumAggregator extends BaseAggregator<DoubleWritable>{
	
	@Override
	public void aggregate(DoubleWritable value) {
		getValue().set(getValue().get() + value.get());
	}

	@Override
	public DoubleWritable initialValue() {
		return new DoubleWritable(0);
	}

}

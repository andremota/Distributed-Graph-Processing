package pt.isel.ps1314v.g11.common.aggregator;

import org.apache.hadoop.io.BooleanWritable;

/**
 * Aggregator that aggregates calculating the OR function of boolean values. 
 * Initial value is false.
 */
public class BooleanOrAggregator extends BaseAggregator<BooleanWritable>{

	@Override
	public void aggregate(BooleanWritable value) {
		getValue().set(getValue().get() || value.get());
		
	}

	@Override
	public BooleanWritable initialValue() {
		return new BooleanWritable(false);
	}

}

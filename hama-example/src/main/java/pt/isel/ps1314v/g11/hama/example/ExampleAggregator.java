package pt.isel.ps1314v.g11.hama.example;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.common.graph.Aggregator;
 
public class ExampleAggregator implements Aggregator<DoubleWritable>{

	public DoubleWritable value = new DoubleWritable(0); 
	
	@Override
	public void aggregate(DoubleWritable toagrr) {
		value.set(value.get()+toagrr.get());
	}

	@Override
	public DoubleWritable getValue() {
		return value;
	}

	@Override
	public DoubleWritable initialValue() {
		return new DoubleWritable(0);
	}

}

package pt.isel.ps1314.giraph.example;

import org.apache.giraph.aggregators.BasicAggregator;
import org.apache.hadoop.io.DoubleWritable;

public class ExampleAggregator extends BasicAggregator<DoubleWritable>{

	public void aggregate(DoubleWritable val) {	
		getAggregatedValue().set(val.get()+getAggregatedValue().get());
	}

	public DoubleWritable createInitialValue() {
		return new DoubleWritable(0);
	}

}

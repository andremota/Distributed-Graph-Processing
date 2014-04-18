package pt.isel.ps1314v.g11.hama.example;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hama.graph.Aggregator;

public class DoubleAggregator implements Aggregator<DoubleWritable>{

	private DoubleWritable value = new DoubleWritable(0d);
	@Override
	public void aggregate(DoubleWritable val) {
		if(val!=null)
			value.set(value.get()+val.get());
		
	}

	@Override
	public DoubleWritable getValue() {
		// TODO Auto-generated method stub
		return value;
	}

}

package pt.isel.ps1314v.g11.hama.example;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hama.graph.Aggregator;

public class BooleanAggregator implements Aggregator<BooleanWritable>{

	
	private BooleanWritable value = new BooleanWritable(true);
	@Override
	public void aggregate(BooleanWritable val) {
		if(val!=null)
			value.set(value.get() && val.get());
	}


	@Override
	public BooleanWritable getValue() {
		// TODO Auto-generated method stub
		return value;
	}

}

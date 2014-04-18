package pt.isel.ps1314v.g11.hama.graph;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Writable;

import pt.isel.ps1314v.g11.common.graph.Aggregator;

class DummyAggregator<V extends Writable> implements Aggregator<V>{


	@Override
	public V initialValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void aggregate(V value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public V getValue() {
		// TODO Auto-generated method stub
		return (V) new DoubleWritable();
	}

}

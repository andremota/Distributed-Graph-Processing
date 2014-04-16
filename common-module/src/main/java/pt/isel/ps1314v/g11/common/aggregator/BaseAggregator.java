package pt.isel.ps1314v.g11.common.aggregator;

import org.apache.hadoop.io.Writable;

import pt.isel.ps1314v.g11.common.graph.Aggregator;

/** Base aggregator implementation **/
public abstract class BaseAggregator<V extends Writable> implements Aggregator<V>{

	private V value;

	@Override
	public V getValue() {
		return value;
	}


}

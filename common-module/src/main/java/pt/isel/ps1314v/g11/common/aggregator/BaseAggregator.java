package pt.isel.ps1314v.g11.common.aggregator;

import org.apache.hadoop.io.Writable;

import pt.isel.ps1314v.g11.common.graph.Aggregator;

/** 
 * 	Base aggregator implementation.
 *  Has an element which initial value is as set by the {@link Aggregator#initialValue()} method with the method 
 *  {@link Aggregator#getValue()} allowing access to it.
 *  
 *  @param <A> The type of the value to be aggregated
 *  **/
public abstract class BaseAggregator<A extends Writable> implements Aggregator<A>{

	private A value;

	
	public BaseAggregator() {
		value = initialValue();
	}
	
	@Override
	public A getValue() {
		return value;
	}
	
	@Override
	public void setValue(A value) {
		this.value = value;
		
	}


}

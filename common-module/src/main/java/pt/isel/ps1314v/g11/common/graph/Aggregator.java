package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;

public interface Aggregator<A extends Writable> {
	
	public static final String AGGREGATOR_CLASS = "pt.isel.ps1314v.g11.aggregatorclass";
	public static final String AGGREGATOR_KEYS = "pt.isel.ps1314v.g11.aggregatorkeys";
	
	/**Aggregates the specified value.
	 * @param value - the value to be aggregated by the aggregator.
	 */
	void aggregate(A value);
	
	/**
	 * @return the aggregated value.
	 */
	A getValue();
	
	/**
	 * @return the initial value of an aggregator to be used by some BSP platforms.
	 */ 
	A initialValue();
	
	/** Sets a specific value - Called by mappers
	 * @param value - the value to be aggregated by the aggregator
	 */
	 
	void setValue(A value);
}
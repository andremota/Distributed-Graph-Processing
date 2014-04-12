package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public interface Computation<V extends WritableComparable<V>,E extends Writable, M extends Writable> {
	/**
	 * @param vertex - the vertex 
	 * @param messages - the messages received in the current superstep for the vertex.
	 */
	void Compute(Vertex<V,E,M> vertex, Iterable<M> messages);
	
	/**
	 * @return
	 */
	long getSuperstep();
}

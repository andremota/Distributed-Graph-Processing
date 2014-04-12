package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 * A single edge with a target and value
 *
 *
 * @param <I> Vertex Id
 * @param <E> Edge value
 */
public interface Edge<I extends WritableComparable<I>, E extends Writable>{

	/**
	 * 
	 * @return Id of the edge's target Vertex
	 */
	I getTargetVertexId();
	
	/**
	 * 
	 * @return The edge value
	 */
	E getValue();
}

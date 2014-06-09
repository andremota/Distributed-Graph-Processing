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
public class Edge<I extends WritableComparable, E extends Writable>{

	
	private I targetVertexId;
	private E value;
	
	public Edge(){}
	
	public Edge(I targetVertexId, E value){
		this.targetVertexId = targetVertexId;
		this.value = value;
	}
	
	/**
	 * 
	 * @return Id of the edge's target Vertex
	 */
	public I getTargetVertexId() {
		return targetVertexId;
	}
	public void setTargetVertexId(I targetVertexId) {
		this.targetVertexId = targetVertexId;
	}
	
	/**
	 * 
	 * @return The edge value
	 */
	public E getValue() {
		return value;
	}
	public void setValue(E value) {
		this.value = value;
	}
}

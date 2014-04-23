package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 * Represents a single vertex.
 *
 * @param <I> Vertex Id
 * @param <V> Vertex Value
 * @param <E> Edge value
 */
public interface Vertex<I extends WritableComparable<?>,V extends Writable, E extends Writable> {
	
	/**
	 * @return the out-edges of this vertex.
	 */
	Iterable<Edge<I,E>> getVertexEdges();
	
	/**
	 * @return the number of out-edges.
	 */
	int getNumEdges();
	
	/** Removes all the edges to the target vertex. 
	 * @param targetVertexId - the id of the target vertex.
	 */
	void removeEdges(I targetVertexId);
	
	/** 
	 * @param targetVertexId - the edge to be hadded to this vertex.
	 */
	void addEdge(Edge<I, E> edge);
	
	/**
	 * @return the current value of the vertex.
	 */
	V getVertexValue();
	
	/** 
	 * @param value - the value to be used to update the vertex value.
	 */
	void setVertexValue(V value);
	
	/**
	 * @return the id of the vertex.
	 */
	I getId();
	
	void voteToHalt();
}

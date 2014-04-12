package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 * Represents a single vertex.
 *
 * @param <I> Vertex Id
 * @param <E> Edge value
 * @param <M> Vertex Value
 */
public interface Vertex<I extends WritableComparable<I>,E extends Writable, M extends Writable> {
	
	/**
	 * @return the outedges of this vertex.
	 */
	Edge<I,E> getEdges();
	
	/**
	 * @return the number of outedges.
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
	M getValue();
	
	/** 
	 * @param value - the value to be used to update the vertex value.
	 */
	void setValue(M value);
	
	/**
	 * @return the id of the vertex.
	 */
	I getId();
}

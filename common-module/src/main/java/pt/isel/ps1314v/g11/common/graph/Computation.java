package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 * Represents the implementation that which will be called for each vertex in each superstep.
 * 
 * @param <I> Vertex id
 * @param <E> Edge Value
 * @param <M> Vertex value and type of messages
 */
public interface Computation<I extends WritableComparable<I>,E extends Writable, M extends Writable> {
	
	/**
	 * @return the current superstep.
	 */
	long getSuperstep();
	
	/**
	 * Sends message to a vertex.
	 * @param targetVertexId - the vertex that will receive the message.
	 * @param message - the message to be received in the next superstep by the targetVertexId.
	 */
	void sendMessage(I targetVertexId, M message);
	
	/**
	 * Sends a message to all neighbors of a vertex.
	 * @param vertex - the vertex wich neighboors will receive the message.
	 * @param message - the message to be received in the next superstep by the vertex neighbors.
	 */
	void sendMessageToNeighbors(Vertex<I, E, M> vertex, M message);
	
	/**
	 * Gives a value to an aggregator previously registered in the given index.
	 * @param index - the index of the aggregator.
	 * @param value - the value to be given to the aggregator.
	 */
	void aggregate(int index, M value);
	
	/**
	 * Returns the aggregated value to the moment by an aggregator previously registered in the given index.
	 * @param index - the index of the aggregator
	 * @return the aggregated value.
	 */
	M getAggregatedValue(int index);
	
}

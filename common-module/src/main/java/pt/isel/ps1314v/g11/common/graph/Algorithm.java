package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 * Any algorithm should extend this class and implement the compute method.
 *
 * @param <I> Vertex id
 * @param <E> Edge Value
 * @param <M> Vertex value and messages
 */
public abstract class Algorithm<I extends WritableComparable<I>,E extends Writable, M extends Writable> 
								implements Computation<I,E,M>{
	
	private Computation<I,E,M> computation;
	
	/**
	 * @param vertex - the vertex 
	 * @param messages - the messages received in the current superstep for the vertex.
	 */
	public abstract void compute(Vertex<I, E, M> vertex, Iterable<M> messages);

	@Override
	public long getSuperstep() {
		return computation.getSuperstep();
	}

	@Override
	public void sendMessage(I targetVertexId, M message) {
		computation.sendMessage(targetVertexId, message);
	}

	@Override
	public void sendMessageToNeighbors(Vertex<I, E, M> vertex, M message) {
		computation.sendMessageToNeighbors(vertex, message);
	}

	@Override
	public void aggregate(int index, M value) {
		computation.aggregate(index, value);
	}

	@Override
	public M getAggregatedValue(int index) {
		return computation.getAggregatedValue(index);
	}
	
	/**
	 * This method sets the platform computation context therefore should only be called by the platform.
	 * @param computation - the platform computation.
	 */
	public void setPlatformComputation(Computation<I, E, M> computation){
		this.computation = computation;
	}
	
}

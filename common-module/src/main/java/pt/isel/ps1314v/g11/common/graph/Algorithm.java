package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 * Any algorithm should extend this class and implement the compute method.
 *
 * @param <I> Vertex id
 * @param <V> Vertex value and messages
 * @param <E> Edge Value
 */
public abstract class Algorithm<I extends WritableComparable<I>,V extends Writable, E extends Writable> 
								implements Computation<I,V,E>{
	
	public static final String ALGORITHM_CLASS = "pt.isel.ps1314v.g11.common.graph.Computation";
	
	private Computation<I,V,E> computation;
	
	/**
	 * @param vertex - the vertex 
	 * @param messages - the messages received in the current superstep for the vertex.
	 */
	public abstract void compute(Vertex<I, V, E> vertex, Iterable<V> messages);

	@Override
	public long getSuperstep() {
		return computation.getSuperstep();
	}

	@Override
	public void sendMessage(I targetVertexId, V message) {
		computation.sendMessage(targetVertexId, message);
	}

	@Override
	public void sendMessageToNeighbors(Vertex<I, V, E> vertex, V message) {
		computation.sendMessageToNeighbors(vertex, message);
	}

	@Override
	public void aggregate(int index, V value) {
		computation.aggregate(index, value);
	}

	@Override
	public V getAggregatedValue(int index) {
		return computation.getAggregatedValue(index);
	}
	
	/**
	 * This method sets the platform computation context therefore should only be called by the platform.
	 * @param computation - the platform computation.
	 */
	public void setPlatformComputation(Computation<I, V, E> computation){
		this.computation = computation;
	}
	
}

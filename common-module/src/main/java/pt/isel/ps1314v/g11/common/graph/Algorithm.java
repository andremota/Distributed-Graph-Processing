package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 * This class must be super to any algorithm implementation, since this class
 * is the one that map all the needed functionalities(for an algorithm) of the platforms.
 * 
 * @param <I> Vertex Id
 * @param <V> Vertex Value
 * @param <E> Edge Value
 * @param <M> Message Value
 */
public abstract class Algorithm<I extends WritableComparable<?>, V extends Writable, E extends Writable, M extends Writable>
		implements Computation<I, V, E, M> {

	private Computation<I, V, E, M> computation;
	public static final String ALGORITHM_CLASS = "pt.isel.ps1314v.g11.common.graph.Computation";

	/**
	 * This method is called by the specific platform for each superstep.
	 * 
	 * @param vertex
	 *            - the vertex
	 * @param messages
	 *            - the messages received in the current superstep for the
	 *            vertex.
	 */
	public abstract void compute(Vertex<I, V, E> vertex, Iterable<M> messages);

	@Override
	public long getTotalVertices() {
		return computation.getTotalVertices();
	}
	
	@Override
	public long getSuperstep() {
		return computation.getSuperstep();
	}

	@Override
	public void sendMessageToVertex(I targetVertexId, M message) {
		computation.sendMessageToVertex(targetVertexId, message);
	}

	@Override
	public void sendMessageToNeighbors(Vertex<I, V, E> vertex, M message) {
		computation.sendMessageToNeighbors(vertex, message);
	}

	@Override
	public <A extends Writable> void aggregateValue(String index, A value) {
		computation.aggregateValue(index, value);
	}

	@Override
	public <A extends Writable> A getValueFromAggregator(String index) {
		return computation.getValueFromAggregator(index);
	}

	/**
	 * This method sets the platform computation context therefore should only
	 * be called by the platform.
	 * 
	 * @param computation
	 *            - the platform computation.
	 */
	public void setPlatformComputation(Computation<I, V, E, M> computation) {
		this.computation = computation;
	}
}

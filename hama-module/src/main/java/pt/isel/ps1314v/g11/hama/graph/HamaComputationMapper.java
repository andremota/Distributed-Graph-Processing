package pt.isel.ps1314v.g11.hama.graph;

import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hama.HamaConfiguration;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Computation;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class HamaComputationMapper<I extends WritableComparable<I>, V extends Writable, E extends Writable>
		extends org.apache.hama.graph.Vertex<I, E, V> implements
		Computation<I, V, E>, Vertex<I, V, E> {

	private Algorithm<I, V, E> algorithm;

	@SuppressWarnings("unchecked")
	@Override
	public void setup(HamaConfiguration conf) {
		super.setup(conf);

		algorithm = (Algorithm<I, V, E>) ReflectionUtils
				.newInstance(conf.getClass(Algorithm.ALGORITHM_CLASS,
						Algorithm.class), conf);

		algorithm.setPlatformComputation(this);
	}

	@Override
	public long getSuperstep() {
		return super.getSuperstepCount();
	}

	@Override
	public void sendMessage(I targetVertexId, V message) {
		try {
			super.sendMessage(targetVertexId, message);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendMessageToNeighbors(Vertex<I, V, E> vertex, V message) {
		for (Edge<I, E> edge : vertex.getVertexEdges())
			sendMessage(edge.getTargetVertexId(), message);
	}

	@Override
	public void aggregate(int index, V value) {
		try {
			super.aggregate(index, value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public V getAggregatedValue(int index) {
		return super.getAggregatedValue(index);
	}

	@Override
	public void compute(Iterable<V> messages) throws IOException {
		algorithm.compute(this, messages);
	}

	@Override
	public Iterable<Edge<I, E>> getVertexEdges() {
		// return getEdges(); Needs edge mapper!
		return null;
	}

	@Override
	public int getNumEdges() {
		return super.getEdges().size();
	}

	@Override
	public void removeEdges(I targetVertexId) {
		// TODO - needs edge mapper!
	}

	@Override
	public void addEdge(Edge<I, E> edge) {
		// TODO - super.addEdge(edge); Needs Edge mapper!
	}

	@Override
	public V getVertexValue() {
		return super.getValue();
	}

	@Override
	public void setVertexValue(V value) {
		super.setValue(value);
	}

	@Override
	public I getId() {
		return super.getVertexID();
	}

}

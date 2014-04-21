package pt.isel.ps1314v.g11.hama.graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hama.HamaConfiguration;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.BasicAlgorithm;
import pt.isel.ps1314v.g11.common.graph.Computation;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;
import pt.isel.ps1314v.g11.hama.util.IteratorsUtil;
import pt.isel.ps1314v.g11.hama.util.IteratorsUtil.KeyCompare;

/**
 * This class maps a {@link org.apache.hama.graph.Vertex} to a {@link Computation} and a {@link Vertex}.
 *
 * @param <I> Vertex Id
 * @param <V> Vertex Value and Message value
 * @param <E> Edge Value
 */
public class HamaComputationMapper<I extends WritableComparable<I>, V extends Writable, E extends Writable, M extends Writable>
		extends org.apache.hama.graph.Vertex<I, E, V> implements
		Computation<I, V, E, M>, Vertex<I, V, E> {

	private HamaEdgeKeyElemCompare hamaEdgeComparator =  new HamaEdgeKeyElemCompare();
	private CommonEdgeKeyElemCompare commonEdgeComparator = new CommonEdgeKeyElemCompare();
	
	//Replicated edges mapping to common edges
	private List<Edge<I, E>> commonEdges = new ArrayList<>();
	
	private Algorithm<I, V, E,M> algorithm;
	private boolean setupCalled = false;

	@SuppressWarnings("unchecked")
	@Override
	public void setup(HamaConfiguration conf) {
		super.setup(conf);
	
		algorithm = (Algorithm<I, V, E,M>) ReflectionUtils
				.newInstance(conf.getClass(Algorithm.ALGORITHM_CLASS,
						BasicAlgorithm.class), conf);
	
		algorithm.setPlatformComputation(this);
		
		setupCalled = true;
	}

	@Override
	public long getSuperstep() {
		return super.getSuperstepCount();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sendMessageToVertex(I targetVertexId, M message) {
		try {
			super.sendMessage(targetVertexId, (V)message);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendMessageToNeighbors(Vertex<I, V, E> vertex, M message) {
		for (Edge<I, E> edge : vertex.getVertexEdges())
			sendMessageToVertex(edge.getTargetVertexId(), message);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <A extends Writable> void aggregateValue(int index, A value) {
		try {
			super.aggregate(index, (V)value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A extends Writable> A getValueFromAggregator(int index) {
		return (A)super.getAggregatedValue(index);
	}

	@Override
	public void compute(Iterable<V> messages) throws IOException {
		if(!setupCalled) setup(getConf());
		
		if(algorithm == null){
			throw new RuntimeException("Algorithm is not set.");
		}
		algorithm.compute(this, (Iterable<M>) IteratorsUtil.cast(messages));
	}

	@Override
	public Iterable<Edge<I, E>> getVertexEdges() {
		return commonEdges;
	}

	@Override
	public int getNumEdges() {
		return super.getEdges().size();
	}

	@Override
	public void removeEdges(I targetVertexId) {
		/*
		 * Removes the edges from the Hama vertex edges list and the replicated common edges.
		 */
		IteratorsUtil.removeKFromIterator(targetVertexId, super.getEdges().iterator(), hamaEdgeComparator);
		IteratorsUtil.removeKFromIterator(targetVertexId, commonEdges.iterator(), commonEdgeComparator);
	}
	
	@Override
	public void addEdge(org.apache.hama.graph.Edge<I, E> edge) {
		super.addEdge(edge);
		commonEdges.add(new Edge<I,E>(edge.getDestinationVertexID(), edge.getValue()));
	}

	@Override
	public void addEdge(Edge<I, E> edge) {
		super.addEdge(new org.apache.hama.graph.Edge<I, E>(edge.getTargetVertexId(), edge.getValue()));
		commonEdges.add(edge);
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
	
	/*
	 * To compare ids and hama edges to be removed from the edges list.
	 */
	private class HamaEdgeKeyElemCompare implements KeyCompare<I, org.apache.hama.graph.Edge<I, E>>{
		@Override
		public boolean compareKeyToElem(I key, org.apache.hama.graph.Edge<I,E>  elem) {
			return elem.getDestinationVertexID().equals(key);
		}
		
	}

	/*
	 * To compare ids and common edges to be removed from the edges list.
	 */
	private class CommonEdgeKeyElemCompare implements KeyCompare<I, Edge<I, E>>{
		@Override
		public boolean compareKeyToElem(I key, Edge<I,E>  elem) {
			return elem.getTargetVertexId().equals(key);
		}
		
	}


}

package pt.isel.ps1314v.g11.hama.graph;

import java.io.IOException;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Computation;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class HamaComputationMapper<I extends WritableComparable<I>, V extends Writable, E extends Writable>
		extends org.apache.hama.graph.Vertex<I, E, V> implements
		Computation<I, E, V>, Vertex<I, E, V>, Configurable {

	private Algorithm<I, E, V> algorithm;
	
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
	public void sendMessageToNeighbors(Vertex<I, E, V> vertex, V message) {
		for(Edge<I, E> edge : vertex.getVertexEdges())
			sendMessage(edge.getTargetVertexId(), message);
	}

	@Override
	public void aggregate(int index, V value) {

	}

	@Override
	public V getAggregatedValue(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void compute(Iterable<V> messages) throws IOException {
		algorithm.compute(this, messages);
	}

	@Override
	public void setConf(Configuration arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterable<Edge<I, E>> getVertexEdges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumEdges() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeEdges(I targetVertexId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEdge(Edge<I, E> edge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public V getVertexValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVertexValue(V value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public I getId() {
		// TODO Auto-generated method stub
		return null;
	}

}

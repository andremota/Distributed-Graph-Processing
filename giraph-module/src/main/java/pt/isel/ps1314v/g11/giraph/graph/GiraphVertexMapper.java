package pt.isel.ps1314v.g11.giraph.graph;

import java.util.ArrayList;

import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import pt.isel.ps1314v.g11.common.graph.Edge;

public class GiraphVertexMapper<I extends WritableComparable<I>,V extends Writable,E extends Writable> 
						implements pt.isel.ps1314v.g11.common.graph.Vertex<I, V, E>{

	private Vertex<I, V, E> vertex;
	
	public GiraphVertexMapper(Vertex<I, V, E> vertex){
		this.vertex = vertex;
	}
	
	@Override
	public void addEdge(Edge<I, E> edge) {
		vertex.addEdge(new GiraphEdgeMapper<I,E>(edge));
	}
	
	@Override
	public Iterable<Edge<I, E>> getVertexEdges() {
		ArrayList<Edge<I,E>> list = new ArrayList<Edge<I,E>>();
		
			
		for(org.apache.giraph.edge.Edge<I, E> edge: vertex.getEdges()){
			GiraphEdgeMapper<I, E> common = new GiraphEdgeMapper<I,E>(edge);
			list.add((new Edge<I, E>(common.getTargetVertexId(), common.getValue())));
		}
		
		return list;
	}

	@Override
	public void setVertexValue(V value) {
		vertex.setValue(value);
	}

	@Override
	public V getVertexValue() {
		return vertex.getValue();
	}

	@Override
	public int getNumEdges() {
		return vertex.getNumEdges();
	}

	@Override
	public void removeEdges(I targetVertexId) {
		vertex.removeEdges(targetVertexId);
	}

	@Override
	public I getId() {
		return vertex.getId();
	}

	@Override
	public void voteToHalt() {
		vertex.voteToHalt();
		
	}

}

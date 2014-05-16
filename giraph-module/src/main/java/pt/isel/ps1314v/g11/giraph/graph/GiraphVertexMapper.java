package pt.isel.ps1314v.g11.giraph.graph;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.giraph.edge.EdgeFactory;
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
		return new GiraphEdgeMapperIterable(vertex.getEdges());
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
	
	private class GiraphEdgeMapperIterable implements Iterable<Edge<I,E>>{

		private final Iterable<org.apache.giraph.edge.Edge<I, E>> iterable;
		
		public GiraphEdgeMapperIterable(Iterable<org.apache.giraph.edge.Edge<I, E>> iterable2) {
			this.iterable = iterable2;
		}
		
		
		@Override
		public Iterator<Edge<I, E>> iterator() {
			// TODO Auto-generated method stub
			return new Iterator<Edge<I,E>>() {
				final Iterator<org.apache.giraph.edge.Edge<I, E>> iterator = iterable.iterator();
				
				@Override
				public boolean hasNext() {
					return iterator.hasNext();
				}

				@Override
				public GiraphEdgeMapper<I, E> next() {
					// TODO Auto-generated method stub
					return new GiraphEdgeMapper<I,E>(iterator.next());
				}

				@Override
				public void remove() {
					iterator.remove();
				}
			};
		}

	}

	@Override
	public void setEdges(Iterable<Edge<I, E>> edges) {
		if(edges!=null){
			ArrayList<org.apache.giraph.edge.Edge<I, E>> newEdges = new ArrayList<>();
			Edge<I, E> edge = null;
			for(Iterator<Edge<I,E>> it = edges.iterator(); it.hasNext();){
				edge = it.next();
				newEdges.add(EdgeFactory.create(edge.getTargetVertexId(),edge.getValue()));
			}
				
			vertex.setEdges(newEdges);
		} else
			vertex.setEdges(null);
		

		
	}

}

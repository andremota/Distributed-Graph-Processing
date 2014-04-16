package pt.isel.ps1314v.g11.giraph.graph;

import org.apache.giraph.edge.EdgeFactory;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import pt.isel.ps1314v.g11.common.graph.Edge;


public class GiraphEdgeMapper<I extends WritableComparable<?>,E extends Writable> extends Edge<I, E> implements org.apache.giraph.edge.Edge<I, E>{

	
	private org.apache.giraph.edge.Edge<I, E> edge;
	
	public GiraphEdgeMapper(I id, E value){
		this.edge = EdgeFactory.create(id, value);
	}
	
	public GiraphEdgeMapper(Edge<I,E> edge){
		this.edge = EdgeFactory.create(edge.getTargetVertexId(),edge.getValue());
	}
	
	public GiraphEdgeMapper(org.apache.giraph.edge.Edge<I, E> edge) {
		this.edge = edge;
	}
	
	@Override
	public I getTargetVertexId() {
		
		return edge.getTargetVertexId();
	}

	@Override
	public E getValue() {
		return edge.getValue();
	}

}

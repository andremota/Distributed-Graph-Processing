package pt.isel.ps1314v.g11.giraph.combiner;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import pt.isel.ps1314v.g11.common.graph.Edge;

public class GiraphEdgeMapper<I extends WritableComparable<I>,E extends Writable> implements Edge<I, E>, org.apache.giraph.edge.Edge<I, E>{

	
	private org.apache.giraph.edge.Edge<I, E> edge;
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

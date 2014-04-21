package pt.isel.ps1314v.g11.giraph.graph;

import org.apache.giraph.edge.EdgeFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

import pt.isel.ps1314v.g11.common.graph.Edge;


public class GiraphEdgeMapper<I extends WritableComparable<?>,E extends Writable> extends Edge<I, E> implements org.apache.giraph.edge.Edge<I, E>{

	
	private org.apache.giraph.edge.Edge<I, E> edge;
	
	public GiraphEdgeMapper(I id, E value){
		this.edge = EdgeFactory.create(id, value);
		this.setTargetVertexId(edge.getTargetVertexId());
		this.setValue(edge.getValue());
	}
	
	public GiraphEdgeMapper(Edge<I,E> edge){
		this.edge = EdgeFactory.create(edge.getTargetVertexId(),edge.getValue());
		this.setTargetVertexId(edge.getTargetVertexId());
		this.setValue(edge.getValue());
	}
	
	public GiraphEdgeMapper(org.apache.giraph.edge.Edge<I, E> edge) {
		this.edge = edge;
		this.setTargetVertexId(edge.getTargetVertexId());
		this.setValue(edge.getValue());
	}
	
	public GiraphEdgeMapper<I, E> cloneEdge() {
		I targetId = null;
		E value = null;

		Configuration conf =  new Configuration();
		targetId = WritableUtils.clone(edge.getTargetVertexId(), conf);
		value = WritableUtils.clone(edge.getValue(), conf);

		return new GiraphEdgeMapper<>(EdgeFactory.create(targetId, value));
	}
	
	
	/*@Override
	public I getTargetVertexId() {
		
		return edge.getTargetVertexId();
	}

	@Override
	public E getValue() {
		return edge.getValue();
	}*/

}

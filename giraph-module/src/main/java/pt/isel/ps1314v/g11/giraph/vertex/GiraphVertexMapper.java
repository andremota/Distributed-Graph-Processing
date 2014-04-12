package pt.isel.ps1314v.g11.giraph.vertex;

import java.util.ArrayList;

import org.apache.giraph.graph.DefaultVertex;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;
import pt.isel.ps1314v.g11.giraph.combiner.GiraphEdgeMapper;

public class GiraphVertexMapper<I extends WritableComparable<I>,M extends Writable,E extends Writable> 
						extends DefaultVertex<I, M, E>
						implements Vertex<I, E, M>{

	@Override
	public void addEdge(Edge<I, E> edge) {
		super.addEdge(new GiraphEdgeMapper<I,E>(edge));
	}
	
	@Override
	public Iterable<Edge<I, E>> getVertexEdges() {
		ArrayList<Edge<I,E>> list = new ArrayList<Edge<I,E>>();
		
		for(org.apache.giraph.edge.Edge<I, E> edge: super.getEdges()){
			list.add((GiraphEdgeMapper<I, E>)edge);
		}
		
		return list;
	}

	@Override
	public void setVertexValue(M value) {
		super.setValue(value);
	}

	@Override
	public M getVertexValue() {
		return super.getValue();
	}

}

package pt.isel.ps1314v.g11.giraph.vertex;

import org.apache.giraph.graph.DefaultVertex;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class GiraphVertexMapper<I extends WritableComparable<I>,M extends Writable,E extends Writable> 
						extends DefaultVertex<I, M, E>
						implements Vertex<I, E, M>{

	@Override
	public void addEdge(Edge<I, E> edge) {
		//TODO - Map common edge to giraph edge and add it.
	}

	@Override
	public Iterable<Edge<I, E>> getOutEdges() {
		//TODO - Map common edge to giraph so that we can return them.
		return null;
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

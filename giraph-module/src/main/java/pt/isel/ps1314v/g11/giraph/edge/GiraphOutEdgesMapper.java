package pt.isel.ps1314v.g11.giraph.edge;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.edge.ConfigurableOutEdges;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.edge.OutEdges;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.giraph.combiner.GiraphEdgeMapper;

public class GiraphOutEdgesMapper<I extends WritableComparable<I>, E extends Writable> extends ConfigurableOutEdges<I, E> {

	private static final String OUTEDGES = "pt.isel.ps1314v.g11.outedges";
	
	private OutEdges<I, E> outEdges;
	@SuppressWarnings("unchecked")
	public GiraphOutEdgesMapper() {
		
		ImmutableClassesGiraphConfiguration<I, Writable, E> conf = getConf();
		outEdges = (OutEdges<I, E>) ReflectionUtils.newInstance(conf.getClass(OUTEDGES, OutEdges.class), conf);
	}
	
	@Override
	public void initialize(Iterable<Edge<I, E>> edges) {
		ArrayList<Edge<I, E>> newEdges = new ArrayList<Edge<I, E>>();
		
		for(Edge<I, E> edge: edges)
			newEdges.add(new GiraphEdgeMapper<I,E>(edge));
		
		outEdges.initialize(newEdges);		
	}

	@Override
	public void initialize(int capacity) {
		outEdges.initialize(capacity);
	}

	@Override
	public void initialize() {
		outEdges.initialize();
		
	}

	@Override
	public void add(Edge<I, E> edge) {
		
		outEdges.add(new GiraphEdgeMapper<I,E>(edge));
		
	}

	@Override
	public void remove(I targetVertexId) {
		outEdges.remove(targetVertexId);
		
	}

	@Override
	public int size() {
		return outEdges.size();
	}


	@Override
	public Iterator<Edge<I, E>> iterator() {
		return outEdges.iterator();
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		outEdges.readFields(arg0);
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		outEdges.write(arg0);
		
	}

}

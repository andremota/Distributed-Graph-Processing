package pt.isel.ps1314v.g11.giraph.graph;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.edge.ConfigurableOutEdges;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.edge.OutEdges;
import org.apache.giraph.utils.ReflectionUtils;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class GiraphOutEdgesMapper<I extends WritableComparable<?>, E extends Writable> extends ConfigurableOutEdges<I, E> {

	public static final String OUTEDGES = "pt.isel.ps1314v.g11.outedges";
	
	private OutEdges<I, E> outEdges;	
	
	@SuppressWarnings("unchecked")
	@Override
	public void setConf(ImmutableClassesGiraphConfiguration<I,Writable,E> conf) {
		super.setConf(conf);
		outEdges = (OutEdges<I, E>) ReflectionUtils.newInstance(conf.getClass(OUTEDGES, OutEdges.class), conf);
		
	};
	
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
		if(!(edge instanceof GiraphEdgeMapper<?, ?>))
			edge = new GiraphEdgeMapper<I,E>(edge);
		outEdges.add(edge);
		
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
		//return new GiraphOutEdgeMapperIterator(outEdges.iterator());
				
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		outEdges.readFields(arg0);
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		outEdges.write(arg0);
		
	}
	
	/*private class GiraphOutEdgeMapperIterator implements Iterator<Edge<I,E>>{

		Iterator<Edge<I,E>> iterator;
		
		public GiraphOutEdgeMapperIterator(Iterator<Edge<I,E>> iterator) {
			this.iterator = iterator;
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public GiraphEdgeMapper<I, E> next() {
			// TODO Auto-generated method stub
			return new GiraphEdgeMapper<>(iterator.next());
		}

		@Override
		public void remove() {
			iterator.remove();
		}
		
	}*/

}

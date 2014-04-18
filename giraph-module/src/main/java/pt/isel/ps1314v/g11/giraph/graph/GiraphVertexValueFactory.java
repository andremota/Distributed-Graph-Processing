package pt.isel.ps1314v.g11.giraph.graph;

import org.apache.giraph.conf.GiraphConstants;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.factories.VertexValueFactory;
import org.apache.giraph.utils.WritableUtils;
import org.apache.hadoop.io.Writable;

public class GiraphVertexValueFactory<V extends Writable> implements VertexValueFactory<V>{

	
	private Class<V> vertexValueClass;
	@SuppressWarnings("rawtypes")
	@Override
	public void initialize(ImmutableClassesGiraphConfiguration conf) {
		GiraphConstants.VERTEX_VALUE_CLASS.get(conf);
		
	}

	@Override
	public V newInstance() {
		return WritableUtils.createWritable(vertexValueClass);
	}

	@Override
	public Class<V> getValueClass() {
		return vertexValueClass;
	}

}

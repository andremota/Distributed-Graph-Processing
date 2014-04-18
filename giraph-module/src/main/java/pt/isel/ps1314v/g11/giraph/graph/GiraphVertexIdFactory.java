package pt.isel.ps1314v.g11.giraph.graph;

import org.apache.giraph.conf.GiraphConstants;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.factories.VertexIdFactory;
import org.apache.giraph.utils.WritableUtils;
import org.apache.hadoop.io.WritableComparable;

public class GiraphVertexIdFactory<I extends WritableComparable<?>> implements VertexIdFactory<I>{

	private Class<I> vertexIdClass;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void initialize(ImmutableClassesGiraphConfiguration conf) {
		GiraphConstants.VERTEX_ID_CLASS.get(conf);
		
	}

	@Override
	public I newInstance() {
		return WritableUtils.createWritable(vertexIdClass);
	}

	@Override
	public Class<I> getValueClass() {
		return vertexIdClass;
	}

}

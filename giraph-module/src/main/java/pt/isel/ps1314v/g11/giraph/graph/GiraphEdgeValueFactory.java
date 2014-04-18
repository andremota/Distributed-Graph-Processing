package pt.isel.ps1314v.g11.giraph.graph;

import org.apache.giraph.conf.GiraphConstants;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.factories.EdgeValueFactory;
import org.apache.giraph.utils.WritableUtils;
import org.apache.hadoop.io.Writable;

public class GiraphEdgeValueFactory<E extends Writable> implements EdgeValueFactory<E>{

	private Class<E> edgeValueClass;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(ImmutableClassesGiraphConfiguration conf) {
		edgeValueClass = (Class<E>) GiraphConstants.EDGE_VALUE_CLASS.get(conf);
		
	}

	@Override
	public E newInstance() {
		return WritableUtils.createWritable(edgeValueClass);
	}

	@Override
	public Class<E> getValueClass() {
		return edgeValueClass;
	}

}

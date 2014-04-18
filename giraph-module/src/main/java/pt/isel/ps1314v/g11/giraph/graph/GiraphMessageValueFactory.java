package pt.isel.ps1314v.g11.giraph.graph;

import org.apache.giraph.conf.GiraphConstants;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.factories.AbstractMessageValueFactory;
import org.apache.hadoop.io.Writable;

public class GiraphMessageValueFactory<M extends Writable> extends AbstractMessageValueFactory<M>{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Class<M> extractMessageValueClass(
			ImmutableClassesGiraphConfiguration conf) {
		return (Class<M>) GiraphConstants.INCOMING_MESSAGE_VALUE_CLASS.get(conf);
	}

}

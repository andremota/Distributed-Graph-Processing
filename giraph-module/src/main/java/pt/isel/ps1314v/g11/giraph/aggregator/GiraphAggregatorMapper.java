package pt.isel.ps1314v.g11.giraph.aggregator;

import org.apache.commons.lang.NotImplementedException;
import org.apache.giraph.conf.ImmutableClassesGiraphConfigurable;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.giraph.utils.Variables;

@SuppressWarnings("rawtypes")
public class GiraphAggregatorMapper<V extends Writable> implements
		org.apache.giraph.aggregators.Aggregator<V>,
		ImmutableClassesGiraphConfigurable<WritableComparable, V, Writable> {

	private ImmutableClassesGiraphConfiguration<WritableComparable, V, Writable> conf;

	private pt.isel.ps1314v.g11.common.graph.Aggregator<V> aggregator;

	@Override
	public void aggregate(V value) {
		if (aggregator == null)
			ReflectionUtils.newInstance(conf.getClass(
					Variables.AGGREGATOR_CLASS,
					pt.isel.ps1314v.g11.common.graph.Aggregator.class), conf);

		aggregator.aggregate(value);

	}

	@Override
	public V createInitialValue() {
		return aggregator.initialValue();
	}

	@Override
	public V getAggregatedValue() {
		return aggregator.getValue();
	}

	@Override
	public void setAggregatedValue(V value) {
		throw new NotImplementedException(
				"The method setAggregatedValue is not supported in this platform");
	}

	@Override
	public void reset() {
		throw new NotImplementedException(
				"The method reset is not supported in this platform");
	}

	@Override
	public void setConf(
			ImmutableClassesGiraphConfiguration<WritableComparable, V, Writable> configuration) {
		conf = configuration;

	}

	@Override
	public ImmutableClassesGiraphConfiguration<WritableComparable, V, Writable> getConf() {
		return conf;
	}

}

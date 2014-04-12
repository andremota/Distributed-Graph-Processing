package pt.isel.ps1314v.g11.giraph.aggregator;

import org.apache.commons.lang.NotImplementedException;
import org.apache.giraph.conf.ImmutableClassesGiraphConfigurable;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.Aggregator;

/**
 * 
 * This class will map any aggregator
 *
 * @param <A>
 *            Aggregated value
 */

@SuppressWarnings("rawtypes")
public class GiraphAggregatorMapper<A extends Writable> implements
		org.apache.giraph.aggregators.Aggregator<A>,
		ImmutableClassesGiraphConfigurable<WritableComparable, A, Writable> {

	private static int COUNT = 0;

	private ImmutableClassesGiraphConfiguration<WritableComparable, A, Writable> conf;

	private pt.isel.ps1314v.g11.common.graph.Aggregator<A> aggregator;

	private Class<Aggregator<A>> aggregatorClass;
	
	@SuppressWarnings("unchecked")
	public GiraphAggregatorMapper() {
		aggregatorClass = (Class<Aggregator<A>>) conf
				.getClass(
						pt.isel.ps1314v.g11.common.utils.Variables.AGGREGATOR
								+ "|" + COUNT,
						pt.isel.ps1314v.g11.common.graph.Aggregator.class);
		COUNT++;
	}
	
	@Override
	public void aggregate(A value) {
		if (aggregator == null) {
			aggregator = ReflectionUtils.newInstance(aggregatorClass, conf);
		}

		aggregator.aggregate(value);

	}

	@Override
	public A createInitialValue() {
		return aggregator.initialValue();
	}

	@Override
	public A getAggregatedValue() {
		return aggregator.getValue();
	}

	@Override
	public void setAggregatedValue(A value) {
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
			ImmutableClassesGiraphConfiguration<WritableComparable, A, Writable> configuration) {
		conf = configuration;

	}

	@Override
	public ImmutableClassesGiraphConfiguration<WritableComparable, A, Writable> getConf() {
		return conf;
	}
}

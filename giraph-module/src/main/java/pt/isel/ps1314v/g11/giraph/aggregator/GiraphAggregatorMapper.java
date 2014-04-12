package pt.isel.ps1314v.g11.giraph.aggregator;

import org.apache.commons.lang.NotImplementedException;
import org.apache.giraph.conf.ImmutableClassesGiraphConfigurable;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.Aggregator;
import pt.isel.ps1314v.g11.giraph.utils.Variables;

/**
 * 
 * This class will map any aggregator
 *
 * @param <Writable>
 *            Aggregated value
 */

@SuppressWarnings("rawtypes")
public class GiraphAggregatorMapper implements
		org.apache.giraph.aggregators.Aggregator<Writable>,
		ImmutableClassesGiraphConfigurable<WritableComparable, Writable, Writable> {

	private static int COUNT = 0;

	private ImmutableClassesGiraphConfiguration<WritableComparable, Writable, Writable> conf;

	private pt.isel.ps1314v.g11.common.graph.Aggregator<Writable> aggregator;

	private Class<Aggregator<Writable>> aggregatorClass;
	
	@SuppressWarnings("unchecked")
	public GiraphAggregatorMapper() {
		aggregatorClass = (Class<Aggregator<Writable>>) conf
				.getClass(Variables.AGGREGATOR_CLASS+ "|" + COUNT,Aggregator.class);
		COUNT++;
	}
	
	@Override
	public void aggregate(Writable value) {
		if (aggregator == null) {
			aggregator = ReflectionUtils.newInstance(aggregatorClass, conf);
		}

		aggregator.aggregate(value);

	}

	@Override
	public Writable createInitialValue() {
		return aggregator.initialValue();
	}

	@Override
	public Writable getAggregatedValue() {
		return aggregator.getValue();
	}

	@Override
	public void setAggregatedValue(Writable value) {
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
			ImmutableClassesGiraphConfiguration<WritableComparable, Writable, Writable> configuration) {
		conf = configuration;

	}

	@Override
	public ImmutableClassesGiraphConfiguration<WritableComparable, Writable, Writable> getConf() {
		return conf;
	}
}

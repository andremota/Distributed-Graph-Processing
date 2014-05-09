package pt.isel.ps1314v.g11.giraph.graph;

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
	
	@Override
	public void aggregate(Writable value) {

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
		aggregator = ReflectionUtils.newInstance(aggregatorClass, conf);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setConf(
			ImmutableClassesGiraphConfiguration<WritableComparable, Writable, Writable> configuration) {
		conf = configuration;
		Class<?>[] classes = conf.getClasses(
				Aggregator.AGGREGATOR_CLASS, Aggregator.class);

		aggregatorClass = (Class<Aggregator<Writable>>) classes[COUNT];
		aggregator = ReflectionUtils.newInstance(aggregatorClass, conf);

		System.out.println(COUNT + " - " +aggregatorClass);
		if(++COUNT==classes.length)
			COUNT = 0;
	}

	@Override
	public ImmutableClassesGiraphConfiguration<WritableComparable, Writable, Writable> getConf() {
		return conf;
	}
}

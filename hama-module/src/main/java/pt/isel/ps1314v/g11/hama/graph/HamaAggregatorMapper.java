package pt.isel.ps1314v.g11.hama.graph;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.Aggregator;

public class HamaAggregatorMapper<V extends Writable> implements
		org.apache.hama.graph.Aggregator<V>, Configurable {

	private Configuration config;
	private Aggregator<V> commonAggregator;

	@Override
	public void aggregate(V valueToAggregate) {
		commonAggregator.aggregate(valueToAggregate);
	}

	@Override
	public V getValue() {
		return commonAggregator.getValue();
	}

	@Override
	public Configuration getConf() {
		return config;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setConf(Configuration config) {
		this.config = config;

		commonAggregator = (Aggregator<V>) ReflectionUtils.newInstance(
				config.getClass(Aggregator.AGGREGATOR_CLASS, Aggregator.class), config);

	}

}

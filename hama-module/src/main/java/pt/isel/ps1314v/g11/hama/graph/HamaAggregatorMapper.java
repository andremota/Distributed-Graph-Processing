package pt.isel.ps1314v.g11.hama.graph;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.Aggregator;

public class HamaAggregatorMapper<V extends Writable> implements
		org.apache.hama.graph.Aggregator<V>, Configurable {

	private static int COUNT = 0;
	
	private Configuration config;
	private Aggregator<V> commonAggregator;
	
	private final int index;

	public HamaAggregatorMapper() {		
		index = COUNT++;
	}
	
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

		/*
		 * Creates the common aggregator related to this index.
		 */
		commonAggregator = (Aggregator<V>) ReflectionUtils.newInstance(
				config.getClass(Aggregator.AGGREGATOR_CLASS + "|" + index, Aggregator.class), config);

		/*
		 * Restart the count if this is the last aggregator because they
		 * may be created several times.
		 */
		if(config.getInt(Aggregator.AGGREGATOR_COUNT, 0) == index){
			COUNT = 0;
		}
	}

}

package pt.isel.ps1314v.g11.hama.graph;

import java.util.HashMap;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;
import org.hsqldb.lib.HashMappedList;

import pt.isel.ps1314v.g11.common.graph.Aggregator;

/**
 * This aggregator maps the registered common aggregators. This aggregators are
 * created by the order of regist.
 * 
 * @param <V>
 *            Type of value to be aggregated
 */
public class HamaAggregatorMapper implements
		org.apache.hama.graph.Aggregator<Writable>, Configurable {

	private Configuration config;
	private MapWritable map = new MapWritable();
	private HashMap<String, Aggregator<Writable>> commonAggregators = new HashMap<>();

	private boolean setup = false;

	@Override
	public void aggregate(Writable valueToAggregate) {
	}

	@Override
	public Writable getValue() {
		return map;
	}

	@Override
	public Configuration getConf() {
		return config;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setConf(Configuration config) {
		this.config = config;
		if (!setup) {

			setup = true;

			Class[] aggregatorsClasses = config.getClasses(
					Aggregator.AGGREGATOR_CLASS, Aggregator.class);
			String[] aggregatorsNames = config
					.getStrings(Aggregator.AGGREGATOR_KEYS);

			Text key = new Text();
			Aggregator<Writable> aggregator;
			for (int i = 0; i < aggregatorsClasses.length; ++i) {

				Class<? extends Aggregator<Writable>> agClass = (Class<? extends Aggregator<Writable>>) config
						.getClass(Aggregator.AGGREGATOR_CLASS + "|" + i,
								Aggregator.class);

				/*
				 * Creates the common aggregator related to this index.
				 */
				key.set(aggregatorsNames[i]);
				
				aggregator = (Aggregator<Writable>) ReflectionUtils.newInstance(agClass, config);
				
				commonAggregators.put(key.toString(),aggregator);
				
				map.put(key, aggregator.initialValue());
			}
		}

	}
}

package pt.isel.ps1314v.g11.hama.graph;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.Aggregator;
import pt.isel.ps1314v.g11.common.graph.KeyValueWritableDummy;

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
	private HashMap<String, Aggregator<Writable>> commonAggregators = new HashMap<String, Aggregator<Writable>>();

	private boolean setup = false;

	@Override
	public void aggregate(Writable valueToAggregate) {
		
		if(valueToAggregate instanceof MapWritable){
			
			MapWritable mapReceived = (MapWritable)valueToAggregate;
			
			Aggregator<Writable> aggregator;
			for(Entry<Writable, Writable> w : mapReceived.entrySet()){
				aggregator = commonAggregators.get(w.getKey().toString());
				aggregator.aggregate(w.getValue());
				map.put(w.getKey(), aggregator.getValue());
			}
			
			return;
		}
		
		KeyValueWritableDummy dummy = (KeyValueWritableDummy)valueToAggregate;
		Aggregator<Writable> aggregator = commonAggregators.get(dummy.getKey());
		aggregator.aggregate(dummy.getValue());
		//System.out.println("val is " + aggregator.getValue() + " and type of parameter is " + valueToAggregate.getClass().getName());
		map.put(new Text(dummy.getKey()), aggregator.getValue());
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

			Aggregator<Writable> aggregator;
			for (int i = 0; i < aggregatorsClasses.length; ++i) {
				Text key = new Text(aggregatorsNames[i]);
				
				aggregator = (Aggregator<Writable>) ReflectionUtils.newInstance(aggregatorsClasses[i], config);

				commonAggregators.put(key.toString(),aggregator);
				
				map.put(key, aggregator.initialValue());
			}
		}

	}
}

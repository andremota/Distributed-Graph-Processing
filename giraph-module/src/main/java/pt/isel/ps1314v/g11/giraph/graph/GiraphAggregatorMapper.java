package pt.isel.ps1314v.g11.giraph.graph;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.giraph.conf.ImmutableClassesGiraphConfigurable;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.Aggregator;
import pt.isel.ps1314v.g11.common.graph.KeyValueWritableDummy;

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

	private ImmutableClassesGiraphConfiguration<WritableComparable, Writable, Writable> conf;

	
	private MapWritable map = new MapWritable();
	private HashMap<String, Aggregator<Writable>> commonAggregators = new HashMap<>();
	@Override
	public void aggregate(Writable value) {
		if(value instanceof MapWritable){
			map = (MapWritable)value;
			return;
		}
		
		KeyValueWritableDummy dummy = (KeyValueWritableDummy)value;
		Aggregator<Writable> aggregator = commonAggregators.get(dummy.getKey());
		aggregator.aggregate(dummy.getValue());
		map.put(new Text(dummy.getKey()), aggregator.getValue());

	}

	@Override
	public Writable createInitialValue() {
		return new MapWritable();
	}

	@Override
	public Writable getAggregatedValue() {
		return map;
	}

	@Override
	public void setAggregatedValue(Writable value) {

		MapWritable other = (MapWritable)value;
		for(Entry<Writable, Writable> entry : other.entrySet()){
			commonAggregators.get(entry.getKey().toString()).setValue(entry.getValue());
		}
			
		map = other;
	}

	@Override
	public void reset() {
		map = new MapWritable();
		for(Entry<String,Aggregator<Writable>> entry: commonAggregators.entrySet()){
			Aggregator<Writable> agg = entry.getValue();
			agg.setValue(agg.initialValue());
			map.put(new Text(entry.getKey()), agg.initialValue());
			
		}
	}


	@SuppressWarnings("unchecked")
	private void setUpFields(){

		Class[] aggregatorsClasses = conf.getClasses(
				Aggregator.AGGREGATOR_CLASS, Aggregator.class);
		String[] aggregatorsNames = conf
				.getStrings(Aggregator.AGGREGATOR_KEYS);

		Aggregator<Writable> aggregator;
		for (int i = 0; i < aggregatorsClasses.length; ++i) {

			Text key = new Text(aggregatorsNames[i]);

			
			aggregator = (Aggregator<Writable>) ReflectionUtils.newInstance(aggregatorsClasses[i], conf);
			
			commonAggregators.put(key.toString(),aggregator);
			
			map.put(key, aggregator.initialValue());
		}
	}
	
	@Override
	public void setConf(
			ImmutableClassesGiraphConfiguration<WritableComparable, Writable, Writable> configuration) {
		conf = configuration;
		setUpFields();
	}

	@Override
	public ImmutableClassesGiraphConfiguration<WritableComparable, Writable, Writable> getConf() {
		return conf;
	}
}

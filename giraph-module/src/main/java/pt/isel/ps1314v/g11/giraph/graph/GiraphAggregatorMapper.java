package pt.isel.ps1314v.g11.giraph.graph;

import java.util.HashMap;

import org.apache.commons.lang.NotImplementedException;
import org.apache.giraph.conf.ImmutableClassesGiraphConfigurable;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
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
	
	private MapWritable map = new MapWritable();
	private HashMap<String, Aggregator<Writable>> commonAggregators = new HashMap<>();
	@Override
	public void aggregate(Writable value) {

		System.out.println("aggregate called");
		//if(value instanceof )
		aggregator.aggregate(value);

	}

	@Override
	public Writable createInitialValue() {
		System.out.println("Initial value called");
		return aggregator.initialValue();
	}

	@Override
	public Writable getAggregatedValue() {
		System.out.println("Get value called");
		return aggregator.getValue();
	}

	@Override
	public void setAggregatedValue(Writable value) {
		reset();
		aggregator.aggregate(value);
		/*throw new NotImplementedException(
				"The method setAggregatedValue is not supported in this platform");*/
	}

	@Override
	public void reset() {
		System.out.println("RESET");
		//setUpFields();
	}

	/*@SuppressWarnings("unchecked")
	private void setUpFields(){

		Class[] aggregatorsClasses = conf.getClasses(
				Aggregator.AGGREGATOR_CLASS, Aggregator.class);
		String[] aggregatorsNames = conf
				.getStrings(Aggregator.AGGREGATOR_KEYS);

		Text key = new Text();
		Aggregator<Writable> aggregator;
		for (int i = 0; i < aggregatorsClasses.length; ++i) {

			key.set(aggregatorsNames[i]);
			
			aggregator = (Aggregator<Writable>) ReflectionUtils.newInstance(aggregatorsClasses[i], conf);
			
			commonAggregators.put(key.toString(),aggregator);
			
			map.put(key, aggregator.initialValue());
		}
	}*/
	
	@SuppressWarnings("unchecked")
	@Override
	public void setConf(
			ImmutableClassesGiraphConfiguration<WritableComparable, Writable, Writable> configuration) {
		conf = configuration;
		Class[] aggregatorsClasses = conf.getClasses(
				Aggregator.AGGREGATOR_CLASS, Aggregator.class);
		aggregator = (Aggregator<Writable>) ReflectionUtils.newInstance(aggregatorsClasses[COUNT], conf);
		if(++COUNT==aggregatorsClasses.length)
			COUNT = 0;
		//setUpFields();
	}

	@Override
	public ImmutableClassesGiraphConfiguration<WritableComparable, Writable, Writable> getConf() {
		return conf;
	}
}

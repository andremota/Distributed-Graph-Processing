package pt.isel.ps1314v.g11.hama.graph;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.Aggregator;

/**
 * This aggregator maps the registered common aggregators.
 * This aggregators are created by the order of regist.
 * 
 * @param <V> Type of value to be aggregated
 */
public class HamaAggregatorMapper implements
		org.apache.hama.graph.Aggregator<Writable>, Configurable {

	private static int COUNT = 0;
	
	private Configuration config;
	private Aggregator<Writable> commonAggregator;
	
	private boolean setup = false;
	
	@Override
	public void aggregate(Writable valueToAggregate) {
		commonAggregator.aggregate(valueToAggregate);
	}

	@Override
	public Writable getValue() {
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
		if(!setup){
			
			//int index = COUNT++;
			

			
			/*
			 * Creates the common aggregator related to this index.
			 */
			
			Class<? extends Aggregator<Writable>> agClass = (Class<? extends Aggregator<Writable>>) config.getClass(Aggregator.AGGREGATOR_CLASS + "|" + COUNT, Aggregator.class);
			
			if(agClass.isInterface())
				commonAggregator = new DummyAggregator();
			else
			commonAggregator = (Aggregator<Writable>) ReflectionUtils.newInstance(agClass, config);
			
			setup = true;
			
			/*
			 * Restart the count if this is the last aggregator because they
			 * may be created several times.
			 */
			
			if(++COUNT > config.getInt(Aggregator.AGGREGATOR_COUNT, 0) ){
				COUNT = 0;
			}
		}
		
	}

}

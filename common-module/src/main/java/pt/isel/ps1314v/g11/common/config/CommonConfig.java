package pt.isel.ps1314v.g11.common.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;

import pt.isel.ps1314v.g11.common.graph.Aggregator;
import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Combiner;
/**
 * 
 * Used to configure common settings between modules.
 *
 */
@SuppressWarnings("rawtypes")
public class CommonConfig{
	
	private List<DefaultKeyValue> aggregators = new ArrayList<DefaultKeyValue>();
	
	private ModuleConfiguration config;
	
	public CommonConfig(ModuleConfiguration config){
		this.config = config;
	}
	
	/**
	 * Used to define the {@link Algorithm} class to be used.
	 * @see pt.isel.ps1314v.g11.common.graph.Algorithm
	 * @param algorithmClass The algorithm class
	 */
	public void setAlgorithmClass(Class<? extends Algorithm<?,?,?,?>> algorithmClass){
		config.setClass(Algorithm.ALGORITHM_CLASS, algorithmClass, Algorithm.class);
		config.useAlgorithm(algorithmClass);
	}
	
	/**
	 * Used to register an {@link Aggregator} to be used by the algorithm.
	 * @param key The key associated to the aggregator
	 * @param aggregator Aggregator class to be registered
	 * @see pt.isel.ps1314v.g11.common.graph.Aggregator
	 */
	public void registerAggregator(String key, Class<? extends Aggregator<?>> aggregator){
		aggregators.add(new DefaultKeyValue(key, aggregator));
		config.useAggregators();
	}
	
	/**
	 * Sets the combiner class to in the algorithm
	 * @param combinerClass The combiner class
	 * @see pt.isel.ps1314v.g11.common.graph.Combiner
	 */
	public void setCombinerClass(Class<? extends Combiner> combinerClass){
		config.setClass(Combiner.COMBINER_CLASS, combinerClass, Combiner.class);
		config.useCombiner();
	}
	
	/**
	 * Sets an integer that can be accessed through a configuration in the algorithm
	 * @param name The name to use to access it
	 * @param value The value of the integer
	 */
	public void setInt(String name, int value){
		config.setInt(name, value);
	}
	/**
	 * Sets a float that can be accessed through a configuration in the algorithm
	 * @param name The name to use to access it
	 * @param value The value of the float
	 */
	public void setFloat(String name, float value) {
		config.setFloat(name,value);
	}
	/**
	 * Sets a String that can be accessed through a configuration in the algorithm
	 * @param name The name to use to access it
	 * @param value The value of the String
	 */
	public void set(String name, String value){
		config.set(name,value);
	}
	
	/**
	 * Sets an array of Strings that can be accessed through a configuration in the algorithm
	 * @param name The name to use to access it
	 * @param value The value of the Strings
	 */
	public void setStrings(String name, String[] value){
		String vals = "";
		for(int i = 0; i<value.length; ++i){
			vals+=value[i]+",";
		}
		set(name, vals);
	}
	
	/**
	 * This method should always be called after finishing configurations
	 */
	public void preparePlatformConfig(){
		String classes = "";
		String names = "";
		DefaultKeyValue kv;
		for(int i = 0; i<aggregators.size(); ++i){
			kv = aggregators.get(i);
			classes += ((Class<?>)kv.getValue()).getName() + ",";
			names += kv.getKey() + ",";
		}
		
		config.set(Aggregator.AGGREGATOR_CLASS, classes);
		config.set(Aggregator.AGGREGATOR_KEYS, names);
		
		config.preparePlatformConfig();
	}

	/**
	 * Sets a boolean that can be accessed through a configuration in the algorithm
	 * @param name The name to use to access it
	 * @param value The value of the boolean
	 */
	public void setBoolean(String string, boolean value) {
		config.setBoolean(string,value);
	}
	
	/**
	 * Gets the algorithm class set using the {@link #setAlgorithmClass(Class) setAlgorithmClass} method
	 * @return The algorithm class
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends Algorithm<?,?,?,?>> getAlgorithmClass(){
		return (Class<? extends Algorithm<?, ?, ?, ?>>) config.getClass(Algorithm.ALGORITHM_CLASS, Algorithm.class);
	}


	
}

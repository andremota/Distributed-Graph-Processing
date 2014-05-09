package pt.isel.ps1314v.g11.common.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;

import pt.isel.ps1314v.g11.common.graph.Aggregator;
import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Combiner;

@SuppressWarnings("rawtypes")
public class CommonConfig{
	
	private List<DefaultKeyValue> aggregators = new ArrayList<>();
	
	private ModuleConfiguration config;
	
	public CommonConfig(ModuleConfiguration config){
		this.config = config;
	}
	 
	public void setAlgorithmClass(Class<? extends Algorithm<?,?,?,?>> algorithmClass){
		config.setClass(Algorithm.ALGORITHM_CLASS, algorithmClass, Algorithm.class);
		config.useAlgorithm(algorithmClass);
	}
	
	public void registerAggregator(String key, Class<? extends Aggregator<?>> aggregator){
		aggregators.add(new DefaultKeyValue(key, aggregator));
		config.useAggregators();
	}
	
	public void setCombinerClass(Class<? extends Combiner> combinerClass){
		config.setClass(Combiner.COMBINER_CLASS, combinerClass, Combiner.class);
		config.useCombiner();
	}
	
	public void setInt(String name, int value){
		config.setInt(name, value);
	}
	
	public void set(String name, String value){
		config.set(name,value);
	}
	
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
	
}

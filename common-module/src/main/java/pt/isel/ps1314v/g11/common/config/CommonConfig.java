package pt.isel.ps1314v.g11.common.config;

import pt.isel.ps1314v.g11.common.graph.Aggregator;
import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Combiner;

@SuppressWarnings("rawtypes")
public class CommonConfig{
	
	private ModuleConfiguration config;
	
	public CommonConfig(ModuleConfiguration config){
		this.config = config;
	}
	
	public void setAlgorithmClass(Class<? extends Algorithm> algorithmClass){
		config.setClass(Algorithm.ALGORITHM_CLASS, algorithmClass, Algorithm.class);
		config.useAlgorithm();
	}
	
	public void setAggregatorClass(Class<? extends Aggregator> aggregatorClass){
		config.setClass(Aggregator.AGGREGATOR_CLASS, aggregatorClass, Aggregator.class);
	}
	
	public void setCombinerClass(Class<? extends Combiner> combinerClass){
		config.setClass(Combiner.COMBINER_CLASS, combinerClass, Combiner.class);
	}
	
}

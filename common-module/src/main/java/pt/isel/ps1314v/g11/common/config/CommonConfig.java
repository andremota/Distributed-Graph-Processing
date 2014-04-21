package pt.isel.ps1314v.g11.common.config;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.common.graph.Combiner;

@SuppressWarnings("rawtypes")
public class CommonConfig{
	
	private ModuleConfiguration config;
	
	public CommonConfig(ModuleConfiguration config){
		this.config = config;
	}
	 
	public void setAlgorithmClass(Class<? extends Algorithm<?,?,?,?>> algorithmClass){
		config.setClass(Algorithm.ALGORITHM_CLASS, algorithmClass, Algorithm.class);
		config.useAlgorithm(algorithmClass);
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
		config.preparePlatformConfig();
	}
	
}

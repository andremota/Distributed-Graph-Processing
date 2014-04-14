package pt.isel.ps1314v.g11.common.config;

import pt.isel.ps1314v.g11.common.graph.Aggregator;
import pt.isel.ps1314v.g11.common.graph.Combiner;

@SuppressWarnings("rawtypes")
public interface ModuleConfiguration {
	void useAlgorithm();
	void setAggregatorClass(Class<? extends Aggregator> aggregatorClass);
	void setCombinerClass(Class<? extends Combiner> combinerClass);
	void setClass(String algorithmClass,Class<?> algorithmClass2, Class<?> class1);
}

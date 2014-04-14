package pt.isel.ps1314v.g11.common.config;

import pt.isel.ps1314v.g11.common.graph.Aggregator;
import pt.isel.ps1314v.g11.common.graph.Combiner;

@SuppressWarnings("rawtypes")
public interface ModuleConfiguration {
	void useAlgorithm();
	void setClass(String name,Class<?> value, Class<?> xface);
	void useAggregator();
	void useCombiner();
	void setInt(String name, int value);
	boolean run(boolean verbose);
	void set(String name, String value);
}

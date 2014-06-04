package pt.isel.ps1314v.g11.common.config;

import pt.isel.ps1314v.g11.common.graph.Algorithm;


public interface ModuleConfiguration {
	void useAlgorithm(Class<? extends Algorithm<?,?,?,?>> klass);
	void setClass(String name,Class<?> value, Class<?> xface);
	void useCombiner();
	void setInt(String name, int value);
	void preparePlatformConfig();
	void set(String name, String value);
	void useAggregators();
	void setBoolean(String string, boolean value);
}

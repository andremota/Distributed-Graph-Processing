package pt.isel.ps1314v.g11.common.config;


public interface ModuleConfiguration {
	void useAlgorithm();
	void setClass(String name,Class<?> value, Class<?> xface);
	void useAggregator();
	void useCombiner();
	void setInt(String name, int value);
	void preparePlatformConfig();
	void set(String name, String value);
}

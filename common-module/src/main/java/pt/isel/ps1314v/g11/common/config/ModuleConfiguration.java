package pt.isel.ps1314v.g11.common.config;

import pt.isel.ps1314v.g11.common.graph.Algorithm;

/**
 * This interface must be implemented by the modules to allow configuration
 *
 */
public interface ModuleConfiguration {
	/**
	 * Called when the algorithm class is set using the {@link CommonConfig#setAlgorithmClass(Class)} method}
	 * @param klass The algorithm class
	 */
	void useAlgorithm(Class<? extends Algorithm<?,?,?,?>> klass);
	/**
	 * Called to set a class to be accessed using the configuration
	 * @param name The name to use to access it
	 * @param value The class to set
	 * @param xface An interface implemented by the class
	 */
	void setClass(String name,Class<?> value, Class<?> xface);
	/**
	 * Called when the combiner class is set using the {@link CommonConfig#setCombinerClass(Class)} method}
	 */
	void useCombiner();
	/**
	 * Sets an integer that can be accessed through a configuration in the algorithm
	 * @param name The name to use to access it
	 * @param value The value of the integer
	 */
	void setInt(String name, int value);
	
	/**
	 * Called after the {@link CommonConfig#preparePlatformConfig()} method
	 * @see CommonConfig#preparePlatformConfig()
	 */
	void preparePlatformConfig();
	/**
	 * Sets a String that can be accessed through a configuration in the algorithm
	 * @param name The name to use to access it
	 * @param value The value of the String
	 */
	void set(String name, String value);
	/**
	 * Called when an aggregator class is registered using the {@link CommonConfig#registerAggregator(String, Class)} method}
	 */
	void useAggregators();
	/**
	 * Sets a boolean that can be accessed through a configuration in the algorithm
	 * @param name The name to use to access it
	 * @param value The value of the boolean
	 */
	void setBoolean(String string, boolean value);
	/**
	 * Gets a previously registered class
	 * @param name The name to use to access it
	 * @param defaultValue Default value
	 * @return
	 */
	Class<?> getClass(String name,Class<?> defaultValue);
	/**
	 * Sets a float that can be accessed through a configuration in the algorithm
	 * @param name The name to use to access it
	 * @param value The value of the float
	 */
	void setFloat(String string, float value);
}

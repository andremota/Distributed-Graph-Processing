package pt.isel.ps1314v.g11.hama.config;

import net.jodah.typetools.TypeResolver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.graph.GraphJob;
import org.apache.hama.graph.Vertex;

import pt.isel.ps1314v.g11.common.config.ModuleConfiguration;
import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.hama.graph.HamaAggregatorMapper;
import pt.isel.ps1314v.g11.hama.graph.HamaCombinerMapper;
import pt.isel.ps1314v.g11.hama.graph.HamaComputationMapper;

public class HamaModuleConfiguration implements ModuleConfiguration{

	public static Class<? extends Writable> HAMA_VERTEX_VALUE_CLASS;/*"pt.isel.ps1314v.g11.hama.vertex_value";*/
	private Configuration config;
	private GraphJob job;
	
	public HamaModuleConfiguration(GraphJob job) {
		this.job = job;
		this.config = (HamaConfiguration)job.getConfiguration();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void useAlgorithm(Class<? extends Algorithm<?, ?, ?, ?>> klass) {
		job.setVertexClass( (Class<? extends Vertex<? extends Writable, ? extends Writable, ? extends Writable>>) (Class) HamaComputationMapper.class);
		Class<? extends Writable>[] classes = (Class<? extends Writable>[]) TypeResolver.resolveRawArguments(Algorithm.class, klass);
		
		job.setVertexIDClass(classes[0]);
		job.setVertexValueClass(classes[3]);
		job.setEdgeValueClass(classes[2]);
		
		HAMA_VERTEX_VALUE_CLASS = classes[1];
		//config.setClass(HAMA_VERTEX_VALUE, classes[1], Writable.class);
	}

	@Override
	public void setClass(String name, Class<?> value, Class<?> xface) {
		config.setClass(name, value, xface);
	}
	
	@Override
	public void useCombiner() {
		job.setCombinerClass(HamaCombinerMapper.class);
	}

	@Override
	public void setInt(String name, int value) {
		config.setInt(name, value);
	}
	
	
	@Override
	public void preparePlatformConfig() {
		/*int nAggregators = config.getInt(Aggregator.AGGREGATOR_COUNT, 0);
		if(nAggregators <= 0) return;
		Logger.getLogger(HamaModuleConfiguration.class).info("Found " + nAggregators + " aggregators.");
		@SuppressWarnings("unchecked")
		Class<? extends HamaAggregatorMapper>[] aggregators = new Class[nAggregators];
		for(int i = 0; i<nAggregators; ++i){
			aggregators[i] = HamaAggregatorMapper.class;
		}
		job.setAggregatorClass(aggregators);*/
		
	}

	@Override
	public void set(String name, String value) {
		config.set(name, value);
	}

	@Override
	public void useAggregators() {
		job.setAggregatorClass(HamaAggregatorMapper.class);
	}
	
	@Override
	public void setBoolean(String name, boolean value) {
		config.setBoolean(name, value);
	}

	@Override
	public Class<?> getClass(String className, Class<?> class1) {
		return config.getClass(className, class1);
	}
}

package pt.isel.ps1314v.g11.hama.config;

import net.jodah.typetools.TypeResolver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hama.graph.GraphJob;
import org.apache.hama.graph.Vertex;

import pt.isel.ps1314v.g11.common.config.ModuleConfiguration;
import pt.isel.ps1314v.g11.common.graph.BasicAlgorithm;
import pt.isel.ps1314v.g11.hama.graph.HamaCombinerMapper;
import pt.isel.ps1314v.g11.hama.graph.HamaComputationMapper;

public class HamaModuleConfiguration implements ModuleConfiguration{

	private Configuration config;
	private GraphJob job;
	
	public HamaModuleConfiguration(GraphJob job) {
		this.job = job;
		this.config = job.getConfiguration();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void useAlgorithm(Class<? extends BasicAlgorithm<?,?,?>> klass) {
		job.setVertexClass( (Class<? extends Vertex<? extends Writable, ? extends Writable, ? extends Writable>>) HamaComputationMapper.class);
		Class<? extends Writable>[] classes = (Class<? extends Writable>[]) TypeResolver.resolveRawArguments(BasicAlgorithm.class, klass);
		
		job.setVertexIDClass(classes[0]);
		job.setVertexValueClass(classes[1]);
		job.setEdgeValueClass(classes[2]);
	
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
}

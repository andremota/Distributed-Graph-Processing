package pt.isel.ps1314v.g11.hama.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hama.graph.GraphJob;
import org.apache.hama.graph.Vertex;

import pt.isel.ps1314v.g11.common.config.ModuleConfiguration;
import pt.isel.ps1314v.g11.common.graph.Aggregator;
import pt.isel.ps1314v.g11.hama.graph.HamaAggregatorMapper;
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
	public void useAlgorithm() {
		job.setVertexClass( (Class<? extends Vertex<? extends Writable, ? extends Writable, ? extends Writable>>) HamaComputationMapper.class);
	}

	@Override
	public void setClass(String name, Class<?> value, Class<?> xface) {
		config.setClass(name, value, xface);
	}

	@Override
	public void useAggregator() {}
	
	@Override
	public void useCombiner() {
		job.setCombinerClass(HamaCombinerMapper.class);
	}

	@Override
	public void setInt(String name, int value) {
		config.setInt(name, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean run(boolean verbose) {
		
		int nAggregators = config.getInt(Aggregator.AGGREGATOR_COUNT, 0);
		
		Class<? extends HamaAggregatorMapper>[] aggregators = new Class[nAggregators];
		for(int i = 0; i<nAggregators; ++i){
			aggregators[i] = HamaAggregatorMapper.class;
		}
		job.setAggregatorClass(aggregators);
		
		
		try {
			return job.waitForCompletion(verbose);
		} catch (Exception e){
			//TODO - refactor!
			return false;
		}
	}

	@Override
	public void set(String name, String value) {
		config.set(name, value);
	}
}

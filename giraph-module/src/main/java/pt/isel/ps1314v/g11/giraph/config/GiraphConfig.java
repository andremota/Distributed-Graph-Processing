package pt.isel.ps1314v.g11.giraph.config;

import org.apache.giraph.conf.GiraphConfiguration;

import pt.isel.ps1314v.g11.common.config.ModuleConfiguration;
import pt.isel.ps1314v.g11.common.graph.Aggregator;
import pt.isel.ps1314v.g11.common.graph.Combiner;
import pt.isel.ps1314v.g11.giraph.graph.AggregatorMasterCompute;
import pt.isel.ps1314v.g11.giraph.graph.GiraphCombinerMapper;
import pt.isel.ps1314v.g11.giraph.graph.GiraphComputationMapper;

@SuppressWarnings("rawtypes")
public class GiraphConfig implements ModuleConfiguration{

	private GiraphConfiguration config;
	
	public public GiraphConfig(GiraphConfiguration config) {
		this.config = config;
	}
	
	@Override
	public void useAlgorithm() {
		config.setComputationClass(GiraphComputationMapper.class);
		
	}


	@Override
	public void useAggregator() {
		config.setMasterComputeClass(AggregatorMasterCompute.class);
		
	}

	@Override
	public void useCombiner() {
		config.setMessageCombinerClass(GiraphCombinerMapper.class);
		
	}

	@Override
	public void setClass(String name, Class<?> value, Class<?> xface) {
		config.setClass(name,value,xface);
		
	}

	@Override
	public void setInt(String name, int value) {
		config.setInt(name,value);
		
	}
	
	@Override
	public void set(String name, String value) {
		config.set(name,value);
	}

	@Override
	public boolean run(boolean verbose) {
		// TODO Auto-generated method stub
		return false;
	}




}

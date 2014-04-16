package pt.isel.ps1314v.g11.giraph.config;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.edge.OutEdges;
import org.apache.giraph.job.GiraphJob;

import pt.isel.ps1314v.g11.common.config.ModuleConfiguration;
import pt.isel.ps1314v.g11.giraph.graph.AggregatorMasterCompute;
import pt.isel.ps1314v.g11.giraph.graph.GiraphCombinerMapper;
import pt.isel.ps1314v.g11.giraph.graph.GiraphComputationMapper;
import pt.isel.ps1314v.g11.giraph.graph.GiraphOutEdgesMapper;

public class GiraphModuleConfiguration implements ModuleConfiguration {

	private GiraphConfiguration config;

	public GiraphModuleConfiguration(GiraphConfiguration config) {
		this.config = config;
	}

	public GiraphModuleConfiguration(GiraphJob job) {
		this.config = job.getConfiguration();
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
		config.setClass(name, value, xface);

	}

	@Override
	public void setInt(String name, int value) {
		config.setInt(name, value);

	}

	@Override
	public void set(String name, String value) {
		config.set(name, value);
	}
	
	@Override
	public void preparePlatformConfig() {
		config.setClass(GiraphOutEdgesMapper.OUTEDGES,
				config.getOutEdgesClass(), OutEdges.class);
		config.setOutEdgesClass(GiraphOutEdgesMapper.class);
	}

}

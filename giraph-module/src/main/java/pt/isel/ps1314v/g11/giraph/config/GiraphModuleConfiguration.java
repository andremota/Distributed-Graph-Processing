package pt.isel.ps1314v.g11.giraph.config;

import net.jodah.typetools.TypeResolver;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.conf.GiraphConstants;
import org.apache.giraph.edge.OutEdges;
import org.apache.giraph.graph.Language;
import org.apache.giraph.job.GiraphJob;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import pt.isel.ps1314v.g11.common.config.ModuleConfiguration;
import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.giraph.graph.AggregatorMasterCompute;
import pt.isel.ps1314v.g11.giraph.graph.GiraphCombinerMapper;
import pt.isel.ps1314v.g11.giraph.graph.GiraphComputationMapper;
import pt.isel.ps1314v.g11.giraph.graph.GiraphEdgeValueFactory;
import pt.isel.ps1314v.g11.giraph.graph.GiraphMessageValueFactory;
import pt.isel.ps1314v.g11.giraph.graph.GiraphOutEdgesMapper;
import pt.isel.ps1314v.g11.giraph.graph.GiraphVertexIdFactory;
import pt.isel.ps1314v.g11.giraph.graph.GiraphVertexValueFactory;

public class GiraphModuleConfiguration implements ModuleConfiguration {

	public static final String AGGREGATOR_CLASS = "pt.isel.ps1314v.g11.aggregatorclass";
	public static final String AGGREGATOR_COUNT = "pt.isel.ps1314v.g11.aggregatorcount";
	
	
	private GiraphConfiguration config;

	public GiraphModuleConfiguration(GiraphConfiguration config) {
		this.config = config;
	}

	public GiraphModuleConfiguration(GiraphJob job) {
		this.config = job.getConfiguration();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void useAlgorithm(Class<? extends Algorithm<?, ?, ?, ?>> klass) {

		config.setComputationClass(GiraphComputationMapper.class);
		Class<? extends Writable>[] classes = (Class<? extends Writable>[]) TypeResolver.resolveRawArguments(Algorithm.class, klass);
		
		GiraphConstants.VERTEX_ID_CLASS.set(config, (Class<? extends WritableComparable<?>>) classes[0]);
		GiraphConstants.VERTEX_VALUE_CLASS.set(config, classes[1]);
		GiraphConstants.EDGE_VALUE_CLASS.set(config, classes[2]);
		GiraphConstants.INCOMING_MESSAGE_VALUE_CLASS.set(config, classes[3]);
		GiraphConstants.OUTGOING_MESSAGE_VALUE_CLASS.set(config,classes[3]);
		
		GiraphConstants.COMPUTATION_LANGUAGE.set(config,Language.JYTHON);
		
		GiraphConstants.VERTEX_ID_FACTORY_CLASS.set(config, GiraphVertexIdFactory.class);
		GiraphConstants.VERTEX_VALUE_FACTORY_CLASS.set(config, GiraphVertexValueFactory.class);
		GiraphConstants.EDGE_VALUE_FACTORY_CLASS.set(config, GiraphEdgeValueFactory.class);
		GiraphConstants.INCOMING_MESSAGE_VALUE_FACTORY_CLASS.set(config, GiraphMessageValueFactory.class);
		GiraphConstants.OUTGOING_MESSAGE_VALUE_FACTORY_CLASS.set(config, GiraphMessageValueFactory.class);
	}
	
	public void useAggregators(){
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

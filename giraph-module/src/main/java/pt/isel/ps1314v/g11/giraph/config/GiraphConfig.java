package pt.isel.ps1314v.g11.giraph.config;

import org.apache.giraph.conf.GiraphConfiguration;

import pt.isel.ps1314v.g11.common.config.ModuleConfiguration;
import pt.isel.ps1314v.g11.common.graph.Aggregator;
import pt.isel.ps1314v.g11.common.graph.Combiner;

@SuppressWarnings("rawtypes")
public class GiraphConfig extends GiraphConfiguration implements ModuleConfiguration{

	@Override
	public void useAlgorithm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAggregatorClass(Class<? extends Aggregator> aggregatorClass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCombinerClass(Class<? extends Combiner> combinerClass) {
		// TODO Auto-generated method stub
		
	}


}

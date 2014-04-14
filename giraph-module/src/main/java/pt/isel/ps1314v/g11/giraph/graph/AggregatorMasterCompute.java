package pt.isel.ps1314v.g11.giraph.graph;

import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.master.DefaultMasterCompute;

import pt.isel.ps1314v.g11.common.graph.Aggregator;

public class AggregatorMasterCompute extends DefaultMasterCompute{

	
	@Override
	public void initialize() throws InstantiationException,
			IllegalAccessException {
		
		ImmutableClassesGiraphConfiguration<?, ?, ?> conf = getConf();
		
		int nAggregators = conf.getInt(Aggregator.AGGREGATOR_COUNT, 0);
		
		for(int i = 0; i<nAggregators; ++i){
			registerPersistentAggregator(i+"", GiraphAggregatorMapper.class);
		}
	}

}

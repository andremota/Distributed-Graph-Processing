package pt.isel.ps1314v.g11.giraph.aggregator;

import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.master.DefaultMasterCompute;
import pt.isel.ps1314v.g11.giraph.utils.Variables;

public class AggregatorMasterCompute extends DefaultMasterCompute{

	
	@Override
	public void initialize() throws InstantiationException,
			IllegalAccessException {
		
		ImmutableClassesGiraphConfiguration<?, ?, ?> conf = getConf();
		
		int nAggregators = conf.getInt(Variables.AGGREGATOR_COUNT, 0);
		
		for(int i = 0; i<nAggregators; ++i){
			registerPersistentAggregator(i+"", GiraphAggregatorMapper.class);
		}
	}

}

package pt.isel.ps1314v.g11.giraph.graph;


import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.master.DefaultMasterCompute;

import pt.isel.ps1314v.g11.common.graph.Aggregator;

public class AggregatorMasterCompute extends DefaultMasterCompute {

	
	/*private static final Logger LOG = Logger
			.getLogger(AggregatorMasterCompute.class);*/

	@Override
	public void initialize() throws InstantiationException,
			IllegalAccessException {
		
		ImmutableClassesGiraphConfiguration<?, ?, ?> conf = getConf();

		/*int nAggregators = conf.getInt(
				GiraphModuleConfiguration.AGGREGATOR_COUNT, 0);*/
	


		String[] aggregatorsNames = conf
				.getStrings(Aggregator.AGGREGATOR_KEYS);

		for (int i = 0; i < aggregatorsNames.length; ++i) {
			
			registerAggregator(aggregatorsNames[i],
					GiraphAggregatorMapper.class);
		}

	}

} 

package pt.isel.ps1314v.g11.giraph.graph;

import org.apache.giraph.aggregators.Aggregator;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.master.DefaultMasterCompute;
import org.apache.hadoop.io.Writable;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.giraph.config.GiraphModuleConfiguration;

public class AggregatorMasterCompute extends DefaultMasterCompute {

	
	private static final Logger LOG = Logger
			.getLogger(AggregatorMasterCompute.class);
	@SuppressWarnings("unchecked")
	@Override
	public void initialize() throws InstantiationException,
			IllegalAccessException {
		
		ImmutableClassesGiraphConfiguration<?, ?, ?> conf = getConf();

		int nAggregators = conf.getInt(
				GiraphModuleConfiguration.AGGREGATOR_COUNT, 0);
	

		LOG.info("NAGGREGATORS = "+nAggregators);
		for (int i = 0; i < nAggregators; ++i) {
			registerAggregator(i + "",
					(Class<? extends Aggregator<Writable>>) conf.getClass(
							GiraphModuleConfiguration.AGGREGATOR_CLASS + "|"
									+ i, Aggregator.class));
		}

	}

} 

package pt.isel.ps1314v.g11.giraph.graph;


import org.apache.giraph.master.DefaultMasterCompute;


public class AggregatorMasterCompute extends DefaultMasterCompute {

	public static final String AGGREGATOR_KEY = "pt.isel.ps1314v.g11.giraph.graph.aggregatormap";
	/*private static final Logger LOG = Logger
			.getLogger(AggregatorMasterCompute.class);*/

	@Override
	public void initialize() throws InstantiationException,
			IllegalAccessException {


		registerAggregator(AGGREGATOR_KEY, GiraphAggregatorMapper.class);
	}


} 

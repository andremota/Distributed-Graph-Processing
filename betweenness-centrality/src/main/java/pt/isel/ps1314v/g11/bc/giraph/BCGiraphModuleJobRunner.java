package pt.isel.ps1314v.g11.bc.giraph;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.job.GiraphJob;

import pt.isel.ps1314v.g11.bc.BetweennessCentralityAlgorithm;
import pt.isel.ps1314v.g11.bc.giraph.io.AdjacencyListBCInputFormat;
import pt.isel.ps1314v.g11.bc.giraph.io.JsonBCOutputFormat;
import pt.isel.ps1314v.g11.bc.util.BCJobBean;
import pt.isel.ps1314v.g11.common.aggregator.BooleanAndAggregator;
import pt.isel.ps1314v.g11.common.aggregator.DoubleMaxAggregator;
import pt.isel.ps1314v.g11.common.aggregator.DoubleMinAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleJobRunner;

public class BCGiraphModuleJobRunner extends GiraphModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new BCJobBean();
	}

	@Override
	public void prepareJob(GiraphJob job, GiraphConfiguration conf,
			CommonConfig commonConfig, JobBean bean) {
		
		BCJobBean config = (BCJobBean)bean;
		
		conf.setVertexInputFormatClass(AdjacencyListBCInputFormat.class);
		conf.setVertexOutputFormatClass(JsonBCOutputFormat.class);
		commonConfig.setAlgorithmClass(BetweennessCentralityAlgorithm.class);
		commonConfig.setStrings(BetweennessCentralityAlgorithm.START_VERTEXES,config.getStarts());
		
		commonConfig.setBoolean(BetweennessCentralityAlgorithm.NORMALIZE, config.shouldNormalize());
		
		if(config.shouldNormalize()){
			commonConfig.registerAggregator(BetweennessCentralityAlgorithm.AGG_ENDED, BooleanAndAggregator.class);
			commonConfig.registerAggregator(BetweennessCentralityAlgorithm.AGG_MIN_BC, DoubleMinAggregator.class);
			commonConfig.registerAggregator(BetweennessCentralityAlgorithm.AGG_MAX_BC, DoubleMaxAggregator.class);
		}
		
	}

}

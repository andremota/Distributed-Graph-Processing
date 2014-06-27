package pt.isel.ps1314v.g11.llp.giraph;

import java.security.InvalidParameterException;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.IdWithValueTextOutputFormat;
import org.apache.giraph.job.GiraphJob;

import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleJobRunner;
import pt.isel.ps1314v.g11.llp.LLPAlgorithm;
import pt.isel.ps1314v.g11.llp.giraph.io.AdjacencyListWithValuesInputFormat;
import pt.isel.ps1314v.g11.llp.util.LLPConfig;

public class LLPGiraphModuleJobRunner extends GiraphModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new LLPConfig();
	}

	@Override
	public void prepareJob(GiraphJob job, GiraphConfiguration conf,
			CommonConfig commonConfig, JobBean bean) {
		LLPConfig llpConf = (LLPConfig) bean;
		conf.setVertexInputFormatClass(AdjacencyListWithValuesInputFormat.class);
		conf.setVertexOutputFormatClass(IdWithValueTextOutputFormat.class);
		
		if(!(llpConf.getDecisionFactor() >= 0 && llpConf.getDecisionFactor() <= 1)){
			throw new InvalidParameterException("Decision factor cannot be lesser than 0 or greater than 1.");
		}
		
		commonConfig.setFloat(LLPAlgorithm.DECISION_FACTOR, llpConf.getDecisionFactor());
		commonConfig.registerAggregator(LLPAlgorithm.GLOBAL_CHANGE_AGGREGATOR, BooleanOrAggregator.class);
		commonConfig.setAlgorithmClass(LLPAlgorithm.class);
	}

}

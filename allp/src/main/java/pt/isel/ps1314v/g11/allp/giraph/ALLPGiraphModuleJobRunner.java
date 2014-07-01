package pt.isel.ps1314v.g11.allp.giraph;

import java.security.InvalidParameterException;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.IdWithValueTextOutputFormat;
import org.apache.giraph.job.GiraphJob;

import pt.isel.ps1314v.g11.allp.ALLPAlgorithm;
import pt.isel.ps1314v.g11.allp.util.ALLPConfig;
import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleJobRunner;
import pt.isel.ps1314v.g11.allp.giraph.io.ALLPAdjacencyListWithValuesInputFormat;

public class ALLPGiraphModuleJobRunner extends GiraphModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new ALLPConfig();
	}

	@Override
	public void prepareJob(GiraphJob job, GiraphConfiguration conf,
			CommonConfig commonConfig, JobBean bean) {
		ALLPConfig llpConf = (ALLPConfig) bean;
		conf.setVertexInputFormatClass(ALLPAdjacencyListWithValuesInputFormat.class);
		conf.setVertexOutputFormatClass(IdWithValueTextOutputFormat.class);
		
		if(!(llpConf.getDecisionFactor() >= 0 && llpConf.getDecisionFactor() <= 1)){
			throw new InvalidParameterException("Decision factor cannot be lesser than 0 or greater than 1.");
		}

		commonConfig.setBoolean(ALLPAlgorithm.COUNT_VERTEX_AS_OWN_NEIGHBOR, llpConf.getCountVertexNg());
		commonConfig.setFloat(ALLPAlgorithm.DECISION_FACTOR, llpConf.getDecisionFactor());
		commonConfig.registerAggregator(ALLPAlgorithm.GLOBAL_CHANGE_AGGREGATOR, BooleanOrAggregator.class);
		commonConfig.setAlgorithmClass(ALLPAlgorithm.class);
	}

}

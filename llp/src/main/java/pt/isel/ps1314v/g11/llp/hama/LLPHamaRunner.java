package pt.isel.ps1314v.g11.llp.hama;

import java.security.InvalidParameterException;

import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;

import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.hama.config.HamaModuleJobRunner;
import pt.isel.ps1314v.g11.llp.LLPAlgorithm;
import pt.isel.ps1314v.g11.llp.hama.io.LLPVertexInputReader;
import pt.isel.ps1314v.g11.llp.util.LLPConfig;

public class LLPHamaRunner extends HamaModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new LLPConfig();
	}

	@Override
	public void prepareJob(GraphJob job, CommonConfig moduleConfig, JobBean bean) {

		LLPConfig llpConf = (LLPConfig) bean;
		//job.setJobName(LLPAlgorithm.class.getSimpleName());
		
		job.setVertexInputReaderClass(LLPVertexInputReader.class);
		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		
		if(!(llpConf.getDecisionFactor() >= 0 && llpConf.getDecisionFactor() <= 1)){
			throw new InvalidParameterException("Decision factor cannot be lesser than 0 or greater than 1.");
		}
		moduleConfig.setFloat(LLPAlgorithm.DECISION_FACTOR, llpConf.getDecisionFactor());
		
		moduleConfig.setAlgorithmClass(LLPAlgorithm.class);
		moduleConfig.registerAggregator(LLPAlgorithm.GLOBAL_CHANGE_AGGREGATOR,
				BooleanOrAggregator.class);
	}

}

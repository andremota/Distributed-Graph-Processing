package pt.isel.ps1314v.g11.allp.hama;

import java.security.InvalidParameterException;

import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;

import pt.isel.ps1314v.g11.allp.ALLPAlgorithm;
import pt.isel.ps1314v.g11.allp.hama.io.ALLPVertexInputReader;
import pt.isel.ps1314v.g11.allp.util.ALLPConfig;
import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.hama.config.HamaModuleJobRunner;

public class ALLPHamaRunner extends HamaModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new ALLPConfig();
	}

	@Override
	public void prepareJob(GraphJob job, CommonConfig moduleConfig, JobBean bean) {

		ALLPConfig llpConf = (ALLPConfig) bean;
		//job.setJobName(LLPAlgorithm.class.getSimpleName());
		
		job.setVertexInputReaderClass(ALLPVertexInputReader.class);
		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		
		if(!(llpConf.getDecisionFactor() >= 0 && llpConf.getDecisionFactor() <= 1)){
			throw new InvalidParameterException("Decision factor cannot be lesser than 0 or greater than 1.");
		}
		moduleConfig.setFloat(ALLPAlgorithm.DECISION_FACTOR, llpConf.getDecisionFactor());
		moduleConfig.setBoolean(ALLPAlgorithm.COUNT_VERTEX_AS_OWN_NEIGHBOR, llpConf.getCountVertexNg());
		
		moduleConfig.setAlgorithmClass(ALLPAlgorithm.class);
		moduleConfig.registerAggregator(ALLPAlgorithm.GLOBAL_CHANGE_AGGREGATOR,
				BooleanOrAggregator.class);
	}

}

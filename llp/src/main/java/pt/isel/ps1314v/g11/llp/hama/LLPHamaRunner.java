package pt.isel.ps1314v.g11.llp.hama;

import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;

import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.hama.config.HamaModuleJobRunner;
import pt.isel.ps1314v.g11.llp.LLPAlgorithm;
import pt.isel.ps1314v.g11.llp.hama.io.LLPVertexInputReader;

public class LLPHamaRunner extends HamaModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new JobBean();
	}

	@Override
	public void prepareJob(GraphJob job, CommonConfig moduleConfig, JobBean bean) {
		
		//job.setJobName(LLPAlgorithm.class.getSimpleName());
		
		job.setVertexInputReaderClass(LLPVertexInputReader.class);
		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		
		moduleConfig.setAlgorithmClass(LLPAlgorithm.class);
		moduleConfig.registerAggregator(LLPAlgorithm.GLOBAL_CHANGE_AGGREGATOR,
				BooleanOrAggregator.class);
	}

}

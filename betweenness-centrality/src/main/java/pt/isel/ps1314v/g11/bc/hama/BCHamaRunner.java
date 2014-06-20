package pt.isel.ps1314v.g11.bc.hama;

import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;

import pt.isel.ps1314v.g11.bc.BetweennessCentralityAlgorithm;
import pt.isel.ps1314v.g11.bc.hama.io.BCVertexInputReader;
import pt.isel.ps1314v.g11.bc.util.BCJobBean;
import pt.isel.ps1314v.g11.common.aggregator.BooleanAndAggregator;
import pt.isel.ps1314v.g11.common.aggregator.DoubleMaxAggregator;
import pt.isel.ps1314v.g11.common.aggregator.DoubleMinAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.hama.config.HamaModuleJobRunner;

public class BCHamaRunner extends HamaModuleJobRunner{

	public BCHamaRunner(Class<?> main) {
		super(main);
	}

	@Override
	public JobBean createJobBean() {
		return new BCJobBean();
	}

	@Override
	public void prepareJob(GraphJob job, CommonConfig moduleConfig, JobBean bean) {
		BCJobBean config = (BCJobBean)bean;
		job.setVertexInputReaderClass(BCVertexInputReader.class);
		 
		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		
		//job.setPartitioner(HashPartitioner.class);
		
		moduleConfig.setAlgorithmClass(BetweennessCentralityAlgorithm.class);
		moduleConfig.setStrings(BetweennessCentralityAlgorithm.START_VERTEXES,config.getStarts());
		moduleConfig.setBoolean(BetweennessCentralityAlgorithm.NORMALIZE, config.shouldNormalize());
		
		if(config.shouldNormalize()){
			moduleConfig.registerAggregator(BetweennessCentralityAlgorithm.AGG_ENDED, BooleanAndAggregator.class);
			moduleConfig.registerAggregator(BetweennessCentralityAlgorithm.AGG_MIN_BC, DoubleMinAggregator.class);
			moduleConfig.registerAggregator(BetweennessCentralityAlgorithm.AGG_MAX_BC, DoubleMaxAggregator.class);
		}
	}

}

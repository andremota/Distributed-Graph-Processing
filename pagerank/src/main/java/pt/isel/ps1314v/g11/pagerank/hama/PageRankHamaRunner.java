package pt.isel.ps1314v.g11.pagerank.hama;

import org.apache.hadoop.conf.Configuration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.hama.config.HamaModuleJobRunner;
import pt.isel.ps1314v.g11.heatkernel.RandomWalkAlgorithm;
import pt.isel.ps1314v.g11.heatkernel.hama.io.RandomWalkVertexInputReader;
import pt.isel.ps1314v.g11.heatkernel.util.RandomWalkConfig;
import pt.isel.ps1314v.g11.pagerank.PageRankAlgorithm;

public class PageRankHamaRunner extends HamaModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new RandomWalkConfig();
	}

	@Override
	public void prepareJob(GraphJob job, CommonConfig moduleConfig, JobBean bean) {
		RandomWalkConfig argsConfig = (RandomWalkConfig)bean;
		Configuration conf = job.getConfiguration();
		
		job.setVertexInputReaderClass(RandomWalkVertexInputReader.class);
		
		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		
		job.setPartitioner(HashPartitioner.class);
		
		
		moduleConfig.setAlgorithmClass(PageRankAlgorithm.class);
		moduleConfig.setBoolean(RandomWalkAlgorithm.VERTEX_VALUE_INITIAL_PROBABILITY,
				argsConfig.useVertexValueAsInitialProbability());
		moduleConfig.setInt(RandomWalkAlgorithm.MAX_SUPERSTEPS_CONF, argsConfig.getNumberOfSupersteps());
		if(argsConfig.getFactor() != 0){
			conf.setFloat(RandomWalkAlgorithm.JUMP_FACTOR_CONF, argsConfig.getFactor());
		}
	}

}

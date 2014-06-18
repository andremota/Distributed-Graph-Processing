package pt.isel.ps1314v.g11.llp.giraph;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.IdWithValueTextOutputFormat;
import org.apache.giraph.job.GiraphJob;

import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleJobRunner;
import pt.isel.ps1314v.g11.llp.LLPAlgorithm;
import pt.isel.ps1314v.g11.llp.giraph.io.AdjacencyListWithValuesInputFormat;

public class LLPGiraphModuleJobRunner extends GiraphModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new JobBean();
	}

	@Override
	public void prepareJob(GiraphJob job, GiraphConfiguration conf,
			CommonConfig commonConfig, JobBean bean) {
		conf.setVertexInputFormatClass(AdjacencyListWithValuesInputFormat.class);
		conf.setVertexOutputFormatClass(IdWithValueTextOutputFormat.class);
		commonConfig.registerAggregator(LLPAlgorithm.GLOBAL_CHANGE_AGGREGATOR, BooleanOrAggregator.class);
		commonConfig.setAlgorithmClass(LLPAlgorithm.class);
	}

}

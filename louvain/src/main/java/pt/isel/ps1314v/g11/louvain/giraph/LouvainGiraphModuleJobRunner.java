package pt.isel.ps1314v.g11.louvain.giraph;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.job.GiraphJob;

import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.aggregator.LongSumAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleJobRunner;
import pt.isel.ps1314v.g11.louvain.LouvainAlgorithm;
import pt.isel.ps1314v.g11.louvain.giraph.io.AdjacencyListLouvainInputFormat;
import pt.isel.ps1314v.g11.louvain.giraph.io.JsonLouvainOutputFormat;

public class LouvainGiraphModuleJobRunner extends GiraphModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new JobBean();
	}

	@Override
	public void prepareJob(GiraphJob job, GiraphConfiguration conf,
			CommonConfig commonConfig, JobBean bean) {
		conf.setVertexInputFormatClass(AdjacencyListLouvainInputFormat.class);
		conf.setVertexOutputFormatClass(JsonLouvainOutputFormat.class);
		commonConfig.setAlgorithmClass(LouvainAlgorithm.class);
		
		commonConfig.registerAggregator(LouvainAlgorithm.AGG_M2, LongSumAggregator.class);
		commonConfig.registerAggregator(LouvainAlgorithm.CHANGE_AGG, BooleanOrAggregator.class);
	}

}

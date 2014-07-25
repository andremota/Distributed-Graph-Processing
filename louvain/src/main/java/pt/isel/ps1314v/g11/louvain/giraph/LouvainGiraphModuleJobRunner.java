package pt.isel.ps1314v.g11.louvain.giraph;

import org.apache.giraph.conf.GiraphConfiguration;

import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.aggregator.LongSumAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleJobRunner;
import pt.isel.ps1314v.g11.louvain.LouvainAlgorithm;
import pt.isel.ps1314v.g11.louvain.giraph.io.AdjacencyListLouvainInputFormat;
import pt.isel.ps1314v.g11.louvain.giraph.io.JsonLouvainOutputFormat;
import pt.isel.ps1314v.g11.louvain.util.LouvainJobBean;

/**
 * Louvain Giraph Module Job Runner
 *
 */
public class LouvainGiraphModuleJobRunner extends GiraphModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new LouvainJobBean();
	}

	@Override
	public void prepareJob(GiraphConfiguration conf, CommonConfig commonConfig,
			JobBean _bean) {
		
		LouvainJobBean bean = (LouvainJobBean)_bean;
		conf.setVertexInputFormatClass(AdjacencyListLouvainInputFormat.class);
		conf.setVertexOutputFormatClass(JsonLouvainOutputFormat.class);
		
		commonConfig.setAlgorithmClass(LouvainAlgorithm.class);
		commonConfig.registerAggregator(LouvainAlgorithm.AGG_M2, LongSumAggregator.class);
		commonConfig.registerAggregator(LouvainAlgorithm.CHANGE_AGG, BooleanOrAggregator.class);
		commonConfig.setFloat(LouvainAlgorithm.RESOLUTION, bean.getResolution());
		commonConfig.setFloat(LouvainAlgorithm.MIN_Q, bean.getMinQ());
	}

}

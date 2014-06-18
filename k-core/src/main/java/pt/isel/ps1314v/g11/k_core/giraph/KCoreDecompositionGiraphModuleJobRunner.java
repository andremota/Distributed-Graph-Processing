package pt.isel.ps1314v.g11.k_core.giraph;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.job.GiraphJob;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleJobRunner;
import pt.isel.ps1314v.g11.k_core.KCoreDecompositionAlgorithm;
import pt.isel.ps1314v.g11.k_core.giraph.io.AdjacencyListKCoreInputFormat;
import pt.isel.ps1314v.g11.k_core.giraph.io.JsonKCoreOutputFormat;

public class KCoreDecompositionGiraphModuleJobRunner extends GiraphModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new JobBean();
	}

	@Override
	public void prepareJob(GiraphJob job, GiraphConfiguration conf,
			CommonConfig commonConfig, JobBean bean) {
		conf.setVertexInputFormatClass(AdjacencyListKCoreInputFormat.class);
		conf.setVertexOutputFormatClass(JsonKCoreOutputFormat.class);
		commonConfig.setAlgorithmClass(KCoreDecompositionAlgorithm.class);
	}

}

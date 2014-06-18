package pt.isel.ps1314v.g11.heatkernel.giraph;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.IdWithValueTextOutputFormat;
import org.apache.giraph.job.GiraphJob;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.giraph.config.GiraphModuleJobRunner;
import pt.isel.ps1314v.g11.heatkernel.HeatKernelAlgorithm;
import pt.isel.ps1314v.g11.heatkernel.RandomWalkAlgorithm;
import pt.isel.ps1314v.g11.heatkernel.giraph.io.AdjacencyListWithValuesInputFormat;
import pt.isel.ps1314v.g11.heatkernel.util.RandomWalkConfig;

public class HeatKernelGiraphJobRunner extends GiraphModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new RandomWalkConfig();
	}

	@Override
	public void prepareJob(GiraphJob job, GiraphConfiguration conf,
			CommonConfig commonConfig, JobBean bean) {
		
		RandomWalkConfig argsConfig = (RandomWalkConfig)bean;
		
		conf.setVertexInputFormatClass(AdjacencyListWithValuesInputFormat.class);
		conf.setVertexOutputFormatClass(IdWithValueTextOutputFormat.class);
		commonConfig.setAlgorithmClass(HeatKernelAlgorithm.class);
		
		conf.setInt(RandomWalkAlgorithm.MAX_SUPERSTEPS_CONF, argsConfig.getNumberOfSupersteps());
		
		if(argsConfig.getFactor() > 0 && argsConfig.getFactor() <= 1){
			conf.setFloat(HeatKernelAlgorithm.HEAT_CONF, argsConfig.getFactor());
		}
		
	}

}

package pt.isel.ps1314v.g11.hama.config;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.graph.GraphJob;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.common.config.ModuleJobRunner;

public abstract class HamaModuleJobRunner implements ModuleJobRunner{

	@Override
	public boolean run(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		HamaConfiguration conf = new HamaConfiguration();
		GraphJob job = new GraphJob(conf, HamaModuleJobRunner.class);
		CommonConfig commonConfig = new CommonConfig(new HamaModuleConfiguration(job));

		JobBean bean = createJobBean(args);
		
		
		prepareJob(job,commonConfig);
		
		job.setInputPath(new Path(bean.getInputPath()));
		job.setOutputPath(new Path(bean.getOutputPath()));
		
		commonConfig.preparePlatformConfig();
		

		return job.waitForCompletion(bean.verbose());

	}

	public abstract void prepareJob(GraphJob job, CommonConfig commonConfig);

}

package pt.isel.ps1314v.g11.hama.config;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.bsp.HashPartitioner;
import org.apache.hama.graph.GraphJob;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.common.config.ModuleJobRunner;

/**
 * 
 * Base class for algorithms running in Hama
 *
 */
public abstract class HamaModuleJobRunner implements ModuleJobRunner{
	
	@Override
	public boolean run(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		HamaConfiguration conf = new HamaConfiguration();
		GraphJob job = new GraphJob(conf,HamaModuleJobRunner.class);
		CommonConfig commonConfig = new CommonConfig(new HamaModuleConfiguration(job));

		JobBean bean = createJobBean();
		
		CmdLineParser parser = new CmdLineParser(bean);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.out.println(e.getMessage());
			parser.printExample(OptionHandlerFilter.PUBLIC);
			parser.printUsage(System.out);
			return false;
		}
		
		job.setNumBspTask(bean.getNWorkers());
		job.setPartitioner(HashPartitioner.class);
		prepareJob(job,commonConfig,bean);
		job.setJobName(commonConfig.getAlgorithmClass().getSimpleName());
		job.setInputPath(new Path(bean.getInputPath()));
		job.setOutputPath(new Path(bean.getOutputPath()));
		
		commonConfig.preparePlatformConfig();
		
		return job.waitForCompletion(bean.verbose());

	}

	/**
	 * Make additional configurations here like setting the algorithm class, etc.
	 * @param job GraphJob to set extra Hama specific configurations
	 * @param commonConfig CommonConfig to set things like algorithm or aggregators used.
	 * @param bean Configuration JobBean, the same as the one created in the {@link ModuleJobRunner#createJobBean() createJobBean}
	 * method.
	 */
	public abstract void prepareJob(GraphJob job, CommonConfig commonConfig, JobBean bean);

}

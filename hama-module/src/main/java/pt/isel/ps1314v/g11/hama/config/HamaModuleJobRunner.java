package pt.isel.ps1314v.g11.hama.config;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hama.HamaConfiguration;
import org.apache.hama.graph.GraphJob;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.common.config.ModuleJobRunner;

public abstract class HamaModuleJobRunner implements ModuleJobRunner{

	@Override
	public boolean run(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		HamaConfiguration conf = new HamaConfiguration();
		GraphJob job = new GraphJob(conf, HamaModuleJobRunner.class);
		CommonConfig commonConfig = new CommonConfig(new HamaModuleConfiguration(job));

		JobBean bean = createJobBean();
		
		CmdLineParser parser = new CmdLineParser(bean);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			parser.printExample(OptionHandlerFilter.PUBLIC);
			parser.printUsage(System.out);
			return false;
		}
		
		prepareJob(job,commonConfig,bean);
		
		job.setJobName(commonConfig.getAlglorithmClass().getSimpleName());
		job.setInputPath(new Path(bean.getInputPath()));
		job.setOutputPath(new Path(bean.getOutputPath()));
		
		commonConfig.preparePlatformConfig();
		

		return job.waitForCompletion(bean.verbose());

	}

	public abstract void prepareJob(GraphJob job, CommonConfig commonConfig, JobBean bean);

}

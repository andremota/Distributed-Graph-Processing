package pt.isel.ps1314v.g11.giraph.config;

import java.io.IOException;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.formats.GiraphFileInputFormat;
import org.apache.giraph.job.GiraphJob;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.common.config.ModuleJobRunner;
import pt.isel.ps1314v.g11.common.graph.Algorithm;

/**
 * 
 * Base class for algorithms running in Giraph
 *
 */
public abstract class GiraphModuleJobRunner implements ModuleJobRunner {

	@Override
	public boolean run(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {

		GiraphConfiguration conf = new GiraphConfiguration();
		GiraphModuleConfiguration giraphConfig = new GiraphModuleConfiguration(
				conf);
		CommonConfig commonConfig = new CommonConfig(giraphConfig);

		GiraphJob job = new GiraphJob(conf, Algorithm.class.getSimpleName());

		JobBean bean = createJobBean();
		CmdLineParser parser = new CmdLineParser(bean);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.out.println(e.getMessage());
			parser.printUsage(System.out);
			return false;
		}
		
		conf.setBoolean("giraph.useSuperstepCounters", bean.useSuperstepCounters());
		
		conf.setWorkerConfiguration(bean.getNWorkers(), bean.getNWorkers(), 100);
		conf.setYarnTaskHeapMb(bean.getHeapSpace());
		if(bean.local() && bean.getNWorkers() > 0)
			conf.set("giraph.SplitMasterWorker", "false");
		prepareJob(conf, commonConfig, bean);
		job.setJobName(commonConfig.getAlgorithmClass().getSimpleName());
		commonConfig.preparePlatformConfig();
		
		GiraphFileInputFormat.setVertexInputPath(conf, new Path(bean.getInputPath()));
		FileOutputFormat.setOutputPath(job.getInternalJob(), new Path(bean.getOutputPath()));
		
		return job.run(bean.verbose());
	}

	/**
	 * Make additional configurations here like setting the algorithm class, etc.
	 * @param conf GiraphConfiguration to set extra Giraph specific configurations.
	 * @param commonConfig CommonConfig to set things like algorithm or aggregators used.
	 * @param bean Configuration JobBean, the same as the one created in the {@link ModuleJobRunner#createJobBean() createJobBean}
	 * method.
	 */
	public abstract void prepareJob(GiraphConfiguration conf, CommonConfig commonConfig,
			JobBean bean);
}

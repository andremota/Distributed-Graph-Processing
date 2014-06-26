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
			parser.printUsage(System.out);
			return false;
		}
		
		conf.setWorkerConfiguration(bean.getNWorkers(), bean.getNWorkers(), 100);
		conf.setYarnTaskHeapMb(bean.getHeapSpace());
		if(bean.local() && bean.getNWorkers() > 0)
			conf.set("giraph.SplitMasterWorker", "false");
		prepareJob(job, conf, commonConfig, bean);
		job.setJobName(commonConfig.getAlglorithmClass().getSimpleName());
		commonConfig.preparePlatformConfig();
		
		GiraphFileInputFormat.addVertexInputPath(conf, new Path(bean.getInputPath()));
		FileOutputFormat.setOutputPath(job.getInternalJob(), new Path(bean.getOutputPath()));

		return job.run(bean.verbose());
	}

	public abstract void prepareJob(GiraphJob job, GiraphConfiguration conf,
			CommonConfig commonConfig, JobBean bean);
}

package pt.isel.ps1314v.g11.giraph.config;

import java.io.IOException;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.job.GiraphJob;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.common.config.ModuleJobRunner;

public abstract class GiraphModuleJobRunner implements ModuleJobRunner {

	@Override
	public boolean run(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {

		GiraphConfiguration conf = new GiraphConfiguration();
		GiraphModuleConfiguration giraphConfig = new GiraphModuleConfiguration(
				conf);
		CommonConfig commonConfig = new CommonConfig(giraphConfig);

		GiraphJob job = new GiraphJob(conf, commonConfig.getAlglorithmClass()
				.getSimpleName());

		JobBean bean = createJobBean();
		CmdLineParser parser = new CmdLineParser(bean);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			throw new IOException(e);
		}
		
		prepareJob(job, conf, commonConfig, bean);
		
		FileInputFormat.setInputPath(job.getInternalJob(), new Path(bean.getInputPath()));
		FileOutputFormat.setOutputPath(job.getInternalJob(), new Path(bean.getOutputPath()));

		return job.run(bean.verbose());
	}

	public abstract void prepareJob(GiraphJob job, GiraphConfiguration conf,
			CommonConfig commonConfig, JobBean bean);
}

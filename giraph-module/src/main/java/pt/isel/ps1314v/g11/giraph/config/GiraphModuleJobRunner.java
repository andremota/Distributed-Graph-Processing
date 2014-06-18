package pt.isel.ps1314v.g11.giraph.config;

import java.io.IOException;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.job.GiraphJob;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.ModuleJobRunner;

public abstract class GiraphModuleJobRunner implements ModuleJobRunner{

	@Override
	public boolean run(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		GiraphConfiguration conf = new GiraphConfiguration();
		GiraphModuleConfiguration giraphConfig = new GiraphModuleConfiguration(conf);
		CommonConfig commonConfig = new CommonConfig(giraphConfig);
		
		GiraphJob job = new GiraphJob(conf, commonConfig.getAlglorithmClass().getSimpleName());
		prepareJob(job,conf,commonConfig);
		
		return job.run(Boolean.parseBoolean(args[0]));
	}

	
	public abstract void  prepareJob(GiraphJob job, GiraphConfiguration conf, CommonConfig commonConfig);
}

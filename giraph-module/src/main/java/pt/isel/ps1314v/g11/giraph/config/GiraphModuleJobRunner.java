package pt.isel.ps1314v.g11.giraph.config;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.job.GiraphJob;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.ModuleJobRunner;

public abstract class GiraphModuleJobRunner implements ModuleJobRunner{

	@Override
	public boolean run(String[] args) {
		
		GiraphConfiguration conf = new GiraphConfiguration();
		GiraphModuleConfiguration giraphConfig = new GiraphModuleConfiguration(conf);
		CommonConfig commonConfig = new CommonConfig(giraphConfig);
		prepareConfig(conf,commonConfig);
		
		//GiraphJob job = new GiraphJob(conf.get, giraphConfig);
		
		return false;
	}

	
	public abstract void  prepareConfig(GiraphConfiguration conf, CommonConfig commonConfig);
}

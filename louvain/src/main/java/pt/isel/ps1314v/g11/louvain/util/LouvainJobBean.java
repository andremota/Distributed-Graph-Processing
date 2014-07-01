package pt.isel.ps1314v.g11.louvain.util;

import org.kohsuke.args4j.Option;

import pt.isel.ps1314v.g11.common.config.JobBean;

public class LouvainJobBean extends JobBean{

	@Option(name="-r", usage="Sets the resolution in Louvain. <1 means more and smaller communities, >1 means bigger and greater"
			, metaVar="res", required=true)
	private float resolution = 1f;
	
	public float getResolution(){
		return resolution;
	}
	
	@Option(name="-q", usage="Sets the minimun delta q."
			, metaVar="min", required=true)
	private float minQ = 0.0000001f;
	
	public float getMinQ(){
		return minQ;
	}
}

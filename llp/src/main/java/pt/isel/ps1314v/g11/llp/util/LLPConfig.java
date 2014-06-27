package pt.isel.ps1314v.g11.llp.util;

import org.kohsuke.args4j.Option;

import pt.isel.ps1314v.g11.common.config.JobBean;

public class LLPConfig extends JobBean{
	@Option(name="-d", usage="Sets the decision factor in LLP. 0<=factor<=1"
			, metaVar="factor", required=true)
	private float factor;
	
	public float getDecisionFactor(){
		return factor;
	}
}

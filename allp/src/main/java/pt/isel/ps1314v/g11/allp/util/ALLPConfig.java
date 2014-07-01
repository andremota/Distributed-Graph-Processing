package pt.isel.ps1314v.g11.allp.util;

import org.kohsuke.args4j.Option;

import pt.isel.ps1314v.g11.llp.util.LLPConfig;

public class ALLPConfig extends LLPConfig{
	@Option(name="-cn", usage="Count vertex as his own neighbor.", required = true)
	private boolean countVertexNg;
	
	public boolean getCountVertexNg(){
		return countVertexNg;
	}
}

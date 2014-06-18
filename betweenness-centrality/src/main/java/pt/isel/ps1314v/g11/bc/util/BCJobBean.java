package pt.isel.ps1314v.g11.bc.util;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import pt.isel.ps1314v.g11.common.config.JobBean;

public class BCJobBean extends JobBean {

	@Option(name="-s", usage="Sets the start vertices for Betweenness Centrality, space separated list of integers."
			+ " Default is all vertices"
			, metaVar = "startsList", handler=StringArrayOptionHandler.class)
	private String[] bcStart;
	
	@Option(name="-n", usage="Use to see a normalized output for Betweenness Centrality"
			, handler=BooleanOptionHandler.class)
	private boolean bcNormal;
	
	
	public String[] getStarts(){
		return bcStart==null?new String[]{}:bcStart;
	}
	
	public boolean shouldNormalize(){
		return bcNormal;
	}
	
	public static BCJobBean parseArgs(String[] args) throws CmdLineException{
		BCJobBean config = new BCJobBean();
		CmdLineParser parser = new CmdLineParser(config);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.out.println(e.getMessage());
			parser.printUsage(System.out);
			throw e;
		}
		return config;
	}
	
}

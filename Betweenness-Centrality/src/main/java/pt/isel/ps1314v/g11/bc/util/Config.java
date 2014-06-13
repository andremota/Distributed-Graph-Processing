package pt.isel.ps1314v.g11.bc.util;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

public class Config {

	@Option(name="-s", usage="Sets the start vertices for Betweenness Centrality, space separated list of integers."
			+ " Default is all vertices"
			, metaVar = "startsList", handler=StringArrayOptionHandler.class)
	private String[] bcStart;
	
	@Option(name="-n", usage="Use to see a normalized output for Betweenness Centrality"
			, handler=BooleanOptionHandler.class)
	private boolean bcNormal;
	
	@Argument(index = 0,usage="Sets the input file.", metaVar = "in")
	private String inFile;
	
	@Argument(index = 1,usage="Sets the output file.", metaVar = "out")
	private String outFile;
	
	
	public String getOutputFile(){
		return outFile;
	}
	
	public String getInputFile(){
		return inFile;
	}
	
	public String[] getStarts(){
		return bcStart==null?new String[]{}:bcStart;
	}
	
	public boolean shouldNormalize(){
		return bcNormal;
	}
	public static Config parseArgs(String[] args) throws CmdLineException{
		Config config = new Config();
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

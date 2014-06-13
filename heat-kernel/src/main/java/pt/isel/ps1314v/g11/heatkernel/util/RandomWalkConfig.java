package pt.isel.ps1314v.g11.heatkernel.util;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class RandomWalkConfig {

	@Option(name="-ns", usage="Set the max number of supersteps in Pagerank or Heatkernel"
			, metaVar="nSuper")
	public int numberOfSupersteps = 30;

	@Option(name="-f", usage="Sets the jumping factor in Pagerank or the heat in Heatkernel"
			, metaVar="factor")
	public float factor;
	
	@Argument(usage="Sets the input file.", metaVar = "in")
	private String inFile;
	
	@Argument(usage="Sets the output file.", metaVar = "out")
	private String outFile;
	
	public static RandomWalkConfig parseArgs(String[] args) throws CmdLineException{
		RandomWalkConfig config = new RandomWalkConfig();
		CmdLineParser parser = new CmdLineParser(config);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			parser.printUsage(System.out);
			throw e;
		}
		return config;
	}
}

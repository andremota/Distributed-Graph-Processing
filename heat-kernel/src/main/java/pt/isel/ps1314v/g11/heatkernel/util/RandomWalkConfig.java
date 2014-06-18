package pt.isel.ps1314v.g11.heatkernel.util;

import java.util.Arrays;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import pt.isel.ps1314v.g11.common.config.JobBean;

public class RandomWalkConfig extends JobBean{

	@Option(name="-ns", usage="Set the max number of supersteps in Pagerank or Heatkernel"
			, metaVar="nSuper")
	private int numberOfSupersteps = 30;

	@Option(name="-f", usage="Sets the jumping factor in Pagerank or the heat in Heatkernel"
			, metaVar="factor")
	private float factor;
	
	public int getNumberOfSupersteps(){
		return numberOfSupersteps;
	}
	
	public float getFactor(){
		return factor;
	}
	
	public static RandomWalkConfig parseArgs(String[] args) throws CmdLineException{
		RandomWalkConfig config = new RandomWalkConfig();
		System.out.println(Arrays.toString(args));
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

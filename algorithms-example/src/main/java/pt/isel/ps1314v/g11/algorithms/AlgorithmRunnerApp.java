package pt.isel.ps1314v.g11.algorithms;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import pt.isel.ps1314v.g11.algorithms.commands.AlgorithmRunnerChooser;
import pt.isel.ps1314v.g11.algorithms.commands.Config;


public class AlgorithmRunnerApp {
	
	
	public static void main(String[] args) throws Throwable{
		Config bean = new Config();
		AlgorithmRunnerChooser runner = new AlgorithmRunnerChooser();
		CmdLineParser parser = new CmdLineParser(bean);
		try {
			parser.parseArgument(args);
			runner.run(bean);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			parser.printUsage(System.out);
		}
	
	}
} 

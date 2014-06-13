package pt.isel.ps1314v.g11.algorithms;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

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
			System.out.println(" java -jar alg plat"+parser.printExample(OptionHandlerFilter.PUBLIC));
			parser.printUsage(System.out);

			//runner.getDriver().driver(new String[]{}); //force to show list
		}
	
	}
} 

package pt.isel.ps1314v.g11.algorithms;

import java.io.IOException;
import java.util.Arrays;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import pt.isel.ps1314v.g11.algorithms.commands.AlgorithmModuleRunnerChooser;
import pt.isel.ps1314v.g11.algorithms.commands.BeanChooser;


public class AlgorithmRunnerApp {
	
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException{
		BeanChooser bean = new BeanChooser();
		CmdLineParser parser = new CmdLineParser(bean);
		
		if(args.length < 2){
			parser.printUsage(System.out);
			return;
		}
		
		try {
			parser.parseArgument(Arrays.copyOf(args, 2));
			AlgorithmModuleRunnerChooser chooser = new AlgorithmModuleRunnerChooser();
			chooser.run(bean, Arrays.copyOfRange(args, 2, args.length));
		} catch (Exception e) {
			parser.printUsage(System.out);
			return;
		}
		
		
	
	}
} 

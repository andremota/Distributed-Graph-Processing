package pt.isel.ps1314v.g11.algorithms.commands;

import org.kohsuke.args4j.Option;

public class Config {

	public static final int REQUIRED_ELEMS_LEN = 4;
	public static final int TO_REPLICATE = 3;
	
	@Option(name="-p", usage="Platform to run the specified algorithm.", required=true)
	private String platform;

	@Option(name="-a", usage="Sets the algorithm that will be executed.", required=true)
	private String algorithm;
	
	@Option(name="-i", usage="Sets the input file.", required=true)
	private String inFile;
	
	@Option(name="-o", usage="Sets the output file.", required=true)
	private String outFile;
	
	public String getDriverKey(){
		return getPlatform()+getAlgorithmName(); //creates the driver key
	}
	
	public String getOutputFile(){
		return outFile;
	}
	
	public String getInputFile(){
		return inFile;
	}
	
	public String getPlatform(){
		return platform;
	}
	
	public String getAlgorithmName(){
		return algorithm;
	}
	
	public String[] generateDriverArgs(){
		String [] newArgs = new String[TO_REPLICATE];
		
		newArgs[0] = getDriverKey(); //creates the driver key
		newArgs[1] = inFile; //algorithm input file
		newArgs[2] = outFile; //algorithm output file
		
		return newArgs;
	}
}

package pt.isel.ps1314v.g11.algorithms.commands;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class Config {

	public static final int REQUIRED_ELEMS_LEN = 4;
	public static final int TO_REPLICATE = 3;
	
	
	//@Option(name="-p", usage=)
	
	@Argument(index = 1, required = true, metaVar = "plat",
			usage = "Platform to run the specified algorithm. Either giraph or hama")
	private String platform;

	//@Option(name="-a", usage=" Defaults to pagerank.")
	@Argument(index = 0, required = true, metaVar = "alg",
			usage = "The algorithm that will be executed. Can be: "
			+ "\npagerank"
			+ "\nlouvain"
			+ "\nkCore"
			+ "\nheatkernel"
			+ "\nbc"
			+ "\nllp")
	private String algorithm;
	
	@Option(name="-i", usage="Sets the input file.", metaVar = "in")
	private String inFile;
	
	@Option(name="-o", usage="Sets the output file.", metaVar = "out")
	private String outFile;
	
	@Option(name="-h", usage="Sees the help messages", hidden = true)
	private String help;
	
	public String getDriverKey(){
		return getPlatform()+" "+getAlgorithmName(); //creates the driver key
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

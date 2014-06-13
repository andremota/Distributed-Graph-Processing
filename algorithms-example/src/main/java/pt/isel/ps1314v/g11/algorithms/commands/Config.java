package pt.isel.ps1314v.g11.algorithms.commands;

import java.util.Arrays;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

public class Config {

	public static final int REQUIRED_ELEMS_LEN = 4;
	public static final int TO_REPLICATE = 5;
	private static final String BC = "bc";
	private static final Object HK = "heatkernel";
	private static final Object PR = "pagerank";
	
	
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
	
	@Option(name="-s", usage="Sets the start vertices for Betweenness Centrality, space separated list of integers."
			+ " Default is all vertices"
			, metaVar = "startsList")
	private String bcStart;
	
	@Option(name="-n", usage="Use to see a normalized output for Betweenness Centrality"
			, handler=BooleanOptionHandler.class)
	private boolean bcNormal;
	
	@Option(name="-ns", usage="Set the max number of supersteps in Pagerank or Heatkernel"
			, metaVar="nSuper")
	private int prHkNSup;
	
	@Option(name="-f", usage="Sets the factor in Pagerank or the heat in Heatkernel"
			, metaVar="factor")
	private float prHkFactor;
	
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
		
		int idx = 3;
		if(algorithm.equals(BC)){
			if(bcStart!=null){
				newArgs[idx] = "-s "+bcStart;
				++idx;
			}
			
			if(bcNormal){
				newArgs[idx] = "-n";
				idx++;
			}
			
		} else if(algorithm.equals(HK) || algorithm.equals(PR)){
			if(prHkFactor > 0 && prHkFactor<= 1){
				newArgs[idx] = "-f " + prHkFactor;
				idx++;
			}
			
			if(prHkNSup>0){
				newArgs[idx] = "-ns "  + prHkNSup;
			}
		}
		return newArgs;
	}
}

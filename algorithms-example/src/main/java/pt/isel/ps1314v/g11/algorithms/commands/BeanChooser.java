package pt.isel.ps1314v.g11.algorithms.commands;

import org.kohsuke.args4j.Argument;

public class BeanChooser {
	@Argument(index = 0, required = true, metaVar = "alg",
			usage = "The algorithm that will be executed. Can be: "
			+ "\npagerank"
			+ "\nlouvain"
			+ "\nkcore"
			+ "\nheatkernel"
			+ "\nbc"
			+ "\nllp")
	private String algorithm;
	
	@Argument(index = 1, required = true, metaVar = "plat",
			usage = "Platform to run the specified algorithm. Either giraph or hama")
	private String platform;
	
	public String getPlatform(){
		return platform;
	}
	
	public String getAlgorithm(){
		return algorithm;
	}
}

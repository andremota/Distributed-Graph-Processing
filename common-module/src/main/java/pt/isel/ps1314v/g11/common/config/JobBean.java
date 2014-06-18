package pt.isel.ps1314v.g11.common.config;

import org.kohsuke.args4j.Option;

public class JobBean {
	@Option(name="-v", usage="Uses verbose or not.", metaVar = "in", required = false)
	public boolean verbose=true;
	
	@Option(name="-i", usage="Sets the input file.", metaVar = "in", required = true)
	private String inFile;
	
	@Option(name="-o", usage="Sets the output file.", metaVar = "out", required = true)
	private String outFile;
}

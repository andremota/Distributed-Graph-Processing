package pt.isel.ps1314v.g11.common.config;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

public class JobBean {
	@Option(name = "-v", usage = "Uses verbose or not.", metaVar = "in", required = false, handler = BooleanOptionHandler.class)
	private boolean verbose = false;

	@Option(name = "-i", usage = "Sets the input file.", metaVar = "in", required = true)
	private String inFile;

	@Option(name = "-o", usage = "Sets the output file.", metaVar = "out", required = true)
	private String outFile;

	@Option(name = "-w", usage = "Sets the number of workers/tasks", metaVar = "nWrks", required = false)
	private int workers;
	
	@Option(name = "-l", usage = "Running in local mode or not", required = false, handler = BooleanOptionHandler.class)
	private boolean local = false;
	
	public String getInputPath() {
		return inFile;
	}

	public String getOutputPath() {
		return outFile;
	}

	public boolean verbose() {
		return verbose;
	}
	
	public int getNWorkers(){
		return workers;
	}
	
	public boolean local(){
		return local;
	}
}

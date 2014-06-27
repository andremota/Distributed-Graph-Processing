package pt.isel.ps1314v.g11.common.config;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

public class JobBean {
	@Option(name = "-v", usage = "Uses verbose or not.", metaVar = "in", handler = BooleanOptionHandler.class)
	private boolean verbose = false;

	@Option(name = "-i", usage = "Sets the input file.", metaVar = "in", required = true)
	private String inFile;

	@Option(name = "-o", usage = "Sets the output file.", metaVar = "out", required = true)
	private String outFile;

	@Option(name = "-w", usage = "Sets the number of workers/tasks", metaVar = "nWrks", required = true)
	private int workers = 1;
	
	@Option(name = "-l", usage = "Running in local mode or not", handler = BooleanOptionHandler.class)
	private boolean local = false;
	
	@Option(name = "-sc", usage = "Use superstep counters", handler = BooleanOptionHandler.class)
	private boolean useCounter;
	
	@Option(name = "-hp", usage = "Sets the heap sapce", metaVar = "size")
	private int heapSpace = 1024;
	
	public boolean useSuperstepCounters(){
		return useCounter;
	}
	
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
	
	public int getHeapSpace(){
		return heapSpace;
	}
}

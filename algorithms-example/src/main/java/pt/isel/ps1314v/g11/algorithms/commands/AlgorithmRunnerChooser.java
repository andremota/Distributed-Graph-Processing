package pt.isel.ps1314v.g11.algorithms.commands;

import org.apache.hadoop.util.ProgramDriver;

import pt.isel.ps1314v.g11.pagerank.giraph.PageRankGiraphExample;
import pt.isel.ps1314v.g11.pagerank.hama.PageRankHamaExample;

public class AlgorithmRunnerChooser {

	public static final String HAMA_KEY = "hama";
	public static final String GIRAPH_KEY = "giraph";
	
	private ProgramDriver algotihms = new ProgramDriver();
	
	public AlgorithmRunnerChooser() throws Throwable{
		algotihms.addClass(HAMA_KEY+"pagerank", PageRankHamaExample.class, "PageRank hama example.");
		algotihms.addClass(GIRAPH_KEY+"pagerank", PageRankGiraphExample.class, "PageRank hama example.");
	}
	
	public ProgramDriver getDriver(){
		return algotihms;
	}
	
	public void run(Config config) throws Throwable {		
		algotihms.driver(config.generateDriverArgs());
	}
}

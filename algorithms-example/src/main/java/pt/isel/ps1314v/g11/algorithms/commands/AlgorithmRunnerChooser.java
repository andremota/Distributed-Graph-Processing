package pt.isel.ps1314v.g11.algorithms.commands;

import org.apache.hadoop.util.ProgramDriver;

import pt.isel.ps1314v.g11.bc.giraph.BCInGiraphExample;
import pt.isel.ps1314v.g11.bc.hama.BCInHamaExample;
import pt.isel.ps1314v.g11.heatkernel.giraph.HeatKernelGiraphExample;
import pt.isel.ps1314v.g11.heatkernel.hama.HeatKernelHamaExample;
import pt.isel.ps1314v.g11.k_core.giraph.KCoreDecompositionInGiraphExample;
import pt.isel.ps1314v.g11.k_core.hama.KCoreDecompositionInHamaExample;
import pt.isel.ps1314v.g11.llp.giraph.LLPGiraphExample;
import pt.isel.ps1314v.g11.llp.hama.LLPHamaExample;
import pt.isel.ps1314v.g11.louvain.giraph.LouvainGiraphExample;
import pt.isel.ps1314v.g11.louvain.hama.LouvainInHamaExample;
import pt.isel.ps1314v.g11.pagerank.giraph.PageRankGiraphExample;
import pt.isel.ps1314v.g11.pagerank.hama.PageRankHamaExample;

public class AlgorithmRunnerChooser {

	public static final String HAMA_KEY = "hama";
	public static final String GIRAPH_KEY = "giraph";
	
	private ProgramDriver algorithms = new ProgramDriver();
	
	public AlgorithmRunnerChooser() throws Throwable{
		algorithms.addClass(HAMA_KEY+" pagerank", PageRankHamaExample.class, "PageRank Hama example.");
		algorithms.addClass(GIRAPH_KEY+" pagerank", PageRankGiraphExample.class, "PageRank Giraph example.");
		algorithms.addClass(HAMA_KEY+" louvain", LouvainInHamaExample.class, "Louvain Hama example");
		algorithms.addClass(GIRAPH_KEY+" louvain", LouvainGiraphExample.class, "Louvain Giraph example");
		algorithms.addClass(HAMA_KEY+" kcore", KCoreDecompositionInHamaExample.class, "k-Core Decomposition Hama example");
		algorithms.addClass(GIRAPH_KEY+" kcore", KCoreDecompositionInGiraphExample.class, "k-Core Decomposition Giraph example");
		algorithms.addClass(HAMA_KEY+" heatkernel", HeatKernelHamaExample.class, "Heat Kernel Hama example");
		algorithms.addClass(GIRAPH_KEY+" heatkernel", HeatKernelGiraphExample.class, "Heat Kernel Giraph example");
		algorithms.addClass(HAMA_KEY+" bc", BCInHamaExample.class, "Betweenness Centrality Hama example");
		algorithms.addClass(GIRAPH_KEY+" bc", BCInGiraphExample.class, "Betweenness Centrality Giraph example");
		algorithms.addClass(HAMA_KEY+" llp", LLPHamaExample.class, "Layered Label Propagation Hama example");
		algorithms.addClass(GIRAPH_KEY+" llp", LLPGiraphExample.class, "Layered Label Propagation Giraph example");

	}
	
	public ProgramDriver getDriver(){
		return algorithms;
	}
	
	public void run(Config config) throws Throwable {		
		algorithms.driver(config.generateDriverArgs());
	}
}

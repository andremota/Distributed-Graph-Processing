package pt.isel.ps1314v.g11.algorithms.commands;

import static pt.isel.ps1314v.g11.algorithms.commands.AlgorithmRunnerChooser.GIRAPH_KEY;
import static pt.isel.ps1314v.g11.algorithms.commands.AlgorithmRunnerChooser.HAMA_KEY;

import java.io.IOException;
import java.util.HashMap;

import pt.isel.ps1314v.g11.algorithms.AlgorithmRunnerApp;
import pt.isel.ps1314v.g11.bc.giraph.BCGiraphModuleJobRunner;
import pt.isel.ps1314v.g11.bc.hama.BCHamaRunner;
import pt.isel.ps1314v.g11.common.config.ModuleJobRunner;
import pt.isel.ps1314v.g11.heatkernel.giraph.HeatKernelGiraphJobRunner;
import pt.isel.ps1314v.g11.heatkernel.hama.HeatKernelHamaRunner;
import pt.isel.ps1314v.g11.k_core.giraph.KCoreDecompositionGiraphModuleJobRunner;
import pt.isel.ps1314v.g11.k_core.hama.KCoreHamaRunner;
import pt.isel.ps1314v.g11.llp.giraph.LLPGiraphModuleJobRunner;
import pt.isel.ps1314v.g11.llp.hama.LLPHamaRunner;
import pt.isel.ps1314v.g11.louvain.giraph.LouvainGiraphModuleJobRunner;
import pt.isel.ps1314v.g11.louvain.hama.LouvainHamaRunner;
import pt.isel.ps1314v.g11.pagerank.giraph.PageRankGiraphJobRunner;
import pt.isel.ps1314v.g11.pagerank.hama.PageRankHamaRunner;

public class AlgorithmModuleRunnerChooser{
	
	private HashMap<String, ModuleJobRunner> runners = new HashMap<String, ModuleJobRunner>(){
		private static final long serialVersionUID = 1L;
	{
		put(HAMA_KEY+" pagerank",  new PageRankHamaRunner(AlgorithmRunnerApp.class));
		put(GIRAPH_KEY+" pagerank", new PageRankGiraphJobRunner());
		put(HAMA_KEY+" louvain", new LouvainHamaRunner(AlgorithmRunnerApp.class));
		put(GIRAPH_KEY+" louvain", new LouvainGiraphModuleJobRunner());
		put(HAMA_KEY+" kcore", new KCoreHamaRunner(AlgorithmRunnerApp.class));
		put(GIRAPH_KEY+" kcore", new KCoreDecompositionGiraphModuleJobRunner());
		put(HAMA_KEY+" heatkernel", new HeatKernelHamaRunner(AlgorithmRunnerApp.class));
		put(GIRAPH_KEY+" heatkernel", new HeatKernelGiraphJobRunner());
		put(GIRAPH_KEY+" bc", new BCGiraphModuleJobRunner());
		put(HAMA_KEY+" bc", new BCHamaRunner(AlgorithmRunnerApp.class));
		put(HAMA_KEY+" llp", new LLPHamaRunner(AlgorithmRunnerApp.class));
		put(GIRAPH_KEY+" llp", new LLPGiraphModuleJobRunner());
	}};
	
	public void run(BeanChooser chooser, String args[]) throws ClassNotFoundException, IOException, InterruptedException{
		runners.get(chooser.getPlatform()+" "+chooser.getAlgorithm()).run(args);
	}
}

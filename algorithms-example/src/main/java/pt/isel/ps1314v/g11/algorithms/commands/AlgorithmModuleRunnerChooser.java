package pt.isel.ps1314v.g11.algorithms.commands;

import java.io.IOException;
import java.util.HashMap;

import pt.isel.ps1314v.g11.allp.giraph.ALLPGiraphModuleJobRunner;
import pt.isel.ps1314v.g11.allp.hama.ALLPHamaRunner;
import pt.isel.ps1314v.g11.bc.giraph.BCGiraphModuleJobRunner;
import pt.isel.ps1314v.g11.bc.hama.BCHamaRunner;
import pt.isel.ps1314v.g11.common.config.ModuleJobRunner;
import pt.isel.ps1314v.g11.heatkernel.giraph.HeatKernelGiraphJobRunner;
import pt.isel.ps1314v.g11.heatkernel.hama.HeatKernelHamaRunner;
import pt.isel.ps1314v.g11.k_core.giraph.KCoreDecompositionGiraphModuleJobRunner;
import pt.isel.ps1314v.g11.k_core.hama.KCoreHamaRunner;
import pt.isel.ps1314v.g11.louvain.giraph.LouvainGiraphModuleJobRunner;
import pt.isel.ps1314v.g11.louvain.hama.LouvainHamaRunner;
import pt.isel.ps1314v.g11.pagerank.giraph.PageRankGiraphJobRunner;
import pt.isel.ps1314v.g11.pagerank.hama.PageRankHamaRunner;

public class AlgorithmModuleRunnerChooser{
	
	public static final String HAMA_KEY = "hama";
	public static final String GIRAPH_KEY = "giraph";
	
	private HashMap<String, ModuleJobRunner> runners = new HashMap<String, ModuleJobRunner>(){
		private static final long serialVersionUID = 1L;
	{
		put(HAMA_KEY+" pagerank",  new PageRankHamaRunner());
		put(GIRAPH_KEY+" pagerank", new PageRankGiraphJobRunner());
		put(HAMA_KEY+" louvain", new LouvainHamaRunner());
		put(GIRAPH_KEY+" louvain", new LouvainGiraphModuleJobRunner());
		put(HAMA_KEY+" kcore", new KCoreHamaRunner());
		put(GIRAPH_KEY+" kcore", new KCoreDecompositionGiraphModuleJobRunner());
		put(HAMA_KEY+" heatkernel", new HeatKernelHamaRunner());
		put(GIRAPH_KEY+" heatkernel", new HeatKernelGiraphJobRunner());
		put(GIRAPH_KEY+" bc", new BCGiraphModuleJobRunner());
		put(HAMA_KEY+" bc", new BCHamaRunner());
		put(HAMA_KEY+" allp", new ALLPHamaRunner());
		put(GIRAPH_KEY+" allp", new ALLPGiraphModuleJobRunner());
	}};
	
	public boolean run(BeanChooser chooser, String args[]) throws ClassNotFoundException, IOException, InterruptedException{
		String k = chooser.getPlatform()+" "+chooser.getAlgorithm();
		if(runners.containsKey(k)){
			runners.get(k).run(args);
			return true;
		}
		return false;
	}
}

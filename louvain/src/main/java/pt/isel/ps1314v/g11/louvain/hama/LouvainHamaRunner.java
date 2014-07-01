package pt.isel.ps1314v.g11.louvain.hama;

import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;

import pt.isel.ps1314v.g11.common.aggregator.BooleanOrAggregator;
import pt.isel.ps1314v.g11.common.aggregator.LongSumAggregator;
import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.hama.config.HamaModuleJobRunner;
import pt.isel.ps1314v.g11.louvain.LouvainAlgorithm;
import pt.isel.ps1314v.g11.louvain.hama.io.LouvainTextReader;
import pt.isel.ps1314v.g11.louvain.util.LouvainJobBean;

public class LouvainHamaRunner extends HamaModuleJobRunner{

	@Override
	public JobBean createJobBean() {
		return new LouvainJobBean();
	}

	@Override
	public void prepareJob(GraphJob job, CommonConfig moduleConfig, JobBean _bean) {
		LouvainJobBean bean = (LouvainJobBean)_bean;
	    // Vertex reader
		job.setVertexInputReaderClass(LouvainTextReader.class);

		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		//job.setPartitioner(HashPartitioner.class);
		

		moduleConfig.setAlgorithmClass(LouvainAlgorithm.class);
		moduleConfig.registerAggregator(LouvainAlgorithm.AGG_M2, LongSumAggregator.class);
		moduleConfig.registerAggregator(LouvainAlgorithm.CHANGE_AGG, BooleanOrAggregator.class);
		moduleConfig.setFloat(LouvainAlgorithm.RESOLUTION, bean.getResolution());
		moduleConfig.setFloat(LouvainAlgorithm.MIN_Q, bean.getMinQ());
	    
		
		
	}

}

package pt.isel.ps1314v.g11.k_core.hama;

import org.apache.hama.bsp.TextInputFormat;
import org.apache.hama.bsp.TextOutputFormat;
import org.apache.hama.graph.GraphJob;

import pt.isel.ps1314v.g11.common.config.CommonConfig;
import pt.isel.ps1314v.g11.common.config.JobBean;
import pt.isel.ps1314v.g11.hama.config.HamaModuleJobRunner;
import pt.isel.ps1314v.g11.k_core.KCoreDecompositionAlgorithm;
import pt.isel.ps1314v.g11.k_core.hama.io.KCoreTextReader;

public class KCoreHamaRunner extends HamaModuleJobRunner{

	public KCoreHamaRunner(Class<?> main) {
		super(main);
	}

	@Override
	public JobBean createJobBean() {
		return new JobBean();
	}

	@Override
	public void prepareJob(GraphJob job, CommonConfig moduleConfig, JobBean bean) {
		job.setVertexInputReaderClass(KCoreTextReader.class);
		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		
		moduleConfig.setAlgorithmClass(KCoreDecompositionAlgorithm.class);
	}

}

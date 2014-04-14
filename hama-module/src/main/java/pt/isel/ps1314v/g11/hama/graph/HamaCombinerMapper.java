package pt.isel.ps1314v.g11.hama.graph;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.Combiner;

public class HamaCombinerMapper<M extends Writable> extends
		org.apache.hama.bsp.Combiner<M> implements Configurable {

	private Combiner<M> combiner;
	private Configuration conf;

	@Override
	public M combine(Iterable<M> messages) {
		M message = combiner.initialValue();

		for (M m : messages) {
			combiner.combine(message, m);
		}
		return message;
	}

	@Override
	public Configuration getConf() {
		return conf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
		combiner = (Combiner<M>) ReflectionUtils.newInstance(
				conf.getClass(Combiner.COMBINER_CLASS, Combiner.class), conf);
	}

}

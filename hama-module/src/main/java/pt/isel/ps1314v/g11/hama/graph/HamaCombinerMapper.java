package pt.isel.ps1314v.g11.hama.graph;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.Combiner;

/**
 *This class wraps a {@link Combiner}.
 *
 * @param <M> Type of message to be combined.
 */
public class HamaCombinerMapper extends
		org.apache.hama.bsp.Combiner<Writable> implements Configurable {

	private Combiner<Writable> combiner;
	private Configuration conf;

	@Override
	public Writable combine(Iterable<Writable> messages) {
		Writable message = combiner.initialValue();

		for (Writable m : messages) {
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
		combiner = (Combiner<Writable>) ReflectionUtils.newInstance(
				conf.getClass(Combiner.COMBINER_CLASS, Combiner.class), conf);
	}

}

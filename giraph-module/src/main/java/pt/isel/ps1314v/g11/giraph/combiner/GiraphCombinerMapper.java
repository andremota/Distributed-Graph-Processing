package pt.isel.ps1314v.g11.giraph.combiner;

import org.apache.giraph.combiner.MessageCombiner;
import org.apache.giraph.conf.ImmutableClassesGiraphConfigurable;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.Combiner;
import pt.isel.ps1314v.g11.giraph.utils.Variables;

public class GiraphCombinerMapper<I extends WritableComparable<I>, M extends Writable>
		extends MessageCombiner<I, M> implements
		ImmutableClassesGiraphConfigurable<I, Writable, M> {

	private ImmutableClassesGiraphConfiguration<I, Writable, M> conf;
	private Combiner<M> combiner;

	@SuppressWarnings("unchecked")
	@Override
	public void combine(I vertexIndex, M originalMessage, M messageToCombine) {
		if (combiner == null)
			combiner = (Combiner<M>) ReflectionUtils.newInstance(
					conf.getClass(Variables.COMBINER_CLASS, Combiner.class),
					conf);

		combiner.combine(originalMessage, messageToCombine);

	}

	@Override
	public M createInitialMessage() {
		return combiner.initialValue();
	}

	@Override
	public void setConf(
			ImmutableClassesGiraphConfiguration<I, Writable, M> configuration) {
		this.conf = configuration;

	}

	@Override
	public ImmutableClassesGiraphConfiguration<I, Writable, M> getConf() {
		return conf;
	}

}

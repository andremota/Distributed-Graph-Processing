package pt.isel.ps1314v.g11.giraph.graph;

import java.io.IOException;

import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.graph.BasicComputation;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.BasicAlgorithm;
import pt.isel.ps1314v.g11.common.graph.Computation;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

/**
 * 
 *
 * @param <I>
 * @param <E>
 * @param <M>
 */
public class GiraphComputationMapper<I extends WritableComparable<I>, V extends Writable, E extends Writable>
		extends BasicComputation<I, V, E, V> implements Computation<I, V, E> {

	private BasicAlgorithm<I, V, E> algorithm;

	@SuppressWarnings("unchecked")
	@Override
	public void setConf(ImmutableClassesGiraphConfiguration<I, V, E> conf) {
		super.setConf(conf);

		algorithm = (BasicAlgorithm<I, V, E>) ReflectionUtils
				.newInstance(conf.getClass(BasicAlgorithm.ALGORITHM_CLASS,
						BasicAlgorithm.class), conf);

		algorithm.setPlatformComputation(this);
	}

	@Override
	public void sendMessage(I targetVertexId, V message) {
		super.sendMessage(targetVertexId, message);
	}

	@Override
	public void sendMessageToNeighbors(Vertex<I, V, E> vertex, V message) {
		for (Edge<I, E> edge : vertex.getVertexEdges()) {
			super.sendMessage(edge.getTargetVertexId(), message);
		}
	}

	@Override
	public <A extends Writable> void aggregateValue(int index, A value) {
		super.aggregate(Integer.toString(index), value);
	}

	@Override
	public <A extends Writable> A getValueFromAggregator(int index) {
		return super.getAggregatedValue(Integer.toBinaryString(index));
	}

	@Override
	public void compute(org.apache.giraph.graph.Vertex<I, V, E> vertex,
			Iterable<V> messages) throws IOException {
		algorithm.compute(new GiraphVertexMapper<I, V, E>(vertex), messages);
	}

}

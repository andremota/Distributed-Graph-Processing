package pt.isel.ps1314v.g11.giraph.graph;

import java.io.IOException;

import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.graph.BasicComputation;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.isel.ps1314v.g11.common.graph.Algorithm;
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
public class GiraphComputationMapper<I extends WritableComparable<I>, V extends Writable, E extends Writable, M extends Writable>
		extends BasicComputation<I, V, E, M> implements Computation<I, V, E,M> {

	private Algorithm<I, V, E, M> algorithm;

	@SuppressWarnings("unchecked")
	@Override
	public void setConf(ImmutableClassesGiraphConfiguration<I, V, E> conf) {
		super.setConf(conf);

		algorithm = (Algorithm<I, V, E,M>) ReflectionUtils
				.newInstance(conf.getClass(Algorithm.ALGORITHM_CLASS,
						Algorithm.class), conf);

		algorithm.setPlatformComputation(this);
	}

	@Override
	public void sendMessageToVertex(I targetVertexId, M message) {
		super.sendMessage(targetVertexId, message);
	}

	@Override
	public void sendMessageToNeighbors(Vertex<I, V, E> vertex, M message) {
		for (Edge<I, E> edge : vertex.getVertexEdges()) {
			super.sendMessage(edge.getTargetVertexId(), message);
		}
	}

	@Override
	public <A extends Writable> void aggregateValue(String name, A value) {
		super.aggregate(name, value);
	}

	@Override
	public <A extends Writable> A getValueFromAggregator(String name) {
		return super.getAggregatedValue(name);
	}

	@Override
	public void compute(org.apache.giraph.graph.Vertex<I, V, E> vertex,
			Iterable<M> messages) throws IOException {
		algorithm.compute(new GiraphVertexMapper<I, V, E>(vertex), messages);
	}

	@Override
	public long getTotalVertices() {
		return super.getTotalNumVertices();
	}

}

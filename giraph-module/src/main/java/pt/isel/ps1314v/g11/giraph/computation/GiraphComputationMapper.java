package pt.isel.ps1314v.g11.giraph.computation;

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
public class GiraphComputationMapper<I extends WritableComparable<I>, E extends Writable, M extends Writable>
		extends BasicComputation<I, E, M, M> implements Computation<I, E, M> {

	public static final String COMPUTATION_CLASS = "pt.isel.ps1314v.g11.common.graph.Computation";
	
	private Algorithm<I, E, M> algorithm;

	@SuppressWarnings("unchecked")
	@Override
	public void setConf(ImmutableClassesGiraphConfiguration<I, E, M> conf) {
		super.setConf(conf);
		/*
		 * Creates the common computation that was registered with the string
		 * Variables.COMPUTATION_CLASS by reflection
		 */
		algorithm = (Algorithm<I, E, M>) ReflectionUtils.newInstance(
				conf.getClass(COMPUTATION_CLASS, Algorithm.class),
				conf);
		
		algorithm.setPlatformComputation(this);
	}

	@Override
	public void sendMessage(I targetVertexId, M message) {
		super.sendMessage(targetVertexId, message);
	}

	@Override
	public void sendMessageToNeighbors(Vertex<I, E, M> vertex, M message) {
		for(Edge<I, E> edge : vertex.getOutEdges()){
			super.sendMessage(edge.getTargetVertexId(), message);
		}
	}

	@Override
	public void aggregate(int index, M value) {
		super.aggregate(Integer.toString(index), value);
	}

	@Override
	public M getAggregatedValue(int index) {
		return super.getAggregatedValue(Integer.toBinaryString(index));
	}

	@Override
	public void compute(org.apache.giraph.graph.Vertex<I, E, M> arg0,
			Iterable<M> arg1) throws IOException {
		//algorithm.compute(vertex, messages);
	}

}

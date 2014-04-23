package pt.isel.ps1314v.g11.heatkernel;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.common.graph.BasicAlgorithm;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

/**
 * This algorithm is an implementation of a random walk. An algorithm based on a
 * random walk should extends this class.
 *
 */
public abstract class RandomWalkAlgorithm extends
		BasicAlgorithm<LongWritable, DoubleWritable, DoubleWritable> implements
		Configurable {

	public static final String INITIAL_PROBABILITY_CONF = "pt.isel.ps1314v.g11.heatkernel.RandomWalkAlgorithm.jumpFactor";
	public static final String JUMP_FACTOR_CONF = "pt.isel.ps1314v.g11.heatkernel.RandomWalkAlgorithm.initialProbability";
	public static final String MAX_SUPERSTEPS_CONF = "pt.isel.ps1314v.g11.heatkernel.RandomWalkAlgorithm.maxSupersteps";

	private static final float DEFAULT_JUMP_FACTOR = 0.85f;
	private static final int DEFAULT_MAX_SUPERSTEPS = 30;

	private final Logger LOG = Logger.getLogger(RandomWalkAlgorithm.class);
	private final DoubleWritable writable = new DoubleWritable();

	private Configuration conf;

	private float jumpFactor;
	private int maxSuperstep;

	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;

		jumpFactor = conf.getFloat(JUMP_FACTOR_CONF, DEFAULT_JUMP_FACTOR);

		maxSuperstep = conf.getInt(MAX_SUPERSTEPS_CONF, DEFAULT_MAX_SUPERSTEPS);
	}

	@Override
	public Configuration getConf() {
		return conf;
	}

	public float getJumpFactor() {
		return jumpFactor;
	}

	public double getNormalInitialProbability() {
		return 1d / getTotalVertices();
	}

	public double getInitialProbability() {
		float confProb = conf.getFloat(INITIAL_PROBABILITY_CONF, Float.NaN);
		return Float.isNaN(confProb) ? getNormalInitialProbability(): confProb;
	}

	public double stateProbability(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> v) {
		return v.getVertexValue().get() / getEdgeWeigth(v);
	}

	public double getEdgeWeigth(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> v) {

		double w = 0;
		for (Edge<LongWritable, DoubleWritable> e : v.getVertexEdges()) {
			w += e.getValue().get();
		}

		return w;
	}

	@Override
	public void compute(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex,
			Iterable<DoubleWritable> messages) {
		if (getSuperstep() == 0) {
			vertex.setVertexValue(new DoubleWritable(getInitialProbability()));
		} else {
			vertex.getVertexValue().set(recompute(vertex, messages));
		}
		
		LOG.info("compute->superstep " + getSuperstep()+
				" on vertex " + vertex.getId() + " with value "+
				 vertex.getVertexValue() + " and has " + vertex.getNumEdges() +
				" edges with total weight "+ getEdgeWeigth(vertex));

		if (getSuperstep() < maxSuperstep) {
			writable.set(stateProbability(vertex));
			sendMessageToNeighbors(vertex, writable);
		} else {
			LOG.info("halt->superstep " + getSuperstep()+
				" on vertex " + vertex.getId() + " with value "+
				 vertex.getVertexValue() + " and has " + vertex.getNumEdges() +
				" edges with total weight "+ getEdgeWeigth(vertex));
			vertex.voteToHalt();
		}

	}

	/**
	 * 
	 * @param vertex
	 *            - the vertex to calculate in this iteration.
	 * @param messages
	 *            - the messages sent to the vertex in the previous superstep.
	 * @return the new value for the vertex.
	 */
	public abstract double recompute(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex,
			Iterable<DoubleWritable> messages);

}

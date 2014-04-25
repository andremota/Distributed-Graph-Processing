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
	
	public int getMaxSuperstep(){
		return maxSuperstep;
	}

	public abstract double contribution(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> from,
			Edge<LongWritable,DoubleWritable> toEdge);

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
			vertex.setVertexValue(new DoubleWritable(getNormalInitialProbability()));
		} else {
			vertex.getVertexValue().set(recompute(vertex, messages));
		}
		
		LOG.info("compute->superstep " + getSuperstep()+
				" on vertex " + vertex.getId() + " with value "+
				 vertex.getVertexValue() + " and has " + vertex.getNumEdges() +
				" edges with total weight "+ getEdgeWeigth(vertex));

		if (getSuperstep() < maxSuperstep) {
			for(Edge<LongWritable,DoubleWritable> edge : vertex.getVertexEdges()){
				writable.set(contribution(vertex, edge));
				sendMessageToNeighbors(vertex, writable);
			}
		} else {
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

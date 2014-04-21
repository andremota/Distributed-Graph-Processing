package pt.isel.ps1314v.g11.heatkernel;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.graph.BasicAlgorithm;
import pt.isel.ps1314v.g11.common.graph.Vertex;

/**
 * This algorithm is an implementation of a random walk.
 * An algorithm based on a random walk should extends this class.
 *
 */
public abstract class RandomWalkAlgorithm extends BasicAlgorithm<LongWritable, DoubleWritable, DoubleWritable> implements Configurable{
	
	private static final String JUMP_FACTOR_CONF = "pt.isel.ps1314v.g11.heatkernel.RandomWalkAlgorithm.jumpFactor";
	private static final float DEFAULT_JUMP_FACTOR = 0.85f;
	
	private Configuration conf;
	
	private float jumpFactor;

	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
		
		jumpFactor = conf.getFloat(JUMP_FACTOR_CONF, DEFAULT_JUMP_FACTOR);
	}

	@Override
	public Configuration getConf() {
		return conf;
	}
	
	public float getJumpFactor(){
		return jumpFactor;
	}
	  
	public double getNormalInitialProbability(){
		return 1d/getTotalVertices();
	}

	@Override
	public void compute(Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex,Iterable<DoubleWritable> messages) {
		
	}
	
	public abstract void recompute(Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex,Iterable<DoubleWritable> messages);
	
}

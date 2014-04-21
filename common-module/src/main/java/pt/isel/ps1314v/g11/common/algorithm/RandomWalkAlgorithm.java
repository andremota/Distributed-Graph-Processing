package pt.isel.ps1314v.g11.common.algorithm;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.graph.BasicAlgorithm;
import pt.isel.ps1314v.g11.common.graph.Vertex;

/**
 * 
 *
 */
public abstract class RandomWalkAlgorithm extends BasicAlgorithm<LongWritable, DoubleWritable, DoubleWritable> implements Configurable{
	
	private static final String JUMP_FACTOR_CONF = "pt.isel.ps1314v.g11.common.algorithm.RandomWalkAlgorithm.jumpFactor";
	private static final float DEFAULT_JUMP_FACTOR = 0.85f;
	
	private Configuration conf;
	
	private float jumpFactor;

	@Override
	public Configuration getConf() {
		return conf;
	}

	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
		
		jumpFactor = conf.getFloat(JUMP_FACTOR_CONF, DEFAULT_JUMP_FACTOR);
	}

	@Override
	public void compute(Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex,Iterable<DoubleWritable> messages) {
		
	}
	
	public abstract void recompute(Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex,Iterable<DoubleWritable> messages);
	
}

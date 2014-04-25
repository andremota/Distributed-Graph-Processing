package pt.isel.ps1314v.g11.heatkernel;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class HeatKernelAlgorithm extends RandomWalkAlgorithm {

	private static final String HEAT_CONF = "pt.isel.ps1314v.g11.heatkernel.HeatKernelAlgorithm.heat";
	private static final float DEFAULT_HEAT = 1;
	
	private float heat;
	
	@Override
	public void setConf(Configuration conf) {
		super.setConf(conf);
		
		heat = getConf().getFloat(HEAT_CONF, DEFAULT_HEAT);
	}
	
	@Override
	public double recompute(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex,
			Iterable<DoubleWritable> messages) {
		
		double sum = 0;
		for(DoubleWritable w : messages){
			sum += w.get();
		}
		
		return vertex.getVertexValue().get() + sum + getNormalInitialProbability() * (1 -  heat / getMaxSuperstep());
	}

	@Override
	public double contribution(
			Vertex<LongWritable, DoubleWritable, DoubleWritable> from,
			Edge<LongWritable, DoubleWritable> toEdge) {
		return getNormalInitialProbability() * (heat / getMaxSuperstep()) * (toEdge.getValue().get() / getEdgeWeigth(from));
	}

}

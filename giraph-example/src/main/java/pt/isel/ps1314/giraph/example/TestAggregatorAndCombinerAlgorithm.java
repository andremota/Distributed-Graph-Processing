package pt.isel.ps1314.giraph.example;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;

import pt.isel.ps1314v.g11.common.graph.BasicAlgorithm;
import pt.isel.ps1314v.g11.common.graph.Vertex;


public class TestAggregatorAndCombinerAlgorithm extends BasicAlgorithm<LongWritable, DoubleWritable, FloatWritable>{

	@Override
	public void compute(
			Vertex<LongWritable, DoubleWritable, FloatWritable> vertex,
			Iterable<DoubleWritable> messages) {
		if(getSuperstep()==0){
			//aggregate(0, value);
			//ag
		}
		
	}

}

package pt.isel.ps1314.giraph.example;

import java.io.IOException;

import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;

/**
 * 
 *
 */
public class ShortestPathComputation extends BasicComputation<LongWritable, IntWritable, NullWritable, IntWritable>{

	public final String START_VERTEX = "pt.isel.ps1314v.g11.giraph.startVertex";
	public final static int DEFAULT_START_VERTEX = 0;
	public long startVertex;
	
	@Override
	public void setConf(
			ImmutableClassesGiraphConfiguration<LongWritable, IntWritable, NullWritable> conf) {
		startVertex = conf.getLong(START_VERTEX, DEFAULT_START_VERTEX);
	}
	
	@Override
	public void compute(Vertex<LongWritable, IntWritable, NullWritable> vertex,
			Iterable<IntWritable> msgs) throws IOException {
		
		if(getSuperstep() == 0){
			if(vertex.getId().get() == startVertex){
				//O vértice fonte envia mensagem para todos os vértices adjacentes com distância 1.
				sendMessageToAllEdges(vertex, new IntWritable(1));
			}else{
				//A distância inicial do vértice fonte para todos os outros vértices é infinita.
				vertex.setValue(new IntWritable(Integer.MAX_VALUE));
			}
		}
		
		if(getSuperstep() > 0){
			//Apenas vértices que tenham recebido mesangens iram estar acordados.
			IntWritable val = vertex.getValue();
			
			for(IntWritable rcvDistance : msgs){
				//O vértice fica com a distância mínima até ao vértice fonte.
				val.set(Math.min(val.get(), rcvDistance.get()));
			}
			
			//Envia para os vértices adjacentes a distância mínima que tem até ao vértice fonte.
			sendMessageToAllEdges(vertex, val);
			
		}
		
		vertex.voteToHalt();
	}

}

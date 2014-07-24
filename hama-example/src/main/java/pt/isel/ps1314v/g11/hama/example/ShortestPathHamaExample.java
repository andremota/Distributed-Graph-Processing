package pt.isel.ps1314v.g11.hama.example;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hama.graph.Vertex;

public class ShortestPathHamaExample extends Vertex<LongWritable, NullWritable, IntWritable>{

	public void compute(Iterable<IntWritable> msgs) throws IOException {
		if(getSuperstepCount() == 0){
			if(getVertexID().get() == 0){
				//O vértice fonte envia mensagem para todos os vértices adjacentes com distância 1.
				sendMessageToNeighbors(new IntWritable(1));
			}else{
				//A distância inicial do vértice fonte para todos os outros vértices é infinita.
				setValue(new IntWritable(Integer.MAX_VALUE));
			}
		}
		
		if(getSuperstepCount() > 0){
			//Apenas vértices que tenham recebido mesangens iram estar acordados.
			IntWritable val = getValue();
			
			for(IntWritable rcvDistance : msgs){
				//O vértice fica com a distância mínima até ao vértice fonte.
				val.set(Math.min(val.get(), rcvDistance.get()));
			}
			
			//Envia para os vértices adjacentes a distância mínima que tem até ao vértice fonte.
			sendMessageToNeighbors(new IntWritable(val.get()+1));
			
		}
		
		voteToHalt();
		
	}


}

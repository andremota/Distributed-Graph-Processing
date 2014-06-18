package pt.isel.ps1314v.g11.louvain.hama.io;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.graph.Edge;
import org.apache.hama.graph.Vertex;
import org.apache.hama.graph.VertexInputReader;

import pt.isel.ps1314v.g11.louvain.LouvainVertexValue;

public class LouvainTextReader extends VertexInputReader<LongWritable, Text,
	LongWritable, IntWritable, LouvainVertexValue>{
		//private Logger LOG = Logger.getLogger(KCoreDecompositionInHamaExample.class);
		@Override
		public boolean parseVertex(
				LongWritable key,
				Text value,
				Vertex<LongWritable, IntWritable, LouvainVertexValue> vertex)
				throws Exception {
			
			String[] ws = value.toString().split(" ");

			vertex.setVertexID(new LongWritable(Long.parseLong(ws[0])));

			for (int i = 2; i < ws.length; i += 2) {
				vertex.addEdge(new Edge<LongWritable, IntWritable>(
						new LongWritable(Long.parseLong(ws[i])),
						new IntWritable(Integer.parseInt(ws[i + 1]))));
			}
/*
			vertex.setVertexID(new LongWritable(Long.parseLong(key.toString())));
			for(Writable w: value.get()){
				vertex.addEdge(new Edge<LongWritable, IntWritable>(
						new LongWritable(Long.parseLong(w.toString())),new IntWritable(1)));
			}
*/
			return true;
		}
		
	}
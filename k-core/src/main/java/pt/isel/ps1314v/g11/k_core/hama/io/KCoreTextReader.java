package pt.isel.ps1314v.g11.k_core.hama.io;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.graph.Edge;
import org.apache.hama.graph.Vertex;
import org.apache.hama.graph.VertexInputReader;

import pt.isel.ps1314v.g11.k_core.KCoreDecompositionVertexValue;

public class KCoreTextReader extends VertexInputReader<LongWritable, Text,
LongWritable, LongWritable, KCoreDecompositionVertexValue>{
	//private Logger LOG = Logger.getLogger(KCoreDecompositionInHamaExample.class);
	@Override
	public boolean parseVertex(
			LongWritable key,
			Text value,
			Vertex<LongWritable, LongWritable, KCoreDecompositionVertexValue> vertex)
			throws Exception {
		String[] ws = value.toString().split(" ");
		vertex.setVertexID(new LongWritable(Long.parseLong(ws[0])));
		
		for (int i = 2; i < ws.length; i += 2) {
			vertex.addEdge(new Edge<LongWritable, LongWritable>(
					new LongWritable(Long.parseLong(ws[i])),
					null));
		}
		/*
		vertex.setVertexID(new LongWritable(Long.parseLong(key.toString())));
		for(Writable w: value.get()){
			vertex.addEdge(new Edge<LongWritable, LongWritable>(
					new LongWritable(Long.parseLong(w.toString())),null));
		}*/
		return true;
	}
	
}
package pt.isel.ps1314v.g11.bc.hama.io;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.graph.Edge;
import org.apache.hama.graph.Vertex;
import org.apache.hama.graph.VertexInputReader;

import pt.isel.ps1314v.g11.bc.BetweennessVertexValue;

public class BCVertexInputReader extends VertexInputReader<LongWritable, Text, LongWritable, IntWritable,BetweennessVertexValue>{

	@Override
	public boolean parseVertex(
			LongWritable key,
			Text value,
			Vertex<LongWritable, IntWritable, BetweennessVertexValue> vertex)
			throws Exception {

		String[] ws = value.toString().split(" ");

		vertex.setVertexID(new LongWritable(Long.parseLong(ws[0])));

		for (	int i = 2; i < ws.length; i += 2) {
			vertex.addEdge(new Edge<LongWritable, IntWritable>(
					new LongWritable(Long.parseLong(ws[i])),
					new IntWritable(Integer.parseInt(ws[i + 1]))));
		}

		return true;
	}
}

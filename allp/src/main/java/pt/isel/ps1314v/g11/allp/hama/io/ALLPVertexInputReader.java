package pt.isel.ps1314v.g11.allp.hama.io;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.graph.Edge;
import org.apache.hama.graph.Vertex;
import org.apache.hama.graph.VertexInputReader;

public class ALLPVertexInputReader
		extends VertexInputReader<LongWritable, Text, LongWritable, NullWritable, LongWritable> {

	@Override
	public boolean parseVertex(
			LongWritable key,
			Text value,
			Vertex<LongWritable, NullWritable, LongWritable> vertex)
			throws Exception {

		String[] ws = value.toString().split(" ");

		vertex.setVertexID(new LongWritable(Long.parseLong(ws[0])));

		for (int i = 2; i < ws.length; i += 2) {
			vertex.addEdge(new Edge<LongWritable,NullWritable>(
					new LongWritable(Long.parseLong(ws[i])), NullWritable.get()));
		}

		return true;
	}
}

package pt.isel.ps1314v.g11.heatkernel.hama.io;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hama.graph.Edge;
import org.apache.hama.graph.Vertex;
import org.apache.hama.graph.VertexInputReader;

public class HeatKernelVertexInputReader
		extends VertexInputReader<LongWritable, Text, LongWritable, DoubleWritable, DoubleWritable> {

	@Override
	public boolean parseVertex(
			LongWritable key,
			Text value,
			Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex)
			throws Exception {

		String[] ws = value.toString().split(" ");

		vertex.setVertexID(new LongWritable(Long.parseLong(ws[0])));

		for (int i = 2; i < ws.length; i += 2) {
			vertex.addEdge(new Edge<LongWritable, DoubleWritable>(
					new LongWritable(Long.parseLong(ws[i])),
					new DoubleWritable(Double.parseDouble(ws[i + 1]))));
		}

		return true;
	}
}

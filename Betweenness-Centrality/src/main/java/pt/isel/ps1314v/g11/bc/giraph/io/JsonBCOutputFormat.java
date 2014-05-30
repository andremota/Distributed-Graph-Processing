package pt.isel.ps1314v.g11.bc.giraph.io;

import java.io.IOException;

import org.apache.giraph.graph.Vertex;
import org.apache.giraph.io.formats.TextVertexOutputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.json.JSONArray;

import pt.isel.ps1314v.g11.bc.BetweennessVertexValue;

public class JsonBCOutputFormat extends TextVertexOutputFormat<LongWritable, BetweennessVertexValue, IntWritable>{



	@Override
	public TextVertexOutputFormat<LongWritable, BetweennessVertexValue, IntWritable>.TextVertexWriter createVertexWriter(
			TaskAttemptContext context) throws IOException,
			InterruptedException {
		return new JsonBCVertexWriter();
	}

	
	public class JsonBCVertexWriter extends TextVertexWriterToEachLine  {


		@Override
		protected Text convertVertexToLine(
				Vertex<LongWritable, BetweennessVertexValue, IntWritable> vertex)
				throws IOException {
			JSONArray jsonVertex = new JSONArray();
			
			jsonVertex.put(vertex.getId().get());
			jsonVertex.put(vertex.getValue().getShortestPaths());
			return new Text(jsonVertex.toString());
		}

	}
}

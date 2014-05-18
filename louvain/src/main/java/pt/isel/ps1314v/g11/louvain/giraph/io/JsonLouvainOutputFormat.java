package pt.isel.ps1314v.g11.louvain.giraph.io;

import java.io.IOException;

import org.apache.giraph.graph.Vertex;
import org.apache.giraph.io.formats.TextVertexOutputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.json.JSONArray;

import pt.isel.ps1314v.g11.louvain.LouvainVertexValue;

public class JsonLouvainOutputFormat extends TextVertexOutputFormat<LongWritable, LouvainVertexValue, IntWritable>{



	@Override
	public TextVertexOutputFormat<LongWritable, LouvainVertexValue, IntWritable>.TextVertexWriter createVertexWriter(
			TaskAttemptContext context) throws IOException,
			InterruptedException {
		return new JsonLouvainVertexWriter();
	}

	
	public class JsonLouvainVertexWriter extends TextVertexWriterToEachLine  {


		@Override
		protected Text convertVertexToLine(
				Vertex<LongWritable, LouvainVertexValue, IntWritable> vertex)
				throws IOException {
			JSONArray jsonVertex = new JSONArray();
			
			jsonVertex.put(vertex.getId().get());
			jsonVertex.put(vertex.getValue().getHub());
			jsonVertex.put(vertex.getValue().getPass());
			return new Text(jsonVertex.toString());
		}

	}
}

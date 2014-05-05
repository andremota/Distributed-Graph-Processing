package pt.isel.ps1314v.g11.k_core.giraph.io;

import java.io.IOException;

import org.apache.giraph.graph.Vertex;
import org.apache.giraph.io.formats.TextVertexOutputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.json.JSONArray;

import pt.isel.ps1314v.g11.k_core.KCoreDecompositionVertexValue;

public class JsonKCoreOutputFormat extends TextVertexOutputFormat<LongWritable, KCoreDecompositionVertexValue, IntWritable>{



	@Override
	public TextVertexOutputFormat<LongWritable, KCoreDecompositionVertexValue, IntWritable>.TextVertexWriter createVertexWriter(
			TaskAttemptContext context) throws IOException,
			InterruptedException {
		return new JsonKCoreVertexWriter();
	}

	
	public class JsonKCoreVertexWriter extends TextVertexWriterToEachLine  {


		@Override
		protected Text convertVertexToLine(
				Vertex<LongWritable, KCoreDecompositionVertexValue, IntWritable> vertex)
				throws IOException {
			JSONArray jsonVertex = new JSONArray();
			
			jsonVertex.put(vertex.getId().get());
			jsonVertex.put(vertex.getValue().getCore());
			
			return new Text(jsonVertex.toString());
		}

	}
}

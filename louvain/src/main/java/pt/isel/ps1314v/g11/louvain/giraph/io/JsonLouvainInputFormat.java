package pt.isel.ps1314v.g11.louvain.giraph.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.giraph.edge.Edge;
import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.Vertex;
import org.apache.giraph.io.formats.TextVertexInputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.json.JSONArray;
import org.json.JSONException;

import pt.isel.ps1314v.g11.louvain.LouvainVertexValue;


public class JsonLouvainInputFormat extends TextVertexInputFormat<LongWritable, LouvainVertexValue, IntWritable>{



	@Override
	public TextVertexInputFormat<LongWritable, LouvainVertexValue, IntWritable>.TextVertexReader createVertexReader(
			InputSplit split, TaskAttemptContext context) throws IOException {
		return new JsonLouvainVertexReader();
	}
	
	public class JsonLouvainVertexReader extends TextVertexReaderFromEachLineProcessedHandlingExceptions<JSONArray, JSONException> {

		@Override
		protected JSONArray preprocessLine(Text line) throws JSONException,
				IOException {
			return new JSONArray(line.toString());
		}

		@Override
		protected LongWritable getId(JSONArray line) throws JSONException,
				IOException {
			return new LongWritable(line.getLong(0));
		}

		@Override
		protected LouvainVertexValue getValue(JSONArray line)
				throws JSONException, IOException {
			return new LouvainVertexValue();
		}

		@Override
		protected Iterable<Edge<LongWritable, IntWritable>> getEdges(
				JSONArray line) throws JSONException, IOException {
			JSONArray jsonEdges = line.getJSONArray(2);
			
			List<Edge<LongWritable,IntWritable>> edges = new ArrayList<Edge<LongWritable,IntWritable>>();
			
			for(int i = 0; i<jsonEdges.length(); ++i){
				
				JSONArray edge = jsonEdges.getJSONArray(i);
				
				edges.add(EdgeFactory.create(
										new LongWritable(edge.getLong(0)),
										new IntWritable(edge.getInt(1)))
										);
			}
			
			return edges;
		}
		
		@Override
		protected Vertex<LongWritable, LouvainVertexValue, IntWritable> handleException(
				Text line, JSONArray processed, JSONException e) {
			// TODO Auto-generated method stub
			throw new IllegalArgumentException("Couldn't get vertex from line "+line, e);
		}

	}

}

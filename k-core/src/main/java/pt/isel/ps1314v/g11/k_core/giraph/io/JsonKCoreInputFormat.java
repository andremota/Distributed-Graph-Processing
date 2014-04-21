package pt.isel.ps1314v.g11.k_core.giraph.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.giraph.edge.Edge;
import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.Vertex;
import org.apache.giraph.io.formats.TextVertexInputFormat;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.json.JSONArray;
import org.json.JSONException;

import pt.isel.ps1314v.g11.k_core.KCoreDecompositionVertexValue;

public class JsonKCoreInputFormat extends TextVertexInputFormat<LongWritable, KCoreDecompositionVertexValue, LongWritable>{



	@Override
	public TextVertexInputFormat<LongWritable, KCoreDecompositionVertexValue, LongWritable>.TextVertexReader createVertexReader(
			InputSplit split, TaskAttemptContext context) throws IOException {
		return new JsonKCoreVertexReader();
	}
	
	public class JsonKCoreVertexReader extends TextVertexReaderFromEachLineProcessedHandlingExceptions<JSONArray, JSONException> {

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
		protected KCoreDecompositionVertexValue getValue(JSONArray line)
				throws JSONException, IOException {
			return new KCoreDecompositionVertexValue();
		}

		@Override
		protected Iterable<Edge<LongWritable, LongWritable>> getEdges(
				JSONArray line) throws JSONException, IOException {
			JSONArray jsonEdges = line.getJSONArray(2);
			
			List<Edge<LongWritable,LongWritable>> edges = new ArrayList<Edge<LongWritable,LongWritable>>();
			
			for(int i = 0; i<jsonEdges.length(); ++i){
				
				JSONArray edge = jsonEdges.getJSONArray(i);
				
				edges.add(EdgeFactory.create(
										new LongWritable(edge.getLong(0)),
										new LongWritable(edge.getLong(1)))
										);
			}
			
			return edges;
		}
		
		@Override
		protected Vertex<LongWritable, KCoreDecompositionVertexValue, LongWritable> handleException(
				Text line, JSONArray processed, JSONException e) {
			// TODO Auto-generated method stub
			throw new IllegalArgumentException("Couldn't get vertex from line "+line, e);
		}

	}

}

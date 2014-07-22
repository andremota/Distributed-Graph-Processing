package pt.isel.ps1314v.g11.llp.giraph.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.giraph.edge.Edge;
import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.io.formats.TextVertexInputFormat;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 *An Adjacency list input formatter with edges values and vertex values.
 *Each SourceVertexId input should be separated by a line.
 *
 *Ex:
 *SourceVertexId SourceVertexIdValue TargetVertexId EdgeValue ...
 *...
 */
public class AdjacencyListWithValuesInputFormat extends TextVertexInputFormat<LongWritable, LongWritable,NullWritable>{


	@Override
	public TextVertexReader createVertexReader(InputSplit split,
			TaskAttemptContext context) throws IOException { 
		return new HeatKernelVertexReader();
	}
	
	public class HeatKernelVertexReader extends TextVertexReaderFromEachLineProcessedHandlingExceptions<Text,IOException>{

		@Override
		protected Text preprocessLine(Text line) throws IOException,
				IOException {
			return line;
		}

		@Override
		protected LongWritable getId(Text line) throws IOException, IOException {
			return new LongWritable(Long.parseLong(line.toString().split(" ")[0]));
		}

		@Override
		protected LongWritable getValue(Text line) throws IOException,
				IOException {
			return new LongWritable(Long.parseLong(line.toString().split(" ")[1]));
		}

		@Override
		protected Iterable<Edge<LongWritable, NullWritable>> getEdges(Text line)
				throws IOException, IOException {
			
			List<Edge<LongWritable, NullWritable>> edges = new ArrayList<Edge<LongWritable, NullWritable>>();
			String edgesText[] = line.toString().split(" ");
			
			for(int i = 2; i< edgesText.length ; i+=2){
				edges.add(EdgeFactory.create(
						new LongWritable(Long.parseLong(edgesText[i]))));
			}
			
			return edges;
		}
	}
}

package pt.isel.ps1314v.g11.louvain.giraph.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.giraph.edge.Edge;
import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.io.formats.TextVertexInputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import pt.isel.ps1314v.g11.louvain.LouvainVertexValue;

public class AdjacencyListLouvainInputFormat extends TextVertexInputFormat<LongWritable, LouvainVertexValue, IntWritable>{



	@Override
	public TextVertexInputFormat<LongWritable, LouvainVertexValue, IntWritable>.TextVertexReader createVertexReader(
			InputSplit split, TaskAttemptContext context) throws IOException {
		return new AdjacencyListLouvainVertexReader();	
	}

	public class AdjacencyListLouvainVertexReader extends TextVertexReaderFromEachLineProcessedHandlingExceptions<Text,IOException> {

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
		protected LouvainVertexValue getValue(Text line)
				throws IOException, IOException {
			return new LouvainVertexValue();
		}

		@Override
		protected Iterable<Edge<LongWritable, IntWritable>> getEdges(Text line)
				throws IOException, IOException {
			List<Edge<LongWritable, IntWritable>> edges = new ArrayList<>();
			String edgesText[] = line.toString().split(" ");
			
			for(int i = 2; i< edgesText.length ; i+=2){
				edges.add(EdgeFactory.create(
						new LongWritable(Long.parseLong(edgesText[i])),
						new IntWritable(Integer.parseInt(edgesText[i+1]))));
			}
			
			return edges;
		}


	}


}

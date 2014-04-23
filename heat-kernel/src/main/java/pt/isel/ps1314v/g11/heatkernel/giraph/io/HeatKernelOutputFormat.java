package pt.isel.ps1314v.g11.heatkernel.giraph.io;

import java.io.IOException;

import org.apache.giraph.graph.Vertex;
import org.apache.giraph.io.formats.TextVertexOutputFormat;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.TaskAttemptContext;


public class HeatKernelOutputFormat extends TextVertexOutputFormat<LongWritable, DoubleWritable, DoubleWritable>{

	@Override
	public TextVertexOutputFormat<LongWritable, DoubleWritable, DoubleWritable>.TextVertexWriter createVertexWriter(
			TaskAttemptContext context) throws IOException,
			InterruptedException {
		return new HeatKernelVertexWriter();
	}
	
	public class  HeatKernelVertexWriter extends TextVertexWriterToEachLine  {
		@Override
		protected Text convertVertexToLine(
				Vertex<LongWritable, DoubleWritable, DoubleWritable> vertex)
				throws IOException {
			return new Text(vertex.getId() + " " + vertex.getValue());
		}

	}

}

package pt.isel.ps1314v.g11.hama.example;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;

import pt.isel.ps1314v.g11.common.graph.BasicAlgorithm;
import pt.isel.ps1314v.g11.common.graph.Edge;
import pt.isel.ps1314v.g11.common.graph.Vertex;

public class EdgesRemovalExample extends BasicAlgorithm<Text, DoubleWritable, NullWritable>{

	private static final Logger LOG = Logger
			.getLogger(EdgesRemovalExample.class);
	
	@Override
	public void compute(Vertex<Text, DoubleWritable, NullWritable> vertex,
			Iterable<DoubleWritable> messages) {
		if(getSuperstep() == 0){
			//vertex.
			sendMessageToVertex(vertex.getId(), new DoubleWritable(vertex.getNumEdges()));
			ArrayList<Edge<Text, NullWritable>> list = new ArrayList<>();
			list.add(new Edge<Text, NullWritable>(vertex.getId(), NullWritable.get()));
			vertex.setEdges(list);
			//vertex.addEdge(new Edge<Text, NullWritable>(vertex.getId(), NullWritable.get()));
		}
		
		if(getSuperstep() == 1){
			Iterator<DoubleWritable> it = messages.iterator();
			if(it.hasNext())
			
			LOG.info("Previous: " + it.next() + " Number of Edges: "+vertex.getNumEdges());
			vertex.voteToHalt();
		}
	}

}

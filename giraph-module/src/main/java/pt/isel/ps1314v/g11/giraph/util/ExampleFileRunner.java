package pt.isel.ps1314v.g11.giraph.util;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.utils.InternalVertexRunner;
/**
 * Basic class to run local examples, should not be used in production
 * 
 *
 */
public class ExampleFileRunner {

	
	public static void run(String inputFile, String outputFile, GiraphConfiguration conf) throws Exception{
		String[] graph = FileUtils.readFromFile(inputFile);
		
		Iterable<String> result = InternalVertexRunner.run(conf, graph);
		
		if(result != null)
			FileUtils.writeToFile(outputFile, result);
	}
}

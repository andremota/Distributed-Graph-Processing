package pt.isel.ps1314v.g11.giraph.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

	public static String[] readFromFile(String fileName) throws IOException{
		Path path = FileSystems.getDefault().getPath(fileName);	
		try {
			return Files.readAllLines(path, Charset.defaultCharset()).toArray(new String[]{});
		} catch (IOException e) {
			System.err.println("Was not able to read the file "+fileName+" due to "+e.getMessage());
			throw e;
		}
	}
	
	public static void writeToFile(String fileName, Iterable<String> lines) throws IOException{
		Path path = FileSystems.getDefault().getPath(fileName);	
		
		try {
			Files.write(path, lines, Charset.defaultCharset());
		} catch (IOException e) {
			System.err.println("Was not able to write to the file "+fileName+" due to "+e.getMessage());
			throw e;
		}
	}
}

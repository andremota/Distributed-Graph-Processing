package pt.isel.ps1314v.g11.bc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.Writable;

public class BetweennessVertexValue implements Writable{

	public static class Pair{
		//int cost;
		Set<Long> predecessors = new HashSet<>();;
	}
	
	private Map<Long,Pair> minimums = new HashMap<>();
	private int shortestPaths;
	@Override
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public Map<Long, Pair> getMinimums() {
		return minimums;
	}

	public void setMinimums(Map<Long,Pair> minimums) {
		this.minimums = minimums;
	}

	public void incNShortestPaths() {
		shortestPaths++;
		
	}

	public int getShortestPaths() {
		return shortestPaths;
	}

	public void setShortestPaths(int shortestPaths) {
		this.shortestPaths = shortestPaths;
	}

}

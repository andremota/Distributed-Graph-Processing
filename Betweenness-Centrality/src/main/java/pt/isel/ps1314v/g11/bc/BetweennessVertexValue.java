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
	public void readFields(DataInput in) throws IOException {
		shortestPaths = in.readInt();
		int minSz = in.readInt();
		
		for(int i = 0; i<minSz; ++i){
			long key = in.readLong();
			Pair pred = null;
			if(in.readBoolean()){
				pred = new Pair();
				int predSz = in.readInt();
				for(int j = 0; j<predSz; ++j){
					pred.predecessors.add(in.readLong());
				}
			}
			minimums.put(key, pred);
		}
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(shortestPaths);
		out.writeInt(minimums.size());
		for(Map.Entry<Long, Pair> entry: minimums.entrySet()){
			out.writeLong(entry.getKey());
			Pair pred = entry.getValue();
			if(pred==null){
				out.writeBoolean(false);
			} else {
				out.writeBoolean(true);
				Set<Long> preds = pred.predecessors;
				out.writeInt(preds.size());
				for(Long l: preds)
					out.writeLong(l);
			}
		}
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

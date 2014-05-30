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

	public static class SymmetricTuple{
		long first;
		long second;
		
		public SymmetricTuple(long start, long from) {
			this.first = start;
			this.second = from;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj==null)
				return false;
			SymmetricTuple other = (SymmetricTuple)obj;
			
			return first == other.first && second == other.second 
					|| second == other.first && first == other.second;
		}
		
		@Override
		public int hashCode() {
			return (int) (first ^ second);
		}
	}
	
	public static class Predecessors{
		int cost;
		Set<Long> predecessors = new HashSet<>();
		
		public Predecessors(int cost){
			this.cost = cost;
		}
	}
	
	private Map<Long,Predecessors> minimums = new HashMap<>();
	private Set<SymmetricTuple> starts = new HashSet<>();
	private int shortestPaths;
	private double finalBC;
	
	@Override
	public void readFields(DataInput in) throws IOException {
		shortestPaths = in.readInt();
		int minSz = in.readInt();
		
		for(int i = 0; i<minSz; ++i){
			long key = in.readLong();
			Predecessors pred = new Predecessors(in.readInt());
			int predSz = in.readInt();
			for(int j = 0; j<predSz; ++j){
				pred.predecessors.add(in.readLong());
			
			}
			minimums.put(key, pred);
		}
		
		int stSz = in.readInt();
		for(int i = 0; i<stSz; ++i){
			starts.add(new SymmetricTuple(
					in.readLong(),
					in.readLong()));
		}
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(shortestPaths);
		out.writeInt(minimums.size());
		for(Map.Entry<Long, Predecessors> entry: minimums.entrySet()){
			out.writeLong(entry.getKey());
			Predecessors pred = entry.getValue();
			out.writeInt(pred.cost);
			Set<Long> preds = pred.predecessors;
			out.writeInt(preds.size());
			for(Long l: preds)
				out.writeLong(l);
		}
		out.writeInt(starts.size());
		for(SymmetricTuple st: starts){
			out.writeLong(st.first);
			out.writeLong(st.second);
		}
	}

	public Map<Long, Predecessors> getMinimums() {
		return minimums;
	}

	public void setMinimums(Map<Long,Predecessors> minimums) {
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

	public Set<SymmetricTuple> getStarts() {
		return starts;
	}

	public void setStarts(Set<SymmetricTuple> starts) {
		this.starts = starts;
	}

	public void setFinalBC(double finalBC) {
		this.finalBC = finalBC;
		
	}
	
	public double getFinalBC(){
		return finalBC;
	}

}

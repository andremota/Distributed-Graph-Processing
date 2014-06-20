package pt.isel.ps1314v.g11.bc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			
			return exactEquals(other) || symetricEquals(other);
		}
		
		@Override
		public int hashCode() {
			return (int) (first ^ second);
		}

		public boolean exactEquals(SymmetricTuple other) {
			return first == other.first && second == other.second ;
		}
		
		public boolean symetricEquals(SymmetricTuple other){
			return second == other.first && first == other.second;
		}
	}
	
	public static class Predecessors{
		int cost;
		List<Long> predecessors = new ArrayList<>();
		
		public Predecessors(int cost){
			this.cost = cost;
		}
	}
	
	private Map<Long,Predecessors> minimums = new HashMap<>();
	private Map<SymmetricTuple,SymmetricTuple> starts = new HashMap<>();
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
			SymmetricTuple tuple = new SymmetricTuple(
					in.readLong(),
					in.readLong());
			
			starts.put(tuple,tuple);
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
			List<Long> preds = pred.predecessors;
			out.writeInt(preds.size());
			for(Long l: preds)
				out.writeLong(l);
		}
		out.writeInt(starts.size());
		for(SymmetricTuple st: starts.values()){
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

	public Map<SymmetricTuple,SymmetricTuple> getStarts() {
		return starts;
	}

	public void setStarts(Map<SymmetricTuple,SymmetricTuple> starts) {
		this.starts = starts;
	}

	public void setFinalBC(double finalBC) {
		this.finalBC = finalBC;
		
	}
	
	public double getFinalBC(){
		return finalBC;
	}
	
	@Override
	public String toString() {
		return 	shortestPaths + " " + finalBC;
	}

}

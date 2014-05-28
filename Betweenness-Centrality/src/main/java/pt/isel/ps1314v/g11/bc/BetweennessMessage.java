package pt.isel.ps1314v.g11.bc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class BetweennessMessage implements Writable{

	private long startVertex;
	private int cost;
	
	public BetweennessMessage(Long start, int i) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public long getStartVertex() {
		return startVertex;
	}

	public void setStartVertex(long startVertex) {
		this.startVertex = startVertex;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

}

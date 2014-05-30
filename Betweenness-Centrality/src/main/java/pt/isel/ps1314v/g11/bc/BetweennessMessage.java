package pt.isel.ps1314v.g11.bc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class BetweennessMessage implements Writable{

	private long startVertex;
	private long vertexFlamenco;
	private int cost;
	private boolean shortestPathMessage;
	
	public BetweennessMessage() {}
	public BetweennessMessage(long start, long fromVertex, int cost) {
		this.startVertex = start;
		this.vertexFlamenco = fromVertex;
		this.cost = cost;
	}

	public BetweennessMessage(long start, boolean shortestPathMessage) {
		this.startVertex = start;
		this.shortestPathMessage = shortestPathMessage;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		startVertex = in.readLong();
		vertexFlamenco = in.readLong();
		shortestPathMessage = in.readBoolean();
		cost = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(startVertex);
		out.writeLong(vertexFlamenco);
		out.writeBoolean(shortestPathMessage);
		out.writeInt(cost);
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

	public long getFromVertex() {
		return vertexFlamenco;
	}

	public void setFromVertex(long vertexFlamenco) {
		this.vertexFlamenco = vertexFlamenco;
	}

	public boolean isShortestPathMessage() {
		return shortestPathMessage;
	}

	public void setShortestPathMessage(boolean shortestPathMessage) {
		this.shortestPathMessage = shortestPathMessage;
	}

}

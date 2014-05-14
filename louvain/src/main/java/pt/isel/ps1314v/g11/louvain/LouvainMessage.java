package pt.isel.ps1314v.g11.louvain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class LouvainMessage implements Writable{

	private int tot;
	private long vertexId;
	private int deg;
	private long hub;
	
	public LouvainMessage(long id, int tot, int deg, long hub) {
		setVertexId(id);
		setTot(tot);
		setDeg(deg);
		setHub(hub);
	}

	public LouvainMessage(long id, int deg) {
		setVertexId(id);
		setDeg(deg);
	}

	public LouvainMessage(int tot, long hub) {
		setTot(tot);
		setHub(hub);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		tot = in.readInt();
		vertexId = in.readLong();
		deg = in.readInt();
		hub = in.readLong();
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(tot);
		out.writeLong(vertexId);
		out.writeInt(deg);
		out.writeLong(hub);
	}

	public int getTot() {
		return tot;
	}

	public void setTot(int tot) {
		this.tot = tot;
	}

	public long getVertexId() {
		return vertexId;
	}

	public void setVertexId(long vertexId) {
		this.vertexId = vertexId;
	}

	public int getDeg() {
		return deg;
	}

	public void setDeg(int deg) {
		this.deg = deg;
	}

	public long getHub() {
		return hub;
	}

	public void setHub(long hub) {
		this.hub = hub;
	}



}

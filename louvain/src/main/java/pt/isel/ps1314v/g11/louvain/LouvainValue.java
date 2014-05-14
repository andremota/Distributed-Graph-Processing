package pt.isel.ps1314v.g11.louvain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class LouvainValue implements Writable{

	private int deg;
	private int tot;
	private long hub;
	private long m2;
	
	public LouvainValue(long hub, int degree) {
		setHub(hub);
		setDeg(degree);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		deg = in.readInt();
		tot = in.readInt();
		hub = in.readLong();
		m2 	= in.readLong();
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(deg);
		out.writeInt(tot);
		out.writeLong(hub);
		out.writeLong(m2);
		
	}

	public int getDeg() {
		return deg;
	}

	public void setDeg(int deg) {
		this.deg = deg;
	}

	public int getTot() {
		return tot;
	}

	public void setTot(int tot) {
		this.tot = tot;
	}

	public long getHub() {
		return hub;
	}

	public void setHub(long hub) {
		this.hub = hub;
	}

	public long getM2() {
		return m2;
	}

	public void setM2(long m2) {
		this.m2 = m2;
	}

}

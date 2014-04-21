package pt.isel.ps1314v.g11.k_core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class KCoreDecompositionMessage implements Writable{

	private long vertexId;
	private int vertexCore;
	
	public KCoreDecompositionMessage(){
		setVertexId(0);
		setVertexCore(0);
	}
	
	public KCoreDecompositionMessage(long id, int core) {
		setVertexId(id);
		setVertexCore(core);
	}
	
	@Override
	public void readFields(DataInput input) throws IOException {
		setVertexId(input.readLong());
		setVertexCore(input.readInt());
		
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeLong(vertexId);
		output.writeInt(vertexCore);
	}

	public long getVertexId() {
		return vertexId;
	}

	public void setVertexId(long vertexId) {
		this.vertexId = vertexId;
	}

	public int getVertexCore() {
		return vertexCore;
	}

	public void setVertexCore(int vertexCore) {
		this.vertexCore = vertexCore;
	}

}

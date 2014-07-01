package pt.isel.ps1314v.g11.allp;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class ALLPVertexValue implements Writable{

	private boolean shouldStop;
	private long oldLabel;
	private long label;
	
	public ALLPVertexValue(){}
	
	public ALLPVertexValue(long oldLabel, long label){
		this.oldLabel= oldLabel;
		this.label = label;
	}
	
	public void setLabel(long label){;
		this.label = label;
	}	
	
	public void setOldLabel(long oldLabel){
		this.oldLabel= oldLabel;
	}
	
	public long getLabel(){
		return label;
	}
	
	public long getOldLabel(){
		return oldLabel;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		oldLabel = in.readLong();
		label = in.readLong();
		shouldStop = in.readBoolean();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(oldLabel);
		out.writeLong(label);
		out.writeBoolean(shouldStop);
	}
	
	public void changeLabel(long newLabel){
		this.oldLabel = this.label;
		this.label = newLabel;
	}

	public boolean shouldStop(){
		return shouldStop;
	}
	
	public void setShouldStop(boolean s){
		shouldStop = s;
	}
	
	@Override
	public String toString() {
		return Long.toString(label);
	}
}

package pt.isel.ps1314v.g11.llp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class LLPVertexValue implements Writable{

	private long oldLabel;
	private long label;
	
	public LLPVertexValue(){}
	
	public LLPVertexValue(long oldLabel, long label){
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
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeLong(oldLabel);
		out.writeLong(label);
	}
	
	public void changeLabel(long newLabel){
		this.oldLabel = this.label;
		this.label = newLabel;
	}

	@Override
	public String toString() {
		return '('+Long.toString(oldLabel)+','+Long.toString(label)+')';
	}
}

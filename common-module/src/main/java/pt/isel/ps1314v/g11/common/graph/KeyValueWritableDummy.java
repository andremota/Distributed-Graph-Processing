package pt.isel.ps1314v.g11.common.graph;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class KeyValueWritableDummy implements Writable {

	private Writable value;
	private String key;
	
	public KeyValueWritableDummy(String key, Writable value){
		this.setKey(key);
		this.setValue(value);
	}
	
	@Override
	public void readFields(DataInput arg0) throws IOException {}

	@Override
	public void write(DataOutput arg0) throws IOException {}

	public Writable getValue() {
		return value;
	}

	public void setValue(Writable value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}

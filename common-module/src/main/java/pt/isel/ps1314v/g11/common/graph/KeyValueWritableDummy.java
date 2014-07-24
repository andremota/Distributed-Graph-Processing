package pt.isel.ps1314v.g11.common.graph;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * This is just a wrapper class for a writable and should only be used locally.
 *
 * Both method readFields(DataInput) and write(DataOutput) won't influence the wrapped writable.
 */
public class KeyValueWritableDummy implements Writable {

	private Writable value;
	private String key;
	
	/**
	 * 
	 * @param key- the key associated to the given writable.
	 * @param value - the writable to be wrapped by this class.
	 */
	public KeyValueWritableDummy(String key, Writable value){
		this.setKey(key);
		this.setValue(value);
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {}
	@Override
	public void write(DataOutput arg0) throws IOException {}
	/* 
	 * Since both the methods readFields and write are not implemented, this class should only
	 * be used in places where communication is not involved.
	 */

	/**
	 * @return - the wrapped writable.
	 */
	public Writable getValue() {
		return value;
	}

	/**
	 * @param value - the writable to be wrapped by this class.
	 */
	public void setValue(Writable value) {
		this.value = value;
	}

	/**
	 * 
	 * @return - a key associated to the wrapped writable.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @param key - the key to be associated to the wrapped writable.
	 */
	public void setKey(String key) {
		this.key = key;
	}

}

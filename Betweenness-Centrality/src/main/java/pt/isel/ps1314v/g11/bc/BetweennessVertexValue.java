package pt.isel.ps1314v.g11.bc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.Writable;

public class BetweennessVertexValue implements Writable{

	private Map<Long,Integer> minimums;
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public Map<Long, Integer> getMinimums() {
		return minimums;
	}

	public void setMinimums(Map<Long,Integer> minimums) {
		this.minimums = minimums;
	}

}

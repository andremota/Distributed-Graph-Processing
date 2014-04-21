package pt.isel.ps1314v.g11.k_core;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import org.apache.hadoop.io.Writable;

public class KCoreDecompositionVertexValue implements Writable{

	
	private boolean changed;
	private int core;
	private Map<Long,Integer> est;
	
	
	public KCoreDecompositionVertexValue(){
		setCore(0);
		setEst(new Hashtable<Long, Integer>());
	}
	
	public KCoreDecompositionVertexValue(int initialCore){
		setCore(initialCore);
		setEst(new Hashtable<Long,Integer>(initialCore));
	}
	
	
	@Override
	public void readFields(DataInput input) throws IOException {
		setChanged(input.readBoolean());
		setCore(input.readInt());
		
		int size = input.readInt();
		
		
		for(int i = 0; i<size; ++i){
			est.put(
					input.readLong(),
					input.readInt()
					);
		}
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeBoolean(changed);
		output.writeInt(core);
		
		output.writeInt(est.size());
		
		for(Map.Entry<Long,Integer> neighbour: est.entrySet()){
			output.writeLong(neighbour.getKey());
			output.writeInt(neighbour.getValue());
		}
		
	}

	public boolean hasChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public int getCore() {
		return core;
	}

	public void setCore(int core) {
		this.core = core;
	}

	public Map<Long,Integer> getEst() {
		return est;
	}

	public void setEst(Map<Long,Integer> est) {
		this.est = est;
	}


	public int computeIndex() {
		int[] count = new int[core+1];
		
		for(Map.Entry<Long,Integer> neighbour: est.entrySet()){
			int j = Math.min(core, neighbour.getValue());
			count[j]++;
		}
		
		int i;
		for(i = core; i > 1; --i){
			count[i-1] -= count[i];
		}
		
		i = core;
		
		while(i > 1 && count[i] < i)
			--i;
		
		return i;
	}

}

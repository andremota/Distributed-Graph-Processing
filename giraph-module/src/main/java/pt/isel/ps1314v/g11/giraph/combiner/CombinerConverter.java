package pt.isel.ps1314v.g11.giraph.combiner;

import org.apache.giraph.combiner.MessageCombiner;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class CombinerConverter<I extends WritableComparable<I>, E extends Writable> 
	extends MessageCombiner<I, E>{

	@Override
	public void combine(I arg0, E arg1, E arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public E createInitialMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}

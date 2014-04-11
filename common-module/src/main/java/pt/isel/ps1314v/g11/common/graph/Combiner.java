package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;

public interface Combiner<M extends Writable> {
	
	/**
	 * Combines two {@link Writable} of the same type.
	 * @param originalMessage - the message to be combined with newMessage and where the result of the combination will be held.
	 * @param newMessage - the message to be combined with originalMessage.
	 */
	void combine(M originalMessage, M newMessage);
	
	/**
	 * @return the initial value to be used by some BSP platforms. Should be a neutral element.
	 */
	M initialValue();
}

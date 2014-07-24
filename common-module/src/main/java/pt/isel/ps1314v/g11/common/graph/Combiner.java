package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;
/**
 * Interface that can be used to implement Combiners that can be used to combine messages,
 *  reducing network costs.
 * @param <M> The message type
 */
public interface Combiner<M extends Writable> {
	
	public static final String COMBINER_CLASS = "pt.isel.ps1314v.g11.combinerclass";
	/**
	 * Combines two {@link Writable} of the same type.
	 * @param originalMessage - the message to be combined with newMessage and where the result of the combination will be held.
	 * @param newMessage - the message to be combined with originalMessage.
	 */
	void combine(M originalMessage, M newMessage);
	
	/**
	 * @return the initial value. Should be a neutral element.
	 */
	M initialValue();
}

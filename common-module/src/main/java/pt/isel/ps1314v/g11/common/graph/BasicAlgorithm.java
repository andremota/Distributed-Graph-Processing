package pt.isel.ps1314v.g11.common.graph;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 * Alternative to the {@link Algorithm} class in which the message type and vertex value type are the same.
 *
 * @param <I> Vertex id
 * @param <V> Vertex value and messages
 * @param <E> Edge Value
 */
public abstract class BasicAlgorithm<I extends WritableComparable,V extends Writable, E extends Writable> extends Algorithm<I, V, E, V>
{			
}

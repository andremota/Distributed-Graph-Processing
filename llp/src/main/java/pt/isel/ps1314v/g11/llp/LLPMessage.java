package pt.isel.ps1314v.g11.llp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 *
 * Messages type used in LLPAlgorithm
 *
 */
public class LLPMessage implements Writable{
	
	//Message sender
	private long sourceVertex;
	
	//The sum of the vertices with the labeli
	private long vi;
	
	//The label of the sourceVertex
	private long labeli;
	
	/**
	 * Default constructor with all values(sourceVertex, vi and labeli) initiated with 0.
	 */
	public LLPMessage(){}
	
	/**
	 * @param sourceVertex -  The vertex that sent the message.
	 * @param vi - The total number of vertices with labeli.
	 */
	public LLPMessage(long sourceVertex, long vi, long labeli){
		this(sourceVertex, vi);
		this.labeli = labeli;
	}
	
	/**
	 * 
	 * @param sourceVertex
	 * @param vi
	 */
	public LLPMessage(long sourceVertex, long vi){
		this(sourceVertex);
		this.vi = vi;
	}
	
	/**
	 * 
	 * @param sourceVertex
	 */
	public LLPMessage(long sourceVertex){
		this.sourceVertex = sourceVertex;
	}

	@Override
	public void readFields(DataInput input) throws IOException {
		sourceVertex = input.readLong();
		vi = input.readLong();
		labeli = input.readLong();
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeLong(sourceVertex);
		output.writeLong(vi);
		output.writeLong(labeli);
	}
	
	/**
	 * @return the vertex that sent the message.
	 */
	public long getSourceVertex(){
		return sourceVertex;
	}
	
	/**
	 * @return the label of the vertex that sent the message.
	 */
	public long getLabeli(){
		return labeli;
	}
	
	/**
	 * @return the total number of vertices with the label of the vertex that sent the message.
	 */
	public long getVi(){
		return vi;
	}
	
	/**
	 * Sets the LLPMessage values and returns itself.
	 * 
	 * @param sourceVertex -  The vertex that sent the message.
	 * @param vi - The total number of vertices with labeli.
	 * @param labeli - The label of the vertex that send the message.
	 */
	public LLPMessage setValues(long sourceVertex, long vi, long labeli){
		this.sourceVertex = sourceVertex;
		this.vi = vi;
		this.labeli = labeli;
		return this;
	}

}

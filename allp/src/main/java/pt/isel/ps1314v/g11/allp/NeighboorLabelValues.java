package pt.isel.ps1314v.g11.allp;


/**
 *Represents the values for a given label.
 *The values are the total number of vertices for a given label
 *and given a vertex the number of vertices in the adjacency with the label. 
 *
 */
public class NeighboorLabelValues {
	
	private long ki;
	private long vi;
	
	/**
	 * @param ki - Number of vertices in the adjacency with a certain labeli.
	 * @param vi - Total number of vertices with a certain labeli.
	 */
	public NeighboorLabelValues(long ki, long vi){
		this.ki = ki;
		this.vi = vi;
	}

	/**
	 * 
	 * @return the number of vertices in the adjacency with a certain labeli.
	 */
	public long getKi() {
		return ki;
	}

	/**
	 * Sets the current ki to the passed parameter.
	 * 
	 * @param ki - number of vertices in the adjacency with a certain labeli.;
	 */
	public void setKi(long ki) {
		this.ki = ki;
	}

	/**
	 * 
	 * @return  the total number of vertices with a certain labeli.
	 */
	public long getVi() {
		return vi;
	}

	/**
	 * Sets the current vi to the passed parameter.
	 * 
	 * @param vi - number of vertices with a certain labeli.
	 */
	public void setVi(long vi) {
		this.vi = vi;
	}
}

package pt.isel.ps1314v.g11.louvain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.Writable;

public class LouvainMessage implements Writable{

	private int tot;
	private long vertexId;
	private int deg;
	private long hub;
	
	private Map<Long,Integer> communities;
	
	public LouvainMessage(){};
	
	public LouvainMessage(long id, int tot, int deg, long hub) {
		setVertexId(id);
		setTot(tot);
		setDeg(deg);
		setHub(hub);
	}

	public LouvainMessage(long id, int deg) {
		setVertexId(id);
		setDeg(deg);
	}

	public LouvainMessage(int tot, long hub) {
		setTot(tot);
		setHub(hub);
	}

	public LouvainMessage(long id,Map<Long, Integer> comms) {
		setVertexId(id);
		setCommunities(comms);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		tot = in.readInt();
		vertexId = in.readLong();
		deg = in.readInt();
		hub = in.readLong();
		
		if(in.readBoolean()){
			communities = new HashMap<Long, Integer>();
			int sz = in.readInt();
			
			for(int i = 0; i<sz; ++i){
				communities.put(
						in.readLong(),
						in.readInt());
			}
		}
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(tot);
		out.writeLong(vertexId);
		out.writeInt(deg);
		out.writeLong(hub);
		
		if(communities!=null){
			out.writeBoolean(true);
			out.writeInt(communities.size());
			for(Map.Entry<Long,Integer> entry: communities.entrySet()){
				out.writeLong(entry.getKey().longValue());
				out.writeInt(entry.getValue().intValue());
			}
		} else {
			out.writeBoolean(false);
		}
	}

	public int getTot() {
		return tot;
	}

	public void setTot(int tot) {
		this.tot = tot;
	}

	public long getVertexId() {
		return vertexId;
	}

	public void setVertexId(long vertexId) {
		this.vertexId = vertexId;
	}

	public int getDeg() {
		return deg;
	}

	public void setDeg(int deg) {
		this.deg = deg;
	}

	public long getHub() {
		return hub;
	}

	public void setHub(long hub) {
		this.hub = hub;
	}

	public Map<Long,Integer> getCommunities() {
		return communities;
	}

	public void setCommunities(Map<Long,Integer> communities) {
		this.communities = communities;
	}

}

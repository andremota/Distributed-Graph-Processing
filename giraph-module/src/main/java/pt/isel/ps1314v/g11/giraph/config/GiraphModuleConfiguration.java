package pt.isel.ps1314v.g11.giraph.config;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import net.jodah.typetools.TypeResolver;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.conf.GiraphConstants;
import org.apache.giraph.edge.OutEdges;
import org.apache.giraph.graph.Computation;
import org.apache.giraph.graph.Language;
import org.apache.giraph.job.GiraphJob;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import pt.isel.ps1314v.g11.common.config.ModuleConfiguration;
import pt.isel.ps1314v.g11.common.graph.Algorithm;
import pt.isel.ps1314v.g11.giraph.graph.AggregatorMasterCompute;
import pt.isel.ps1314v.g11.giraph.graph.GiraphCombinerMapper;
import pt.isel.ps1314v.g11.giraph.graph.GiraphComputationMapper;
import pt.isel.ps1314v.g11.giraph.graph.GiraphOutEdgesMapper;

public class GiraphModuleConfiguration implements ModuleConfiguration {

	private GiraphConfiguration config;

	public GiraphModuleConfiguration(GiraphConfiguration config) {
		this.config = config;
	}

	public GiraphModuleConfiguration(GiraphJob job) {
		this.config = job.getConfiguration();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void useAlgorithm(Class<? extends Algorithm<?,?,?>> klass) {

		config.setComputationClass(GiraphComputationMapper.class);
		Class<? extends Writable>[] classes = (Class<? extends Writable>[]) TypeResolver.resolveRawArguments(Algorithm.class, klass);
		
		GiraphConstants.VERTEX_ID_CLASS.set(config, (Class<? extends WritableComparable<?>>) classes[0]);
		GiraphConstants.VERTEX_VALUE_CLASS.set(config, classes[1]);
		GiraphConstants.INCOMING_MESSAGE_VALUE_CLASS.set(config, classes[1]);
		GiraphConstants.OUTGOING_MESSAGE_VALUE_CLASS.set(config,classes[1]);
		GiraphConstants.EDGE_VALUE_CLASS.set(config, classes[2]);
		
		
		GiraphConstants.COMPUTATION_LANGUAGE.set(config,Language.JYTHON);
	}

	@Override
	public void useAggregator() {
		config.setMasterComputeClass(AggregatorMasterCompute.class);

	}

	@Override
	public void useCombiner() {
		config.setMessageCombinerClass(GiraphCombinerMapper.class);

	}

	@Override
	public void setClass(String name, Class<?> value, Class<?> xface) {
		config.setClass(name, value, xface);

	}

	@Override
	public void setInt(String name, int value) {
		config.setInt(name, value);

	}

	@Override
	public void set(String name, String value) {
		config.set(name, value);
	}
	
	@Override
	public void preparePlatformConfig() {
		config.setClass(GiraphOutEdgesMapper.OUTEDGES,
				config.getOutEdgesClass(), OutEdges.class);
		config.setOutEdgesClass(GiraphOutEdgesMapper.class);
	}

	
	final class ParameterizedTypeImpl implements ParameterizedType {

		  private final Type rawType;
		  private final Type[] actualTypeArguments;
		  private final Type owner;

		  public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type owner) {
		    this.rawType = rawType;
		    this.actualTypeArguments = actualTypeArguments;
		    this.owner = owner;
		  }

		  public Type getRawType() {
		    return rawType;
		  }

		  public Type[] getActualTypeArguments() {
		    return actualTypeArguments;
		  }

		  public Type getOwnerType() {
		    return owner;
		  }

		  @Override
		  public boolean equals(Object o) {
		    if (!(o instanceof  ParameterizedType)) {
		      return false;
		    }
		    // Check that information is equivalent
		    ParameterizedType that = (ParameterizedType) o;
		    if (this  == that) {
		      return true;
		    }
		    Type thatOwner = that.getOwnerType();
		    Type thatRawType = that.getRawType();

		    return (owner == null ? thatOwner == null : owner.equals(thatOwner))
		      && (rawType == null ? thatRawType == null : rawType.equals(thatRawType))
		      && Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
		  }

		  @Override
		  public int hashCode() {
		    return Arrays.hashCode(actualTypeArguments)
		        ^ (owner == null ? 0 : owner.hashCode())
		        ^ (rawType == null ? 0 : rawType.hashCode());
		  }
		}
}

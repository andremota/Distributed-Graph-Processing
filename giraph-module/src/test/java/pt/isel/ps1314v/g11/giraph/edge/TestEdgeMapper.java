package pt.isel.ps1314v.g11.giraph.edge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.conf.ImmutableClassesGiraphConfiguration;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.edge.EdgeFactory;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.giraph.utils.ReflectionUtils;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import pt.isel.ps1314v.g11.giraph.config.GiraphModuleConfiguration;
import pt.isel.ps1314v.g11.giraph.graph.GiraphEdgeMapper;
import pt.isel.ps1314v.g11.giraph.graph.GiraphOutEdgesMapper;

public class TestEdgeMapper {

	private static class DummyComputation
			extends
			BasicComputation<LongWritable, LongWritable, DoubleWritable, LongWritable> {

		@Override
		public void compute(
				Vertex<LongWritable, LongWritable, DoubleWritable> vertex,
				Iterable<LongWritable> messages) throws IOException {
			vertex.voteToHalt();

		}

	}

	private GiraphConfiguration giraphConf = new GiraphConfiguration();
	private GiraphModuleConfiguration giraphModuleConf = new GiraphModuleConfiguration(
			giraphConf);

	@Before
	public void initializeConf() {
		giraphConf.setComputationClass(DummyComputation.class);
		giraphModuleConf.preparePlatformConfig();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_giraph_out_edges_mapper_initialize() {

		GiraphOutEdgesMapper<LongWritable, DoubleWritable> outEdgesMapper = (GiraphOutEdgesMapper<LongWritable, DoubleWritable>) ReflectionUtils
				.newInstance(GiraphOutEdgesMapper.class,
						new ImmutableClassesGiraphConfiguration<>(giraphConf));

		assertNotNull(outEdgesMapper);

		assertEquals(0, outEdgesMapper.size());

		List<Edge<LongWritable, DoubleWritable>> initialEdges = Lists
				.newArrayList(EdgeFactory.create(new LongWritable(1),
						new DoubleWritable(1)), EdgeFactory.create(
						new LongWritable(2), new DoubleWritable(2)),
						EdgeFactory.create(new LongWritable(3),
								new DoubleWritable(3)), EdgeFactory.create(
								new LongWritable(2), new DoubleWritable(20)));

		outEdgesMapper.initialize(initialEdges);

		assertEquals(4, outEdgesMapper.size());

		int i = 0;
		for (Iterator<Edge<LongWritable, DoubleWritable>> it = outEdgesMapper
				.iterator(); it.hasNext(); ++i) {
			Edge<LongWritable, DoubleWritable> edge = it.next();
			assertTrue(edge.getClass().getName(),
					edge instanceof GiraphEdgeMapper<?, ?>);
			assertEquals(initialEdges.get(i).getTargetVertexId().get(), edge
					.getTargetVertexId().get());
			assertTrue(initialEdges.get(i).getValue().get() == edge.getValue()
					.get());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_giraph_out_edges_mapper_add_and_remove() {

		GiraphOutEdgesMapper<LongWritable, DoubleWritable> outEdgesMapper = (GiraphOutEdgesMapper<LongWritable, DoubleWritable>) ReflectionUtils
				.newInstance(GiraphOutEdgesMapper.class,
						new ImmutableClassesGiraphConfiguration<>(giraphConf));

		assertNotNull(outEdgesMapper);

		outEdgesMapper.initialize();

		assertEquals(0, outEdgesMapper.size());

		List<Edge<LongWritable, DoubleWritable>> initialEdges = Lists
				.newArrayList(	EdgeFactory.create(new LongWritable(1), new DoubleWritable(1)),
								EdgeFactory.create(new LongWritable(2), new DoubleWritable(2)),
								EdgeFactory.create(new LongWritable(3),	new DoubleWritable(3)),
								EdgeFactory.create(new LongWritable(4), new DoubleWritable(20)));


		for (Edge<LongWritable, DoubleWritable> e : initialEdges)
			outEdgesMapper.add(e);

		assertEquals(4, outEdgesMapper.size());

		outEdgesMapper.remove(new LongWritable(4));
		
		assertEquals(3, outEdgesMapper.size());
		
		outEdgesMapper.add(new GiraphEdgeMapper<LongWritable, DoubleWritable>(new LongWritable(4), new DoubleWritable(20)));
		
		int i = 0;
		for (Iterator<Edge<LongWritable, DoubleWritable>> it = outEdgesMapper
				.iterator(); it.hasNext(); ++i) {
			Edge<LongWritable, DoubleWritable> edge = it.next();
			assertTrue(edge.getClass().getName(),
					edge instanceof GiraphEdgeMapper<?, ?>);
			assertEquals(initialEdges.get(i).getTargetVertexId().get(), edge
					.getTargetVertexId().get());
			assertEquals(initialEdges.get(i).getValue().get(), edge.getValue()
					.get(), 0d);
		}
	}
}

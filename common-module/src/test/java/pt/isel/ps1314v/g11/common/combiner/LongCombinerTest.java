package pt.isel.ps1314v.g11.common.combiner;

import static org.junit.Assert.*;

import org.apache.hadoop.io.LongWritable;
import org.junit.Test;

public class LongCombinerTest {

	@Test
	public void test_long_sum_combiner() {
		LongSumCombiner combiner = new LongSumCombiner();
		
		assertNotNull(combiner.initialValue());
		
		LongWritable initial = new LongWritable(2l);
		LongWritable other = new LongWritable(4l);
		
		combiner.combine(initial, other);
		
		assertEquals(6l, initial.get());
		assertEquals(4l, other.get());
		
		assertEquals(0l, combiner.initialValue().get());
	}

}

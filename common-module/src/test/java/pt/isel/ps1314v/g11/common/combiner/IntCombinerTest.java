package pt.isel.ps1314v.g11.common.combiner;

import static org.junit.Assert.*;

import org.apache.hadoop.io.IntWritable;
import org.junit.Test;

public class IntCombinerTest {

	@Test
	public void test_int_sum_combiner() {
		IntSumCombiner combiner = new IntSumCombiner();
		
		assertNotNull(combiner.initialValue());
		
		IntWritable initial = new IntWritable(2);
		IntWritable other = new IntWritable(4);
		
		combiner.combine(initial, other);
		
		assertEquals(6, initial.get());
		assertEquals(4, other.get());
		
		assertEquals(0, combiner.initialValue().get());
	}

}

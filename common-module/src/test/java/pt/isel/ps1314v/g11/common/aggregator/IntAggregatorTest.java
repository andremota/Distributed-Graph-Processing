package pt.isel.ps1314v.g11.common.aggregator;

import static org.junit.Assert.*;

import org.apache.hadoop.io.IntWritable;
import org.junit.Test;

public class IntAggregatorTest {

	
	@Test
	public void test_int_sum_aggregator(){
		
		IntSumAggregator aggregator = new IntSumAggregator();
		
		assertNotNull(aggregator.initialValue());
		
		aggregator.aggregate(new IntWritable(2));
		assertEquals(2, aggregator.getValue().get());
		
		aggregator.aggregate(new IntWritable(4));
		assertEquals(6, aggregator.getValue().get());
		
		assertEquals(0, aggregator.initialValue().get());
		
	}
}

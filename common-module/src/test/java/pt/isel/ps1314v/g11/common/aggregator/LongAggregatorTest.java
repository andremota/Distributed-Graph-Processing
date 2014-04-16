package pt.isel.ps1314v.g11.common.aggregator;

import static org.junit.Assert.*;

import org.apache.hadoop.io.LongWritable;
import org.junit.Test;

public class LongAggregatorTest {

	
	@Test
	public void test_long_sum_aggregator(){
		
		LongSumAggregator aggregator = new LongSumAggregator();
		
		assertNotNull(aggregator.initialValue());
		
		aggregator.aggregate(new LongWritable(2l));
		assertEquals(2l, aggregator.getValue().get());
		
		aggregator.aggregate(new LongWritable(4l));
		assertEquals(6l, aggregator.getValue().get());
		
		assertEquals(0l, aggregator.initialValue().get());
		
	}

}

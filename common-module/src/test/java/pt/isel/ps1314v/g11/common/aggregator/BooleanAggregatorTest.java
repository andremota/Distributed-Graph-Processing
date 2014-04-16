package pt.isel.ps1314v.g11.common.aggregator;

import static org.junit.Assert.*;

import org.apache.hadoop.io.BooleanWritable;
import org.junit.Test;

public class BooleanAggregatorTest {

	@Test
	public void test_boolean_and_aggregator() {
		BooleanAndAggregator aggregator = new BooleanAndAggregator();
		
		assertNotNull(aggregator.initialValue());
		
		aggregator.aggregate(new BooleanWritable(true));
		assertTrue(aggregator.getValue().get());
		
		aggregator.aggregate(new BooleanWritable(false));
		assertFalse(aggregator.getValue().get());
		
		assertTrue(aggregator.initialValue().get());
	}

}

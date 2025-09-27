package com.karate.test;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SimpleTestRunner {

    @Test
    public void testSimpleCSVWithPassStatus() {
        Results results = Runner.path("classpath:com/karate/features")
                .tags("~@ignore")
                .parallel(1);
        assertTrue(results.getErrorMessages(), results.getFailCount() == 0);
    }
    
    @Test 
    public void testSpecificFeature() {
        Results results = Runner.path("classpath:com/karate/features/SimpleCSVWithPassStatus.feature").parallel(1);
        assertTrue(results.getErrorMessages(), results.getFailCount() == 0);
    }
}
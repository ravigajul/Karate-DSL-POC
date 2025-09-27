package com.karate.test;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.intuit.karate.Results;
import com.intuit.karate.Runner;

public class SimpleRowStatusTest {

    @Test
    public void testRowStatus() {
        System.setProperty("karate.env", "dev");
        
        // Run only the WorkingRowByRow feature with single thread (no parallel)
        Results results = Runner.path("classpath:com/karate/features/WorkingRowByRow.feature")
                .tags("@row-status")
                .parallel(1); // Single thread execution
        
        System.out.println("===== TEST RESULTS =====");
        System.out.println("Features run: " + results.getFeaturesTotal());
        System.out.println("Scenarios run: " + results.getScenariosTotal());
        System.out.println("Failed: " + results.getFailCount());
        System.out.println("========================");
        
        assertTrue("Test failed with errors: " + results.getErrorMessages(), 
                   results.getFailCount() == 0);
    }
}

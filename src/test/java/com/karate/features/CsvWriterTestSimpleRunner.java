package com.karate.features;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.intuit.karate.Results;
import com.intuit.karate.Runner;

public class CsvWriterTestSimpleRunner {
    
    @Test
    public void testCsvWriter() {
        Results results = Runner.builder()
            .path("classpath:com/karate/features/WriteToSameFile.feature")
            .parallel(1);
        assertTrue(results.getErrorMessages(), results.getFailCount() == 0);
    }
}
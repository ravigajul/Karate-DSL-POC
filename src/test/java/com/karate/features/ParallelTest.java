package com.karate.features;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParallelTest {

	@Test
	public void testParallel() {
		System.setProperty("karate.env", "qa");
		// Results results =
		// Runner.path("classpath:com/karate/features").parallel(5);
		/*
		 * /the above line is throwing INFO: Unexpected error
		 * net.masterthought.cucumber.ValidationException: None report file was
		 * added! Hence using the below
		 */
		Results results = Runner.builder().outputCucumberJson(true).path("classpath:com/karate/features").tags("@CSVDemo")
				.parallel(10);
		generateReport(results.getReportDir());
		assertTrue(results.getErrorMessages(), results.getFailCount() == 0);
	}

	public static void generateReport(String karateOutputPath) {
		Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutputPath), new String[] { "json" }, true);
		List<String> jsonPaths = new ArrayList(jsonFiles.size());
		jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
		Configuration config = new Configuration(new File("target"), "Karate-DSL-POC");
		ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
		reportBuilder.generateReports();
	}

	/*
	 * @Test void testParallel() { Results results =
	 * Runner.path("classpath:com/karate/features").parallel(5); assertEquals(0,
	 * results.getFailCount(), results.getErrorMessages()); }
	 */

}
package com.karate.features;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

public class ParallelTest {
	
	@Test
	public void testParallel() {
		System.setProperty("karate.env", "qa"); //this line should be commentd if env from cmd is to be picked
		// Results results =
		// Runner.path("classpath:com/karate/features").parallel(5);
		/*
		 * /the above line is throwing INFO: Unexpected error
		 * net.masterthought.cucumber.ValidationException: None report file was added!
		 * Hence using the below
		 */
		Results results = Runner.builder().outputCucumberJson(true).path("classpath:com/karate/features/WriteToSameFile.feature")
				.tags("@same-file").parallel(1);
		generateReport(results.getReportDir());
		assertTrue(results.getErrorMessages(), results.getFailCount() == 0);
		/*
		 * printJson(System.getProperty("user.dir") + "\\target\\karate-reports\\" +
		 * "com.karate.features.DummyBefore.json");
		 */	}

	public static void generateReport(String karateOutputPath) {
		Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutputPath), new String[] { "json" }, true);
		List<String> jsonPaths = new ArrayList(jsonFiles.size());
		jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
		Configuration config = new Configuration(new File("target"), "Karate-DSL-POC");
		ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
		reportBuilder.generateReports();
	}
	
	public  void printJson() {
		String jsonPath=System.getProperty("user.dir") + "\\target\\karate-reports\\" +
				  "com.karate.features.DummyBefore.json";
		JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader(jsonPath))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray employeeList = (JSONArray) obj;
            System.out.println(employeeList);
             
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
 
	}
	
	//@After
	public void postToXRay()
	{
		  String requestBody = "{\n" +
		            "  \"title\": \"foo\",\n" +
		            "  \"body\": \"bar\",\n" +
		            "  \"userId\": \"1\" \n}";
		RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
		Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .extract().response();
		System.out.println(response.asPrettyString());
        Assertions.assertEquals(201, response.statusCode());
        System.out.println("Successfully posted");
		 
	}

}
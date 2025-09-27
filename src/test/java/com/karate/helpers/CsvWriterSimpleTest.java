package com.karate.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Simple test to demonstrate and verify the CsvWriter.updateCSVWithStatus method
 */
public class CsvWriterSimpleTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing CsvWriter.updateCSVWithStatus ===");
        
        // Create a test CSV file first
        createTestCsvFile();
        
        // Test the updateCSVWithStatus method
        testUpdateMethod();
    }
    
    private static void createTestCsvFile() {
        try {
            String testContent = "ID,Execute,FirstName,LastName,Email\n" +
                               "1,Y,John,Doe,john.doe@test.com\n" +
                               "2,N,Jane,Smith,jane.smith@test.com\n" +
                               "3,Y,Bob,Wilson,bob.wilson@test.com\n";
            
            String filePath = "src/test/java/com/karate/data/test-output.csv";
            Files.write(Paths.get(filePath), testContent.getBytes());
            
            System.out.println("✅ Test CSV file created at: " + filePath);
            System.out.println("\n--- Initial CSV Content ---");
            displayCsvContent(filePath);
            
        } catch (IOException e) {
            System.err.println("❌ Error creating test file: " + e.getMessage());
        }
    }
    
    private static void testUpdateMethod() {
        String csvPath = "classpath:com/karate/data/test-output.csv";
        String actualPath = "src/test/java/com/karate/data/test-output.csv";
        
        try {
            // Test 1: Add new column and update row 1
            System.out.println("\n=== Test 1: Adding 'ExecutionStatus' column and updating row 1 ===");
            CsvWriter.updateCSVWithStatus(csvPath, 1, "PASS", "ExecutionStatus");
            displayCsvContent(actualPath);
            
            // Test 2: Update row 2 with same column
            System.out.println("\n=== Test 2: Updating row 2 with 'FAIL' ===");
            CsvWriter.updateCSVWithStatus(csvPath, 2, "FAIL", "ExecutionStatus");
            displayCsvContent(actualPath);
            
            // Test 3: Update row 3 with same column
            System.out.println("\n=== Test 3: Updating row 3 with 'SKIP' ===");
            CsvWriter.updateCSVWithStatus(csvPath, 3, "SKIP", "ExecutionStatus");
            displayCsvContent(actualPath);
            
            // Test 4: Add another column
            System.out.println("\n=== Test 4: Adding 'TestResult' column and updating row 2 ===");
            CsvWriter.updateCSVWithStatus(csvPath, 2, "COMPLETED", "TestResult");
            displayCsvContent(actualPath);
            
            // Test 5: Update existing column
            System.out.println("\n=== Test 5: Updating existing ExecutionStatus for row 1 ===");
            CsvWriter.updateCSVWithStatus(csvPath, 1, "RERUN", "ExecutionStatus");
            displayCsvContent(actualPath);
            
            System.out.println("\n✅ All tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void displayCsvContent(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 0; i < lines.size(); i++) {
                if (i == 0) {
                    System.out.println("Header: " + lines.get(i));
                } else {
                    System.out.println("Row " + i + ":   " + lines.get(i));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
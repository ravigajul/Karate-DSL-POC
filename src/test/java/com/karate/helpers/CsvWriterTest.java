package com.karate.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CsvWriterTest {
    
    public static void main(String[] args) {
        testUpdateCSVWithStatus();
    }
    
    public static void testUpdateCSVWithStatus() {
        try {
            // Test file paths
            String testCsvPath = "src/test/resources/com/karate/data/test-sample.csv";
            String classpathPath = "classpath:com/karate/data/test-sample.csv";
            
            // Create test CSV file first
            createTestCsvFile(testCsvPath);
            
            System.out.println("=== Testing updateCSVWithStatus Function ===");
            
            // Display original CSV content
            System.out.println("\n--- Original CSV Content ---");
            displayCsvContent(testCsvPath);
            
            // Test 1: Add 'pass' status to row 1 in a new column
            System.out.println("\n--- Test 1: Adding 'pass' to row 1 in 'testStatus' column (new column) ---");
            CsvWriter.updateCSVWithStatus(classpathPath, 1, "pass", "testStatus");
            displayCsvContent(testCsvPath);
            
            // Test 2: Add 'pass' status to row 2 in the same column
            System.out.println("\n--- Test 2: Adding 'pass' to row 2 in existing 'testStatus' column ---");
            CsvWriter.updateCSVWithStatus(classpathPath, 2, "pass", "testStatus");
            displayCsvContent(testCsvPath);
            
            // Test 3: Add 'fail' status to row 3
            System.out.println("\n--- Test 3: Adding 'fail' to row 3 in 'testStatus' column ---");
            CsvWriter.updateCSVWithStatus(classpathPath, 3, "fail", "testStatus");
            displayCsvContent(testCsvPath);
            
            // Test 4: Add another column 'executionResult' with 'completed'
            System.out.println("\n--- Test 4: Adding 'completed' to row 1 in 'executionResult' column (another new column) ---");
            CsvWriter.updateCSVWithStatus(classpathPath, 1, "completed", "executionResult");
            displayCsvContent(testCsvPath);
            
            // Test 5: Update multiple rows in executionResult column
            System.out.println("\n--- Test 5: Adding 'pending' to row 2 and 'completed' to row 3 in 'executionResult' column ---");
            CsvWriter.updateCSVWithStatus(classpathPath, 2, "pending", "executionResult");
            CsvWriter.updateCSVWithStatus(classpathPath, 3, "completed", "executionResult");
            displayCsvContent(testCsvPath);
            
            System.out.println("\n=== All tests completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createTestCsvFile(String filePath) throws IOException {
        // Create directories if they don't exist
        Files.createDirectories(Paths.get(filePath).getParent());
        
        // Create test CSV with id, firstname, lastname columns as requested
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("id,firstname,lastname\n");
        csvContent.append("1,John,Doe\n");
        csvContent.append("2,Jane,Smith\n");
        csvContent.append("3,Bob,Johnson\n");
        csvContent.append("4,Alice,Williams\n");
        
        // Write to file
        Files.write(Paths.get(filePath), csvContent.toString().getBytes());
        System.out.println("Test CSV file created at: " + filePath);
    }
    
    private static void displayCsvContent(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 0; i < lines.size(); i++) {
                System.out.println("Row " + i + ": " + lines.get(i));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
package com.karate.helpers;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class CsvWriter {
    
    public static void writeResultsToCSV(List<Map<String, Object>> results, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            if (!results.isEmpty()) {
                Map<String, Object> firstRow = results.get(0);
                String header = String.join(",", firstRow.keySet());
                writer.append(header).append("\n");
                
                // Write data rows
                for (Map<String, Object> row : results) {
                    String dataRow = String.join(",", 
                        row.values().stream()
                           .map(value -> value != null ? value.toString() : "")
                           .toArray(String[]::new));
                    writer.append(dataRow).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void appendResultToCSV(Map<String, Object> result, String filePath) {
        try {
            boolean fileExists = Files.exists(Paths.get(filePath));
            
            try (FileWriter writer = new FileWriter(filePath, true)) {
                // Write header if file doesn't exist
                if (!fileExists) {
                    String header = String.join(",", result.keySet());
                    writer.append(header).append("\n");
                }
                
                // Write data row
                String dataRow = String.join(",", 
                    result.values().stream()
                          .map(value -> value != null ? value.toString() : "")
                          .toArray(String[]::new));
                writer.append(dataRow).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void createResultCSV(String id, String execute, String firstName, String lastName, 
                                     String email, String endPointUrl, String queryPath, 
                                     String responseStatus, String responseTime, String testResult, 
                                     String filePath) {
        try {
            boolean fileExists = Files.exists(Paths.get(filePath));
            
            try (FileWriter writer = new FileWriter(filePath, true)) {
                // Write header if file doesn't exist
                if (!fileExists) {
                    writer.append("ID,Execute,FirstName,LastName,Email,endPointUrl,querypath,ResponseStatus,ResponseTime,TestResult\n");
                }
                
                // Write data row
                String dataRow = String.join(",", id, execute, firstName, lastName, email, 
                                            endPointUrl, queryPath, responseStatus, responseTime, testResult);
                writer.append(dataRow).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateCSVWithStatus(String csvPath, int rowNumber, String status, String columnName) {
        try {
            // Convert classpath resource to actual file path for reading
            String actualPath = csvPath.replace("classpath:", "src/test/resources/");
            List<String> lines = Files.readAllLines(Paths.get(actualPath));
            
            if (lines.isEmpty() || rowNumber >= lines.size() || rowNumber < 1) {
                return; // Invalid row number or empty file
            }
            
            // Get header line to find column index
            String[] headers = lines.get(0).split(",");
            int columnIndex = -1;
            
            // Find the column index for the given column name
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(columnName.trim())) {
                    columnIndex = i;
                    break;
                }
            }
            
            // If column doesn't exist, add it to header and all rows
            if (columnIndex == -1) {
                columnIndex = headers.length;
                // Add column to header
                lines.set(0, lines.get(0) + "," + columnName);
                
                // Add empty column to all existing data rows EXCEPT the target row
                // The target row will be handled separately below to set the actual status value
                for (int i = 1; i < lines.size(); i++) {
                    if (i != rowNumber) {
                        lines.set(i, lines.get(i) + ",");
                    }
                }
            }
            
            // Update ONLY the specific target row with the status value
            String line = lines.get(rowNumber);
            String[] parts = line.split(",");
            
            // Ensure the row has enough columns
            if (parts.length <= columnIndex) {
                // Extend the row with empty values
                String[] newParts = new String[columnIndex + 1];
                System.arraycopy(parts, 0, newParts, 0, parts.length);
                for (int i = parts.length; i < columnIndex; i++) {
                    newParts[i] = "";
                }
                newParts[columnIndex] = status;
                lines.set(rowNumber, String.join(",", newParts));
            } else {
                // Update existing column
                parts[columnIndex] = status;
                lines.set(rowNumber, String.join(",", parts));
            }
            
            // Write all lines back to file (preserves all other rows unchanged)
            Files.write(Paths.get(actualPath), lines);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void clearStatusColumn(String csvPath, String columnName) {
        try {
            // Convert classpath resource to actual file path for reading
            String actualPath = csvPath.replace("classpath:", "src/test/resources/");
            List<String> lines = Files.readAllLines(Paths.get(actualPath));
            
            if (lines.isEmpty()) {
                return;
            }
            
            // Get header line to find column index
            String[] headers = lines.get(0).split(",");
            int columnIndex = -1;
            
            // Find the column index for the given column name
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(columnName.trim())) {
                    columnIndex = i;
                    break;
                }
            }
            
            // If column doesn't exist, nothing to clear
            if (columnIndex == -1) {
                return;
            }
            
            // Clear values in the specified column for all data rows (skip header)
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");
                
                if (parts.length > columnIndex) {
                    parts[columnIndex] = ""; // Clear the value
                    lines.set(i, String.join(",", parts));
                }
            }
            
            // Write back to file
            Files.write(Paths.get(actualPath), lines);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

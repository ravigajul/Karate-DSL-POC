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
    
    public static void updateCSVWithStatus(String csvPath, int rowNumber, String status) {
        try {
            // Convert classpath resource to actual file path for reading
            String actualPath = csvPath.replace("classpath:", "src/test/resources/");
            List<String> lines = Files.readAllLines(Paths.get(actualPath));
            
            if (rowNumber < lines.size()) {
                String line = lines.get(rowNumber);
                // If the line doesn't end with a status, append it
                if (!line.endsWith(",pass") && !line.endsWith(",fail")) {
                    lines.set(rowNumber, line + "," + status);
                } else {
                    // Update existing status
                    String[] parts = line.split(",");
                    parts[parts.length - 1] = status;
                    lines.set(rowNumber, String.join(",", parts));
                }
                
                // Write back to file
                Files.write(Paths.get(actualPath), lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void clearStatusColumn(String csvPath) {
        try {
            // Convert classpath resource to actual file path for reading
            String actualPath = csvPath.replace("classpath:", "src/test/resources/");
            List<String> lines = Files.readAllLines(Paths.get(actualPath));
            
            if (!lines.isEmpty()) {
                // Keep the header (first line) as is
                for (int i = 1; i < lines.size(); i++) {
                    String line = lines.get(i);
                    
                    // Remove status column if it exists (assuming status is the last column)
                    if (line.endsWith(",pass") || line.endsWith(",fail")) {
                        String[] parts = line.split(",");
                        if (parts.length > 1) {
                            // Remove the last column (status)
                            String[] newParts = new String[parts.length - 1];
                            System.arraycopy(parts, 0, newParts, 0, parts.length - 1);
                            lines.set(i, String.join(",", newParts));
                        }
                    }
                }
                
                // Write back to file
                Files.write(Paths.get(actualPath), lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

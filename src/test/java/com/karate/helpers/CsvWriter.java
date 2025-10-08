package com.karate.helpers;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A utility class for updating CSV files
 * Provides methods to update specific rows and columns in CSV files while maintaining proper CSV formatting
 */
public class CsvWriter {
    
    /**
     * Updates a specific row in a CSV file with a new value for the specified column
     * @param csvPath Path to the CSV file
     * @param rowNumber Row number to update (1-based, where 1 is the first data row after header)
     * @param columnName Name of the column to update
     * @param newValue New value to set for the column
     * @throws IOException If there's an error reading or writing the file
     * @throws IllegalArgumentException If the column is not found, file is invalid, or row number is invalid
     */
    public static void updateColumn(String csvPath, int rowNumber, String columnName, String newValue) throws IOException {
        Path path = Paths.get(csvPath);
        
        if (!Files.exists(path)) {
            throw new FileNotFoundException("CSV file not found: " + csvPath);
        }
        
        List<String> lines = Files.readAllLines(path);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("CSV file is empty");
        }
        
        if (rowNumber < 1 || rowNumber >= lines.size()) {
            throw new IllegalArgumentException("Row number " + rowNumber + " is out of bounds. Valid range: 1 to " + (lines.size() - 1));
        }
        
        // Parse header row to find column index
        String headerRow = lines.get(0);
        String[] headers = parseCSVLineAsArray(headerRow);
        int columnIndex = findColumnIndex(headers, columnName);
        
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Column '" + columnName + "' not found in CSV file");
        }
        
        // Update specific row
        int actualRowIndex = rowNumber; // rowNumber is 1-based, but we need 0-based index for lines array
        String line = lines.get(actualRowIndex);
        String[] values = parseCSVLineAsArray(line);
        
        if (columnIndex < values.length) {
            values[columnIndex] = escapeCSVValue(newValue);
            lines.set(actualRowIndex, String.join(",", values));
        }
        
        // Write back to file
        Files.write(path, lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Successfully updated row " + rowNumber + ", column '" + columnName + "' with value '" + newValue + "' in " + csvPath);
    }
    
    /**
     * Updates a specific row and column in a CSV file (alternative method with 0-based indexing)
     * @param csvPath Path to the CSV file
     * @param rowIndex Index of the row to update (0-based, excluding header)
     * @param columnName Name of the column to update
     * @param newValue New value to set
     * @throws IOException If there's an error reading or writing the file
     * @throws IllegalArgumentException If the column is not found or row index is invalid
     */
    public static void updateCell(String csvPath, int rowIndex, String columnName, String newValue) throws IOException {
        // Convert 0-based to 1-based and call the main method
        updateColumn(csvPath, rowIndex + 1, columnName, newValue);
    }
    
    /**
     * Reads and returns the CSV content as a 2D array
     * @param csvPath Path to the CSV file
     * @return 2D String array representing the CSV data
     * @throws IOException If there's an error reading the file
     */
    public static String[][] readCsv(String csvPath) throws IOException {
        Path path = Paths.get(csvPath);
        List<String> lines = Files.readAllLines(path);
        
        if (lines.isEmpty()) {
            return new String[0][0];
        }
        
        List<String[]> data = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                data.add(parseCSVLineAsArray(line));
            }
        }
        
        return data.toArray(new String[0][]);
    }
    
    /**
     * Gets the column names from the CSV file
     * @param csvPath Path to the CSV file
     * @return Array of column names
     * @throws IOException If there's an error reading the file
     */
    public static String[] getColumnNames(String csvPath) throws IOException {
        Path path = Paths.get(csvPath);
        List<String> lines = Files.readAllLines(path);
        
        if (lines.isEmpty()) {
            return new String[0];
        }
        
        return parseCSVLineAsArray(lines.get(0));
    }
    
    /**
     * Helper method to display CSV content (for demo purposes)
     * @param csvPath Path to the CSV file
     */
    public static void displayCSVForDemo(String csvPath) throws IOException {
        Path path = Paths.get(csvPath);
        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            System.out.println(line);
        }
    }
    
    // Private helper methods
    
    /**
     * Finds the index of a column by name (case-insensitive)
     * @param headers Array of header names
     * @param columnName Name to search for
     * @return Column index or -1 if not found
     */
    private static int findColumnIndex(String[] headers, String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(columnName.trim())) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Parses a CSV line handling quotes and commas properly
     * @param line CSV line to parse
     * @return List of values
     */
    private static List<String> parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                // Check if this is the start of a quoted field
                if (!inQuotes && currentField.length() == 0) {
                    inQuotes = true;
                    // Don't add the opening quote to the field value
                } else if (inQuotes) {
                    // Check for escaped quote (double quotes)
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        currentField.append('"');
                        i++; // Skip the next quote
                    } else {
                        // End of quoted field
                        inQuotes = false;
                        // Don't add the closing quote to the field value
                    }
                } else {
                    // Quote in the middle of unquoted field (shouldn't happen in well-formed CSV)
                    currentField.append(c);
                }
            } else if (c == ',' && !inQuotes) {
                // End of field
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        // Add the last field
        fields.add(currentField.toString());
        return fields;
    }
    
    /**
     * Helper method to convert List to Array for backward compatibility
     */
    private static String[] parseCSVLineAsArray(String line) {
        List<String> fields = parseCSVLine(line);
        return fields.toArray(new String[0]);
    }    public static void updateCSVWithStatus(String filePath, int rowIndex, String statusValue, String statusColumnName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            if (lines.isEmpty()) return;
            
            // Parse header to find status column index
            List<String> headers = parseCSVLine(lines.get(0));
            int statusColumnIndex = -1;
            
            for (int i = 0; i < headers.size(); i++) {
                if (headers.get(i).equals(statusColumnName)) {
                    statusColumnIndex = i;
                    break;
                }
            }
            
            // If status column doesn't exist, add it
            if (statusColumnIndex == -1) {
                statusColumnIndex = headers.size();
                headers.add(statusColumnName);
                // Update header line
                lines.set(0, String.join(",", headers.stream().map(CsvWriter::escapeCSVValue).collect(Collectors.toList())));
            }
            
            // Update the specified row
            if (rowIndex < lines.size()) {
                List<String> rowData = parseCSVLine(lines.get(rowIndex));
                
                // Ensure row has enough columns
                while (rowData.size() <= statusColumnIndex) {
                    rowData.add("");
                }
                
                // Update status value
                rowData.set(statusColumnIndex, statusValue);
                
                // Rebuild the line with proper CSV escaping
                String updatedLine = rowData.stream()
                    .map(CsvWriter::escapeCSVValue)
                    .collect(Collectors.joining(","));
                
                lines.set(rowIndex, updatedLine);
            }
            
            // Write back to file
            Files.write(Paths.get(filePath), lines);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Escapes a value for CSV format (adds quotes if necessary)
     * @param value Value to escape
     * @return Escaped value
     */
    private static String escapeCSVValue(String value) {
        if (value == null) {
            return "";
        }
        
        // If value contains comma, quote, or newline, wrap in quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            // Escape any existing quotes by doubling them
            String escaped = value.replace("\"", "\"\"");
            return "\"" + escaped + "\"";
        }
        
        return value;
    }
}
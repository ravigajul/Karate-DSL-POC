package com.karate.helpers;
import java.io.*;
import java.nio.file.*;
import java.util.*;

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
        String[] headers = parseCSVLine(headerRow);
        int columnIndex = findColumnIndex(headers, columnName);
        
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Column '" + columnName + "' not found in CSV file");
        }
        
        // Update specific row
        int actualRowIndex = rowNumber; // rowNumber is 1-based, but we need 0-based index for lines array
        String line = lines.get(actualRowIndex);
        String[] values = parseCSVLine(line);
        
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
                data.add(parseCSVLine(line));
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
        
        return parseCSVLine(lines.get(0));
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
     * @return Array of values
     */
    private static String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote (double quotes within quoted string)
                    currentValue.append('"');
                    i++; // Skip next quote
                } else {
                    // Start or end of quoted value
                    inQuotes = !inQuotes;
                }
<<<<<<< HEAD
            } else if (c == ',' && !inQuotes) {
                // End of current value
                values.add(currentValue.toString().trim());
                currentValue.setLength(0);
            } else {
                currentValue.append(c);
=======
                
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
            String[] headers = lines.get(0).split(",", -1);
            int columnIndex = -1;
            
            // Find the column index for the given column name
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(columnName.trim())) {
                    columnIndex = i;
                    break;
                }
            }
            
            // If column doesn't exist, add it to header and extend all rows
            if (columnIndex == -1) {
                columnIndex = headers.length;
                // Add column to header
                lines.set(0, lines.get(0) + "," + columnName);
                
                // Add empty column to all existing data rows
                for (int i = 1; i < lines.size(); i++) {
                    lines.set(i, lines.get(i) + ",");
                }
            }
            
            // Now update the specific target row with the status value
            String targetLine = lines.get(rowNumber);
            String[] parts = targetLine.split(",", -1); // Use -1 to preserve trailing empty strings
            
            // Ensure we have enough columns in the target row
            if (parts.length <= columnIndex) {
                // This shouldn't happen if we added the column correctly above, but just in case
                String[] newParts = new String[columnIndex + 1];
                System.arraycopy(parts, 0, newParts, 0, parts.length);
                for (int i = parts.length; i <= columnIndex; i++) {
                    newParts[i] = (i == columnIndex) ? status : "";
                }
                lines.set(rowNumber, String.join(",", newParts));
            } else {
                // Update the specific column with the status
                parts[columnIndex] = status;
                lines.set(rowNumber, String.join(",", parts));
>>>>>>> 2ba59ae (clean up)
            }
        }
        
        // Add the last value
        values.add(currentValue.toString().trim());
        
        return values.toArray(new String[0]);
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
            // Escape existing quotes by doubling them
            String escaped = value.replace("\"", "\"\"");
            return "\"" + escaped + "\"";
        }
        
        return value;
    }
}

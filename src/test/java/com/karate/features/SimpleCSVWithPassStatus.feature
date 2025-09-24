Feature: Simple CSV Processing with Status Update

Background:
    * def CsvWriter = Java.type('com.karate.helpers.CsvWriter')

Scenario Outline: Process CSV data and update status
    * def csvFilePath = 'com/karate/data/simple-testdata.csv'
    * def rowIndex = __num + 1  # Convert 0-based index to 1-based for CSV row
    
    # Process the data (simulate some test logic)
    Given url 'https://reqres.in'
    And path 'api/users'
    And param page = 1
    When method GET
    Then status 200
    And match response.data != null
    
    # Update CSV with pass status for this row
    * CsvWriter.updateCSVWithStatus('classpath:' + csvFilePath, rowIndex, 'pass')
    * print 'Updated row', rowIndex, 'with status: pass'
    
    Examples:
    | read('classpath:com/karate/data/simple-testdata.csv') |

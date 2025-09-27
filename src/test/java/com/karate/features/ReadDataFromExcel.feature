Feature: Read Data From Excel and Write Status Back

Background:
    * def csvWriter = Java.type('com.karate.helpers.CsvWriter')

@CSVDemo-debug
Scenario Outline: Process CSV Row with Status Update
    * print 'Processing row:', __num + 1, 'with data:', __row
    * def status = '<Execute>' == 'Y' ? 'Passed' : 'Skipped'
    * csvWriter.updateCSVWithStatus('src/test/java/com/karate/data/testdata.csv', __num + 1, status, 'Status')
    * print 'Updated row', __num + 1, 'with status:', status

Examples:
| read('classpath:com/karate/data/testdata.csv') |
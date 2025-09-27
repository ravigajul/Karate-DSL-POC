Feature: Enhanced CSV Reading with Response Status Writing

Background:
    * url 'https://reqres.in'

@csvWithStatus
Scenario Outline: Test API and capture response status
    * print 'Processing ID:', <ID>, 'Execute:', <Execute>
    
    # Only execute if Execute flag is 'Y' and endpoint is provided
    * def shouldExecute = <Execute> == 'Y' && <endPointUrl> != null && <endPointUrl> != ''
    
    # Initialize result variables
    * def apiResponseStatus = 'N/A'
    * def apiResponseTime = 'N/A'
    * def testResult = 'SKIPPED'
    
    # Execute API call if conditions are met
    * if (shouldExecute)
      """
      Given path <endPointUrl>
      When method GET
      Then status 200
      * apiResponseStatus = responseStatus
      * apiResponseTime = responseTime
      * testResult = responseStatus == 200 ? 'PASS' : 'FAIL'
      * print 'API Response Status:', apiResponseStatus
      * print 'API Response Time:', apiResponseTime
      """
    
    # Prepare result data
    * def resultData = 
      """
      {
        id: '<ID>',
        execute: '<Execute>',
        firstName: '<FirstName>',
        lastName: '<LastName>',
        email: '<Email>',
        endPointUrl: '<endPointUrl>',
        queryPath: '<querypath>',
        responseStatus: '#(apiResponseStatus)',
        responseTime: '#(apiResponseTime)',
        testResult: '#(testResult)',
        executedAt: '#(new Date().toISOString())'
      }
      """
    
    # Create CSV line (manually format to ensure proper CSV structure)
    * def csvLine = <ID> + ',' + <Execute> + ',' + <FirstName> + ',' + <LastName> + ',' + <Email> + ',' + <endPointUrl> + ',' + <querypath> + ',' + apiResponseStatus + ',' + apiResponseTime + ',' + testResult
    
    # Append to CSV file (this will create the file if it doesn't exist)
    * def csvHeader = 'ID,Execute,FirstName,LastName,Email,endPointUrl,querypath,ResponseStatus,ResponseTime,TestResult'
    
    # Check if file exists, if not create with header
    * def timestamp = new Date().getTime()
    * def outputFile = 'target/test-results-' + timestamp + '.csv'
    
    # Write header and data
    * java.type('java.nio.file.Files').write(java.type('java.nio.file.Paths').get(outputFile), (csvHeader + '\n' + csvLine).getBytes())

Examples:
    | read('classpath:com/karate/data/testdata.csv') |

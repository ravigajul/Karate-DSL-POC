Feature: This feature is used to be called from ReadDataFromExcel.feature
Background: background
    * url 'https://reqres.in'
@getcall
Scenario: This is a test scenario
    Given path endPointUrl
    When method GET
    Then status 200
    * def result = response.body
    * print response
 

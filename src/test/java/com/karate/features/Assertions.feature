Feature: sample karate test script
  for help, see: https://github.com/intuit/karate/wiki/IDE-Support

  Background: 
    * url baseurl

  @smoke2222
  Scenario: A simple get call
    Given path '/api/tags'
    When method GET
    Then status 200
    * print response

  
  Scenario: Passing params to get call
    Given path '/api/articles'
    And param limit = 10
    And param offset = 0
    When method GET
    Then status 200

  Scenario: Passing params through json to get call
    Given path '/api/articles'
    And params { limit: 10, offset: 0 }
    When method GET
    Then status 200

  Scenario: Assertion on the response text
    Given path '/api/tags'
    When method GET
    Then status 200
    And match response.tags contains 'Gandhi'

  @smoke
  Scenario: Multiple Assertions on the response text
    Given path '/api/tags'
    When method GET
    Then status 200
    And match response.tags contains ['Gandhi', 'dragons']
    And match response.tags !contains 'cars'

  Scenario: Verify that the response tag is an array using karate markers
    Given path '/api/tags'
    When method GET
    Then status 200
    And match response.tags == '#array'

  Scenario: Verify that the response tag item is a string using karate markers
    Given path '/api/tags'
    When method GET
    Then status 200
    And match response.tags[0] == '#string'

  @smoke
  Scenario: Looping through each response tag item to validate if it is a string
    Given path '/api/tags'
    When method GET
    Then status 200
    And match each response.tags == '#string'

  Scenario: verify array size of articles is 10
    Given path '/api/articles'
    And params { limit: 10, offset: 0 }
    When method GET
    Then status 200
    And match response.articles == '#[10]'
    And match response.articlesCount == 500

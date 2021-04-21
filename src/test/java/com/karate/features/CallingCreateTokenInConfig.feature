Feature: Feature to demonstrate feature calling from config file

  Background: 
    Given url baseurl

  @callingfeatureinconfig
  Scenario: Calling other feature to get authtoken and use it in this scenario
    Given path '/articles/'
    And request {"article":{"tagList":[],"title":"test article","description":"this is test article","body":"he there test article"}}
    When method post
    Then status 200
    * print response
    And match response.article.title == 'test article'
Feature: Feature to demonstrate calling other feature 

  Background: 
    Given url baseurl
    * def responseToken = callonce read('classpath:com/karate/helpers/CreateTokenConfigVariables.feature')
    #responseToken is an object of all the variables defined in createtoken feature
    * def authtoken = responseToken.authToken

  @debug
  Scenario: Calling other feature to get authtoken and use it in this scenario
    Given path '/api/articles/'
    And header Authorization = 'Token ' + authtoken
    And request {"article":{"tagList":[],"title":"test article","description":"this is test article","body":"he there test article"}}
    When method post
    Then status 200
    * print response
    And match response.article.title == 'test article'
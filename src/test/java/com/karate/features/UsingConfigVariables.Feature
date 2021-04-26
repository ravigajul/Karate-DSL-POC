Feature: Feature to demonstrate passing config values to other feature file 
  Background: 
    Given url baseurl
    * def responseToken = callonce read('classpath:com/karate/helpers/CreateTokenConfigVariables.feature')
    #responseToken is an object of all the variables defined in createtoken feature
    * def authtoken = responseToken.authToken
    * print authtoken

  @configparams
 # mvn test -Dkarate.options=--tags @configparams -Dkarate.env=qa
  Scenario: Calling other feature to get authtoken and use it in this scenario
    Given path '/api/articles/'
    And header Authorization = 'Token ' + authtoken
    And request read('classpath:com/test/resources/Articles_Request.json')
    When method post
    Then status 200
    * print response
    And match response.article.title == 'test article'
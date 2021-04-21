Feature: Feature to demonstrate passing config values to other feature file 
  Background: 
    Given url baseurl
    * def responseToken = callonce read('classpath:com/karate/helpers/CreateTokenParameterized.feature')
    #responseToken is an object of all the variables defined in createtoken feature
    * def authtoken = responseToken.authToken
    * print authtoken

  @configparams
 # mvn test -Dkarate.options=--tags @configparams -Dkarate.env=qa
  Scenario: Calling other feature to get authtoken and use it in this scenario
    Given path '/articles/'
    And header Authorization = 'Token ' + authtoken
    And request {"article":{"tagList":[],"title":"test article","description":"this is test article","body":"he there test article"}}
    When method post
    Then status 200
    * print response
    And match response.article.title == 'test article'
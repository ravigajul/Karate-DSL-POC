Feature: Feature to demonstrate passing values to other feature file while calling it

  Background: 
    Given url baseurl
    * def responseToken = callonce read('classpath:com/karate/helpers/CreateTokenParameterized.feature') {'paramemail': 'ravi.gajul@test.com','parampassword': 'Ant3m3an!'}
    #responseToken is an object of all the variables defined in createtoken feature
    * def authtoken = responseToken.authToken

  @passingvalues
  Scenario: Calling other feature to get authtoken and use it in this scenario
    Given path '/api/articles/'
    And header Authorization = 'Token ' + authtoken
    And request {"article":{"tagList":[],"title":"test article","description":"this is test article","body":"he there test article"}}
    When method post
    Then status 200
    * print response
    And match response.article.title == 'test article'
@BasicAuth1234
Feature: Feature to demonstrate the Basic Auth
  I want to use this template for my feature file

  Background: 
    * print 'Run this with dev environment as its a different token that is configured in headers'
    * url 'https://postman-echo.com'
    * header Authorization = call read('classpath:com/javascript/utils/basic-auth.js') { username: 'postman', password: 'password' }

  @BasicAuth12345678
  Scenario: Scenario to generate basic auth token
    Given path '/basic-auth'
    When method GET
    Then status 200

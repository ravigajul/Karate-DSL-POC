Feature: Feature to create a token by using config parameters

  Scenario: Create Token by accepting parameters
    Given url 'https://conduit.productionready.io/api'
    And path '/users/login'
    And request {"user":{"email":"#(email)","password":"#(password)"}}
    When method post
    Then status 200
    * def authToken = response.user.token
    * print authToken

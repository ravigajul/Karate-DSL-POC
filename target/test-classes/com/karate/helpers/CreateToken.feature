Feature: Feature to create a token as a helper

  Scenario: Create Token
    Given url 'https://conduit.productionready.io/api'
    And path '/users/login'
    And request {"user":{"email":"ravi.gajul@test.com","password":"Ant3m3an!"}}
    When method post
    Then status 200
    * def authToken = response.user.token
    * print authToken

Feature: Feature to create a token as a helper

@login
  Scenario: Create Token inline
    Given url 'https://conduit.productionready.io'
    And path '/api/users/login'
    And request read('classpath:com/test/resources/Login_Request.json')
    When method post
    Then status 200
    * def authToken = response.user.token
    * print authToken

   
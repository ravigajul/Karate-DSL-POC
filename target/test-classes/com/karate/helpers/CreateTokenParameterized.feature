Feature: Feature to create a token by accepting parameters

@parameterizedtoken
  Scenario: Create Token by accepting parameters
    Given url baseurl
    And path '/api/users/login'
    And request {"user":{"email":#(paramemail),"password":#(parampassword)}}
    When method post
    Then status 200
    * def authToken = response.user.token
    * print authToken
    * print 'Email: '+paramemail+'--'+'Password: '+parampassword

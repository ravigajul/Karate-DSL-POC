Feature: Feature to create a token by using config parameters

  Scenario: Create Token by accepting parameters
    Given url 'https://conduit.productionready.io'
    And path '/api/users/login'
    And request {"user":{"email":#(useremail),"password":#(userpassword)}}
    When method post
    Then status 200
    * def authToken = response.user.token
    * print authToken

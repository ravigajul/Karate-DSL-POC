Feature: Feature to create a token by accepting parameters

  Scenario: Create Token by accepting parameters
    Given url 'https://conduit.productionready.io/api'
    And path '/users/login'
    And request {"user":{"email":"#(useremail)","password":"#(userpassword)"}}
    When method post
    Then status 200
    * def authToken = response.user.token
    * print authToken
    * print 'Email: '+useremail+'--'+'Password: '+userpassword

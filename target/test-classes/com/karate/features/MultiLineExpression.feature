Feature: Feature to create a token as a helper

@multilineexpression @embeddedexpression
#mvn test "-Dkarate.options=--tags @multilineexpression" -Dkarate.env=qa
  Scenario: Create Token
    Given url 'https://conduit.productionready.io/api'
    And path '/users/login'
    And request 
    """
		    {
		    "user": {
		        			"email": #(useremail),
		        			"password": #(userpassword)
		    				}
				}
    """
    When method post
    Then status 200
    * def authToken = response.user.token
    * print authToken

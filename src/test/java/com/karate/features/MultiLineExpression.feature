Feature: Feature to create a token as a helper

@multilineexpression @embeddedexpression
#mvn test "-Dkarate.options=--tags @multilineexpression" -Dkarate.env=qa
  Scenario: Create Token
    Given url baseurl
    And path '/api/users/login'
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

@ignore
Feature: Feature to demonstrate the schema validation

  Background: 
    Given url  baseurl
    And path '/api/users/login'
    And request {"user":{"email":"ravi.gajul@test.com","password":"Ant3m3an!"}}
    When method post
    Then status 200
    * def authtoken = response.user.token
    * print authtoken
    
@schemavalidation
 # mvn test "-Dkarate.options=--tags @schemavalidation"
  Scenario: To demonstrate schema validation by externalization.
  	* def isValidTime = read('classpath:com/javascript/utils/time-validator.js')
    Given path '/api/articles/'
    And header Authorization = 'Token ' + authtoken
    And request {"article":{"tagList":[],"title":"test article","description":"this is test article","body":"he there test article"}}
    When method post
    Then status 200
    * print response
    And match response.article.title == 'test article'
    And match response.article == read('classpath:com/test/resources/Articles_Response_Schema.json')

    
  @schemavalidation
 # mvn test "-Dkarate.options=--tags @schemavalidation"
  Scenario: To demonstrate schema validation without externalization.
  	* def isValidTime = read('classpath:com/javascript/utils/time-validator.js')
    Given path '/api/articles/'
    And header Authorization = 'Token ' + authtoken
    And request {"article":{"tagList":[],"title":"test article","description":"this is test article","body":"he there test article"}}
    When method post
    Then status 200
    * print response
    And match response.article.title == 'test article'
    And match response.article ==  
    """
			  {
					"tagList": "#array",
					"createdAt": "#? isValidTime(_)",
					"author": {
						"image": "#string",
						"following": "#boolean",
						"bio": "##string",
						"username": "#string"
				},
					"description": "#string",
					"title": "#string",
					"body": "#string",
					"favoritesCount": "#number",
					"slug": "#string",
					"updatedAt": "#? isValidTime(_)",
					"favorited": "#boolean"
			}
   """
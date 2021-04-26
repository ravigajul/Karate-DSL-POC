Feature: Feature to demonstrate user Signup
  Background: pre-conditions 
  	* def datagenerator = Java.type('com.karate.helpers.DataGenerator')
    Given url baseurl
     
 @signup
 # mvn test -Dkarate.options=--tags @configparams -Dkarate.env=qa
  Scenario: User Sign up through random user generator
  	* def username = datagenerator.generateRandomUser()
  	* def useremail = datagenerator.generateRandomEmail()
  	* print username
    Given path '/api/users'
    And request read('classpath:com/test/resources/Signup_Request.json')
    When method post
    Then status 200
    * print response
    And match response.user == read('classpath:com/test/resources/Signup_Response_Schema.json')
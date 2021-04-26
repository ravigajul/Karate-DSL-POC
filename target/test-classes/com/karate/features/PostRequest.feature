Feature: Feature to demonstrate Post Request scenarios

  Background: 
    Given url baseurl
    And path '/api/users/login'
    And request {"user":{"email":"ravi.gajul@test.com","password":"Ant3m3an!"}}
    When method post
    Then status 200
    * def authtoken = response.user.token
    * print authtoken
@postrequest
 # mvn test "-Dkarate.options=--tags @postrequest"
  Scenario: Post article with authtoken context passing
    Given path '/api/articles/'
    And header Authorization = 'Token ' + authtoken
    And request {"article":{"tagList":[],"title":"test article","description":"this is test article","body":"he there test article"}}
    When method post
    Then status 200
    * print response
    And match response.article.title == 'test article'

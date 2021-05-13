Feature: Feature to demonstrate Create and Delete Article

  Background: 
    Given url baseurl
   
   @Create & Delete Article
 # mvn test "-Dkarate.options=--tags @postrequest"
  Scenario: Post article with authtoken context passing
    Given path '/api/articles/'
    And request {"article":{"tagList":[],"title":"ArticleDelete","description":"this is test article for deletion","body":"he there test article"}}
    When method post
    Then status 200
    * print response
    And match response.article.title == 'ArticleDelete'
    * def slug = response.article.slug
    Given path 'api','articles',slug
    When method DELETE
    Then status 200
    
    

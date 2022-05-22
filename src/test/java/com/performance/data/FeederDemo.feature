Feature: Feature to demonstrate feeder Functionality

  Background:
    Given url baseurl

  @CreateDeleteArticles
  # mvn clean test-compile gatling:test
  Scenario: Post article with authtoken context passing
    * def articleRequestBody = read('classpath:com/karate/data/newArticleData.json')
    * set articleRequestBody.article.title = __gatling.Title
    * set articleRequestBody.article.description = __gatling.Description
    Given path '/api/articles/'
    And request articleRequestBody
    When method post
    Then status 200
    * print response




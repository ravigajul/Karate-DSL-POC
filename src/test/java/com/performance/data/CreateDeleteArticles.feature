Feature: Feature to demonstrate Create and Delete Article

  Background:
    Given url baseurl

  @CreateDeleteArticles
  # mvn clean test-compile gatling:test
  Scenario: Post article with authtoken context passing
    * def articleRequestBody = read('classpath:com/karate/data/newArticleData.json')
    Given path '/api/articles/'
    And request articleRequestBody
    And header karate-name = 'Create Article'
    When method post
    Then status 200
    * print response
    And match response.article.title == 'ArticleDelete'
# simulating user think time using karate pause
# * karate.pause(5000)
# * def slug = response.article.slug
# Given path 'articles', slug
# When method DELETE
# Then status 204



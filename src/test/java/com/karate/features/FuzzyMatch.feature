Feature: Fuzzy Matching
  Background:
    Given url baseurl

  @fuzzymatch
  Scenario: A simple get call
    Given path '/api/tags'
    When method GET
    Then status 200
    * print response
    And match each response.tags[*] == '#string'
    And match response.tags contains ['welcome']
    Then match response ==
      """
      {
      "tags": '#array'
      }
      """
  @articles
  Scenario: Passing params to get call
    * def isValidTime = read('classpath:com/karate/helpers/time-validator.js')
    Given path '/api/articles'
    And param limit = 10
    And param offset = 0
    When method GET
    Then status 200
    And match response.articles == '#[10]'
    And match response.articlesCount == '#number'
    And match each response.articles[*].createdAt == '#? isValidTime(_)'
    And match response.articles[0].createdAt contains '2023'
    And match response ==
      """
      {
      "articles": '#array',
      "articlesCount":'#number'
      }
      """
    And match each response.articles[*] ==
      """
      {
      "slug": "#string",
      "title": "#string",
      "description": "#string",
      "body": "#string",
      "tagList": '#array',
      "createdAt": "#? isValidTime(_)",
      "updatedAt": "#? isValidTime(_)",
      "favorited": '#boolean',
      "favoritesCount": '#number',
      "author": {
      "username": "#string",
      "bio": '#null',
      "image": "#string",
      "following": '#boolean'
      }
      }
      """
    And match response.articles[0] ==
      """
      {
      "slug": "#string",
      "title": "#string",
      "description": "#string",
      "body": "#string",
      "tagList": '#array',
      "createdAt": "#? isValidTime(_)",
      "updatedAt": "#? isValidTime(_)",
      "favorited": '#boolean',
      "favoritesCount": '#number',
      "author": {
      "username": "#string",
      "bio": '#null',
      "image": "#string",
      "following": '#boolean'
      }
      }
      """
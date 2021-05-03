Feature: Feature to get slug that is used in other feature
  I want to use this template for my feature file
  
  Background: 
  
  * url baseurl

  @addlikes
  Scenario: scenario to generate slug
   Given path 'api','articles', slug, 'favorite'
   And request {}
   When method POST
   Then status 200
   * def likescount = response.article.favoritesCount

  
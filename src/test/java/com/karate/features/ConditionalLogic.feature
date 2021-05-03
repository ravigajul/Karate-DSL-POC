Feature: Feature to demonstrate how if condition can be used
  I want to use this template for my feature file
Background: Pre-condition
	* url baseurl
	
  @conditionallogic
  Scenario: Conditional logic for IF condition
    Given path 'api','articles'
    And params {limit: 10, offset: 0}
    When method GET
    Then status 200
    * def favoritesCount = response.articles[0].favoritesCount
    * def article = response.articles[0]
    # in the below step article is an object that we are using to retrive slug in AddLikes.feature. 
    #It wont work if we directly pass the slug value ins callSingle accepts object as parameter
   # * if (favoritesCount == 0) karate.callSingle('classpath:com/karate/helpers/AddLikes.feature', article)
    * def result = favoritesCount == 0?karate.callSingle('classpath:com/karate/helpers/AddLikes.feature', article).likescount:favoritesCount 
    Given path 'api','articles'
    And params {limit: 10, offset: 0}
    When method GET
    Then status 200
    And match response.articles[0].favoritesCount == result
    
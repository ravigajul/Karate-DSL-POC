Feature: Feature to demonstrate retry Logic
  I want to use this template for my feature file
	Background: background
	* url baseurl
  @retrylogic
  Scenario: Retry logic
  	* configure retry = {count:4, interval: 1000}
    Given path 'api','articles'
    And params {limit: 10, offset: 0}
    And retry until response.articles[0].favoritesCount == 10
    When method GET
    Then status 200

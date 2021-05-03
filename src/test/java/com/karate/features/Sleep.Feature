Feature: Feature to demonstrate Sleep Logic
  I want to use this template for my feature file
	Background: background
	* url baseurl
  @sleep
  Scenario: Sleep Feaure
  	* def sleep = function(pause){java.lang.Thread.sleep(pause)}
    Given path 'api','articles'
    And params {limit: 10, offset: 0}
    * eval sleep(5000)
    When method GET
    Then status 200

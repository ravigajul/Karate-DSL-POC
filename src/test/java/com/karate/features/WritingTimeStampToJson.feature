
Feature: Feature to demonstrate retrieval of current date time stamp and add it to json
  I want to use this template for my feature file

  @timestamp
  Scenario: Retrieve and put current date and time stamp
  	* def requestbody = { "TIME_STAMP": ""}
  	* def currentdatetimestamp = call read('classpath:com/karate/helpers/timestamp-generator.js')
  	* print currentdatetimestamp
  	* requestbody['TIME_STAMP'] = currentdatetimestamp
  	* print requestbody
    

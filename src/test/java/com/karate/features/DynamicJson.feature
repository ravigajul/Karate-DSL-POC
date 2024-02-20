@dynamicjson
Feature: Feature to demonstrate forming dynamic json using variables
  I want to use this template for my feature file

  Background: 
    * def result = 
    """{
        "UserName": "Ravi",
        "Password": "testing123"
      }
    """
# mvn test '-Dkarate.options=--tags @dynamicjson' -Dtest=ParallelTest
  @BasicAuth12345678
  Scenario: Scenario to generate basic auth token
    * print result
    * def data = read('classpath:com/karate/data/dynamicdata.json')
    * print data

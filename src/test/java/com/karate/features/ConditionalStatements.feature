@conditional
#mvn test '-Dkarate.options=--tags @conditional' -Dtest=ParallelTest
Feature: Feature to demonstrate how multi line if else condition can be used
  I want to use this template for my feature file
Background: Pre-condition
	* def a = 10 
	
  @conditionallogic
  Scenario: Conditional logic for multi line IF condition
    * eval
    """
    if(a == 10) {
        karate.log('Value matches')
    } else {
        karate.log('Value doesnt match')
    }
    """
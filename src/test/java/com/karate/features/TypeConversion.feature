@typeconversion
Feature: Feature to demonstrate type conversions
  I want to use this template for my feature file

  Scenario: scenario to convert number to string
    * def foo = 10
    * def json = {"bar": "#(foo+'')"}
    * match json == {"bar":'10'}

  Scenario: scenario to convert string to number
    * def boo = '10'
    * def json = {"bar": "#(boo*1)"}
    * match json == {"bar":10}
    
  Scenario: scenario to use java script function to convert string to double
    * def boo = '10'
    * def json = {"bar": "#(parseInt(boo))"}
    * match json == {"bar":10}
    
 Scenario: scenario to use java script function to convert string to number
    * def boo = '10'
    * def json = {"bar": "#(~~parseInt(boo))"}
    * match json == {"bar":10}

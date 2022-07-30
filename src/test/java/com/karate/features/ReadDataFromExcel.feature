@CSVDemo
Feature: Feature to demonstrate how to read data from excel from examples section
  
  Background : test background
  * configure afterScenario = function(){karate.call('classpath:com/karate/features/DummyAfter.feature')}

  # mvn test "-Dkarate.options=--tags @CSVDemo" -Dtest=ParallelTest
  @CSVDemo
  Scenario Outline: Scenario to demonstrate reading data from csv
  * print <FirstName>
  * print <LastName>
  * print <Email>
  
  @CSVDemo
  Examples:
  #| FirstName | LastName | Email                 |
 # | 'Ravi'    | 'Gajul'  | 'Ravi.Gajul@test.com' |
  |read('classpath:com/karate/data/testdata.csv')|
 

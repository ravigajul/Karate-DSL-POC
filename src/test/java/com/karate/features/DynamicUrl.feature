@dynamicurl
# mvn clean test '-Dkarate.options=--tags @dynamicurl' -Dtest=ParallelTest '-Dkarate.env=dev'
Feature:  Form Dynamic url combining values read from json file plus value passed in maven goal

  Background:
    * def vConfig = read('classpath:com/karate/data/env.json')
    * def vUrl = vConfig[karate.properties['karate.env']].baseurl + vConfig[karate.properties['karate.env']].resource
    * print vUrl
  @BasicAuth12345678
  Scenario: Scenario to generate basic auth token
    Given url vUrl
    When method GET
    Then status 403
    * print  response
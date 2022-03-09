Feature: This is a dummy feature

Background: background
* configure afterScenario = function(){karate.call('classpath:com/karate/features/DummyAfter.feature')}
@DummyBefore
Scenario: DummyBefore
* print 'This is a before dummy feature'
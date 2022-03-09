Feature: Feature to demonstrate before and after hooks

  #Before Scenario to execute after every scenario
  Background: Background will serve as before scenario hook
	* def dummy = call read('classpath:com/karate/features/DummyBefore.feature')
	
	#Before feature like once before first scenario of a feature
	* def dummy = callonce read('classpath:com/karate/features/DummyBefore.feature')
	
	#After Hook this run after every feature
	* configure afterFeature = function(){karate.call('classpath:com/karate/features/DummyAfter.feature')}
	
	#After Hook this run after every scenario
	* configure afterScenario = function(){karate.call('classpath:com/karate/features/DummyAfter.feature')}
  
  #After hook to use java script function
  
 	* configure afterFeature = 
 	"""
 	function()
 	{
 		karate.log('This is coming from java script function after feature')
 	}
 	"""
  @Hooks
  Scenario: hooks
    * print 'This is scenario 1'

  Scenario: hooks scenario 2
    * print 'This is scenario 2'

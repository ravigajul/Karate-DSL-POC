Feature: This is a dummy feature

  @DummyBefore
  Scenario: DummyAfter
    * print 'This is a after dummy feature'
    * print java.lang.System.getProperty('user.dir')
    * def filePath = "file:"+ java.lang.System.getProperty('user.dir')+"\\target\\karate-reports\\"+"com.karate.features."+karate.scenario.name+".json"
    * def cucumberjson = read(filePath)
    * print cucumberjson

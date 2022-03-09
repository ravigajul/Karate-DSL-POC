Feature: This is a dummy feature

Scenario: DummyBefore
* print 'This is a after dummy feature'
* def filePath = "file:"+java.lang.System.getProperty('user.dir')+"\\target\\karate-reports\\"+"com.karate.features.DummyBefore.json"
* def cucumberjson = read(filePath)
* print cucumberjson
#karate.properties['user.dir']+"\\target\\karate-reports\\com\\karate\\features\\"+

#C:\Users\anjal\eclipse-workspace\Karate-DSL-POC\target\karate-reports
Feature:feature to demo java calling

@callingjava
# mvn clean test '-Dkarate.options=--tags @callingjava' -Dtest=ParallelTest -Pcoverage
Scenario: scenario to demo calling java
* def randomEmail = Java.type('com.karate.helpers.DataGenerator').getRandomEmail()
* print randomEmail
* def randomUserName = Java.type('com.karate.helpers.DataGenerator').getRandomUserName()
* print randomUserName
* def randomArticleValues = Java.type('com.karate.helpers.DataGenerator').getRandomArticleValues()
* print randomArticleValues


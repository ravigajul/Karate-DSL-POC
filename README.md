# Karate-DSL-POC

This is a karate poc for API testing

## 1. For setting path and param parameters

    1  Scenario: Passing param to get call
    2  Given path 'articles'
    3  And param limit = 10   # note exactly one space surrounding '=' sign

## 2. To pass multiple params in json format

    1  And params { limit: 10, offset: 0 }

## 3. Assertions

 And match response.tags contains ['Gandhi', 'dragons']
 And match response.tags !contains 'cars'
 And match response.tags == '#array'

## 4. Run a specific testmethod in the runner file through maven

    1  mvn clean test -Dtest=UsersTest#testAssertions
    2   //where  UserTest is the UsersTest.java runner and testAssertions is the test method in the usersTest.java file

## 5. Run methods with specific tags

 mvn clean test "-Dkarate.options=--tags @smoke"

## 6. Run method with specific tags and passing environment value

 mvn test "-Dkarate.options=--tags @configparams" -Dkarate.env=qa

## 7. Calling other feature file

 def responseToken = call read('classpath:com/karate/helpers/CreateToken.feature')
 #responseToken is an object of all the variables defined in createtoken feature
 Hence to retrieve it is responseToken.<<variablename>> defined in CreateToken
 Callonce to call it only once

## 8. Calling other features with parameters

 def responseToken = callonce read('classpath:com/karate/helpers/CreateToken.feature') {'email': 'ravi.gajul@test.com','password': 'Ant3m3an!'}

## 9. Calling feature in config file

 //passing feature file and config object to callSingle method which returns an object of variables declared in feature file.
     var accessToken=karate.callSingle('classpath:com/karate/helpers/CreateToken.feature',config).authToken
     //passing global headers that can be used by all urls
             karate.configure('headers',{Authorization: 'Token ' + accessToken})  

## 10. MultiLine Expressions

  Can be used between thriple double quotes like below
 """
 {
     "user": {
         "email": "ravi.gajul@test.com",
         "password": "Ant3m3an!"
     }
 }
 """"

## 11. Getting data from Java Class file

* def datagenerator = Java.type('classpath:com/karate/helpers/DataGenerator')
 This will throw
 org.graalvm.polyglot.PolyglotException: TypeError: Access to host class classpath:com/karate/helpers/DataGenerator is not allowed or does not exist.

* <js>.:program(Unnamed:1)
 Remove the classpath and it will work

* def datagenerator = Java.type('com/karate/helpers/DataGenerator')

## 12 . Before Scenario

 Background : Background keyword works for before each scenario
 Using callonce to execute only once

## 13. After Scenario

 • Configure afterScenario = call read('classpath:com/test/resources/test.feature')
 • Configure after

## 14. When parallel test builds successfully but doesn't run anything

 a. <https://github.com/intuit/karate/issues/823>
 <!-- <configuration>
  <argLine>-Dfile.encoding=UTF-8</argLine>
  <includes>
   <include>com/karate/features/RunnerTest.java</include>
  </includes>
  <systemProperties>
   <karate.options>--tags ~@ignore</karate.options>
  </systemProperties>
 </configuration> -->

## 15. Disable SSL verification

 Karate.configure('ssl', true) for SSL disabling

## 16. Configure keystore for SSL verification

* configure ssl = { trustAll: true, keyStore: '#(keyStoreLocation)', keyStorePassword: 'somePass' }

## 17. conditional logic in karate

    #in the below step article is an object that we are using to retrive slug in AddLikes.feature. 
    #It wont work if we directly pass the slug value ins callSingle accepts object as parameter
    * def article = response.articles[0]
    * if (favouritesCount == 0) karate.callSingle('classpath:com/karate/helpers/AddLikes.feature', article)
    Another Way
    * def result = favoritesCount == 0 ? karate.callSingle('classpath:com/karate/helpers/AddLikes.feature',   rticle).likescount:favoritesCount 
  
## 19. Retry Logic

* configure retry = {count:5, interval: 10000} #the below line should be before method call.
  And retry until response.articles[0].favoritesCount == 5

## 20. Sleep

* def sleep = function(pause){java.lang.Thread.sleep(pause)}
* eval sleep(5000)

## 21. Type Conversion

* Foo+'' will convert integer(foo) to String

* b.Foo*1 will convert String(foo) to Integer
* c.def json = {"bar": "#(parseInt(boo))"} will parse boo into integer using java script function.
* vd.def json = {"bar": "#(~~parseInt(boo))"} will parse boo into int using java script function.

## 22. Docker

## a. Build image from Dockerfile

* Docker build -t "name of the container--karatetest" .

## b. Run the created container karatetest

* Docker run -it karatetest

## Run Scala Test

mvn clean test-compile gatling:test

## simulating user think time using karate pause

* karate.pause(5000)

## Karate user simulation through injection

<https://gatling.io/docs/gatling/reference/current/core/injection/>

```
setUp(
  scn.inject(
    nothingFor(4), // 1
    atOnceUsers(10), // 2
    rampUsers(10).during(5), // 3
    constantUsersPerSec(20).during(15), // 4
    constantUsersPerSec(20).during(15).randomized, // 5
    rampUsersPerSec(10).to(20).during(10.minutes), // 6
    rampUsersPerSec(10).to(20).during(10.minutes).randomized, // 7
    stressPeakUsers(1000).during(20) // 8
  ).protocols(httpProtocol)
)
```

## Feeder

<https://gatling.io/docs/gatling/reference/current/core/session/feeder/>

```
val csvfeeder =  csv('csvFeeder.csv')
val create = scenario("create and delete article").feed(csvFeeder).exec(karateFeature("classpath:com/performance/data/FeederDemo.feature"))
```

## Strategies

```javascript
// default behavior: use an Iterator on the underlying sequence
csv("foo").queue()
// randomly pick an entry in the sequence
csv("foo").random()
// shuffle entries, then behave like queue
csv("foo").shuffle()
// go back to the top of the sequence once the end is reached
csv("foo").circular()
```

## Gain access to gatling session

```java
__gatling.Title //title is the header in csv file
__gatling.Description //Description is the header in csv file.
```

## Name Resolver

 This is to display a user friendly name in the report instead of end point
 Add the below step in performance.scala file

 ```
  protocol.nameResolver = (req, ctx) => req.getHeader("karate-name")
  ```

And access it before sending the request in feature file

```
And header karate-name = 'Create Article'
```

## Dispatcher Configuration

Is to increase the pool size to some number so that the expected load is hit and no unexpected results are seen.
Create a gatling-akka.config file at same level as karate-config and add below lines for achieving a pool of size 100. for more info see below
<https://github.com/karatelabs/karate/tree/master/karate-gatling#increasing-thread-pool-size>

```
akka {
  actor {
    default-dispatcher {
      type = Dispatcher
      executor = "thread-pool-executor"
      thread-pool-executor {
        fixed-pool-size = 100
      }
      throughput = 1
    }
  }
}
```

## File Seperator

file.separator" -> Character that separates components of a file path. This is "/" on UNIX and "\\" on Windows.

```java
System.getProperty("user.dir")+File.separator+"Other"
```

<https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html>

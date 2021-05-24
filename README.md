# Karate-DSL-POC
This is a karate poc for API testing
# 1. For setting path and param parameters
    1 	Scenario: Passing param to get call
    2 	Given path 'articles'
    3 	And param limit = 10   # note exactly one space surrounding '=' sign
# 2. To pass multiple params in json format.
    1 	And params { limit: 10, offset: 0 }
# 3. Assertions
	And match response.tags contains ['Gandhi', 'dragons']
	And match response.tags !contains 'cars'
	And match response.tags == '#array'	
# 4. Run a specific testmethod in the runner file through maven
    1 	mvn clean test -Dtest=UsersTest#testAssertions
    2 	 //where  UserTest is the UsersTest.java runner and testAssertions is the test method in the usersTest.java file
	
# 5. Run methods with specific tags
	mvn clean test "-Dkarate.options=--tags @smoke"

# 6. Run method with specific tags and passing environment value
	mvn test "-Dkarate.options=--tags @configparams" -Dkarate.env=qa
	
# 7. Calling other feature file
	def responseToken = call read('classpath:com/karate/helpers/CreateToken.feature')
	#responseToken is an object of all the variables defined in createtoken feature
	Hence to retrieve it is responseToken.<<variablename>> defined in CreateToken
	Callonce to call it only once
	
# 8. Calling other features with parameters
	def responseToken = callonce read('classpath:com/karate/helpers/CreateToken.feature') {'email': 'ravi.gajul@test.com','password': 'Ant3m3an!'}
	
# 9. Calling feature in config file
	//passing feature file and config object to callSingle method which returns an object of variables declared in feature file.        
	    var accessToken=karate.callSingle('classpath:com/karate/helpers/CreateToken.feature',config).authToken	
	    //passing global headers that can be used by all urls
	            karate.configure('headers',{Authorization: 'Token ' + accessToken})  
# 10. MultiLine Expressions 
	 Can be used between thriple double quotes like below 
	"""
	{
	    "user": {
	        "email": "ravi.gajul@test.com",
	        "password": "Ant3m3an!"
	    }
	}
	""""
# 11. Getting data from Java Class file
	* def datagenerator = Java.type('classpath:com/karate/helpers/DataGenerator')
	This will throw 
	org.graalvm.polyglot.PolyglotException: TypeError: Access to host class classpath:com/karate/helpers/DataGenerator is not allowed or does not exist.
	- <js>.:program(Unnamed:1)
	Remove the classpath and it will work
	* def datagenerator = Java.type('com/karate/helpers/DataGenerator')
# 12 . Before Scenario
	Background : Background keyword works for before each scenario
	Using callonce to execute only once
# 13. After Scenario
	• Configure afterScenario = call read('classpath:com/test/resources/test.feature')
	• Configure after
# 14. When parallel test builds successfully but doesn't run anything
	a. https://github.com/intuit/karate/issues/823
	<!-- <configuration>
		<argLine>-Dfile.encoding=UTF-8</argLine>
		<includes>
			<include>com/karate/features/RunnerTest.java</include>
		</includes>
		<systemProperties>
			<karate.options>--tags ~@ignore</karate.options>
		</systemProperties>
	</configuration> -->
	
# 15. Disable SSL verification
	Karate.configure('ssl', true) for SSL disabling

# 16. Configure keystore for SSL verification	
	* configure ssl = { trustAll: true, keyStore: '#(keyStoreLocation)', keyStorePassword: 'somePass' }
	
# 17. conditional logic in karate
    #in the below step article is an object that we are using to retrive slug in AddLikes.feature. 
    #It wont work if we directly pass the slug value ins callSingle accepts object as parameter
    * def article = response.articles[0]
    * if (favouritesCount == 0) karate.callSingle('classpath:com/karate/helpers/AddLikes.feature', article)
    Another Way
    * def result = favoritesCount == 0 ? karate.callSingle('classpath:com/karate/helpers/AddLikes.feature',   rticle).likescount:favoritesCount 
  
# 19. Retry Logic
 	* configure retry = {count:5, interval: 10000} #the below line should be before method call.
 	And retry until response.articles[0].favoritesCount == 5
# 20. Sleep
 	* def sleep = function(pause){java.lang.Thread.sleep(pause)}
	* eval sleep(5000)
# 21. Type Conversion
	* Foo+'' will convert integer(foo) to String
	* b.Foo*1 will convert String(foo) to Integer
	* c.def json = {"bar": "#(parseInt(boo))"} will parse boo into integer using java script function.
	* vd.def json = {"bar": "#(~~parseInt(boo))"} will parse boo into int using java script function.
# 22. Docker
## a. Build image from Dockerfile
* i. Docker build -t <<name of the container--karatetest>> .
## b. Run the created container karatetest
* Docker run -it karatetest

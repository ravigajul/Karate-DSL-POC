# Karate-DSL-POC

This is a karate poc for API testing

## Maven Archetype

```maven
mvn archetype:generate ^
-DarchetypeGroupId=com.intuit.karate ^
-DarchetypeArtifactId=karate-archetype ^
-DarchetypeVersion=1.2.0 ^
-DgroupId=com.mycompany ^
-DartifactId=myproject
```

## 1. For setting path and param parameters

```cucumber
  1  Scenario: Passing param to get call
  2  Given path 'articles'
  3  And param limit = 10   # note exactly one space surrounding '=' sign
```

## 2. To pass multiple params in json format

```karate
    1  And params { limit: 10, offset: 0 }
```

## 3. Assertions

And match response.tags contains ['Gandhi', 'dragons']
And match response.tags !contains 'cars'
And match response.tags == '#array'

## 4. Run a specific test method in the runner file through Maven

```maven
mvn clean test -Dtest=UsersTest#testAssertions
//where  UserTest is the UsersTest.java runner and test assertions is the test method in the usersTest.java file
mvn test '-Dkarate.options=--tags @test' -Dkarate.config.dir='src/test/java' -Dtest='DemoParallelTest' -Dkarate.env='dev'
```

## 5. Run methods with specific tags

mvn clean test "-Dkarate.options=--tags @smoke"

## 6. Run method with specific tags and passing environment value

mvn test "-Dkarate.options=--tags @configparams" -Dkarate.env=qa

## 7. Calling another feature file

def responsetoken = call read('classpath:com/karate/helpers/CreateToken.feature')
responseToken is an object of all the variables defined in createtoken feature

Hence to retrieve it is responseToken.variablename defined in CreateToken
Callonce to call it only once

## 8. Calling other features with parameters

def responseToken = callonce read('classpath:com/karate/helpers/CreateToken.feature') {'email': '<ravi.gajul@test.com>','password': 'Ant3m3an!'}

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
        "email": "<ravi.gajul@test.com>",
        "password": "Ant3m3an!"
    }
}
""""

## 11. Getting data from Java Class file

```javascript
- def datagenerator = Java.type('classpath:com/karate/helpers/DataGenerator')
  This will throw
  org.graalvm.polyglot.PolyglotException: TypeError: Access to host class classpath:com/karate/helpers/DataGenerator is not allowed or does not exist.

- <js>.:program(Unnamed:1)
  Remove the classpath and it will work

- def datagenerator = Java.type('com/karate/helpers/DataGenerator')
```

## 12 . Before Scenario

Background : Background keyword works for before each scenario
Using callonce to execute only once

## 13. After Scenario

• Configure afterScenario = call read('classpath:com/test/resources/test.feature')
• Configure after

## 14. When parallel test builds successfully but doesn't run anything

a. <https://github.com/intuit/karate/issues/823>

```xml
  <configuration>
  <argLine>-Dfile.encoding=UTF-8</argLine>
  <includes>
   <include>com/karate/features/RunnerTest.java</include>
  </includes>
  <systemProperties>
   <karate.options>--tags ~@ignore</karate.options>
  </systemProperties>
 </configuration>
```

## 15. Disable SSL verification

Karate.configure('ssl', true) for SSL disabling

## 16. Configure keystore for SSL verification

- configure ssl = { trustAll: true, keyStore: '#(keyStoreLocation)', keyStorePassword: 'somePass' }

## 17. conditional logic in karate

```javascript
  #in the below step article is an object that we are using to retrive slug in AddLikes.feature.
  #It wont work if we directly pass the slug value ins callSingle accepts object as parameter
  * def article = response.articles[0]
  * if (favouritesCount == 0) karate.callSingle('classpath:com/karate/helpers/AddLikes.feature', article)
  Another Way
  * def result = favoritesCount == 0 ? karate.callSingle('classpath:com/karate/helpers/AddLikes.feature',   rticle).likescount:favoritesCount
  # Conditional Match
 * if ('<somecolumn>' !== '' && (x !== y)) karate.fail('<Error Message>')
```
## Conditional Match or Conditional Assertion
```javascript
    * def allDeclineReasons = $.decisionResponse.allDeclineReasons
    * if (allDeclineReasons) karate.match(allDeclineReasons[0].aaReasonCode, '<aaReasonCode>')
```
## Conditional feature call passing an argument
When you use karate.call() put the second argument inside the round brackets. This is pure JS and "Karate-style" embedded expressions will not work.
https://github.com/karatelabs/karate#call-vs-read
```javascript
* if (role=="SME"||role=="BA") karate.call('classpath:rough/utility.feature@checkDisabled', {element: elem})
```
### Manipulate request body for a given condition
```
* eval
    """
    if ('<ColumFromExcel>' == 'Y')
    requestBody.<Path> = 'Test'
    """
```
### Set request body to have an array
```
requestBody.<jsonPath> = [<array>] //array in csv = "'6001','6002'"
```
## 19. Retry Logic

- configure retry = {count:5, interval: 10000} #the below line should be before method call.
  And retry until response.articles[0].favoritesCount == 5

## 20. Sleep

- def sleep = function(pause){java.lang.Thread.sleep(pause)}
- eval sleep(5000)
- * eval karate.pause(5000)

## 21. Type Conversion

- Foo+'' will convert integer(foo) to String

- b.Foo\*1 will convert String(foo) to Integer
- c.def json = {"bar": "#(parseInt(boo))"} will parse boo into integer using java script function.
- vd.def json = {"bar": "#(~~parseInt(boo))"} will parse boo into int using java script function.

## 22. Docker

## a. Build image from Dockerfile

- Docker build -t "name of the container--karatetest" .

## b. Run the created container karatetest

- Docker run -it karatetest

## Run Scala Test

```maven
mvn clean test-compile gatling:test
```

## simulating user think time using karate pause

- karate.pause(5000)

## Karate user simulation through injection

<https://gatling.io/docs/gatling/reference/current/core/injection/>

```gatlin
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

```javscript
val csvfeeder =  csv('csvFeeder.csv')
val create = scenario("create and delete article").feed(csvFeeder).exec(karateFeature("classpath:com/performance/data/FeederDemo.feature"))
```

## Strategies

```javascript
// default behavior: use an Iterator on the underlying sequence
csv("foo").queue();
// randomly pick an entry in the sequence
csv("foo").random();
// shuffle entries, then behave like queue
csv("foo").shuffle();
// go back to the top of the sequence once the end is reached
csv("foo").circular();
```

## Gain access to gatling session

```java
__gatling.Title //title is the header in csv file
__gatling.Description //Description is the header in csv file.
```

## Name Resolver

This is to display a user friendly name in the report instead of end point
Add the below step in performance.scala file

```gatlin
 protocol.nameResolver = (req, ctx) => req.getHeader("karate-name")
```

And access it before sending the request in feature file

```gatlin
And header karate-name = 'Create Article'
```

## Dispatcher Configuration

Is to increase the pool size to some number so that the expected load is hit and no unexpected results are seen.
Create a gatling-akka.config file at same level as karate-config and add below lines for achieving a pool of size 100. for more info see below
<https://github.com/karatelabs/karate/tree/master/karate-gatling#increasing-thread-pool-size>

```json
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

## Fuzzy Matching

<https://github.com/karatelabs/karate#fuzzy-matching>

## Schema Validation

<https://github.com/karatelabs/karate#schema-validation>

## 'Self' Validation Expressions

<https://github.com/karatelabs/karate#self-validation-expressions>

```javascript
And Match response.[0].empid == '#? _ ==1'
# here _ is called self variabe
```

## Merging external objects into karate config

```javascript
function() {
  var env = karate.env; // get java system property 'karate.env'
  karate.log('karate.env system property was:', env);
  if (!env) {
    env = 'dev'; // a custom 'intelligent' default
  }
  var config = { // base config JSON
    appId: 'my.app.id',
    appSecret: 'my.secret',
    someUrlBase: 'https://some-host.com/v1/auth/',
    anotherUrlBase: 'https://another-host.com/v1/'
  };
  if (env == 'stage') {
    // over-ride only those that need to be
    config.someUrlBase = 'https://stage-host/v1/auth';
  } else if (env == 'e2e') {
    config.someUrlBase = 'https://e2e-host/v1/auth';
  }
  //read from external json file
  var externalConfig = read('classpath:config/config.json');
  //merge external config with base config
  config = karate.merge(config, externalConfig);
  return config;
}
```

## To run the karate through maven goal passing env and tag

```javascript
mvn test "-Dkarate.env=prod" "-Dkarate.options=--tags @readexternalconfig"
```

## Reading data from CSV

```javascript
@CSVDemo
  Scenario Outline: Scenario to demonstrate reading data from csv
  * print <FirstName>
  * print <LastName>
  * print <Email>
  * print __row
  //This line prints each row of csv as json object
  @CSVDemo
  Examples:
  #| FirstName | LastName | Email                 |
 # | 'Ravi'    | 'Gajul'  | 'Ravi.Gajul@test.com' |
  |read('classpath:com/karate/data/testdata.csv')|
```

## Print Rows from csv

```javascript
* print __row
```

## Conditional Execution

```javascript
@CSVDemo22
  Scenario Outline: Scenario to demonstrate reading data from csv
    * print <FirstName>
    * print <LastName>
    * print <Email>
    * print __row
    * def filename = <Execute> == 'Y' ? 'ForCSVRead.feature' : 'Dummy.feature'
   * def result = call read(filename){"endPointUrl" : <endPointUrl>}
    Examples:
      | read('classpath:com/karate/data/testdata.csv') |
```
## Using Json-Path filters for fetching rows based on a filter
```javascript
Background:
        * def data = read('classpath:com/karate/data/testdata.csv')
        # * def filtered = get data[?(@.Execute=="'Y'")]
        * def selected = "'Y'"
        * def fun = function(x){ return x.Execute == selected }
        * def filtered = karate.filter(data, fun)       

    @csvfilter
    Scenario Outline: Scenario name
        Given print __row
        Examples:
            | filtered |
```
## Passing the user defined variable through command line.
Here -DExecute is passed as environment variable which is retrieved by karate.properties['Execute']
during runtime.
```javascript
# mvn test '-Dkarate.options=--tags @csvfilter' -Dtest=ParallelTest -DExecute="'Y'"
    Background:
        * def data = read('classpath:com/karate/data/testdata.csv')
        # * def filtered = get data[?(@.Execute=="'Y'")]
        # * def filtered = "'Y'"
        * def selected = karate.properties['Execute']
        * def fun = function(x){ return x.Execute == selected }
        * def filtered = karate.filter(data, fun)       

    @csvfilter
    Scenario Outline: Scenario name
        Given print __row
        Examples:
            | filtered |
```

## For dynamic url by reading json and env variable from maven goal
```javasript
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
```

## Binding runtime data to external json file
```cucumber
Background: 
    * def result = 
    """{
        "UserName": "Ravi",
        "Password": "testing123"
      }
    """
# mvn test '-Dkarate.options=--tags @dynamicjson' -Dtest=ParallelTest
  @BasicAuth12345678
  Scenario: Scenario to generate basic auth token
    * print result
    * def data = read('classpath:com/karate/data/dynamicdata.json')
    * print data
```
The external dynamicdata.json is here which will be binding with the result data in background. This binding will work even when the json file is externally stored not just within feature file
```json
{
    "id": #(result.UserName),
    "pwd": #(result.Password)
}
```

## Multiline if condition 
```javascript
 * eval
    """
    if(a == 10) {
        karate.log('Value matches')
    } else {
        karate.log('Value doesnt match')
    }
    """
  ```

  ## Json Path Filter experessions
  ```javascript
  * def cat = 
  """
  {
    name: 'Billie',
    kittens: [
      { id: 23, name: 'Bob' },
      { id: 42, name: 'Wild' }
    ]
  }
  """
# find single kitten where id == 23
* def bob = get[0] cat.kittens[?(@.id==23)]
* match bob.name == 'Bob'

# using the karate object if the expression is dynamic
* def temp = karate.jsonPath(cat, "$.kittens[?(@.name=='" + bob.name + "')]")
* match temp[0] == bob

# or alternatively
* def temp = karate.jsonPath(cat, "$.kittens[?(@.name=='" + bob.name + "')]")[0]
* match temp == bob
```

## When working with SOAP Calls

Use below 
```cucumber
WHEN SOAP Action //instead of
WHEN METHOD POST
```

## Filter data using jsonpath Path expression & karate.jsonPath
```gherkin
#mvn test '-Dkarate.options=--tags @rowswithnonemptyendpointurlcolumn' -Dtest=ParallelTest
    @rowswithnonemptyendpointurlcolumn
    Scenario: Scenario to fetch the rows with non empty endpoint values
        * def testData =
            """
            [
            {
            "ID": "1",
            "Execute": "Y",
            "FirstName": "Ravi",
            "LastName": "Gajul",
            "Email": "Ravi.Gajul@test.com",
            "endPointUrl": "/api/users?page=2"
            },
            {
            "ID": "2",
            "Execute": "N",
            "FirstName": "Rajesh",
            "LastName": "Sandupatla",
            "Email": "Rajesh.Sand@test.com",
            "endPointUrl": "/api/users/2"
            },
            {
            "ID": "3",
            "Execute": "Y",
            "FirstName": "Ravi",
            "LastName": "Gajul",
            "Email": "Ravi.Gajul@test.com",
            "endPointUrl": ""
            }
            ]
            """
        * def filteredData = karate.jsonPath(testData, "$.[?(@.Execute == 'Y' && @.endPointUrl != '')]")
        * print filteredData
```
## Remove json key
This will work: remove is a Karate keyword so it won't work when mixed with JS.
```javascript
* def json = { a: 1, b: 2 }
* def key = 'b'
* if (true) karate.remove('json', key)
* match json == { a: 1 }
```
But the JS engine in 1.0 onwards will support the JS delete keyword. So you can do things like

```javascript
*  if('<Scenario>' == 'Missing Applicant Info')  delete requestBody['applicantInfo'];
```
## UI Automation with Karate

```javascript
Scenario:
    Given driver 'https://formy-project.herokuapp.com/form'
    And input('#first-name', 'Ravi') 
    And input('#last-name', 'Gajul') 
    And input('#job-title', 'QA')
    And click('#radio-button-1')
    And click('#checkbox-1')
    And select('#select-menu', '2')
    And input('#datepicker', '01/01/2020')
    And input('#datepicker', Key.ENTER)
    And click('a.btn.btn-lg.btn-primary')
    Then waitForUrl('formy-project.herokuapp.com/thanks')
    Then match text('div.alert.alert-success').trim() == 'The form was successfully submitted!'
  ```
## Compare two json files and return the differences
```javascript
* def compareJSONs =
      """
      function compareJSONs(json1, json2, ignoreFields) {
    function removeIgnoredFields(obj, ignoreFields) {
    ignoreFields.forEach(field => {
    const pathParts = field.replace(/\$\./, '').replace(/\[(\d+)\]/g, '.$1').split('.');
    let current = obj;
    for (let i = 0; i < pathParts.length - 1; i++) {
    if (current[pathParts[i]] !== undefined) {
    current = current[pathParts[i]];
    } else {
    return;
    }
    }
    delete current[pathParts[pathParts.length - 1]];
    });
    }

    function findDifferences(obj1, obj2, path = '') {
    let differences = [];

    if (typeof obj1 === 'object' && typeof obj2 === 'object' && obj1 !== null && obj2 !== null) {
    const keys = new Set([...Object.keys(obj1), ...Object.keys(obj2)]);
    keys.forEach(key => {
    const newPath = path ? `${path}.${key}` : key;
    differences = differences.concat(findDifferences(obj1[key], obj2[key], newPath));
    });
    } else if (obj1 !== obj2) {
    differences.push(path);
    }

    return differences;
    }

    // Clone the JSONs to avoid modifying the original objects
    const json1Clone = JSON.parse(JSON.stringify(json1));
    const json2Clone = JSON.parse(JSON.stringify(json2));

    // Remove ignored fields
    removeIgnoredFields(json1Clone, ignoreFields);
    removeIgnoredFields(json2Clone, ignoreFields);

    // Find differences
    const differences = findDifferences(json1Clone, json2Clone);

    return {
    result: differences.length === 0,
    unmatchedAttributes: differences,
    modifiedJson1: json1Clone,
    modifiedJson2: json2Clone
    };
    }
    """
```

## Compare jsons and return the differences alternate function
```javascript
function compareJSON(obj1, obj2) {
    const differences = {};

    function findDifferences(obj1, obj2, path = '') {
        for (const key in obj1) {
            if (obj1.hasOwnProperty(key)) {
                const newPath = path ? `${path}.${key}` : key;

                if (!obj2.hasOwnProperty(key)) {
                    differences[newPath] = { type: 'removed', value: obj1[key] };
                } else if (typeof obj1[key] === 'object' && typeof obj2[key] === 'object') {
                    findDifferences(obj1[key], obj2[key], newPath);
                } else if (obj1[key] !== obj2[key]) {
                    differences[newPath] = { type: 'changed', oldValue: obj1[key], newValue: obj2[key] };
                }
            }
        }

        for (const key in obj2) {
            if (obj2.hasOwnProperty(key) && !obj1.hasOwnProperty(key)) {
                const newPath = path ? `${path}.${key}` : key;
                differences[newPath] = { type: 'added', value: obj2[key] };
            }
        }
    }

    findDifferences(obj1, obj2);
    return differences;
}

// Example usage:
const json1 = {
    name: "John",
    age: 30,
    address: {
        city: "New York",
        zip: "10001"
    }
};

const json2 = {
    name: "John",
    age: 31,
    address: {
        city: "Los Angeles",
        zip: "90001"
    },
    email: "john@example.com"
};

console.log(compareJSON(json1, json2));
```

## Convert xml to json and compare 

```java
import org.json.JSONObject;
import org.json.XML;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class ResponseValidator {

    public static JSONObject validateResponse(String rawResponse, String sampleResponseStr) {
        JSONObject mismatches = new JSONObject();
        try {
            // Parse the XML string to a Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(rawResponse.getBytes(StandardCharsets.UTF_8));
            Document xmlDocument = builder.parse(input);

            // Convert XML Document to JSON
            JSONObject xmlJson = XML.toJSONObject(rawResponse);

            // Debugging: Print the XML JSON structure
            System.out.println("XML JSON: " + xmlJson.toString(2));

            // Convert sampleResponseStr to JSONObject
            JSONObject Response = new JSONObject(sampleResponseStr);

            // Validate each key-value pair in Response
            validateJson(xmlJson, Response, mismatches);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mismatches;
    }

    private static void validateJson(JSONObject xmlJson, JSONObject Response, JSONObject mismatches) {
        Iterator<String> keys = Response.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object equifaxValue = Response.get(key);

            if (!xmlJson.has(key)) {
                mismatches.put(key, "Key not found in XML");
            } else {
                Object xmlValue = xmlJson.get(key);
                if (!equifaxValue.equals(xmlValue)) {
                    JSONObject mismatchDetail = new JSONObject();
                    mismatchDetail.put("expected", xmlValue);
                    mismatchDetail.put("actual", equifaxValue);
                    mismatches.put(key, mismatchDetail);
                }
            }
        }
    }
}
```
## Response Validator
```java
package com.test.karate.test.automation.features;

import org.json.JSONObject;
import org.json.XML;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class ResponseValidator {

    public static JSONObject validateResponse(String rawResponse, String ResponseStr) {
        JSONObject mismatches = new JSONObject();
        try {
            // Parse the XML string to a Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(rawResponse.getBytes(StandardCharsets.UTF_8));
            Document xmlDocument = builder.parse(input);

            // Convert XML Document to JSON
            JSONObject xmlJson = XML.toJSONObject(rawResponse);

            // Debugging: Print the XML JSON structure
            System.out.println("XML JSON: " + xmlJson.toString(2));

            // Convert ResponseStr to JSONObject
            JSONObject Response = new JSONObject(ResponseStr);

            // Validate each key-value pair in Response
            validateJson(xmlJson, Response, mismatches);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mismatches;
    }

    private static void validateJson(JSONObject xmlJson, JSONObject Response, JSONObject mismatches) {
        Iterator<String> keys = Response.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object equifaxValue = Response.get(key);

            if (!xmlJson.has(key)) {
                mismatches.put(key, "Key not found in XML");
            } else {
                Object xmlValue = xmlJson.get(key);
                if (!equifaxValue.equals(xmlValue)) {
                    JSONObject mismatchDetail = new JSONObject();
                    mismatchDetail.put("expected", xmlValue);
                    mismatchDetail.put("actual", equifaxValue);
                    mismatches.put(key, mismatchDetail);
                }
            }
        }
    }
}
```

Jsch
```java
JSch jsch = new JSch();
Session session = jsch.getSession(username, hostname, port);

// Add this before connecting
java.util.Properties config = new java.util.Properties();
config.put("StrictHostKeyChecking", "no");
config.put("PreferredAuthentications", "password,keyboard-interactive,publickey");
config.put("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256,ecdh-sha2-nistp256,ecdh-sha2-nistp384,ecdh-sha2-nistp521");
config.put("server_host_key", "ssh-rsa,ssh-dss,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521");
config.put("cipher.s2c", "aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-ctr,aes192-cbc,aes256-ctr,aes256-cbc");
config.put("cipher.c2s", "aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-ctr,aes192-cbc,aes256-ctr,aes256-cbc");
config.put("mac.s2c", "hmac-md5,hmac-sha1,hmac-sha2-256,hmac-sha1-96,hmac-md5-96");
config.put("mac.c2s", "hmac-md5,hmac-sha1,hmac-sha2-256,hmac-sha1-96,hmac-md5-96");
session.setConfig(config);

session.connect();
```

## Karate - Custom-HTML-Report
```java
package com.test.karate.automation.features;
import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomTestRunner {

    @Test
    public void testParallel() {
        String karateOutputPath = "target/custom-karate-reports";
        System.setProperty("karate.env", System.getProperty("karate.env"));

        Results results = Runner.builder()
                .outputCucumberJson(true)
                .path("classpath:com/test/karate/automation")
                .reportDir(karateOutputPath)
                .tags("@debuggingreport")
                .parallel(1);

        generateCustomReport(results, karateOutputPath);
        System.out.println(results.getErrorMessages());
    }

    public static void generateCustomReport(Results results, String outputPath) {
        try {
            String reportContent = generateHtmlContent(results, outputPath);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = outputPath + "/custom-report-" + timestamp + ".html";
            Files.createDirectories(Paths.get(outputPath));
            Files.write(Paths.get(reportPath), reportContent.getBytes());
            System.out.println("Custom report generated at: " + reportPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateHtmlContent(Results results, String outputPath) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
                .append("<html>\n<head>\n")
                .append("<title>Karate Test Execution Report</title>\n")
                .append("<meta name='viewport' content='width=device-width, initial-scale=1'>\n")
                .append("<link href='https://fonts.googleapis.com/css2?family=Google+Sans:wght@400;500;600&display=swap' rel='stylesheet'>\n")
                .append("<style>\n")
                .append(":root {\n")
                .append("  --primary-color: #0062ff;\n")
                .append("  --primary-gradient: #2979ff;\n")
                .append("  --success-color: #00c853;\n")
                .append("  --danger-color: #ff3d00;\n")
                .append("  --background-light: #f5f7fa;\n")
                .append("  --surface-color: #ffffff;\n")
                .append("  --text-primary: #1d1d1f;\n")
                .append("  --text-secondary: #424245;\n")
                .append("  --dark-surface: #1a1a1a;\n")
                .append("  --dark-elevated: #2c2c2e;\n")
                .append("  --dark-text: #f5f5f7;\n")
                .append("  --border-radius: 12px;\n")
                .append("  --transition: all 0.3s ease;\n")
                .append("}\n")
                .append("* { \n")
                .append("  box-sizing: border-box;\n")
                .append("  -webkit-font-smoothing: antialiased;\n")
                .append("}\n")
                .append("body {\n")
                .append("  font-family: 'Google Sans', 'Segoe UI', system-ui, -apple-system;\n")
                .append("  line-height: 1.6;\n")
                .append("  background-color: var(--background-light);\n")
                .append("  color: var(--text-primary);\n")
                .append("  margin: 0;\n")
                .append("  padding: 32px;\n")
                .append("}\n")
                .append(".container {\n")
                .append("  max-width: 1280px;\n")
                .append("  margin: 0 auto;\n")
                .append("  background-color: var(--surface-color);\n")
                .append("  box-shadow: 0 8px 24px rgba(0,0,0,0.08);\n")
                .append("  border-radius: var(--border-radius);\n")
                .append("  overflow: hidden;\n")
                .append("}\n")
                .append("h1 {\n")
                .append("  background: linear-gradient(135deg, var(--primary-color), var(--primary-gradient));\n")
                .append("  color: white;\n")
                .append("  padding: 32px;\n")
                .append("  margin: 0;\n")
                .append("  text-align: center;\n")
                .append("  font-weight: 500;\n")
                .append("  letter-spacing: -0.5px;\n")
                .append("}\n")
                .append(".summary {\n")
                .append("  display: grid;\n")
                .append("  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));\n")
                .append("  gap: 24px;\n")
                .append("  padding: 32px;\n")
                .append("}\n")
                .append(".summary-card {\n")
                .append("  background: var(--surface-color);\n")
                .append("  border-radius: var(--border-radius);\n")
                .append("  padding: 24px;\n")
                .append("  box-shadow: 0 4px 12px rgba(0,0,0,0.05);\n")
                .append("  transition: var(--transition);\n")
                .append("  border: 1px solid rgba(0,0,0,0.08);\n")
                .append("}\n")
                .append(".summary-card:hover {\n")
                .append("  transform: translateY(-4px);\n")
                .append("  box-shadow: 0 8px 16px rgba(0,0,0,0.1);\n")
                .append("}\n")
                .append(".summary-value {\n")
                .append("  font-size: 2.75rem;\n")
                .append("  font-weight: 600;\n")
                .append("  margin: 16px 0;\n")
                .append("  line-height: 1.2;\n")
                .append("}\n")
                .append("h3 {\n")
                .append("  color: var(--text-secondary);\n")
                .append("  font-weight: 500;\n")
                .append("  margin: 0;\n")
                .append("  font-size: 1rem;\n")
                .append("}\n")
                .append(".progress-container {\n")
                .append("  width: 100%;\n")
                .append("  background-color: rgba(0,0,0,0.05);\n")
                .append("  border-radius: 12px;\n")
                .append("  height: 8px;\n")
                .append("  margin-top: 20px;\n")
                .append("  overflow: hidden;\n")
                .append("}\n")
                .append(".progress-bar {\n")
                .append("  height: 100%;\n")
                .append("  border-radius: 12px;\n")
                .append("  background: linear-gradient(to right, var(--success-color), var(--primary-gradient));\n")
                .append("  transition: width 1s cubic-bezier(0.4, 0, 0.2, 1);\n")
                .append("}\n")
                .append("table {\n")
                .append("  width: 100%;\n")
                .append("  border-collapse: separate;\n")
                .append("  border-spacing: 0;\n")
                .append("  margin: 0 0 32px;\n")
                .append("  padding: 0 32px;\n")
                .append("}\n")
                .append("th, td {\n")
                .append("  border: none;\n")
                .append("  padding: 16px;\n")
                .append("  text-align: left;\n")
                .append("  word-wrap: break-word;\n")
                .append("  max-width: 300px;\n")
                .append("}\n")
                .append("th {\n")
                .append("  background-color: var(--background-light);\n")
                .append("  color: var(--text-primary);\n")
                .append("  font-weight: 500;\n")
                .append("  white-space: nowrap;\n")
                .append("  position: sticky;\n")
                .append("  top: 0;\n")
                .append("}\n")
                .append("th:first-child {\n")
                .append("  border-top-left-radius: 8px;\n")
                .append("  border-bottom-left-radius: 8px;\n")
                .append("}\n")
                .append("th:last-child {\n")
                .append("  border-top-right-radius: 8px;\n")
                .append("  border-bottom-right-radius: 8px;\n")
                .append("}\n")
                .append("tr {\n")
                .append("  transition: var(--transition);\n")
                .append("}\n")
                .append("tr:hover {\n")
                .append("  background-color: rgba(0,0,0,0.02);\n")
                .append("}\n")
                .append("td {\n")
                .append("  border-bottom: 1px solid rgba(0,0,0,0.05);\n")
                .append("}\n")
                .append(".status-passed {\n")
                .append("  color: #00841f;\n")
                .append("  background: rgba(0, 200, 83, 0.12);\n")
                .append("  padding: 4px 12px;\n")
                .append("  border-radius: 16px;\n")
                .append("  display: inline-block;\n")
                .append("  font-weight: 500;\n")
                .append("}\n")
                .append(".status-failed {\n")
                .append("  color: #c41d00;\n")
                .append("  background: rgba(255, 61, 0, 0.12);\n")
                .append("  padding: 4px 12px;\n")
                .append("  border-radius: 16px;\n")
                .append("  display: inline-block;\n")
                .append("  font-weight: 500;\n")
                .append("}\n")
                .append("@media (max-width: 768px) {\n")
                .append("  body { padding: 16px; }\n")
                .append("  .summary {\n")
                .append("    grid-template-columns: 1fr;\n")
                .append("    padding: 16px;\n")
                .append("  }\n")
                .append("  table { padding: 0 16px; }\n")
                .append("  .summary-value { font-size: 2.25rem; }\n")
                .append("}\n")
                .append("@media (prefers-color-scheme: dark) {\n")
                .append("  :root {\n")
                .append("    --background-light: var(--dark-surface);\n")
                .append("    --text-primary: var(--dark-text);\n")
                .append("    --surface-color: var(--dark-elevated);\n")
                .append("  }\n")
                .append("  td { border-bottom-color: rgba(255,255,255,0.1); }\n")
                .append("  tr:hover { background-color: rgba(255,255,255,0.03); }\n")
                .append("}\n")
                .append("</style>\n")
                .append("</head>\n<body>\n")
                .append("<div class='container'>\n");

        // Calculate metrics
        long totalTests = results.getScenarioResults().count();
        long failedTests = results.getFailCount();
        long passedTests = totalTests - failedTests;
        double passPercentage = (passedTests * 100.0) / totalTests;

        // Summary section
        html.append("<h1>Karate Test Execution Report</h1>\n")
                .append("<div class='summary'>\n")
                .append("<div class='summary-card'>\n")
                .append("<h3>Total Tests</h3>\n")
                .append("<div class='summary-value'>").append(totalTests).append("</div>\n")
                .append("</div>\n")
                .append("<div class='summary-card'>\n")
                .append("<h3>Passed Tests</h3>\n")
                .append("<div class='summary-value' style='color: var(--success-color);'>").append(passedTests).append("</div>\n")
                .append("</div>\n")
                .append("<div class='summary-card'>\n")
                .append("<h3>Failed Tests</h3>\n")
                .append("<div class='summary-value' style='color: var(--danger-color);'>").append(failedTests).append("</div>\n")
                .append("</div>\n")
                .append("<div class='summary-card'>\n")
                .append("<h3>Pass Percentage</h3>\n")
                .append("<div class='summary-value'>").append(String.format("%.2f%%", passPercentage)).append("</div>\n")
                .append("<div class='progress-container'>\n")
                .append(String.format("<div class='progress-bar' style='width: %.2f%%'></div>\n", passPercentage))
                .append("</div>\n")
                .append("</div>\n")
                .append("</div>\n");

        // Results Table
        html.append("<table>\n")
                .append("<tr>\n")
                .append("<th>Feature</th>\n")
                .append("<th>Scenario</th>\n")
                .append("<th>Status</th>\n")
                .append("<th>Error Message</th>\n")
                .append("</tr>\n");

        results.getScenarioResults().forEach(scenarioResult -> {
            String feature = scenarioResult.getScenario().getFeature().getName();
            String scenario = scenarioResult.getScenario().getName();
            String status = scenarioResult.isFailed() ? "Failed" : "Passed";
            String statusClass = scenarioResult.isFailed() ? "status-failed" : "status-passed";
            String errorMessage = scenarioResult.isFailed() ? scenarioResult.getErrorMessage() : "";

            html.append("<tr>\n")
                    .append("<td>").append(feature).append("</td>\n")
                    .append("<td>").append(scenario).append("</td>\n")
                    .append("<td><span class='").append(statusClass).append("'>")
                    .append(status).append("</span></td>\n")
                    .append("<td>").append(errorMessage).append("</td>\n")
                    .append("</tr>\n");
        });

        html.append("</table>\n")
                .append("</div>\n")
                .append("</body>\n</html>");

        return html.toString();
    }

    private static String formatTimestamp(long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
    }
}
```

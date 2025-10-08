Feature: This is to demonstrate json path filters to execute data for a given filter condition
    # mvn test '-Dkarate.options=--tags @csvfilter' -Dtest=ParallelTest -DExecute="'Y'"
    Background:
        * def csvWriter = Java.type('com.karate.helpers.CsvWriter')
        * def data = read('classpath:com/karate/data/testdata.csv')
    # * def filtered = get data[?(@.Execute == "'Y'" && @.FirstName = "Ravi")]
    # * def filtered = "'Y'"
    * def selected = karate.properties['Execute']
    * def fun = function(x){ return x.Execute == selected }
    * def filtered = karate.filter(data, fun)

    @csvfilter
    Scenario Outline: Scenario name
    * print 'Processing row:', __num + 1, 'with data:', __row
    * def status = '<Execute>' == 'Y' ? 'Passed' : 'Skipped'
    * csvWriter.updateCSVWithStatus('src/test/java/com/karate/data/testdata.csv', __num + 1, status, 'Status')
    * print 'Updated row', __num + 1, 'with status:', status
        Examples:
            | read('classpath:com/karate/data/testdata.csv') |

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
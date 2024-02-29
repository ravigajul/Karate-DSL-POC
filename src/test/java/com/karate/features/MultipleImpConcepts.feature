Feature: Contains eval, filter, calling other feature

    Background:
        * def data = read('classpath:com/karate/data/testdata.csv')
        # Filter the json rows with execute equals y and endpoint url not empty
        * def filteredData = karate.jsonPath(data, "$.[?(@.Execute == 'N' && @.endPointUrl != '')]")

    @evalconditional
    #mvn test '-Dkarate.options=--tags @evalconditional' -Dtest=ParallelTest
    Scenario Outline: Scenario name
        * def EXECUTE = '<Execute>'
        * eval
        """
        if (EXECUTE == "Y"){
            karate.call('CallOne.feature', __row)
        }else{
            karate.call('CallTwo.feature', __row)
        }
        """
        Examples:
            | filteredData |

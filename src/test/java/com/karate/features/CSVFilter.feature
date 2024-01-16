Feature: This is to demonstrate json path filters to execute data for a given filter condition

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

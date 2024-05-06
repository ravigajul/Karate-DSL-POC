Feature: UI Automation demo with karate framework
  Background: 
    * configure driver = { type: 'chrome', addOptions: ['--start-maximized','--remote-allow-origins=*'] }

    @UIDemo
    # mvn test '-Dkarate.options=--tags @UIDemo' -Dtest=ParallelTest
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
    






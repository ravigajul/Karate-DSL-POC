@DBConnection
Feature: This feature is for connecting to Database.

  Background: 
    * def dbHandler = Java.type('com.karate.helpers.DBHandler')

  @DBConnection
  Scenario: Scenario to demonstrate db connection.
   #* eval dbHandler.getSysDateFromDual()
   * def jsonobj = dbHandler.getSysDateFromDual()
   * print jsonobj.SYS_DATE
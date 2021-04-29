@ignore
Feature: Feature to pass javakeystore and password for ssl issues
  I want to use this template for my feature file

  @tag1
  Scenario: SSL certificate feature
  * configure ssl = { trustAll: true, keyStore: '#(keyStoreLocation/file.jks)', keyStorePassword: 'somepassword' }
    
    
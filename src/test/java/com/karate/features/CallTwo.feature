@ignore
Feature: Feature to demonstrate the Basic Auth
  
 @ignore
  Scenario: Scenario to generate basic auth token
  # __row is coming for calling feature
    * def articleData = read('classpath:com/karate/data/newArticleData.json')
    # eval(__row.querypath) will evaluate to json path articleData.article.title
    ## without using eval it will be a string "articleData.article.title" Hence the use of eval
    * print eval(__row.querypath)
    * print __row.querypath
Feature: sample karate test script
  for help, see: https://github.com/intuit/karate/wiki/IDE-Support

  Background: 
    * url baseurl

@yml
Scenario: scenario to read yaml file from external Test.yaml file.
		#Given def yamlobj = yamlobj. This object is directly available globally from config file hence commented.
	* print yamlobj.jobs.build.docker[0].image
	* print yamlobj.jobs.build.docker[0].auth.username
	* print yamlobj.jobs.build.docker[0].auth.password
	
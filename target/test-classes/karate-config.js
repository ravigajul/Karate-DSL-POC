function fn() {
  var env = karate.env; // get system property 'karate.env'
  karate.log('karate.env system property was:', env);
  if (!env) {
    env = 'qa';
  }
  var config = {
    env: env,
    baseurl: 'https://conduit.productionready.io'
  };
 if (env == 'qa') {
	 karate.configure('ssl', true);
	 karate.configure('readTimeout', 10000);
    config.useremail= 'rgajul@test.com';
    config.userpassword= 'karate123';
    	
   //passing feature file and config object to callSingle method which returns an object of variables declared in feature file.	
    var accessToken=karate.callSingle('classpath:com/karate/helpers/CreateToken.feature',config).authToken;
    
  //passing global headers that can be used by all urls
    karate.configure('headers',{Authorization: 'Token ' + accessToken});
    
    //reading yaml file for configuration data
    config.yamlobj = read('classpath:com/test/resources/Test.yaml');
    
    //reusing 
    config.isValidTime = read('classpath:com/karate/helpers/time-validator.js');
     
  }  else if (env == 'dev') {
	  config.useremail= 'ravi.gajul@test.com'
	  config.userpassword= 'Ant3m3an!'
  }
  
  return config;
}
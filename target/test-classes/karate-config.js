function fn() {
  var env = karate.env; // get system property 'karate.env'
  karate.log('karate.env system property was:', env);
  if (!env) {
    env = 'qa';
  }
  var config = {
    env: env,
    baseurl: 'https://conduit.productionready.io/api'
  }
  if (env == 'qa') {
    config.useremail= 'rgajul@test.com'
    config.userpassword= 'karate123'
    	
   //passing feature file and config object to callSingle method which returns an object of variables declared in feature file.	
    var accessToken=karate.callSingle('classpath:com/karate/helpers/CreateToken.feature',config).authToken
    
  //passing global headers that can be used by all urls
    karate.configure('headers',{Authorization: 'Token ' + accessToken})  
     
  } else if (env == 'dev') {
	  config.useremail= 'ravi.gajul@test.com'
	  config.userpassword= 'Ant3m3an!'
  }
  return config;
}
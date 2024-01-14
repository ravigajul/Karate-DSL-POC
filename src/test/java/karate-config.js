function fn() {
	var env = karate.env; // get system property 'karate.env'
	karate.log('karate.env system property was:', env);
	karate.configure('ssl', true);
	karate.configure('readTimeout', 20000);
	if (!env) {
		env = 'qa';
	}
	var config = {
		env : env,
		baseurl : 'https://conduit.productionready.io'
			
	};
	if (env == 'qa') {	

		// SSL keystory and passowrd
		// karate.configure('ssl', { trustAll: true, keyStore:
		// config.keyStoreLocation, keyStorePassword: 'somePass' });

		
		config.useremail = 'rgajul@test.com';
		config.userpassword = 'karate123';

		// passing feature file and config object to callSingle method which
		// returns an object of variables declared in feature file.
		var accessToken = karate.callSingle('classpath:com/karate/helpers/CreateToken.feature', config).authToken;

		// passing global headers that can be used by all urls
		karate.configure('headers', {
			Authorization : 'Token ' + accessToken
		});

		// reading yaml file for configuration data
		config.yamlobj = read('classpath:com/test/resources/Test.yaml');

		// reusing
		config.isValidTime = read('classpath:com/javascript/utils/time-validator.js');

	} else if (env == 'dev') {
		config.useremail = 'ravi.gajul@test.com'
		config.userpassword = 'Ant3m3an!'
	} else if (env == 'prod'){
		//read from external json file
		var externalConfig = read('classpath:config/config.json');
		//merge external config with base config
		config = karate.merge(config, externalConfig);
	}
	return config;
}
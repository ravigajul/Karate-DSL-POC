function getTimeStamp() {
	  var SimpleDateFormat = Java.type("java.text.SimpleDateFormat");
	  var sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
      return sdf.format(new Date());
      //new Date().toISOString();
	} 

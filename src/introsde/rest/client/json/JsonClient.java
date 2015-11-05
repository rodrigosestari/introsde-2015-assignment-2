package introsde.rest.client.json;

import introsde.rest.client.util.Request;

public class JsonClient {

    public static void main(String[] args) throws Exception {
        
    	if(args.length > 0) {
    		System.out.println("USAGE:");
    		System.out.println("Parameter 0: SERVER_BASE_URL (e.g. http://localhost:8000/sdelab).");
    		Request.setBaseUrl(args[0]);
    	}
    	
    	System.out.println("SERVER_BASE_URL set to " + Request.getBaseUrl());
        
    	Request.setRequest_number(0);
    	
    	try {
    		
	        JSON_Request.request1();
	        JSON_Request.request2();
	        JSON_Request.request3();
	        JSON_Request.request4();
	        JSON_Request.request5();
	        JSON_Request.request6();
	        JSON_Request.request7();
	        JSON_Request.request8();
	        JSON_Request.request9();
//	        JSON_Request.request10();
//	        JSON_Request.request11();
//	        JSON_Request.request12();
        
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}
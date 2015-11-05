package introsde.rest.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;

public class Request {
	
	
	public static String baseUrl = "http://localhost:8000/sdelab";	// Remote server URL

	public static int request_number = 0;							// Sequence number of each request #1, #2, #3, ..
	
    private String result;	// Result Status
    private String path;	// Request path e.g. /person
    private String method;	// Request method e.g. GET, POST, PUT, ..
    private String type;	// Request type could be xml or json
    private String body;	// Response
    private int ret;		// Result
    
    private HttpURLConnection con;

    public Request(String path, String method, String type) {
    	setRequest_number(getRequest_number()+1);
    	this.path = path;
        this.method = method;
        this.type = type;
    }

    // Getter and Setters
    public static int getRequest_number() {
		return request_number;
	}

	public static void setRequest_number(int request_number) {
		Request.request_number = request_number;
	}
	
	public static String getBaseUrl() {
		return baseUrl;
	}

	public static void setBaseUrl(String baseUrl) {
		Request.baseUrl = baseUrl;
	}

	public String doRequest(String data) throws IOException, JSONException {
    	
        URL url = new URL(baseUrl + path);
        
        // Set method and headers
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("Accept", type);
        con.setRequestProperty("Content-Type", type);
        
        if (method.equals("POST") || method.equals("PUT")) {
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(data);
            writer.close();
        }
        
        try {
            ret = con.getResponseCode();
            body = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                body += inputLine;
            }
            in.close();
            
        } catch (IOException e) {
        	e.printStackTrace();
            body = "";
        }
        return body;
    }

    public int getRespCode() {
        return ret;
    }
    
    public String getRespMessage() throws IOException {
        return con.getResponseMessage();
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void printReq() throws IOException {
        System.out.println("\n\nREQUEST #" + request_number + " : " + method + " " + path);
        System.out.println("Accept: " + type);
        System.out.println("Content-Type: " + type);
        System.out.println("=> Result: " + result);
        System.out.println("=> HTTP Status: " + con.getResponseCode());
        System.out.println(body + "\n\n");
    }
}
package introsde.rest.client.xml;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONException;
import org.w3c.dom.NodeList;

import introsde.rest.client.util.Request;
import introsde.rest.client.util.Settings;
import introsde.rest.client.util.XPathEvaluator;



public class ClientXml {


	public static String auxj;
	public static String person_2;
	public static int id;
	public static int idreq7;

	public static String mid,measureType;
	public static int countR9;
	public static String midR9;


	public static ArrayList<String> measure = new ArrayList<String>();
	private static String xmlFistPerson;
	public static int firstPerson, lastPerson,newIdPerson;
	private static FileWriter writer =null;

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"https://rodrigo-sestari.herokuapp.com/assignment2").build();
	}
	private static void write(String line){
		try {
			writer.append(line + " \n");
		} catch (IOException e) {
			e.printStackTrace();
		}  	
	}

	public static void main(String[] args) throws Exception {



		writer = new FileWriter("resources/client-server-xml.log");
		try {
			try {
				write("URL BASE: https://rodrigo-sestari.herokuapp.com/assignment2");
				request1();
				request2();
				request3();
				request4();
				request5();
				request6();
				request7();
				// request8();
				// request9();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			writer.flush();
			writer.close();
		}

	}

	/**
	 * Request #1: GET /person should list all the people
	 *  (see above Person model to know what data to return here) in your database 
	 *  (wrapped under the root element "people")
	 * @throws IOException
	 * @throws JSONException
	 * @throws Exception
	 */
	public static void request1() throws IOException, JSONException, Exception {


		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person");

		write("Request #1: [GET] ["+service.getUri()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get(); //content-type request //accept accept
		int httpStatus =response.getStatus(); 
		//String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		String xml = response.readEntity(String.class);


		NodeList n = XPathEvaluator.getNodes(xml, "//person");
		if (n.getLength() > 2) {
			write("=> Result:OK"); 
		} else {
			write("=> Result:ERROR");
		}

		NodeList n1 = XPathEvaluator.getNodes(xml, "//person[1]/idPerson/text()");
		firstPerson = Integer.parseInt(n1.item(0).getNodeValue());
		NodeList n2 = XPathEvaluator.getNodes(xml, "//person[last()]/idPerson/text()");
		lastPerson = Integer.parseInt(n2.item(0).getNodeValue());

		write("=> HTTP Status: " +httpStatus);

	}


	/**
	 * Step 3.2. Send R#2 for first_person_id. 
	 * If the responses for this is 200 or 202, the result is OK.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 */
	public static void request2() throws IOException, JSONException {


		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person/"+firstPerson);

		write("Request #2: [GET] ["+service.getUri()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get(); //content-type request //accept accept
		int httpStatus =response.getStatus(); 
		//String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		xmlFistPerson = response.readEntity(String.class);

		if ((httpStatus == 200) || (httpStatus == 202)){
			write("=> Result:OK");
		}else{
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " +httpStatus);


	}


	/**
	 * Step 3.3. Send R#3 for first_person_id changing the firstname. 
	 * If the responses has the name changed, the result is OK.
	 * @throws Exception
	 */
	public static void request3() throws Exception {


		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person/"+firstPerson);

		write("Request #3: [PUT] ["+service.getUri()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		NodeList nl = XPathEvaluator.getNodes(xmlFistPerson, "//firstname/text()");
		String name = nl.item(0).getNodeValue();
		xmlFistPerson = xmlFistPerson.replace(name,  "Changed Name");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).put(Entity.xml(xmlFistPerson));
		int httpStatus =response.getStatus(); 
		//String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		//	String xml = response.readEntity(String.class);


		if ((httpStatus == 201)){ //created
			write("=> Result:OK");
		}else{
			write("=> Result:ERROR");
		}

		write("=> HTTP Status: " +httpStatus);

	}

	/**
	 * Step 3.4. Send R#4 to create the following person. 
	 * Store the id of the new person. If the answer is 201 (200 or 202 are also applicable) 
	 * with a person in the body who has an ID, the result is OK.
	 * 
	 * @throws IOException
	 * @throws JSONException
	 * @throws Exception
	 */
	public static void request4() throws IOException, JSONException, Exception {


		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person");

		write("Request #4: [POST] ["+service.getUri()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).post(Entity.xml(xmlFistPerson));
		int httpStatus =response.getStatus(); 
		//String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		String xml = response.readEntity(String.class);
		NodeList n1 = XPathEvaluator.getNodes(xml, "//idPerson/text()");
		newIdPerson = Integer.parseInt(n1.item(0).getNodeValue());

		if ((httpStatus == 200) || (httpStatus == 201) || (httpStatus== 202)) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write(xml);
	}


	/**
	 * Step 3.5. Send R#5 for the person you have just created. 
	 * Then send R#1 with the id of that person. If the answer is 404, your result must be OK.
	 * @throws IOException
	 * @throws Exception
	 */
	public static void request5() throws IOException, Exception {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person/"+newIdPerson);

		writer.append("Request #5: [DELETE] ["+service.getUri()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).delete();
		int httpStatus =response.getStatus(); 
		//String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		String xml = response.readEntity(String.class);


		service = client.target(getBaseURI()).path("person/"+newIdPerson);
		response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		httpStatus =response.getStatus();
		xml = response.readEntity(String.class);

		if (httpStatus== 404) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}

	}


	/**
	 * Step 3.6. Follow now with the R#9 (GET BASE_URL/measureTypes). 
	 * If response contains more than 2 measureTypes - result is OK, else is ERROR 
	 * (less than 3 measureTypes). Save all measureTypes into array (measure_types)
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public static void request6() throws IOException, Exception {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("MeasureType/");

		writer.append("Request #6: [POST] ["+service.getUri()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).delete();
		//int httpStatus =response.getStatus(); 
		//String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		String xml = response.readEntity(String.class);

		NodeList n1 = XPathEvaluator.getNodes(xml, "//MeasureType");
		if (n1.getLength() > 2) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}

		n1 = XPathEvaluator.getNodes(xml, "//MeasureType/text()");
		for (int i = 0; i < n1.getLength(); i++) {
			String aux = n1.item(i).getNodeValue();
			measure.add(aux);
		}

	}


	/**
	 * Step 3.7. Send R#6 (GET BASE_URL/person/{id}/{measureType}) for the first person you obtained at the 
	 * beginning and the last person, and for each measure types from measure_types. 
	 * If no response has at least one measure - result is ERROR (no data at all) else result is OK.
	 *  Store one measure_id and one measureType.
	 *  
	 * @throws IOException
	 * @throws Exception
	 */
	public static void request7() throws IOException, Exception {

		boolean at_least_one = true;
		Request r = null;

		for (String mt : measure) {
			r = new Request("/person/" + firstPerson + "/" + mt, "GET", Settings.APP_XML);
			Request.setRequest_number(7);
			String x = r.doRequest("");

			NodeList n1 = XPathEvaluator.getNodes(x, "//measure");
			if(at_least_one && n1.getLength()>1){
				idreq7 = firstPerson;
				at_least_one = false;
				n1 = XPathEvaluator.getNodes(x, "//measure/mid/text()");
				mid = n1.item(0).getNodeValue();

				n1 = XPathEvaluator.getNodes(x, "//measure/measureType/text()");
				measureType = n1.item(0).getNodeValue();
				r.setResult("OK");
				r.printReq();
			}
		}
		if(at_least_one){
			r.setResult("ERROR");
			r.printReq();
		}
	}

	/**
	 * Request #8: Saves a new value for the {measureType} (e.g. weight) of person identified by {id}
	 * and archives the old value in the history
	 */
	public static void request8() throws IOException, Exception {
		Request r = new Request("/person/" + idreq7 + "/"+ measureType+"/"+mid, "GET", Settings.APP_XML);
		String j = r.doRequest("");
		if(r.getRespCode() == 200)
			r.setResult("OK");
		else
			r.setResult("ERROR");

		r.printReq();
	}

	/**
	 * Request #9: Returns the list of measures our model supports
	 */
	public static void request9() throws IOException, Exception {

		Request r = new Request("/person/" + idreq7 + "/"+ measure.get(0), "GET", Settings.APP_XML);
		String x = r.doRequest("");
		NodeList n1 = XPathEvaluator.getNodes(x, "//measure");
		countR9 = n1.getLength();
		r.setResult("OK");
		r.printReq();

		Request r2 = new Request("/person/" + idreq7 + "/"+ measure.get(0), "POST", Settings.APP_XML);
		Request.setRequest_number(9);
		x = r2.doRequest("<measure>\n" +
				"            <value>72</value>\n" +
				"            <created>2011-12-09</created>\n" +
				"        </measure>");
		r2.setResult("OK");
		n1 = XPathEvaluator.getNodes(x, "//measure/mid/text()");
		midR9 = n1.item(0).getNodeValue();
		r2.printReq();

		r = new Request("/person/" + idreq7 + "/"+ measure.get(0), "GET", Settings.APP_XML);
		Request.setRequest_number(9);
		x = r.doRequest("");
		n1 = XPathEvaluator.getNodes(x, "//measure");
		if(n1.getLength() == countR9+1)
			r.setResult("OK");
		else
			r.setResult("ERROR");
		r.printReq();
	}

	/**
	 * Request #10: Updates the value for the {measureType} (e.g., weight) identified by {mid},
	 * related to the person identified by {id}
	 */
	public static void request10() throws IOException, Exception {

		Request r = new Request("/person/" + idreq7 + "/" + measureType + "/" + midR9, "GET", Settings.APP_XML);
		Request.setRequest_number(10);
		r.doRequest("");
		r.setResult("OK");
		r.printReq();

		r = new Request("/person/" + idreq7 + "/" + measureType + "/" + midR9, "PUT", Settings.APP_XML);
		Request.setRequest_number(10);
		try {
			r.doRequest("<measure>\n<value>80</value>\n<created>2011-12-09</created>\n</measure>");
			r.setResult("OK");
			r.printReq();
		} catch (Exception e) {
			r.setResult("ERROR");
			r.printReq();
			return;
		}

		r = new Request("/person/" + idreq7 + "/" + measureType + "/" + midR9, "GET", Settings.APP_XML);
		Request.setRequest_number(10);
		String x = r.doRequest("");
		NodeList n1 = XPathEvaluator.getNodes(x, "//measure/value/text()");
		String val = n1.item(0).getNodeValue();
		if(val.equals("80"))
			r.setResult("OK");
		else
			r.setResult("ERROR");

		r.printReq();
	}

	/**
	 * Request #11: Returns the history of {measureType} (e.g., weight) for person {id} in the specified range of date
	 */
	public static void request11() throws IOException, Exception {
		Request r = new Request("/person/" + idreq7 + "/"+ measureType+"/?before=10-12-2012&after=21-12-2012", "GET", Settings.APP_XML);
		String x = r.doRequest("");
		NodeList n1 = XPathEvaluator.getNodes(x, "//measure");
		if (n1.getLength()>=1 && (r.getRespCode() == 200 || r.getRespCode() == 201 || r.getRespCode() == 202)) {
			r.setResult("OK");
		} else {
			r.setResult("ERROR");
		}
		r.printReq();
	}

	/**
	 * Request #12: Retrieves people whose {measureType} (e.g., weight) value is in
	 * the [{min},{max}] range (if only one for the query params is provided, use only that)
	 */
	public static void request12() throws IOException, Exception {

		Request r = new Request("/person?measureType=heigth&max=150&min=190", "GET", Settings.APP_XML);
		String x = r.doRequest("");
		NodeList n1 = XPathEvaluator.getNodes(x, "//person");
		if (n1.getLength()>=1 && (r.getRespCode() == 200 || r.getRespCode() == 201 || r.getRespCode() == 202)) {
			r.setResult("OK");
		} else {
			r.setResult("ERROR");
		}
		r.printReq();
	}
}
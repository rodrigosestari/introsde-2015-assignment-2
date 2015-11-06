package introsde.rest.client.xml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONException;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class ClientXml {

	public static ArrayList<String> measure = new ArrayList<String>();
	private static String xmlFistPerson, measure_id, measureType;
	public static int firstPerson, lastPerson, newIdPerson, countMeasure, newcountMeasure;
	private static FileWriter writer = null;

	private static URI getBaseURI() {
		return UriBuilder.fromUri("https://rodrigo-sestari.herokuapp.com/assignment2").build();
	}

	public static NodeList getNodes(String source, String query) throws Exception {

		InputSource input_source = new InputSource(new StringReader(source));

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		org.w3c.dom.Document document = db.parse(input_source);

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		NodeList nl = (NodeList) xpath.evaluate(query, document, XPathConstants.NODESET);
		return nl;
	}

	private static void write(String line) {
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
				request8();
				request9();

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

		write("\n \n Request #1: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get(); //content-type request //accept accept
		int httpStatus =response.getStatus(); 
		//String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		String xml = response.readEntity(String.class);


		NodeList n = getNodes(xml, "//person");
		if (n.getLength() > 2) {
			write("=> Result:OK"); 
		} else {
			write("=> Result:ERROR");
		}

		NodeList n1 = getNodes(xml, "//person[1]/idPerson/text()");
		firstPerson = Integer.parseInt(n1.item(0).getNodeValue());
		NodeList n2 = getNodes(xml, "//person[last()]/idPerson/text()");
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

		write("\n \n Request #2: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

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

		write("\n \n Request #3: [PUT] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		NodeList nl = getNodes(xmlFistPerson, "//firstname/text()");
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

		write("\n \n Request #4: [POST] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		String newPerson ="<person><firstname>Rodrigo</firstname><lastname>Sestari</lastname><birthdate>2000-03-22</birthdate><healthProfile><measureType><measure>heigth</measure><value>58.1</value></measureType><measureType><measure>weigth</measure><value>34.2</value></measureType></healthProfile></person>";
		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).post(Entity.xml(newPerson));
		int httpStatus =response.getStatus();     		
		String xml = response.readEntity(String.class);
		NodeList n1 = getNodes(xml, "//idPerson/text()");
		newIdPerson = Integer.parseInt(n1.item(0).getNodeValue());

		if ((httpStatus == 200) || (httpStatus == 201) || (httpStatus== 202)) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " +httpStatus);
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

		write("\n \n Request #5: [DELETE] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).delete();
		int httpStatus =response.getStatus(); 
		String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		String xml = response.readEntity(String.class);
		write("=> Result:"+responseStatus);
		write("=> HTTP Status: " +httpStatus);


		service = client.target(getBaseURI()).path("person/"+newIdPerson);
		write("\n Request #5: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");
		response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		httpStatus =response.getStatus();
		xml = response.readEntity(String.class);

		if (httpStatus== 404) { 
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " +httpStatus);

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
		WebTarget service = client.target(getBaseURI()).path("measureTypes/");

		write("\n \n Request #6: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		int httpStatus =response.getStatus(); 
		//String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		String xml = response.readEntity(String.class);

		NodeList n1 = getNodes(xml, "//measureType");
		if (n1.getLength() > 2) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " +httpStatus);

		n1 = getNodes(xml, "//measureType/text()");
		for (int i = 0; i < n1.getLength(); i++) {
			String aux = n1.item(i).getNodeValue();
			measure.add(aux);
		}

	}

	private static boolean auxrequest7(int idPerson) throws IOException, Exception{
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service =null;
		Response response  =null;
		String xml = null;
		boolean trovato = false;
		for (String mt : measure) {
			service = client.target(getBaseURI()).path("person/"+idPerson+"/"+mt);
			write("\n Request #7: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");
			response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
			int httpStatus =response.getStatus(); 
			xml = response.readEntity(String.class);
		
			
			try {
				NodeList n1 = getNodes(xml, "//measure");
				if((!trovato) && n1.getLength()>1){

					n1 = getNodes(xml, "//measure/mid/text()");
					measure_id = n1.item(0).getNodeValue();

					measureType = mt;
					newIdPerson = idPerson;

					trovato = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(trovato)
			    write("=> Result:OK");
			    else
			    	write("=> Result:ERROR");
			write("=> HTTP Status: " +httpStatus);

		}
		
		return trovato;
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


		
		write("\n");
		boolean a1 = auxrequest7(firstPerson);
		boolean a2 = auxrequest7(lastPerson);
		if (a1 && a2){
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
	}

	/**
	 * Step 3.8. Send R#7 (GET BASE_URL/person/{id}/{measureType}/{mid}) for the stored measure_id and measureType.
	 *  If the response is 200, result is OK, else is ERROR.
	 * @throws IOException
	 * @throws Exception
	 */
	public static void request8() throws IOException, Exception {


		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person/"+newIdPerson+"/"+measureType+"/"+measure_id);

		write("\n \n Request #8: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		int httpStatus =response.getStatus(); 
		//String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		//String xml = response.readEntity(String.class);

		if(httpStatus == 200){
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " +httpStatus);
	}

	/**
	 * Step 3.9. Choose a measureType from measure_types and send the request R#6 (GET BASE_URL/person/{first_person_id}/{measureType}) 
	 * and save count value (e.g. 5 measurements). Then send R#8 (POST BASE_URL/person/{first_person_id}/{measureTypes}) with 
	 * the measurement specified below. Follow up with another R#6 as the first to check the new count value.
	 *  If it is 1 measure more - print OK, else print ERROR. Remember, first with JSON and then with XML as content-types
	 *  
	 * @throws IOException
	 * @throws Exception
	 */
	public static void request9() throws IOException, Exception {


		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI()).path("person/"+newIdPerson+"/"+measureType+"/"+measure_id);

		write("\n \n Request #9: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");

		Response response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		int httpStatus =response.getStatus(); 		    		
		String xml = response.readEntity(String.class);
		NodeList n1 = getNodes(xml, "//measure");
		countMeasure = n1.getLength();
		String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		write("=> Result:"+responseStatus);
		write("=> HTTP Status: " +httpStatus);


		service = client.target(getBaseURI()).path("person/"+newIdPerson+"/"+measureType);

		write("\n Request #9: [POST] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");
		xml = "<measure> <value>99</value> <created>2015-11-05</created> </measure>";
		response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).post(Entity.xml(xml));
		httpStatus =response.getStatus();
		responseStatus =response.getStatusInfo().getReasonPhrase();    		
		write("=> Result:"+responseStatus);
		write("=> HTTP Status: " +httpStatus);


		service = client.target(getBaseURI()).path("person/"+newIdPerson+"/"+measureType+"/"+measure_id);

		write("\n Request #9: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [MediaType.APPLICATION_XML]");
		response = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML).get();
		httpStatus =response.getStatus(); 		    		
		xml = response.readEntity(String.class);
		n1 = getNodes(xml, "//measure");
		newcountMeasure = n1.getLength();
		if (newcountMeasure == countMeasure){
			write("=> Result:ERROR");
		}else{
			write("=> Result:OK");
		}
		write("=> HTTP Status: " +httpStatus);

	}
}
package introsde.rest.client.json;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientJson {


	public static ArrayList<String> measure = new ArrayList<String>();
	private static String jsonFistPerson, measure_id, measureType;
	public static int firstPerson, lastPerson, newIdPerson, countMeasure, newcountMeasure;
	private static FileWriter writer = null;

	private static URI getBaseURI() {
		return UriBuilder.fromUri("https://rodrigo-sestari.herokuapp.com/assignment2").build();
	}


	private static void write(String line) {
		try {
			System.out.println(line);
			writer.append(line + " \n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		writer = new FileWriter("resources/client-server-json.log");
		try {
			try {
				System.out.println("START client Json");
				write("URL BASE: https://rodrigo-sestari.herokuapp.com/assignment2");
				write(" \n -------------");
				request1();
				write(" \n -------------");
				request2();
				write(" \n -------------");
				request3();
				write(" \n -------------");
				request4();
				write(" \n -------------");
				request5();
				write(" \n -------------");
				request6();
				write(" \n -------------");
				request7();
				write(" \n -------------");
				request8();
				write(" \n -------------");
				request9();
				write(" \n END");
				System.out.println("END client XML");

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

		write("\n \n Request #1: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_JSON] Content-type: [APPLICATION_JSON]");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus =response.getStatus();     		
		String json = response.readEntity(String.class);

		JSONObject j = new JSONObject(json);
		JSONArray people = j.getJSONArray("person");

		if (people.length() > 2) {
			write("=> Result:OK"); 
		} else {
			write("=> Result:ERROR");
		}


		firstPerson = people.getJSONObject(0).getInt("idPerson");		
		lastPerson = people.getJSONObject(people.length() - 1).getInt("idPerson");

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

		write("\n \n Request #2: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_JSON] Content-type: [APPLICATION_JSON]");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get(); //content-type request //accept accept
		int httpStatus =response.getStatus();     		
		jsonFistPerson = response.readEntity(String.class);

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

		write("\n \n Request #3: [PUT] ["+service.getUri().getPath()+"] Accept: [APPLICATION_JSON] Content-type: [APPLICATION_JSON]");


		JSONObject j = new JSONObject(jsonFistPerson);
		j.put("firstname", "New Firstname");
		j.put("lastname", "Changed lastname json");


		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(Entity.json(jsonFistPerson));
		int httpStatus =response.getStatus(); 


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

		write("\n \n Request #4: [POST] ["+service.getUri().getPath()+"] Accept: [APPLICATION_JSON] Content-type: [APPLICATION_JSON]");

		String newPerson ="{ \"firstname\": \"Rodrigo\", \"lastname\": \"JSON\", \"birthdate\": \"2000-03-22\",\n"
				+ "		\"healthProfile\" : {\"measure\": [\n"
				+ "		{ \"created\": \"2015-10-10\", \"value\": 34.8, \"measureType\": \"weigth\" },\n"
				+ "		{  \"created\": \"2015-10-10\", \"value\": 173, \"measureType\": \"heigth\"  }"
				+ "		]}"
				+ "}";;

				Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.json(newPerson));
				int httpStatus =response.getStatus();     		
				String json = response.readEntity(String.class);
				JSONObject j = new JSONObject(json);
				newIdPerson  = j.getInt("idPerson");

				if ((httpStatus == 200) || (httpStatus == 201) || (httpStatus== 202)) {
					write("=> Result:OK");
				} else {
					write("=> Result:ERROR");
				}
				write("=> HTTP Status: " +httpStatus);
				write(json);
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

		write("\n \n Request #5: [DELETE] ["+service.getUri().getPath()+"] Accept: [APPLICATION_JSON] Content-type: [APPLICATION_JSON]");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).delete();
		int httpStatus =response.getStatus(); 
		String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		String json = response.readEntity(String.class);
		//	write("=> Result:"+responseStatus);
		write("=> HTTP Status: " +httpStatus);


		service = client.target(getBaseURI()).path("person/"+newIdPerson);
		write("\n Request #5: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_XML] Content-type: [APPLICATION_XML]");
		response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		httpStatus =response.getStatus();
		json = response.readEntity(String.class);

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

		write("\n \n Request #6: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_JSON] Content-type: [APPLICATION_JSON]");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus =response.getStatus();     		
		String json = response.readEntity(String.class);

		JSONObject j = new JSONObject(json);
		JSONArray ja = j.getJSONArray("MeasureType");
		if (ja.length() > 2) {
			write("=> Result:OK");
		} else {
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " +httpStatus);


		for (int i = 0; i < ja.length(); i++) {			
			measure.add(ja.getString(i));
		}

	}





	private static boolean auxrequest7(int idPerson) throws IOException, Exception{
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service =null;
		Response response  =null;
		String json = null;
		boolean trovato = false;
		for (String mt : measure) {
			service = client.target(getBaseURI()).path("person/"+idPerson+"/"+mt);
			write("\n Request #7: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_JSON] Content-type: [APPLICATION_JSON]");
			response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
			int httpStatus =response.getStatus(); 
			json = response.readEntity(String.class);


			try {
				JSONObject jo = new JSONObject(json);
				JSONArray ja = jo.getJSONArray("measure");
				if((!trovato) && ja.length() >1){

					measure_id = ja.getJSONObject(0).getString("mid");

					measureType = mt;
					newIdPerson = idPerson;

					trovato = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			//if(trovato)
			//  write("=> Result:OK");
			//else
			//write("=> Result:ERROR");
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

		write("\n \n Request #8: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_JSON] Content-type: [APPLICATION_JSON]");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus =response.getStatus(); 

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
		WebTarget service = client.target(getBaseURI()).path("person/"+newIdPerson+"/"+measureType);

		write("\n \n Request #9: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_JSON] Content-type: [APPLICATION_JSON]");

		Response response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		int httpStatus =response.getStatus(); 		    		
		String json = response.readEntity(String.class);
		JSONObject j = new JSONObject(json);
		countMeasure = j.getJSONArray("measure").length();

		String responseStatus =response.getStatusInfo().getReasonPhrase();    		
		//	write("=> Result:"+responseStatus);
		write("=> HTTP Status: " +httpStatus);


		service = client.target(getBaseURI()).path("person/"+newIdPerson+"/"+measureType);

		write("\n Request #9: [POST] ["+service.getUri().getPath()+"] Accept: [APPLICATION_JSON] Content-type: [APPLICATION_JSON]");
		json ="{  \"value\": 72, \"measureType\": \"2015-10-10\"  }";
		response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(Entity.json(json));
		httpStatus =response.getStatus();
		responseStatus =response.getStatusInfo().getReasonPhrase();    		
		//write("=> Result:"+responseStatus);
		write("=> HTTP Status: " +httpStatus);
		write(json);


		service = client.target(getBaseURI()).path("person/"+newIdPerson+"/"+measureType);

		write("\n Request #9: [GET] ["+service.getUri().getPath()+"] Accept: [APPLICATION_JSON] Content-type: [APPLICATION_JSON]");
		response = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
		httpStatus =response.getStatus(); 		    		
		json = response.readEntity(String.class);
		j = new JSONObject(json);
		newcountMeasure = j.getJSONArray("measure").length();

		if (newcountMeasure > countMeasure){
			write("=> Result:OK");
		}else{
			write("=> Result:ERROR");
		}
		write("=> HTTP Status: " +httpStatus);

	}


}
package introsde.rest.client.json;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import introsde.rest.client.util.Request;
import introsde.rest.client.util.Settings;

public class JSON_Request {

    public static int first;
    public static int last;
    
    public static String body_r2;
    
    public static String auxx;
    public static int id;
    public static int idreq7;
    public static ArrayList<String> measure_types = new ArrayList<>();
    public static String mid, measureType;
    public static int countR9;
    public static String midR9;

    /**
     * Request #1: Lists all people stored in the database
     */
    public static void request1() throws IOException, JSONException, Exception {

        Request req = new Request("/person", "GET", Settings.APP_JSON);
        String json = req.doRequest("");
        JSONObject j = new JSONObject(json);
        JSONArray people = j.getJSONArray("person");
        if (people.length() > 2) {
            req.setResult("OK");
        } else {
            req.setResult("ERROR");
        }
        first = people.getJSONObject(0).getInt("idp");
        last = people.getJSONObject(people.length() - 1).getInt("idp");
        req.printReq();
    }

    /**
     * Request #2: Gives all the personal information plus current measures of person identified by {id} 
     * (e.g., current measures means current health profile).
     */
    public static void request2() throws IOException, JSONException {
    	
        Request req = new Request("/person/" + first, "GET", Settings.APP_JSON);
        body_r2 = req.doRequest("");
        if (req.getRespCode() == 200 || req.getRespCode() == 202) {
            req.setResult("OK");
        } else {
            req.setResult("ERROR");
        }
        req.printReq();
    }

    /**
     * Request #3: Updates the personal information of the person identified by {id}
     * (e.g., only the person's information, not the measures of the health profile)
     */
    public static void request3() throws Exception {

        JSONObject j = new JSONObject(body_r2);
        j.put("firstname", "New Firstname");
        j.put("lastname", "New Lastname");
        Request req = new Request("/person/" + first, "PUT", Settings.APP_JSON);
        req.doRequest(j.toString());
        if ( req.getRespCode() == 202 || req.getRespMessage().equalsIgnoreCase("changed")) {
            req.setResult("OK");
        } else {
            req.setResult("ERROR");
        }
        req.printReq();
    }

    /**
     * Request #4: Creates a new person and returns the newly created person with its assigned id
     * (if a health profile is included, create also those measurements for the new person).
     */
    public static void request4() throws IOException, JSONException, Exception {
    	
        Request req = new Request("/person", "POST", Settings.APP_JSON);
        String ret = req.doRequest(Settings.JSON_POST_EXAMPLE);
        JSONObject j = new JSONObject(ret);
        id = j.getInt("idp");
        if (req.getRespCode() == 200 || req.getRespCode() == 201 || req.getRespCode() == 202) {
            req.setResult("OK");
        } else {
            req.setResult("ERROR");
        }
        req.printReq();
    }

    /**
     * Request #5: Deletes the person identified by {id} from the database
     */
    public static void request5() throws IOException, Exception {
    	
        Request req = new Request("/person/" + id, "DELETE", Settings.APP_JSON);
        req.doRequest("");
        req.setResult("OK");
        req.printReq();

        Request req2 = new Request("/person/" + id, "GET", Settings.APP_JSON);
        Request.setRequest_number(5);
        try {
            req2.doRequest("");
            if (req2.getRespCode() == 404) {
                req2.setResult("OK");
            } else {
                req2.setResult("ERROR");
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        req2.printReq();
    }

    /**
     * Request #6: Returns the list of values (the history) of {measureType}
     * (e.g. weight) for person identified by {id}
     */
    public static void request6() throws IOException, Exception {
    	
        Request req = new Request("/measureTypes/", "GET", Settings.APP_JSON);
        String ret = req.doRequest("");
        JSONObject j = new JSONObject(ret);
        JSONArray ja = j.getJSONArray("MeasureType");
        if (ja.length() > 2) {
            req.setResult("OK");
        } else {
            req.setResult("ERROR");
        }

        for (int i = 0; i < ja.length(); i++) {
            measure_types.add(ja.getString(i));
        }
        req.printReq();
    }

    /**
     * Request #7: Returns the value of {measureType} (e.g. weight)
     * identified by {mid} for person identified by {id}
     */
    public static void request7() throws IOException, Exception {
    	
        boolean at_least_one = true;
        Request r = null;
        for (String mt : measure_types) {
        	
            r = new Request("/person/" + first + "/" + mt, "GET", Settings.APP_JSON);
            Request.setRequest_number(7);
            
            String j = r.doRequest("");
            JSONObject jo = new JSONObject(j);
            JSONArray ja = jo.getJSONArray("measure");
            
            if (at_least_one && ja.length() > 1) {
                idreq7 = first;
                at_least_one = false;
                mid = ja.getJSONObject(0).getString("mid");
                measureType = ja.getJSONObject(0).getString("measureType");
                r.setResult("OK");
                r.printReq();
            }
        }
        if (at_least_one) {
            r.setResult("ERROR");
            r.printReq();
        }
    }

    /**
     * Request #8: Saves a new value for the {measureType} (e.g. weight) of person identified by {id}
     * and archives the old value in the history
     */
    public static void request8() throws IOException, Exception {
    	
        Request r = new Request("/person/" + idreq7 + "/" + measureType + "/" + mid, "GET", Settings.APP_JSON);
        r.doRequest("");
        if (r.getRespCode() == 200) {
            r.setResult("OK");
        } else {
            r.setResult("ERROR");
        }
        r.printReq();
    }

    /**
     * Request #9: Returns the list of measures our model supports
     */
    public static void request9() throws IOException, Exception {
    	
        Request r = new Request("/person/" + idreq7 + "/" + measure_types.get(0), "GET", Settings.APP_JSON);
        String x = r.doRequest("");
        JSONObject j = new JSONObject(x);
        countR9 = j.getJSONArray("measure").length();
        r.setResult("OK");
        r.printReq();

        Request r2 = new Request("/person/" + idreq7 + "/" + measure_types.get(0), "POST", Settings.APP_JSON);
        Request.setRequest_number(9);
        x = r2.doRequest("{\n"
                + "    \"value\": 72,\n"
                + "    \"measureType\": \"weigth\"\n"
                + "}");
        r2.setResult("OK");
        j = new JSONObject(x);
        midR9 = j.getString("mid");
        r2.printReq();

        r = new Request("/person/" + idreq7 + "/" + measure_types.get(0), "GET", Settings.APP_JSON);
        Request.setRequest_number(9);
        x = r.doRequest("");
        j = new JSONObject(x);
        if (j.getJSONArray("measure").length() == countR9 + 1) {
            r.setResult("OK");
        } else {
            r.setResult("ERROR");
        }
        r.printReq();
    }

    /**
     * Request #10: Updates the value for the {measureType} (e.g., weight) identified by {mid},
     * related to the person identified by {id}
     */
    public static void request10() throws IOException, Exception {
    	
        Request r = new Request("/person/" + idreq7 + "/" + measureType + "/" + midR9, "PUT", Settings.APP_JSON);
        try {
            r.doRequest("{\n"
                + "    \"value\": 80,\n"
                + "    \"measureType\": \"weigth\"\n"
                + "}");
        } catch (Exception e) {
            r.setResult("ERROR");
            r.printReq();
            return;
        }
        r.setResult("OK");
        r.printReq();
        
        r = new Request("/person/" + idreq7 + "/" + measureType + "/" + midR9, "GET", Settings.APP_JSON);
        Request.setRequest_number(10);
        r.doRequest("");
        String x = r.doRequest("");
        JSONObject j = new JSONObject(x);
        if(j.getString("value").equals("80"))
            r.setResult("OK");
        else
            r.setResult("ERROR");
        
        r.printReq();
    }
    
    /**
     * Request #11: Returns the history of {measureType} (e.g., weight) for person {id} in the specified range of date
     */
    public static void request11() throws IOException, Exception {
    	
        Request r = new Request("/person/" + idreq7 + "/"+ measureType+"/?before=10-12-2012&after=21-12-2012", "GET", Settings.APP_JSON);
        String j = r.doRequest("");
        JSONObject jo = new JSONObject(j);
        JSONArray ja = jo.getJSONArray("measure");
        if (ja.length()>=1 && (r.getRespCode() == 200 || r.getRespCode() == 201 || r.getRespCode() == 202)) {
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
    	
        Request r = new Request("/person/?measureType=heigth&max=100&min=200", "GET", "json");
        String j = r.doRequest("");
        JSONObject jo = new JSONObject(j);
        JSONArray ja = jo.getJSONArray("person");
        if (ja.length()>=1 && (r.getRespCode() == 200 || r.getRespCode() == 201 || r.getRespCode() == 202)) {
            r.setResult("OK");
        } else {
            r.setResult("ERROR");
        }
        r.printReq();
    }
}
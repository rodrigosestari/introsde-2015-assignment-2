package introsde.rest.client.util;

public final class Settings {

	public static final String APP_JSON = "application/json";
	public static final String APP_XML = "application/xml";
	
	
	public static final String JSON_POST_EXAMPLE = ""
			+ "{\n"
            + "		\"firstname\"     : \"Just\",\n"
            + "		\"lastname\"      : \"Created\",\n"
            + "		\"birthdate\"     : \"01-01-1987\",\n"
            + "		\"healthProfile\" : {\"measure\": [\n"
            + "		{\n"
            + "				\"created\": \"11-02-2015\",\n"
            + "				\"value\": 89.9,\n"
            + "				\"measureType\": \"weigth\"\n"
            + "		},\n"
            + "		{\n"
            + "				\"created\": \"11-02-2015\",\n"
            + "				\"value\": 180,\n"
            + "				\"measureType\": \"heigth\"\n"
            + "            }"
            + "		]}"
            + "}";
	
	
	public static final String XML_POST_EXAMPLE = "<person>\n"
            + "        <lastname>Norris</lastname>\n"
            + "        <firstname>Chuck</firstname>\n"
            + "        <birthdate>01-01-1991</birthdate>\n"
            + "        <healthProfile>\n"
            + "            <measure>\n"
            + "                <value>78.9</value>\n"
            + "                <created>1416172824725</created>\n"
            + "                <measureType>weigth</measureType>\n"
            + "                <created>16-11-2014</created>\n"
            + "            </measure>\n"
            + "            <measure>\n"
            + "                <value>172.4</value>\n"
            + "                <created>1416172824741</created>\n"
            + "                <measureType>heigth</measureType>\n"
            + "                <created>16-11-2014</created>\n"
            + "            </measure>\n"
            + "        </healthProfile>\n"
            + "    </person>";
}

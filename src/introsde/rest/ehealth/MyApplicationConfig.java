package introsde.rest.ehealth;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("assignment2")
public class MyApplicationConfig extends ResourceConfig {
    public MyApplicationConfig() {
        packages("introsde.rest.ehealth");
    }
}

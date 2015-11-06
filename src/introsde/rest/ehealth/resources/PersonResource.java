package introsde.rest.ehealth.resources;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import introsde.rest.ehealth.model.Person;
import introsde.rest.ehealth.model.PersonBean;

@Stateless // only used if the the application is deployed in a Java EE
			// container
@LocalBean // only used if the the application is deployed in a Java EE
			// container
public class PersonResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	int id;

	EntityManager entityManager; // only used if the application is deployed in
									// a Java EE container

	public PersonResource(UriInfo uriInfo, Request request, int id, EntityManager em) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.entityManager = em;
	}

	public PersonResource(UriInfo uriInfo, Request request, int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	/**
	 * Request #2: GET /person/{id} should give all the personal information
	 * plus current measures of person identified by {id} (e.g., current
	 * measures means current health profile)
	 * 
	 * @return
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
	public Response getPerson() {

		PersonBean person = null;
		try {
			person = this.getPersonById(id);

			if (person == null) {
				return Response.noContent().build();
			} else {
				return Response.ok().entity(person).build();
			}
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Request #3: PUT /person/{id} should update the personal information of
	 * the person identified by {id} (e.g., only the person's information, not
	 * the measures of the health profile)
	 * 
	 * @param person
	 * @return
	 */
	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putPerson(PersonBean person) {
		System.out.println("--> Updating Person... " + this.id);
		System.out.println("--> " + person.toString());
		Response res;
		Person existing = Person.getPersonById(this.id);
		if (existing == null) {
			res = Response.noContent().build();
		} else {
			person.setIdPerson(existing.getIdPerson());
			Person.updatePerson(person);
			res = Response.created(uriInfo.getAbsolutePath()).build();
		}
		return res;
	}

	/**
	 * Request #5: DELETE /person/{id} should delete the person identified by
	 * {id} from the system
	 */
	@DELETE
	public Response deletePerson() {
		try {
			Person c = Person.getPersonById(id);
			if (c == null)
					return Response.noContent().build();
		
			Person.removePerson(c);
		} catch (Exception e) {
			return Response.serverError().build();
		}
		
		return Response.ok().build();
	}


	private PersonBean getPersonById(int personId) {
		System.out.println("Reading person from DB with id: " + personId);
		PersonBean person = Person.getPersonBeanById(personId);		
		System.out.println("Person: " + person.toString());
		return person;
	}

}
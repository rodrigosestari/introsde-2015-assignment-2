package introsde.rest.ehealth.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import introsde.rest.ehealth.bean.MeasureHistoryBean;
import introsde.rest.ehealth.bean.MeasureTypeBean;
import introsde.rest.ehealth.bean.PeopleBean;
import introsde.rest.ehealth.model.MeasureDefinition;
import introsde.rest.ehealth.model.MeasureHistory;
import introsde.rest.ehealth.model.Person;
import introsde.rest.ehealth.model.PersonBean;

@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
@Path("/person")
public class PersonCollectionResource {

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	// will work only inside a Java EE application
	@PersistenceUnit(unitName = "introsde-jpa")
	EntityManager entityManager;

	// will work only inside a Java EEapplication
	@PersistenceContext(unitName = "introsde-jpa", type = PersistenceContextType.TRANSACTION)
	private EntityManagerFactory entityManagerFactory;

	// Return the list of people to the user in the browser
	/**
	 * Request #1: GET /person should list all the people (see above Person
	 * model to know what data to return here) in your database (wrapped under
	 * the root element "people")
	 * 
	 * @return
	 */
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response  getPersonsBrowser() {
		List<PersonBean> people = null;
		PeopleBean peopleBean = null;		
		try {
			System.out.println("Getting list of people...");
			people = Person.getAllBean(true);			
			if (people == null) {
				return Response.noContent().build();
			} else {
				peopleBean =  new PeopleBean();
				peopleBean.setMeasureType(people);
				return Response.ok().entity(peopleBean).build();
			}

		} catch (Exception e) {
		  return Response.serverError().build();
		}
		
	}

	// retuns the number of people
	// to get the total number of records
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		System.out.println("Getting count...");
		List<Person> people = Person.getAll();
		int count = people.size();
		return String.valueOf(count);
	}

	/**
	 * Request #4: POST /person should create a new person and return the newly
	 * created person with its assigned id (if a health profile is included,
	 * create also those measurements for the new person).
	 * 
	 * @param person
	 * @return
	 * @throws IOException
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response newPerson(PersonBean person) throws IOException {
		PersonBean pb = null;
		try {
			System.out.println("Creating new person...123");
			pb = Person.insertPersonBean(person);
			if (pb == null) {
				return Response.noContent().build();
			} else {
				return Response.ok().entity(pb).build();
			}
		} catch (Exception e) {
			return Response.serverError().build();
		}

	}

	// Defines that the next path parameter after the base url is
	// treated as a parameter and passed to the PersonResources
	// Allows to type http://localhost:599/base_url/1
	// 1 will be treaded as parameter todo and passed to PersonResource
	@Path("{personId}")
	public PersonResource getPerson(@PathParam("personId") int id) {
		return new PersonResource(uriInfo, request, id);
	}

	/**
	 * Request #6: GET /person/{id}/{measureType} should return the list of
	 * values (the history) of {measureType} (e.g. weight) for person identified
	 * by {id}
	 * 
	 * @param id
	 * @param md
	 * @return
	 */
	@GET
	@Path("{personId}/{measureType}")
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getMeasurement(@PathParam("personId") int id, @PathParam("measureType") String md) {
		MeasureHistoryBean mhb = null;
		try {
			System.out.println("Reading measurement from DB with idp: " + id + " for " + md);
			mhb = MeasureHistoryBean.getHistoryBeanFromMeasureList(MeasureHistory.getAllForMeasureType(id, md));
			if (mhb == null) {
				return Response.noContent().build();
			} else {
				return Response.ok().entity(mhb).build();
			}
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Request #7: GET /person/{id}/{measureType}/{mid} should return the value
	 * of {measureType} (e.g. weight) identified by {mid} for person identified
	 * by {id}
	 * 
	 * @param id
	 * @param md
	 * @param mid
	 * @return
	 */
	@GET
	@Path("{personId}/{measureType}/{mid}")
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getMeasurementById(@PathParam("personId") int id, @PathParam("measureType") String md,
			@PathParam("mid") int mid) {

		MeasureHistoryBean mhb = null;
		try {
			System.out.println("Reading measurement from DB with idp: " + id + " for " + md);
			MeasureHistory mh = MeasureHistory.getMeasureTypeById(id, md, mid);
			ArrayList<MeasureHistory> l = new ArrayList<MeasureHistory>();
			l.add(mh);
			mhb = MeasureHistoryBean.getHistoryBeanFromMeasureList(l);
			if (mhb == null) {
				return Response.noContent().build();
			} else {
				return Response.ok().entity(mhb).build();
			}
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Request #8: POST /person/{id}/{measureType} should save a new value for
	 * the {measureType} (e.g. weight) of person identified by {id} and archive
	 * the old value in the history
	 * 
	 * @param id
	 * @param md
	 * @param mb
	 * @return
	 * @throws IOException
	 */
	@POST
	@Path("{personId}/{measureType}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response newMeasure(@PathParam("personId") int id, @PathParam("measureType") String md, MeasureTypeBean mtb)
			throws IOException {

		MeasureHistoryBean mhb = null;
		try {
			System.out.println("Creating new measure...");

			Person p = Person.getPersonById(id);
			if (null == p) {
				p = new Person();
				p.setIdPerson(id);
				p = Person.insertPerson(p);
			}
			MeasureDefinition m = MeasureDefinition.getMeasureDefinitionByName(md);
			if (null == m) {
				m = new MeasureDefinition();
				m.setMeasureName(md);
				m = MeasureDefinition.insertMeasureDefinition(m);
			}

			MeasureHistory mh = new MeasureHistory();
			mh.setCreated(new Date());
			mh.setMeasureDefinition(m);
			mh.setPerson(p);
			mh.setValue(String.valueOf(mtb.getValue()));

			mh = MeasureHistory.insertMeasure(mh);
			if (mh != null) {
				mhb = new MeasureHistoryBean();
				mhb.setMeasure(MeasureHistory.getBeanAllForMeasureType(id, md));
			}

			if (mhb == null) {
				return Response.noContent().build();
			} else {
				return Response.ok().entity(mhb).build();
			}
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

}
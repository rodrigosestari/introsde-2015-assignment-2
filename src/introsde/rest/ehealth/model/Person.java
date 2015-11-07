package introsde.rest.ehealth.model;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.rest.ehealth.bean.HealthProfile;
import introsde.rest.ehealth.bean.MeasureTypeBean;
import introsde.rest.ehealth.bean.PersonBean;
import introsde.rest.ehealth.dao.LifeCoachDao;
/**
 * The persistent class for the "Person" database table.
 * 
 */
@Entity // indicates that this class is an entity to persist in DB
@Table(name = "Person") // to whole table must be persisted
@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p ORDER BY p.idPerson DESC ")
@XmlRootElement
public class Person implements Serializable {

	private static final long serialVersionUID = -7439379398075877771L;
	@Id // defines this attributed as the one that identifies the entity
	@GeneratedValue(generator = "sqlite_person")
	@TableGenerator(name = "sqlite_person", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Person")
	@Column(name = "idPerson")
	private int idPerson;
	@Column(name = "lastname")
	private String lastname;
	@Column(name = "name")
	private String name;

	@Temporal(TemporalType.DATE) // defines the precision of the date attribute
	@Column(name = "birthdate")
	private Date birthdate;

	public int getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthdate() {

		return birthdate;
	}

	public void setBirthdate(Date birthdate) {

		this.birthdate = birthdate;
	}

	/**
	 * Get person thorugh id person
	 * 
	 * @param personId
	 *            id person
	 * @return object Person
	 */
	public static Person getPersonById(int personId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		Person p = em.find(Person.class, personId);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	/**
	 * Get person through id person
	 * 
	 * @param personId
	 *            id person
	 * @return object PersonBean
	 */
	public static PersonBean getPersonBeanById(int personId) {
		PersonBean pb = null;
		try {
			Person p = getPersonById(personId);
			if (p != null) {
				pb = new PersonBean();
				pb.setBirthdate(dateToString(p.getBirthdate()));
				pb.setFirstname(p.getName());
				pb.setLastname(p.getLastname());
				pb.setHealthprofile(HealthProfile
						.getHealthProfileFromMeasureList(MeasureHistory.getHealthMeasureHistoryOldPerson(personId)));

				pb.setIdPerson(p.getIdPerson());
			}
		} catch (Exception e) {
			pb = null;
		}

		return pb;

	}

	/**
	 * get all person
	 * 
	 * @return list of Person
	 */
	public static List<Person> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	/**
	 * Get all person from DB
	 * 
	 * @param lastMeasure
	 *            get only the last measure
	 * @return list of PersonBean
	 */
	public static List<PersonBean> getAllBean(boolean lastMeasure) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
		ArrayList<PersonBean> pbl = null;
		if ((null != list) && (list.size() > 0)) {
			pbl = new ArrayList<PersonBean>();
			for (Person p : list) {
				try {
					PersonBean pb = new PersonBean();
					pb.setBirthdate(dateToString(p.getBirthdate()));
					pb.setFirstname(p.getName());
					pb.setLastname(p.getLastname());
					if (lastMeasure) {
						pb.setHealthprofile(HealthProfile.getHealthProfileFromMeasureList(
								MeasureHistory.getHealthMeasureHistoryOldPerson(p.getIdPerson())));
					} else {
						pb.setHealthprofile(HealthProfile.getHealthProfileFromMeasure(
								MeasureHistory.getHealthMeasureHistoryById(p.getIdPerson())));
					}
					pb.setIdPerson(p.getIdPerson());
					pbl.add(pb);
				} catch (Exception e) {
				}

			}
		}
		LifeCoachDao.instance.closeConnections(em);
		return pbl;
	}

	/**
	 * Insert a Person in DB
	 * 
	 * @param p
	 *            Object Person to insert
	 * @return object Person inserted
	 */
	public static Person insertPerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			em.persist(p);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}

		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	/**
	 * Insert a PersonBean in DB
	 * 
	 * @param pb
	 *            Object PersonBean to insert
	 * @return object PersonBean inserted
	 */
	public static PersonBean insertPersonBean(PersonBean pb) {
		Person p = new Person();

		p.setBirthdate(stringToDate(pb.getBirthdate()));
		p.setLastname(pb.getLastname());
		p.setName(pb.getFirstname());
		p = insertPerson(p);
		pb.setIdPerson(p.getIdPerson());
		try {
			HealthProfile hp = pb.getHealthprofile();
			if ((null != hp) && (null != hp.getMeasure()) && (hp.getMeasure().size() > 0)) {
				for (MeasureTypeBean mb : hp.getMeasure()) {

					MeasureHistory m = new MeasureHistory();
					m.setPerson(p);

					MeasureDefinition md = MeasureDefinition.getMeasureDefinitionByName(mb.getMeasure());
					if (md == null) {
						md = new MeasureDefinition();
						md.setMeasureName(mb.getMeasure());
						md = MeasureDefinition.insertMeasureDefinition(md);
					}
					m.setMeasureDefinition(md);

					m.setCreated(new Date());
					m.setValue(String.valueOf(mb.getValue()));
					MeasureHistory.insertHealthMeasureHistory(m);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pb;

	}

	/**
	 * Update a PersonBean in DB
	 * 
	 * @param p
	 *            object PersonBean
	 * @return object PersonBean updated
	 */
	public static Person updatePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			p = em.merge(p);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	/**
	 * Update a PersonBean in DB
	 * 
	 * @param pb
	 *            object PersonBean
	 * @return object PersonBean updated
	 */
	public static Person updatePerson(PersonBean pb) {

		Person p = new Person();

		p.setBirthdate(stringToDate(pb.getBirthdate()));
		p.setIdPerson(pb.getIdPerson());
		p.setLastname(pb.getLastname());
		p.setName(pb.getFirstname());
		return updatePerson(p);
	}

	/**
	 * Delete Person in DB
	 * 
	 * @param p
	 *            object Person
	 */
	public static void removePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			p = em.merge(p);
			em.remove(p);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}

		LifeCoachDao.instance.closeConnections(em);
	}

	/**
	 * Trasform String Date in object Data
	 * 
	 * @param data
	 *            a object Date
	 * @return a String data yyyy-MM-dd
	 */
	public static Date stringToDate(String data) {
		Date dataresult = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			dataresult = sdf.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dataresult;
	}

	/**
	 * Trasform object Data in String Date
	 * 
	 * @param data
	 *            a object Date
	 * @return a String data yyyy-MM-dd
	 */
	public static String dateToString(Date data) {
		String dataresult = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			dataresult = sdf.format(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataresult;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idPerson;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (idPerson != other.idPerson)
			return false;
		return true;
	}



}
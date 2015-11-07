package introsde.rest.ehealth.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.rest.ehealth.bean.MeasureBean;
import introsde.rest.ehealth.dao.LifeCoachDao;

/**
 * The persistent class for the "HealthMeasureHistory" database table.
 * 
 */
@Entity
@Table(name = "MeasureHistory")

@NamedQueries({ @NamedQuery(name = "MeasureHistory.findAll", query = "SELECT h FROM MeasureHistory h"),
		@NamedQuery(name = "MeasureHistory.findPerson", query = "SELECT h FROM MeasureHistory h WHERE h.person.idPerson = :id "),
		@NamedQuery(name = "MeasureHistory.findOldMeasurePerson", query = "SELECT h FROM MeasureHistory h WHERE h.person.idPerson = :id GROUP BY h.measureDefinition, h.person ORDER BY h.idMeasureHistory DESC"),
		@NamedQuery(name = "MeasureHistory.findPersonTypeID", query = "SELECT h FROM MeasureHistory h WHERE h.person.idPerson = :id and h.measureDefinition.measureName = :md and h.idMeasureHistory = :idhm"),
		@NamedQuery(name = "MeasureHistory.findPersonDefinition", query = "SELECT h FROM MeasureHistory h WHERE h.person.idPerson = :id and h.measureDefinition.measureName = :md") })
@XmlRootElement
public class MeasureHistory implements Serializable {
	

	private static final long serialVersionUID = 3691697186849968325L;

	@Id
	@GeneratedValue(generator = "sqlite_mhistory")
	@TableGenerator(name = "sqlite_mhistory", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "MeasureHistory")
	@Column(name = "idMeasureHistory")
	private int idMeasureHistory;

	@Temporal(TemporalType.DATE)
	@Column(name = "created")
	private Date created;

	@Column(name = "value")
	private String value;

	@ManyToOne
	@JoinColumn(name = "idMeasureDef", referencedColumnName = "idMeasureDef")
	private MeasureDefinition measureDefinition;

	// notice that we haven't included a reference to the history in Person
	// this means that we don't have to make this attribute XmlTransient
	@ManyToOne
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
	private Person person;

	public int getIdMeasureHistory() {
		return idMeasureHistory;
	}

	public void setIdMeasureHistory(int idMeasureHistory) {
		this.idMeasureHistory = idMeasureHistory;
	}

	public Date getCreated() {

		return created;
	}

	public void setCreated(Date created) {

		this.created = created;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MeasureDefinition getMeasureDefinition() {
		return measureDefinition;
	}

	public void setMeasureDefinition(MeasureDefinition measureDefinition) {
		this.measureDefinition = measureDefinition;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * get measure by id
	 * 
	 * @param id
	 *            id measure
	 * @return object MeasureHistory
	 */
	public static MeasureHistory getHealthMeasureHistoryById(int id) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		MeasureHistory p = em.find(MeasureHistory.class, id);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	/**
	 * It takes latest measures by type from person
	 * 
	 * @param idPerson
	 *            id person
	 * @return list of measure
	 */
	public static List<MeasureHistory> getHealthMeasureHistoryOldPerson(int idPerson) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();

		List<MeasureHistory> pd = em.createNamedQuery("MeasureHistory.findOldMeasurePerson", MeasureHistory.class)
				.setParameter("id", idPerson).getResultList();

		LifeCoachDao.instance.closeConnections(em);
		return pd;
	}

	/**
	 * get all MeasureHistory
	 * 
	 * @return list of MeasureHistory
	 */
	public static List<MeasureHistory> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<MeasureHistory> list = em.createNamedQuery("HealthMeasureHistory.findAll", MeasureHistory.class)
				.getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	/**
	 * get all measure from person
	 * 
	 * @param idPerson
	 *            id person
	 * @return list of MeasureHistory
	 */
	public static List<MeasureHistory> getAllbyPerson(int idPerson) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<MeasureHistory> list = null;
		list = em.createNamedQuery("MeasureHistory.findPerson", MeasureHistory.class).setParameter("id", idPerson)
				.getResultList();

		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	/**
	 * insert a new MeasureHistory
	 * 
	 * @param p
	 *            object MeasureHistory to insert
	 * @return object MeasureHistory inserted
	 */
	public static MeasureHistory insertHealthMeasureHistory(MeasureHistory p) {
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
	 * update a measure
	 * 
	 * @param p
	 *            object MeasureHistory to update
	 * @return object MeasureHistory updated
	 */
	public static MeasureHistory updateHealthMeasureHistory(MeasureHistory p) {
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
	 * remove a measure
	 * 
	 * @param p
	 *            object MeasureHistory to remove
	 */
	public static void removeHealthMeasureHistory(MeasureHistory p) {
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
	 * get all MeasureBean from person and type
	 * 
	 * @param id
	 *            id person
	 * @param md
	 *            measure type
	 * @return list of MeasureBean
	 */
	public static List<MeasureHistory> getAllForMeasureType(int id, String md) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<MeasureHistory> list = em.createNamedQuery("MeasureHistory.findPersonDefinition", MeasureHistory.class)
				.setParameter("id", id).setParameter("md", md).getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;

	}

	/**
	 * get all MeasureBean from person and type
	 * 
	 * @param id
	 *            id person
	 * @param md
	 *            measure type
	 * @return list of MeasureBean
	 */
	public static List<MeasureBean> getBeanAllForMeasureType(int id, String md) {
		List<MeasureHistory> mhl = getAllForMeasureType(id, md);
		ArrayList<MeasureBean> mbl = new ArrayList<MeasureBean>();

		for (MeasureHistory mh : mhl) {
			MeasureBean mb = new MeasureBean();
			mb.setCreated(Person.dateToString(mh.getCreated()));
			mb.setMid(mh.getIdMeasureHistory());
			mb.setValue(Double.parseDouble(mh.getValue()));
			mbl.add(mb);
		}
		return mbl;

	}

	/**
	 * insert a new measure
	 * 
	 * @param m
	 *            object MeasureHistory to insert
	 * @return object MeasureHistory inserted
	 */
	public static MeasureHistory insertMeasure(MeasureHistory m) {

		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			em.persist(m);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}

		LifeCoachDao.instance.closeConnections(em);

		return m;

	}

	/**
	 * Get a measure from person, measure type and id measure
	 * 
	 * @param id
	 *            id person
	 * @param md
	 *            measure type
	 * @param idmh
	 *            id measure history
	 * @return
	 */
	public static MeasureHistory getMeasureTypeById(int id, String md, int idmh) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();

		MeasureHistory ret = em.createNamedQuery("MeasureHistory.findPersonTypeID", MeasureHistory.class)
				.setParameter("id", id).setParameter("md", md).setParameter("idhm", idmh).getSingleResult();
		LifeCoachDao.instance.closeConnections(em);
		return ret;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idMeasureHistory;
		result = prime * result + ((measureDefinition == null) ? 0 : measureDefinition.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
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
		MeasureHistory other = (MeasureHistory) obj;
		if (idMeasureHistory != other.idMeasureHistory)
			return false;
		if (measureDefinition == null) {
			if (other.measureDefinition != null)
				return false;
		} else if (!measureDefinition.equals(other.measureDefinition))
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		return true;
	}

}

package introsde.rest.ehealth.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.rest.ehealth.dao.LifeCoachDao;

/**
 * The persistent class for the "MeasureDefinition" database table.
 * 
 */
@Entity
@Table(name = "MeasureDefinition")
@NamedQueries({
		@NamedQuery(name = "MeasureDefinition.findbyName", query = "SELECT h FROM MeasureDefinition h WHERE h.measureName = :name"),
		@NamedQuery(name = "MeasureDefinition.findAll", query = "SELECT h FROM MeasureDefinition h") })
@XmlRootElement
public class MeasureDefinition implements Serializable {
	

	private static final long serialVersionUID = -7320798646552225232L;

	@Id
	@GeneratedValue(generator = "sqlite_measuredef")
	@TableGenerator(name = "sqlite_measuredef", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "MeasureDefinition")
	@Column(name = "idMeasureDef")
	private int idMeasureDef;

	@Column(name = "measureName")
	private String measureName;

	public int getIdMeasureDef() {
		return this.idMeasureDef;
	}

	public void setIdMeasureDef(int idMeasureDef) {
		this.idMeasureDef = idMeasureDef;
	}

	public String getMeasureName() {
		return this.measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	// database operations
	public static MeasureDefinition getMeasureDefinitionById(int personId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		MeasureDefinition p = em.find(MeasureDefinition.class, personId);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	/**
	 * get all meausere types
	 * 
	 * @return list of MeasureDefinition
	 */
	public static List<MeasureDefinition> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<MeasureDefinition> list = em.createNamedQuery("MeasureDefinition.findAll", MeasureDefinition.class)
				.getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	/**
	 * insert a new measure type
	 * 
	 * @param p
	 *            MeasureDefinition to insert
	 * @return MeasureDefinition inserted
	 */
	public static MeasureDefinition insertMeasureDefinition(MeasureDefinition p) {
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
	 * update a mesuare type
	 * 
	 * @param p
	 *            object MeasureDefinition to update
	 * @return object MeasureDefinition updated
	 */
	public static MeasureDefinition updateMeasureDefinition(MeasureDefinition p) {
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
	 * remove a measure type from db
	 * 
	 * @param p
	 *            object MeasureDefinition
	 */
	public static void removeMeasureDefinition(MeasureDefinition p) {
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
	 * Get a measure type from your name
	 * 
	 * @param name
	 *            measure name type
	 * @return object MeasureDefinition
	 */
	static public MeasureDefinition getMeasureDefinitionByName(String name) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();

		MeasureDefinition me = em.createNamedQuery("MeasureDefinition.findbyName", MeasureDefinition.class)
				.setParameter("name", name).getSingleResult();

		LifeCoachDao.instance.closeConnections(em);
		return me;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idMeasureDef;
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
		MeasureDefinition other = (MeasureDefinition) obj;
		if (idMeasureDef != other.idMeasureDef)
			return false;
		return true;
	}


	
}

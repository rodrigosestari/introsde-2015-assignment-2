package introsde.rest.ehealth.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "people")
public class PeopleBean implements Serializable {

	private static final long serialVersionUID = -6571361062455280397L;
	
	private List<PersonBean> person = new ArrayList<PersonBean>();

	@XmlElement(name = "person")
	public List<PersonBean> getPerson() {
		return person;
	}

	public void setPerson(List<PersonBean> person) {
		this.person = person;
	}

	public PeopleBean() {

	}

	@Override
	public String toString() {
		return "PeopleBean [person=" + person + "]";
	}


}

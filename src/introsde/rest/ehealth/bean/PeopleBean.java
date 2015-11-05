package introsde.rest.ehealth.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.rest.ehealth.model.PersonBean;

@XmlRootElement(name = "people")
public class PeopleBean {

	private List<PersonBean> person = new ArrayList<PersonBean>();

	@XmlElement(name = "person")
	public List<PersonBean> getMeasureType() {
		return person;
	}

	public void setMeasureType(List<PersonBean> person) {
		this.person = person;
	}

	public PeopleBean() {

	}


}

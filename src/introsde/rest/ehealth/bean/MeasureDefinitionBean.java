package introsde.rest.ehealth.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.rest.ehealth.model.MeasureDefinition;

@XmlRootElement(name = "measureTypes")
public class MeasureDefinitionBean {

	private List<String> measureType = new ArrayList<String>();

	@XmlElement(name = "measureType")
	public List<String> getMeasureType() {
		return measureType;
	}

	public void setMeasureType(List<String> measureType) {
		this.measureType = measureType;
	}

	public MeasureDefinitionBean() {

	}

	public static List<String> getAll() {
		List<String> mtl = null;
		List<MeasureDefinition> mdl = MeasureDefinition.getAll();
		if ((null != mdl) && (mdl.size() > 0)) {
			mtl = new ArrayList<String>();
			for (MeasureDefinition m : MeasureDefinition.getAll()) {
				mtl.add(m.getMeasureName());
			}
		}
		return mtl;
	}

}

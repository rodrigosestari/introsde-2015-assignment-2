package introsde.rest.ehealth.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "measureType")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "measure", "value" })
public class MeasureTypeBean implements Serializable {

	private static final long serialVersionUID = -5954240662398049240L;

	private String measure;

	private double value;

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "MeasureTypeBean [measure=" + measure + ", value=" + value + "]";
	}

}
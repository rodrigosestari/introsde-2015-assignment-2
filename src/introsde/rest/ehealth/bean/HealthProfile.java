package introsde.rest.ehealth.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.rest.ehealth.model.MeasureHistory;

@XmlRootElement
public class HealthProfile implements Serializable {


	private static final long serialVersionUID = -7294911323864810080L;
	private List<MeasureTypeBean> measure;

	public HealthProfile() {
	}

	@XmlElement(name = "measureType")
	public List<MeasureTypeBean> getMeasure() {
		return measure;
	}

	public void setMeasure(List<MeasureTypeBean> measure) {
		this.measure = measure;
	}

	/**
	 * get a MeasureHistory and put into
	 * a HealthProfile.
	 * @param measure
	 * List of MeasureHistory entity
	 * @return
	 * a HealthProfile structure
	 * 
	 */
	public static HealthProfile getHealthProfileFromMeasure(MeasureHistory measure) {
		ArrayList<MeasureHistory> m = new ArrayList<MeasureHistory>();
		m.add(measure);
		return getHealthProfileFromMeasureList(m);
	}

	/**
	 * get a List of MeasureHistory and put into
	 * a HealthProfile.
	 * 
	 * @param measure
	 * List of MeasureHistory entity
	 * @return
	 *  a HealthProfile structure
	 */
	public static HealthProfile getHealthProfileFromMeasureList(List<MeasureHistory> measure) {
		HealthProfile hp = null;
		List<MeasureTypeBean> lmb = new ArrayList<MeasureTypeBean>();

		if ((null != measure) && (measure.size() > 0)) {
			hp = new HealthProfile();
			for (MeasureHistory mh : measure) {
				MeasureTypeBean mb = new MeasureTypeBean();
				mb.setMeasure(mh.getMeasureDefinition().getMeasureName());
				mb.setValue(Double.parseDouble(mh.getValue()));
				lmb.add(mb);
			}
			hp.setMeasure(lmb);
		}
		return hp;
	}

	@Override
	public String toString() {
		return "HealthProfile [measure=" + measure + "]";
	}
	
	
}
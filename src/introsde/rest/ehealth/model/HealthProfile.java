package introsde.rest.ehealth.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.rest.ehealth.bean.MeasureTypeBean;

@XmlRootElement
public class HealthProfile implements Serializable {

	private static final long serialVersionUID = 558619596614320430L;
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

	public static HealthProfile getHealthProfileFromMeasure(MeasureHistory measure) {
		ArrayList<MeasureHistory> m = new ArrayList<MeasureHistory>();
		m.add(measure);
		return getHealthProfileFromMeasureList(m);
	}

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
}
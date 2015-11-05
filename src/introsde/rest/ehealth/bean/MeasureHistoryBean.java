package introsde.rest.ehealth.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.rest.ehealth.model.MeasureHistory;
import introsde.rest.ehealth.model.Person;

@XmlRootElement(name = "measureHistory")
public class MeasureHistoryBean implements Serializable {

	private static final long serialVersionUID = 558619596614320430L;
	private List<MeasureBean> measure;

	public MeasureHistoryBean() {
	}

	@XmlElement(name = "measure")
	public List<MeasureBean> getMeasure() {
		return measure;
	}

	public void setMeasure(List<MeasureBean> measure) {
		this.measure = measure;
	}

	public static MeasureHistoryBean getHistoryBeanFromMeasure(MeasureHistory measure) {
		ArrayList<MeasureHistory> m = new ArrayList<MeasureHistory>();
		m.add(measure);
		return getHistoryBeanFromMeasureList(m);
	}

	public static MeasureHistoryBean getHistoryBeanFromMeasureList(List<MeasureHistory> measure) {
		MeasureHistoryBean hp = null;
		List<MeasureBean> lmb = new ArrayList<MeasureBean>();

		if ((null != measure) && (measure.size() > 0)) {
			hp = new MeasureHistoryBean();
			for (MeasureHistory mh : measure) {
				MeasureBean mb = new MeasureBean();
				mb.setCreated(Person.dateToString(mh.getCreated()));
				mb.setMid(mh.getIdMeasureHistory());
				mb.setValue(Double.parseDouble(mh.getValue()));
				lmb.add(mb);
			}
			hp.setMeasure(lmb);
		}
		return hp;
	}
}
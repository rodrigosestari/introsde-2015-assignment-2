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


	private static final long serialVersionUID = 3521996173451216932L;
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

	/**
	 * get a list of MeasureHistory from MeasureHistory
	 * 
	 * @param measure
	 *            object MeasureHistory
	 * @return object MeasureHistoryBean
	 * 
	 */
	public static MeasureHistoryBean getHistoryBeanFromMeasure(MeasureHistory measure) {
		ArrayList<MeasureHistory> m = new ArrayList<MeasureHistory>();
		m.add(measure);
		return getHistoryBeanFromMeasureList(m);
	}

	/**
	 * trasnform a list of MeasureHistory into MeasureHistoryBean
	 * 
	 * @param measure
	 *            list of MeasureHistory
	 * @return object MeasureHistoryBean
	 * 
	 */
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

	@Override
	public String toString() {
		return "MeasureHistoryBean [measure=" + measure + "]";
	}

}
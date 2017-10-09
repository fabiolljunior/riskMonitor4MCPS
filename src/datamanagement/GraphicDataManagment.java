package datamanagement;

import java.util.HashMap;
import java.util.Map;

import datareader.DataCollectionUtil;
import observer.RiskObserver;
import riskAssessment.Risk;

public class GraphicDataManagment implements RiskObserver {
	
	private Map<Long, Risk> mapa = new HashMap<>();

	@Override
	public void notifyRiskChange(Risk newRisk, long currentTimeMilis) {
		System.out.println(newRisk.getValue());
		System.out.println(currentTimeMilis);
		
		this.mapa.put(currentTimeMilis, newRisk);
		DataCollectionUtil.getInstance().addData(newRisk, currentTimeMilis);
	}
}

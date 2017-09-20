package datamanagement;

import datareader.LineChartSample;
import javafx.application.Application;
import observer.RiskObserver;
import riskAssessment.Risk;

public class GraphicDataManagment implements RiskObserver {
	
	private boolean inited = false;

	@Override
	public void notifyRiskChange(Risk newRisk, long currentTimeMilis) {
		System.out.println(newRisk.getValue());
		System.out.println(currentTimeMilis);
		
		LineChartSample.addRisk(newRisk);
		if (!inited) {
			Application.launch(LineChartSample.class, null);
		}
		
	}
}

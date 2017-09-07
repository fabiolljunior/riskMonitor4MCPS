package observer;

import riskAssessment.Risk;

public interface RiskObserver {
	
	public void notifyRiskChange(Risk newRisk, long currentTimeMilis);

}

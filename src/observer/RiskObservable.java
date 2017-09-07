package observer;

import riskAssessment.Risk;

public interface RiskObservable {
	
	public void notifyRiskChange(Risk newRiskValue);
	
	public void registerListener(RiskObserver riskobserver);
	
	public void unregisterListener(RiskObserver riskobserver);

}

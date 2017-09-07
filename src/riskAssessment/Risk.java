package riskAssessment;

public enum Risk {
	
	LOWER_RISK_VALUE(0),
	LOW_ALERT_RISK_VALUE(4),
	ALERT_RISK_VALUE(9),
	CRITIC_RISK_VALUE(12),
	HIGHTEST_RISK_VALUE(25);
	
	private int finalValue;
	
	Risk(int value) {
		this.finalValue = value;
	}
	
	Risk() {
		this.finalValue = 0;
	}
	
	static Risk getRisk(RiskCriticalityLevel monitoringRisk) {
		if (RiskCriticalityLevel.Catastrophical.equals(monitoringRisk)) {
			return HIGHTEST_RISK_VALUE;
		} else if (RiskCriticalityLevel.Critical.equals(monitoringRisk)) {
			return CRITIC_RISK_VALUE;
		} else if (RiskCriticalityLevel.Serious.equals(monitoringRisk)) {
			return ALERT_RISK_VALUE;
		} else if (RiskCriticalityLevel.Minor.equals(monitoringRisk)) {
			return LOW_ALERT_RISK_VALUE;
		} else {
			return LOWER_RISK_VALUE;
		}
	}
	
	/**
	 * Main method to calculate the risk
	 * @param risk2 
	 * @param riskCapnometer 
	 * @return
	 */
	static Risk getRiskValue(RiskCriticalityLevel monitoringRiskCap, RiskCriticalityLevel monitoringRiskPO) {
		
		
		if (monitoringRiskPO.getValue() <= 2) {
			return Risk.LOW_ALERT_RISK_VALUE;
		} else if (monitoringRiskPO.getValue() == 3) {
			return Risk.ALERT_RISK_VALUE;
		} else if (monitoringRiskPO.getValue() == 4) {
			return Risk.CRITIC_RISK_VALUE;
		} else {
			return Risk.HIGHTEST_RISK_VALUE;
		}
	}

	public int getValue() {
		return finalValue;
	}

	public void setValue(int value) {
		this.finalValue = value;
	}

}

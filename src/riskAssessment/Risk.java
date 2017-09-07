package riskAssessment;

public class Risk {
	
	public static int LOWER_RISK_VALUE = 0;
	public static int LOW_ALERT_RISK_VALUE = 4;
	public static int ALERT_RISK_VALUE = 9;
	public static int CRITIC_RISK_VALUE = 12;
	public static int HIGHER_RISK_VALUE = 25;
	
	private Risk monitoringRiskPO = null;
	private Risk monitoringRiskCap = null;
	
	private int finalValue;
	
	public Risk(int value) {
		this.finalValue = value;
	}
	
	public Risk() {
		this.finalValue = 0;
	}
	
	/**
	 * Main method to calculate the risk
	 * @param risk2 
	 * @param riskCapnometer 
	 * @return
	 */
	public Risk calculateRisk(Risk monitoringRiskCap, Risk monitoringRiskPO) {
		
		if (monitoringRiskPO.getValue() <= 2) {
			return new Risk(Risk.LOW_ALERT_RISK_VALUE);
		} else if (monitoringRiskPO.getValue() == 3) {
			return new Risk(Risk.ALERT_RISK_VALUE);
		} else if (monitoringRiskPO.getValue() == 4) {
			return new Risk(Risk.CRITIC_RISK_VALUE);
		} else {
			return new Risk(Risk.HIGHER_RISK_VALUE);
		}
	}

	public int getValue() {
		return finalValue;
	}

	public void setValue(int value) {
		this.finalValue = value;
	}

	@Override
	public boolean equals(Object arg0) {
		return this.finalValue == ((Risk)arg0).getValue();
	}

	public Risk getMonitoringRiskPO() {
		return monitoringRiskPO;
	}

	public void setMonitoringRiskPO(Risk monitoringRiskPO) {
		this.monitoringRiskPO = monitoringRiskPO;
	}

	public Risk getMonitoringRiskCap() {
		return monitoringRiskCap;
	}

	public void setMonitoringRiskCap(Risk monitoringRiskCap) {
		this.monitoringRiskCap = monitoringRiskCap;
	}
	
	

}

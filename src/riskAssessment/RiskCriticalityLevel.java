package riskAssessment;

import deviceManager.GenericRespirationMonitor;

public enum RiskCriticalityLevel {
	
	Negligible("Negligible",1),
	Minor("Minor",2),
	Serious("Serious",3),
	Critical("Critical",4),
	Catastrophical("Catastrophic",5);
	
	private String name;
	private int value;
	
	RiskCriticalityLevel(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
	
	static RiskCriticalityLevel getCriticalityLevelofSPO2(double spo2) {
		if (spo2 <= 75) {
			return RiskCriticalityLevel.Catastrophical;
		} else if (spo2 > 75 && spo2 <= 85) {
			return RiskCriticalityLevel.Critical;
		} else if (spo2 > 85 && spo2 <= 90) {
			return RiskCriticalityLevel.Serious;
		} else if (spo2 > 90 && spo2 <= 93) {
			return RiskCriticalityLevel.Minor;
		} 
		return RiskCriticalityLevel.Negligible;
	}
	
	static RiskCriticalityLevel getCriticalityLevelofHR(int hr) {
		if (hr < 40 && hr > 140) {
			return RiskCriticalityLevel.Catastrophical;
		} else if ((hr >= 40 && hr <= 45) || (hr > 130 && hr <= 140)) {
			return RiskCriticalityLevel.Critical;
		} else if ((hr > 45 && hr <= 51) || (hr > 120 && hr <= 130)) {
			return RiskCriticalityLevel.Serious;
		} else if ((hr >= 52 && hr <= 55) || (hr > 110 && hr <= 120)) {
			return RiskCriticalityLevel.Minor;
		} 
		return RiskCriticalityLevel.Negligible;
	}

	public static RiskCriticalityLevel getCriticalityLevelRR(int respRate) {
		if (respRate != GenericRespirationMonitor.INVALID_VALUE && respRate <= 1) {
			return RiskCriticalityLevel.Catastrophical;
		} else if (respRate > 1 && respRate <= 6) {
			return RiskCriticalityLevel.Critical;
		} else if (respRate > 6 && respRate <= 8) {
			return RiskCriticalityLevel.Serious;
		} else if (respRate > 8 && respRate <= 12) {
			return RiskCriticalityLevel.Minor;
		} 
		return RiskCriticalityLevel.Negligible;
	}

	public static RiskCriticalityLevel getCriticalityLevelETCO2(double etco2) {
		if (etco2 != GenericRespirationMonitor.INVALID_VALUE && (etco2 > 55 || etco2 <= 25)) {
			return RiskCriticalityLevel.Catastrophical;
		} else if ((etco2 <= 55 && etco2 > 50) || (etco2 <= 30 && etco2 > 25 )) {
			return RiskCriticalityLevel.Critical;
		} else if ((etco2 <= 50 && etco2 > 45) || (etco2 <= 35 && etco2 > 30 ) ) {
			return RiskCriticalityLevel.Serious;
		} else if (etco2 > 30 && etco2 <= 45) {
			return RiskCriticalityLevel.Minor;
		} 
		return RiskCriticalityLevel.Negligible;
	}

}

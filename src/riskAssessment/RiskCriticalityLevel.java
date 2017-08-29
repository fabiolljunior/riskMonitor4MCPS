package riskAssessment;

public enum RiskCriticalityLevel {
	
	Negligible("Negligible",1),
	Minor("Minor",2),
	Serious("Serious",3),
	Critical("Critical",4),
	Catastrophical("Catastrophical",5);
	
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
	
	static RiskCriticalityLevel getCriticalityLevelofHR(double hr) {
		if (hr <= 75) {
			return RiskCriticalityLevel.Catastrophical;
		} else if (hr > 75 && hr <= 85) {
			return RiskCriticalityLevel.Critical;
		} else if (hr > 85 && hr <= 90) {
			return RiskCriticalityLevel.Serious;
		} else if (hr > 90 && hr <= 93) {
			return RiskCriticalityLevel.Minor;
		} 
		return RiskCriticalityLevel.Negligible;
	}

}

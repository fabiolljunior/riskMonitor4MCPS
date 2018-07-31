package riskAssessment;

public class EtCO2CriticalityTable {
	
	private static EtCO2CriticalityTable instance = null;
	
	public static EtCO2CriticalityTable getInstance() {
		if (instance == null) {
			instance = new EtCO2CriticalityTable();
		}
		return instance;
	}
	
	private EtCO2CriticalityTable() {	}
	
	public RiskCriticalityLevel getCriticalityLevelEtCO2(double etco2) {
		if (etco2 <= 20 || etco2 > 60) {
			return RiskCriticalityLevel.Catastrophical;
		} else if ( (etco2 >= 55 && etco2 < 60) || (etco2 > 20 && etco2 <= 25) ) {
			return RiskCriticalityLevel.Critical;
		} else if ( (etco2 >= 50 && etco2 < 55) || (etco2 > 25 && etco2 < 31) ) {
			return RiskCriticalityLevel.Serious;
		} else if ((etco2 >= 46 && etco2 < 50) || (etco2 >= 31 && etco2 < 35)) {
			return RiskCriticalityLevel.Minor;
		} else if (etco2 >= 35 && etco2 < 46) {
			return RiskCriticalityLevel.Negligible;
		}
		return RiskCriticalityLevel.Negligible;
	}

}

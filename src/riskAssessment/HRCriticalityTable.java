package riskAssessment;

public class HRCriticalityTable {
	
private static HRCriticalityTable instance = null;
	
	public static HRCriticalityTable getInstance() {
		if (instance == null) {
			instance = new HRCriticalityTable();
		}
		return instance;
	}
	
	private HRCriticalityTable() {	}
	
	public RiskCriticalityLevel getCriticalityLevelofHR(int heartRate) {
		if (heartRate < 35 || heartRate > 170) {
			return RiskCriticalityLevel.Catastrophical;
		} else if ( (heartRate >= 35 && heartRate < 40) || (heartRate > 130 && heartRate <= 170) ) {
			return RiskCriticalityLevel.Critical;
		} else if ( (heartRate >= 40 && heartRate < 45) || (heartRate > 110 && heartRate <= 130) ) {
			return RiskCriticalityLevel.Serious;
		} else if ((heartRate >= 45 && heartRate < 50) || (heartRate > 100 && heartRate <= 110)) {
			return RiskCriticalityLevel.Minor;
		} else if ((heartRate >= 50 && heartRate <= 55) || (heartRate > 90 && heartRate <= 100)) {
			return RiskCriticalityLevel.Negligible;
		}
		return RiskCriticalityLevel.Negligible;
	}
}

package riskAssessment;

public class RRCriticalityTable {
	
	private static RRCriticalityTable instance = null;
	
	public static RRCriticalityTable getInstance() {
		if (instance == null) {
			instance = new RRCriticalityTable();
		}
		return instance;
	}
	
	private RRCriticalityTable() {
		
	}
	
	public RiskCriticalityLevel getCriticalityLevelRR(double respirationRate) {
		if (respirationRate < 6) {
			return RiskCriticalityLevel.Catastrophical;
		} else if ( (respirationRate >= 6 && respirationRate <= 7) || (respirationRate >= 26 && respirationRate <= 35) ) {
			return RiskCriticalityLevel.Critical;
		} else if ( (respirationRate >= 8 && respirationRate <= 9) || (respirationRate >= 22 && respirationRate < 25) ) {
			return RiskCriticalityLevel.Serious;
		} else if ( (respirationRate == 10) || (respirationRate >= 20 && respirationRate < 22) ) {
			return RiskCriticalityLevel.Minor;
		} else if ( (respirationRate == 11) || (respirationRate == 19) ) {
			return RiskCriticalityLevel.Negligible;
		}
		return RiskCriticalityLevel.Negligible;
	}

}

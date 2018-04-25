package riskAssessment;

/**
 * TODO: vai ser usado depois
 * @author FábioLuiz
 *
 */
public class PulseOximeterCriticalityTable {
	
	private static PulseOximeterCriticalityTable instance = null;
	
	public static PulseOximeterCriticalityTable getInstance() {
		if (instance == null) {
			instance = new PulseOximeterCriticalityTable();
		}
		return instance;
	}
	
	private PulseOximeterCriticalityTable() {
		
	}
	
	public double getCriticalityLevelofHR(int heartRate) {
		return 0;
	}
	
	public RiskCriticalityLevel getCriticalityLevelofSPO2(double spo2) {
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

}

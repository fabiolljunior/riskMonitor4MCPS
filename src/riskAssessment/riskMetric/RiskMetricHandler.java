package riskAssessment.riskMetric;

public class RiskMetricHandler {
	
	private static RiskMetricHandler singleton = null;
	
	private RiskMetricHandler(){}
	
	public static RiskMetricHandler getInstance() {
		if (singleton == null) {
			singleton = new RiskMetricHandler();
		}
		return singleton;
	}
	
	public BBNHandlerIF getRiskMetricConfig1() {
		return BBNGeNIeHandler_Config1.getInstance();
	}
	
	public BBNHandlerIF getRiskMetricConfig2() {
		return BBNGeNIeHandler_Config2.getInstance();
	}
	
	public BBNHandlerIF getRiskMetricConfig3() {
		return BBNGeNIeHandler_Config3.getInstance();
	}

}

package riskAssessment.riskMetric;

import riskAssessment.Risk;
import smile.Network;

/**
 * Classe manipuladora de risco BBN GeNIe xsdl
 * 
 * @author FábioLuiz
 *
 */
public class BBNGeNIeHandler_Config3 implements BBNHandlerIF {

	private static final String ETCO2_SENSORS_VALUE_NODE_NAME = "EtCO2_Sensors_Value";
	private static final String RR_SENSORS_VALUE_NODE_NAME = "RR_Sensors_Value";
	private static final String SPO2_SENSOR_VALUE_NODENAME = "SpO2_sensor_value";
	private static final String HR_SENSOR_VALUE_NODE_NAME = "HR_Sensor_Value";
	private static final String OVP_NODE_NAME = "Overall_patient_context";
	private static final String JOINT_FUNCTION_FUNTION_PEFORMANCE_NODENAME = "Joint_Function_funtion_peformance";
	private static final String ET_CO2_SAFETY_GUARANTEES_NODENAME = "EtCO2_Safety_Guarantees";
	private static final String RR_SAFETY_GUARANTEES_NODENAME = "RR_Safety_Guarantees";
	private static final String SPO2_SAFETY_GUARANTEES_NODENAME = "SPO2_Safety_Guarantees";
	private static final String HR_SAFETY_GUARANTEES_NODENAME = "HR_Safety_Guarantees";
	private static final String O2_SUPPLEMENT_NODE_NAME = "O2_Supplement";
	private static final String TAKING_OTHER_MEDICATIONS_NODE_NAME = "Taking_other_medications";
	private static final String LOCKOUT_INTERVAL_NODE_NAME = "lockout_interval";
	private static final String SYS_ACTUATION_PARAM_MEDIUM = "Medium";
	private static final String DRUG_CRITICALITY_LOW = "Low";
	private static final String DRUG_AMOUNT_CRITIC_NODE_NAME = "x_DA__Amount";
	private static BBNGeNIeHandler_Config3 singleton = null;
	// TODO:refactor
	private static String filePath = "resources/RiskMonitoringNetMCPS_v3.xdsl";
	private Network net = null;

	private BBNGeNIeHandler_Config3() {
		net = new Network();

		net.readFile(filePath);
		System.out.println("BBNGeNIeHandler_Config3 leu...");

		net.updateBeliefs();
		System.out.println("BBNGeNIeHandler_Config3 atualizou...");

		// TODO: set all the current evidences
		updateScenarioInitial();
		System.out.println("BBNGeNIeHandler_Config1 Initial Scenario...");
//		updateConfigurationSILA();
		updateConfigurationSILC();
		System.out.println("BBNGeNIeHandler_Config3 configurou...");
	}
	
	@Override
	public String toString() {
		return "BBNGeNIeHandler_Config3";
	}


	private void updateScenarioInitial() {
		net.setEvidence(DRUG_AMOUNT_CRITIC_NODE_NAME, DRUG_CRITICALITY_LOW);
		net.setEvidence(LOCKOUT_INTERVAL_NODE_NAME, SYS_ACTUATION_PARAM_MEDIUM);
		net.setEvidence(TAKING_OTHER_MEDICATIONS_NODE_NAME, "No");
		net.setEvidence(O2_SUPPLEMENT_NODE_NAME, "None");
	}

	private void updateConfigurationSILA() {
		net.setEvidence(HR_SAFETY_GUARANTEES_NODENAME, "Guarantee_A");
		net.setEvidence(SPO2_SAFETY_GUARANTEES_NODENAME, "Guarantee_A");
		net.setEvidence(RR_SAFETY_GUARANTEES_NODENAME, "OFF");
		net.setEvidence(ET_CO2_SAFETY_GUARANTEES_NODENAME, "OFF");
		net.setEvidence(JOINT_FUNCTION_FUNTION_PEFORMANCE_NODENAME, "Guarantee_A");
	}
	
	private void updateConfigurationSILC() {
		net.setEvidence(HR_SAFETY_GUARANTEES_NODENAME, "Guarantee_C");
		net.setEvidence(SPO2_SAFETY_GUARANTEES_NODENAME, "Guarantee_C");
		net.setEvidence(RR_SAFETY_GUARANTEES_NODENAME, "Guarantee_C");
		net.setEvidence(ET_CO2_SAFETY_GUARANTEES_NODENAME, "Guarantee_C");
		net.setEvidence(JOINT_FUNCTION_FUNTION_PEFORMANCE_NODENAME, "Guarantee_C");
	}

	/* (non-Javadoc)
	 * @see riskAssessment.BBNGenericHandler#updateHR(int)
	 */
	@Override
	public Risk updateHR(int value) {
//		RiskCriticalityLevel criticLevel = HRCriticalityTable.getInstance().getCriticalityLevelofHR(value);
//		System.out.println("BBNGeNIeHandler.updateHR() - vai atualizar: " + value + " critic level: " + criticLevel.getName());
		net.updateBeliefs();
		net.setNodeEquation(HR_SENSOR_VALUE_NODE_NAME, HR_SENSOR_VALUE_NODE_NAME + " = " + value);
		net.updateBeliefs();
		return updateRisk();
		
	}
	
	/* (non-Javadoc)
	 * @see riskAssessment.BBNGenericHandler#updateSpO2(double)
	 */
	@Override
	public Risk updateSpO2(double value) {
//		RiskCriticalityLevel criticLevel = PulseOximeterCriticalityTable.getInstance().getCriticalityLevelofSPO2(value);
//		System.out.println("BBNGeNIeHandler.updateSPO2() - vai atualizar: " + value + " critic level: " + criticLevel.getName());
		net.updateBeliefs();
		net.setNodeEquation(SPO2_SENSOR_VALUE_NODENAME, SPO2_SENSOR_VALUE_NODENAME + " = " + value);
		net.updateBeliefs();
		return updateRisk();
		
	}
	
	/* (non-Javadoc)
	 * @see riskAssessment.BBNGenericHandler#updateRespirationRate(int)
	 */
	@Override
	public Risk updateRespirationRate(int value) {
//		RiskCriticalityLevel criticLevel = RRCriticalityTable.getInstance().getCriticalityLevelRR(value);
//		System.out.println("BBNGeNIeHandler.updateRR() - vai atualizar: " + value + " critic level: " + criticLevel.getName());
		net.updateBeliefs();
		net.setNodeEquation(RR_SENSORS_VALUE_NODE_NAME, RR_SENSORS_VALUE_NODE_NAME + " = " + value);
		net.updateBeliefs();
		return updateRisk();
		
	}
	
	/* (non-Javadoc)
	 * @see riskAssessment.BBNGenericHandler#updateEtCO2(double)
	 */
	@Override
	public Risk updateEtCO2(double value) {
//		RiskCriticalityLevel criticLevel = EtCO2CriticalityTable.getInstance().getCriticalityLevelEtCO2(value);
//		System.out.println("BBNGeNIeHandler.updateEtCO2() - vai atualizar: " + value + " critic level: " + criticLevel.getName());
		net.updateBeliefs();
		net.setNodeEquation(ETCO2_SENSORS_VALUE_NODE_NAME, ETCO2_SENSORS_VALUE_NODE_NAME +" = " + value);
		net.updateBeliefs();
		return updateRisk();
		
	}

	/* (non-Javadoc)
	 * @see riskAssessment.BBNGenericHandler#updateRisk()
	 */
	@Override
	public Risk updateRisk() {
		net.updateBeliefs();
		String[] aForecastOutcomeIds = net.getOutcomeIds(OVP_NODE_NAME);
		int outcomeIndex;
		double currentRisk = 0;
		for (outcomeIndex = 0; outcomeIndex < aForecastOutcomeIds.length; outcomeIndex++) {
			if ("Catastrophic".equals(aForecastOutcomeIds[outcomeIndex])) {
				double[] aValues = net.getNodeValue(OVP_NODE_NAME);
				double catastrophicOccurrences = aValues[outcomeIndex];
//				System.out.println("BBNGeNIeHandler.updateRisk() - Catastrophic: " + catastrophicOccurrences);
				currentRisk += catastrophicOccurrences*105;
				
			} else if ("Critical".equals(aForecastOutcomeIds[outcomeIndex])) {
				double[] aValues = net.getNodeValue(OVP_NODE_NAME);
				double criticOccurrences = aValues[outcomeIndex];
//				System.out.println("BBNGeNIeHandler.updateRisk() - Critic: " + criticOccurrences);
				currentRisk += criticOccurrences*85;
				
			} else if ("Serious".equals(aForecastOutcomeIds[outcomeIndex])) {
				double[] aValues = net.getNodeValue(OVP_NODE_NAME);
				double seriousOccurrences = aValues[outcomeIndex];
//				System.out.println("BBNGeNIeHandler.updateRisk() - Serious: " + seriousOccurrences);
				currentRisk += seriousOccurrences*65;
			} else if ("Minor".equals(aForecastOutcomeIds[outcomeIndex])) {
				double[] aValues = net.getNodeValue(OVP_NODE_NAME);
				double minorOccurrences = aValues[outcomeIndex];
//				System.out.println("BBNGeNIeHandler.updateRisk() - minor: " + minorOccurrences);
				currentRisk += minorOccurrences*45;
			} else if ("Negligible".equals(aForecastOutcomeIds[outcomeIndex])) {
				double[] aValues = net.getNodeValue(OVP_NODE_NAME);
				double negligibleOccurrences = aValues[outcomeIndex];
//				System.out.println("BBNGeNIeHandler.updateRisk() - Negligible: " + negligibleOccurrences);
				currentRisk += negligibleOccurrences*25;
			}
		}
		
		Risk current = Risk.LOWER_RISK_VALUE;
		
		if (currentRisk >= 90) {
			current = Risk.HIGHTEST_RISK_VALUE;
		} else if (currentRisk < 85 && currentRisk >= 65) {
			current = Risk.CRITIC_RISK_VALUE;
		} else if (currentRisk < 65 && currentRisk >= 45) {
			current = Risk.ALERT_RISK_VALUE;
		} else if (currentRisk < 45 && currentRisk >= 27) {
			current = Risk.LOW_ALERT_RISK_VALUE;
		} else if (currentRisk < 27 && currentRisk >= 0) {
			current = Risk.LOWER_RISK_VALUE; 
		}
		current.setRiskvalue(currentRisk);
//		System.out.println("BBNGeNIeHandler.updateRisk() novo valor do risco: " + current.getName());
//		System.out.println("BBNGeNIeHandler.updateRisk() currentRisk: " + currentRisk);
		return current;
	}

	public static BBNGeNIeHandler_Config3 getInstance() {
		if (singleton == null) {
			singleton = new BBNGeNIeHandler_Config3();
		}
		return singleton;
	}

}

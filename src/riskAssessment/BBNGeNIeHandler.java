package riskAssessment;

import smile.Network;

/**
 * Classe manipuladora de risco BBN GeNIe xsdl
 * 
 * @author FábioLuiz
 *
 */
public class BBNGeNIeHandler {

	private static final String ETCO2_SENSORS_VALUE_NODE_NAME = "EtCO2_sensors_value";
	private static final String RR_SENSORS_VALUE_NODE_NAME = "RR_sensors_value";
	private static final String SPO2_SENSOR_VALUE_NODENAME = "SPO2_sensor_value";
	private static final String HR_SENSOR_VALUE_NODE_NAME = "HR_sensor_Value";
	private static final String OVP_NODE_NAME = "Overall_patient_context";
	private static final String JOINT_FUNCTION_FUNTION_PEFORMANCE_NODENAME = "Joint_Function_funtion_peformance";
	private static final String ET_CO2_SAFETY_GUARANTEES_NODENAME = "EtCO2_Safety_Guarantees";
	private static final String RR_SAFETY_GUARANTEES_NODENAME = "RR_Safety_Guarantees";
	private static final String SPO2_SAFETY_GUARANTEES_NODENAME = "SPO2_Safety_Guarantees";
	private static final String CPT_SAFETY_GUARANTEES_NODENAME = "CPT_Safety_Guarantees";
	private static final String O2_SUPPLEMENT_NODE_NAME = "O2_supplement";
	private static final String TAKING_OTHER_MEDICATIONS_NODE_NAME = "Taking_other_medications";
	private static final String LOCKOUT_INTERVAL_NODE_NAME = "lockout_interval";
	private static final String SYS_ACTUATION_PARAM_MEDIUM = "Medium";
	private static final String DRUG_CRITICALITY_LOW = "Low";
	private static final String DRUG_AMOUNT_CRITIC_NODE_NAME = "x_DA__Amount";
	private static BBNGeNIeHandler singleton = null;
	// TODO:refactor
	private static String filePath = "resources/RiskMonitoringNetMCPS_v2.xdsl";
	private Network net = null;

	private BBNGeNIeHandler() {
		licenceSMILE();
		net = new Network();

		net.readFile(filePath);
		System.out.println("LoadBBNTest.main() - 1. leu...");

		net.updateBeliefs();
		System.out.println("LoadBBNTest.main() - 2. atualizou...");

		// TODO: set all the current evidences
		updateScenarioInitial();
//		updateConfigurationSILA();
		updateConfigurationSILC();
	}

	private void updateScenarioInitial() {
		net.setEvidence(DRUG_AMOUNT_CRITIC_NODE_NAME, DRUG_CRITICALITY_LOW);
		net.setEvidence(LOCKOUT_INTERVAL_NODE_NAME, SYS_ACTUATION_PARAM_MEDIUM);
		net.setEvidence(TAKING_OTHER_MEDICATIONS_NODE_NAME, "No");
		net.setEvidence(O2_SUPPLEMENT_NODE_NAME, "None");
	}

	private void updateConfigurationSILA() {
		net.setEvidence(CPT_SAFETY_GUARANTEES_NODENAME, "SIL_A");
		net.setEvidence(SPO2_SAFETY_GUARANTEES_NODENAME, "SIL_A");
		net.setEvidence(RR_SAFETY_GUARANTEES_NODENAME, "OFF");
		net.setEvidence(ET_CO2_SAFETY_GUARANTEES_NODENAME, "OFF");
		net.setEvidence(JOINT_FUNCTION_FUNTION_PEFORMANCE_NODENAME, "SIL_A");
	}
	
	private void updateConfigurationSILC() {
		net.setEvidence(CPT_SAFETY_GUARANTEES_NODENAME, "SIL_C");
		net.setEvidence(SPO2_SAFETY_GUARANTEES_NODENAME, "SIL_C");
		net.setEvidence(RR_SAFETY_GUARANTEES_NODENAME, "SIL_C");
		net.setEvidence(ET_CO2_SAFETY_GUARANTEES_NODENAME, "SIL_C");
		net.setEvidence(JOINT_FUNCTION_FUNTION_PEFORMANCE_NODENAME, "SIL_C");
	}

	public Risk updateHR(int value) {
		RiskCriticalityLevel criticLevel = HRCriticalityTable.getInstance().getCriticalityLevelofHR(value);
		System.out.println("BBNGeNIeHandler.updateHR() - vai atualizar: " + value + " critic level: " + criticLevel.getName());
		net.setEvidence(HR_SENSOR_VALUE_NODE_NAME, criticLevel.getName());
		net.updateBeliefs();
		return updateRisk();
		
	}
	
	public Risk updateSpO2(double value) {
		RiskCriticalityLevel criticLevel = PulseOximeterCriticalityTable.getInstance().getCriticalityLevelofSPO2(value);
		System.out.println("BBNGeNIeHandler.updateSPO2() - vai atualizar: " + value + " critic level: " + criticLevel.getName());
		net.setEvidence(SPO2_SENSOR_VALUE_NODENAME, criticLevel.getName());
		net.updateBeliefs();
		return updateRisk();
		
	}
	
	public Risk updateRespirationRate(int value) {
		RiskCriticalityLevel criticLevel = RRCriticalityTable.getInstance().getCriticalityLevelRR(value);
		System.out.println("BBNGeNIeHandler.updateRR() - vai atualizar: " + value + " critic level: " + criticLevel.getName());
		net.setEvidence(RR_SENSORS_VALUE_NODE_NAME, criticLevel.getName());
		net.updateBeliefs();
		return updateRisk();
		
	}
	
	public Risk updateEtCO2(double value) {
		RiskCriticalityLevel criticLevel = EtCO2CriticalityTable.getInstance().getCriticalityLevelEtCO2(value);
		System.out.println("BBNGeNIeHandler.updateEtCO2() - vai atualizar: " + value + " critic level: " + criticLevel.getName());
		net.setEvidence(ETCO2_SENSORS_VALUE_NODE_NAME, criticLevel.getName());
		net.updateBeliefs();
		return updateRisk();
		
	}

	public Risk updateRisk() {
		net.updateBeliefs();
		String[] aForecastOutcomeIds = net.getOutcomeIds(OVP_NODE_NAME);
		int outcomeIndex;
		double currentRisk = 0;
		for (outcomeIndex = 0; outcomeIndex < aForecastOutcomeIds.length; outcomeIndex++) {
			if ("Catastrophic".equals(aForecastOutcomeIds[outcomeIndex])) {
				double[] aValues = net.getNodeValue(OVP_NODE_NAME);
				double catastrophicOccurrences = aValues[outcomeIndex];
				System.out.println("BBNGeNIeHandler.updateRisk() - Catastrophic: " + catastrophicOccurrences);
				currentRisk += catastrophicOccurrences*105;
				
			} else if ("Critical".equals(aForecastOutcomeIds[outcomeIndex])) {
				double[] aValues = net.getNodeValue(OVP_NODE_NAME);
				double criticOccurrences = aValues[outcomeIndex];
				System.out.println("BBNGeNIeHandler.updateRisk() - Critic: " + criticOccurrences);
				currentRisk += criticOccurrences*85;
				
			} else if ("Serious".equals(aForecastOutcomeIds[outcomeIndex])) {
				double[] aValues = net.getNodeValue(OVP_NODE_NAME);
				double seriousOccurrences = aValues[outcomeIndex];
				System.out.println("BBNGeNIeHandler.updateRisk() - Serious: " + seriousOccurrences);
				currentRisk += seriousOccurrences*65;
			} else if ("Minor".equals(aForecastOutcomeIds[outcomeIndex])) {
				double[] aValues = net.getNodeValue(OVP_NODE_NAME);
				double minorOccurrences = aValues[outcomeIndex];
				System.out.println("BBNGeNIeHandler.updateRisk() - minor: " + minorOccurrences);
				currentRisk += minorOccurrences*45;
			} else if ("Negligible".equals(aForecastOutcomeIds[outcomeIndex])) {
				double[] aValues = net.getNodeValue(OVP_NODE_NAME);
				double negligibleOccurrences = aValues[outcomeIndex];
				System.out.println("BBNGeNIeHandler.updateRisk() - Negligible: " + negligibleOccurrences);
				currentRisk += negligibleOccurrences*25;
			}
		}
		
		Risk current = Risk.LOWER_RISK_VALUE;
		
		if (currentRisk >= 90) {
			current = Risk.HIGHTEST_RISK_VALUE;
		} else if (currentRisk < 90 && currentRisk >= 70) {
			current = Risk.CRITIC_RISK_VALUE;
		} else if (currentRisk < 70 && currentRisk >= 50) {
			current = Risk.ALERT_RISK_VALUE;
		} else if (currentRisk < 50 && currentRisk >= 30) {
			current = Risk.LOW_ALERT_RISK_VALUE;
		} else if (currentRisk < 30 && currentRisk >= 0) {
			current = Risk.LOWER_RISK_VALUE; 
		}
		System.out.println("BBNGeNIeHandler.updateRisk() novo valor do risco: " + current.getName());
		System.out.println("BBNGeNIeHandler.updateRisk() currentRisk: " + currentRisk);
		return current;
	}

	public static BBNGeNIeHandler getInstance() {
		if (singleton == null) {
			singleton = new BBNGeNIeHandler();
		}
		return singleton;
	}

	public static void licenceSMILE() {
		// License issued by BayesFusion Licensing Server
		// This code must be executed before any other jSMILE object is created
		new smile.License(
				"SMILE LICENSE 683d4e7e 3313f60e 7a635492 " + "THIS IS AN ACADEMIC LICENSE AND CAN BE USED  "
						+ "SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " + "AS DEFINED IN THE BAYESFUSION ACADEMIC  "
						+ "SOFTWARE LICENSING AGREEMENT. " + "Serial #: c821rhkjxk6g15llx8axbjdf1 "
						+ "Issued for: Fabio Luiz Leite J\u00fanior (fabioleite@gmail.com) "
						+ "Academic institution: TU Kaiserslautern " + "Valid until: 2018-07-20 "
						+ "Issued by BayesFusion activation server",
				new byte[] { 66, -51, -117, 35, -15, -128, 117, -13, 25, 37, 106, -55, 69, 59, 47, -46, -102, -77, 93,
						66, -75, 101, 59, 5, 112, 39, -41, 31, 24, 72, -117, 65, -23, -62, 21, 96, -99, 82, -111, -115,
						114, 13, 4, 105, -25, 16, -101, 61, -103, 68, -60, -87, 118, 34, -8, -57, -110, 96, 65, 113, 15,
						-12, -2, -70 });
	}

}

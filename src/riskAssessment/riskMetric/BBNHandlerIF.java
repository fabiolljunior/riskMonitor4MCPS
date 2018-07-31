package riskAssessment.riskMetric;

import riskAssessment.Risk;

public interface BBNHandlerIF {

	Risk updateHR(int value);

	Risk updateSpO2(double value);

	Risk updateRespirationRate(int value);

	Risk updateEtCO2(double value);

	Risk updateRisk();

}
package riskAssessment;

import deviceManager.DeviceManager;
import deviceManager.GenericPulseOximeter;
import deviceManager.GenericRespirationMonitor;
import observer.HRObserver;
import observer.SPO2Observer;

public class RiskCalculator implements SPO2Observer, HRObserver{
	
	private GenericPulseOximeter pulseOximeter;
	private GenericRespirationMonitor respMonitor;
	private Risk currentriskLevel = null;
	
	public RiskCalculator() {
		pulseOximeter = DeviceManager.getInstance().getGenericPulseOximeter();
		respMonitor = DeviceManager.getInstance().getGenericRespirationMonitor();
		pulseOximeter.registerHRObserver(this);
		pulseOximeter.registerSPO2Observer(this);
	}
	
	public RiskCalculator(DeviceManager deviceManager) {
		pulseOximeter = deviceManager.getGenericPulseOximeter();
		respMonitor = deviceManager.getGenericRespirationMonitor();
		pulseOximeter.registerHRObserver(this);
		pulseOximeter.registerSPO2Observer(this);
	}
	
	public Risk calculateRisk() {
		Risk monitoringRisk = calculateRiskPulseOximetry();
		if (monitoringRisk.getValue() <= 2) {
			return new Risk(4);
		} else if (monitoringRisk.getValue() == 3) {
			return new Risk(9);
		} else if (monitoringRisk.getValue() == 4) {
			return new Risk(12);
		} else {
			return new Risk(25);
		}
	}
	
	public Risk calculateRiskPulseOximetry() {
		int heartRate = pulseOximeter.getCurrentHeartRate();
		double spo2 = pulseOximeter.getCurrentSpO2();
		RiskCriticalityLevel riskCriticalityLevelSPO2 = null;
		RiskCriticalityLevel riskCriticalityLevelHR = null;
		
		if (heartRate != GenericRespirationMonitor.INVALID_VALUE) {
			riskCriticalityLevelHR = RiskCriticalityLevel.getCriticalityLevelofHR(heartRate);
		}
		
		if (spo2 != GenericPulseOximeter.INVALID_VALUE) {
			riskCriticalityLevelSPO2 = RiskCriticalityLevel.getCriticalityLevelofSPO2(spo2);
		}
		int criticalValue = 0;
		if (riskCriticalityLevelSPO2 != null && riskCriticalityLevelHR != null) {
			//highest critical value
			criticalValue = (comparePORiskLevel(riskCriticalityLevelSPO2, riskCriticalityLevelHR) ? riskCriticalityLevelSPO2.getValue(): riskCriticalityLevelHR.getValue()); 
		} else if (riskCriticalityLevelSPO2 == null) {
			criticalValue = riskCriticalityLevelHR.getValue();
		} else {
			criticalValue = riskCriticalityLevelSPO2.getValue();
		}
		
		return new Risk(criticalValue);
		
	}

	private boolean comparePORiskLevel(RiskCriticalityLevel riskCriticalityLevelSPO2,
			RiskCriticalityLevel riskCriticalityLevelHR) {
			if (riskCriticalityLevelSPO2.compareTo(riskCriticalityLevelHR) > 1) {
				return true;
			}
		return false;
	}
	
	@Override
	public void changeHR(float value) {
		this.currentriskLevel = calculateRisk();
		
	}

	@Override
	public void changeSPO2(double value) {
		this.currentriskLevel = calculateRisk();
		
	}

	public GenericPulseOximeter getPulseOximeter() {
		return pulseOximeter;
	}

	public void setPulseOximeter(GenericPulseOximeter pulseOximeter) {
		this.pulseOximeter = pulseOximeter;
	}

	public GenericRespirationMonitor getRespMonitor() {
		return respMonitor;
	}

	public void setRespMonitor(GenericRespirationMonitor respMonitor) {
		this.respMonitor = respMonitor;
	}

	public Risk getCurrentriskLevel() {
		return currentriskLevel;
	}

	public void setCurrentriskLevel(Risk currentriskLevel) {
		this.currentriskLevel = currentriskLevel;
	}

}

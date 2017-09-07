package riskAssessment;

import java.util.ArrayList;
import java.util.Calendar;

import deviceManager.DeviceManager;
import deviceManager.GenericPulseOximeter;
import deviceManager.GenericRespirationMonitor;
import observer.ETCO2Observer;
import observer.HRObserver;
import observer.RRObserver;
import observer.RiskObservable;
import observer.RiskObserver;
import observer.SPO2Observer;

public class RiskCalculator implements SPO2Observer, HRObserver, RRObserver, ETCO2Observer, RiskObservable{
	
	private GenericPulseOximeter pulseOximeter;
	private GenericRespirationMonitor respMonitor;
	private Risk currentriskLevel = null;
	
	private ArrayList<RiskObserver> riskListeners = null;
	
	public RiskCalculator() {
		riskListeners = new ArrayList<>();
		pulseOximeter = DeviceManager.getInstance().getGenericPulseOximeter();
		respMonitor = DeviceManager.getInstance().getGenericRespirationMonitor();
		pulseOximeter.registerHRObserver(this);
		pulseOximeter.registerSPO2Observer(this);
		respMonitor.registerRRObserver(this);
		respMonitor.registerETCO2Observer(this);
	}
	
	public RiskCalculator(DeviceManager deviceManager) {
		riskListeners = new ArrayList<>();
		pulseOximeter = deviceManager.getGenericPulseOximeter();
		respMonitor = deviceManager.getGenericRespirationMonitor();
		pulseOximeter.registerHRObserver(this);
		pulseOximeter.registerSPO2Observer(this);
		respMonitor.registerRRObserver(this);
		respMonitor.registerETCO2Observer(this);
	}
	
	/**
	 * Main method to calculate the risk
	 * @return
	 */
	public Risk calculateRisk() {
		Risk finalRisk = new Risk();
		return finalRisk.calculateRisk(calculateRiskCapnography(),calculateRiskPulseOximetry());
	}
	
	public Risk calculateRiskCapnography() {
		int respRate = respMonitor.getCurrentHeartRate();
		double etco2 = respMonitor.getCurrentEtCO2();
		RiskCriticalityLevel riskCriticalityLevelETCO2 = null;
		RiskCriticalityLevel riskCriticalityLevelRR = null;
		
		if (respRate != GenericRespirationMonitor.INVALID_VALUE) {
			riskCriticalityLevelRR = RiskCriticalityLevel.getCriticalityLevelRR(respRate);
		}
		
		if (etco2 != GenericRespirationMonitor.INVALID_VALUE) {
			riskCriticalityLevelETCO2 = RiskCriticalityLevel.getCriticalityLevelETCO2(etco2);
		}
		
		int criticalValue = 0;
		if (riskCriticalityLevelETCO2 != null && riskCriticalityLevelRR != null) {
			//highest critical value
			criticalValue = (comparePORiskLevel(riskCriticalityLevelETCO2, riskCriticalityLevelRR) ? riskCriticalityLevelETCO2.getValue(): riskCriticalityLevelRR.getValue()); 
		} else if (riskCriticalityLevelETCO2 == null) {
			criticalValue = riskCriticalityLevelRR.getValue();
		} else {
			criticalValue = riskCriticalityLevelETCO2.getValue();
		}
		
		return new Risk(criticalValue);
	}

	public Risk calculateRiskPulseOximetry() {
		int heartRate = pulseOximeter.getCurrentHeartRate();
		double spo2 = pulseOximeter.getCurrentSpO2();
		RiskCriticalityLevel riskCriticalityLevelSPO2 = null;
		RiskCriticalityLevel riskCriticalityLevelHR = null;
		
		if (heartRate != GenericPulseOximeter.INVALID_VALUE) {
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
	public void changeHR(int value) {
		this.currentriskLevel = calculateRisk();
		notifyRiskChange(currentriskLevel);
	}

	@Override
	public void changeSPO2(double value) {
		this.currentriskLevel = calculateRisk();
		notifyRiskChange(currentriskLevel);
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

	@Override
	public void changeETCO2(double newETCO2Value) {
		this.currentriskLevel = calculateRisk();
		notifyRiskChange(this.currentriskLevel);
	}

	@Override
	public void changeRR(int newHearRate) {
		this.currentriskLevel = calculateRisk();
		notifyRiskChange(this.currentriskLevel);
	}

	@Override
	public void notifyRiskChange(Risk newRiskValue) {
		long currentTimeMilis = Calendar.getInstance().getTimeInMillis();
		for (RiskObserver riskObserver : riskListeners) {
			riskObserver.notifyRiskChange(newRiskValue,currentTimeMilis);
		}
	}

	@Override
	public void registerListener(RiskObserver riskobserver) {
		riskListeners.add(riskobserver);
	}

	@Override
	public void unregisterListener(RiskObserver riskobserver) {
		riskListeners.remove(riskobserver);
	}

}

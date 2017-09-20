package riskAssessment;

import java.util.ArrayList;
import java.util.Calendar;

import datamanagement.GraphicDataManagment;
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
		riskListeners.add(new GraphicDataManagment());
		pulseOximeter = DeviceManager.getInstance().getGenericPulseOximeter();
		respMonitor = DeviceManager.getInstance().getGenericRespirationMonitor();
		pulseOximeter.registerHRObserver(this);
		pulseOximeter.registerSPO2Observer(this);
		respMonitor.registerRRObserver(this);
		respMonitor.registerETCO2Observer(this);
	}
	
	public RiskCalculator(DeviceManager deviceManager) {
		riskListeners = new ArrayList<>();
		riskListeners.add(new GraphicDataManagment());
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
		
		RiskCriticalityLevel calculatedCapnometetryRisk = calculateRiskCapnometry();
		
		RiskCriticalityLevel calculatedPulseOximetryRisk = calculateRiskPulseOximetry();
		
		if (calculatedCapnometetryRisk == null && calculatedPulseOximetryRisk == null) {
			return null;
		} else if (calculatedCapnometetryRisk == null) {
			return Risk.getRisk(calculatedPulseOximetryRisk);
		} else if (calculatedPulseOximetryRisk == null) {
			return Risk.getRisk(calculatedCapnometetryRisk);
		}		
		
		//Just getting the higher risk
		return Risk.getRisk(
				comparePORiskLevel(calculatedCapnometetryRisk,calculatedPulseOximetryRisk) ?
						calculatedCapnometetryRisk : calculatedPulseOximetryRisk);
	}
	
	public RiskCriticalityLevel calculateRiskCapnometry() {
		int respRate = respMonitor.getCurrentRespirationRate();
		double etco2 = respMonitor.getCurrentEtCO2();
		RiskCriticalityLevel riskCriticalityLevelETCO2 = null;
		RiskCriticalityLevel riskCriticalityLevelRR = null;
		
		if (respRate == GenericRespirationMonitor.INVALID_VALUE && etco2 == GenericRespirationMonitor.INVALID_VALUE) {
//			throw new NotEnoughDataException("There is no available data for calculating the risk of respiration monitor");
			return null;
		}
		
		if (respRate != GenericRespirationMonitor.INVALID_VALUE) {
			riskCriticalityLevelRR = RiskCriticalityLevel.getCriticalityLevelRR(respRate);
		}
		
		if (etco2 != GenericRespirationMonitor.INVALID_VALUE) {
			riskCriticalityLevelETCO2 = RiskCriticalityLevel.getCriticalityLevelETCO2(etco2);
		}
		
		if (riskCriticalityLevelETCO2 != null && riskCriticalityLevelRR != null) {
			//highest critical value
			return (comparePORiskLevel(riskCriticalityLevelETCO2, riskCriticalityLevelRR) ? riskCriticalityLevelETCO2: riskCriticalityLevelRR); 
		} else if (riskCriticalityLevelETCO2 == null) {
			return riskCriticalityLevelRR;
		} else {
			return riskCriticalityLevelETCO2;
		}
		
	}

	public RiskCriticalityLevel calculateRiskPulseOximetry() {
		int heartRate = pulseOximeter.getCurrentHeartRate();
		double spo2 = pulseOximeter.getCurrentSpO2();
		RiskCriticalityLevel riskCriticalityLevelSPO2 = null;
		RiskCriticalityLevel riskCriticalityLevelHR = null;
		
		if (heartRate == GenericPulseOximeter.INVALID_VALUE && spo2 == GenericPulseOximeter.INVALID_VALUE) {
//			throw new NotEnoughDataException("There is not enough data for calculating the risk for pulseOximetry");
			return null;
		}
		
		if (heartRate != GenericPulseOximeter.INVALID_VALUE) {
			riskCriticalityLevelHR = RiskCriticalityLevel.getCriticalityLevelofHR(heartRate);
		}
		
		if (spo2 != GenericPulseOximeter.INVALID_VALUE) {
			riskCriticalityLevelSPO2 = RiskCriticalityLevel.getCriticalityLevelofSPO2(spo2);
		}
		if (riskCriticalityLevelSPO2 != null && riskCriticalityLevelHR != null) {
			//highest critical value
			return (comparePORiskLevel(riskCriticalityLevelSPO2, riskCriticalityLevelHR) ? riskCriticalityLevelSPO2: riskCriticalityLevelHR); 
		} else if (riskCriticalityLevelSPO2 == null) {
			return riskCriticalityLevelHR;
		} else {
			return riskCriticalityLevelSPO2;
		}
		
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

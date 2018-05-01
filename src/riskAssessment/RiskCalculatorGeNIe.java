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

public class RiskCalculatorGeNIe implements SPO2Observer, HRObserver, RRObserver, ETCO2Observer, RiskObservable{
	
	private GenericPulseOximeter pulseOximeter;
	private GenericRespirationMonitor respMonitor;
	private Risk currentriskLevel = null;
	private BBNGeNIeHandler bbnHandler = null; 
	
	private ArrayList<RiskObserver> riskListeners = null;
	
	public RiskCalculatorGeNIe() {
		riskListeners = new ArrayList<>();
		riskListeners.add(new GraphicDataManagment());
		pulseOximeter = DeviceManager.getInstance().getGenericPulseOximeter();
		respMonitor = DeviceManager.getInstance().getGenericRespirationMonitor();
		pulseOximeter.registerHRObserver(this);
		pulseOximeter.registerSPO2Observer(this);
		respMonitor.registerRRObserver(this);
		respMonitor.registerETCO2Observer(this);
		bbnHandler = BBNGeNIeHandler.getInstance();
	}
	
	public RiskCalculatorGeNIe(DeviceManager deviceManager) {
		riskListeners = new ArrayList<>();
		riskListeners.add(new GraphicDataManagment());
		pulseOximeter = deviceManager.getGenericPulseOximeter();
		respMonitor = deviceManager.getGenericRespirationMonitor();
		pulseOximeter.registerHRObserver(this);
		pulseOximeter.registerSPO2Observer(this);
		respMonitor.registerRRObserver(this);
		respMonitor.registerETCO2Observer(this);
		bbnHandler = BBNGeNIeHandler.getInstance();
	}
	

	@Override
	public void changeHR(int value) {
		this.currentriskLevel = bbnHandler.updateHR(value);
		notifyRiskChange(currentriskLevel);
	}

	@Override
	public void changeSPO2(double value) {
		this.currentriskLevel = bbnHandler.updateSpO2(value);
		notifyRiskChange(currentriskLevel);
	}
	
	@Override
	public void changeETCO2(double newETCO2Value) {
		this.currentriskLevel = bbnHandler.updateEtCO2(newETCO2Value);
		notifyRiskChange(this.currentriskLevel);
	}

	@Override
	public void changeRR(int newRespRate) {
		this.currentriskLevel = bbnHandler.updateRespirationRate(newRespRate);
		notifyRiskChange(this.currentriskLevel);
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

	public Risk calculateRisk() {
		return bbnHandler.updateRisk();
	}

}

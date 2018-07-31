package riskAssessment;

import java.util.ArrayList;
import java.util.Calendar;

import datamanagement.GraphicDataManagment;
import deviceManager.DeviceManager;
import deviceManager.GenericDevice;
import deviceManager.GenericPulseOximeter;
import deviceManager.GenericRespirationMonitor;
import observer.DeviceListener;
import observer.ETCO2Observer;
import observer.HRObserver;
import observer.RRObserver;
import observer.RiskObservable;
import observer.RiskObserver;
import observer.SPO2Observer;
import riskAssessment.riskMetric.BBNHandlerIF;
import riskAssessment.riskMetric.RiskMetricHandler;

public class RiskCalculatorGeNIe implements SPO2Observer, HRObserver, RRObserver, ETCO2Observer, RiskObservable, DeviceListener{
	
	private GenericPulseOximeter pulseOximeter;
	private GenericRespirationMonitor respMonitor;
	private Risk currentriskLevel = null;
	private BBNHandlerIF bbnHandler = null; 
	
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
		bbnHandler = RiskMetricHandler.getInstance().getRiskMetricConfig3();
		pulseOximeter.registerDeviceObserver(this);
		respMonitor.registerDeviceObserver(this);
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
		bbnHandler = RiskMetricHandler.getInstance().getRiskMetricConfig3();
		pulseOximeter.registerDeviceObserver(this);
		respMonitor.registerDeviceObserver(this);
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

	@Override
	public void deviceTurnedOn(GenericDevice device) {
		//TODO: change risk configuration according to the turned on device
		System.out.println("RiskCalculatorGeNIe.deviceTurnedOn() - device: " + device);
		if (device instanceof GenericPulseOximeter) {
			if (respMonitor.isAlive()) {
				bbnHandler = RiskMetricHandler.getInstance().getRiskMetricConfig3();
			} else {
				bbnHandler = RiskMetricHandler.getInstance().getRiskMetricConfig1();
			}
		} else {
			if (pulseOximeter.isAlive()) {
				bbnHandler = RiskMetricHandler.getInstance().getRiskMetricConfig3();
			} else {
				bbnHandler = RiskMetricHandler.getInstance().getRiskMetricConfig2();
			}
		}
		System.out.println("RiskCalculatorGeNIe.deviceTurnedOn() - vai mudar a metrica de risco: " + bbnHandler);
		notifyRiskChange(bbnHandler.updateRisk());
	}

	@Override
	public void deviceTurnedOff(GenericDevice device) {
		//TODO: change risk configuration according to the turned on device
		System.out.println("RiskCalculatorGeNIe.deviceTurnedOff() - device: " + device);
		if (device instanceof GenericPulseOximeter) {
			if (respMonitor.isAlive()) {
				bbnHandler = RiskMetricHandler.getInstance().getRiskMetricConfig2();
			} //TODO: avisar na tela quando não tiver equipamentos ligados
		} else {
			if (pulseOximeter.isAlive()) {
				System.out.println("RiskCalculatorGeNIe.deviceTurnedOff() - vai ligar a metrica de risco 1");
				bbnHandler = RiskMetricHandler.getInstance().getRiskMetricConfig1();
				System.out.println("RiskCalculatorGeNIe.deviceTurnedOff() - ligou a metrica de risco 1");
			} //TODO: avisar na tela quando não tiver equipamentos ligados
		}
		System.out.println("RiskCalculatorGeNIe.deviceTurnedOff() - vai mudar a metrica de risco: " + bbnHandler);
		notifyRiskChange(bbnHandler.updateRisk());
	}

}

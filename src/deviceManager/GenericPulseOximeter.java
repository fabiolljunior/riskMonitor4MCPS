package deviceManager;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import observer.HRObservable;
import observer.HRObserver;
import observer.SPO2Observable;
import observer.SPO2Observer;
import util.Device;

public class GenericPulseOximeter extends GenericDevice implements SpO2Device, HRDevice, SPO2Observable, HRObservable {

	public static final int INVALID_VALUE = -1;

	private int currentHeartRate = INVALID_VALUE;
	private double currentSpO2 = INVALID_VALUE;
	private List<SPO2Observer> spo2Observers = new ArrayList<>();
	private List<HRObserver> hrObservers = new ArrayList<>();
	private ScheduledExecutorService scheduler = null;

	public GenericPulseOximeter() {
		super();
	}

	@Override
	public void setData(float value, Device measurementType) {
		setLastDataReceived(Calendar.getInstance());
		if (!wakeUp) {
			System.out.println("GenericDevice.isAlive() - device: " + this.toString() + " ACORDOU!!!");
			setWakeUp(true);
			notifyDeviceIsOn(this);
			scheduler = Executors.newScheduledThreadPool(5);
			ScheduledFuture<?> handle = scheduler.scheduleWithFixedDelay(
					DeviceManager.getInstance().getGenericPulseOximeter().getDeviceMonitor(), 1, 2, SECONDS);
		}
		if (Device.PULS_OXIM_PULS_RATE.equals(measurementType)) {
			currentHeartRate = Math.round(value);
			notifyHRListeners(currentHeartRate);
		} else { // then Device.PULS_OXIM_SAT_O2
			currentSpO2 = value;
			notifySPO2Listeners(currentSpO2);
		}

	}
	
	@Override
	public String toString() {
		return "GenericPulseOximeter";
	}

	public void notifySPO2Listeners(double currentSpO2) {
		for (SPO2Observer spo2Observer : spo2Observers) {
			spo2Observer.changeSPO2(currentSpO2);
		}
	}

	public void notifyHRListeners(int currentHeartRate2) {
		for (HRObserver hrObserver : hrObservers) {
			hrObserver.changeHR(currentHeartRate2);
		}
	}

	public int getCurrentHeartRate() {
		return currentHeartRate;
	}

	public double getCurrentSpO2() {
		return currentSpO2;
	}

	public void setCurrentSpO2(double currentSpO2) {
		this.currentSpO2 = currentSpO2;
	}

	@Override
	public int getHR() {
		return currentHeartRate;
	}

	@Override
	public double getSpO2() {
		return currentSpO2;
	}

	@Override
	public void registerSPO2Observer(SPO2Observer observer) {
		if (this.spo2Observers == null) {
			this.spo2Observers = new ArrayList<SPO2Observer>();
		}
		spo2Observers.add(observer);
	}

	@Override
	public void removeSPO2Observer(SPO2Observer observer) {
		spo2Observers.remove(observer);
	}

	@Override
	public void registerHRObserver(HRObserver observer) {
		if (this.hrObservers == null) {
			this.hrObservers = new ArrayList<HRObserver>();
		}
		hrObservers.add(observer);

	}

	@Override
	public void removeHRObserver(HRObserver observer) {
		hrObservers.remove(observer);

	}

	@Override
	protected void resetCurrentValues() {
		this.currentHeartRate = INVALID_VALUE;
		this.currentSpO2 = INVALID_VALUE;

	}

	@Override
	public void turnOffMonitor() {
		scheduler.shutdownNow();
	}
}

package deviceManager;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import observer.ETCO2Observer;
import observer.RRObserver;
import util.Device;

public class GenericRespirationMonitor extends GenericDevice implements RRObservable, ETCO2Observable {
	
	public static final int INVALID_VALUE = -1;
	private int currentRespirationRate = INVALID_VALUE ;
	private double currentEtCO2 = INVALID_VALUE ;
	private ArrayList<RRObserver> rrObservers = null;
	private ArrayList<ETCO2Observer> etco2Observers = null;
	private ScheduledExecutorService scheduler = null;
	
	public GenericRespirationMonitor() {
		super();
	}
	
	@Override
	public void setData(float value, Device measurementType) {
		setLastDataReceived(Calendar.getInstance());
		if (!wakeUp) {
			System.out.println("GenericDevice.isAlive() - device: " + this.toString() + " ACORDOU!!!");
			setWakeUp(true);
			notifyDeviceIsOn(this);
			System.out.println("GenericRespirationMonitor.setData() - vai rodar a nova thread");
			scheduler = Executors.newScheduledThreadPool(5);
			ScheduledFuture<?> handle2 = scheduler.scheduleWithFixedDelay(
					this.getDeviceMonitor(), 1, 2, SECONDS);
		}
		if (Device.RESP_MONITOR_RESP_RATE.equals(measurementType)) {
			currentRespirationRate = Math.round(value);
			notifyRRhasChanged(currentRespirationRate);
			
		} else { // then Device.PULS_OXIM_SAT_O2
			currentEtCO2 = value;
			notifyETCO2hasChanged(currentEtCO2);
		}
	}

	public double getCurrentEtCO2() {
		return currentEtCO2;
	}
	
	@Override
	public String toString() {
		return "GenericRespirationMonitor";
	}

	public void setCurrentEtCO2(double currentEtCO2) {
		this.currentEtCO2 = currentEtCO2;
	}

	public int getCurrentRespirationRate() {
		return currentRespirationRate;
	}

	public void setCurrentHeartRate(int currentHeartRate) {
		this.currentRespirationRate = currentHeartRate;
	}

	@Override
	public void notifyETCO2hasChanged(double newETCO2Value) {
		if (etco2Observers != null) {
			for (ETCO2Observer etco2Observer : etco2Observers) {
				etco2Observer.changeETCO2(newETCO2Value);
			}
		}
	}

	@Override
	public void notifyRRhasChanged(int newRR) {
		if (this.rrObservers != null) {
			for (RRObserver rrObserver : rrObservers) {
				rrObserver.changeRR(newRR);
			}
		}
		
	}

	@Override
	public void registerRRObserver(RRObserver observer) {
		if (this.rrObservers == null) {
			this.rrObservers = new ArrayList<RRObserver>();
		}
		rrObservers.add(observer);
	}

	@Override
	public void removeRRObserver(RRObserver observer) {
		rrObservers.remove(observer);
	}

	@Override
	public void registerETCO2Observer(ETCO2Observer observer) {
		if (this.etco2Observers == null) {
			this.etco2Observers = new ArrayList<ETCO2Observer>();
		}
		etco2Observers.add(observer);
	}

	@Override
	public void removeETCO2Observer(ETCO2Observer observer) {
		etco2Observers.remove(observer);
	}

	@Override
	protected void resetCurrentValues() {
		this.currentEtCO2 = INVALID_VALUE;
		this.currentRespirationRate = INVALID_VALUE;
		
	}

	@Override
	public void turnOffMonitor() {
		scheduler.shutdownNow();
		
	}

}

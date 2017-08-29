package deviceManager;

import java.util.List;

import observer.DeviceObserver;
import util.Device;

public class GenericDevice {
	
	protected static final int TIME_TO_DEAD = 5;
	
	protected float data;
	protected int secondsWithoutData = 0;
	
	protected List<DeviceObserver> devicesObservers; 
	
	public float getData() {
		return this.data;
	}

	public void setData(float value, Device measurementType) {
		resetSecondsWithOutData();
		this.data = value;
		//TODO: notify riskMonitor
	}

	protected int getSecondsWithoutData() {
		return secondsWithoutData;
	}

	protected void setSecondsWithoutData(int secondsWithoutData) {
		this.secondsWithoutData = secondsWithoutData;
	}
	
	public void resetSecondsWithOutData() {
		this.secondsWithoutData = 0;
	}
	
	public void updateSecondsWithOutData() {
		this.secondsWithoutData++;
	}

	
}

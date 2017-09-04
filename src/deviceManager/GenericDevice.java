package deviceManager;

import java.util.Calendar;
import java.util.List;

import observer.DeviceObserver;
import util.Device;

public class GenericDevice {
	
	protected static final int TIME_TO_DEAD = 5;
	
	private static final long ONE_SECOND = 1000;
	
	protected float data;
	protected int secondsWithoutData = 0;
	private Calendar lastDataReceived;
	
	protected List<DeviceObserver> devicesObservers; 
	
	public float getData() {
		return this.data;
	}

	public void setData(float value, Device measurementType) {
		setLastDataReceived(Calendar.getInstance());
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

	public Calendar getLastDataReceived() {
		return lastDataReceived;
	}

	public void setLastDataReceived(Calendar lastDataReceived) {
		this.lastDataReceived = lastDataReceived;
	}
	
	public boolean isAlive() {
		boolean differenceInSecondsBiggerThenTimeToDead = isDifferenceInSecondsBiggerThenTimeToDead();
		if (differenceInSecondsBiggerThenTimeToDead) {
			return false;
		}
		return true;
	}
	
	private boolean isDifferenceInSecondsBiggerThenTimeToDead() {
		if (this.lastDataReceived != null) {
			long seconds = (Calendar.getInstance().getTimeInMillis()  
					- this.lastDataReceived.getTimeInMillis()) / ONE_SECOND;
			
			if (seconds >= TIME_TO_DEAD) {
				return true;
			}
		}
		return false;
	}
}

package deviceManager;

import util.Device;

public class GenericRespirationMonitor extends GenericDevice {
	
	public static final int INVALID_VALUE = -1;
	private int currentRespirationRate = INVALID_VALUE ;
	private double currentEtCO2 = INVALID_VALUE ;
	
	public boolean isAlive() {
		if (this.secondsWithoutData == TIME_TO_DEAD) {
			return false;
		}
		return true;
	}
	
	@Override
	public void setData(float value, Device measurementType) {
		if (Device.RESP_MONITOR_RESP_RATE.equals(measurementType)) {
			currentRespirationRate = Math.round(value);
		} else { // then Device.PULS_OXIM_SAT_O2
			currentEtCO2 = value;
		}
		//TODO: Notify the listeners
	}

	public double getCurrentEtCO2() {
		return currentEtCO2;
	}

	public void setCurrentEtCO2(double currentEtCO2) {
		this.currentEtCO2 = currentEtCO2;
	}

	public int getCurrentHeartRate() {
		return currentRespirationRate;
	}

	public void setCurrentHeartRate(int currentHeartRate) {
		this.currentRespirationRate = currentHeartRate;
	}

}

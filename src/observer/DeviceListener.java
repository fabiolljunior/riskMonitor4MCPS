package observer;

import deviceManager.GenericDevice;

public interface DeviceListener {
	
	public void deviceTurnedOn(GenericDevice device);
	public void deviceTurnedOff(GenericDevice device); 

}

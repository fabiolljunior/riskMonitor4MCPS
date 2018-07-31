package observer;

import deviceManager.GenericDevice;

public interface DeviceObservable {
	
	public void registerDeviceObserver(DeviceListener observer);
    public void removeDeviceObserver(DeviceListener observer);
    public void notifyDeviceIsOff(GenericDevice device);
    public void notifyDeviceIsOn(GenericDevice device);
    
}

package deviceManager;

public class DeviceMonitorRunnable implements Runnable {

	private GenericDevice genericDevice;

	public DeviceMonitorRunnable(GenericDevice genericDevice) {
		this.genericDevice = genericDevice;
	}

	@Override
	public void run() {
//		System.out.println(
//				"DeviceMonitorRunnable.run() - " + genericDevice.toString() + " alive: " + genericDevice.isAlive());
		if (!genericDevice.isAlive()) {
			System.out.println("DeviceMonitorRunnable.run() - device " + genericDevice.toString() + " FORA!!");
			genericDevice.setWakeUp(false);
			genericDevice.notifyDeviceIsOff(genericDevice);
			genericDevice.turnOffMonitor();
		}
	}
}

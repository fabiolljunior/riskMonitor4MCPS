package deviceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import observer.DeviceListener;
import observer.DeviceObservable;
import util.Device;

public abstract class GenericDevice implements DeviceObservable {
	
	protected static final int TIME_TO_DEAD = 5;
	
	private static final long ONE_SECOND = 1000;
	
	protected float data;
	protected int secondsWithoutData = 0;
	private Calendar lastDataReceived;
	
	protected DeviceMonitorRunnable deviceMonitor = null;
	protected boolean wakeUp = false;
	
	protected List<DeviceListener> devicesListener = new ArrayList<DeviceListener>(); 
	
	
	public GenericDevice() {
		this.setDeviceMonitor(new DeviceMonitorRunnable(this));
//		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//		ScheduledFuture<?> handle = scheduler.scheduleAtFixedRate(deviceMonitor, 0, 500, MILLISECONDS);
//		handle.cancel(false);
//		scheduler.schedule(new Runnable() {
//			public void run() {
//				handle.cancel(true);
//			}
//		}, 60 * 60, SECONDS);

		// executor.submit(deviceMonitor);
	}
	
	
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
		if (!wakeUp)
			return false;
		boolean differenceInSecondsBiggerThenTimeToDead = isDifferenceInSecondsBiggerThenTimeToDead();
		if (differenceInSecondsBiggerThenTimeToDead) {
			resetCurrentValues();
			return false;
		}
		return true;
	}
	
	protected abstract void resetCurrentValues();
	
	public abstract void turnOffMonitor();

	private boolean isDifferenceInSecondsBiggerThenTimeToDead() {
		if (this.getLastDataReceived() != null) {
			long seconds = (Calendar.getInstance().getTimeInMillis()  
					- this.lastDataReceived.getTimeInMillis()) / ONE_SECOND;
			
			if (seconds >= TIME_TO_DEAD) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void registerDeviceObserver(DeviceListener observer) {
		devicesListener.add(observer);
	}

	@Override
	public void removeDeviceObserver(DeviceListener observer) {
		devicesListener.remove(observer);
	}

	@Override
	public void notifyDeviceIsOff(GenericDevice device) {
		System.out.println("GenericDevice.notifyDeviceIsOn() - size: " + devicesListener.size());
		for (DeviceListener deviceObserver : devicesListener) {
			System.out.println("GenericDevice.notifyDeviceIsOff() - vai notificar: " + deviceObserver);
			deviceObserver.deviceTurnedOff(device);
		}
	}

	@Override
	public void notifyDeviceIsOn(GenericDevice device) {
		System.out.println("GenericDevice.notifyDeviceIsOn() - size: " + devicesListener.size());
		for (DeviceListener deviceObserver : devicesListener) {
			deviceObserver.deviceTurnedOn(device);
		}
	}


	public DeviceMonitorRunnable getDeviceMonitor() {
		return deviceMonitor;
	}


	public void setDeviceMonitor(DeviceMonitorRunnable deviceMonitor) {
		this.deviceMonitor = deviceMonitor;
	}

	public void setWakeUp(boolean wakeUp) {
		this.wakeUp = wakeUp;
	}

}

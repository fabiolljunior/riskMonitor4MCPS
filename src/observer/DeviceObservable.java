package observer;

public interface DeviceObservable {
	
	public void registerObserver(DeviceObserver observer);
    public void removeObserver(DeviceObserver observer);
    public void updateValueObservers(float value);

}

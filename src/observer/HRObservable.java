package observer;
public interface HRObservable {
	
    public void registerHRObserver(HRObserver observer);
    public void removeHRObserver(HRObserver observer);
    public void notifyHRListeners(int currentHR); 
}

package deviceManager;

import observer.RRObserver;

public interface RRObservable {
	
	public void notifyRRhasChanged(int newRR);
	public void registerRRObserver(RRObserver observer);
    public void removeRRObserver(RRObserver observer);

}

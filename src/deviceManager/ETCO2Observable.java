package deviceManager;

import observer.ETCO2Observer;

public interface ETCO2Observable {
	
	public void notifyETCO2hasChanged(double newETCO2Value);
	public void registerETCO2Observer(ETCO2Observer observer);
    public void removeETCO2Observer(ETCO2Observer observer);

}

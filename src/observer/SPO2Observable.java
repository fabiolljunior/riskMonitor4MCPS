package observer;
public interface SPO2Observable {
	
    public void registerSPO2Observer(SPO2Observer observer);
    public void removeSPO2Observer(SPO2Observer observer);
    public void notifySPO2Listeners(double currentSpO2); 
}

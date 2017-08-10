package deviceManager;

import java.util.ArrayList;
import java.util.List;

import observer.Observable;
import observer.Observer;
import riskAssessment.RiskMonitor;

public class DeviceManager implements Observable {
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	private GenericPulseOximeter genericPulseOximeter;
	private GenericRespirationMonitor genericRespirationMonitor;
	private RiskMonitor riskMonitor;
	
	public DeviceManager() {
		this.riskMonitor = new RiskMonitor();
		this.genericPulseOximeter = new GenericPulseOximeter();
		this.genericRespirationMonitor = new GenericRespirationMonitor();
		registerObserver(this.genericPulseOximeter);
		registerObserver(this.genericRespirationMonitor);
	}

	@Override
	public void registerObserver(Observer observer) {
		this.observers.add(observer);
		
	}

	@Override
	public void removeObserver(Observer observer) {
		this.observers.remove(observer);
		
	}

	@Override
	public void notifyObservers() {
		for (Observer ob : this.observers) {
			System.out.println("Notificando observers!");
			this.riskMonitor.addRisk(ob.updateRisk());
		}
		
	}

	public void sendDaTa(String data) {
		if (data.contains("PulseOximeter")) {
			int indexInit = data.lastIndexOf(":") + 1;
			String value = data.substring(indexInit, data.length()).trim();
			this.genericPulseOximeter.setData(new Double(value));
		}
		
		this.notifyObservers();
	}

}

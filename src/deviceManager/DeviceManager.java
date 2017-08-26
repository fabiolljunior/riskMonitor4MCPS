package deviceManager;

import java.util.ArrayList;
import java.util.List;

import observer.Observable;
import observer.Observer;
import riskAssessment.RiskMonitor;
import util.Device;

/**
 * Esta classe deveria ser uma factory singleton de devices.  Acho que não faz sentido deixar aberto o código
 * para criar várias, acredito que só haverá uma classe destas aqui e diversos dipositivos.
 * 
 * No futuro essa classe terá uma interface que deve ser conhecida pelo pacote dataReader. 
 * @author FábioLuiz
 *
 */
public class DeviceManager implements Observable {
	
	private static DeviceManager singleton = null;
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	private GenericPulseOximeter genericPulseOximeter;
	private GenericRespirationMonitor genericRespirationMonitor;
	private RiskMonitor riskMonitor;
	
	private DeviceManager() {
		this.riskMonitor = new RiskMonitor();
		this.genericPulseOximeter = new GenericPulseOximeter();
		this.genericRespirationMonitor = new GenericRespirationMonitor();
		registerObserver(this.genericPulseOximeter);
		registerObserver(this.genericRespirationMonitor);
	}
	
	public static DeviceManager getInstance() {
		if (singleton == null) {
			singleton = new DeviceManager();
		}
		return singleton;
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

			if(ob.isAlive()) {
				this.riskMonitor.updateRisk(ob.updateRisk());
			} else {
				System.out.println("device is not alive!");
			}
		}
	}

	public void sendDaTa(ice.Numeric data) {
        
		if (data != null) {
			
			try {
				String deviceID = data.metric_id;
				
				if (deviceID.equals(Device.PULS_OXIM_PULS_RATE.getName())) {
					this.genericPulseOximeter.setData(new Double(data.value));
					this.genericPulseOximeter.resetSecondsWithOutData();
					this.genericRespirationMonitor.updateSecondsWithOutData();
					
				} else if (deviceID.equals(Device.CO2_RESP_RATE.getName())) {
					this.genericRespirationMonitor.setData(new Double(data.value));
					this.genericRespirationMonitor.resetSecondsWithOutData();
					this.genericPulseOximeter.updateSecondsWithOutData();
					
				} else {
					System.out.println("Device not registered!");
				}
			} catch (java.lang.NoSuchFieldError nsfe) {
				System.out.println("nao tem campo correspondente");
			}
			
			
		}
		this.notifyObservers();
	}

}

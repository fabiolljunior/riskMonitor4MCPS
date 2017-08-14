package deviceManager;

import java.util.ArrayList;
import java.util.List;

import observer.Observable;
import observer.Observer;
import riskAssessment.RiskMonitor;

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
			this.riskMonitor.updateRisk(ob.updateRisk());
		}
		
	}

	/**
	 * Entendo que este método seja responsável por receber os measurements brutos (String,por exemplo)
	 * e transformá-los em objetos measurements e atribuir para o respectivo despositivo.
	 * 
	 *  Acredito que este código faça mais sentido por aqui. Não sei se neste método.
	 *  List<String> devices = new ArrayList<String>(Arrays.asList("Name: PulseOximeter,", "Name: RespirationMonitor,"));
		Random r = new Random();
		
		int z = 10;
		while (z>0) {
			int device = r.nextInt(1);
			
			String data = devices.get(device) + " Valor: " + r.nextDouble();
			manager.sendDaTa(data);
			z = z-1;
		}
	 *  
	 * @param data
	 */
	public void sendDaTa(String data) {
		if (data.contains("PulseOximeter")) {
			int indexInit = data.lastIndexOf(":") + 1;
			String value = data.substring(indexInit, data.length()).trim();
			this.genericPulseOximeter.setData(new Double(value));
		}
		
		this.notifyObservers();
	}
	
	/**
	 * TODO: Proposta de analisar essa forma de usar a api do opeICE. O que é que você acha ?
	 * Dá uma olhada na classe data tem como pegar ja'os valores.
	 * @param data
	 */
	public void sendDaTa(ice.Numeric data) {
		if (data != null) {
			this.genericPulseOximeter.setData(new Double(data.value));
		}
		this.notifyObservers();
	}

}

package deviceManager;

import java.util.ArrayList;
import java.util.List;

import util.Device;

/**
 * Esta classe deveria ser uma factory singleton de devices.  
 * Acho que não faz sentido deixar aberto o código para criar várias,
 * acredito que só haverá uma classe destas aqui e diversos dipositivos.
 * 
 * No futuro essa classe terá uma interface que deve ser conhecida pelo pacote dataReader. 
 * @author FábioLuiz
 *
 */
public class DeviceManager {
	
	private static DeviceManager singleton = null;
	
	private GenericPulseOximeter genericPulseOximeter;
	private GenericRespirationMonitor genericRespirationMonitor;
	
	private List<GenericDevice> devices;
	
	private DeviceManager() {
		this.setGenericPulseOximeter(new GenericPulseOximeter());
		this.setGenericRespirationMonitor(new GenericRespirationMonitor());
		
		addDeviceToList(getGenericPulseOximeter());
		addDeviceToList(getGenericRespirationMonitor());
	}
	
	public static DeviceManager getInstance() {
		if (singleton == null) {
			singleton = new DeviceManager();
		}
		return singleton;
	}
	
	private void addDeviceToList(GenericDevice dev) {
		if (this.devices == null) {
			this.devices = new ArrayList<GenericDevice>();
		}
		
		this.devices.add(dev);
	}
	
	public void sendDaTa(ice.Numeric data) {
		if (data != null) {
			
			try {
				String deviceID = data.metric_id;
				
				if (deviceID.equals(Device.PULS_OXIM_PULS_RATE.getName())) {
					this.getGenericPulseOximeter().setData(data.value, Device.PULS_OXIM_PULS_RATE);
					//TODO: eu acho que é melhor confiar no clock do relógio do sistema, pode acontecer de receber mais de uma msg por segundo
					
				}  else if (deviceID.equals(Device.PULS_OXIM_SAT_O2.getName())) {
					this.getGenericPulseOximeter().setData(data.value,Device.PULS_OXIM_SAT_O2);
					//TODO: eu acho que é melhor confiar no clock do relógio do sistema, pode acontecer de receber mais de uma msg por segundo
					
				} else if (deviceID.equals(Device.RESP_MONITOR_RESP_RATE.getName())) {
					this.getGenericRespirationMonitor().setData(data.value, Device.RESP_MONITOR_RESP_RATE);
					//TODO: eu acho que é melhor confiar no clock do relógio do sistema, pode acontecer de receber mais de uma msg por segundo
					
				} else if (deviceID.equals(Device.RESP_MONITOR_ETCO2.getName())) {
					this.getGenericRespirationMonitor().setData(data.value, Device.RESP_MONITOR_ETCO2);
					//TODO: eu acho que é melhor confiar no clock do relógio do sistema, pode acontecer de receber mais de uma msg por segundo
					
				} else {
					System.out.println("Device not registered!");
					
				}
			} catch (java.lang.NoSuchFieldError nsfe) {
				System.out.println("nao tem campo correspondente");
			}
		}
	}

	public GenericPulseOximeter getGenericPulseOximeter() {
		return genericPulseOximeter;
	}

	protected void setGenericPulseOximeter(GenericPulseOximeter genericPulseOximeter) {
		this.genericPulseOximeter = genericPulseOximeter;
	}

	public GenericRespirationMonitor getGenericRespirationMonitor() {
		return genericRespirationMonitor;
	}

	public void setGenericRespirationMonitor(GenericRespirationMonitor genericRespirationMonitor) {
		this.genericRespirationMonitor = genericRespirationMonitor;
	}

}

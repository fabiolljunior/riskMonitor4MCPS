package datareader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import deviceManager.DeviceManager;

public class OpenIceMonitor {
	
	public static void main(String[] args) {
		DeviceManager manager = new DeviceManager();
		List<String> devices = new ArrayList<String>(Arrays.asList("Name: PulseOximeter,", "Name: RespirationMonitor,"));
		Random r = new Random();
		
		int z = 10;
		while (z>0) {
			int device = r.nextInt(1);
			
			String data = devices.get(device) + " Valor: " + r.nextDouble();
			manager.sendDaTa(data);
			z = z-1;
		}
	}

}

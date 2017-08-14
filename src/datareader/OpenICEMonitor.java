package datareader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import deviceManager.DeviceManager;

/**
 * Classe que gerencia os possíveis listeners do openICE. Por enquanto teremos apenas um.
 * Acredito que não faça sentido ter mais de uma classe dessas, pois ela deveria ser uma factory gerenciadora
 * de threads (OpenICEListeners). Esta classe seria a porta de acesso para quem quiser escutar os dados 
 * do OpenICE. A classe OpenICEListener deveria ser transparente para que estiver de fora deste pacote.
 * @author FábioLuiz
 *
 */
public class OpenICEMonitor {
	
	private static OpenICEMonitor singleton = null;
	
	private OpenICEListener listener;
	
	private ExecutorService executor = null;
	
	private OpenICEMonitor() {
		listener = new OpenICEListener();
		listener.setDeviceManager(DeviceManager.getInstance());
		executor = Executors.newFixedThreadPool(1);
		executor.submit(listener);
		executor.shutdown();
		System.out.println("all tasks subitted...");
		
		try {
			executor.awaitTermination(2, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static OpenICEMonitor getInstance() {
		if (singleton == null) {
			singleton = new OpenICEMonitor();
		}
		return singleton;
	}
	
	public static void main(String[] args) {
		
		System.out.println("OpenICEMonitor.main() - creating the monitor...");
		OpenICEMonitor monitor = OpenICEMonitor.getInstance();
		System.out.println("OpenICEMonitor.main() - monitor created ...");
		// Pause for 30 seconds and force quitting the app (because we're
        // looping infinitely)
        try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.exit(0);
		
		
	}

}

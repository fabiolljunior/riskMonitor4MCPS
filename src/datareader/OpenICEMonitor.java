package datareader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import deviceManager.DeviceManager;
import riskAssessment.RiskCalculatorGeNIe;

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
	
	private RiskCalculatorGeNIe riskCalculator;
	
	private OpenICEMonitor() {
		listener = new OpenICEListener();
		listener.setDeviceManager(DeviceManager.getInstance());
		riskCalculator = new RiskCalculatorGeNIe();
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
		licenceSMILE();
		System.out.println("OpenICEMonitor.main() - creating the monitor...");
		OpenICEMonitor monitor = OpenICEMonitor.getInstance();
		System.out.println("all monitors subitted...");
		DynamicLineChart.main(null);
//		monitor.getRiskCalculator();
		System.out.println("OpenICEMonitor.main() - monitor created ...");
		
		// Pause for 30 seconds and force quitting the app (because we're
        // looping infinitely)
        try {
			Thread.sleep(30000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.exit(0);
		
		
	}

	public RiskCalculatorGeNIe getRiskCalculator() {
		return riskCalculator;
	}

	public void setRiskCalculator(RiskCalculatorGeNIe riskCalculator) {
		this.riskCalculator = riskCalculator;
	}
	
	
	public static void licenceSMILE() {
		// License issued by BayesFusion Licensing Server
		// This code must be executed before any other jSMILE object is created
		new smile.License(
			"SMILE LICENSE a7362980 fc745f9d d3f0336c " +
			"THIS IS AN ACADEMIC LICENSE AND CAN BE USED " +
			"SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " +
			"AS DEFINED IN THE BAYESFUSION ACADEMIC " +
			"SOFTWARE LICENSING AGREEMENT. " +
			"Serial #: 4ovxzxnfrsl1jb5f4e2ffk6fh " +
			"Issued for: Fabio Luiz Leite J\u00fanior (fabioleite@gmail.com) " +
			"Academic institution: TU Kaisrslautern " +
			"Valid until: 2019-01-25 " +
			"Issued by BayesFusion activation server",
			new byte[] {
			-63,-54,118,-73,-20,-6,-14,-105,-83,-105,-60,-52,-51,123,-103,-10,
			-95,-88,21,-103,-41,-47,2,98,65,-7,61,99,-61,-87,-90,118,
			0,-63,65,-52,0,109,-70,-127,119,-110,-15,93,-110,-11,-5,35,
			7,-15,-110,1,104,42,-58,76,56,59,-14,-85,75,93,116,-40
			}
		);
	}

}

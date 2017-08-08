package deviceManager;

import observer.Observer;

public class GenericRespirationMonitor extends GenericDevice implements Observer {
	
	@Override
	public Double updateRisk() {
		if (this.data != null) {
			//risco do equipamento
			return this.data * 0.5;
		}
		return 0d;
	}
}

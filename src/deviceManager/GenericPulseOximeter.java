package deviceManager;

import observer.Observer;

public class GenericPulseOximeter extends GenericDevice implements Observer {
	
	@Override
	public Double updateRisk() {
		if (this.data != null) {
			//risco do equipamento
			return this.data * 2;
		}
		return 0d;
	}

}

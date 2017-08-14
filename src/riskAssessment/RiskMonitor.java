package riskAssessment;

import java.util.ArrayList;
import java.util.List;

public class RiskMonitor {
	
	private List<Double> risks;
	
	public void updateRisk(Double risk) {
		if (this.risks == null) {
			this.risks = new ArrayList<Double>();
		}
		
		this.risks.add(risk);
		System.out.println("Risco avaliado: " + risk);
	}
}

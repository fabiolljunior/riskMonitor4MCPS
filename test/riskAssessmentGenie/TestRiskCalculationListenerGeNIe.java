package riskAssessmentGenie;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import deviceManager.DeviceManager;
import deviceManager.GenericPulseOximeter;
import deviceManager.GenericRespirationMonitor;
import riskAssessment.Risk;
import riskAssessment.RiskCalculatorGeNIe;

public class TestRiskCalculationListenerGeNIe {
	
	private RiskCalculatorGeNIe calculator = null;
	
	@Test
	public void updateRiskTestGenie() {
		
	//  create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(myPulseOximeter.getCurrentSpO2()).thenReturn(new Double(60));
		when(myPulseOximeter.getCurrentHeartRate()).thenReturn(-1);
		
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter);
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor);
		
		calculator = new RiskCalculatorGeNIe(deviceManager);
		
		assertEquals(calculator.calculateRisk(),Risk.ALERT_RISK_VALUE);
		
		
		//  create mock
		DeviceManager deviceManager2 = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter2 = mock(GenericPulseOximeter.class);
		when(myPulseOximeter2.getCurrentSpO2()).thenReturn(new Double(98));
		when(myPulseOximeter2.getCurrentHeartRate()).thenReturn(-1);
		
		when(deviceManager2.getGenericPulseOximeter()).thenReturn(myPulseOximeter2);
		GenericRespirationMonitor myRespirationMonitor2 = mock(GenericRespirationMonitor.class);
		when(deviceManager2.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor2);
		
		calculator = new RiskCalculatorGeNIe(deviceManager2);
		
		assertEquals(calculator.calculateRisk(),Risk.LOWER_RISK_VALUE);
	}

}

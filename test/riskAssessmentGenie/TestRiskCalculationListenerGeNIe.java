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


	@Test
	public void updateRiskTestGenie() {
		System.out.println("TestRiskCalculationListenerGeNIe.updateRiskTestGenie() -- initializing...");
		RiskCalculatorGeNIe calculator = null;
		// create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter);
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor);
		
		calculator = new RiskCalculatorGeNIe(deviceManager);
		
		calculator.changeETCO2(new Double(35));
		calculator.changeHR(50);
		calculator.changeRR(11);
		calculator.changeSPO2(new Double(20));

		assertEquals(calculator.calculateRisk(), Risk.HIGHTEST_RISK_VALUE);

	}

	@Test
	public void updateEtCO2TestGenie() {
		System.out.println("TestRiskCalculationListenerGeNIe.updateEtCO2TestGenie() -- initializing...");
		RiskCalculatorGeNIe calculator = null;
		// create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter);
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor);

		calculator = new RiskCalculatorGeNIe(deviceManager);
//		assertEquals(calculator.calculateRisk(), Risk.ALERT_RISK_VALUE);
		calculator.changeHR(50);
		calculator.changeRR(11);
		calculator.changeSPO2(new Double(99));
		calculator.changeETCO2(new Double(35));

		assertEquals(Risk.LOWER_RISK_VALUE, calculator.calculateRisk());
	}
	
	@Test
	public void updateHRRiskTestGenie() {
		System.out.println("TestRiskCalculationListenerGeNIe.updateHRRiskTestGenie() -- initializing...");
		RiskCalculatorGeNIe calculator = null;
		// create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter);
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor);

		calculator = new RiskCalculatorGeNIe(deviceManager);
		
		calculator.changeETCO2(new Double(35));
		calculator.changeRR(11);
		calculator.changeSPO2(new Double(99));
		calculator.changeHR(45);

		assertEquals(calculator.calculateRisk(), Risk.LOWER_RISK_VALUE);

	}
	
	@Test
	public void updateSpO2RiskTestGenie() {
		System.out.println("TestRiskCalculationListenerGeNIe.updateSpO2RiskTestGenie() -- initializing...");
		RiskCalculatorGeNIe calculator = null;
		// create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter);
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor);

		calculator = new RiskCalculatorGeNIe(deviceManager);
		calculator.changeETCO2(new Double(35));
		calculator.changeHR(50);
		calculator.changeRR(11);
		calculator.changeSPO2(new Double(99));

		assertEquals(Risk.LOWER_RISK_VALUE, calculator.calculateRisk());

	}
	
	@Test
	public void updateRRRiskTestGenie() {
		System.out.println("TestRiskCalculationListenerGeNIe.updateRRRiskTestGenie() -- initializing...");
		RiskCalculatorGeNIe calculator = null;
		// create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter);
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor);

		calculator = new RiskCalculatorGeNIe(deviceManager);
		calculator.changeETCO2(new Double(35));
		calculator.changeHR(50);
		calculator.changeSPO2(new Double(99));
		calculator.changeRR(8);

		assertEquals(Risk.ALERT_RISK_VALUE, calculator.calculateRisk());

	}


}

package riskAssessment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import deviceManager.DeviceManager;
import deviceManager.GenericPulseOximeter;
import deviceManager.GenericRespirationMonitor;
import riskAssessment.Risk;
import riskAssessment.RiskCalculator;
import riskAssessment.RiskCriticalityLevel;
import util.Device;

public class TestRiskCalculationListener {
	
	private RiskCalculator calculator = null;
	
	@Test
	public void updateRiskTest() {
		
	//  create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(myPulseOximeter.getCurrentSpO2()).thenReturn(new Double(60));
		when(myPulseOximeter.getCurrentHeartRate()).thenReturn(-1);
		
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter );
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor );
		
		calculator = new RiskCalculator(deviceManager);
		
		assertEquals(calculator.calculateRisk(),Risk.HIGHTEST_RISK_VALUE);
		myPulseOximeter.setData(97f, Device.PULS_OXIM_SAT_O2);
		assertEquals(calculator.calculateRiskPulseOximetry(),RiskCriticalityLevel.Negligible);
		
	}

}

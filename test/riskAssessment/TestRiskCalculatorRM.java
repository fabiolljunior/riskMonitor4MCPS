package riskAssessment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import deviceManager.DeviceManager;
import deviceManager.GenericPulseOximeter;
import deviceManager.GenericRespirationMonitor;

public class TestRiskCalculatorRM {
	
private RiskCalculator calculator = null;
	
	@Test
	public void calculateRiskETCO2CatastrophicalTest() {
	//  create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(myRespirationMonitor.getCurrentEtCO2()).thenReturn(new Double(20));
		when(myRespirationMonitor.getCurrentRespirationRate()).thenReturn(GenericRespirationMonitor.INVALID_VALUE);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor);
		
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(myPulseOximeter.getCurrentSpO2()).thenReturn(new Double(GenericRespirationMonitor.INVALID_VALUE));
		when(myPulseOximeter.getCurrentHeartRate()).thenReturn(GenericRespirationMonitor.INVALID_VALUE);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter);
		
		calculator = new RiskCalculator(deviceManager);
		
		assertEquals(RiskCriticalityLevel.Catastrophical, calculator.calculateRiskCapnometry());
	}
	
	@Test
	public void calculateRiskRRCatastrophicalTest() {
	//  create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(myRespirationMonitor.getCurrentEtCO2()).thenReturn(new Double(GenericRespirationMonitor.INVALID_VALUE));
		when(myRespirationMonitor.getCurrentRespirationRate()).thenReturn(1);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor);
		
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(myPulseOximeter.getCurrentSpO2()).thenReturn(new Double(GenericRespirationMonitor.INVALID_VALUE));
		when(myPulseOximeter.getCurrentHeartRate()).thenReturn(GenericRespirationMonitor.INVALID_VALUE);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter);
		
		calculator = new RiskCalculator(deviceManager);
		
		assertEquals(RiskCriticalityLevel.Catastrophical, calculator.calculateRiskCapnometry());
	}
	

}

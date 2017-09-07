package riskAssessment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import deviceManager.DeviceManager;
import deviceManager.GenericPulseOximeter;
import deviceManager.GenericRespirationMonitor;

public class TestRiskCalculatorPO {
	
	private RiskCalculator calculator = null;
	
	@Test
	public void calculateRiskPulseOximetryTest() {
	//  create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(myPulseOximeter.getCurrentSpO2()).thenReturn(new Double(60));
		when(myPulseOximeter.getCurrentHeartRate()).thenReturn(-1);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter );
		
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor );
		
		calculator = new RiskCalculator(deviceManager);
		
		assertEquals(calculator.calculateRiskPulseOximetry(),RiskCriticalityLevel.Negligible);
	}
	
	@Test
	public void calculateRiskPulseOximetryTestCritical() {
	//  create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(myPulseOximeter.getCurrentSpO2()).thenReturn(new Double(76));
		when(myPulseOximeter.getCurrentHeartRate()).thenReturn(-1);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter );
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor );
		
		calculator = new RiskCalculator(deviceManager);
		
		assertEquals(calculator.calculateRiskPulseOximetry(),RiskCriticalityLevel.Critical);
	}
	
	@Test
	public void calculateRiskPulseOximetryTestCriticalSerious() {
	//  create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(myPulseOximeter.getCurrentSpO2()).thenReturn(new Double(86));
		when(myPulseOximeter.getCurrentHeartRate()).thenReturn(-1);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter );
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor );
		
		calculator = new RiskCalculator(deviceManager);
		
		assertEquals(calculator.calculateRiskPulseOximetry(),RiskCriticalityLevel.Serious);
	}
	
	@Test
	public void calculateRiskPulseOximetryTestCriticalMinor() {
	//  create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(myPulseOximeter.getCurrentSpO2()).thenReturn(new Double(93));
		when(myPulseOximeter.getCurrentHeartRate()).thenReturn(-1);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter );
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor );
		
		calculator = new RiskCalculator(deviceManager);
		
		assertEquals(calculator.calculateRiskPulseOximetry(),(RiskCriticalityLevel.Minor));
	}
	
	@Test
	public void calculateRiskPulseOximetryTestCriticalMinorNegligible() {
	//  create mock
		DeviceManager deviceManager = mock(DeviceManager.class);
		GenericPulseOximeter myPulseOximeter = mock(GenericPulseOximeter.class);
		when(myPulseOximeter.getCurrentSpO2()).thenReturn(new Double(97));
		when(myPulseOximeter.getCurrentHeartRate()).thenReturn(-1);
		when(deviceManager.getGenericPulseOximeter()).thenReturn(myPulseOximeter );
		GenericRespirationMonitor myRespirationMonitor = mock(GenericRespirationMonitor.class);
		when(deviceManager.getGenericRespirationMonitor()).thenReturn(myRespirationMonitor );
		
		calculator = new RiskCalculator(deviceManager);
		
		assertEquals(calculator.calculateRiskPulseOximetry(),RiskCriticalityLevel.Negligible);
	}

}

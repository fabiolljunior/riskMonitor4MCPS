package riskAssessmentGenie;

import org.junit.Test;

import deviceManager.DeviceManager;

public class TestDeviceManagement {
	
	@Test
	public void testPOonRRoff() { //Test must run OpenICE 
		System.out.println("TestDeviceManagement.testPOonRRoff() - initializing...");
//		DeviceManager.getInstance().getStatusRR();   

		
	}
	
	@Test
	public void testPOoffRRon() {
		DeviceManager.getInstance();
	}
	
	@Test
	public void testPOonRRon() {
		
	}
	
	@Test
	public void testPOoffRRoff() {
		
	}

}

package measurementModel;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.InvalidInputException;

public class MeasurementParserTest {
	
	private static Measurement stringToMeasurement1;
	private static Measurement stringToMeasurement2;
	private static Measurement stringToMeasurement3;
	
	@BeforeClass
	public static void setUp() throws InvalidInputException {
		StringBuffer measurement1 = new StringBuffer("unique_device_identifier: bvkcuL0MtpSHovslPhsXmQR936pYkYvV1ZHg\n"
				+ "metric_id: MDC_PULS_OXIM_PLETH\n" + "vendor_metric_id: " + "instance_id: 0\n"
				+ "unit_id: MDC_DIM_DIMLESS\n" + "frequency: 100\n" + "values :\n"
				+ "userData: 37650.504, 36799.227, 35933.277, 35075.297, 34233.098,"
				+ " 33417.105, 32653.22, 31982.814, 31467.523, 31161.143, 31085.293,"
				+ " 31218.877, 31510.154, 31878.604, 32244.217, 32536.367, 32711.39,"
				+ " 32749.709, 32645.238, 32414.742, 32090.914, 32727.75, 33053.01,"
				+ " 33258.277, 33330.695, 33274.05, 33107.062, 32846.945, 32517.771,"
				+ " 32137.797, 31729.266, 31308.746, 30891.62, 30487.93, 30105.64,"
				+ " 29756.438, 29450.531, 29196.373, 28999.246, 28868.457, 28808.355,"
				+ " 28819.35, 28890.156, 29007.648, 29160.744, 29325.06, 29480.107,"
				+ " 29608.957, 29708.025, 29782.088, 29828.809, 29856.621, 29878.764,"
				+ " 29911.422, 29971.768, 30103.059, 30393.053, 30950.227, 31850.592,"
				+ " 33091.344, 34583.914, 36153.164, 37587.117, 38707.863, 39416.1,"
				+ " 39694.086, 39584.254, 39159.996, 38510.26, 37720.793, 36863.53,"
				+ " 35987.02, 35115.598, 34265.13, 33448.79, 32708.041, 32100.385,"
				+ " 31695.15, 31547.188, 31666.049, 32012.111, 32509.787, 33061.71,"
				+ " 33573.277, 33980.72, 34248.547, 34376.074, 34362.586, 34225.477,"
				+ " 33988.44, 33677.027, 33313.92, 32925.508, 32527.299, 32129.023,"
				+ " 31739.01, 31366.432, 31028.326, 30738.264, 30512.055\n"
				+ "device_time :" + "\nsec: 1502357625" + "\n nanosec: 0\n" 
				+ "presentation_time :\n" + "sec: 1502357625"
				+ "\nnanosec: 0");
		
		StringBuffer measurement2 = new StringBuffer("unique_device_identifier: bvkcuL0MtpSHovslPhsXmQR936pYkYvV1ZHg \n"
				+ "metric_id: MDC_PULS_OXIM_PULS_RATE \n" + "vendor_metric_id: \n" + "instance_id: 0 \n"
				+ "unit_id: MDC_DIM_DIMLESS \n" + "value: 60.0 \n" + "device_time : \n" + " sec: 1502357611 \n"
				+ "nanosec: 0 \n" + "presentation_time : \n" + " sec: 1502357611 \n" + "nanosec: 0");
		
		StringBuffer measurement3 = new StringBuffer("unique_device_identifier: bvkcuL0MtpSHovslPhsXmQR936pYkYvV1ZHg \n"
				+ "metric_id: MDC_PULS_OXIM_SAT_O2 \n" + "vendor_metric_id: \n" + "instance_id: 0 \n"
				+ "unit_id: MDC_DIM_DIMLESS \n" + "value: 98.0 \n" + "device_time : \n" + " sec: 1502357611 \n"
				+ "nanosec: 0 \n" + "presentation_time : \n" + " sec: 1502357611 \n" + "nanosec: 0");
		
		stringToMeasurement1 = MeasurementParser.stringToMeasurement(measurement1.toString());
		stringToMeasurement2 = MeasurementParser.stringToMeasurement(measurement2.toString());
		stringToMeasurement3 = MeasurementParser.stringToMeasurement(measurement3.toString());
	}

	@Test(expected = InvalidInputException.class)
	public void evaluateInvalidInputString() throws InvalidInputException {
		MeasurementParser.stringToMeasurement("");
	}
	
	@Test(expected = InvalidInputException.class)
	public void evaluateNullInputString() throws InvalidInputException {
		MeasurementParser.stringToMeasurement(null);
	}
	
	@Test
	public void evaluateNullInputStringMessage() {
		try {
			MeasurementParser.stringToMeasurement(null);
		} catch (InvalidInputException e) {
			assertEquals("Invalid Measurement Input. Value is null or empty.", e.getMessage());
		}
	}
	
	@Test
	public void evaluateEmptyInputStringMessage() {
		try {
			MeasurementParser.stringToMeasurement("");
		} catch (InvalidInputException e) {
			assertEquals("Invalid Measurement Input. Value is null or empty.", e.getMessage());
		}
	}
	
	@Test
	public void evaluateDeviceIdMeasurement1() {
		assertEquals("bvkcuL0MtpSHovslPhsXmQR936pYkYvV1ZHg", stringToMeasurement1.getDeviceId());
	}
	
	@Test
	public void evaluateMeasurementIdMeasurement1() {
		assertEquals("MDC_PULS_OXIM_PLETH", stringToMeasurement1.getMeasurementId());
	}
	
	@Test
	public void evaluateUnitIdMeasurement1() {
		assertEquals("MDC_DIM_DIMLESS", stringToMeasurement1.getUnitId());
	}
	
	@Test
	public void evaluateTimeStampInMilisecondsMeasurement1() {
		assertEquals(1502357625l, stringToMeasurement1.getTimeStampInMiliseconds());
	}
	
	@Test
	public void evaluateValuesMeasurement1() {
		Double[] values = { 37650.504, 36799.227, 35933.277, 35075.297, 34233.098, 33417.105, 32653.22, 31982.814,
				31467.523, 31161.143, 31085.293, 31218.877, 31510.154, 31878.604, 32244.217, 32536.367, 32711.39,
				32749.709, 32645.238, 32414.742, 32090.914, 32727.75, 33053.01, 33258.277, 33330.695, 33274.05,
				33107.062, 32846.945, 32517.771, 32137.797, 31729.266, 31308.746, 30891.62, 30487.93, 30105.64,
				29756.438, 29450.531, 29196.373, 28999.246, 28868.457, 28808.355, 28819.35, 28890.156, 29007.648,
				29160.744, 29325.06, 29480.107, 29608.957, 29708.025, 29782.088, 29828.809, 29856.621, 29878.764,
				29911.422, 29971.768, 30103.059, 30393.053, 30950.227, 31850.592, 33091.344, 34583.914, 36153.164,
				37587.117, 38707.863, 39416.1, 39694.086, 39584.254, 39159.996, 38510.26, 37720.793, 36863.53, 35987.02,
				35115.598, 34265.13, 33448.79, 32708.041, 32100.385, 31695.15, 31547.188, 31666.049, 32012.111,
				32509.787, 33061.71, 33573.277, 33980.72, 34248.547, 34376.074, 34362.586, 34225.477, 33988.44,
				33677.027, 33313.92, 32925.508, 32527.299, 32129.023, 31739.01, 31366.432, 31028.326, 30738.264,
				30512.055 };
		
		assertEquals(Arrays.asList(values), stringToMeasurement1.getValues());
	}
	
	@Test
	public void evaluateDeviceIdMeasurement2() {
		assertEquals("bvkcuL0MtpSHovslPhsXmQR936pYkYvV1ZHg", stringToMeasurement2.getDeviceId());
	}
	
	@Test
	public void evaluateMeasurementIdMeasurement2() {
		assertEquals("MDC_PULS_OXIM_PULS_RATE", stringToMeasurement2.getMeasurementId());
	}
	
	@Test
	public void evaluateUnitIdMeasurement2() {
		assertEquals("MDC_DIM_DIMLESS", stringToMeasurement2.getUnitId());
	}
	
	@Test
	public void evaluateTimeStampInMilisecondsMeasurement2() {
		assertEquals(1502357611l, stringToMeasurement2.getTimeStampInMiliseconds());
	}
	
	@Test
	public void evaluateValueMeasurement2() {
		assertEquals(new Double(60.0), stringToMeasurement2.getValue());
	}
	
	@Test
	public void evaluateDeviceIdMeasurement3() {
		assertEquals("bvkcuL0MtpSHovslPhsXmQR936pYkYvV1ZHg", stringToMeasurement3.getDeviceId());
	}
	
	@Test
	public void evaluateMeasurementIdMeasurement3() {
		assertEquals("MDC_PULS_OXIM_SAT_O2", stringToMeasurement3.getMeasurementId());
	}
	
	@Test
	public void evaluateUnitIdMeasurement3() {
		assertEquals("MDC_DIM_DIMLESS", stringToMeasurement3.getUnitId());
	}
	
	@Test
	public void evaluateTimeStampInMilisecondsMeasurement3() {
		assertEquals(1502357611l, stringToMeasurement3.getTimeStampInMiliseconds());
	}
	
	@Test
	public void evaluateValueMeasurement3() {
		assertEquals(new Double(98.0), stringToMeasurement3.getValue());
	}

}

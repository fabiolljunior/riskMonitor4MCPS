package measurementModel;

public class Measurement {
	
	private String measurementId;
	private String unitId;
	private String deviceId;
	private long timeStampInMiliseconds;
	private double value;
	
	public String getMeasurementId() {
		return measurementId;
	}
	public void setMeasurementId(String measurementId) {
		this.measurementId = measurementId;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public long getTimeStampInMiliseconds() {
		return timeStampInMiliseconds;
	}
	public void setTimeStampInMiliseconds(long timeStampInMiliseconds) {
		this.timeStampInMiliseconds = timeStampInMiliseconds;
	}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
	

}

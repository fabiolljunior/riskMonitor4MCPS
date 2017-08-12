package measurementModel;

public enum VARIABLE_INPUT_DEFAULT {
	
	UNIQUE_DEVICE_ID("unique_device_identifier"),
	METRIC_ID("metric_id"),
	VENDOR_ID("vendor_metric_id"),
	UNIT_ID("unit_id"),
	FREQUENCY("frequency"),
	DEVICE_TIME("device_time"),
	VALUES("values"),
	USER_DATA("userData"),
	SEC("sec"),
	NANOSEC("nanosec"),
	VALUE("value");
	
	private String name;
	
	VARIABLE_INPUT_DEFAULT(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

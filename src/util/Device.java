package util;

public enum Device {
	
	PULS_OXIM_SAT_O2("MDC_PULS_OXIM_SAT_O2"),
	PULS_OXIM_PULS_RATE("MDC_PULS_OXIM_PULS_RATE"),
	CO2_RESP_RATE("MDC_CO2_RESP_RATE");
	
	private String name;
	
	Device(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

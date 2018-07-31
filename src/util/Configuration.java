package util;

public enum Configuration {
	
	Config1("Configuration1"),
	Config2("Configuration2"),
	Config3("Configuration3"),
	Config_OFF("Configuration_OFF");
	
	private String name;
	
	Configuration(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

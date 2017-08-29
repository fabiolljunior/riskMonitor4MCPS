package riskAssessment;

public class Risk {
	
	private int value;
	
	public Risk(int value) {
		this.value = value;
	}
	
	public Risk() {
		this.value = 0;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object arg0) {
		return this.value == ((Risk)arg0).getValue();
	}
	
	

}

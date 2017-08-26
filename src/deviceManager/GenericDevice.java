package deviceManager;

public class GenericDevice {
	
	protected static final int TIME_TO_DEAD = 5;
	
	protected Double data;
	protected int secondsWithoutData = 0;
	
	public Double getData() {
		return this.data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	public int getSecondsWithoutData() {
		return secondsWithoutData;
	}

	public void setSecondsWithoutData(int secondsWithoutData) {
		this.secondsWithoutData = secondsWithoutData;
	}
	
	public void resetSecondsWithOutData() {
		this.secondsWithoutData = 0;
	}
	
	public void updateSecondsWithOutData() {
		this.secondsWithoutData++;
	}
}

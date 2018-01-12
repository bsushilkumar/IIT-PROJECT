package gui.entities.sectionEntities.signal;

public class SignalEntry {
	private double startTime;
	private double endTime;
	private int signalValue;

	public SignalEntry(double startTime, double endTime, int signalValue) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.signalValue = signalValue;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

	public int getSignalValue() {
		return signalValue;
	}

	public void setSignalValue(int signalValue) {
		this.signalValue = signalValue;
	}

}

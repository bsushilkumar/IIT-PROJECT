package gui.entities.sectionEntities.trackProperties;

/**
 * @author devendra
 * 
 */
public class BlockInterval {

	/**
	 * startTime
	 */
	public double startTime;
	/**
	 * endTime
	 */
	private double endTime;
	/**
	 * direction
	 */
	int direction;

	/**
	 * @param intervalStartTime
	 * @param intervalEndTime
	 * @param blockDirection
	 */
	public BlockInterval(double intervalStartTime, double intervalEndTime,
			int blockDirection) {
		startTime = Math.min(intervalStartTime, intervalEndTime);
		setEndTime(Math.max(intervalStartTime, intervalEndTime));
		direction = blockDirection;
	}

	// Devendra
	/**
	 * @param time
	 * @return true if the time lies in the interval
	 */
	public boolean isTimeInInterval(double time) {
		if (time >= this.startTime && time <= this.getEndTime()) {
			return true;
		}
		return false;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}
}

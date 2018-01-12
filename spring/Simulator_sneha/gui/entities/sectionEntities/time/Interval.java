package gui.entities.sectionEntities.time;

/* ------ interval is used to store the intervals, start time and end
 time of various dynamic properties, for example - block section / loop
 occupancy etc. the object of type interval are used in arraylists to
 retrive and store the dynamic data
 --------------------------*/

/**
 * interval
 */
public class Interval {
	/**
	 * startTime
	 */
	private double startTime; // start time
	/**
	 * endTime
	 */
	private double endTime; // end time
	/**
	 * velocity
	 */
	double velocity;
	/**
	 * trainNo
	 */
	private int trainNo;
	private boolean actualOccupancy = false;
	private boolean lock = false;

	/**
 * 
 */
	Interval() {
	}

	/**
	 * @param a
	 * @param b
	 */
	public Interval(double a, double b) {

		if (a > b) {
			setStartTime(b);
			setEndTime(a);
		}
		if (a < b) {
			setStartTime(a);
			setEndTime(b);
		}
		if (a == b) {
			setStartTime(a);
			setEndTime(b);
		}
	}

	/**
	 * @param no
	 * @param a
	 * @param b
	 * @param actualOccupancy
	 */
	public Interval(int no, double a, double b, boolean actualOccupancy) {
		setTrainNo(no);
		if (a > b) {
			setStartTime(b);
			setEndTime(a);
		}
		if (a < b) {
			setStartTime(a);
			setEndTime(b);
		}
		if (a == b) {
			setStartTime(a);
			setEndTime(b);
		}

		this.setActualOccupancy(actualOccupancy);
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 */
	Interval(double a, double b, double c) {
		velocity = c;
		if (a > b) {
			setStartTime(b);
			setEndTime(a);
		}
		if (a < b) {
			setStartTime(a);
			setEndTime(b);
		}
		if (a == b) {
			setStartTime(a);
			setEndTime(b);
		}
	}

	// Devendra
	/**
	 * @param time
	 * @return true if the time lies in the interval
	 */
	public boolean isTimeInInterval(double time) {
		if (time >= this.getStartTime() && time <= this.getEndTime()) {
			return true;
		}
		return false;
	}

	public int getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(int trainNo) {
		this.trainNo = trainNo;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getTime() {
		// TODO Auto-generated method stub
		return endTime - startTime;
	}

	public boolean isActualOccupancy() {
		return actualOccupancy;
	}

	public void setActualOccupancy(boolean actualOccupancy) {
		this.actualOccupancy = actualOccupancy;
	}

	public boolean isLocked() {
		// TODO Auto-generated method stub
		return lock;
	}

	public void setLock(boolean b) {
		lock = b;
	}
}
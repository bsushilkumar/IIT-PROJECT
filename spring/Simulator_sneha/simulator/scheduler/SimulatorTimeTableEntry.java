package simulator.scheduler;

import simulator.velocityProfileCalculation.VelocityProfileArray;

/**
 * 
 */
public class SimulatorTimeTableEntry {
	/**
	 * loopNo
	 */
	private int loopNo;
	/**
	 * arrivalTime
	 */
	private double arrivalTime;
	/**
	 * departureTime
	 */
	private double departureTime;
	/**
	 * signal
	 */
	private int signal = 0;
	/**
	 * startMilePost
	 */
	private double startMilePost;
	/**
	 * endMilePost
	 */
	private double endMilePost;
	/**
	 * svelo
	 */
	private double startVelocity;
	/**
	 * velocityProfileArray
	 */
	private VelocityProfileArray velocityProfileArray;


//	/**
//	 * @param loopNo
//	 * @param time1
//	 * @param time2
//	 * @param startDistance
//	 * @param endDistance
//	 * @param veloArray
//	 */
//	SimulatorTimeTableEntry(int loopNo, double time1, double time2,
//			double startDistance, double endDistance,
//			VelocityProfileArray veloArray) {
//		this.setLoopNo(loopNo);
//		if (time1 > time2) {
//			setArrivalTime(time2);
//			setDepartureTime(time1);
//		}
//
//		if (time1 <= time2) {
//			setArrivalTime(time1);
//			setDepartureTime(time2);
//		}
//
//		setStartMilePost(startDistance);
//		setEndMilePost(endDistance);
//		setVelocityProfileArray(veloArray);
//	}

	/**
	 * @param loopNo
	 * @param time1
	 * @param time2
	 * @param startDistance
	 * @param endDistance
	 * @param veloArray
	 * @param sig
	 */

	SimulatorTimeTableEntry(int loopNo, double time1, double time2,
			double startDistance, double endDistance,
			VelocityProfileArray velocityProfileArray, int sig) {
//		this(loopNo, time1, time2, startDistance, endDistance,
//				velocityProfileArray);
		this.setLoopNo(loopNo);
		if (time1 > time2) {
			setArrivalTime(time2);
			setDepartureTime(time1);
		}

		if (time1 <= time2) {
			setArrivalTime(time1);
			setDepartureTime(time2);
		}

		setStartMilePost(startDistance);
		setEndMilePost(endDistance);
		setVelocityProfileArray(velocityProfileArray);
		setSignal(sig);
	}

	/**
     * 
     */
	public void print() {
		System.out.println("Loop No: " + getLoopNo() + " Start MileP: "
				+ getStartMilePost() + " End Milep: " + getEndMilePost()
				+ " Arrival Time " + getArrivalTime() + " Departure Time: "
				+ getDepartureTime());
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public double getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(double departureTime) {
		this.departureTime = departureTime;
	}

	public int getLoopNo() {
		return loopNo;
	}

	public void setLoopNo(int loopNo) {
		this.loopNo = loopNo;
	}

	public double getStartMilePost() {
		return startMilePost;
	}

	public void setStartMilePost(double startMilePost) {
		this.startMilePost = startMilePost;
	}

	public double getEndMilePost() {
		return endMilePost;
	}

	public void setEndMilePost(double endMilePost) {
		this.endMilePost = endMilePost;
	}

	public VelocityProfileArray getVelocityProfileArray() {
		return velocityProfileArray;
	}

	public void setVelocityProfileArray(
			VelocityProfileArray velocityProfileArray) {
		this.velocityProfileArray = velocityProfileArray;
	}

	public int getSignal() {
		return signal;
	}

	public void setSignal(int signal) {
		this.signal = signal;
	}

	public double getStartVelocity() {
		return startVelocity;
	}

	public void setStartVelocity(double startVelocity) {
		this.startVelocity = startVelocity;
	}

	public double getTime() {
		// TODO Auto-generated method stub
		return getDepartureTime() - getArrivalTime();
	}
}
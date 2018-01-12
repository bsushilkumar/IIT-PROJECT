package simulator.scheduler;

/**
 * 
 */
public class StatusTraverseBlock {
	/**
	 * status
	 */
	public boolean status;
	/**
	 * limit
	 */
	public boolean limit = false; // added by uhp on May17
	/**
	 * trainNo
	 */
	public int trainNo;
	/**
	 * departureTime
	 */
	public double departureTime;

	public StatusTraverseBlock() {

	}

	/**
	 * @param st
	 * @param time
	 * @param no
	 */
	public StatusTraverseBlock(boolean status, double time, int trainNo) {
		this.status = status;
		this.departureTime = time;
		this.trainNo = trainNo;
	}

	/**
	 * @param _status
	 * @param _limit
	 * @param _trainNo
	 */
	public StatusTraverseBlock(boolean status, boolean limit, int trainNo) {
		this.trainNo = trainNo;
		this.status = status;
		this.limit = limit;
	}

}

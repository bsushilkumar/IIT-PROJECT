package simulator.scheduler;

/**
 * 
 */
public class StatusTraversePath extends StatusTraverseBlock {
	public static final int BLOCK_RESERVATION_FAILURE = -1;
	public static final int SIGNAL_FAILURE = -2;
	/**
	 * signal : -1 value indicates that the scheduling of the train for next
	 * block scheduling was not successful. -2 value indicates that there was
	 * signal failure took place on some block along the path
	 */
	int signal;
	/**
	 * arrivalTime
	 */
	double arrivalTime;

	/**
	 * @param st
	 * @param arrtime
	 * @param deptime
	 * @param sig
	 */
	public StatusTraversePath(boolean status, double arrivaltime,
			double departuretime, int signal) {
		this.status = status;
		this.departureTime = departuretime;
		this.arrivalTime = arrivaltime;
		this.signal = signal;
	}

}
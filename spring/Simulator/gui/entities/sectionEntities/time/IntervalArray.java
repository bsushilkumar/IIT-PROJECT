package gui.entities.sectionEntities.time;

import java.util.ArrayList;

import simulator.util.Debug;

/*we can change the get method for the seeing to it that the occupy array can be concatenated when one
 * ends and the other starts at the same time.*/

/**
 * IntervalArray for list of intervals.
 */
@SuppressWarnings({ "serial" })
public class IntervalArray extends ArrayList<Interval> {
	/**
	 * Constructor.
	 */
	public IntervalArray() {
	}

	/**
	 * Print all the intervals in the intervalArray.
	 */
	public void printInterval() {
		for (int i = 0; i < size(); i++) {
			Interval interval = get(i);
			Debug.fine("In Interval start Time " + interval.getStartTime()
					+ " dept time " + interval.getEndTime() + " trainNo "
					+ interval.getTrainNo());
		}
	}

	/**
	 * Get total time in each interval of the IntervalArray.
	 * 
	 * @return double time between intervals
	 */
	public double totalInterval() {
		double timeInInterval = 0;
		for (int i = 0; i < size(); i++) {
			Interval interval = get(i);
			timeInInterval += interval.getEndTime() - interval.getStartTime();
		}
		return timeInInterval;
	}

	/**
	 * @param time
	 *            time to check in which interval
	 * @return the interval in which the given time lies. If there is no such
	 *         interval then returns -1.
	 */
	public int inInterval(double time) {
		Interval interval;
		for (int i = 0; i < size(); i++) {
			interval = get(i);
			if ((time >= interval.getStartTime())
					&& (time <= interval.getEndTime())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param startTime
	 *            starting time to check
	 * @param endTime
	 *            ending time to check
	 * 
	 * @return the interval in which the time period between startTime and
	 *         endTime lies
	 */
	public int inInterval(double startTime, double endTime) {
		Debug.info("inInterval - start time " + startTime + " end time " + endTime);
		 printInterval();
		Interval interval;
		for (int count = 0; count < size(); count++) {
			interval = get(count);
			double intervalStartTime = interval.getStartTime();
			double intervalEndTime = interval.getEndTime();
			int trainNo = interval.getTrainNo();

			if (!((intervalEndTime <= startTime) || (intervalStartTime >= endTime))) {
				Debug.info("inInterval: trainNo " + trainNo
						+ " intervalStartTime " + intervalStartTime
						+ " intervalEndTime " + intervalEndTime
						+ " probeStartTime " + startTime + " probeEndTime "
						+ endTime);
				return count;
			}
		}
		
		return -1;

	}

}

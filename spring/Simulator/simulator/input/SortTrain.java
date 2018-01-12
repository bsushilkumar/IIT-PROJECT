package simulator.input;

import gui.entities.trainEntities.Train;

import java.util.Comparator;

/**
 * 
 */

@SuppressWarnings("unchecked")
public class SortTrain implements Comparator<Train> {

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Train train1, Train train2) {
		int var = 0;
		double startTime1, startTime2, departTime1, departTime12;
		double priority1, priority2;
		boolean isScheduled1 = false, isScheduled2 = false;

		priority1 = train1.getPriority();
		priority2 = train2.getPriority();
		startTime1 = train1.getStartTime();
		startTime2 = train2.getStartTime();
		departTime1 = train1.getDepartTime();
		departTime12 = train2.getDepartTime();
		isScheduled1 = train1.isScheduled();
		isScheduled2 = train2.isScheduled();

		System.out.println("SortTrain.Compare: train1" + train1.getTrainNo()+
							" Train 2: "+ train2.getTrainNo());
		if (train1.getTrainNo() < train2.getTrainNo())
			return -1;
		else if(train1.getTrainNo() > train2.getTrainNo())
			return 1;

		if (priority1 > priority2) {
			var = 1;
		} else if (priority1 < priority2) {
			var = -1;
		} else if (priority1 == priority2) {
			if (isScheduled1 == true && isScheduled2 == true) {
				if (departTime1 > departTime12) {
					var = 1;
				} else if (departTime1 < departTime12) {
					var = -1;
				} else {
					var = 0;
				}
			} else {
				if (startTime1 > startTime2) {
					var = 1;
				} else if (startTime1 < startTime2) {
					var = -1;
				} else {
					var = 0;
				}
			}
		}

		return var;
	}

}

/**
 * sorter for the third track.
 */

class SortTrainWithTimeInterval implements Comparator<Train> {

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 *      This will help in sorting the trains as per the scheduling of the
	 *      third track as the up track or the down track.
	 */
	public int compare(Train train1, Train train2) {
		int var = 0;
		double startTime1, startTime2, departTime1, departTime12;
		double priority1, priority2;
		boolean isScheduled1 = false, isScheduled2 = false;
		priority1 = train1.getPriority();
		priority2 = train2.getPriority();
		startTime1 = train1.getStartTime();
		startTime2 = train2.getStartTime();
		departTime1 = train1.getDepartTime();
		departTime12 = train2.getDepartTime();
		isScheduled1 = train1.isScheduled();
		isScheduled2 = train2.isScheduled();

		if (priority1 > priority2) {
			var = 1;
		} else if (priority1 < priority2) {
			var = -1;
		} else if (priority1 == priority2) {
			if (isScheduled1 == true && isScheduled2 == true) {
				if (departTime1 > departTime12) {
					var = 1;
				} else if (departTime1 < departTime12) {
					var = -1;
				} else {
					var = 0;
				}
			} else {
				if (startTime1 > startTime2) {
					var = 1;
				} else if (startTime1 < startTime2) {
					var = -1;
				} else {
					var = 0;
				}
			}
		}

		return var;
	}
}


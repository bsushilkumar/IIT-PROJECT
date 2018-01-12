package gui.entities.sectionEntities.trackEntities;

import globalVariables.GlobalVariables;
import gui.entities.sectionEntities.Entity;
import gui.entities.sectionEntities.time.Interval;
import gui.entities.sectionEntities.time.IntervalArray;
import gui.entities.sectionEntities.trackProperties.TinyBlockList;
import gui.entities.trainEntities.Train;

import java.util.HashSet;
import java.util.Set;

import simulator.util.Debug;

public abstract class TrackSegment extends Entity {
	public double startMilePost;
	public double endMilePost;
	public double maximumPossibleSpeed;
	protected int direction;

	/**
	 * occupy
	 */
	protected IntervalArray occupy = new IntervalArray();
	/**
	 * tinyBlockList
	 */
	private TinyBlockList tinyBlockList = new TinyBlockList();

	protected double length = 0;

	public TrackSegment(double startMilepost, double endMilepost,
			double maximumSpeed) {
		this.setStartMilePost(startMilepost);
		this.setEndMilePost(endMilepost);
		this.setMaximumPossibleSpeed(maximumSpeed);
	}

	public TrackSegment(double startMilePost, double endMilePost) {
		this.setStartMilePost(startMilePost);
		this.setEndMilePost(endMilePost);
	}

	public TrackSegment() {

	}

	public void setStartMilePost(double startMilepost) {
		this.startMilePost = GlobalVariables
				.roundToThreeDecimals(startMilepost);
	}

	public double getStartMilePost() {
		return startMilePost;
	}

	public void setEndMilePost(double endMilepost) {
		this.endMilePost = GlobalVariables.roundToThreeDecimals(endMilepost);
	}

	public double getEndMilePost() {
		return endMilePost;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	public IntervalArray getOccupy() {
		return occupy;
	}

	public IntervalArray getOccupanciesToDisplay() {

		if (GlobalVariables.showCondensedOccupyBlocks) {
			IntervalArray intervalArray = new IntervalArray();
			Set<Integer> trainNumbers = new HashSet<Integer>();
			for (Interval interval : occupy) {
				trainNumbers.add(interval.getTrainNo());
			}

			for (Integer trainNumber : trainNumbers) {
				// 7 days, 24 hours, 60 minutes
				double minimumBlockReserveTime = 7 * 24 * 60;
				double maximumBlockReserveTime = -7 * 24 * 60;

				for (Interval interval : occupy) {
					if (interval.getTrainNo() == trainNumber) {
						double startTime = interval.getStartTime();
						double endTime = interval.getEndTime();

						minimumBlockReserveTime = startTime < minimumBlockReserveTime ? startTime
								: minimumBlockReserveTime;
						maximumBlockReserveTime = endTime > maximumBlockReserveTime ? endTime
								: maximumBlockReserveTime;
					}

				}

				Interval condensedInterval = new Interval(trainNumber,
						minimumBlockReserveTime, maximumBlockReserveTime, false);
				intervalArray.add(condensedInterval);
			}

			return intervalArray;
		} else
			return occupy;
	}

	public void setOccupy(IntervalArray occupy) {
		this.occupy = occupy;
	}

	/**
	 * @param trainNo
	 * @param actualOccupancy
	 * @param time1
	 * @param time2
	 */
	public void setOccupied(Train train, double startTime, double endTime,
			boolean actualOccupancy, String blockName) {

		Debug.fine("trackSegment: setOccupied: " + blockName + " trainNo "
				+ train.getTrainNo() + " arrival " + startTime + " departure "
				+ endTime);
		getOccupy().add(
				new Interval(train.getTrainNo(), startTime, endTime,
						actualOccupancy));

	}

	/**
	 * @param time1
	 * @param time2
	 */
	public void setOccupied(double time1, double time2) {
		Debug.print("Block: setOccupied: with 2 parameters(topaz_K)");
		getOccupy().add(new Interval(time1, time2));
	}

	/**
	 * @param startTime
	 * @param endTime
	 * @return isFree
	 */
	public int isFree(double startTime, double endTime) {
		return getOccupy().inInterval(startTime, endTime);
	}

	/**
	 * @param time
	 * @return isFree
	 */
	public int isFree(double time) {
		return getOccupy().inInterval(time);
	}

	public double getLength() {
		return GlobalVariables.roundToThreeDecimals(Math.abs(getStartMilePost()
				- getEndMilePost()));
	}

	// public TinyBlockList getTinyBlock() {
	// return getTinyBlockList();
	// }

	// public void setTinyBlock(TinyBlockList tinyBlockList) {
	// this.setTinyBlockList(tinyBlockList);
	// }

	public double getMaximumPossibleSpeed() {
		return maximumPossibleSpeed;
	}

	public void setMaximumPossibleSpeed(double maximumPossibleSpeed) {
		this.maximumPossibleSpeed = maximumPossibleSpeed;
	}

	public TinyBlockList getTinyBlockList() {
		return tinyBlockList;
	}

	public void setTinyBlockList(TinyBlockList tinyBlockList) {
		this.tinyBlockList = tinyBlockList;
	}

	public void setLength(double length) {
		this.length = GlobalVariables.roundToThreeDecimals(length);
	}

}

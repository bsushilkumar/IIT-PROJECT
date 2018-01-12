package gui.entities.sectionEntities.signal;

import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Entity;
import gui.entities.sectionEntities.time.Interval;
import gui.entities.sectionEntities.time.IntervalArray;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.trainEntities.Train;

import java.awt.Color;

import simulator.input.SimulationInstance;
import simulator.util.Debug;

/**
 * signal
 */
public class Signal extends Entity {

	private int signalValue = GlobalVar.RED_SIGNAL;
	
	private String signalId = "S-";

	private int numberOfAspects = 4;

	/**
	 * constructor
	 */
	public Signal() {
	}

	/**
	 * 1) It first calls block.isFree(arrivalTime) which in return calls
	 * occupy.inInterval(time) IntervalArray: int inInterval(double time)
	 * returns the interval in the array which contains the given time argument
	 * or returns -1 if no such interval exists. 2) If no such interval exists
	 * then the function checks if according to the reference time table and if
	 * the train is a scheduled train has to wait. It then returns yellow
	 * meaning it has to wait. If it is a freight train, it checks if the next
	 * block is free to what extent i.e. it recursively calls for
	 * nextBlock.signal(currTrain, noOfColor -1, arrivalTime, direction) and
	 * returns a (signal+1). 3) Else, meaning there exists an interval in which
	 * the block is occupied, hence it returns a red signal, that is 0.
	 * 
	 * @param currentBlock
	 * @param currTrain
	 * @param noOfColor
	 * @param arrivalTime
	 * @param consideringHalts
	 * @param maxSignalAspectsToBeConsidered
	 * @param direction
	 * @return getSignal
	 */

	public int getSignal(Block currentBlock, Train currTrain,
			double arrivalTime, boolean consideringHalts,
			int maxSignalAspectsToBeConsidered,
			SimulationInstance simulationInstance) {
		int direction = currTrain.getDirection();
		Debug.fine("getSignal: " + currentBlock.getBlockNo() + " "
				+ currentBlock.getStartMilePost() + " "
				+ currentBlock.getEndMilePost() + " " + currTrain.getTrainNo()
				+ " " + arrivalTime + " " + direction);

		int trainDirection = currTrain.getDirection();
		Block block = currentBlock;
		int signalValue = 0;
		int maxSignalValue = Math.min(numberOfAspects,
				maxSignalAspectsToBeConsidered) - 1;
		for (; signalValue < maxSignalValue; signalValue++) {
			Debug.fine("getSignal: " + block.getBlockName()
					+ " maxSignalValue " + maxSignalValue + " signalValue "
					+ signalValue);
			int intervalNo;
			if (!block.isDirectionOk(currTrain, arrivalTime)) {
				// Devendra- if the direction of the block is not same as the
				// direction
				// of train at arrival time signal should be red
				break;
			} else if (!block.allowsTrain(currTrain, arrivalTime,
					simulationInstance.TRAIN_TYPE_ALLOWED_ON_THIRD_LINE)) {
				// code for controlling third line for suburban trains
				// only or long distance trains only
				break;
			} else if ((intervalNo = block.isFree(arrivalTime)) != -1) {
				IntervalArray intervalArray = block.getOccupy();
				Interval interval = intervalArray.get(intervalNo);
				if (interval.getTrainNo() == currTrain.getTrainNo())
					continue;
				else
					// block is not available
					break;
			} else if (block.isLoop()) {
				Loop loop = (Loop) block;

				boolean hasScheduledHalt = currTrain.hasScheduledHalt(loop,
						simulationInstance);

				if (consideringHalts && hasScheduledHalt) {
					signalValue++;
					break;
				}
			}

			block = block.getNextBlock(currTrain);

			if (block == null) {
				signalValue++;
				break;
			} else {
				int nSignalAspects = block.getBlockSignal(trainDirection)
						.getNumberOfAspects();
				maxSignalValue = Math.min(maxSignalValue, signalValue
						+ (nSignalAspects - 1));
			}
		}

		Debug.fine("Signal count is " + signalValue);
		return signalValue;
	}

	public int getNumberOfAspects() {
		return numberOfAspects;
	}

	@Override
	public boolean hasError() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getSignalValue() {
		return signalValue;
	}

	public void setSignalValue(int signalValue) {
		this.signalValue = signalValue;
	}

	public static Color getSignalColor(int signalSeenByTrain,
			int numberOfSignalAspects) {
		Color signalColor = null;

		if (numberOfSignalAspects == 4) {
			signalColor = (6 == signalSeenByTrain) ? Color.pink
					: (3 == signalSeenByTrain) ? Color.green
							: ((2 == signalSeenByTrain) ? Color.blue
									: ((1 == signalSeenByTrain) ? Color.yellow
											: Color.red));
		} else if (numberOfSignalAspects == 3) {
			signalColor = (6 == signalSeenByTrain) ? Color.pink
					: (2 == signalSeenByTrain) ? Color.green
							: (((1 == signalSeenByTrain) ? Color.yellow
									: Color.red));
		} else if (numberOfSignalAspects == 2) {
			signalColor = (6 == signalSeenByTrain) ? Color.pink
					: (0 == signalSeenByTrain) ? Color.red
							: (1 == signalSeenByTrain) ? Color.green
									: Color.green;
		} else {
			signalColor = Color.green;
		}
		return signalColor;
	}

	public void setNumberOfSignalAspects(int nSignalAspects) {
		this.numberOfAspects = nSignalAspects;
	}

}
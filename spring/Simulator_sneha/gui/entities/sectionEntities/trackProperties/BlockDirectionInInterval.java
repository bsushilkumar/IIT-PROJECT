package gui.entities.sectionEntities.trackProperties;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.trackEntities.Block;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import simulator.input.ChangeTimeTable;
import simulator.input.SimulationInstance;
import simulator.util.Debug;

/**
 * This class is to read the list of time instances when the block direction is
 * reversed.
 * */
public class BlockDirectionInInterval {

	/**
	 * timeOfDirectionSwitching list of time instances at which the direction of
	 * the block is changed. We always consider the first time instance
	 * indicates the change of direction from UP to DOWN.
	 */
	public ArrayList<BlockInterval> blockIntervalArray;

	/**
	 * 
	 */
	public BlockDirectionInInterval() {
		blockIntervalArray = new ArrayList<BlockInterval>();
	}

	/**
	 * For reading the BlockDirectionInInterval file
	 * 
	 * @param hasNoGUI
	 * 
	 * @throws IOException
	 */
	public static void readBlockDirectionInIntervalFile(
			SimulationInstance simulationInstance) throws IOException {

		Reader reader;

		// Devendra
		reader = new FileReader(FileNames.getBlockDirectionFileName());

		StreamTokenizer streamTokenizer = new StreamTokenizer(reader);

		streamTokenizer.slashSlashComments(false);

		// debug
		String debugCommentStart = "BlockDirectionInInterval: readBlockDirectionInIntervalFile: ";

		while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {

			// read the block number
			double blockValue = streamTokenizer.nval;
			int blockNumber = (int) blockValue;

			Debug.print(debugCommentStart + "blockNumber " + blockNumber);

			// read the string of list of startTimes of BlockIntervals
			streamTokenizer.nextToken();
			String startTimeListString = streamTokenizer.sval;

			// read the string of list of endTimes of BlockIntervals
			streamTokenizer.nextToken();
			String endTimeListString = streamTokenizer.sval;

			// read the string of list of directions of BlockIntervals
			streamTokenizer.nextToken();
			String directionListString = streamTokenizer.sval;

			Debug.print(debugCommentStart + directionListString + " "
					+ streamTokenizer.ttype);

			// insert the timeChangeList into the above block
			addBlockDirectionInIntervalToBlock(blockNumber,
					startTimeListString, endTimeListString,
					directionListString, simulationInstance);
		}
	}

	/**
	 * Add the list of timeChanges of direction switching to the block
	 * 
	 * @param blockNumber
	 * @param startTimeListString
	 * @param endTimeListString
	 * @param directionString
	 * @param simulationInstance
	 */
	public static void addBlockDirectionInIntervalToBlock(int blockNumber,
			String startTimeListString, String endTimeListString,
			String directionString, SimulationInstance simulationInstance) {

		String debugCommentStart = "BlockDirectionInInterval: addBlockDirectionInInterval: ";
		StringTokenizer startTimeStringTokenizer = new StringTokenizer(
				startTimeListString, ",");
		StringTokenizer endTimeStringTokenizer = new StringTokenizer(
				endTimeListString, ",");
		StringTokenizer directionStringTokenizer = new StringTokenizer(
				directionString, ",");

		// find the block
		Block block = simulationInstance.getBlockFromBlockNo(blockNumber);
		if (block == null) {
			throw new NullPointerException(debugCommentStart + "block is null");
		}

		// instantiate BlockDirectionInInterval
		BlockDirectionInInterval blockDirectionInInterval = new BlockDirectionInInterval();
		ArrayList<BlockInterval> blockIntervalArray = blockDirectionInInterval.blockIntervalArray;

		// read the time changes
		while (startTimeStringTokenizer.hasMoreTokens()) {

			// read the startTime value
			String value = startTimeStringTokenizer.nextToken();
			int startTime = Integer.parseInt(value);
			startTime = ChangeTimeTable.changeToMin(startTime);
			
			// read the endTime value
			value = endTimeStringTokenizer.nextToken();
			int endTime = Integer.parseInt(value);
			endTime = ChangeTimeTable.changeToMin(endTime);

			// read the direction value
			String dirString = directionStringTokenizer.nextToken();
			int direction = GlobalVar.NONE_DIRECTION;
			if (dirString.equalsIgnoreCase("up")) {
				direction = GlobalVar.UP_DIRECTION;
			} else if (dirString.equalsIgnoreCase("down")) {
				direction = GlobalVar.DOWN_DIRECTION;
			} else if (dirString.equalsIgnoreCase("common")) {
				direction = GlobalVar.COMMON_DIRECTION;
			} else if (dirString.equalsIgnoreCase("none")) {
				direction = GlobalVar.NONE_DIRECTION;
			}

			BlockInterval blockInterval = new BlockInterval(startTime, endTime,
					direction);

			// insert
			blockIntervalArray.add(blockInterval);
		}

		block.setBlockDirectionInInterval(blockDirectionInInterval);
	}

	// Devendra
	/**
	 * Check if train direction is same as block direction at a given time
	 * 
	 * @param trainDirection
	 * @param arrivalTime
	 * @return true if train direction is same as block direction
	 */
	public boolean isDirectionOk(int trainDirection, double arrivalTime) {

		BlockInterval blockInterval = this
				.getBlockIntervalFromTime(arrivalTime);
		if (blockInterval == null) {
			// direction not specified hence is common at arrival time. So true.
			return true;
		}

		// arrival time is in the blockInterval
		if (trainDirection == blockInterval.direction
				|| blockInterval.direction == GlobalVar.COMMON_DIRECTION) {
			// train direction is same as the block's direction
			return true;
		}
		// block direction explicitly specified and is not the same as train
		// direction
		return false;

	}

	/**
	 * @param time
	 * @return the blockInterval in which the time lies. If there is no such
	 *         blockInterval return null.
	 */
	public BlockInterval getBlockIntervalFromTime(double time) {
		for (int index = 0; index < this.blockIntervalArray.size(); index++) {
			BlockInterval blockInterval = this.blockIntervalArray.get(index);
			if (blockInterval.isTimeInInterval(time)) {
				return blockInterval;
			}
		}
		return null;
	}

	/**
	 * @param arrivalTime
	 * @param direction
	 * @return the earliest time in which the train's direction is not
	 *         conflicting with the block's direction
	 */
	public double getEarliestArrivalTimeFromBlockInterval(double arrivalTime,
			int direction) {

		for (int index = 0; index < this.blockIntervalArray.size(); index++) {
			BlockInterval blockInterval = this.blockIntervalArray.get(index);
			if (blockInterval.isTimeInInterval(arrivalTime)) {
				if (direction == blockInterval.direction
						|| blockInterval.direction == GlobalVar.COMMON_DIRECTION)
					return arrivalTime;
				else
					return blockInterval.getEndTime();
			}
		}

		return arrivalTime;
	}
}

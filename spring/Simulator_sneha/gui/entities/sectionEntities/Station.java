package gui.entities.sectionEntities;

import globalVariables.GlobalVar;
import globalVariables.GlobalVariables;
import gui.entities.sectionEntities.time.Interval;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Link;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.trainEntities.Train;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Comparator;

import simulator.input.SimulationInstance;
import simulator.scheduler.StatusTraverseBlock;
import simulator.util.Debug;

public class Station extends Entity implements Comparator<Station> {
	public double startMilePost = 0;
	public double endMilePost = 0.01;
	private String stationCode = "";
	public String stationName = "";
	public double stationVelocity = 50;

	/**
	 * previousStation
	 */
	public Station previousStation = null;
	/** prevStn will store the next station in the down(1) direction */
	public Station nextStation = null;
	/** nextStn will store the next station in the up(0) direction */

	ArrayList<Loop> Loops = new ArrayList<Loop>();

	/**
	 * stationName
	 */

	public Station(String stationName, String stationCode,
			double startMilePost, double endMilePost, double stationVelocity) {
		setStartMilePost(startMilePost);
		setEndMilePost(endMilePost);
		setStationName(stationName);
		setStationCode(stationCode);
		setStationVelocity(stationVelocity);
	}

	public Station() {
		setStationName("");
		setStationCode("");
	}

	public void printAttributes() {
		System.out.println("Station Name " + getStationName());
		System.out.println("Station Code " + getStationCode());
		System.out.println("Start Milepost " + getStartMilePost());
		System.out.println("End Milepost " + getEndMilePost());
	}

	public String getInfoString() {
		return "Name: " + getStationName() + " Code: " + getStationCode()
				+ " StartMilePost " + getStartMilePost() + " EndMilePost "
				+ getEndMilePost();
	}

	public void setStartMilePost(double startMilePost) {
		this.startMilePost = startMilePost;
	}

	public double getStartMilePost() {
		return startMilePost;
	}

	public void setEndMilePost(double endMilePost) {
		this.endMilePost = endMilePost;
	}

	public double getEndMilePost() {
		return endMilePost;
	}

	public void setStationName(String stationName) {
		if (stationName.equalsIgnoreCase("UNNAMED")) {
			this.stationName = "UNNAMED";
		} else
			this.stationName = stationName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationCode(String stationCode) {
		if (stationCode.equalsIgnoreCase("UNNAMED"))
			this.stationCode = "";
		else
			this.stationCode = stationCode;
	}

	public String getStationCode() {
		return stationCode;
	}


	public void write(BufferedWriter stationWriter) throws IOException {
		String stationName = this.stationName;
		stationWriter.write("\"" + stationName + "\"" + " "
				+ GlobalVariables.getPrettyPrinted(startMilePost, 3) + " "
				+ GlobalVariables.getPrettyPrinted(endMilePost, 3) + " "
				+ stationVelocity + "\n");
	}

	public void setStationVelocity(double stationVelocity) {
		this.stationVelocity = stationVelocity;
	}

	public double getStationVelocity() {
		return stationVelocity;
	}

	public static Station getStationFromFile(StreamTokenizer stationTokenizer,
			String stationCode) throws IOException {

		stationTokenizer.nextToken();
		String stationName = stationTokenizer.sval;

		stationTokenizer.nextToken();
		double startMilePost = stationTokenizer.nval;

		stationTokenizer.nextToken();
		double endMilePost = stationTokenizer.nval;

		stationTokenizer.nextToken();
		double stationVelocity = stationTokenizer.nval;

		// System.out.println("stationName " + stationName + " " + startMilePost
		// + " " + endMilePost + " " + stationVelocity);

		return new Station(stationName, stationCode, startMilePost,
				endMilePost, stationVelocity);

	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param str
	 */
	public Station(double a, double b, double c, String str) {
		stationVelocity = c;
		stationName = str;
		if (a > b) {
			startMilePost = b;
			endMilePost = a;
		} else {
			startMilePost = a;
			endMilePost = b;
		}
	}

	/**
	 * This will return the next station in the direction given. null if its the
	 * last station in the given direction
	 * 
	 * @param dir
	 * @return next Station
	 */
	public Station nextStation(int dir) {
		if (dir == 0) {// up
			return nextStation;
		}
		return previousStation;
	}

	/**
	 * This will return the previous station in the direction given. null if its
	 * the last station in the given direction
	 * 
	 * @param dir
	 *            direction
	 * @return the previous station
	 */
	public Station prevStation(int dir) {
		if (dir == 0) {// up
			return previousStation;
		}
		return nextStation;
	}

	/**
	 * Get the main loop of the station.
	 * 
	 * @param direction
	 * @return the main loop
	 */
	public Loop getMainLoop(int direction) {
		for (int i = 0; i < Loops.size(); i++) {
			if ((((Loop) Loops.get(i)).getWhetherMainLine() == 1)
					&& (((Loop) Loops.get(i)).getDirection()) == direction) {
				return ((Loop) Loops.get(i));
			}
		}
		return null;
	}

	/**
	 * Get the departing time of the train
	 * 
	 * @param trainNo
	 *            train number
	 * @param dir
	 *            direction
	 * @return double
	 */
	public double getDepartTime(int trainNo, int dir) {
		for (int currLoop = 0; currLoop < Loops.size(); currLoop++) {
			Loop loop = (Loop) Loops.get(currLoop);
			if (loop.getDirection() == dir) {

				for (int currInterv = 0; currInterv < loop.getOccupy().size(); currInterv++) {
					Interval interv = loop.getOccupy().get(currInterv);
					if (interv.getTrainNo() == trainNo) {
						return interv.getEndTime();
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Station: int simulateTrain(Train currTrain) 1) Consider the starting
	 * loop. 2) Get the loop's nextBlocks and the previousBlocks. 3) It does
	 * some signal failure changes (which we are not bothered about now) 4) And
	 * until the simulation is not finished it keeps traversing the block by
	 * newLoop.traverseBlock(currTrain,startTime, startTime,0)
	 * 
	 * @param currTrain
	 * @param simulationType
	 * @return simulateTrain
	 */
	@SuppressWarnings({})
	public int simulateTrain(Train currTrain, String simulationType,
			SimulationInstance simulationInstance) {

		// Loop newLoop = new Loop(currTrain.getDirection(), 0, 0);

		// newLoop.setStartMilePost(startMilePost);
		// newLoop.setEndMilePost(endMilePost);
		// newLoop.setMaximumPossibleSpeed(crossVelocity);
		// newLoop.setBlockNo(currTrain.getStartLoopNo());
		// newLoop.setStation(this);

		Block block = simulationInstance.getBlockFromBlockNo(currTrain
				.getStartLoopNo());
		if (block == null) {
			Debug.print("Station: simulateTrain: Error: block not present "
					+ currTrain.getStartLoopNo());
			// System.exit(0);
			System.out.println("simulateTrain: hbEntry is null");
			throw new NullPointerException("simulateTrain: hbEntry is null");
		}

		// newLoop.setTinyBlock(hbEntry.block.getTinyBlock());
		Debug.print("Station: simulateTrain: Train starts at "
				+ currTrain.getStartLoopNo() + " cross velo " + stationVelocity);
		
		int trainDirection = currTrain.getDirection();
		
		Loop startLoop = (Loop) block;

		double originalStartMilePost = startLoop.getStartMilePost();
		double originalEndMilePost = startLoop.getEndMilePost();
		if (trainDirection == GlobalVar.UP_DIRECTION)
			startLoop.setStartMilePost(originalEndMilePost);
		else
			startLoop.setDirectEndMilePost(originalStartMilePost);

		int nextReferenceLoopNumber;
		if (currTrain.isScheduled()) {
			nextReferenceLoopNumber = startLoop
					.getNextReferenceLoop(currTrain.getRefTables()).getBlockNo();
		} else {
			nextReferenceLoopNumber = currTrain.getEndLoopNo();
		}

		boolean simFinish = false;

		double firstLoopTraversalTime = 1.5D;
		double startTime = currTrain.getStartTime();// - firstLoopTraversalTime;
		if (simulationType.equalsIgnoreCase("SignalFailure")) {
			simulationInstance.setSignalFailFlags();
		}

		while (simFinish != true) {

			if (false/*
					 * simulationInstance.temp - simulationInstance.trainTemp >
					 * 1000
					 */) {
				Debug.error("Station: simulateTrain: Infinite loop for train "
						+ currTrain.getTrainNo());
				return -1;
			}

			System.out.println("NewLoop " + startLoop.getBlockNo());
			System.out.println("TrainNo " + currTrain.getTrainNo());

			// StatusTraverseBlock retStatus = newLoop.traverseBlock(currTrain,
			// startTime, startTime, 0);
			Link previousLink = null;
			double startVelocity = 0;
			StatusTraverseBlock retStatus = startLoop.traverseBlock(currTrain,
					startTime, startTime, startVelocity, previousLink,
					null/* nextFastestLink */, 0 /* profileStartingMilePost */,
					simulationInstance, nextReferenceLoopNumber);

			simFinish = retStatus.status;

			if (retStatus.limit)
				return -1;

			if (simFinish) {

				System.out.println("SimulatedTrain " + currTrain.getTrainNo());
			}

			// startTime = retStatus.departureTime + 1;
			Debug.fine("Station: simulateTrain: renewedDepartureTime "
					+ retStatus.departureTime);
			startTime = retStatus.departureTime;// waiting for 1 minute is too
												// much. Leave as soon as you
												// see a yellow.
		}

		if (trainDirection == GlobalVar.UP_DIRECTION)
			startLoop.setStartMilePost(originalStartMilePost);
		else
			startLoop.setDirectEndMilePost(originalEndMilePost);
		// System.out.println("Setting velocity profile times");
			currTrain.setTimesForVelocityProfiles();
		
		Debug.info("Station: simulateTrain: this train simulation is complete "
				+ currTrain.getTrainNo());

		return 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Station a, Station b) {

		double c = a.startMilePost;
		double d = b.startMilePost;

		if (c > d) {
			return 1;
		}
		if (c < d) {
			return -1;
		}
		return 0;
	}

	@Override
	public boolean hasError() {
		if (stationName.isEmpty())
			return true;
		else if (stationCode.isEmpty())
			return true;

		return false;
	}

}

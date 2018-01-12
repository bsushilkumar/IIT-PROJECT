package gui.entities.sectionEntities.trackEntities;

import globalVariables.GlobalVariables;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.time.Interval;
import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.trainEntities.Train;

import java.io.StreamTokenizer;
import java.util.ArrayList;

import simulator.input.SimulationInstance;
import simulator.scheduler.BlockScheduler;
import simulator.scheduler.LoopTrainScheduleData;
import simulator.scheduler.StatusTraverseBlock;
import simulator.util.Debug;

public class Loop extends Block {
	private static final double minimumLoopLength = Block.minimumBlockLength;
	private static final String MAINLINE = "ml";
	private static final String LOOPLINE = "loop";
	private String loopType = GlobalVariables.loopTypeMainline;	
	
	//This parameter is to set the loop for "Scheduled" trains or "Freight" trains or "all" kind of trains
	private String loopTrainType = "all"; 
	
	private static int defaultLoopVelocity = 15;

	private String platformName = "PF-";

	private Station station = null;

	private int loopTrainScheduleDataIterator = 0;
	public ArrayList<LoopTrainScheduleData> loopTrainScheduleDataList = new ArrayList<LoopTrainScheduleData>();

	public Loop() {
		super();
		init();
	}

	public Loop(double startMilePost, double endMilePost) {
		init();
		this.setStartMilePost(startMilePost);
		this.setEndMilePost(endMilePost);

		this.maximumPossibleSpeed = defaultLoopVelocity;
	}

	private void init() {
		this.setEntityType(LOOP_ENTITY);
	}

	public void setEndMilePost(double endMilePost) {
		if (endMilePost > this.startMilePost + Loop.minimumLoopLength) {
			super.setEndMilePost(endMilePost);
		} else {
			super.setEndMilePost(this.startMilePost
					+ Loop.minimumLoopLength);
		}
	}

	public void setDirectEndMilePost(double endMilePost) {
		this.endMilePost = GlobalVariables.roundToThreeDecimals(endMilePost);
	}

	public double getLoopVelocity() {
		return getMaximumPossibleSpeed();
	}

	public void setLoopType(String loopType) {
		if (loopType.equalsIgnoreCase(Loop.MAINLINE)) {
			this.loopType = loopType;
		} else {
			this.loopType = Loop.LOOPLINE;
		}

	}
	
	public void setLoopTrainType(String loopType) {
		if (loopType.equalsIgnoreCase("scheduled")) {
			this.loopTrainType = loopType;
		} else if(loopType.equalsIgnoreCase("freight")) {
			this.loopTrainType = loopType;
		}else{
			this.loopTrainType = "all";
		}

	}

	public boolean isFreightLoop() {
		if (this.loopTrainType.equalsIgnoreCase("all")) {
			return true;
		} else if(loopTrainType.equalsIgnoreCase("Freight")) {
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isPassengerLoop() {
		if (this.loopTrainType.equalsIgnoreCase("all")) {
			return true;
		} else if(loopType.equalsIgnoreCase("scheduled")) {
			return true;
		}else{
			return false;
		}
	}
	public String getLoopType() {
		return loopType;
	}
	
	public String getLoopTrainType() {
		return loopTrainType;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	/**
	 * crossMainLine
	 */
	private int crossMainLine; /*
								 * 0 - no crossing 1- cross up main line (means
								 * crosses the 0 direction main line 2-cross
								 * down main line (means main line dir = 1)
								 */

	/**
	 * whetherMainLine
	 */
	public int whetherMainLine; // Whether main line 0 -not main line 1- main
									// line
	public String stationName;

	/**
	 * @param st
	 * @param isWarner
	 */
	public Loop(StreamTokenizer st, boolean isWarner) {
		setWarner(false);
	}

	/**
	 * @param time
	 * @return the number of the interval which contains the given time.
	 */
	public int inWhichInterval(double time) {
		return (getOccupy().inInterval(time));
	} // method isFree ends

	/**
	 * (non-Javadoc)
	 * 
	 * @see Block#waitingPermitted()
	 */
	@Override
	public boolean waitingPermitted() {
		Debug.print("waiting is permitted ");
		return true;
	}

	/**
	 * Loop: StatusTraverseBlock traverseBlock((Train currTrain, double
	 * arrivalTime, double deptTime, double startVelocity) 1) creates an
	 * instance of the blockScheduler for this loop as the currentBlock and the
	 * currTrain 2) goes to blockScheduler.traverseBlock(arrivalTime, deptTime,
	 * startVelocity) (non-Javadoc)
	 * 
	 * @see Block#traverseBlock(Train, double, double, double)
	 */
	@Override
	public StatusTraverseBlock traverseBlock(Train currTrain,
			double arrivalTime, double deptTime, double startVelocity,
			Link previousLink, Link nextFastestLink,
			double profileStartingMilePost,
			SimulationInstance simulationInstance, int nextReferenceLoopNumber) {

		BlockScheduler blockScheduler = new BlockScheduler(this, currTrain,
				simulationInstance);

		if (!GlobalVariables.stationToStationScheduling) {
			if (currTrain.isScheduled()) {
				Loop nextReferenceLoop = this.getNextReferenceLoop(currTrain
						.getRefTables());
				if (nextReferenceLoop != null)
					nextReferenceLoopNumber = nextReferenceLoop.getBlockNo();
			} else {
				nextReferenceLoopNumber = currTrain.getEndLoopNo();
			}
		}

		try {

			return blockScheduler.traverseBlock(arrivalTime, deptTime,
					startVelocity, previousLink, nextFastestLink,
					profileStartingMilePost, nextReferenceLoopNumber);
		} catch (Exception e) {
			e.printStackTrace();

			System.out
					.println("Loop: traverseBlock: Caught exception arrivalTime "
							+ arrivalTime
							+ " depTime "
							+ deptTime
							+ " startVelocity " + startVelocity);
			System.exit(1);
		}
		return null;
	}

	/**
	 * Get the loop number of the next reference entry of a scheduled train.
	 * 
	 * @param referenceTables
	 * @return loop number of next reference entry. If there is no next entry -2
	 *         is returned.
	 */
	public Loop getNextReferenceLoop(
			ArrayList<ReferenceTableEntry> referenceTables) {
		Debug.fine("Loop: getNextReferenceLoopNo: ");
		int i;
		boolean currentReferenceEntryFound = false;
		for (i = 0; i < referenceTables.size() && !currentReferenceEntryFound; i++) {
			ReferenceTableEntry referenceTableEntry = referenceTables.get(i);
			if (referenceTableEntry.getReferenceLoopNo() == this.getBlockNo())
				currentReferenceEntryFound = true;
		}

		if (i == referenceTables.size())
			return null;
		Loop loop = referenceTables.get(i).getReferenceLoop();

		Debug.fine("Loop: getNextReferenceLoop: " + loop.getBlockName());
		return loop;

	}

	public int getCrossMainLine() {
		return crossMainLine;
	}

	public void setCrossMainLine(int crossMainLine) {
		this.crossMainLine = crossMainLine;
	}

	public int getWhetherMainLine() {
		return whetherMainLine;
	}

	public void setWhetherMainLine(int whetherMainLine) {
		this.whetherMainLine = whetherMainLine;
	}

	public String getStationName() {
		return station.getStationName();
	}

	@Override
	public boolean hasError() {
		boolean error = super.hasError();
		if (error) {
			return error;
		}
		double stationStart = station.getStartMilePost();
		double stationEnd = station.getEndMilePost();
		if (stationStart > startMilePost) {
			// System.out.println("Loop: hasError: stationStart " + stationStart
			// + " loopStart " + startMilePost);
			return true;
		}

		else if (startMilePost + Loop.minimumLoopLength > endMilePost) {
			// System.out.println("Loop: hasError: loop too short");
			return true;
		} else if (endMilePost > stationEnd) {
			// System.out.println("Loop: hasError: stationEnd " + stationEnd
			// + " loopEnd " + endMilePost);
			return true;
		}

		return false;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public boolean isLocked() {
		for (Interval interval : occupy) {
			if (interval.isLocked())
				return true;
		}

		return false;
	}

	public void unlock() {
		for (int i = 0; i < occupy.size(); i++) {
			Interval interval = occupy.get(i);
			if (interval.isLocked()) {
				occupy.remove(i);
				i--;
			}

		}
	}

	public void lock(double lockTime, int trainNo) {
		Interval lockInterval = new Interval(trainNo, 0, 0, true);
		lockInterval.setLock(true);
		occupy.add(lockInterval);
	}

	public void forwardLoopTrainScheduleDataList() {
		if (loopTrainScheduleDataIterator >= loopTrainScheduleDataList.size())
			return;

		LoopTrainScheduleData loopTrainScheduleData = loopTrainScheduleDataList
				.get(loopTrainScheduleDataIterator);
		loopTrainScheduleData.setScheduled(true);

		loopTrainScheduleDataIterator++;
	}

	public void undoLoopTrainScheduleDataList() {
		if (loopTrainScheduleDataList.isEmpty()
				|| loopTrainScheduleDataIterator == 0)
			return;

		loopTrainScheduleDataIterator--;

		LoopTrainScheduleData loopTrainScheduleData = loopTrainScheduleDataList
				.get(loopTrainScheduleDataIterator);
		loopTrainScheduleData.setScheduled(false);
	}

	public Train getLoopNextTrainToBeScheduled() {
		if (loopTrainScheduleDataList.isEmpty()
				|| loopTrainScheduleDataIterator >= loopTrainScheduleDataList
						.size()) {
			return null;
		}

		Train train = loopTrainScheduleDataList.get(
				loopTrainScheduleDataIterator).getTrain();
		return train;
	}

	public double getNextTrainToBeScheduledDepartureTime() {
		if (loopTrainScheduleDataList.isEmpty()
				|| loopTrainScheduleDataIterator >= loopTrainScheduleDataList
						.size()) {
			return 10000;
		}
		
		double referenceDepartureTime = loopTrainScheduleDataList.get(
				loopTrainScheduleDataIterator).getReferenceDepartureTime();
		
		return referenceDepartureTime;
	}
	
	public static int getWhetherMainLineFromType(String loopType) {
		if (loopType.equalsIgnoreCase("ml"))
			return 0;
		return 1;
	}

}
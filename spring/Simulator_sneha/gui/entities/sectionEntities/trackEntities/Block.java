package gui.entities.sectionEntities.trackEntities;

import globalVariables.GlobalVar;
import globalVariables.GlobalVariables;
import gui.diagramEntities.trainDiagrams.VelocityProfile;
import gui.entities.sectionEntities.signal.Signal;
import gui.entities.sectionEntities.signal.SignalFailure;
import gui.entities.sectionEntities.signal.SignalFailureFormat;
import gui.entities.sectionEntities.time.Interval;
import gui.entities.sectionEntities.time.IntervalArray;
import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.sectionEntities.trackProperties.BlockDirectionInInterval;
import gui.entities.sectionEntities.trackProperties.BlockInterval;
import gui.entities.sectionEntities.trackProperties.Gradient;
import gui.entities.sectionEntities.trackProperties.SpeedRestriction;
import gui.entities.sectionEntities.trackProperties.TinyBlock;
import gui.entities.sectionEntities.trackProperties.TinyBlockList;
import gui.entities.sectionEntityList.GradientList;
import gui.entities.sectionEntityList.LinkList;
import gui.entities.sectionEntityList.SpeedRestrictionList;
import gui.entities.trainEntities.Train;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.logging.Logger;

import simulator.input.SimulationInstance;
import simulator.input.SortTinyBlock;
import simulator.scheduler.BlockScheduler;
import simulator.scheduler.OverlapParameters;
import simulator.scheduler.RunTimeReturn;
import simulator.scheduler.SchedulerFactory;
import simulator.scheduler.SimulatorException;
import simulator.scheduler.SplitProfileForWarner;
import simulator.scheduler.StationToStationScheduler;
import simulator.scheduler.StatusTraverseBlock;
import simulator.util.Debug;
import simulator.velocityProfileCalculation.VelocityProfileArray;

public class Block extends TrackSegment {
	private static final int defaultBlockVelocity = 100;

	public static final double minimumBlockLength = 0.01;

	/**
	 * blockNo
	 */
	public int blockNo = -1;
	/**
	 * blockName
	 */
	protected String blockName = "";

	/**
	 * upDirectionSignal
	 */
	protected Signal upDirectionSignal = new Signal();
	/**
	 * upDirectionSignal
	 */
	protected Signal downDirectionSignal = new Signal();
	/**
	 * warner
	 */
	protected boolean warner = false;
	/**
	 * direction Direction of the block. Either up or down or bidirectional.
	 */
	public int direction;
	/**
	 * speedRestrictionList
	 */
	public SpeedRestrictionList speedRestrictionList = new SpeedRestrictionList();

	/**
	 * sfsr SignalFailureSpeedRestriction
	 */
	protected TinyBlockList sfsr = new TinyBlockList();
	/**
	 * nightsfsr night SignalFailureSpeedRestriction
	 */
	protected TinyBlockList nightsfsr = new TinyBlockList();
	/**
	 * nextBlocks
	 */
	protected LinkList nextBlocks = new LinkList();
	/**
	 * previousBlocks
	 */
	protected LinkList previousBlocks = new LinkList();
	/**
	 * gradientList
	 */
	protected GradientList gradientList = new GradientList();;

	/**
	 * signalFailure
	 */
	protected SignalFailure signalFailure = new SignalFailure();

	/**
	 * signalFailFlag
	 */
	protected int signalFailFlag = 0;

	// Devendra
	/**
	 * blockDirectionInInterval: maintains the timeChangeList at which the
	 * direction of block is changed
	 */
	protected BlockDirectionInInterval blockDirectionInInterval = null;

	private double defaultOverlapDistance = 0.12D;
	private double downDirectionOverlapDistance = defaultOverlapDistance;
	private double upDirectionOverlapDistance = defaultOverlapDistance;

	protected static Logger logger;
	protected HashMap<Integer, Boolean> hasPathToBlockInUpDirectionMap = new HashMap<Integer, Boolean>();
	protected HashMap<Integer, Boolean> hasPathToBlockInDownDirectionMap = new HashMap<Integer, Boolean>();;

	public ArrayList<Link> upLinks = new ArrayList<Link>();
	public ArrayList<Link> downLinks = new ArrayList<Link>();
	
	public Block(double startMilepost, double endMilepost) {
		init();
		this.setStartMilePost(startMilepost);
		this.setEndMilePost(endMilepost);

		this.maximumPossibleSpeed = defaultBlockVelocity;
	}

	public Block() {
		super(0.0, 0.01 + 0.1);
		init();
		this.maximumPossibleSpeed = defaultBlockVelocity;
	}

	/**
	 * constructor
	 */

	/**
	 * @param i
	 */
	public Block(int i) {
		init();
		setWarner(false);
		setOccupy(new IntervalArray());
		setSignalFailure(new SignalFailure());
		setSignalFailFlag(0);
		setBlockNo(i);
		setBlockDirectionInInterval(null);
	}

	/**
	 * @param i
	 * @param d
	 * @param d1
	 * @param doubleMaximumPossibleSpeed
	 */
	Block(int direction, double startMilePost, double endMilePost,
			double maximumPossibleSpeed) {
		init();
		setDirection(direction);
		setStartMilePost(startMilePost);
		setEndMilePost(endMilePost);
		setMaximumPossibleSpeed(maximumPossibleSpeed);

	}

	public Block(Block block) {
		setBlockNo(block.getBlockNo());
		setBlockName(block.getBlockName());
		setNextLinks(block.getNextLinks());
		setPreviousLinks(block.getPreviousLinks());
		setSpeedRestrictionList(block.getSpeedRestrictionList());
		setGradientList(block.getGradientList());
		setEndMilePost(block.getEndMilePost());
		setStartMilePost(block.getStartMilePost());
		setTinyBlockList(block.getTinyBlockList());
	}

	private void init() {
		this.setEntityType(BLOCK_ENTITY);
	}

	public HashMap<Integer, Boolean> getHasPathToBlockMap(int direction) {
		if (direction == GlobalVar.UP_DIRECTION)
			return hasPathToBlockInUpDirectionMap;
		else
			return hasPathToBlockInDownDirectionMap;
	}

	public void setEndMilePost(double endMilePost) {
		if (endMilePost > this.startMilePost) {
			super.setEndMilePost(endMilePost);
		} else {
			super.setEndMilePost(startMilePost + Block.minimumBlockLength);
		}
	}

	public void setSpeedRestrictionList(
			SpeedRestrictionList speedRestrictionList) {
		this.speedRestrictionList = speedRestrictionList;
	}

	public SpeedRestrictionList getSpeedRestrictionList() {
		return speedRestrictionList;
	}

	public String getSpeedRestrictionsString() {
		String majorDelimiter = GlobalVar.majorStringDelimiter;

		String speedLimitString = "\"";
		String startMilepostString = "\"";
		String endMilepostString = "\"";

		int size = speedRestrictionList.size();
		for (int i = 0; i < size; i++) {
			SpeedRestriction speedRestriction = speedRestrictionList.get(i);
			speedLimitString += speedRestriction.getSpeedLimit();
			startMilepostString += speedRestriction.getRelativeStartMilePost();
			endMilepostString += speedRestriction.getRelativeEndMilePost();

			if (i < size - 1) {
				speedLimitString += majorDelimiter;
				startMilepostString += majorDelimiter;
				endMilepostString += majorDelimiter;
			}
		}

		speedLimitString += "\"";
		startMilepostString += "\"";
		endMilepostString += "\"";

		return speedLimitString + " " + startMilepostString + " "
				+ endMilepostString;
	}

	public String getGradientsString() {
		String majorDelimiter = GlobalVar.majorStringDelimiter;
		String space = GlobalVar.spaceCharacter;

		String slopesString = "\"";
		String directionsString = "\"";
		String startMilePostsString = "\"";
		String endMilePostsString = "\"";

		int size = getGradientList().size();
		for (int i = 0; i < size; i++) {
			Gradient gradient = getGradientList().get(i);
			slopesString += gradient.getGradientValue();
			directionsString += gradient.getDirection();
			startMilePostsString += gradient.getRelativeStartMilePost();
			endMilePostsString += gradient.getRelativeEndMilePost();

			if (i < size - 1) {
				if (i < size - 1) {
					slopesString += majorDelimiter;
					directionsString += majorDelimiter;
					startMilePostsString += majorDelimiter;
					endMilePostsString += majorDelimiter;
				}
			}
		}

		slopesString += "\"";
		directionsString += "\"";
		startMilePostsString += "\"";
		endMilePostsString += "\"";

		return slopesString + space + directionsString + space
				+ startMilePostsString + space + endMilePostsString;
	}

	/**
	 * @param streamtokenizer
	 * @return {@link SpeedRestriction}
	 * @throws IOException
	 * @throws SimulatorException
	 */
	public SpeedRestriction readSpeedRes(StreamTokenizer streamtokenizer)
			throws IOException, SimulatorException {
		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug.print("Error in format of input file : station.dat......");
			Debug.print("Error : max velocity in km/hr expected");
			throw new SimulatorException();
		}
		Debug.print((new StringBuilder()).append("value read is ")
				.append(streamtokenizer.nval).toString());
		double d = streamtokenizer.nval / 60D;
		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug.print("Error in format of input file : station.dat......");
			Debug.print("Error : from mile post expected");
			throw new SimulatorException();
		}
		Debug.print((new StringBuilder()).append("value read is ")
				.append(streamtokenizer.nval).toString());
		float f = (float) streamtokenizer.nval;
		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug.print("Error in format of input file : station.dat......");
			Debug.print("Error : to mile post expected");
			throw new SimulatorException();
		}
		Debug.print((new StringBuilder()).append("value read is ")
				.append(streamtokenizer.nval).toString());
		float f1 = (float) streamtokenizer.nval;
		return new SpeedRestriction(d, f, f1);
	}

	/**
	 * @param timeInterval
	 * @param d
	 * @return get Train number
	 */
	public ArrayList<Integer> getTrainNoFromOccupancy(double time,
			double timeInterval) {
		double tStart = time - timeInterval;
		double tEnd = time + timeInterval;
		ArrayList<Integer> trainNoList = new ArrayList<Integer>();
		IntervalArray intervalArray = getOccupy();
		for (Interval interval : intervalArray) {
			double iStartTime = interval.getStartTime();
			double iEndTime = interval.getEndTime();
			if ((tStart <= iStartTime && iStartTime <= tEnd)
					|| (tStart <= iEndTime && iEndTime <= tEnd)
					|| (iStartTime <= tStart && tEnd <= iEndTime)) {
				int trainNo = interval.getTrainNo();
				trainNoList.add(trainNo);
			}
		}
		return trainNoList;
	}

	/**
	 * @param d
	 * @param d1
	 * @param d2
	 * @param d3
	 * @param i
	 * @return true if block is free.
	 */
	public int isFree(double d, double d1, double d2, double d3, int i) {
		return getOccupy().inInterval(d, d1 + d3);
	}

	/**
	 * Consider the block occupancies and determine if the block is free during
	 * the interval specified by the startTime and endTime. If it is not free,
	 * return the ending time of the interval in which "startTime" lies. If the
	 * block is free, then check if the direction of the block, if specified by
	 * BlockdDirectionInInterval file does not change during the same interval.
	 * If it does change then return the ending time of the blockInterval in
	 * which startTime lies. If the direction of the block does not change
	 * during this interval then return -1.
	 * 
	 * @param startTime
	 * @param endTime
	 * @return time when the block is free.
	 */
	
	public double whenFree(double startTime, double endTime) {
		int i = isFree(startTime, endTime);
		if (i != -1) {
			return getOccupy().get(i).getEndTime();
		}

		// Devendra - to check if the block direction is compatible with train's
		// direction
		// during the interval
		//Some changes were made by R.Vidyadhar.
		if (this.getBlockDirectionInInterval() != null) {
			if (this.getBlockDirectionInInterval().blockIntervalArray != null) {
				
				BlockInterval startTimeBlockInterval = this
						.getBlockDirectionInInterval()
						.getBlockIntervalFromTime(startTime);
				BlockInterval endTimeBlockInterval = this
						.getBlockDirectionInInterval()
						.getBlockIntervalFromTime(endTime);
				/**if (startTimeBlockInterval != endTimeBlockInterval){
					return startTimeBlockInterval.getEndTime();
				}*/
				if (endTimeBlockInterval != null){
					return endTimeBlockInterval.getEndTime();
				}
				else if (startTimeBlockInterval != null){
					return startTimeBlockInterval.getEndTime();
				}
				else{
					double finalEndTime = 0;
					BlockDirectionInInterval blockDirectionInInterval = this.getBlockDirectionInInterval();
					for (int index = 0; index < blockDirectionInInterval.blockIntervalArray.size(); index++) {
						BlockInterval blockInterval = blockDirectionInInterval.blockIntervalArray.get(index);
						if(blockInterval.startTime > startTime &&  blockInterval.getEndTime() < endTime){
							if(finalEndTime < blockInterval.getEndTime())
								finalEndTime = blockInterval.getEndTime();
						}
					}
					if(finalEndTime == 0)
						return -1D;
					else
						return finalEndTime;
				}
			}
		}
		return -1D;
	}
	/**public double whenFree(double startTime, double endTime) {
		int i = isFree(startTime, endTime);
		if (i != -1) {
			return getOccupy().get(i).getEndTime();
		}

		// Devendra - to check if the block direction is compatible with train's
		// direction
		// during the interval
		if (this.getBlockDirectionInInterval() != null) {
			if (this.getBlockDirectionInInterval().blockIntervalArray != null) {
				//System.out.println("Block StartTime: " + startTime + "Block EndTime: " + endTime);
				BlockInterval startTimeBlockInterval = this
						.getBlockDirectionInInterval()
						.getBlockIntervalFromTime(startTime);
				BlockInterval endTimeBlockInterval = this
						.getBlockDirectionInInterval()
						.getBlockIntervalFromTime(endTime);
				if (startTimeBlockInterval != endTimeBlockInterval){
					return startTimeBlockInterval.getEndTime();
				}
				/**if (startTimeBlockInterval != null){
					return startTimeBlockInterval.getEndTime();
				}
				else if (endTimeBlockInterval != null){
					return endTimeBlockInterval.getEndTime();
				}
			}
		}
		return -1D;
	}**/

	/**
	 * @param trainNo
	 * @param arrivalTime
	 * @param departureTime
	 * @param actualOccupancy
	 */
	public void reserve(Train train, double arrivalTime, double departureTime,
			boolean actualOccupancy) {
		Debug.fine("block: reserve: " + blockName + " arrival " + arrivalTime
				+ " departure " + departureTime);
		setOccupied(train, arrivalTime, departureTime, actualOccupancy,
				blockName);
		if (GlobalVariables.stationToStationScheduling) {
			Stack<StationToStationScheduler> stationToStationSchedulerStack = train.stationToStationSchedulerStack;
			if (stationToStationSchedulerStack.size() > 0) {
				StationToStationScheduler stationToStationScheduler = stationToStationSchedulerStack
						.peek();
				TreeSet<Integer> blockNumberSet = stationToStationScheduler
						.getBlockNumberSet();
				blockNumberSet.add(new Integer(getBlockNo()));
			}
		}
	}

	/**
	 * @param direction
	 * @return startMilePost depending upon the direction of the block.
	 */
	public double getStartMilePost(int direction) {
		return direction == GlobalVar.UP_DIRECTION ? startMilePost
				: endMilePost;
	}

	/**
	 * @param direction
	 * @return the endMilePost of the block depending upon its direction.
	 */
	public double getEndMilePost(int direction) {
		return direction == GlobalVar.DOWN_DIRECTION ? startMilePost
				: endMilePost;
	}

	/**
	 * @param i
	 * @return endTime of interval
	 */
	public double getDepartureTimeFromTrainNo(int trainNo) {
		for (int j = 0; j < getOccupy().size(); j++) {
			Interval interval = getOccupy().get(j);
			if (interval.getTrainNo() == trainNo) {
				return interval.getEndTime();
			}
		}

		return -1D;
	}

	/**
	 * @param trainDirection
	 * @return the nextBlocks or previousBlocks depending upon the direction of
	 *         the block
	 */
	public LinkList getNextLinks(int trainDirection) {
		if (trainDirection == GlobalVar.UP_DIRECTION) {
			return nextBlocks;
		}

		return getPreviousLinks();
	}

	/**
	 * @return true if waiting is permitted on the block. returns false as of
	 *         now.
	 */
	public boolean waitingPermitted() {
		return true;
	}

	/**
	 * 1) It finds a preferred loop or a block as per whether the currentblock
	 * has next block as a loop or block. 2) If the train is scheduled and if
	 * the currentBlock is a "block" it calls to see what is the type of next
	 * Block by calling getNextBlock(int trainDirection)
	 * 
	 * Block getNextBlock(int trainDirection): 1) Take the nextBlockArray and
	 * find the block amongst that for which the link Priority is one. 3) If the
	 * nextBlock is indeed a "block" it returns the block corresponding to the
	 * link which has priority one. 4) If the nextBlock is a "loop" infact then
	 * it finds the preferredLoop from the referenceTable since the train is
	 * scheduled. and returns that loop. 5) If the train is not scheduled or the
	 * currentBlock is a loop then it returns the nextMainBlock by calling the
	 * function getNextMainBlock(trainDirection).
	 * 
	 * @param trainDirection
	 * @param Train
	 * @return get next block
	 */

	public Block getNextBlock(Train train) {
		Block returnBlock = null;
		Debug.fine("getNextBlock: " + this.getBlockName() + " train "
				+ train.getTrainNo());

		Link returnLink = getNextLink(train);
		if (returnLink != null) {
			returnBlock = returnLink.getNextBlock();
		}

		if (returnBlock == null) {
			Debug.fine("getNextBlock: returnBlock is null");
		} else {
			Debug.fine("getNextBlock: returnBlock " + returnBlock.blockName);
		}
		return returnBlock;
	}

	/**
	 * @param i
	 * @return get the nextMainBlock of the nextBlocks depending upon the
	 *         direction of the block.
	 */
	public Block getNextBlockByDirection(int direction) {
		Debug.fine("getNextBlockByDirection: " + this.getBlockName());
		LinkList nextblockarray = getNextLinks(direction);
		return nextblockarray.getNextMainBlock();
	}

	/**
	 * @param i
	 * @param j
	 * @return ?
	 */
	public boolean checkLastBlock(int i, int j) {
		LinkList nextblockarray = getNextLinks(i);
		// Block block = new Block();
		boolean flag = false;
		for (int k = 0; k < nextblockarray.size(); k++) {

			Debug.print("came to find last block in block array");
			if (((Link) nextblockarray.get(k)).getNextBlockNo() == j) {
				flag = true;
			}
		}

		return flag;
	}

	/**
	 * @param d
	 * @return true if block is not free.
	 */
	public boolean isSignalRed(double d) {
		return isFree(d) != -1;
	}

	public boolean hasPathTo(Block nextBlock, int direction) {
		if (blockNo == nextBlock.blockNo)
			return true;
		LinkList nextLinks = this.getNextLinks(direction);
		for (Link link : nextLinks) {
			boolean hasPath = link.getNextBlock().hasPathTo(nextBlock,
					direction);
			if (hasPath)
				return true;
		}

		return false;
	}

	/**
	 * @param block
	 * @param trainEndLoopNo
	 * @param trainDirection
	 * @return true if hasPath
	 */
	public boolean hasPath(int trainEndLoopNo, int trainDirection) {
		// Block block1 = new Block();
		// trainEndLoopNo=7;
		//System.out.println("TrainEndLoop: " + trainEndLoopNo + " Block = " +
		 //this.getBlockNo());
		int blockDirection = this.getDirection();
		
		if (trainEndLoopNo == -1
				&& (blockDirection == trainDirection || blockDirection == GlobalVar.COMMON_DIRECTION))
			return true;

		HashMap<Integer, Boolean> hasPathToBlockMap = getHasPathToBlockMap(trainDirection);
		Boolean hasPathBoolean = hasPathToBlockMap.get(new Integer(
				trainEndLoopNo));

		if (this.getBlockNo() == trainEndLoopNo) {
			return true;
		}

		
		if (hasPathBoolean != null) {
			return hasPathBoolean.booleanValue();
		} else {
			
			LinkList nextblockarray = this.getNextLinks(trainDirection);
//			Debug.print((new StringBuilder()).append(this.getBlockNo())
//					.append("-->").toString());


			for (int k = 0; k < nextblockarray.size(); k++) {
				
				Block block2 = nextblockarray.get(k).getNextBlock();
				
				/**System.out.println("Block.haspath: nextblockarray.size = "+ nextblockarray.size()
				+ " Block2 = "+ block2.getBlockNo());**/
				if (block2.hasPath(trainEndLoopNo, trainDirection)) {
					hasPathToBlockMap.put(trainEndLoopNo, true);
					return true;
				}
			}

			hasPathToBlockMap.put(trainEndLoopNo, false);

		}

//		Debug.fine("Block: hasPath: currentBlock " + this.blockName
//				+ " has no path to " + trainEndLoopNo);
		return false;
	}

	/**
	 * @param arrivalTime
	 * @param trainDirection
	 * @return earliest possible arrival time.
	 */
	
	public double getEarliestArrivalTime(double arrivalTime, int trainDirection) {

		int j = getOccupy().inInterval(arrivalTime);
		double newArrivalTime = 0;
		double timeFromBlockInterval = 0;
		double timeFromOccupy = 0;
		if (j != -1) {
			timeFromOccupy = getOccupy().get(j).getEndTime();
			/**for (int i =0; i< this.getOccupy().size(); i++ ){
			System.out.println("Block.getEarliestArrivalTime: Block No: " + this.blockNo + 
						" Train No: "+ this.getOccupy().get(i).getTrainNo()+
						" Start time: "+ this.getOccupy().get(i).getStartTime()+
						" End Time: "+ this.getOccupy().get(i).getEndTime());
			}**/
			/**System.out.println("Block.getEarliestArrivalTime: Current Block: "+ this.blockNo
				 + " Overtaking train: "+ getOccupy().get(j).getTrainNo()+
				 " start time: "+ getOccupy().get(j).getStartTime()+
				 " End time: "+ getOccupy().get(j).getEndTime()
				 +" Arrival time: "+arrivalTime);**/
		}
		// Vidyadhar
		if (this.getBlockDirectionInInterval() != null) {
			if (this.getBlockDirectionInInterval().isDirectionOk(
					trainDirection, arrivalTime)) {
			} else {
				 timeFromBlockInterval = this
							.getBlockDirectionInInterval()
							.getEarliestArrivalTimeFromBlockInterval(
									arrivalTime, trainDirection);
			}
		}
			
		newArrivalTime = Math.max(timeFromOccupy,timeFromBlockInterval);
		
		if(newArrivalTime!=0){
			return newArrivalTime;
		}else{
			return arrivalTime;
		}
	}
	/**public double getEarliestArrivalTime(double arrivalTime, int trainDirection) {

		int j = getOccupy().inInterval(arrivalTime);
		if (j != -1) {
			double newArrivalTime = getOccupy().get(j).getEndTime();
			
			/**for (int i =0; i< this.getOccupy().size(); i++ ){
				System.out.println("Block.getEarliestArrivalTime: Block No: " + this.blockNo + 
							" Train No: "+ this.getOccupy().get(i).getTrainNo()+
							" Start time: "+ this.getOccupy().get(i).getStartTime()+
							" End Time: "+ this.getOccupy().get(i).getEndTime());
			}**/
			/**System.out.println("Block.getEarliestArrivalTime: Current Block: "+ this.blockNo
					 + " Overtaking train: "+ getOccupy().get(j).getTrainNo()+
					 " start time: "+ getOccupy().get(j).getStartTime()+
					 " End time: "+ getOccupy().get(j).getEndTime()
					 +" Arrival time: "+arrivalTime);
			// Devendra
			if (this.getBlockDirectionInInterval() != null) {
				if (this.getBlockDirectionInInterval().isDirectionOk(
						trainDirection, newArrivalTime)) {
				} else {
					double timeFromBlockInterval = this
							.getBlockDirectionInInterval()
							.getEarliestArrivalTimeFromBlockInterval(
									newArrivalTime, trainDirection);

					newArrivalTime = Math.max(newArrivalTime,
							timeFromBlockInterval);
					// System.out.println("Block: getEarliestArrivalTime: "
					// + newArrivalTime);
				}
			}

			return newArrivalTime;
		}
		return arrivalTime;
	}**/

	/**
	 * @param d
	 * @param d1
	 * @param i
	 * @return get the earliest possible arrival time.
	 */
	public double getEarliestArrivalTime(double d, double d1, int i) {
		int j = getOccupy().inInterval(d, d1);
		if (j != -1) {
			return getOccupy().get(j).getEndTime();
		}
		return d1;
	}

	/**
	 * @param Train
	 * @param noOfColor
	 * @param arrivalTime
	 * @param maxSignalAspectsToBeConsidered
	 * @param direction
	 * @return signal.
	 */
	public int getSignal(Train train, double arrivalTime,
			boolean consideringHalts, int maxSignalAspectsToBeConsidered,
			SimulationInstance simulationInstance) {
		return getBlockSignal(train.getDirection()).getSignal(this, train,
				arrivalTime, consideringHalts, maxSignalAspectsToBeConsidered,
				simulationInstance);
	}

	/**
	 * @param block
	 * @param Train
	 * @param numberOfSignalAspects
	 * @return path
	 */
	public Block getPath(Block block, Train Train, int numberOfSignalAspects) {
		Block block1 = new Block();
		Block block2 = this;
		block1.setMaximumPossibleSpeed(getMaximumPossibleSpeed());
		block1.setStartMilePost(getStartMilePost());
		block1.setEndMilePost(getEndMilePost());
		Debug.print((new StringBuilder()).append("SpeedRes ")
				.append(getMaximumPossibleSpeed())
				.append(" maximumPossibleSpeed   ")
				.append(block2.getMaximumPossibleSpeed()).toString());
		for (int i = 0; i < numberOfSignalAspects; i++) {
			double d = Math.min(block2.getMaximumPossibleSpeed(),
					Train.getMaximumPossibleSpeed());
			block1.getSpeedRestrictionList().add(
					new SpeedRestriction(d, block2.getStartMilePost(), block2
							.getEndMilePost()));
			for (int j = 0; j < block2.getSpeedRestrictionList().size(); j++) {
				if (block2.getSpeedRestrictionList().get(j).getSpeedLimit() < d) {
					block1.getSpeedRestrictionList().add(
							block2.getSpeedRestrictionList().get(j));
					Debug.print((new StringBuilder())
							.append("SpeedRes added  from block ").append(i)
							.toString());
				}
			}

			block1.setEndMilePost(block2.getEndMilePost());
			if (i == 0) {
				block2 = block;
			} else {
				block2 = block2.getNextBlock(Train);
			}
		}

		return block1;
	}

	/**
	 * Add the tiny blocks of the blocks from the startBlock till the signal
	 * value number of the blocks into the emptyBlock.
	 * 
	 * @param emptyBlock
	 * @param startBlock
	 * @param Train
	 * @param signal
	 * @param previousLink
	 * @param nextLink
	 * @param profileStartingMilePost
	 * @param enteredOnYellow
	 * @param enteringNextBlockOnYellow
	 * @param afterSighting
	 */
	public Block getEmptyBlockWithTinyBlocksAdded(Train train, int signal,
			Link previousLink, Link nextLink, double profileStartingMilePost,
			boolean enteredOnYellow, boolean enteringNextBlockOnYellow) {
		Debug.fine("getEmptyBlockWithTinyBlocksAdded: signal " + signal
				+ " enteringNextBlockOnYellow " + enteringNextBlockOnYellow);
		Block emptyBlock = new Block();

		emptyBlock.setMaximumPossibleSpeed(this.getMaximumPossibleSpeed());

		emptyBlock.setBlockNo(this.blockNo);

		Link previousIterLink = previousLink;
		Link nextIterLink;

		double milepost = emptyBlock.setMilePostsByPreviousLink(
				previousIterLink, this, train, profileStartingMilePost);

		Block iterBlock = this;
		for (int j = 0; j < signal; j++) {
			nextIterLink = iterBlock.getNextLink(train);

			if (nextIterLink == null) {
				enteredOnYellow = true;
			} else if (0 < j && j < signal - 1) {
				enteredOnYellow = false;
			} else if (0 < j && signal == 2) {
				enteredOnYellow = enteringNextBlockOnYellow;
			} else if (0 < j && 2 < signal && j == signal - 1) {
				enteredOnYellow = true;
			}

			Debug.fine("addTinyBlocksToBlock: milepost is " + milepost
					+ " enteredOnYellow " + enteredOnYellow);
			milepost = emptyBlock.addTinyBlocksToEmptyBlock(previousIterLink,
					iterBlock, train, nextIterLink, milepost, enteredOnYellow);

			// iterBlock = iterBlock.getNextBlock(Train);
			if (nextIterLink != null) {
				iterBlock = nextIterLink.getNextBlock();
				previousIterLink = nextIterLink;
			} else {
				break;
			}
		}

		int trainDirection = train.getDirection();
		emptyBlock.setEndMilePost(trainDirection, milepost);

		Debug.fine("addTinyBlocksToEmptyBlock: empty block's endMilePost in direction "
				+ direction + " is " + milepost);

		Debug.fine("addTinyBlocksToEmptyBlock: tinyBlockListSize "
				+ emptyBlock.getTinyBlockList().size());
		emptyBlock.adjustTinyBlockMilePostsByTrain(train);
		Debug.fine("addTinyBlocksToEmptyBlock: tinyBlockListSize "
				+ emptyBlock.getTinyBlockList().size());

		return emptyBlock;
	}

	public void setStartMilePost(int trainDirection, double milepost) {
		if (trainDirection == GlobalVar.UP_DIRECTION) {
			startMilePost = milepost;
		} else {
			endMilePost = milepost;
		}

	}

	public void setEndMilePost(int trainDirection, double milepost) {
		if (trainDirection == GlobalVar.UP_DIRECTION) {
			endMilePost = milepost;
		} else {
			startMilePost = milepost;
		}

	}

	private void adjustTinyBlockMilePostsByTrain(Train train) {
		TinyBlockList tinyBlockList = this.getTinyBlockList();
		Collections.sort(tinyBlockList, new SortTinyBlock());

		int trainDirection = train.getDirection();
		double trainLength = train.getLength();

		TinyBlockList newTinyBlockList = new TinyBlockList();

		TreeSet<Double> sortedMilePosts = new TreeSet<Double>();
		for (TinyBlock tinyBlock : tinyBlockList) {
			double tinyBlockStartMilePost;
			double tinyBlockExtendedEndMilePost;
			double milepost1 = tinyBlock.getStartMilePost(trainDirection);
			double milepost2 = tinyBlock.getEndMilePost(trainDirection)
					+ trainDirection * trainLength;
			tinyBlockStartMilePost = Math.min(milepost1, milepost2);
			tinyBlockExtendedEndMilePost = Math.max(milepost1, milepost2);

			tinyBlockStartMilePost = Math.max(tinyBlockStartMilePost,
					startMilePost);
			tinyBlockExtendedEndMilePost = Math.min(
					tinyBlockExtendedEndMilePost, endMilePost);

			tinyBlockStartMilePost = GlobalVariables
					.roundToThreeDecimals(tinyBlockStartMilePost);
			tinyBlockExtendedEndMilePost = GlobalVariables
					.roundToThreeDecimals(tinyBlockExtendedEndMilePost);

			sortedMilePosts.add(tinyBlockStartMilePost);
			sortedMilePosts.add(tinyBlockExtendedEndMilePost);
		}

		Iterator<Double> startMilePostIterator = sortedMilePosts.iterator();
		Iterator<Double> endMilePostIterator = sortedMilePosts.iterator();

		if (!endMilePostIterator.hasNext()) {
			return;
		}

		endMilePostIterator.next();
		for (; endMilePostIterator.hasNext();) {
			double newTinyStartMilePost = startMilePostIterator.next();
			double newTinyEndMilePost = endMilePostIterator.next();

			Debug.fine("Searching for: newTinyStartMilePost "
					+ newTinyStartMilePost + " newTinyEndMilePost "
					+ newTinyEndMilePost);

			for (TinyBlock tinyBlock : tinyBlockList) {
				double tinyBlockStartMilePost = tinyBlock.getStartMilePost();
				double tinyBlockEndMilePost = tinyBlock.getEndMilePost();

				Debug.fine("Searching in: tinyBlockStartMilePost "
						+ tinyBlockStartMilePost + " tinyBlockEndMilePost "
						+ tinyBlockEndMilePost);

				if (tinyBlockStartMilePost <= newTinyStartMilePost
						&& newTinyEndMilePost <= tinyBlockEndMilePost) {
					double accelerationChange = tinyBlock
							.getAccelerationChange();
					double decelerationChange = tinyBlock
							.getDecelerationChange();
					double maxSpeed = tinyBlock.getMaxSpeed();

					TinyBlock newTinyBlock = new TinyBlock(
							newTinyStartMilePost, newTinyEndMilePost,
							accelerationChange, decelerationChange, maxSpeed);
					newTinyBlockList.add(newTinyBlock);
				}
			}
		}

		for (TinyBlock newTinyBlock : newTinyBlockList) {
			double newTinyStartMilePost = newTinyBlock.getStartMilePost();
			double newTinyEndMilePost = newTinyBlock.getEndMilePost();

			for (TinyBlock tinyBlock : tinyBlockList) {
				double tinyBlockMaxSpeed = tinyBlock.getMaxSpeed();

				if (tinyBlockMaxSpeed < newTinyBlock.getMaxSpeed()) {
					double tinyBlockStartMilePost;
					double tinyBlockExtendedEndMilePost;
					double milepost1 = tinyBlock
							.getStartMilePost(trainDirection);
					double milepost2 = tinyBlock.getEndMilePost(trainDirection)
							+ trainDirection * trainLength;
					tinyBlockStartMilePost = Math.min(milepost1, milepost2);
					tinyBlockExtendedEndMilePost = Math.max(milepost1,
							milepost2);

					tinyBlockStartMilePost = GlobalVariables
							.roundToThreeDecimals(tinyBlockStartMilePost);
					tinyBlockExtendedEndMilePost = GlobalVariables
							.roundToThreeDecimals(tinyBlockExtendedEndMilePost);

					if (tinyBlockStartMilePost <= newTinyStartMilePost
							&& newTinyEndMilePost <= tinyBlockExtendedEndMilePost) {
						newTinyBlock.setMaxSpeed(tinyBlockMaxSpeed);
					}
				}

			}
		}

		this.setTinyBlockList(newTinyBlockList);
	}

	public double getMilePostsByNextLink(Link nextLink, Train train,
			double profileStartingMilePost) {
		int trainDirection = train.getDirection();
		double milepost = this.getEndMilePost(trainDirection);
		// double milepost = profileStartingMilePost;
		Debug.fine("getMilePostsByNextLink: startBlockEndMilePost in "
				+ GlobalVariables.getDirectionString(trainDirection) + " is "
				+ milepost);

		if (nextLink != null) {
			milepost = milepost - trainDirection
					* nextLink.getPreviousBlockRelativeMilePost();
			Debug.fine("getMilePostsByNextLink: startBlockLength "
					+ getLength() + " previousLinkPreviousRelativeMilePost "
					+ nextLink.getPreviousBlockRelativeMilePost());
		}

		milepost = GlobalVariables.roundToThreeDecimals(milepost);

		return milepost;
	}

	public double getMilePostsByPreviousLink(Link previousLink, Train train,
			double profileStartingMilePost) {
		int trainDirection = train.getDirection();
		double milepost = this.getStartMilePost(trainDirection);
		// double milepost = profileStartingMilePost;

		if (previousLink != null && previousLink.getLength() > 0.0001) {
			milepost = milepost
					+ trainDirection
					* (previousLink.getNextBlockRelativeMilePost() - previousLink
							.getLength());

			Debug.fine("getMilePostsByPreviousLink: newStartMilepost "
					+ milepost);
		}

		milepost = GlobalVariables.roundToThreeDecimals(milepost);

		return milepost;
	}

	private double setMilePostsByPreviousLink(Link previousLink,
			Block startBlock, Train train, double profileStartingMilePost) {

		int trainDirection = train.getDirection();
		double milepost = startBlock.getStartMilePost(trainDirection);
		// double milepost = profileStartingMilePost;
		milepost = startBlock.getMilePostsByPreviousLink(previousLink, train,
				profileStartingMilePost);

		if (trainDirection == GlobalVar.UP_DIRECTION) {
			setStartMilePost(milepost);
		} else {
			setEndMilePost(milepost);
		}

		return milepost;
	}

	public Link getNextLink(Train train) {
		int trainDirection = train.getDirection();

		if (train.isScheduled()) {
			if (!isLoop()) {
				Block nextBlock = getNextBlockByDirection(trainDirection);
				Link nextBlockLink = null;
				if (nextBlock == null)
					return null;
				Block nextToNextBlock = nextBlock
						.getNextBlockByDirection(trainDirection);
				if (nextToNextBlock != null && nextToNextBlock.isLoop()) {
					Loop nextToNextLoop = (Loop) nextToNextBlock;
					String stationName = nextToNextLoop.getStationName();

					ReferenceTableEntry referenceTableEntry = train
							.getRefTabEntryFromStationName(stationName);
					if (referenceTableEntry == null) {
						LinkList nextblockarray = getNextLinks(trainDirection);
						return nextblockarray.getNextMainLink();
					} else {
						Loop nextReferenceLoop = referenceTableEntry
								.getReferenceLoop();
						LinkList nextblockarray = getNextLinks(trainDirection);
						if (nextReferenceLoop != null) {
							nextblockarray = getNextLinks(trainDirection)
									.getSortedLinks(train,
											nextReferenceLoop.getBlockNo());
						}
						return nextblockarray.getNextMainLink();
					}

				} else if (!nextBlock.isLoop()) {
					LinkList nextblockarray = getNextLinks(trainDirection);
					return nextblockarray.getNextMainLink();
				} else {
					String nextStationName = ((Loop) nextBlock).getStation()
							.getStationName();
					ArrayList<ReferenceTableEntry> arraylist = train
							.getRefTables();
					int refTablesSize = train.getRefTables().size();
					int preferredLoop = -1;
					for (int l = 0; l < refTablesSize; l++) {
						if (((ReferenceTableEntry) arraylist.get(l))
								.getStationName().equalsIgnoreCase(
										nextStationName)) {
							preferredLoop = train.getRefTables().get(l)
									.getReferenceLoopNo();
						}
					}

					LinkList nextblockarray2 = getNextLinks(trainDirection);
					Block block = null;
					Link link = null;
					int i1 = 0;
					while (i1 < nextblockarray2.size()) {
						Link linkIter = (Link) nextblockarray2.get(i1);
						Block blockIter = linkIter.getNextBlock();
						if (blockIter.getBlockNo() == preferredLoop
								&& (blockIter.getDirection() == trainDirection || blockIter
										.getDirection() == GlobalVar.COMMON_DIRECTION)) {
							link = linkIter;
							block = blockIter;
							break;
						}
						//Loop nextLoop = (Loop) blockIter;
						if (blockIter.getBlockNo() == nextBlock.getBlockNo() ) {
							nextBlockLink = linkIter;
						}

						i1++;
					}

					if (block != null)
						Debug.fine("getNextLink: preferredLoop "
								+ preferredLoop + " " + block.blockName);
					else {
						Debug.fine("getNextLink: no preferred loop. blockIter is null.");

						link = nextBlockLink;
						Debug.fine("getNextLink: reassigning block as nextBlock "
								+ nextBlock.getBlockName());
					}
					return link;
				}
			} else {
				Loop loop = (Loop) this;
				Loop nextReferenceLoop = loop.getNextReferenceLoop(train
						.getRefTables());
				LinkList nextblockarray = getNextLinks(trainDirection);
				if (nextReferenceLoop != null) {
					nextblockarray = getNextLinks(trainDirection)
							.getSortedLinks(train,
									nextReferenceLoop.getBlockNo());
				}
				return nextblockarray.getNextMainLink();
			}

		} else {
			LinkList nextblockarray1 = getNextLinks(trainDirection);
			// System.out.println("getNextLink: next block " + blockNo + " has "
			// + nextblockarray1.size() + " links");
			return nextblockarray1.getNextMainLink();
		}

	}

	private double addPreviousLinkTinyBlocks(Block iterBlock, Train train,
			Link previousLink, double milepost) {
		double linkStartMilePost, linkEndMilePost;

		int trainDirection = train.getDirection();

		if (trainDirection == GlobalVar.UP_DIRECTION) {
			linkStartMilePost = milepost;
			milepost += previousLink.getLength();
			linkEndMilePost = milepost;
			milepost -= previousLink.getNextBlockRelativeMilePost();
		} else {
			linkEndMilePost = milepost;
			milepost -= previousLink.getLength();
			linkStartMilePost = milepost;
			milepost += previousLink.getNextBlockRelativeMilePost();
		}

		double maxSpeed = previousLink.getMaximumPossibleSpeed() / 60D;
		linkStartMilePost = GlobalVariables
				.roundToThreeDecimals(linkStartMilePost);
		linkEndMilePost = GlobalVariables.roundToThreeDecimals(linkEndMilePost);
		milepost = GlobalVariables.roundToThreeDecimals(milepost);

		TinyBlock previousLinkTinyBlockFormat = new TinyBlock(
				linkStartMilePost, linkEndMilePost, 0, 0, maxSpeed);

		this.getTinyBlockList().add(previousLinkTinyBlockFormat);
		Debug.fine("");
		Debug.fine("previousLinkTinyBlockFormat: start " + linkStartMilePost
				+ " end " + linkEndMilePost + " maxSpeed "
				+ previousLinkTinyBlockFormat.getMaxSpeed());
		Debug.fine("previousLinkTinyBlockFormat: milepost is " + milepost);
		return milepost;
	}

	/**
	 * 1) It does some signal handling. with signalFailFlag = 0 , 1, 2 which
	 * respectively means that the signal has not failed, that the signal has
	 * failed in a daytime and that the signal has failed in the night time. So,
	 * it respectively adds the tinyblocks of the SpeedRestrictions or
	 * SignalFailedSpeedRestrictions or NightSignalFailedSpeedRestrictions 2) It
	 * does that for all the blocks that it can find till the signal (i.e. the
	 * number of blocks that are free to go at once) by updating the block to
	 * getNextBlock(train.direction,train) 3) It then creates an instance of
	 * velocityProfileArray for the tinyBlockArrays obtained by calling to the
	 * constructor VelocityProfileArray(train, block, startVelocity1,
	 * endVelocity1) 4) After having constructed the velocityProfileArray, it
	 * creates another instance of VelocityProfileArray namely
	 * velocityProfileArray1 which is a velocityProfileArray for the specific
	 * block that we are considering. If the GlobalVar.overlap is false:
	 * velocityProfileArray1 = velocityprofilearray.returnInterval(
	 * startMilePost, endMilePost); 5) The blockVelocityProfileArray, thus
	 * returned is given to a RunTimeReturn constructor which notes the time for
	 * the block and the velocity with the train enters the block and the
	 * velocityProfileArray1 (i.e. the blockVelocityProfileArray), which can be
	 * used for further use.
	 * 
	 * @param Train
	 * @param signal
	 * @param d
	 * @param startVelocity1
	 * @param endVelocity1
	 * @param previousLink
	 * @param nextLink
	 * @param profileStartingMilePost
	 * @param enteredOnYellow
	 * @return runTimeSignal... totalTime is -1 if the velocityProfileArray has
	 *         no velocity profile in it. Its an exceptional case.
	 */
	public RunTimeReturn getNonOverlappingRunTimeSignal(Train train,
			int signal, double startVelocity, double endVelocity,
			Link previousLink, Link nextLink, double profileStartingMilePost,
			boolean enteredOnYellow) {

		boolean enteringNextBlockOnYellow = true;
		VelocityProfileArray velocityProfileArray = getVelocityProfileArray(
				train, signal, startVelocity, endVelocity, nextLink,
				previousLink, profileStartingMilePost, enteredOnYellow,
				enteringNextBlockOnYellow);

		// System.out.println("getNonOverlappingRunTimeSignal: total runTime for "
		// + signal + " blocks is " + velocityProfileArray.getRunTime());
		// System.out.println(velocityProfileArray.size());
		// for (int j = 0; j < velocityProfileArray.size(); j++) {
		// System.out.println(" A " + " "
		// + velocityProfileArray.get(j).getStartMilePost() + " "
		// + velocityProfileArray.get(j).getEndMilePost());
		// }

		if (0 == velocityProfileArray.size()) {
			System.out
					.println("getNonOverlappingRunTimeSignal: velocity profile array size is zero for train "
							+ train.getTrainNo());
			return new RunTimeReturn(-1D, -1D, velocityProfileArray, null);
		}

		int trainDirection = train.getDirection();
		double startMilePost, endMilePost, linkMilePost;
		if (previousLink == null || previousLink.length < 0.0001) {
			startMilePost = this.getStartMilePost(trainDirection);
			linkMilePost = startMilePost;
		} else {
			startMilePost = getMilePostsByPreviousLink(previousLink, train,
					profileStartingMilePost);
			linkMilePost = startMilePost + trainDirection
					* previousLink.getLength();
		}

		if (nextLink == null || nextLink.length < 0.0001)
			endMilePost = this.getEndMilePost(trainDirection);
		else
			endMilePost = getMilePostsByNextLink(nextLink, train,
					profileStartingMilePost);

		linkMilePost = GlobalVariables.roundToThreeDecimals(linkMilePost);

		return getNonOverlappingRunTimeReturn(train, velocityProfileArray,
				startMilePost, endMilePost, linkMilePost, previousLink);

	}

	/**
	 * @param Train
	 * @param velocityProfileArray
	 * @param linkMilePost
	 * @return {@link RunTimeReturn} from the beginning of the block till only
	 *         the end of the block and not the overlapping part of the train
	 *         when the train just exits the block.
	 */
	public RunTimeReturn getNonOverlappingRunTimeReturn(Train train,
			VelocityProfileArray velocityProfileArray, double startMilePost,
			double endMilePost, double linkMilePost, Link previousLink) {
		Debug.fine("getNonOverlappingRunTimeReturn: " + blockName
				+ " startMilePost " + startMilePost + " endMilePost "
				+ endMilePost + " linkMilePost " + linkMilePost);

		VelocityProfileArray previousLinkVelocityProfileArray = velocityProfileArray
				.returnInterval(startMilePost, linkMilePost);
		VelocityProfileArray nonOverlappingProfileArray = velocityProfileArray
				.returnInterval(linkMilePost, endMilePost);

		for (VelocityProfile velocityProfile : previousLinkVelocityProfileArray) {
			velocityProfile.setTrackSegment(previousLink);
		}

		for (VelocityProfile velocityProfile : nonOverlappingProfileArray) {
			velocityProfile.setTrackSegment(this);
		}

		Debug.fine("getNonOverlappingRunTimeReturn: nonOverLappingRunTime is "
				+ nonOverlappingProfileArray.getRunTime());

		double nextBlockStartVelocity = nonOverlappingProfileArray
				.getEndVelocity(train);

		double runTime = nonOverlappingProfileArray.getRunTime()
				+ previousLinkVelocityProfileArray.getRunTime();
		return new RunTimeReturn(runTime, nextBlockStartVelocity,
				nonOverlappingProfileArray, previousLinkVelocityProfileArray);

	}

	/**
	 * Get the {@link RunTimeReturn} for the block and the overlapping part of
	 * the train after it just exits the block.
	 * 
	 * @param Train
	 * @param velocityprofilearray
	 * @return {@link RunTimeReturn} from the beginning of the block till the
	 *         overlapping part of the train
	 */
	public RunTimeReturn getOverlappingRunTimeReturn(Train train,
			VelocityProfileArray velocityprofilearray,
			OverlapParameters overlapParameters) {
		VelocityProfileArray overlappingProfileArray;

		double overlapStartDistance = overlapParameters.overlapStartDistance;
		double overlapEndDistance = overlapParameters.overlapEndDistance;

		if (train.getDirection() == GlobalVar.UP_DIRECTION) {
			overlappingProfileArray = velocityprofilearray.returnInterval(
					overlapStartDistance, overlapEndDistance);
		} else {// train.direction == 1
			overlappingProfileArray = velocityprofilearray.returnInterval(
					overlapEndDistance, overlapStartDistance);
		}
		if (overlappingProfileArray == null) {
			throw new NullPointerException("overlappingProfileArray is null");
		}

		double runTime = overlappingProfileArray.getRunTime();

		Debug.fine("getOverlappingRunTimeReturn: overlapStartDistance "
				+ overlapStartDistance + " overlapEndDistance "
				+ overlapEndDistance + " runTime " + runTime);
		return new RunTimeReturn(runTime, 0.0D, overlappingProfileArray, null);

	}

	/**
	 * @param Train
	 * @param signal
	 * @param arrivalTime
	 * @param nextBlock
	 * @param runtimereturn
	 * @param finalVelocity
	 * @param numberOfSignalAspects
	 * @param warnerDistance
	 * @param maxSignalAspectsToBeConsidered
	 * @return calculateRecurrenceVelocity
	 */
	public RunTimeReturn calculateWarnerVelocityProfile(Train train,
			int signal, double arrivalTime, Block nextBlock,
			RunTimeReturn runtimereturn, double finalVelocity,
			double warnerDistance, SimulationInstance simulationInstance,
			int maxSignalAspectsToBeConsidered) {

		double tempArrivalTime = arrivalTime;

		Debug.fine(" CAME TO CALCULATE VELOCITY PROFILE FOR WARNER BLOCK ");

		if (Math.abs(getEndMilePost() - getStartMilePost()) <= warnerDistance) {
			return runtimereturn;
		}

		// runTimeReturnVelocityProfileArray abbreviation
		VelocityProfileArray originalProfile = runtimereturn
				.getVelocityProfileArray();

		// We are not completely sure about the signal. So we should try and
		// come to a halt before the signal.
		double startVelocity = originalProfile.getStartVelocity(train);
		originalProfile = new VelocityProfileArray(train, this, startVelocity,
				0);

		tempArrivalTime += originalProfile.getRunTime();

		Block largeBlock = new Block();
		largeBlock.setMaximumPossibleSpeed(getMaximumPossibleSpeed());
		largeBlock.setNextLinks(nextBlocks);
		largeBlock.setPreviousLinks(previousBlocks);

		// System.out.println("largeBlock startMilePost "
		// + largeBlock.startMilePost);
		// abbreviation for Warner Distance till endMilePost
		VelocityProfileArray warnerProfile = null;

		SplitProfileForWarner splitProfileForWarner = splitProfileForWarnerDistance(
				train, originalProfile, largeBlock, warnerDistance);

		warnerProfile = splitProfileForWarner.warnerProfile;
		double warnerBlockStartVelocity = splitProfileForWarner.warnerBlockStartVelocity;

		tempArrivalTime -= warnerProfile.getRunTime();

		boolean consideringHalts = true;
		int nextBlockSignal = nextBlock.getSignal(train, tempArrivalTime,
				consideringHalts, maxSignalAspectsToBeConsidered,
				simulationInstance);

		// System.out
		// .println("Returned from getSignal to calculateWarnerVelocityProfile");

		// System.out.println("signal " + signal + " nextBlockSignal "
		// + nextBlockSignal);

		if (signal > nextBlockSignal) {
			return runtimereturn;
		}

		Debug.print((new StringBuilder()).append(" Just B4 first for loop ")
				.append(signal).toString());

		int trainDirection = train.getDirection();
		Block blockIter = nextBlock;
		for (int l = 0; l < signal; l++) {
			largeBlock.setEndMilePost(trainDirection,
					blockIter.getEndMilePost(trainDirection));

			blockIter = blockIter.getNextBlock(train);
		}

		Debug.print((new StringBuilder())
				.append("  Going to calculate warner signal with ")
				.append(warnerBlockStartVelocity).append("  ")
				.append(finalVelocity).toString());

		// abbreviation for WarnerDistanceToSignalFreeBlocks
		// System.out.println("calculateWarnerDistance: compute velocityProfile");
		VelocityProfileArray WD_to_SFB = new VelocityProfileArray(train,
				largeBlock, warnerBlockStartVelocity, finalVelocity);

		Debug.print("came back after calculating the velocity profile for the warner signal block");
		Debug.print(" profile under consideration is ");

		// if (0 == WD_to_SFB.size()) {
		// System.out
		// .println("calculateWarnerProfile: error: returning a null profile array");
		// return new RunTimeReturn(-1D, -1D, null);
		// }

		Debug.print("going to/find the running time required for this section ");

		// boolean flag = false;
		double endVelocity = 0;
		if (WD_to_SFB.size() > 0) {
			double endMilePost = getEndMilePost(trainDirection);
			double warnerMilePost = endMilePost - trainDirection
					* warnerDistance;
			warnerProfile = WD_to_SFB.returnInterval(endMilePost,
					warnerMilePost);
			endVelocity = warnerProfile.getEndVelocity(train);
		}

		Debug.print("after calculating the velocity profile for the warner signal block ");
		// boolean flag1 = true;
		originalProfile.clear();
		VelocityProfileArray beforeWarnerProfile = splitProfileForWarner.majorProfile;
		originalProfile.addAll(beforeWarnerProfile);
		originalProfile.addAll(warnerProfile);
		originalProfile.setRunTime(beforeWarnerProfile.getRunTime()
				+ warnerProfile.getRunTime());

		return new RunTimeReturn(originalProfile.getRunTime(), endVelocity,
				originalProfile, null);
	}

	/**
	 * Split the profile of the block into two profiles one for the warner
	 * distance and other for the rest of the block. In the end return the
	 * startVelocity of the warner profile.
	 * 
	 * @param Train
	 * @param originalProfile
	 * @param largeBlock
	 * @param warnerDistance
	 * @return the start velocity of the warner profile.
	 */
	public SplitProfileForWarner splitProfileForWarnerDistance(Train train,
			VelocityProfileArray originalProfile, Block largeBlock,
			double warnerDistance) {

		SplitProfileForWarner splitProfileForWarner = new SplitProfileForWarner();
		splitProfileForWarner.largeBlock = largeBlock;
		splitProfileForWarner.originalProfile = originalProfile;

		VelocityProfileArray majorProfile = splitProfileForWarner.majorProfile;

		double warnerBlockStartVelocity = 0;
		if (train.getDirection() == GlobalVar.UP_DIRECTION) {
			Debug.print("train direction is zero  ");

			splitProfileForWarner.warnerProfile = originalProfile
					.returnInterval(getEndMilePost() - warnerDistance,
							getEndMilePost());

			majorProfile = originalProfile.returnInterval(getStartMilePost(),
					getEndMilePost() - warnerDistance);

			if (majorProfile.size() != 0) {
				warnerBlockStartVelocity = (majorProfile.get(majorProfile
						.size() - 1)).getEndVelocity();
			}

			largeBlock.setStartMilePost(getEndMilePost() - warnerDistance);
			largeBlock.setEndMilePost(getEndMilePost());
			largeBlock.setTinyBlockList(getTinyBlockList().split(
					getEndMilePost() - warnerDistance).get(1));
		} else {
			Debug.print("train direction is not zero ");
			splitProfileForWarner.warnerProfile = originalProfile
					.returnInterval(getStartMilePost(), getStartMilePost()
							+ warnerDistance);
			majorProfile = originalProfile.returnInterval(getStartMilePost()
					+ warnerDistance, getEndMilePost());
			if (majorProfile.size() != 0) {
				warnerBlockStartVelocity = (majorProfile.get(majorProfile
						.size() - 1)).getStartVelocity();
			}

			largeBlock.setStartMilePost(getStartMilePost());
			largeBlock.setEndMilePost(getStartMilePost() + warnerDistance);
			largeBlock.setTinyBlockList(getTinyBlockList().split(
					getStartMilePost() + warnerDistance).get(0));
		}

		splitProfileForWarner.majorProfile = majorProfile;
		splitProfileForWarner.warnerBlockStartVelocity = warnerBlockStartVelocity;
		return splitProfileForWarner;
	}

	/**
	 * @param Train
	 * @param signal
	 * @param arrivalTime
	 * @param startVelocity
	 * @return runTimeSignal
	 */
	// public RunTimeReturn getRunTimeSignal(Train Train,
	// int signal, double arrivalTime, double startVelocity) {
	// return getRunTimeSignal(Train, signal, arrivalTime,
	// startVelocity, 0.0D);
	// }

	/**
	 * @param Train
	 * @param d
	 * @param d1
	 * @return getRunTimeVelocity
	 */
	public RunTimeReturn getRunTimeVelocity(Train Train, double d, double d1) {
		Block block = new Block();
		block.setStartMilePost(getStartMilePost());
		block.setEndMilePost(getEndMilePost());
		double d2 = getMaximumPossibleSpeed() <= Train
				.getMaximumPossibleSpeed() ? getMaximumPossibleSpeed() : Train
				.getMaximumPossibleSpeed();
		d2 = d <= d2 ? d : d2;
		block.getSpeedRestrictionList().add(
				new SpeedRestriction(d2, getStartMilePost(), getEndMilePost()));
		for (int i = 0; i < getSpeedRestrictionList().size(); i++) {
			if (getSpeedRestrictionList().get(i).getSpeedLimit() < d2) {
				block.getSpeedRestrictionList().add(
						getSpeedRestrictionList().get(i));
			}
		}

		// System.out.println("getRunTimeVelocity: compute velocityProfile");
		VelocityProfileArray velocityprofilearray = new VelocityProfileArray(
				Train, block, 0.0D, 0.0D);
		return new RunTimeReturn(velocityprofilearray.getRunTime(), 0.0D,
				velocityprofilearray, null);
	}

	/**
	 * @param Train
	 * @param arrivalTime
	 * @param departureTime
	 * @param startVelocity
	 * @param nextFastestLink
	 * @param nextReferenceLoopNumber
	 * @param simulationType
	 * @param numberOfSignalAspects
	 * @param link
	 * @return {@link StatusTraverseBlock}
	 */
	public StatusTraverseBlock traverseBlock(Train train, double arrivalTime,
			double departureTime, double startVelocity, Link previousLink,
			Link nextFastestLink, double profileStartingMilePost,
			SimulationInstance simulationInstance, int nextReferenceLoopNumber) {

		SchedulerFactory schedulerfactory = new SchedulerFactory();

		BlockScheduler blockscheduler = schedulerfactory.getScheduler(this,
				train, simulationInstance);
		if (blockscheduler == null) {
			throw new NullPointerException(
					"traverseBlock: blockScheduler is null.");
		}
		Debug.print("Block: StatusTraverseBlock: returning.");

		return blockscheduler.traverseBlock(arrivalTime, departureTime,
				startVelocity, previousLink, nextFastestLink,
				profileStartingMilePost, nextReferenceLoopNumber);
	}

	/**
	 * @return true if the block is a loop
	 */
	public boolean isLoop() {
		return this.getClass().getName()
				.equalsIgnoreCase(new Loop().getClass().getName());
	}
	
	/**public boolean isFreightLoop() {
		
		if (this.isLoop()){
			Loop loop = (Loop) this;
			if (loop.getLoopTrainType().equalsIgnoreCase("all")) {
				return true;
			} else if(loop.getLoopTrainType().equalsIgnoreCase("Freight")) {
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}**/
	

	/**
	 * @param time
	 * @return false if no signal failure is found. Else return true.
	 */
	public boolean isSignalFailed(double time) {
		Debug.print((new StringBuilder()).append("In signalFailed function   ")
				.append(time).toString());

		int i = 0;
		for (int j = 0; j < getSignalFailure().getArraySignalFailure().size(); j++) {

			SignalFailureFormat signalfailureformat = getSignalFailure()
					.getArraySignalFailure().get(j);
			if (time >= signalfailureformat.getStart()
					&& time <= signalfailureformat.getEnd()) {
				return true;
				// i = 1;
				// i = j+1;// Devendra Made a correction here which should
				// probably
				// // be right.
			}
		}

		Debug.print((new StringBuilder()).append(" Returning ").append(i)
				.append("from signal_failed function ").toString());
		return false;
	}

	/**
	 * @param Train
	 * @param arrivalTime
	 * @return true if the block direction is same as the train direction at the
	 *         given arrival time
	 */
	public boolean isDirectionOk(Train Train, double arrivalTime) {

		// block direction not specified at all
		if (this.getBlockDirectionInInterval() == null)
			return (this.getDirection() == Train.getDirection() || this
					.getDirection() == 2);

		return this.getBlockDirectionInInterval().isDirectionOk(
				Train.getDirection(), arrivalTime);
	}

	/**
	 * @param iterLink
	 * @param tinyBlockArray
	 * @param Train
	 * @param iterBlock
	 * @param nextIterLink
	 * @param milepost
	 * @param enteredOnYellow
	 */
	public double addTinyBlocksToEmptyBlock(Link previousLink, Block iterBlock,
			Train train, Link nextIterLink, double milepost,
			boolean enteredOnYellow) {

		if (previousLink != null && previousLink.getLength() > 0.0001) {
			milepost = this.addPreviousLinkTinyBlocks(iterBlock, train,
					previousLink, milepost);
		}

		int trainDirection = train.getDirection();
		if (trainDirection == GlobalVar.DOWN_DIRECTION) {
			milepost = milepost - iterBlock.getLength();
		}

		TinyBlockList tinyBlockList;
		switch (iterBlock.getSignalFailFlag()) {
		case 0:
			tinyBlockList = iterBlock.getTinyBlockList();
			break;
		case 1:
			Debug.print("Block: getRunTimeSignal: TRAIN ENTERS SIGNAL FAILED BLOCK IN day TIME ");
			tinyBlockList = iterBlock.getSfsr();
			break;
		case 2:
			Debug.print("Block: getRunTimeSignal: TRAIN ENTERS SIGNAL FAIELD BLOCK IN NIGHT TIME ");
			tinyBlockList = iterBlock.getNightsfsr();
			break;
		default:
			tinyBlockList = null;
			System.out
					.println("Block: addTinyBlocksToEmptyBlock: tinyBlockArray is null");
		}

		if (tinyBlockList == null) {
			throw new NullPointerException(
					"addTinyBlocksToEmptyBlock: tinyBlockArray passed is null");
		} else {
			Debug.fine("addTinyBlocksToEmptyBlock: Finding searchTinyBlockMilePosts for block "
					+ iterBlock.blockNo);
			double firstSearchTinyBlockMilePost = iterBlock
					.getSearchStartMilePost(previousLink, train);
			double secondSearchTinyBlockMilePost = iterBlock
					.getSearchEndMilePost(nextIterLink, train);

			double startTinyBlockMilePost = Math
					.min(firstSearchTinyBlockMilePost,
							secondSearchTinyBlockMilePost);
			double endTinyBlockMilePost = Math
					.max(firstSearchTinyBlockMilePost,
							secondSearchTinyBlockMilePost);

			Debug.fine("addTinyBlocksToEmptyBlock: startTinyBlockMilePost "
					+ startTinyBlockMilePost + " endTinyBlockMilePost "
					+ endTinyBlockMilePost);
			TinyBlockList newTinyBlockList = tinyBlockList
					.getTinyBlockListBetweenMilePosts(startTinyBlockMilePost,
							endTinyBlockMilePost);

			if (GlobalVariables.automaticWarningSystemOn) {
				if (enteredOnYellow) {
					newTinyBlockList = newTinyBlockList.getAWSTinyBlockList(
							startTinyBlockMilePost, endTinyBlockMilePost,
							trainDirection);
				}
			}

			for (TinyBlock tinyBlock : newTinyBlockList) {
				double newStartMilePost = milepost
						+ tinyBlock.getRelativeStartMilePost();
				tinyBlock.setStartMilePost(newStartMilePost);

				double newEndMilePost = milepost
						+ tinyBlock.getRelativeEndMilePost();
				tinyBlock.setEndMilePost(newEndMilePost);

				newStartMilePost = GlobalVariables
						.roundToThreeDecimals(newStartMilePost);
				newEndMilePost = GlobalVariables
						.roundToThreeDecimals(newEndMilePost);

				Debug.fine("addTinyBlocksToEmptyBlock: new start "
						+ newStartMilePost + " new end " + newEndMilePost);

				this.getTinyBlockList().add(tinyBlock);
			}

		}

		double newMilePost;
		if (nextIterLink != null /* && nextIterLink.getLength() > 0.0001 */) {
			if (trainDirection == GlobalVar.UP_DIRECTION) {
				newMilePost = milepost + iterBlock.getLength()
						- nextIterLink.getPreviousBlockRelativeMilePost();
			} else {
				newMilePost = milepost // - iterBlock.getLength()
						+ nextIterLink.getPreviousBlockRelativeMilePost();
			}
		} else {
			Debug.fine("addTinyBlocksToEmptyBlock: nextIterLink is null");
			if (trainDirection == GlobalVar.UP_DIRECTION) {
				newMilePost = milepost + iterBlock.getLength();
			} else {
				newMilePost = milepost;// - iterBlock.getLength();
			}
		}

		newMilePost = GlobalVariables.roundToThreeDecimals(newMilePost);
		Debug.fine("addTinyBlocksToEmptyBlock : newMilePost " + newMilePost);

		return newMilePost;
	}

	private double getSearchEndMilePost(Link nextLink, Train train) {
		double milepost;
		int trainDirection = train.getDirection();
		if (nextLink != null) {
			if (trainDirection == GlobalVar.UP_DIRECTION) {
				milepost = getLength()
						- nextLink.getPreviousBlockRelativeMilePost();
			} else {
				milepost = nextLink.getPreviousBlockRelativeMilePost();
			}
		} else {

			if (trainDirection == GlobalVar.UP_DIRECTION) {
				milepost = getLength();
			} else {
				milepost = 0;
			}
		}

		milepost = GlobalVariables.roundToThreeDecimals(milepost);
		Debug.fine("getSearchEndMilePost: returning milepost of block "
				+ blockNo + " start: " + getStartMilePost() + " end "
				+ getEndMilePost() + " searchEndMilePost " + milepost);
		return milepost;
	}

	private double getSearchStartMilePost(Link previousLink, Train train) {
		double searchStartMilePost;
		int trainDirection = train.getDirection();

		if (previousLink != null) {
			Block nextBlock = previousLink.getNextBlock();
			double nextBlockLength = nextBlock.getLength();
			if (trainDirection == GlobalVar.UP_DIRECTION)
				searchStartMilePost = previousLink
						.getNextBlockRelativeMilePost();
			else
				searchStartMilePost = nextBlockLength
						- previousLink.getNextBlockRelativeMilePost();
		} else {
			if (trainDirection == GlobalVar.UP_DIRECTION) {
				searchStartMilePost = 0;
			} else {
				searchStartMilePost = getLength();
			}
		}

		searchStartMilePost = GlobalVariables
				.roundToThreeDecimals(searchStartMilePost);

		Debug.fine("getSearchStartMilePost: returning milepost of block "
				+ blockNo + " start: " + getStartMilePost() + " end "
				+ getEndMilePost() + " searchStartMilePost "
				+ searchStartMilePost);
		return searchStartMilePost;
	}

	// This function is made for a specific case when the section has three
	// lines.
	public boolean allowsTrain(Train currTrain, double arrivalTime,
			String TRAIN_TYPE_ALLOWED_ON_THIRD_LINE) {
		// TODO Auto-generated method stub
		if (this.getBlockDirectionInInterval() != null) {// is a block on the
			// third
			// line
			if (currTrain.isSuburban()
					&& TRAIN_TYPE_ALLOWED_ON_THIRD_LINE
							.equalsIgnoreCase("ONLY_LONG_DISTANCE"))
				return false;
			else if (currTrain.isScheduled()
					&& TRAIN_TYPE_ALLOWED_ON_THIRD_LINE
							.equalsIgnoreCase("ONLY_SUBURBAN"))
				return false;
		}
		return true;
	}

	public int getBlockNo() {
		return blockNo;
	}

	public void setBlockNo(int blockNo) {
		this.blockNo = blockNo;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public Signal getBlockSignal(int direction) {
		if (direction == GlobalVar.UP_DIRECTION)
			return upDirectionSignal;
		return downDirectionSignal;
	}

	public void setBlockSignal(int direction, Signal signal) {
		if (direction == GlobalVar.UP_DIRECTION) {
			this.upDirectionSignal = signal;
		} else {
			this.downDirectionSignal = signal;
		}

	}

	public boolean isWarner() {
		return warner;
	}

	public void setWarner(boolean warner) {
		this.warner = warner;
	}

	public TinyBlockList getSfsr() {
		return sfsr;
	}

	public void setSfsr(TinyBlockList sfsr) {
		this.sfsr = sfsr;
	}

	public TinyBlockList getNightsfsr() {
		return nightsfsr;
	}

	public void setNightsfsr(TinyBlockList nightsfsr) {
		this.nightsfsr = nightsfsr;
	}

	public LinkList getNextLinks() {
		return nextBlocks;
	}

	public void setNextLinks(LinkList nextBlocks) {
		this.nextBlocks = nextBlocks;
	}

	public LinkList getPreviousLinks() {
		return previousBlocks;
	}

	public void setPreviousLinks(LinkList previousBlocks) {
		this.previousBlocks = previousBlocks;
	}

	public int getSignalFailFlag() {
		return signalFailFlag;
	}

	public void setSignalFailFlag(int signalFailFlag) {
		this.signalFailFlag = signalFailFlag;
	}

	public BlockDirectionInInterval getBlockDirectionInInterval() {
		return blockDirectionInInterval;
	}

	public void setBlockDirectionInInterval(
			BlockDirectionInInterval blockDirectionInInterval) {
		this.blockDirectionInInterval = blockDirectionInInterval;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		Block.logger = logger;
	}

	public SignalFailure getSignalFailure() {
		return signalFailure;
	}

	public void setSignalFailure(SignalFailure signalFailure) {
		this.signalFailure = signalFailure;
	}

	public void printOccupancies(int trainNo) {
		System.out.println("Printing occupancies");
		for (Interval interval : occupy) {
			if (interval.getTrainNo() == trainNo) {
				System.out.println("trainNo " + trainNo + " blockNo " + blockNo
						+ " startTime " + interval.getStartTime() + " endTime "
						+ interval.getEndTime());
			}
		}
	}

	public double returnMaxSpeedBetweenMilePosts(double start, double end) {
		double maxSpeed = getSpeedRestrictionList().returnMaxsp(start, end);
		if (maxSpeed <= 0) {
			maxSpeed = getMaximumPossibleSpeed();
		}

		return maxSpeed;
	}

	public Gradient returnGradientFormat(double startMilePost,
			double endMilePost) {
		return getGradientList().returnGradientFormat(startMilePost,
				endMilePost, this);
	}

	public GradientList getGradientList() {
		return gradientList;
	}

	public void setGradientList(GradientList gradientList) {
		this.gradientList = gradientList;
	}

	public ArrayList<Block> splitBlock(double newMilepost) {
		Block b1 = new Block(getStartMilePost(), newMilepost);
		Block b2 = new Block(newMilepost, getEndMilePost());
		ArrayList<Block> bl = new ArrayList<Block>();
		bl.add(b1);
		bl.add(b2);
		return bl;
	}

	@Override
	public boolean hasError() {
		if (getEndMilePost() < getStartMilePost()
				+ Block.minimumBlockLength) {
			// System.out.println("Block " + getBlockNo() + " is too short");
			return true;
		} else
			return false;
	}

	public double getOverlapDistance(int trainDirection) {
		if (trainDirection == GlobalVar.UP_DIRECTION) {
			return upDirectionOverlapDistance;
		} else
			return downDirectionOverlapDistance;

	}

	public double getOccupancyTimeByTrain(Train train) {
		Interval interval = getLastIntervalByTrain(train);
		if (interval != null)
			return interval.getTime();
		return 0;
	}

	public Interval getLastIntervalByTrain(Train train) {
		int trainNo = train.getTrainNo();
		Interval lastInterval = null;
		for (Interval interval : occupy) {
			if (interval.getTrainNo() == trainNo)
				return lastInterval = interval;
		}

		return lastInterval;
	}

	public IntervalArray getOnlyActualOccupancies() {
		IntervalArray intervalArray = new IntervalArray();
		for (Interval interval : occupy) {
			if (interval.isActualOccupancy()) {
				intervalArray.add(interval);
			}
		}

		return intervalArray;
	}

	public VelocityProfileArray getVelocityProfileArray(Train train,
			int signal, double startVelocity, double endVelocity,
			Link nextLink, Link previousLink, double profileStartingMilePost,
			boolean enteredOnYellow, boolean enteringNextBlockOnYellow) {

		if (this.startMilePost == this.endMilePost) {
			VelocityProfile velocityProfile = new VelocityProfile(
					startMilePost, endMilePost, 0, 0, 1, 0);
			VelocityProfileArray velocityProfileArray = new VelocityProfileArray();
			velocityProfileArray.add(velocityProfile);
			Debug.fine("getVelocityProfileArray: startMilePost "
					+ startMilePost + " same as endMilePost " + endMilePost);
			return velocityProfileArray;
		}

		Block emptyBlock = getEmptyBlockWithTinyBlocksAdded(train, signal,
				previousLink, nextLink, profileStartingMilePost,
				enteredOnYellow, enteringNextBlockOnYellow);

		// generates the velocity profile of the train considering the various
		// tiny blocks and acceleration and deceleration parts of the profile
		// System.out.println("block "+block.blockNo);
		Debug.fine("getVelocityProfileArray: startVelocity " + startVelocity
				+ " endVelocity " + endVelocity + " emptyBlock startMilePost "
				+ emptyBlock.getStartMilePost() + " emptyBlock endMilePost "
				+ emptyBlock.getEndMilePost());
		VelocityProfileArray velocityProfileArray = new VelocityProfileArray(
				train, emptyBlock, startVelocity, endVelocity);
		// System.out.println(velocityProfileArray.size());
		// for (int j = 0; j < velocityProfileArray.size(); j++) {
		// System.out.println(" A " + " "
		// + velocityProfileArray.get(j).getStartMilePost() + " "
		// + velocityProfileArray.get(j).getEndMilePost());
		// }
		Debug.print("Block: getRunTimeSignal: Out of VelocityProfileArray ");

		return velocityProfileArray;
	}

	// public double getLength(){
	// return length;
	// }

	public void setEndMilePostDirectly(double endMilePost) {
		super.setEndMilePost(endMilePost);
	}

	public void readCommonBlockLoopProperties(StreamTokenizer streamTokenizer)
			throws IOException {

		streamTokenizer.nextToken();
		int maximumSpeed = (int) streamTokenizer.nval;
		this.maximumPossibleSpeed = maximumSpeed;

		readLinks(GlobalVar.UP_DIRECTION, streamTokenizer);
		readLinks(GlobalVar.DOWN_DIRECTION, streamTokenizer);
		readSpeedLimits(streamTokenizer);
	}
	
	private void readSpeedLimits(StreamTokenizer streamTokenizer)
			throws IOException {
		streamTokenizer.nextToken();
		String speedLimitString = streamTokenizer.sval;

		streamTokenizer.nextToken();
		String speedLimitStartMilePostsString = streamTokenizer.sval;

		streamTokenizer.nextToken();
		String speedLimitEndMilePostsString = streamTokenizer.sval;

		StringTokenizer speedLimitTokenizer = new StringTokenizer(
				speedLimitString, ",");
		StringTokenizer startKMTokenizer = new StringTokenizer(
				speedLimitStartMilePostsString, ",");
		StringTokenizer endKMTokenizer = new StringTokenizer(
				speedLimitEndMilePostsString, ",");

		while (speedLimitTokenizer.hasMoreTokens()) {
			//sneha:speedlimit was int earlier now changed to double
			double speedLimit = Double.parseDouble(speedLimitTokenizer.nextToken());
			double startKm = Double.parseDouble(startKMTokenizer.nextToken());
			double endKm = Double.parseDouble(endKMTokenizer.nextToken());
			SpeedRestriction speedRestrictionFormat = new SpeedRestriction(
					speedLimit, startKm, endKm);
			speedRestrictionList.add(speedRestrictionFormat);
		}

	}
	
	private void readLinks(int direction, StreamTokenizer streamTokenizer)
			throws IOException {
		streamTokenizer.nextToken();
		String linkString = streamTokenizer.sval;

		streamTokenizer.nextToken();
		String lengthsString = streamTokenizer.sval;

		streamTokenizer.nextToken();
		String prioritiesString = streamTokenizer.sval;

		streamTokenizer.nextToken();
		String crossoversString = streamTokenizer.sval;

		StringTokenizer linkStringTokenizer = new StringTokenizer(linkString,
				",");
		StringTokenizer lengthsStringTokenizer = new StringTokenizer(
				lengthsString, ",");
		StringTokenizer prioritiesStringTokenizer = new StringTokenizer(
				prioritiesString, ",");
		StringTokenizer crossoversStringTokenizer = new StringTokenizer(
				crossoversString, ",");

		while (linkStringTokenizer.hasMoreTokens()) {
			Link link = new Link();
			int nextBlockId = Integer.parseInt(linkStringTokenizer.nextToken());
			double length = Double.parseDouble(lengthsStringTokenizer
					.nextToken());
			int priority = Integer.parseInt(prioritiesStringTokenizer
					.nextToken());
			String crossoverString = crossoversStringTokenizer.nextToken();

			link.setNextBlockNo(nextBlockId);
			link.length = length;
			link.setPriority(priority);
			if (!crossoverString.equalsIgnoreCase("#")) {
				link.crossoverIds.add(Integer.parseInt(crossoverString));
			}

			if (direction == GlobalVar.UP_DIRECTION)
				upLinks.add(link);
			else
				downLinks.add(link);
		}
	}

	public String getLinkListString(int direction) {
		if (direction == GlobalVar.UP_DIRECTION)
			return getLinkListString(upLinks);
		else
			return getLinkListString(downLinks);
	}

	private String getLinkListString(ArrayList<Link> linkList) {
		int size = linkList.size();
		String linkListString = "";
		for (int i = 0; i < size; i++) {
			linkListString += linkList.get(i).nextBlockNo;
			if (i < size - 1)
				linkListString += ",";
		}

		return linkListString;
	}

	public String getLinkPrioritiesString(int direction) {
		if (direction == GlobalVar.UP_DIRECTION)
			return getLinkPrioritiesString(upLinks);
		else
			return getLinkPrioritiesString(downLinks);
	}

	private String getLinkPrioritiesString(ArrayList<Link> linkList) {
		int size = linkList.size();
		String linkPrioritiesString = "";
		for (int i = 0; i < size; i++) {
			linkPrioritiesString += linkList.get(i).priority;
			if (i < size - 1)
				linkPrioritiesString += ",";
		}

		return linkPrioritiesString;
	}

	public String getLinkCrossoversString(int direction) {
		if (direction == GlobalVar.UP_DIRECTION)
			return getLinkCrossoversString(upLinks);
		else
			return getLinkCrossoversString(downLinks);
	}

	private String getLinkCrossoversString(ArrayList<Link> linkList) {
		int size = linkList.size();
		String linkCrossoversString = "";
		for (int i = 0; i < size; i++) {
			String crossoverString = linkList.get(i).getCrossoverString();
			linkCrossoversString += crossoverString;
			if (i < size - 1)
				linkCrossoversString += ",";
		}

		return linkCrossoversString;
	}

	public String getLinkLengthsString(int direction) {
		if (direction == GlobalVar.UP_DIRECTION)
			return getLinkLengthsString(upLinks);
		else
			return getLinkLengthsString(downLinks);
	}

	private String getLinkLengthsString(ArrayList<Link> linkList) {
		int size = linkList.size();
		String linkLengthsString = "";
		for (int i = 0; i < size; i++) {
			linkLengthsString += linkList.get(i).length;
			if (i < size - 1)
				linkLengthsString += ",";
		}

		return linkLengthsString;
	}

	public String getSpeedLimitsString() {
		ArrayList<SpeedRestriction> speedLimitList = speedRestrictionList;
		int size = speedLimitList.size();
		String speedLimitListString = "";
		for (int i = 0; i < size; i++) {
			speedLimitListString += speedLimitList.get(i).getSpeedLimit();
			if (i < size - 1)
				speedLimitListString += ",";
		}

		return speedLimitListString;
	}

	public String getSpeedLimitStartMilePostsString() {
		ArrayList<SpeedRestriction> speedLimitList = speedRestrictionList;
		int size = speedLimitList.size();
		String speedLimitStartMilePostsString = "";
		for (int i = 0; i < size; i++) {
			speedLimitStartMilePostsString += speedLimitList.get(i).relativeStartMilePost;
			if (i < size - 1)
				speedLimitStartMilePostsString += ",";
		}

		return speedLimitStartMilePostsString;
	}

	public String getSpeedLimitEndMilePostsString() {
		ArrayList<SpeedRestriction> speedLimitList = speedRestrictionList;
		int size = speedLimitList.size();
		String speedLimitEndMilePostsString = "";
		for (int i = 0; i < size; i++) {
			speedLimitEndMilePostsString += speedLimitList.get(i).getRelativeEndMilePost();
			if (i < size - 1)
				speedLimitEndMilePostsString += ",";
		}

		return speedLimitEndMilePostsString;
	}

	
}
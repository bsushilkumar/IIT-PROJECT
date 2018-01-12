package gui.entities.sectionEntities.trackEntities;

import globalVariables.GlobalVar;
import globalVariables.GlobalVariables;
import gui.entities.sectionEntities.trackProperties.TinyBlockList;
import gui.entities.trainEntities.Train;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeSet;

import simulator.input.SimulationInstance;
import simulator.scheduler.StationToStationScheduler;
import simulator.util.Debug;

public class Link extends TrackSegment {

	private Block previousBlock;
	/**
	 * nextBlockNo
	 */
	public int nextBlockNo;
	/**
	 * nextBlock
	 */
	private Block nextBlock = null;
	/**
	 * priority
	 */
	public int priority = 1;

	private double previousBlockRelativeMilePost;
	private double nextBlockRelativeMilePost;
	/**
	 * crossovers
	 */
	private ArrayList<Block> crossovers = new ArrayList<Block>();
	public TinyBlockList tinyBlockArray = new TinyBlockList();

	private int direction = GlobalVar.COMMON_DIRECTION;
	private int crossover = -1;
	

	public ArrayList<Integer> crossoverIds = new ArrayList<Integer>();

	public Link() {
		init();
		previousBlock = null;
		nextBlock = null;
	}

	public Link(Block _previousBlock, Block _nextBlock) {
		init();
		previousBlock = _previousBlock;
		nextBlock = _nextBlock;
	}

	public void setCrossover(int crossover) {
		this.crossover = crossover;
	}

	public int getCrossover() {
		return crossover;
	}

	public String getCrossoverString() {
		if (crossover == -1)
			return "#";
		else
			return String.valueOf(crossover);
	}

	/**
	 * @param no
	 * @param length
	 */
	public Link(int no, double length) {
		init();
		setNextBlockNo(no);
		this.setLength(length);
	}

	/**
	 * @param no
	 * @param length2
	 * @param length
	 * @param priority
	 * @param block
	 */
	public Link(int nextBlockNo, double relativePreviousBlockMilePost,
			double relativeNextBlockMilePost, double length, int priority,
			double maximumSpeed, Block previousBlock) {
		init();
		this.setPriority(priority);
		setNextBlockNo(nextBlockNo);
		this.setPreviousBlockRelativeMilePost(relativePreviousBlockMilePost);
		this.setNextBlockRelativeMilePost(relativeNextBlockMilePost);
		this.setLength(length);
		this.setMaximumPossibleSpeed(maximumSpeed);
		this.setPreviousBlock(previousBlock);
	}

	/**
	 * @param blk
	 * @param length
	 * @param priority
	 */
	public Link(Block blk, double length, int priority) {
		init();
		this.setPriority(priority);
		setNextBlock(blk);
		this.setLength(length);
	}

	/**
	 * @param blk
	 * @param length
	 */
	public Link(Block blk, double length) {
		init();
		setNextBlock(blk);
		this.setLength(length);
	}

	public void init() {
		this.setEntityType(LINK_ENTITY);
	}

	/**
	 * This will check if all the crossovers are free during the specified
	 * interval. It will return -1 if all crossovers are free or else it will
	 * return the earliest arrival time so that the train can pass over this
	 * link
	 * 
	 * @param startTime
	 * @param interBlockTime
	 * @return time when link is free.
	 */
	public double whenFree(double startTime, double endTime) {
		int no;
		Debug.fine("whenFree: Crossovers size " + getCrossovers().size());
		for (int i = 0; i < getCrossovers().size(); i++) {
			Block crossoverBlock = getCrossovers().get(i);
			if ((no = crossoverBlock.isFree(startTime, endTime)) != -1) {
				Debug.fine("whenFree: " + crossoverBlock.getBlockName()
						+ " id " + crossoverBlock.getBlockNo());
				return getCrossovers().get(i).getOccupy().get(no).getEndTime();
			}
		}

		return -1;
	}

	/**
	 * @param trainNo
	 * @param startTime
	 * @param interBlockTime
	 */
	public void reserve(Train train, double startTime, double interBlockTime) {

		// nextBlock.setOccupied(trainNo, startTime, startTime +
		// interBlockTime);
		// interBlockTime=0.7;
		for (int i = 0; i < getCrossovers().size(); i++) {
			Block block = getCrossovers().get(i);
			block.setOccupied(train, startTime, startTime + interBlockTime,
					true, block.blockName);
			// System.out.println("crossovers reserve "+getCrossovers().get(i).getDirection()+" "+startTime+" "+interBlockTime);
			if (GlobalVariables.stationToStationScheduling) {
				Stack<StationToStationScheduler> stationToStationSchedulerStack = train.stationToStationSchedulerStack;
				if (stationToStationSchedulerStack.size() > 0) {
					StationToStationScheduler stationToStationScheduler = stationToStationSchedulerStack
							.peek();
					TreeSet<Integer> blockNumberSet = stationToStationScheduler
							.getBlockNumberSet();
					blockNumberSet.add(new Integer(block.getBlockNo()));
				}
			}
		}

	}

	/**
	 * @param linkDirection
	 * 
	 */
	public void convert(int linkDirection, SimulationInstance simulationInstance) {

//		Debug.fine("Link: convert: new link to block " + getNextBlockNo());

		Block nextHashBlock = simulationInstance
				.getBlockFromBlockNo(getNextBlockNo());

		if (nextHashBlock != null) {
			setNextBlock(nextHashBlock);
		} else {
			Debug.error("Link: convert: hashBlockLinkEntry is null");
			Debug.error("Link: convert: Error: block not present " + getNextBlockNo());
		}

		// System.out.println("reached here");
		for (int i = 0; i < getCrossovers().size(); i++) {

			Integer crossoverId = (getCrossovers().get(i).getBlockNo());
			Block block = simulationInstance.getBlockFromBlockNo(crossoverId);

			if (block != null) {
				getCrossovers().set(i, block);
				Debug.print("Link: convert: added " + crossoverId.toString());

			} else {
				Debug.print("Link: convert: Error: block not present "	+ getCrossovers().get(i));
			}
		}
	}

	/**
	 * @return true if the priority of the link is one
	 */
	public boolean isPriorityOneLink() {
		return (this.getPriority() == 1);
	}

	public ArrayList<Block> getCrossovers() {
		return crossovers;
	}

	public void setCrossovers(ArrayList<Block> crossovers) {
		this.crossovers = crossovers;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = GlobalVariables.roundToThreeDecimals(length);
	}

	public Block getNextBlock() {
		return nextBlock;
	}

	public void setNextBlock(Block nextBlock) {
		this.nextBlock = nextBlock;
	}

	public int getNextBlockNo() {
		return nextBlockNo;
	}

	public void setNextBlockNo(int nextBlockNo) {
		this.nextBlockNo = nextBlockNo;
	}

	public int getPreviousBlockNo() {
		return previousBlock.getBlockNo();
	}

	public Block getPreviousBlock() {
		return previousBlock;
	}

	public void setPreviousBlock(Block previousBlock) {
		this.previousBlock = previousBlock;
	}

	public double getPreviousBlockRelativeMilePost() {
		return previousBlockRelativeMilePost;
	}

	public void setPreviousBlockRelativeMilePost(
			double previousBlockRelativeMilePost) {
		this.previousBlockRelativeMilePost = GlobalVariables
				.roundToThreeDecimals(previousBlockRelativeMilePost);
	}

	public double getNextBlockRelativeMilePost() {
		return nextBlockRelativeMilePost;
	}

	public void setNextBlockRelativeMilePost(double nextBlockRelativeMilePost) {
		this.nextBlockRelativeMilePost = GlobalVariables
				.roundToThreeDecimals(nextBlockRelativeMilePost);
	}

	public double getStartMilePost() {
		// double previousBlockMilePost =
		return GlobalVariables.roundToThreeDecimals(startMilePost);
	}

	public double getEndMilePost() {
		return GlobalVariables.roundToThreeDecimals(endMilePost);
	}

	@Override
	public boolean hasError() {
		// TODO Auto-generated method stub
		return false;
	}

}

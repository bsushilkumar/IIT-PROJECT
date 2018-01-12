package simulator.scheduler;

import java.util.ArrayList;

import globalVariables.GlobalVar;
import globalVariables.GlobalVariables;
import gui.diagramEntities.trainDiagrams.VelocityProfile;
import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Link;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntities.trackProperties.TinyBlock;
import gui.entities.sectionEntities.trackProperties.TinyBlockList;
import gui.entities.sectionEntityList.LinkList;
import gui.entities.trainEntities.Train;
import input.SectionInputDialog;
import simulator.input.SimulationInstance;
import simulator.outputFeatures.GraphPanel;
import simulator.util.Debug;
import simulator.velocityProfileCalculation.TrainKinetics;
import simulator.velocityProfileCalculation.VelocityProfileArray;

/**
 * BlockScheduler.
 */
public class BlockScheduler {

	/**
	 * currentTrain
	 */

	protected Train currentTrain;
	/**
	 * currentBlock
	 */
	protected Block currentBlock;
	/**
	 * flag
	 */
	boolean flag;
	public SimulationInstance simulationInstance;

	/**
	 * blockSchedulingParameters
	 */
	// private BlockSchedulingParameters blockSchedulingParameters;

	/**
	 * @param block
	 * @param Train
	 */
	public BlockScheduler(Block block, Train Train,
			SimulationInstance simulationInstance) {
		flag = false;
		currentTrain = Train;
		currentBlock = block;
		this.simulationInstance = simulationInstance;
		// blockSchedulingParameters = null;
	}

	/**
	 * Determine if currenTrain has reached its destination station
	 * 
	 * @return true if the currentTrain has reached its destination station.
	 */
	public boolean hasReachedDestinationStation() {
		// System.out.println("hasReachedDestinationStation: "+currentBlock.getClass().getName());
		if (currentBlock.isLoop()) {
			Loop loop = (Loop) currentBlock;
			String currentStation = loop.getStationName();
			/**System.out.println("hasReachedDestinationStation: "
			 + currentStation + " endStation "
			 + currentTrain.getEndStation());**/
			if (currentStation.equalsIgnoreCase(currentTrain.getEndStation()))
				return true;
		}
		return false;
	}

	/**
	 * @return true if there is no next block to be considered. If the
	 *         simulation is over or current block has no next block return
	 *         true.
	 */
	public boolean hasNoNextBlockToTraverse() {
		return (currentBlock.getNextBlock(currentTrain) == null);
	}

	/**
	 * @param arrivalTime
	 * @return true if currentBlock has red signal at the arrival time
	 */
	public boolean sawRedSignal(double arrivalTime) {
		boolean consideringHalts = false;
		int nAspects = currentBlock.getBlockSignal(currentTrain.getDirection())
				.getNumberOfAspects();
		return currentBlock.getSignal(currentTrain, arrivalTime,
				consideringHalts, nAspects, simulationInstance) == GlobalVar.RED_SIGNAL;
	}

	/**
	 * Handle the cases in which departure time is greater than the arrival time
	 * 
	 * @param departureTime
	 * @param arrivalTime
	 * @param startVelocity
	 * @param previousLink
	 * @param nextFastestLink2
	 * @param nextReferenceLoopNumber
	 * @return null in exceptional cases.
	 */
	public StatusTraverseBlock handleDepTimeGreaterThanArrTime(
			double departureTime, double arrivalTime, double startVelocity,
			Link previousLink, Link nextFastestLink2,
			double profileStartingMilePost, int nextReferenceLoopNumber) {

		Debug.info("handleDepTimeGreaterThanArrTime: currentBlock "
				+ currentBlock.getBlockNo() + " currentTrain "
				+ currentTrain.getTrainNo());
		Debug.info("handleDepTimeGreaterThanArrTime: arrivalTime "
				+ arrivalTime + " departureTime " + departureTime);

		if (currentBlock.waitingPermitted()) {
			Debug.print("BlockScheduler: statusTraverseBlock: waiting is permitted");

			// The train has to halt in this block so no overlap runtime
			// calculation
			boolean enteredOnYellow = true;
			RunTimeReturn runtimereturn = currentBlock
					.getNonOverlappingRunTimeSignal(currentTrain, 1,
							startVelocity, 0.0D, previousLink,
							nextFastestLink2, profileStartingMilePost,
							enteredOnYellow);

			if (-1D == runtimereturn.totalTime) {
				Debug.error("velocityProfileArray of runTimeReturn has size 0");
				return new StatusTraverseBlock(false, departureTime,
						currentTrain.getTrainNo());
			} else {
				LinkList linkList = currentBlock.getNextLinks(currentTrain
						.getDirection());

				boolean linkPriorityIsNotOne = !(linkList.getNextMainLink() == nextFastestLink2);

				StatusTraverseBlock statusTraversePath = traversePath(
						previousLink, nextFastestLink2, arrivalTime,
						departureTime, startVelocity, linkPriorityIsNotOne,
						simulationInstance.numberOfSignalAspects,
						profileStartingMilePost, nextReferenceLoopNumber);

				if (statusTraversePath.status) {
					Debug.fine("handleDepTimeGreaterThanArrTime: returning successfully");
					return new StatusTraverseBlock(true, arrivalTime,
							currentTrain.getTrainNo());
				}
			}
		} else {
			Debug.fine("waiting is not permitted case of deptime<arrivaltime ");
			return new StatusTraverseBlock(false, departureTime,
					currentTrain.getTrainNo());
		}

		return null;
	}

	/**
	 * Determine the time when a scheduled train should leave a station of halt.
	 * It follows basic rules like a train should not leave a scheduled halt
	 * before its departure time. Also, if a train arrives at a station late
	 * then it should wait at the station for the pre-specified duration of
	 * waiting time.
	 * 
	 * @param arrivalTime
	 * 
	 * @return departure time for a scheduled train at a halt. returns 0 if the
	 *         train is not scheduled or the currentBlock is not a loop.
	 */
	public double calDepTimeForSchedTrainHalt(double arrivalTime) {
		double calDepTime = -1000D;

		if (currentTrain.isScheduled() && currentBlock.isLoop()) {
			String stationName = ((Loop) currentBlock).getStation()
					.getStationName();
			ReferenceTableEntry refTabEntry = currentTrain
					.getRefTabEntryFromStationName(stationName);

			if (refTabEntry == null)
				return calDepTime;

			double refArrTime = refTabEntry.getReferenceArrivalTime();
			double refDepTime = refTabEntry.getReferenceDepartureTime();
			//System.out.println("calDepTimeForSchedTrainHalt: refArrTime "
			//+ refArrTime + " refDepTime " + refDepTime
			//+ " arrivalTime " + arrivalTime);

			if (refArrTime == refDepTime) {
				// reference arrival time is same as the reference departure
				// time
				calDepTime = arrivalTime;
			} else if (refArrTime > refDepTime) {
				// arrival time is greater than the departure time
			} else if (arrivalTime >= refArrTime) {
				calDepTime = arrivalTime + (refDepTime - refArrTime);
			} else {// (arrivalTime < refArrTime)
				calDepTime = refDepTime;
			}
		}else if (!currentTrain.isScheduled() && currentBlock.isLoop()) {
			String stationName = ((Loop) currentBlock).getStation()
					.getStationName();
			ReferenceTableEntry refTabEntry = currentTrain
					.getRefTabEntryFromStationName(stationName);
			if (refTabEntry == null)
				return calDepTime;
			double refMinHaltTime =  refTabEntry.getMinHaltTime();
			calDepTime = arrivalTime + refMinHaltTime;			
		}

		return calDepTime;
	}

	/**
	 * BlockScheduler: StatusTraverseBlock traverseBlock(arrivalTime, deptTime,
	 * startVelocity) 1) if there is no next block it traverses the last block:
	 * traverseLastBlock(arrivalTime, startVelocity) 2) if got a redSignal it
	 * handles that with : handleRedSignal(arrivalTime); Then it calls the
	 * getSortedLinks. 3) if the train is not supposed to wait, then it just
	 * zooms past the block. 4) Otherwise it waits for prescheduled waiting time
	 * obtained using ReferenceTimeTable. 5) Then if the scheduled departure
	 * time exceeds the 24hrs limit it returns a StatusTraverseBlock which says
	 * that the limit is reached. 6) if the link priority selected is 1 then it
	 * marks its flag as true. 7) then it goes to traversePath(link,
	 * arrivalTime, tempDepartureTime, startVelocity, flag2,
	 * GlobalVar.numberOfSignalAspects);
	 * 
	 * 
	 * @param arrivalTime
	 * @param departureTime
	 * @param startVelocity
	 * @param nextFastestLink2
	 * @param nextReferenceLoopNumber
	 * @param j
	 * @param link2
	 * @return {@link StatusTraverseBlock}
	 */
	public StatusTraverseBlock traverseBlock(double arrivalTime,
			double departureTime, double startVelocity, Link previousLink,
			Link nextFastestLink2, double profileStartingMilePost,
			int nextReferenceLoopNumber) {
		{
			Debug.info("");
			Debug.info("temp is " + simulationInstance.temp + " "
					+ currentBlock.getBlockName() + " start "
					+ currentBlock.getStartMilePost() + " end "
					+ currentBlock.getEndMilePost() + " train "
					+ currentTrain.getTrainNo() + " arrival time "
					+ arrivalTime + " departure time " + departureTime
					+ " startVelocity " + startVelocity
					+ " trainNextReferenceLoopNo " + nextReferenceLoopNumber);
			simulationInstance.temp++;

		}
		/**System.out.println("BlockScheduler.traverseBlock: Train :"+ currentTrain.getTrainNo()+
				" Block: " + currentBlock.blockNo + " Arrival Time: "+arrivalTime
				+ " trainNextReferenceLoopNo " + nextReferenceLoopNumber);**/

		GraphPanel graphPanel = simulationInstance.freightSimulator.graphPanel;
		if (graphPanel != null) {
			graphPanel.trainNoLabel.setText(String.valueOf(currentTrain
					.getTrainNo()));
			graphPanel.blockNoLabel.setText(String.valueOf(currentBlock
					.getBlockNo()));
		}
	
			if (simulationInstance.temp > 10000000) {
				Debug.error("traverseBlock: Infinite loop: exiting");
				System.exit(0);
			}
		

		// double tempDepartureTime = departureTime;

		double minArrTime = Double.MAX_VALUE;// 1.7976931348623157E+308D;
		double minDepTime = Double.MAX_VALUE;// 1.7976931348623157E+308D;

		// if the loop belongs to the station at which the train has to halt,
		// then traverse the last block.
		// SignalDirection sigDir = new SignalDirection(currentTrain,
		// currentBlock, previousLink, currentBlock.getSignal(
		// currentTrain, simulationInstance.numberOfSignalAspects,
		// arrivalTime, simulationInstance), arrivalTime);
		// simulationInstance.signalDirectionList.add(sigDir);

		// if there is red signal, handleRedSignal.
		if (sawRedSignal(arrivalTime)) {
			Debug.info("Train " + currentTrain.getTrainNo()
					+ " saw red signal at block " + currentBlock.getBlockNo()
					+ " at arrivalTime " + arrivalTime);
			return handleRedSignal(arrivalTime);
		}

		if (hasReachedDestinationStation()) {
			Debug.info("BlockScheduler: Reached the endStation");
			StatusTraverseBlock statusTraverseBlock = traverseLastBlock(
					arrivalTime, startVelocity, previousLink,
					profileStartingMilePost);

			return statusTraverseBlock;
		} else if (hasNoNextBlockToTraverse()) {

			// if there is no next block traverse the last block again.
			Debug.info("BlockScheduler: traverseBlock: nextBlock is null");
			return traverseLastBlock(arrivalTime, startVelocity, previousLink,
					profileStartingMilePost);
		} else if (GlobalVariables.stationToStationScheduling
				&& currentBlock.getBlockNo() == nextReferenceLoopNumber) {
			Debug.info("BlockScheduler: traverseBlock: reached a loop "
					+ currentBlock.getBlockName());
			return traverseLastBlock(arrivalTime, startVelocity, previousLink,
					profileStartingMilePost);
		}

		StatusTraverseBlock statusTraverseBlock = null;
		if (departureTime > arrivalTime) {
			statusTraverseBlock = handleDepTimeGreaterThanArrTime(
					departureTime, arrivalTime, startVelocity, previousLink,
					nextFastestLink2, profileStartingMilePost,
					nextReferenceLoopNumber);

		}

		if (statusTraverseBlock != null)
			return statusTraverseBlock;

		LinkList linkList = currentBlock.getNextLinks(
				currentTrain.getDirection()).getSortedLinks(currentTrain,
				nextReferenceLoopNumber);

		if (linkList == null || linkList.size() == 0) {
			// no links to be considered. Hence return false status. with limit
			// status true
			Debug.warning("linkList is null or has no links. Hence limit reached");
			return new StatusTraverseBlock(false, true,
					currentTrain.getTrainNo());
		}

		int nMinutesInDay = 24 * 60;
		double trainStartTime = currentTrain.getStartTime();

		if (departureTime - trainStartTime > nMinutesInDay
				* simulationInstance.simulationTime
				&& currentTrain.getStartLoopNo() == currentBlock.getBlockNo()) {

			// System.out.println("Limit Reached");
			return new StatusTraverseBlock(false, true,
					currentTrain.getTrainNo());
		}

		// flag1 false implies that on some particular path, on some particular
		// block the train had to stop or there was signal failure
		boolean flag1 = true;
		Link nextFastestLink = null;
		for (int i = 0; i < linkList.size(); i++) {
			Link tempLink = linkList.get(i);
			// System.out.println(tempLink.getNextBlockNo() + " test "
			// + linkList.size());
			boolean linkPriorityIsNotOne = !tempLink.isPriorityOneLink();

			Debug.info("Calling traversePath: arrivalTime " + arrivalTime
					+ " depTime " + departureTime + " startVelocity "
					+ startVelocity);
			StatusTraverseBlock statusTraversePath = traversePath(previousLink,
					tempLink, arrivalTime, departureTime, startVelocity,
					linkPriorityIsNotOne,
					simulationInstance.numberOfSignalAspects,
					profileStartingMilePost, nextReferenceLoopNumber);

			Debug.info("traverseBlock: returned from traversePath, currentBlock "
					+ currentBlock.getBlockName()
					+ " currentTrain "
					+ currentTrain.getTrainNo());
			
			
			// successfully got a path
			if (statusTraversePath.status) {
				return new StatusTraverseBlock(true, arrivalTime,
						currentTrain.getTrainNo());
			}

			// signal value of status traverse path ranges is -2,-1 or 0
			if (((StatusTraversePath) statusTraversePath).signal == -2) {
				Debug.print("IN SIGNAL FAILED MODE");
				return new StatusTraverseBlock(false,
						currentTrain.getStartTime(), currentTrain.getTrainNo());
			}

			if (((StatusTraversePath) statusTraversePath).signal == -1) {
				flag1 = false;
			}


			if (minDepTime > statusTraversePath.departureTime) {
				minDepTime = statusTraversePath.departureTime;
				minArrTime = ((StatusTraversePath) statusTraversePath).arrivalTime;
				nextFastestLink = tempLink;
			}
			/**System.out.println("traversePath.CurrentBlock: "+ currentBlock.getBlockName() +
					" currentTrain "+ currentTrain.getTrainNo() + " Next Block = " +
					tempLink.nextBlockNo +" nextFastestLink: "+ nextFastestLink.nextBlockNo + 
					" minDepTime: " + minDepTime + " nextReferenceLoopNumber: "+
					nextReferenceLoopNumber);**/
		} // end of for loop consisting of linkList

		if (currentBlock.waitingPermitted() && !flag1) {
			Debug.print("waiting is permitted so trying to wait");
			if (simulationInstance.hasBlockDirectionFile) {
				// return currentBlock.traverseBlock(currentTrain,
				// arrivalTime + 1, minDepTime, startVelocity,
				// previousLink, nextFastestLink, profileStartingMilePost,
				// simulationInstance, nextReferenceLoopNumber);
				return currentBlock.traverseBlock(currentTrain, arrivalTime
						+ GlobalVar.MINIMUM_WAIT_TIME_FOR_RETRY, minDepTime,
						startVelocity, previousLink, nextFastestLink,
						profileStartingMilePost, simulationInstance,
						nextReferenceLoopNumber);
			} else {
				return currentBlock.traverseBlock(currentTrain, arrivalTime,
						minDepTime, startVelocity, previousLink,
						nextFastestLink, profileStartingMilePost,
						simulationInstance, nextReferenceLoopNumber);
				// return currentBlock.traverseBlock(currentTrain, minArrTime,
				// minDepTime, startVelocity, previousLink,
				// nextFastestLink, profileStartingMilePost,
				// simulationInstance, nextReferenceLoopNumber);
			}
		}

		return new StatusTraverseBlock(false, minArrTime,
				currentTrain.getTrainNo());
		// return new StatusTraversePath(false, minArrTime,minDepTime,
		// currentTrain.getTrainNo());
	}

	/**
	 * @param arrivalTime
	 * @param startVelocity
	 * @param Train
	 * @param previousLink
	 * @param profileStartingMilePost
	 * @return {@link StatusTraverseBlock}
	 */
	public StatusTraverseBlock traverseLastBlock(double arrivalTime,
			double startVelocity, Link previousLink,
			double profileStartingMilePost) {

		// //
		Debug.fine("traverseLastBlock: startVelocity " + startVelocity
				+ " train " + currentTrain.getTrainNo() + " block "
				+ currentBlock.getBlockNo() + " startVelocity " + startVelocity);

		// last block of the train. So no overlap calculation.
		int nFreeBlocksToBeTraversed = 1;
		double endVelocity = 0;
		Link nextLink = null; // last link does not exist
		boolean enteredOnYellow = determineEnteredOnYellow(currentBlock,
				currentTrain, arrivalTime);
		RunTimeReturn runTimeReturn = currentBlock
				.getNonOverlappingRunTimeSignal(currentTrain,
						nFreeBlocksToBeTraversed, startVelocity, endVelocity,
						previousLink, nextLink, profileStartingMilePost,
						enteredOnYellow);

		// VelocityProfileArray velocityprofilearray = new VelocityProfileArray(
		// currentTrain, currentBlock, startVelocity, 0.0D);
		VelocityProfileArray velocityprofilearray = runTimeReturn
				.getVelocityProfileArray();
		VelocityProfileArray blockVelocityProfileArray = runTimeReturn
				.getBlockVelocityProfileArray();
		VelocityProfileArray previousLinkVelocityProfileArray = runTimeReturn
				.getPreviousLinkVelocityProfileArray();

		Debug.print((new StringBuilder()).append(" Velocuit oia ")
				.append(velocityprofilearray.getRunTime()).toString());

		double departureTime = arrivalTime + velocityprofilearray.getRunTime();
		double calDepTime = calDepTimeForSchedTrainHalt(departureTime);

		Debug.fine("traverseLastBlock: calDepTime " + calDepTime
				+ " departureTime " + departureTime);

		if (velocityprofilearray == null || velocityprofilearray.size() == 0) {
			System.out
					.println("traverseLastBlock: velocityProfileArray is null");
		}

		departureTime = Math.max(departureTime, calDepTime);

		Debug.fine("traverseLastBlock: blockNo " + currentBlock.getBlockNo()
				+ " arr " + arrivalTime + " dep " + departureTime);

		// check if the last block is free for entire duration
		double earliestFreeTime = currentBlock.whenFree(arrivalTime,
				departureTime);
		//System.out.println("Earliest Free Time = " + earliestFreeTime+" for arrivalTime =  "+ arrivalTime + 
			//" and departure time = " + departureTime );
		if (earliestFreeTime != -1D) {
			earliestFreeTime += simulationInstance.minTime;
			//earliestFreeTime += 1;
			Debug.warning("block reservation failure. So returning false with earliestFreeTime "
					+ earliestFreeTime);

			return new StatusTraversePath(false, earliestFreeTime,
					earliestFreeTime, 0);
		}

		// int trainDirection = currentTrain.getDirection();
		// currentTrain.getTimeTables().add(
		// 0,
		// new SimulatorTimeTableEntry(currentBlock.getBlockNo(),
		// arrivalTime, departureTime, velocityprofilearray
		// .getStartMilePost(trainDirection),
		// velocityprofilearray.getEndMilePost(trainDirection),
		// velocityprofilearray, 6));

		// boolean actualOccupancy = true;
		// currentBlock.reserve(currentTrain.getTrainNo(), arrivalTime,
		// departureTime, actualOccupancy);
		double totalTimeTillEnd = 0;
		reserveBlocks(null, previousLink, nFreeBlocksToBeTraversed,
				arrivalTime, departureTime, totalTimeTillEnd,
				blockVelocityProfileArray, previousLinkVelocityProfileArray, 1);

		if (currentBlock.isLoop()) {

			// Loop loop = (Loop) currentBlock;
			// if (!(loop.getBlockNo() ==
			// currentTrain.getDestinationLoopNumber())) {
			// double lockTime = GlobalVariables.MAXIMUM_LOCK_TIME;
			// loop.lock(lockTime);
			// }

			// int newStartLoopNo = currentBlock.getBlockNo();
			// int newNextReferenceLoopNumber = loop
			// .getNextReferenceLoopNumber(currentTrain.getRefTables());

			// currentTrain.setStartLoopNo(newStartLoopNo);
			currentTrain.setDepartTime(departureTime);
			// currentTrain.setNextReferenceLoopNumber(newNextReferenceLoopNumber);

			// StationToStationScheduler stationToStationScheduler = new
			// StationToStationScheduler(
			// currentTrain, newStartLoopNo, newNextReferenceLoopNumber,
			// departureTime);
			// currentTrain.stationToStationSchedulerStack
			// .push(stationToStationScheduler);
		}

		// return new StatusTraversePath(true, arrivalTime,
		// arrivalTime, currentTrain.getTrainNo());
		return new StatusTraverseBlock(true, arrivalTime,
				currentTrain.getTrainNo());
	}

	public boolean determineEnteredOnYellow(Block block, Train train,
			double arrivalTime) {
		Link nextLink = block.getNextLink(train);

		int numberOfSignalAspects = simulationInstance.numberOfSignalAspects;
		boolean enteredOnYellow;
		if (nextLink != null) {
			if (!nextLink.isPriorityOneLink()) {
				enteredOnYellow = true;
			} else {
				Block nextBlock = nextLink.getNextBlock();
				// int nextBlockPreviousSignal = nextBlock.getSignal(train,
				// numberOfSignalAspects, arrivalTime, simulationInstance);
				// enteredOnYellow = nextBlockPreviousSignal ==
				// GlobalVar.RED_SIGNAL;
				boolean consideringHalts = false;
				int numberOfAspects = block
						.getBlockSignal(train.getDirection())
						.getNumberOfAspects();
				int blockSignal = block.getSignal(train, arrivalTime,
						consideringHalts, numberOfAspects, simulationInstance);
				enteredOnYellow = blockSignal == GlobalVar.YELLOW_SIGNAL;
			}
		} else {
			enteredOnYellow = false;
		}

		Debug.fine("determineEnteredOnYellow: enteredOnYellow "
				+ enteredOnYellow);
		return enteredOnYellow;
	}

	/**
	 * @param arrivalTime
	 * @return {@link StatusTraverseBlock} with a false status and arrival and
	 *         departureTimes to be the earliestArrivalTime possible for the
	 *         currentBlock
	 */
	public StatusTraverseBlock handleRedSignal(double arrivalTime) {
		Debug.print("In BLOCKSCHEDULER.HANDLEREDSIGNAL");
		Debug.print("Red Signal - backtrack");
		
		/**System.out.println("Blockscheduler.handleRedSignal: Current Block: "+ currentBlock.blockNo
				+" Current Train: "+currentTrain.getTrainNo() + " Arrival Time: "+ arrivalTime);**/
		
		/**SectionInputDialog sectioninputdialog = GlobalVar
				.getSectionInputDialog();
		ArrayList<Block> blocklist = sectioninputdialog.blockList;
		for  ( int i =0 ;i < blocklist.size(); i ++ )
		{
			//if (blocklist.get(i).blockNo == 590020){
				System.out.println("Block Occupancy of 590010: " + blocklist.get(i).getBlockNo());
			//}
		}**/
		
		double earliestArrivalTime = currentBlock.getEarliestArrivalTime(
				arrivalTime, currentTrain.getDirection())
				//+1;
				+ simulationInstance.minTime;

		Debug.info("handleRedSignal: currentBlock " + currentBlock.getBlockNo()
				+ " Earliest arrival Time " + earliestArrivalTime);
		return new StatusTraverseBlock(false, earliestArrivalTime,
				currentTrain.getTrainNo());
	}

	/**
	 * If there is a need to consider the warner distance, get the new
	 * {@link RunTimeReturn} for the modified block considering the warner
	 * distance
	 * 
	 * @param link
	 * @param signal
	 * @param arrivalTime
	 * @param linkPriorityIsNotOne
	 * @param runTimeReturn
	 * @return {@link RunTimeReturn} if the warner distance is considered. Else
	 *         returns null.
	 */
	public RunTimeReturn getRunTimeReturnConsideringWarner(Link link,
			int signal, double arrivalTime, boolean linkPriorityIsNotOne,
			RunTimeReturn runTimeReturn, final double warnerDistance,
			int maxSignalAspectsToBeConsidered) {
		RunTimeReturn newRunTimeReturn = null;
		if (simulationInstance.simulationType.equalsIgnoreCase("SignalFailure")) {
			if (link.getNextBlock().getSignalFailFlag() == 0
					&& currentBlock.getSignalFailFlag() == 0
					&& linkPriorityIsNotOne) {
				Debug.print(" From Blocksched.traversepath -->block.calculateWarnerVelocityProfile");
				newRunTimeReturn = currentBlock.calculateWarnerVelocityProfile(
						currentTrain, signal, arrivalTime, link.getNextBlock(),
						runTimeReturn, 0.0D, warnerDistance,
						simulationInstance, maxSignalAspectsToBeConsidered);
			}
		} else if (linkPriorityIsNotOne) {
			Debug.print(" From Blocksched.traversepath -->block.calculateWarnerVelocityProfile");
			// System.out.println("link priority is not one " +
			// link.nextBlockNo);
			newRunTimeReturn = currentBlock.calculateWarnerVelocityProfile(
					currentTrain, signal, arrivalTime, link.getNextBlock(),
					runTimeReturn, 0.0D, warnerDistance, simulationInstance,
					maxSignalAspectsToBeConsidered);
		}

		return newRunTimeReturn;
	}

	/**
	 * Get the final velocity for the scheduling of the train. If the train can
	 * go with top priority link it may go at the fullest speed allowed.
	 * 
	 * @param link
	 * @return finalVelocity of the profile of the train to be calculated.
	 */
	public double calculateFinalVelocity(Link link, int signal) {
		if (signal < simulationInstance.numberOfSignalAspects)
			return 0.0D;

		if (link.isPriorityOneLink())
			return link.getNextBlock().getMaximumPossibleSpeed();
		else
			return 0.0D;
	}

	private class BlockSchedulingParameters {
		Link link;
		double arrivalTime;
		double departureTime;
		int nFreeBlocksToBeTraversed;
		RunTimeReturn runTimeReturn;
		double totalTimeTillEnd;
		int signal;
		double finalVelocity;
		double tempDepartureTime;
	}

	public BlockSchedulingParameters determineBlockSchedulingParameters(
			Link previousLink, Link nextLink, double arrivalTime,
			double startVelocity, boolean linkPriorityIsNotOne,
			double departureTime, double profileStartingMilePost,
			int maxSignalAspectsToBeConsidered) {
		// System.out.println("determineBSP: blockNo " +
		// currentBlock.getBlockNo()
		// + " start " + currentBlock.getStartMilePost() + " end "
		// + currentBlock.getEndMilePost());
		BlockSchedulingParameters blockSchedulingParameters = new BlockSchedulingParameters();

		blockSchedulingParameters.arrivalTime = arrivalTime;

		double tempDepartureTime = -3D;// = departureTime;

		boolean consideringHalts = true;
		int signal = currentBlock.getSignal(currentTrain, arrivalTime,
				consideringHalts, maxSignalAspectsToBeConsidered,
				simulationInstance);

		boolean hasScheduledHalt = false;
		if (currentBlock.isLoop()) {
			Loop currentLoop = (Loop) currentBlock;
			hasScheduledHalt = currentTrain.hasScheduledHalt(currentLoop,
					simulationInstance);
		}

		// if (nextLink != null && !nextLink.isPriorityOneLink()) {
		// signal = 1;
		// }

		Debug.info("determineBlockSchedulingParameters: Printing signal -- "
				+ signal);

		double finalVelocity = calculateFinalVelocity(nextLink, signal);

		Debug.fine("determineBlockSchedulingParameters: temp "
				+ simulationInstance.temp + " signal " + signal + " "
				+ currentBlock.getBlockName() + " finalVelocity "
				+ finalVelocity);

		boolean enteredOnYellow = determineEnteredOnYellow(currentBlock,
				currentTrain, arrivalTime);

		// Calculating runtime for a block and not the overlap
		RunTimeReturn runTimeReturn = currentBlock
				.getNonOverlappingRunTimeSignal(currentTrain, signal,
						startVelocity, finalVelocity, previousLink, nextLink,
						profileStartingMilePost, enteredOnYellow);

		if (runTimeReturn.getVelocityProfileArray() == null) {
			Debug.error("determineBlockSchedulingParameters: runTimeReturn "
					+ "has velocityProfileArray null");
		}

		final double warnerDistance = simulationInstance.warnerDistance;
		if (warnerDistance > 0.1D) {
			RunTimeReturn considerWarnerRunTime = getRunTimeReturnConsideringWarner(
					nextLink, signal, arrivalTime, linkPriorityIsNotOne,
					runTimeReturn, warnerDistance,
					maxSignalAspectsToBeConsidered);
			if (considerWarnerRunTime != null)
				runTimeReturn = considerWarnerRunTime;
		}

		// sightingDistanceRunTimeReturn should happen in absence of warner
		// distance
		double sightingDistance = GlobalVar.sightingDistance;
		if (GlobalVar.considerSightingDistance) {
			if (!hasScheduledHalt) {
				RunTimeReturn consideringSightingRunTimeReturn = getRunTimeReturnConsideringSighting(
						previousLink, nextLink, signal, arrivalTime,
						linkPriorityIsNotOne, runTimeReturn, sightingDistance,
						maxSignalAspectsToBeConsidered);
				if (consideringSightingRunTimeReturn != null) {
					runTimeReturn = consideringSightingRunTimeReturn;
				}
			} else {
				Debug.fine("determineBlockSchedulingParameters: hasScheduledHalt "
						+ hasScheduledHalt);
			}
		}

		if (runTimeReturn.getVelocityProfileArray() == null) {
			Debug.error("determineBlockSchedulingParameters: runTimeReturn has velocityProfileArray null");
		}

		int nFreeBlocksToBeTraversed = 0;
		if (currentTrain.isScheduled() && currentBlock.isLoop()) {

			String stationName = ((Loop) currentBlock).getStation()
					.getStationName();

			ReferenceTableEntry referenceTableEntry = currentTrain
					.getRefTabEntryFromStationName(stationName);

			if (referenceTableEntry == null) {
				// the train is not supposed to halt at the station :
				// Devendra
				nFreeBlocksToBeTraversed = signal;
				tempDepartureTime = arrivalTime + runTimeReturn.totalTime
						+ simulationInstance.minTime;
			} else {
				double refArrivalTime = referenceTableEntry
						.getReferenceArrivalTime();
				double refDepartureTime = referenceTableEntry
						.getReferenceDepartureTime();

				if (refDepartureTime > refArrivalTime) {
					nFreeBlocksToBeTraversed = 1;
					double endVelocity = 0;
					enteredOnYellow = determineEnteredOnYellow(currentBlock,
							currentTrain, arrivalTime);
					runTimeReturn = currentBlock
							.getNonOverlappingRunTimeSignal(currentTrain,
									nFreeBlocksToBeTraversed, startVelocity,
									endVelocity, previousLink, nextLink,
									profileStartingMilePost, enteredOnYellow);
					tempDepartureTime = arrivalTime + runTimeReturn.totalTime;
					// System.out.println("hi 1 " + tempDepartureTime);
				} else {
					// the train is not supposed to halt at the station :
					// Devendra
					nFreeBlocksToBeTraversed = signal;
					tempDepartureTime = arrivalTime + runTimeReturn.totalTime
							+ simulationInstance.minTime;
					// System.out.println("hi 2 " + tempDepartureTime + " "
					// + runTimeReturn.totalTime + " " + GlobalVar.minTime
					// + " " + arrivalTime);
				}
			}
		}else if (!currentTrain.isScheduled() && currentBlock.isLoop()) {
			//Vidyadhar
			//Freight Train Halting
			String stationName = ((Loop) currentBlock).getStation()
					.getStationName();

			ReferenceTableEntry referenceTableEntry = currentTrain
					.getRefTabEntryFromStationName(stationName);

			if (referenceTableEntry == null) {
				// the train is not supposed to halt at the station :
				// Devendra
				nFreeBlocksToBeTraversed = signal;
				tempDepartureTime = arrivalTime + runTimeReturn.totalTime
						+ simulationInstance.minTime;
			} else {
				double refMinHaltTime = referenceTableEntry
						.getMinHaltTime();

				if (refMinHaltTime > 0) {
					nFreeBlocksToBeTraversed = 1;
					double endVelocity = 0;
					enteredOnYellow = determineEnteredOnYellow(currentBlock,
							currentTrain, arrivalTime);
					runTimeReturn = currentBlock
							.getNonOverlappingRunTimeSignal(currentTrain,
									nFreeBlocksToBeTraversed, startVelocity,
									endVelocity, previousLink, nextLink,
									profileStartingMilePost, enteredOnYellow);
					tempDepartureTime = arrivalTime + runTimeReturn.totalTime;
					// System.out.println("hi 1 " + tempDepartureTime);
				} else {
					// the train is not supposed to halt at the station :
					// Devendra
					nFreeBlocksToBeTraversed = signal;
					tempDepartureTime = arrivalTime + runTimeReturn.totalTime
							+ simulationInstance.minTime;
					// System.out.println("hi 2 " + tempDepartureTime + " "
					// + runTimeReturn.totalTime + " " + GlobalVar.minTime
					// + " " + arrivalTime);
				}
			}
			// yet another important backTracking step. If the train can reach
			// earlier than the time it is asked to wait,that means some other
			// previously scheduled train is on its way block this train's path.
			// Devendra
		} else if (departureTime > arrivalTime + runTimeReturn.totalTime) {
			Debug.info("determineBlockSchedulingParameters: tempDepartureTime > arrivalTime + runtimereturn.totalTime");
			Debug.info("tempDepartureTime " + departureTime + " arrivalTime "
					+ arrivalTime + " runTime " + runTimeReturn.totalTime
					+ " total " + (arrivalTime + runTimeReturn.totalTime));
			nFreeBlocksToBeTraversed = 1;
			double endVelocity = 0;
			enteredOnYellow = determineEnteredOnYellow(currentBlock,
					currentTrain, arrivalTime);
			runTimeReturn = currentBlock.getNonOverlappingRunTimeSignal(
					currentTrain, nFreeBlocksToBeTraversed, startVelocity,
					endVelocity, previousLink, nextLink,
					profileStartingMilePost, enteredOnYellow);
		} else {
			// System.out.println("I came here "+GlobalVar.temp+" times. "+GlobalVar.minTime);
			nFreeBlocksToBeTraversed = signal;
			tempDepartureTime = arrivalTime + runTimeReturn.totalTime;
			// + GlobalVar.minTime;

			Debug.info("determineBlockSchedulingParameters: tempDepartureTime is "
					+ tempDepartureTime);
		}

		tempDepartureTime = Math.max(tempDepartureTime, departureTime);

		blockSchedulingParameters.finalVelocity = finalVelocity;
		blockSchedulingParameters.signal = signal;
		blockSchedulingParameters.tempDepartureTime = tempDepartureTime;
		blockSchedulingParameters.runTimeReturn = runTimeReturn;
		blockSchedulingParameters.nFreeBlocksToBeTraversed = nFreeBlocksToBeTraversed;
		Debug.fine("determineBlockSchedulingParameters: tempDepartureTime "
				+ tempDepartureTime);
		return blockSchedulingParameters;
	}

	private RunTimeReturn getRunTimeReturnConsideringSighting(
			Link previousLink, Link nextLink, int signal, double arrivalTime,
			boolean linkPriorityIsNotOne, RunTimeReturn runTimeReturn,
			double sightingDistance, int maxSignalAspectsToBeConsidered) {
		Debug.info("getRunTimeReturnConsideringSighting: signal " + signal
				+ " arrivalTime " + arrivalTime + " linkPriorityIsNotOne "
				+ linkPriorityIsNotOne);

		if (nextLink == null) {
			return null;
		}

		int numberOfSignalAspects = simulationInstance.numberOfSignalAspects;
		Block nextBlock = nextLink.getNextBlock();
		double oldDepartureTime = arrivalTime + runTimeReturn.totalTime;

		boolean consideringHalts = false;
		int nextBlockPreviousSignal = nextBlock.getSignal(currentTrain,
				arrivalTime, consideringHalts, maxSignalAspectsToBeConsidered,
				simulationInstance);
		int nextBlockLatestSignal = nextBlock.getSignal(currentTrain,
				oldDepartureTime, consideringHalts,
				maxSignalAspectsToBeConsidered, simulationInstance);

		if (nextBlockLatestSignal > nextBlockPreviousSignal
				|| nextBlockLatestSignal == numberOfSignalAspects - 1) {
			Debug.fine("getRunTimeReturnConsideringSighting: nextBlockPreviousSignal "
					+ nextBlockPreviousSignal
					+ " nextBlockLatestSignal "
					+ nextBlockLatestSignal);

			// get the block which was freed during traversal of currentBlock
			Block nTHBlock = nextBlock;
			for (int blockIterNo = nextBlockPreviousSignal; blockIterNo > 0; blockIterNo--) {
				/**System.out.println("getRunTimeReturnConsideringSighting: nextBlockPreviousSignal"+
						nextBlockPreviousSignal + " blockIer: "+ blockIterNo 
						 + " nTHBlock = " + nTHBlock.blockNo);**/
				nTHBlock = nextBlock.getNextBlock(currentTrain);
				/**System.out.println("getRunTimeReturnConsideringSighting: nextBlockPreviousSignal"+
						nextBlockPreviousSignal + " blockIer: "+ blockIterNo 
						 + " nTHBlock = " + nTHBlock.blockNo);**/
			}

			Debug.fine("getRunTimeReturnConsideringSighting: nTHBlock "
					+ nTHBlock.getBlockName());

			// get the time when that block was freed
			int trainDirection = currentTrain.getDirection();
			
			/**System.out.println("BlockScheduler.getRunTimeReturnConsideringSighting: Current Block: "+ nTHBlock.blockNo
					 +" Arrival time: "+arrivalTime+ " Current Train: "+ currentTrain.getTrainNo());**/
			
			double nextSignalChangeTime = nTHBlock.getEarliestArrivalTime(
					arrivalTime, trainDirection);

			Debug.fine("getRunTimeReturnConsideringSighting: nextSignalChangeTime "
					+ nextSignalChangeTime);

			// get velocity profile corresponding to this time
			VelocityProfileArray velocityProfileArray = runTimeReturn
					.getVelocityProfileArray();
			VelocityProfileArray previousLinkVelocityProfileArray = runTimeReturn
					.getPreviousLinkVelocityProfileArray();

			double time = nextSignalChangeTime - arrivalTime;
			TrainKinetics trainKinetics = velocityProfileArray
					.getTrainKineticsByTime(time, trainDirection, currentBlock);

			if (trainKinetics == null) {
				return null;
			}

			// get milepost at this time
			double milepost = trainKinetics.getMilepost();

			// get velocity at this time
			double velocity = trainKinetics.getVelocity();

			Debug.fine("getRunTimeReturnConsideringSighting: milepost "
					+ milepost + " velocity " + velocity);

			// ensure the milepost is within certain bounds related to sighting
			// distance
			Block largeBlock = new Block(currentBlock);

			double blockEndMilePost = currentBlock.getMilePostsByNextLink(
					nextLink, currentTrain, 0);
			double blockStartMilePost = currentBlock
					.getMilePostsByPreviousLink(previousLink, currentTrain, 0);

			Debug.fine("getRunTimeReturnConsideringSighting: blockStartMilePost "
					+ blockStartMilePost
					+ " blockEndMilePost "
					+ blockEndMilePost);

			boolean enteredOnYellow = nextBlockPreviousSignal == GlobalVar.RED_SIGNAL;

			double startTinyBlockMilePost, endTinyBlockMilePost;
			startTinyBlockMilePost = milepost - currentBlock.getStartMilePost();
			endTinyBlockMilePost = blockEndMilePost
					- currentBlock.getStartMilePost();

			startTinyBlockMilePost = GlobalVariables
					.roundToThreeDecimals(startTinyBlockMilePost);
			endTinyBlockMilePost = GlobalVariables
					.roundToThreeDecimals(endTinyBlockMilePost);

			TinyBlockList tinyBlockList = largeBlock.getTinyBlockList();
			tinyBlockList = tinyBlockList.getTinyBlockListBetweenMilePosts(
					startTinyBlockMilePost, endTinyBlockMilePost);
			double start = 0;
			double length;
			for (TinyBlock tinyBlock : tinyBlockList) {
				length = Math.abs(tinyBlock.getRelativeEndMilePost()
						- tinyBlock.getRelativeStartMilePost());
				start = GlobalVariables.roundToThreeDecimals(start);
				tinyBlock.setRelativeStartMilePost(start);
				start += length;
				start = GlobalVariables.roundToThreeDecimals(start);
				tinyBlock.setRelativeEndMilePost(start);
			}

			largeBlock.setTinyBlockList(tinyBlockList);

			VelocityProfileArray newVelocityProfileArray = new VelocityProfileArray();
			VelocityProfileArray vpaTillMilepost = velocityProfileArray
					.returnInterval(blockStartMilePost, milepost);
			largeBlock.setStartMilePost(trainDirection, milepost);

			consideringHalts = true;
			nextBlockLatestSignal = nextBlock.getSignal(currentTrain,
					oldDepartureTime, consideringHalts,
					maxSignalAspectsToBeConsidered, simulationInstance);

			consideringHalts = false;
			boolean enteringNextBlockOnYellow = nextBlock.getSignal(
					currentTrain, oldDepartureTime, consideringHalts,
					maxSignalAspectsToBeConsidered, simulationInstance) == GlobalVar.YELLOW_SIGNAL;

			VelocityProfileArray vpaFromSignalChange = largeBlock
					.getVelocityProfileArray(currentTrain,
							nextBlockLatestSignal + 1, velocity, 0, nextLink,
							null, 0, enteredOnYellow, enteringNextBlockOnYellow);
			VelocityProfileArray vpaFromMilePost = vpaFromSignalChange
					.returnInterval(milepost, blockEndMilePost);

			newVelocityProfileArray.addAll(vpaTillMilepost);
			newVelocityProfileArray.addAll(vpaFromMilePost);

			for (VelocityProfile velocityProfile : vpaFromMilePost) {
				velocityProfile.setTrackSegment(currentBlock);
			}

			double endVelocity = vpaFromMilePost.getEndVelocity(currentTrain);

			double totalTime = newVelocityProfileArray.getRunTime();
			RunTimeReturn newRunTimeReturn = new RunTimeReturn(totalTime,
					endVelocity, newVelocityProfileArray,
					previousLinkVelocityProfileArray);

			return newRunTimeReturn;

		}

		return null;
	}

	/**
	 * 1) It first finds what is the signal of the block for the train and the
	 * arrival time by calling currentBlock.getSignal(currentTrain, noOfColor,
	 * arrivalTime,currentTrain.direction); 2) It first tries to find the
	 * running time required to traverse the path, by going to
	 * currentBlock.getRunTimeSignal(currentTrain, j, arrivalTime,
	 * startVelocity, !linkPriorityIsOne ? link.nextBlock.maximumPossibleSpeed :
	 * 0.0D); and setting that value in runTimeReturn 3) If the simulation is in
	 * the signalFailureMode or if the linkPriority is one it resets the
	 * runTimeReturn to a block whose starting milePost is the same but the
	 * endMilePost is the actual endMilePost minus the warnerDistance. This it
	 * does by runtimereturn = currentBlock.calculateRecurrenceVelocity(
	 * currentTrain, signal, arrivalTime, link.nextBlock, runtimereturn, 0.0D);
	 * 
	 * Block: RunTimeReturn calculateRecurrenceVelocity(currentTrain, signal,
	 * arrivalTime, link.nextBlock, runtimereturn, 0.0D) 4) If the train is
	 * scheduled and the currentBlock is a loop, then it checks in the
	 * trainReferenceTable if the train has to halt at the station. It does so
	 * by checking against the entries of the train, the stationName of the
	 * loop. If the train has to halt, it signals a yellow so that the train
	 * will have to stop at the station. The departure time will be the
	 * startTime + runTimeReturn.totalTime, meaning that the departureTime is
	 * now set to the arrivalTime of the train in the block plus the time taken
	 * by the train to traverse the block which in this case is the loop. If the
	 * train does not have to halt, then it gives the train the required signal
	 * gotten from currentBlock.getSignal function. The departureTime is
	 * arrivalTime + runTimeReturn.totalTime + GlobalVar.minTime (which is the
	 * minimum time a train should take to traverse any block). If the train is
	 * not scheduled or the currentBlock is just a "block" is the same case as
	 * the case where the scheduled train does not have to halt. 5) It then
	 * marks the overlap region. The overlapStartDistance is the beginning of
	 * the block where the train hits first. The overlapEndDistance is the start
	 * of the block plus the length of the train plus some minimum distance. It
	 * sets the GlobalVar.overlap to be true. 6) Then it makes sure that the
	 * GlobalVar.overlapEndDistance fits between the distance of the last loop
	 * that the train will face. For scheduled trains it is the referenceTable's
	 * last station entry's preferredLoop and for the unscheduled train it is
	 * the train.endLoopNo. 7) Then it loops over block2 by updating it to the
	 * getNextBlock along the trainDirection and finds the last such block. If
	 * that lastBlock is a loop it checks the arrivalTime and departureTime of
	 * the train at the loop's station and accordingly sets the endVelocity of
	 * the train.
	 * 
	 * @param link
	 * @param tempLink
	 * @param arrivalTime
	 * @param departureTime
	 * @param startVelocity
	 * @param linkPriorityIsNotOne
	 * @param numberOfColour
	 * @param profileStartingMilePost
	 * @param nextReferenceLoopNumber
	 * @return {@link StatusTraverseBlock}
	 */

	public StatusTraverseBlock traversePath(Link previousLink, Link nextLink,
			double arrivalTime, double departureTime, double startVelocity,
			boolean linkPriorityIsNotOne, int maxSignalAspectsToBeConsidered,
			double profileStartingMilePost, int nextReferenceLoopNumber) {

		Debug.print(" In BlockScheduler.traversePath ");
		Debug.info("traversePath: departureTime " + departureTime
				+ " arrivalTime " + arrivalTime
				+ " maxSignalAspectsToBeConsidered "
				+ maxSignalAspectsToBeConsidered);

		BlockSchedulingParameters blockSchedulingParameters = determineBlockSchedulingParameters(
				previousLink, nextLink, arrivalTime, startVelocity,
				linkPriorityIsNotOne, departureTime, profileStartingMilePost,
				maxSignalAspectsToBeConsidered);

		int signal = blockSchedulingParameters.signal;
		int nFreeBlocksToBeTraversed = blockSchedulingParameters.nFreeBlocksToBeTraversed;
		RunTimeReturn runTimeReturn = blockSchedulingParameters.runTimeReturn;

		double tempDepartureTime = blockSchedulingParameters.tempDepartureTime;

		if (!GlobalVariables.stationToStationScheduling) {
			double calDepTime = calDepTimeForSchedTrainHalt(arrivalTime
					+ runTimeReturn.totalTime);
			// System.out.println("traversePath: calDepTime " + calDepTime
			// + " tempDepTime " + tempDepartureTime);
			tempDepartureTime = Math.max(tempDepartureTime, calDepTime);
		}

		Debug.info("traversePath: After returning tempDepartureTime "
				+ tempDepartureTime);

		// Block nextBlock = currentBlock.getNextBlock(
		// currentTrain.getDirection(), currentTrain);
		Block nextBlock = nextLink.getNextBlock();

		OverlapParameters overlapParameters = setOverlapParameters(nextLink,
				profileStartingMilePost);

		boolean consideringHalts = false;
		int currentBlockSignalSeen = currentBlock.getSignal(currentTrain,
				tempDepartureTime, consideringHalts,
				maxSignalAspectsToBeConsidered, simulationInstance);
		boolean enteredOnYellow = currentBlockSignalSeen == GlobalVar.YELLOW_SIGNAL;

		// System.out.println("calculateTotalTimeTillEnd: rTR time "
		// + runTimeReturn.totalTime + " rTR velocity "
		// + runTimeReturn.endVelocity + " arrivalTime " + arrivalTime
		// + " overlap " + GlobalVar.overlap);
		// time to cover overlapping part
		double totalTimeTillEnd = calculateTotalTimeTillEnd(runTimeReturn,
				nextBlock, arrivalTime, nextLink, profileStartingMilePost,
				overlapParameters, enteredOnYellow);

		// System.out.println("Arrival Time " + arrivalTime + " departure time "
		// + tempDepartureTime);

		StatusTraverseBlock earliestFreeTimeSTP = computeEarliestFreeTimeSTP(
				previousLink, nextLink, nFreeBlocksToBeTraversed, arrivalTime,
				tempDepartureTime, totalTimeTillEnd, startVelocity,
				linkPriorityIsNotOne, profileStartingMilePost,
				nextReferenceLoopNumber);
		if (earliestFreeTimeSTP != null) {
			Debug.info("traversePath: returning earliestFreeTimeSTP");
			return earliestFreeTimeSTP;
		}

		StatusTraverseBlock signalFailureSTP = handleSignalFailure(nextLink,
				tempDepartureTime);
		if (signalFailureSTP != null)
			return signalFailureSTP;

		// recursion terminating conditions or the base cases - Devendra
		// GlobalVar.flag = isNextBlockLastBlock();

		// System.out.println("calling RBAGCS: tempDepTime " + tempDepartureTime
		// + " arrivalTime " + arrivalTime + " totalTimeTillEnd "
		// + totalTimeTillEnd + " runTimeReturn "
		// + runTimeReturn.totalTime);
		// if next block scheduling is successful reserve blocks and return

		return reserveBlocksAndGetCurrentSTP(nextLink, previousLink,
				tempDepartureTime, runTimeReturn, nFreeBlocksToBeTraversed,
				arrivalTime, totalTimeTillEnd, signal, profileStartingMilePost,
				nextReferenceLoopNumber);
	}

	/**
	 * Calculate the time since the head of the train leaves the currentBlock
	 * till the tail of the train leaves currentBlock.
	 * 
	 * @param runTimeReturn
	 * @param nextBlock
	 * @param arrivalTime
	 * @param profileStartingMilePost
	 * @param enteredOnYellow
	 * @return time between the exiting time instances of the head and tail of
	 *         the currentTrain from currentBlock
	 */
	public double calculateTotalTimeTillEnd(RunTimeReturn runTimeReturn,
			Block nextBlock, double arrivalTime, Link nextLink,
			double profileStartingMilePost,
			OverlapParameters overlapParameters, boolean enteredOnYellow) {

		Block blockIter = nextBlock;
		LastOverlapBlock lastOverlapBlock = getLastOverlapBlock(blockIter,
				overlapParameters);
		int numberOfLastBlockFromCurrentBlock = lastOverlapBlock.numberOfBlockFromCurrentBlock;
		Block lastBlock = lastOverlapBlock.block;

		double maxSpeedOfTrain = calculateMaxSpeedOfTrain(lastBlock);

		double runTimeReturnEndVelocity = runTimeReturn.endVelocity;
		RunTimeReturn runtimereturn1 = null;

		// j1 is the number of blocks after the currentBlock
		Debug.print("block1 " + nextBlock.getBlockNo()
				+ " numberOfLastBlockFromCurrentBlock "
				+ numberOfLastBlockFromCurrentBlock);

		Debug.fine("calculateTotalTimeTillEnd: Calling gRTS: arrivalTime "
				+ arrivalTime + " velocity " + runTimeReturnEndVelocity
				+ " speed " + maxSpeedOfTrain + " nBlocks "
				+ numberOfLastBlockFromCurrentBlock);

		double nextBlockEndVelocity = maxSpeedOfTrain;
		if (nextBlock.isLoop()) {
			Loop loop = (Loop) nextBlock;
			if (currentTrain.hasScheduledHalt(loop, simulationInstance)) {
				nextBlockEndVelocity = 0;
			}
		}
		
		boolean enteringNextBlockOnYellow = true;
		VelocityProfileArray overlappingVelocityProfileArray = nextBlock
				.getVelocityProfileArray(currentTrain,
						numberOfLastBlockFromCurrentBlock,
						runTimeReturnEndVelocity, nextBlockEndVelocity, null,
						nextLink, profileStartingMilePost, enteredOnYellow,
						enteringNextBlockOnYellow);

		double totalTimeTillEnd = -1D;

		if (overlappingVelocityProfileArray.size() == 0) {
			return totalTimeTillEnd;
		}

		// runtimereturn1
		// = nextBlock.getRunTimeSignal(currentTrain,
		// numberOfLastBlockFromCurrentBlock, runTimeReturnEndVelocity,
		// maxSpeedOfTrain, nextLink, null, profileStartingMilePost,
		// overlapFlag, simulationInstance.overlapStartDistance,
		// simulationInstance.overlapEndDistance);

		// System.out.println("OverlappingRunTime: startMilePost "
		// + emptyBlock.startMilePost + " endMilePost "
		// + emptyBlock.endMilePost + " overlapStartDistance "
		// + overlapStartDistance + " overlapEndDistance "
		// + overlapEndDistance);
		runtimereturn1 = currentBlock.getOverlappingRunTimeReturn(currentTrain,
				overlappingVelocityProfileArray, overlapParameters);

		totalTimeTillEnd = runtimereturn1.totalTime;

		totalTimeTillEnd += simulationInstance.blockWorkingTime;
		return totalTimeTillEnd;
	}

	/**
	 * @return true if the next block to be traversed is the last one. Else
	 *         return false.
	 */
	public boolean isNextBlockLastBlock() {

		int loopNo = currentTrain.getLastLoopNo();
		if (currentTrain.isScheduled()) {

			int nextBlockNo = currentBlock.getNextBlock(currentTrain)
					.getBlockNo();
			flag = false;
			if (nextBlockNo == loopNo)
				return true;

		} else {// (!currentTrain.isScheduled)
			return currentBlock.checkLastBlock(currentTrain.getDirection(),
					loopNo);
		}
		return false;
	}

	/**
	 * Get the {@link StatusTraverseBlock} for the current block if the
	 * scheduling for the next block is successful. In that case, the
	 * corresponding blocks should also be reserved. If the scheduling for next
	 * block is unsuccessful, it should return a {@link StatusTraverseBlock}
	 * with false status.
	 * 
	 * @param link
	 * @param previousLink
	 * @param tempDepartureTime
	 * @param runtimereturn
	 * @param nFreeBlocksToBeTraversed
	 * @param arrivalTime
	 * @param totalTimeTillEnd
	 * @param signal
	 * @param profileStartingMilePost
	 * @param nextReferenceLoopNumber
	 * @return {@link StatusTraverseBlock} with true or false status depending
	 *         upon the successful scheduling of train on next block.
	 */
	public StatusTraverseBlock reserveBlocksAndGetCurrentSTP(Link nextLink,
			Link previousLink, double tempDepartureTime,
			RunTimeReturn runtimereturn, int nFreeBlocksToBeTraversed,
			double arrivalTime, double totalTimeTillEnd, int signal,
			double profileStartingMilePost, int nextReferenceLoopNumber) {

		double previousBlockRelativeMilePost = nextLink
				.getPreviousBlockRelativeMilePost();
		Block previousBlock = nextLink.getPreviousBlock();
		double previousBlockLength = previousBlock.getLength();
		double nextProfileStartingMilePost;
		int trainDirection = currentTrain.getDirection();
		if (trainDirection == GlobalVar.UP_DIRECTION) {
			nextProfileStartingMilePost = profileStartingMilePost
					+ previousBlockRelativeMilePost;
		} else {
			nextProfileStartingMilePost = profileStartingMilePost
					- previousBlockLength + previousBlockRelativeMilePost;
		}

		StatusTraverseBlock nextStatustraverseblock = null;
		try {
			// nextFastestLink not determined yet. So null.
			Link nextFastestLink = null;
			nextStatustraverseblock = nextLink.getNextBlock().traverseBlock(
					currentTrain, tempDepartureTime, tempDepartureTime,
					runtimereturn.endVelocity, nextLink, nextFastestLink,
					nextProfileStartingMilePost, simulationInstance,
					nextReferenceLoopNumber);
		} catch (Exception ex) {
			ex.printStackTrace();
			Debug.error(ex.getMessage());
			Debug.error("currentTrain " + currentTrain.getTrainNo()
					+ " currentBlock " + currentBlock.getBlockNo()
					+ " caused trouble");
			System.exit(0);
		}

		if (nextStatustraverseblock == null) {
			return new StatusTraverseBlock(false, true,
					currentTrain.getTrainNo());
		}

		if (nextStatustraverseblock.status) {

			Debug.info("blockNo + " + currentBlock.getBlockNo()
					+ " nFreeBlocksToBeTraversed " + nFreeBlocksToBeTraversed);
			reserveBlocks(nextLink, previousLink, nFreeBlocksToBeTraversed,
					arrivalTime, tempDepartureTime, totalTimeTillEnd,
					runtimereturn.getBlockVelocityProfileArray(),
					runtimereturn.getPreviousLinkVelocityProfileArray(), signal);
			return new StatusTraverseBlock(true, arrivalTime,
					currentTrain.getTrainNo());
		}

		// arrival time for next block
		double nextBlockArrivalTime = nextStatustraverseblock.departureTime;

		// backtracking: calculating new arrival time for the current block
		// double newCurrentBlockArrivalTime = nextBlockArrivalTime
		// - runtimereturn.totalTime - GlobalVar.minTime;
		double newCurrentBlockArrivalTime = nextBlockArrivalTime
				- runtimereturn.totalTime;

		// System.out
		// .println("reserveBlocksAndGetCurrentSTP: nextStatusTraverseBlock has false status");
		//System.out.println("reserveBlocksAndGetCurrentSTP: new ArrivalTime "
		//+ newCurrentBlockArrivalTime + " for block "
		//+ currentBlock.getBlockNo());
		return new StatusTraversePath(false, newCurrentBlockArrivalTime,
				nextBlockArrivalTime, -1);
	}

	/**
	 * @param link
	 * @param departureTime
	 * @return {@link StatusTraverseBlock} with signal -2, suggesting signal
	 *         failure. returns null if there is no signal failure.
	 */
	public StatusTraverseBlock handleSignalFailure(Link link,
			double departureTime) {
		if (/*
			 * (link.nextBlock.isSignalFailed(departureTime) == 1 ||
			 * link.nextBlock .signalFailed(tempDepartureTime) == 2)
			 */
		link.getNextBlock().isSignalFailed(departureTime)
				&& link.getNextBlock().getSignalFailFlag() == 0) {

			if (departureTime % 1400D > 1140D || departureTime % 1400D < 420D) {
				link.getNextBlock().setSignalFailFlag(2);
			} else {
				link.getNextBlock().setSignalFailFlag(1);
			}

			currentTrain.setSignalFailCounter(currentTrain
					.getSignalFailCounter() + 1);
			currentTrain.getSignalFailedBlocks()[currentTrain
					.getSignalFailCounter()] = link.getNextBlock().getBlockNo();
			return new StatusTraversePath(false, 0.0D, 0.0D, -2);
		}
		return null;
	}

	/**
	 * @param link
	 * @param nFreeBlocksToBeTraversed
	 * @param arrivalTime
	 * @param departureTime
	 * @param totalTimeTillEnd
	 * @param startVelocity
	 * @param linkPriorityIsNotOne
	 * @param profileStartingMilePost
	 * @param nextReferenceLoopNumber
	 * @return If any block till the lastOverlapping Block is not free then
	 *         "traversePath" the train only until that particular block, else
	 *         if the currentBlock is not free retry with the
	 *         earliestArrivalTime of the currentBlock. If neither of the above
	 *         situations is reached,return null.
	 */
	public StatusTraverseBlock computeEarliestFreeTimeSTP(Link previousLink,
			Link nextLink, int nFreeBlocksToBeTraversed, double arrivalTime,
			double departureTime, double totalTimeTillEnd,
			double startVelocity, boolean linkPriorityIsNotOne,
			double profileStartingMilePost, int nextReferenceLoopNumber) {

		Debug.info("computeEarliestFreeTimeSTP: arrivalTime " + arrivalTime
				+ " departureTime " + departureTime + " totalTimeTillEnd "
				+ totalTimeTillEnd + " nFreeBlocks " + nFreeBlocksToBeTraversed);
		Block blockIter = currentBlock;
		
		for (int blockIterNo = 0; blockIterNo < nFreeBlocksToBeTraversed; blockIterNo++) {
			double earliestFreeTime = blockIter.whenFree(arrivalTime,
					departureTime + totalTimeTillEnd);
			Debug.info("computeEarliestFreeTimeSTP: blockIter "
					+ blockIter.getBlockName() + " earliestFreeTime "
					+ earliestFreeTime);
			
			//System.out.println("Blockname "	+ blockIter.getBlockName());
			//System.out.println("Earliest Free Time = " + earliestFreeTime+" for arrivalTime =  "+ arrivalTime + 
					//" and departure time = " 	+ departureTime + " totalTimeTillEnd "	+ totalTimeTillEnd );
			
			if (blockIterNo == 0 && earliestFreeTime == -1D) {
				earliestFreeTime = nextLink.whenFree(departureTime,
						departureTime + totalTimeTillEnd);		
			}

			if (earliestFreeTime != -1D) {
				//earliestFreeTime += 1;
				earliestFreeTime += simulationInstance.minTime;
				// System.out.println();
				// System.out.println("minTime "+GlobalVar.minTime);
				// System.out.println();

				if (blockIterNo != 0) {
					Debug.print((new StringBuilder())
							.append("Reducing Signal to ").append(blockIterNo)
							.toString());
					Debug.warning("computerEarliestFreeTimeSTP: reducing signal to "
							+ (blockIterNo + 1));
					return traversePath(previousLink, nextLink, arrivalTime,
							departureTime, startVelocity, linkPriorityIsNotOne,
							blockIterNo + 1, profileStartingMilePost,
							nextReferenceLoopNumber);
				}

				Debug.warning("block reservation failure. So returning false with earliestFreeTime "
						+ earliestFreeTime);

				return new StatusTraversePath(false, earliestFreeTime,
						earliestFreeTime, 0);
			}

			blockIter = blockIter.getNextBlock(currentTrain);
		}

		return null;
	}

	/**
	 * @author devendra This class is to determine the last block which the
	 *         currentTrain will overlap with while just exiting the
	 *         currentBlock.
	 */
	private class LastOverlapBlock {
		/**
		 * numberOfBlockFromCurrentBlock
		 */
		int numberOfBlockFromCurrentBlock;
		/**
		 * block
		 */
		Block block;

		/**
		 * @param count
		 * @param lastBlock
		 */
		public LastOverlapBlock(int count, Block lastBlock) {
			numberOfBlockFromCurrentBlock = count;
			block = lastBlock;
		}
	}

	/**
	 * @param blockIter
	 * @param overlapParameters
	 * @return {@link LastOverlapBlock} such that the last block from the
	 *         currentBlock which the currentTrain will overlap while just
	 *         exiting the currentBlock
	 */
	public LastOverlapBlock getLastOverlapBlock(Block blockIter,
			OverlapParameters overlapParameters) {
		int count = 1;
		do {
			if (hasReachedOverlapEndDistance(blockIter, overlapParameters)) {
				break;
			}
			count++;

			blockIter = blockIter.getNextBlock(currentTrain);
		} while (blockIter != null);

		return new LastOverlapBlock(count, blockIter);
	}

	/**
	 * Get the maximum speed of the train for the block it would overlap into
	 * while just exiting the currentBlock.
	 * 
	 * @param block
	 * @return calculate maximum speed of the train for the last block it would
	 *         overlap onto while exiting the currentBlock.
	 */
	public double calculateMaxSpeedOfTrain(Block block) {
		double blockMaxSpeed = block == null ? 0 : block
				.getMaximumPossibleSpeed();
		double maxSpeedOfTrain = Math.max(
				currentTrain.getMaximumPossibleSpeed(), blockMaxSpeed);

		if (block == null)
			return maxSpeedOfTrain;

		if (currentTrain.isScheduled() && block.isLoop()) {

			String stationName = ((Loop) block).getStation().getStationName();

			ReferenceTableEntry referenceTableEntry = currentTrain
					.getRefTabEntryFromStationName(stationName);
			if (referenceTableEntry != null) {
				double refArrTime = referenceTableEntry
						.getReferenceArrivalTime();
				double refDepTime = referenceTableEntry
						.getReferenceDepartureTime();
				if (refDepTime > refArrTime) {
					maxSpeedOfTrain = 0.0D;
				}
			}
		}else if (!currentTrain.isScheduled() && block.isLoop()) {
			//Freight Train Halting
			String stationName = ((Loop) block).getStation().getStationName();

			ReferenceTableEntry referenceTableEntry = currentTrain
					.getRefTabEntryFromStationName(stationName);
			if (referenceTableEntry != null) {
				double refMinHaltTime = referenceTableEntry
						.getMinHaltTime();
				if (refMinHaltTime > 0) {
					maxSpeedOfTrain = 0.0D;
				}
			}
		}
		return maxSpeedOfTrain;
	}

	/**
	 * Determine if the block depending upon the train's direction covers the
	 * Global.overlapEndDistance
	 * 
	 * @param block
	 * @return true if the block covers the Global overlapEndDistance
	 */
	public boolean hasReachedOverlapEndDistance(Block block,
			OverlapParameters overlapParameters) {
		if (currentTrain.getDirection() == GlobalVar.UP_DIRECTION) {
			return (block.getEndMilePost(currentTrain.getDirection()) > overlapParameters.overlapEndDistance);
		} else {// currentTrain direction is down
			return (block.getEndMilePost(currentTrain.getDirection()) < overlapParameters.overlapEndDistance);
		}
	}

	/**
	 * Set the overlapStartDistance, overlapEndDistance and overlap boolean
	 * depending upon the block and the train.
	 * 
	 * @param nextLink
	 * @param profileStartingMilePost
	 */
	public OverlapParameters setOverlapParameters(Link nextLink,
			double profileStartingMilePost) {
		double overlapStartDistance;
		double overlapEndDistance;

		// Block nextBlock = currentBlock.getNextBlock(
		// currentTrain.getDirection(), currentTrain);
		Block nextBlock = nextLink.getNextBlock();
		Block previousBlock = nextLink.getPreviousBlock();
		int trainDirection = currentTrain.getDirection();

		double nextBlockStartMilePost = nextBlock
				.getStartMilePost(trainDirection);

		overlapStartDistance = previousBlock.getMilePostsByNextLink(nextLink,
				currentTrain, profileStartingMilePost);
		overlapEndDistance = nextBlockStartMilePost
				// + GlobalVar.minimumOverLapDistance
				+ trainDirection
				* (nextBlock.getOverlapDistance(trainDirection) + currentTrain
						.getLength());

		int endLoopNo;
		if (currentTrain.isScheduled()) {
			int size = currentTrain.getRefTables().size();
			endLoopNo = currentTrain.getRefTables().get(size - 1)
					.getReferenceLoopNo();
		} else {
			endLoopNo = currentTrain.getEndLoopNo();
		}

		double overlapEndMilePost = simulationInstance.getLastEndmilepost(
				endLoopNo, trainDirection);

		if (trainDirection * (overlapEndDistance - overlapEndMilePost) >= 0) {
			overlapEndDistance = overlapEndMilePost - trainDirection * 0.01D;
		}

		overlapStartDistance = GlobalVariables
				.roundToThreeDecimals(overlapStartDistance);
		overlapEndDistance = GlobalVariables
				.roundToThreeDecimals(overlapEndDistance);

		OverlapParameters overlapParameters = new OverlapParameters(
				overlapStartDistance, overlapEndDistance);
		return overlapParameters;
	}

	/**
	 * @param link
	 * @param i
	 * @param arrivalTime
	 * @param departureTime
	 * @param totalTimeForRestOfPath
	 * @param velocityProfileArray
	 * @param velocityProfileArray2
	 * @param signal
	 */

	public void reserveBlocks(Link nextLink, Link previousLink,
			int nFreeBlocksToBeReserved, double arrivalTime,
			double departureTime, double totalTimeTillEnd,
			VelocityProfileArray blockVelocityProfileArray,
			VelocityProfileArray previousLinkVelocityProfileArray, int signal) {

		double previousLinkRunTime = 0;
		if (previousLinkVelocityProfileArray != null) {
			previousLinkRunTime = previousLinkVelocityProfileArray.getRunTime();
		}

		double haltTime = 0D;
		if (blockVelocityProfileArray != null) {
			double runTime = blockVelocityProfileArray.getRunTime();
			runTime += previousLinkRunTime;

			haltTime = departureTime - (arrivalTime + runTime);
		} else {
			Debug.error("reserveBlocks: currentBlock "
					+ currentBlock.getBlockName() + " currentTrain "
					+ currentTrain.getTrainNo()
					+ " velocityProfileArray is null");
		}

		if (haltTime > 0) {
			double waitingMilePost = currentBlock.getMilePostsByNextLink(
					nextLink, currentTrain, 0);

			VelocityProfile haltingVelocityProfile = new VelocityProfile(
					waitingMilePost, waitingMilePost, 0, 0, haltTime, 0);
			haltingVelocityProfile.setTrackSegment(currentBlock);
			blockVelocityProfileArray.add(haltingVelocityProfile);
		}

		Debug.info("reserveBlocks: blockNo " + currentBlock.getBlockNo()
				+ " arr " + arrivalTime + " dep " + departureTime + " tot "
				+ totalTimeTillEnd);

		Debug.print(" In Blockscheduler.RESERVEBLOCKS ");
		Block block = currentBlock;
		for (int k = 0; k < nFreeBlocksToBeReserved; k++) {
			if (k == 0) {
				block.reserve(currentTrain, arrivalTime, departureTime
						+ totalTimeTillEnd, true);

				if (nextLink != null)
					nextLink.reserve(currentTrain, departureTime,
							totalTimeTillEnd);
				// TODO correct previousLink reservation timings
				// if (previousLink != null)
				// previousLink.reserve(currentTrain, arrivalTime,
				// departureTime);
			} else {

				block.reserve(currentTrain, arrivalTime, departureTime
						+ totalTimeTillEnd, false);
			}

			block = block.getNextBlock(currentTrain);
		}

		int trainDirection = currentTrain.getDirection();

		Debug.info("reserveBlocks: " + currentBlock.getBlockName()
				+ " reservation complete");
		// if (blockVelocityProfileArray.size() > 0) {
		if (currentBlock.getSignalFailFlag() != 0) {

			currentTrain.getTimeTables().add(
					0,
					new SimulatorTimeTableEntry(currentBlock.getBlockNo(),
							arrivalTime + previousLinkRunTime, departureTime,
							blockVelocityProfileArray
									.getStartMilePost(trainDirection),
							blockVelocityProfileArray
									.getEndMilePost(trainDirection),
							blockVelocityProfileArray, 6));
			if (previousLinkVelocityProfileArray.size() > 0)
				currentTrain.getTimeTables().add(
						0,
						new SimulatorTimeTableEntry(currentBlock.getBlockNo(),
								arrivalTime, arrivalTime + previousLinkRunTime,
								previousLinkVelocityProfileArray
										.getStartMilePost(trainDirection),
								previousLinkVelocityProfileArray
										.getEndMilePost(trainDirection),
								previousLinkVelocityProfileArray, 6));

		} else {
			currentTrain.getTimeTables().add(
					0,
					new SimulatorTimeTableEntry(currentBlock.getBlockNo(),
							arrivalTime + previousLinkRunTime, departureTime,
							blockVelocityProfileArray
									.getStartMilePost(trainDirection),
							blockVelocityProfileArray
									.getEndMilePost(trainDirection),
							blockVelocityProfileArray, signal));
			if (previousLinkVelocityProfileArray != null
					&& previousLinkVelocityProfileArray.size() > 0)
				currentTrain.getTimeTables().add(
						0,
						new SimulatorTimeTableEntry(currentBlock.getBlockNo(),
								arrivalTime, arrivalTime + previousLinkRunTime,
								previousLinkVelocityProfileArray
										.getStartMilePost(trainDirection),
								previousLinkVelocityProfileArray
										.getEndMilePost(trainDirection),
								previousLinkVelocityProfileArray, signal));

		}
		// }
		// else {
		// Debug.error("BlockScheduler: reserveBlocks: blockVelocityProfileArray has size 0");
		// }
	}
}

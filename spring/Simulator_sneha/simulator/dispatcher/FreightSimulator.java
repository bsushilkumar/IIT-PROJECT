package simulator.dispatcher;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import globalVariables.GlobalVariables;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.time.Interval;
import gui.entities.sectionEntities.time.IntervalArray;
import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntityList.TrainList;
import gui.entities.trainEntities.Train;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeSet;

import simulator.input.SimulationInstance;
import simulator.outputFeatures.GraphPanel;
import simulator.scheduler.SimulatorTimeTableEntry;
import simulator.scheduler.StationToStationScheduler;
import simulator.scheduler.StatusTraverseBlock;
import simulator.util.Debug;

/**
 * FreightSimulator
 */
public class FreightSimulator extends Simulator {
	/**
	 * constructor.
	 * 
	 * @param Trains
	 *            {@link ArrayList} of trains
	 * @param stations
	 *            {@link ArrayList} of stations
	 */
	public FreightSimulator(TrainList trainList,
			SimulationInstance simulationInstance) {
		setTrainList(trainList);
		this.simulationInstance = simulationInstance;
	}

	/**
	 * Simulate the next train.
	 * 
	 * @param numberOfSignalAspects
	 * 
	 * @return currentTrainNumber. returns -1 on error.
	 */
	public int simulateNextTrain(int numberOfSignalAspects) {
		Debug.fine("simulateNextTrain: trainArray size "
				+ getTrainList().size() + " currentTrainNo "
				+ getCurrentTrainNo());
		Debug.print("I am in simulate next train");
		//santhosh
		if(GlobalVar.capacitySelected==true){
			simulationInstance.temp=0;
		}
		Train currTrain = null;
		if (getTrainList().size() > getCurrentTrainNo()) {
			currTrain = getTrainList().get(getCurrentTrainNo());
			
			Debug.print("Train no is " + currTrain.getTrainNo());
			int val = simulateTrain(currTrain, numberOfSignalAspects,
					simulationInstance);

			setCurrentTrainNo(getCurrentTrainNo() + 1);
			if (val == -1)
				return -1;

			// currTrain.printVelocityProfileForTrain();
			// currTrain.printDiscontinuity();
			// ArrayList<Block> blocksTraversed =
			// currTrain.getBlocksTraversedByTrain();
			ArrayList<Block> maxTimeBlocks = currTrain
					.getBlocksByMaxTimeTaken(simulationInstance);
			// Block block = maxTimeBlocks.get(0);
			// Interval interval = block.getLastIntervalByTrain(currTrain);
			// double blockTraversalTime = interval.getTime();
			// System.out.println("Train " + currTrain.getTrainNo() +
			// " maxTimeBlock "
			// + block.getBlockNo() + " timeTaken " + blockTraversalTime);

			if (getCurrentTrainNo() >= getTrainList().size()) {
				return -1;
			}

			return getCurrentTrainNo();
		}

		return -1;

	}

	/**
	 * FreightSimulator: int simulateTrain(Train currTrain) 1) First get the
	 * train's starting loop number, then its station from the loop number. Then
	 * we go to station.simulateTrain(Train currTrain)
	 * 
	 * @param currTrain
	 * @param numberOfSignalAspects
	 * @return simulateTrain
	 */
	private int simulateTrain(Train currTrain, int numberOfSignalAspects,
			SimulationInstance simulationInstance) {
		Station currStn = null;

		// double arrivalTime,departTime;
		Debug.print("FreightSimulator: simulateTrain: trainNo = "
				+ currTrain.getTrainNo());
		Debug.currTrainNo = currTrain.getTrainNo();

		simulationInstance.currentTrain = currTrain;

		// arrivalTime=currTrain.startTime;
		// departTime=currTrain.startTime;

		int loopNo = currTrain.getStartLoopNo();
		Loop startLoop = (Loop) simulationInstance.getBlockFromBlockNo(loopNo);

		if (startLoop != null) {
			currStn = startLoop.getStation();
		} else {
			Debug.print("FreightSimulator: simulateTrain: Error: Loop not present "
					+ loopNo);
			System.out
					.println("FreightSimulator: hbLinkEntry is null. Assigning a random starting loop");
		}

		if (currStn == null) {
			throw new NullPointerException(
					"FreightSimulator: simulateTrain: currStn is null");
		}

		int val = currStn.simulateTrain(currTrain,
				simulationInstance.simulationType, simulationInstance);

		if (val == -1) {
			return -1;
		}

		Debug.print("FreightSimulator: simulateTrain: ");
		if (getGraphPanel() != null) {
			getGraphPanel().drawTrain(currTrain, numberOfSignalAspects);
		}

		simulationInstance.currentTrain = null;
		// Train train = getTrainList().get(0);
		// train.printVelocityProfileForTrain();
		if (val == -1)
			return -1;
		return 0;
	}

	/**
	 * Produce the graph.
	 * 
	 * @param p
	 */
	public void produceGraph(GraphPanel p) {
		setGraphPanel(p);
	}

	/**
	 * NoGraph.
	 */
	public void noGraph() {
		setGraphPanel(null);
	}

	/**
	 * simulate.
	 */
	public void simulate(int numberOfSignalAspects) {
		System.out.println("FreightSimulator: simulate: train array size "
				+ getTrainList().size());
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					FileNames.getTestCaseDirectory() + "/error.txt"));

			while (getCurrentTrainNo() < getTrainList().size()) {
				Train train = getTrainList().get(currentTrainNo);
				simulationInstance.trainTemp = simulationInstance.temp;

				int val = simulateNextTrain(numberOfSignalAspects);
				if (val == -1) {
					// setCurrentTrainNo(getCurrentTrainNo() + 1);
					bw.write("Could not simulate train " + train.getTrainNo()
							+ "\n");
					// break;
				} else {
					double traversalTime = train.totalTime();

					if (traversalTime < 2 * 60) {// minutes
						bw.write("Train " + train.getTrainNo()
								+ " successfully simulated in " + traversalTime
								+ " minutes\n");

					} else {
						bw.write("Train " + train.getTrainNo() + " took "
								+ traversalTime
								+ " which is greater than 2 hours\n");
					}

					ArrayList<SimulatorTimeTableEntry> simulatorTimeTableEntries = train
							.getTimeTables();
					ArrayList<ReferenceTableEntry> referenceTableEntries = train
							.getRefTables();

					if (simulatorTimeTableEntries.size() > 0
							&& referenceTableEntries.size() > 0) {
						double endTime = simulatorTimeTableEntries.get(
								simulatorTimeTableEntries.size() - 1)
								.getDepartureTime();
						double referenceEndTime = referenceTableEntries.get(
								referenceTableEntries.size() - 1)
								.getReferenceDepartureTime();
						double delayTime = endTime - referenceEndTime;

						if (delayTime < 0) {
							bw.write("Status - on time - ");
						} else if (delayTime < 2) {
							bw.write("Status - delayed by less than 2 minutes - "
									+ delayTime);
						} else if (delayTime < 5) {
							bw.write("Status - delayed by less than 5 minutes - "
									+ delayTime);
						} else if (delayTime < 10) {
							bw.write("Status - delayed by less than 10 minutes - "
									+ delayTime);
						} else if (delayTime < 15) {
							bw.write("Status - delayed by less than 15 minutes - "
									+ delayTime);
						} else if (delayTime < 20) {
							bw.write("Status - delayed by less than 20 minutes - "
									+ delayTime);
						} else if (delayTime < 40) {
							bw.write("Status - delayed by less than 40 minutes - "
									+ delayTime);
						} else {
							bw.write("Status - delayed by more than 40 minutes - "
									+ delayTime);
						}

						bw.write("\n");
					}

				}
			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("SimulationIsOver");
		System.out.println("Exited");
	}

	public TrainList getTrainList() {
		return trainList;
	}

	public void setTrainList(TrainList trainList) {
		this.trainList = trainList;
	}

	public int getCurrentTrainNo() {
		return currentTrainNo;
	}

	public void setCurrentTrainNo(int currentTrainNo) {
		this.currentTrainNo = currentTrainNo;
	}

	public GraphPanel getGraphPanel() {
		return graphPanel;
	}

	public void setGraphPanel(GraphPanel graphPanel) {
		this.graphPanel = graphPanel;
	}

	public void startStationToStationScheduling(int trainNo) {
		Debug.fine("FreightSimulator: stationToStationScheduling");
		System.out.println("FreightSimulator: stationToStationScheduling");

		if (trainNo == -1) {
			Train trainToBeScheduled = null;
			boolean allTrainsScheduledTillEnd = true;
			for (int i = 0; i < trainList.size(); i++) {
				Train train = trainList.get(i);
				Debug.error("s2s: train " + train.getTrainNo());
				if (train.isScheduledTillEnd()) {
					Debug.error("s2s: train " + train.getTrainNo()
							+ " already scheduled till end");
					continue;
				}

				allTrainsScheduledTillEnd = false;

				Loop currentLoop = train.getCurrentLoop();
				if (currentLoop != null) {
					ReferenceTableEntry referenceTableEntry = train
							.getRefTabEntryFromStationName(currentLoop
									.getStationName());
					if (!referenceTableEntry.areConditionsSatisfied(trainList)) {
						Debug.error("All conditions not met");
						continue;
					}
				}

				Loop nextReferenceLoop = train.getNextReferenceLoop();
				Train previousRakeLinkTrain = train.getPreviousRakeLinkTrain();
				boolean previousRakeLinkTrainScheduled = true;

				if (previousRakeLinkTrain != null) {
					previousRakeLinkTrainScheduled = previousRakeLinkTrain
							.isScheduledTillEnd();
					if (!previousRakeLinkTrainScheduled)
						continue;
				}

				boolean allLoopsTillNextHaltUnlocked = true;
				if (nextReferenceLoop != null) {

					Train loopNextTrainToBeScheduled = nextReferenceLoop
							.getLoopNextTrainToBeScheduled();

					if (loopNextTrainToBeScheduled != null
							&& loopNextTrainToBeScheduled.getTrainNo() != train
									.getTrainNo()) {
						continue;
					}

					if (currentLoop.equals(nextReferenceLoop)) {
						if (currentLoop.isLocked())
							allLoopsTillNextHaltUnlocked = false;
					} else {
						Block currentBlock = currentLoop.getNextBlock(train);
						while (currentBlock != null) {
							if (currentBlock.isLoop()) {
								Loop loop = (Loop) currentBlock;
								if (loop.isLocked())
									allLoopsTillNextHaltUnlocked = false;
							}

							if (currentBlock.equals(nextReferenceLoop))
								break;
							else
								currentBlock = currentBlock.getNextBlock(train);
						}
					}
				}

				if ((allLoopsTillNextHaltUnlocked || train
						.hasReachedDestination())) {
					trainToBeScheduled = train;
					break;
				}
			}

			if (trainToBeScheduled == null) {
				if (!allTrainsScheduledTillEnd) {
					Debug.error("FreightSimulator: startStationToStationScheduling: deadlock ");
					// TODO
				} else {
					Debug.error("FreightSimulator: startStationToStationScheduling: all trains scheduled. SUCCESS");
				}
			} else {
				Train train = trainToBeScheduled;
				s2sScheduleTrain(train);
			}

		} else {
			Train train = simulationInstance.getTrainFromTrainNo(trainNo);
			s2sScheduleTrain(train);
		}
	}

	private void s2sScheduleTrain(Train train) {

		if (train.isScheduledTillEnd()) {
			Debug.info("FreightSimulator: s2sScheduleTrain: train + "
					+ train.getTrainNo() + " already scheduled till end ");
			return;
		}

		trainList.remove(train);
		Debug.error("train " + train.getTrainNo() + " removed from list");
		Loop startLoop;
		StationToStationScheduler stationToStationScheduler;
		double departureTime;
		double lockTime = GlobalVariables.MAXIMUM_LOCK_TIME;
		Loop nextReferenceLoop = null;
		Train rakeLinkTrain = null;

		if (train.stationToStationSchedulerStack.isEmpty()) {
			startLoop = train.getOriginLoop();
			nextReferenceLoop = startLoop;
		} else {
			StationToStationScheduler topStationToStationScheduler = train.stationToStationSchedulerStack
					.peek();
			startLoop = topStationToStationScheduler.nextReferenceLoop;
			nextReferenceLoop = startLoop.getNextReferenceLoop(train
					.getRefTables());

			if (train.hasReachedDestination()) {
				Debug.fine("s2sSchedule: Train already traversed its destination loop");
				Debug.fine("s2sSchedule: train + " + train.getTrainNo()
						+ " successfully scheduled");
				topStationToStationScheduler.scheduled = true;
				startLoop.unlock();
				train.setScheduledTillEnd(true);

				rakeLinkTrain = train.getRakeLinkTrain();
				if (rakeLinkTrain != null) {
					double refArrTime = train.getDepartTime();
					ArrayList<ReferenceTableEntry> refTables = rakeLinkTrain
							.getRefTables();
					refTables.get(0).setReferenceArrivalTime(refArrTime);
				}
			}
		}

		TreeSet<Integer> blockNumberSet = new TreeSet<Integer>();
		double displayTime;

		if (train.isScheduledTillEnd()) {
			displayTime = Double.POSITIVE_INFINITY;
		} else {
			Debug.fine("stationToStationSchedulerStack is empty "
					+ train.stationToStationSchedulerStack.isEmpty());

			departureTime = train.getActionableTime();

			Debug.fine("departureTime " + departureTime);

			stationToStationScheduler = new StationToStationScheduler(train,
					startLoop, nextReferenceLoop, departureTime);
			train.stationToStationSchedulerStack
					.push(stationToStationScheduler);

			int trainDirection = train.getDirection();

			boolean simFinish = false;

			double originalStartMilePost = startLoop.getStartMilePost();
			double originalEndMilePost = startLoop.getEndMilePost();
			if (trainDirection == GlobalVar.UP_DIRECTION)
				startLoop.setStartMilePost(originalEndMilePost);
			else
				startLoop.setDirectEndMilePost(originalStartMilePost);

			try {
				while (simFinish != true) {

					double startVelocity = 0;
					StatusTraverseBlock retStatus = startLoop.traverseBlock(
							train, departureTime, departureTime, startVelocity,
							null/* previousLink */, null/* nextFastestLink */,
							0 /* profileStartingMilePost */, simulationInstance,
							nextReferenceLoop.getBlockNo());

					simFinish = retStatus.status;

					if (retStatus.limit)
						return;

					// startTime = retStatus.departureTime + 1;
					Debug.fine("Station: simulateTrain: renewedDepartureTime "
							+ retStatus.departureTime);
					departureTime = retStatus.departureTime;// waiting for 1
															// minute
															// is
															// too
					// much. Leave as soon as you
					// see a yellow.

					if (simFinish) {
						stationToStationScheduler.scheduled = true;
						startLoop.unlock();
						if (nextReferenceLoop != null) {
							nextReferenceLoop
									.lock(lockTime, train.getTrainNo());
							nextReferenceLoop
									.forwardLoopTrainScheduleDataList();
						}
						Debug.info("SimulatedTrain " + train.getTrainNo()
								+ " till " + nextReferenceLoop.getBlockName());
					}

				}
			} catch (Exception e) {
				train.stationToStationSchedulerStack.pop();

				Debug.error("s2sSchedule: Exception occured with train "
						+ train.getTrainNo() + " at "
						+ startLoop.getBlockName());
				Debug.error(e.getMessage());

			}

			if (trainDirection == GlobalVar.UP_DIRECTION)
				startLoop.setStartMilePost(originalStartMilePost);
			else
				startLoop.setEndMilePost(originalEndMilePost);

			// System.out.println("Setting velocity profile times");
			train.setTimesForVelocityProfiles();

			blockNumberSet = stationToStationScheduler.getBlockNumberSet();
			displayTime = train.getDepartTime();
			// displayTime = Math.floor(displayTime);
			stationToStationScheduler.print();
		}


		StationToStationScheduler.insertionSort(trainList, train);
		simulationInstance.trainStack.add(train);
		Debug.error("train " + train.getTrainNo() + " put back into list");

		if (rakeLinkTrain != null)
			s2sScheduleTrain(rakeLinkTrain);

	}

	public void undoStationToStationScheduling(int trainNo) {
		if (trainNo == -1) {
			Stack<Train> trainStack = simulationInstance.trainStack;
			TrainList trainList = simulationInstance.trainList;
			if (trainStack.size() == 0) {
				Debug.info("FreightSimulator: undoStationToStationScheduling: No trains previously scheduled");
				return;
			}

			Train lastTrainScheduled = trainStack.pop();
			s2sUndoScheduleTrain(lastTrainScheduled);
			trainList.remove(lastTrainScheduled);
			trainList.add(0, lastTrainScheduled);

		} else {
			Train train = simulationInstance.getTrainFromTrainNo(trainNo);
			s2sUndoScheduleTrain(train);
		}
	}

	private void s2sUndoScheduleTrain(Train train) {
		Debug.info("s2sUndoScheduleTrain: train " + train.getTrainNo());
		Stack<StationToStationScheduler> stationToStationSchedulerStack = train.stationToStationSchedulerStack;
		StationToStationScheduler stationToStationScheduler;

		boolean wasScheduledTillEnd = train.isScheduledTillEnd();
		TreeSet<Integer> blockNumberSet = new TreeSet<Integer>();

		if (stationToStationSchedulerStack.isEmpty()) {
			return;
		} else {

			stationToStationScheduler = stationToStationSchedulerStack.peek();
			Loop newStartLoop = stationToStationScheduler.startLoop;

			double lockTime = GlobalVariables.MAXIMUM_LOCK_TIME;
			if (train.isScheduledTillEnd() && train.hasReachedDestination()) {
				train.setScheduledTillEnd(false);
				int destinationLoopNumber = train.getDestinationLoopNumber();
				Loop loop = (Loop) simulationInstance
						.getBlockFromBlockNo(destinationLoopNumber);
				loop.lock(lockTime, train.getTrainNo());
			} else {
				stationToStationSchedulerStack.pop();

				stationToStationScheduler.scheduled = false;
				Loop startLoop = newStartLoop;
				startLoop.lock(lockTime, train.getTrainNo());

				Loop nextReferenceLoop = stationToStationScheduler.nextReferenceLoop;
				nextReferenceLoop.unlock();
				nextReferenceLoop.undoLoopTrainScheduleDataList();

				double departureTime = stationToStationScheduler.departureTime;
				train.setDepartTime(departureTime);
			}

		}

		if (wasScheduledTillEnd) {
			// Do nothing
		} else {
			double s2sDepartureTime = stationToStationScheduler.departureTime;
			ArrayList<SimulatorTimeTableEntry> simulatorTimeTableEntries = train
					.getTimeTables();

			for (int i = 0; i < simulatorTimeTableEntries.size(); i++) {
				SimulatorTimeTableEntry simulatorTimeTableEntry = simulatorTimeTableEntries
						.get(i);
				double arrivalTime = simulatorTimeTableEntry.getArrivalTime();
				double departureTime = simulatorTimeTableEntry
						.getDepartureTime();
				if (arrivalTime >= s2sDepartureTime
						&& departureTime >= s2sDepartureTime) {
					simulatorTimeTableEntries.remove(i);
					i--;
				}
			}

			blockNumberSet = stationToStationScheduler.getBlockNumberSet();
			for (Integer blockNo : blockNumberSet) {
				Block block = simulationInstance.getBlockFromBlockNo(blockNo);
				IntervalArray intervalArray = block.getOccupy();
				for (int i = 0; i < intervalArray.size(); i++) {
					Interval interval = intervalArray.get(i);
					if (interval.getTrainNo() == train.getTrainNo()) {
						double startTime = interval.getStartTime();
						double endTime = interval.getEndTime();
						if (startTime >= s2sDepartureTime
								&& endTime >= s2sDepartureTime) {
							intervalArray.remove(i);
							i--;
						}
					}
				}
			}
		}

		double displayTime = train.getDepartTime();
		// displayTime = Math.floor(displayTime);

	}
}

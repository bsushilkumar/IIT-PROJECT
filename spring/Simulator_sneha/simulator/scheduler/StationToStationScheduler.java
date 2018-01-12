package simulator.scheduler;

import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntityList.TrainList;
import gui.entities.trainEntities.Train;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

import simulator.util.Debug;

public class StationToStationScheduler implements Comparator<Train> {
	public Train train;
	private TreeSet<Integer> blockNumberSet = new TreeSet<Integer>();
	public double departureTime;
	public Loop startLoop = null;
	public Loop nextReferenceLoop = null;
	public boolean scheduled = false;

	public StationToStationScheduler() {

	}

	public StationToStationScheduler(Train train, Loop startLoop,
			Loop nextReferenceLoop, double departureTime) {
		this.train = train;
		this.departureTime = departureTime;
		this.startLoop = startLoop;
		this.nextReferenceLoop = nextReferenceLoop;
	}

	public void print() {
		System.out.println("StationToStationScheduler:print : train "
				+ train.getTrainNo() + " startLoop " + startLoop.getBlockNo()
				+ " nextReferenceLoop " + nextReferenceLoop.getBlockNo()
				+ " departureTime " + departureTime + " blockNumberSet size "
				+ getBlockNumberSet().size());
	}

	public TreeSet<Integer> getBlockNumberSet() {
		return blockNumberSet;
	}

	public void setBlockNumberSet(TreeSet<Integer> blockNumberSet) {
		this.blockNumberSet = blockNumberSet;
	}

	public static TrainList getSortedTrainList(TrainList trainList) {
		Collections.sort((ArrayList<Train>) trainList,
				new StationToStationScheduler());
		return trainList;
	}

	public static void insertionSort(TrainList trainList, Train train) {
		StationToStationScheduler s2sScheduler = new StationToStationScheduler();

		int first = 0;
		int last = trainList.size();

		while (first != last) {
			int mid = (first + last) / 2;
//			System.out.println("first " + first + " mid " + mid + " last "
//					+ last);

			Train midTrain = trainList.get(mid);

			int compare = s2sScheduler.compare(train, midTrain);
			if (compare == -1) {
				last = mid;
			} else if (compare == 1) {
				first = mid + 1;
			} else {
				Debug.error("StationToStationScheduler: insertionSort: compare method cannot return 0");
			}
		}

		trainList.add(first, train);
		
		for (Train train2 : trainList) {
			System.out.println("insertionSort: train " + train2.getTrainNo()
					+ " actionableTime " + train2.getActionableTime());
		}

		System.out.println("TrainList size is " + trainList.size());

		System.out.println();
		
	}

	@Override
	public int compare(Train train1, Train train2) {

		boolean trainScheduledTillEnd1 = train1.isScheduledTillEnd();
		boolean trainScheduledTillEnd2 = train2.isScheduledTillEnd();
		double actionableTime1 = train1.getActionableTime();
		double actionableTime2 = train2.getActionableTime();

		// Loop nextReferenceLoop1 = train1.getNextReferenceLoop();
		// if (nextReferenceLoop1 != null)
		// actionableTime1 = nextReferenceLoop1
		// .getNextTrainToBeScheduledDepartureTime();
		//
		// Loop nextReferenceLoop2 = train2.getNextReferenceLoop();
		// if (nextReferenceLoop2 != null)
		// actionableTime2 = nextReferenceLoop2
		// .getNextTrainToBeScheduledDepartureTime();
		//
		// System.out.println("train1 " + train1.getTrainNo() + " train2 "
		// + train2.getTrainNo() + " t1 " + actionableTime1 + " t2 "
		// + actionableTime2);

		int rakeLinkNo1 = train1.getRakeLinkNo();
		int rakeLinkNo2 = train2.getRakeLinkNo();
		int trainNo1 = train1.getTrainNo();
		int trainNo2 = train2.getTrainNo();

		int value = 0;
		if ((!trainScheduledTillEnd1 && !trainScheduledTillEnd2)
				|| (trainScheduledTillEnd1 && trainScheduledTillEnd2)) {
			if (rakeLinkNo1 == trainNo2) {
				value = -1;
			} else if (rakeLinkNo2 == trainNo1) {
				value = 1;
			} else if (actionableTime1 < actionableTime2)
				value = -1;
			else if (actionableTime1 > actionableTime2) {
				value = 1;
			} else {
				if (trainNo1 < trainNo2)
					return -1;
				else
					return 1;
			}
		} else if (trainScheduledTillEnd1)
			value = 1;
		else
			value = -1;

		return value;
	}

}

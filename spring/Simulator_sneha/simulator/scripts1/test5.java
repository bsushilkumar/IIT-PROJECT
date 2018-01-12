package simulator.scripts1;


import globalVariables.FileNames;
import globalVariables.GlobalVar;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import simulator.util.Time;

public class test5 {
	static int priority = 3;
	static double acceleration = 1.8;
	static double deceleration = 1.08;
	static String operatingDays = "all";
	static int maxSpeed = 100;
	static int haltTime = 15;

	public static void main(String args[]) throws IOException {
		String scheduledfilename = FileNames.getScheduledTrainsFileName();
		String trainDiagramFileName = FileNames.getTrainDiagramFileName();

		ArrayList<Train> trainList = new ArrayList<Train>();
		HashSet<Train> upTrainList = test2.addDownTrains(scheduledfilename,
				false, trainDiagramFileName);
		HashSet<Train> downTrainList = test4.addUPTrains(true,
				scheduledfilename, trainDiagramFileName);

		trainList.addAll(downTrainList);
		trainList.addAll(upTrainList);

		int trainNos[] = { 90670, 90453, 90467, 90947, 90736, 90208, 90788,
				90868, 90092, 91154, 90935, 90010, 90925, 92024, 90200, 90326,
				90259, 90205, 91008, 90468, 90420 };
		for (int i = 0; i < trainList.size(); i++) {
			Train train = trainList.get(i);
			// if (i > 10) {
			// trainList.remove(i);
			// i--;
			// } else
			for (int j : trainNos) {
				if (j == train.trainNo) {
					trainList.remove(i);
					i--;
				}
			}
		}

		Collections.sort(trainList, new Train());
		int i = 0;
		while (i < trainList.size()) {
			int trainNo = trainList.get(i).trainNo;
			for (int j = i + 1; j < trainList.size()
					&& trainList.get(j).trainNo == trainNo;) {
				trainList.remove(j);
			}

			i++;
		}

		createRakeLinking(trainList);

		// reorderTrains(trainList);

		writeTrains(trainList, trainDiagramFileName, scheduledfilename);

	}

	private static void reorderTrains(ArrayList<Train> trainList) {
		// Collections.sort(trainList, new OrderTrains());
		for (int i = 0; i < trainList.size(); i++) {
			for (int j = i + 1; j < trainList.size(); j++) {
				Train train1 = trainList.get(i);
				Train train2 = trainList.get(j);
				int result = OrderTrains.myCompare(train1, train2);
				if (result == 1) {
					trainList.set(i, train2);
					trainList.set(j, train1);
				}
			}
		}
	}

	private static void createRakeLinking(ArrayList<Train> trainList) {
		System.out.println("trainListSize " + trainList.size());
		for (int j = 0; j < trainList.size(); j++) {
			Train train1 = trainList.get(j);
			int trainNo1 = train1.trainNo;

			if (trainNo1 % 2 == 1)
				continue;

			int rakeLinkNo = train1.rakeLinkNo;

			// System.out.println("train1 " + trainNo1 + " train2 " +
			// rakeLinkNo);
			if (rakeLinkNo == -1 || (rakeLinkNo - trainNo1) % 2 != 1) {
				continue;
			}

			System.out.println("considering: train1 " + trainNo1 + " train2 "
					+ rakeLinkNo);

			Train train2 = null;
			for (int k = j + 1; k < trainList.size(); k++) {
				if (trainList.get(k).trainNo == rakeLinkNo) {
					train2 = trainList.get(k);
					break;
				}
			}

			if (train2 == null)
				continue;

			ArrayList<Entry> entryList1 = train1.entryList;
			ArrayList<Entry> entryList2 = train2.entryList;
			Entry lastEntry = entryList1.get(entryList1.size() - 1);
			int lastDepartureTime = Time
					.getIntegerFromTimeString(lastEntry.departureTimeString);

			Entry firstEntry = entryList2.get(0);

			int lastLoopNo = lastEntry.loopNo;
			if (lastLoopNo == 0 || lastLoopNo == 28 || lastLoopNo == 55
					|| lastLoopNo == 80 || lastLoopNo == 527
					|| lastLoopNo == 526 || lastLoopNo == 530
					|| lastLoopNo == 528) {
				firstEntry.loopNo = lastLoopNo;
			} else {
				lastEntry.loopNo = firstEntry.loopNo;
			}

			String firstArrivalTimeString = firstEntry.arrivalTimeString;
			int firstArrivalTime = Time
					.getIntegerFromTimeString(firstArrivalTimeString);

			if (lastDepartureTime > firstArrivalTime - 10 * 60) {
				lastDepartureTime = firstArrivalTime - 90;

				String lastDepartureTimeString = Time
						.getTimeStringFromInteger(lastDepartureTime);
				lastEntry.departureTimeString = lastDepartureTimeString;
			} else {
				train1.rakeLinkNo = -1;
			}
		}

	}

	private static void writeTrains(ArrayList<Train> trainList,
			String trainDiagramFileName, String scheduledfilename)
			throws IOException {
		// BufferedWriter trainDiagramBW = new BufferedWriter(new FileWriter(
		// trainDiagramFileName));
		BufferedWriter trainBW = new BufferedWriter(new FileWriter(
				scheduledfilename));

		for (Train train : trainList) {
			int trainDirection = train.trainNo % 2;

			// String trainDiagramString = train.trainNo + " " + "SCHEDULED "
			// + trainDirection + " 0\n\n";
			// trainDiagramBW.write(trainDiagramString);

			String trainString = train.trainNo + " " + train.trainId + " "
					+ train.rakeLinkNo + " " + train.directionString + " "
					+ train.length + " " + train.acceleration + " "
					+ train.deceleration + " " + train.priority + " "
					+ train.maximumSpeed + " " + train.operatingDays + " "
					+ train.entryList.size() + "\t";
			trainBW.write(trainString);
			for (int i = 0; i < train.entryList.size(); i++) {
				Entry entry = train.entryList.get(i);
				trainBW.write(entry.loopNo + " " + entry.arrivalTimeString
						+ " " + entry.departureTimeString + "\t");
			}

			trainBW.write("\"" + train.returnTimeString + "\"");

			trainBW.write("\n\n");

		}

		// trainDiagramBW.close();
		trainBW.close();

	}

	protected static String getProperPlatformString(
			String previousPlatformString, int direction) {
		previousPlatformString = previousPlatformString.toUpperCase();
		if (direction == GlobalVar.UP_DIRECTION) {
			if (isThroughPlatform(previousPlatformString))
				return "downThrough";
			return "downLocal";
		} else {

			if (isThroughPlatform(previousPlatformString))
				return "upThrough";
			return "upLocal";
		}
	}

	protected static String getDisplayTimeString(String timeString) {
		String secondString = "00";
		if (timeString.contains("1/2")) {
			secondString = "30";
			timeString.substring(0, timeString.length() - 3).trim();
		}

		String[] timeTokens = timeString.split(":");
		String hourString = timeTokens[0];
		String minuteString = timeTokens[1];

		String timeDisplayString = hourString + minuteString + secondString;
		return timeDisplayString;

	}

	protected static boolean isTimeString(String timeString) {
		return timeString.contains(":");
	}

	protected static boolean isThroughPlatform(String platformId) {
		platformId = platformId.toUpperCase();
		boolean isThroughPlatform = platformId.equalsIgnoreCase("T")
				|| platformId.contains("/T") || platformId.contains("T/")
				|| platformId.equalsIgnoreCase("T/Line");

		return isThroughPlatform;
	}

	protected static boolean isLocalPlatform(String platformId) {
		platformId = platformId.toUpperCase();
		boolean isLocalPlatform = platformId.equalsIgnoreCase("L")
				|| platformId.contains("/L") || platformId.contains("L/");

		return isLocalPlatform;
	}
}

class Platform {
	String stationName;
	int loopNo;
	String platformName;

	public Platform(String stationName, String platformName, int loopNo) {
		this.stationName = stationName;
		this.platformName = platformName;
		this.loopNo = loopNo;
	}
}

class Entry {
	String arrivalTimeString;
	String departureTimeString;
	int loopNo;

	public Entry(int loopNo, String arrivalTimeString,
			String departureTimeString, int haltTime, boolean firstEntry) {
		this.loopNo = loopNo;
		int daysInSecond = 24 * 60 * 60;
		if (arrivalTimeString.isEmpty()) {
			this.departureTimeString = departureTimeString;

			int departureTime = Time
					.getIntegerFromTimeString(departureTimeString);
			if (firstEntry) {
				departureTime = (departureTime - haltTime);
			} else {
				departureTime = (departureTime - haltTime);
			}

			if (departureTime < 0)
				departureTime += daysInSecond;
			// System.out.println("departureTime " + departureTime);
			this.arrivalTimeString = Time
					.getTimeStringFromInteger(departureTime);

		} else if (departureTimeString.isEmpty()) {
			this.arrivalTimeString = arrivalTimeString;

			int arrivalTime = Time.getIntegerFromTimeString(arrivalTimeString);
			arrivalTime = arrivalTime + haltTime;
			// System.out.println("arrivalTime " + arrivalTime);
			this.departureTimeString = Time
					.getTimeStringFromInteger(arrivalTime);
		} else {
			this.arrivalTimeString = arrivalTimeString;
			this.departureTimeString = departureTimeString;
		}

	}

	static void processAllEntries(ArrayList<Entry> entryList) {
		int entryListSize = entryList.size();
		Entry firstEntry = entryList.get(0);
		Entry lastEntry = entryList.get(entryListSize - 1);

		String startTimeString = firstEntry.arrivalTimeString;
		String endTimeString = lastEntry.departureTimeString;

		int startTime = Time.getIntegerFromTimeString(startTimeString);
		int endTime = Time.getIntegerFromTimeString(endTimeString);

		int secondsInDay = 24 * 60 * 60;

		if (endTime < startTime) {
			for (Entry entry : entryList) {
				int arrivalTime = Time
						.getIntegerFromTimeString(entry.arrivalTimeString);
				int departureTime = Time
						.getIntegerFromTimeString(entry.departureTimeString);
				if (arrivalTime < 2 * 60 * 60) {
					arrivalTime = arrivalTime + secondsInDay;
					entry.arrivalTimeString = Time
							.getTimeStringFromInteger(arrivalTime);
				}

				if (departureTime < 2 * 60 * 60) {
					departureTime = departureTime + secondsInDay;
					entry.departureTimeString = Time
							.getTimeStringFromInteger(departureTime);
				}
			}
		}

		startTimeString = firstEntry.arrivalTimeString;
		endTimeString = lastEntry.departureTimeString;
		startTime = Time.getIntegerFromTimeString(startTimeString);
		endTime = Time.getIntegerFromTimeString(endTimeString);

	}
}

class Train implements Comparator<Train>, Comparable<Train> {

	public int trainNo;
	public String trainId;
	public int rakeLinkNo;
	public String directionString;
	public double length;
	public double acceleration;
	public double deceleration;
	public int priority = 3;
	public int maximumSpeed = 100;
	public String operatingDays = "all";
	public ArrayList<Entry> entryList;
	public String returnTimeString;

	public Train(int trainNo, String trainId, int rakeLinkNo,
			String directionString, double length, double acceleration,
			double deceleration, int priority, int maxSpeed,
			String operatingDays, ArrayList<Entry> entryList,
			String returnTimeString) {

		this.trainNo = trainNo;
		this.trainId = trainId;
		this.rakeLinkNo = rakeLinkNo;
		this.directionString = directionString;
		this.length = length;
		this.acceleration = acceleration;
		this.deceleration = deceleration;
		this.priority = priority;
		this.maximumSpeed = maxSpeed;
		this.operatingDays = operatingDays;
		this.entryList = entryList;
		this.returnTimeString = returnTimeString;
	}

	public Train() {

	}

	@Override
	public int compare(Train a, Train b) {
		if (a.trainNo < b.trainNo)
			return -1;
		else
			return 1;

	}

	@Override
	public int compareTo(Train b) {
		if (this.trainNo == b.trainNo)
			return 0;
		else
			return -1;
	}
}

class OrderTrains implements Comparator<Train> {

	@Override
	public int compare(Train train1, Train train2) {
		return myCompare(train1, train2);
	}

	public static int myCompare(Train train1, Train train2) {
		int result = determineOrderWithCommonLoop(train1, train2);
		if (result == 0) {
			if (train1.trainNo < train2.trainNo)
				result = -1;
			else
				result = 1;
		}

		return result;

	}

	private static int determineOrderWithCommonLoop(Train train1, Train train2) {
		System.out.println("Comparing train1 " + train1.trainNo + " train2 "
				+ train2.trainNo);
		int arrivalTime1, arrivalTime2;
		for (Entry entry1 : train1.entryList) {
			for (Entry entry2 : train2.entryList) {
				if (entry1.loopNo == entry2.loopNo) {
					arrivalTime1 = Time
							.getIntegerFromTimeString(entry1.arrivalTimeString);
					arrivalTime2 = Time
							.getIntegerFromTimeString(entry2.arrivalTimeString);

					System.out.println("loop " + entry1.loopNo + " arr1 "
							+ arrivalTime1 + " arr2 " + arrivalTime2);
					if (arrivalTime1 < arrivalTime2)
						return -1;
					else if (arrivalTime1 > arrivalTime2)
						return 1;
					else
						return 0;
				}
			}
		}

		System.out.println("Compared train1 " + train1.trainNo + " train2 "
				+ train2.trainNo);
		return 0;
	}

}
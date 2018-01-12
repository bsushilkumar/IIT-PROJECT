package simulator.input;

import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.trainEntities.Train;

import java.util.ArrayList;

import simulator.scheduler.SimulatorTimeTableEntry;

/**
 * 
 */
public class ChangeTimeTable {
	/**
	 * @param Trains
	 */

	public static void changeToMin(ArrayList<Train> Trains) {
		Object o;
		SimulatorTimeTableEntry srf;
		int i, j;
		double time, min, hrs;
		Train Train;
		for (j = 0; j < Trains.size(); j++) {
			Train = Trains.get(j);

			if (Train.getPriority() < 5) {
				for (i = 0; i < Train.getTimeTables().size(); i++) {
					o = Train.getTimeTables().get(i);
					srf = (SimulatorTimeTableEntry) o;
					time = srf.getArrivalTime();
					min = (int) (time / 100);
					hrs = time - min * 100;
					srf.setArrivalTime(min * 60 + hrs);
					time = srf.getDepartureTime();
					min = (int) (time / 100);
					hrs = time - min * 100;
					srf.setDepartureTime(min * 60 + hrs);
				}
			} else {
				time = Train.getStartTime();
				min = (int) (time / 100);
				hrs = time - min * 100;
				Train.setStartTime(min * 60 + hrs);
			}
		}
	}

	/**
	 * @param Train
	 */
	public static void changeToMin(Train Train) {
		SimulatorTimeTableEntry srf;
		int i;
		double time, min, hrs;
		if (Train.getPriority() < 5) {
			for (i = 0; i < Train.getTimeTables().size(); i++) {

				srf = Train.getTimeTables().get(i);

				time = srf.getArrivalTime();
				min = (int) (time / 100);
				hrs = time - min * 100;
				srf.setArrivalTime(min * 60 + hrs);

				time = srf.getDepartureTime();
				min = (int) (time / 100);
				hrs = time - min * 100;
				srf.setDepartureTime(min * 60 + hrs);
			}
		} else {
			time = Train.getStartTime();
			min = (int) (time / 100);
			hrs = time - min * 100;
			Train.setStartTime(min * 60 + hrs);
		}
	}

	/**
	 * @param Trains
	 */

	public static void changeToHrs(ArrayList<Train> Trains) {
		SimulatorTimeTableEntry srf;
		int i, j;
		double time, min, hrs;
		Train Train;
		for (j = 0; j < Trains.size(); j++) {
			Train = Trains.get(j);
			for (i = 0; i < Train.getTimeTables().size(); i++) {
				srf = Train.getTimeTables().get(i);
				time = srf.getArrivalTime();
				hrs = (int) (time / 60);
				min = time - hrs * 60;
				srf.setArrivalTime(hrs * 100 + min);
				time = srf.getDepartureTime();
				hrs = (int) (time / 60);
				min = time - hrs * 60;
				srf.setDepartureTime(hrs * 100 + min);
			}
		}
	}

	/**
	 * @param time
	 * @return time in minutes
	 */
	public static int changeToMin(int time) {
		int min = (time % 100);
		int hrs = time / 100;
		return (min + hrs*60);
	}

	/**
	 * @param time
	 * @return time in hrs
	 */
	public static int changeToHrs(int time) {
		int hrs = (time / 60);
		int min = time - hrs * 60;
		return (hrs * 100 + min);
	}

	/**
	 * @param Trains
	 */

	public static void changeRefToMin(ArrayList<Train> trains) {
		ReferenceTableEntry srf;
		int i, j;
		double var, var1, var2;
		Train train;
		for (j = 0; j < trains.size(); j++) {
			train = trains.get(j);

			// if (train.priority < 5 ) {
			if (train.isScheduled() == true) {
				for (i = 0; i < train.getRefTables().size(); i++) {
					srf = train.getRefTables().get(i);
					var = srf.getReferenceArrivalTime();
					var1 = (int) (var / 100);
					var2 = var - var1 * 100;
					srf.setReferenceArrivalTime(var1 * 60 + var2);
					var = srf.getReferenceDepartureTime();
					var1 = (int) (var / 100);
					var2 = var - var1 * 100;
					srf.setReferenceDepartureTime(var1 * 60 + var2);
				}
			} else {
				var = train.getStartTime();
				var1 = (int) (var / 100);
				var2 = var - var1 * 100;
				train.setStartTime(var1 * 60 + var2);
			}
		}
	}

	/**
	 * @param train
	 */
	public static void changeRefToMin(Train train) {
		ReferenceTableEntry srf;
		int i;
		int hour, minute, second, time;
		double totalMinutes;

		if (train.isScheduled() == true) {
			for (i = 0; i < train.getRefTables().size(); i++) {
				srf = train.getRefTables().get(i);

				// converting arrival time in decimal to minutes
				time = (int) srf.getReferenceArrivalTime();
				second = time % 100;
				minute = (time / 100) % 100;
				hour = time / 100 / 100;

				totalMinutes = minute + (second / 60.0);
				double arrivalTime = hour * 60 + totalMinutes;
				srf.setReferenceArrivalTime(arrivalTime);

				// changing departure time in decimal to minutes
				time = (int) srf.getReferenceDepartureTime();
				second = time % 100;
				minute = (time / 100) % 100;
				hour = time / 100 / 100;

				totalMinutes = minute + (second / 60.0);
				double departureTime = hour * 60 + totalMinutes;
//				System.out.println("time " + time + " hour " + hour
//						+ " minute " + minute + " second " + second
//						+ " totalMinutes " + totalMinutes);
				srf.setReferenceDepartureTime(departureTime);
//				System.out.println("trainno " + train.getTrainNo() + " arr "
//						+ arrivalTime + " dep " + departureTime);
			}
		} else {
			time = (int) train.getStartTime();
			second = time % 100;
			minute = (time / 100) % 100;
			hour = time / 100 / 100;
			totalMinutes = minute + (second / 60.0);

			train.setStartTime(hour * 60 + totalMinutes);
		}
	}

	/**
	 * @param Trains
	 */

	public static void changeRefToHrs(ArrayList<Train> trains) {
		ReferenceTableEntry srf;
		int i, j;
		double var, var1, var2;
		Train train;
		for (j = 0; j < trains.size(); j++) {
			train = trains.get(j);
			for (i = 0; i < train.getRefTables().size(); i++) {
				srf = train.getRefTables().get(i);
				var = srf.getReferenceArrivalTime();
				var1 = (int) (var / 60);
				var2 = var - var1 * 60;
				srf.setReferenceArrivalTime(var1 * 100 + var2);
				var = srf.getReferenceDepartureTime();
				var1 = (int) (var / 60);
				var2 = var1 * 60 - var;
				srf.setReferenceDepartureTime(var1 * 100 + var2);
			}
		}
	}
	
	public static String getTimeInHHMMformat(int startTimeInput) {
		if (startTimeInput == 0) {
			return "0000";
		} else if (startTimeInput / 10 == 0) {
			return "000" + startTimeInput;
		} else if (startTimeInput / 100 == 0) {
			return "00" + startTimeInput;
		} else if (startTimeInput / 1000 == 0) {
			return "0" + startTimeInput;
		} else
			return startTimeInput + "";

	}
	public static String getTimeInHHMMSSformat(int startTimeInput) {
		if (startTimeInput == 0) {
			return "000000";
		} else if (startTimeInput / 10 == 0) {
			return "00000" + startTimeInput;
		} else if (startTimeInput / 100 == 0) {
			return "0000" + startTimeInput;
		} else if (startTimeInput / 1000 == 0) {
			return "000" + startTimeInput;
		} else if (startTimeInput / 10000 == 0) {
			return "00" + startTimeInput;
		} else if (startTimeInput / 100000 == 0) {
			return "0" + startTimeInput;
		} else
			return startTimeInput + "";

	}

	
	
}
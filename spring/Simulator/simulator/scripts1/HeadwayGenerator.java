package simulator.scripts1;



import globalVariables.FileNames;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import simulator.util.Time;

public class HeadwayGenerator {
	public static String upString = "	A 10001 up	0.258	1.8	1.08	3	65	all	21	";
	public static String downString = "	A 10001 down	0.258	1.8	1.08	3	65	all	21	";

	public static void main(String args[]) throws IOException {

		int headway = 137;
		int haltTime = 20;
		int clearTime = 40;
		int nLoops = 15;
		int nTrains = 30;
		writeUpTrains(headway, haltTime, nLoops, nTrains, clearTime);
		// int trainNo = 10000;
		//
		// int nTrains = 10;
		// BufferedWriter bw = new BufferedWriter(new FileWriter(
		// FileNames.getScheduledTrainsFileName()));
		// int upStartTime = 57 * 60, downStartTime = 0;
		// int headway = 150;
		// int haltTime = 25;
		// int time;
		// int nGroups = 20;
		//
		// String upTrainString, downTrainString;
		//
		// downTrainString = getDownTrainString(trainNo, downStartTime,
		// haltTime);
		// bw.write(downTrainString);
		// downStartTime += headway;
		//
		// trainNo += 2;
		// downTrainString = getDownTrainString(trainNo, downStartTime,
		// haltTime);
		// bw.write(downTrainString);
		// downStartTime += headway;
		//
		// for (int i = 0; i < nGroups - 1; i++) {
		// trainNo--;
		// upTrainString = getUpTrainString(trainNo, upStartTime, haltTime);
		// bw.write(upTrainString);
		// upStartTime += headway;
		//
		// trainNo += 3;
		// downTrainString = getDownTrainString(trainNo, downStartTime,
		// haltTime);
		// bw.write(downTrainString);
		// downStartTime += headway;
		//
		// trainNo--;
		// upTrainString = getUpTrainString(trainNo, upStartTime, haltTime);
		// bw.write(upTrainString);
		// upStartTime += headway;
		//
		// trainNo += 3;
		// downTrainString = getDownTrainString(trainNo, downStartTime,
		// haltTime);
		// bw.write(downTrainString);
		// downStartTime += headway;
		// }
		//
		// trainNo--;
		// upTrainString = getUpTrainString(trainNo, upStartTime, haltTime);
		// bw.write(upTrainString);
		// upStartTime += headway;
		//
		// trainNo += 2;
		// upTrainString = getUpTrainString(trainNo, upStartTime, haltTime);
		// bw.write(upTrainString);
		//
		// // for (int i = 0; i < nTrains; i++, trainNo++, upStartTime +=
		// headway)
		// // {
		// // String upTrainString = getUpTrainString(trainNo, upStartTime,
		// // haltTime);
		// // bw.write(upTrainString);
		// // }
		// //
		// // trainNo = 20000;
		// // for (int i = 0; i < nTrains; i++, trainNo++, upStartTime +=
		// headway)
		// // {
		// // String downTrainString = getDownTrainString(trainNo, upStartTime,
		// // haltTime);
		// // bw.write(downTrainString);
		// // }
		//
		// bw.close();
	}

	private static void writeUpTrains(int headway, int haltTime, int nLoops,
			int nTrains, int clearTime) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				FileNames.getScheduledTrainsFileName()));

		int velocity = 72;
		double acceleration = 0.9;
		double deceleration = 1.8;

		String upTrainString1 = "	A 10001 up	0.258	" + acceleration + " "
				+ deceleration + "	3	" + velocity + "	all ";
		String haltTimeString = Time.getTimeStringFromInteger(0) + " "
				+ Time.getTimeStringFromInteger(haltTime) + "\t";

		int startTime = 0;
		int trainNo = 10000;
		for (int i = 0; i < nTrains; i++, startTime += headway) {
			String trainString = trainNo++ + upTrainString1 + nLoops + "\t";
			String startTimeString = Time.getTimeStringFromInteger(startTime)
					+ " " + Time.getTimeStringFromInteger(startTime + haltTime)
					+ "\t";
			trainString += "0 " + startTimeString;

			for (int j = 1; j < nLoops; j++) {
				if (j == nLoops - 1) {
					String haltTimeString2 = Time.getTimeStringFromInteger(0)
							+ " "
							+ Time.getTimeStringFromInteger(haltTime
									+ clearTime) + "\t";
					trainString += j + " " + haltTimeString2;
				} else {
					trainString += j + " " + haltTimeString;
				}
			}

			trainString += "\"\"\n\n";
			bw.write(trainString);
		}

		bw.close();
	}

	private static String getUpTrainString(int trainNo, int startTime,
			int haltTime) {
		String upTrainString = trainNo + upString;
		int time = startTime;
		for (int j = 0; j < 21; j++, time += j) {
			int loopNo = j;
			if (j == 0) {
				if ((trainNo - 1) % 4 == 0) {
					loopNo = 100;
				}
			}
			upTrainString += " " + loopNo + " "
					+ Time.getTimeStringFromInteger(time) + " "
					+ Time.getTimeStringFromInteger(time + haltTime);

		}

		upTrainString += " \"\"\n\n";

		return upTrainString;
	}

	private static String getDownTrainString(int trainNo, int startTime,
			int haltTime) {
		String downTrainString = trainNo + downString;
		int time = startTime;

		for (int j = 0; j < 21; j++, time += j) {
			int loopNo = 120 - j;
			if (j == 20) {
				if (trainNo % 4 != 0) {
					loopNo = 0;
				}
			}
			downTrainString += " " + loopNo + " "
					+ Time.getTimeStringFromInteger(time) + " "
					+ Time.getTimeStringFromInteger(time + haltTime);

		}

		downTrainString += " \"\"\n\n";

		return downTrainString;
	}
}

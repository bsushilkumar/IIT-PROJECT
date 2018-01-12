package simulator.scripts1;


import globalVariables.FileNames;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import simulator.util.Time;

public class TerminalHeadwayGenerator {
	static int trainNo, platformLoop1 = 0, platformLoop2 = 28;
	static int headway = 151, haltTime = 25, terminalHaltTime = 30;
	static double acceleration = 1.8, deceleration = 0.9;

	static String zeroTimeString = Time.getTimeStringFromInteger(0);
	static String haltTimeString = Time.getTimeStringFromInteger(haltTime);
	static String downAttr1 = " A -1 down 0.258 1.8 0.9 3 100 all 5	30 ";
	static String downAttr2 = "	53 " + zeroTimeString + " " + haltTimeString
			+ "	52 " + zeroTimeString + " " + haltTimeString + "	29 "
			+ zeroTimeString + " " + haltTimeString + "	";
	static String downAttr3 = " " + zeroTimeString + " 000030  \"\"\n\n";

	static String upAttr1 = " A -1 up 0.258 1.8 0.9 3 100 all 5	";
	static String upAttr2 = "	1 " + zeroTimeString + " " + haltTimeString
			+ "	2 " + zeroTimeString + " " + haltTimeString + "	3 "
			+ zeroTimeString + " " + haltTimeString + "	4 " + zeroTimeString
			+ " " + haltTimeString + " \"\"\n\n";

	static String downAttr11 = " A -1 down 0.258 1.8 0.9 3 100 all 3	16 ";
	static String downAttr21 = "	13 " + zeroTimeString + " " + haltTimeString
			+ "	";
	static String downAttr31 = " " + zeroTimeString + " 000001  \"\"\n\n";

	static String upAttr11 = " A -1 up 0.258 1.8 0.9 3 100 all 3	";
	static String upAttr21 = "	3 " + zeroTimeString + " " + haltTimeString
			+ "	6 " + zeroTimeString + " " + haltTimeString + " \"\"\n\n";

	static int nGroups = 20;

	public static void main(String args[]) throws IOException {

		int downStartTime = 0 - headway;
		int upStartTime = 5 * 60 - headway;

		trainNo = 9999;

		BufferedWriter scheduledBW = new BufferedWriter(new FileWriter(
				FileNames.getScheduledTrainsFileName()));
		BufferedWriter trainDiagramBW = new BufferedWriter(new FileWriter(
				FileNames.getTrainDiagramFileName()));

		scheduledBW.write(getDownTrainString((downStartTime += headway),
				platformLoop1));
		trainDiagramBW.write(trainNo + " SCHEDULED 1 0\n\n");
		scheduledBW.write(getDownTrainString((downStartTime += headway),
				platformLoop2));
		trainDiagramBW.write(trainNo + " SCHEDULED 1 0\n\n");

		for (int i = 0; i < nGroups - 1; i++) {
			scheduledBW.write(getUpTrainString((upStartTime += headway),
					platformLoop1));
			trainDiagramBW.write(trainNo + " SCHEDULED 0 0\n\n");
			scheduledBW.write(getDownTrainString((downStartTime += headway),
					platformLoop1));
			trainDiagramBW.write(trainNo + " SCHEDULED 1 0\n\n");
			scheduledBW.write(getUpTrainString((upStartTime += headway),
					platformLoop2));
			trainDiagramBW.write(trainNo + " SCHEDULED 0 0\n\n");
			scheduledBW.write(getDownTrainString((downStartTime += headway),
					platformLoop2));
			trainDiagramBW.write(trainNo + " SCHEDULED 1 0\n\n");
		}

		scheduledBW.write(getUpTrainString((upStartTime += headway),
				platformLoop1));
		trainDiagramBW.write(trainNo + " SCHEDULED 0 0\n\n");
		scheduledBW.write(getUpTrainString((upStartTime += headway),
				platformLoop2));
		trainDiagramBW.write(trainNo + " SCHEDULED 0 0\n\n");

		scheduledBW.close();
		trainDiagramBW.close();

	}

//	static String getDownTrainString(int downStartTime, int platformLoop) {
//		String downTrainString = "" + ++trainNo + downAttr1
//				+ Time.getTimeStringFromInteger(downStartTime) + " "
//				+ Time.getTimeStringFromInteger(downStartTime + haltTime)
//				+ downAttr2 + platformLoop + downAttr3;
//		return downTrainString;
//	}
//
//	static String getUpTrainString(int upStartTime, int platformLoop) {
//		String upTrainString = "" + ++trainNo + upAttr1 + platformLoop + " "
//				+ Time.getTimeStringFromInteger(upStartTime) + " "
//				+ Time.getTimeStringFromInteger(upStartTime + haltTime)
//				+ upAttr2;
//		return upTrainString;
//	}
	
	static String getDownTrainString(int downStartTime, int platformLoop) {
		String downTrainString = "" + ++trainNo + downAttr11
				+ Time.getTimeStringFromInteger(downStartTime) + " "
				+ Time.getTimeStringFromInteger(downStartTime + haltTime)
				+ downAttr21 + platformLoop + downAttr31;
		return downTrainString;
	}

	static String getUpTrainString(int upStartTime, int platformLoop) {
		String upTrainString = "" + ++trainNo + upAttr11 + platformLoop + " "
				+ Time.getTimeStringFromInteger(upStartTime) + " "
				+ Time.getTimeStringFromInteger(upStartTime + haltTime)
				+ upAttr21;
		return upTrainString;
	}
}

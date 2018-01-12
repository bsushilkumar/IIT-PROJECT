package files;

import globalVariables.FileNames;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;

public class Parameters {
	public static int simulationTime = 1;
	public static int delayFactor = 0;
	public static int blockWorkingTime = 0;
	public static int noOfColours = 4;
	public static int redFailWaitTime = 1;
	public static int redFailVelocity = 18;
	public static double warnerDistance = 0;

	private static final String formatString = "/* simulationTime delayFactor "
			+ "blockWorkingTime noOfColor redFailWaitTime redFailVelocity warnerDistance */";

	public static void setSimulationTime(int simulationTime) {
		Parameters.simulationTime = simulationTime;
	}

	public static int getSimulationTime() {
		return simulationTime;
	}

	public static void setDelayFactor(int delayFactor) {
		Parameters.delayFactor = delayFactor;
	}

	public static int getDelayFactor() {
		return delayFactor;
	}

	public static void setBlockWorkingTime(int blockWorkingTime) {
		Parameters.blockWorkingTime = blockWorkingTime;
	}

	public static int getBlockWorkingTime() {
		return blockWorkingTime;
	}

	public static void setRedFailWaitTime(int redFailWaitTime) {
		Parameters.redFailWaitTime = redFailWaitTime;
	}

	public static int getRedFailWaitTime() {
		return redFailWaitTime;
	}

	public static void setRedFailVelocity(int redFailVelocity) {
		Parameters.redFailVelocity = redFailVelocity;
	}

	public static int getRedFailVelocity() {
		return redFailVelocity;
	}

	public static void setWarnerDistance(double warnerDistance) {
		Parameters.warnerDistance = warnerDistance;
	}

	public static double getWarnerDistance() {
		return warnerDistance;
	}

	public static void setNoOfColours(int noOfColours) {
		Parameters.noOfColours = noOfColours;
	}

	public static int getNoOfColours() {
		return noOfColours;
	}

	public static void readParametersFile() throws IOException {
		String fileName = FileNames.getParametersFileName();
		StreamTokenizer streamTokenizer = new StreamTokenizer(new FileReader(
				fileName));
		streamTokenizer.slashSlashComments(true);
		streamTokenizer.slashStarComments(true);

		streamTokenizer.nextToken();
		setSimulationTime((int) streamTokenizer.nval);

		streamTokenizer.nextToken();
		setDelayFactor((int) streamTokenizer.nval);

		streamTokenizer.nextToken();
		setBlockWorkingTime((int) streamTokenizer.nval);

		streamTokenizer.nextToken();
		setNoOfColours((int) streamTokenizer.nval);

		streamTokenizer.nextToken();
		setRedFailWaitTime((int) streamTokenizer.nval);

		streamTokenizer.nextToken();
		setRedFailVelocity((int) streamTokenizer.nval);

		streamTokenizer.nextToken();
		setWarnerDistance(streamTokenizer.nval);
	}

	public static void writeParametersFile() throws IOException {
		String fileName = FileNames.getParametersFileName();
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		bw.write(getSimulationTime() + " ");
		bw.write(getDelayFactor() + " ");
		bw.write(getBlockWorkingTime() + " ");
		bw.write(getNoOfColours() + " ");
		bw.write(getRedFailWaitTime() + " ");
		bw.write(getRedFailVelocity() + " ");
		bw.write(getWarnerDistance() + "\n");
		bw.write(formatString);
		bw.close();
	}

	public static void printParameters() {
		System.out.println("Printing parameters");
		System.out.println("Simulation Time: " + getSimulationTime());
		System.out.println("Delay Factor: " + getDelayFactor());
		System.out.println("Block Working Time: " + getBlockWorkingTime());
		System.out.println("Number Of Colours: " + getNoOfColours());
		System.out.println("Red Fail Wait Time: " + getRedFailWaitTime());
		System.out.println("Red Fail Velocity: " + getRedFailVelocity());
		System.out.println("Warner Distance: " + getWarnerDistance());
	}
}

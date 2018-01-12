package gui.entities.sectionEntities.time;

import globalVariables.FileNames;
import gui.entities.sectionEntityList.PassengerDelayFormatList;
import gui.entities.sectionEntityList.TrainList;
import gui.entities.trainEntities.Train;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

import simulator.input.SimulationInstance;
import simulator.scheduler.SimulationStart;
import simulator.util.Debug;

/**
 * Class PassengerDelay. File to read the format of delay.
 */

@SuppressWarnings("serial")
public class PassengerDelay extends ArrayList<PassengerDelayFormat> {

	/**
	 * constructor
	 */
	public PassengerDelay() {
	}

	/**
	 * Read the delay.
	 * 
	 * @return {@link ArrayList} of the delay.
	 */
	public static PassengerDelayFormatList readDelay() {
		Debug.print("I am in passenger train delay ");

		PassengerDelayFormatList delay = new PassengerDelayFormatList();

		try {
			Reader r = new FileReader(FileNames.getPassengerDelayFileName());
			StreamTokenizer streamTokenizer = new StreamTokenizer(r);
			streamTokenizer.whitespaceChars(0, 3);

			while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {

				if (streamTokenizer.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("Error in format of input file : passengerdelay....");
					Debug.print("Error : train priority");
				}

				double trainPriority = (double) streamTokenizer.nval;
				Debug.print("value read" + streamTokenizer.nval);
				streamTokenizer.nextToken();

				if (streamTokenizer.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("Error in format of input file : passenferdelay...");
					Debug.print("Error : % delay expected");
				}

				Debug.print("value read is " + streamTokenizer.nval);
				float percentDelay = (float) streamTokenizer.nval;
				streamTokenizer.nextToken();

				if (streamTokenizer.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("Error in format of input file : passengerdelay..");
					Debug.print("Error : mean delay  expected");
				}

				Debug.print("value read is " + streamTokenizer.nval);
				double meanDelay = streamTokenizer.nval;
				Debug.print(" delay size is  " + delay.size());

				PassengerDelayFormat pass = new PassengerDelayFormat(
						trainPriority, percentDelay, meanDelay);
				delay.add(pass);
			}
		}

		catch (IOException e) {
			Debug.print("*********I am iN delay File not Found *******");
		}

		for (int i = 0; i < delay.size(); i++) {
			PassengerDelayFormat passengerDelayFormat = delay.get(i);
			Debug.print(passengerDelayFormat.trainPriority + "  "
					+ passengerDelayFormat.percentDelay + "  "
					+ passengerDelayFormat.meanDelay);
		}
		return delay;
	}

	public static void setDelayParametersForTrains(
			TrainList trainArray,SimulationInstance simulationInstance) {
		// delay module start
		// find total no. of train for a given priority
		Train trainDelayed;
		int totalTrains[] = new int[1000];// GlobalVar.Delay.size()]; //array
		// for storing no. of trains for a
		// given priority
		int delayedTrains[] = new int[1000];// GlobalVar.Delay.size()];
		int check[] = new int[1000];// arrayTrain.size()];
		int check1[] = new int[1000];

		try {
			OutputStream f1 = new FileOutputStream("Delayfile.txt");
			PrintStream aPrintStream = new PrintStream(f1);
			aPrintStream.println("Delayed Train NO." + "         "
					+ "priority" + "        " + "Delay(min)"
					+ "                 " + "LoopNos.");
			trainDelayed = null;
			PassengerDelayFormatList passengerDelayFormatList = simulationInstance.getPassengerDelayList();
			
			if (simulationInstance.delay == 1) {
				for (int i = 0; i < passengerDelayFormatList.size(); i++) {
					for (int p = 0; p < trainArray.size(); p++)
						if (passengerDelayFormatList.get(i).trainPriority == trainArray
								.get(p).getPriority())
							totalTrains[i]++;
					System.out.println("total trains of type "
							+ passengerDelayFormatList.get(i).trainPriority
							+ "are " + totalTrains[i]);

				}
				// find total no. of train to be delayed for a given priority

				for (int i = 0; i < passengerDelayFormatList.size(); i++) {
					if (passengerDelayFormatList.get(i).meanDelay > 0) {
						float s = passengerDelayFormatList.get(i).percentDelay;
						delayedTrains[i] = (int) s * totalTrains[i] / 100;
					}
				}
				// select trains randomly

				for (int i = 0; i < passengerDelayFormatList.size(); i++) {
					outer: for (int jj = 0; jj < delayedTrains[i]; jj++) {
						int randomTrain = SimulationStart.randomGenerator(totalTrains[i]);
						check[jj] = randomTrain;
						for (int k = 0; k <= jj - 1; k++)
							// check to avoid repeatation of same train
							if (randomTrain == check[k]) {
								jj = jj - 1;
								continue outer;
							}

						for (int k = 0; k < trainArray.size(); k++) {

							if (passengerDelayFormatList.get(i).trainPriority == trainArray
									.get(k).getPriority()) {
								trainDelayed = trainArray
										.get(k + check[jj] - 1);
								System.out.println("the train seclected is"
										+ trainDelayed.getTrainNo());
								aPrintStream.print(trainDelayed.getTrainNo()
										+ "                      ");
								aPrintStream.print(trainDelayed.getPriority()
										+ "                     ");
								break;
							}
						}
						if (trainDelayed == null) {
							throw new NullPointerException(
									"SimulationStart: trainDelayed is null");
						}
						
						ArrayList<ReferenceTableEntry> refTables = trainDelayed.getRefTables();
						int siz4 = refTables.size();
						System.out.println("total no of staion in section"
								+ siz4);

						int randomNoStn = SimulationStart.randomGenerator(siz4);
						System.out.println(" no of staion to be delayed"
								+ randomNoStn);
						double delayTime = passengerDelayFormatList.get(i).meanDelay
								/ randomNoStn;
						System.out.println("delay time  " + delayTime);

						aPrintStream.print((float) delayTime
								+ "                   ");
						outer1: for (int l = 0; l < randomNoStn; l++) {
							int randomStn = SimulationStart.randomGenerator(siz4);

							check1[l] = randomStn;
							for (int k = 0; k <= l - 1; k++) { // check to avoid
								// repeatation of
								// same station
								if (randomStn == check1[k]) {
									l = l - 1;
									continue outer1;
								}
							}
							System.out.println("delayed at Stn   " + randomStn);

							System.out.println("ref dep time before delay  "
									+ ((ReferenceTableEntry) refTables
											.get(randomStn - 1))
											.getReferenceDepartureTime());
							System.out.println(" delay at loop nn  "
									+ ((ReferenceTableEntry) refTables
											.get(randomStn - 1))
											.getReferenceLoopNo());
							aPrintStream.print(((ReferenceTableEntry) refTables
									.get(randomStn - 1)).getReferenceLoopNo()
									+ ",");
							((ReferenceTableEntry) refTables.get(randomStn - 1))
									.setReferenceDepartureTime(((ReferenceTableEntry) refTables
											.get(randomStn - 1))
											.getReferenceDepartureTime()
											+ delayTime);
							System.out.println("ref dep time is now  "
									+ ((ReferenceTableEntry) refTables
											.get(randomStn - 1))
											.getReferenceDepartureTime());
							for (int m = randomStn; m < refTables.size(); m++) {
								((ReferenceTableEntry) refTables.get(m))
										.setReferenceArrivalTime(((ReferenceTableEntry) refTables
												.get(m))
												.getReferenceArrivalTime()
												+ delayTime);
								((ReferenceTableEntry) refTables.get(m))
										.setReferenceDepartureTime(((ReferenceTableEntry) refTables
												.get(m))
												.getReferenceDepartureTime()
												+ delayTime);
							}
						}

						aPrintStream.println(" ");
					}
				}

			}

		} catch (Exception e1) {
			Debug.print("start: Error in handling o/p file ");
			return;
		}
	}
}

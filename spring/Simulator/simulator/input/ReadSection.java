package simulator.input;

import globalVariables.FileNames;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntityList.HashBlockTable;
import gui.entities.sectionEntityList.LoopList;
import gui.entities.sectionEntityList.StationList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

import simulator.scheduler.HashBlockEntry;
import simulator.scheduler.SimulatorException;
import simulator.util.Debug;

/**
 * read section
 */
public class ReadSection {
	/**
	 * @return list of stations
	 * @throws SimulatorException
	 */
	public static StationList readStations() throws SimulatorException {
		StationList arrayStn = new StationList();

		Station currStn;

		try {
			Reader reader;

			// Devendra
			reader = new FileReader(FileNames.getStationFileName());
			StreamTokenizer st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(true);
			st.slashStarComments(true);

			while (st.nextToken() != StreamTokenizer.TT_EOF) {

				double startMileP, endMileP, crossVel;
				String name;
				// if (st.ttype != StreamTokenizer.TT_WORD) {
				// Debug.print("readStation: Error in format of input file : station.dat......");
				// Debug.print("readStation: Error : station name expected");
				// throw new SimulatorException();
				// }

				Debug.print("readStation: value read is " + st.sval);
				name = st.sval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("readStation: Error in format of input file : station.dat......");
					Debug.print("readStation: Error : start mile post expected");
					throw new SimulatorException();
				}

				Debug.print("readStation: value read is " + st.nval);
				startMileP = st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("readStation: Error in format of input file : station.dat......");
					Debug.print("readStation: Error : end mile post expected");
					throw new SimulatorException();
				}

				Debug.print("readStation: value read is " + st.nval);
				endMileP = st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("readStation: Error in format of input file : station.dat......");
					Debug.print("readStation: Error : cross over velocity in km/hr  expected");
					throw new SimulatorException();
				}
				Debug.print("readStation: value read is " + st.nval);
				crossVel = st.nval / 60;

				currStn = new Station(startMileP, endMileP, crossVel, name);
				arrayStn.add(currStn);
			}
		} catch (FileNotFoundException e) {
			Debug.print("readStation: FileNotFound");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (arrayStn);
	}

	/**
	 * @return list of sections
	 * @throws SimulatorException
	 */

	public static LoopList readSection(HashBlockTable hashBlockTable,
			StationList stationList) throws SimulatorException {// ///////////////////

		LoopList arrayLoop = new LoopList();
		Reader reader;
		StreamTokenizer st;
		try {

			// Devendra
			reader = new FileReader(FileNames.getBlockFileName());

			st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(true);
			st.slashStarComments(true);

			Debug.print("readSection: Now will read all blocks");

			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				HashBlockEntry hashBlockEntry = HashBlockEntry
						.getHashBlockEntryForBlock(st);
				hashBlockTable.put(new Integer(hashBlockEntry.getBlock()
						.getBlockNo()), hashBlockEntry);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// Devendra
			reader = new FileReader(FileNames.getLoopFileName());

			st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(true);
			st.slashStarComments(true);

			Debug.print("readSection: Now will read all loops");
			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				HashBlockEntry hashBlockEntry = HashBlockEntry
						.getHashBlockEntryForLoop(st, stationList);

				Loop loop = (Loop) hashBlockEntry.getBlock();
				hashBlockTable.put(new Integer(loop.getBlockNo()),
						hashBlockEntry);
				arrayLoop.add(loop);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (arrayLoop);// ////////////////
	}

	/**
	 * @param arrayStn
	 */
	public static void linkStations(StationList arrayStn) {
		Debug.print("ReadSection: linkBlocks: ");
		Station stn, prevStn = null;

		// ArrayList blkSect;
		// ArrayList loopArray;
		// Block prevBlock=null,blk;
		// Loop loop,mainLoop=null;
		// for up block sections
		for (int i = 0; i < arrayStn.size(); i++) {

			stn = arrayStn.get(i);
			Debug.print("ReadSection: linkBlocks: " + stn.getStationName());

			if (i == arrayStn.size()) {
				stn.nextStation = null;
			}

			// assign previous and next stations........
			if (prevStn != null) {
				prevStn.nextStation = stn;
				stn.previousStation = prevStn;
			} else {
				stn.previousStation = null;
			}
			prevStn = stn;
		}
	}

	/**
	 * read parameters
	 */
	public static void readParameters(SimulationInstance simulationInstance) {
		int delayFactor = 0;
		double redFailVelocity = 0;
		int numberOfSignalAspects = 0, redFailWaitTime = 0;
		// Block block ;
		double blockWorkingTime = 0; // BlockWorking Time
		// int SimulationTime; // simulation time

		try {
			Reader reader;

			// Devendra
			reader = new FileReader(FileNames.getParametersFileName());

			StreamTokenizer st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(false);
			st.slashStarComments(true);

			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("readParameters: Error in format of input file : param.dat......");
					Debug.print("readParamaters: Error : Simulation Time expected");
				}

				Debug.print("value read is " + st.nval);
				simulationInstance.simulationTime = (int) st.nval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("readParameters: Error in format of input file : param.dat......");
					Debug.print("readParameters: Error : Delay  Factor expected");
				}
				Debug.print("readParameters: value read is " + st.nval);
				delayFactor = (int) st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("readParameters: Error in format of input file : param.dat......");
					Debug.print("readParameters: Error : Block Working Time expected");
				}

				Debug.print("readParameters: value read is " + st.nval);
				blockWorkingTime = st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("readParameters: Error in format of input file : param.dat......");
					Debug.print("readParameters: Error : No Of Color expected");
				}
				Debug.print("readParameters: value read is " + st.nval);
				numberOfSignalAspects = (int) st.nval;

				simulationInstance.numberOfSignalAspects = numberOfSignalAspects;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("readParameters: Error in format of input file : param.dat......");
					Debug.print("readParameters: Error : Red Fail Wait Time expected");
				}

				Debug.print("readParameters: value read is " + st.nval);
				redFailWaitTime = (int) st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("readParameters: Error in format of input file : param.dat......");
					Debug.print("readParameters: Error : Red Fail Velocity expected");
				}
				Debug.print("readParamters: value read is " + st.nval);
				redFailVelocity = st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.print("readParameters: Error in format of input file : param.dat......");
					Debug.print("readParameters: Error : warned distance expected");
				}
				Debug.print("readParameters: value read is " + st.nval);
				simulationInstance.warnerDistance = st.nval;
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			Debug.print("readParamters: FileNotFound param.dat");
		} catch (Exception e) {

			e.printStackTrace();
			Debug.print("readParamters: IO Error");
		}

		simulationInstance.delayFactor = delayFactor;

		simulationInstance.numberOfSignalAspects = numberOfSignalAspects;
		simulationInstance.redFailVelocity = redFailVelocity / 60;
		simulationInstance.redFailWaitTime = redFailWaitTime;
		simulationInstance.blockWorkingTime = blockWorkingTime;
	}

	/**
	 * @param filepath
	 * @param testCaseDirectory
	 */
	public static void readFiles(String filepath, String testCaseDirectory,
			SimulationInstance simulationInstance) {
		try {
			Reader reader = new FileReader(filepath);
			StreamTokenizer st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(false);
			st.slashStarComments(false);

			// st.nextToken();
			FileNames.setTestCaseDirectory(testCaseDirectory);

			while (st.nextToken() != StreamTokenizer.TT_EOF) {

				String filename = st.sval;

				if (filename.contains("param")) {
					FileNames.setParametersFileName(filename);
				} else if (filename.contains("block")) {
					FileNames.setBlockFileName(filename);
				} else if (filename.contains("loop")) {
					FileNames.setLoopFileName(filename);
				} else if (filename.contains("station")) {
					FileNames.setStationFileName(filename);
				} else if (filename.contains("unscheduled")) {
					FileNames.setUnscheduledTrainsFileName(filename);
				} else if (filename.contains("scheduled")) {
					FileNames.setScheduledTrainsFileName(filename);
				} else if (filename.contains("gradientEffect")) {
					FileNames.setGradientEffectsFileName(filename);
				} else if (filename.contains("BlockDirectionInInterval")) {
					simulationInstance.hasBlockDirectionFile = true;
					FileNames.setBlockDirectionFileName(filename);
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

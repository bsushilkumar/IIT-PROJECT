package gui.entities.trainEntities;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import globalVariables.GlobalVariables;
import gui.diagramEntities.trainDiagrams.VelocityProfile;
import gui.entities.sectionEntities.Entity;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntities.trackEntities.Route;
import gui.entities.sectionEntityList.BlockList;
import gui.entities.sectionEntityList.LoopList;
import gui.entities.sectionEntityList.StationList;
import gui.entities.sectionEntityList.TrainList;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import simulator.input.ChangeTimeTable;
import simulator.input.SimulationInstance;
import simulator.scheduler.SimulatorException;
import simulator.scheduler.SimulatorTimeTableEntry;
import simulator.scheduler.StationToStationScheduler;
import simulator.util.Debug;
import simulator.velocityProfileCalculation.VelocityProfileArray;

public abstract  class Train extends Entity implements
		Comparator<SimulatorTimeTableEntry> {
	public int startTimeInput = 0;

	protected static final String allOperatingDays = "all";
	public String operatingDays = allOperatingDays;
	
	public String trainId = "T-";

	public int direction = GlobalVar.UP_DIRECTION;

	public int numberofhalts ;

	protected final static double defaultLength = 0.3;
	public double length = defaultLength;

	protected final static double defaultAcceleration = 0.35;
	protected double acceleration = defaultAcceleration;
	protected final static double defaultDeceleration = 0.35;
	protected double deceleration = defaultDeceleration;

	public double priority = 5;

	protected final static int defaultVelocity = 60;
	protected int velocity = defaultVelocity;

	public boolean scheduled = false;

	/**
	 * maximumPossibleSpeed
	 */
	public double maximumPossibleSpeed;
	/**
	 * BS
	 */
	public double bookedSpeed;
	/**
	 * acceleration
	 */
	public double accParam;
	/**
	 * deceleration
	 */
	public double deceParam;
	/**
	 * trainNo
	 */
	public int trainNo;
	/**
	 * startTime
	 */
	public double startTime;
	/**
	 * departTime
	 */
	public double departTime;
	/**
	 * startLoopNo
	 */
	public int startLoopNo;
	/**
	 * endLoopNo
	 */
	public int endLoopNo;

	/**
	 * endStation
	 */
	private String endStation;
	/**
	 * drawColour
	 */
	private Color drawColour;

	/**
	 * timeTables
	 */

	private ArrayList<SimulatorTimeTableEntry> timeTables = new ArrayList<SimulatorTimeTableEntry>();

	/**
	 * refTables
	 */
	public ArrayList<ReferenceTableEntry> refTables = new ArrayList<ReferenceTableEntry>();// KJD
	public ArrayList<ReferenceTableEntry> tempRefTables = new ArrayList<ReferenceTableEntry>();// sneha

	/**
	 * signalFailedBlocks
	 */
	// private int signalFailedBlocks[] = new int[GlobalVar
	// .getSimulationInstance().getNumberOfBlocks()];
	private int signalFailedBlocks[] = new int[100];

	/**
	 * signalFailCounter
	 */
	private int signalFailCounter = 0;

	// private int nextReferenceLoopNumber = -1;
	public Stack<StationToStationScheduler> stationToStationSchedulerStack = new Stack<StationToStationScheduler>();

	private boolean scheduledTillEnd = false;

	protected String rakeId = "";

	protected int rakeLinkNo = -1;

	protected int previousRakeLinkNo;

	protected Train rakeLinkTrain = null;

	protected Train previousRakeLinkTrain = null;

	private Loop startLoop;

	/**
	 * @param dir
	 * @param prior
	 * @param len
	 */
	public Train(int dir, double prior, double len) {
		setDirection(dir);
		setLength(len);
		setPriority(prior);
		setEndStation(null);
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param prior
	 */
	public Train(int a, double b, double c, double d, double e, double prior) {
		setDirection(a);
		setStartTime(b);
		setLength(c);
		setAccParam(d);
		setDeceParam(e);
		setPriority(prior);
		setEndStation(null);
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 */
	public Train(double priority, double startTime, double length,
			double acceleration, double deceleration) {

		setPriority(priority);
		setStartTime(startTime);
		setLength(length);
		setAccParam(acceleration);
		setDeceParam(deceleration);
		setEndStation(null);
	}

	/**
	 * @param f
	 * @param a
	 * @param c
	 * @param d
	 * @param e
	 */
	public Train(int f, double a, double c, double d, double e) {

		setPriority(a);
		setLength(c);
		setAccParam(d);
		setDeceParam(e);
		setDirection(f);
		setEndStation(null);
	}

	/**
     * 
     */
	public Train() {
		setEndStation(null);
	}

	public Train(Train simulatorTrain) {
		setPriority(simulatorTrain.getPriority());
		setLength(simulatorTrain.getLength());
		setAccParam(simulatorTrain.getAccParam());
		setDeceParam(simulatorTrain.getDeceParam());
		setTrainNo(simulatorTrain.getTrainNo());
		setMaximumPossibleSpeed(simulatorTrain.getMaximumPossibleSpeed());
		setBookedSpeed(simulatorTrain.getBookedSpeed());
		setDirection(simulatorTrain.getDirection());
		setStartLoopNo(simulatorTrain.getStartLoopNo());
		setVelocity(simulatorTrain.getVelocity());
		setScheduled(simulatorTrain.isScheduled());

	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getLength() {
		return length;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setDeceleration(double deceleration) {
		this.deceleration = deceleration;
	}

	public double getDeceleration() {
		return deceleration;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	public void setPriority(double priority) {
		this.priority = priority;
	}

	public double getPriority() {
		return priority;
	}

	public void printAttributes() {
		System.out.print("trainNumber " + trainNo);
		System.out.print(" direction " + getDirectionString());
		System.out.print(" acceleration " + acceleration);
		System.out.print(" deceleration " + deceleration);
		System.out.print(" priority " + priority);
		System.out.print(" velocity " + velocity);
		System.out.println();
	}

	public String getDirectionString() {
		if (direction == GlobalVar.UP_DIRECTION)
			return "up";
		else
			return "down";
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	protected void setDirection(String directionString) {
		// System.out.println("directionString "+directionString);
		if (directionString.equalsIgnoreCase("up"))
			setDirection(GlobalVar.UP_DIRECTION);
		else
			setDirection(GlobalVar.DOWN_DIRECTION);
	}

	public abstract void setTrainProperties(
			StreamTokenizer trainStreamTokenizer, int nTimeTableEntries)
			throws IOException;


	/**
	 * @return totalTime
	 */
	public double totalTime() {

		double trainArrivalTime = this.getStartTime();
		// double time = ((SimulatorTimeTableEntry)
		// timeTables.get(timeTables.size() -
		// 1)).departureTime
		// - ((SimulatorTimeTableEntry) timeTables.get(0)).arrivalTime;
		double time = (getTimeTables().get(getTimeTables().size() - 1))
				.getDepartureTime() - trainArrivalTime;

		return time;
	}

	/**
	 * @return travelTime
	 */
	public double travelTime() {
		double time = (getTimeTables().get(getTimeTables().size() - 1))
				.getArrivalTime() - (getTimeTables().get(0)).getDepartureTime();
		return time;
	}

	/**
     * 
     */
	public void printTimeTable() {
		System.out.println("Printing timetables for train " + getTrainNo());
		for (int i = 0; i < getTimeTables().size(); i++) {
			SimulatorTimeTableEntry ttEntry = getTimeTables().get(i);
			ttEntry.print();
		}
	}

	public VelocityProfile getVelocityProfileFromMilepost(double milePost) {
		for (int i = 0; i < this.getTimeTables().size(); i++) {
			SimulatorTimeTableEntry ttEntry = this.getTimeTables().get(i);

			if (ttEntry.getStartMilePost() <= milePost
					&& milePost <= ttEntry.getEndMilePost()) {

				VelocityProfileArray velocityProfileArray = (VelocityProfileArray) ttEntry
						.getVelocityProfileArray();

				for (int j = 0; j < velocityProfileArray.size(); j++) {
					VelocityProfile VelocityProfile = velocityProfileArray
							.get(j);

					if (VelocityProfile.getStartMilePost() <= milePost
							&& milePost <= VelocityProfile.getEndMilePost()) {
						return VelocityProfile;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param simulationInstance
	 * @param hasNoGUI
	 * @return list of trains
	 * @throws IOException
	 * @throws SimulatorException
	 */
	public static TrainList readTrain(SimulationInstance simulationInstance,
			boolean hasNoGUI) throws IOException, SimulatorException {

		Station currStn;
		Train currTrain;
		SimulatorTimeTableEntry currttEntry;
		ReferenceTableEntry lastRefEntry;
		SimulatorTimeTableEntry lastEntry;

		double runTimeToNextStation = 0;
		int lastRefLoopNo = -1;
		Loop lastRefLoop = null;
		double lastRefArrTime = 0;
		double lastRefDepTime = 0;
		int refLoopNo = 0;
		Loop refLoop = null;
		int refArrTime = 0;
		int refDepTime = 0;
		String stationName = "NULL";
		String lastStationName = "NULL";
		String days = "NULL";
		StationList stationList = simulationInstance.getStationList();
		LoopList loopList = simulationInstance.getLoopList();

		TrainList arrayTrain = new TrainList();
		int trainNo = 0;

		Set<Route> routeSet = new TreeSet<Route>();

		try {
			Reader reader;

			// Devendra
			reader = new FileReader(FileNames.getScheduledTrainsFileName());

			// Reader r = new FileReader(GlobalVar.fileScheduled);
			StreamTokenizer st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(true);
			st.slashStarComments(true);

			Debug.fine("Reading input file : ScheduledTrain.dat......");
			double len, accParam, deceParam;
			double priority; 
			int trainDirection;

			// System.out.println(StreamTokenizer.TT_EOF + " "
			// + StreamTokenizer.TT_EOL);
			while (st.nextToken() != StreamTokenizer.TT_EOF) {

				// Train number
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : scheduledTrain.dat");
					Debug.fine("Train: readTrain: Error : train number expected");
					throw new SimulatorException();
				}

				Debug.info("Train: readTrain: train no is " + st.nval);
				System.out.println("Train: readTrain: train no is " + st.nval);
				trainNo = (int) st.nval;

//				st.nextToken();
//				String rakeId = st.sval;
//
//				st.nextToken();
//				int rakeLinkNo = (int) st.nval;

				Debug.fine("Train: readTrain: Train no is  " + trainNo);

				// Train direction
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug.fine("Train: readTrain: Error in format of input file : scheduledTrain.dat......");
					Debug.fine("Train: readTrain: Error : train direction expected "
							+ st.nval);
					throw new SimulatorException();
				}

				if (st.sval.equalsIgnoreCase("up")) {
					trainDirection = GlobalVar.UP_DIRECTION;
				} else {
					trainDirection = GlobalVar.DOWN_DIRECTION;
				}

				// System.out.println("Train: readTrain: direction is  " + dir);

				// Train length
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : scheduledTrain.dat......");
					Debug.fine("Train: readTrain: Error : length expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				len = st.nval;
				Debug.fine("Train: readTrain: length is  " + len);

				// Train acceleration parameter
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : scheduledTrain.dat......");
					Debug.fine("Train: readTrain: Error : accelaration parameter expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				accParam = st.nval;
				Debug.fine("Train: readTrain: accel is  " + accParam);

				// Train deceleration
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : scheduledTrain.dat");
					Debug.fine("Train: readTrain: Error : deceleration parameter expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				deceParam = st.nval;
				Debug.fine("Train: readTrain: decel is  " + deceParam);

				// Train priority
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : scheduledTrain.dat......");
					Debug.fine("Train: readTrain: Error : priority expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				priority = (double) st.nval;
				Debug.fine("Train: readTrain: priority is  " + priority);

				currTrain = new ScheduledTrain(priority, 0, len, accParam,
						deceParam);
				currTrain.setDirection(trainDirection);

				currTrain.setTrainNo(trainNo);

				// Train velocity
				st.nextToken();
				double maximumPossibleSpeed = st.nval / 60;
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : scheduledTrain.dat");
					Debug.fine("Train: readTrain: Error : velocity expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: velocity   is " + st.nval);
				Debug.fine("Train: readTrain: value read is " + st.nval);

				// Train operating days
				st.nextToken();
				String opera = st.sval;
				Debug.fine("Train: readTrain: operating days " + opera);
				((ScheduledTrain) currTrain).setOperatingDays(opera);

				// ENDS HERE
				currTrain.setMaximumPossibleSpeed(maximumPossibleSpeed);
				currTrain.setScheduled(true);
				currTrain.setAccParam(accParam);
				currTrain.setDeceParam(deceParam);
//				currTrain.setRakeId(rakeId);
//				currTrain.setRakeLinkNo(rakeLinkNo);

				boolean isStartStn = true;
				arrayTrain.add(currTrain);

				st.nextToken();
				
				int nReferenceEntries = (int) st.nval;
				int i;
				//sneha: added following line
				currTrain.numberofhalts=nReferenceEntries;
				if (currTrain.getDirection() == GlobalVar.UP_DIRECTION)
					i = 0;
				else
					i = nReferenceEntries - 1;
				Debug.fine("Train: readTrain: nReferenceEntries "
						+ nReferenceEntries);
				System.out.println("Train: readTrain: nReferenceEntries "
						+ nReferenceEntries);
				while (i < nReferenceEntries && i >= 0) {

					// loops at which the train can halt
					st.nextToken();
					// System.out.println("Train: readTrain: loop number type "
					// + st.sval);
					// System.out.println("Train: readTrain: loop number is "
					// + st.nval);

					if (st.sval != null) {
						Debug.fine("Train: readTrain: st.sval is not null");
						break;
					}

					refLoopNo = ((int) st.nval);
					refLoop = (Loop)simulationInstance.getBlockFromBlockNo(refLoopNo);

					if (st.ttype == StreamTokenizer.TT_NUMBER) {
						// HashBlockEntry hbLinkEntry = GlobalVar
						// .getSimulationInstance().getBlockFromBlockNo(
						// refLoopNo);

						// Debug.assert(hbLinkEntry!=null,"Error: block not present ");
						// currLoop = (Loop) hbLinkEntry.block;
						// stationName = currLoop.station.stationName;
						// stationName = arrayStn.get(i).stationName;
						stationName = loopList.getStationName(refLoopNo);

						if (isStartStn == true) {
							currTrain.setStartLoopNo(refLoopNo);
						}

						st.nextToken();
						// System.out.println("Train: readTrain: value read is "
						// + st.nval);
						refArrTime = (int) st.nval;

						st.nextToken();
						// System.out.println("Train: readTrain: value read is "
						// + st.nval);
						refDepTime = (int) st.nval;

						if (isStartStn == true) {

							Debug.fine("Train: readTrain: refDepartureTime is "
									+ refDepTime);
							int hour, minute, second;
							double totalMinutes;

							second = (refDepTime % 100);
							minute = (refDepTime / 100) % 100;
							hour = refDepTime / 100 / 100;

							totalMinutes = minute + (second / 60.0);
							currTrain.setDepartTime(hour * 60 + totalMinutes);

							second = refArrTime % 100;
							minute = (refArrTime / 100) % 100;
							hour = refArrTime / 100 / 100;

							totalMinutes = minute + (second / 60.0);
							currTrain.setStartTime(hour * 60 + totalMinutes);

							// System.out.println("currTrain departTime arrTime "
							// + currTrain.getDepartTime() + " "
							// + currTrain.getStartTime());

							Debug.fine("Train: readTrain: startTime of current train = "
									+ currTrain.getStartTime());
							currTrain.setStartLoopNo(refLoopNo);
						}

						if (!isStartStn) {
							runTimeToNextStation = Math.abs(refArrTime
									- lastRefDepTime);

							// debugging reasons
							routeSet.add(new Route(lastRefLoopNo, refLoopNo,
									simulationInstance, currTrain));
							
							lastRefEntry = new ReferenceTableEntry(
									lastRefLoop, lastRefArrTime,
									lastRefDepTime, lastStationName,
									runTimeToNextStation, simulationInstance);
							currTrain.getRefTables().add(lastRefEntry);
						}

						lastRefLoopNo = refLoopNo;
						lastRefLoop = refLoop;
						lastRefArrTime = refArrTime;
						lastRefDepTime = refDepTime;
						lastStationName = stationName;
						isStartStn = false;

					} else {
						st.nextToken();
						st.nextToken();
					}

					if (currTrain.getDirection() == GlobalVar.UP_DIRECTION)
						i++;
					else
						i--;

				}

//				st.nextToken();
//				String rakeLinkDepartureTime = st.sval;
				// TODO

				lastRefEntry = new ReferenceTableEntry(lastRefLoop,
						lastRefArrTime, lastRefDepTime, lastStationName, 0.0,
						simulationInstance);
				currTrain.setEndStation(lastStationName);
				currTrain.setScheduled(true);

				currTrain.getRefTables().add(lastRefEntry);
				ChangeTimeTable.changeRefToMin(currTrain);

			}

//			BufferedWriter bw = new BufferedWriter(new FileWriter(
//					FileNames.getTestCaseDirectory()
//							+ "/routesWithoutLinks.txt"));
//
//			for (Route route : routeSet) {
//				Block previousBlock = route.previousBlock;
//				Block nextBlock = route.nextBlock;
//				int trainNo2 = route.train.getTrainNo();
//				int trainDirection2 = route.train.direction;
//				if (!previousBlock.hasPathTo(nextBlock, trainDirection2)) {
//
//					String directionString = "up";
//					if (trainDirection2 == GlobalVar.DOWN_DIRECTION)
//						directionString = "down";
//					bw.write("Route from " + previousBlock.getBlockNo()
//							+ " to " + nextBlock.getBlockNo() + " in "
//							+ directionString + " for train " + trainNo2
//							+ " does not exist\n");
//				}
//			}
//		
//			bw.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// reading unscheduled trains
	if(GlobalVar.capacitySelected==false){	
		try {
			Reader reader;

			// Devendra
			reader = new FileReader(FileNames.getUnscheduledTrainsFileName());

			// Reader r = new FileReader(GlobalVar.fileUnscheduled);
			StreamTokenizer st = new StreamTokenizer(reader);
			st.parseNumbers();
			st.lowerCaseMode(false);
			st.slashSlashComments(false);
			st.slashStarComments(true);

			Debug.fine("Reading input file : UnScheduled.dat......");

			while (st.nextToken() != StreamTokenizer.TT_EOF) {

				double len, accParam, deceParam, maximumPossibleSpeed;
				double priority;
				int dir;
				double startTime;
				String refStationName = "NULL";
				int minHaltTime;

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug.fine("Train: readTrain: Error : train number expected");
					throw new SimulatorException();
				}

				Debug.info("Train: readTrain: value read is " + st.nval);
				trainNo = (int) st.nval;

				st.nextToken();
				System.out.println("Train.Unscheduled: Direction: "+st.sval);
				if (st.ttype != StreamTokenizer.TT_WORD) {
					Debug.fine("Train: readTrain: Error in format of input file : unscheduledTrain.dat......");
					Debug.fine("Train: readTrain: Error : train direction expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.sval);

				if (st.sval.equalsIgnoreCase("Down")) {
					dir = GlobalVar.DOWN_DIRECTION;
				} else {
					dir = GlobalVar.UP_DIRECTION;
				}

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug.fine("Train: readTrain: Error : start time expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				startTime = (int) st.nval;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug.fine("Train: readTrain: Error : length expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				len = st.nval;
				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug.fine("Train: readTrain: Error : accelaration parameter expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				accParam = (st.nval)*3.6;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug.fine("Train: readTrain: Error : deccelaration parameter expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				deceParam = (st.nval)*3.6;

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug.fine("Train: readTrain: Error : priority expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				priority = (double) st.nval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : unscheduledTrain.dat......");
					Debug.fine("Train: readTrain: Error : Max permisible speed expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				maximumPossibleSpeed = st.nval;
				st.nextToken();

				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : unscheduledTrain.dat");
					Debug.fine("Train: readTrain: Error : loop no expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				int stLoopNo = ((int) st.nval);

				st.nextToken();
				if (st.ttype != StreamTokenizer.TT_NUMBER) {
					Debug.fine("Train: readTrain: Error in format of input file : scheduledTrain.dat......");
					Debug.fine("Train: readTrain: Error : end loop no expected");
					throw new SimulatorException();
				}

				Debug.fine("Train: readTrain: value read is " + st.nval);
				int enLoopNo = ((int) st.nval);
				
				
				currTrain = new UnscheduledTrain(dir, startTime, len, accParam,
						deceParam, priority);
				currTrain.setTrainNo(trainNo);
				currTrain.setStartLoopNo(stLoopNo);
				currTrain.setEndLoopNo(enLoopNo);
				trainNo++;

				currTrain.setEndStation(loopList.getStationName(currTrain
						.getEndLoopNo()));
				if (currTrain.getEndStation() == null) {
					currTrain.setEndStation("");
				}

				// the speed given is in km/hr we conver it to km/min
				currTrain.setMaximumPossibleSpeed(maximumPossibleSpeed / 60);
				ChangeTimeTable.changeToMin(currTrain);
				currTrain.setScheduled(false);
				
				st.nextToken();
				/**Vidyadhar
				Providing Halt at stations**/
				int nReferenceEntries = (int) st.nval;
				currTrain.setNumberofHalts(nReferenceEntries);
				int i;
				
				if (currTrain.getDirection() == GlobalVar.UP_DIRECTION)
					i = 0;
				else
					i = nReferenceEntries - 1;
				
				System.out.println("Train.Unscheduled: nReferenceEntries: "+ nReferenceEntries
									+ " i = "+i);
				while (i < nReferenceEntries && i >= 0) {

					// Station at which the train can halt
					st.nextToken();
					
					
					if (st.ttype == StreamTokenizer.TT_WORD) {
						
				
						refStationName = st.sval;
						st.nextToken();
						minHaltTime = (int) st.nval;
						lastRefEntry = new ReferenceTableEntry(refStationName,
							minHaltTime);
						//System.out.println("Train.Unscheduled: HaltStation: "+ lastRefEntry.getStationName()
													//+ "  minHaltTime: " +minHaltTime);
						
						if (currTrain.getDirection() == GlobalVar.UP_DIRECTION)
							i++;
						else
							i--;
						
						currTrain.getRefTables().add(lastRefEntry);
					
					}else 
						st.nextToken();
				}
				arrayTrain.add(currTrain);

			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

		sortTrains(arrayTrain);
		return (arrayTrain);
	}

	public void setRakeId(String rakeId) {
		this.rakeId = rakeId;
	}

	public void setRakeLinkNo(int rakeLinkNo) {
		this.rakeLinkNo = rakeLinkNo;
	}

	/**
	 * @return the designated loop number of the destination station of the
	 *         train.
	 */
	public int getDestinationLoopNumber() {
		if (isScheduled() == true) {
			int referenceTableSize = this.getRefTables().size();

			// reference loop number of the last reference entry.
			return this.getRefTables().get(referenceTableSize - 1)
					.getReferenceLoopNo();
		} else {// unscheduled
			return this.getEndLoopNo();
		}
	}

	/**
	 * Get reference table entry from the station name.
	 * 
	 * @param stationName
	 * @return the reference entry corresponding to the station name. If there
	 *         is no such entry return null.
	 */
	public ReferenceTableEntry getRefTabEntryFromStationName(String stationName) {
		ArrayList<ReferenceTableEntry> referenceTable = this.getRefTables();
		for (ReferenceTableEntry referenceTableEntry : referenceTable) {
			if (referenceTableEntry.getStationName().equalsIgnoreCase(
					stationName)) {
				return referenceTableEntry;
			}
		}

		return null;
	}

	/**
	 * Get the loop number of the last loop to be traversed by the train.
	 * 
	 * @return loop number of the destination loop of the train
	 */
	public int getLastLoopNo() {
		if (this.isScheduled()) {// scheduled train
			int size = getRefTables().size();
			int loopNo = getRefTables().get(size - 1).getReferenceLoopNo();
			return loopNo;
		} else {// unscheduled train
			return this.getEndLoopNo();
		}
	}

	/**
	 * Determine if the train has a scheduled halt at the loop
	 * 
	 * @param loop
	 * @param simulationInstance
	 * @return true if the train has a scheduled halt else returns false.
	 */
	public boolean hasScheduledHalt(Loop loop,
			SimulationInstance simulationInstance) {
		if (isScheduled()) {
			String stationName = loop.getStation().getStationName();
			ReferenceTableEntry referenceTableEntry = getRefTabEntryFromStationName(stationName);
			if (referenceTableEntry != null
					&& referenceTableEntry.getReferenceDepartureTime() > referenceTableEntry
							.getReferenceArrivalTime())
				return true;
			else
				return false;
		} else {

			String endStationString = simulationInstance.getStationName(this
					.getEndLoopNo());
			String stationName = loop.getStation().getStationName();
			ReferenceTableEntry referenceTableEntry = getRefTabEntryFromStationName(stationName);
			// System.out.println("endStation " + endStationString
			// + " loopStation " + loop.stationName);
			if (referenceTableEntry!= null)
				return true;
			
			if (endStationString.equalsIgnoreCase(loop.getStationName()))
				return true;
			return false;
		}
	}

	public void printVelocityProfileForTrain() {
		double totalTime = 0;

		ArrayList<VelocityProfile> velocityProfileList = getVelocityProfileList();

		for (VelocityProfile VelocityProfile : velocityProfileList) {
			System.out.print("arrivaltime " + totalTime + " ");
			VelocityProfile.print(this.getDirection());
			totalTime += VelocityProfile.getTotalTime();
		}

		System.out.println("Total time of the train is " + totalTime
				+ " stored total time is " + this.totalTime());
		System.out.println("StartTime "
				+ this.getStartTime()
				+ " endTime "
				+ this.getTimeTables().get(getTimeTables().size() - 1)
						.getDepartureTime());
	}

	/**
	 * @return {@link ArrayList} of {@link VelocityProfile} in the order which
	 *         the train traverses.
	 */
	public ArrayList<VelocityProfile> getVelocityProfileList() {

		ArrayList<VelocityProfile> velocityProfileList = new ArrayList<VelocityProfile>();

		for (int i = 0; i < this.getTimeTables().size(); i++) {
			SimulatorTimeTableEntry tte = this.getTimeTables().get(i);

			int j;
			int inc;
			if (this.getDirection() == 0) {
				j = 0;
				inc = 1;
			} else {
				j = tte.getVelocityProfileArray().size() - 1;
				inc = -1;
			}
			for (; j < tte.getVelocityProfileArray().size() && j >= 0; j = j
					+ inc) {
				velocityProfileList.add(tte.getVelocityProfileArray().get(j));
			}
		}
		return velocityProfileList;
	}

	public void printDiscontinuity() {
		double lastEndVelocity = 0.0;

		ArrayList<VelocityProfile> velocityProfileList = this
				.getVelocityProfileList();
		System.out.println("Discontinuity found at these points");
		for (VelocityProfile VelocityProfile : velocityProfileList) {
			if (VelocityProfile.getStartVelocity() != lastEndVelocity) {
				System.out.print("lastEndVelocity " + lastEndVelocity + " ");
				VelocityProfile.print(this.getDirection());
			}
			lastEndVelocity = VelocityProfile.getEndVelocity();
		}
	}
	
	private static void sortTrains(ArrayList<Train> arrayTrains){
		Collections.sort(arrayTrains, trainComparator);
	}

	public static Comparator<Train> trainComparator = new Comparator<Train>() {

		@Override
		/**
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Train train1, Train train2) {
			int var = 0;
			double startTime1, startTime2, departTime1, departTime12;
			double priority1, priority2;
			boolean isScheduled1 = false, isScheduled2 = false;

			SimulatorTimeTableEntry e, f;
			priority1 = train1.getPriority();
			priority2 = train2.getPriority();
			startTime1 = train1.getStartTime();
			startTime2 = train2.getStartTime();
			departTime1 = train1.getDepartTime();
			departTime12 = train2.getDepartTime();
			isScheduled1 = train1.isScheduled();
			isScheduled2 = train2.isScheduled();

			if (priority1 > priority2) {
				var = 1;
			} else if (priority1 < priority2) {
				var = -1;
			} else if (priority1 == priority2) {
				if (isScheduled1 == true && isScheduled2 == true) {
					if (departTime1 > departTime12) {
						var = 1;
					} else if (departTime1 < departTime12) {
						var = -1;
					} else {
						var = 0;
					}
				} else {
					if (startTime1 > startTime2) {
						var = 1;
					} else if (startTime1 < startTime2) {
						var = -1;
					} else {
						var = 0;
					}
				}
			}

			return var;
		}
	};
	

	public boolean isSuburban() {
		if (getPriority() == GlobalVar.SUBURBAN_TRAIN_PRIORITY)
			return true;
		return false;
	}

	public double getMaximumPossibleSpeed() {
		return maximumPossibleSpeed;
	}

	public void setMaximumPossibleSpeed(double maximumPossibleSpeed) {
		this.maximumPossibleSpeed = maximumPossibleSpeed;
	}

	public double getBookedSpeed() {
		return bookedSpeed;
	}

	public void setBookedSpeed(double bookedSpeed) {
		this.bookedSpeed = bookedSpeed;
	}

	public double getAccParam() {
		return accParam;
	}

	public void setAccParam(double accParam) {
		this.accParam = accParam;
	}

	public double getDeceParam() {
		return deceParam;
	}

	public void setDeceParam(double deceParam) {
		this.deceParam = deceParam;
	}

	public int getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(int trainNo) {
		this.trainNo = trainNo;
	}
	
	public int getNumberofHalts() {
		return numberofhalts;
	}

	public void setNumberofHalts(int halts) {
		this.numberofhalts = halts;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getDepartTime() {
		return departTime;
	}

	public void setDepartTime(double departTime) {
		this.departTime = departTime;
	}

	public int getStartLoopNo() {
		return startLoopNo;
	}

	public void setStartLoopNo(int startLoopNo) {
		this.startLoopNo = startLoopNo;
	}

	public int getEndLoopNo() {
		return endLoopNo;
	}

	public void setEndLoopNo(int endLoopNo) {
		this.endLoopNo = endLoopNo;
	}

	public String getEndStation() {
		return endStation;
	}

	public void setEndStation(String endStation) {
		this.endStation = endStation;
	}

	public Color getDrawColour() {
		return drawColour;
	}

	public void setDrawColour(Color drawColour) {
		this.drawColour = drawColour;
	}

	public ArrayList<SimulatorTimeTableEntry> getTimeTables() {
		return timeTables;
	}

	public void setTimeTables(ArrayList<SimulatorTimeTableEntry> timeTables) {
		this.timeTables = timeTables;
	}

	public ArrayList<ReferenceTableEntry> getRefTables() {
		return refTables;
	}
	
	//sneha added new function
	public ArrayList<ReferenceTableEntry> getTempRefTables() {
		return tempRefTables;
	}

	public void setRefTables(ArrayList<ReferenceTableEntry> refTables) {
		this.refTables = refTables;
	}

	public int[] getSignalFailedBlocks() {
		return signalFailedBlocks;
	}

	public void setSignalFailedBlocks(int signalFailedBlocks[]) {
		this.signalFailedBlocks = signalFailedBlocks;
	}

	public int getSignalFailCounter() {
		return signalFailCounter;
	}

	public void setSignalFailCounter(int signalFailCounter) {
		this.signalFailCounter = signalFailCounter;
	}

	public void printPathBlockOccupancies() {

		for (SimulatorTimeTableEntry simulatorTimeTableEntry : getTimeTables()) {
			int blockNo = simulatorTimeTableEntry.getLoopNo();
			// Block block = simulationInstance
			// .getBlockFromBlockNo(blockNo);
			// block.printOccupancies(trainNo);
		}
	}

	public void copyTimeTablesFromTrain(Train simulatorTrain,
			int simulationTimeInstance, SimulationInstance simulationInstance) {
		for (int k = 0; k < simulatorTrain.getRefTables().size(); k++) {

			// System.out.println("startTime"+startTime+"train no"
			// +trainNo);
			setStartTime(simulatorTrain.getStartTime()
					+ ((simulationTimeInstance) * 1440));

			// System.out.println("startTime"+startTime);
			setDepartTime(simulatorTrain.getDepartTime()
					+ (simulationTimeInstance * 1440));
			ReferenceTableEntry referenceTableEntry = simulatorTrain
					.getRefTables().get(k);
			endStation = simulatorTrain.getEndStation();
			endLoopNo = simulatorTrain.getEndLoopNo();
			startLoopNo = simulatorTrain.getStartLoopNo();
			
			double arrivalTime = referenceTableEntry.getReferenceArrivalTime()
					+ ((simulationTimeInstance) * 1440);
			double departureTime = referenceTableEntry
					.getReferenceDepartureTime()
					+ ((simulationTimeInstance) * 1440);
			ReferenceTableEntry newReferenceTableEntry = new ReferenceTableEntry(
					referenceTableEntry, simulationInstance);
			newReferenceTableEntry.setArrivalDepartureTimes(arrivalTime,
					departureTime, simulationInstance);
			getRefTables().add(newReferenceTableEntry);
			
		}
	}

	public boolean hasError() {
		return false;
	}

	public void setTimesForVelocityProfiles() {

		ArrayList<SimulatorTimeTableEntry> simulatorTimeTableEntries = this
				.getSortedTimeTableEntries();

		// double arrivalTime =
		// simulatorTimeTableEntries.get(0).getArrivalTime();

		for (SimulatorTimeTableEntry simulatorTimeTableEntry : simulatorTimeTableEntries) {
			double arrivalTime = simulatorTimeTableEntry.getArrivalTime();

			VelocityProfileArray simulatorVelocityProfiles = simulatorTimeTableEntry
					.getVelocityProfileArray();
			simulatorVelocityProfiles.sort(this);

			int blockId = simulatorTimeTableEntry.getLoopNo();
			// simulatorTimeTableEntry.print();
			for (VelocityProfile velocityProfile : simulatorVelocityProfiles) {
				// velocityProfile.setBlockId(blockId);
				// velocityProfile.setTrackSegment(sim)

				// setting arrival time
				velocityProfile.setArrivalTime(arrivalTime);

				// calculating departure time
				double totalTime = velocityProfile.getTotalTime();
				arrivalTime = arrivalTime + totalTime;

				// setting departure time
				velocityProfile.setDepartureTime(arrivalTime);

				double ca = 0;
				if (velocityProfile.getAcceleration() != 0) {
					ca = velocityProfile.getEndVelocity()
							- velocityProfile.getStartVelocity();
					ca = ca / velocityProfile.getTotalTime();
					ca = GlobalVariables.roundToThreeDecimals(ca);
				}
				double s = velocityProfile.getEndMilePost()
						- velocityProfile.getStartMilePost();
				double ca2 = 0;
				if (s != 0) {
					double v = velocityProfile.getEndVelocity();
					double u = velocityProfile.getStartVelocity();
					ca2 = (v * v - u * u) / (2 * s);
					ca2 = GlobalVariables.roundToThreeDecimals(ca2);
				}

				Debug.fine("Train: setTimesForVelocityProfiles" + " "
						+ velocityProfile.getStartMilePost() + " "
						+ velocityProfile.getEndMilePost() + " "
						+ velocityProfile.getArrivalTime() + " "
						+ velocityProfile.getDepartureTime() + " "
						+ velocityProfile.getStartVelocity() + " "
						+ velocityProfile.getEndVelocity() + " "
						+ velocityProfile.getTotalTime() + " "
						+ velocityProfile.getAcceleration() + " ca " + ca
						+ " ca2 " + ca2);

			}

		}
	}

	private ArrayList<SimulatorTimeTableEntry> getSortedTimeTableEntries() {
		Collections.sort(timeTables, this);
		return timeTables;
	}

	public BlockList getBlocksTraversedByTrain(
			SimulationInstance simulationInstance) {
		BlockList blockList = new BlockList();
		for (SimulatorTimeTableEntry simulatorTimeTableEntry : timeTables) {
			int blockNo = simulatorTimeTableEntry.getLoopNo();
			Block block = simulationInstance.getBlockFromBlockNo(blockNo);
			// if (block.isLoop())
			// continue;
			blockList.add(block);
		}

		// remove first loop
		// blockList.remove(0);

		return blockList;
	}

	public ArrayList<Block> getBlocksByMaxTimeTaken(
			SimulationInstance simulationInstance) {
		ArrayList<Block> blocksTraversed = getBlocksTraversedByTrain(simulationInstance);
		double maxTraversalTime = -1;
		ArrayList<Double> blockTraversalTimes = new ArrayList<Double>();
		for (Block block : blocksTraversed) {

			double blockTraversalTime = block.getOccupancyTimeByTrain(this);
			blockTraversalTimes.add(blockTraversalTime);

			maxTraversalTime = maxTraversalTime > blockTraversalTime ? maxTraversalTime
					: blockTraversalTime;
		}

		ArrayList<Block> maxTimeBlocks = new ArrayList<Block>();
		for (int i = 0; i < blocksTraversed.size(); i++) {
			Block block = blocksTraversed.get(i);
			double time = blockTraversalTimes.get(i);
			if (time == maxTraversalTime)
				maxTimeBlocks.add(block);
		}

		return maxTimeBlocks;
	}

	public ArrayList<SimulatorTimeTableEntry> getTimeTablesHaltsOnly(
			SimulationInstance simulationInstance) {
		ArrayList<SimulatorTimeTableEntry> timeTableEntries = new ArrayList<SimulatorTimeTableEntry>();
		for (SimulatorTimeTableEntry simulatorTimeTableEntry : timeTables) {
			int loopNo = simulatorTimeTableEntry.getLoopNo();
			Block block = simulationInstance.getBlockFromBlockNo(loopNo);
			if (block.isLoop()) {
				timeTableEntries.add(simulatorTimeTableEntry);
			}
		}

		return timeTableEntries;

	}

	public boolean isStartLoop(Block block) {
		int originalStartLoopNo = this.getRefTables().get(0)
				.getReferenceLoopNo();
		return (block.isLoop() && originalStartLoopNo == block.getBlockNo());
	}

	public int compare(SimulatorTimeTableEntry a, SimulatorTimeTableEntry b) {
		if (a.getArrivalTime() < b.getArrivalTime())
			return -1;
		else if (a.getArrivalTime() > b.getArrivalTime())
			return 1;

		return 0;
	}

	public boolean isScheduledTillEnd() {
		return scheduledTillEnd;
	}

	public void setScheduledTillEnd(boolean scheduledTillEnd) {
		this.scheduledTillEnd = scheduledTillEnd;
	}

	public double getActionableTime() {

		double actionableTime;
		Debug.fine("Train: getActionableTime: train is scheduled "
				+ this.scheduled);
		if (this.stationToStationSchedulerStack.isEmpty() && this.scheduled) {
			actionableTime = this.getRefTables().get(0)
					.getReferenceArrivalTime();

		} else {
			actionableTime = this.getDepartTime();
		}

		return actionableTime;
	}

	public Loop getNextReferenceLoop() {

		Loop nextReferenceLoop = null;
		if (stationToStationSchedulerStack.size() == 0) {
			nextReferenceLoop = getOriginLoop();
		} else {
			StationToStationScheduler stationToStationScheduler = stationToStationSchedulerStack
					.peek();
			boolean scheduling = stationToStationScheduler.scheduled;

			if (scheduling) {
				Loop newStartLoop = stationToStationScheduler.nextReferenceLoop;

				if (newStartLoop != null) {
					nextReferenceLoop = newStartLoop
							.getNextReferenceLoop(refTables);
				}
			} else {
				nextReferenceLoop = stationToStationScheduler.nextReferenceLoop;

			}
		}

		return nextReferenceLoop;
	}

	// public void setNextReferenceLoopNumber(int nextReferenceLoopNumber) {
	// this.nextReferenceLoopNumber = nextReferenceLoopNumber;
	// }

	public Loop getOriginLoop() {
		Loop originLoop =null;
		if (scheduled) {
			originLoop = refTables.get(0).getReferenceLoop();
		} else {
			originLoop = startLoop;
		}

		return originLoop;
	}

	public boolean hasReachedDestination() {
		if (stationToStationSchedulerStack.isEmpty())
			return false;

		StationToStationScheduler s2sScheduler = stationToStationSchedulerStack
				.peek();
		int destinationLoopNumber = getDestinationLoopNumber();
		int s2sNextReferenceLoopNumber = s2sScheduler.nextReferenceLoop.getBlockNo();
		boolean s2sScheduled = s2sScheduler.scheduled;

		if (s2sNextReferenceLoopNumber == destinationLoopNumber && s2sScheduled)
			return true;

		return false;
	}

	public int getRakeLinkNo() {
		return rakeLinkNo;
	}

	public int getPreviousRakeLinkNo() {
		return previousRakeLinkNo;
	}

	public void setPreviousRakeLinkNo(int trainNo2) {
		this.previousRakeLinkNo = trainNo2;
	}

	public Train getRakeLinkTrain() {
		return rakeLinkTrain;
	}

	public void setRakeLinkTrain(Train rakeLinkTrain) {
		this.rakeLinkTrain = rakeLinkTrain;
	}

	public Train getPreviousRakeLinkTrain() {
		return previousRakeLinkTrain;
	}

	public void setPreviousRakeLinkTrain(Train previousRakeLinkTrain) {
		this.previousRakeLinkTrain = previousRakeLinkTrain;
	}

	public Loop getCurrentLoop() {
		Loop currentLoop;
		if (stationToStationSchedulerStack.isEmpty()) {
			currentLoop = getOriginLoop();
		} else {
			StationToStationScheduler stationToStationScheduler = stationToStationSchedulerStack
					.peek();
			boolean scheduling = stationToStationScheduler.scheduled;

			if (!scheduling) {
				currentLoop = stationToStationScheduler.startLoop;
			} else {
				currentLoop = stationToStationScheduler.nextReferenceLoop;
			}
		}

		return currentLoop;

	}
}

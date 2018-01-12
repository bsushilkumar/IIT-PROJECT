package gui.entities.trainEntities;


import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * PassengerTrain
 */
public class PassengerTrain extends ScheduledTrain {

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 */
	public PassengerTrain(int a, double b, double c, double d, double e, int f) {
		super(a, b, c, d, e, f);
	}

	/**
	 * @param dir
	 * @param prior
	 * @param len
	 */
	public PassengerTrain(int dir, double prior, double len) {
		setDirection(dir);
		setLength(len);
		setPriority(prior);
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 */
	public PassengerTrain(double priority, double startTime, double length,
			double acceleration, double deceleration) {
		super(priority, startTime, length, acceleration, deceleration);
	}

	/**
	 * @param a
	 */
	public PassengerTrain(String a) {
		setOperatingDays(a);
	}

//	/**
//	 * @param Stations
//	 */
//	public void occupyBlocks(StationList stations) {
//		ArrayList<SimulatorTimeTableEntry> currTimeTable = getTimeTables();
//		ArrayList<SimulatorTimeTableEntry> newTimeTable = new ArrayList<SimulatorTimeTableEntry>();
//		SimulatorTimeTableEntry ttEntry, nextTTEntry;
//		for (int m = 0; m < currTimeTable.size() - 1; m++) {
//			ttEntry = (SimulatorTimeTableEntry) currTimeTable.get(m);
//			nextTTEntry = (SimulatorTimeTableEntry) currTimeTable.get(m + 1);
//			Debug.print("looping " + m + " loop no " + ttEntry.getLoopNo()
//					+ "next loop no" + nextTTEntry.getLoopNo());
//			Loop currLoop = (Loop) GlobalVar.getSimulationInstance()
//					.getBlockFromBlockNo(ttEntry.getLoopNo());
//			if (currLoop != null) {
//
//				currLoop.setOccupied(getTrainNo(), ttEntry.getArrivalTime(),
//						ttEntry.getDepartureTime(), true);
//				Loop nextLoop = (Loop) GlobalVar.getSimulationInstance()
//						.getBlockFromBlockNo(nextTTEntry.getLoopNo());
//				ttEntry.setStartMilePost(currLoop.getStartMilePost(this
//						.getDirection()));
//				ttEntry.setEndMilePost(currLoop.getEndMilePost(this
//						.getDirection()));
//				double rdist;
//				if (getDirection() == GlobalVar.UP_DIRECTION) {
//					rdist = Math.abs(currLoop.getEndMilePost()
//							- nextLoop.getStartMilePost());
//				} else {
//					rdist = Math.abs(currLoop.getStartMilePost()
//							- nextLoop.getEndMilePost());
//				}
//				Debug.print("Time table entry for loop added ");
//
//				newTimeTable.add(ttEntry);
//				double rtime = nextTTEntry.getArrivalTime()
//						- ttEntry.getDepartureTime();
//				double deptTime = 0;
//				double arrtime = ttEntry.getDepartureTime();
//				Debug.print("rtime is:" + rtime + "rdist is:" + rdist);
//				Block currBlk = currLoop.getNextBlock(this);
//				Debug.print(" arrtime is:" + arrtime + " deptTime is:"
//						+ deptTime);
//				Debug.print(" take next " + nextLoop.getStartMilePost());
//				Debug.print(" take curr " + currBlk.getStartMilePost());
//				while (nextLoop.getStartMilePost() != currBlk
//						.getStartMilePost()) {
//					deptTime = arrtime
//							+ (rtime * (currBlk.getEndMilePost() - currBlk
//									.getStartMilePost())) / rdist;
//					ttEntry = new SimulatorTimeTableEntry(currBlk.getBlockNo(),
//							arrtime, deptTime, 0, currBlk.getStartMilePost(this
//									.getDirection()));
//					ttEntry.setEndMilePost(currBlk.getEndMilePost(this
//							.getDirection()));
//					newTimeTable.add(ttEntry);
//					Block nextBlock = currBlk;
//					// if 3 colour aspect then 2 blocks should be reserved.
//					for (int i = 0; i < GlobalVar.getSimulationInstance().numberOfSignalAspects - 1; i++) {
//						Debug.print("Reserving for block "
//								+ nextBlock.getBlockName());
//
//						nextBlock.setOccupied(getTrainNo(), arrtime, deptTime,
//								true);
//						nextBlock = nextBlock.getNextBlock(this);
//						if (nextBlock == null)
//							break;
//					}
//					Debug.print("Block no " + currBlk.getBlockName()
//							+ " arrtime is:" + arrtime + " deptTime is:"
//							+ deptTime);
//					arrtime = deptTime;
//					currBlk = currBlk.getNextBlock(this);
//				}
//			} // for n =
//		} // for m ++
//
//		// adding the timings for last satation
//		ttEntry = (SimulatorTimeTableEntry) currTimeTable.get(currTimeTable
//				.size() - 1);
//		Loop currLoop = (Loop) GlobalVar.getSimulationInstance()
//				.getBlockFromBlockNo(ttEntry.getLoopNo());
//		currLoop.setOccupied(getTrainNo(), ttEntry.getArrivalTime(),
//				ttEntry.getDepartureTime(), true);
//		ttEntry.setStartMilePost(currLoop.getStartMilePost());
//		ttEntry.setEndMilePost(currLoop.getStartMilePost());
//		newTimeTable.add(ttEntry);
//		for (int m = 0; m < newTimeTable.size(); m++) {
//			ttEntry = (SimulatorTimeTableEntry) newTimeTable.get(m);
//			Debug.print(" Alloted loop:  " + ttEntry.getLoopNo()
//					+ "   Arr Time: " + ttEntry.getArrivalTime()
//					+ "   DepTime: " + ttEntry.getDepartureTime()
//					+ "   Velocity:" + ttEntry.getStartVelocity()
//					+ "     dist:" + ttEntry.getStartMilePost());
//		}
//		setTimeTables(newTimeTable);
//	}

	@Override
	public void setTrainProperties(StreamTokenizer trainStreamTokenizer,
			int nTimeTableEntries) throws IOException {
		// TODO Auto-generated method stub

	}


}// for delay class
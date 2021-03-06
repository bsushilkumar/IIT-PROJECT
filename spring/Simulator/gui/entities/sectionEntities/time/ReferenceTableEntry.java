package gui.entities.sectionEntities.time;

import gui.diagramEntities.trainDiagrams.VelocityProfile;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntityList.TrainList;

import java.util.ArrayList;

import simulator.input.SimulationInstance;
import simulator.scheduler.Condition;
import simulator.util.Debug;

/**
 * 
 */
public class ReferenceTableEntry {
	/**
	 * stationName
	 */
	private String stationName;
	
	//sneha:added following line
	public String tempstationName;
	/**
	 * refLoopNo
	 */
	public int refLoopNo;
	/**
	 * refArrTime
	 */
	private double refArrTime;
	/**
	 * refDepTime
	 */
	private double refDepTime;
	/**
	 * minHaltTime
	 */
	private double minHaltTime;
	/**
	 * runTimeToNextStn
	 */
	private double runTimeToNextStn = 60;
	/**
	 * velocityProfileArray
	 */
	private ArrayList<VelocityProfile> velocityProfileArray;
	private Loop referenceLoop = null;
	public ArrayList<Condition> conditionList = new ArrayList<Condition>();

	public int refArrTimeInput = 0;
	public int refDepTimeInput = 0;
	//santhosh
	public String loopType="";
	public String loopDir="";
	/**
	 * @param loopNo
	 * @param arrTime
	 * @param depTime
	 * @param stationName
	 * @param simulationInstance
	 * @param runTime
	 */
	public ReferenceTableEntry(Loop loop, double arrivalTime,
			double departureTime, String stationName,
			double runTimeToNextStation, SimulationInstance simulationInstance) {

		setReferenceLoop(loop);
		this.setStationName(stationName);
		setArrivalDepartureTimes(arrivalTime, departureTime, simulationInstance);
		setRunTimeToNextStation(runTimeToNextStation);
	}

	/**
     * 
     */
	public ReferenceTableEntry() {
	}

	public ReferenceTableEntry(ReferenceTableEntry referenceTableEntry,
			SimulationInstance simulationInstance) {
		setReferenceLoop(referenceTableEntry.getReferenceLoop());
		stationName = referenceTableEntry.stationName;
		setArrivalDepartureTimes(referenceTableEntry.getReferenceArrivalTime(),
				referenceTableEntry.getReferenceDepartureTime(),
				simulationInstance);
		setRunTimeToNextStation(referenceTableEntry.getRunTimeToNextStation());
	}

	public ReferenceTableEntry(int loopNo, int arrivalTimeInput,
			int departureTimeInput, String stationName, String loopType, String loopDir) {
		this.refLoopNo = loopNo;
		this.refArrTimeInput = arrivalTimeInput;
		this.refDepTimeInput = departureTimeInput;
		//santhosh
		this.stationName=stationName;
		this.loopType=loopType;
		this.loopDir=loopDir;
	}
	
	public ReferenceTableEntry(String stationName, int minHaltTime) {
		//Vidyadhar
		this.stationName = stationName;
		this.minHaltTime = minHaltTime;

	}
	
	/**
     * 
     */
	public void print() {
		System.out.print("Ref Loop No: " + getReferenceLoopNo() + " Station: "
				+ getStationName() + " Ref Arrival Time: "
				+ getReferenceArrivalTime() + " Ref Departure Time: "
				+ getReferenceDepartureTime() + " minHaltTime: " + minHaltTime
				+ " runTimeToNextStn: " + getRunTimeToNextStation());
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public void setArrivalDepartureTimes(double arrivalTime,
			double departureTime, SimulationInstance simulationInstance) {
		if (simulationInstance.delayFactor == 1) {
			/*
			 * if (arrTime > depTime) { System.out.print("arrTime > depTime ");
			 * System.exit(0);} else
			 */{
				setReferenceArrivalTime(arrivalTime);
				setReferenceDepartureTime(departureTime);
			}
		}
		if (simulationInstance.delayFactor == 0) {
			setReferenceArrivalTime(arrivalTime);
			setReferenceDepartureTime(departureTime);
		}
		minHaltTime = getReferenceDepartureTime() - getReferenceArrivalTime();

	}

	public double getReferenceArrivalTime() {
		return refArrTime;
	}

	public void setReferenceArrivalTime(double refArrTime) {
		this.refArrTime = refArrTime;
	}

	public double getReferenceDepartureTime() {
		return refDepTime;
	}

	public void setReferenceDepartureTime(double refDepTime) {
		this.refDepTime = refDepTime;
	}

	public int getReferenceLoopNo() {
		return referenceLoop.getBlockNo();
	}

	public double getRunTimeToNextStation() {
		return runTimeToNextStn;
	}

	public void setRunTimeToNextStation(double runTimeToNextStn) {
		this.runTimeToNextStn = runTimeToNextStn;
	}

	public double getMinHaltTime(){
		return minHaltTime;
	}
	
	public void setMinHaltTime(int refMinHaltTime){
		minHaltTime =  refMinHaltTime;
	}
	
	public Loop getReferenceLoop() {
		return referenceLoop ;
	}
	
	public void setReferenceLoop(Loop loop) {
		this.referenceLoop =loop;
	}
	
	public boolean areConditionsSatisfied(TrainList trainList){
		for (Condition condition : conditionList) {
			Debug.error(condition.getInfoString());
			if(!condition.isSatisfied(trainList)){
				Debug.error("condition not satisfied");
				return false;
			}
		}
		
		return true;
	}
}
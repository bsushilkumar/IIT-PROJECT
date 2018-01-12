package gui.entities.trainEntities;

import globalVariables.GlobalVariables;
import gui.entities.sectionEntities.time.ReferenceTableEntry;

import java.io.IOException;
import java.io.StreamTokenizer;

public class UnscheduledTrain extends Train {

	

	public UnscheduledTrain(int dir, double startTime, double len,
			double accParam, double deceParam, double priority) {
		super(dir, startTime, len, accParam, deceParam, priority);
	}

	public UnscheduledTrain() {
		// TODO Auto-generated constructor stub
	}


	public void setTrainProperties(
			StreamTokenizer unscheduledTrainStreamTokenizer,
			int nTimeTableEntries) throws IOException {

		unscheduledTrainStreamTokenizer.nextToken();
		int trainNumber = (int) unscheduledTrainStreamTokenizer.nval;
//		System.out.println("trainNumber " + trainNumber);

		unscheduledTrainStreamTokenizer.nextToken();
		String directionString = unscheduledTrainStreamTokenizer.sval;
		int direction = GlobalVariables.getDirectionFromDirectionString(directionString);
//		System.out.println("directionString " + directionString);

		unscheduledTrainStreamTokenizer.nextToken();
		int arrivalTime = (int)unscheduledTrainStreamTokenizer.nval;
//		System.out.println("arrivalTime " + arrivalTime);

		unscheduledTrainStreamTokenizer.nextToken();
		double length = unscheduledTrainStreamTokenizer.nval;
//		System.out.println("length " + length);

		unscheduledTrainStreamTokenizer.nextToken();
		double acceleration = unscheduledTrainStreamTokenizer.nval;
//		System.out.println("acceleration " + acceleration);

		unscheduledTrainStreamTokenizer.nextToken();
		double deceleration = unscheduledTrainStreamTokenizer.nval;
//		System.out.println("deceleration " + deceleration);

		unscheduledTrainStreamTokenizer.nextToken();
		int priority = (int) unscheduledTrainStreamTokenizer.nval;
//		System.out.println("priority " + priority);

		unscheduledTrainStreamTokenizer.nextToken();
		int maximumVelocity = (int) unscheduledTrainStreamTokenizer.nval;
//		System.out.println("maximumVelocity " + maximumVelocity);

		unscheduledTrainStreamTokenizer.nextToken();
		int startLoopId = (int) unscheduledTrainStreamTokenizer.nval;
//		System.out.println("startLoopId " + startLoopId);

		unscheduledTrainStreamTokenizer.nextToken();
		int endLoopId = (int) unscheduledTrainStreamTokenizer.nval;
//		System.out.println("endLoopId " + endLoopId);
		
		unscheduledTrainStreamTokenizer.nextToken();
		nTimeTableEntries = (int) unscheduledTrainStreamTokenizer.nval;

		for (int i = 0; i < nTimeTableEntries; i++) {
			unscheduledTrainStreamTokenizer.nextToken();
			unscheduledTrainStreamTokenizer.nextToken();
			unscheduledTrainStreamTokenizer.nextToken();
		}
		
		setTrainNo(trainNumber);
		setStartTime(arrivalTime);
		setDirection(direction);
		setLength(length);
		setAcceleration(acceleration);
		setDeceleration(deceleration);
		setPriority(priority);
		setVelocity(maximumVelocity);
	}
	
	public void readReferenceTableEntries(StreamTokenizer streamTokenizer,
			int nEntries) throws IOException {
		for (int i = 0; i < nEntries; i++) {

			streamTokenizer.nextToken();
			ReferenceTableEntry referenceTableEntry = new ReferenceTableEntry();
			referenceTableEntry.setStationName(String.valueOf(streamTokenizer.nval));

			streamTokenizer.nextToken();
			referenceTableEntry.setMinHaltTime((int) streamTokenizer.nval);


			refTables.add(referenceTableEntry);
		}
	}
	
//sneha added following function
	public void readTempReferenceTableEntries(StreamTokenizer streamTokenizer,
			int nEntries) throws IOException {
		for (int i = 0; i < nEntries; i++) {

			streamTokenizer.nextToken();
			ReferenceTableEntry referenceTableEntry = new ReferenceTableEntry();
			referenceTableEntry.tempstationName=String.valueOf(streamTokenizer.nval);

			streamTokenizer.nextToken();
			referenceTableEntry.setMinHaltTime((int) streamTokenizer.nval);


			tempRefTables.add(referenceTableEntry);
		}
	}


}

package gui.entities.sectionEntityList;

import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.trainEntities.ScheduledTrain;
import gui.entities.trainEntities.Train;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import simulator.dispatcher.FreightSimulator;
import simulator.input.SimulationInstance;
import simulator.util.Debug;

public class TrainList extends ArrayList<Train> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param trainNo
	 * @return Train having the corresponding trainNumber
	 */
	public Train getTrain(int trainNo) {
		Train trn;
		for (int i = 0; i < this.size(); i++) {
			trn = this.get(i);
			if (trn.getTrainNo() == trainNo) {
				return trn;
			}
		}
		
		return null;
	}

	/**
	 * @param trainNo
	 * @param time
	 * @param bu
	 *            block
	 * @return getTrainNew
	 */
	public Train getTrainNew(int trainNo, double time, Block block) {

		Train result = null;

		for (int i = 0; i < this.size(); i++) {
			Train train = this.get(i);
			if (train.getTrainNo() == trainNo) {

				for (int j = 0; j < train.getTimeTables().size(); j++) {

					if (((train.getTimeTables().get(j)).getLoopNo() == block
							.getBlockNo())) {

						if (((train.getTimeTables().get(j)).getArrivalTime() <= time)
								&& ((train.getTimeTables().get(j))
										.getDepartureTime() >= time)) {

							result = train;
						}

					}
				}
			}
		}

		return result;
	}

	public double getWeightedTraffic(int trainDirection) {
		double count = 0;
		double sumPriorities = 0;
		for (int i = 0; i < this.size(); i++) {
			Train simulatorTrain = this.get(i);
			if (simulatorTrain.getDirection() == trainDirection) {
				count++;
				double priority = 11 - simulatorTrain.getPriority();
				sumPriorities += priority;
			}
		}
		System.out.println("count " + count);
		return ((double) sumPriorities) / 11;
	}

	public void outputWeightedTrainTravel() {
		double totalTime = 0;
		for (int i = 0; i < this.size(); i++) {
			Train trn = this.get(i);
			double priority = 11 - trn.getPriority();
			totalTime += trn.totalTime() * priority;

		}

		double averageTime = totalTime / this.size() / 11;

		System.out.printf("WeightedAverageTravellingTime %6.3f\n", averageTime);
		System.out.printf("WeightedTotalTravellingTime %6.3f\n", totalTime);

	}

	public void outputTotalTrainTravel() {
		Debug.print("\n Train Traversal Time \n Train No  -  total time  -  traversal time");
		double totalTime = 0;
		for (int i = 0; i < this.size(); i++) {
			Train trn = this.get(i);
			totalTime += trn.totalTime();
			Debug.print(trn.getTrainNo() + "  -  " + trn.totalTime() + "  -  "
					+ trn.travelTime());
		}
		// Debug.print("Average Travelling time " +
		// totalTime/GlobalVar.arrayTrain.size());
		double averageTime = totalTime / this.size();

		System.out.println("AverageTravellingTime " + averageTime);
		System.out.println("TotalTravellingTime " + totalTime);
	}

	public void displayAverageTravellingTime(FreightSimulator freightSimulator) {

		double freighttotalTime = 0;
		int freightcount = 0;
		for (int i = 0; i < this.size(); i++) {
			Train trn = this.get(i);

			if (trn.getTimeTables().size() > 0)
					{	
				
				
					if(trn.isScheduled() == false) {
				// system.out.println(" LLL "+trn.trainNo+"  "+trn.operatingdays
				// );
				freighttotalTime += trn.totalTime();
				freightcount++;
			}
			// System.out.println(trn.trainNo + "  -  " + trn.totalTime() +
			// "  -  " + trn.travelTime() );
		}
		}
		if (freightcount != 0) {
			System.out.println( freightcount);

			System.out.println("Average Travelling time for freight trains  "
					+ freighttotalTime / freightcount);

			JOptionPane
					.showMessageDialog(
							null,
									"  Freight trains Scheduled successfully \n Average Travelling time for freight trains  "
									+ Double.toString(Math.floor((freighttotalTime / freightcount)*100)/100)
									+ " mins", "Simulator",
							JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void printTimeTable() {
		Train train;
		for (int m = 0; m < this.size(); m++) {
			train = this.get(m);
			train.printTimeTable();
		}
	}

	public void generateTrainArrayToSimulate(
			SimulationInstance simulationInstance) {

		Train simulatorTrain, train1;
		int trainArraySize = this.size();
		int siz1 = 0, siz2 = 0, abc = 0;

		Debug.print("start: arrayTrain size is   " + trainArraySize);

		for (int m = 0; m < trainArraySize; m++) {

			Debug.print("start: ");
			simulatorTrain = this.get(m);
			Debug.print("start: train.trainNo = " + simulatorTrain.getTrainNo());

			if (simulatorTrain.isScheduled()) {

				String rdays = ((ScheduledTrain) simulatorTrain)
						.getOperatingDays();
				if (rdays.equalsIgnoreCase("all")) {
					for (int simulationTimeInstance = 1; simulationTimeInstance < simulationInstance.simulationTime; simulationTimeInstance++) {
						train1 = new ScheduledTrain(simulatorTrain);

						train1.copyTimeTablesFromTrain(simulatorTrain,
								simulationTimeInstance,simulationInstance);
						this.add(train1);
						// System.out.println("added"+ train1.trainNo);
					}
				} else {
					Debug.print("start:  case of non-daily train :");
					StringTokenizer st = new StringTokenizer(rdays, ",");
					int counter = 0;
					while (st.hasMoreTokens()) {
						counter++;
						st.nextToken();
					}
					Debug.print("start: no of days is " + counter);

					int days[] = new int[counter];
					StringTokenizer s = new StringTokenizer(rdays, ",");
					counter = 0;
					while (s.hasMoreTokens()) {
						String str = s.nextToken();
						days[counter] = Integer.parseInt(str);
						counter++;
					}

					for (int simulationTimeInstance = 0; simulationTimeInstance < simulationInstance.simulationTime; simulationTimeInstance++) {
						for (int i = 0; i < counter; i++) {
							if ((simulationTimeInstance) % 7 == days[i]) {
								// System.out.println("start: operating day "
								// + days[i]);
								train1 = new ScheduledTrain(simulatorTrain);
								train1.copyTimeTablesFromTrain(simulatorTrain,
										simulationTimeInstance, simulationInstance);
								this.add(train1);
							}
						}
					}
				}
			}
		}

		/*
		 * for(int i=0;i<arrayTrain.size();i++) { train =
		 * (Train)arrayTrain.get(i) ; System.out.println(" train is "+i +"  "+
		 * train.trainNo+" :: "+train.operatingdays+" __ "+train.startTime); }
		 */

		siz1 = trainArraySize;
		siz2 = trainArraySize;
		for (int ji = 0; ji < siz1; ji++) {// mj

			for (int m = 0; m < siz2; m++) {
				Debug.print("start:  SIZE IS " + siz2);
				simulatorTrain = this.get(m);
				Debug.print("start:  ~~~ " + simulatorTrain.getTrainNo()
						+ " !! " + m);

				if (simulatorTrain.isScheduled()) {
					if (((ScheduledTrain) simulatorTrain).getOperatingDays()
							.equalsIgnoreCase("all")) {
						Debug.print("start: adk na");
					} else {
						Debug.print("start: removing train "
								+ simulatorTrain.getTrainNo());
						this.remove(simulatorTrain);
						abc = siz2;
						siz2 = -1;

						m -= 1;
					}
				}
			}
			siz2 = abc - 1;
		}
		/*
		 * System.out.println("AFTER THE DELETION ^^^^^^^^^^^^^^  "); for(int
		 * i=0;i<arrayTrain.size();i++) { train = (Train)arrayTrain.get(i) ;
		 * System.out.println(" train is "+i +"  "+
		 * train.trainNo+"  "+train.operatingdays+"   "+train.startTime); }
		 */

		Debug.print("start:  &&&&&&&&&&&&&&&&&&&&&&&&&& ");
		Train t;
		for (int i = 0; i < this.size(); i++) {
			t = this.get(i);
			Debug.print("start: train no is " + t.getTrainNo()
					+ "  arrival time is " + t.getStartTime());
			System.out.println("TrainList. Train no: "+ t.getTrainNo() + 
						" Train reference: " + t.getRefTables().size());
			for (int kp = 0; kp < t.getRefTables().size(); kp++) {
				if(t.isScheduled()){
				ReferenceTableEntry referenceTableEntry = t.getRefTables().get(
						kp);
				Debug.print("start:  arrival time "
						+ referenceTableEntry.getReferenceArrivalTime()
						+ " dept time "
						+ referenceTableEntry.getReferenceDepartureTime()
						+ " llopp " + referenceTableEntry.getReferenceLoopNo());
				}
			}
		}

		Debug.print("start: ");
		if (this.size() == 0) {
			System.out
					.println("start: THERE ARE NO TRAINS WITHIN THE SIMULATED TIME ");
		}

//		Collections.sort(this, new SortTrain());

	}

}

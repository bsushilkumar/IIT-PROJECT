package simulator.scheduler;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.time.PassengerDelay;
import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntities.trackProperties.BlockDirectionInInterval;
import gui.entities.sectionEntityList.GradientList;
import gui.entities.sectionEntityList.HashBlockTable;
import gui.entities.sectionEntityList.LoopList;
import gui.entities.sectionEntityList.PassengerDelayFormatList;
import gui.entities.sectionEntityList.StationList;
import gui.entities.sectionEntityList.TrainList;
import gui.entities.trainEntities.ScheduledTrain;
import gui.entities.trainEntities.Train;
import gui.entities.trainEntities.UnscheduledTrain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StreamTokenizer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import simulator.dispatcher.FreightSimulator;
import simulator.input.ChangeTimeTable;
import simulator.input.ReadSection;
import simulator.input.SimulationInstance;
import simulator.input.SortStation;
import simulator.outputFeatures.GraphFrame;
import simulator.util.Debug;

/**
 * Class file to start the simulator
 */
public class SimulationStart {
	
	public SimulationInstance simulationInstance = new SimulationInstance();
	/**
	 * nogui no graphical user interface.
	 */
	private boolean nogui = false;

	/**
	 * debug whether debugging mode is on or off.
	 */
	boolean debug = false;
	/**
	 * printOccupy if Occupy should be printed or not.
	 */
	boolean printOccupy = false;

	public void startSimulation(String[] args) {

		String filepath = null;
		@SuppressWarnings("unused")
		Debug debug = new Debug();

		processArguments(args);

		// Debug.assert(args.length>0,"Error : keyword expected 1 ");
		Debug.print(args[args.length - 1]);
		Debug.print("*********************** ");
		Debug.print(args[args.length - 1]);

		filepath = args[args.length - 1];
		String testCaseDirectory = args[args.length - 2];
		this.simulationInstance.testCaseDirectory = testCaseDirectory;

		Debug.print("filepath: " + filepath);

		ReadSection.readFiles(filepath, testCaseDirectory,
				this.simulationInstance);

		start();
	}

	/**
	 * Main function.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		SimulationStart simulationStart = new SimulationStart();
		String filepath = null;
		@SuppressWarnings("unused")
		Debug debug = new Debug();
		simulationStart.processArguments(args);

		// Debug.assert(args.length>0,"Error : keyword expected 1 ");
		Debug.print(args[args.length - 1]);
		Debug.print("*********************** ");
		Debug.print(args[args.length - 1]);

		filepath = args[args.length - 1];
		String testCaseDirectory = args[args.length - 2];
		SimulationInstance simulationInstance = simulationStart.simulationInstance;
		simulationInstance.testCaseDirectory = testCaseDirectory;

		Debug.print("filepath: " + filepath);
		Debug.print("filepath: assd" + filepath);
		ReadSection.readFiles(filepath, testCaseDirectory, simulationInstance);

		simulationStart.start();
	}

	/**
	 * Process the arguments.
	 * 
	 * @param args
	 *            arguments
	 */

	public void processArguments(String args[]) {
		new Debug();
		Debug.print("Processing Args");
		for (int i = 0; i < args.length; i++) {
			Debug.print(args[i]);
			setNogui((args[i].equals("-nogui")) ? true : isNogui());
			debug = (args[i].equals("-debug")) ? true : debug;

			if (args[i].equals("-train")) {
				Debug.debugTrain = Integer.parseInt(args[i + 1]);
			}

			if (args[i].equals("-op")) {
				Debug.setOutput(args[i + 1]);
			}

			// this is specific for the 3line case with a bidirectionalline
			if (args[i].equals("-3line")) {
				i++;
				System.out.println("3linemode activated");
				simulationInstance.TRAIN_TYPE_ALLOWED_ON_THIRD_LINE = args[i];
			}
			printOccupy = (args[i].equals("-printOccupy")) ? true : printOccupy;
		}
		Debug.debug = debug;
	}

	/**
	 * Generate integer random number between 1 and num
	 * 
	 * @param num
	 * @return random number
	 */
	public static int randomGenerator(int num) {
		Random generator = new Random();
		return generator.nextInt(num) + 1;
	}

	/**
	 * Start function.
	 * 
	 * @param simulationInstance
	 */
	public void start() {
		FreightSimulator fSim = readAndPrepareInput();
		int noOfPassengerTrains=fSim.getTrainList().size();
		if (fSim == null)
			return;

		if (isNogui() == true) {
			int numberOfSignalAspects = simulationInstance.numberOfSignalAspects;
			fSim.simulate(numberOfSignalAspects);
			 
//			 for (Train train : GlobalVar.trainArrayList) {
//			 train.printVelocityProfileForTrain();
//			 train.printPathBlockOccupancies();
//			 }

			ArrayList<Block> blocks = new ArrayList<Block>();
			// Block block101 = GlobalVar.getBlockFromBlockNo(101);
			// blocks.add(block101);
			// Block block102 = GlobalVar.getBlockFromBlockNo(102);
			// blocks.add(block102);
			// Block block12 = GlobalVar.getBlockFromBlockNo(12);
			// blocks.add(block12);

			// for (Block block : blocks) {
			// System.out
			// .println("Printing gradients and speed restrictions for block "
			// + block.getBlockNo()
			// + " start "
			// + block.getStartMilePost()
			// + " end "
			// + block.getEndMilePost());
			// for (Gradient gradient : block.getGradientList()) {
			// System.out.println("Gradient: start "
			// + gradient.getRelativeStartMilePost() + " end "
			// + gradient.getRelativeEndMilePost() + " value "
			// + gradient.getGradientValue());
			// }
			//
			// for (TinyBlock tinyBlockFormat : block.getTinyBlockList())
			// {
			// System.out.println("TinyBlock: start "
			// + tinyBlockFormat.getStartMilePost() + " end "
			// + tinyBlockFormat.getEndMilePost() + " maxSpeed "
			// + tinyBlockFormat.getMaxSpeed() + " accChange "
			// + tinyBlockFormat.getAccelerationChange()
			// + " decChange "
			// + tinyBlockFormat.getDecelerationChange());
			// }
			// }

			Debug.print("start: case 1");
			Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
			Debug.print("start: simulation is over ");
			Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
			if (printOccupy == true) {
				Debug.print("start: case 2");
				Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
				Debug.print("start: simulation is over ");
				Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
				simulationInstance.outputBlockOccupancies();
				double upWeightedTraffic = simulationInstance
						.getWeightedTraffic(0);
				double downWeightedTraffic = simulationInstance
						.getWeightedTraffic(1);

				// ut stands for uptraffic; dt stands for downTraffic
				// System.out.print("upTraffic123 " + upWeightedTraffic
				// + " downTraffic " + downWeightedTraffic + " ");
				System.out.printf("ut %6.3f" + " dt %6.3f ", upWeightedTraffic,
						downWeightedTraffic);
				simulationInstance.outputWeightedTrainTravel();
				simulationInstance.outputTotalTrainTravel();

			}

			Debug.print("start: case 3");
			Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
			Debug.print("start: simulation is over ");
			Debug.print("start:  ~~~~~~~~~~~~~~~~~ ");
			Debug.print("start: In Exit");
			if (simulationInstance.hasRailClipse) {

			} else {
				System.out.println("No SketchRail so quitting");
				// System.exit(0);
			}
		} else {
			//santhosh code for determining raw capacity
			if(GlobalVar.rawCapacitySelected){
				String freightTrainDir="";
				ArrayList<Block> blockArray=null;
				int lineCapacity=0;
				double maxTime=0.0;
				double leastTraversalTime=0.0;
				String criticalBlocks="";
				LoopList loopList = simulationInstance.getLoopList();
				String freightStartStationName=loopList.getStationName(GlobalVar.freightTrain.startLoopNo);
				String freightEndStationName=loopList.getStationName(GlobalVar.freightTrain.endLoopNo);
				fSim.getTrainList().clear();
				int trainNo=600;
				Train tempFreightTrain=GlobalVar.freightTrain;
				Station currStn;
				Train currTrain;
				double len, accParam, deceParam, maximumPossibleSpeed;
				int  dir;
				double priority;
				double startTime=0001;
				dir = tempFreightTrain.getDirection();
				if(dir==1){
					freightTrainDir="up";
				}else{
					freightTrainDir="down";
				}
				len = tempFreightTrain.length;
				accParam = tempFreightTrain.accParam;
				deceParam = tempFreightTrain.deceParam;
				priority = tempFreightTrain.priority;
				maximumPossibleSpeed = tempFreightTrain.maximumPossibleSpeed;
				int stLoopNo = tempFreightTrain.startLoopNo;
				int enLoopNo = tempFreightTrain.endLoopNo;
				int numberOfHalts = tempFreightTrain.getNumberofHalts();
				
				currTrain = new UnscheduledTrain(dir, startTime, len, accParam,
						deceParam, priority);
				currTrain.setTrainNo(trainNo);
				currTrain.setStartLoopNo(stLoopNo);
				currTrain.setEndLoopNo(enLoopNo);
				currTrain.setEndStation(loopList.getStationName(currTrain
						.getEndLoopNo()));
				currTrain.setNumberofHalts(numberOfHalts);
				for(int i =0; i<numberOfHalts; i++){
					String haltStation = tempFreightTrain.
							getRefTables().get(i).getStationName();
					int haltMinutes= (int) tempFreightTrain.
							getRefTables().get(i).getMinHaltTime();
					ReferenceTableEntry lastRefEntry = new ReferenceTableEntry(haltStation,haltMinutes);
					currTrain.getRefTables().add(lastRefEntry);					
				}
				if (currTrain.getEndStation() == null) {
					currTrain.setEndStation("");
				}
				currTrain.setMaximumPossibleSpeed(maximumPossibleSpeed/60);
				currTrain.setScheduled(false);
				fSim.getTrainList().add(currTrain);
				while (fSim.getCurrentTrainNo() < fSim
						.getTrainList().size()) {
	
					// freightSimulator.simulate();
					
					int val = fSim
							.simulateNextTrain(simulationInstance.numberOfSignalAspects);
					
					/*if (val == -1)
						break;*/
					
				}
				Train trn =  fSim
						.getTrainList().get(0);
				blockArray=trn.getBlocksByMaxTimeTaken(simulationInstance);
				maxTime=blockArray.get(0).getOccupancyTimeByTrain(trn);
				for(int s=0;s<blockArray.size();s++){
					Block tempBlock=blockArray.get(s);
					String tempCriticalBlockNo=String.valueOf(tempBlock.getBlockNo());
					criticalBlocks=criticalBlocks+tempCriticalBlockNo;
				}
				System.out.println("Critical blocks are "+criticalBlocks);
				lineCapacity=(int)Math.round((60/maxTime)*24*.7);
				if(blockArray.size()>1)
				JOptionPane
				.showMessageDialog(
						null,
								" The critical blocks in the "+freightStartStationName+" - "+freightEndStationName+" section are "+criticalBlocks+" and the raw capacity is "+lineCapacity, "Simulator",
						JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane
					.showMessageDialog(
							null,
									" The critical block in the "+freightStartStationName+" - "+freightEndStationName+" section is "+criticalBlocks+" and the raw capacity is "+lineCapacity, "Simulator",
							JOptionPane.INFORMATION_MESSAGE);
			}else if(GlobalVar.capacitySelected==true){
			//getting startTime of all scheduled trains
			LoopList loopList = simulationInstance.getLoopList();
			String freightStartStationName=loopList.getStationName(GlobalVar.freightTrain.startLoopNo);
			String freightEndStationName=loopList.getStationName(GlobalVar.freightTrain.endLoopNo);
			int tempLoopNo;
			String tempStationName;
			String freightTrainDir="";
			Loop tempLoop;
			int timeDiff=0;
			double tempStartTime=0.0;
			boolean firstSimulationRun=true;
			ArrayList modifiedTrainList=null;
			ArrayList<Double> startTimeArray=new ArrayList<Double>();
			TrainList tempTrainList=simulationInstance.trainList;
			for (int m=0; m<tempTrainList.size();m++){
				Train tempTrain=tempTrainList.get(m);
				if(tempTrain.getDirection()==1){
					tempLoopNo=tempTrain.getStartLoopNo();
					tempStationName=loopList.getStationName(tempLoopNo);
					if(tempStationName.equals(freightStartStationName)){
					   tempStartTime=tempTrain.getStartTime();
					   startTimeArray.add(tempStartTime);
					}
				}
			}
			System.out.println("size of passenger start time array: "+startTimeArray.size());
			// PassengerStartTime of all the trains at the start station is added
			int initialNoOfFrTrains=200;
			int headwayScheduled=GlobalVar.headwayScheduled;
			int headwayFreight=GlobalVar.headwayFreight;
			double randomStTime=0.0;
			ArrayList<Double> freightStartTimeArray=null;
			ArrayList<Double> randFrStTimeArray=new ArrayList<Double>();
			ArrayList<Double> tempFreightTimeArray=null;
			double leastTraversalTime=0.0;
			int counter=0;
			double capacity=0;
			int lowerTraverseTimeFactor=0;
			int higherTraverseTimeFactor=0;
			int newTraverseTime=initialNoOfFrTrains;
			int baseValue=0;
			int counter1=0;
			int randomNoCounter=0;
			double avgFreightTraverseTime=0.0;
			HashMap<Double,Double> traversalTimeMap=null;
			int scheduledTrainCounter=0;
			if(firstSimulationRun){
				fSim.getTrainList().clear();
				int trainNo=600;
				Train tempFreightTrain=GlobalVar.freightTrain;
				Station currStn;
				Train currTrain;
				double len, accParam, deceParam, maximumPossibleSpeed;
				double priority;
				int dir;
				double startTime=0001;
				dir = tempFreightTrain.getDirection();
				if(dir==1){
					freightTrainDir="up";
				}else{
					freightTrainDir="down";
				}
				len = tempFreightTrain.length;
				accParam = tempFreightTrain.accParam;
				deceParam = tempFreightTrain.deceParam;
				priority = tempFreightTrain.priority;
				maximumPossibleSpeed = tempFreightTrain.maximumPossibleSpeed;
				int stLoopNo = tempFreightTrain.startLoopNo;
				int enLoopNo = tempFreightTrain.endLoopNo;
				int numberOfHalts = tempFreightTrain.getNumberofHalts();
				
				currTrain = new UnscheduledTrain(dir, startTime, len, accParam,
						deceParam, priority);
				currTrain.setTrainNo(trainNo);
				currTrain.setStartLoopNo(stLoopNo);
				currTrain.setEndLoopNo(enLoopNo);
				currTrain.setEndStation(loopList.getStationName(currTrain
						.getEndLoopNo()));
				if (currTrain.getEndStation() == null) {
					currTrain.setEndStation("");
				}
				currTrain.setNumberofHalts(numberOfHalts);
				for(int i =0; i<numberOfHalts; i++){
					String haltStation = tempFreightTrain.
							getRefTables().get(i).getStationName();
					int haltMinutes= (int) tempFreightTrain.
							getRefTables().get(i).getMinHaltTime();
					ReferenceTableEntry lastRefEntry = new ReferenceTableEntry(haltStation,haltMinutes);
					currTrain.getRefTables().add(lastRefEntry);					
				}
				currTrain.setMaximumPossibleSpeed(maximumPossibleSpeed/60);
				currTrain.setScheduled(false);
				/**currTrain.numberofhalts = 4;
				ReferenceTableEntry lastRefEntry;
				String[] haltStation = {"Allahabad", "Kanpur-Freight", "Juhi-GMC", "Tundla"};
				Integer[] haltTime = { 65, 5, 70, 55};
				//String[] haltStation = {"Allahabad"};
				//Integer[] haltTime = {120};
				for ( int j = 0; j < currTrain.numberofhalts; j++){
					lastRefEntry = new ReferenceTableEntry(haltStation[j],
							haltTime[j]);
					currTrain.getRefTables().add(lastRefEntry);
				}**/
				
				fSim.getTrainList().add(currTrain);
				while (fSim.getCurrentTrainNo() < fSim
						.getTrainList().size()) {
	
					// freightSimulator.simulate();
					
					int val = fSim
							.simulateNextTrain(simulationInstance.numberOfSignalAspects);
					
					/*if (val == -1)
						break;*/
					
				}
				Train trn =  fSim
						.getTrainList().get(0);
				leastTraversalTime=trn.totalTime();
				//simulationInstance.printResultsInCsv();
				System.out.println("least traversal time is "+leastTraversalTime);
				
			}
			while(initialNoOfFrTrains<=200){
				simulationInstance=new SimulationInstance();
				fSim = readAndPrepareInput();
				freightStartTimeArray=new ArrayList<Double>();
				while(randomStTime<=1440){
					if(randomNoCounter>300000){
						newTraverseTime=freightStartTimeArray.size();
						randomNoCounter=0;
						break;
					}
					randomNoCounter=randomNoCounter+1;
					
					counter=0;
					randomStTime= Math.round(0+Math.random()*1440);
					
					for(int p=0;p<startTimeArray.size();p++){
						timeDiff=(int)Math.abs(startTimeArray.get(p)-randomStTime);
						if(timeDiff<headwayScheduled){
							break;
						}/**else if(randomStTime>=720 && randomStTime <= 850){ 
							break;  //Maintenance Block at Mughalsarai
						}else if(randomStTime>=0 && randomStTime <= 60){ 
							break;  //Maintenance Block at Kanpur
						}else if(randomStTime>=1380 && randomStTime <= 1440){ 
							break;  //Maintenance Block at Kanpur
						}**/
						else{
							counter=counter+1;
						}
					}
					if (counter==startTimeArray.size() && freightStartTimeArray.size()<=initialNoOfFrTrains && !freightStartTimeArray.contains(randomStTime)){
						if(freightStartTimeArray.size()==0){
							freightStartTimeArray.add(randomStTime);
						}else{
							counter1=0;
							for(int y=0;y<freightStartTimeArray.size();y++){
								if(Math.abs(randomStTime-freightStartTimeArray.get(y))<headwayFreight){
									break;
								}else{
									counter1=counter1+1;
								}
							}
							if(counter1==freightStartTimeArray.size()){
								freightStartTimeArray.add(randomStTime);
							}
						}
					}
				  
					if(freightStartTimeArray.size()==initialNoOfFrTrains){
						break;
					}
				}
				System.out.println("size of freight start time array: "+freightStartTimeArray.size());
				Collections.sort(freightStartTimeArray);
				//generate no of freight trains equal to the size of freightStarttimeArray
				/*simulationInstance=new SimulationInstance();
				fSim = readAndPrepareInput();*/
				
				modifiedTrainList=new ArrayList();
				int trainNo=600;
				Train tempFreightTrain=null;
				for(int g=0;g<freightStartTimeArray.size();g++){
					//create train start
					tempFreightTrain=GlobalVar.freightTrain;
					Station currStn;
					Train currTrain;
					double len, accParam, deceParam, maximumPossibleSpeed;
					double priority;
					int dir;
					trainNo=trainNo+1;
					double startTime=freightStartTimeArray.get(g);
					dir = tempFreightTrain.getDirection();
					if(dir==1){
						freightTrainDir="up";
					}else{
						freightTrainDir="down";
					}
					len = tempFreightTrain.length;
					accParam = tempFreightTrain.accParam;
					deceParam = tempFreightTrain.deceParam;
					priority = tempFreightTrain.priority;
					maximumPossibleSpeed = tempFreightTrain.maximumPossibleSpeed;
					int stLoopNo = tempFreightTrain.startLoopNo;
					int enLoopNo = tempFreightTrain.endLoopNo;
					int numberOfHalts = tempFreightTrain.getNumberofHalts();
					
					currTrain = new UnscheduledTrain(dir, startTime, len, accParam,
							deceParam, priority);
					currTrain.setTrainNo(trainNo);
					currTrain.setStartLoopNo(stLoopNo);
					currTrain.setEndLoopNo(enLoopNo);
					currTrain.setEndStation(loopList.getStationName(currTrain
							.getEndLoopNo()));
					if (currTrain.getEndStation() == null) {
						currTrain.setEndStation("");
					}
					currTrain.setMaximumPossibleSpeed(maximumPossibleSpeed/60);
					currTrain.setScheduled(false);
					/**currTrain.numberofhalts = 4;
					ReferenceTableEntry lastRefEntry;
					String[] haltStation = {"Allahabad", "Kanpur-Freight", "Juhi-GMC", "Tundla"};
					//String[] haltStation = {"Allahabad"};
					//Integer[] haltTime = {120};
					Integer[] haltTime = {65, 5, 70, 55};
					for ( int j = 0; j < currTrain.numberofhalts; j++){
						lastRefEntry = new ReferenceTableEntry(haltStation[j],
								haltTime[j]);
						currTrain.getRefTables().add(lastRefEntry);
					}**/
					currTrain.setNumberofHalts(numberOfHalts);
					for(int i =0; i<numberOfHalts; i++){
						String haltStation = tempFreightTrain.
								getRefTables().get(i).getStationName();
						int haltMinutes= (int) tempFreightTrain.
								getRefTables().get(i).getMinHaltTime();
						ReferenceTableEntry lastRefEntry = new ReferenceTableEntry(haltStation,haltMinutes);
						currTrain.getRefTables().add(lastRefEntry);					
					}
					modifiedTrainList.add(currTrain);
				}
				
				fSim.getTrainList().addAll(modifiedTrainList);
				//simulate the trains
				scheduledTrainCounter=0;
					while (fSim.getCurrentTrainNo() < fSim
							.getTrainList().size()) {
		
						// freightSimulator.simulate();
						Train tempTrain=fSim.getTrainList().get(fSim.getCurrentTrainNo());
						int trnDir=tempTrain.getDirection();
					
							if(tempTrain.isScheduled()==true && trnDir==tempFreightTrain.getDirection()){
								scheduledTrainCounter=scheduledTrainCounter+1;
							}
							int val = fSim
									.simulateNextTrain(simulationInstance.numberOfSignalAspects);
							if (val == -1)
								break;
							

					}
				//find the average traversal time of all freight trains
					double freighttotalTime = 0;
					int freightcount = 0;
					ArrayList<Double> traversalTimeArray =new ArrayList<Double>();
					traversalTimeMap=new HashMap<Double,Double>();
					
					for (int i = 0; i < fSim
							.getTrainList().size(); i++) {
							Train trn =  fSim
									.getTrainList().get(i);
		
							if (trn.getTimeTables().size() > 0)
									{	
								
								
									if(trn.isScheduled() == false) {
								// system.out.println(" LLL "+trn.trainNo+"  "+trn.operatingdays
								// );
								freighttotalTime += trn.totalTime();
								traversalTimeArray.add(trn.totalTime());
								System.out.println(" Start Time: " + trn.startTime + "  -  " + trn.totalTime() +
										 "  -  " + trn.travelTime() +" Speed: "+
										trn.maximumPossibleSpeed+" End Loop: "+ trn.getEndLoopNo());
								traversalTimeMap.put(trn.startTime, trn.totalTime());
								freightcount++;						
								
							}
							
							// System.out.println(trn.trainNo + "  -  " + trn.totalTime() +
							// "  -  " + trn.travelTime() );
						}
					}
					avgFreightTraverseTime=freighttotalTime/freightcount;
					System.out.println("AvgFreightTraverseTime = " + avgFreightTraverseTime);
					//simulationInstance.printResultsInCsv();
					
					/*if(firstSimulationRun){
						Collections.sort(traversalTimeArray);
						leastTraversalTime=traversalTimeArray.get(0);
						firstSimulationRun=false;
					}*/
					
					//fSim.getTrainList().removeAll(modifiedTrainList);
					System.out.println(" baseValue: "+baseValue+ " lowerTraverseTimeFactor: "
							+ lowerTraverseTimeFactor+ " higherTraverseTimeFactor :"+
							higherTraverseTimeFactor + " newTraverseTime :" +
							newTraverseTime + " initialNoOfFrTrains: " + initialNoOfFrTrains);
					if(avgFreightTraverseTime>2.1*leastTraversalTime){
						baseValue=lowerTraverseTimeFactor;
						higherTraverseTimeFactor=newTraverseTime;
						if(higherTraverseTimeFactor==baseValue){
							capacity=freightStartTimeArray.size();
							break;
						}
						newTraverseTime=(higherTraverseTimeFactor+baseValue)/2;
						initialNoOfFrTrains=newTraverseTime;
						System.out.println(" 2.1 baseValue: "+baseValue+ " lowerTraverseTimeFactor: "
								+ lowerTraverseTimeFactor+ " higherTraverseTimeFactor :"+
								higherTraverseTimeFactor + " newTraverseTime :" +
								newTraverseTime + " initialNoOfFrTrains: " + initialNoOfFrTrains);
						
						
					}else if(avgFreightTraverseTime<1.9*leastTraversalTime){
						baseValue=higherTraverseTimeFactor;
						lowerTraverseTimeFactor=newTraverseTime;
						if(lowerTraverseTimeFactor==baseValue){
							capacity=freightStartTimeArray.size();
							break;
						}
						newTraverseTime=(lowerTraverseTimeFactor+baseValue)/2;
						initialNoOfFrTrains=newTraverseTime;
						System.out.println(" 1.9 baseValue: "+baseValue+ " lowerTraverseTimeFactor: "
								+ lowerTraverseTimeFactor+ " higherTraverseTimeFactor :"+
								higherTraverseTimeFactor + " newTraverseTime :" +
								newTraverseTime + " initialNoOfFrTrains: " + initialNoOfFrTrains);
						}else{
						capacity=freightStartTimeArray.size();
						break;
					}
				
			}
			
			long travTime=Math.round(avgFreightTraverseTime);
			 double hh=Math.floor(travTime/60);
			 long mm=Math.floorMod(travTime, 60);
			 String hour=String.format("%02d",(int)hh);
			 String minute=String.format("%02d",(int)mm);
			 
			JOptionPane
			.showMessageDialog(
					null,
							" On the "+freightStartStationName+" - "+freightEndStationName+" section, in addition to "+scheduledTrainCounter+" passenger trains in the "+freightTrainDir+" direction \n that are timetabled, the section admits "+Math.round(capacity)+" freight trains in the "+freightTrainDir+" direction whose paths are computed. \n The average traversal time on the section is "+hour+" : "+minute+" (hours and minutes) ", "Simulator",
					JOptionPane.INFORMATION_MESSAGE);
			Set bestFreightTimeSet=traversalTimeMap.keySet();
			ArrayList<Double> bestFreightTimeArray=new ArrayList<Double>(bestFreightTimeSet);
			String[] columnNames = {"S.No","Departure Time","Traversal Time (Mins)"};
			Object[][] data=new Object[bestFreightTimeArray.size()][3];
			Iterator<Double> itr=bestFreightTimeSet.iterator();
			int rowNo=0;
			for(int j=0;j<bestFreightTimeArray.size();j++){
				long bestTravTime=Math.round(bestFreightTimeArray.get(j));
				 double tempHH=Math.floor(bestTravTime/60);
				 long tempMM=Math.floorMod(bestTravTime, 60);
				 String HHstring=String.format("%02d",(int)tempHH);
				 String MMstring=String.format("%02d",(int)tempMM);
				 String timeString=HHstring+":"+MMstring;
				data[j][0]=j+1;
				data[j][1]=timeString;
				data[j][2]=Math.round(traversalTimeMap.get(bestFreightTimeArray.get(j)));
			}
			
			
			 JFrame jframe=new JFrame();
			 jframe.setSize(600, 700);
			 JTable jtable= new JTable(data,columnNames);
			 JScrollPane scrollPane = new JScrollPane(jtable);
			 jtable.setFillsViewportHeight(true);
			 jframe.getContentPane().add(scrollPane);
			 scrollPane.setVisible(true);
			 jframe.setTitle("Freight Paths");
			 jframe.setVisible(true);
	         System.out.println("capacity of the section is "+freightStartTimeArray.size());
	         GraphFrame gf = new GraphFrame(fSim, simulationInstance);
	         simulationInstance.graphFrame = gf;
		//santhosh - code for generation of timetables end
			}else{
			GraphFrame gf = new GraphFrame(fSim, simulationInstance);
			simulationInstance.graphFrame = gf;
			}
			
			//new algorithm suggested by Ayush
			/*if(GlobalVar.capacitySelected==true){
				//getting startTime of all scheduled trains
				LoopList loopList = simulationInstance.getLoopList();
				String freightStartStationName=loopList.getStationName(GlobalVar.freightTrain.startLoopNo);
				int tempLoopNo;
				String tempStationName;
				
				Loop tempLoop;
				int timeDiff=0;
				int absTimeDiff=0;
				double tempStartTime=0.0;
				boolean firstSimulationRun=true;
				ArrayList<Double> startTimeArray=new ArrayList<Double>();
				TrainList tempTrainList=simulationInstance.trainList;
				HashMap<Double,Double> traversalTimeMap=null;
				for (int m=0; m<tempTrainList.size();m++){
					Train tempTrain=tempTrainList.get(m);
					if(tempTrain.getDirection()==1){
						tempLoopNo=tempTrain.getStartLoopNo();
						tempStationName=loopList.getStationName(tempLoopNo);
						if(tempStationName.equals(freightStartStationName)){
						   tempStartTime=tempTrain.getStartTime();
						   startTimeArray.add(tempStartTime);
						}
					}
				}
				Collections.sort(startTimeArray);
				System.out.println("size of passenger start time array: "+startTimeArray.size());
				ArrayList<Double> freightStartTimeArray=null;
				freightStartTimeArray=new ArrayList<Double>();
				double tempFreightStartTime=0.0;
				int counter;
				int p=0;
				traversalTimeMap=new HashMap<Double,Double>(); 
				int headwayScheduled=GlobalVar.headwayScheduled;
				int headwayFreight=GlobalVar.headwayFreight;
				for(int t=1;t<1400;t++){
					counter=0;
					tempFreightStartTime=t ;
					if(p<startTimeArray.size()){
						absTimeDiff=(int)Math.abs(startTimeArray.get(p)-tempFreightStartTime);
						timeDiff=(int)(startTimeArray.get(p)-tempFreightStartTime);
					}else{
						absTimeDiff=headwayScheduled;
					}
				
					if(absTimeDiff<headwayScheduled){
						p=p+1;
						t=t+(headwayScheduled+timeDiff-1);
					}else{
						freightStartTimeArray.add(tempFreightStartTime);
						t=t+headwayFreight-1;
					}
					
				}
				System.out.println("size of freight start time array: "+freightStartTimeArray.size());
				Collections.sort(freightStartTimeArray);
				int trainNo=600;
				double freightTraversalTime=0.0;
				for (int k=0;k<freightStartTimeArray.size();k++){
					simulationInstance=new SimulationInstance();
					fSim = readAndPrepareInput();
					//create train start
					Train tempFreightTrain=GlobalVar.freightTrain;
					Station currStn;
					Train currTrain;
					double len, accParam, deceParam, maximumPossibleSpeed;
					int priority, dir;
					trainNo=trainNo+1;
					double startTime=freightStartTimeArray.get(k);
					dir = tempFreightTrain.getDirection();
					len = tempFreightTrain.length;
					accParam = tempFreightTrain.accParam;
					deceParam = tempFreightTrain.deceParam;
					priority = tempFreightTrain.priority;
					maximumPossibleSpeed = tempFreightTrain.maximumPossibleSpeed;
					int stLoopNo = tempFreightTrain.startLoopNo;
					int enLoopNo = tempFreightTrain.endLoopNo;
					currTrain = new UnscheduledTrain(dir, startTime, len, accParam,
							deceParam, priority);
					currTrain.setTrainNo(trainNo);
					currTrain.setStartLoopNo(stLoopNo);
					currTrain.setEndLoopNo(enLoopNo);
					currTrain.setEndStation(loopList.getStationName(currTrain
							.getEndLoopNo()));
					if (currTrain.getEndStation() == null) {
						currTrain.setEndStation("");
					}
					currTrain.setMaximumPossibleSpeed(maximumPossibleSpeed/60);
					currTrain.setScheduled(false);
					fSim.getTrainList().add(currTrain);
					//simulate the trains
					int trainIndex=0;
					while (fSim.getCurrentTrainNo() < fSim
							.getTrainList().size()) {
		
						// freightSimulator.simulate();
						if(fSim.getTrainList().get(fSim.getCurrentTrainNo()).getDirection()==1){
						int val = fSim
								.simulateNextTrain(simulationInstance.numberOfSignalAspects);
						
						if (val == -1)
							break;
						}else{
							trainIndex=fSim.getCurrentTrainNo();
							trainIndex=trainIndex+1;
							fSim.setCurrentTrainNo(trainIndex);
						}
						
					}
				//find the average traversal time of all freight trains
					double freighttotalTime = 0;
					int freightcount = 0;
					
					
					int noOfScheduled=fSim.getTrainList().size()-1;
					freightTraversalTime=fSim.getTrainList().get(noOfScheduled).totalTime();
					traversalTimeMap.put(freightStartTimeArray.get(k),freightTraversalTime);
					

				}
				//Sort the hashmap to get the least value
				List traverseTimeSet=new ArrayList(traversalTimeMap.values());
		        Collections.sort(traverseTimeSet);
		        double leastTraversalTime=(double)traverseTimeSet.get(0);
		        HashMap<Double,Double> bestFreightDepTimes=new HashMap<Double,Double>();
		        Set departureTimeSet=traversalTimeMap.keySet();
		        Iterator itr=departureTimeSet.iterator();
		        while(itr.hasNext()){
		        	double tempKey=(double)itr.next();
		        	double tempVal=(double)traversalTimeMap.get(tempKey);
		        	if(tempVal<=2*leastTraversalTime){
		        		bestFreightDepTimes.put(tempKey,tempVal);
		        	}
		        }
		        System.out.println("Capacity of the section is "+ bestFreightDepTimes.size());
		        JOptionPane
				.showMessageDialog(
						null,
								"  Capacity of the section  "
								+ bestFreightDepTimes.size(), "Simulator",
						JOptionPane.INFORMATION_MESSAGE);
				}else{
					GraphFrame gf = new GraphFrame(fSim, simulationInstance);
					simulationInstance.graphFrame = gf;
					}*/
			}
			
		}

	

	private FreightSimulator readAndPrepareInput() {

		// FileOutputStream fout;

		// ArrayList TimeTable = new ArrayList();
		// ArrayList runTime = new ArrayList();
		if(GlobalVar.simulationInstance!=null){
			this.simulationInstance=GlobalVar.simulationInstance;
		}
		StationList stationList = simulationInstance.stationList;
		TrainList trainArray = simulationInstance.trainList;
		HashBlockTable hashBlockTable = simulationInstance.hashBlockTable;

		// ArrayList old_arrayTrain = new ArrayList();

		/* ---------------- Important parameters ----------------------- */

		ReadSection.readParameters(simulationInstance);

		try {

			stationList = ReadSection.readStations();
			simulationInstance.setStationList(stationList);

			System.out.println("start: " + stationList.size()
					+ " Stations are read");
			Debug.print("start: STATION IS READ ");
			Collections.sort(stationList, new SortStation());

			LoopList loopList = ReadSection.readSection(hashBlockTable,
					stationList);

			simulationInstance.setLoopList(loopList);
			Debug.print("start: LOOP ARRAY IS READ ");
			System.out.println("start: loops and blocks are read");

			// Block bi = new Block();
			// Debug.print("start: SECTION IS READ ");

			ReadSection.linkStations(stationList);

			simulationInstance.convertLinks();
			System.out.println("start: linkBlocks over.");

			simulationInstance.setStationList(stationList);
			
			trainArray = Train.readTrain(simulationInstance, nogui);
			simulationInstance.setTrainList(trainArray);
			System.out.println("SimulationStart: trainArray size is "
					+ trainArray.size());
			//sneha:called function to generate new train schedule_list
			printModifiedFormatTrainschedule();
			// for (Train train : trainArray) {
			// System.out.println("Trains inserted " + train.getTrainNo());
			// }

		} catch (Exception e) {
			System.out.println("start: Error reading input " + e.getMessage());
			e.printStackTrace();
			return null;
		}

		/************************** Code starts here by Chandra ***************************/

		Debug.print("start: Code starts here for passenger train Delay ");
		//santhosh
		/*try {
			PassengerDelayFormatList passengerDelayFormatList = PassengerDelay
					.readDelay();
			simulationInstance.setPassengerDelayList(passengerDelayFormatList);
		} catch (Exception e) {
			Debug.print("start: Could not read passenger train delay of section ");
		}*/

		// Devendra
		/** Read the values for BlockDirectionInInterval */
		try {
			if (simulationInstance.hasBlockDirectionFile) {
				BlockDirectionInInterval
						.readBlockDirectionInIntervalFile(simulationInstance);
			}
		} catch (IOException e) {
			Debug.print("SimulationStart: BlockDirectionInInterval.txt does not exist");
			e.printStackTrace();
		}

		GradientList.readGradientAndGenerateTinyBlocks(simulationInstance);

		PassengerDelay.setDelayParametersForTrains(trainArray,
				simulationInstance);
		simulationInstance.generateTrainArrayToSimulate();
		FreightSimulator fSim = new FreightSimulator(trainArray,
				simulationInstance);
		simulationInstance.freightSimulator = fSim;

		return fSim;
	}

	//sneha added following new function
	void readScheduledTrains(ArrayList<Train> trainList)
			throws IOException {
		String trainFileName = FileNames.getScheduledTrainsFileName();
		StreamTokenizer streamTokenizer = new StreamTokenizer(new FileReader(trainFileName));
		streamTokenizer.slashSlashComments(true);
		streamTokenizer.slashStarComments(true);
		
		while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			Train train = new ScheduledTrain();
			train.scheduled = true;

			train.trainNo = (int) streamTokenizer.nval;
		
			
			streamTokenizer.nextToken();
			
			train.direction = GlobalVar.getDirectionFromDirectionString(streamTokenizer.sval);
		

			streamTokenizer.nextToken();
			train.length = streamTokenizer.nval;
		
			

			streamTokenizer.nextToken();
			train.accParam = streamTokenizer.nval;

			

			streamTokenizer.nextToken();
			train.deceParam = streamTokenizer.nval;

			
			

			streamTokenizer.nextToken();
			train.priority = (double) streamTokenizer.nval;

			

			streamTokenizer.nextToken();
			train.maximumPossibleSpeed = (int) streamTokenizer.nval;

			

			streamTokenizer.nextToken();
			ScheduledTrain sTrain = (ScheduledTrain) train;
			train.operatingDays = streamTokenizer.sval;

			

			streamTokenizer.nextToken();
			train.numberofhalts = (int) streamTokenizer.nval;

			
			
			//Sneha modified following line to give sTain.numberofhalts as parameters otherwise
			//gives exception
			//sTrain.readReferenceTableEntries(streamTokenizer, nEntries);
			sTrain.readTempReferenceTableEntries(streamTokenizer, train.numberofhalts);
			

			trainList.add(sTrain);

		}

		
	}
//sneha added following new function
	 void readUnscheduledTrains(ArrayList<Train> trainList)
			throws IOException {
		String trainFileName = FileNames.getUnscheduledTrainsFileName();
		StreamTokenizer streamTokenizer = new StreamTokenizer(new FileReader(trainFileName));
		streamTokenizer.slashSlashComments(true);
		streamTokenizer.slashStarComments(true);

		while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			UnscheduledTrain train = new UnscheduledTrain();
			train.trainNo = (int) streamTokenizer.nval;

			streamTokenizer.nextToken();
			train.direction = GlobalVar
					.getDirectionFromDirectionString(streamTokenizer.sval);

			streamTokenizer.nextToken();
			train.startTimeInput = (int) streamTokenizer.nval;

			streamTokenizer.nextToken();
			train.length = streamTokenizer.nval;

			streamTokenizer.nextToken();
			train.accParam = streamTokenizer.nval;

			streamTokenizer.nextToken();
			train.deceParam = streamTokenizer.nval;

			streamTokenizer.nextToken();
			train.priority = (double) streamTokenizer.nval;

			streamTokenizer.nextToken();
			train.maximumPossibleSpeed = (int) streamTokenizer.nval;

			streamTokenizer.nextToken();
			train.startLoopNo = (int) streamTokenizer.nval;

			streamTokenizer.nextToken();
			train.endLoopNo = (int) streamTokenizer.nval;

			//System.out.println("endloop " + train.endLoopNo);
			
			streamTokenizer.nextToken();
			train.numberofhalts = (int) streamTokenizer.nval;

			train.readTempReferenceTableEntries(streamTokenizer, train.numberofhalts);
			
			trainList.add(train);
		}
	}
	
	
	//sneha: added following new function
	 void printModifiedFormatTrainschedule(){
		Train train1;
		this.simulationInstance=GlobalVar.simulationInstance;
		TrainList tempTrainList=simulationInstance.tempTrainList ;
		
		

			System.out.println("sneha :in print modified");
		try {
			readScheduledTrains(tempTrainList);
			readUnscheduledTrains(tempTrainList);
			String fileName = FileNames.getTestCaseDirectory() + "/" + "Schedule_new.csv";
			File file = new File(fileName);
			OutputStream f2 = new FileOutputStream(file);
			PrintStream bPrintStream = new PrintStream(f2);
			bPrintStream.println(String.format("%-10s","T.No.") + "\t" 
								+ String.format("%-6s" ,"Dir")  
								+ String.format("%-6s","Len") + "\t"  
								+ String.format("%-6s","Acc") + "\t"
								+ String.format("%-6s","Dec") + "\t"
								+ "Pri" + "\t\t"
								+ "MaxSpeed" + "\t"
								+ "Op.D"  + "\t\t"
								+ "Pr.Halts" + "\t\t"
								+ "stnName" + "\t"
								+ "Loop" + "\t\t" 
								+ "A.Time" + "\t" 
								+ "D.Time");

			ArrayList<Station> tempstationList=GlobalVar.simulationInstance.getStationList();
			
			for (int m = 0; m < tempTrainList.size(); m++) {
				train1 = tempTrainList.get(m);
				System.out.println("train no" + train1.trainNo);
				bPrintStream.print(String.format("%-10s",train1.getTrainNo()) + "\t");
				bPrintStream.print(train1.getDirectionString() + "\t");
				String str1=null;
				str1=String.format("%.4f",train1.length);
				bPrintStream.print( str1 + "\t");
				str1=String.format("%.4f",train1.getAccParam());
				bPrintStream.print(str1 + "\t");
				str1=String.format("%.4f",train1.getDeceParam());
				bPrintStream.print(str1 + "\t");
				str1=String.format("%.3f",train1.priority);
				bPrintStream.print(str1 + "\t\t");
				str1=String.format("%.3f",train1.maximumPossibleSpeed);

				
				bPrintStream.print( str1 + "\t\t");
				bPrintStream.print(train1.operatingDays + "\t\t");
				str1=String.format("%02d",train1.numberofhalts );
				bPrintStream.print( str1 + "\t");
								
				
				for (int n = 0; n < train1.getTempRefTables().size(); n++) {
					 
					if(train1.isScheduled()){
						Integer stationPos=Integer.parseInt(String.format("%07d",train1.tempRefTables.get(n).refLoopNo).substring(0,3));
						String stName=(tempstationList.get(stationPos-1).stationName);
						bPrintStream.print( String.format("%20s",stName)+ "\t");
						bPrintStream.print(String.format("%07d",(train1.tempRefTables.get(n).refLoopNo)) + "\t");
						int aTime =(int) train1.tempRefTables.get(n).getReferenceArrivalTime();
											
						int hour, minute, second;
						second = (aTime % 100);
						minute = (aTime/ 100) % 100;
						hour = aTime / 100 / 100;

						bPrintStream.print((String.format("%02d",hour))
											+ (String.format("%02d",minute))
											+ (String.format("%02d",second))
											+ "\t");
						
						int dTime =(int) train1.tempRefTables.get(n).getReferenceDepartureTime();
						System.out.println("refDepTime " + train1.tempRefTables.get(n).getReferenceDepartureTime());

						second = (dTime % 100);
						minute = (dTime/ 100) % 100;
						hour = dTime / 100 / 100;						
						
						bPrintStream.print((String.format("%02d",hour))
								+ (String.format("%02d",minute))
								+ (String.format("%02d",second))
								+ "\t");
						}
					else{

						Integer stationPos=Integer.parseInt(String.format("%07d",train1.startLoopNo).substring(0,3));
						String stName=(tempstationList.get(stationPos-1).stationName);
						bPrintStream.print( stName + "\t");
						bPrintStream.print(String.format("%07d",(train1.startLoopNo)) + "\t");
						stationPos=Integer.parseInt(String.format("%07d",train1.endLoopNo).substring(0,3));
						stName=(tempstationList.get(stationPos-1).stationName);
						bPrintStream.print( stName + "\t");
						bPrintStream.print(String.format("%07d",(train1.endLoopNo)) + "\t");

						//bPrintStream.print(train1.getTempRefTables().get(n).getMinHaltTime()+ "\t");
						
					}
					

				}

				bPrintStream.println("\t");
			}
		} catch (Exception e1) {
			Debug.print("Error in handling o/p file ");
			return;
		}

	}

	public boolean isNogui() {
		return nogui;
	}

	public void setNogui(boolean nogui) {
		this.nogui = nogui;
	}

	public void startStationToStationScheduling(String[] args) {
		Debug.fine("SimulationStart: stationToStationScheduling");
		String filepath = null;

		processArguments(args);

		filepath = args[args.length - 1];
		String testCaseDirectory = args[args.length - 2];
		this.simulationInstance.testCaseDirectory = testCaseDirectory;

		Debug.info("filepath: " + filepath);

		ReadSection.readFiles(filepath, testCaseDirectory,
				this.simulationInstance);

		FreightSimulator fSim = readAndPrepareInput();

		TrainList trainList = simulationInstance.getTrainList();

		for (Train train : trainList) {
			int rakeLinkNo = train.getRakeLinkNo();
			Train rakeLinkTrain = simulationInstance
					.getTrainFromTrainNo(rakeLinkNo);
			if (rakeLinkTrain != null) {
				train.setRakeLinkTrain(rakeLinkTrain);
				rakeLinkTrain.setPreviousRakeLinkNo(train.getTrainNo());
				rakeLinkTrain.setPreviousRakeLinkTrain(train);
			}
		}

		for (Train train : trainList) {
			Train previouslyRakeLinkTrain = train.getPreviousRakeLinkTrain();
			ReferenceTableEntry firstEntry = train.getRefTables().get(0);
			double arrivalTime = firstEntry.getReferenceDepartureTime();
			if (previouslyRakeLinkTrain == null) {
				arrivalTime -= 3;
				firstEntry.setReferenceArrivalTime(arrivalTime);
			} else {
				ArrayList<ReferenceTableEntry> refTables = previouslyRakeLinkTrain
						.getRefTables();
				ReferenceTableEntry lastEntry = refTables
						.get(refTables.size() - 1);
				double lastDepartureTime = lastEntry
						.getReferenceDepartureTime();
				if (lastDepartureTime > arrivalTime) {
					for (ReferenceTableEntry referenceTableEntry : refTables) {
						double a = referenceTableEntry
								.getReferenceArrivalTime();
						double d = referenceTableEntry
								.getReferenceDepartureTime();
						referenceTableEntry
								.setReferenceArrivalTime(a + 24 * 60);
						referenceTableEntry
								.setReferenceDepartureTime(d + 24 * 60);
					}
				}
			}
		}

		// for (Train train : trainList) {
		// Train previouslyRakeLinkTrain = train.getPreviousRakeLinkTrain();
		// if (previouslyRakeLinkTrain != null) {
		// ReferenceTableEntry firstEntry = train.getRefTables().get(0);
		// Loop firstLoop = firstEntry.getReferenceLoop();
		// int firstLoopNo = firstLoop.getBlockNo();
		// if (firstLoopNo == 0 || firstLoopNo == 28 || firstLoopNo == 55
		// || firstLoopNo == 80)
		// continue;
		// ArrayList<ReferenceTableEntry> refTables = previouslyRakeLinkTrain
		// .getRefTables();
		// ReferenceTableEntry lastEntry = refTables
		// .get(refTables.size() - 1);
		// lastEntry.setReferenceLoop(firstLoop);
		// }
		// }

		for (Train train : trainList) {
			ArrayList<ReferenceTableEntry> referenceTableEntries = train
					.getRefTables();
			for (ReferenceTableEntry referenceTableEntry : referenceTableEntries) {
				double departureTime = referenceTableEntry
						.getReferenceDepartureTime();
				Loop loop = referenceTableEntry.getReferenceLoop();

				if (loop == null) {
					Debug.error("train " + train.getTrainNo()
							+ " has no specified loop for time "
							+ departureTime);
				} else {
					LoopTrainScheduleData loopTrainScheduleData = new LoopTrainScheduleData(
							loop, train, departureTime);
					loop.loopTrainScheduleDataList.add(loopTrainScheduleData);
				}
			}
		}

		ScheduledTrain specialTrain = null;
		for (Train train : trainList) {
			if (!train.isScheduled())
				continue;

			if (train.getTrainNo() == 90387) {
				specialTrain = (ScheduledTrain) train;
			}

			ScheduledTrain scheduledTrain = (ScheduledTrain) train;
			Loop originLoop = scheduledTrain.getOriginLoop();
			int destinationLoopNumber = scheduledTrain
					.getDestinationLoopNumber();
			LoopList pathLoopList = scheduledTrain.getPathLoopList();

			Block blockIter = originLoop;
			// boolean print = false;
			// if (blockIter.getBlockNo() == 55 || blockIter.getBlockNo() == 80)
			// {
			// print = true;
			// }

			while (blockIter != null) {
				// if(train.getTrainNo()==90387)
				// if (print)
				// Debug.error("train " + train.getTrainNo() + " blockIter "
				// + blockIter.getBlockName());

				if (blockIter.isLoop()) {
					pathLoopList.add((Loop) blockIter);
				}

				if (blockIter.getBlockNo() == destinationLoopNumber)
					break;

				blockIter = blockIter.getNextBlock(scheduledTrain);
			}

			System.out.println("Train " + train.getTrainNo() + " encounters "
					+ pathLoopList.size() + " loops");
		}

		// Debug.error("Special train " + specialTrain.getTrainNo() + " size "
		// + specialTrain.getPathLoopList().size());

		for (Train train1 : trainList) {
			for (Train train2 : trainList) {
				if (train1.getTrainNo() >= train2.getTrainNo()
						|| train1.getDirection() != train2.getDirection())
					continue;

				Debug.error("train1 " + train1.getTrainNo() + " train2 "
						+ train2.getTrainNo());

				ArrayList<ReferenceTableEntry> referenceTableEntries1 = train1
						.getRefTables();
				ArrayList<ReferenceTableEntry> referenceTableEntries2 = train2
						.getRefTables();
				ReferenceTableEntry firstEntry1 = referenceTableEntries1.get(0);
				ReferenceTableEntry firstEntry2 = referenceTableEntries2.get(0);
				ReferenceTableEntry lastEntry1 = referenceTableEntries1
						.get(referenceTableEntries1.size() - 1);
				ReferenceTableEntry lastEntry2 = referenceTableEntries2
						.get(referenceTableEntries2.size() - 1);

				if ((lastEntry1.getReferenceDepartureTime() < firstEntry2
						.getReferenceArrivalTime())
						|| lastEntry2.getReferenceDepartureTime() < firstEntry1
								.getReferenceArrivalTime())
					continue;

				boolean allLoopsSame = false;
				if (referenceTableEntries1.size() == referenceTableEntries2
						.size()) {
					allLoopsSame = true;
					for (int i = 0; i < referenceTableEntries1.size(); i++) {
						ReferenceTableEntry referenceTableEntry1 = referenceTableEntries1
								.get(i);
						ReferenceTableEntry referenceTableEntry2 = referenceTableEntries2
								.get(i);
						if (referenceTableEntry1.getReferenceLoopNo() != referenceTableEntry2
								.getReferenceLoopNo())
							allLoopsSame = false;
					}
				}

				if (allLoopsSame)
					continue;

				System.out.println("train1 " + train1.getTrainNo() + " train2 "
						+ train2.getTrainNo());
				Debug.error("train1 " + train1.getTrainNo() + " train2 "
						+ train2.getTrainNo() + " considered ");
				// Debug.error("Special train " + specialTrain.getTrainNo()
				// + " size " + specialTrain.getPathLoopList().size());

				ScheduledTrain scheduledTrain1 = (ScheduledTrain) train1;
				ScheduledTrain scheduledTrain2 = (ScheduledTrain) train2;

				LoopList pathLoopList1 = scheduledTrain1.getPathLoopList();
				LoopList pathLoopList2 = scheduledTrain2.getPathLoopList();

				Debug.error("pathLoopList sizes are " + pathLoopList1.size()
						+ " " + pathLoopList2.size());
				// for (Loop loop : pathLoopList1) {
				// System.out.print(loop.getBlockName() + " ");
				// }
				// System.out.println();
				//
				// for (Loop loop : pathLoopList2) {
				// System.out.print(loop.getBlockName() + " ");
				// }
				//
				// System.out.println();

				Loop commonLoop = null;
				int startIndex1 = -1;
				int startIndex2 = -1;
				for (int i = 0; i < pathLoopList1.size(); i++) {
					boolean foundCommonLoop = false;
					for (int j = 0; j < pathLoopList2.size(); j++) {
						Loop loop1 = pathLoopList1.get(i);
						Loop loop2 = pathLoopList2.get(j);

						if (loop1.equals(loop2)) {
							startIndex1 = i;
							startIndex2 = j;
							commonLoop = loop1;
							foundCommonLoop = true;
							// System.out.println("Found common loop "
							// + loop1.getBlockName());
							break;
						}
					}

					if (foundCommonLoop) {
						break;
					}
				}

				// determine order between the trains by finding times on first
				// common halt

				Loop commonHalt = null;
				int order = 0;
				for (int i = 0; i < referenceTableEntries1.size(); i++) {
					boolean foundCommonHalt = false;
					for (int j = 0; j < referenceTableEntries2.size(); j++) {
						ReferenceTableEntry entry1 = referenceTableEntries1
								.get(i);
						ReferenceTableEntry entry2 = referenceTableEntries2
								.get(j);
						Loop loop1 = entry1.getReferenceLoop();
						Loop loop2 = entry2.getReferenceLoop();

						if (loop1.equals(loop2)) {
							commonHalt = loop1;
							double referenceDepartureTime1 = entry1
									.getReferenceDepartureTime();
							double referenceDepartureTime2 = entry2
									.getReferenceDepartureTime();

							Debug.error("Found common halt "
									+ loop1.getBlockName());
							if (referenceDepartureTime1 < referenceDepartureTime2) {
								order = -1;
							} else {
								order = 1;
							}

							foundCommonHalt = true;
							break;
						}
					}

					if (foundCommonHalt) {
						break;
					}
				}

				ScheduledTrain followingTrain, leadingTrain;
				int leadingIndex, followingIndex;
				if (order != 0 && commonHalt != null && commonLoop != null) {
					if (order == -1) {
						leadingTrain = scheduledTrain1;
						leadingIndex = startIndex1;
						followingTrain = scheduledTrain2;
						followingIndex = startIndex2;
					} else {
						leadingTrain = scheduledTrain2;
						leadingIndex = startIndex2;
						followingTrain = scheduledTrain1;
						followingIndex = startIndex1;
					}

					ReferenceTableEntry leadingTrainFirstHaltEntryAfterCommonLoop = null;
					ReferenceTableEntry followingTrainFirstHaltEntryBeforeCommonLoop = null;
					Debug.error("common loop " + commonLoop.getBlockName());

					// find firstHaltEntry for leading train after common
					// loop
					LoopList leadingPathLoopList = leadingTrain
							.getPathLoopList();
					ArrayList<ReferenceTableEntry> leadingReferenceTableEntries = leadingTrain
							.getRefTables();
					for (int i = leadingIndex; i < leadingPathLoopList.size(); i++) {
						Loop loop = leadingPathLoopList.get(i);
						// System.out.println("Searching for first train "
						// + loop.getBlockName());

						boolean foundFirstHaltAfterCommonLoop = false;
						for (ReferenceTableEntry referenceTableEntry : leadingReferenceTableEntries) {
							if (referenceTableEntry.getReferenceLoop().equals(
									loop)) {
								// System.out.println("Found first entry");
								leadingTrainFirstHaltEntryAfterCommonLoop = referenceTableEntry;
								foundFirstHaltAfterCommonLoop = true;
								break;
							}
						}

						if (foundFirstHaltAfterCommonLoop)
							break;
					}

					// find firstHaltEntry for second train before common
					// loop
					LoopList followingPathLoopList = followingTrain
							.getPathLoopList();
					ArrayList<ReferenceTableEntry> followingReferenceTableEntries = followingTrain
							.getRefTables();
					for (int i = followingIndex-1; i >= 0
							&& i < followingPathLoopList.size(); i--) {
						Loop loop = followingPathLoopList.get(i);
						// System.out.println("Searching for second train "
						// + loop.getBlockName());

						boolean foundFirstHaltBeforeCommonLoop = false;
						for (int j = 0; j < followingReferenceTableEntries
								.size(); j++) {
							ReferenceTableEntry referenceTableEntry = followingReferenceTableEntries
									.get(j);
							if (referenceTableEntry.getReferenceLoop().equals(
									loop)) {
								// System.out.println("Found first entry");
								followingTrainFirstHaltEntryBeforeCommonLoop = referenceTableEntry;
								foundFirstHaltBeforeCommonLoop = true;
								break;
							}
						}

						if (foundFirstHaltBeforeCommonLoop)
							break;
					}
					
					if(followingTrainFirstHaltEntryBeforeCommonLoop==null){
						followingTrainFirstHaltEntryBeforeCommonLoop= followingReferenceTableEntries.get(0);
					}

					ReferenceTableEntry entryToBeConditioned;
					Loop loopToBeConditionedUpon;
					Condition condition;

					if (followingTrainFirstHaltEntryBeforeCommonLoop != null
							&& leadingTrainFirstHaltEntryAfterCommonLoop != null) {
						entryToBeConditioned = followingTrainFirstHaltEntryBeforeCommonLoop;
						loopToBeConditionedUpon = leadingTrainFirstHaltEntryAfterCommonLoop
								.getReferenceLoop();

						condition = new Condition(leadingTrain,
								loopToBeConditionedUpon);
						entryToBeConditioned.conditionList.add(condition);
						Loop waitingLoop = entryToBeConditioned
								.getReferenceLoop();
						Debug.error("Train " + followingTrain.getTrainNo()
								+ " waits at " + waitingLoop.getBlockName()
								+ " for train "
								+ leadingTrain.getTrainNo()
								+ " to be scheduled till "
								+ loopToBeConditionedUpon.getBlockName()
								+ " order " + order);
					}

				}
			}
		}

		LoopList loopList = simulationInstance.getLoopList();
		for (Loop loop : loopList) {
			Collections.sort(loop.loopTrainScheduleDataList,
					new LoopTrainScheduleData());
		}

		simulationInstance.trainList = StationToStationScheduler
				.getSortedTrainList(trainList);

		System.out.println("StartS2S: trainListSize " + trainList.size());
		fSim.startStationToStationScheduling(-1);
	}
}

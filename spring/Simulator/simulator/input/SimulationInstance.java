package simulator.input;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.signal.SignalDirection;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackProperties.Gradient;
import gui.entities.sectionEntityList.BlockList;
import gui.entities.sectionEntityList.HashBlockTable;
import gui.entities.sectionEntityList.LinkList;
import gui.entities.sectionEntityList.LoopList;
import gui.entities.sectionEntityList.PassengerDelayFormatList;
import gui.entities.sectionEntityList.StationList;
import gui.entities.sectionEntityList.TrainList;
import gui.entities.trainEntities.Train;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Stack;
import java.util.TreeSet;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import simulator.dispatcher.FreightSimulator;
import simulator.outputFeatures.GraphFrame;
import simulator.scheduler.HashBlockEntry;
import simulator.util.Debug;

public class SimulationInstance {
	/**
	 * delayFactor
	 */
	public int delayFactor = 0;
	/**
	 * delay
	 */
	public int delay;

	public int temp = 0;
	/**
	 * freightSimulator
	 */
	public FreightSimulator freightSimulator = null;
	/**
	 * 
	 */
	public GraphFrame graphFrame = null;
	/**
	 * hasBlockDirectionFile
	 */
	public boolean hasBlockDirectionFile = false;
	public boolean hasRailClipse = false;

	/**
	 * warnerDistance
	 */
	public double warnerDistance = 0;
	/**
	 * minTime
	 */
	public double minTime = 0.01;
	/**
	 * This is a specific parameter used only for the 3-line case scenario...
	 * The values it can take are ALL ONLY_SUBURBAN, ONLY_LONG_DISTANCE,
	 * ONLY_FREIGHT, NOT_SUBURBAN, NOT_LONG_DISTANCE, NOT_FREIGHT
	 */
	public String TRAIN_TYPE_ALLOWED_ON_THIRD_LINE = "ALL";
	/**
	 * redFailWaitTime
	 */
	public int redFailWaitTime;
	/**
	 * redFailWaitNightTime
	 */
	public int redFailWaitNightTime = 2;
	/**
	 * redFailVelocity
	 */
	public double redFailVelocity;
	/**
	 * numberOfColour
	 */
	public int numberOfSignalAspects;
	/**
	 * simulationTime
	 */
	public int simulationTime;
	/**
	 * simulationType
	 */
	public String simulationType = "normal";
	/**
	 * blockWorkingTime
	 */
	public double blockWorkingTime = 0;
	/**
	 * currentTrain
	 */
	public Train currentTrain = null;

	/**
	 * testCaseDirectory. For verifying a particular testCase.
	 */
	public String testCaseDirectory;

	public boolean showCondensedOccupyBlocks = true;
	public ArrayList<SignalDirection> signalDirectionList = new ArrayList<SignalDirection>();
	public final double minimumOverLapDistance = 0.12D;
	/**
	 * loopArrayList
	 */
	public LoopList loopList = new LoopList();

	/**
	 * stationArrayList
	 */
	public StationList stationList = new StationList();

	/**
	 * trainArrayList
	 */
	public TrainList trainList = new TrainList();

	//sneha added following templist
	public TrainList tempTrainList =new TrainList();
	
	/**
	 * hashBlock
	 */
	public HashBlockTable hashBlockTable = new HashBlockTable();

	/**
	 * delayArrayList
	 */
	public PassengerDelayFormatList delayList = new PassengerDelayFormatList();
	public int trainTemp = 0;
	public Stack<Train> trainStack =new Stack<Train>();

	/**
	 * Reset the stationArrayList, trainArrayList and hashBlock.
	 */
	public void reset() {
		stationList.clear();
		trainList.clear();
		hashBlockTable.clear();
		loopList.clear();
		delayList.clear();
	}

	public Block getBlockFromBlockNo(int blockNo) {
		return hashBlockTable.getBlockFromBlockNo(blockNo);
	}

	/**
	 * @param blockNo
	 * @param dir
	 * @return get the endPost according to the direction of the block.
	 */
	public double getLastEndmilepost(int blockNo, int direction) {
		double s = 0.0;
		for (Enumeration<HashBlockEntry> e = hashBlockTable.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hbEntry = (HashBlockEntry) e.nextElement();
			Block currBlock = hbEntry.getBlock();
			if (currBlock.getBlockNo() == blockNo) {

				if (direction == GlobalVar.UP_DIRECTION)
					s = currBlock.getEndMilePost();
				if (direction == GlobalVar.DOWN_DIRECTION)
					s = currBlock.getStartMilePost();
			}
		}

		// if the blockNo is -1, it means we have reached the end loop which is
		// not clearly specified, means that the train should be scheduled till
		// the end of the section no matter which loop
		// it may enter. In that case we require the last end mile post to be
		// the end of the section depending upon the direction of the train
		if (blockNo == -1) {
			int size = stationList.size();
			Station endStation;
			if (direction == GlobalVar.UP_DIRECTION) {
				endStation = stationList.get(size - 1);
				// System.out.println(endStation.stationName);
				s = endStation.getEndMilePost();
			}
			if (direction == GlobalVar.DOWN_DIRECTION) {
				endStation = stationList.get(0);
				// System.out.println(endStation.stationName);
				s = endStation.getStartMilePost();
			}
		}

		return s;
	}

	public int getNumberOfBlocks() {
		return hashBlockTable.size();
	}

	public String getStationName(int endLoopNo) {
		return loopList.getStationName(endLoopNo);
	}

	public void createTinyBlockFormatsForBlocks(ArrayList<Gradient> gradientFormatList) {
		hashBlockTable.createTinyBlockFormatsForBlocks(this, gradientFormatList);
	}

	public void convertLinks() {
		hashBlockTable.convertLinks(this);
	}

	public void setStationList(StationList stationList) {
		this.stationList = stationList;
	}

	public void setLoopList(LoopList loopList) {
		this.loopList = loopList;
	}

	public void addBlock(Integer integer, HashBlockEntry hashblockentry) {
		hashBlockTable.put(integer, hashblockentry);
	}

	public void setTrainList(TrainList trainList) {
		this.trainList = trainList;
	}

	public PassengerDelayFormatList getPassengerDelayList() {
		return delayList;
	}

	public void setPassengerDelayList(
			PassengerDelayFormatList passengerDelayFormatList) {
		this.delayList = passengerDelayFormatList;
	}

	public double getWeightedTraffic(int trainDirection) {
		// TODO Auto-generated method stub
		return trainList.getWeightedTraffic(trainDirection);
	}

	public void outputBlockOccupancies() {
		hashBlockTable.outputBlockOccupancies();
	}

	public void outputWeightedTrainTravel() {
		trainList.outputWeightedTrainTravel();
	}

	public void outputTotalTrainTravel() {
		trainList.outputTotalTrainTravel();
	}

	public void displayAverageTravellingTime(FreightSimulator freightSimulator) {
		trainList.displayAverageTravellingTime(freightSimulator);
	}

	public void printResultsInCsv() {
		Train train1;

		try {
			
			String nextResultFileName;						
			Path resultsFolderPath = Paths.get(FileNames.getTestCaseDirectory() + "/Capacity-Results");
			JFileChooser resultsFolderDirectory = new JFileChooser(resultsFolderPath.toString());
			
			/**Creating "Results" folder, if it is already not present in the test case**/	
			if (!Files.exists(resultsFolderPath)){
				Files.createDirectories(resultsFolderPath);
			}
			
			/**Checking whether the "Results" folder is empty or not
			 * If it is not free, then "nextResultFileName" is set accordingly
			 */
			File[] currentResultFiles = new File(resultsFolderPath.toString()).listFiles();
			if(currentResultFiles.length == 0){
				
				nextResultFileName = "Result-1.csv";
				
			}else{
				
				/**This Variable will store the index of the last result file available in "Capacity_Results" Folder**/
				int indexOfLastFile = 0;
				
				for (File file : currentResultFiles) {
				   if (file.getName().contains("Result-") && file.getName().contains(".csv") ) {
					   
				   
					    indexOfLastFile = Integer.parseInt(file.getName().replaceAll("[^0-9]", ""));
				   }
				}
				
				indexOfLastFile+=1;
				nextResultFileName = "Result-" + indexOfLastFile + ".csv";
			}
					
 
			JOptionPane.showMessageDialog(null,
					"Select a file for saving timetable", "TimeTable File",
					JOptionPane.OK_OPTION);

			System.out
					.println("I came here *********************************************");
			
			File resultFile = new File(resultsFolderPath.toString()+"/"+nextResultFileName);
			resultsFolderDirectory.setSelectedFile(resultFile);
			
			int response = resultsFolderDirectory.showSaveDialog(graphFrame);
			if (response == JFileChooser.APPROVE_OPTION) {
				// user clicked Save
				resultFile = resultsFolderDirectory.getSelectedFile();
				System.out.println("I saved the file");
			} else {
				// user cancelled...
				System.out.println("I cancelled saving the file");
			}
			

			System.out.println("help me");
			OutputStream f2 = new FileOutputStream(resultFile);
			PrintStream bPrintStream = new PrintStream(f2);
			
			/**Printing the Parameters
			bPrintStream.println("Freight Train Parameters which are considered for Capacity analysis ");
			bPrintStream.println("Direction " + "," + ((parametersList.get(0)==1)?"Up":"Down")); 
			bPrintStream.println("Length " + "," + parametersList.get(1)); 
			bPrintStream.println("Acceleration (m/s^2) " + "," + parametersList.get(2)*1000/3600); 
			bPrintStream.println("Deceleration (m/s^2) " + "," + parametersList.get(3)*1000/3600); 
			bPrintStream.println("Priority " + "," + parametersList.get(4)); 
			bPrintStream.println("Maximum Possible Speed (kmph)" + "," + parametersList.get(5)); 
			bPrintStream.println("Start Loop No. " + "," + parametersList.get(6));
			bPrintStream.println("End Loop No. " + "," + parametersList.get(7));
			bPrintStream.println("Headway for Scheduled Train " + "," + parametersList.get(8));
			bPrintStream.println("Headway for Freight Train " + "," + parametersList.get(9));
			bPrintStream.println("");**/
			
			//Printing the timetable
			bPrintStream.println("T.No." + "," + "station Name" + ","
					+ "Loop" + "," + "A.Time" + "," + "D.Time");

			for (int m = 0; m < trainList.size(); m++) {
				train1 = trainList.get(m);
				bPrintStream.print(train1.getTrainNo() + ",");
				for (int n = 0; n < train1.getTimeTables().size(); n++) {
					if (((loopList.getStationName((train1.getTimeTables()
							.get(n)).getLoopNo()))) != null) {
						bPrintStream.print((loopList.getStationName((train1
								.getTimeTables().get(n)).getLoopNo())) + ",");
						bPrintStream.print((train1.getTimeTables().get(n))
								.getLoopNo() + ",");
						double aTime = (train1.getTimeTables().get(n))
								.getArrivalTime();
						int aTimeHr = (int) aTime / 60;
						aTime = (aTime - aTimeHr * 60) * 60;
						int aTimeMin = (int) aTime / 60;
						double aTimeSec = aTime - aTimeMin * 60;
						bPrintStream.print(aTimeHr + ":" + aTimeMin + ":"
								+ (int) aTimeSec + ",");
						double dTime = (train1.getTimeTables().get(n))
								.getDepartureTime();
						int dTimeHr = (int) dTime / 60;
						dTime = (dTime - dTimeHr * 60) * 60;
						int dTimeMin = (int) dTime / 60;
						double dTimeSec = dTime - dTimeMin * 60;
						bPrintStream.print(dTimeHr + ":" + dTimeMin + ":"
								+ (int) dTimeSec + ",");
					}

				}

				bPrintStream.println(",");
			}
		} catch (Exception e1) {
			Debug.print("Error in handling o/p file ");
			return;
		}
	}
	
	public void printTimeTableInExcel() {
		Train train1,train2;

		try {
			String fileName = "TimeTable.csv";
			JOptionPane.showMessageDialog(null,
					"Select a file for saving timetable", "TimeTable File",
					JOptionPane.OK_OPTION);
			String currentDirectoryPath = (new File("..")).getCanonicalPath();
			JFileChooser chooser = new JFileChooser(currentDirectoryPath);
			System.out
					.println("I came here *********************************************");
			File file = new File(fileName);
			chooser.setSelectedFile(file);

			int response = chooser.showSaveDialog(graphFrame);
			if (response == JFileChooser.APPROVE_OPTION) {
				// user clicked Save
				file = chooser.getSelectedFile();
				System.out.println("I saved the file");
			} else {
				// user cancelled...
				System.out.println("I cancelled saving the file");
			}
			System.out.println("help me");
			
			/*File file;
			String filePath="E:\\sneha\\test\\TimeTable.csv";
			file=new File(filePath);
			System.out.println("TimeTable.xls file path is :" + filePath);
			*/
			
			OutputStream f2 = new FileOutputStream(file);
			PrintStream bPrintStream = new PrintStream(f2);
			bPrintStream.println(String.format("%-10s","T.No.") + "\t" 
								+ String.format("%-6s" ,"Dir")  
								+ String.format("%-6s","Len") + "\t"  
								+ String.format("%-6s","Acc") + "\t"
								+ String.format("%-6s","Dec") + "\t"
								+ "Pri" + "\t\t"
								+ "Velocity" + "\t"
								+ "Op.D"  + "\t\t"
								+ "Pr.Halts" + "\t\t"
								+ "stnName" + "\t"
								+ "Loop" + "\t\t" 
								+ "A.Time" + "\t" 
								+ "D.Time");

			for (int m = 0; m < trainList.size(); m++) {
				train1 = trainList.get(m);
				train2 = tempTrainList.get(m);
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
				int noofhalts=train2.numberofhalts;
				str1=String.format("%02d",noofhalts );
				bPrintStream.print( str1 + "\t");
				
				
				for (int n = 0; n < train1.getTimeTables().size(); n++) {
					if (((loopList.getStationName((train1.getTimeTables().get(n)).getLoopNo()))) != null) {
						String stName=(loopList.getStationName((train1.getTimeTables().get(n)).getLoopNo()));
						bPrintStream.print( String.format("%20s",stName)+ "\t");
						bPrintStream.print(String.format("%07d",(train1.getTimeTables().get(n)).getLoopNo()) + "\t");
						double aTime = (train1.getTimeTables().get(n)).getArrivalTime();
						int aTimeHr = (int) aTime / 60;
						aTime = (aTime - aTimeHr * 60) * 60;
						int aTimeMin = (int) aTime / 60;
						double aTimeSec = aTime - aTimeMin * 60;
						bPrintStream.print((String.format("%02d",aTimeHr))
											+ (String.format("%02d",aTimeMin))
											+ (String.format("%02d",(int) aTimeSec))
											+ "\t");
						double dTime = (train1.getTimeTables().get(n)).getDepartureTime();
						int dTimeHr = (int) dTime / 60;
						dTime = (dTime - dTimeHr * 60) * 60;
						int dTimeMin = (int) dTime / 60;
						double dTimeSec = dTime - dTimeMin * 60;
						bPrintStream.print((String.format("%02d",dTimeHr ))
											+ (String.format("%02d",dTimeMin ))
											+ (String.format("%02d",(int) dTimeSec))
											+ "\t");
					}

				}

				bPrintStream.println("\t");
			}
		} catch (Exception e1) {
			Debug.print("Error in handling o/p file ");
			return;
		}
	}


	public void printHeadwayInExcel() {
		Train train2;
		try {
			OutputStream f4 = new FileOutputStream("Headway.xls");
			PrintStream bPrintStream = new PrintStream(f4);
			bPrintStream.println("Block No" + "\t" + "Red to Green" + "\t"
					+ "Red to Double Yellow ");
			for (int m = 0; m < 1; m++)
			// for( int m =0 ; m < trainArrayList.size(); m++)
			{
				train2 = trainList.get(m);
				for (int n = 1; n < train2.getTimeTables().size() - 2; n++) {
					int sks = (train2.getTimeTables().get(n)).getLoopNo();
					bPrintStream.print((train2.getTimeTables().get(n))
							.getLoopNo() + "\t");
					double aTime = (train2.getTimeTables().get(n))
							.getArrivalTime();
					double dTime = (train2.getTimeTables().get(n + 2))
							.getDepartureTime();
					// if (train2.direction == 0) {
					// bPrintStream.print(Math.round((dTime - aTime) * 60
					// + GlobalVar.sudhir[n + 3])
					// + "\t");
					// } else {
					// bPrintStream.print(Math.round((dTime - aTime) * 60
					// + GlobalVar.sudhir[sks - 2])
					// + "\t");
					// }
					// dTime = ( train2.timeTables.get(n +
					// 1)).departureTime;
					// if (train2.direction == 0) {
					// bPrintStream.println(Math.round((dTime - aTime)
					// * 60 + GlobalVar.sudhir[n + 2])
					// + "\t");
					// } else {
					// bPrintStream.println(Math.round((dTime - aTime)
					// * 60 + GlobalVar.sudhir[sks - 1])
					// + "\t");
					//
					// }
				}

			}
		} catch (Exception e1) {
			Debug.print("Error in handling o/p file ");
			return;
		}

	}

	public void printSignalsSeenInExcel() {
		Train train3;
		try {
			OutputStream f3 = new FileOutputStream("Signal.xls");
			PrintStream bPrintStream = new PrintStream(f3);

			for (int m = 0; m < trainList.size(); m++) {
				bPrintStream.print("Train.No." + "\t");
				train3 = trainList.get(m);
				bPrintStream.print(train3.getTrainNo() + "\t");
				String dir = null;
				dir = (train3.getDirection() == 0) ? "UP" : "DOWN";
				bPrintStream.println(dir + "\t");
				bPrintStream.println("**************************************");
				bPrintStream.println("Block No." + "\t" + "Signal Colour");
				bPrintStream
						.println("-----------------------------------------------");
				for (int n = 0; n < train3.getTimeTables().size(); n++) {

					bPrintStream.print((train3.getTimeTables().get(n))
							.getLoopNo() + "\t");
					int color;
					color = (train3.getTimeTables().get(n)).getSignal();

					String signalColor = null;
					if (numberOfSignalAspects == 4) {
						signalColor = (6 == color) ? "pink"
								: (3 == color) ? "Green"
										: ((2 == color) ? "DoubleYellow"
												: ((1 == color) ? "Yellow"
														: "Red"));
					}
					if (numberOfSignalAspects == 3) {
						signalColor = (6 == color) ? "pink"
								: (2 == color) ? "Green"
										: (((1 == color) ? "Yellow" : "Red"));
					}
					if (numberOfSignalAspects == 2) {
						signalColor = (6 == color) ? "pink"
								: (0 == color) ? "Red" : (1 == color) ? "Green"
										: "Green";
					}

					bPrintStream.println(signalColor + "\t");
				}
				bPrintStream.println("\t");
			}

		} catch (Exception e1) {
			Debug.print("Error in handling o/p file ");
			return;
		}

	}

	/**
	 * @param signal
	 * @return returns the type of signal in words
	 */
	public String getSignalColor(int signal) {
		if (signal == 0)
			return "red";
		if (numberOfSignalAspects == 3) {
			if (signal == 1)
				return "yellow";
			if (signal == 2)
				return "green";
		} else if (numberOfSignalAspects == 4) {
			if (signal == 1)
				return "double yellow";
			if (signal == 2)
				return "yellow";
			if (signal == 3)
				return "green";
		} else
			return "null";
		return "yo";
	}

	public TrainList getTrainList() {
		// TODO Auto-generated method stub
		return trainList;
	}

	public Train getTrainNew(int trainNo, double time, Block block) {
		return trainList.getTrainNew(trainNo, time, block);
	}

	public StationList getStationList() {
		return stationList;
	}

	public LoopList getLoopList() {
		return loopList;
	}

	public Train getTrainFromTrainNo(int trainNumber) {
		// TODO Auto-generated method stub
		return trainList.getTrain(trainNumber);
	}

	public BlockList getBlock(double distance) {
		return hashBlockTable.getBlock(distance);
	}

	public LinkList getAllLinksForBlock(Block block) {
		return hashBlockTable.getAllLinksForBlock(block);
	}

	public Station getStation(String stationName) {
		return stationList.getStation(stationName);
	}

	public void setSignalFailFlags() {
		hashBlockTable.setSignalFailFlags();
	}

	public BlockList getBlockListByTime(int trainNo,
			double simulationCurrentTime) {
		return hashBlockTable
				.getBlockListByTime(trainNo, simulationCurrentTime);
	}

	public void initiateSignalFailureParameters() {
		hashBlockTable.initiateSignalFailureParameters(this);
	}

	public void generateTrainArrayToSimulate() {
		trainList.generateTrainArrayToSimulate(this);
	}

	public TreeSet<Double> getLinkLocationsOnBlock(Block block) {
		return hashBlockTable.getLinkLocationsOnBlock(block);
	}

}

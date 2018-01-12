package simulator.outputFeatures;

import globalVariables.GlobalVariables;
import gui.diagramEntities.trainDiagrams.VelocityProfile;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.signal.Signal;
import gui.entities.sectionEntities.time.Interval;
import gui.entities.sectionEntities.time.IntervalArray;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntityList.BlockList;
import gui.entities.sectionEntityList.LoopList;
import gui.entities.sectionEntityList.StationList;
import gui.entities.sectionEntityList.TrainList;
import gui.entities.trainEntities.Train;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import simulator.input.SimulationInstance;
import simulator.outputFeatures.graph.DrawStyle;
import simulator.outputFeatures.graph.GLine;
import simulator.outputFeatures.graph.LineLayer;
import simulator.outputFeatures.graph.Marker;
import simulator.outputFeatures.graph.Paper;
import simulator.outputFeatures.graph.XGridLine;
import simulator.outputFeatures.graph.YGridLine;
import simulator.scheduler.SimulatorTimeTableEntry;
import simulator.util.Debug;
import simulator.velocityProfileCalculation.VelocityProfileArray;

/**
 * GraphPaper
 */
@SuppressWarnings("serial")
public class GraphPaper extends Paper {
	// /**
	// * yScale
	// */
	// double yScale = 100;
	// /**
	// * xScale
	// */
	// double xScale = 2;
	SimulationInstance simulationInstance = null;
	/**
	 * gLayer
	 */
	LineLayer gLayer = new LineLayer();
	/**
	 * blockReserveLayer
	 */
	LineLayer blockReserveLayer = new LineLayer();

	private GridLayer yGridLayer = new GridLayer();
	/**
	 * blockReserve
	 */
	boolean blockReserve = true;

	// ------train graph variables......
	/**
	 * xOrigin
	 */
	final float xOrigin = 130;
	/**
	 * yOrigin
	 */
	final float yOrigin = 20;
	/**
	 * xWidth
	 */
	final float xWidth = 600;
	/**
	 * yShift
	 */
	double yShift = 0;
	/**
	 * xShift
	 */
	double xShift = 110;

	/**
	 * xPoint
	 */
	int xPoint;
	/**
	 * yPoint
	 */
	int yPoint;
	/**
	 * xStart
	 */
	int xStart = 0;

	/**
	 * constructor.
	 */
	public GraphPaper(SimulationInstance simulationInstance) {
		// trainGraph = new Paper();
		this.simulationInstance = simulationInstance;
	}

	/**
	 * nextScreen
	 */
	public void nextScreen() {
		xStart += xWidth / 2;
		adjustLayers();
	}

	/**
	 * previousScreen
	 */
	public void previousScreen() {
		if (xStart > 0) {
			xStart -= xWidth / 2;
		}

		adjustLayers();
	}

	public void adjustLayers() {
		gLayer.setXShift(-(xStart / xScale) + xShift);
		// timeGridLayer.set
		blockReserveLayer.setXShift(-(xStart / xScale) + xShift);
		redrawGraph();
	}

	/**
	 * redrawGraph
	 */
	public void redrawGraph() {
		gLayer.clear();
		blockReserveLayer.clear();

		int timeShift = (int) xShift;
		timeGrid.clear();

		for (int i = 0; i < 1440; i += 10) {
			if (xStart / xScale <= i) {
				if (i % 60 == 0) {
					XGridLine xGridLine = new XGridLine(i * xScale + timeShift
							* 2 - xStart, Integer.toString(i / 60));
					timeGrid.add(xGridLine);
				}
			}
		}

		TrainList trainList = simulationInstance.getTrainList();
		drawScheduledTrains(trainList);
		drawGraph();
	}

	/**
	 * @param trn
	 * @param numberOfSignalAspects
	 */

	public void drawTrain(Train train, int numberOfSignalAspects) {

		getProfileLayer().clear();
		// ArrayList pts = new ArrayList();

		getSignalLayer().clear();

		for (int m = 0; m < train.getTimeTables().size(); m++) {
			SimulatorTimeTableEntry trainTimeTableEntry = train.getTimeTables()
					.get(m);
			GLine gLine = new GLine();
			gLine.add(new Marker(0, trainTimeTableEntry.getStartMilePost()));
			gLine.add(new Marker(
					0,
					trainTimeTableEntry.getStartMilePost()
							+ (trainTimeTableEntry.getEndMilePost() - trainTimeTableEntry
									.getStartMilePost()) / 5));

			int signalSeenByTrain = trainTimeTableEntry.getSignal();
			Color signalColor = Signal.getSignalColor(signalSeenByTrain,
					numberOfSignalAspects);

			gLine.setPaint(signalColor);
			getSignalLayer().add(gLine);

			VelocityProfileArray profileArray = trainTimeTableEntry
					.getVelocityProfileArray();

			for (int i = 0; i < profileArray.size(); i++) {
				GLine profLine = new GLine();
				VelocityProfile velocityProfile = profileArray.get(i);
				double velocityProfileStartMilePost = velocityProfile
						.getStartMilePost();
				double velocityProfileEndMilePost = velocityProfile
						.getEndMilePost();
				
				velocityProfileStartMilePost = GlobalVariables.roundToThreeDecimals(velocityProfileStartMilePost);
				velocityProfileEndMilePost = GlobalVariables.roundToThreeDecimals(velocityProfileEndMilePost);
				
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
				Debug.fine("drawTrain: " + i + " smp "
						+ velocityProfileStartMilePost + " emp "
						+ velocityProfileEndMilePost + " sv "
						+ velocityProfile.getStartVelocity() + " ev "
						+ velocityProfile.getEndVelocity() + " a "
						+ velocityProfile.getAcceleration() + " ca " + ca
						+ " ca2 " + ca2 + velocityProfile.getDraw());
				/**System.out.println("GraphPaper.drawTrain velocityProfileStartMilePost = "+velocityProfileStartMilePost+
						" velocityProfileEndMilePost = "+ velocityProfileEndMilePost+ " Start Velocity = "+
						velocityProfile.getStartVelocity() + " End Velocity = "+ velocityProfile.getEndVelocity()+
						" Total time = "+ velocityProfile.getTotalTime() + " Arrival TIme = "+velocityProfile.getArrivalTime()
						+" Departure Time = "+ velocityProfile.getDepartureTime());**/
				profLine.add(new Marker(
						velocityProfile.getStartVelocity() * 25,
						velocityProfile.getStartMilePost()));
				profLine.add(new Marker(velocityProfile.getEndVelocity() * 25,
						velocityProfile.getEndMilePost()));
				profLine.setPaint(Color.black);
				getProfileLayer().add(profLine);
			}
		}

		GLine gl = getPlotForTrain(train);

		int trainNo = train.getTrainNo();
		Color plotColor = getPlotColorByTrainNo(trainNo);
		gl.setPaint(plotColor);

		gLayer.add(gl);
		drawBlockReservations(train);
		drawGraph();
	}

	private Color getPlotColorByTrainNo(int trainNo) {
		/*int a = trainNo % 5;
		if (a == 5)
			a = -1;

		a++;
		Debug.print("a is :" + a);
		if (a == 0)
			return Color.black;
		else if (a == 1)
			return Color.magenta;
		else if (a == 2)
			return Color.green;
		else if (a == 3)
			return Color.cyan;
		else if (a == 4)
			return Color.red;
		else
			// a == 5
			return Color.yellow;*/
		String tempTrainNo=String.valueOf(trainNo);
		if(tempTrainNo.substring(0,1).equals("6") || tempTrainNo.length()==3){
			return Color.black;
		}else if(tempTrainNo.substring(0,2).equals("12")){
			return Color.magenta;
		}else return Color.green;

	}

	/**
	 * @param Trains
	 * @param StnArray
	 */
	public void drawGraph(TrainList trainList, StationList stationList,
			LoopList loopList) {
		Station stn;
		double yMax;
		// addLayer(profileLayer);
		// addLayer(signalLayer);
		// ArrayList hrA = new ArrayList();
		// ArrayList vrA = new ArrayList();
		// Paper.GridLayer yGridLayer = getGridInstance();
		YGridLine yGridLine;
		simulator.outputFeatures.graph.XGridLine xGridLine;
		setOrigin(xOrigin, yOrigin);
		Debug.print("graph " + stationList.size() + " " + trainList.size());
		stn = stationList.get(stationList.size() - 1);
		yMax = stn.getEndMilePost();

		// Devendra - To add the time axis calibration
		GLine timeAxis = new GLine();
		// System.out.println("ymax" + yMax + " " + this.getBounds().y);

		timeAxis.add(new Marker(0, getHeight(), "time1"));
		timeAxis.add(new Marker(xWidth, getHeight(), "time2"));
		gLayer.add(timeAxis);

		// display no of loops for station in graph

		for (int i = 0; i < stationList.size(); i++) {
			stn = stationList.get(i);
			int count = 0;
			for (int j = 0; j < loopList.size(); j++) {
				String s1, s2;
				s1 = stn.getStationName();
				s2 = loopList.get(j).getStationName();

				if (s1.equalsIgnoreCase(s2))
					count++;
			}

			// yMax = Math.max(yMax, stn.getStartMilePost());
			if (i == 0) {
				yShift = stn.getStartMilePost();
			}

			int noOfLoops = count;
			yGridLine = new YGridLine((stn.getStartMilePost() - yShift),// TODO
					stn.getStationName() + "(" + Integer.toString(noOfLoops)
							+ ")");
			Debug.print("graph " + stn.getStartMilePost() + " "
					+ stn.getStationName());
			yGridLayer.add(yGridLine);
		}

		for (int i = 0; i < 100; i += 25) {
			xGridLine = new XGridLine(i, Integer.toString(i * 60 / 25));
			yGridLayer.add(xGridLine);
		}

		// Devendra :- add the time lines
		GridLayer timeGridLayer = new GridLayer();
		int timeShift = (int) xShift;
		for (int i = 0; i < 1440; i += 10) {
			if (i % 60 == 0) {
				xGridLine = new XGridLine(i * xScale + timeShift * 2,
						Integer.toString(i / 60));
				timeGridLayer.add(xGridLine);
			}
		}

		yScale = (getHeight() - 35) / (yMax - yShift);
		// System.out.println("max height  " + yMax + " height of graph  "
		// + getHeight() + " xScale " + xScale + " yScale " + yScale);
		setScale(xScale, yScale);

		gLayer.setYShift(-yShift);
		blockReserveLayer.setYShift(-yShift);
		getProfileLayer().setYShift(-yShift);
		getSignalLayer().setYShift(-yShift);
		gLayer.setXShift(xShift);
		blockReserveLayer.setXShift(xShift);
		getSignalLayer().setXShift(xShift - 10);

		yGridLayer.setVisible(true);
		setYGrid(yGridLayer);
		// timeGridLayer.lateralShift = xShift;
		timeGridLayer.setVisible(true);
		timeGrid = timeGridLayer;

		drawScheduledTrains(trainList);
		gLayer.setStroke(DrawStyle.thinStroke);
		blockReserveLayer.setStroke(DrawStyle.thinStroke);
		timeGridLayer.setStroke(DrawStyle.thinStroke);
		addLayer(gLayer);
		addLayer(blockReserveLayer);
		// addLayer(timeGridLayer);
		drawGraph();
	}

	/**
	 * @param Trains
	 */
	public void drawScheduledTrains(TrainList trainList) {

		int a = 0;
		for (int i = 0; i < trainList.size(); i++) {
			Train simulatorTrain = trainList.get(i);
			if (simulatorTrain.getTimeTables().size() <= 0) {
				continue;
			}
			SimulatorTimeTableEntry trainTimeTableEntryStart = simulatorTrain
					.getTimeTables().get(0);
			SimulatorTimeTableEntry trainTimeTableEntryEnd = simulatorTrain
					.getTimeTables().get(
							simulatorTrain.getTimeTables().size() - 1);
			if (((trainTimeTableEntryStart.getArrivalTime() < xStart / xScale) && (trainTimeTableEntryEnd
					.getDepartureTime() < xStart / xScale))
					|| ((trainTimeTableEntryEnd.getDepartureTime() > ((xStart + xWidth) / xScale)) && (trainTimeTableEntryStart
							.getArrivalTime() > ((xStart + xWidth) / xScale)))) {
				continue;
			}

			GLine gl = getPlotForTrain(simulatorTrain);

			simulatorTrain.setDrawColour((Color) gl.getPaint());
			int trainNo = simulatorTrain.getTrainNo();
			Color plotColor = getPlotColorByTrainNo(trainNo);
			gl.setPaint(plotColor);

			gLayer.add(gl);
			drawBlockReservations(simulatorTrain);
		}
	}

	private GLine getPlotForTrain(Train simulatorTrain) {
		GLine gl = new GLine();
		for (int m = 0; m < simulatorTrain.getTimeTables().size(); m++) {
			Debug.print("adding train " + m);
			SimulatorTimeTableEntry trainTimeTableEntry = simulatorTrain
					.getTimeTables().get(m);
			if (((trainTimeTableEntry.getArrivalTime() < xStart / xScale) && (trainTimeTableEntry
					.getDepartureTime() < xStart / xScale))
					|| ((trainTimeTableEntry.getDepartureTime() > ((xStart + xWidth) / xScale)) && (trainTimeTableEntry
							.getArrivalTime() > ((xStart + xWidth) / xScale)))) {
				continue;
			}
			// gl.add(new Marker(trainTimeTableEntry.getArrivalTime(),
			// trainTimeTableEntry.getStartMilePost()));
			// gl.add(new Marker(trainTimeTableEntry.getDepartureTime(),
			// trainTimeTableEntry.getEndMilePost()));
			// Debug.print(" Alloted loop:  "
			// + trainTimeTableEntry.getLoopNo() + "   Arr Time: "
			// + (trainTimeTableEntry.getArrivalTime())
			// + "   DepTime: "
			// + (trainTimeTableEntry.getDepartureTime())
			// + "   milep: " + trainTimeTableEntry.getStartMilePost()
			// + "   end mile" + trainTimeTableEntry.getEndMilePost());

			VelocityProfileArray velocityProfileArray = trainTimeTableEntry
					.getVelocityProfileArray();
			int trainDirection = simulatorTrain.getDirection();
			for (VelocityProfile velocityProfile : velocityProfileArray) {
				double arrivalTime = velocityProfile.getArrivalTime();
				double startMilePost = velocityProfile
						.getStartMilePost(trainDirection);
				double departureTime = velocityProfile.getDepartureTime();
				double endMilePost = velocityProfile
						.getEndMilePost(trainDirection);
				// double startVelocity = velocityProfile.getStartVelocity();
				// double endVelocity = velocityProfile.getEndVelocity();

				gl.add(new Marker(arrivalTime, startMilePost));
				gl.add(new Marker(departureTime, endMilePost));

				// System.out.println("drawTrain: startMilePost " +
				// startMilePost
				// + " endMilePost " + endMilePost + " arrivalTime "
				// + arrivalTime + " departureTime " + departureTime
				// + " startVelocity " + startVelocity + " endVelocity "
				// + endVelocity);
			}

			// gl.add(new Marker(trainTimeTableEntry.getArrivalTime(),
			// trainTimeTableEntry.getStartMilePost()));
			// gl.add(new Marker(trainTimeTableEntry.getDepartureTime(),
			// trainTimeTableEntry.getEndMilePost()));
			// System.out.println("Alloted loop:  "
			// + trainTimeTableEntry.getLoopNo() + "   Arr Time: "
			// + trainTimeTableEntry.getArrivalTime() + "   DepTime: "
			// + trainTimeTableEntry.getDepartureTime() + "   start: "
			// + trainTimeTableEntry.getStartMilePost() + "   end: "
			// + trainTimeTableEntry.getEndMilePost() + "   Velocity:"
			// + trainTimeTableEntry.svelo);

		}

		return gl;
	}

	/**
	 * clearBlockReservations.
	 */
	public void clearBlockReservations() {
		blockReserveLayer.clear();
	}

	/**
	 * @param trn
	 */

	public void drawBlockReservations(Train train) {
		Debug.print("I am drwablock reservations " + train.getTrainNo());
		if (blockReserve == false) {
			return;
		}

		BlockList blockList = train
				.getBlocksTraversedByTrain(simulationInstance);
		for (Block block : blockList) {

			IntervalArray Occupy = block.getOccupy();
			Occupy.printInterval();
			Color clr = getColor(train.getTrainNo());

			for (int i = 0; i < Occupy.size(); i++) {
				GLine gl = new GLine();
				int trnNo = Occupy.get(i).getTrainNo();
				Debug.print("Train no " + trnNo);

				Interval interval = Occupy.get(i);
				double startTime = interval.getStartTime();
				double endTime = interval.getEndTime();

				if (trnNo == train.getTrainNo()) {
					if (((startTime < xStart / xScale) && (endTime < xStart
							/ xScale))
							|| ((endTime > ((xStart + xWidth) / xScale)) && (startTime > ((xStart + xWidth) / xScale)))) {
						continue;
					}
					double startMilePost = block.getStartMilePost();
					double endMilePost = block.getEndMilePost();

					gl.add(new Marker(startTime, startMilePost));
					gl.add(new Marker(endTime, startMilePost));
					gl.add(new Marker(endTime, endMilePost));
					gl.add(new Marker(startTime, endMilePost));
					gl.add(new Marker(startTime, startMilePost));
					gl.setPaint(clr);
					blockReserveLayer.add(gl);
				}
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return {@link JPopupMenu}
	 */
	public JPopupMenu analysePoint(int x, int y) {
		double time = ((x - xOrigin + xStart) / xScale) - xShift;
		double distance = ((getHeight() - y - yOrigin) / yScale) + yShift;
		System.out.println("yorig : " + y + " xOrigi : " + x);
		System.out.println("xStart: " + xStart + " xShift: " + xShift
				+ " xScale: " + xScale);
		System.out.println("y : " + distance + " x : " + time);
		BlockList selectBlk = simulationInstance.getBlock(distance);
		JPopupMenu popup = new JPopupMenu();
		popup.setLightWeightPopupEnabled(false);

		double timeInterval = 5;
		for (int i = 0; i < selectBlk.size(); i++) {
			Block currBlock = (Block) selectBlk.get(i);
			JMenuItem menuItem = new BlockMenuItem(currBlock.getBlockName(),
					currBlock);
			popup.add(menuItem);
			ArrayList<Integer> trainNoList = currBlock.getTrainNoFromOccupancy(
					time, timeInterval);
			for (Integer trainNo : trainNoList) {
				menuItem = new TrainMenuItem("Train no - " + trainNo, trainNo,
						time, currBlock);
				popup.add(menuItem);
			}

		}
		return popup;
	}

	/**
	 * @param trainNumber
	 * @return Color corresponding to the given trainNumber
	 */
	public Color getColor(int trainNumber) {
		Train train = simulationInstance.getTrainFromTrainNo(trainNumber);
		if (train != null)
			return train.getDrawColour();

		return Color.black;
	}

	/**
	 * TrainMenuItem
	 */
	class TrainMenuItem extends JMenuItem {
		/**
		 * trainNo
		 */
		int trainNo;
		/**
		 * time
		 */
		double time;
		/**
		 * block
		 */
		Block block;

		/**
		 * @param name
		 * @param no
		 * @param ti
		 * @param bloc
		 */
		public TrainMenuItem(String name, int no, double ti, Block bloc) {
			super(name);
			trainNo = no;
			time = ti;
			block = bloc;
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					drawGraph();
					Debug.print("Processing menu train no " + trainNo);
					Train tr = simulationInstance.getTrainNew(trainNo, time,
							block);
					if (tr == null) {
						System.out.println("Train with train no " + trainNo
								+ " not found");
						return;
					}
					Debug.print(" Train returned is " + tr.getTrainNo());
					@SuppressWarnings("unused")
					TrainData trnFrame = new TrainData(tr);

					// redraw the velocity profile for the train
					if (simulationInstance.freightSimulator != null) {
						if (simulationInstance.freightSimulator.getGraphPanel() != null) {
							Debug.print("GraphPaper: redrawing the train profile");

							int numberOfSignalAspects = simulationInstance.numberOfSignalAspects;

							simulationInstance.freightSimulator.getGraphPanel()
									.drawTrain(tr, numberOfSignalAspects);
						}
					}
				}
			});
		}
	}

	/**
	 * BlockMenuItem.
	 */
	class BlockMenuItem extends JMenuItem {
		/**
		 * block
		 */
		Block block;

		/**
		 * @param name
		 * @param b
		 */
		public BlockMenuItem(String name, Block b) {
			super(name);
			block = b;
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					drawGraph();
					Debug.print("Processing menu train no "
							+ block.getBlockNo());
					@SuppressWarnings("unused")
					BlockData blkFrame = new BlockData(block);
				}
			});
		}
	}

	public void drawBlockReservations(TrainList trainList) {
		for (int m = 0; m < trainList.size(); m++) {
			Train train = trainList.get(m);
			this.drawBlockReservations(train);
		}
	}
}

package simulator.outputFeatures;

import globalVariables.GlobalVar;
import gui.entities.sectionEntityList.BlockList;
import gui.entities.sectionEntityList.LoopList;
import gui.entities.sectionEntityList.StationList;
import gui.entities.sectionEntityList.TrainList;
import gui.entities.trainEntities.Train;
import gui.entities.trainEntities.UnscheduledTrain;
import input.SectionInputDialog;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.dispatcher.FreightSimulator;
import simulator.input.ChangeTimeTable;
import simulator.input.SimulationInstance;
import simulator.outputFeatures.graph.Paper;
import simulator.util.Debug;

/**
 * GraphPanel
 */
@SuppressWarnings("serial")
public class GraphPanel extends JPanel implements PropertyChangeListener {
	SimulationInstance simulationInstance = null;
	GraphFrame graphFrame = null;
	/**
	 * btNextTrain
	 */
	JButton btNextTrain;
	/**
	 * btNextStation
	 */
	JButton btNextStation;
	/**
	 * btComplete
	 */
	JButton btComplete;
	/**
	 * btExit
	 */
	JButton btExit;
	/**
	 * btSave take screenshots and save as png files
	 */
	JButton btSave;
	/**
	 * btNextScreen
	 */
	JButton btNextScreen;
	/**
	 * btPrevScreen
	 */
	JButton btPrevScreen;

	JButton zoom;
	JSlider xSlide;
	JSlider ySlide;

	/**
	 * scrollPane
	 */
	JScrollPane scrollPane;
	/**
	 * freightSimulator
	 */
	FreightSimulator freightSimulator;
	/**
	 * trainGraph
	 */
	GraphPaper trainGraph;
	/**
	 * xPoint
	 */
	int xPoint;
	/**
	 * yPoint
	 */
	int yPoint;// pointer variables
	/**
	 * checkBlockReserve
	 */
	JCheckBox checkBlockReserve;

	public JLabel trainNoLabel = new JLabel();
	public JLabel blockNoLabel = new JLabel();
	/**
	 * popup
	 */
	JPopupMenu popup;
	/**
	 * blockReserve
	 */
	boolean blockReserve = true;

	JLabel xLabel;
	JLabel yLabel;

	/**
	 * task
	 */
	private Task task;
	/**
	 * progressBar
	 */
	private JProgressBar progressBar;

	/**
	 * @param propertyChangeEvent
	 *            event
	 * 
	 */
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		if ("progress" == propertyChangeEvent.getPropertyName()) {
			int progress = (Integer) propertyChangeEvent.getNewValue();
			// int progress=freightSimulator.currTrainNo;
			
//Sneha : commented following line as progress bar related code in btcomplete button action listner is coomented out 
//			this.progressBar.setValue(progress);
			// taskOutput.append(String.format(
			// / "Completed %d%% of task.\n", task.getProgress()));
		}
	}

	/**
	 * task
	 */
	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		public Void doInBackground() {
			// Random random = new Random();
			int progress = 0;
			// Initialize progress property.
			setProgress(progress);

			// Sleep for up to one second.

			int numberOfSignalAspects = simulationInstance.numberOfSignalAspects;
			
				while (freightSimulator.getCurrentTrainNo() < freightSimulator
					.getTrainList().size()) {

				// freightSimulator.simulate();
				
				int val = freightSimulator
						.simulateNextTrain(numberOfSignalAspects);
				progress = ((freightSimulator.getCurrentTrainNo() * 100) / freightSimulator
						.getTrainList().size()) + 1;
				// if(freightSimulator.currTrainNo ==
				// freightSimulator.trainsArray.size())
				// progress=100;

				// numSimTrain=progress;
				// System.out.println("NASH2:"+progress+"  "+freightSimulator.currTrainNo
				// +"=="+freightSimulator.trainsArray.size());
				if (val == -1)
					break;
				setProgress(progress);
			}
				
			//SectionInputDialog sectioninputdialog = GlobalVar
						//.getSectionInputDialog();
			/**BlockList blocklist = new BlockList();
			for  ( int i =0 ;i < 10; i ++ )
			{
				System.out.println("just some thing"+blocklist.size());

			}**/
			System.out.println("NASH:" + freightSimulator.getCurrentTrainNo()
					+ "==" + freightSimulator.getTrainList().size());

			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		/**
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			// startButton.setEnabled(true);
			setCursor(null); // turn off the wait cursor

			simulationInstance.displayAverageTravellingTime(
					freightSimulator);

			// Display of Timetable in excel file
			simulationInstance.printTimeTableInExcel();

			// End for Display of Timetable in text file
			// start for headway
			simulationInstance.printHeadwayInExcel();

			// End for Display of Timetable in text file
			// Display of signals seen by trains in a text file
			simulationInstance.printSignalsSeenInExcel();

			// End for Display of Timetable in text file

			System.out.println("Simulation is over ");
			btComplete.setEnabled(false);
			btNextTrain.setEnabled(false);

			// taskOutput.append("Done!\n");
		}
	}

	/**
	 * 
	 * @param s
	 *            freightSimulator
	 * @param graphFrame
	 * @param graphKeyListener
	 */
	public GraphPanel(FreightSimulator s, final GraphFrame graphFrame,
			GraphKeyListener graphKeyListener,
			final SimulationInstance simulationInstance) {
		this.simulationInstance = simulationInstance;
		
		this.freightSimulator = s;
		this.graphFrame = graphFrame;
		setLayout(null);

		this.addKeyListener(graphKeyListener);
		add(trainNoLabel);
		add(blockNoLabel);

		trainGraph = new GraphPaper(simulationInstance);
		add(trainGraph);
		checkBlockReserve = new JCheckBox("Reservations");
		checkBlockReserve.setMnemonic(KeyEvent.VK_R);
		checkBlockReserve.setSelected(true);
		add(checkBlockReserve);
		task = new Task();
		task.addPropertyChangeListener(this);

		checkBlockReserve.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					trainGraph.blockReserve = false;
					clearBlockReservations();
				} else {
					trainGraph.blockReserve = true;
					TrainList trainList = simulationInstance
							.getTrainList();
					trainGraph.drawBlockReservations(trainList);

				}
				trainGraph.drawGraph();
			}
		});

		checkBlockReserve.addKeyListener(graphKeyListener);

		btNextTrain = new JButton("Next Train");
		btNextTrain.addKeyListener(graphKeyListener);

		add(btNextTrain);
		btNextTrain.setMnemonic(KeyEvent.VK_N);
		btNextTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int numberOfSignalAspects = simulationInstance.numberOfSignalAspects;

				int completeSim = freightSimulator
						.simulateNextTrain(numberOfSignalAspects);
				if (completeSim == -1) {
					Debug.print("Complete simulation");
					btNextTrain.setEnabled(false);
					btComplete.setEnabled(false);
					JOptionPane.showMessageDialog(null,
							"All Freight trains Scheduled successfully",
							"Simulator", JOptionPane.INFORMATION_MESSAGE);
					System.out.println("Simulation is over ");

				}

				// btNextTrain.setFocusable(false);
				graphFrame.setFocusable(true);
			}
		});

		btNextStation = new JButton("Print Timetable");
		btNextStation.addKeyListener(graphKeyListener);
		btNextStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulationInstance.getTrainList()
						.printTimeTable();
				Debug.print("In Next Station");
				// btNextStation.setFocusable(false);
				graphFrame.setFocusable(true);
			}
		});

		add(btNextStation);

		btComplete = new JButton("Complete");
		//santhosh
		//create an unscheduled train
		
		btComplete.addKeyListener(graphKeyListener);
		btComplete.addActionListener(new ActionListener() {
			@SuppressWarnings({ "synthetic-access" })
			public void actionPerformed(ActionEvent e) {
				Debug.print("In Complete");
				System.out.print("In complete");
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				task.execute();
		
//sneha commented follwoing progressBar releted code				
/*
				progressBar = new JProgressBar(0, 100);
				progressBar.setValue(0);
				progressBar.setStringPainted(true);

				JPanel panel = new JPanel();
				panel.add(progressBar);

				JFrame frame = new JFrame("ProgressBarDemo");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				// Create and set up the content pane.

				frame.setContentPane(panel);

				// Display the window.
				frame.pack();
				frame.setVisible(true);
*/
				// freightSimulator.simulate();
				/*
				 * double totalTime = 0; int count = 0; for (int i =0 ;
				 * i<GlobalVar.trainArrayList.size(); i++) { Train trn =
				 * (Train)GlobalVar.trainArrayList.get(i);
				 * if((trn.timeTables.size()>0)&&(trn.Sched==false)) {
				 * //system.out
				 * .println(" LLL "+trn.trainNo+"  "+trn.operatingdays );
				 * totalTime += trn.totalTime(); count++; }
				 * //System.out.println(trn.trainNo + "  -  " + trn.totalTime()
				 * + "  -  " + trn.travelTime() ); } if(count != 0) {
				 * System.out.
				 * println("Average Travelling time for freight trains  " +
				 * totalTime);
				 * 
				 * JOptionPane.showMessageDialog(null,
				 * "All Freight trains Scheduled successfully \n Average Travelling time for freight trains  "
				 * +Double.toString(totalTime),
				 * "Simulator",JOptionPane.INFORMATION_MESSAGE); } // Display of
				 * Timetable in excel file Train train1;
				 * 
				 * try{ OutputStream f2 = new FileOutputStream("Timetable.xls");
				 * PrintStream bPrintStream = new PrintStream(f2);
				 * bPrintStream.println
				 * ("T.No."+"\t"+"station Name"+"\t"+"Loop"+"\t"
				 * +"A.Time"+"\t"+"D.Time"); for( int m =0 ; m <
				 * GlobalVar.trainArrayList.size() ; m++) { train1 =
				 * (Train)GlobalVar.trainArrayList.get(m) ;
				 * bPrintStream.print(train1.trainNo+"\t"); for( int n =0 ; n
				 * <train1.timeTables.size(); n++) {
				 * if(((GlobalVar.getStationName
				 * ((train1.timeTables.get(n)).loopNo)))!= null) {
				 * bPrintStream.print
				 * ((GlobalVar.getStationName(((SimulatorTimeTableEntry
				 * )train1.timeTables.get(n)).loopNo))+"\t");
				 * bPrintStream.print( (train1.timeTables.get(n)).loopNo+"\t");
				 * double aTime = (train1.timeTables.get(n)).arrTime; int
				 * aTimeHr=(int)aTime/60; aTime=(aTime-aTimeHr*60)*60; int
				 * aTimeMin =(int)aTime/60; double aTimeSec=aTime-aTimeMin*60;
				 * bPrintStream
				 * .print(aTimeHr+":"+aTimeMin+":"+(int)aTimeSec+"\t"); double
				 * dTime = (train1.timeTables.get(n)).depTime; int
				 * dTimeHr=(int)dTime/60; dTime=(dTime-dTimeHr*60)*60; int
				 * dTimeMin =(int)dTime/60; double dTimeSec=dTime-dTimeMin*60;
				 * bPrintStream
				 * .print(dTimeHr+":"+dTimeMin+":"+(int)dTimeSec+"\t"); }
				 * 
				 * } bPrintStream.println("\t"); } } catch(Exception e1) {
				 * Debug.print("Error in handling o/p file " ); return; }
				 * 
				 * // End for Display of Timetable in text file // start for
				 * headway
				 * 
				 * Train train2; try{ OutputStream f4 = new
				 * FileOutputStream("Headway.xls"); PrintStream bPrintStream =
				 * new PrintStream(f4);
				 * bPrintStream.println("Block No"+"\t"+"Red to Green"
				 * +"\t"+"Red to Double Yellow "); for( int m =0 ; m <1; m++)
				 * //for( int m =0 ; m < GlobalVar.trainArrayList.size(); m++) {
				 * train2 = (Train)GlobalVar.trainArrayList.get(m) ; for( int n
				 * =1 ; n <train2.timeTables.size()-2; n++) { int
				 * sks=(train2.timeTables.get(n)).loopNo; bPrintStream
				 * .print((train2.timeTables.get(n)). loopNo+"\t"); double aTime
				 * = (train2.timeTables.get(n)).arrTime; double dTime =
				 * (train2.timeTables.get(n+2)).depTime; if(train2.Direction==
				 * 0) { bPrintStream.print(Math.round((dTime
				 * -aTime)*60+GlobalVar.sudhir[n+3])+"\t"); } else {
				 * bPrintStream
				 * .print(Math.round((dTime-aTime)*60+GlobalVar.sudhir
				 * [sks-2])+"\t"); } dTime =
				 * (train2.timeTables.get(n+1)).depTime; if(train2.Direction==
				 * 0) { bPrintStream.println(Math.round((dTime
				 * -aTime)*60+GlobalVar.sudhir[n+2])+"\t"); } else {
				 * bPrintStream
				 * .println(Math.round((dTime-aTime)*60+GlobalVar.sudhir
				 * [sks-1])+"\t");
				 * 
				 * } }
				 * 
				 * } } catch(Exception e1) {
				 * Debug.print("Error in handling o/p file " ); return; }
				 * 
				 * // End for Display of Timetable in text file // Display of
				 * signals seen by trains in a text file Train train3;
				 * 
				 * try{ OutputStream f3 = new FileOutputStream("Signal.xls");
				 * PrintStream bPrintStream = new PrintStream(f3);
				 * 
				 * for( int m =0 ; m < GlobalVar.trainArrayList.size() ; m++) {
				 * bPrintStream.print("Train.No."+"\t"); train3 =
				 * (Train)GlobalVar.trainArrayList.get(m) ;
				 * bPrintStream.print(train3.trainNo+"\t"); String dir=null;
				 * dir=(train3.Direction==0)?"UP":"DOWN";
				 * bPrintStream.println(dir+"\t");
				 * bPrintStream.println("**************************************"
				 * ); bPrintStream.println("Block No."+"\t"+"Signal Colour");
				 * bPrintStream
				 * .println("-----------------------------------------------");
				 * for( int n =0 ; n <train3.timeTables.size(); n++) {
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * bPrintStream.print((train3.timeTables.get(n)). loopNo+"\t");
				 * int Color; Color=(train3.timeTables.get(n)).signal;
				 * 
				 * String signalColor = null; if(GlobalVar.NoOfColour ==4) {
				 * signalColor =(6==Color)?"pink":(3==Color)?
				 * "Green":((2==Color)?"DoubleYellow":
				 * ((1==Color)?"Yellow":"Red")); } if(GlobalVar.NoOfColour ==3)
				 * { signalColor =
				 * (6==Color)?"pink":(2==Color)?"Green":(((1==Color
				 * )?"Yellow":"Red")); } if(GlobalVar.NoOfColour ==2) {
				 * signalColor =
				 * (6==Color)?"pink":(0==Color)?"Red":(1==Color)?"Green"
				 * :"Green"; }
				 * 
				 * 
				 * bPrintStream.println(signalColor+"\t"); }
				 * bPrintStream.println("\t"); }
				 * 
				 * 
				 * } catch(Exception e1) {
				 * Debug.print("Error in handling o/p file " ); return; }
				 * 
				 * // End for Display of Timetable in text file
				 * 
				 * System.out.println("Simulation is over ");
				 * btComplete.setEnabled(false); btNextTrain.setEnabled(false);
				 */
				// btComplete.setFocusable(false);
				graphFrame.setFocusable(true);
			}
		});

		add(btComplete);

		btNextScreen = new JButton("Next Screen");
		btNextScreen.addKeyListener(graphKeyListener);
		btNextScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debug.print("In Next Screen");
				trainGraph.nextScreen();
				// btNextScreen.setFocusable(false);
				graphFrame.setFocusable(true);
			}
		});

		add(btNextScreen);

		btPrevScreen = new JButton("Previous Screen");
		btPrevScreen.addKeyListener(graphKeyListener);
		btPrevScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debug.print("In Prev Screen");
				trainGraph.previousScreen();

				// btPrevScreen.setFocusable(false);
				graphFrame.setFocusable(true);
			}
		});

		add(btPrevScreen);

		xSlide = new JSlider(1, 20, 2);
		xSlide.setMajorTickSpacing(2);
		xSlide.setMinorTickSpacing(2);
		xSlide.addKeyListener(graphKeyListener);
		xSlide.addChangeListener(new ChangeListener() {
			/*
			 * public void actionPerformed(ActionEvent e) {
			 * Debug.print("In Prev Screen"); trainGraph.previousScreen(); }
			 */

			@Override
			public void stateChanged(ChangeEvent arg0) {
				double tempY = trainGraph.yScale;
				trainGraph.setScale((double) xSlide.getValue(), tempY);
				// System.out.println("xscale " + trainGraph.xScale);
				trainGraph.redrawGraph();

				// xSlide.setFocusable(false);
				graphFrame.setFocusable(true);
			}
		});

		add(xSlide);
		xLabel = new JLabel("X Scale");
		add(xLabel);

		ySlide = new JSlider(1, 20, 2);
		ySlide.setMajorTickSpacing(2);
		ySlide.setMinorTickSpacing(2);
		ySlide.addKeyListener(graphKeyListener);
		ySlide.addChangeListener(new ChangeListener() {
			/*
			 * public void actionPerformed(ActionEvent e) {
			 * Debug.print("In Prev Screen"); trainGraph.previousScreen(); }
			 */

			@Override
			public void stateChanged(ChangeEvent arg0) {
				double tempX = trainGraph.xScale;
				trainGraph.setScale(tempX, (double) ySlide.getValue());
				// System.out.println("yscale " + trainGraph.yScale);
				trainGraph.redrawGraph();

				// ySlide.setFocusable(false);
				graphFrame.setFocusable(true);
			}
		});

		add(ySlide);
		yLabel = new JLabel("Y Scale");
		add(yLabel);

		btSave = new JButton("Save");
		btSave.addKeyListener(graphKeyListener);
		btSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Save called");
				try {
					Rectangle rect = trainGraph.getBounds();
					BufferedImage screenCapture = new Robot()
							.createScreenCapture(new Rectangle(20 + rect.x,
									20 + rect.y, rect.width, rect.height + 30));

					JFileChooser fc = new JFileChooser();
					fc.showOpenDialog(GraphPanel.this);
					// save as png
					// FileDialog fileDialog = new FileDialog(new Frame("yo"));

					// File file= new File("ScreenCapture.png");
					File file = fc.getSelectedFile();
					ImageIO.write(screenCapture, "png", file);
					System.out.println("Hopefully saved");
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				btSave.setFocusable(false);
				graphFrame.setFocusable(true);
			}
		});

		add(btSave);
		btExit = new JButton("Close");
		btExit.addKeyListener(graphKeyListener);
		btExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debug.print("In Close");
				// System.exit(0);
				simulationInstance.graphFrame.setVisible(false);
			}
		});

		add(btExit);
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				drawBounds();
			}
		});

		popup = new JPopupMenu();
		popup.setLightWeightPopupEnabled(false);

		trainGraph.addMouseListener(new MouseAdapter() { // Add listener to
					// components that can
					// bring up popup
					// menus.
					public void mousePressed(MouseEvent e) {
						maybeShowPopup(e);
					}

					public void mouseReleased(MouseEvent e) {
						maybeShowPopup(e);
					}

					private void maybeShowPopup(MouseEvent e) {
						if (e.isPopupTrigger()) {
							xPoint = e.getX();
							yPoint = e.getY();
							popup = trainGraph.analysePoint(xPoint, yPoint);
							popup.show(e.getComponent(), e.getX(), e.getY());
						}
					}
				});

		freightSimulator.produceGraph(this);
	}

	/**
	 * Set the bounds.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		drawBounds();
		TrainList trainList = simulationInstance.getTrainList();
		StationList stationList = simulationInstance
				.getStationList();
		LoopList loopList = simulationInstance.getLoopList();
		trainGraph.drawGraph(trainList, stationList, loopList);
	}

	/**
	 * Draw the bounds.
	 */
	public void drawBounds() {
		// btPrevScreen.setBounds(400, 10, 130, 30);
		btPrevScreen.setBounds(400, 0, 130, 30);
		btNextScreen.setBounds(550, 0, 130, 30);
		btSave.setBounds(700, 0, 100, 30);
		btNextStation.setBounds(250, 0, 130, 30);
		trainGraph.setBounds(0, 75, getWidth(), getHeight() - 175);
		btExit.setBounds(getWidth() - 150, getHeight() - 75, 120, 30);
		btComplete.setBounds(getWidth() - 400, getHeight() - 75, 120, 30);
		btNextTrain.setBounds(getWidth() - 600, getHeight() - 75, 120, 30);
		checkBlockReserve.setBounds(0, getHeight() - 75, 100, 30);
		trainNoLabel.setBounds(110, getHeight() - 75, 100, 30);
		blockNoLabel.setBounds(220, getHeight() - 75, 100, 30);

		xSlide.setBounds(830, 0, 200, 20);
		xLabel.setBounds(1040, 0, 70, 30);
		ySlide.setBounds(830, 20, 200, 30);
		yLabel.setBounds(1040, 20, 70, 30);
	}

	/**
	 * @return the trainGraph
	 */
	public Paper getPaper() {
		return trainGraph;
	}

	/**
	 * @param Train
	 */
	public void drawTrain(Train Train, int numberOfSignalAspects) {
		trainGraph.drawTrain(Train, numberOfSignalAspects);
	}

	/**
	 * @param Trains
	 *            {@link ArrayList} of trains
	 * @param stnArray
	 *            {@link ArrayList} of stations
	 */
	public void drawGraph(TrainList trainList, StationList stationList,
			LoopList loopList) {
		trainGraph.drawGraph(trainList, stationList, loopList);
	}

	/**
	 * clearBlockReservations
	 */
	public void clearBlockReservations() {
		trainGraph.clearBlockReservations();
	}

	/**
	 * @param trainNumber
	 * @return Color corresponding to the given trainNumber
	 */
	public Color getColor(int trainNumber) {
		Train train = simulationInstance.getTrainFromTrainNo(
				trainNumber);
		if (train != null)
			return train.getDrawColour();

		return Color.black;
	}

}

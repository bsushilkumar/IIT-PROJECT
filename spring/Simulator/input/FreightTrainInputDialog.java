package input;

import input.TrainInputDialog.AllLoopData1;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;

import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntityList.LoopList;
import gui.entities.sectionEntityList.StationList;
import gui.entities.trainEntities.ScheduledTrain;
import gui.entities.trainEntities.Train;
import gui.entities.trainEntities.UnscheduledTrain;
import simulator.input.SimulationInstance;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import simulator.scheduler.SimulationStart;

public class FreightTrainInputDialog extends InputDialog{
	
	private static final long serialVersionUID = 1L;
	public JLabel trainNumberLabel = new JLabel("Train Number");
	public JTextField trainNumberField = new JTextField();
	public JLabel inputTypeLabel = new JLabel("Input type");
	public ButtonGroup inputTypeButtonGroup = new ButtonGroup();
	public JRadioButton locoloadRadioButton = new JRadioButton(
			"Loco-load combination");
	public JRadioButton traindynamicsRadioButton = new JRadioButton(
			"Train dynamics parameters");
	boolean istraindyanmics;
	public JButton viewLoopButton = new JButton("View Loop Details");
	public JLabel directionLabel = new JLabel("Direction");
	public ButtonGroup directionButtonGroup = new ButtonGroup();
	public JRadioButton upRadioButton = new JRadioButton("Up");
	public JRadioButton downRadioButton = new JRadioButton("Down");
	String X = "WDM 2";
	public JLabel locotype = new JLabel("Loco Type");
	public JTextField locotypefield = new JTextField();
	public JComboBox locolist = new JComboBox();
	public String[] locotypeList = { "WDM 2", "WDG 4", "WAP 7", "WAG 7",
			"WAG 9" };
	
	//Variables for Freight Halting
	public JLabel freighthaltLabel = new JLabel("Number of halts");
	public JComboBox<Integer> freighthaltCombo = new JComboBox();
	public ArrayList<JComboBox> stationComboList = new ArrayList<JComboBox>();
	public ArrayList<JTextField> haltTimeFieldList = new ArrayList<JTextField>();
	public boolean freighthaltFlag = false;
	public JLabel stationNameLabel = new JLabel("Station Name");
	public JLabel haltTimeLabel = new JLabel("Minutes of Halt");
	public ArrayList<Station> stationList = new ArrayList<Station>();
	
	public JLabel loadtype = new JLabel("Enter load (tonnes)");
	public JTextField loadtypefield = new JTextField();
	
	public JLabel coachno = new JLabel("Number of coaches");
	public JTextField coachnofield = new JTextField();

	public JLabel lengthLabel = new JLabel("Length in m");

	public JTextField lengthfield = new JTextField();
	public JTextField lengthField = new JTextField();

	public JLabel accelerationLabel = new JLabel("Acceleration in m/s^2");
	public JTextField accelerationField = new JTextField();

	public JLabel decelerationLabel = new JLabel("Deceleration in m/s^2");
	public JTextField decelerationField = new JTextField();
	
	public JLabel priorityLabel = new JLabel("Priority");
	public JTextField priorityField = new JTextField();

	public JLabel maximumSpeedLabel = new JLabel("Maximum Speed in km/hr");
	public JTextField maximumSpeedField = new JTextField();

	public JLabel startLoopLabel = new JLabel("Start Loop");
	public JTextField startLoopField = new JTextField();

	public JLabel endLoopLabel = new JLabel("End Loop");
	public JTextField endLoopField = new JTextField();

	public JLabel minHeadwaySchedule = new JLabel("Headway Scheduled Trains");
	public JTextField minHeadwayScheduleField = new JTextField();
	
	public JLabel minHeadwayFreight = new JLabel("Headway Freight Trains");
	public JTextField minHeadwayFreightField = new JTextField();
	
	public JButton capacityButton = new JButton("Get Capacity");
	public JButton rawCapacityButton=new JButton("Raw Capacity");
	public JLabel errorLabel = new JLabel("");
	public FreightTrainInputDialog() {
		x = 50;
		y = 50;
		width = 800;
		height = 600;
		locolist = new JComboBox<String>(locotypeList);		
		this.setBounds(x, y, width, height);
		
		//GlobalVar.getStationInputDialog().readStations(stationList);
		
		//SimulationInstance simulationInstance = new SimulationInstance();
		//stationList  = simulationInstance.getStationList();
		try {
			GlobalVar.getStationInputDialog().readStations(stationList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stationList = GlobalVar.getSectionInputDialog().stationList;
		Integer[] stationNumberList = new Integer[stationList.size()+1];
		for (int i = 0; i <= stationList.size(); i++) {
			stationNumberList[i] = i;
		}
		freighthaltCombo = new JComboBox<>(stationNumberList);
		
		setComponentActionListeners();
		setComponentBounds();
		addComponents();
		
		this.add(jpanel);

		this.setTitle("Freight Train Input");
		// this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// this.setVisible(true);

	}
	
	@Override
	public void addComponents() {
		
		this.getContentPane().add(inputTypeLabel);
		this.getContentPane().add(locoloadRadioButton);
		this.getContentPane().add(traindynamicsRadioButton);
		inputTypeButtonGroup.add(locoloadRadioButton);
		inputTypeButtonGroup.add(traindynamicsRadioButton);
		locoloadRadioButton.setSelected(true);

		this.getContentPane().add(viewLoopButton);
		
		this.getContentPane().add(freighthaltLabel);
		this.getContentPane().add(freighthaltCombo);

		this.getContentPane().add(directionLabel);
		this.getContentPane().add(upRadioButton);
		this.getContentPane().add(downRadioButton);
		directionButtonGroup.add(upRadioButton);
		directionButtonGroup.add(downRadioButton);
		upRadioButton.setSelected(true);


		this.getContentPane().add(lengthLabel);

		this.getContentPane().add(lengthField);

		this.getContentPane().add(accelerationLabel);
		this.getContentPane().add(accelerationField);

		this.getContentPane().add(decelerationLabel);
		this.getContentPane().add(decelerationField);

		this.getContentPane().add(priorityLabel);
		this.getContentPane().add(priorityField);

		this.getContentPane().add(maximumSpeedLabel);
		this.getContentPane().add(maximumSpeedField);

		this.getContentPane().add(startLoopLabel);
		this.getContentPane().add(startLoopField);

		this.getContentPane().add(endLoopLabel);
		this.getContentPane().add(endLoopField);
		this.getContentPane().add(errorLabel);
		if(!GlobalVar.rawCapacitySelected){
			this.getContentPane().add(capacityButton);
			this.getContentPane().add(minHeadwaySchedule);
			this.getContentPane().add(minHeadwayScheduleField);
			this.getContentPane().add(minHeadwayFreight);
			this.getContentPane().add(minHeadwayFreightField);
		}
		else{
			this.getContentPane().add(rawCapacityButton);
		}

		this.getContentPane().add(locotype);
		// this.getContentPane().add(locotypefield);
		this.getContentPane().add(loadtype);
		this.getContentPane().add(loadtypefield);
		this.getContentPane().add(locolist);
		this.getContentPane().add(coachno);
		this.getContentPane().add(coachnofield);
		this.getContentPane().add(maximumSpeedLabel);
		this.getContentPane().add(maximumSpeedField);
		
				
		// this.getContentPane().add();
		// this.getContentPane().add();
	}
	@Override
	public void setComponentBounds() {
		int yDifference = 25;
		labelWidth = 180;
		
		buttonWidth = 100;
		x2 = x1 + labelWidth + 20;
		trainNumberLabel.setBounds(x1, y1, labelWidth, labelHeight);
		trainNumberField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		inputTypeLabel.setBounds(x1, y1, labelWidth, labelHeight);
		locoloadRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
		
		freighthaltLabel.setBounds(x2+200, y1, labelWidth, labelHeight);
		freighthaltCombo.setBounds(x2+300, y1+5, fieldWidth, fieldHeight);

		y1 += yDifference;
		traindynamicsRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		directionLabel.setBounds(x1, y1, labelWidth, labelHeight);
		upRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
		y1 += yDifference;
		downRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
		//stationLabel.setBounds(x2+250, y1, fieldWidth, fieldHeight);
		//haltMinutesLabel.setBounds(x2+400, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		locotype.setBounds(x1, y1, labelWidth, labelHeight);
		locolist.setBounds(x2, y1, fieldWidth, fieldHeight);
		//stationComboBox.setBounds(x2+250, y1, fieldWidth, fieldHeight);
		//haltMinutesField.setBounds(x2+400, y1, fieldWidth, fieldHeight);

		accelerationLabel.setBounds(x1, y1, labelWidth, labelHeight);
		accelerationField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		loadtype.setBounds(x1, y1, labelWidth, labelHeight);
		loadtypefield.setBounds(x2, y1, fieldWidth, fieldHeight);

		decelerationLabel.setBounds(x1, y1, labelWidth, labelHeight);
		decelerationField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		coachno.setBounds(x1, y1, labelWidth, labelHeight);
		coachnofield.setBounds(x2, y1, fieldWidth, fieldHeight);

		lengthLabel.setBounds(x1, y1, labelWidth, labelHeight);
		lengthField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		maximumSpeedLabel.setBounds(x1, y1, labelWidth, labelHeight);
		maximumSpeedField.setBounds(x2, y1, fieldWidth, fieldHeight);
		
		y1 += yDifference;
		maximumSpeedLabel.setBounds(x1, y1, labelWidth, labelHeight);
		maximumSpeedField.setBounds(x2, y1, fieldWidth, fieldHeight);
		
		y1 += yDifference;
		priorityLabel.setBounds(x1, y1, labelWidth, labelHeight);
		priorityField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		startLoopLabel.setBounds(x1, y1, labelWidth, labelHeight);
		startLoopField.setBounds(x2, y1, fieldWidth, fieldHeight);
		
		y1 += yDifference;

		endLoopLabel.setBounds(x1, y1, labelWidth, labelHeight);
		endLoopField.setBounds(x2, y1, fieldWidth, fieldHeight);
		
		
		
		if(!GlobalVar.rawCapacitySelected){
			y1 += yDifference;

			minHeadwaySchedule.setBounds(x1, y1, labelWidth, labelHeight);
			minHeadwayScheduleField.setBounds(x2, y1, fieldWidth, fieldHeight);
			
			y1 += yDifference;

			minHeadwayFreight.setBounds(x1, y1, labelWidth, labelHeight);
			minHeadwayFreightField.setBounds(x2, y1, fieldWidth, fieldHeight);
			
			y1 += yDifference;
			capacityButton.setBounds(x1, y1, 150, buttonHeight);
		}else{
			y1 += yDifference;
			rawCapacityButton.setBounds(x1, y1, 150, buttonHeight);
		}
		y1 += yDifference;
		errorLabel.setBounds(x1, y1, 500, labelHeight);
		
	}
	@Override
	public void setComponentActionListeners() {
		
		locoloadRadioButton.addItemListener(inputTypeItemListener);
		traindynamicsRadioButton.addItemListener(inputTypeItemListener);
		capacityButton.addActionListener(capacityActionListener);
		rawCapacityButton.addActionListener(rawCapacityActionListener);
		viewLoopButton.addActionListener(viewLoopActionListener);
		locolist.addActionListener(locolistActionListener);
		locoloadRadioButton.addItemListener(inputTypeItemListener);
		traindynamicsRadioButton.addItemListener(inputTypeItemListener);
		
		freighthaltCombo.addActionListener(freighthaltActionListner);
	}
	
	ActionListener freighthaltActionListner = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			{	
				
				String x = String.valueOf(freighthaltCombo.getSelectedItem());
				int foo = Integer.parseInt(x);
				freighthaltGenerator(foo);
			}
		}
	};	
	
	/** Vidyadhar
	 * This function gets 'x' as input which is no. of scheduled halts 
	 * for a freight train. Then the function generates stationCombos and 
	 * haltTimeField corresponding to the no. of scheduled halts.
	 * To record changes made while entering, freighthaltFlag is used
	 * freighthaltFlag is by default set as 'false'. Once the first entry is made,
	 * it is been set as 'true. With this, the function clears all past entry and 
	 * records new entries.
	 * @param x
	 */
	void freighthaltGenerator(int x) {
			
			//Checking whether entry is already made or not
			if(freighthaltFlag){		
				for (int i = 0; i < stationComboList.size(); i++) {
					//blockNumberField = blockNumberFieldList.get(i);
					//minutesField = minutesFieldList.get(i);
					//totalMinutesField = totalMinutesFieldList.get(i);
					

					jpanel.remove(stationComboList.get(i));
					//jpanel.remove(minutesField);
					jpanel.remove(haltTimeFieldList.get(i));
					jpanel.revalidate();
					jpanel.repaint();
				}
				stationComboList.clear();
				//minutesFieldList.clear();
				haltTimeFieldList.clear();

			}

			int x1 = 420;
			int x2 = 650;		
			int y1 = 95;		
			int ydifference = 25;
			int y2 = 95;		
			//SectionInputDialog sectioninputdialog = GlobalVar
					//.getSectionInputDialog();
			//ArrayList<Station> stationlist = sectioninputdialog.stationList;
			String[] stationNameList = new String[stationList.size()];
			JComboBox[] stationCombo = new JComboBox[x];
			JTextField[] haltTimeField = new JTextField[x];
			
			if (x!=0){
				stationNameLabel.setBounds(x1, y1, labelWidth, 
						labelHeight);
				haltTimeLabel.setBounds(x2, y1, labelWidth, 
						labelHeight);
				jpanel.add(stationNameLabel);	
				jpanel.add(haltTimeLabel);	
				jpanel.revalidate();
				jpanel.repaint();
				jpanel.setLayout(null);
			}
			
			for (int i = 0; i < stationList.size(); i++) {
				stationNameList[i] = String.valueOf(stationList.get(i).getStationName());
			}
				
			
			
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < x; j++) {				
					if (i == 0) {
						stationCombo[j] = new JComboBox(stationNameList);
						stationComboList.add(stationCombo[j]);
						y1 += ydifference;
						stationCombo[j].setBounds(x1, y1, 2*fieldWidth, 
								fieldHeight);
						jpanel.add(stationCombo[j]);
						
						jpanel.revalidate();
						jpanel.repaint();
						jpanel.setLayout(null);
						
					} else if (i == 1) {
						haltTimeField[j] = new JTextField();
						haltTimeFieldList.add(haltTimeField[j]);
						y2 += ydifference;
						haltTimeField[j].setBounds(x2, y2, fieldWidth, 
								fieldHeight);
						jpanel.add(haltTimeField[j]);
						
						jpanel.revalidate();
						jpanel.repaint();
						jpanel.setLayout(null);
					}				
						
				}

			}
			freighthaltFlag = true; // Setting true, if entries is made
		}	
	
	
	
	
	ActionListener viewLoopActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			AllLoopData1 allLoopData = new AllLoopData1();

		}
	};
	
	ActionListener locolistActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {

			X = String.valueOf(locolist.getSelectedItem());

		}
	};
	
	ItemListener inputTypeItemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			// TrainInputDialog trainInputDialog =
			// GlobalVar.getTrainInputDialog();

			istraindyanmics = traindynamicsRadioButton.isSelected();
			
			accelerationLabel.setVisible(istraindyanmics);
			decelerationLabel.setVisible(istraindyanmics);
			accelerationField.setVisible(istraindyanmics);
			decelerationField.setVisible(istraindyanmics);
			lengthLabel.setVisible(istraindyanmics);
			lengthField.setVisible(istraindyanmics);
			
			locotype.setVisible(!istraindyanmics);
			loadtype.setVisible(!istraindyanmics);
			
			locolist.setVisible(!istraindyanmics);
			loadtypefield.setVisible(!istraindyanmics);
			coachno.setVisible(!istraindyanmics);
			coachnofield.setVisible(!istraindyanmics);

			maximumSpeedLabel.setVisible(istraindyanmics);
			maximumSpeedField.setVisible(istraindyanmics);
			//santhosh
			if(locoloadRadioButton.isSelected()){
				lengthLabel.setVisible(!istraindyanmics);
				lengthField.setVisible(!istraindyanmics);
				coachno.setVisible(istraindyanmics);
				coachnofield.setVisible(istraindyanmics);
			}else if(traindynamicsRadioButton.isSelected()){
				lengthLabel.setVisible(istraindyanmics);
				lengthField.setVisible(istraindyanmics);
				coachno.setVisible(!istraindyanmics);
				coachnofield.setVisible(!istraindyanmics);
			}else if(traindynamicsRadioButton.isSelected()){
				lengthLabel.setVisible(istraindyanmics);
				lengthField.setVisible(istraindyanmics);
				coachno.setVisible(!istraindyanmics);
				coachnofield.setVisible(!istraindyanmics);
			}else if(locoloadRadioButton.isSelected()){
				lengthLabel.setVisible(istraindyanmics);
				lengthField.setVisible(istraindyanmics);
				coachno.setVisible(!istraindyanmics);
				coachnofield.setVisible(!istraindyanmics);
			}

		}

	};
	
	ActionListener capacityActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			SimulationStart simulationStart = new SimulationStart();
			GlobalVar.capacitySelected=true;
			Train freightTrain=null;
			try {
				freightTrain= setTrainProperties();
				GlobalVar.freightTrain=freightTrain;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GlobalVar.capacitySelected=true;
			simulationStart.start();
		}
	};
	ActionListener rawCapacityActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			SimulationStart simulationStart = new SimulationStart();
			Train freightTrain=null;
			try {
				freightTrain= setTrainProperties();
				GlobalVar.freightTrain=freightTrain;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			simulationStart.start();
		}
	};
	protected Train setTrainProperties() throws Exception {
		if(!GlobalVar.rawCapacitySelected){
			GlobalVar.headwayScheduled=Integer.parseInt(minHeadwayScheduleField.getText());
			GlobalVar.headwayFreight=Integer.parseInt(minHeadwayFreightField.getText());
		}
		UnscheduledTrain train=new UnscheduledTrain();
		//int trainNumber = Integer.parseInt(trainNumberField.getText());
		int direction = GlobalVar.UP_DIRECTION;
		if (downRadioButton.isSelected())
			direction = GlobalVar.DOWN_DIRECTION;
		double length = .4;
		double acceleration = .7;
		double deceleration = .7;
		double maximumSpeed = 100;
		if (locoloadRadioButton.isSelected()) {
			String tempLength=lengthField.getText();
			length = (Double.parseDouble(lengthField.getText()))/1000;
			if (X == "WDM 2") {
				acceleration = 1 - .0001 * Double.parseDouble(loadtypefield
						.getText());
				deceleration = 1 - .0001 * Double.parseDouble(loadtypefield
						.getText());
				maximumSpeed = 120 - .005 * Double.parseDouble(loadtypefield
						.getText());

			} else if (X == "WDG 4") {
				acceleration = .9 - .0001 * Double.parseDouble(loadtypefield
						.getText());
				deceleration = .9 - .0001 * Double.parseDouble(loadtypefield
						.getText());
				maximumSpeed = 110 - .005 * Double.parseDouble(loadtypefield
						.getText());

			} else if (X == "WAP 7") {
				acceleration = .8 - .0001 * Double.parseDouble(loadtypefield
						.getText());
				deceleration = .8 - .0001 * Double.parseDouble(loadtypefield
						.getText());
				maximumSpeed = 125 - .005 * Double.parseDouble(loadtypefield
						.getText());

			} else if (X == "WAG 7") {
				acceleration = .95 - .0001 * Double.parseDouble(loadtypefield
						.getText());
				deceleration = .95 - .0001 * Double.parseDouble(loadtypefield
						.getText());
				maximumSpeed = 115 - .005 * Double.parseDouble(loadtypefield
						.getText());

			} else if (X == "WAG 9") {
				acceleration = 1.05 - .0001 * Double.parseDouble(loadtypefield
						.getText());
				deceleration = 1.05 - .0001 * Double.parseDouble(loadtypefield
						.getText());
				maximumSpeed = 100 - .005 * Double.parseDouble(loadtypefield
						.getText());

			}

		
		}else {
			length = (Double.parseDouble(lengthField.getText()))/1000;
			acceleration = Double.parseDouble(accelerationField.getText())*3.6;
			deceleration = Double.parseDouble(decelerationField.getText())*3.6;
			maximumSpeed = Double.parseDouble(maximumSpeedField.getText());

		}

		int priority = Integer.parseInt(priorityField.getText());
		int numberOfHalts = Integer.parseInt(String.valueOf(freighthaltCombo.getSelectedItem()));
		//train.trainNo = trainNumber;
		train.scheduled = false;
		train.direction = direction;
		train.length = length;
		train.accParam = acceleration;
		train.deceParam = deceleration;
		train.priority = priority;
		train.maximumPossibleSpeed = maximumSpeed;
		int startLoop = Integer.parseInt(startLoopField.getText());
		int endLoop = Integer.parseInt(endLoopField.getText());
		train.startLoopNo = startLoop;
		train.endLoopNo = endLoop;
		
		//R.Vidyadhar
		//Updating Scheduled halt for freight train
		//Also checking for Multiple entries of same Station
		//If multiple entries are noted, 0 will be returned
		for (int m=0 ; m<numberOfHalts ; m++){
			String station = String.valueOf(stationComboList.get(m).getSelectedItem());
			for (int n=m+1 ; n<numberOfHalts ; n++){
				if (station == String.valueOf(stationComboList.get(n).getSelectedItem())){
					errorLabel.setText("Multiple entries for freight halting station  " + station + 
							" is found");
					return null;
				}					
			}
			int haltingMinutes = Integer.parseInt(haltTimeFieldList.get(m).getText());
			//System.out.println("Station " + station + "haltingMinutes = "+ haltingMinutes);
			ReferenceTableEntry freightRefEntry = new ReferenceTableEntry(station,	haltingMinutes);
			train.getRefTables().add(freightRefEntry);
		}
		train.setNumberofHalts(numberOfHalts);
		return train;

	}
	class AllLoopData1 extends JFrame implements TableModelListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JTable loopDataTable;

		public AllLoopData1() {

			this.setTitle("List of all loops");

			loopDataTable = new JTable(new AbstractTableModel() {
				SectionInputDialog sectioninputdialog = GlobalVar
						.getSectionInputDialog();
				ArrayList<Loop> loopList = sectioninputdialog.loopList;

				/**
				 * 

				 *
				 */

				private static final long serialVersionUID = 1L;

				String[] columnNames = { "Id", "Direction", "Station Name",
						"Type", "Type of trains allowed", "Maximum Speed" };

				public String getColumnName(int col) {
					return columnNames[col];
				}

				public int getRowCount() {
					return loopList.size();
				}

				public int getColumnCount() {
					return columnNames.length;
				}

				public Object getValueAt(int row, int col) {
					switch (col) {
					case 0:
						return loopList.get(row).blockNo;
					case 1:
						int direction = loopList.get(row).direction;
						String directionString = GlobalVar
								.getDirectionStringFromDirection(direction);
						return directionString;
					case 2:
						return loopList.get(row).stationName;
					case 3:
						String loopType = "mainLine";
						if (loopList.get(row).whetherMainLine != 0)
							loopType = "loop";
						return loopType;
					case 4:	
						return loopList.get(row).getLoopTrainType();
					case 5:
						return loopList.get(row).maximumPossibleSpeed;
					}

					return null;
				}

			});

			JScrollPane scrollPane = new JScrollPane(loopDataTable);
			loopDataTable.setPreferredScrollableViewportSize(new Dimension(500,
					70));
			getContentPane().add(scrollPane);
			setBounds(70, 70, 900, 400);
			setVisible(true);
		}

		@Override
		public void tableChanged(TableModelEvent arg0) {
		}

	}
}

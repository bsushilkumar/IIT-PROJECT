package input;

import globalVariables.FileNames;

import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.trainEntities.ScheduledTrain;
import gui.entities.trainEntities.Train;
import gui.entities.trainEntities.UnscheduledTrain;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.JOptionPane;
import simulator.input.ChangeTimeTable;

public class TrainInputDialog extends InputDialog {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public JLabel trainNumberLabel = new JLabel("Train Number");
	public JTextField trainNumberField = new JTextField();

	public JLabel trainTypeLabel = new JLabel("Train Type");
	public ButtonGroup trainTypeButtonGroup = new ButtonGroup();
	public JRadioButton passengerTrainRadioButton = new JRadioButton(
			"Scheduled");
	public JRadioButton freightTrainRadioButton = new JRadioButton("Freight");
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
	
	//Variables for Freight Halting
	public JLabel freighthaltLabel = new JLabel("Number of halts");
	public JComboBox<Integer> freighthaltCombo = new JComboBox();
	public ArrayList<JComboBox> stationComboList = new ArrayList<JComboBox>();
	public ArrayList<JTextField> haltTimeFieldList = new ArrayList<JTextField>();
	public boolean freighthaltFlag = false;
	public JLabel stationNameLabel = new JLabel("Station Name");
	public JLabel haltTimeLabel = new JLabel("Minutes of Halt");
	
	public JLabel locotype = new JLabel("Loco Type");
	public JTextField locotypefield = new JTextField();
	public JComboBox locolist = new JComboBox();
	public String[] locotypeList = { "WDM 2", "WDG 4", "WAP 7", "WAG 7",
			"WAG 9" };
	
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

	/*
	 * public JLabel haltnumberLabel = new JLabel("No. of halts"); public
	 * JTextField haltnumberField = new JTextField();
	 */

	public JLabel stationLabel = new JLabel("Station");
	public JTextField stationField=new JTextField();
	public JComboBox stationLoopsCombo=null;

	public JLabel priorityLabel = new JLabel("Priority");
	public JTextField priorityField = new JTextField();

	public JLabel maximumSpeedLabel = new JLabel("Maximum Speed in km/hr");
	public JTextField maximumSpeedField = new JTextField();

	public JLabel startLoopLabel = new JLabel("Start Loop");
	public JTextField startLoopField = new JTextField();

	public JLabel endLoopLabel = new JLabel("End Loop");
	public JTextField endLoopField = new JTextField();

	public JLabel startTimeLabel = new JLabel("Start Time in HHMM");
	public JTextField startTimeField = new JTextField();

	public JLabel scheduledTrainTimeTableLabel = new JLabel("Timetable");

	public JLabel loopLabel = new JLabel("Loop Id");
	public JTextField loopField = new JTextField();

	public JLabel arrivalTimeLabel = new JLabel("Arrival in HHMM");
	public JTextField arrivalTimeField = new JTextField();

	public JLabel departureTimeLabel = new JLabel("Departure in HHMM");
	public JTextField departureTimeField = new JTextField();

	public JLabel operationDaysLabel = new JLabel("Operation Days e.g. all");
	public JTextField operationDaysField = new JTextField();

	public JButton addButton = new JButton("Add Train");
	public JButton addTimeTableEntryButton = new JButton("Add Timetable entry");

	public JLabel editDeleteOptionLabel = new JLabel("Edit/Delete");

	public JLabel editDeleteTrainNumberLabel = new JLabel("Train Number");
	public JTextField editDeleteTrainNumberField = new JTextField();

	public JButton editButton = new JButton("Edit");
	public JButton deleteButton = new JButton("Delete");
	public JButton updateButton = new JButton("Update");

	// public JButton Button = new JButton("");

	public JLabel Label = new JLabel("");
	public JTextField Field = new JTextField();

	public JRadioButton Button = new JRadioButton("");
	public ButtonGroup ButtonGroup = new ButtonGroup();
	public ArrayList<Station> stationList = null;
	public ArrayList<JComboBox> loopFieldList = new ArrayList<JComboBox>();
	public ArrayList<JTextField> arrivalTimeFieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> departureTimeFieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> stationFieldList=new ArrayList<JTextField>();
	public ArrayList<JCheckBox> checkHaltFieldList=new ArrayList<JCheckBox>();
	public JMenuBar menuBar = new JMenuBar();
	public JMenu viewMenu = new JMenu("View");
	public JMenuItem allScheduledTrainsMenuItem = new JMenuItem(
			"Scheduled Trains");
	public JMenuItem allFreightTrainsMenuItem = new JMenuItem("Freight Trains");

	public JLabel resultLabel = new JLabel("");
	public JLabel errorLabel = new JLabel("");
	public Train trainToBeEdited = null;
	public JButton doneButton = new JButton("Done");
	public JLabel noHaltLabel=new JLabel("No stopping");
	public JCheckBox checkHalt=null;
	public TrainInputDialog() {
		x = 50;
		y = 50;
		width = 800;
		height = 700;
		this.setBounds(x, y, width, height);
		
		locolist = new JComboBox<String>(locotypeList);
		
		stationList = GlobalVar.getSectionInputDialog().stationList;
		Integer[] stationNumberList = new Integer[stationList.size()+1];
		for (int i = 0; i <= stationList.size(); i++) {
			stationNumberList[i] = i;
		}
		freighthaltCombo = new JComboBox<>(stationNumberList);

		//stationList = GlobalVar.getSectionInputDialog().stationList;

		setComponentActionListeners();
		setComponentBounds();
		addComponents();
		
		this.add(jpanel);

		this.setTitle("Train Input");
		// this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// this.setVisible(true);

	}

	@Override
	public void addComponents() {
		viewMenu.add(allFreightTrainsMenuItem);
		viewMenu.add(allScheduledTrainsMenuItem);
		menuBar.add(viewMenu);
		this.setJMenuBar(menuBar);
		
		this.getContentPane().add(trainNumberLabel);
		this.getContentPane().add(trainNumberField);

		this.getContentPane().add(trainTypeLabel);
		this.getContentPane().add(passengerTrainRadioButton);
		this.getContentPane().add(freightTrainRadioButton);
		trainTypeButtonGroup.add(passengerTrainRadioButton);
		trainTypeButtonGroup.add(freightTrainRadioButton);
		freightTrainRadioButton.setSelected(true);
		
		this.getContentPane().add(freighthaltLabel);
		this.getContentPane().add(freighthaltCombo);
		
		this.getContentPane().add(inputTypeLabel);
		this.getContentPane().add(locoloadRadioButton);
		this.getContentPane().add(traindynamicsRadioButton);
		inputTypeButtonGroup.add(locoloadRadioButton);
		inputTypeButtonGroup.add(traindynamicsRadioButton);
		locoloadRadioButton.setSelected(true);

		this.getContentPane().add(viewLoopButton);

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

		this.getContentPane().add(startTimeLabel);
		this.getContentPane().add(startTimeField);

		this.getContentPane().add(operationDaysLabel);
		this.getContentPane().add(operationDaysField);

		this.getContentPane().add(scheduledTrainTimeTableLabel);
		this.getContentPane().add(stationLabel);
		this.getContentPane().add(loopLabel);
		this.getContentPane().add(arrivalTimeLabel);
		this.getContentPane().add(departureTimeLabel);

		this.getContentPane().add(addButton);

		this.getContentPane().add(editDeleteOptionLabel);
		this.getContentPane().add(editDeleteTrainNumberLabel);
		this.getContentPane().add(editDeleteTrainNumberField);

		/*
		 * this.getContentPane().add(haltnumberLabel);
		 * this.getContentPane().add(haltnumberField);
		 */

		this.getContentPane().add(editButton);
		this.getContentPane().add(updateButton);
		
		this.getContentPane().add(deleteButton);
		this.getContentPane().add(addTimeTableEntryButton);

		this.getContentPane().add(resultLabel);
		this.getContentPane().add(errorLabel);
		this.getContentPane().add(doneButton);

		this.getContentPane().add(arrivalTimeLabel);
		this.getContentPane().add(departureTimeLabel);

		this.getContentPane().add(addButton);

		this.getContentPane().add(editDeleteOptionLabel);
		this.getContentPane().add(editDeleteTrainNumberLabel);
		this.getContentPane().add(editDeleteTrainNumberField);

		this.getContentPane().add(locotype);
		// this.getContentPane().add(locotypefield);
		this.getContentPane().add(loadtype);
		this.getContentPane().add(loadtypefield);
		this.getContentPane().add(locolist);
		this.getContentPane().add(coachno);
		this.getContentPane().add(coachnofield);
		
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
		trainTypeLabel.setBounds(x1, y1, labelWidth, labelHeight);
		freightTrainRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
		y1 += yDifference;
		passengerTrainRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		directionLabel.setBounds(x1, y1, labelWidth, labelHeight);
		upRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
		y1 += yDifference;
		downRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		locotype.setBounds(x1, y1, labelWidth, labelHeight);
		locolist.setBounds(x2, y1, fieldWidth, fieldHeight);

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
		priorityLabel.setBounds(x1, y1, labelWidth, labelHeight);
		priorityField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		startLoopLabel.setBounds(x1, y1, labelWidth, labelHeight);
		startLoopField.setBounds(x2, y1, fieldWidth, fieldHeight);
		operationDaysLabel.setBounds(x1, y1, labelWidth, labelHeight);
		operationDaysField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;

		/*
		 * haltnumberLabel.setBounds(x1, y1, labelWidth, labelHeight);
		 * haltnumberField.setBounds(x2, y1, fieldWidth, fieldHeight);
		 */

		endLoopLabel.setBounds(x1, y1, labelWidth, labelHeight);
		endLoopField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		startTimeLabel.setBounds(x1, y1, labelWidth, labelHeight);
		startTimeField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		addButton.setBounds(x1, y1, 150, buttonHeight);
		viewLoopButton.setBounds(x2, y1, 150, buttonHeight);

		y1 += 2 * yDifference;
		editDeleteOptionLabel.setBounds(x1, y1, buttonWidth, labelHeight);

		y1 += yDifference;
		editDeleteTrainNumberLabel.setBounds(x1, y1, labelWidth, labelHeight);
		editDeleteTrainNumberField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		editButton.setBounds(x1, y1, buttonWidth, buttonHeight);
		deleteButton.setBounds(x2, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		updateButton.setBounds(x1, y1, buttonWidth, buttonHeight);
		doneButton.setBounds(x2, y1, buttonWidth, buttonHeight);
		
		y1 += yDifference;
		errorLabel.setBounds(x1, y1, 500, labelHeight);
		
		y1 += yDifference;
		resultLabel.setBounds(x1, y1, 200, labelHeight);

		// time table entry bounds
		setTimeTableEntryBounds();
	}

	public void setTimeTableEntryBounds() {
		int x0=350;
		//x1 = 400;
		//x2 = 500;
		//x3 = 650;
		x1=460;
		x2=570;
		x3=680;
		x4=790;
		labelWidth = 150;
		fieldWidth = 90;
		y1 = 20;
		fieldWidth = 70;
		int yDifference = 25;

		scheduledTrainTimeTableLabel.setBounds(x0, y1, 200, labelHeight);

		y1 += yDifference;
		stationLabel.setBounds(x0,y1,90,labelHeight);
		loopLabel.setBounds(x1, y1, 90, labelHeight);
		arrivalTimeLabel.setBounds(x2, y1, 140, labelHeight);
		departureTimeLabel.setBounds(x3, y1, 140, labelHeight);
		noHaltLabel.setBounds(x4,y1,90,labelHeight);
		//stationListCombo.setBounds(x0+labelWidth,y1,fieldWidth,fieldHeight);
		this.remove(jpanel);
		clearTimeTablePanel();

		for (int i = 0; i < stationList.size(); i++) {
			y1 += yDifference;
			addTimeTableEntry();
			stationField.setText(stationList.get(i).stationName);
			stationField.setEditable(false);
			stationFieldList.add(stationField);
			stationField.setBounds(x0,y1,fieldWidth, fieldHeight);
			ArrayList loopList=GlobalVar.getSectionInputDialog().loopList;
			DefaultComboBoxModel loopIds= new DefaultComboBoxModel();
			String tempLoopDir="";
			String tempLoopType="";
			for(int k=0;k<loopList.size();k++){
				Loop loop=(Loop)loopList.get(k);
				if(loop.stationName.equals(stationList.get(i).stationName)){
					String blockNo=String.valueOf(loop.getBlockNo());
					if(blockNo.length()==5){
						tempLoopDir=blockNo.substring(1, 2);
						tempLoopType=blockNo.substring(1,3);
					}else if(blockNo.length()==6){
						tempLoopDir=blockNo.substring(2, 3);
						tempLoopType=blockNo.substring(2,4);
					}
							
					String loopType="";
					String loopDir="";
					switch(tempLoopDir){
						case "0":
							loopDir="Up";
							break;
						case "1":
							loopDir="Down";
							break;
						case "2":
							loopDir="Common";
							break;
						
					}
					switch(tempLoopType){
					case "00":
						loopType="ML";
						break;
					case "10":
						loopType="ML";
						break;
					case "01":
						loopType="LL";
						break;
					case "11":
						loopType="LL";
						break;
					case "21":
						loopType="CML";
						break;
					case "22":
						loopType="CUL";
						break;
					case "23":
						loopType="CDL";
						break;
				}
					loopIds.addElement(loop.getBlockNo()+"_"+loopDir+"_"+loopType);
				}
			}
			stationLoopsCombo = new JComboBox(loopIds);
			loopFieldList.add(stationLoopsCombo);
			stationLoopsCombo.setBounds(x1-20, y1, fieldWidth+60, fieldHeight);
			//loopField.setBounds(x1, y1, fieldWidth, fieldHeight);
			arrivalTimeField.setBounds(x2+30, y1, fieldWidth, fieldHeight);
			departureTimeField.setBounds(x3+30, y1, fieldWidth, fieldHeight);
			/*checkHalt=new JCheckBox();
			checkHalt.addItemListener(haltItemListener);
			checkHaltFieldList.add(checkHalt);
			checkHalt.setBounds(x4,y1,20,20);*/
			this.getContentPane().add(stationField);
			this.getContentPane().add(stationLoopsCombo);
			this.getContentPane().add(arrivalTimeField);
			this.getContentPane().add(departureTimeField);
			//this.getContentPane().add(checkHalt);
		}

		this.setTimeTableEntryPanelVisible();

		this.add(jpanel);
		this.repaint();
	}

	private void clearTimeTablePanel() {
		for (int i = 0; i < loopFieldList.size(); i++) {
			stationLoopsCombo = loopFieldList.get(i);
			arrivalTimeField = arrivalTimeFieldList.get(i);
			departureTimeField = departureTimeFieldList.get(i);
			//checkHalt=checkHaltFieldList.get(i);
			this.getContentPane().remove(stationField);
			this.getContentPane().remove(stationLoopsCombo);
			this.getContentPane().remove(arrivalTimeField);
			this.getContentPane().remove(departureTimeField);
			//this.getContentPane().remove(checkHalt);
		}
	}

	private void addTimeTableEntry() {
		arrivalTimeField = new JTextField();
		departureTimeField = new JTextField();
		stationField= new JTextField();
		//checkHalt=new JCheckBox();
		//loopFieldList.add(stationLoopsCombo);
		
		arrivalTimeFieldList.add(arrivalTimeField);
		departureTimeFieldList.add(departureTimeField);
		//checkHaltFieldList.add(checkHalt);
	}

	@Override
	public void setComponentActionListeners() {

		locoloadRadioButton.addItemListener(inputTypeItemListener);
		traindynamicsRadioButton.addItemListener(inputTypeItemListener);
		passengerTrainRadioButton.addItemListener(trainTypeItemListener);
		freightTrainRadioButton.addItemListener(trainTypeItemListener);
		allFreightTrainsMenuItem
				.addActionListener(viewAllFreightTrainsActionListener);
		allScheduledTrainsMenuItem
				.addActionListener(viewAllScheduledTrainsActionListener);
		addButton.addActionListener(addActionListener);
		editButton.addActionListener(editActionListener);
		deleteButton.addActionListener(deleteActionListener);
		updateButton.addActionListener(updateActionListener);
		doneButton.addActionListener(DoneActionListener);
		viewLoopButton.addActionListener(viewLoopActionListener);
		locolist.addActionListener(locolistActionListener);
		
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
		int x2 = 620;		
		int y1 = 95;		
		int ydifference = 25;
		int y2 = 95;		
		SectionInputDialog sectioninputdialog = GlobalVar
				.getSectionInputDialog();
		ArrayList<Station> stationlist = sectioninputdialog.stationList;
		String[] stationNameList = new String[stationlist.size()];
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
		
		for (int i = 0; i < stationlist.size(); i++) {
			stationNameList[i] = String.valueOf(stationlist.get(i).getStationName());
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
					haltTimeField[j].setBounds(x2, y2, 2*fieldWidth, 
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
	
	/*ItemListener haltItemListener=new ItemListener(){
		public void itemStateChanged(ItemEvent arg0) {
			System.out.println("Hikjdfslakdfjlasjkd");
			if(checkHalt.isSelected()==true){
				stationLoopsCombo.setEnabled(false);
				arrivalTimeField.setEnabled(false);
				departureTimeField.setEnabled(false);
				System.out.println("selected");
			}else{
				stationLoopsCombo.setEnabled(true);
				arrivalTimeField.setEnabled(true);
				departureTimeField.setEnabled(true);
				System.out.println("please check");
			}
		}
	};*/
	ActionListener locolistActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {

			X = String.valueOf(locolist.getSelectedItem());

		}
	};

	ActionListener editActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (editDeleteTrainNumberField.getText().isEmpty()) {
				resultLabel
						.setText("Input train number to edit or delete a train");
				return;
			}

			try {
				int trainNumber = Integer.parseInt(editDeleteTrainNumberField
						.getText());
				Train findTrain = GlobalVar.getSectionInputDialog()
						.getTrainByTrainNumber(trainNumber);
				if (findTrain == null) {
					resultLabel.setText("No train " + trainNumber + " exists");
					return;
				}

				setFieldsFromTrainAttributes(findTrain);
				trainToBeEdited = findTrain;
				resultLabel.setText("Train " + trainNumber + " is shown");
			} catch (Exception ex) {
				resultLabel
						.setText("Check train number field for edit/delete option");
			}
		}
	};

	ActionListener deleteActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (editDeleteTrainNumberField.getText().isEmpty()) {
				resultLabel
						.setText("Input train number to edit or delete a train");
				return;
			}

			try {
				int trainNumber = Integer.parseInt(editDeleteTrainNumberField
						.getText());
				Train findTrain = GlobalVar.getSectionInputDialog()
						.getTrainByTrainNumber(trainNumber);
				if (findTrain == null) {
					resultLabel.setText("No train " + trainNumber + " exists");
					return;
				}
				GlobalVar.getSectionInputDialog().removeTrain(findTrain);
				resultLabel.setText("Train " + trainNumber + " deleted");

			} catch (Exception ex) {
				resultLabel
						.setText("Check train number field for edit/delete option");
			}
		}
	};

	ItemListener trainTypeItemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			// TrainInputDialog trainInputDialog =
			// GlobalVar.getTrainInputDialog();

			boolean isFreightTrain = freightTrainRadioButton.isSelected();

			operationDaysLabel.setVisible(!isFreightTrain);
			operationDaysField.setVisible(!isFreightTrain);
			/*
			 * haltnumberLabel.setVisible(!isFreightTrain);
			 * haltnumberField.setVisible(!isFreightTrain);
			 */
			loadtype.setVisible(isFreightTrain && !istraindyanmics);
			loadtypefield.setVisible(isFreightTrain && !istraindyanmics);
			lengthLabel.setVisible(isFreightTrain || istraindyanmics);
			lengthField.setVisible(isFreightTrain || istraindyanmics);
			coachno.setVisible(!isFreightTrain && !istraindyanmics);
			coachnofield.setVisible(!isFreightTrain && !istraindyanmics);

			startLoopLabel.setVisible(isFreightTrain);
			startLoopField.setVisible(isFreightTrain);

			endLoopLabel.setVisible(isFreightTrain);
			endLoopField.setVisible(isFreightTrain);

			startTimeLabel.setVisible(isFreightTrain);
			startTimeField.setVisible(isFreightTrain);
			
			freighthaltLabel.setVisible(isFreightTrain);
			freighthaltCombo.setVisible(isFreightTrain);


			setTimeTableEntryPanelVisible(isFreightTrain);

			if (passengerTrainRadioButton.isSelected()) {
				// trainInputDialog.setBounds(x, y, 1000, 600);
				priorityLabel.setText("Priority (1 to 5)");
			} else {
				priorityLabel.setText("Priority (6 to 10)");
			}
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
			
			//freighthaltLabel.setVisible(!istraindyanmics);
			//freighthaltCombo.setVisible(!istraindyanmics);

			maximumSpeedLabel.setVisible(istraindyanmics);
			maximumSpeedField.setVisible(istraindyanmics);
			//santhosh
			if(freightTrainRadioButton.isSelected() && locoloadRadioButton.isSelected()){
				lengthLabel.setVisible(!istraindyanmics);
				lengthField.setVisible(!istraindyanmics);
				coachno.setVisible(istraindyanmics);
				coachnofield.setVisible(istraindyanmics);
			}else if(freightTrainRadioButton.isSelected() && traindynamicsRadioButton.isSelected()){
				lengthLabel.setVisible(istraindyanmics);
				lengthField.setVisible(istraindyanmics);
				coachno.setVisible(!istraindyanmics);
				coachnofield.setVisible(!istraindyanmics);
			}else if(passengerTrainRadioButton.isSelected() && traindynamicsRadioButton.isSelected()){
				lengthLabel.setVisible(istraindyanmics);
				lengthField.setVisible(istraindyanmics);
				coachno.setVisible(!istraindyanmics);
				coachnofield.setVisible(!istraindyanmics);
			}else if(passengerTrainRadioButton.isSelected() && locoloadRadioButton.isSelected()){
				lengthLabel.setVisible(istraindyanmics);
				lengthField.setVisible(istraindyanmics);
				coachno.setVisible(!istraindyanmics);
				coachnofield.setVisible(!istraindyanmics);
			}

		}

	};

	ActionListener DoneActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			dispose();
		}
	};

	ActionListener updateActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (trainNumberField.getText().isEmpty()) {
				resultLabel.setText("Train number cannot be empty");
				return;
			}

			try {
				Train train = trainToBeEdited;
				if (train == null) {
					resultLabel.setText("Choose the train to be updated again");
					return;
				}

				train.refTables.clear();
				
				//If there is error in user input regarding Freight halts
				if(setTrainProperties(train)==0)
					return;

				resultLabel.setText("Train " + train.trainNo + " updated");
			} catch (Exception ex) {
				resultLabel.setText("Check all inputs");
			}
		}
	};

	ActionListener addActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (trainNumberField.getText().isEmpty()) {
				resultLabel.setText("Train number cannot be empty");
				return;
			}

			SectionInputDialog sectioninputdialog = GlobalVar
					.getSectionInputDialog();
			Train train;
			if (passengerTrainRadioButton.isSelected()) {
				train = new ScheduledTrain();
			} else
				train = new UnscheduledTrain();

			try {
				int trainNumber = Integer.parseInt(trainNumberField.getText());
				Train findTrain = sectioninputdialog
						.getTrainByTrainNumber(trainNumber);
				if (findTrain != null) {
					resultLabel.setText("Train " + trainNumber
							+ " already exists");
					return;
				}
				//If there is error in user input regarding Freight halts
				if(setTrainProperties(train)==0)
					return;

				sectioninputdialog.addTrain(train);
				resultLabel.setText("Train " + train.trainNo + " added");
			} catch (Exception ex) {
				resultLabel.setText("Check all inputs");
			}
		}
	};

	private void addReferenceTimeTableEntries(Train train) {
		for (int i = 0; i < loopFieldList.size(); i++) {
			String tempLoopNo=loopFieldList.get(i).getItemAt(loopFieldList.get(i).getSelectedIndex()).toString();
			String[] tempLoopDetails=tempLoopNo.split("_");
			/*if(tempLoopNo.charAt(5)=='_')
			loopNo = Integer.parseInt(tempLoopNo.substring(0,5));
			else
				loopNo=Integer.parseInt(tempLoopNo.substring(0,6));*/
			int loopNo=Integer.parseInt(tempLoopDetails[0]);
			String loopDir=tempLoopDetails[1];
			String loopType=tempLoopDetails[2];
			String tempArrivalField=arrivalTimeFieldList.get(i).getText();
			String tempDepartureField=departureTimeFieldList.get(i).getText();
			if(!tempArrivalField.equals("") && !tempDepartureField.equals("")){
			int arrivalTimeInput = Integer.parseInt(tempArrivalField+"00");
			int departureTimeInput = Integer.parseInt(tempDepartureField+"00");
			//santhosh
			String stationName=stationFieldList.get(i).getText();
			ReferenceTableEntry referenceTableEntry = new ReferenceTableEntry(
					loopNo, arrivalTimeInput, departureTimeInput,stationName,loopType,loopDir);
			train.refTables.add(referenceTableEntry);
			}
		}
	}

	protected int setTrainProperties(Train train) throws Exception {
		int trainNumber = Integer.parseInt(trainNumberField.getText());

		boolean isScheduled = passengerTrainRadioButton.isSelected();

		int direction = GlobalVar.UP_DIRECTION;
		if (downRadioButton.isSelected())
			direction = GlobalVar.DOWN_DIRECTION;
		double length = .4;
		double acceleration = .7;
		double deceleration = .7;
		double maximumSpeed = 100;
		if (isScheduled && !istraindyanmics) {

			length = .015 * Double.parseDouble(coachnofield.getText());
			if (X == "WDM 2") {
				acceleration = 1 - .02 * Double.parseDouble(coachnofield
						.getText());
				deceleration = 1 - .02 * Double.parseDouble(coachnofield
						.getText());
				maximumSpeed = 120 - Double.parseDouble(coachnofield.getText());

			} else if (X == "WDG 4") {
				acceleration = .9 - .02 * Double.parseDouble(coachnofield
						.getText());
				deceleration = .9 - .02 * Double.parseDouble(coachnofield
						.getText());
				maximumSpeed = 110 - Double.parseDouble(coachnofield.getText());

			} else if (X == "WAP 7") {
				acceleration = .8 - .02 * Double.parseDouble(coachnofield
						.getText());
				deceleration = .8 - .02 * Double.parseDouble(coachnofield
						.getText());
				maximumSpeed = 125 - Double.parseDouble(coachnofield.getText());

			} else if (X == "WAG 7") {
				acceleration = .95 - .02 * Double.parseDouble(coachnofield
						.getText());
				deceleration = .95 - .02 * Double.parseDouble(coachnofield
						.getText());
				maximumSpeed = 115 - Double.parseDouble(coachnofield.getText());

			} else if (X == "WAG 9") {
				acceleration = 1.05 - .02 * Double.parseDouble(coachnofield
						.getText());
				deceleration = 1.05 - .02 * Double.parseDouble(coachnofield
						.getText());
				maximumSpeed = 100 - Double.parseDouble(coachnofield.getText());

			}

		} else if (!isScheduled && !istraindyanmics) {
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
			acceleration = Double.parseDouble(accelerationField.getText());
			deceleration = Double.parseDouble(decelerationField.getText());
			maximumSpeed = Double.parseDouble(maximumSpeedField.getText());

		}

		double priority = Double.parseDouble(priorityField.getText());
		//System.out.println("TrainInput.setTrainProperties  trainNumber: "+ trainNumber+
						//" Priority: "+ priority);
		train.trainNo = trainNumber;
		train.scheduled = isScheduled;
		train.direction = direction;
		train.length = length;
		train.accParam = acceleration;
		train.deceParam = deceleration;
		train.priority = priority;
		train.maximumPossibleSpeed = maximumSpeed;

		if (isScheduled) {

			if (operationDaysField.getText().isEmpty()) {
				throw new Exception();
			}
			//edited by santhosh 06/11/2015
			// int haltnumber = Integer.parseInt(haltnumberField.getText());
			int haltnumber=0;
			for (int m=0;m<arrivalTimeFieldList.size();m++){
				String a=arrivalTimeFieldList.get(m).getText();
				if(!arrivalTimeFieldList.get(m).getText().equals("")){
					haltnumber=haltnumber+1;
					
				}
			}
			ScheduledTrain sTrain = (ScheduledTrain) train;
			sTrain.operatingDays = operationDaysField.getText();
			sTrain.numberofhalts = haltnumber;
			addReferenceTimeTableEntries(train);

		} else {
			int numberOfHalts = Integer.parseInt(String.valueOf(freighthaltCombo.getSelectedItem()));
			int startLoop = Integer.parseInt(startLoopField.getText());
			int endLoop = Integer.parseInt(endLoopField.getText());
			int startTimeInput = Integer.parseInt(startTimeField.getText());
			int endStationCode = 0;
			SectionInputDialog sectioninputdialog = GlobalVar
					.getSectionInputDialog();
			ArrayList<Station> stationList = sectioninputdialog.stationList;
			ArrayList<Loop> loopList = sectioninputdialog.loopList;
			
			/**
			//R.Vidyadhar
			//Endstation code from Endloop Number
			for(int l =0 ; l<loopList.size(); l++){
				if (loopList.get(l).getBlockNo() == endLoop){
					 System.out.println("Loop= "+loopList.get(l).getBlockNo()
							 +" endStationCode = "+ loopList.get(l)
										.getStation().getStationCode());
					 endStationCode = Integer.parseInt(loopList.get(l)
													.getStation().getStationCode());
				}
			}
			
			//R.Vidyadhar
			//Checking whether each halting station is before Endloop Station
			//Else 0 will be returned
			for (int n=0 ; n<numberOfHalts ; n++){
				String station = String.valueOf(stationComboList.get(n).getSelectedItem());
				for(int k =0 ; k<stationList.size(); k++){
					if(stationList.get(k).getStationName() == station){
						int stationCode = Integer.parseInt(stationList.get(k).getStationCode());						
						if (stationCode > endStationCode){
							errorLabel.setText("Station  " + station + 
											" is located ahead of EndLoop No.");
							return;
						}
					}
				}					
			}**/
			
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
						return 0; 
					}					
				}
				int haltingMinutes = Integer.parseInt(haltTimeFieldList.get(m).getText());
				//System.out.println("Station " + station + "haltingMinutes = "+ haltingMinutes);
				ReferenceTableEntry freightRefEntry = new ReferenceTableEntry(station,	haltingMinutes);
				train.getRefTables().add(freightRefEntry);
			}
			train.setNumberofHalts(numberOfHalts);
			train.startLoopNo = startLoop;
			train.endLoopNo = endLoop;
			UnscheduledTrain uTrain = (UnscheduledTrain) train;
			uTrain.startTimeInput = startTimeInput;
		}
		return 1;
	}

	protected void setFieldsFromTrainAttributes(Train train) {
		trainNumberField.setText(String.valueOf(train.trainNo));
		if (train.scheduled)
			passengerTrainRadioButton.setSelected(true);
		else
			freightTrainRadioButton.setSelected(true);
		lengthField.setText(String.valueOf(train.length*1000));

		decelerationField.setText(String.valueOf(train.deceParam));
		priorityField.setText(String.valueOf(train.priority));
		maximumSpeedField.setText(String.valueOf(train.priority));

		if (train.scheduled) {

			locolist.setSelectedIndex(((int) (train.accParam)));
			coachnofield.setText(String.valueOf(Math.round(train.length / .015)));

			ScheduledTrain sTrain = (ScheduledTrain) train;
			operationDaysField.setText(sTrain.operatingDays);
			// haltnumberField.setText (String.valueOf(sTrain.numberofhalts));
			setReferenceTableFields(train);
		} else {
			startLoopField.setText(String.valueOf(train.startLoopNo));
			endLoopField.setText(String.valueOf(train.endLoopNo));
			UnscheduledTrain uTrain = (UnscheduledTrain) train;
			startTimeField.setText(ChangeTimeTable
					.getTimeInHHMMformat(uTrain.startTimeInput));
			freighthaltCombo.setSelectedItem(train.getNumberofHalts());
			freighthaltGenerator(train.getNumberofHalts());
			for(int i= 0; i<train.getNumberofHalts(); i++){
				String station = train.getRefTables().get(i).getStationName();
				String haltTime = String.valueOf(train.getRefTables().get(i).getMinHaltTime());
				stationComboList.get(i).setSelectedItem(station);
				haltTimeFieldList.get(i).setText(haltTime);	
			}
			
		}

	}

	private void setReferenceTableFields(Train train) {
		for (int i = 0; i < train.refTables.size(); i++) {
			ReferenceTableEntry referenceTableEntry = train.refTables.get(i);
			int loopNo = referenceTableEntry.refLoopNo;
			String refArrivalString = ChangeTimeTable
					.getTimeInHHMMSSformat(referenceTableEntry.refArrTimeInput);
			String refDepartureString = ChangeTimeTable
					.getTimeInHHMMSSformat(referenceTableEntry.refDepTimeInput);
			String stationName=referenceTableEntry.getStationName();
			String loopDir=referenceTableEntry.loopDir;
			String loopType=referenceTableEntry.loopType;
			for (int k=0;k<stationFieldList.size();k++){
				String a=stationFieldList.get(k).getText();
				if(a.equals(stationName)){
					loopFieldList.get(k).setSelectedItem((String.valueOf(loopNo))+"_"+loopDir+"_"+loopType);
					arrivalTimeFieldList.get(k).setText(refArrivalString.substring(0,4));
					departureTimeFieldList.get(k).setText(refDepartureString.substring(0,4));
					}else{
						//loopFieldList.get(k).setSelectedItem(0);
						arrivalTimeFieldList.get(k).setText("");
						departureTimeFieldList.get(k).setText("");
					}
			}
			
		}
	}

	ActionListener viewAllScheduledTrainsActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			AllScheduledTrainsData allScheduledTrainsData = new AllScheduledTrainsData();
		}
	};

	ActionListener viewAllFreightTrainsActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			AllFreightTrainsData allFreightTrainsData = new AllFreightTrainsData();
		}
	};

	public static void main(String[] args) {
		TrainInputDialog trainInputDialog = GlobalVar.getTrainInputDialog();
	}

	protected void setTimeTableEntryPanelVisible() {
		setTimeTableEntryPanelVisible(freightTrainRadioButton.isSelected());
	}

	ActionListener viewLoopActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			AllLoopData1 allLoopData = new AllLoopData1();

		}
	};

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
						"Type","Type of Trains allowed ", "Maximum Speed" };

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

	protected void setTimeTableEntryPanelVisible(boolean isFreightTrain) {
		scheduledTrainTimeTableLabel.setVisible(!isFreightTrain);
		stationLabel.setVisible(!isFreightTrain);
		loopLabel.setVisible(!isFreightTrain);
		arrivalTimeLabel.setVisible(!isFreightTrain);
		departureTimeLabel.setVisible(!isFreightTrain);
		noHaltLabel.setVisible(!isFreightTrain);
		for (int i = 0; i < stationList.size(); i++) {
			stationFieldList.get(i).setVisible(!isFreightTrain);
			loopFieldList.get(i).setVisible(!isFreightTrain);
			loopFieldList.get(i).setEnabled(!isFreightTrain);
			arrivalTimeFieldList.get(i).setVisible(!isFreightTrain);
			departureTimeFieldList.get(i).setVisible(!isFreightTrain);
			//checkHaltFieldList.get(i).setVisible(!isFreightTrain);
		}

		this.repaint();
	}

	public void write(ArrayList<Train> trainList, boolean isScheduled)
			throws IOException {
		String trainFileName;
		String formatString;
		if (!isScheduled) {
			trainFileName = FileNames.getUnscheduledTrainsFileName();
			formatString = "/*Train No.  direction  startTime   length   acceleration    deceleration   priority   maximumSpeed   startLoop   endLoop  Number Of Halts    Station   Minutes of Halt   Station   Minutes of Halt */\n\n";
		} else {
			trainFileName = FileNames.getScheduledTrainsFileName();
			formatString = " /*Train No	 Direction 	Length	Acceleration	Decelaration	Priority 	Speed 	Op.Days	Number Of Halts	Loop No.	Arr	Dep	Loop No. 	Arr	Dep	Loop No. 	Arr	Dep	Loop No. 	Arr	Dep	Loop No. 	Arr	Dep	Loop No. 	Arr	Dep	Loop No. 	Arr	Dep	Loop No. 	Arr	Dep	Loop No. 	Arr	Dep */\n\n";
		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(trainFileName));
		bw.write(formatString);

		bw.write("");
		for (Train train : trainList) {
			if (isScheduled)
				writeScheduledTrain(bw, train);
			else
				writeUnscheduledTrain(bw, train);

		}

		bw.close();

	}

	private void writeUnscheduledTrain(BufferedWriter bw, Train train)
			throws IOException {
		UnscheduledTrain uTrain = (UnscheduledTrain) train;

		bw.write(String.valueOf(train.trainNo));
		bw.write(" ");

		bw.write(GlobalVar.getDirectionStringFromDirection(train.direction));
		bw.write(" ");

		bw.write(ChangeTimeTable.getTimeInHHMMformat(uTrain.startTimeInput));
		bw.write(" ");

		bw.write(String.valueOf(train.length));
		bw.write(" ");

		bw.write(String.valueOf(train.accParam));
		bw.write(" ");

		bw.write(String.valueOf(train.deceParam));
		bw.write(" ");

		bw.write(String.valueOf(train.priority));
		bw.write(" ");

		bw.write(String.valueOf(train.maximumPossibleSpeed));
		bw.write(" ");

		bw.write(String.valueOf(train.startLoopNo));
		bw.write(" ");

		bw.write(String.valueOf(train.endLoopNo));
		bw.write(" ");
		
		bw.write(String.valueOf(train.getNumberofHalts()));
		bw.write(" ");
		
		for(int i =0; i<train.getNumberofHalts(); i++ ){
			bw.write(train.getRefTables().get(i).getStationName());
			bw.write(" ");
			bw.write(String.valueOf(train.getRefTables().get(i).getMinHaltTime()));
			bw.write(" ");
		}
		bw.write("\n");
		
	}

	private void writeScheduledTrain(BufferedWriter bw, Train train)
			throws IOException {
		ScheduledTrain sTrain = (ScheduledTrain) train;
		bw.write(String.valueOf(train.trainNo));
		bw.write(" ");

		bw.write(GlobalVar.getDirectionStringFromDirection(train.direction));
		bw.write(" ");

		bw.write(String.valueOf(train.length));
		bw.write(" ");

		bw.write(String.valueOf(train.accParam));
		bw.write(" ");

		bw.write(String.valueOf(train.deceParam));
		bw.write(" ");

		bw.write(String.valueOf(train.priority));
		bw.write(" ");

		bw.write(String.valueOf(train.maximumPossibleSpeed));
		bw.write(" ");

		bw.write("\"" + sTrain.operatingDays + "\"");
		bw.write(" ");

		bw.write(String.valueOf(sTrain.numberofhalts));
		bw.write(" ");
		//code added by santhosh on Feb 02,2016 to resolve the down train labelling issue
		String direction=GlobalVar.getDirectionStringFromDirection(train.direction);
		if(direction.equals("up")){
			for (ReferenceTableEntry referenceTableEntry : train.refTables) {
				bw.write(String.valueOf(referenceTableEntry.refLoopNo));
				bw.write(" ");
	
				bw.write(ChangeTimeTable
						.getTimeInHHMMSSformat(referenceTableEntry.refArrTimeInput));
				bw.write(" ");
	
				bw.write(ChangeTimeTable
						.getTimeInHHMMSSformat(referenceTableEntry.refDepTimeInput));
				bw.write(" ");
			}
		}else if(direction.equals("down")){
			for(int k=train.refTables.size()-1;k>=0;k--){
				ReferenceTableEntry referenceTableEntry=train.refTables.get(k);
				bw.write(String.valueOf(referenceTableEntry.refLoopNo));
				bw.write(" ");
	
				bw.write(ChangeTimeTable
						.getTimeInHHMMSSformat(referenceTableEntry.refArrTimeInput));
				bw.write(" ");
	
				bw.write(ChangeTimeTable
						.getTimeInHHMMSSformat(referenceTableEntry.refDepTimeInput));
				bw.write(" ");
			}
		}

		bw.write("\n");
	}

	public void readScheduledTrains(ArrayList<Train> trainList, int nEntries)
			throws IOException {
		String trainFileName = FileNames.getScheduledTrainsFileName();
		StreamTokenizer streamTokenizer = new StreamTokenizer(new FileReader(
				trainFileName));
		streamTokenizer.slashSlashComments(true);
		streamTokenizer.slashStarComments(true);

		while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			Train train = new ScheduledTrain();
			train.scheduled = true;

			train.trainNo = (int) streamTokenizer.nval;

			streamTokenizer.nextToken();
			train.direction = GlobalVar
					.getDirectionFromDirectionString(streamTokenizer.sval);

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
			sTrain.operatingDays = streamTokenizer.sval;

			streamTokenizer.nextToken();
			sTrain.numberofhalts = (int) streamTokenizer.nval;

			sTrain.readReferenceTableEntries(streamTokenizer, nEntries);

			trainList.add(train);

		}
	}

	public void readUnscheduledTrains(ArrayList<Train> trainList)
			throws IOException {
		String trainFileName = FileNames.getUnscheduledTrainsFileName();
		StreamTokenizer streamTokenizer = new StreamTokenizer(new FileReader(
				trainFileName));
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

			System.out.println("endloop " + train.endLoopNo);
			
			streamTokenizer.nextToken();
			train.numberofhalts = (int) streamTokenizer.nval;

			train.readReferenceTableEntries(streamTokenizer, train.numberofhalts);
			
			trainList.add(train);
		}
	}
	
	
}

class AllScheduledTrainsData extends JFrame implements TableModelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable scheduledTrainDataTable;

	public AllScheduledTrainsData() {
		super("List of Scheduled Trains");

		scheduledTrainDataTable = new JTable(new AbstractTableModel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			SectionInputDialog sectioninputdialog = GlobalVar
					.getSectionInputDialog();
			ArrayList<Train> trainList = sectioninputdialog.getTrainList(true);
			ArrayList<Station> stationList = sectioninputdialog.stationList;

			String[] columnNames = { "Train No", "Direction", "Length",
					"Acceleration", "Deceleration", "Priority",
					"Maximum Speed", "OperationDays", "Number of Halts" };

			public String getColumnName(int col) {
				if (col < columnNames.length)
					return columnNames[col];
				else {
					int i = (col - columnNames.length) % 3;
					if (i == 0)
						return "Loop";
					else if (i == 1)
						return "Arrival";
					else
						return "Departure";
				}
			}

			public int getRowCount() {
				return trainList.size();
			}

			public int getColumnCount() {
				return columnNames.length + 3 * stationList.size();
			}

			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return trainList.get(row).trainNo;
				case 1:
					int direction = trainList.get(row).direction;
					String directionString = GlobalVar
							.getDirectionStringFromDirection(direction);
					return directionString;
				case 2:
					return trainList.get(row).length;
				case 3:
					return trainList.get(row).accParam;
				case 4:
					return trainList.get(row).deceParam;
				case 5:
					return trainList.get(row).priority;
				case 6:
					return trainList.get(row).maximumPossibleSpeed;
				case 7:
					return trainList.get(row).operatingDays;
				case 8:
					return trainList.get(row).numberofhalts;

				}

				int numberOfColumn = col - columnNames.length;
				int nReferenceTableEntry = numberOfColumn / 3;
				int nAttribute = numberOfColumn % 3;
				ArrayList<ReferenceTableEntry> referenceTableEntries = trainList
						.get(row).refTables;
				if (nReferenceTableEntry < referenceTableEntries.size()) {
					ReferenceTableEntry referenceTableEntry = referenceTableEntries
							.get(nReferenceTableEntry);
					if (nAttribute == 0)
						return referenceTableEntry.refLoopNo;
					else if (nAttribute == 1)
						return ChangeTimeTable
								.getTimeInHHMMSSformat(referenceTableEntry.refArrTimeInput);
					else
						return ChangeTimeTable
								.getTimeInHHMMSSformat(referenceTableEntry.refDepTimeInput);
				}

				return null;
			}

		});

		JScrollPane scrollPane = new JScrollPane(scheduledTrainDataTable);
		scheduledTrainDataTable
				.setPreferredScrollableViewportSize(new Dimension(500, 70));
		getContentPane().add(scrollPane);
		setBounds(70, 70, 900, 400);
		setVisible(true);
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
	}

}

class AllFreightTrainsData extends JFrame implements TableModelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable freightTrainDataTable;

	public AllFreightTrainsData() {
		super("List of Freight Trains");

		freightTrainDataTable = new JTable(new AbstractTableModel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			SectionInputDialog sectioninputdialog = GlobalVar
					.getSectionInputDialog();
			ArrayList<Train> trainList = sectioninputdialog.getTrainList(false);
			ArrayList<Station> stationList = sectioninputdialog.stationList;

			String[] columnNames = { "Train No", "Direction", "Length",
					"Acceleration", "Deceleration", "Priority",
					"Maximum Speed", "Start Loop", "End Loop", "Start Time" , "No. of Halts"};
			
			public String getColumnName(int col) {
				if (col < columnNames.length)
					return columnNames[col];
				else {
					int i = (col - columnNames.length) % 2;
					if (i == 0)
						return "Station";
					else
						return "Halting Time in Minutes";
				}
			}
			
			/**public int getColumnCount() {
				if (Integer.parseInt(columnNames[11]) == 0)
					return columnNames.length;
				else
					
			}**/

			public int getRowCount() {
				return trainList.size();
			}

			public int getColumnCount() {
				return columnNames.length+ 2 * stationList.size();
			}

			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return trainList.get(row).trainNo;
				case 1:
					int direction = trainList.get(row).direction;
					String directionString = GlobalVar
							.getDirectionStringFromDirection(direction);
					return directionString;
				case 2:
					return trainList.get(row).length;
				case 3:
					return trainList.get(row).accParam;
				case 4:
					return trainList.get(row).deceParam;
				case 5:
					return trainList.get(row).priority;
				case 6:
					return trainList.get(row).maximumPossibleSpeed;
				case 7:
					return trainList.get(row).startLoopNo;
				case 8:
					return trainList.get(row).endLoopNo;
				case 9:
					return trainList.get(row).startTimeInput;
				case 10:
					return trainList.get(row).getNumberofHalts();
				}
				
				int numberOfColumn = col - columnNames.length;
				int nReferenceTableEntry = numberOfColumn / 2;
				int nAttribute = numberOfColumn % 2;
				ArrayList<ReferenceTableEntry> referenceTableEntries = trainList
						.get(row).refTables;
				if (nReferenceTableEntry < referenceTableEntries.size()) {
					ReferenceTableEntry referenceTableEntry = referenceTableEntries
							.get(nReferenceTableEntry);
					if (nAttribute == 0)
						return referenceTableEntry.getStationName();
					else if (nAttribute == 1)
						return referenceTableEntry.getMinHaltTime();
				}
				return null;
			}

		});

		JScrollPane scrollPane = new JScrollPane(freightTrainDataTable);
		freightTrainDataTable.setPreferredScrollableViewportSize(new Dimension(
				500, 70));
		getContentPane().add(scrollPane);
		setBounds(70, 70, 900, 400);
		setVisible(true);
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
	}
	
}

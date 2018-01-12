package input;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntities.trackEntities.Link;
import gui.entities.sectionEntityList.StationList;
import gui.entities.trainEntities.Train;
import input.LoopInputDialog.AllLoopData;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.GlowBuilder;

import java.awt.Color;
import java.awt.Dialog;
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

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class StationInputDialog extends InputDialog {

	private static final long serialVersionUID = 1L;
	
	public JLabel stationNameLabel = new JLabel("Station Name");
	public JLabel startKilometreLabel = new JLabel("Start Km.");
	public JLabel endKilometreLabel = new JLabel("End Km.");
	public JLabel stationCodeEditDeleteLabel = new JLabel("Select Station");
	
	public JLabel templates = new JLabel();
	public JLabel resultLabel = new JLabel();
	public JRadioButton mainLineCommonRadioButton = new JRadioButton("Common Mainline");
	public JRadioButton mainLineUpDownRadioButton = new JRadioButton("Up / Down Mainline");
	public JLabel loopTypeLabel = new JLabel("Type of Line");
	public JLabel looptraintypelabel = new JLabel("Type of Trains allowed");
	public JLabel looplengthlabel = new JLabel("Loop Length");
	public JLabel maximumspeedLabel = new JLabel("Maximum Loop Entry Velocity");
	public JTextField maximumspeedField = new JTextField();


	//public ArrayList<Loop> loopList = new ArrayList<Loop>();
	
	
	public JTextField stationNameField = new JTextField();
	public JTextField startKilometreField = new JTextField();
	public JTextField endKilometreField = new JTextField();


	public JButton addButton = new JButton("Add station");
	public JButton deleteButton = new JButton("Delete station");
	public JButton DoneButton = new JButton("Done");
	public JButton viewLoopButton = new JButton("View Loop Details");
	public JButton viewAllButton = new JButton("View Station Details");


	public JComboBox<String> stationNameComboBox = null;
	public JButton addLoopButton = new JButton("Add Loopline Loop");
	public JButton deleteLoopButton = new JButton("Delete Loopline loop");
	public JButton updateButton = new JButton("Update");
	public ButtonGroup loopTypeButtonGroup = new ButtonGroup();
	

	public Station stationToBeUpdated = null;
	boolean mainLineUpDownLoop=false;
	boolean mainLineCommonLoop=false;

	private static String [] filename = {"Standard -686m ","Other"};
	private static String [] looptraintypestring = {"All", "Scheduled", "Freight"};
	public JComboBox looptraintype = new JComboBox(looptraintypestring);
	public JComboBox looplength = new JComboBox(filename);
	String[] stationNames = null;
	private JComboBox<String> stationtype;
	//private static String[] filename = { "General" };
	public String stationName;
	private Icon[] pics = {
			new ImageIcon(
					"/home/shashank/Desktop/RA/ozil/Simulator/TrainSchedulingSimulator/src/football_PNG1087.png"),
			new ImageIcon(
					"/home/shashank/Desktop/RA/ozil/Simulator/TrainSchedulingSimulator/src/Baseball_(crop).png") };

	public int z;

	public StationInputDialog() {
		// super();
		x = 40;
		y = 40;
		this.setBounds(x, y, 600, 700);
		SectionInputDialog sectioninputdialog = GlobalVar
				.getSectionInputDialog();
		
        		
		stationNames = sectioninputdialog.getStationNames();
		stationNameComboBox = new JComboBox<String>(stationNames);
		

		//stationtype = new JComboBox<String>(filename);
		templates = new JLabel(pics[0]);
		setComponentActionListeners();
		setComponentBounds();
		addComponents();

		this.add(jpanel);

		this.setTitle("Station Input");
		resultLabel.setText("");
	}

	@Override
	public void addComponents() {

		
		this.getContentPane().add(startKilometreLabel);
		this.getContentPane().add(endKilometreLabel);
		this.getContentPane().add(stationNameLabel);

		this.getContentPane().add(startKilometreField);
		this.getContentPane().add(endKilometreField);
		this.getContentPane().add(stationNameField);

		this.getContentPane().add(addButton);
		
		this.add(loopTypeLabel);

		loopTypeButtonGroup.add(mainLineCommonRadioButton);
		loopTypeButtonGroup.add(mainLineUpDownRadioButton);
		this.getContentPane().add(mainLineCommonRadioButton);
		this.getContentPane().add(mainLineUpDownRadioButton);
		mainLineUpDownRadioButton.setSelected(true);

		

		
		this.getContentPane().add(looptraintype);
		this.getContentPane().add(looptraintypelabel);
		this.getContentPane().add(looplengthlabel);
		this.getContentPane().add(maximumspeedLabel);
        this.getContentPane().add(looplength);
        this.getContentPane().add(maximumspeedField);
        
        

		this.getContentPane().add(stationCodeEditDeleteLabel);
		this.getContentPane().add(deleteButton);

		this.getContentPane().add(stationNameComboBox);
		this.getContentPane().add(addButton);
		this.getContentPane().add(deleteLoopButton);
		this.getContentPane().add(addLoopButton);
		
		
		

		this.getContentPane().add(viewAllButton);
		this.getContentPane().add(resultLabel);
		this.getContentPane().add(templates);
		this.getContentPane().add(viewLoopButton);

		this.getContentPane().add(DoneButton);
		


		
	}

	@Override
	public void setComponentBounds() {
		labelWidth = 210;
		x2 = x1 + labelWidth + 20;

		stationNameLabel.setBounds(x1, y1, labelWidth, labelHeight);
		stationNameField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += heightDifference;
		startKilometreLabel.setBounds(x1, y1, labelWidth, labelHeight);
		startKilometreField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += heightDifference;
		endKilometreLabel.setBounds(x1, y1, labelWidth, labelHeight);
		endKilometreField.setBounds(x2, y1, fieldWidth, fieldHeight);
		
		
		y1 += heightDifference;
		loopTypeLabel.setBounds(x1, y1, labelWidth, labelHeight);
		mainLineCommonRadioButton.setBounds(x2, y1, fieldWidth+50, fieldHeight);
		mainLineUpDownRadioButton.setBounds(x2+fieldWidth+60, y1, fieldWidth+100, fieldHeight);
		
		y1 += heightDifference;
		looptraintypelabel.setBounds(x1, y1, 2*fieldWidth, fieldHeight);
		looptraintype.setBounds(x2, y1, fieldWidth, fieldHeight);

		
		y1 += heightDifference;
		looplengthlabel.setBounds(x1, y1, fieldWidth, fieldHeight);
		looplength.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += heightDifference;
		maximumspeedLabel.setBounds(x1, y1, labelWidth, labelHeight);
		maximumspeedField.setBounds(x2, y1, fieldWidth, fieldHeight);
		

		y1 += 2 * heightDifference;
		addButton.setBounds(x1, y1, buttonWidth, buttonHeight);
		viewAllButton.setBounds(x2, y1, buttonWidth, buttonHeight);

		y1 += heightDifference;
		viewLoopButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += 2 * heightDifference;
		

		y1 += heightDifference;
		stationCodeEditDeleteLabel.setBounds(x1, y1, labelWidth, labelHeight);
		stationNameComboBox.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += heightDifference;
		addLoopButton.setBounds(x1, y1, 5 * buttonWidth / 5, buttonHeight);
		deleteLoopButton.setBounds(x2 - 2 * x1, y1, 5 * buttonWidth / 5,buttonHeight);
		deleteButton.setBounds(2 * x2 - 4 * x1, y1, 4 * buttonWidth / 5,buttonHeight);

		y1 += heightDifference;
		DoneButton.setBounds(2 * x2- 4 * x1, y1, 4*buttonWidth/5, buttonHeight);

		y1 += heightDifference/2;
		resultLabel.setBounds(x1, y1, 500, labelHeight);
		
	}

	@Override
	public void setComponentActionListeners() {
		addButton.addActionListener(addActionListener);
		deleteButton.addActionListener(deleteActionListener);
		
		addLoopButton.addActionListener(addloopbuttonButtonActionListener);
		deleteLoopButton.addActionListener(deleteloopbuttonButtonActionListener);
		viewAllButton.addActionListener(viewAllActionListener);
		mainLineCommonRadioButton.addItemListener(LooptypeItemListener);
		mainLineUpDownRadioButton.addItemListener(LooptypeItemListener);
		viewLoopButton.addActionListener(viewLoopActionListener);
		DoneButton.addActionListener(DoneActionListener);
	}

	
	ItemListener LooptypeItemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			mainLineUpDownLoop = mainLineUpDownRadioButton.isSelected();
			mainLineCommonLoop=mainLineCommonRadioButton.isSelected();
					maximumspeedField.setText("100");
					

			
					}
	};
	
	

	
	ActionListener DoneActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {

			dispose();
		}
	};

	ActionListener viewLoopActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			AllLoopData allLoopData = new AllLoopData();

		}
	};


	ActionListener viewAllActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			AllStationData allStationData = new AllStationData();
		}
	};

	ActionListener editActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// jpanel.repaint();
		}
	};

	ActionListener addActionListener = new ActionListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultLabel.setText("");
			try {
				Station prevStation=null;
				Station nextStation=null;
				int prevIndex=-1;
				String stationName = stationNameField.getText();
				
				SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
				Station findStation = sectioninputdialog.getStationByStationName(stationName);
				
				if (findStation!= null){
					resultLabel.setText("Station with stationCode "	+ stationName + " already exists");
					return;
					}
						
				if (findStation == null) {
					double startMilePost = Double
							.parseDouble(startKilometreField.getText());
					double endMilePost = Double.parseDouble(endKilometreField
							.getText());
					//double speed = Double.parseDouble(stationEntrySpeedField.getText());
					double speed=100;
					
					if(startMilePost>=endMilePost)
					{
						resultLabel.setText("start kilometer should be less then end kilometer");
						return;
					}
					else
					{
//---------------------------------------------------------------------------------------------
						//Sneha : if one tries to add station between two station and there is already
						//added blocks found then prompt for delete block 

						ArrayList<Station> tempstationlist1=sectioninputdialog.stationList;
						if(tempstationlist1.size()!=0){
							for(int i=0; i<tempstationlist1.size();i++ ){
								Station tempstation =tempstationlist1.get(i);
								if(tempstation.endMilePost <= startMilePost)prevIndex++;
							}
							if(prevIndex==-1){
								if(tempstationlist1.get(0).startMilePost>=endMilePost){
									updateBlockIDBeforAdd(1);
									updateLoopIDOfTrainHaltsBeforeAdd(1);
								}
							}
						
							if(prevIndex!=-1){
								   if(prevIndex<(tempstationlist1.size()-1)){
									   Station tempstation =tempstationlist1.get(prevIndex);
									   prevStation=tempstation;
									   tempstation=tempstationlist1.get(prevIndex+1);
									   nextStation=tempstation;
								   	}
								   else{
									   Station tempstation =tempstationlist1.get(prevIndex);
									   prevStation=tempstation;
									   nextStation=null;
								   }
						     
								   if(prevStation!=null){
									   if(prevStation.endMilePost>=startMilePost){
										   resultLabel.setText("Check start km and end km");
										   return;
									   }

									   if(nextStation!=null){
										   if(nextStation.startMilePost<=endMilePost){
											   resultLabel.setText("Check start km and end km");
											   return;
										   }
									   }

								   	}
								   int prevstationId= prevIndex+1;
								   SectionInputDialog sectioninputdialog2 = GlobalVar.getSectionInputDialog();
								   ArrayList<Block> blockList = sectioninputdialog2.blockList;
								   for (Block tempBlock:blockList){
									   String blockId = String.format("%07d", tempBlock.blockNo);
									   int blockstationId = Integer.parseInt(blockId.substring(0, 3));
									   
									   if(blockstationId==prevstationId){
										   //resultLabel.setText("Delete blocks between station " + prevStation.stationName + " and " + nextStation.stationName);
										   String msg="Adding " + stationName + " will delete all blocks between staion " + prevStation.stationName + " and " + nextStation.stationName; 
										   int editResponse = JOptionPane.showConfirmDialog(null, msg, "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
											if(editResponse==JOptionPane.YES_NO_OPTION)	{
												deleteBlockBetweenTwoStation(prevstationId,prevstationId+1);
												updateBlockIDBeforAdd(prevstationId+1);

												break;
											}
											else return;
										   }
								   }
								   updateLoopIDOfTrainHaltsBeforeAdd(prevstationId+1);
								}
								
						   
							}
						
						
										
						
//--------------------------------------------------------------------------------
					Station station = new Station();
					station.stationName = stationName;
					station.startMilePost = startMilePost;
					station.endMilePost = endMilePost;
					station.stationVelocity = speed;

					sectioninputdialog.addStation(station);
					
					
					ArrayList<Loop> temploopList = sectioninputdialog.loopList;
					ArrayList<Station> tempstationlist=sectioninputdialog.stationList;
					ArrayList<Block> tempblocklist=sectioninputdialog.blockList;
					int count=0x0;
					for(Station tempstation: tempstationlist ){
					count++;
						for (Loop loop : temploopList) {
							if( loop.stationName.equals(tempstation.stationName)){
								if((loop.blockNo / 10000 )!=count){
									loop.blockNo = (loop.blockNo-((loop.blockNo / 10000)*10000))+(count * 10000);
									if(loop.upLinks.size()>0)
									{
										ArrayList<Link> tempLinkList = loop.upLinks;
										for (Link link:tempLinkList){
											link.nextBlockNo = (link.nextBlockNo-((link.nextBlockNo / 10000)*10000))+(count * 10000);
										}
									}
									if(loop.downLinks.size()>0){
										ArrayList<Link> tempLinkList = loop.downLinks;
										for (Link link:tempLinkList){
											link.nextBlockNo = (link.nextBlockNo-((link.nextBlockNo / 10000)*10000))+((count-1) * 10000);
											
										}
									
									}
								}
						}
					 
					 }
					}
					
					
					
					
					
					int i = 1;
					String stationId=null  ;
					for (Station tempstation : sectioninputdialog.stationList) {
						if( tempstation.stationName.equals(stationName))
							stationId = String.format("%03d",i);
							i++;
						}
				    String typeoftrain = (String) looptraintype.getSelectedItem();					

				    
					if(mainLineCommonLoop){
						Loop loop = new Loop();
						loop.stationName = stationNameField.getText();
						String loopblockidstring = (stationId+"20"+"01");
						loop.blockNo = Integer.parseInt(loopblockidstring);
					    loop.setLoopTrainType(typeoftrain);
					    loop.maximumPossibleSpeed = Double.parseDouble(maximumspeedField.getText()); // Double.parseDouble(maximumSpeedField
					    loop.direction = GlobalVar.COMMON_DIRECTION;
					    sectioninputdialog.addLoop(loop);
					    updateLinkage(loop);
					}
					
					
					if(mainLineUpDownLoop){
						Loop loopUp = new Loop();
						Loop loopDown= new Loop();
						loopUp.stationName = stationNameField.getText();
						loopDown.stationName = stationNameField.getText();
						String loopblockidstring = (stationId+"00"+"01");
						loopUp.blockNo = Integer.parseInt(loopblockidstring);
						loopblockidstring = (stationId+"10"+"01");
						loopDown.blockNo = Integer.parseInt(loopblockidstring);
					    loopUp.setLoopTrainType(typeoftrain);
					    loopDown.setLoopTrainType(typeoftrain);
					    loopUp.maximumPossibleSpeed = Double.parseDouble(maximumspeedField.getText()); // Double.parseDouble(maximumSpeedField
					    loopDown.maximumPossibleSpeed = Double.parseDouble(maximumspeedField.getText()); // Double.parseDouble(maximumSpeedField
					    loopUp.direction = GlobalVar.UP_DIRECTION;
					    loopDown.direction=GlobalVar.DOWN_DIRECTION;
					    sectioninputdialog.addLoop(loopUp);
					    updateLinkage(loopUp);
					    sectioninputdialog.addLoop(loopDown);
					    updateLinkage(loopDown);
					}
					
				   	
					resultLabel.setText("Station with stationCode "	+ station.stationName + " added");
			
					}
				}
				
				

			}

			catch (Exception ex) {
				resultLabel.setText(ex.getMessage() + " Check Input Fields");
			}
			// jpanel.repaint();
		}
	};

	ActionListener deleteActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultLabel.setText("");
			String stationName = (String) stationNameComboBox.getSelectedItem();
			if (stationNameComboBox.getSelectedIndex() == -1) {
				resultLabel.setText("Enter station code of station to be deleted");

			} else {
				SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
				ArrayList<Station> stationArrayList = sectioninputdialog.stationList;
				Station stationToDelete = null;
				for (Station station : stationArrayList) {
					if (station.stationName.equalsIgnoreCase(stationName)) {
						stationToDelete = station;
						break;
					}
				}

				if (stationToDelete == null) {
					resultLabel.setText("Station with station name "+ stationName + " not found");
				} else {
				//Sneha: Code added below to check if there is any block exists between prev station and selected station
			    //or block exists between selected station and next station
				//-----------------------------------------------------	
					ArrayList<Station> tempstationlist=sectioninputdialog.stationList;
					ArrayList<Block> blockList = sectioninputdialog.blockList;
					int prevstationId=-1,stationToDeleteStationId=-1;
					boolean prev=false;
					boolean next=false;
					if(stationNameComboBox.getSelectedIndex()>0)
					{
						prevstationId=(stationNameComboBox.getSelectedIndex());
						stationToDeleteStationId=stationNameComboBox.getSelectedIndex()+1;
						
						for (Block tempBlock:blockList){
							String blockId = String.format("%07d", tempBlock.blockNo);
							int blockstationId = Integer.parseInt(blockId.substring(0, 3));
							if(blockstationId==prevstationId){
								prev=true;
								break;
								
							}
						}

					}
					if((stationNameComboBox.getSelectedIndex()+1)!=stationNameComboBox.getItemCount()){
					
					stationToDeleteStationId=stationNameComboBox.getSelectedIndex()+1;
					
					for (Block tempBlock:blockList){
						String blockId = String.format("%07d", tempBlock.blockNo);
						int blockstationId = Integer.parseInt(blockId.substring(0, 3));
						
						if(blockstationId==stationToDeleteStationId){
							next=true;
							break;
							
						}
					}

					}
					
					//printLoopWithBlockLinkage();


					if(ifTrainHaltAtThisStation(stationToDeleteStationId)){
						String msg="Some of the Trains halts on selected station " + stationToDelete.stationName + "Are you sure you want to delete the station?";
						   int editResponse = JOptionPane.showConfirmDialog(null, msg, "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(editResponse==JOptionPane.NO_OPTION)	{
								return;
							}
						
					}
					
					
					
					if(prev && next){
						//System.out.println("prev and next both found");
						
						Station prevStation=tempstationlist.get(prevstationId-1);
						Station nextStation=tempstationlist.get(stationToDeleteStationId);
						
						resultLabel.setText("Delete blocks between stations " + prevStation.stationName + " , " + stationToDelete.stationName +" and "+ nextStation.stationName );
						   //resultLabel.setText("Delete blocks between station " + prevStation.stationName + " and " + nextStation.stationName);
						   String msg="Deleting " + stationName + " will delete all blocks between staion " + prevStation.stationName + " , " + stationToDelete.stationName +" and "+ nextStation.stationName; 
						   int editResponse = JOptionPane.showConfirmDialog(null, msg, "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(editResponse==JOptionPane.YES_NO_OPTION)	{
								deleteBlockBetweenTwoStation(stationToDeleteStationId-1,stationToDeleteStationId);
								deleteBlockBetweenTwoStation(stationToDeleteStationId,stationToDeleteStationId+1);
								updateBlockIDAfterDelete(stationToDeleteStationId);

								
								
							}
							else return;
					}

					
					else if (prev||next){
						if(prev){
							//System.out.println("prev found");
							Station prevStation=tempstationlist.get(prevstationId-1);
							resultLabel.setText("Delete blocks between station " + prevStation.stationName + " and " + stationToDelete.stationName);
							   //resultLabel.setText("Delete blocks between station " + prevStation.stationName + " and " + nextStation.stationName);
							   String msg="Deleting " + stationName + " will delete all blocks between staion " + prevStation.stationName + " and " + stationToDelete.stationName; 
							   int editResponse = JOptionPane.showConfirmDialog(null, msg, "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if(editResponse==JOptionPane.YES_NO_OPTION)	{
									deleteBlockBetweenTwoStation(stationToDeleteStationId-1,stationToDeleteStationId);
									updateBlockIDAfterDelete(stationToDeleteStationId);
									
								
								
								}
								else return;
								

						}
						if(next)
						{
							//System.out.println("next found");
							Station nextStation=tempstationlist.get(stationToDeleteStationId);
							resultLabel.setText("Delete blocks between station " +  stationToDelete.stationName +" and " +nextStation.stationName );
							   //resultLabel.setText("Delete blocks between station " + prevStation.stationName + " and " + nextStation.stationName);
							   String msg="Deleting " + stationName + " will delete all blocks between staion " + stationToDelete.stationName + " and " + nextStation.stationName; 
							   int editResponse = JOptionPane.showConfirmDialog(null, msg, "Confirm",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if(editResponse==JOptionPane.YES_NO_OPTION)	{
									deleteBlockBetweenTwoStation(stationToDeleteStationId,stationToDeleteStationId+1);
									updateBlockIDAfterDelete(stationToDeleteStationId);
									
									
								
								}
								else return;
								

						}
					
					}
					/*else{
						ArrayList<Loop> temploopList = sectioninputdialog.loopList;
						ArrayList<Station> tempList=sectioninputdialog.stationList;
						sectioninputdialog.removeStation(stationToDelete);
						resultLabel.setText("Station with station name "+ stationName + " deleted");
						BlockInput blockInput = GlobalVar.getBlockInput();
						blockInput.updateStationNames();
						int count=0x0;
						for(Station tempstation: tempList ){
						count++;
							for (Loop loop : temploopList) {
								if( loop.stationName.equals( tempstation.stationName)){
										if(loop.upLinks.size()>0){
											if(count==tempList.size())loop.upLinks.clear();
											else{
												ArrayList<Link> tempLinkList = loop.upLinks;
												for (Link link:tempLinkList)link.nextBlockNo = (link.nextBlockNo-((link.nextBlockNo / 10000)*10000))+(count * 10000);
											}
										}
										if(loop.downLinks.size()>0){
											if(count==0)loop.downLinks.clear();
											else{
												ArrayList<Link> tempLinkList1 = loop.downLinks;
												for (Link link:tempLinkList1)link.nextBlockNo = (link.nextBlockNo-((link.nextBlockNo / 10000)*10000))+((count-1) * 10000);
											}
										}
									}
							}
						}
						return;
					}*/
					
					removeAllHaltsAtStation(stationToDeleteStationId);
					updateHaltIDAfterDelete(stationToDeleteStationId);
					updateBlockIDAfterDelete(stationToDeleteStationId);
					
					ArrayList<Loop> temploopList = sectioninputdialog.loopList;
					ArrayList<Station> tempList=sectioninputdialog.stationList;
					sectioninputdialog.removeStation(stationToDelete);
					resultLabel.setText("Station with station name "+ stationName + " deleted");
					BlockInput blockInput = GlobalVar.getBlockInput();
					blockInput.updateStationNames();
					int count=0x0;
					for(Station tempstation: tempList ){
					count++;
					for (Loop loop : temploopList) {
						if( loop.stationName.equals( tempstation.stationName)){
								if(loop.upLinks.size()>0){
									if(count==tempList.size())loop.upLinks.clear();
									else{
										ArrayList<Link> tempLinkList = loop.upLinks;
										for (Link link:tempLinkList)link.nextBlockNo = (link.nextBlockNo-((link.nextBlockNo / 10000)*10000))+(count * 10000);
									}
								}
								if(loop.downLinks.size()>0){
									if(count==0)loop.downLinks.clear();
									else{
										ArrayList<Link> tempLinkList1 = loop.downLinks;
										for (Link link:tempLinkList1)link.nextBlockNo = (link.nextBlockNo-((link.nextBlockNo / 10000)*10000))+((count-1) * 10000);
									}
								}
							}
				 	}
				}
				}
			}
			// jpanel.repaint();
		}
	};

	ActionListener addloopbuttonButtonActionListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultLabel.setText("");
			if(stationNameComboBox.getSelectedIndex()!=-1){
			String stationName=stationNameComboBox.getSelectedItem().toString();
			//GlobalVar.loopInputDialog=null;
			LoopInputDialog m = new LoopInputDialog(stationName);
			m.directionLabel.setVisible(true);
			m.upRadioButton.setVisible(true);
			m.downRadioButton.setVisible(true);
			m.commonupRadioButton.setVisible(true);
			m.commondownRadioButton .setVisible(true);
			m.commonmiddleRadioButton.setVisible(true);
			m.looptraintype.setVisible(true);
			m.looptraintypelabel.setVisible(true);
	        m.looplength.setVisible(true);
	        m.looplengthlabel.setVisible(true);
			m.addloopbutton.setVisible(true);
			m.maximumspeedField.setVisible(true);
			m.maximumspeedLabel.setVisible(true);
			m.deleteButton.setVisible(false);
			m.editdeletelabel.setVisible(false);
			m.selectLoop.setVisible(false);
			m.setVisible(true);
			}
			else
			{
			resultLabel.setText("Select station to which Loopline loop need to be added");
			}
		}		
					
		};
		
		ActionListener deleteloopbuttonButtonActionListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resultLabel.setText("");
				if(stationNameComboBox.getSelectedIndex()!=-1){
				String stationName=stationNameComboBox.getSelectedItem().toString();
				//GlobalVar.loopInputDialog=null;
				LoopInputDialog m = new LoopInputDialog(stationName);
				if(m.getLoopLineCount()!=0){
					m.selectLoop.setVisible(true);
					m.deleteButton.setVisible(true);
					
					m.editdeletelabel.setVisible(true);
					m.directionLabel.setVisible(false);
					m.upRadioButton.setVisible(false);
					m.downRadioButton.setVisible(false);
					m.commonupRadioButton.setVisible(false);
					m.commondownRadioButton .setVisible(false);
					m.commonmiddleRadioButton.setVisible(false);
					m.looptraintype.setVisible(false);
					m.looptraintypelabel.setVisible(false);
					m.looplength.setVisible(false);
					m.looplengthlabel.setVisible(false);
					m.addloopbutton.setVisible(false);
					m.maximumspeedField.setVisible(false);
					m.maximumspeedLabel.setVisible(false);
					
					m.setVisible(true);
					}
					else
					{
					resultLabel.setText("Station " + stationName +" do not have any Loopline loop.");
					}
					
				}
				else
				{
				resultLabel.setText("Select station to delete Loopline loop ");
				}
			}		
						
			};

		

	ActionListener ViewallbuttonButtonActionListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
					AllLoopData allLoopData = new AllLoopData();
				
			

		}
	};


	public static void main(String[] args) throws IOException {
		StationInputDialog stationInputDialog = GlobalVar.getStationInputDialog();
	}
	
public void deleteBlockBetweenTwoStation(int firstStationIndex,int secondStationIndex){
				resultLabel.setText("");
				
			SectionInputDialog sectioninputdialog = GlobalVar
					.getSectionInputDialog();
			//Sneha:Come here to ensure why substring is just 1st digit?????????????
			//test with bit bigger input having more then 10 station

			ArrayList tempArray=sectioninputdialog.blockList;
			ArrayList temploopArray=sectioninputdialog.loopList;
			int stationId=firstStationIndex;
			int nextStationId=secondStationIndex;
			for (int l=0;l<temploopArray.size();l++){
				Loop tempLoop=(Loop)temploopArray.get(l);
				String tempLoopNo=String.format("%07d",tempLoop.blockNo);
				int checkLoopNo=Integer.parseInt(tempLoopNo.substring(0, 3));
				if(stationId==checkLoopNo){
					tempLoop.upLinks.clear();

				}else if(nextStationId==checkLoopNo){
					tempLoop.downLinks.clear();

				}
			}

			for (int b=tempArray.size()-1;b>=0;b--){
				Block tempBlock=(Block)tempArray.get(b);
				String tempBlockNo=String.format("%07d",tempBlock.getBlockNo());
				int checkBlockNo=Integer.parseInt(tempBlockNo.substring(0,3).toString());
				if(stationId==checkBlockNo){
					sectioninputdialog.blockList.remove(tempBlock);

				}
				
			}
			
	
}



	
	
void removeAllHaltsAtStation(int stationID){
	SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
	ArrayList<Loop> tempLoopList =sectioninputdialog.loopList;
	for(Loop tempLoop:tempLoopList){
		String loopIdString=String.format("%07d",tempLoop.blockNo);
		int loopStationID=Integer.parseInt(loopIdString.substring(0,3).toString());
		if(loopStationID==stationID){
		 	ArrayList<Train> trainList = sectioninputdialog.getTrainList(true);
			for(Train tempTrain:trainList){
				if(ifTrainHaltAtThisLoop(tempTrain, tempLoop))
				{
					removeHalt(tempTrain,tempLoop.blockNo);
				}
			}
		
		}
	}
}


void removeHalt(Train tempTrain,int loopId){
	
	for(int i=0;i<tempTrain.getNumberofHalts();i++){
		if(tempTrain.refTables.get(i).refLoopNo==loopId){
//			System.out.println("i the item removed " + i);
			tempTrain.refTables.remove(i);
			tempTrain.setNumberofHalts((tempTrain.getNumberofHalts())-1);
			return;
		}
	}
		
	
}


void updateHaltIDAfterDelete(int stationId){
	SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
	ArrayList<Train> trainList = sectioninputdialog.getTrainList(true);
	for(Train tempTrain:trainList){
		for(int i=0;i<tempTrain.getNumberofHalts();i++){
			String loopIdString=String.format("%07d",tempTrain.refTables.get(i).refLoopNo);
			int loopStationID=Integer.parseInt(loopIdString.substring(0,3).toString());
			if(loopStationID>stationId){
				//System.out.println("before " + tempTrain.refTables.get(i).refLoopNo);
				int haltLoopNo=tempTrain.refTables.get(i).refLoopNo;
				haltLoopNo=(haltLoopNo-((haltLoopNo / 10000)*10000))+((loopStationID-1) * 10000);
				//Loop loop=tempTrain.refTables.get(i).getReferenceLoop();
				//loop.blockNo=haltLoopNo;
				ReferenceTableEntry referenceTableEntry = new ReferenceTableEntry();
				referenceTableEntry.refLoopNo=haltLoopNo;
				//referenceTableEntry.setReferenceLoop(loop);
				referenceTableEntry.refArrTimeInput=tempTrain.refTables.get(i).refArrTimeInput;
				referenceTableEntry.refDepTimeInput=tempTrain.refTables.get(i).refDepTimeInput;
				tempTrain.refTables.set(i, referenceTableEntry);
				//System.out.println("after " + tempTrain.refTables.get(i).refLoopNo);
				
				
			}
		}
			
	}	
}


public void updateBlockIDAfterDelete(int stationID){
	SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
	ArrayList<Block> blockList = sectioninputdialog.blockList;
	for(Block tempBlock:blockList){
		String blockIdString=String.format("%07d",tempBlock.getBlockNo());
		int blockStationID=Integer.parseInt(blockIdString.substring(0,3).toString());
		if(blockStationID>stationID){
			tempBlock.blockNo = (tempBlock.blockNo-((tempBlock.blockNo / 10000)*10000))+((blockStationID-1) * 10000);
					if(tempBlock.upLinks.size()>0){
							ArrayList<Link> tempLinkList = tempBlock.upLinks;
							for (Link link:tempLinkList)
								link.nextBlockNo = (link.nextBlockNo-((link.nextBlockNo / 10000)*10000))+blockStationID * 10000;
					}
					if(tempBlock.downLinks.size()>0){
							ArrayList<Link> tempLinkList1 = tempBlock.downLinks;
							for (Link link:tempLinkList1)
								link.nextBlockNo = (link.nextBlockNo-((link.nextBlockNo / 10000)*10000))+(blockStationID-1) * 10000;
					}
			}
		
	}
	
}
public void updateBlockIDBeforAdd(int stationID){
	SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
	ArrayList<Block> blockList = sectioninputdialog.blockList;
	for(Block tempBlock:blockList){
		String blockIdString=String.format("%07d",tempBlock.getBlockNo());
		int blockStationID=Integer.parseInt(blockIdString.substring(0,3).toString());
		if(blockStationID>=stationID){
			tempBlock.blockNo = (tempBlock.blockNo-((tempBlock.blockNo / 10000)*10000))+((blockStationID+1) * 10000);
					if(tempBlock.upLinks.size()>0){
							ArrayList<Link> tempLinkList = tempBlock.upLinks;
							for (Link link:tempLinkList)
								link.nextBlockNo = (link.nextBlockNo-((link.nextBlockNo / 10000)*10000))+(blockStationID+2) * 10000;
					}
					if(tempBlock.downLinks.size()>0){
							ArrayList<Link> tempLinkList1 = tempBlock.downLinks;
							for (Link link:tempLinkList1)
								link.nextBlockNo = (link.nextBlockNo-((link.nextBlockNo / 10000)*10000))+(blockStationID * 10000);
					}
			}
		
	}
	
}

boolean ifTrainHaltAtThisLoop( Train tempTrain, Loop loop){

	if(tempTrain.scheduled){
	for(int i=0;i<tempTrain.numberofhalts;i++)
		if(tempTrain.refTables.get(i).refLoopNo==loop.blockNo){
		return true;	
		}
	}
	return false;
}



boolean ifTrainHaltAtThisStation(int stationId )
{
	SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
	ArrayList<Loop> tempLoopList= sectioninputdialog.loopList;
	for(Loop loop:tempLoopList){
		String loopIdString=String.format("%07d",loop.blockNo);
		int loopStationID=Integer.parseInt(loopIdString.substring(0,3).toString());
		if(loopStationID>=stationId){
		    ArrayList<Train> trainList = sectioninputdialog.getTrainList(true);
			for(Train tempTrain:trainList){
				if(ifTrainHaltAtThisLoop(tempTrain, loop))
				{
					return true;
				}
			}
		}
	}
	return false;

}


public void updateLoopIDOfTrainHaltsBeforeAdd(int stationId){
	//System.out.println("in train halt modification");
	SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
	ArrayList<Train> trainList = sectioninputdialog.getTrainList(true);
	for(Train tempTrain:trainList){
		for(int i=0;i<tempTrain.getNumberofHalts();i++){
			String loopIdString=String.format("%07d",tempTrain.refTables.get(i).refLoopNo);
			int loopStationID=Integer.parseInt(loopIdString.substring(0,3).toString());
			if(loopStationID>=stationId){
				//System.out.println("before " + tempTrain.refTables.get(i).refLoopNo);
				int haltLoopNo=tempTrain.refTables.get(i).refLoopNo;
				haltLoopNo=(haltLoopNo-((haltLoopNo / 10000)*10000))+((loopStationID+1) * 10000);
				//Loop loop=tempTrain.refTables.get(i).getReferenceLoop();
				//loop.blockNo=haltLoopNo;
				ReferenceTableEntry referenceTableEntry = new ReferenceTableEntry();
				referenceTableEntry.refLoopNo=haltLoopNo;
				//referenceTableEntry.setReferenceLoop(loop);
				referenceTableEntry.refArrTimeInput=tempTrain.refTables.get(i).refArrTimeInput;
				referenceTableEntry.refDepTimeInput=tempTrain.refTables.get(i).refDepTimeInput;
				tempTrain.refTables.set(i, referenceTableEntry);
				//System.out.println("after " + tempTrain.refTables.get(i).refLoopNo);
				
				
			}
		}
			
	}
}


public void updateLinkage(Loop loop){
	
	String loopId = String.format("%07d",loop.blockNo);
	int loopstationId = Integer.parseInt(loopId.substring(0, 3));
	int uplinkLinkPriority=0;
	int downlinkLinkPriority=0;
	
	int loopPriorityCheck = Integer.parseInt(loopId.substring(3, 5));
	if (loopPriorityCheck == 00	|| loopPriorityCheck == 20)
		uplinkLinkPriority=1;
	else if (loopPriorityCheck == 01)
		uplinkLinkPriority=2;
	else if (loopPriorityCheck == 22)
		uplinkLinkPriority=3;
	else if (loopPriorityCheck == 21)
		uplinkLinkPriority=4;
	else if (loopPriorityCheck == 23)
		uplinkLinkPriority=5;
	
	
	if (loopPriorityCheck == 10
			|| loopPriorityCheck == 20)
		downlinkLinkPriority = 1;
	else if (loopPriorityCheck == 11)
		downlinkLinkPriority = 2;
	else if (loopPriorityCheck == 23)
		downlinkLinkPriority = 3;
	else if (loopPriorityCheck == 21)
		downlinkLinkPriority = 4;
	else if (loopPriorityCheck == 22)
		downlinkLinkPriority = 5;
	
	
	SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
	for (Block block : sectioninputdialog.blockList){
		
		String blockId=String.format("%07d", block.blockNo);
		int blockstationId = Integer.parseInt(blockId.substring(0, 3));
		int blockSerialNo =Integer.parseInt(blockId.substring(5));
		if ((loopstationId == blockstationId) && (blockSerialNo == 10)){
			if  (block.direction==GlobalVar.UP_DIRECTION){
				if(loop.direction==GlobalVar.UP_DIRECTION||loop.direction==GlobalVar.COMMON_DIRECTION){
				Link link = new Link();
				link.setNextBlockNo(block.blockNo);
				link.setPriority(1);
				loop.upLinks.add(link);
				//System.out.println("---Block Up--loop common/up " + loop.blockNo + " " + block.blockNo);
				}
			}
			if(block.direction==GlobalVar.COMMON_DIRECTION) {
				if(loop.direction==GlobalVar.UP_DIRECTION){
					Link link = new Link();
					link.setNextBlockNo(block.blockNo);
					link.setPriority(1);
					loop.upLinks.add(link);
					//System.out.println("---Block common--loop up " + loop.blockNo + " " + block.blockNo);
				}
				if(loop.direction==GlobalVar.DOWN_DIRECTION){
					Link linkforBlock = new Link();
					linkforBlock.setNextBlockNo(loop.blockNo);
					linkforBlock.setPriority(downlinkLinkPriority);
					block.downLinks.add(linkforBlock);
					//System.out.println("---Block common--loop down " + loop.blockNo + " " + block.blockNo);
				}
				if(loop.direction==GlobalVar.COMMON_DIRECTION){
					Link linkforLoop = new Link();
					linkforLoop.setNextBlockNo(block.blockNo);
					linkforLoop.setPriority(1);
					loop.upLinks.add(linkforLoop);
					
					//System.out.println("---Block common--loop coomon " + loop.blockNo + " " + block.blockNo);
					
					Link linkforBlock = new Link();
					linkforBlock.setNextBlockNo(loop.blockNo);
					linkforBlock.setPriority(downlinkLinkPriority);
					block.downLinks.add(linkforBlock);
					//System.out.println("---Block common--loop coomon " + block.blockNo + " " + loop.blockNo);
				}
			

			}
			if(block.direction==GlobalVar.DOWN_DIRECTION){
				if(loop.direction==GlobalVar.DOWN_DIRECTION||loop.direction==GlobalVar.COMMON_DIRECTION){
					Link linkforBlock = new Link();
					linkforBlock.setNextBlockNo(loop.blockNo);
					linkforBlock.setPriority(downlinkLinkPriority);
					block.downLinks.add(linkforBlock);
					//System.out.println("Block down--loop down/common " + block.blockNo + " " + loop.blockNo);

				}
			
			}

		}
		
	}


	//--------------------------------------------------------------------
	Block lastblockInCommon=null;
	Block lastblockInDown=null;
	Block lastblockInUp=null;
	for (Block block : sectioninputdialog.blockList){
		String blockId=String.format("%07d", block.blockNo);
		int blockstationId = Integer.parseInt(blockId.substring(0, 3));
		if ((loopstationId-1) == blockstationId){
			if(block.direction==GlobalVar.DOWN_DIRECTION){
				lastblockInDown=block;
			}
			if(block.direction==GlobalVar.COMMON_DIRECTION) {
				lastblockInCommon=block;
			}
			if(block.direction==GlobalVar.UP_DIRECTION) {
				lastblockInUp=block;
			}
		}
		
	}
	
	if(loop.direction==GlobalVar.DOWN_DIRECTION){
		if(lastblockInDown!=null){
			Link linkforloop=new Link();
			linkforloop.setNextBlockNo(lastblockInDown.blockNo);
			linkforloop.setPriority(1);
			loop.downLinks.add(linkforloop);
			//System.out.println("---Block down--loop down " + loop.blockNo + " " + lastblockInDown.blockNo);

			}
		if(lastblockInCommon!=null){
			Link linkforloop=new Link();
			linkforloop.setNextBlockNo(lastblockInCommon.blockNo);
			linkforloop.setPriority(1);
			loop.downLinks.add(linkforloop);
			//System.out.println("---Block common--loop down " + loop.blockNo + " " + lastblockInCommon.blockNo);
		}
	}
	if(loop.direction==GlobalVar.COMMON_DIRECTION){
		if(lastblockInDown!=null){
			Link linkforloop=new Link();
			linkforloop.setNextBlockNo(lastblockInDown.blockNo);
			linkforloop.setPriority(1);
			loop.downLinks.add(linkforloop);
			//System.out.println("---Block down--loop common " + loop.blockNo + " " + lastblockInDown.blockNo);

		}
		if(lastblockInUp!=null){
			Link linkforblock=new Link();
			linkforblock.setNextBlockNo(loop.blockNo);
			linkforblock.setPriority(uplinkLinkPriority);
			lastblockInUp.upLinks.add(linkforblock);
			//System.out.println("---Block up--loop common " + lastblockInUp.blockNo + " " + loop.blockNo);
		}
		if(lastblockInCommon!=null){
			Link linkforloop=new Link();
			linkforloop.setNextBlockNo(lastblockInCommon.blockNo);
			linkforloop.setPriority(1);
			loop.downLinks.add(linkforloop);
			//System.out.println("---Block common--loop common " + loop.blockNo + " " + lastblockInCommon.blockNo);
			
			Link linkforblock=new Link();
			linkforblock.setNextBlockNo(loop.blockNo);
			linkforblock.setPriority(uplinkLinkPriority);
			lastblockInCommon.upLinks.add(linkforblock);
			//System.out.println("---Block common--loop common " + lastblockInCommon.blockNo + " " + loop.blockNo);

		}
	}
	if(loop.direction==GlobalVar.UP_DIRECTION){
		if(lastblockInUp!=null){
			Link linkforblock=new Link();
			linkforblock.setNextBlockNo(loop.blockNo);
			linkforblock.setPriority(uplinkLinkPriority);
			lastblockInUp.upLinks.add(linkforblock);
			//System.out.println("---Block up--loop up " + lastblockInUp.blockNo + " " + loop.blockNo);
		}
		if(lastblockInCommon!=null){
			Link linkforblock=new Link();
			linkforblock.setNextBlockNo(loop.blockNo);
			linkforblock.setPriority(uplinkLinkPriority);
			lastblockInUp.upLinks.add(linkforblock);
			//System.out.println("---Block common--loop up " + lastblockInUp.blockNo + " " + loop.blockNo);

		}

			
	}
	
	
	

}
public void printLoopWithBlockLinkage(){
	SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
	ArrayList<Loop> temploopList = sectioninputdialog.loopList;
	ArrayList<Station> tempList=sectioninputdialog.stationList;
	System.out.println("started.............................");
	for(Station tempstation: tempList ){
		for (Loop loop : temploopList) {
			if( loop.stationName.equals( tempstation.stationName)){
					if(loop.upLinks!=null)
					{

						ArrayList<Link> tempLinkList = loop.upLinks;
						for (Link link:tempLinkList){
							System.out.println("loop.stationName: " + loop.stationName + "loop.blockno: " + loop.blockNo+" uplink " + link.nextBlockNo);
						}
					}
					if(loop.downLinks!=null){
						ArrayList<Link> tempLinkList1 = loop.downLinks;
						for (Link link:tempLinkList1){
							System.out.println("loop.stationName: " + loop.stationName + "loop.blockno: " + loop.blockNo+" downlink " + link.nextBlockNo);
							
						}
					
					}
				
		}
	 
	 }
	}	
	System.out.println("ended.............................");
}

	public void write(ArrayList<Station> stationList) throws IOException {
		String stationFileName = FileNames.getStationFileName();
		BufferedWriter bw = new BufferedWriter(new FileWriter(stationFileName));
		String formatString = "/*Name    startKM    endKM    stationEntrySpeed  */\n\n";
		bw.write(formatString);

		for (Station station : stationList) {
			bw.write("\"" + station.stationName + "\"");
			bw.write(" ");
			bw.write(String.valueOf(station.startMilePost));
			bw.write(" ");
			bw.write(String.valueOf(station.endMilePost));
			bw.write(" ");
			bw.write(String.valueOf(station.stationVelocity));
			bw.write("\n");
		}
		bw.close();
	}

	public void readStations(ArrayList<Station> stationList) throws IOException {
		String stationFileName = FileNames.getStationFileName();
		StreamTokenizer streamTokenizer = new StreamTokenizer(new FileReader(
				stationFileName));
		streamTokenizer.slashSlashComments(true);
		streamTokenizer.slashStarComments(true);

		while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			String stationName = streamTokenizer.sval;

			streamTokenizer.nextToken();
			double startKM = streamTokenizer.nval;

			streamTokenizer.nextToken();
			double endKM = streamTokenizer.nval;

			streamTokenizer.nextToken();
			int crossVelocity = (int) streamTokenizer.nval;

			Station station = new Station(startKM, endKM, crossVelocity,
					stationName);

			GlobalVar.getSectionInputDialog().addStation(station);
		}

	}

	class AllStationData extends JFrame implements TableModelListener {
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		JTable stationDataTable;

		public AllStationData() {
			super("List of stations");

			stationDataTable = new JTable(new AbstractTableModel() {

				/**
			 * 
			 */
				private static final long serialVersionUID = 1L;
				String[] columnNames = { "Station Id", "Name", "Start Km",
						"End Km", "Entry Speed" };
				SectionInputDialog sectioninputdialog = GlobalVar
						.getSectionInputDialog();

				ArrayList<Station> stationList = sectioninputdialog.stationList;

				public String getColumnName(int col) {
					return columnNames[col];
				}

				public int getRowCount() {
					return stationList.size();
				}

				public int getColumnCount() {
					return columnNames.length;
				}

				public Object getValueAt(int row, int col) {

					switch (col) {
					case 0:

						int l = row + 1;
						String station_Id = String.format("%02d", l);
						return station_Id;

					case 1:
						return stationList.get(row).stationName;
					case 2:
						return stationList.get(row).startMilePost;
					case 3:
						return stationList.get(row).endMilePost;
					case 4:
						return stationList.get(row).stationVelocity;
					}

					return null;
				}

			});

			JScrollPane scrollPane = new JScrollPane(stationDataTable);
			stationDataTable.setPreferredScrollableViewportSize(new Dimension(
					500, 70));
			getContentPane().add(scrollPane);
			setBounds(100, 100, 400, 400);
			setVisible(true);
		}

		@Override
		public void tableChanged(TableModelEvent arg0) {
		}
	}

	class AllLoopData extends JFrame implements TableModelListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JTable loopDataTable;

		public AllLoopData() {

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

				String[] columnNames = { "Id", "Direction", "Station Name", "Type",
						"Type of Trains Allowed", "Maximum Speed" };

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
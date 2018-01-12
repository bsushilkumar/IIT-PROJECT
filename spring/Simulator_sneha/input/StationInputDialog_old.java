package input;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntityList.StationList;
import javafx.scene.effect.GlowBuilder;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class StationInputDialog extends InputDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JLabel stationNameLabel = new JLabel("Station Name");
	// public JLabel stationCodeLabel = new JLabel("station name");
	public JLabel startKilometreLabel = new JLabel("Start Km.");
	public JLabel endKilometreLabel = new JLabel("End Km.");
	//public JLabel stationEntrySpeedLabel = new JLabel("Station Entry Speed in Km/hr");
	public JButton addButton = new JButton("Add station");
	public JTextField stationNameField = new JTextField();
	// public JTextField stationCodeField = new JTextField();
	public JTextField startKilometreField = new JTextField();
	public JTextField endKilometreField = new JTextField();
	//public JTextField stationEntrySpeedField = new JTextField("100");

	public JButton deleteButton = new JButton("Delete station");
	public JButton DoneButton = new JButton("Done");

	public JLabel editDeleteOptionLabel = new JLabel("Edit/Delete option");
	public JLabel stationCodeEditDeleteLabel = new JLabel("Station name");
	// public JTextField stationCodeEditField = new JTextField();
	public JComboBox<String> stationNameComboBox = null;
	public JButton editButton = new JButton("Edit");
	public JButton updateButton = new JButton("Update");

	public JButton viewLoopButton = new JButton("View Loop Details");
	public JButton viewAllButton = new JButton("View Station Details");

	public JLabel resultLabel = new JLabel();

	public Station stationToBeUpdated = null;

	public JLabel templates = new JLabel();
	String[] stationNames = null;
	private JComboBox<String> stationtype;
	private static String[] filename = { "General" };
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
		

		stationtype = new JComboBox<String>(filename);
		templates = new JLabel(pics[0]);
		setComponentActionListeners();
		setComponentBounds();
		addComponents();

		// JScrollPane scrollPane = new JScrollPane(jpanel);
		// this.add(scrollPane);
		this.add(jpanel);

		this.setTitle("Station Input");
		// this.setVisible(true);
		
		
		

	}

	@Override
	public void addComponents() {

		
		this.getContentPane().add(startKilometreLabel);
		this.getContentPane().add(endKilometreLabel);
		this.getContentPane().add(stationNameLabel);
		// this.getContentPane().add(stationCodeLabel);
		//this.getContentPane().add(stationEntrySpeedLabel);

		this.getContentPane().add(startKilometreField);
		this.getContentPane().add(endKilometreField);
		// this.getContentPane().add(stationCodeField);
		this.getContentPane().add(stationNameField);
		//this.getContentPane().add(stationEntrySpeedField);

		this.getContentPane().add(addButton);

		this.getContentPane().add(stationCodeEditDeleteLabel);
		this.getContentPane().add(deleteButton);

		this.getContentPane().add(stationNameComboBox);
		this.getContentPane().add(editButton);
		this.getContentPane().add(updateButton);
		this.getContentPane().add(editDeleteOptionLabel);

		this.getContentPane().add(viewAllButton);
		this.getContentPane().add(resultLabel);
		//this.getContentPane().add(stationtype);
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

		// y1 += heightDifference;
		// stationCodeLabel.setBounds(x1, y1, labelWidth, labelHeight);
		// stationCodeField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += heightDifference;
		startKilometreLabel.setBounds(x1, y1, labelWidth, labelHeight);
		startKilometreField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += heightDifference;
		endKilometreLabel.setBounds(x1, y1, labelWidth, labelHeight);
		endKilometreField.setBounds(x2, y1, fieldWidth, fieldHeight);

		/*y1 += heightDifference;
		stationEntrySpeedLabel.setBounds(x1, y1, labelWidth, labelHeight);
		stationEntrySpeedField.setBounds(x2, y1, fieldWidth, fieldHeight);*/

		/*y1 += 2 * heightDifference;
		stationtype.setBounds(x1, y1, fieldWidth, fieldHeight);
		templates.setBounds(x2, y1, labelWidth, labelHeight);*/

		y1 += 2 * heightDifference;
		addButton.setBounds(x1, y1, buttonWidth, buttonHeight);
		viewAllButton.setBounds(x2, y1, buttonWidth, buttonHeight);

		y1 += heightDifference;
		viewLoopButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += 2 * heightDifference;
		editDeleteOptionLabel.setBounds(x1, y1, labelWidth, labelHeight);

		y1 += heightDifference;
		stationCodeEditDeleteLabel.setBounds(x1, y1, labelWidth, labelHeight);
		stationNameComboBox.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += heightDifference;
		editButton.setBounds(x1, y1, 4 * buttonWidth / 5, buttonHeight);
		deleteButton.setBounds(x2 - 2 * x1, y1, 4 * buttonWidth / 5,
				buttonHeight);
		updateButton.setBounds(2 * x2 - 4 * x1, y1, 4 * buttonWidth / 5,
				buttonHeight);

		y1 += 2 * heightDifference;
		DoneButton.setBounds(x2 - 2 * x1, y1, buttonWidth, buttonHeight);

		y1 += heightDifference;
		resultLabel.setBounds(x1, y1, 500, labelHeight);
	}

	@Override
	public void setComponentActionListeners() {
		deleteButton.addActionListener(deleteActionListener);
		addButton.addActionListener(addActionListener);
		editButton.addActionListener(editActionListener);
		updateButton.addActionListener(updateActionListener);
		viewAllButton.addActionListener(viewAllActionListener);
		//stationtype.addActionListener(stationtypeActionListener);
		viewLoopButton.addActionListener(viewLoopActionListener);
		DoneButton.addActionListener(DoneActionListener);
	}

	ActionListener DoneActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//sneha : make loopInputDialog null so that in section input dialogbox visibility of 
			//station input dialog box decided accordingly
			GlobalVar.loopInputDialog=null;
			dispose();
		}
	};

	ActionListener viewLoopActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			AllLoopData allLoopData = new AllLoopData();

		}
	};

	ActionListener updateActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (stationToBeUpdated == null) {
				resultLabel.setText("Choose a station to edit");
				return;
			}

			String stationName = stationNameField.getText();

			if (stationName.isEmpty()) {
				resultLabel.setText("Station name cannot be empty");
			} else {
				SectionInputDialog sectioninputdialog = GlobalVar
						.getSectionInputDialog();
				ArrayList<Loop> temploopList = sectioninputdialog.loopList;
				ArrayList<Loop> addloopList = new ArrayList<Loop>();
				ArrayList<Loop> removeloopList = new ArrayList<Loop>();

				for (Loop temploop : temploopList) {
					if (temploop.stationName == stationToBeUpdated.stationName) {

						Loop loop = new Loop();
						loop.blockNo = temploop.blockNo;
						loop.direction = temploop.direction;
						loop.whetherMainLine = temploop.whetherMainLine;
						loop.stationName = stationName;
						loop.maximumPossibleSpeed = temploop.maximumPossibleSpeed;

						addloopList.add(loop);
						removeloopList.add(temploop);

					}

				}

				for (Loop loop : addloopList) {
					sectioninputdialog.addLoop(loop);
				}
				for (Loop loop : removeloopList) {
					sectioninputdialog.removeLoop(loop);
				}
				addloopList.clear();
				removeloopList.clear();
				double startMilePost = Double.parseDouble(startKilometreField
						.getText());
				double endMilePost = Double.parseDouble(endKilometreField
						.getText());
				//double speed = Double.parseDouble(stationEntrySpeedField.getText());
				double speed = 100;
				Station station = stationToBeUpdated;
				station.stationName = stationName;
				//station.setStationCode(stationCode);
				station.startMilePost = startMilePost;
				station.endMilePost = endMilePost;
				station.stationVelocity = speed;

				resultLabel.setText("Station updated");
				sectioninputdialog.updateStationNames();

			}

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
			stationName = (String) stationNameComboBox.getSelectedItem();
			SectionInputDialog sectioninputdialog = GlobalVar
					.getSectionInputDialog();
			if (stationNameComboBox.getSelectedIndex() == -1) {
				resultLabel
						.setText("Choose a station name to edit station properties ");
			} else {
				Station station = sectioninputdialog
						.getStationByStationName(stationName);
				if (station == null) {
					resultLabel.setText("Station with station name "
							+ stationName + " not found");
				} else {
					stationNameField.setText(station.stationName);
					// stationCodeField.setText(station.getStationCode());
					startKilometreField.setText(station.startMilePost + "");
					endKilometreField.setText(station.endMilePost + "");
					//stationEntrySpeedField.setText(station.stationVelocity + "");
					stationToBeUpdated = station;

					LoopInputDialog m = GlobalVar.setLoopInputDialog(stationName);
					m.setVisible(true);
				}
			}

			// jpanel.repaint();
		}
	};

	ActionListener addActionListener = new ActionListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				Station prevStation=null;
				Station nextStation=null;
				int prevIndex=-1;
				int nextIndex=-1;
				String stationName = stationNameField.getText();
				
				SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
				Station findStation = sectioninputdialog.getStationByStationName(stationName);
				
						
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
					}
					else
					{
//---------------------------------------------------------------------------------------------

						
						//Sneha : if one tries to add station between two station and there is already
						//added blocks found then prompt for delete block 
					

						ArrayList<Station> tempstationlist1=sectioninputdialog.stationList;

						for(int i=0; i<tempstationlist1.size();i++ ){
								Station tempstation =tempstationlist1.get(i);
								if(tempstation.endMilePost < startMilePost){
									prevIndex++;
								}
							
						}
						
						   if(prevIndex!=-1){
							   
							   if(prevIndex<tempstationlist1.size()){
								   Station tempstation =tempstationlist1.get(prevIndex);
								   prevStation=tempstation;
								   tempstation=tempstationlist1.get(prevIndex+1);
								   nextStation=tempstation;
							   }
						     
							   
			
								SectionInputDialog sectioninputdialog2 = GlobalVar.getSectionInputDialog();
								ArrayList<Block> blockList = sectioninputdialog2.blockList;
								for (Block tempBlock:blockList){
									String blockId = String.format("%06d", tempBlock.blockNo);
									
									
									int blockstationId = Integer.parseInt(blockId.substring(0, 2));
									int prevstationId= prevIndex+1;
									if(blockstationId==prevstationId){
										resultLabel.setText("Delete blocks between station " + prevStation.stationName + " and " + nextStation.stationName);
										return;
										
									}
								}
							
						   }
						   else
						   {
							   resultLabel.setText("Check Start and End Kilometer");
							   return;
						   }
						
						
					
						
//--------------------------------------------------------------------------------
					Station station = new Station();
					station.stationName = stationName;
					station.startMilePost = startMilePost;
					station.endMilePost = endMilePost;
					station.stationVelocity = speed;

					resultLabel.setText("Station with stationCode "	+ station.stationName + " added");
					sectioninputdialog.addStation(station);
					
					
					ArrayList<Loop> temploopList = sectioninputdialog.loopList;
					ArrayList<Station> tempstationlist=sectioninputdialog.stationList;
					int count=0x0;
					for(Station tempstation: tempstationlist ){
					count++;
						for (Loop loop : temploopList) {
							if( loop.stationName.equals(tempstation.stationName))
								{
								if((loop.blockNo / 10000 )!=count){
							    	loop.blockNo = (loop.blockNo-((loop.blockNo / 10000)*10000))+(count * 10000);
							    }


								}
						}
					 
					 }
					
					
					//Sneha: before opening loop input dialog make station input dialog box invisible. 
					StationInputDialog stationinputdialog = GlobalVar.getStationInputDialog();
					stationinputdialog.setVisible(false);
					
					//Sneha : Following two lines were after else block
					//this was generating exception whenever new station with same
					//station name added.
					LoopInputDialog m = GlobalVar.setLoopInputDialog(station.stationName);
					m.setVisible(true);
					


					

					
					}
				}
				else {
					
					resultLabel.setText("Station with stationCode "
							+ stationName + " already exists");
					 }
				

			}

			catch (Exception ex) {
				
				resultLabel.setText("Check Input Fields");
			}
			// jpanel.repaint();
		}
	};

	ActionListener deleteActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String stationName = (String) stationNameComboBox.getSelectedItem();
			if (stationNameComboBox.getSelectedIndex() == -1) {
				resultLabel
						.setText("Enter station code of station to be deleted");

			} else {
				SectionInputDialog sectioninputdialog = GlobalVar
						.getSectionInputDialog();
				
				ArrayList<Station> stationArrayList = sectioninputdialog.stationList;
				Station stationToDelete = null;
				for (Station station : stationArrayList) {
					if (station.stationName.equalsIgnoreCase(stationName)) {
						stationToDelete = station;
						break;
					}
				}

				if (stationToDelete == null) {
					resultLabel.setText("Station with station name "
							+ stationName + " not found");
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
						
						for (Block tempBlock:blockList){
							String blockId = String.format("%06d", tempBlock.blockNo);
							int blockstationId = Integer.parseInt(blockId.substring(0, 2));
							if(blockstationId==prevstationId){
								prev=true;
								break;
								
							}
						}

					}
					if((stationNameComboBox.getSelectedIndex()+1)!=stationNameComboBox.getItemCount()){
					stationToDeleteStationId=stationNameComboBox.getSelectedIndex()+1;
					for (Block tempBlock:blockList){
						String blockId = String.format("%06d", tempBlock.blockNo);
						int blockstationId = Integer.parseInt(blockId.substring(0, 2));
						
						if(blockstationId==stationToDeleteStationId){
							next=true;
							break;
							
						}
					}

					}
					
					
					
					
					if(prev && next){
						Station prevStation=tempstationlist.get(prevstationId-1);
						Station nextStation=tempstationlist.get(stationToDeleteStationId);
						resultLabel.setText("Delete blocks between stations " + prevStation.stationName + " , " + stationToDelete.stationName +" and "+ nextStation.stationName );
						return;
					}

					
					if (prev||next){
						if(prev){
							Station prevStation=tempstationlist.get(prevstationId-1);
							resultLabel.setText("Delete blocks between station " + prevStation.stationName + " and " + stationToDelete.stationName);
						}
						if(next)
						{
							Station nextStation=tempstationlist.get(stationToDeleteStationId);
							resultLabel.setText("Delete blocks between station " +  stationToDelete.stationName +" and " +nextStation.stationName );
						}
					return;
					}
					
				

							
				
				//-------------------------------------------------------
					sectioninputdialog.removeStation(stationToDelete);
					resultLabel.setText("Station with station name "
							+ stationName + " deleted");
					BlockInput blockInput = GlobalVar.getBlockInput();
					blockInput.updateStationNames();
				}
				
			}

			// jpanel.repaint();
		}
	};

	ActionListener stationtypeActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (stationtype.getSelectedIndex() < 2) {
				templates.setIcon(pics[stationtype.getSelectedIndex()]);
				z = stationtype.getSelectedIndex();
			} else
				z = 2;
		}

	};

	public static void main(String[] args) throws IOException {
		StationInputDialog stationInputDialog = GlobalVar
				.getStationInputDialog();
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
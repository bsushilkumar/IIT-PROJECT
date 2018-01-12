package input;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntities.trackProperties.Gradient;
import gui.entities.sectionEntities.trackProperties.GradientEffect;
import gui.entities.sectionEntityList.StationList;
import gui.entities.trainEntities.Train;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import simulator.input.SortStation;

public class SectionInputDialog extends InputDialog {

	private static final long serialVersionUID = 1L;
	public JLabel sectionNameLabel = new JLabel("TextCase Name");

	public JLabel icon = new JLabel();

	public JButton stationButton = new JButton("Station,Loop ");
	public JButton blockButton = new JButton("Block");
	public JButton parameterButton = new JButton("Parameters");
	public JButton gradientButton = new JButton("Gradient");
	public JButton trainButton = new JButton("Trains");
	public JLabel resultLabel = new JLabel("");
	private int labelWidth = 70;
	private int y1 = 20;
	private int x1 = 40;
	private int buttonHeight = 30;
	private int buttonWidth = 190;
	//shashank
	public JButton maintblockButton = new JButton("Maintenance Blocks");
	public JMenuBar menuBar = new JMenuBar();
	public JMenu fileMenu = new JMenu("File");
	public JMenuItem newMenuItem = new JMenuItem("New");
	public JMenuItem openMenuItem = new JMenuItem("Open");
	public JMenuItem saveMenuItem = new JMenuItem("Save");
	public JMenuItem saveAsMenuItem = new JMenuItem("Save As");
	public JMenuItem closeMenuItem = new JMenuItem("Close");

	public ArrayList<Station> stationList = new ArrayList<Station>();
	public ArrayList<Block> blockList = new ArrayList<Block>();
	public ArrayList<Loop> loopList = new ArrayList<Loop>();
	public ArrayList<Train> trainList = new ArrayList<Train>();
	public ArrayList<Gradient> gradientFormatList = new ArrayList<Gradient>();
	public ArrayList<GradientEffect> gradientEffectList = new ArrayList<GradientEffect>();
	//shashank
	
	public ArrayList<String>blockloopList  = new ArrayList<String>();
	public ArrayList<String>starttimeList  = new ArrayList<String>();
	public ArrayList<String>endtimeList  = new ArrayList<String>();
	public ArrayList<String>directionList  = new ArrayList<String>();

	public SectionInputDialog() {
		// super();
		x = 40;
		y = 40;
		this.setBounds(20, 20, 300, 500);
		setComponentActionListeners();
		setComponentBounds();
		addComponents();

		// JScrollPane scrollPane = new JScrollPane(jpanel);
		// this.add(scrollPane);
		this.add(jpanel);

		this.setTitle("Test Case Input");
		// this.setVisible(true);

	}

	@Override
	public void addComponents() {

		this.getContentPane().add(stationButton);
		this.getContentPane().add(parameterButton);
		this.getContentPane().add(gradientButton);
		this.getContentPane().add(trainButton);
		this.getContentPane().add(blockButton);
		this.getContentPane().add(resultLabel);
		this.getContentPane().add(maintblockButton);
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		//sneha : enable save and saveas option only if new directory is selected for saving files or opened  
		//existing directory with previous files
		saveMenuItem.setEnabled(false);
		saveAsMenuItem.setEnabled(false);
		fileMenu.add(closeMenuItem);
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
	}

	@Override
	public void setComponentBounds() {
		int yDifference = 40;

		// y1 += yDifference;
		// RDSO.setBounds(x1, y1, 150,50);

		y1 += yDifference;
		stationButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		blockButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		parameterButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		gradientButton.setBounds(x1, y1, buttonWidth, buttonHeight);
		
		y1 += yDifference;
		maintblockButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		trainButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += 7 * yDifference / 2;
		resultLabel.setBounds(x1 / 2, y1, buttonWidth, buttonHeight);

	}

	@Override
	public void setComponentActionListeners() {

		stationButton.addActionListener(stationActionListener);
		blockButton.addActionListener(blockActionListener);
		trainButton.addActionListener(trainActionListener);
		parameterButton.addActionListener(parameterActionListener);
		gradientButton.addActionListener(gradientActionListener);

		saveMenuItem.addActionListener(saveActionListener);
		closeMenuItem.addActionListener(closeActionListener);
		openMenuItem.addActionListener(openActionListener);
		newMenuItem.addActionListener(newMenuItemActionListener);
		saveAsMenuItem.addActionListener(saveAsActionListener);
		maintblockButton.addActionListener(maintblockActionListener);

	}

	ActionListener stationActionListener = new ActionListener() {

	
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			StationInputDialog stationInputDialog = GlobalVar
					.getStationInputDialog();
			

			stationInputDialog.setVisible(true);

		
		
		}
	};

	ActionListener blockActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (stationList.size() > 1) {
				BlockInput blockInput = GlobalVar.getBlockInput();
				blockInput.setVisible(true);
			} else
				resultLabel.setText("Enter min. 2 stations");
		}
	};

	ActionListener trainActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			TrainInputDialog trainInputDialog = GlobalVar.getTrainInputDialog();
			trainInputDialog.setVisible(true);
		}
	};

	ActionListener parameterActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ParameterInputDialog parameterInputDialog = GlobalVar
					.getParameterInputDialog();
			parameterInputDialog.setVisible(true);
		}
	};

	ActionListener gradientActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			GradientInputDialog gradientInputDialog = GlobalVar
					.getGradientInputDialog();
			gradientInputDialog.setVisible(true);
		}
	};

	ActionListener maintblockActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Maintblock maintblock = GlobalVar.getMaintInputDialog();
			maintblock.setVisible(true);
		}
	};

	ActionListener newMenuItemActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int response = FileNames.chooseDirectory(
					FileNames.getTestCaseDirectory(), "new");
			if (response == JFileChooser.APPROVE_OPTION) {
				
				//Sneha: Added following two line
				//Enable both option if new test input creation option selected. 
				saveMenuItem.setEnabled(true);
				saveAsMenuItem.setEnabled(true);
				
				clearAllLists();

			}
		}

	};

	ActionListener saveActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (FileNames.getTestCaseDirectory().equalsIgnoreCase("")) {
				int response = FileNames.chooseDirectory(
						FileNames.getTestCaseDirectory(), "save");
				if (response != JFileChooser.APPROVE_OPTION)
					return;
			}

			saveFiles();
		}
	};

	ActionListener saveAsActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int response = FileNames.chooseDirectory(
					FileNames.getTestCaseDirectory(), "save as");
			if (response != JFileChooser.APPROVE_OPTION)
				return;

			saveFiles();
		}
	};
	ActionListener openActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int response = FileNames.chooseDirectory(
					FileNames.getTestCaseDirectory(), "open");

			if (response != JFileChooser.APPROVE_OPTION)
				return;
			try {
				clearAllLists();

				GlobalVar.getStationInputDialog().readStations(stationList);
				GlobalVar.getBlockInput().readBlocks(blockList);
				GlobalVar.getBlockInput().readLoops(loopList);
				GlobalVar.getParameterInputDialog().readParameters();
				GlobalVar.getGradientInputDialog().readGradients(
						gradientFormatList);
				GlobalVar.getGradientEffectsInputDialog().readGradientEffects(
						gradientEffectList);
				GlobalVar.getTrainInputDialog().readScheduledTrains(trainList,
						stationList.size());
				GlobalVar.getTrainInputDialog()
						.readUnscheduledTrains(trainList);
				
				//Sneha: Added following two line
				//enable save and save as only if openAction handler does not result in exception
				saveMenuItem.setEnabled(true);
				saveAsMenuItem.setEnabled(true);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	};

	ActionListener closeActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Closed");
			System.exit(0);
		}
	};

	public Station getStationByStationName(String stationName) {
		int count=0;

		
		for (Station station : stationList) {
			if (station.getStationName().equalsIgnoreCase(stationName))
			{
				return station;
			}
		}
		return null;
	}

	public String getStationNameByStationId(int stationId) {
		int count = 0;
		String q = null;
		for (Station station : stationList) {
			count++;
			if (stationId == count) {
				q = station.getStationName();
				return q;
			}
		}
		return null;
	}

	public String getStationIdByStationName(String stationId) {
		int count = 1;
		String q = null;
		for (Station station : stationList) {

			if (stationId == station.getStationName())

				q = String.format("%02d", count);
			count++;
			return q;

		}
		return null;
	}

	public Loop getupLoopByStationId(int blockstationId) {
		for (Loop loop : loopList) {
			int loopno = loop.getBlockNo();
			String loopId = String.format("%06d", loopno);
			int loopstationId = Integer.parseInt(loopId.substring(0, 2));
			if (loopstationId == blockstationId)
				return loop;

		}
		return null;
	}

	public void clearAllLists() {
		for (Station station : stationList) {
			removeStation(station);
		}

		for (Block block : blockList) {
			removeBlock(block);
		}

		for (Loop loop : loopList) {
			removeLoop(loop);
		}

		for (Gradient gradientFormat : gradientFormatList) {
			removeGradient(gradientFormat);
		}

		for (GradientEffect gradientEffect : gradientEffectList) {
			removeGradientEffect(gradientEffect);
		}

		for (Train train : trainList) {
			removeTrain(train);
		}
	}

	protected void saveFiles() {
		try {
			GlobalVar.getStationInputDialog().write(stationList);
			GlobalVar.getBlockInput().writeBlocks(blockList);
			GlobalVar.getBlockInput().writeLoops(loopList);
			GlobalVar.getParameterInputDialog().write();
			GlobalVar.getGradientInputDialog().write(gradientFormatList);
			GlobalVar.getGradientEffectsInputDialog().write(gradientEffectList);
			GlobalVar.getTrainInputDialog().write(getTrainList(true), true);
			GlobalVar.getTrainInputDialog().write(getTrainList(false), false);
			//shashank
			GlobalVar.getMaintInputDialog().writemaintblocks(blockloopList, starttimeList, endtimeList, directionList);
			FileNames.writeFilePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addStation(Station station) {

		stationList.add(station);
	   
	
		Collections.sort(stationList, new SortStation());

		BlockInput blockInput = GlobalVar.getBlockInput();
		blockInput.updateStationNames();
		
		ArrayList<Station> tempstationlist=stationList;
		
		JComboBox<String> stationNameComboBox;
		if (GlobalVar.stationInputDialog != null) {
			stationNameComboBox = GlobalVar.getStationInputDialog().stationNameComboBox;
			stationNameComboBox.removeAllItems();
			for (Station station2 : tempstationlist) {
				stationNameComboBox.addItem(station2.getStationName());
			}
		}
		

		/*
		 * if (GlobalVar.blockInput != null) { stationNameComboBox =
		 * GlobalVar.getBlockInput().stationNameComboBox;
		 * stationNameComboBox.removeAllItems(); for (Station station2 :
		 * stationList) { stationNameComboBox.addItem(station2.stationName); } }
		 */
		if (GlobalVar.trainInputDialog != null) {
			GlobalVar.trainInputDialog.setTimeTableEntryBounds();
		}
	}

	public String[] getStationNames() {
		String[] stationNames = new String[stationList.size()];
		int i = 0;
		for (Station station : stationList) {
			stationNames[i++] = station.getStationName();
		}

		return stationNames;
	}

	public String[] getPrefinalStationNames() {

		String[] stationNames = new String[stationList.size() - 1];
		int i = 0;
		int j = 0;

		for (Station station : stationList) {
			while (j < stationList.size() - 1) {
				stationNames[i++] = station.getStationName();

				j++;
			}
		}

		return stationNames;

	}

	public void removeStation(Station stationToDelete) {
		
		
		//Sneha : before removing station from stationList ensure that corresponding block entries are
		//are removed as well.For that code added up to stationList.remove() function call 
		SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
		
		ArrayList<Loop> temploopList = sectioninputdialog.loopList;
			int foundcount=0x0;
			for (Loop loop : temploopList) {
					
				if( loop.stationName.equals(stationToDelete.stationName))
					{
					foundcount++;
				    }
			}
			
			for (int count=0x0;count< foundcount;count++) {
                    				
				for (Loop loop : temploopList) {
					if(loop.stationName.equals(stationToDelete.stationName)){
					temploopList.remove(loop);
					break;
					}
				}
			}
				
		stationList.remove(stationToDelete);	

		//After station is removed we have to sort the station array list as per start and end kilometer
		//this will change station id so one need to update loop id accordingly.
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
		  

		JComboBox<String> stationNameComboBox;
		if (GlobalVar.stationInputDialog != null) {
			stationNameComboBox = GlobalVar.stationInputDialog.stationNameComboBox;
			stationNameComboBox.removeItem(stationToDelete.getStationName());
		}
		
		
		/*
		 * if (GlobalVar.blockInput != null) { stationNameComboBox =
		 * GlobalVar.getBlockInput().stationNameComboBox;
		 * stationNameComboBox.removeItem(stationToDelete.stationName); }
		 */
		if (GlobalVar.trainInputDialog != null) {
			GlobalVar.trainInputDialog.setTimeTableEntryBounds();
		}
	}

	public void addBlock(Block block) {
		blockList.add(block);
	}

	public void addLoop(Loop loop) {
		loopList.add(loop);
	}

	public Loop getLoopByBlockNo(int blockNo) {
		for (Loop loop : loopList) {
			if (loop.getBlockNo() == blockNo)
				return loop;
		}
		return null;
	}

	public Block getBlockByBlockNo(int blockNo) {
		for (Block block : blockList) {
			if (block.getBlockNo() == blockNo)
				return block;
		}
		return null;
	}

	public void removeBlock(Block block) {
		blockList.remove(block);
	}

	public void removeLoop(Loop loop) {
		loopList.remove(loop);

	}

	public ArrayList<Train> getTrainList(boolean isScheduled) {
		ArrayList<Train> returnTrainList = new ArrayList<Train>();
		for (Train train : trainList) {
			if (train.isScheduled() == isScheduled) {
				returnTrainList.add(train);
			}
		}

		return returnTrainList;
	}

	public static void main(String[] args) {
		GlobalVar.getInputFileInterface();
	}

	public Train getTrainByTrainNumber(int trainNumber) {
		for (Train train : trainList) {
			if (train.getTrainNo() == trainNumber)
				return train;
		}

		return null;
	}

	public void addTrain(Train train) {
		trainList.add(train);
	}

	public void removeTrain(Train train) {
		trainList.remove(train);
	}

	public void updateStationNames() {
		JComboBox<String> stationNameComboBox;
		Collections.sort(stationList, new SortStation());
		String[] stationNames = getStationNames();
		if (GlobalVar.stationInputDialog != null) {
			stationNameComboBox = GlobalVar.stationInputDialog.stationNameComboBox;
			stationNameComboBox.removeAllItems();
			for (int i = 0; i < stationNames.length; i++) {
				stationNameComboBox.addItem(stationNames[i]);
			}

			GlobalVar.stationInputDialog.repaint();
		}

		/*
		 * if (GlobalVar.blockInput != null) { stationNameComboBox =
		 * GlobalVar.blockInput.stationNameComboBox;
		 * stationNameComboBox.removeAllItems(); for (int i = 0; i <
		 * stationNames.length; i++) {
		 * stationNameComboBox.addItem(stationNames[i]); }
		 * 
		 * GlobalVar.blockInput.repaint(); }
		 */
	}

	public Gradient getGradientBySlopeDirection(String slopeString,
			boolean isGradientUp, boolean isGradientlevel) {
		String directionString = "Up";
		if (!isGradientUp && !isGradientlevel)
			directionString = "Down";
		if (isGradientlevel)
			directionString = "Level";

		for (Gradient gradientFormat : gradientFormatList) {
			if (gradientFormat.getGradientValue().equalsIgnoreCase(slopeString)
					&& gradientFormat.getDirection().equalsIgnoreCase(
							directionString))
				return gradientFormat;
		}

		return null;
	}

	public void addGradientFormat(Gradient gradientFormat) {
		gradientFormatList.add(gradientFormat);
	}

	public void removeGradient(Gradient gradientFormat) {
		gradientFormatList.remove(gradientFormat);
	}

	public void addGradientEffect(GradientEffect gradientEffect) {
		gradientEffectList.add(gradientEffect);
	}

	public GradientEffect getGradientEffectBySlopeDirection(String slopeString,
			boolean isGradientUp) {
		String directionString = "Up";
		if (!isGradientUp)
			directionString = "Down";

		for (GradientEffect gradientEffect : gradientEffectList) {
			if (gradientEffect.getGradientValue().equalsIgnoreCase(slopeString)
					&& GlobalVar.getDirectionStringFromDirection(
							gradientEffect.getDirection()).equalsIgnoreCase(
							directionString))
				return gradientEffect;
		}

		return null;
	}

	public void removeGradientEffect(GradientEffect gradientEffect) {
		gradientEffectList.remove(gradientEffect);
	}
	
	
}

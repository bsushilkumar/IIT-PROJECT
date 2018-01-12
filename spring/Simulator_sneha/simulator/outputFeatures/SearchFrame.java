package simulator.outputFeatures;

import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.trainEntities.Train;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import simulator.input.SimulationInstance;
import simulator.scheduler.SimulatorTimeTableEntry;
import simulator.util.Time;

public class SearchFrame extends JFrame {
	GraphFrame graphFrame = null;
	SimulationInstance simulationInstance = null;

	JTextField trainSearchTextField = new JTextField();
	JTextField blockSearchTextField = new JTextField();
	JLabel trainSearchLabel = new JLabel("Train no");
	JLabel blockSearchLabel = new JLabel("Block no");
	JButton trainSearchButton = new JButton("Search");
	JButton blockSearchButton = new JButton("Search");

	JLabel resultLabel = new JLabel("");

	int width = 500;
	int height = 200;

	public KeyListener hideKeyListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				setVisible(false);
				break;
			}
		}
	};

	public KeyListener trainKeyListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				searchTrain();
				break;
			}
		}
	};

	public KeyListener blockKeyListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				searchBlock();
				break;
			}
		}
	};

	public SearchFrame(GraphFrame graphFrame,
			SimulationInstance simulationInstance) {
		this.graphFrame = graphFrame;
		this.simulationInstance = simulationInstance;

		int x = 300;
		int y = 300;

		setBounds(x, y, width, height);

		int x1 = 30, x2 = 130, x3 = 230, labelWidth = 80, textFieldWidth = 80, buttonWidth = 120;
		int y1 = 30, componentHeight = 30, stepSize = 30;

		trainSearchLabel.setBounds(x1, y1, labelWidth, componentHeight);
		trainSearchTextField.setBounds(x2, y1, textFieldWidth, componentHeight);
		trainSearchButton.setBounds(x3, y1, buttonWidth, componentHeight);

		trainSearchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchTrain();
			}
		});

		y1 += stepSize;
		blockSearchLabel.setBounds(x1, y1, labelWidth, componentHeight);
		blockSearchTextField.setBounds(x2, y1, textFieldWidth, componentHeight);
		blockSearchButton.setBounds(x3, y1, buttonWidth, componentHeight);

		blockSearchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchBlock();
			}
		});

		y1 += stepSize;
		resultLabel.setBounds(x1, y1, 200, 30);

		trainSearchTextField.addKeyListener(hideKeyListener);
		trainSearchButton.addKeyListener(hideKeyListener);
		blockSearchTextField.addKeyListener(hideKeyListener);
		blockSearchButton.addKeyListener(hideKeyListener);

		getContentPane().add(trainSearchTextField);
		getContentPane().add(trainSearchLabel);
		getContentPane().add(trainSearchButton);

		getContentPane().add(blockSearchTextField);
		getContentPane().add(blockSearchLabel);
		getContentPane().add(blockSearchButton);

		JPanel jpanel = new JPanel();
		getContentPane().add(jpanel);
		setVisible(true);
	}

	public void searchTrain() {
		int trainNo = Integer.parseInt(trainSearchTextField.getText());
		for (Train train : simulationInstance.getTrainList()) {
			if (train.getTrainNo() == trainNo) {
				TrainData trainDataFrame = new TrainData(train);
				new InterHaltsRunningTime(train,simulationInstance);
				return;
			}
		}

		resultLabel.setText("Train " + trainNo + " not found.");
	}

	public void searchBlock() {
		int blockNo = Integer.parseInt(blockSearchTextField.getText());
		Block block = simulationInstance.getBlockFromBlockNo(blockNo);
		if (block != null) {
			BlockData blockDataFrame = new BlockData(block);
			return;
		}

		resultLabel.setText("Block " + blockNo + " not found.");
	}

}

class InterHaltsRunningTime extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * currentTrain
	 */
	Train currentTrain;
	/**
	 * trainTable
	 */
	JTable trainTable;
	ArrayList<SimulatorTimeTableEntry> interHaltTimes = null;
	ArrayList<ReferenceTableEntry> refTable = null;
	SimulationInstance simulationInstance = null;

	/**
	 * @param trn
	 */
	public InterHaltsRunningTime(Train train,
			final SimulationInstance simulationInstance) {
		super("Time-Table for TrainNo-" + train.getTrainNo());
		currentTrain = train;
		interHaltTimes = currentTrain.getTimeTablesHaltsOnly(simulationInstance);
		refTable = currentTrain.getRefTables();
		this.simulationInstance = simulationInstance;

		trainTable = new JTable(new AbstractTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			String[] columnNames = { "Station", "Arrival Time",
					"Departure Time", "Duration of Stay", "Sch Arr", "Sch Dep",
					"Inc Time", "Cumu Time" };

			public String getColumnName(int col) {
				return columnNames[col].toString();
			}

			public int getRowCount() {
				return interHaltTimes.size();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				int loopNo;
				switch (col) {
				case 0:
					loopNo = interHaltTimes.get(row).getLoopNo();
					Block block = simulationInstance
							.getBlockFromBlockNo(loopNo);
					String stationName = ((Loop) block).getStationName();
					return stationName;
				case 1:
					return Time
							.timeToString(((SimulatorTimeTableEntry) interHaltTimes
									.get(row)).getArrivalTime());
				case 2:
					return Time
							.timeToString(((SimulatorTimeTableEntry) interHaltTimes
									.get(row)).getDepartureTime());
				case 3:
					return Time
							.timeToString(((SimulatorTimeTableEntry) interHaltTimes
									.get(row)).getDepartureTime()
									- ((SimulatorTimeTableEntry) interHaltTimes
											.get(row)).getArrivalTime());

				case 4: {
					loopNo = new Integer(
							((SimulatorTimeTableEntry) interHaltTimes.get(row))
									.getLoopNo());
					int i;
					for (i = 0; i < refTable.size(); i++) {
						if (loopNo == refTable.get(i).getReferenceLoopNo())
							break;
					}

					if (refTable.size() == i)
						return null;
					return Time.timeToString(refTable.get(i)
							.getReferenceArrivalTime());
				}

				case 5: {
					loopNo = new Integer(
							((SimulatorTimeTableEntry) interHaltTimes.get(row))
									.getLoopNo());
					int i;
					for (i = 0; i < refTable.size(); i++) {
						if (loopNo == refTable.get(i).getReferenceLoopNo())
							break;
					}
					if (refTable.size() == i)
						return null;
					return Time.timeToString(refTable.get(i)
							.getReferenceDepartureTime());
				}

				case 6: {
					if (row == 0)
						return null;
					return Time.timeToString(interHaltTimes.get(row)
							.getDepartureTime()
							- interHaltTimes.get(row - 1).getDepartureTime());
				}

				case 7: {

				}
					return Time.timeToString(interHaltTimes.get(row)
							.getDepartureTime()
							- interHaltTimes.get(0).getDepartureTime());
				}

				return null;
			}

		});

		JScrollPane scrollPane = new JScrollPane(trainTable);
		trainTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		getContentPane().add(scrollPane);

		int height = 400;
		if (interHaltTimes.size() > 0)
			height = Math.min(interHaltTimes.size() * 40, 400);
		setBounds(100, 100, 600, height);
		setVisible(true);
	}
}

package simulator.outputFeatures;

import gui.entities.sectionEntities.time.ReferenceTableEntry;
import gui.entities.trainEntities.Train;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import simulator.scheduler.SimulatorTimeTableEntry;
import simulator.util.Time;

/**
 * 
 */
@SuppressWarnings("serial")
public class TrainData extends JFrame {
	/**
	 * currentTrain
	 */
	Train currentTrain;
	/**
	 * trainTable
	 */
	JTable trainTable;
	ArrayList<SimulatorTimeTableEntry> interArray = null;
	ArrayList<ReferenceTableEntry> refTable = null;

	/**
	 * @param trn
	 */
	@SuppressWarnings("deprecation")
	public TrainData(Train trn) {
		super("Time-Table for TrainNo-" + trn.getTrainNo());
		currentTrain = trn;
		interArray = currentTrain.getTimeTables();
		refTable = currentTrain.getRefTables();

		trainTable = new JTable(new AbstractTableModel() {
			String[] columnNames = { "Block No", "Arrival Time",
					"Departure Time", "Duration of Stay", "Sch Arr", "Sch Dep" };

			public String getColumnName(int col) {
				return columnNames[col].toString();
			}

			public int getRowCount() {
				return interArray.size();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return new Integer(((SimulatorTimeTableEntry) interArray
							.get(row)).getLoopNo());
				case 1:
					return Time
							.timeToString(((SimulatorTimeTableEntry) interArray
									.get(row)).getArrivalTime());
				case 2:
					return Time
							.timeToString(((SimulatorTimeTableEntry) interArray
									.get(row)).getDepartureTime());
				case 3:
					return Time
							.timeToString(((SimulatorTimeTableEntry) interArray
									.get(row)).getDepartureTime()
									- ((SimulatorTimeTableEntry) interArray
											.get(row)).getArrivalTime());

				case 4: {
					int loopNo = new Integer(
							((SimulatorTimeTableEntry) interArray.get(row))
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
					int loopNo = new Integer(
							((SimulatorTimeTableEntry) interArray.get(row))
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

				}

				return null;
			}

		});

		JScrollPane scrollPane = new JScrollPane(trainTable);
		trainTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		getContentPane().add(scrollPane);

		int height = 400;
		if (interArray.size() > 0)
			height = Math.min(interArray.size() * 40, 400);
		setBounds(100, 100, 400, height);
		setVisible(true);
	}

}

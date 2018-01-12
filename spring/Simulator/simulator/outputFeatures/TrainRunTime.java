package simulator.outputFeatures;

import gui.entities.sectionEntityList.TrainList;
import gui.entities.trainEntities.Train;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import simulator.util.Time;

/**
 * 
 */
@SuppressWarnings("serial")
public class TrainRunTime extends JFrame {
	/**
	 * currentTrain
	 */
	Train currentTrain;
	/**
	 * trainTable
	 */
	JTable trainTable;

	/**
 * 
 */
	@SuppressWarnings("deprecation")
	public TrainRunTime() {
		
		super("Running times of all trains");
		trainTable = new JTable(new AbstractTableModel() {
			String[] columnNames = { "Train No", "Total Time", "Traversal Time" };
//			TrainList interArray = GlobalVar.getSimulationInstance().getTrainList();
			//TODO
			TrainList interArray = null;

			public String getColumnName(int col) {
				return columnNames[col].toString();
			}

			// public int getRowCount() { return freightSimulator.currTrainNo; }
			public int getRowCount() {
				return interArray.size();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return new Double(((Train) interArray.get(row)).getTrainNo());

				case 1:
					return Time.timeToString(Math
							.round(((Train) interArray.get(row)).getTimeTables()
									.size() > 0 ? ((Train) interArray.get(row))
									.totalTime() : 0.0));
				case 2:
					return Time.timeToString(Math
							.round(((Train) interArray.get(row)).getTimeTables()
									.size() > 0 ? ((Train) interArray.get(row))
									.travelTime() : 0.0));
				}
				return null;
			}

		});

		JScrollPane scrollPane = new JScrollPane(trainTable);
		trainTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		getContentPane().add(scrollPane);
		setBounds(100, 100, 400, 400);
		show();
	}

}

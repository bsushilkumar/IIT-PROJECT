package simulator.outputFeatures;

import gui.entities.sectionEntities.time.IntervalArray;
import gui.entities.sectionEntities.trackEntities.Block;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import simulator.util.Time;

/**
 * Title: Simulation of SimulatorTrain Pathing Description: Copyright: Copyright
 * (c) 2002 Company: IIT
 * 
 * @author Rajesh Naik
 * @version 1.0
 */

@SuppressWarnings("serial")
public class BlockData extends JFrame {
	/**
	 * currentBlock
	 */
	Block currentBlock;
	IntervalArray intervalArray = null;
	/**
	 * blockTable
	 */
	JTable blockTable;

	/**
	 * @param blk
	 *            constructor.
	 */
	public BlockData(Block blk) {
		super("Occupancy Table for " + blk.getBlockName());
		currentBlock = blk;
		intervalArray = currentBlock.getOccupanciesToDisplay();

		blockTable = new JTable(new AbstractTableModel() {
			String[] columnNames = { "SimulatorTrain No", "Arrival Time",
					"Departure Time", "Duration of Stay" };

			public String getColumnName(int col) {
				return columnNames[col].toString();
			}

			public int getRowCount() {
				return intervalArray.size();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return new Integer(intervalArray.get(row).getTrainNo());
				case 1:
					return Time.timeToString(intervalArray.get(row)
							.getStartTime());
				case 2:
					return Time.timeToString(intervalArray.get(row)
							.getEndTime());
				case 3:
					return Time.timeToString(intervalArray.get(row)
							.getEndTime()
							- intervalArray.get(row).getStartTime());
				}

				return null;
			}
		});

		JScrollPane scrollPane = new JScrollPane(blockTable);
		blockTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		getContentPane().add(scrollPane);

		int height = 400;
		if (intervalArray.size() > 0)
			height = Math.min(intervalArray.size() * 40, 400);
		setBounds(100, 100, 400, height);
		setVisible(true);
	}
}

package simulator.input;

import gui.entities.sectionEntities.trackProperties.TinyBlock;

import java.util.Comparator;

/**
 * 
 */

public class SortTinyBlock implements Comparator<TinyBlock> {

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.TinyBlock,
	 *      java.lang.TinyBlock)
	 */
	public int compare(TinyBlock a, TinyBlock b) {

		TinyBlock firstTiny = a;
		TinyBlock secondTiny = b;

		if (firstTiny.getStartMilePost() > secondTiny.getStartMilePost()) {
			return 1;
		}
		if (firstTiny.getStartMilePost() < secondTiny.getStartMilePost()) {
			return -1;
		}

		return 0;
	}
}

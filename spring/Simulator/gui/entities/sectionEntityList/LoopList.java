package gui.entities.sectionEntityList;

import gui.entities.sectionEntities.trackEntities.Loop;

import java.util.ArrayList;

public class LoopList extends ArrayList<Loop> {

	private static final long serialVersionUID = 1L;

	/**
	 * @param blockNo
	 * @return the StationName corresponding to the blockNo
	 */
	public String getStationName(int blockNo) {
		String s = null;
		for (int j = 0; j < this.size(); j++) {
			Loop loop = this.get(j);
			// System.out.println("GlobalVar: getStationName: loop no "+loop.blockNo+
			// " blockNo "+blockNo);
			if (loop.getBlockNo() == blockNo) {
				s = loop.getStationName();
				// System.out.println("GlobalVar: getStationName: stationName "+s);
				return s;
			}
		}

		return s;
	}

}

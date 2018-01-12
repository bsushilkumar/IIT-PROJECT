package gui.entities.sectionEntityList;

import gui.entities.sectionEntities.Station;

import java.util.ArrayList;

public class StationList extends ArrayList<Station>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param strStn
	 *            station's abbreviated name
	 * @return {@link Station} having the abbreviated stationName
	 */
	public Station getStation(String strStn) {
		Station station;
		for (int i = 0; i < this.size(); i++) {
			station = this.get(i);

			if (strStn.equalsIgnoreCase(station.getStationName())) {
				return station;
			}
		}

		return null;
	}

}

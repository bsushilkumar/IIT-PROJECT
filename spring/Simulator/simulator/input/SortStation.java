package simulator.input;

import gui.entities.sectionEntities.Station;

import java.util.Comparator;

/**
 * 
 */

public class SortStation implements Comparator<Station> {

    /** (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Station a, Station b) {
	double c = a.getStartMilePost();
	double d = b.getStartMilePost();

	if (c > d) {
	    return 1;
	}
	if (c < d) {
	    return -1;
	}
	return 0;
    }
}

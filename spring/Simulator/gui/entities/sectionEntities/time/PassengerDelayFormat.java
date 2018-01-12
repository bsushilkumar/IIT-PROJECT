package gui.entities.sectionEntities.time;

/**
 * Class {@link PassengerDelayFormat} Format for the passenger delay
 */
public class PassengerDelayFormat {
	/**
	 * trainPriority
	 */
	double trainPriority;
	/**
	 * percentDelay
	 */
	float percentDelay;
	/**
	 * meanDelay
	 */
	double meanDelay;

	/**
	 * constructor.
	 * 
	 * @param tp
	 *            trainPriority
	 * @param pD
	 *            percentDelay
	 * @param mD
	 *            meanDelay
	 */
	public PassengerDelayFormat(double tp, float pD, double mD) {
		this.trainPriority = tp;
		this.percentDelay = pD;
		this.meanDelay = mD;

	}
}
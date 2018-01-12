package gui.entities.sectionEntities.trackProperties;

import gui.entities.sectionEntities.trackEntities.Block;

/**
 * 
 */
public class SpeedRestriction extends TrackCharacteristic {

	private static final double defaultSpeedLimit = 30;
	/**
	 * speedLimit
	 */
	private double speedLimit = SpeedRestriction.defaultSpeedLimit;

	/**
	 * @param velo
	 * @param b
	 * @param c
	 */
	public SpeedRestriction(double velo, double b, double c) {
		setSpeedLimit(velo);
		if (b != c) {
			setRelativeStartMilePost(Math.min(b, c));
			setRelativeEndMilePost(Math.max(b, c));
		}
	}

	/**
     * 
     */
	public SpeedRestriction() {
	}

	public SpeedRestriction(double speedLimit, double relativeStartMilePost,
			double relativeEndMilePost, Block block) {
		setSpeedLimit(speedLimit);
		setRelativeStartMilePost(Math.min(relativeStartMilePost,
				relativeEndMilePost));
		setRelativeEndMilePost(Math.max(relativeStartMilePost,
				relativeEndMilePost));

	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(double speedLimit) {
		this.speedLimit = speedLimit;
	}

	@Override
	public boolean hasError() {
		// TODO Auto-generated method stub
		return false;
	}

}
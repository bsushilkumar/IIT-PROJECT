package gui.entities.sectionEntities.trackProperties;

import gui.entities.sectionEntities.trackEntities.TrackSegment;

/**
 * 
 */
public class Gradient extends TrackCharacteristic {

	private static final String DEFAULT_GRADIENT_VALUE = "0";

	private static final String GRADIENT_TYPE_LEVEL = "level";

	/**
	 * gradientValue
	 */
	private String gradientValue=Gradient.DEFAULT_GRADIENT_VALUE;
		
	/**
	 * direction
	 */
	private String direction = Gradient.GRADIENT_TYPE_LEVEL;

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public Gradient(String gradientValue, String gradientDirection,
			double relativeStartMilePost, double relativeEndMilePost,
			TrackSegment trackSegment) {
		this.setGradientValue(gradientValue);
		this.setDirection(gradientDirection);
		this.setRelativeStartMilePost(relativeStartMilePost);
		this.setRelativeEndMilePost(relativeEndMilePost);
		this.setTrackSegment(trackSegment);
	}

	public Gradient(String gradientValue, String directionString,
			double startMilePost, double endMilePost) {
		this.gradientValue = gradientValue;
		this.direction = directionString;
		this.relativeStartMilePost = startMilePost;
		this.relativeEndMilePost = endMilePost;
	}
	
	/**
 * 
 */
	public Gradient() {
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getGradientValue() {
		return gradientValue;
	}

	public void setGradientValue(String gradientValue) {
		this.gradientValue = gradientValue;
	}

	@Override
	public boolean hasError() {
		// TODO Auto-generated method stub
		return false;
	}
}
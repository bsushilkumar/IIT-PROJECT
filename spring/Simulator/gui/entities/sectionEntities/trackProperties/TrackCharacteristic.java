package gui.entities.sectionEntities.trackProperties;

import globalVariables.GlobalVariables;
import gui.entities.sectionEntities.Entity;
import gui.entities.sectionEntities.trackEntities.TrackSegment;

public abstract class TrackCharacteristic extends Entity {
	public double relativeStartMilePost;
	protected double relativeEndMilePost;

	protected TrackSegment trackSegment;

	public double getRelativeStartMilePost() {
		return relativeStartMilePost;
	}

	public void setRelativeStartMilePost(double startMilePost) {
		this.relativeStartMilePost = GlobalVariables.roundToThreeDecimals(startMilePost);
	}

	public double getRelativeEndMilePost() {
		return relativeEndMilePost;
	}

	public void setRelativeEndMilePost(double endMilePost) {
		this.relativeEndMilePost = GlobalVariables.roundToThreeDecimals(endMilePost);
	}

	public TrackSegment getTrackSegment() {
		return trackSegment;
	}

	public void setTrackSegment(TrackSegment trackSegment) {
		this.trackSegment = trackSegment;
	}

}

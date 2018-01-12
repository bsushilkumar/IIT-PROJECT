package gui.entities.sectionEntities.trackProperties;

import globalVariables.GlobalVar;
import globalVariables.GlobalVariables;

/**
 * 
 */
public class TinyBlock {
	/**
	 * startMilePost
	 */
	private double startMilePost;
	/**
	 * endMilePost
	 */
	private double endMilePost;
	/**
	 * accelerationChange
	 */
	private double accelerationChange;
	/**
	 * decelerationChange
	 */
	private double decelerationChange;
	/**
	 * maxSpeed
	 */
	private double maxSpeed;
	private double relativeStartMilePost;
	private double relativeEndMilePost;

	/**
	 * constructor.
	 */
	public TinyBlock() {
		setStartMilePost(0);
		setEndMilePost(0);
		setAccelerationChange(0);
		setDecelerationChange(0);
		setMaxSpeed(0);
	}

	/**
	 * @param startMilePost
	 * @param endMilePost
	 * @param acChange
	 * @param deChange
	 * @param maxSpeed
	 */
	public TinyBlock(double startMilePost, double endMilePost, double acChange,
			double deChange, double maxSpeed) {
		this.setStartMilePost(Math.min(startMilePost, endMilePost));
		this.setEndMilePost(Math.max(startMilePost, endMilePost));

		this.setAccelerationChange(acChange);
		this.setDecelerationChange(deChange);
		this.setMaxSpeed(maxSpeed);
	}

	public TinyBlock(double length) {
		this.setRelativeStartMilePost(0);
		this.setRelativeEndMilePost(length);

	}

	public TinyBlock(TinyBlock tinyBlock) {
		// System.out.println("TinyBlock: constructor "
		// + tinyBlockFormat.relativeStartMilePost + " "
		// + tinyBlockFormat.relativeEndMilePost);
		this.setRelativeEndMilePost(tinyBlock.getRelativeEndMilePost());
		this.setRelativeStartMilePost(tinyBlock.getRelativeStartMilePost());
		this.setAccelerationChange(tinyBlock.getAccelerationChange());
		this.setDecelerationChange(tinyBlock.getDecelerationChange());
		this.setMaxSpeed(tinyBlock.getMaxSpeed());
	}

	public TinyBlock(double startMilePost, double endMilePost,
			double accelerationChange, double decelerationChange,
			double maxSpeed, boolean relativeMilePosts) {
		if (!relativeMilePosts) {
			relativeStartMilePost = Math.min(startMilePost, endMilePost);
			relativeEndMilePost = Math.max(startMilePost, endMilePost);
		}
		this.setAccelerationChange(accelerationChange);
		this.setDecelerationChange(decelerationChange);
		this.setMaxSpeed(maxSpeed);
	}

	public double getRelativeStartMilePost() {
		return relativeStartMilePost;
	}

	public double getRelativeEndMilePost() {
		return relativeEndMilePost;
	}

	public void setRelativeStartMilePost(double relativeStartMilePost) {
		this.relativeStartMilePost = GlobalVariables
				.roundToThreeDecimals(relativeStartMilePost);
	}

	public void setRelativeEndMilePost(double relativeEndMilePost) {
		this.relativeEndMilePost = GlobalVariables
				.roundToThreeDecimals(relativeEndMilePost);
	}

	public double getStartMilePost() {
		return startMilePost;
	}

	public void setStartMilePost(double startMilePost) {
		this.startMilePost = GlobalVariables
				.roundToThreeDecimals(startMilePost);
	}

	public double getEndMilePost() {
		return endMilePost;
	}

	public void setEndMilePost(double endMilePost) {
		this.endMilePost = GlobalVariables.roundToThreeDecimals(endMilePost);
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public double getDecelerationChange() {
		return decelerationChange;
	}

	public void setDecelerationChange(double decelerationChange) {
		this.decelerationChange = decelerationChange;
	}

	public double getAccelerationChange() {
		return accelerationChange;
	}

	public void setAccelerationChange(double accelerationChange) {
		this.accelerationChange = accelerationChange;
	}

	public TinyBlockList split(double extendedEndMilePost) {
		TinyBlockList splitTinyBlocks = new TinyBlockList();
		TinyBlock firstSplitTinyBlock = new TinyBlock(startMilePost,
				extendedEndMilePost, accelerationChange, decelerationChange,
				maxSpeed);
		TinyBlock secondSplitTinyBlock = new TinyBlock(extendedEndMilePost,
				endMilePost, accelerationChange, decelerationChange, maxSpeed);
		splitTinyBlocks.add(firstSplitTinyBlock);
		splitTinyBlocks.add(secondSplitTinyBlock);
		return splitTinyBlocks;
	}

	public double getStartMilePost(int trainDirection) {
		if (trainDirection == GlobalVar.UP_DIRECTION) {
			return startMilePost;
		} else {
			return endMilePost;
		}
	}

	public double getEndMilePost(int trainDirection) {
		if (trainDirection == GlobalVar.UP_DIRECTION) {
			return endMilePost;
		} else {
			return startMilePost;
		}
	}
}
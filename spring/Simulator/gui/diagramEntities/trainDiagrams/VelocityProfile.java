package gui.diagramEntities.trainDiagrams;

import globalVariables.GlobalVar;
import gui.entities.sectionEntities.trackEntities.TrackSegment;

public class VelocityProfile {
	private boolean drawMe = true;
	// private boolean linkFlag = false;
	// private Link nextLink = null;
	// private BlockDiagram blockDiagram = null;
	private double arrivalTime;
	private double departureTime;
	private double startMilePost;
	private double endMilePost;
	private double acceleration;
	private double totalTime;

	private double startVelocity;
	/**
	 * endVelocity
	 */
	private double endVelocity;

	// private int blockId;
	private TrackSegment trackSegment = null;

	public void setDraw(boolean flag) {
		this.drawMe = flag;
	}

	public boolean getDraw() {
		return this.drawMe;
	}

	// public void setLinkFlag(boolean flag) {
	// this.linkFlag = flag;
	// }
	//
	// public void setNextLink(Link nextLink) {
	// this.nextLink = nextLink;
	// }
	//
	// public boolean getLinkFlag() {
	// return linkFlag;
	// }
	//
	// public Link getNextLink() {
	// return this.nextLink;
	// }

	/**
	 * @param startLen
	 * @param endLen
	 * @param startVelo
	 * @param finalVelo
	 * @param totTime
	 * @param accel
	 */
	public VelocityProfile(double startLen, double endLen, double startVelo,
			double finalVelo, double totTime, double accel) {
		setStartMilePost(startLen);
		setEndMilePost(endLen);
		setStartVelocity(startVelo);
		setEndVelocity(finalVelo);
		setAcceleration(accel);
		setTotalTime(totTime);
	}

	public VelocityProfile(VelocityProfile simulatorVelocityProfile) {
		this.setTotalTime(simulatorVelocityProfile.getTotalTime());
		this.setStartMilePost(simulatorVelocityProfile.getStartMilePost());
		this.setEndMilePost(simulatorVelocityProfile.getEndMilePost());
		this.setAcceleration(simulatorVelocityProfile.getAcceleration());
		this.setStartVelocity(simulatorVelocityProfile.getStartVelocity());
		this.setEndVelocity(simulatorVelocityProfile.getEndVelocity());
		this.setArrivalTime(simulatorVelocityProfile.arrivalTime);
		this.setDepartureTime(simulatorVelocityProfile.departureTime);

		// int blockId = simulatorVelocityProfile.getBlockId();
		TrackSegment trackSegment = simulatorVelocityProfile.getTrackSegment();
		// this.setBlockDiagram(ManagerGroup.blockManager
		// .getDiagramFromId(blockId));
		// this.setLinkFlag(simulatorVelocityProfile.getLinkFlag());
		// this.setNextLink(simulatorVelocityProfile.getNextLink());
		this.setDraw(simulatorVelocityProfile.getDraw());
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public double getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(double departureTime) {
		this.departureTime = departureTime;
	}

	public double getStartMilePost() {
		return startMilePost;
	}

	public void setStartMilePost(double startMilePost) {
		this.startMilePost = startMilePost;
	}

	public double getEndMilePost() {
		return endMilePost;
	}

	public void setEndMilePost(double endMilePost) {
		this.endMilePost = endMilePost;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getStartVelocity() {
		return startVelocity;
	}

	public void setStartVelocity(double startVelocity) {
		this.startVelocity = startVelocity;
	}

	public double getEndVelocity() {
		return endVelocity;
	}

	public void setEndVelocity(double endVelocity) {
		this.endVelocity = endVelocity;
	}

	public double getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}

	// public BlockDiagram getBlockDiagram() {
	// return blockDiagram;
	// }
	//
	// public void setBlockDiagram(BlockDiagram blockDiagram) {
	// this.blockDiagram = blockDiagram;
	// }

	public void print(int trainDirection) {
		if (trainDirection == GlobalVar.UP_DIRECTION) {
			System.out.println("startMilePost : " + getStartMilePost()
					+ " endMilePost " + getEndMilePost() + " time "
					+ getTotalTime());
			System.out.println(" startVelocity " + getStartVelocity()
					+ " endVelocity " + getEndVelocity());
		} else if (trainDirection == GlobalVar.DOWN_DIRECTION) {
			System.out.println("startMilePost : " + getEndMilePost()
					+ " endMilePost " + getStartMilePost() + " time "
					+ getTotalTime());
			System.out.println(" startVelocity " + getEndVelocity()
					+ " endVelocity " + getStartVelocity());
		}
	}

	public double getVelocityFromMilepost(double milePost) {
		double s = this.getStartMilePost();
		double e = this.getEndMilePost();
		double m = milePost;
		double sv = this.getStartVelocity();
		double ev = this.getEndVelocity();

		if (s <= m && m <= e)
			return (m - s) / (e - s) * (ev - sv) + sv;
		return -100;
	}

	public boolean containsMilePost(double milePost) {
		if ((this.getStartMilePost() <= milePost && milePost <= this
				.getEndMilePost())
				|| (this.getStartMilePost() >= milePost && milePost >= this
						.getEndMilePost()))
			return true;
		else
			return false;
	}

	// public void setBlockId(int blockId) {
	// this.blockId = blockId;
	// }
	//
	// public int getBlockId() {
	// return blockId;
	// }

	public double getStartMilePost(int trainDirection) {
		return trainDirection == GlobalVar.UP_DIRECTION ? startMilePost
				: endMilePost;
	}

	public double getEndMilePost(int trainDirection) {
		return trainDirection == GlobalVar.UP_DIRECTION ? endMilePost
				: startMilePost;
	}

	public TrackSegment getTrackSegment() {
		return trackSegment;
	}

	public void setTrackSegment(TrackSegment trackSegment) {
		this.trackSegment = trackSegment;
	}
}

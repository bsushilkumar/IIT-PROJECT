package simulator.scheduler;

import simulator.velocityProfileCalculation.VelocityProfileArray;

/**
 * Title: Simulation of SimulatorTrain Pathing Description: Copyright: Copyright
 * (c) 2002 Company: IIT
 * 
 * @author Rajesh Naik
 * @version 1.0
 */

public class RunTimeReturn {
	/**
	 * totalTime
	 */
	double totalTime;
	/**
	 * endVelocity
	 */
	double endVelocity;
	/**
	 * velocityProfileArray
	 */
	private VelocityProfileArray blockVelocityProfileArray;
	private VelocityProfileArray previousLinkVelocityProfileArray;

	/**
	 * @param time
	 * @param velo
	 * @param veloProfArray
	 */
	public RunTimeReturn(double totalTime, double endVelocity,
			VelocityProfileArray blockVelocityProfileArray,
			VelocityProfileArray previousLinkVelocityProfileArray) {
		this.totalTime = totalTime;
		this.endVelocity = endVelocity;
		this.blockVelocityProfileArray = blockVelocityProfileArray;
		this.previousLinkVelocityProfileArray = previousLinkVelocityProfileArray;
	}

	public VelocityProfileArray getBlockVelocityProfileArray() {
		return blockVelocityProfileArray;
	}

	public void setBlockVelocityProfileArray(
			VelocityProfileArray blockVelocityProfileArray) {
		this.blockVelocityProfileArray = blockVelocityProfileArray;
	}

	public VelocityProfileArray getPreviousLinkVelocityProfileArray() {
		return previousLinkVelocityProfileArray;
	}

	public void setPreviousLinkVelocityProfileArray(
			VelocityProfileArray linkVelocityProfileArray) {
		this.previousLinkVelocityProfileArray = linkVelocityProfileArray;
	}

	public VelocityProfileArray getVelocityProfileArray() {
		VelocityProfileArray velocityProfileArray = new VelocityProfileArray();
		if (previousLinkVelocityProfileArray != null)
			velocityProfileArray.addAll(previousLinkVelocityProfileArray);
		if (blockVelocityProfileArray != null)
			velocityProfileArray.addAll(blockVelocityProfileArray);
		return velocityProfileArray;
	}
}
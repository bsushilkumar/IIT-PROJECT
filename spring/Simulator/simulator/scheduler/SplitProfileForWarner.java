package simulator.scheduler;

import gui.entities.sectionEntities.trackEntities.Block;
import simulator.velocityProfileCalculation.VelocityProfileArray;

/**
 * @author devendra This class is used to create velocityProfile for warner
 *         distance and the remaining part of the major block.
 */
public class SplitProfileForWarner {
	/**
	 * warnerProfile velocity profile of the warner block
	 */
	public VelocityProfileArray warnerProfile;
	/**
	 * majorProfile velocity profile of the major block of the big block
	 */
	public VelocityProfileArray majorProfile;
	/**
	 * originalProfile original velocity profile of the block
	 */
	public VelocityProfileArray originalProfile;
	/**
	 * warnerBlockStartVelocity
	 */
	public double warnerBlockStartVelocity;
	/**
	 * largeBlock
	 */
	public Block largeBlock;
	
	/**
	 * 
	 */
	public SplitProfileForWarner(){
		warnerProfile=null;
		majorProfile=null;
		originalProfile=null;
		warnerBlockStartVelocity=0;
		largeBlock=null;
	}
}

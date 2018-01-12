package gui.entities.sectionEntities.trackProperties;

import globalVariables.GlobalVar;
import globalVariables.GlobalVariables;
import gui.entities.sectionEntities.trackEntities.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import simulator.input.SimulationInstance;
import simulator.util.Debug;

/**
 * Tiny Block.
 */
public class TinyBlockList extends ArrayList<TinyBlock> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 */
	public TinyBlockList() {
	}

	/**
	 * END OF THE FUNCTION CREATINY
	 * 
	 * @param distance
	 * @return {@link ArrayList}
	 * **/

	public ArrayList<TinyBlockList> split(double distance) {
		Debug.print(" I am in tiny block split  function ");
		ArrayList<TinyBlockList> returnArray = new ArrayList<TinyBlockList>();
		TinyBlockList tinyStart, tinyEnd;
		tinyStart = new TinyBlockList();
		tinyEnd = new TinyBlockList();
		double currEndMileP, currStartMileP;
		TinyBlock tinyBlock;

		for (int i = 0; i < this.size(); i++) {
			tinyBlock = this.get(i);

			currStartMileP = tinyBlock.getStartMilePost();
			currEndMileP = tinyBlock.getEndMilePost();

			if (currStartMileP < distance) {
				if (currEndMileP <= distance) {
					tinyStart.add(tinyBlock);
				} else {
					tinyStart.add(new TinyBlock(tinyBlock.getStartMilePost(),
							distance, tinyBlock.getAccelerationChange(),
							tinyBlock.getDecelerationChange(), tinyBlock
									.getMaxSpeed()));
					tinyEnd.add(new TinyBlock(distance, tinyBlock
							.getEndMilePost(), tinyBlock
							.getAccelerationChange(), tinyBlock
							.getDecelerationChange(), tinyBlock.getMaxSpeed()));
				}
			} else {
				tinyEnd.add(this.get(i));
			}
		}

		returnArray.add(tinyStart);
		returnArray.add(tinyEnd);

		Debug.print("REturinign from split bloc kfunction ");
		for (int i = 0; i < returnArray.size(); i++) {
			Debug.print(" " + returnArray.get(i));
		}

		return returnArray;
	}

	/**
	 * @param tinyBlock
	 */
	public boolean add(TinyBlock tinyBlock) {
		return super.add(tinyBlock);
	}

	/**
	 * @param a
	 * @param b
	 * @return {@link TinyBlock}
	 */
	public TinyBlock returnTinyBlockArray(double a, double b) {
		Debug.print("I am in return tiny " + a + "     ==   " + b);

		TinyBlock tui = new TinyBlock();
		for (int i = 0; i < this.size(); i++) {
			TinyBlock ti = this.get(i);

			Debug.print(" tiny block properties are " + ti.getStartMilePost()
					+ "  **  " + ti.getEndMilePost());
			if (((a >= ti.getStartMilePost()) && (b <= ti.getEndMilePost()))
					|| (((ti.getStartMilePost() >= a) && (ti.getEndMilePost() <= b)))
					|| (((ti.getStartMilePost() >= a)
							&& (ti.getEndMilePost() >= b) && (ti
							.getStartMilePost() < b)))
					|| ((((ti.getStartMilePost() <= a)
							&& (ti.getEndMilePost() <= b) && (ti
							.getEndMilePost() > a))))) {
				Debug.print("case  1");
				tui = ti;

			}
		}
		Debug.print(" Returining  tiny block " + tui.getStartMilePost()
				+ "  __ " + tui.getEndMilePost());
		return tui;
	}

	public static TinyBlockList createTinyBlockFormats(Block block,
			ArrayList<Gradient> gradientFormatList,
			SimulationInstance simulationInstance) {

		TinyBlockList tinyBlockFormats = new TinyBlockList();

		TreeSet<Double> sortedMilePosts = getDistinctAndSortedMilePostsArray(
				block, gradientFormatList, simulationInstance);

		Iterator<Double> startMilePostIterator = sortedMilePosts.iterator();
		Iterator<Double> endMilePostIterator = sortedMilePosts.iterator();

		try {
			endMilePostIterator.next();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Distinct mileposts on " + block.getBlockName()
					+ " are less than 2");
		}

		for (; endMilePostIterator.hasNext();) {

			double startMilePost = startMilePostIterator.next();
			double endMilePost = endMilePostIterator.next();

			Gradient gradient = block.returnGradientFormat(startMilePost,
					endMilePost);

			String gradientValue = "", direction = "LEVEL";
			if (gradient != null) {
				gradientValue = gradient.getGradientValue();
				direction = gradient.getDirection();
			}

			double maxSpeed = block.returnMaxSpeedBetweenMilePosts(
					startMilePost, endMilePost);

			int directionInt = GlobalVar.getDirectionIntFromString(direction);

			double accelerationChange = 0, decelerationChange = 0;
			if (!direction.equalsIgnoreCase("LEVEL")) {
				accelerationChange = GradientEffect.returnAcceleration(
						gradientValue, directionInt);
				decelerationChange = GradientEffect.returnDeceleration(
						gradientValue, directionInt);
			}

			Debug.info("TinyBlockList: creatiny: adding " + startMilePost
					+ "   " + endMilePost + "   " + accelerationChange + "   "
					+ decelerationChange + "   " + maxSpeed);
			tinyBlockFormats.add(new TinyBlock(startMilePost, endMilePost,
					accelerationChange, decelerationChange, maxSpeed, false));
		}

		Debug.info("createTinyBlockFormats: " + block.getBlockName() + " has "
				+ tinyBlockFormats.size() + " tinyBlockFormats");

		return tinyBlockFormats;
	}

	private static TreeSet<Double> getDistinctAndSortedMilePostsArray(
			Block block, ArrayList<Gradient> gradientFormatList,
			SimulationInstance simulationInstance) {
		ArrayList<Gradient> gradientArrayList = block.getGradientList();
		ArrayList<SpeedRestriction> speedRestrictionArrayList = block
				.getSpeedRestrictionList();
		// ArrayList<Link> allLinksForBlock =
		// simulationInstance.getAllLinksForBlock(block);
		TreeSet<Double> linkLocationsOnBlock = simulationInstance
				.getLinkLocationsOnBlock(block);

		TreeSet<Double> locationsOnBlock = new TreeSet<Double>();
		double locationOnBlock;

		for (int i = 0; i < gradientFormatList.size(); i++) {
			Gradient gradient = gradientFormatList.get(i);
			double start = gradient.getRelativeStartMilePost();
			double end = gradient.getRelativeEndMilePost();
			double blockStart = block.getStartMilePost();
			double blockEnd = block.getEndMilePost();
			if (blockStart <= start && end <= blockEnd) {
				gradientArrayList.add(gradient);
			}
		}

		for (int i = 0; i < gradientArrayList.size(); i++) {
			locationOnBlock = ((gradientArrayList.get(i))
					.getRelativeStartMilePost());
			locationOnBlock = GlobalVariables
					.roundToThreeDecimals(locationOnBlock);
			locationsOnBlock.add(locationOnBlock);

			locationOnBlock = ((gradientArrayList.get(i))
					.getRelativeEndMilePost());
			locationOnBlock = GlobalVariables
					.roundToThreeDecimals(locationOnBlock);
			locationsOnBlock.add(locationOnBlock);
		}

		for (int i = 0; i < speedRestrictionArrayList.size(); i++) {
			locationOnBlock = speedRestrictionArrayList.get(i)
					.getRelativeStartMilePost();
			locationOnBlock = GlobalVariables
					.roundToThreeDecimals(locationOnBlock);
			locationsOnBlock.add(locationOnBlock);

			locationOnBlock = speedRestrictionArrayList.get(i)
					.getRelativeEndMilePost();
			locationOnBlock = GlobalVariables
					.roundToThreeDecimals(locationOnBlock);
			locationsOnBlock.add(locationOnBlock);
		}

		locationsOnBlock.addAll(linkLocationsOnBlock);

		// relative mileposts of startMilePost and endMilePost
		locationsOnBlock.add(0D);
		locationsOnBlock.add(block.getLength());

		return locationsOnBlock;
	}

	public TinyBlockList getTinyBlockListBetweenMilePosts(
			double startTinyBlockMilePost, double endTinyBlockMilePost) {

		double searchStart = Math.min(startTinyBlockMilePost,
				endTinyBlockMilePost);
		double searchEnd = Math.max(startTinyBlockMilePost,
				endTinyBlockMilePost);

		TinyBlockList newTinyBlockFormats = new TinyBlockList();
		for (TinyBlock tinyBlock : this) {
			double start = tinyBlock.getRelativeStartMilePost();
			double end = tinyBlock.getRelativeEndMilePost();

			if (end <= searchStart || searchEnd <= start)
				continue;

			TinyBlock newTinyBlockFormat = new TinyBlock();
			double newStart = Math.max(searchStart, start);
			double newEnd = Math.min(searchEnd, end);
			double accelerationChange = tinyBlock.getAccelerationChange();
			double decelerationChange = tinyBlock.getDecelerationChange();
			double maxSpeed = tinyBlock.getMaxSpeed();

			newTinyBlockFormat.setRelativeStartMilePost(newStart);
			newTinyBlockFormat.setRelativeEndMilePost(newEnd);
			newTinyBlockFormat.setAccelerationChange(accelerationChange);
			newTinyBlockFormat.setDecelerationChange(decelerationChange);
			newTinyBlockFormat.setMaxSpeed(maxSpeed);

			newTinyBlockFormats.add(newTinyBlockFormat);
			Debug.fine("getTinyBlockListBetweenMilePosts: newTinyBlockFormat start "
					+ newStart + " end " + newEnd);
		}

		return newTinyBlockFormats;
	}

	public TinyBlockList getAWSTinyBlockList(double startTinyBlockMilePost,
			double endTinyBlockMilePost, int trainDirection) {
		double length = Math.abs(endTinyBlockMilePost - startTinyBlockMilePost);
		length = GlobalVariables.roundToThreeDecimals(length);

		double milepost1 = startTinyBlockMilePost;
		double milepost2 = endTinyBlockMilePost;

		if (trainDirection == GlobalVar.UP_DIRECTION) {
			// do nothing
		} else {// down direction
			startTinyBlockMilePost = milepost2;
			endTinyBlockMilePost = milepost1;
		}

		TinyBlockList entryOnYellowTinyBlockList, approachRedTinyBlockList;
		double midSearchMilePost;

		if (length >= AutomaticWarningSystem.minimumDesiredBlockLength) {
			midSearchMilePost = startTinyBlockMilePost + trainDirection
					* AutomaticWarningSystem.entryOnYellowDistance;
		} else if (length >= AutomaticWarningSystem.approachRedDistance) {
			midSearchMilePost = endTinyBlockMilePost - trainDirection
					* AutomaticWarningSystem.approachRedDistance;
		} else {
			midSearchMilePost = startTinyBlockMilePost;
		}

		entryOnYellowTinyBlockList = this.getTinyBlockListBetweenMilePosts(
				startTinyBlockMilePost, midSearchMilePost);
		approachRedTinyBlockList = this.getTinyBlockListBetweenMilePosts(
				midSearchMilePost, endTinyBlockMilePost);

		for (TinyBlock tinyBlock : entryOnYellowTinyBlockList) {
			double speedLimit = Math.min(tinyBlock.getMaxSpeed(),
					AutomaticWarningSystem.entryOnYellowSpeedLimit);
			tinyBlock.setMaxSpeed(speedLimit);
		}

		for (TinyBlock tinyBlock : approachRedTinyBlockList) {
			double speedLimit = Math.min(tinyBlock.getMaxSpeed(),
					AutomaticWarningSystem.approachRedSpeedLimit);
			tinyBlock.setMaxSpeed(speedLimit);
		}

		this.clear();
		if (trainDirection == GlobalVar.UP_DIRECTION) {
			this.addAll(entryOnYellowTinyBlockList);
			this.addAll(approachRedTinyBlockList);
		} else {
			this.addAll(approachRedTinyBlockList);
			this.addAll(entryOnYellowTinyBlockList);
		}

		return this;
	}

}
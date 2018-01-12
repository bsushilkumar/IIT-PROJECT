package gui.entities.sectionEntityList;

import globalVariables.GlobalVar;
import globalVariables.GlobalVariables;
import gui.entities.sectionEntities.signal.SignalFailure;
import gui.entities.sectionEntities.time.Interval;
import gui.entities.sectionEntities.time.IntervalArray;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Link;
import gui.entities.sectionEntities.trackProperties.Gradient;
import gui.entities.sectionEntities.trackProperties.TinyBlock;
import gui.entities.sectionEntities.trackProperties.TinyBlockList;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TreeSet;

import simulator.input.SimulationInstance;
import simulator.scheduler.HashBlockEntry;
import simulator.util.Debug;

public class HashBlockTable extends Hashtable<Integer, HashBlockEntry> {

	private static final long serialVersionUID = 1L;

	/**
	 * @param distance
	 * @return {@link LinkList} the array of blocks which are longer than
	 *         distance.
	 */
	public BlockList getBlock(double distance) {
		BlockList blkArray = new BlockList();

		for (Enumeration<HashBlockEntry> e = this.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hbEntry = (HashBlockEntry) e.nextElement();
			Block currBlock = hbEntry.getBlock();

			if ((currBlock.getStartMilePost() <= distance)
					&& (currBlock.getEndMilePost() >= distance)) {
				blkArray.add(currBlock);
			}
		}
		return blkArray;
	}

	public Block getBlockFromBlockNo(int blockNo) {
		HashBlockEntry hbEntry = this.get(blockNo);
		if (hbEntry == null) {
			// System.out.println("Hbentry is null for "+blockNo);
			return null;

		} else
			return hbEntry.getBlock();
	}

	/**
	 * @param a
	 * @param b
	 * @return new block array
	 */
	public ArrayList<Block> returnBlockArrayNew(double a, double b) {
		Debug.print("GlobalVar: returnBlockArrayNew: In the for loop of ret_block_array of Global Var simstart");
		Debug.print("GlobalVar: returnBlockArrayNew: values passed here are  "
				+ a + "   " + b + "   ");
		ArrayList<Block> blockList = new ArrayList<Block>();

		for (Enumeration<HashBlockEntry> e = this.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hbEntry = e.nextElement();
			Block hashBlock = hbEntry.getBlock();

			double startMilePost = hashBlock.getStartMilePost();
			double endMilePost = hashBlock.getEndMilePost();

			Debug.print("GlobalVar: returnBlockArrayNew:  Blk no is   "
					+ hashBlock.getBlockNo() + "  " + startMilePost + "    "
					+ endMilePost);
			if (((startMilePost >= a) && (endMilePost <= b))
					|| ((startMilePost <= a) && (endMilePost >= b))
					|| ((startMilePost >= a) && (endMilePost >= b) && (startMilePost < b))
					|| (((startMilePost <= a) && (endMilePost <= b) && (endMilePost > a)))) {
				Debug.print("GlobalVar: returnBlockArrayNew: this block no ahbjdbj  is "
						+ hashBlock.getBlockNo()
						+ "  "
						+ startMilePost
						+ "  "
						+ endMilePost);
				blockList.add(hashBlock);
			}

		}
		return blockList;
	}

	public void createTinyBlockFormatsForBlocks(
			SimulationInstance simulationInstance, ArrayList<Gradient> gradientFormatList) {
		for (Enumeration<HashBlockEntry> globalHashBlocks = this.elements(); globalHashBlocks
				.hasMoreElements();) {

			HashBlockEntry hbEntry = globalHashBlocks.nextElement();
			Block block = hbEntry.getBlock();
			TinyBlockList tinyBlockFormats = TinyBlockList
					.createTinyBlockFormats(block, gradientFormatList, simulationInstance);
			block.setTinyBlockList(tinyBlockFormats);
		}

	}

	public void convertLinks(SimulationInstance simulationInstance) {
		for (Enumeration<HashBlockEntry> e = this.elements(); e
				.hasMoreElements();) {

			HashBlockEntry hbEntry = e.nextElement();
			Block currBlock = hbEntry.getBlock();
			// System.out.println(currBlock.getBlockName()
			// + " has nextlinks and previouslinks "
			// + currBlock.getNextLinks().size() + " "
			// + currBlock.getPreviousLinks().size());

			for (int i = 0; i < hbEntry.getUpLinks().size(); i++) {
				Link link = hbEntry.getUpLinks().get(i);
				link.convert(GlobalVar.UP_DIRECTION, simulationInstance);
				currBlock.getNextLinks().add(link);
			}
			// System.out.println("Reached here");

			Debug.print("ReadSection: convertLinks: ");
			for (int i = 0; i < hbEntry.getDownLinks().size(); i++) {
				Link link = hbEntry.getDownLinks().get(i);
				link.convert(GlobalVar.DOWN_DIRECTION, simulationInstance);
				currBlock.getPreviousLinks().add(link);
			}

		}

	}

	public void outputBlockOccupancies() {
		Debug.print("\n Block Occupancy \n Block No : total time occupied");
		for (Enumeration<HashBlockEntry> e = this.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hbEntry = e.nextElement();
			Block currBlock = hbEntry.getBlock();

			System.out.println(currBlock.getClass().toString() + " "
					+ currBlock.getBlockNo() + " "
					+ currBlock.getOccupy().totalInterval());

		}

	}

	public LinkList getAllLinksForBlock(Block block) {
		LinkList linkList = new LinkList();
		// System.out.println("getAllLinksForBlock: block " + block.blockNo);
		// System.out.println("hashblocksize: "+GlobalVar.hashBlock.size());
		for (Enumeration<HashBlockEntry> e = this.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hashBlockEntry = e.nextElement();
			// System.out.println("hashblock: "+hashBlockEntry.block.blockNo);
			for (Link link : hashBlockEntry.getUpLinks()) {
				// System.out.println("uplinks: linkNext " +
				// link.getNextBlockNo()
				// + " linkPrevious " + link.getPreviousBlockNo());
				if (link.getNextBlockNo() == block.getBlockNo()
						|| block.getBlockNo() == link.getPreviousBlockNo())
					linkList.add(link);
			}

			// System.out.println("uplinks ended");
			for (Link link : hashBlockEntry.getDownLinks()) {
				// System.out.println("downlinks: linkNext "+link.getNextBlockNo()+" linkPrevious "+link.getPreviousBlockNo());
				if (link.getNextBlockNo() == block.getBlockNo()
						|| block.getBlockNo() == link.getPreviousBlockNo())
					linkList.add(link);
			}
			// System.out.println("downlinks ended");

		}

		return linkList;
	}

	public void setSignalFailFlags() {
		Block bi = new Block();
		for (Enumeration<HashBlockEntry> e = this.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hbEntry = e.nextElement();
			bi = hbEntry.getBlock();
			bi.setSignalFailFlag(0);
		}
	}

	public BlockList getBlockListByTime(int trainNo,
			double simulationCurrentTime) {
		BlockList blockList = new BlockList();
		for (Enumeration<HashBlockEntry> e = this.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hbEntry = (HashBlockEntry) e.nextElement();
			Block currBlock = hbEntry.getBlock();
			IntervalArray interArray = currBlock.getOccupy();

			for (int i = 0; i < interArray.size(); i++) {
				Interval interval = interArray.get(i);
				if (interval.getTrainNo() == trainNo
						&& interval.getStartTime() <= simulationCurrentTime
						&& interval.getEndTime() >= simulationCurrentTime) {
					blockList.add(currBlock);
					// System.out.println("Not Here");
				}
				// System.out.println(outputTrain.getTrainNo()
				// +" y "+interval.getTrainNo());
			}
		}

		return blockList;
	}

	public void initiateSignalFailureParameters(
			SimulationInstance simulationInstance) {
		if (simulationInstance.simulationType.equalsIgnoreCase("SignalFailure")) {
			TinyBlock s = new TinyBlock();

			for (Enumeration<HashBlockEntry> e = this.elements(); e
					.hasMoreElements();) {
				HashBlockEntry hbEntry = e.nextElement();
				Block block = hbEntry.getBlock();
				double startMilePost = block.getStartMilePost();
				double endMilePost = block.getEndMilePost();

				s = (new TinyBlock(startMilePost, startMilePost + 0.001, 0, 0,
						0.001));
				block.getSfsr().add(s);
				s = (new TinyBlock(startMilePost + 0.001, endMilePost, 0, 0,
						0.25));
				block.getSfsr().add(s);

				s = (new TinyBlock(startMilePost, startMilePost + 0.001, 0, 0,
						0.0005));
				block.getNightsfsr().add(s);
				s = (new TinyBlock(startMilePost + 0.001, endMilePost, 0, 0,
						0.25));
				block.getNightsfsr().add(s);
			}

			Debug.print("start: I am in signal failure mode ");
			SignalFailureFormatList signalFailureFormatList = new SignalFailureFormatList();
			try {
				signalFailureFormatList = SignalFailure.readSignalFailure();
			} catch (Exception e) {
				Debug.print("start: ****************      Could not read Signal_Failure file  ********** ");
			}

			Debug.print("start: Signal Failure size is "
					+ signalFailureFormatList.size());
			for (int i = 0; i < signalFailureFormatList.size(); i++) {
				Debug.print("start: value is   "
						+ (signalFailureFormatList.get(i)).getBlockNo() + "   "
						+ signalFailureFormatList.get(i).getStart() + "   "
						+ signalFailureFormatList.get(i).getEnd());
				for (Enumeration e = this.elements(); e.hasMoreElements();) {
					HashBlockEntry hbEntry = (HashBlockEntry) e.nextElement();
					Block block = hbEntry.getBlock();
					if (block.getBlockNo() == signalFailureFormatList.get(i)
							.getBlockNo()) {
						block.getSignalFailure().add(
								signalFailureFormatList.get(i));
					}
				}
			}

		}
	}

	public TreeSet<Double> getLinkLocationsOnBlock(Block block) {
		TreeSet<Double> linkLocationsOnBlock = new TreeSet<Double>();
		boolean toOrFrom, upLinkOrDownLink;
		toOrFrom = false;
		upLinkOrDownLink = true;
		for (Link link : block.getNextLinks(GlobalVar.UP_DIRECTION)) {
			double linkLocationOnBlock = getLinkLocationFromStartMilePost(link,
					block, toOrFrom, upLinkOrDownLink);
			linkLocationOnBlock = GlobalVariables
					.roundToThreeDecimals(linkLocationOnBlock);
			linkLocationsOnBlock.add(linkLocationOnBlock);
			Debug.fine("getLinkLocationsOnBlock: " + block.getBlockName()
					+ " linkLocationOnBlock " + linkLocationOnBlock);
		}

		toOrFrom = false;
		upLinkOrDownLink = false;
		for (Link link : block.getNextLinks(GlobalVar.DOWN_DIRECTION)) {
			double linkLocationOnBlock = getLinkLocationFromStartMilePost(link,
					block, toOrFrom, upLinkOrDownLink);
			linkLocationOnBlock = GlobalVariables
					.roundToThreeDecimals(linkLocationOnBlock);
			linkLocationsOnBlock.add(linkLocationOnBlock);
			Debug.fine("getLinkLocationsOnBlock: " + block.getBlockName()
					+ " linkLocationOnBlock " + linkLocationOnBlock);
		}

		toOrFrom = true;

		for (Enumeration<HashBlockEntry> e = this.elements(); e
				.hasMoreElements();) {
			HashBlockEntry hashBlockEntry = e.nextElement();
			Block previousBlock = hashBlockEntry.getBlock();
			// System.out.println("hashblock: "+hashBlockEntry.block.blockNo);
			upLinkOrDownLink = true;
			for (Link link : previousBlock.getNextLinks(GlobalVar.UP_DIRECTION)) {
				// System.out.println("uplinks: linkNext " +
				// link.getNextBlockNo()
				// + " linkPrevious " + link.getPreviousBlockNo());
				if (link.getNextBlockNo() == block.getBlockNo()) {
					double linkLocationOnBlock = getLinkLocationFromStartMilePost(
							link, block, toOrFrom, upLinkOrDownLink);
					linkLocationOnBlock = GlobalVariables
							.roundToThreeDecimals(linkLocationOnBlock);
					linkLocationsOnBlock.add(linkLocationOnBlock);
					Debug.fine("getLinkLocationsOnBlock: "
							+ block.getBlockName() + " linkLocationOnBlock "
							+ linkLocationOnBlock);
				}
			}

			upLinkOrDownLink = false;
			// System.out.println("uplinks ended");
			for (Link link : previousBlock
					.getNextLinks(GlobalVar.DOWN_DIRECTION)) {
				// System.out.println("downlinks: linkNext "+link.getNextBlockNo()+" linkPrevious "+link.getPreviousBlockNo());
				if (link.getNextBlockNo() == block.getBlockNo()) {
					double linkLocationOnBlock = getLinkLocationFromStartMilePost(
							link, block, toOrFrom, upLinkOrDownLink);
					linkLocationOnBlock = GlobalVariables
							.roundToThreeDecimals(linkLocationOnBlock);
					linkLocationsOnBlock.add(linkLocationOnBlock);
					Debug.fine("getLinkLocationsOnBlock: "
							+ block.getBlockName() + " linkLocationOnBlock "
							+ linkLocationOnBlock);
				}
			}

			// System.out.println("downlinks ended");

		}

		return linkLocationsOnBlock;
	}

	private static double getLinkLocationFromStartMilePost(Link link,
			Block block, boolean toOrFrom, boolean upLinkOrDownLink) {
		double linkLocationFromStartMilePost;
		double linkLocationFromRelevantPost;
		double linkLocationFromOtherPost;
		if (toOrFrom) {
			linkLocationFromRelevantPost = link.getNextBlockRelativeMilePost();
			linkLocationFromOtherPost = block.getLength()
					- link.getNextBlockRelativeMilePost();

			Debug.fine("linkLocationFromRelevantPost "
					+ linkLocationFromRelevantPost
					+ " linkLocationFromOtherPost " + linkLocationFromOtherPost);

			switch (block.getDirection()) {
			case GlobalVar.UP_DIRECTION:
				linkLocationFromStartMilePost = linkLocationFromRelevantPost;
				break;
			case GlobalVar.DOWN_DIRECTION:
				linkLocationFromStartMilePost = linkLocationFromOtherPost;
				break;
			default:// GlobalVar.COMMON_DIRECTION:
				linkLocationFromStartMilePost = upLinkOrDownLink ? linkLocationFromRelevantPost
						: linkLocationFromOtherPost;
				break;
			}
		} else {
			linkLocationFromRelevantPost = link
					.getPreviousBlockRelativeMilePost();
			linkLocationFromOtherPost = block.getLength()
					- link.getPreviousBlockRelativeMilePost();
			Debug.fine("linkLocationFromRelevantPost "
					+ linkLocationFromRelevantPost
					+ " linkLocationFromOtherPost " + linkLocationFromOtherPost);

			switch (block.getDirection()) {
			case GlobalVar.UP_DIRECTION:
				linkLocationFromStartMilePost = linkLocationFromOtherPost;
				break;
			case GlobalVar.DOWN_DIRECTION:
				linkLocationFromStartMilePost = linkLocationFromRelevantPost;
				break;
			default:// GlobalVar.COMMON_DIRECTION:
				linkLocationFromStartMilePost = upLinkOrDownLink ? linkLocationFromOtherPost
						: linkLocationFromRelevantPost;
				break;
			}
		}

		Debug.fine("getLinkLocationFromStartMilePost: " + block.getBlockName()
				+ " toOrFrom " + toOrFrom + " upLinkOrDownLink "
				+ upLinkOrDownLink + " linkLocationFromStart "
				+ linkLocationFromStartMilePost);

		return linkLocationFromStartMilePost;
	}
}

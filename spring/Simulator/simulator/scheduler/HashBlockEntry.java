package simulator.scheduler;

import globalVariables.GlobalVar;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Link;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntities.trackProperties.TinyBlock;
import gui.entities.sectionEntityList.LinkList;
import gui.entities.sectionEntityList.SpeedRestrictionList;
import gui.entities.sectionEntityList.StationList;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;

import simulator.util.Debug;

/**
 * HashBlockEntry
 */
public class HashBlockEntry {
	/**
	 * block
	 */
	private Block block;
	/**
	 * upLinks
	 */
	private LinkList upLinks = new LinkList();
	/**
	 * downLinks
	 */
	private LinkList downLinks = new LinkList();

	public static HashBlockEntry getHashBlockEntryForBlock(
			StreamTokenizer streamtokenizer) throws IOException,
			SimulatorException {

		Block block = new Block();
		HashBlockEntry hashBlockEntry = new HashBlockEntry();
		hashBlockEntry.setBlock(block);
		/* initialization */

		// System.out.println(streamtokenizer.ttype);
		System.out.println((new StringBuilder()).append("value read is ")
				.append(streamtokenizer.nval).toString());

		Debug.print("Block constructor: Reading a new block");
		if (streamtokenizer.ttype != StreamTokenizer.TT_NUMBER) {
			Debug.print("Block constructor: Error in format of input file : block.txt......");
			Debug.print("Block constructor: Error : Block no expected");
			throw new SimulatorException();
		}

		int blockNo = (int) streamtokenizer.nval;
		block.setBlockNo(blockNo);

		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -3) {
			Debug.print("Block constructor: Error in format of input file : block.txt......");
			Debug.print("Block constructor: Error : direction(0/1/2) expected");
			throw new SimulatorException();
		}
		Debug.print((new StringBuilder()).append("value read is ")
				.append(streamtokenizer.sval).toString());

		if (streamtokenizer.sval.equalsIgnoreCase("DOWN")) {
			block.setDirection(GlobalVar.DOWN_DIRECTION);
		} else if (streamtokenizer.sval.equalsIgnoreCase("UP")) {
			block.setDirection(GlobalVar.UP_DIRECTION);
		} else {
			block.setDirection(GlobalVar.COMMON_DIRECTION);
		}

		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug.print("Block constructor: Error in format of input file : block.txt......");
			Debug.print("Block constructor: Error : Start milepost expected");
			throw new SimulatorException();
		}

		Debug.print((new StringBuilder()).append("value read is ")
				.append(streamtokenizer.nval).toString());
		block.setStartMilePost(streamtokenizer.nval);

		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug.print("Block constructor: Error in format of input file : block.txt......");
			Debug.print("Block constructor: Error : End milepost expected");
			throw new SimulatorException();
		}

		block.setEndMilePost(streamtokenizer.nval);

		streamtokenizer.nextToken();
		if (streamtokenizer.ttype != -2) {
			Debug.print("Block constructor: Error in format of input file : block.txt......");
			Debug.print("Block constructor: Error : maximumPossibleSpeed  expected");
			throw new SimulatorException();
		}

		Debug.print((new StringBuilder()).append("value read is ")
				.append(streamtokenizer.nval).toString());
		block.setMaximumPossibleSpeed(streamtokenizer.nval / 60D);

		block.setBlockName((new StringBuilder()).append("Block - ")
				.append(blockNo).toString());

		hashBlockEntry.readCommonBlockLoop(streamtokenizer);
		return hashBlockEntry;
	}

	private void readCommonBlockLoop(StreamTokenizer streamtokenizer)
			throws IOException, SimulatorException {

		this.readAndBuildLink(streamtokenizer, GlobalVar.UP_DIRECTION);
		this.readAndBuildLink(streamtokenizer, GlobalVar.DOWN_DIRECTION);

		SpeedRestrictionList.readSpeedRestriction(streamtokenizer,
				this.getBlock());
		// GradientList.readGradients(streamtokenizer, this.getBlock());
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public LinkList getUpLinks() {
		return upLinks;
	}

	public void setUpLinks(LinkList upLinks) {
		this.upLinks = upLinks;
	}

	public LinkList getDownLinks() {
		return downLinks;
	}

	public void setDownLinks(LinkList downLinks) {
		this.downLinks = downLinks;
	}

	/**
	 * @param dir
	 * @param blkNos
	 * @param linkLength
	 * @param strPriority
	 * @param strCrossOver
	 * @param s3
	 */
	public void buildLink(int linkDirection, String nextBlocksString,
	// String relativePreviousBlockMilePostsString,
	// String relativeNextBlockMilePostsString,
			String lengthsString, String prioritiesString,
			// String maximumSpeedsString,
			String crossoversString) {

		Block block = this.getBlock();

		String majorDelimiter = GlobalVar.majorStringDelimiter;
		StringTokenizer blockNoStringTokenizer = new StringTokenizer(
				nextBlocksString, majorDelimiter);
		// StringTokenizer relativeStartMilePostsStringTokenizer = new
		// StringTokenizer(
		// relativePreviousBlockMilePostsString, majorDelimiter);
		// StringTokenizer relativeEndMilePostsStringTokenizer = new
		// StringTokenizer(
		// relativeNextBlockMilePostsString, majorDelimiter);
		StringTokenizer lengthStringTokenizer = new StringTokenizer(
				lengthsString, majorDelimiter);

		StringTokenizer prioritiesStringTokenizer = new StringTokenizer(
				prioritiesString, majorDelimiter);
		// StringTokenizer maximumSpeedsStringTokenizer = new StringTokenizer(
		// maximumSpeedsString, majorDelimiter);
		StringTokenizer crossoversStringTokenizer = new StringTokenizer(
				crossoversString, majorDelimiter);

		// System.out.println("Link: buildLink: block " + block.blockNo
		// + " crossovers " + crossOversString);

		while (blockNoStringTokenizer.hasMoreTokens()) {

			String nextBlockNoString = blockNoStringTokenizer.nextToken();
			// String relativeStartMilePostString =
			// relativeStartMilePostsStringTokenizer
			// .nextToken();
			// String relativeEndMilePostString =
			// relativeEndMilePostsStringTokenizer
			// .nextToken();
			String lengthString = lengthStringTokenizer.nextToken();
			String priorityString = prioritiesStringTokenizer.nextToken();
			// String maximumSpeedString = maximumSpeedsStringTokenizer
			// .nextToken();
			String crossoverString = crossoversStringTokenizer.nextToken();

			int nextBlockNo = Integer.parseInt(nextBlockNoString);

			double previousBlockRelativeMilePost = 0, nextBlockRelativeMilePost = 0, length = 0;
			// // previousBlockRelativeMilePost = Double
			// // .parseDouble(relativeStartMilePostString);
			// double startMilePost = previousBlockRelativeMilePost
			// + block.getStartMilePost();
			//
			// // nextBlockRelativeMilePost = Double
			// // .parseDouble(relativeEndMilePostString);
			// double endMilePost = nextBlockRelativeMilePost
			// + block.getStartMilePost();
			//
			length = Double.parseDouble(lengthString);

			int linkPriority = Integer.parseInt(priorityString);
			double maximumSpeed = 0;
			// maximumSpeed = Double.parseDouble(maximumSpeedString);

			Link newLink = new Link(nextBlockNo, previousBlockRelativeMilePost,
					nextBlockRelativeMilePost, length, linkPriority,
					maximumSpeed, block);

			String[] crossovers = crossoverString
					.split(GlobalVar.minorStringDelimiter);
			for (int i = 0; i < crossovers.length; i++) {
				String strCrossover = crossovers[i];
				if (!strCrossover.equalsIgnoreCase("#")) {
					int intCrossover = Integer.parseInt(strCrossover);
					if (intCrossover != -1) {
						newLink.getCrossovers().add(new Block(intCrossover));
					}
				}
			}

			TinyBlock tinyBlock = new TinyBlock(length);
			newLink.tinyBlockArray.add(tinyBlock);

			Debug.print("buildLink: after reading crossover");

			if (linkDirection == GlobalVar.UP_DIRECTION) {
				Debug.print("buildLink: direction of the link is UP");
				this.getUpLinks().add(newLink);
			} else {
				Debug.print("buildLink: direction of the link is DOWN");
				this.getDownLinks().add(newLink);
			}
		}
	}

	public void readAndBuildLink(StreamTokenizer streamtokenizer,
			int linkDirection) throws IOException {

		// read next block
		streamtokenizer.nextToken();
		String nextBlocksString = streamtokenizer.sval;

		// read relative starting milePosts of the links
		// streamtokenizer.nextToken();
		// String relativePreviousBlockMilePostsString = streamtokenizer.sval;
		//
		// // read relative ending milePosts of the links
		// streamtokenizer.nextToken();
		// String relativeNextBlockMilePostsString = streamtokenizer.sval;
		//
		// // read lengths of the links
		streamtokenizer.nextToken();
		String lengthsString = streamtokenizer.sval;

		// read priorities of the links
		streamtokenizer.nextToken();
		String prioritiesString = streamtokenizer.sval;

		// read the maximum permissible speeds on the links
		// streamtokenizer.nextToken();
		// String maximumSpeedsString = streamtokenizer.sval;

		// read the crossovers of the links
		streamtokenizer.nextToken();
		String crossoversString = streamtokenizer.sval;

		// this.buildLink(linkDirection, nextBlocksString,
		// relativePreviousBlockMilePostsString,
		// relativeNextBlockMilePostsString, lengthsString,
		// prioritiesString, maximumSpeedsString, crossoversString);
		this.buildLink(linkDirection, nextBlocksString, lengthsString,
				prioritiesString, crossoversString);

	}

	public static HashBlockEntry getHashBlockEntryForLoop(
			StreamTokenizer streamTokenizer, StationList stationList)
			throws IOException, SimulatorException {
		Loop loop = new Loop();
		HashBlockEntry hashBlockEntry = new HashBlockEntry();
		hashBlockEntry.setBlock(loop);

		Debug.print("Loop: constructor: ");

		if (streamTokenizer.ttype != StreamTokenizer.TT_NUMBER) {
			Debug.print("Loop: constructor: Error in format of input file : station.dat......");
			Debug.print("Loop: constructor: Error : loop number expected");
			throw new SimulatorException();
		}

		Debug.print("Loop: constructor: loop number read is "
				+ streamTokenizer.nval);

		int blockNo = (int) streamTokenizer.nval;
		loop.setBlockNo(blockNo);
		Debug.print("Loop: constructor: loop number is " + loop.getBlockNo());

		streamTokenizer.nextToken();
		if (streamTokenizer.ttype != StreamTokenizer.TT_WORD) {
			Debug.print("Loop: constructor: Error in format of input file : station.dat......");
			Debug.print("Loop: constructor: Error : direction(0/1/2) expected");
			throw new SimulatorException();
		}

		Debug.print("Loop: constructor: value read is " + streamTokenizer.sval);

		if (streamTokenizer.sval.equals("DOWN")) {
			loop.setDirection(GlobalVar.DOWN_DIRECTION);
		} else {
			if (streamTokenizer.sval.equals("UP")) {
				loop.setDirection(GlobalVar.UP_DIRECTION);
			} else {
				// Debug.assert(st.sval.equals("COMMON"),"Input either UP, DOWN or COMMON");
				loop.setDirection(GlobalVar.COMMON_DIRECTION);
			}
		}

		streamTokenizer.nextToken();
		if (streamTokenizer.ttype != StreamTokenizer.TT_WORD) {
			Debug.print("Loop: constructor: Error in format of input file : station.dat......");
			Debug.print("Loop: constructor: Error : cross main line(0/1/2) expected");
			throw new SimulatorException();
		}
		Debug.print("Loop: constructor: value read is " + streamTokenizer.sval);

		if (streamTokenizer.sval.equals("uml")) {
			loop.setCrossMainLine(1);
			loop.setWhetherMainLine(0);
		} else {
			if (streamTokenizer.sval.equals("dml")) {
				loop.setCrossMainLine(2);
				loop.setWhetherMainLine(0);
			} else {
				loop.setCrossMainLine(0);
				if (streamTokenizer.sval.equals("ml")) {
					loop.setWhetherMainLine(1);
				} else {
					// Debug.assert(st.sval.equals("loop"),"Input either ml or loop");
					loop.setWhetherMainLine(0);
				}
			}
		}

		streamTokenizer.nextToken();
		// if (streamTokenizer.ttype != StreamTokenizer.TT_WORD) {
		// Debug.print("Loop: constructor: Error in format of input file : station.dat......");
		// Debug.print("Loop: constructor: Error : direction(0/1/2) expected");
		// throw new SimulatorException();
		// }

		String strStn = streamTokenizer.sval;
		Debug.print("Loop: constructor: Station is " + strStn);
		Debug.print("Loop: constructor: value read is " + streamTokenizer.sval);
		
		streamTokenizer.nextToken();
		String loopTrainType = streamTokenizer.sval;
		Debug.print("Loop: constructor: Type of train which is allowed in this loop is " + loopTrainType);
		Debug.print("Loop: constructor: value read is " + streamTokenizer.sval);

		/*
		 * New input format for accurate mileposts modelling of loops
		 */
		// streamTokenizer.nextToken();
		// String platformName = streamTokenizer.sval;

		// streamTokenizer.nextToken();
		// double doubleStartMilePost = streamTokenizer.nval;
		// streamTokenizer.nextToken();
		// double doubleEndMilePost = streamTokenizer.nval;
		System.out.println("Loop: no " + blockNo);
		streamTokenizer.nextToken();
		if (streamTokenizer.ttype != StreamTokenizer.TT_NUMBER) {
			Debug.print("Loop: constructor: Error in format of input file : station.dat......");
			Debug.print("Loop: constructor: Error : maximumPossibleSpeed expected");
			throw new SimulatorException();
		}

		Debug.print("Loop: constructor: value read is " + streamTokenizer.nval);
		loop.setMaximumPossibleSpeed((streamTokenizer.nval / 60));
		Debug.print("Loop: constructor: " + loop.getMaximumPossibleSpeed());

		loop.setBlockName("Loop - " + loop.getBlockNo());

		// loop.setPlatformName(platformName);
		loop.setStation(stationList.getStation(strStn));
		loop.setLoopTrainType(loopTrainType);

		// Debug.assert(stn!=null,"Error Station " + strStn + " not found");
		double doubleStartMilePost, doubleEndMilePost;
		doubleStartMilePost = loop.getStation().getStartMilePost();
		doubleEndMilePost = loop.getStation().getEndMilePost();
		loop.setStartMilePost(doubleStartMilePost);
		loop.setEndMilePost(doubleEndMilePost);

		Debug.print("  hbEntry is " + hashBlockEntry.getBlock().getBlockNo());

		hashBlockEntry.readCommonBlockLoop(streamTokenizer);
		return hashBlockEntry;

	}

}
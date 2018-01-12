package gui.entities.sectionEntityList;

import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Link;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.trainEntities.Train;

import java.util.ArrayList;

import simulator.util.Debug;

/**
 * LinkList
 */
public class LinkList extends ArrayList<Link> {

	/**
	 * NextBLockArray: ArrayList<Link> getSortedLinks(Train train, Block block,
	 * double arrivalTime, double deptTime, double startVelocity) 1) The base
	 * cases as in the nextBlockArray has size 0 or 1 it should return the
	 * linkArray or add the only nextBlock and return. 2) It checks if the
	 * current block is actually a "block" and if the train is scheduled. Then
	 * it checks if the next block should be a loop then finds the station name,
	 * refers the referenceTable, finds the preferred loop, adds that first,
	 * then adds the rest of the loops which do not conflict with the train's
	 * direction. 3) Else if the currentBlock is a "loop" or the train is
	 * unscheduled one, it adds those links which has a path till the endLoop
	 * for the train. Block: boolean hasPath(Block block, int trainEndLoopNo,
	 * int trainDirection) 1) It iterates through the all the links and ignores
	 * those which are null, while returns true if one of them is the endLoop 2)
	 * OtherWise it takes that link's nextBlock for recursion and finds if there
	 * exists a path.
	 * 
	 * 
	 * @param Train
	 * @param block
	 * @param arrivalTime
	 * @param deptTime
	 * @param startVelocity
	 * @return list of links.
	 */
	public LinkList getSortedLinks(Train train, int nextStnBlock) {

		int direction = train.getDirection();

		int preferredLoop = -1;
		String nextStnName = "NULL";
		Block blk;
		LinkList linkArray = new LinkList();

		Debug.print("LinkList: getSortedLinks: next Block array size " + size());
		if (0 == size()) {
			Debug.print("LinkList: getSortedLinks: nextBlockArraySize is 0");
			return linkArray;
		}

		if (1 == size()) {
			linkArray.add((Link) get(0));
			return linkArray;
		}

		Debug.fine("LinkList: getSortedLinks: size = " + size());

		// for scheduled trains
		if (train.isScheduled()) {

			for (Link link : this) {
				Block nextBlock = link.getNextBlock();
				boolean nextBlockHasPath = nextBlock.hasPath(nextStnBlock,
						train.getDirection());

				if (nextBlockHasPath) {
					linkArray.addLinkPriorityWise(link);
				}
			}

			if (linkArray.size() == 0) {
				System.out.println("no further links to reach destination");
			}

			return linkArray;
		}

		// for unscheduled trains

		preferredLoop = train.getEndLoopNo();
		Debug.fine("preferredLoop: " + preferredLoop);
		LinkList linkSetHavingPath = new LinkList();
		LinkList linkSetNotHavingPath = new LinkList();

		for (int i = 0; i < size(); i++) {

			Debug.print("LinkList: getSortedLinks: inside for loop");

			Debug.print(("Block No. HasPath" + (((Link) get(i)).getNextBlock())
					.getBlockNo()));

			Debug.print(((((Link) get(i)).getNextBlock()).getClass()).getName());

			Link thisLink = (Link) get(i);
			if (!(thisLink.getNextBlock()).isLoop()) {

				Debug.print("Train End Loop-->" + train.getEndLoopNo());

				if (thisLink.getNextBlock().hasPath(train.getEndLoopNo(),
						train.getDirection())) {

					Debug.print("HasPath");

					// insertion sort for links having path
					linkSetHavingPath.addLinkPriorityWise(thisLink);
				} else {// if no path exists from this link then simply add this
					// insertion sort for links not having path
					linkSetNotHavingPath.addLinkPriorityWise(thisLink);
				}
			} else {// we are considering links to a loop

				if (thisLink.getNextBlock().hasPath(train.getEndLoopNo(),
						train.getDirection())) {
					
					Loop nextLoop = (Loop) thisLink.getNextBlock();
					//System.out.println("Linklist.SortLinks: nextLoop: " + nextLoop.getBlockNo());
					if (nextLoop.isFreightLoop()){
						
						// Devendra uptil above
						if (((Loop) thisLink.getNextBlock()).getWhetherMainLine() == 1) {
	
							Debug.print("LinkList: getSortedLinks: whetherMainLine==1");
							linkArray.add(0, thisLink);
						} else {
							// insertion sort for links having path
							linkSetNotHavingPath.addLinkPriorityWise(thisLink);
						}
					}
				} else {
					
					Loop nextLoop = (Loop) thisLink.getNextBlock();
					if (nextLoop.isFreightLoop()){
						
						if (((Loop) thisLink.getNextBlock()).getWhetherMainLine() == 1) {
							Debug.print("LinkList: getSortedLinks: whetherMainLine==1");
							linkArray.add(0, thisLink);
						} else {
							// insertion sort for links not having path
							linkSetNotHavingPath.addLinkPriorityWise(thisLink);
	
						}
					}
				}
			}
		}

		linkArray.addAll(linkSetHavingPath);
		linkArray.addAll(linkSetNotHavingPath);

		Debug.fine("LinkList: getSortedLinks: end linkArray has "
				+ linkArray.size());
		return linkArray;
	}

	/**
	 * Insert the link into a linkList sorted according to its priority
	 * 
	 * @param linkList
	 * @param link
	 */
	public void addLinkPriorityWise(Link link) {

		boolean isLinkAdded = false;

		// sorted insertion
		for (int i = 0; i < this.size(); i++) {
			if (link.getPriority() < this.get(i).getPriority()) {
				this.add(i, link);
				isLinkAdded = true;
				break;
			}
		}

		// if link is not added meaning it has the worst priority and should
		// be added last
		if (isLinkAdded == false) {
			this.add(link);
		}
	}

	/**
	 * @return next main block.
	 */
	public Block getNextMainBlock() {
		int mostPreferredLinkPriority = 10;
		Block mostPreferredLinkBlock = null;
		Debug.fine("LinkList: getNextMainBlock: ");

		for (Link link : this) {

			Debug.fine("LinkList: getNextMainBlock: Priority is "
					+ link.getPriority());
			if (mostPreferredLinkPriority > link.getPriority()) {
				mostPreferredLinkPriority = link.getPriority();
				mostPreferredLinkBlock = link.getNextBlock();
			}
		}

		if (mostPreferredLinkBlock == null) {
			Debug.fine("LinkList: getNextMainBlock: return null");
		}

		return mostPreferredLinkBlock;
	}

	public Link getNextMainLink() {
		int mostPreferredLinkPriority = 10;
		for (Link link : this) {
			if (mostPreferredLinkPriority > link.getPriority())
				mostPreferredLinkPriority = link.getPriority();
		}

		for (int i = 0; i < size(); i++) {
			if (get(i).getPriority() == mostPreferredLinkPriority) {
				return get(i);
			}
		}

		if (this.size() != 0)
			Debug.fine("getNextMainLink: returning a null link");
		return null;
	}

}

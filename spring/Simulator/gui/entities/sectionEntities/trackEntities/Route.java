package gui.entities.sectionEntities.trackEntities;

import gui.entities.trainEntities.Train;
import simulator.input.SimulationInstance;

public class Route implements Comparable<Route> {
	public Block previousBlock;
	public Block nextBlock;
	public Train train;

	public Route(int previousBlockNo, int nextBlockNo,
			SimulationInstance simulationInstance, Train train) {
		Block previousBlock = simulationInstance
				.getBlockFromBlockNo(previousBlockNo);
		Block nextBlock = simulationInstance.getBlockFromBlockNo(nextBlockNo);
		if (previousBlock == null)
			System.out.println("Block " + previousBlockNo + " is null");
		if (nextBlock == null)
			System.out.println("Block " + nextBlockNo + " is null");
		this.previousBlock = previousBlock;
		this.nextBlock = nextBlock;
		this.train = train;

	}

	@Override
	public int compareTo(Route otherRoute) {
		int thisRoutePreviousBlockNo = previousBlock.getBlockNo();
		int thisRoutenextBlockNo = nextBlock.getBlockNo();

		Train otherTrain = otherRoute.train;
		int otherRoutePreviousBlockNo = otherRoute.previousBlock.blockNo;
		int otherRoutenextBlockNo = otherRoute.nextBlock.blockNo;
		if (thisRoutePreviousBlockNo == otherRoutePreviousBlockNo
				&& thisRoutenextBlockNo == otherRoutenextBlockNo
				&& train.getTrainNo() == otherTrain.getTrainNo())
			return 0;
		else if (thisRoutePreviousBlockNo < otherRoutePreviousBlockNo)
			return -1;
		else if (thisRoutePreviousBlockNo > otherRoutePreviousBlockNo)
			return 1;
		else if (thisRoutenextBlockNo < otherRoutenextBlockNo)
			return -1;
		else if (thisRoutenextBlockNo > otherRoutenextBlockNo)
			return 1;
		else if (train.getTrainNo() < otherTrain.getTrainNo())
			return -1;
		else if (train.getTrainNo() > otherTrain.getTrainNo())
			return 1;
		return 0;
	}
}

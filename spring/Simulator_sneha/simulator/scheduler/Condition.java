package simulator.scheduler;

import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntityList.TrainList;
import gui.entities.trainEntities.Train;

public class Condition {
	private Train train = null;
	private Loop loop = null;

	public Condition(Train train, Loop loop) {
		this.train = train;
		this.loop = loop;
	}

	public boolean isSatisfied(TrainList trainList) {
		boolean trainExists = false;
		for (Train train : trainList) {
			if (train.equals(this.train))
				trainExists = true;
		}

		if (!trainExists)
			return true;

		for (StationToStationScheduler s2sScheduler : train.stationToStationSchedulerStack) {
			Loop loop = s2sScheduler.nextReferenceLoop;

			if (loop.equals(this.loop)) {
				return s2sScheduler.scheduled;
			}
		}

		return false;
	}

	public String getInfoString() {
		return "Train " + train.getTrainNo() + " conditioned at "
				+ loop.getBlockName();
	}
}

package simulator.scheduler;

import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.trainEntities.Train;

import java.util.Comparator;

import simulator.util.Debug;

public class LoopTrainScheduleData implements Comparator<LoopTrainScheduleData> {
	private Loop loop = null;
	private Train train = null;
	private boolean scheduled = false;
	private double referenceDepartureTime = 10000;

	public LoopTrainScheduleData(Loop loop, Train train,
			double referenceDepartureTime) {
		this.loop = loop;
		this.setTrain(train);
		this.referenceDepartureTime = referenceDepartureTime;
	}

	public LoopTrainScheduleData() {
		// TODO Auto-generated constructor stub
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public double getReferenceDepartureTime() {
		return referenceDepartureTime;
	}

	public void setReferenceDepartureTime(double referenceDepartureTime) {
		this.referenceDepartureTime = referenceDepartureTime;
	}

	@Override
	public int compare(LoopTrainScheduleData o1, LoopTrainScheduleData o2) {
		// same train will not move over the same loop twice
		// in good timetable two trains won't leave the same platform at the
		// same time

		double time1 = o1.referenceDepartureTime;
		double time2 = o2.referenceDepartureTime;

		Train train1 = o1.train;
		Train train2 = o2.train;

		int t1 = train1.getTrainNo();
		int t2 = train2.getTrainNo();

		if (o1.loop.getBlockNo() == 45
				&& ((t1 == 90006 && t2 == 90012) || (t1 == 90012 && t2 == 90006))) {
			Debug.error("t1 " + t1 + " t2 " + t2 + " time1 " + time1
					+ " time2 " + time2);
		}

		if (time1 < time2)
			return -1;
		else if (time1 > time2)
			return 1;

		else {
			Debug.error("LoopTrainScheduleData: Comparator: problem between "
					+ o1.train.getTrainNo() + " and " + o2.train.getTrainNo()
					+ " for time " + time1 + " on " + o1.loop.getBlockName());
			return 0;
		}

	}

	public void setLoop(Loop loop) {
		this.loop = loop;
	}
}

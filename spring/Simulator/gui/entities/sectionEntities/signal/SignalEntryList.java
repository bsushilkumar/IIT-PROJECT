package gui.entities.sectionEntities.signal;

import java.util.ArrayList;

import simulator.input.SimulationInstance;

public class SignalEntryList extends ArrayList<SignalEntry> {

	public int getSignalValueFromTime(double simulationTime,
			SimulationInstance simulationInstance) {
		int signalValue = simulationInstance.numberOfSignalAspects - 1;
		for (SignalEntry signalEntry : this) {
			double startTime = signalEntry.getStartTime();
			double endTime = signalEntry.getEndTime();

			if (startTime <= simulationTime && simulationTime <= endTime) {
				return signalValue = signalEntry.getSignalValue();
			}
		}

		return signalValue;
	}

}

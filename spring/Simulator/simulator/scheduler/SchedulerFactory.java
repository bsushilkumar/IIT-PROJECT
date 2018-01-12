package simulator.scheduler;

import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.trainEntities.Train;
import simulator.input.SimulationInstance;

/**
 * Title: Simulation of SimulatorTrain Pathing Description: Copyright: Copyright
 * (c) 2002 Company: IIT
 * 
 * @author
 * @version 1.0
 */

public class SchedulerFactory {

	/**
	 * constructor
	 */
	public SchedulerFactory() {
	}

	/**
	 * Get the Block scheduler.
	 * 
	 * @param type
	 * @param block
	 * @param simulatorTrain
	 * @param simulationInstance
	 * @return {@link BlockScheduler}
	 */
	public BlockScheduler getScheduler(Block block, Train simulatorTrain,
			SimulationInstance simulationInstance) {
		/*
		 * if(type.equalsIgnoreCase("SignalFailure")){ return (new
		 * SignalFailureScheduler(block,train)); }
		 */
		return (new BlockScheduler(block, simulatorTrain, simulationInstance));
	}
}
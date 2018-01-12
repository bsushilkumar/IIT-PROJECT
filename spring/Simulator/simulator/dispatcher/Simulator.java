package simulator.dispatcher;

import gui.entities.sectionEntityList.TrainList;
import simulator.input.SimulationInstance;
import simulator.outputFeatures.GraphPanel;

public class Simulator {

	/**
	 * currentTrainNo
	 */
	protected int currentTrainNo;// gives the current train which is being
								// scheduled.
	/**
	 * graphPanel
	 */
	public GraphPanel graphPanel; // Graphical output

	/**
	 * trainsArray
	 */
	protected TrainList trainList;

	protected SimulationInstance simulationInstance;

	/**
	 * original
	 */
	// int original;

}

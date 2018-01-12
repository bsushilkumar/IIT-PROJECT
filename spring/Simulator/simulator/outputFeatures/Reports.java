package simulator.outputFeatures;

import gui.entities.sectionEntityList.TrainList;
import gui.entities.trainEntities.Train;

/**
 * Title: Simulation of SimulatorTrain Pathing Description: Copyright: Copyright (c) 2002
 * Company: IIT
 * 
 * @author
 * @version 1.0
 */

public class Reports {

    /**
     * constructor.
     */
    public Reports() {
    }

    /**
     * 
     */
    public void outputTotalTrainTravel(TrainList trainList) {
	double totalTime = 0;
	for (int i = 0; i < trainList.size(); i++) {
	    Train simulatorTrain = trainList.get(i);
	    totalTime += simulatorTrain.totalTime();

	    System.out.println(simulatorTrain.getTrainNo() + " -  " + simulatorTrain.totalTime() + "  -  "
		    + simulatorTrain.travelTime() + " - " + totalTime / (i + 1));
	}
	// System.out.println("Average Travelling time " +
	// totalTime/GlobalVar.trainArrayList.size());
	System.out.println("Average Travelling time " + totalTime);

    }
}

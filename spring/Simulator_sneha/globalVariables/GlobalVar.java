package globalVariables;

import gui.entities.sectionEntities.trackEntities.Block;


import gui.entities.trainEntities.Train;
import input.BlockInput;
import input.GradientEffectsInputDialog;
import input.GradientInputDialog;
import input.InputFiles;
import input.Maintblock;
import input.ParameterInputDialog;
import input.SectionInputDialog;
import input.StationInputDialog;
import input.LoopInputDialog;
import input.TrainInputDialog;

import java.io.File;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;

import simulator.input.SimulationInstance;


/**
 * global variables class.
 */
public class GlobalVar {
	// /**
	// * pathParam
	// */
	// public static String pathParam = "param.dat";
	// /**
	// * pathBlock
	// */
	// public static String pathBlock = "block.dat";
	// /**
	// * pathLoop
	// */
	// public static String pathLoop = "";
	// /**
	// * pathStation
	// */
	// public static String pathStation = "station.dat";
	// /**
	// * pathUnscheduled
	// */
	// public static String pathUnscheduled = "unScheduled.dat";
	// /**
	// * pathScheduled
	// */
	// public static String pathScheduled = "Scheduled.dat";
	// /**
	// * pathGradient
	// */
	// public static String pathGradient = "";
	// /**
	// * pathGradientEffect
	// */
	// public static String pathGradientEffect = "";
	// // Devendra
	// /**
	// * pathBlockDirectionInInterval
	// */
	// public static String pathBlockDirectionInInterval = "";

	// /**
	// * fileParam
	// */
	// public static File fileParam = new File("");
	// /**
	// * fileBlock
	// */
	// public static File fileBlock = new File("");
	// /**
	// * fileLoop
	// */
	// public static File fileLoop = new File("");
	// /**
	// * fileStation
	// */
	// public static File fileStation = new File("");
	// /**
	// * fileUnscheduled
	// */
	// public static File fileUnscheduled = new File("");
	// /**
	// * fileScheduled
	// */
	// public static File fileScheduled = new File("");
	// /**
	// * fileSignalFailure
	// */
	// public static File fileSignalFailure = new File("");
	//
	// /**
	// * fileGradient
	// */
	// public static File fileGradient = new File("");
	// /**
	// * fileGradientEffect
	// */
	// public static File fileGradientEffect = new File("");
	// /**
	// * filePassDelay
	// */
	// public static File filePassDelay = new File("");
	
	public static SectionInputDialog sectionInputDialog =null;
	private static GlobalVar inst;
	public static StationInputDialog stationInputDialog = null;
	public static LoopInputDialog loopInputDialog = null;
	public static TrainInputDialog trainInputDialog = null;
	public static ParameterInputDialog parameterInputDialog = null;
	public static GradientInputDialog gradientInputDialog = null;
	public static GradientEffectsInputDialog gradientEffectsInputDialog = null;
	public static InputFiles inputFiles = null;
	public static BlockInput blockInput = null;
	//shashank
	public static Maintblock maintblock = null;

	//Santhosh
	public static boolean capacitySelected=false;
	public static Train freightTrain=null;
	public static int headwayScheduled=0;
	public static int headwayFreight=0;
	public static boolean rawCapacitySelected=false;
	/**
	 * debugFile
	 */
	public static File debugFile = new File("debugFile.txt");

	// Devendra
	/**
	 * fileBlockDirectionInInterval
	 */
	public static File fileBlockDirectionInInterval = new File("");

	public static SimulationInstance simulationInstance = null;

	public static double sightingDistance = 0.2;

	public static boolean considerSightingDistance = true; //true to not halt train

	public static final String tabCharacter = "\t";
	public static final String spaceCharacter = " ";
	public static final String majorStringDelimiter = ",";
	public static final String minorStringDelimiter = ",";

	public static final int UP_DIRECTION = 1;
	public static final int DOWN_DIRECTION = -1;
	public static final int COMMON_DIRECTION = 2;
	public static final int SUBURBAN_TRAIN_PRIORITY = 10;
	public static final int N_BLOCK_LOOP_COMMON_PROPERTIES = 7 // uplinks
	+ 7 // downlinks properties
	+ 3 // speed restriction properties
	+ 4 // gradient properties
	;

	public static final int RED_SIGNAL = 0;
	public static final int YELLOW_SIGNAL = 1;
	
	//santhosh-added on devendra's instructions
	public static final int NONE_DIRECTION = 3;
	public static final double MINIMUM_WAIT_TIME_FOR_RETRY = 1;


	/**
	 * private constructor.
	 */
	private GlobalVar() {
	}

	public static void initializeLogFiles(Level logLevel) {
		FileHandler handler = null;
		try {
			handler = new FileHandler("log/Block.log");
			Block.setLogger(Logger.getAnonymousLogger());
			Block.getLogger().addHandler(handler);
			Block.getLogger().setLevel(logLevel);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static int getDirectionIntFromString(String direction) {
		if (direction.equalsIgnoreCase("up"))
			return GlobalVar.UP_DIRECTION;
		else if (direction.equalsIgnoreCase("down"))
			return GlobalVar.DOWN_DIRECTION;
		else
			return GlobalVar.COMMON_DIRECTION;
	}

	// public static SimulationInstance getSimulationInstance() {
	// if (simulationInstance == null)
	// simulationInstance = new SimulationInstance();
	//
	// return simulationInstance;
	// }
	
	public static SectionInputDialog getSectionInputDialog() {
		if (sectionInputDialog == null)
			sectionInputDialog = new SectionInputDialog();
		    
		return sectionInputDialog;
	}

	public static StationInputDialog getStationInputDialog() {
		if (stationInputDialog == null)
			stationInputDialog = new StationInputDialog();
		    
		return stationInputDialog;
	}
	



	public static TrainInputDialog getTrainInputDialog() {
		if (trainInputDialog == null)
			trainInputDialog = new TrainInputDialog();
		return trainInputDialog;
	}

	public static ParameterInputDialog getParameterInputDialog() {
		if (parameterInputDialog == null)
			parameterInputDialog = new ParameterInputDialog();
		return parameterInputDialog;
	}

	public static GradientInputDialog getGradientInputDialog() {
		if (gradientInputDialog == null)
			gradientInputDialog = new GradientInputDialog();
		return gradientInputDialog;
	}

	public static GradientEffectsInputDialog getGradientEffectsInputDialog() {
		if (gradientEffectsInputDialog == null)
			gradientEffectsInputDialog = new GradientEffectsInputDialog();
		return gradientEffectsInputDialog;
	}

	public static InputFiles getInputFileInterface() {
		if (inputFiles == null) {
			inputFiles = new InputFiles();
		}

		return inputFiles;
	}

	public static BlockInput getBlockInput() {
		if (blockInput == null)
			blockInput = new BlockInput();
		//else blockInput.updateStationNames();
		return blockInput;
	}
	
	//shashank
		public static Maintblock getMaintInputDialog() {
			if (maintblock == null)
				maintblock = new Maintblock();
			return maintblock;
		}


	public static int getDirectionFromDirectionString(String directionString) {
		if (directionString.equalsIgnoreCase("up"))
			return GlobalVar.UP_DIRECTION;
		if (directionString.equalsIgnoreCase("down"))
			return GlobalVar.DOWN_DIRECTION;
		return GlobalVar.COMMON_DIRECTION;
	}
	
	public static String getDirectionStringFromDirection(int direction) {
		String dirString=null; 
		if (direction==GlobalVar.UP_DIRECTION)
		{dirString="up";
		   return dirString;
				   
		}
		else if (direction == GlobalVar.DOWN_DIRECTION)
		{dirString="down";
			return dirString;
				   
		}
		else
		{
			dirString="common";
			return dirString;
					   
			
		}
			
	}
	
	
}
	
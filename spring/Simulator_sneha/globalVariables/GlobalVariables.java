package globalVariables;


import java.awt.Cursor;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;

public class GlobalVariables {
	public static boolean showBlockNumbers = false;
	public static boolean showBlockReservations = false;
	public static boolean showCondensedOccupyBlocks = false;
	public static boolean showSignalIds = true;
	public static boolean showSignalColors = false;
	public static boolean showBlockLengths = false;
	public static boolean selectingDiagrams = false;
	public static double xScale = 1;
	public static double yScale = 1;
	public static int viewLevel = 1;

	public static final String MAIN_FRAME_TITLE = "SketchRail";
	public static final String TOOL_FRAME_TITLE = "Tools";

	public static final int MAIN_FRAME_Y = 20;
	public static final int MAIN_FRAME_X = 10;
	public static final int MAIN_FRAME_WIDTH = 1350;
	public static final int MAIN_FRAME_HEIGHT = 740;

	public static final int BUTTON_X = 30;
	public static final int BUTTON_WIDTH = 150;
	public static final int BUTTON_HEIGHT = 20;
	public static final int BUTTON_Y = 20;
	public static final int BUTTON_STEP = 30;

	public static final int VIEW_SECTION_MODE = 0;
	public static final int VIEW_TRAINS_MODE = 1;
	private static int viewMode = VIEW_SECTION_MODE;

	public static int actionToBePerformed = -1;
	public static final int INSERT_ACTION = 0;
	public static final int DELETE_ACTION = 1;
	public static final int SELECT_ACTION = 2;
	public static final int STATION_INSERT_TOOL = 10;
	public static final int PLATFORM_INSERT_TOOL = 20;
	public static final int RAILWAY_LINE_INSERT_TOOL = 30;
	public static final int SELECT_TOOL = 40;
	public static final int CLEAR_TOOL = 50;
	public static final int SIGNAL_INSERT_TOOL = 60;
	public static final int LINK_INSERT_TOOL = 70;
	public static final int SAVE_PROJECT_TOOL = 80;
	public static final int NEW_PROJECT_TOOL = 90;
	public static final int OPEN_PROJECT_TOOL = 100;
	public static final int SPEED_RESTRICTION_INSERT_TOOL = 110;
	public static final int GRADIENT_INSERT_TOOL = 120;
	public static final int BLOCK_INSERT_TOOL = 130;
	public static final int LOOP_INSERT_TOOL = 140;
	public final static int DELETE_TOOL = 150;

	public static final int STATION_DELETE_TOOL = 160;
	public static final int PLATFORM_DELETE_TOOL = 170;
	public static final int RAILWAY_LINE_DELETE_TOOL = 180;
	public static final int SIGNAL_DELETE_TOOL = 190;
	public static final int BLOCK_DELETE_TOOL = 200;
	public static final int LOOP_DELETE_TOOL = 210;
	public static final int LINK_DELETE_TOOL = 220;
	public static final int GRADIENT_DELETE_TOOL = 230;
	public static final int SPEED_RESTRICTION_DELETE_TOOL = 240;

	public static final int STATION_SELECT_TOOL = 250;
	public static final int PLATFORM_SELECT_TOOL = 260;
	public static final int RAILWAY_LINE_SELECT_TOOL = 270;
	public static final int SIGNAL_SELECT_TOOL = 280;
	public static final int BLOCK_SELECT_TOOL = 290;
	public static final int LOOP_SELECT_TOOL = 300;
	public static final int LINK_SELECT_TOOL = 310;
	public static final int GRADIENT_SELECT_TOOL = 320;
	public static final int SPEED_RESTRICTION_SELECT_TOOL = 330;
	public static final int SCHEDULED_TRAIN_INSERT_TOOL = 340;
	public static final int SCHEDULED_TRAIN_DELETE_TOOL = 350;
	public static final int SCHEDULED_TRAIN_SELECT_TOOL = 360;
	public static final int UNSCHEDULED_TRAIN_INSERT_TOOL = 370;
	public static final int UNSCHEDULED_TRAIN_DELETE_TOOL = 380;
	public static final int UNSCHEDULED_TRAIN_SELECT_TOOL = 390;

	public static int toolSelected = -10;


	// public static final int UP_DIRECTION = 0;
	// public static final int DOWN_DIRECTION = 1;
	// public static final int BOTH_DIRECTIONS = 2;
	public static final int UNASSIGNED_DIRECTION = -1;

	public static final int NO_DIAGRAM_SELECTED = -1;
	public static final int SIGNAL_DIAGRAM_SELECTED = 0;
	public static final int RAILWAY_LINE_DIAGRAM_SELECTED = 1;
	public static final int PLATFORM_DIAGRAM_SELECTED = 2;
	public static final int STATION_DIAGRAM_SELECTED = 3;
	public static final int LOOP_DIAGRAM_SELECTED = 4;
	public static final int LINK_DIAGRAM_SELECTED = 5;
	public static final int SPEED_RESTRICTION_DIAGRAM_SELECTED = 6;
	public static final int GRADIENT_DIAGRAM_SELECTED = 7;
	public static final int BLOCK_DIAGRAM_SELECTED = 8;


	public static int cursors[] = { Cursor.NW_RESIZE_CURSOR,
			Cursor.NE_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR,
			Cursor.SW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR,
			Cursor.E_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR,
			Cursor.W_RESIZE_CURSOR, Cursor.MOVE_CURSOR };

	public static final int HAS_ABOVE_LOOP = 0;
	public static final int HAS_BELOW_LOOP = 1;
	public static final int HAS_BOTH_LOOPS = 2;


	public static int railwayLineDiagramCount = 0;
	public static int signalDiagramCount = 0;
	public static int stationDiagramCount = 0;
	public static int speedRestrictionDiagramCount = 0;
	public static int linkDiagramCount = 0;
	public static int blockDiagramCount = 0;
	public static int platformDiagramCount = 0;
	public static int gradientDiagramCount = 0;

	private static int xOffset = 0;
	private static int yOffset = 0;
	public static int counter = 0;

	private static boolean consistent = false;
	public static double offsetForLoopAtStation = 5;
	public static boolean viewAllSpeedRestrictions = true;
	public static boolean viewAllGradients = true;

	public static boolean viewInSameWindow = true;
	private static double simulationCurrentTime = 4*60;// 57;// 4*60;
	public static boolean showHidingLinks = false;
	public static boolean showSignalInfo = false;
	public static boolean showStartMilePosts = false;
	public static boolean showPlatformNames = false;
	public static boolean stationToStationScheduling = false;
	public static boolean playMode = true;
	public static boolean automaticWarningSystemOn = false;

	public static final double maxScale = 20;
	public static final double minScale = 0.1;

	public final static double SIGNAL_DISTANCE_FROM_BLOCK_END = 10;
	public static final double minimumLoopOffsetFromTop = 10;

	public final static String loopTypeMainline = "ml";
	public final static String loopTypeLoop = "loop";

	public final static double defaultLoopVelocity = 30.0;
	private static final String UP_DIRECTION_STRING = "Up";
	private static final String DOWN_DIRECTION_STRING = "Down";
	public static double MAXIMUM_LOCK_TIME = Double.POSITIVE_INFINITY;


	//
	// public static GradientDiagram getGradientDiagram() {
	// if (gradientDiagram == null)
	// gradientDiagram = new GradientDiagram();
	// return gradientDiagram;
	// }

	public static void setConsistent(boolean consistent) {
		GlobalVariables.consistent = consistent;
	}

	public static boolean isConsistent() {
		return consistent;
	}


	public static void setXOffset(int xOffset) {
		GlobalVariables.xOffset = xOffset;
	}

	public static int getXOffset() {
		return xOffset;
	}

	public static void setYOffset(int yOffset) {
		GlobalVariables.yOffset = yOffset;
	}

	public static int getYOffset() {
		return yOffset;
	}

	public static boolean isSectionViewMode() {
		return GlobalVariables.getViewMode() == GlobalVariables.VIEW_SECTION_MODE;
	}

	public static boolean isTrainViewMode() {
		return GlobalVariables.getViewMode() == GlobalVariables.VIEW_TRAINS_MODE;
	}

	public static int getViewMode() {
		return viewMode;
	}


	public static void setSimulationCurrentTime(double i) {
		simulationCurrentTime = i;
	}

	public static double getSimulationCurrentTime() {
		// return mainFrame.timeSlider.getValue()
		// / (double) mainFrame.minorTimeScale;
		return simulationCurrentTime;
	}

	public static Rectangle2D.Double getInterpolatedRect(
			Rectangle2D.Double rect1, Rectangle2D.Double rect2,
			double distance, double size, boolean fromBeginning) {
		double x1 = rect1.getCenterX();
		double x2 = rect2.getCenterX();
		double y1 = rect1.getCenterY();
		double y2 = rect2.getCenterY();

		double interpolatedY, interpolatedX;
		double length = getDistance(x1, y1, x2, y2);
		assert (length != 0);
		double forwardDistance;
		if (fromBeginning) {
			// forwardDistance = Math.abs(distance);
			forwardDistance = distance;
		} else {
			// forwardDistance = length - Math.abs(distance);
			forwardDistance = length - distance;
		}

		if (x1 == x2) {
			interpolatedX = x1;
			interpolatedY = y1 + forwardDistance;
		} else {
			double cosTheta = (x2 - x1) / length;
			double sinTheta = (y2 - y1) / length;
			interpolatedX = x1 + forwardDistance * cosTheta;
			interpolatedY = y1 + forwardDistance * sinTheta;
		}

		Rectangle2D.Double interpolatedRect = new Rectangle2D.Double();

		double centerX = interpolatedX;
		double centerY = interpolatedY;
		double cornerX = centerX + size / 2;
		double cornerY = centerY + size / 2;

		interpolatedRect.setFrameFromCenter(centerX, centerY, cornerX, cornerY);

		return interpolatedRect;
	}

	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public static double getPrettyPrinted(double value, int nDigitsAfterDecimal) {
		int multiplier = (int) Math.pow(10, nDigitsAfterDecimal);
		double returnValue = ((int) (multiplier * value)) / (double) multiplier;
		// System.out.println("value " + value + " multiplier " + multiplier
		// + " returnValue " + returnValue);
		return returnValue;
	}

	public static boolean isRoughlyEqual(double value1, double value2) {
		double approx1 = getPrettyPrinted(value1, 6);
		double approx2 = getPrettyPrinted(value2, 6);
		return approx1 == approx2;
	}

	public static String getDirectionString(int direction) {
		if (direction == GlobalVar.UP_DIRECTION) {
			return "Up";
		} else if (direction == GlobalVar.DOWN_DIRECTION) {
			return "Down";

		} else
			return "Common";
	}

	public static int getDirectionFromDirectionString(String directionString) {
		if (directionString
				.equalsIgnoreCase(GlobalVariables.UP_DIRECTION_STRING)) {
			return GlobalVar.UP_DIRECTION;
		} else if (directionString
				.equalsIgnoreCase(GlobalVariables.DOWN_DIRECTION_STRING)) {
			return GlobalVar.DOWN_DIRECTION;
		} else
			return GlobalVar.COMMON_DIRECTION;
	}


	public static double roundToThreeDecimals(double d) {
		return round(d, 4, BigDecimal.ROUND_HALF_EVEN); 
	 
	}

	public static double getXScale() {
		return xScale;
	}

	public static double getYScale() {
		return yScale;
	}


	public static double round(double unrounded, int precision, int roundingMode) {
		BigDecimal bd = new BigDecimal(unrounded);
		BigDecimal rounded = bd.setScale(precision, roundingMode);
		return rounded.doubleValue();
	}


}

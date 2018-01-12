package gui.entities.sectionEntities.trackProperties;

import globalVariables.GlobalVariables;

public class AutomaticWarningSystem {
	public static final double entryOnYellowDistance = 0.290;
	public static final double minimumDesiredBlockLength = 0.4;

	public static final double approachRedDistance = GlobalVariables
			.roundToThreeDecimals(minimumDesiredBlockLength
					- entryOnYellowDistance);
	public static final double entryOnYellowSpeedLimit = 60 / 60D;
	public static final double approachRedSpeedLimit = 38 / 60D;

}

package gui.ui.trainViewMode;

import globalVariables.GlobalVariables;

import java.awt.Color;
import java.awt.Graphics2D;

public class TimeAxis extends XGridLine {

	public TimeAxis(double startCoordinate, double endCoordinate,
			double yCoordinate) {
		this.startCoordinate = startCoordinate;
		this.endCoordinate = endCoordinate;
		this.yCoordinate = yCoordinate;
		setLine();
	}

	public void drawMe(Graphics2D g2) {
		if (!(GlobalVariables.getViewMode() == GlobalVariables.VIEW_TRAINS_MODE))
			return;

		// setVisibleFromActualCoordinates();
		g2.setColor(Color.GRAY);
		g2.draw(line);
		g2.setColor(Color.BLACK);
	}

}

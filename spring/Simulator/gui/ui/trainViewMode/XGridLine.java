package gui.ui.trainViewMode;

import globalVariables.GlobalVariables;

import java.awt.geom.Line2D;

public class XGridLine {
	protected Line2D.Double line = new Line2D.Double();
	protected double startCoordinate = 10;
	protected double endCoordinate = 500;
	protected double yCoordinate = 70;

	// protected Marker

	protected void setLine() {
		line.x1 = startCoordinate;
		line.y1 = yCoordinate;
		line.y2 = yCoordinate;
		line.x2 = endCoordinate;
	}

	protected void setActualFromVisibleCoordinates() {
		int xOffset = GlobalVariables.getXOffset();
		int yOffset = GlobalVariables.getYOffset();
		line.x1 = startCoordinate + xOffset;
		line.x2 = endCoordinate + xOffset;
		line.y1 = yCoordinate + yOffset;
		line.y2 = yCoordinate + yOffset;
	}

	protected void setVisibleFromActualCoordinates() {
		int xOffset = GlobalVariables.getXOffset();
		int yOffset = GlobalVariables.getYOffset();
		line.x1 = startCoordinate - xOffset;
		line.x2 = endCoordinate - xOffset;
		line.y1 = yCoordinate - yOffset;
		line.y2 = yCoordinate - yOffset;
	}

	protected void printCoordinates() {
		System.out.println("timeAxis: printCoordinates: " + line.x1 + " "
				+ line.x2 + " " + line.y1);
	}

	public void drawMe() {

	}
}

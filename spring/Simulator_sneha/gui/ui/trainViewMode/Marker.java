package gui.ui.trainViewMode;

import globalVariables.GlobalVariables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Marker {
	public final static int S = 6;
	private Rectangle2D.Double visibleRect = new Rectangle2D.Double();
	private Rectangle2D.Double actualRect = new Rectangle2D.Double();
	private boolean highlighting = false;

	public Marker(double x, double y) {
		visibleRect.setFrameFromCenter(x, y, x + S / 2, y + S / 2);
	}

	private void setActualRectFromVisibleRect() {
		int xOffset = GlobalVariables.getXOffset();
		int yOffset = GlobalVariables.getYOffset();
		Rectangle2D.Double v = visibleRect;
		actualRect.setFrame(v.x + xOffset, v.y + yOffset, v.width, v.height);
	}

	private void setVisibleRectFromActualRect() {
		int xOffset = GlobalVariables.getXOffset();
		int yOffset = GlobalVariables.getYOffset();
		Rectangle2D.Double a = actualRect;
		visibleRect.setFrame(a.x - xOffset, a.y - yOffset, a.width, a.height);
	}

	public void setHighlighting(boolean highlighting) {
		this.highlighting = highlighting;
	}

	public boolean isHighlighting() {
		return highlighting;
	}

	public void drawMe(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		if (highlighting) {
			g2.setColor(Color.YELLOW);
		}

		g2.draw(visibleRect);
		g2.setColor(Color.BLACK);
	}

}

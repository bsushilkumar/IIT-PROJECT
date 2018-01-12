package simulator.outputFeatures.graph;

import java.awt.Graphics2D;

import simulator.util.Debug;

/**
 * LineLayer
 */
public class LineLayer extends Layer {
	/**
	 * xShift
	 */
	double xShift;
	/**
	 * yShift
	 */
	double yShift;

	/**
	 * 
	 */
	public LineLayer() {
		super();
		xShift = 0;
		yShift = 0;
	}

	/**
	 * This method sets parameter which determines by how much the whole layer
	 * should be shifted by along the x axis. A positive number will shift it
	 * towards the positive side of the axis and a negative number will shift it
	 * towards the negetive side if the axis default is 0
	 * 
	 * @param x
	 */
	public void setXShift(double x) {
		xShift = x;
	}

	/**
	 * @return xShift
	 */
	public double getXShift() {
		return (xShift);
	}

	/**
	 * This method sets parameter which determines by how much the whole layer
	 * should be shifted by along the y axis. A positive number will shift it
	 * towards the positive side of the axis and a negative number will shift it
	 * towards the negative side if the axis. default is 0
	 * 
	 * @param y
	 */
	public void setYShift(double y) {
		yShift = y;
	}

	/**
	 * @return yShift
	 */
	public double getYShift() {
		return (yShift);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see graph.Layer#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g2) {
		if (visible) {
			g2.setStroke(stroke);
			Debug.print("There are " + curves.size() + " in this layer");
			for (int i = 0; i < curves.size(); i++) {
				DrawObject c;
				c = curves.get(i);
				((GLine) c).addXShift(xShift);
				((GLine) c).addYShift(yShift);
				c.setScale(xScale, yScale);
				// g2.setPaint(c.getPaint());
				// System.out.println("layer calling draw of drawobject");
				c.draw(g2);
				((GLine) c).addXShift(-xShift);
				((GLine) c).addYShift(-yShift);
			}
		}

	}
}

package simulator.outputFeatures.graph;

import java.awt.Color;
import java.awt.Paint;

//can output error to indicate that the user has not called the set origin
//function and the set mark function
/**
 * 
 */
abstract public class GridLine extends DrawObject {
	/**
	 * mark
	 */
	protected double mark;
	/**
	 * max
	 */
	protected double max;
	/**
	 * xOrigin
	 */
	protected double xOrigin;
	/**
	 * yOrigin
	 */
	protected double yOrigin;
	/**
	 * xShift
	 */
	protected double xShift;
	/**
	 * yShift
	 */
	protected double yShift;

	/**
	 * 
	 */
	public GridLine() {
		setPaint(Color.gray);
	}

	/**
	 * @param mark
	 * @param name
	 */
	public GridLine(double mark, String name) {
		super(name);
		this.mark = mark;
		setPaint(Color.gray);
	}

	/**
	 * @param mark
	 * @param p
	 */
	public GridLine(double mark, Paint p) {
		super(p);
		this.mark = mark;
		setPaint(Color.gray);
	}

	/**
	 * @param mark
	 * @param name
	 * @param p
	 */
	public GridLine(double mark, String name, Paint p) {
		super(name, p);
		this.mark = mark;
		setPaint(Color.gray);
	}

	/**
	 * @param mark
	 */
	public GridLine(double mark) {
		super();
		setPaint(Color.gray);
		this.mark = mark;
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
	 * towards the positive side of the axis and a negetive number will shift it
	 * towards the negetive side if the axis. default is 0
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
	 * @return mark
	 */
	public double getMark() {
		return mark;
	}

	/**
	 * @param mark
	 */
	public void setMark(double mark) {
		this.mark = mark;
	}

	/**
	 * @return yOrigin
	 */
	public double getYOrigin() {
		return yOrigin;
	}

	/**
	 * @return xOrigin
	 */
	public double getXOrigin() {
		return xOrigin;
	}

	/**
	 * @param x
	 * @param y
	 */
	public void setOrigin(double x, double y) {
		xOrigin = x;
		yOrigin = y;
	}

	/**
	 * @param max
	 */
	public void setMax(double max) {
		this.max = max;
	}

	/**
	 * @return max
	 */
	public double getMax() {
		return max;
	}

}

package simulator.outputFeatures.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

/**
 * 
 */
public abstract class DrawObject {
	/**
	 * name
	 */
	String name;
	/**
	 * paint
	 */
	Paint paint;
	/**
	 * xScale
	 */
	protected double xScale;
	/**
	 * yScale
	 */
	protected double yScale;

	/**
	 * 
	 */
	public DrawObject() {
		paint = Color.black;
		name = "";
	}

	/**
	 * @param p
	 */
	public DrawObject(Paint p) {
		paint = p;
		name = "";
	}

	/**
	 * @param s
	 */
	public DrawObject(String s) {
		paint = Color.black;
		name = s;
	}

	/**
	 * @param s
	 * @param p
	 */
	public DrawObject(String s, Paint p) {
		paint = p;
		name = s;
	}

	/**
	 * Scale the graph. everything in the graph paper is scaled, including the
	 * strings. Later on a magnify method may be provided for magnifying a
	 * portion of the graph. every time it is called the scale is set to the one
	 * specified by the one in the parameters.
	 * 
	 * @param x
	 * @param y
	 */
	public void setScale(double x, double y) {
		xScale = x;
		yScale = y;
	}

	/**
	 * gets the scaling factor in the x direction
	 * 
	 * @return xscale
	 */
	public double getXScale() {
		return xScale;
	}

	/**
	 * gets the scaling factor in the Y direction
	 * 
	 * @return yScale
	 */
	public double getYScale() {
		return yScale;
	}

	/**
	 * This method will reset the curve to empty. This will also reset the paint
	 * to black color
	 */
	public void reset() {
		paint = Color.black;
		name = "";
	}

	/**
	 * This sets the color in which the curve should be painted.
	 * 
	 * @param p
	 */
	public void setPaint(Paint p) {
		paint = p;
	}

	/**
	 * This gets the color in which the curve is being painted.
	 * 
	 * @return Paint
	 */
	public Paint getPaint() {
		return paint;
	}

	/**
	 * This sets the name of the curve.
	 * 
	 * @param s
	 */
	public void setName(String s) {
		name = s;
	}

	/**
	 * This gets the name of the curve.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param g2
	 */
	abstract public void draw(Graphics2D g2);
}

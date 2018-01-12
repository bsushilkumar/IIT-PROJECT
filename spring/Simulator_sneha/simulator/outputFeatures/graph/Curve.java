package simulator.outputFeatures.graph;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.GeneralPath;

import simulator.util.Debug;

/** This class does not implement the scaling methods........ */
public class Curve extends DrawObject {
	/**
	 * path
	 */
	protected GeneralPath path;

	/**
	 * 
	 */
	public Curve() {
		super();
		path = new GeneralPath();
	}

	/**
	 * @param p
	 */
	public Curve(Paint p) {
		super(p);
		path = new GeneralPath();

	}

	/**
	 * @param s
	 */
	public Curve(String s) {
		super(s);
		path = new GeneralPath();
	}

	/**
	 * @param s
	 * @param p
	 */
	public Curve(String s, Paint p) {
		super(s, p);
		path = new GeneralPath();
	}

	/**
	 * @param x
	 * @param y
	 */
	public void lineTo(float x, float y) {
		path.lineTo(x, y);
	}

	// there vis a need for move to functon

	/** Adds a point to the path by moving to the specified coordinates. 
	 * @param x 
	 * @param y */
	public void moveTo(float x, float y) {
		path.moveTo(x, y);
	}

	/**
	 * This method will reset the curve to empty. This will also reset the paint
	 * to black color
	 */
	public void reset() {
		super.reset();
		path.reset();
	}

	/** This method will reset the points in the to empty */
	public void resetPath() {
		path.reset();
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void quadTo(float x1, float y1, float x2, float y2) {
		path.quadTo(x1, y1, x2, y2);
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public void curveTo(float x1, float y1, float x2, float y2, float x3,
			float y3) {
		path.curveTo(x1, y1, x2, y2, x3, y3);
	}

	
	/** (non-Javadoc)
	 * @see graph.DrawObject#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g2) {
		Debug.print("Curve will  be printing generalpath");
		g2.setPaint(paint);
		g2.draw(path);
	}

}

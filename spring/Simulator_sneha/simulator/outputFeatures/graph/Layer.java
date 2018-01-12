package simulator.outputFeatures.graph;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import simulator.util.Debug;

/**
 * 
 */
public abstract class Layer {
	/**
	 * curves
	 */
	protected ArrayList<DrawObject> curves;
	/**
	 * stroke
	 */
	protected Stroke stroke;
	/**
	 * visible
	 */
	protected boolean visible;
	/**
	 * xScale
	 */
	protected double xScale;
	/**
	 * yScale
	 */
	protected double yScale;

	// protected double
	// xTemaximumPossibleSpeedcale=1,yTemaximumPossibleSpeedcale=1;

	/**
	 * 
	 */
	public Layer() {
		stroke = DrawStyle.stroke;
		curves = new ArrayList<DrawObject>();
		visible = true;
	}

	/**
	 * @param crv
	 */
	public void add(DrawObject crv) {
		curves.add(crv);
	}

	/*
	 * public void addAll(Collection c) { curves.addAll(c); }
	 */
	/**
	 * 
	 */
	public void clear() {
		curves.clear();
	}

	/**
	 * @param crv
	 * @return contains the curve
	 */
	public boolean contains(DrawObject crv) {
		return (curves.contains(crv));
	}

	/**
	 * @param index
	 * @return ith element of curves
	 */
	public DrawObject get(int index) {
		return curves.get(index);
	}

	/**
	 * @return true if curves is empty
	 */
	public boolean isEmpty() {
		return curves.isEmpty();
	}

	/**
	 * @param index
	 * @return remove the ith element from curves
	 */
	public DrawObject remove(int index) {
		return curves.remove(index);
	}

	/**
	 * @return size of curves
	 */
	public int size() {
		return curves.size();
	}

	/**
	 * Scale the graph. everything in the graph paer is scaled, including the
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
	 * @return xScale
	 * */
	public double getXScale() {
		return xScale;
	}

	/**
	 * gets the scaling factor in the Y direction
	 * 
	 * @return yScale
	 * */
	public double getYScale() {
		return yScale;
	}

	/**
	 * @param s
	 */
	public void setStroke(Stroke s) {
		stroke = s;
	}

	/**
	 * @return getStroke
	 */
	public Stroke getStroke() {
		return stroke;
	}

	/**
	 * @param b
	 */
	public void setVisible(boolean b) {
		visible = b;
	}

	/**
	 * @return visibility
	 */
	public boolean getVisible() {
		return visible;
	}

	/**
	 * @param g2
	 */
	public void draw(Graphics2D g2) {
		Debug.print("LAYER from base class layer calling draw of drawobject");
		if (visible) {
			g2.setStroke(stroke);
			Debug.print("There are " + curves.size() + " in this layer");
			for (int i = 0; i < curves.size(); i++) {
				DrawObject c;
				c = curves.get(i);
				c.setScale(xScale, yScale);
				// c.setScale(xScale*xTemaximumPossibleSpeedcale,yScale*yTemaximumPossibleSpeedcale);
				// g2.setPaint(c.getPaint());
				Debug.print("layer calling draw of drawobject");
				c.draw(g2);
			}
		}
	}
	/*
	 * public void draw(Graphics2D g2,double xSl,double ySl){
	 * xTemaximumPossibleSpeedcale = xSl; yTemaximumPossibleSpeedcale = ySl;
	 * this.draw(g2); xTemaximumPossibleSpeedcale = 1;
	 * yTemaximumPossibleSpeedcale = 1; }
	 */
}

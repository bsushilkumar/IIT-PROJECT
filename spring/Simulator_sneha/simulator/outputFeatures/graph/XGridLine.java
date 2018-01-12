package simulator.outputFeatures.graph;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

/**
 * 
 */
public class XGridLine extends GridLine {
	/**
	 * Tx
	 */
	AffineTransform Tx;
	/**
	 * TShift
	 */
	AffineTransform TShift;

	/**
	 * 
	 */
	public XGridLine() {
		super();
	}

	/**
	 * @param mark
	 * @param name
	 */
	public XGridLine(double mark, String name) {
		super(mark, name);
	}

	/**
	 * @param mark
	 * @param p
	 */
	public XGridLine(double mark, Paint p) {
		super(mark, p);
	}

	/**
	 * @param mark
	 * @param name
	 * @param p
	 */
	public XGridLine(double mark, String name, Paint p) {
		super(mark, name, p);
	}

	/**
	 * @param mark
	 */
	public XGridLine(double mark) {
		super(mark);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see graph.DrawObject#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g2) {
		g2.setPaint(getPaint());
		// g2.draw(new Line2D.Double(xOrigin+getMark(),yOrigin,xOrigin+getMark()
		// ,yOrigin+getMax()));
		g2.draw(new Line2D.Double(getMark() * xScale, 0, getMark() * xScale,
				getMax() * yScale));
		Tx = new AffineTransform();
		TShift = new AffineTransform();
		Tx.setTransform(1, 0, 0, -1, 0, 0);
		TShift.translate(0, -20);
		g2.transform(TShift);
		g2.transform(Tx);
		g2.drawString(getName(), (float) (getMark() * xScale), 0);
		Tx = new AffineTransform();
		TShift = new AffineTransform();
		Tx.setTransform(1, 0, 0, -1, 0, 0);
		TShift.translate(0, -20);
		g2.transform(TShift);
		g2.transform(Tx);
	}

}

package simulator.outputFeatures.graph;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import simulator.util.Debug;

/**
 * This is the paper class. This class is the graph paper and every thing has to
 * be put in it. One can add layers. Scaling is provided.
 */
@SuppressWarnings("serial")
public class Paper extends Canvas implements MouseMotionListener, MouseListener {
	final double originalXScale = 2;
	/**
	 * xScale
	 */
	public double xScale = originalXScale;
	/**
	 * yScale
	 */
	public double yScale;
	/**
	 * xOrigin
	 */
	float xOrigin;
	/**
	 * yOrigin
	 */
	float yOrigin;
	/**
	 * layers
	 */
	private ArrayList<Layer> layers;
	/**
	 * signalLayer
	 */
	private LineLayer signalLayer = new LineLayer();
	/**
	 * profLayer
	 */
	private LineLayer profileLayer = new LineLayer();
	/**
	 * Tx
	 */
	AffineTransform Tx;
	/**
	 * TShift
	 */
	AffineTransform TShift;
	/**
	 * TScale
	 */
	AffineTransform TScale;
	/**
	 * TOrigin
	 */
	AffineTransform TOrigin;
	/**
	 * transformed
	 */
	boolean transformed;
	/**
	 * xGrid
	 */
	GridLayer xGrid;
	/**
	 * yGrid
	 */
	GridLayer yGrid;
	protected GridLayer timeGrid;
	/**
	 * pointLayer
	 */
	LineLayer pointLayer;
	/**
	 * xPoint
	 */
	GLine xPoint;
	/**
	 * yPoint
	 */
	GLine yPoint;
	/**
	 * mouseTrigger
	 */
	boolean mouseTrigger;

	/** This is the only available constructor */
	public Paper() {
		layers = new ArrayList<Layer>();
		Tx = new AffineTransform();
		TShift = new AffineTransform();
		TScale = new AffineTransform();
		TOrigin = new AffineTransform();
		Tx.setTransform(1, 0, 0, -1, 0, 0);
		TScale.scale(1, 1);
		xScale = 1;
		yScale = 1;
		TShift.translate(0, 0);
		transformed = false;
		addMouseMotionListener(this);
		addMouseListener(this);
		pointLayer = new LineLayer();
		pointLayer.setVisible(false);
		xPoint = new GLine();
		xPoint.add(new Marker(0, 0));
		xPoint.add(new Marker(0, 0));
		yPoint = new GLine();
		yPoint.add(new Marker(0, 0));
		yPoint.add(new Marker(0, 0));
		pointLayer.add(xPoint);
		pointLayer.add(yPoint);
		// addLayer(pointLayer);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent m) {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent m) {
		Debug.print("mouse pressed");
		int x = m.getX();
		int y = m.getY();
		mouseTrigger = true;
		pointLayer.clear();
		pointLayer.setVisible(true);
		xPoint = new GLine();
		xPoint.add(new Marker((x - xOrigin) / xScale, 0));
		xPoint.add(new Marker((x - xOrigin) / xScale, getHeight()));
		yPoint = new GLine();
		yPoint.add(new Marker(0, (getHeight() - y - yOrigin) / yScale));
		yPoint.add(new Marker(getWidth(), (getHeight() - y - yOrigin) / yScale));
		pointLayer.add(xPoint);
		pointLayer.add(yPoint);
		// repaint();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent m) {
		Debug.print("mouse draged");
		int x = m.getX();
		int y = m.getY();
		if (mouseTrigger) {
			pointLayer.clear();
			pointLayer.setVisible(true);
			xPoint = new GLine();
			xPoint.add(new Marker((x - xOrigin) / xScale, 0));
			xPoint.add(new Marker((x - xOrigin) / xScale, getHeight()));
			yPoint = new GLine();
			yPoint.add(new Marker(0, (getHeight() - y - yOrigin) / yScale));
			yPoint.add(new Marker(getWidth(), (getHeight() - y - yOrigin)
					/ yScale));
			pointLayer.add(xPoint);
			pointLayer.add(yPoint);
			/*
			 * Graphics2D g2 = (Graphics2D)this.getGraphics();
			 * g2.setTransform(TShift); g2.transform(Tx); g2.transform(TOrigin);
			 * g2.setStroke(drawStyle.stroke);
			 * pointLayer.setScale(xScale,yScale); pointLayer.draw(g2);
			 */
			// repaint();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent m) {
		Debug.print("mouse released");
		mouseTrigger = false;
		pointLayer.clear();
		pointLayer.setVisible(false);
		xPoint = new GLine();
		xPoint.add(new Marker(0, 0));
		xPoint.add(new Marker(0, getHeight()));
		yPoint = new GLine();
		yPoint.add(new Marker(0, 0));
		yPoint.add(new Marker(getWidth(), 0));
		pointLayer.add(xPoint);
		pointLayer.add(yPoint);
		// repaint();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent m) {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent m) {
		/*
		 * if(origShown) { int x = m.getX(); int y = m.getY(); if(mouseTrigger)
		 * { validateMouse(x, y); newRectX = eventX; newRectY = eventY;
		 * drawTwoRects(); rectShow = true; } }
		 */
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent m) {
	}

	/**
	 * Add a layer.
	 * 
	 * @param l
	 */
	public void addLayer(Layer l) {
		layers.add(l);
	}

	/**
	 * Remove a layer.
	 * 
	 * @param l
	 */
	public void removeLayer(Layer l) {
		layers.remove(l);
	}

	/**
	 * Scale the graph. everything in the graph paer is scaled, including the
	 * strings. Later on a mgnify method may be provided for magnifying a
	 * portion of the graph. every time it is called the scale is set to the one
	 * sopecified by the one in the parameters.
	 * 
	 * @param x
	 * @param y
	 */
	public void setScale(double x, double y) {
		xScale = x;
		yScale = y;
		TScale = new AffineTransform();
		TScale.scale(x, y);
	}

	/**
	 * gets the scaling factor in the x direction
	 * 
	 * @return xScale
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
	 * This sets the Origin with respect to the bottom left corner of the paper.
	 * The utility of this method is to set the origin so that the strings
	 * displayed with the x and y axis are displayed properly without
	 * overlapping
	 * 
	 * @param x
	 * @param y
	 */
	public void setOrigin(float x, float y) {
		xOrigin = x;
		yOrigin = y;
		TOrigin = new AffineTransform();
		TOrigin.translate(x, y);
	}

	/**
	 * Sets the grid for the X axis
	 * 
	 * @param x
	 */
	public void setXGrid(GridLayer x) {
		xGrid = x;
	}

	/**
	 * Sets the grid for the Y axis
	 * 
	 * @param y
	 */
	public void setYGrid(GridLayer y) {
		yGrid = y;
	}

	/**
	 * Sets the grid layers of the paper. These layers should contain the grid
	 * lines which will draw the grid for the graph.
	 * 
	 * @param x
	 * @param y
	 */
	public void setGrid(GridLayer x, GridLayer y) {
		xGrid = x;
		yGrid = y;
	}

	/**
	 * gets the instance of the grid layer.
	 * 
	 * @return new instance of gridlayer
	 */
	public GridLayer getGridInstance() {
		return (new GridLayer());
	}

	/**
	 * Sets the grid for the Y axis
	 * 
	 * @return yGrid
	 */
	public GridLayer getYGrid() {
		return yGrid;
	}

	/**
	 * Sets the grid for the X axis
	 * 
	 * @return xGrid
	 */
	public GridLayer getXGrid() {
		return xGrid;
	}

	/** This is the methos that should be called to draw the graph. */
	public void drawGraph() {
		TShift = new AffineTransform();
		TShift.translate(0, getHeight());
		repaint();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Canvas#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		Graphics2D g2;

		g2 = (Graphics2D) g;
		Debug.print("For layer " + transformed + " of " + getHeight()
				+ " calling draw");
		g2.setTransform(TShift);
		g2.transform(Tx);
		// g2.transform(TScale);
		g2.transform(TOrigin);
		g2.setStroke(DrawStyle.stroke);
		g2.setPaint(Color.black);
		g2.draw(new Line2D.Double(0, 0, getWidth(), 0));
		g2.draw(new Line2D.Double(0, 0, 0, getHeight()));
		if (xGrid != null) {
			xGrid.setMax(getHeight());
			xGrid.setScale(xScale, yScale);
			xGrid.draw(g2);
			// xGrid.draw(g2,xScale,yScale);
		}

		if (yGrid != null) {
			yGrid.setMax(getWidth());
			yGrid.setScale(originalXScale, yScale);
			yGrid.draw(g2);
			// yGrid.draw(g2,xScale,yScale);
		}

		if (timeGrid != null) {
			timeGrid.setMax(getWidth());
			timeGrid.setScale(1, yScale);
			timeGrid.draw(g2);
		}

		for (int i = 0; i < layers.size(); i++) {
			Debug.print("For layer " + i + " of " + layers.size()
					+ " calling draw");
			Layer l = layers.get(i);
			l.setScale(xScale, yScale);
			l.draw(g2);
		}

		getSignalLayer().setScale(originalXScale, yScale);
		getProfileLayer().setScale(originalXScale, yScale);

		getSignalLayer().draw(g2);
		getProfileLayer().draw(g2);

	}

	public LineLayer getProfileLayer() {
		return profileLayer;
	}

	public void setProfileLayer(LineLayer profileLayer) {
		this.profileLayer = profileLayer;
	}

	public LineLayer getSignalLayer() {
		return signalLayer;
	}

	public void setSignalLayer(LineLayer signalLayer) {
		this.signalLayer = signalLayer;
	}

	/**
	 * 
	 */
	// make this a inner class of graph paper

	public class GridLayer extends Layer {
		public double lateralShift = 0;
		/**
		 * Max
		 */
		private double Max;

		/**
		 * 
		 */
		public GridLayer() {
			super();
			setStroke(DrawStyle.thinStroke);
		}

		/**
		 * @param d
		 */
		public void setMax(double d) {
			Max = d;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see graph.Layer#draw(java.awt.Graphics2D)
		 */
		public void draw(Graphics2D g2) {
			if (visible) {
				g2.setStroke(stroke);
				for (int i = 0; i < curves.size(); i++) {
					GridLine gl = (GridLine) curves.get(i);
					gl.setOrigin(xOrigin, yOrigin);
					gl.setXShift(lateralShift);
					gl.setMax(Max);
					gl.setScale(super.xScale, super.yScale);
					// gl.setOrigin(xOrigin,yOrigin);
					gl.draw(g2);
				}
			}
		}

	}

}

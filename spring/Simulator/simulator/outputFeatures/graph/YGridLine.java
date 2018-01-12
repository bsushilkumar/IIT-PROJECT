package simulator.outputFeatures.graph;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;


/**
 * 
 */
public class YGridLine extends GridLine{
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
	public YGridLine(){
		  super();
	 }

	 /**
	 * @param mark
	 * @param name
	 */
	public YGridLine(double mark,String name){
		  super(mark,name);
	 }

	 /**
	 * @param mark
	 * @param p
	 */
	public YGridLine(double mark,Paint p){
		  super(mark,p);
	 }

	 /**
	 * @param mark
	 * @param name
	 * @param p
	 */
	public YGridLine(double mark,String name,Paint p){
		  super(mark,name,p);
	 }
	 
	 /**
	 * @param mark
	 */
	public YGridLine(double mark){
		  super(mark);
	 }

	 /** (non-Javadoc)
	 * @see graph.DrawObject#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g2){
		  g2.setPaint(getPaint());
		 //g2.draw(new Line2D.Double(xOrigin,yOrigin+getMark(),xOrigin+getMax() ,yOrigin+getMark()));
		 g2.draw(new Line2D.Double(0,getMark()*yScale,getMax()*xScale ,getMark()*yScale));
		  Tx = new AffineTransform();
		  TShift = new AffineTransform();
		  Tx.setTransform(1,0,0,-1,0,0);
		  TShift.translate(-getXOrigin(),(2*(getMark()*yScale)));
		  g2.transform(TShift);
		  g2.transform(Tx);
		 g2.drawString(getName(),5,(float)(getMark()*yScale));
		  Tx = new AffineTransform();
		  TShift = new AffineTransform();
		  Tx.setTransform(1,0,0,-1,0,0);
		  TShift.translate(getXOrigin(),2*getMark()*yScale);
		  g2.transform(TShift);
		  g2.transform(Tx);
	 }
}

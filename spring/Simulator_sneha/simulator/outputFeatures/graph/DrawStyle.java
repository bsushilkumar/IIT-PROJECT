package simulator.outputFeatures.graph;
import java.awt.BasicStroke;

/**
 * 
 */
public class DrawStyle
{
	 /**
	 * stroke
	 */
	final public static BasicStroke stroke = new BasicStroke(2.0f);
	 /**
	 * thinStroke
	 */
	final public static BasicStroke thinStroke = new BasicStroke(1.0f);
	 /**
	 * wideStroke
	 */
	final public static BasicStroke wideStroke = new BasicStroke(8.0f);
  
	 /**
	 * dash1
	 */
	final public static float dash1[] = {10.0f};
	 
	 /**
	 * dashed
	 */
	final public static BasicStroke dashed = new BasicStroke(1.0f, 
			   BasicStroke.CAP_BUTT, 
			   BasicStroke.JOIN_MITER, 
			   10.0f, dash1, 0.0f);
}

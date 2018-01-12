package input;

import java.awt.Dialog;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class InputDialog extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int x = 100;
	public int y = 100;
	public int width = 500;
	public int height = 500;
	public int x1 = 20;
	public int y1 = 20;
	public int labelWidth = 150;
	
	public int labelHeight = 30;
	public int fieldWidth = 100;
	public int fieldHeight = 20;
	public int buttonWidth = 150;
	public int buttonHeight = 20;
	public int heightDifference = 40;
	int x2 = x1 + labelWidth + 20;
	int x3 = x2 + fieldWidth + 20;
	int x4 = x1 + 20;

	public JButton okButton = new JButton("Ok");
	public JButton cancelButton = new JButton("Cancel");
	public JPanel jpanel = new JPanel();
	
	public InputDialog(){
		
		
	}

	public abstract void addComponents() ;

	public abstract void setComponentBounds() ;

	public abstract void setComponentActionListeners() ;

}
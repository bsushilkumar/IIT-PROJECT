package simulator.ui;

import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 
 */
@SuppressWarnings("serial")
public class StartUpScreen extends JFrame {

	/**
	 * jLabel1
	 */
	JLabel jLabel1;
	/**
	 * jLabel2
	 */
	JLabel jLabel2;

	/**
     * 
     */
	public StartUpScreen() {
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     */
	private void jbInit() {
		jLabel1.setFont(new Font("Monospaced", 1, 20));
		jLabel1.setHorizontalAlignment(0);
		jLabel1.setText("SimulatorTrain Simulation Software");
		jLabel1.setBounds(new Rectangle(17, 5, 330, 133));
		getContentPane().setLayout(null);
		jLabel2.setFont(new Font("Serif", 1, 18));
		jLabel2.setHorizontalAlignment(0);
		jLabel2.setText("IIT Bombay");
		jLabel2.setBounds(new Rectangle(98, 337, 170, 40));
		getContentPane().add(jLabel1, null);
		getContentPane().add(jLabel2, null);
	}
}

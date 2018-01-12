package simulator.outputFeatures;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import simulator.dispatcher.FreightSimulator;
import simulator.input.SimulationInstance;
import simulator.outputFeatures.graph.Paper;

public class GraphFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * graphPanel
	 */
	GraphPanel graphPanel;

	public SearchFrame searchFrame = null;

	/**
	 * @param freightSimulator
	 */
	public GraphFrame(FreightSimulator freightSimulator,
			final SimulationInstance simulationInstance) {
		super("Graph");
		this.setFocusable(true);
		GraphKeyListener graphKeyListener = new GraphKeyListener(this,
				simulationInstance);
		graphPanel = new GraphPanel(freightSimulator, this, graphKeyListener,
				simulationInstance);
		graphKeyListener.trainGraph = graphPanel.trainGraph;
		this.addKeyListener(graphKeyListener);
		getContentPane().setLayout(null);

		graphPanel.setBounds(0, 0, 1200, 1200);
		setBounds(20, 20, 1150, 700);
		getContentPane().add(graphPanel);
		requestFocusInWindow();

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				if (simulationInstance.hasRailClipse == false)
					System.exit(0);
			}
		});
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				graphPanel.setBounds(0, 0, getWidth(), getHeight());
			}
		});

		setVisible(true);
	}

	/**
	 * @return {@link Paper}
	 */
	public Paper getPaper() {
		return graphPanel.getPaper();
	}
}
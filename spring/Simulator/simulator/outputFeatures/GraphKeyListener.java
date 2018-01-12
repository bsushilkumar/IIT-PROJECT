package simulator.outputFeatures;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import simulator.input.SimulationInstance;

public class GraphKeyListener implements KeyListener {
	private GraphPanel graphPanel = null;
	public GraphPaper trainGraph = null;
	private GraphFrame graphFrame = null;
	private SimulationInstance simulationInstance = null;

	public GraphKeyListener(GraphFrame graphFrame,
			SimulationInstance simulationInstance) {
		this.graphFrame = graphFrame;
		this.graphPanel = graphFrame.graphPanel;
		this.simulationInstance = simulationInstance;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!e.isControlDown() && !e.isShiftDown() && !e.isAltDown()) {
			int changeValue = 25;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				trainGraph.xStart += changeValue;
				break;
			case KeyEvent.VK_RIGHT:
				trainGraph.xStart += changeValue;
				break;
			case KeyEvent.VK_UP:
				trainGraph.xStart += changeValue;
				break;
			case KeyEvent.VK_LEFT:
				trainGraph.xStart = Math
						.max(0, trainGraph.xStart - changeValue);
				break;
			case KeyEvent.VK_N:
				
				break;
			}

			trainGraph.adjustLayers();
		}

		if (e.isControlDown() && !e.isAltDown() && !e.isShiftDown()) {
			double newXScale = trainGraph.xScale, newYScale = trainGraph.yScale;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_EQUALS:
				newXScale = trainGraph.xScale + 0.3;
				newYScale = trainGraph.yScale + 0.3;
				trainGraph.xScale = newXScale;
				trainGraph.setScale(newXScale, newYScale);
				trainGraph.redrawGraph();
				break;
			case KeyEvent.VK_MINUS:
				newXScale = Math.max(0.1, trainGraph.xScale - 0.3);
				newYScale = Math.max(0.1, trainGraph.yScale - 0.3);
				trainGraph.setScale(newXScale, newYScale);
				trainGraph.redrawGraph();
				break;
			case KeyEvent.VK_X:
				newXScale = trainGraph.xScale + 0.3;
				trainGraph.setScale(newXScale, newYScale);
				trainGraph.redrawGraph();
				break;
			case KeyEvent.VK_Y:
				newYScale = trainGraph.yScale + 0.3;
				trainGraph.setScale(newXScale, newYScale);
				trainGraph.redrawGraph();
				break;

			case KeyEvent.VK_F:
				SearchFrame searchFrame = graphFrame.searchFrame;
				if (searchFrame == null) {
					searchFrame = new SearchFrame(graphFrame,
							simulationInstance);
					graphFrame.searchFrame = searchFrame;
				}

				searchFrame.setVisible(true);
			}
		}

		if (e.isControlDown() && !e.isAltDown() && e.isShiftDown()) {
			double newXScale = trainGraph.xScale, newYScale = trainGraph.yScale;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_X:
				newXScale = Math.max(0.1, trainGraph.xScale - 0.3);
				trainGraph.setScale(newXScale, newYScale);
				trainGraph.redrawGraph();
				break;
			case KeyEvent.VK_Y:
				newYScale = Math.max(0.1, trainGraph.yScale - 0.3);
				trainGraph.setScale(newXScale, newYScale);
				trainGraph.redrawGraph();
				break;
			}

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
//	public ActionListener nextTrainActionListener = 

}

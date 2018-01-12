package input;

import globalVariables.GlobalVar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InputFiles extends JFrame {
	// public JLabel testCaseDirectoryLabel = new JLabel("Directory");
	// public JTextField testCaseDirectoryField = new JTextField();
	// public JButton chooseDirectoryButton = new JButton("Choose");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JButton stationButton = new JButton("Station,Loop,Block ");
	public JButton blockButton = new JButton("Block / Loop");
	public JButton parameterButton = new JButton("Parameters");
	public JButton gradientButton = new JButton("Gradient");
	public JButton gradientEffectButton = new JButton("Gradient Effects");
	public JButton trainButton = new JButton("Trains");
    public JLabel RDSO = new JLabel(" A RDSO Property.");
	
	private int labelWidth = 70;
	private int y1 = 20;
	private int x1 = 40;
	private int buttonHeight = 30;
	private int buttonWidth = 190;

	
	public InputFiles() {
		this.setTitle("Section Data");
		this.setBounds(20, 20, 300, 400);

		setComponentActionListeners();
		setComponentBounds();
		addComponents();
		this.add(new JPanel());

		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setComponentActionListeners() {
		// chooseDirectoryButton.addActionListener(chooseDirectoryActionListener);
		stationButton.addActionListener(stationActionListener);
		blockButton.addActionListener(blockActionListener);
		trainButton.addActionListener(trainActionListener);
		parameterButton.addActionListener(parameterActionListener);
		gradientButton.addActionListener(gradientActionListener);
		gradientEffectButton.addActionListener(gradientEffectsActionListener);
	}

	private void addComponents() {
		//this.getContentPane().add(RDSO);
		// this.getContentPane().add(testCaseDirectoryField);
		// this.getContentPane().add(chooseDirectoryButton);
//		this.getContentPane().add(blockButton);

    	this.getContentPane().add(stationButton);
		this.getContentPane().add(parameterButton);
		this.getContentPane().add(gradientButton);
		this.getContentPane().add(gradientEffectButton);
		this.getContentPane().add(trainButton);

	}

	private void setComponentBounds() {
		// testCaseDirectoryLabel.setBounds(x1, y1, labelWidth, labelHeight);
		// testCaseDirectoryField.setBounds(x2, y1, fieldWidth, fieldHeight);
		// chooseDirectoryButton.setBounds(x2 + fieldWidth + 20, y1,
		// buttonWidth,
		// buttonHeight);
		
		int yDifference = 40;

	//	y1 += yDifference;
	//	RDSO.setBounds(x1, y1, 150,50);

		y1 += yDifference;
		stationButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		parameterButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		gradientButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		gradientEffectButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		trainButton.setBounds(x1, y1, buttonWidth, buttonHeight);

	}

	// ActionListener chooseDirectoryActionListener = new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	//
	// // JOptionPane.showMessageDialog(null,
	// // "Select an existing project directory",
	// // "Testcase directory", JOptionPane.OK_OPTION);
	// FileNames.chooseDirectory(FileNames.getTestCaseDirectory());
	// };
	// };

	
	ActionListener stationActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			StationInputDialog stationInputDialog = GlobalVar
					.getStationInputDialog();
			stationInputDialog.setVisible(true);
		}
	};

	ActionListener blockActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			BlockInput blockInput = GlobalVar.getBlockInput();
			blockInput.setVisible(true);
		}
	};

	ActionListener trainActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			TrainInputDialog trainInputDialog = GlobalVar.getTrainInputDialog();
			trainInputDialog.setVisible(true);
		}
	};

	ActionListener parameterActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ParameterInputDialog parameterInputDialog = GlobalVar
					.getParameterInputDialog();
			parameterInputDialog.setVisible(true);
		}
	};

	ActionListener gradientActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			GradientInputDialog gradientInputDialog = GlobalVar
					.getGradientInputDialog();
			gradientInputDialog.setVisible(true);
		}
	};

	ActionListener gradientEffectsActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			GradientEffectsInputDialog gradientEffectsInputDialog = GlobalVar
					.getGradientEffectsInputDialog();
			gradientEffectsInputDialog.setVisible(true);
		}
	};

	}

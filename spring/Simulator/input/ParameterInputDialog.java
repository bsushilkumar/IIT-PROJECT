package input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import files.Parameters;
import globalVariables.FileNames;
import globalVariables.GlobalVar;

public class ParameterInputDialog extends InputDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JLabel simulationTimeLabel = new JLabel("Simulation Time");
	public JTextField simulationTimeField = new JTextField("1");

	public JLabel delayFactorLabel = new JLabel("Delay Factor");
	public JTextField delayFactorField = new JTextField("0");

	public JLabel blockWorkingTimeLabel = new JLabel(
			"Block Working Time in min");
	public JTextField blockWorkingTimeField = new JTextField("0");

	public JLabel numberOfSignalColorLabel = new JLabel(
			"Number of signal colors");
	public JTextField numberOfSignalColorField = new JTextField("4");

	public JLabel redFailWaitTimeLabel = new JLabel("Red Fail Wait Time in min");
	public JTextField redFailWaitTimeField = new JTextField("1");

	public JLabel redFailVelocityLabel = new JLabel(
			"Red Fail Velocity in km/hr");
	public JTextField redFailVelocityField = new JTextField("18");

	public JLabel warnerDistanceLabel = new JLabel("Warner Distance in km");
	public JTextField warnerDistanceField = new JTextField("0");

	public JLabel resultLabel = new JLabel("");

	public JButton okButton = new JButton("Ok");
	public JButton cancelButton = new JButton("Cancel");
	public JButton doneButton = new JButton("Done");

	public ParameterInputDialog() {
		this.setBounds(x, y, width, height);

		setComponentActionListeners();
		setComponentBounds();
		addComponents();
		this.add(jpanel);

		this.setTitle("Parameters Input");
		// this.setVisible(true);

	}

	@Override
	public void addComponents() {
		this.getContentPane().add(simulationTimeLabel);
		this.getContentPane().add(simulationTimeField);

		this.getContentPane().add(delayFactorLabel);
		this.getContentPane().add(delayFactorField);

		this.getContentPane().add(blockWorkingTimeLabel);
		this.getContentPane().add(blockWorkingTimeField);

		this.getContentPane().add(numberOfSignalColorLabel);
		this.getContentPane().add(numberOfSignalColorField);

		this.getContentPane().add(redFailWaitTimeLabel);
		this.getContentPane().add(redFailWaitTimeField);

		this.getContentPane().add(redFailVelocityLabel);
		this.getContentPane().add(redFailVelocityField);

		this.getContentPane().add(warnerDistanceLabel);
		this.getContentPane().add(warnerDistanceField);

		this.getContentPane().add(resultLabel);

		this.getContentPane().add(okButton);
		this.getContentPane().add(cancelButton);
this.getContentPane().add(doneButton);
		// this.getContentPane().add();
	}

	@Override
	public void setComponentBounds() {
		int yDifference = 25;
		buttonWidth = 100;
		labelWidth = 200;
		x2 = x1 + labelWidth + 20;

		simulationTimeLabel.setBounds(x1, y1, labelWidth, labelHeight);
		simulationTimeField.setBounds(x2, y1, fieldWidth, fieldHeight);

		// y1 += yDifference;
		// delayFactorLabel.setBounds(x1, y1, labelWidth, labelHeight);
		// delayFactorField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		blockWorkingTimeLabel.setBounds(x1, y1, labelWidth, labelHeight);
		blockWorkingTimeField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		numberOfSignalColorLabel.setBounds(x1, y1, labelWidth, labelHeight);
		numberOfSignalColorField.setBounds(x2, y1, fieldWidth, fieldHeight);

		// y1 += yDifference;
		// redFailWaitTimeLabel.setBounds(x1, y1, labelWidth, labelHeight);
		// redFailWaitTimeField.setBounds(x2, y1, fieldWidth, fieldHeight);

		// y1 += yDifference;
		// redFailVelocityLabel.setBounds(x1, y1, labelWidth, labelHeight);
		// redFailVelocityField.setBounds(x2, y1, fieldWidth, fieldHeight);

		// y1 += yDifference;
		// warnerDistanceLabel.setBounds(x1, y1, labelWidth, labelHeight);
		// warnerDistanceField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		resultLabel.setBounds(x1, y1, 300, labelHeight);

		y1 += yDifference;
		okButton.setBounds(x1, y1, buttonWidth, buttonHeight);
		cancelButton.setBounds(x2, y1, buttonWidth, buttonHeight);
		y1 += yDifference;
		doneButton.setBounds(x1, y1, buttonWidth, buttonHeight);
				
	}

	ActionListener okButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				Parameters.simulationTime = Integer.parseInt(simulationTimeField
						.getText());
				Parameters.delayFactor = Integer.parseInt(delayFactorField
						.getText());
				Parameters.blockWorkingTime = (int) Double
						.parseDouble(blockWorkingTimeField.getText());
				Parameters.noOfColours = Integer
						.parseInt(numberOfSignalColorField.getText());
				Parameters.redFailWaitTime = Integer
						.parseInt(redFailVelocityField.getText());
				Parameters.redFailVelocity = (int) Double
						.parseDouble(redFailVelocityField.getText());
				Parameters.warnerDistance = Double
						.parseDouble(warnerDistanceField.getText());
				resultLabel.setText("Parameters changed");
			} catch (Exception ex) {
				resultLabel.setText("Check the input fields");
			}
		}
	};

	ActionListener cancelButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			GlobalVar.getParameterInputDialog().setVisible(false);
		}
	};

	ActionListener DoneButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			dispose();	}
	};
	@Override
	public void setComponentActionListeners() {
		okButton.addActionListener(okButtonActionListener);
		cancelButton.addActionListener(cancelButtonActionListener);
		doneButton.addActionListener(DoneButtonActionListener);
	}

	public static void main(String[] args) {
		ParameterInputDialog parameterInputDialog = GlobalVar
				.getParameterInputDialog();
	}

	public void write() throws IOException {
		String parameterFileName = FileNames.getParametersFileName();
		BufferedWriter bw = new BufferedWriter(
				new FileWriter(parameterFileName));
		String formatString = "/*SimulationTime    DelayFactor   blockWorkingTime    noOfSignalColors   redFailWaitTime   redFailWaitVelocity   warnerDistance   */\n\n";
		bw.write(formatString);

		bw.write(String.valueOf(Parameters.simulationTime));
		bw.write(" ");

		bw.write(String.valueOf(Parameters.delayFactor));
		bw.write(" ");

		bw.write(String.valueOf(Parameters.blockWorkingTime));
		bw.write(" ");

		bw.write(String.valueOf(Parameters.noOfColours));
		bw.write(" ");

		bw.write(String.valueOf(Parameters.redFailWaitTime));
		bw.write(" ");

		bw.write(String.valueOf(Parameters.redFailVelocity));
		bw.write(" ");

		bw.write(String.valueOf(Parameters.warnerDistance));
		bw.write(" ");

		bw.close();
	}

	public void readParameters() throws IOException {
		String parameterFileName = FileNames.getParametersFileName();
		StreamTokenizer streamTokenizer = new StreamTokenizer(new FileReader(
				parameterFileName));
		streamTokenizer.slashSlashComments(true);
		streamTokenizer.slashStarComments(true);

		streamTokenizer.nextToken();
		int simulationTime = (int) streamTokenizer.nval;

		streamTokenizer.nextToken();
		int delayFactor = (int) streamTokenizer.nval;

		streamTokenizer.nextToken();
		double blockWorkingTime = streamTokenizer.nval;

		streamTokenizer.nextToken();
		int numberOfSignalColors = (int) streamTokenizer.nval;

		streamTokenizer.nextToken();
		int redFailWaitTime = (int) streamTokenizer.nval;

		streamTokenizer.nextToken();
		double redFailWaitVelocity = streamTokenizer.nval;

		streamTokenizer.nextToken();
		double warnerDistance = streamTokenizer.nval;

		Parameters.simulationTime = simulationTime;
		Parameters.delayFactor = delayFactor;
		Parameters.blockWorkingTime = (int) blockWorkingTime;
		Parameters.noOfColours = numberOfSignalColors;
		Parameters.redFailWaitTime = redFailWaitTime;
		Parameters.redFailVelocity = (int) redFailWaitVelocity;
		Parameters.warnerDistance = warnerDistance;

	}
}

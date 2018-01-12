package simulator.outputFeatures;

import globalVariables.FileNames;


import globalVariables.GlobalVar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import simulator.input.SimulationInstance;
import simulator.util.Debug;

/**
 * Graphical user interface for selecting files.
 */
@SuppressWarnings("serial")
public class SelectFiles extends JFrame {
	SimulationInstance simulationInstance = null;

	JTextField directoryTextField;
	/**
	 * directoryLabel
	 */
	JLabel directoryLabel;
	/**
	 * stationButton
	 */
	JButton directoryButton;
	// For station
	/**
	 * stationTextField
	 */
	JTextField stationTextField;
	/**
	 * stationLabel
	 */
	JLabel stationLabel;
	/**
	 * stationButton
	 */
	JButton stationButton;
	/**
	 * fc
	 */
	JFileChooser fc;
	JFileChooser dfc;

	// For Block
	/**
	 * blockTextField
	 */
	JTextField blockTextField;
	/**
	 * blockLabel
	 */
	JLabel blockLabel;
	/**
	 * blockButton
	 */
	JButton blockButton;

	// For Loop
	/**
	 * loopTextField
	 */
	JTextField loopTextField;
	/**
	 * loopLabel
	 */
	JLabel loopLabel;
	/**
	 * loopButton
	 */
	JButton loopButton;

	// For Scheduled
	/**
	 * scheduledTextField
	 */
	JTextField scheduledTextField;
	/**
	 * scheduledLabel
	 */
	JLabel scheduledLabel;
	/**
	 * scheduledButton
	 */
	JButton scheduledButton;

	// For Unscheduled
	/**
	 * unscheduledTextField
	 */
	JTextField unscheduledTextField;
	/**
	 * unscheduledLabel
	 */
	JLabel unscheduledLabel;
	/**
	 * unscheduledButton
	 */
	JButton unscheduledButton;

	// For param
	/**
	 * parameterButton
	 */
	JButton parameterButton;
	/**
	 * parameterLabel
	 */
	JLabel parameterLabel;
	/**
	 * parameterTextField
	 */
	JTextField parameterTextField;

	// For signal Failure
	/**
	 * signalFailureTextField
	 */
	JTextField signalFailureTextField;
	/**
	 * signalFailureLabel
	 */
	JLabel signalFailureLabel;
	/**
	 * signalFailureButton
	 */
	JButton signalFailureButton;
	/**
	 * signalFailureCheckBox
	 */
	JCheckBox signalFailureCheckBox;

	// Ok Button
	/**
	 * okButton
	 */
	JButton okButton;

	// Cancel Button
	/**
	 * cancelButton
	 */
	JButton cancelButton;

	// GradientList
	/**
	 * gradientTextField
	 */
	JTextField gradientTextField;
	/**
	 * gradientLabel
	 */
	JLabel gradientLabel;
	/**
	 * gradientButton
	 */
	JButton gradientButton;

	// GradientEffect
	/**
	 * gradientEffectTextField
	 */
	JTextField gradientEffectTextField;
	/**
	 * gradientEffectLabel
	 */
	JLabel gradientEffectLabel;
	/**
	 * gradientEffectButton
	 */
	JButton gradientEffectButton;

	// Passdelay
	/**
	 * passengerDelayCheckBox
	 */
	JCheckBox passengerDelayCheckBox;
	/**
	 * passengerDelayTextField
	 */
	JTextField passengerDelayTextField;
	/**
	 * passengerDelayLabel
	 */
	JLabel passengerDelayLabel;
	/**
	 * passengerDelayButton
	 */
	JButton passengerDelayButton;

	/**
	 * blockDirectionInIntervalTextField : filepath for
	 * blockDirectionInInterval.txt
	 */
	JTextField blockDirectionInIntervalTextField;
	/**
	 * blockDirectionInIntervalLabel
	 */
	JLabel blockDirectionInIntervalLabel;
	/**
	 * blockDirectionInIntervalButton : open file browser
	 */
	JButton blockDirectionInIntervalButton;
	/**
	 * blockDirectionInIntervalCheckBox
	 */
	JCheckBox blockDirectionInIntervalCheckBox;

	//
	/**
	 * me
	 */
	private static SelectFiles me = null;

	public int lblXCoord = 40;
	public int txtXCoord = 240;
	public int butXCoord = 340;
	public int checkBoxXCoord = 20;

	public int lblWidth = 180;
	public int txtWidth = 80;
	public int butWidth = 37;

	public int butHeight = 24;
	public int lblHeight = 27;
	public int txtHeight = 21;

	public int yTxtStartCoord = 39;
	public int yLblStartCoord = 20;
	public int yButStartCoord = 38;
	public int yDiffLblTxt = 5;
	public int yDiffLblBut = 4;
	public int yDiff = 48;
	public int tempY;

	/**
     * 
     */
	SelectFiles(SimulationInstance simulationInstance) {
		Debug.print("In Select Files");
		this.simulationInstance = simulationInstance;

		jbInit();
		setBounds(0, 0, 600, 600);

	}

	/**
     * 
     */
	private void jbInit() {

		initiateCoordinateValues();

		this.getContentPane().setLayout(null);
		fc = new JFileChooser();
		try {
			dfc = new JFileChooser((new File("")).getCanonicalPath());
			dfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JLabel jSelectFiles = new JLabel("Select files");
		jSelectFiles.setBounds(lblXCoord, tempY, lblWidth, lblHeight);

		tempY += yDiff;
		createDirectoryComponents();

		tempY += yDiff;
		createStationComponents();
		tempY += yDiff;
		createBlockComponents();
		tempY += yDiff;
		createLoopComponents();
		tempY += yDiff;
		createScheduledTrainComponents();
		tempY += yDiff;
		createUnscheduledTrainComponents();
		tempY += yDiff;
		createParametersComponents();
		tempY += yDiff;
		createGradientComponents();
		tempY += yDiff;
		createGradientEffectsComponents();
		/*tempY += yDiff;
		createSignalFailureComponents();
		tempY += yDiff;
		createPassengerDelayComponents();*/
		tempY += yDiff;
		createBlockDirectionIntervalComponents();
		tempY += yDiff;
		createOKButton();

		createCancelButton();

		Debug.print("adding comp");
		this.addWindowListener(new WindowAdapter() {
			@SuppressWarnings({ "deprecation", "synthetic-access" })
			public void windowClosing(WindowEvent e) {
				dispose();
				//me.setVisible(false);
			}
		});

		this.getContentPane().add(jSelectFiles);
		addComponentsToPane();
	}

	private void createDirectoryComponents() {
		// For station
		directoryTextField = new JTextField();
		directoryLabel = new JLabel();
		directoryButton = new JButton();
		directoryLabel.setText("Directory");

		directoryLabel.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		directoryTextField.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		directoryButton.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(stationLabel, stationTextField,
		// stationButton,
		// "Station File");
		Debug.print("now action");
		directoryButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = dfc.showOpenDialog(me);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String directory;
					File statf = null;
					File blockf = null;
					File loopf = null;
					File paraf = null;
					File gradf = null;
					File gradef = null;
					File schedf = null;
					File unschedf = null;
					//File should be existing in the directory
					
					try {
						directory = dfc.getSelectedFile().getCanonicalPath();
						directoryTextField.setText(directory);
						
						statf=new File(directory +"/station.txt");
						boolean bs=statf.exists();
						if(bs==true){
						stationTextField.setText("station.txt");}
						else
						{
						stationTextField.setText("No File");
						}
						
						blockf=new File(directory +"/block.txt");
						boolean bb=blockf.exists();
						if(bb==true){
						blockTextField.setText("block.txt");}
						else
						{
						blockTextField.setText("No File");
						}
						
						loopf=new File(directory +"/loop.txt");
						boolean bl=loopf.exists();
						if(bl==true){
						loopTextField.setText("loop.txt");}
						else
						{
						loopTextField.setText("No File");
						}
						
						paraf=new File(directory +"/param.dat");
						boolean bp=paraf.exists();
						if(bp==true){
						parameterTextField.setText("param.dat");}
						else
						{
						parameterTextField.setText("No File");
						}
						
						gradf=new File(directory +"/gradient.txt");
						boolean bg=gradf.exists();
						if(bg==true){
						gradientTextField.setText("gradient.txt");}
						else
						{
						gradientTextField.setText("No File");
						}
						
						gradef=new File(directory +"/gradientEffects.txt");
						boolean bge=gradef.exists();
						if(bge==true){
						gradientEffectTextField.setText("gradientEffects.txt");}
						else
						{
						gradientEffectTextField.setText("No File");
						}
						
						schedf=new File(directory +"/scheduled.txt");
						boolean bsc=schedf.exists();
						if(bsc==true){
						scheduledTextField.setText("scheduled.txt");}
						else
						{
						scheduledTextField.setText("No File");
						}
						
						unschedf=new File(directory +"/unscheduled.txt");
						boolean busc=unschedf.exists();
						if(busc==true){
						unscheduledTextField.setText("unscheduled.txt");}
						else
						{
						unscheduledTextField.setText("No File");
						}

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});
	}

	/**
	 * Initiate coordinate values;
	 */
	public void initiateCoordinateValues() {
		lblXCoord = 40;
		txtXCoord = 240;
		butXCoord = 340;
		checkBoxXCoord = 20;

		lblWidth = 180;
		txtWidth = 80;
		butWidth = 37;

		butHeight = 24;
		lblHeight = 27;
		txtHeight = 21;

		yTxtStartCoord = 39;
		yLblStartCoord = 20;
		yButStartCoord = 38;
		yDiffLblTxt = 5;
		yDiffLblBut = 4;
		yDiff = 48;

		tempY = yLblStartCoord;
	}

	/**
	 * create and set label, text, button and bounds
	 * 
	 * @param jLabel
	 * @param jText
	 * @param jButton
	 */
	public void createAndSetLblTxtButBounds(Object jLabel, Object jText,
			Object jButton, String textField) {
		jLabel = new JLabel();
		jText = new JTextField(textField);
		jButton = new JButton();

		((JLabel) jLabel).setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		((JTextField) jText).setBounds(txtXCoord, tempY + yDiffLblTxt,
				txtWidth, txtHeight);
		((JButton) jButton).setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);

	}

	/**
	 * create Station Components
	 */
	public void createStationComponents() {
		// For station
		stationTextField = new JTextField();
		stationLabel = new JLabel();
		stationButton = new JButton();
		stationLabel.setText("Station File");

		stationLabel.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		stationTextField.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		stationButton.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(stationLabel, stationTextField,
		// stationButton,
		// "Station File");
		Debug.print("now action");
		stationButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileStation = fc.getSelectedFile().getName();
					stationTextField.setText(fileStation);

				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

	}

	/**
	 * create BlockComponents
	 */
	public void createBlockComponents() {

		// For Block
		blockTextField = new JTextField();
		blockLabel = new JLabel("Block File");
		blockButton = new JButton();

		blockLabel.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		blockTextField.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		blockButton.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(blockLabel, blockTextField, blockButton,
		// "Block File");
		blockButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileBlock = fc.getSelectedFile().getName();
					blockTextField.setText(fileBlock);
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

	}

	/**
	 * create loop components
	 */
	public void createLoopComponents() {
		// For Loop
		loopTextField = new JTextField();
		loopLabel = new JLabel("Loop File");
		loopButton = new JButton();

		loopLabel.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		loopTextField.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		loopButton.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(loopLabel, loopTextField, loopButton,
		// "Loop file");
		loopButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileLoop = fc.getSelectedFile().getName();
					loopTextField.setText(fileLoop);
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

	}

	/**
	 * createScheduledTrainComponents
	 */
	public void createScheduledTrainComponents() {
		// For Scheduled SimulatorTrain

		scheduledTextField = new JTextField();
		scheduledLabel = new JLabel("Scheduled Trains File");
		scheduledButton = new JButton();

		scheduledLabel.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		scheduledTextField.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		scheduledButton.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(scheduledLabel, scheduledTextField,
		// scheduledButton, "Scheduled file");
		scheduledButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileScheduled = fc.getSelectedFile().getName();
					scheduledTextField.setText(fileScheduled);
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});
	}

	/**
	 * create unscheduled train components
	 */
	public void createUnscheduledTrainComponents() {

		// For Unscheduled SimulatorTrain
		unscheduledTextField = new JTextField();
		unscheduledLabel = new JLabel("UnScheduled Trains File");
		unscheduledButton = new JButton();

		unscheduledLabel.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		unscheduledTextField.setBounds(txtXCoord, tempY + yDiffLblTxt,
				txtWidth, txtHeight);
		unscheduledButton.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(unscheduledLabel, unscheduledTextField,
		// unscheduledButton, "Unscheduled file");
		unscheduledButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileUnScheduled = fc.getSelectedFile().getName();
					unscheduledTextField.setText(fileUnScheduled);
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

	}

	/**
	 * Create parameters components
	 */
	public void createParametersComponents() {
		// For Param
		parameterButton = new JButton();
		parameterLabel = new JLabel("Parameter File");
		parameterTextField = new JTextField();

		parameterLabel.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		parameterTextField.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		parameterButton.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(parameterLabel, parameterTextField,
		// parameterButton,
		// "Parameters File");
		parameterButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileParam = fc.getSelectedFile().getName();
					parameterTextField.setText(fileParam);
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

	}

	/**
	 * Create GradientList components
	 */
	public void createGradientComponents() {

		// GradientList
		//
		gradientButton = new JButton();
		gradientLabel = new JLabel("GradientList File");
		gradientTextField = new JTextField();

		gradientLabel.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		gradientTextField.setBounds(txtXCoord, tempY + yDiffLblTxt, txtWidth,
				txtHeight);
		gradientButton.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(gradientLabel, gradientTextField,
		// gradientButton, "GradientList File");
		gradientButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileGradient = fc.getSelectedFile().getName();
					gradientTextField.setText(fileGradient);
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});
	}

	/**
	 * create GradientEffects Components
	 */
	public void createGradientEffectsComponents() {
		// For GradientList Effect
		gradientEffectButton = new JButton();
		gradientEffectLabel = new JLabel("GradientList Effect File");
		gradientEffectTextField = new JTextField();

		gradientEffectLabel.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		gradientEffectTextField.setBounds(txtXCoord, tempY + yDiffLblTxt,
				txtWidth, txtHeight);
		gradientEffectButton.setBounds(butXCoord, tempY + yDiffLblBut,
				butWidth, butHeight);
		// createAndSetLblTxtButBounds(gradientEffectLabel,
		// gradientEffectTextField, gradientEffectButton,
		// "GradientList Effect File");
		gradientEffectButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileGradientEffect = fc.getSelectedFile().getName();
					gradientEffectTextField.setText(fileGradientEffect);
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});
	}

	/**
	 * Create signal failure components
	 */
	public void createSignalFailureComponents() {

		// Signal Failure

		signalFailureCheckBox = new JCheckBox();
		signalFailureTextField = new JTextField();
		signalFailureLabel = new JLabel("Signal Failure File");
		signalFailureButton = new JButton();

		signalFailureLabel.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		signalFailureTextField.setBounds(txtXCoord, tempY + yDiffLblTxt,
				txtWidth, txtHeight);
		signalFailureButton.setBounds(butXCoord, tempY + yDiffLblBut, butWidth,
				butHeight);
		// createAndSetLblTxtButBounds(signalFailureLabel,
		// signalFailureTextField,
		// signalFailureButton, "Signal Failure File");
		signalFailureCheckBox.setBounds(checkBoxXCoord, tempY, butWidth,
				butHeight);

		signalFailureButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileSignalFailure = fc.getSelectedFile().getName();
					signalFailureTextField.setText(fileSignalFailure);
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});
		signalFailureCheckBox.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {

				Debug.print("Checked");
				if (signalFailureCheckBox.isSelected() == true) {
					Debug.print("Checked - in true");
					simulationInstance.simulationType = "SignalFailure";
					System.out
							.println(" SIM ULATION IS IN SIGANL FAILURE MODE ");
					signalFailureButton.setEnabled(true);
					signalFailureTextField.setEnabled(true);
				} else {
					Debug.print("Checked -  not in true:-)");
					simulationInstance.simulationType = "Normal";
					System.out.println(" SIM ULATION IS IN NORMAL      MODE ");
					signalFailureButton.setEnabled(false);
					signalFailureTextField.setEnabled(false);
				}
			}
		});
	}

	/**
	 * Create PassengerDelay components.
	 */
	public void createPassengerDelayComponents() {
		// Passenger SimulatorTrain Delay

		passengerDelayCheckBox = new JCheckBox();
		passengerDelayTextField = new JTextField();
		passengerDelayLabel = new JLabel("Delay File");
		passengerDelayButton = new JButton();

		passengerDelayLabel.setBounds(lblXCoord, tempY, lblWidth, lblHeight);
		passengerDelayTextField.setBounds(txtXCoord, tempY + yDiffLblTxt,
				txtWidth, txtHeight);
		passengerDelayButton.setBounds(butXCoord, tempY + yDiffLblBut,
				butWidth, butHeight);
		// createAndSetLblTxtButBounds(passengerDelayLabel,
		// passengerDelayTextField, passengerDelayButton,
		// "Passenger Delay File");
		passengerDelayCheckBox.setBounds(checkBoxXCoord, tempY, butWidth,
				butHeight);

		passengerDelayButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filePassengerDelay = fc.getSelectedFile().getName();
					passengerDelayTextField.setText(filePassengerDelay);
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

		passengerDelayCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debug.print("Checked");
				if (passengerDelayCheckBox.isSelected() == true) {
					simulationInstance.delay = 1;
					Debug.print("Checked - in true");

					passengerDelayButton.setEnabled(true);
					passengerDelayTextField.setEnabled(true);
				} else {
					Debug.print("Checked -  not in true:-)");
					simulationInstance.delay = 0;
					passengerDelayButton.setEnabled(false);
					passengerDelayTextField.setEnabled(false);
				}
			}
		});
	}

	/**
	 * create BlockDirectionInInterval components
	 */
	public void createBlockDirectionIntervalComponents() {
		// blockDirectionInIntervalFile

		blockDirectionInIntervalCheckBox = new JCheckBox();
		blockDirectionInIntervalTextField = new JTextField();
		blockDirectionInIntervalLabel = new JLabel(
				"BlockDirectionInInterval File");
		blockDirectionInIntervalButton = new JButton();

		blockDirectionInIntervalLabel.setBounds(lblXCoord, tempY, lblWidth,
				lblHeight);
		blockDirectionInIntervalTextField.setBounds(txtXCoord, tempY
				+ yDiffLblTxt, txtWidth, txtHeight);
		blockDirectionInIntervalButton.setBounds(butXCoord,
				tempY + yDiffLblBut, butWidth, butHeight);
		// createAndSetLblTxtButBounds(blockDirectionInIntervalLabel,
		// blockDirectionInIntervalTextField,
		// blockDirectionInIntervalButton, "Block Direction File");
		blockDirectionInIntervalCheckBox.setBounds(checkBoxXCoord, tempY,
				butWidth, butHeight);

		blockDirectionInIntervalButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(me);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileBlockDirectionInInterval = fc.getSelectedFile()
							.getName();
					blockDirectionInIntervalTextField
							.setText(fileBlockDirectionInInterval);
				} else {
					Debug.print("Open command cancelled by user.");
				}
			}
		});

		blockDirectionInIntervalCheckBox
				.addActionListener(new ActionListener() {
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e) {
						Debug.print("Checked");
						if (blockDirectionInIntervalCheckBox.isSelected() == true) {
							simulationInstance.hasBlockDirectionFile = true;
							System.out.println("Turned hasBlockDirectionFile "
									+ simulationInstance.hasBlockDirectionFile);
							Debug.print("Checked - in true");

							blockDirectionInIntervalButton.setEnabled(true);
							blockDirectionInIntervalTextField.setEnabled(true);
							//santhosh
							GlobalVar.simulationInstance=simulationInstance;
						} else {
							Debug.print("Checked -  not in true:-)");
							// GlobalVar.delay = 0;
							simulationInstance.hasBlockDirectionFile = false;
							System.out.println("Turned hasBlockDirectionFile "
									+ simulationInstance.hasBlockDirectionFile);
							blockDirectionInIntervalButton.setEnabled(false);
							blockDirectionInIntervalTextField.setEnabled(false);
						}
					}
				});
	}

	/**
	 * Create OK Button
	 */
	public void createOKButton() {
		// OK
		okButton = new JButton("OK");
		okButton.setBounds(64, tempY, 94, butHeight);
		okButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				FileNames.setTestCaseDirectory(directoryTextField.getText());
				FileNames.setStationFileName(stationTextField.getText());
				FileNames.setScheduledTrainsFileName(scheduledTextField
						.getText());
				FileNames.setBlockFileName(blockTextField.getText());
				FileNames.setLoopFileName(loopTextField.getText());
				FileNames.setParametersFileName(parameterTextField.getText());
				//FileNames.setSignalFailureFileName(signalFailureTextField.getText());
				FileNames.setGradientFileName(gradientTextField.getText());
				FileNames.setGradientEffectsFileName(gradientEffectTextField
						.getText());
				//FileNames.setPassengerDelayFileName(passengerDelayTextField.getText());
				FileNames
						.setBlockDirectionFileName(blockDirectionInIntervalTextField
								.getText());
				//santhosh-code added to integrate raw capacity 
				setVisible(false);
			}
		});
	}

	/**
	 * Create a cancel button
	 */
	public void createCancelButton() {
		// Cancel
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(317, tempY, 94, butHeight);
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
				//me.setVisible(false);
			}
		});
	}

	/**
	 * Add the components to the pane
	 */
	public void addComponentsToPane() {
		this.getContentPane().add(directoryButton);
		this.getContentPane().add(directoryTextField);
		this.getContentPane().add(directoryLabel);

		this.getContentPane().add(stationButton);
		this.getContentPane().add(stationTextField);
		this.getContentPane().add(stationLabel);
		this.getContentPane().add(blockLabel);
		this.getContentPane().add(blockTextField);
		this.getContentPane().add(blockButton);
		this.getContentPane().add(loopTextField);
		this.getContentPane().add(loopLabel);
		this.getContentPane().add(loopButton);
		this.getContentPane().add(scheduledTextField);
		this.getContentPane().add(scheduledLabel);
		this.getContentPane().add(scheduledButton);
		this.getContentPane().add(unscheduledTextField);
		this.getContentPane().add(unscheduledLabel);
		this.getContentPane().add(unscheduledButton);
		this.getContentPane().add(parameterButton);
		this.getContentPane().add(parameterLabel);
		this.getContentPane().add(parameterTextField);
		//
		this.getContentPane().add(gradientButton);
		this.getContentPane().add(gradientLabel);
		this.getContentPane().add(gradientTextField);

		this.getContentPane().add(gradientEffectButton);
		this.getContentPane().add(gradientEffectLabel);
		this.getContentPane().add(gradientEffectTextField);

		/*this.getContentPane().add(passengerDelayButton);
		this.getContentPane().add(passengerDelayLabel);
		this.getContentPane().add(passengerDelayTextField);
		this.getContentPane().add(passengerDelayCheckBox);
		//
		this.getContentPane().add(signalFailureButton);
		this.getContentPane().add(signalFailureLabel);
		this.getContentPane().add(signalFailureTextField);
		this.getContentPane().add(signalFailureCheckBox);*/

		this.getContentPane().add(blockDirectionInIntervalButton);
		this.getContentPane().add(blockDirectionInIntervalLabel);
		this.getContentPane().add(blockDirectionInIntervalTextField);
		this.getContentPane().add(blockDirectionInIntervalCheckBox);

		this.getContentPane().add(okButton);
		this.getContentPane().add(cancelButton);

	}

}
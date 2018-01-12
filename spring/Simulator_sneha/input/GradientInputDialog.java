package input;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.trackProperties.Gradient;
import gui.entities.sectionEntities.trackProperties.GradientEffect;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;


public class GradientInputDialog extends InputDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JLabel slopeLabel = new JLabel("Gradient Value ");
	public JComboBox<String> GradientList = null;
	public JLabel directionLabel = new JLabel("Direction");
	public ButtonGroup directionButtonGroup = new ButtonGroup();
	public JRadioButton upRadioButton = new JRadioButton("Up");
	public JRadioButton downRadioButton = new JRadioButton("Down");
	public JRadioButton levelRadioButton = new JRadioButton("Level");
	public JTextField leveldirectionLabel = new JTextField("0");

	public JLabel startKmLabel = new JLabel("Start Km");
	public JTextField startKmField = new JTextField();

	public JLabel endKmLabel = new JLabel("End Km");
	public JTextField endKmField = new JTextField();

	public JButton addButton = new JButton("Add");
	public JButton viewAllGradientsButton = new JButton("View all gradients");

	public JLabel editDeleteOptionLabel = new JLabel("Edit/Delete");
	public JLabel editDeleteSlopeLabel = new JLabel("Gradient Value e.g. 1/100");
	public JLabel editDeleteDirectionLabel = new JLabel("Direction");

	public JTextField editDeleteSlopeField = new JTextField();
	public ButtonGroup editDeleteDirectionButtonGroup = new ButtonGroup();
	public JRadioButton editDeleteUpDirectionButton = new JRadioButton("Up");
	public JRadioButton editDeleteDownDirectionButton = new JRadioButton("Down");
	public JRadioButton editDeleteLevelDirectionButton = new JRadioButton(
			"Level");

	public JButton editButton = new JButton("Edit");
	public JButton deleteButton = new JButton("Delete");
	public JButton updateButton = new JButton("Update");
	public JButton doneButton = new JButton("Done");
	public JLabel resultLabel = new JLabel("");
	SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();

	ArrayList<Gradient> gradientFormatList = sectioninputdialog.gradientFormatList;

	public Gradient gradientFormatToBeEdited = null;
	public static String[] gradienttype = { "1/100", "1/150", "1/200", "1/250",
			"1/300", "1/350", "1/400", "1/450" };

	public GradientInputDialog() {
		this.setBounds(x, y, width, height);
		GradientList = new JComboBox<String>(gradienttype);

		SectionInputDialog sectioninputdialog = GlobalVar
				.getSectionInputDialog();
		ArrayList<Station> stationList = sectioninputdialog.stationList;
		if (stationList.size() > 0) {
			String startmilepost = String
					.valueOf(stationList.get(0).getStartMilePost());
			String endmilepost = String.valueOf(stationList.get(stationList
					.size() - 1).getEndMilePost());
			startKmField.setText(startmilepost);
			endKmField.setText(endmilepost);
		}
		setComponentActionListeners();
		setComponentBounds();
		addComponents();
		this.add(jpanel);

		this.setTitle("Gradients Input");
		// this.setVisible(true);

	}

	@Override
	public void addComponents() {
		this.getContentPane().add(slopeLabel);

		this.getContentPane().add(directionLabel);

		this.getContentPane().add(upRadioButton);
		this.getContentPane().add(downRadioButton);
		this.getContentPane().add(levelRadioButton);
		directionButtonGroup.add(upRadioButton);
		directionButtonGroup.add(downRadioButton);
		directionButtonGroup.add(levelRadioButton);

		levelRadioButton.setSelected(true);
		this.getContentPane().add(leveldirectionLabel);
		leveldirectionLabel.disable();
		this.getContentPane().add(startKmLabel);
		this.getContentPane().add(startKmField);
		this.getContentPane().add(endKmLabel);
		this.getContentPane().add(endKmField);

		this.getContentPane().add(addButton);
		this.getContentPane().add(viewAllGradientsButton);
		this.getContentPane().add(GradientList);

		this.getContentPane().add(editDeleteOptionLabel);
		this.getContentPane().add(editDeleteSlopeLabel);
		this.getContentPane().add(editDeleteDirectionLabel);

		this.getContentPane().add(editDeleteSlopeField);
		this.getContentPane().add(editDeleteUpDirectionButton);
		this.getContentPane().add(editDeleteDownDirectionButton);
		this.getContentPane().add(editDeleteLevelDirectionButton);
		editDeleteDirectionButtonGroup.add(editDeleteUpDirectionButton);
		editDeleteDirectionButtonGroup.add(editDeleteDownDirectionButton);
		editDeleteDirectionButtonGroup.add(editDeleteLevelDirectionButton);
		editDeleteUpDirectionButton.setSelected(true);

		this.getContentPane().add(editButton);

		this.getContentPane().add(deleteButton);
		this.getContentPane().add(updateButton);
		this.getContentPane().add(resultLabel);
		this.getContentPane().add(doneButton);

	}

	@Override
	public void setComponentBounds() {
		int yDifference = 25;
		buttonWidth = 100;
		labelWidth = 200;
		x2 = x1 + labelWidth + 20;

		slopeLabel.setBounds(x1, y1, labelWidth, labelHeight);
		GradientList.setBounds(x2, y1, 3 * fieldWidth / 4, fieldHeight);
		leveldirectionLabel.setBounds(x2, y1, 3 * fieldWidth / 4, fieldHeight);

		y1 += yDifference;
		directionLabel.setBounds(x1, y1, labelWidth, labelHeight);
		upRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
		y1 += yDifference;
		downRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
		y1 += yDifference;
		levelRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		startKmLabel.setBounds(x1, y1, labelWidth, labelHeight);
		startKmField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		endKmLabel.setBounds(x1, y1, labelWidth, labelHeight);
		endKmField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		addButton.setBounds(x1, y1, buttonWidth, buttonHeight);
		viewAllGradientsButton.setBounds(x2, y1, 200, fieldHeight);

		y1 += 3 * yDifference;
		editDeleteOptionLabel.setBounds(x1, y1, labelWidth, labelHeight);

		y1 += yDifference;
		editDeleteSlopeLabel.setBounds(x1, y1, labelWidth, labelHeight);
		editDeleteSlopeField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		editDeleteDirectionLabel.setBounds(x1, y1, labelWidth, labelHeight);
		editDeleteUpDirectionButton.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		editDeleteDownDirectionButton
				.setBounds(x2, y1, fieldWidth, fieldHeight);
		y1 += yDifference;

		editDeleteLevelDirectionButton.setBounds(x2, y1, fieldWidth,
				fieldHeight);

		y1 += yDifference;
		editButton.setBounds(x1, y1, buttonWidth, buttonHeight);
		deleteButton.setBounds(x2, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		updateButton.setBounds(x1, y1, buttonWidth, buttonHeight);
		doneButton.setBounds(x2, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		resultLabel.setBounds(x1, y1, 300, labelHeight);

	}

	@Override
	public void setComponentActionListeners() {
		addButton.addActionListener(addActionListener);
		editButton.addActionListener(editActionListener);
		deleteButton.addActionListener(deleteActionListener);
		updateButton.addActionListener(updateActionListener);
		viewAllGradientsButton
				.addActionListener(viewAllGradientsActionListener);
		doneButton.addActionListener(doneActionListener);
		upRadioButton.addItemListener(GradienttypeItemListener);
		downRadioButton.addItemListener(GradienttypeItemListener);
		levelRadioButton.addItemListener(GradienttypeItemListener);

	}

	ItemListener GradienttypeItemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			boolean levelButtonSelected = levelRadioButton.isSelected();
			leveldirectionLabel.setVisible(levelButtonSelected);
			GradientList.setVisible(!levelButtonSelected);

		}
	};

	ActionListener addActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {

				String slopeString = (String) GradientList.getSelectedItem();
				if (GradientList.getSelectedIndex() == -1) {
					resultLabel
							.setText("Choose a station name to edit station properties ");
				} else {
					boolean isGradientLevel = levelRadioButton.isSelected();
					boolean isGradientUp = upRadioButton.isSelected();
					GradientEffect gradientEffect = new GradientEffect();
					Gradient gradientFormat = new Gradient();

					gradientFormat.setGradientValue("0");
					gradientEffect.setGradientValue("0");
					if (!isGradientLevel) {
						gradientFormat.setGradientValue(slopeString);
						gradientEffect.setGradientValue(slopeString);

						if (isGradientUp) {
							gradientFormat.setDirection("Up");
							gradientEffect.setDirection(GlobalVar.UP_DIRECTION);
						} else {
							gradientFormat.setDirection("Down");
							gradientEffect.setDirection(GlobalVar.DOWN_DIRECTION);
						}
						gradientFormat.setRelativeStartMilePost(Double
								.parseDouble(startKmField.getText()));
						gradientFormat.setRelativeEndMilePost(Double
								.parseDouble(endKmField.getText()));

						if (GradientList.getSelectedIndex() < 1
								&& !isGradientUp) {
							gradientEffect.setAccelerationChange(0);
							gradientEffect.setDecelerationChange(-.15);
						} else if (GradientList.getSelectedIndex() < 2
								&& !isGradientUp) {
							gradientEffect.setAccelerationChange(0);
							gradientEffect.setDecelerationChange(-.13);
						} else if (GradientList.getSelectedIndex() < 3
								&& !isGradientUp) {
							gradientEffect.setAccelerationChange(0);
							gradientEffect.setDecelerationChange(-.11);
						} else if (GradientList.getSelectedIndex() < 4
								&& !isGradientUp) {
							gradientEffect.setAccelerationChange(0);
							gradientEffect.setDecelerationChange(-.09);
						} else if (GradientList.getSelectedIndex() < 5
								&& !isGradientUp) {
							gradientEffect.setAccelerationChange(0);
							gradientEffect.setDecelerationChange(-.07);
						} else if (GradientList.getSelectedIndex() < 6
								&& !isGradientUp) {
							gradientEffect.setAccelerationChange(0);
							gradientEffect.setDecelerationChange(-.05);
						} else if (GradientList.getSelectedIndex() < 7
								&& !isGradientUp) {
							gradientEffect.setAccelerationChange(0);
							gradientEffect.setDecelerationChange(-.03);
						} else if (GradientList.getSelectedIndex() < 8
								&& !isGradientUp) {
							gradientEffect.setAccelerationChange(0);
							gradientEffect.setDecelerationChange(-.01);
						}

						else if (GradientList.getSelectedIndex() < 1
								&& isGradientUp) {
							gradientEffect.setAccelerationChange(-.15);

							gradientEffect.setDecelerationChange(.15);
						} else if (GradientList.getSelectedIndex() < 2
								&& isGradientUp) {
							gradientEffect.setAccelerationChange(-.13);
							gradientEffect.setDecelerationChange(.13);
						} else if (GradientList.getSelectedIndex() < 3
								&& isGradientUp) {
							gradientEffect.setAccelerationChange(-.11);
							gradientEffect.setDecelerationChange(.11);
						} else if (GradientList.getSelectedIndex() < 4
								&& isGradientUp) {
							gradientEffect.setAccelerationChange(-.09);
							gradientEffect.setDecelerationChange(.09);
						} else if (GradientList.getSelectedIndex() < 5
								&& isGradientUp) {
							gradientEffect.setAccelerationChange(-.07);
							gradientEffect.setDecelerationChange(.07);
						} else if (GradientList.getSelectedIndex() < 6
								&& isGradientUp) {
							gradientEffect.setAccelerationChange(-.05);
							gradientEffect.setDecelerationChange(.05);
						} else if (GradientList.getSelectedIndex() < 7
								&& isGradientUp) {
							gradientEffect.setAccelerationChange(-.03);
							gradientEffect.setDecelerationChange(.03);
						} else if (GradientList.getSelectedIndex() < 8
								&& isGradientUp) {
							gradientEffect.setAccelerationChange(-.01);
							gradientEffect.setDecelerationChange(.01);
						}

					} else {
						gradientFormat.setDirection("Level");
						gradientEffect.setDirection(GlobalVar.COMMON_DIRECTION);
						
						gradientFormat.setRelativeStartMilePost(Double
								.parseDouble(startKmField.getText()));
						gradientFormat.setRelativeEndMilePost(Double
								.parseDouble(endKmField.getText()));
						gradientEffect.setAccelerationChange(0);
						gradientEffect.setDecelerationChange(0);
					}

					GlobalVar.getSectionInputDialog().addGradientFormat(
							gradientFormat);
					GlobalVar.getSectionInputDialog().addGradientEffect(
							gradientEffect);
					//santhosh
					/*ArrayList stationList=GlobalVar.getSectionInputDialog().stationList;
					ArrayList stationStartKmArray=new ArrayList();
					ArrayList stationEndKmArray=new ArrayList();
					for (int s=0;s<stationList.size();s++){
						Station tempStation=(Station)stationList.get(s);
						double startKm=tempStation.startMilePost;
						double endKm=tempStation.endMilePost;
						stationStartKmArray.add(startKm);
						stationEndKmArray.add(endKm);
					}
					Collections.sort(stationStartKmArray);
					Collections.sort(stationEndKmArray);
					double startChainage=(double)stationStartKmArray.get(0);
					double endChainage=(double)stationEndKmArray.get(stationEndKmArray.size()-1);
					
					ArrayList tempGradientFormatList=GlobalVar.getSectionInputDialog().gradientFormatList;
					ArrayList<Gradient> tempUpGradientFormatList=new ArrayList<Gradient>();
					ArrayList<Gradient> tempDownGradientFormatList=new ArrayList<Gradient>();
					for (int r=0;r<tempGradientFormatList.size();r++){
						Gradient tempGradient=(Gradient)tempGradientFormatList.get(r);
						if(tempGradient.getDirection().equals("Up")){
							tempUpGradientFormatList.add(tempGradient);
						}else{
							tempDownGradientFormatList.add(tempGradient);
						}
					}
					if(isGradientUp){
						if(tempUpGradientFormatList.size()==1){
							Gradient lastAddedUpGradient=(Gradient)tempUpGradientFormatList.get(tempUpGradientFormatList.size()-1);
							double laststmile=lastAddedUpGradient.getRelativeStartMilePost();
							double lastenmile=lastAddedUpGradient.getRelativeEndMilePost();
							String lastSlope=lastAddedUpGradient.getGradientValue();
							String lastDir=lastAddedUpGradient.getDirection();
							
								
								double tempStMile=startChainage;
								double tempEndMile=endChainage;
								String tempSlope="0";
								String tempDir=lastDir;
								//GlobalVar.getSectionInputDialog().gradientFormatList.remove(lastAddedUpGradient);
								tempUpGradientFormatList.remove(lastAddedUpGradient);
									Gradient grad1 = new Gradient();
									//tempGradientEffect=new GradientEffect();
	
									//tempGradientEffect.setGradientValue("0");
									
									//tempGradientEffect.setDirection(GlobalVar.UP_DIRECTION);
									//gradientEffect.setAccelerationChange(0);
									//gradientEffect.setDecelerationChange(0);
									grad1.setRelativeStartMilePost(tempStMile);
									grad1.setRelativeEndMilePost(laststmile);
									grad1.setGradientValue(tempSlope);
									grad1.setDirection(lastDir);
									tempUpGradientFormatList.add(grad1);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad1);
									Gradient grad2=new Gradient();
									grad2.setRelativeStartMilePost(laststmile);
									grad2.setRelativeEndMilePost(lastenmile);
									grad2.setGradientValue(lastSlope);
									grad2.setDirection(lastDir);
									tempUpGradientFormatList.add(grad2);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad2);
									Gradient grad3=new Gradient();
									grad3.setRelativeStartMilePost(lastenmile);
									grad3.setRelativeEndMilePost(tempEndMile);
									grad3.setGradientValue(tempSlope);
									grad3.setDirection(lastDir);
									tempUpGradientFormatList.add(grad3);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad3);
								
								
							
						}else{
							Gradient lastAddedUpGradient=(Gradient)tempUpGradientFormatList.get(tempUpGradientFormatList.size()-1);
							double laststmile=lastAddedUpGradient.getRelativeStartMilePost();
							double lastenmile=lastAddedUpGradient.getRelativeEndMilePost();
							String lastSlope=lastAddedUpGradient.getGradientValue();
							String lastDir=lastAddedUpGradient.getDirection();
							for(int u=0;u<tempUpGradientFormatList.size();u++){
								Gradient tempGradientFormat=(Gradient)tempUpGradientFormatList.get(u);
								double tempStMile=tempGradientFormat.getRelativeStartMilePost();
								double tempEndMile=tempGradientFormat.getRelativeEndMilePost();
								String tempSlope=tempGradientFormat.getGradientValue();
								if(laststmile>tempStMile && lastenmile<tempEndMile){
									//GlobalVar.getSectionInputDialog().gradientFormatList.remove(tempGradientFormat);
									//GlobalVar.getSectionInputDialog().gradientFormatList.remove(lastAddedUpGradient);
									tempUpGradientFormatList.remove(tempGradientFormat);
									tempUpGradientFormatList.remove(lastAddedUpGradient);
									Gradient grad1 = new Gradient();
									//tempGradientEffect=new GradientEffect();
		
									//tempGradientEffect.setGradientValue("0");
									
									//tempGradientEffect.setDirection(GlobalVar.UP_DIRECTION);
									//gradientEffect.setAccelerationChange(0);
									//gradientEffect.setDecelerationChange(0);
									grad1.setRelativeStartMilePost(tempStMile);
									grad1.setRelativeEndMilePost(laststmile);
									grad1.setGradientValue(tempSlope);
									grad1.setDirection(lastDir);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad1);
									tempUpGradientFormatList.add(grad1);
									Gradient grad2=new Gradient();
									grad2.setRelativeStartMilePost(laststmile);
									grad2.setRelativeEndMilePost(lastenmile);
									grad2.setGradientValue(lastSlope);
									grad2.setDirection(lastDir);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad2);
									tempUpGradientFormatList.add(grad2);
									Gradient grad3=new Gradient();
									grad3.setRelativeStartMilePost(lastenmile);
									grad3.setRelativeEndMilePost(tempEndMile);
									grad3.setGradientValue(tempSlope);
									grad3.setDirection(lastDir);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad3);
									tempUpGradientFormatList.add(grad3);
								}
								
							}
						}
					}else{

						if(tempDownGradientFormatList.size()==1){
							Gradient lastAddedDownGradient=(Gradient)tempDownGradientFormatList.get(tempDownGradientFormatList.size()-1);
							double laststmile=lastAddedDownGradient.getRelativeStartMilePost();
							double lastenmile=lastAddedDownGradient.getRelativeEndMilePost();
							String lastSlope=lastAddedDownGradient.getGradientValue();
							String lastDir=lastAddedDownGradient.getDirection();
							
								
								double tempStMile=startChainage;
								double tempEndMile=endChainage;
								String tempSlope="0";
								String tempDir=lastDir;
								//GlobalVar.getSectionInputDialog().gradientFormatList.remove(lastAddedDownGradient);
								tempDownGradientFormatList.remove(lastAddedDownGradient);
									Gradient grad1 = new Gradient();
									//tempGradientEffect=new GradientEffect();
	
									//tempGradientEffect.setGradientValue("0");
									
									//tempGradientEffect.setDirection(GlobalVar.UP_DIRECTION);
									//gradientEffect.setAccelerationChange(0);
									//gradientEffect.setDecelerationChange(0);
									grad1.setRelativeStartMilePost(tempStMile);
									grad1.setRelativeEndMilePost(laststmile);
									grad1.setGradientValue(tempSlope);
									grad1.setDirection(lastDir);
									tempDownGradientFormatList.add(grad1);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad1);
									Gradient grad2=new Gradient();
									grad2.setRelativeStartMilePost(laststmile);
									grad2.setRelativeEndMilePost(lastenmile);
									grad2.setGradientValue(lastSlope);
									grad2.setDirection(lastDir);
									tempDownGradientFormatList.add(grad2);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad2);
									Gradient grad3=new Gradient();
									grad3.setRelativeStartMilePost(lastenmile);
									grad3.setRelativeEndMilePost(tempEndMile);
									grad3.setGradientValue(tempSlope);
									grad3.setDirection(lastDir);
									tempDownGradientFormatList.add(grad3);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad3);
								
								
							
						}else{
							Gradient lastAddedUpGradient=(Gradient)tempGradientFormatList.get(tempGradientFormatList.size()-1);
							double laststmile=lastAddedUpGradient.getRelativeStartMilePost();
							double lastenmile=lastAddedUpGradient.getRelativeEndMilePost();
							String lastSlope=lastAddedUpGradient.getGradientValue();
							String lastDir=lastAddedUpGradient.getDirection();
							for(int u=0;u<tempGradientFormatList.size();u++){
								Gradient tempGradientFormat=(Gradient)tempGradientFormatList.get(u);
								double tempStMile=tempGradientFormat.getRelativeStartMilePost();
								double tempEndMile=tempGradientFormat.getRelativeEndMilePost();
								String tempSlope=tempGradientFormat.getGradientValue();
								if(laststmile>tempStMile && lastenmile<tempEndMile){
									//GlobalVar.getSectionInputDialog().gradientFormatList.remove(tempGradientFormat);
									//GlobalVar.getSectionInputDialog().gradientFormatList.remove(lastAddedUpGradient);
									tempDownGradientFormatList.remove(tempGradientFormat);
									tempDownGradientFormatList.remove(lastAddedUpGradient);
									Gradient grad1 = new Gradient();
									//tempGradientEffect=new GradientEffect();
		
									//tempGradientEffect.setGradientValue("0");
									
									//tempGradientEffect.setDirection(GlobalVar.UP_DIRECTION);
									//gradientEffect.setAccelerationChange(0);
									//gradientEffect.setDecelerationChange(0);
									grad1.setRelativeStartMilePost(tempStMile);
									grad1.setRelativeEndMilePost(laststmile);
									grad1.setGradientValue(tempSlope);
									grad1.setDirection(lastDir);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad1);
									tempDownGradientFormatList.add(grad1);
									Gradient grad2=new Gradient();
									grad2.setRelativeStartMilePost(laststmile);
									grad2.setRelativeEndMilePost(lastenmile);
									grad2.setGradientValue(lastSlope);
									grad2.setDirection(lastDir);
									tempDownGradientFormatList.add(grad2);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad2);
									Gradient grad3=new Gradient();
									grad3.setRelativeStartMilePost(lastenmile);
									grad3.setRelativeEndMilePost(tempEndMile);
									grad3.setGradientValue(tempSlope);
									grad3.setDirection(lastDir);
									tempDownGradientFormatList.add(grad3);
									//GlobalVar.getSectionInputDialog().gradientFormatList.add(grad3);
								}
								
							}
						}
					
					}
					GlobalVar.getSectionInputDialog().gradientFormatList.clear();
					GlobalVar.getSectionInputDialog().gradientFormatList.addAll(tempUpGradientFormatList);
					GlobalVar.getSectionInputDialog().gradientFormatList.addAll(tempDownGradientFormatList);*/
					resultLabel.setText("Gradient added");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				resultLabel.setText("Check all inputs");
			}
		}
	};

	ActionListener editActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (editDeleteSlopeField.getText().isEmpty()) {
				resultLabel
						.setText("Enter a slope string to edit a gradient format");
				return;
			}

			String slopeString = editDeleteSlopeField.getText();

			boolean isGradientUp = editDeleteUpDirectionButton.isSelected();
			boolean isGradientLevel = editDeleteLevelDirectionButton
					.isSelected();
			Gradient gradientFormat = GlobalVar.getSectionInputDialog()
					.getGradientBySlopeDirection(slopeString,
							isGradientUp, isGradientLevel);

			if (gradientFormat == null) {
				resultLabel.setText("No such gradient exists");
				return;
			}

			gradientFormatToBeEdited = gradientFormat;
			GradientList.setSelectedItem(gradientFormat.getGradientValue());
			if (gradientFormat.getDirection().equalsIgnoreCase("Up")) {
				upRadioButton.setSelected(true);
			} else if (gradientFormat.getDirection().equalsIgnoreCase("Down")) {
				downRadioButton.setSelected(true);
			} else
				levelRadioButton.setSelected(true);

			startKmField.setText(String.valueOf(gradientFormat.getRelativeStartMilePost()));
			endKmField.setText(String.valueOf(gradientFormat.getRelativeEndMilePost()));

			resultLabel.setText("Required gradient is shown");
		}
	};
	
	
	public Gradient getGradientFormatBySlopeDirection(String slopeString,
			boolean isGradientUp, boolean isGradientlevel) {
		String directionString = "Up";
		if (!isGradientUp && !isGradientlevel)
			directionString = "Down";
		if (isGradientlevel)
			directionString = "Level";

		for (Gradient gradientFormat : gradientFormatList) {
			if (gradientFormat.getGradientValue().equalsIgnoreCase(slopeString)
					&& gradientFormat.getDirection()
							.equalsIgnoreCase(directionString))
				return gradientFormat;
		}

		return null;
	}

	ActionListener deleteActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (editDeleteSlopeField.getText().isEmpty()) {
				resultLabel
						.setText("Enter a slope string to edit a gradient format");
				return;
			}

			String slopeString = editDeleteSlopeField.getText();

			boolean isGradientUp = editDeleteUpDirectionButton.isSelected();
			boolean isGradientLevel = editDeleteLevelDirectionButton
					.isSelected();

			Gradient gradientFormat = GlobalVar.getSectionInputDialog()
					.getGradientBySlopeDirection(slopeString,
							isGradientUp, isGradientLevel);

			if (gradientFormat == null) {
				resultLabel.setText("No such gradient exists");
				return;
			}

			GlobalVar.getSectionInputDialog().removeGradient(
					gradientFormat);
			resultLabel.setText("Gradient deleted");
		}
	};

	ActionListener updateActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (gradientFormatToBeEdited == null) {
					resultLabel.setText("Choose a gradient to update");
					return;
				}

				String slopeString = (String) GradientList.getSelectedItem();
				if (slopeString.isEmpty()) {
					resultLabel.setText("Slope field cannot be empty");
					return;
				}

				boolean isGradientUp = upRadioButton.isSelected();

				Gradient gradientFormat = gradientFormatToBeEdited;
				gradientFormat.setGradientValue(slopeString);
				if (isGradientUp)
					gradientFormat.setDirection("Up");
				else
					gradientFormat.setDirection("Down");
				gradientFormat.setRelativeStartMilePost(Double.parseDouble(startKmField
						.getText()));
				gradientFormat.setRelativeEndMilePost(Double.parseDouble(endKmField
						.getText()));

				resultLabel.setText("Gradient updated");

			} catch (Exception ex) {
				resultLabel.setText("Check all inputs");
			}
		}
	};

	ActionListener viewAllGradientsActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			AllGradientData allGradientData = new AllGradientData();
		}
	};

	ActionListener doneActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			dispose();
		}
	};

	public static void main(String[] args) {
		GradientInputDialog gradientInputDialog = GlobalVar
				.getGradientInputDialog();
	}

	public void write(ArrayList<Gradient> gradientFormatList)
			throws IOException {
		String gradientFileName = FileNames.getGradientFileName();
		BufferedWriter bw = new BufferedWriter(new FileWriter(gradientFileName));

		String formatString = "/* GradientValue    direction    startKM    endKM  */";
		bw.write(formatString);

		for (Gradient gradientFormat : gradientFormatList) {

			bw.write("\"" + gradientFormat.getGradientValue() + "\"");
			bw.write(" ");

			bw.write(gradientFormat.getDirection());
			bw.write(" ");

			bw.write(String.valueOf(gradientFormat.getRelativeStartMilePost()));
			bw.write(" ");

			bw.write(String.valueOf(gradientFormat.getRelativeEndMilePost()));
			bw.write(" ");

			bw.write("\n");
		}
		bw.close();

	}

	public void readGradients(ArrayList<Gradient> gradientFormatList)
			throws IOException {
		String gradientFileName = FileNames.getGradientFileName();
		StreamTokenizer streamTokenizer = new StreamTokenizer(new FileReader(
				gradientFileName));
		streamTokenizer.slashSlashComments(true);
		streamTokenizer.slashStarComments(true);

		while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			String gradientValue = streamTokenizer.sval;

			streamTokenizer.nextToken();
			String directionString = streamTokenizer.sval;

			streamTokenizer.nextToken();
			double startKm = streamTokenizer.nval;

			streamTokenizer.nextToken();
			double endKm = streamTokenizer.nval;

			Gradient gradientFormat = new Gradient(gradientValue,
					directionString, startKm, endKm);
			gradientFormatList.add(gradientFormat);
		}
	}

}

class AllGradientData extends JFrame implements TableModelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable gradientFormatDataTable;

	public AllGradientData() {
		super("List of gradients");

		gradientFormatDataTable = new JTable(new AbstractTableModel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			String[] columnNames = { "Slope", "Direction", "Start Km",
					"End Km", };
			SectionInputDialog sectioninputdialog = GlobalVar
					.getSectionInputDialog();
			ArrayList<Gradient> gradientFormatList = sectioninputdialog.gradientFormatList;

			public String getColumnName(int col) {
				return columnNames[col];
			}

			public int getRowCount() {
				return gradientFormatList.size();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return gradientFormatList.get(row).getGradientValue();
				case 1:
					return gradientFormatList.get(row).getDirection();
				case 2:
					return gradientFormatList.get(row).getRelativeStartMilePost();
				case 3:
					return gradientFormatList.get(row).getRelativeEndMilePost();

				}

				return null;
			}

		});

		JScrollPane scrollPane = new JScrollPane(gradientFormatDataTable);
		gradientFormatDataTable
				.setPreferredScrollableViewportSize(new Dimension(500, 70));
		getContentPane().add(scrollPane);
		setBounds(100, 100, 400, 400);
		setVisible(true);
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
	}

}

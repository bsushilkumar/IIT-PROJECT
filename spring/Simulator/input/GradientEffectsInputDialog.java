package input;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.trackProperties.GradientEffect;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class GradientEffectsInputDialog extends InputDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JLabel slopeLabel = new JLabel("Gradient value e.g. 1/100");
	public JTextField slopeField = new JTextField();

	public JLabel directionLabel = new JLabel("Direction");
	public ButtonGroup directionButtonGroup = new ButtonGroup();
	public JRadioButton upRadioButton = new JRadioButton("Up");
	public JRadioButton downRadioButton = new JRadioButton("Down");

	public JLabel accelerationEffectLabel = new JLabel("Effect on acceleration in km/min^2");
	public JTextField accelerationEffectField = new JTextField();

	public JLabel decelerationEffectLabel = new JLabel("Effect on deceleration in km/min^2");
	public JTextField decelerationEffectField = new JTextField();

	public JButton addButton = new JButton("Add");
	public JButton viewAllGradientEffectsButton = new JButton(
			"View all gradients");

	public JLabel editDeleteOptionLabel = new JLabel("Edit/Delete");
	public JLabel editDeleteSlopeLabel = new JLabel("Gradient Value e.g. 1/100");
	public JLabel editDeleteDirectionLabel = new JLabel("Direction");

	public JTextField editDeleteSlopeField = new JTextField();
	public ButtonGroup editDeleteDirectionButtonGroup = new ButtonGroup();
	public JRadioButton editDeleteUpDirectionButton = new JRadioButton("Up");
	public JRadioButton editDeleteDownDirectionButton = new JRadioButton("Down");

	public JButton editButton = new JButton("Edit");
	public JButton deleteButton = new JButton("Delete");
	public JButton updateButton = new JButton("Update");

	public JLabel resultLabel = new JLabel("");

	public GradientEffect gradientEffectToBeEdited = null;

	public GradientEffectsInputDialog() {
		this.setBounds(x, y, width, height);

		setComponentActionListeners();
		setComponentBounds();
		addComponents();
		this.add(jpanel);

		this.setTitle("Gradient Effects Input");
		// this.setVisible(true);

	}

	@Override
	public void addComponents() {
		this.getContentPane().add(slopeLabel);
		this.getContentPane().add(slopeField);

		this.getContentPane().add(directionLabel);

		this.getContentPane().add(upRadioButton);
		this.getContentPane().add(downRadioButton);
		directionButtonGroup.add(upRadioButton);
		directionButtonGroup.add(downRadioButton);
		upRadioButton.setSelected(true);

		this.getContentPane().add(accelerationEffectLabel);
		this.getContentPane().add(accelerationEffectField);
		this.getContentPane().add(decelerationEffectLabel);
		this.getContentPane().add(decelerationEffectField);

		this.getContentPane().add(addButton);
		this.getContentPane().add(viewAllGradientEffectsButton);

		this.getContentPane().add(editDeleteOptionLabel);
		this.getContentPane().add(editDeleteSlopeLabel);
		this.getContentPane().add(editDeleteDirectionLabel);

		this.getContentPane().add(editDeleteSlopeField);
		this.getContentPane().add(editDeleteUpDirectionButton);
		this.getContentPane().add(editDeleteDownDirectionButton);
		editDeleteDirectionButtonGroup.add(editDeleteUpDirectionButton);
		editDeleteDirectionButtonGroup.add(editDeleteDownDirectionButton);
		editDeleteUpDirectionButton.setSelected(true);

		this.getContentPane().add(editButton);

		this.getContentPane().add(deleteButton);
		this.getContentPane().add(updateButton);
		this.getContentPane().add(resultLabel);

	}

	@Override
	public void setComponentBounds() {
		int yDifference = 25;
		buttonWidth = 100;
		labelWidth = 260;
		x2 = x1 + labelWidth + 20;

		slopeLabel.setBounds(x1, y1, labelWidth, labelHeight);
		slopeField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		directionLabel.setBounds(x1, y1, labelWidth, labelHeight);
		upRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
		y1 += yDifference;
		downRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		accelerationEffectLabel.setBounds(x1, y1, labelWidth, labelHeight);
		accelerationEffectField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		decelerationEffectLabel.setBounds(x1, y1, labelWidth, labelHeight);
		decelerationEffectField.setBounds(x2, y1, fieldWidth, fieldHeight);

		y1 += yDifference;
		addButton.setBounds(x1, y1, buttonWidth, buttonHeight);
		viewAllGradientEffectsButton.setBounds(x2, y1, 180, fieldHeight);

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
		editButton.setBounds(x1, y1, buttonWidth, buttonHeight);
		deleteButton.setBounds(x2, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		updateButton.setBounds(x1, y1, buttonWidth, buttonHeight);

		y1 += yDifference;
		resultLabel.setBounds(x1, y1, 300, labelHeight);
	}

	@Override
	public void setComponentActionListeners() {
		addButton.addActionListener(addActionListener);
		editButton.addActionListener(editActionListener);
		deleteButton.addActionListener(deleteActionListener);
		updateButton.addActionListener(updateActionListener);
		viewAllGradientEffectsButton
				.addActionListener(viewAllGradientEffectsActionListener);
	}

	ActionListener addActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				String slopeString = slopeField.getText();
				if (slopeString.isEmpty()) {
					resultLabel.setText("Slope field cannot be empty");
					return;
				}

				boolean isGradientUp = upRadioButton.isSelected();

				GradientEffect gradientEffect = new GradientEffect();
				gradientEffect.gradientValue = slopeString;
				if (isGradientUp)
					gradientEffect.direction = GlobalVar.UP_DIRECTION;
				else
					gradientEffect.direction = GlobalVar.DOWN_DIRECTION;
				gradientEffect.accelerationChange = Double
						.parseDouble(accelerationEffectField.getText());
				gradientEffect.decelerationChange = Double
						.parseDouble(decelerationEffectField.getText());

				GlobalVar.getSectionInputDialog().addGradientEffect(
						gradientEffect);
				resultLabel.setText("Gradient effect added");

			} catch (Exception ex) {
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
			GradientEffect gradientEffect = GlobalVar.getSectionInputDialog()
					.getGradientEffectBySlopeDirection(slopeString,
							isGradientUp);

			if (gradientEffect == null) {
				resultLabel.setText("No such gradient exists");
				return;
			}

			gradientEffectToBeEdited = gradientEffect;

			slopeField.setText(gradientEffect.gradientValue);
			if (GlobalVar.getDirectionStringFromDirection(gradientEffect.direction).equalsIgnoreCase("Up")) {
				upRadioButton.setSelected(true);
			} else
				downRadioButton.setSelected(true);

			accelerationEffectField.setText(String
					.valueOf(gradientEffect.accelerationChange));
			decelerationEffectField.setText(String
					.valueOf(gradientEffect.decelerationChange));

			resultLabel.setText("Required gradient effect is shown");
		}
	};

	ActionListener deleteActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (editDeleteSlopeField.getText().isEmpty()) {
				resultLabel
						.setText("Enter a slope string to edit a gradient effect");
				return;
			}

			String slopeString = editDeleteSlopeField.getText();

			boolean isGradientUp = editDeleteUpDirectionButton.isSelected();
			GradientEffect gradientEffect = GlobalVar.getSectionInputDialog()
					.getGradientEffectBySlopeDirection(slopeString,
							isGradientUp);

			if (gradientEffect == null) {
				resultLabel.setText("No such gradient effect exists");
				return;
			}

			GlobalVar.getSectionInputDialog().removeGradientEffect(
					gradientEffect);
			resultLabel.setText("Gradient effect deleted");
		}
	};

	ActionListener updateActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (gradientEffectToBeEdited == null) {
					resultLabel.setText("Choose a gradient effect to update");
					return;
				}

				String slopeString = slopeField.getText();
				if (slopeString.isEmpty()) {
					resultLabel.setText("Slope field cannot be empty");
					return;
				}

				boolean isGradientUp = upRadioButton.isSelected();

				GradientEffect gradientEffect = gradientEffectToBeEdited;
				gradientEffect.gradientValue = slopeString;
				if (isGradientUp)
					gradientEffect.direction = GlobalVar.UP_DIRECTION;
				else
					gradientEffect.direction = GlobalVar.DOWN_DIRECTION;
				gradientEffect.accelerationChange = Double
						.parseDouble(accelerationEffectField.getText());
				gradientEffect.decelerationChange = Double
						.parseDouble(decelerationEffectField.getText());

				resultLabel.setText("Gradient effect updated");

			} catch (Exception ex) {
				resultLabel.setText("Check all inputs");
			}
		}
	};

	ActionListener viewAllGradientEffectsActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			AllGradientEffectData allGradientEffectData = new AllGradientEffectData();
		}
	};

	public static void main(String[] args) {
		GradientEffectsInputDialog gradientEffectsInputDialog = GlobalVar
				.getGradientEffectsInputDialog();
	}

	public void write(ArrayList<GradientEffect> gradientEffectList)
			throws IOException {
		String gradientEffectFileName = FileNames.getGradientEffectsFileName();
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				gradientEffectFileName));

		String formatString = "/*  GradientValue  Direction   accelerationChange   decelerationChange */";
		bw.write(formatString);

		for (GradientEffect gradientEffect : gradientEffectList) {

			bw.write("\"" + gradientEffect.gradientValue + "\"");
			bw.write(" ");

			bw.write(gradientEffect.direction);
			bw.write(" ");

			bw.write(String.valueOf(gradientEffect.accelerationChange));
			bw.write(" ");

			bw.write(String.valueOf(gradientEffect.decelerationChange));
			bw.write(" ");

			bw.write("\n");
		}
		bw.close();

	}

	public void readGradientEffects(ArrayList<GradientEffect> gradientEffectList)
			throws IOException {
		String gradientEffectsFileName = FileNames.getGradientEffectsFileName();
		StreamTokenizer streamTokenizer = new StreamTokenizer(new FileReader(
				gradientEffectsFileName));
		streamTokenizer.slashSlashComments(true);
		streamTokenizer.slashStarComments(true);

		while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			String gradientValue = streamTokenizer.sval;

			streamTokenizer.nextToken();
			String directionString = streamTokenizer.sval;

			streamTokenizer.nextToken();
			double accelerationChange = streamTokenizer.nval;

			streamTokenizer.nextToken();
			double decelerationChange = streamTokenizer.nval;

			GradientEffect gradientEffect = new GradientEffect();
			gradientEffect.gradientValue = gradientValue;
			//Sneha : this was giving exception while opening previously created test case.In which gradient information
			//was not entered.so temperary commented out.
			gradientEffect.direction = GlobalVar.getDirectionIntFromString(directionString);
			gradientEffect.accelerationChange = accelerationChange;
			gradientEffect.decelerationChange = decelerationChange;
			
			gradientEffectList.add(gradientEffect);
			
		}
	}

}

class AllGradientEffectData extends JFrame implements TableModelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable gradientEffectDataTable;

	public AllGradientEffectData() {
		super("List of gradients");

		gradientEffectDataTable = new JTable(new AbstractTableModel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			String[] columnNames = { "Slope", "Direction", "Start Km",
					"End Km", };
			SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
			ArrayList<GradientEffect> gradientEffectList = sectioninputdialog.gradientEffectList;

			public String getColumnName(int col) {
				return columnNames[col];
			}

			public int getRowCount() {
				return gradientEffectList.size();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return gradientEffectList.get(row).gradientValue;
				case 1:
					return gradientEffectList.get(row).direction;
				case 2:
					return gradientEffectList.get(row).accelerationChange;
				case 3:
					return gradientEffectList.get(row).decelerationChange;

				}

				return null;
			}

		});

		JScrollPane scrollPane = new JScrollPane(gradientEffectDataTable);
		gradientEffectDataTable
				.setPreferredScrollableViewportSize(new Dimension(500, 70));
		getContentPane().add(scrollPane);
		setBounds(100, 100, 400, 400);
		setVisible(true);
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
	}

}

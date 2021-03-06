package input;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Link;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntities.trackProperties.SpeedRestriction;
import gui.entities.sectionEntityList.BlockList;
import sun.java2d.pipe.GlyphListLoopPipe;

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
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BlockInput extends InputDialog {

	private static final long serialVersionUID = 1L;
	public JLabel stationinfoLabel = new JLabel(
			"Please enter the initial station for inserting blocks");
	public JComboBox stationno = new JComboBox();
	String[] stationNames = null;
	String[] nextstationNames = null;
	public JComboBox uploops = new JComboBox();
	Integer[] upList = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	public JComboBox commonloops = new JComboBox();
	Integer[] commonList = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	public JComboBox downloops = new JComboBox();
	Integer[] downList = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	public JLabel succedingno = new JLabel();
	public JLabel uploopsLabel = new JLabel("Up Blocks");
	public JLabel downloopsLabel = new JLabel("Down Blocks");
	public JLabel commonloopsLabel = new JLabel("Common Blocks");
	public JButton Submit1Button = new JButton("Submit");
	public JButton Submit2Button = new JButton("Submit");
	
	//sneha : added following submit3button
	//previously Submit1Button action handler was updating up and down both block
	//and Submit2Button action handler was updating common block
	//now Submit1Button updates up block,Submit2Button updates common block and Submit3Button updates down block
	public JButton Submit3Button = new JButton("Submit");
	
	
	public JButton DoneButton = new JButton("Done");
	public JButton ViewAllBlockButton = new JButton("View All ");
	public JLabel resultLabel = new JLabel("");
	public JLabel startkm1Label = new JLabel("Start kms.");
	public JLabel endkm1Label = new JLabel("End kms.");
	public JLabel startkm2Label = new JLabel("Start kms.");
	public JLabel endkm2Label = new JLabel("End kms.");
	public JLabel startkm3Label = new JLabel("Start kms.");
	public JLabel endkm3Label = new JLabel("End kms.");
	public JLabel BlockVelocity1Label = new JLabel("Max Speed");
	public JLabel BlockVelocity2Label = new JLabel("Max Speed");
	public JLabel BlockVelocity3Label = new JLabel("Max Speed");

	public JLabel Speedrestrictionstartkm1Label = new JLabel(
			"<html>Speed Rest.<br>    Start kms.</html>");
	public JLabel Speedrestrictionendkm1Label = new JLabel(
			"<html>Speed Rest.<br>    End kms.</html>");
	public JLabel Speedrestrictionmaxspeed1Label = new JLabel("Speed Limit");
	public JLabel Speedrestrictionstartkm2Label = new JLabel(
			"<html>Speed Rest.<br>    Start kms.</html>");
	public JLabel Speedrestrictionendkm2Label = new JLabel(
			"<html>Speed Rest.<br>    End kms.</html>");
	public JLabel Speedrestrictionmaxspeed2Label = new JLabel("Speed Limit");
	public JLabel Speedrestrictionstartkm3Label = new JLabel(
			"<html>Speed Rest.<br>    Start kms.</html>");
	public JLabel Speedrestrictionendkm3Label = new JLabel(
			"<html>Speed Rest.<br>    End kms.</html>");
	public JLabel Speedrestrictionmaxspeed3Label = new JLabel("Speed Limit");

	public ArrayList<JTextField> startkm1FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> startkm2FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> startkm3FieldList = new ArrayList<JTextField>();
	
	public ArrayList<JTextField> endkm1FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> endkm2FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> endkm3FieldList = new ArrayList<JTextField>();
	
	public ArrayList<JTextField> BlockVelocity1FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> BlockVelocity2FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> BlockVelocity3FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> Speedrestrictionstartkm1FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> Speedrestrictionendkm1FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> Speedrestrictionmaxspeed1FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> Speedrestrictionstartkm2FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> Speedrestrictionendkm2FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> Speedrestrictionmaxspeed2FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> Speedrestrictionstartkm3FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> Speedrestrictionendkm3FieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> Speedrestrictionmaxspeed3FieldList = new ArrayList<JTextField>();

	
	public ArrayList<Integer> UpBlockIdList = new ArrayList<Integer>();
	public ArrayList<Integer> DownBlockIdList = new ArrayList<Integer>();
	public ArrayList<Integer> CommonBlockIdList = new ArrayList<Integer>();

	public JTextField startkm1Field = new JTextField("");
	public JTextField endkm1Field = new JTextField("");
	public JTextField startkm2Field = new JTextField("");
	public JTextField endkm2Field = new JTextField("");
	public JTextField startkm3Field = new JTextField("");
	public JTextField endkm3Field = new JTextField("");
	public JTextField BlockVelocity1Field = new JTextField("");
	public JTextField BlockVelocity2Field = new JTextField("");
	public JTextField BlockVelocity3Field = new JTextField("");

	public JTextField Speedrestrictionstartkm1Field = new JTextField("");
	public JTextField Speedrestrictionendkm1Field = new JTextField("");
	public JTextField Speedrestrictionmaxspeed1Field = new JTextField("");
	public JTextField Speedrestrictionstartkm2Field = new JTextField("");
	public JTextField Speedrestrictionendkm2Field = new JTextField("");
	public JTextField Speedrestrictionmaxspeed2Field = new JTextField("");
	public JTextField Speedrestrictionstartkm3Field = new JTextField("");
	public JTextField Speedrestrictionendkm3Field = new JTextField("");
	public JTextField Speedrestrictionmaxspeed3Field = new JTextField("");

	public JRadioButton CommonBlockButton = new JRadioButton(" Bidirectional");

    //public JRadioButton UpDownBlockButton = new JRadioButton("Double Line");
	
	//sneha:earlier radio button group was having one of two possible selection
	//Bidirectional and Double Line
	//Now it have Bidirectional , up block and down block
	public JRadioButton UpBlockButton = new JRadioButton("Up Block");
	public JRadioButton DownBlockButton = new JRadioButton("Down Block");

	
	public ButtonGroup BlockTypeButtonGroup = new ButtonGroup();
	public JLabel BlockTypeLabel = new JLabel("Please choose block type");
	public String StationName;
	public String NextStationName;
	public int mesut, ozil;
	public JButton editBlock=new JButton("Edit Blocks");
	int prev = 0;

	public BlockInput() {
		x = 100;
		y = 2;
		width = 1100;
		height = 750;

		SectionInputDialog sectioninputdialog = GlobalVar
				.getSectionInputDialog();
		stationNames = sectioninputdialog.getStationNames();

		if (stationNames.length > 0) {
			String[] l = Arrays.copyOf(stationNames, stationNames.length - 1);

			stationno = new JComboBox<String>(l);
		}
		nextstationNames = sectioninputdialog.getStationNames();

		uploops = new JComboBox<Integer>(upList);

		downloops = new JComboBox<Integer>(downList);
		commonloops = new JComboBox<Integer>(commonList);

		// cards = new JPanel(new CardLayout());

		this.setBounds(x, y, width, height);
		setComponentActionListeners();
		setComponentBounds();
		addComponents();

		this.add(jpanel);
		this.setTitle("Block Input");
		// this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// this.setVisible(true);

	}

	//sneha : this method updates the station names in blockinput stationno Jcombobox.
	//called from add station action listner whenever new station is added.  
	@SuppressWarnings("unchecked")
	public void updateStationNames(){
		SectionInputDialog sectioninputdialog = GlobalVar
				.getSectionInputDialog();
		stationno.setEnabled(false);
		
		stationNames = sectioninputdialog.getStationNames();
		if (stationNames.length > 0) {
			String[] l = Arrays.copyOf(stationNames, stationNames.length - 1);
			@SuppressWarnings("rawtypes")
			DefaultComboBoxModel model = (DefaultComboBoxModel) stationno.getModel();
			model.removeAllElements();
			for(int i=0;i<l.length;i++){
				model.addElement(l[i]);
				}
			stationno.setModel(model);
			nextstationNames = sectioninputdialog.getStationNames();
		}
		stationno.setEnabled(true);
	}
	
	public void addComponents() {
		this.add(stationno);
		this.add(stationinfoLabel);
		this.add(succedingno);
		this.add(resultLabel);

		this.add(BlockTypeLabel);
		this.add(CommonBlockButton);
//		this.add(UpDownBlockButton);
		

		BlockTypeButtonGroup.add(CommonBlockButton);
//		BlockTypeButtonGroup.add(UpDownBlockButton);
		
		//sneha : added folowwing two button
		this.add(UpBlockButton);
		this.add(DownBlockButton);
		BlockTypeButtonGroup.add(UpBlockButton);
		BlockTypeButtonGroup.add(DownBlockButton);
		UpBlockButton.setVisible(false);
		DownBlockButton.setVisible(false);
		
		CommonBlockButton.setVisible(false);
//		UpDownBlockButton.setVisible(false);
		BlockTypeLabel.setVisible(false);
		this.add(startkm1Label);
		startkm1Label.setVisible(false);
		this.add(startkm2Label);
		startkm2Label.setVisible(false);
		this.add(endkm1Label);
		endkm1Label.setVisible(false);

		this.add(endkm2Label);
		endkm2Label.setVisible(false);

		this.add(downloopsLabel);
		downloopsLabel.setVisible(false);
		this.add(uploopsLabel);
		uploopsLabel.setVisible(false);
		this.add(uploops);
		uploops.setVisible(false);
		this.add(downloops);
		downloops.setVisible(false);
		this.add(BlockVelocity1Label);
		BlockVelocity1Label.setVisible(false);
		this.add(BlockVelocity2Label);
		BlockVelocity2Label.setVisible(false);

		this.add(commonloops);
		commonloops.setVisible(false);
		this.add(commonloopsLabel);
		commonloopsLabel.setVisible(false);
		this.add(endkm3Label);
		endkm3Label.setVisible(false);
		this.add(startkm3Label);
		startkm3Label.setVisible(false);

		this.add(BlockVelocity3Label);
		BlockVelocity3Label.setVisible(false);

		this.add(Submit1Button);
		Submit1Button.setVisible(false);
		this.add(Submit2Button);
		Submit2Button.setVisible(false);
		this.add(Submit3Button);
		Submit3Button.setVisible(false);


		this.add(ViewAllBlockButton);
		//Sneha : commented this to ensure that view all button is visible all the time
		//ViewAllBlockButton.setVisible(false);
		ViewAllBlockButton.setVisible(true);
		this.add(DoneButton);
		DoneButton.setVisible(false);
		this.add(Speedrestrictionstartkm1Label);
		Speedrestrictionstartkm1Label.setVisible(false);
		this.add(Speedrestrictionendkm1Label);
		Speedrestrictionendkm1Label.setVisible(false);
		this.add(Speedrestrictionmaxspeed1Label);
		Speedrestrictionmaxspeed1Label.setVisible(false);
		this.add(Speedrestrictionstartkm2Label);
		Speedrestrictionstartkm2Label.setVisible(false);
		this.add(Speedrestrictionendkm2Label);
		Speedrestrictionendkm2Label.setVisible(false);

		this.add(Speedrestrictionmaxspeed2Label);
		Speedrestrictionmaxspeed2Label.setVisible(false);
		this.add(Speedrestrictionstartkm3Label);
		Speedrestrictionstartkm3Label.setVisible(false);
		this.add(Speedrestrictionendkm3Label);
		Speedrestrictionendkm3Label.setVisible(false);
		this.add(Speedrestrictionmaxspeed3Label);
		Speedrestrictionmaxspeed3Label.setVisible(false);
		
		this.add(editBlock);
		editBlock.setVisible(false);
	};

	public void setComponentBounds() {
		x = 10;
		y = 100;
		width = 800;
		height = 800;

		int yDifference = 25;
		labelWidth += 80;
		x2 += 40;
		fieldWidth = 2 * fieldWidth / 5;

		y1 += yDifference;
		stationinfoLabel.setBounds((x2 / 2) + x2 - x1 / 2, y1, 3 * labelWidth,
				labelHeight);

		y1 += 2 * yDifference;
		stationno.setBounds(x1 + x2 + (x2 / 2), y1, 5 * fieldWidth / 2,
				fieldHeight);
		succedingno.setBounds(x1 + 2 * x2 + (x2 / 2), y1, 2 * labelWidth,
				labelHeight);

		y1 += 2 * yDifference;
		BlockTypeLabel.setBounds(7 * x2 / 5 + x1 + (x2 / 2), y1,
				3 * labelWidth, labelHeight);

		y1 += yDifference;
		


//		UpDownBlockButton.setBounds(x1 + (x2 / 2)     , y1, labelWidth,	labelHeight);
		CommonBlockButton.setBounds(x1 + x2 + (x2 / 2) , y1, labelWidth,	labelHeight);
		UpBlockButton.setBounds(x1 + 2* x2 + (x2 / 2)      , y1, labelWidth,	labelHeight);
		DownBlockButton.setBounds(x1 + 3* x2 + (x2 / 2)      , y1, labelWidth,	labelHeight);


		y1 += 2 * yDifference;
		uploopsLabel.setBounds(x1 + 3 * x2 / 4, y1, labelWidth, labelHeight);
		commonloopsLabel.setBounds((x1 + x2) / 2 + (x2), y1, labelWidth,
				labelHeight);
		downloopsLabel.setBounds(x1 + x2 / 2 + (5 * x2 / 2), y1, labelWidth,
				labelHeight);

		y1 += 2 * yDifference;
		uploops.setBounds(3 * x1 / 2 + 3 * x2 / 4, y1, fieldWidth, fieldHeight);
		commonloops
				.setBounds((x1 + x2) / 2 + (x2), y1, fieldWidth, fieldHeight);
		downloops.setBounds(2 * x1 + x2 / 2 + (5 * x2 / 2), y1, fieldWidth,
				fieldHeight);

		y1 += 2 * yDifference;
		startkm1Label.setBounds(3 * x1 / 2, y1, fieldWidth, fieldHeight);
		endkm1Label.setBounds(3 * x1 / 2 + 35 * x2 / 100, y1, fieldWidth,
				fieldHeight);
		BlockVelocity1Label.setBounds(3 * x1 / 2 + 70 * x2 / 100, y1,
				fieldWidth, fieldHeight);

		startkm2Label.setBounds(3 * x1 / 2 + 5 * x2 / 2, y1, fieldWidth,
				fieldHeight);
		endkm2Label.setBounds(3 * x1 + 285 * x2 / 100, y1, fieldWidth,
				fieldHeight);
		BlockVelocity2Label.setBounds(3 * x1 + 320 * x2 / 100, y1,
				5 * fieldWidth / 3, fieldHeight);

		startkm3Label.setBounds((5 * x2) / 4, y1, fieldWidth, fieldHeight);
		endkm3Label.setBounds((5 * x2) / 4 + 35 * x2 / 100, y1, fieldWidth,
				fieldHeight);
		BlockVelocity3Label.setBounds((5 * x2) / 4 + 70 * x2 / 100, y1,
				5 * fieldWidth / 3, fieldHeight);

		Speedrestrictionstartkm1Label.setBounds(3 * x1 + 90 * x2 / 100, y1,
				2 * fieldWidth, 2 * fieldHeight);
		Speedrestrictionendkm1Label.setBounds(3 * x1 + 130 * x2 / 100, y1,
				2 * fieldWidth, 2 * fieldHeight);
		Speedrestrictionmaxspeed1Label.setBounds(3 * x1 + 165 * x2 / 100, y1,
				fieldWidth, fieldHeight);

		Speedrestrictionstartkm2Label.setBounds(3 * x1 + 345 * x2 / 100, y1,
				2 * fieldWidth, 2 * fieldHeight);
		Speedrestrictionendkm2Label.setBounds(3 * x1 + 380 * x2 / 100, y1,
				2 * fieldWidth, 2 * fieldHeight);
		Speedrestrictionmaxspeed2Label.setBounds(3 * x1 + 415 * x2 / 100, y1,
				fieldWidth, fieldHeight);

		Speedrestrictionstartkm3Label.setBounds((5 * x2) / 4 + 105 * x2 / 100,
				y1, 2 * fieldWidth, 2 * fieldHeight);
		Speedrestrictionendkm3Label.setBounds((5 * x2) / 4 + 140 * x2 / 100,
				y1, 2 * fieldWidth, 2 * fieldHeight);
		Speedrestrictionmaxspeed3Label.setBounds((5 * x2) / 4 + 170 * x2 / 100,
				y1, fieldWidth, fieldHeight);

		y1 = 580 + 2 * yDifference;
		Submit1Button.setBounds(7 * x1 / 2 + x2, y1, 2 * labelWidth / 5,
				labelHeight);
		Submit2Button.setBounds(7 * x1 / 2 + x2, y1, 2 * labelWidth / 5,
				labelHeight);
		Submit3Button.setBounds(7 * x1 / 2 + x2, y1, 2 * labelWidth / 5,
				labelHeight);

		ViewAllBlockButton.setBounds(15 * x1 / 2 + x2, y1, 2 * labelWidth / 5,
				labelHeight);
		DoneButton.setBounds(22 * x1 / 2 + x2, y1, 2 * labelWidth / 5,
				labelHeight);
		editBlock.setBounds(30 * x1 / 2 + x2, y1, 2 * labelWidth / 5,
				labelHeight);
		y1 = 650 + yDifference;
		resultLabel.setBounds(7 * x1 / 2 + 3 * x2 / 2, y1, (2 * labelWidth / 3)*2,
				labelHeight);

	};

	public void setComponentActionListeners() {
		stationno.addActionListener(stationnoActionListener);
		
		/*CommonBlockButton.addItemListener(BlockTypeItemListener);
		UpDownBlockButton.addItemListener(BlockTypeItemListener);
		*/
		
		//Sneha:implemented new BLOCKTypeIntemListener_new()
		//older implementation of BlockTypeIntemListener was
		//updating the panel based on two radio button mainly CommonBlockButton and UpDownBlockButton
		//Now new implementation is based on three radio button mainly CommonBlockButton ,DownBlockButton and UpBlockButton
		CommonBlockButton.addItemListener(BlockTypeItemListener_new);
		UpBlockButton.addItemListener(BlockTypeItemListener_new);
		DownBlockButton.addItemListener(BlockTypeItemListener_new);
		
		
		Submit1Button.addActionListener(Submit1ButtonActionListener);
		Submit2Button.addActionListener(Submit2ButtonActionListener);
		Submit3Button.addActionListener(Submit3ButtonActionListener);
		ViewAllBlockButton.addActionListener(viewAllBlockActionListener);
		
		DoneButton.addActionListener(DoneButtonActionListener);
		downloops.addActionListener(downloopsActionListener);
		uploops.addActionListener(uploopsActionListener);
		commonloops.addActionListener(commonloopsActionListener);
		editBlock.addActionListener(EditBlockActionListener);
	};
	
	
	ActionListener EditBlockActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultLabel.setText("");
			SectionInputDialog sectioninputdialog = GlobalVar
					.getSectionInputDialog();
			//Sneha:Come here to ensure why substring is just 1st digit?????????????
			//test with bit bigger input having more then 10 station

			ArrayList tempArray=sectioninputdialog.blockList;
			ArrayList temploopArray=sectioninputdialog.loopList;
			int stationId=stationno.getSelectedIndex()+1;
			int nextStationId=stationId+1;
			for (int l=0;l<temploopArray.size();l++){
				Loop tempLoop=(Loop)temploopArray.get(l);
				String tempLoopNo=String.format("%07d",tempLoop.blockNo);
				int checkLoopNo=Integer.parseInt(tempLoopNo.substring(0, 3));
				if(stationId==checkLoopNo){
					tempLoop.upLinks.clear();
					
				}else if(nextStationId==checkLoopNo){
					tempLoop.downLinks.clear();
					
				}
			}

			for (int b=tempArray.size()-1;b>=0;b--){
				Block tempBlock=(Block)tempArray.get(b);
				String tempBlockNo=String.format("%07d",tempBlock.getBlockNo());
				int checkBlockNo=Integer.parseInt(tempBlockNo.substring(0,3).toString());
				if(stationId==checkBlockNo){
					sectioninputdialog.blockList.remove(tempBlock);
					//System.out.println("tempBlock.blockNo: " + " " + tempBlock.blockNo);
				}
			}
			
/*			if(startkm3FieldList.size()==0){
				
				CommonBlockButton.doClick();
				UpDownBlockButton.setSelected(true);
				succedingno.setText(nextstationNames[(stationno
						.getSelectedIndex() + 1)]);
			}else{
				UpDownBlockButton.doClick();
				CommonBlockButton.setSelected(true);
				succedingno.setText(nextstationNames[(stationno
						.getSelectedIndex() + 1)]);
			}*/
		}
		
	};

	

	ActionListener stationnoActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultLabel.setText("");
			
			//Sneha : added another check whether stationno dialogbox is enabled or not
			//if new station is added then adding new item generates an event so to
			//avoid this temp stationno is disabled 
			//check updatestationnames method.
			if (stationno.getSelectedIndex() >= 0 && stationno.isEnabled()) {

				BlockTypeButtonGroup.clearSelection();
				CommonBlockButton.setVisible(false);
     			//UpDownBlockButton.setVisible(false);
				UpBlockButton.setVisible(false);
				DownBlockButton.setVisible(false);
				BlockTypeLabel.setVisible(false);
				ViewAllBlockButton.setVisible(true);

				int count = 0;
				SectionInputDialog sectioninputdialog = GlobalVar
						.getSectionInputDialog();
				ArrayList<Block> blockList = sectioninputdialog.blockList;
				for (Block block : blockList) {
					String blockId = String.format("%07d", block.getBlockNo());

					int blockstationId=0;
					if(blockId.length()==6){
					blockstationId = Integer.parseInt(blockId.substring(0,2)) - 1;
					}
					else
					{
						blockstationId = Integer.parseInt(blockId.substring(0,3)) - 1;
					}
					
					
					if (blockstationId == stationno.getSelectedIndex()) {
						count++;
					}
				}
				
				
				
				if (count == 0)
				{

					succedingno.setText(nextstationNames[(stationno
							.getSelectedIndex() + 1)]);
					StationName = stationNames[(stationno.getSelectedIndex())];
					NextStationName = nextstationNames[(stationno
							.getSelectedIndex() + 1)];


					//sneha : implemented allowedblock type test between two station
					boolean allowUpBlock = testAllowedUpBlock(StationName, NextStationName);
					boolean allowDownBlock = testAllowedDownBlock(StationName, NextStationName);
					boolean allowCommonBlock= testAllowedcommonBlock(StationName, NextStationName);
					
					if(allowCommonBlock)
					CommonBlockButton.setVisible(true);
					//UpDownBlockButton.setVisible(true);
	
					if(allowUpBlock)
					UpBlockButton.setVisible(true);
					
					if(allowDownBlock)
					DownBlockButton.setVisible(true);
					BlockTypeLabel.setVisible(true);
				} else {

					int editResponse = JOptionPane.showConfirmDialog(null, "Do you want to edit the block details of selected station", "Confirm",
					        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(editResponse==JOptionPane.YES_NO_OPTION){
						int confirmEdit = JOptionPane.showConfirmDialog(null, "All existing block details will be erased", "Confirm",
						        JOptionPane. OK_CANCEL_OPTION , JOptionPane.QUESTION_MESSAGE);
						if(confirmEdit==JOptionPane.OK_OPTION){
							editBlock.doClick();
							
							/*if(startkm3FieldList.size()==0){
								UpDownBlockButton.setSelected(true);
								succedingno.setText(nextstationNames[(stationno
										.getSelectedIndex() + 1)]);
							}else{
								CommonBlockButton.setSelected(true);
								succedingno.setText(nextstationNames[(stationno
										.getSelectedIndex() + 1)]);
							}*/
							succedingno.setText(nextstationNames[(stationno
									.getSelectedIndex() + 1)]);
							StationName = stationNames[(stationno.getSelectedIndex())];
							NextStationName = nextstationNames[(stationno
									.getSelectedIndex() + 1)];
							


							//sneha : implemented allowedblock type test between two station
							boolean allowUpBlock = testAllowedUpBlock(StationName, NextStationName);
							boolean allowDownBlock = testAllowedDownBlock(StationName, NextStationName);
							boolean allowCommonBlock= testAllowedcommonBlock(StationName, NextStationName);
			
							if(allowUpBlock)
							UpBlockButton.setVisible(true);
							
							if(allowDownBlock)
							DownBlockButton.setVisible(true);
							BlockTypeLabel.setVisible(true);
							
							if(allowCommonBlock)
							CommonBlockButton.setVisible(true);
							//UpDownBlockButton.setVisible(true);
						}
					}
					
					/*CommonBlockButton.setVisible(false);
					UpDownBlockButton.setVisible(false);
					BlockTypeLabel.setVisible(false);*/
				}
				//jpanel.revalidate();
				//jpanel.repaint();

			}
			
			//jpanel.revalidate();
			//jpanel.repaint();
		}

	};
	ActionListener DoneButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			{
				dispose();
			}
		}

	};
	ItemListener BlockTypeItemListener_new = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			resultLabel.setText("");
			boolean UpBlockSelected = UpBlockButton.isSelected();
			boolean CommonBlockSelected = CommonBlockButton.isSelected();
			boolean DownBlockSelected = DownBlockButton.isSelected();

			startkm1Label.setVisible(UpBlockSelected);
			startkm2Label.setVisible(DownBlockSelected);
			endkm1Label.setVisible(UpBlockSelected);
			endkm2Label.setVisible(DownBlockSelected);
			downloopsLabel.setVisible(DownBlockSelected);
			uploopsLabel.setVisible(UpBlockSelected);
			uploops.setVisible(UpBlockSelected);
			downloops.setVisible(DownBlockSelected);
			BlockVelocity1Label.setVisible(UpBlockSelected);
			BlockVelocity2Label.setVisible(DownBlockSelected);
			Submit1Button.setVisible(UpBlockSelected);
			Submit3Button.setVisible(DownBlockSelected);
			//Sneha : commented this to ensure that view all button is visible all the time
			//ViewAllBlockButton.setVisible(DownBlockSelected ||UpBlockSelected|| CommonBlockSelected);
			ViewAllBlockButton.setVisible(true);
			editBlock.setVisible(DownBlockSelected ||UpBlockSelected || CommonBlockSelected);
			DoneButton.setVisible(DownBlockSelected ||UpBlockSelected || CommonBlockSelected);
			Speedrestrictionstartkm1Label.setVisible(UpBlockSelected);
			Speedrestrictionendkm1Label.setVisible(UpBlockSelected);
			Speedrestrictionmaxspeed1Label.setVisible(UpBlockSelected);
			Speedrestrictionstartkm2Label.setVisible(DownBlockSelected);
			Speedrestrictionendkm2Label.setVisible(DownBlockSelected);
			Speedrestrictionmaxspeed2Label.setVisible(DownBlockSelected);
			Speedrestrictionstartkm3Label.setVisible(CommonBlockSelected);
			Speedrestrictionendkm3Label.setVisible(CommonBlockSelected);
			Speedrestrictionmaxspeed3Label.setVisible(CommonBlockSelected);
			commonloops.setVisible(CommonBlockSelected);
			commonloopsLabel.setVisible(CommonBlockSelected);
			endkm3Label.setVisible(CommonBlockSelected);
			startkm3Label.setVisible(CommonBlockSelected);
			BlockVelocity3Label.setVisible(CommonBlockSelected);
			Submit2Button.setVisible(CommonBlockSelected);
			if (!CommonBlockSelected) {
				if (startkm3FieldList.size() > 0) {
					for (int i = 0; i < startkm3FieldList.size(); i++) {
						startkm3Field = startkm3FieldList.get(i);
						endkm3Field = endkm3FieldList.get(i);
						BlockVelocity3Field = BlockVelocity3FieldList.get(i);
						Speedrestrictionstartkm3Field = Speedrestrictionstartkm3FieldList
								.get(i);
						Speedrestrictionendkm3Field = Speedrestrictionendkm3FieldList
								.get(i);
						Speedrestrictionmaxspeed3Field = Speedrestrictionmaxspeed3FieldList
								.get(i);

						jpanel.remove(startkm3Field);
						jpanel.remove(endkm3Field);
						jpanel.remove(BlockVelocity3Field);
						jpanel.remove(Speedrestrictionstartkm3Field);
						jpanel.remove(Speedrestrictionendkm3Field);
						jpanel.remove(Speedrestrictionmaxspeed3Field);
						jpanel.revalidate();
						jpanel.repaint();

					}
					Speedrestrictionstartkm3FieldList.clear();
					Speedrestrictionendkm3FieldList.clear();
					Speedrestrictionmaxspeed3FieldList.clear();
					startkm3FieldList.clear();
					endkm3FieldList.clear();
					BlockVelocity3FieldList.clear();
					CommonBlockIdList.clear();
				}
				if (DownBlockSelected) {

					downloops.setSelectedIndex(0);
				}
				if(UpBlockSelected)
				{
					uploops.setSelectedIndex(0);	
				}
			}
			if (!(DownBlockSelected|| UpBlockSelected)) {
				if(!UpBlockSelected){
				if (startkm1FieldList.size() > 0)

				{
					for (int i = 0; i < startkm1FieldList.size(); i++) {
						startkm1Field = startkm1FieldList.get(i);
						
						endkm1Field = endkm1FieldList.get(i);
						BlockVelocity1Field = BlockVelocity1FieldList.get(i);
						Speedrestrictionstartkm1Field = Speedrestrictionstartkm1FieldList
								.get(i);
						Speedrestrictionendkm1Field = Speedrestrictionendkm1FieldList
								.get(i);
						Speedrestrictionmaxspeed1Field = Speedrestrictionmaxspeed1FieldList
								.get(i);

						jpanel.remove(startkm1Field);
						jpanel.remove(endkm1Field);
						jpanel.remove(BlockVelocity1Field);
						jpanel.remove(Speedrestrictionstartkm1Field);
						jpanel.remove(Speedrestrictionendkm1Field);
						jpanel.remove(Speedrestrictionmaxspeed1Field);
						jpanel.revalidate();
						jpanel.repaint();

					}
					startkm1FieldList.clear();
					endkm1FieldList.clear();
					BlockVelocity1FieldList.clear();
					Speedrestrictionstartkm1FieldList.clear();
					Speedrestrictionendkm1FieldList.clear();
					Speedrestrictionmaxspeed1FieldList.clear();
					UpBlockIdList.clear();

				}
				if (CommonBlockSelected) {

					commonloops.setSelectedIndex(0);
				}
				if(UpBlockSelected)
				{
					uploops.setSelectedIndex(0);	
				}
				}

				if(!DownBlockSelected){
				if (startkm2FieldList.size() > 0) {
					for (int i = 0; i < startkm2FieldList.size(); i++) {
						startkm2Field = startkm2FieldList.get(i);
						endkm2Field = endkm2FieldList.get(i);
						BlockVelocity2Field = BlockVelocity2FieldList.get(i);

						Speedrestrictionstartkm2Field = Speedrestrictionstartkm2FieldList
								.get(i);
						Speedrestrictionendkm2Field = Speedrestrictionendkm2FieldList
								.get(i);
						Speedrestrictionmaxspeed2Field = Speedrestrictionmaxspeed2FieldList
								.get(i);

						jpanel.remove(Speedrestrictionstartkm2Field);
						jpanel.remove(Speedrestrictionendkm2Field);
						jpanel.remove(Speedrestrictionmaxspeed2Field);

						jpanel.remove(startkm2Field);
						jpanel.remove(endkm2Field);
						jpanel.remove(BlockVelocity2Field);
						jpanel.revalidate();
						jpanel.repaint();

					}
					startkm2FieldList.clear();
					endkm2FieldList.clear();
					BlockVelocity2FieldList.clear();
					DownBlockIdList.clear();
					Speedrestrictionstartkm2FieldList.clear();
					Speedrestrictionendkm2FieldList.clear();
					Speedrestrictionmaxspeed2FieldList.clear();

				}
				}
				
				if (CommonBlockSelected) {
					commonloops.setSelectedIndex(0);
					
				}
				if(UpBlockSelected)
				{
					uploops.setSelectedIndex(0);	
				}
			}
		}
	};

/*	ItemListener BlockTypeItemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			resultLabel.setText("");
			boolean UpDownBlockSelected = UpDownBlockButton.isSelected();
			boolean CommonBlockSelected = CommonBlockButton.isSelected();

			startkm1Label.setVisible(UpDownBlockSelected);
			startkm2Label.setVisible(UpDownBlockSelected);
			endkm1Label.setVisible(UpDownBlockSelected);
			endkm2Label.setVisible(UpDownBlockSelected);
			downloopsLabel.setVisible(UpDownBlockSelected);
			uploopsLabel.setVisible(UpDownBlockSelected);
			uploops.setVisible(UpDownBlockSelected);
			downloops.setVisible(UpDownBlockSelected);
			BlockVelocity1Label.setVisible(UpDownBlockSelected);
			BlockVelocity2Label.setVisible(UpDownBlockSelected);
			Submit1Button.setVisible(UpDownBlockSelected);
			ViewAllBlockButton.setVisible(UpDownBlockSelected
					|| CommonBlockSelected);
			editBlock.setVisible(UpDownBlockSelected
					|| CommonBlockSelected);
			DoneButton.setVisible(UpDownBlockSelected || CommonBlockSelected);
			Speedrestrictionstartkm1Label.setVisible(UpDownBlockSelected);
			Speedrestrictionendkm1Label.setVisible(UpDownBlockSelected);
			Speedrestrictionmaxspeed1Label.setVisible(UpDownBlockSelected);
			Speedrestrictionstartkm2Label.setVisible(UpDownBlockSelected);
			Speedrestrictionendkm2Label.setVisible(UpDownBlockSelected);
			Speedrestrictionmaxspeed2Label.setVisible(UpDownBlockSelected);
			Speedrestrictionstartkm3Label.setVisible(CommonBlockSelected);
			Speedrestrictionendkm3Label.setVisible(CommonBlockSelected);
			Speedrestrictionmaxspeed3Label.setVisible(CommonBlockSelected);
			commonloops.setVisible(CommonBlockSelected);
			commonloopsLabel.setVisible(CommonBlockSelected);
			endkm3Label.setVisible(CommonBlockSelected);
			startkm3Label.setVisible(CommonBlockSelected);
			BlockVelocity3Label.setVisible(CommonBlockSelected);
			Submit2Button.setVisible(CommonBlockSelected);
			if (!CommonBlockSelected) {
				if (startkm3FieldList.size() > 0) {
					for (int i = 0; i < startkm3FieldList.size(); i++) {
						startkm3Field = startkm3FieldList.get(i);
						endkm3Field = endkm3FieldList.get(i);
						BlockVelocity3Field = BlockVelocity3FieldList.get(i);
						Speedrestrictionstartkm3Field = Speedrestrictionstartkm3FieldList
								.get(i);
						Speedrestrictionendkm3Field = Speedrestrictionendkm3FieldList
								.get(i);
						Speedrestrictionmaxspeed3Field = Speedrestrictionmaxspeed3FieldList
								.get(i);

						jpanel.remove(startkm3Field);
						jpanel.remove(endkm3Field);
						jpanel.remove(BlockVelocity3Field);
						jpanel.remove(Speedrestrictionstartkm3Field);
						jpanel.remove(Speedrestrictionendkm3Field);
						jpanel.remove(Speedrestrictionmaxspeed3Field);
						jpanel.revalidate();
						jpanel.repaint();

					}
					Speedrestrictionstartkm3FieldList.clear();
					Speedrestrictionendkm3FieldList.clear();
					Speedrestrictionmaxspeed3FieldList.clear();
					startkm3FieldList.clear();
					endkm3FieldList.clear();
					BlockVelocity3FieldList.clear();
					CommonBlockIdList.clear();
				}
				if (UpDownBlockSelected) {
					uploops.setSelectedIndex(0);
					downloops.setSelectedIndex(0);
				}
			}
			if (!UpDownBlockSelected) {
				if (startkm1FieldList.size() > 0)

				{
					for (int i = 0; i < startkm1FieldList.size(); i++) {
						startkm1Field = startkm1FieldList.get(i);
						endkm1Field = endkm1FieldList.get(i);
						BlockVelocity1Field = BlockVelocity1FieldList.get(i);
						Speedrestrictionstartkm1Field = Speedrestrictionstartkm1FieldList
								.get(i);
						Speedrestrictionendkm1Field = Speedrestrictionendkm1FieldList
								.get(i);
						Speedrestrictionmaxspeed1Field = Speedrestrictionmaxspeed1FieldList
								.get(i);

						jpanel.remove(startkm1Field);
						jpanel.remove(endkm1Field);
						jpanel.remove(BlockVelocity1Field);
						jpanel.remove(Speedrestrictionstartkm1Field);
						jpanel.remove(Speedrestrictionendkm1Field);
						jpanel.remove(Speedrestrictionmaxspeed1Field);
						jpanel.revalidate();
						jpanel.repaint();

					}
					startkm1FieldList.clear();
					endkm1FieldList.clear();
					BlockVelocity1FieldList.clear();
					Speedrestrictionstartkm1FieldList.clear();
					Speedrestrictionendkm1FieldList.clear();
					Speedrestrictionmaxspeed1FieldList.clear();
					UpBlockIdList.clear();

				}

				if (startkm2FieldList.size() > 0) {
					for (int i = 0; i < startkm2FieldList.size(); i++) {
						startkm2Field = startkm2FieldList.get(i);
						endkm2Field = endkm2FieldList.get(i);
						BlockVelocity2Field = BlockVelocity2FieldList.get(i);

						Speedrestrictionstartkm2Field = Speedrestrictionstartkm2FieldList
								.get(i);
						Speedrestrictionendkm2Field = Speedrestrictionendkm2FieldList
								.get(i);
						Speedrestrictionmaxspeed2Field = Speedrestrictionmaxspeed2FieldList
								.get(i);

						jpanel.remove(Speedrestrictionstartkm2Field);
						jpanel.remove(Speedrestrictionendkm2Field);
						jpanel.remove(Speedrestrictionmaxspeed2Field);

						jpanel.remove(startkm2Field);
						jpanel.remove(endkm2Field);
						jpanel.remove(BlockVelocity2Field);
						jpanel.revalidate();
						jpanel.repaint();

					}
					startkm2FieldList.clear();
					endkm2FieldList.clear();
					BlockVelocity2FieldList.clear();
					DownBlockIdList.clear();
					Speedrestrictionstartkm2FieldList.clear();
					Speedrestrictionendkm2FieldList.clear();
					Speedrestrictionmaxspeed2FieldList.clear();

				}
				if (CommonBlockSelected) {
					commonloops.setSelectedIndex(0);
					
				}
			}
		}
	};
*/
		
	ActionListener Submit1ButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			resultLabel.setText("");
			int count = 0;
			SectionInputDialog sectioninputdialog1 = GlobalVar
					.getSectionInputDialog();
			ArrayList<Block> blockList = sectioninputdialog1.blockList;
			for (Block block : blockList) {
				String blockId = String.format("%07d", block.getBlockNo());
				int blockstationId = Integer.parseInt(blockId.substring(0, 3)) - 1;
				if (blockstationId == stationno.getSelectedIndex()) {
					int blockType = Integer.parseInt(blockId.substring(3, 5));
					if(blockType==0x0){//check for existing up block
					count++;
					}
				}
			}
			if (count == 0)

			{
				try {

					for (int i = 0; i < startkm1FieldList.size(); i++) {
						SectionInputDialog sectioninputdialog = GlobalVar
								.getSectionInputDialog();

						Block block = sectioninputdialog
								.getBlockByBlockNo(UpBlockIdList.get(i));

						block = new Block();

						block.setStartMilePost(Double
								.parseDouble(startkm1FieldList.get(i).getText()));
						block.setEndMilePost(Double.parseDouble(endkm1FieldList
								.get(i).getText()));

						int blockNo = UpBlockIdList.get(i);
						block.setBlockNo(blockNo);
						block.setMaximumPossibleSpeed(Double
								.parseDouble(BlockVelocity1FieldList.get(i)
										.getText()));
						//sneha: some how block.setDirection() is not setting up direction value properly.
						//check implementation. As of now set direction by assigning value directly
						//block.setDirection(GlobalVar.UP_DIRECTION);
						block.direction=GlobalVar.UP_DIRECTION;

						if (!Speedrestrictionstartkm1FieldList.get(i).getText()
								.isEmpty()
								&& !Speedrestrictionendkm1FieldList.get(i)
										.getText().isEmpty()
								&& !Speedrestrictionmaxspeed1FieldList.get(i)
										.getText().isEmpty()) {
							double speedLimit = Double
									.parseDouble(Speedrestrictionmaxspeed1FieldList
											.get(i).getText());
							double startKm = Double
									.parseDouble(Speedrestrictionstartkm1FieldList
											.get(i).getText());
							double endKm = Double
									.parseDouble(Speedrestrictionendkm1FieldList
											.get(i).getText());

							SpeedRestriction speedRestrictionFormat = new SpeedRestriction(
									speedLimit, startKm, endKm);
							block.speedRestrictionList.add(speedRestrictionFormat);
						}

						if (i == startkm1FieldList.size() - 1 || i == 0) {
							String blockId = String.format("%07d", blockNo);
							int blockstationId = Integer.parseInt(blockId
									.substring(0, 3));

							if (i == 0) {
								for (Loop loop : sectioninputdialog.loopList) {
									int loopno = loop.getBlockNo();
									String loopId = String.format("%07d",
											loopno);
									int loopstationId = Integer.parseInt(loopId
											.substring(0, 3));
									if (loopstationId == blockstationId) {

										int loopIdcheck = Integer
												.parseInt(loopId
														.substring(3, 4));

										if (loopIdcheck != 1) {

											Link link = new Link();
											link.setNextBlockNo(blockNo);
											link.setPriority(1);

											int loopcrossovercheck = Integer
													.parseInt(loopId.substring(
															3, 5));

											if (loopcrossovercheck == 23) {
												link.crossoverIds
														.add(UpBlockIdList
																.get(i));

											}
											loop.upLinks.add(link);
										}
									}
								}
							}
							if (i == startkm1FieldList.size() - 1) {
								for (Loop loop : sectioninputdialog.loopList) {
									int loopno = loop.getBlockNo();
									String loopId = String.format("%07d",
											loopno);
									int loopstationId = Integer.parseInt(loopId
											.substring(0, 3));

									if (loopstationId == blockstationId + 1)

									{
										int loopIdcheck = Integer
												.parseInt(loopId
														.substring(3, 4));
										if (loopIdcheck != 1) {

											Link link = new Link();
											link.setNextBlockNo(loopno);
											int loopPriorityCheck = Integer
													.parseInt(loopId.substring(
															3, 5));
											if (loopPriorityCheck == 00	|| loopPriorityCheck == 20)
												link.setPriority(1);
											else if (loopPriorityCheck == 01)
												link.setPriority(2);
											else if (loopPriorityCheck == 22)
												link.setPriority(3);
											else if (loopPriorityCheck == 21)
												link.setPriority(4);
											else if (loopPriorityCheck == 23)
												link.setPriority(5);

											int loopcrossovercheck = Integer
													.parseInt(loopId.substring(
															3, 5));

											if (loopcrossovercheck == 23)

											{

												link.crossoverIds
														.add(UpBlockIdList
																.get(i));

											}
											block.upLinks.add(link);

										}

									}
								}
							}
						} else {
							String blockId = String.format("%07d", blockNo);
							int currno = Integer.parseInt(blockId.substring(4,
									6));
							String currstationId = blockId.substring(0, 3);
							String nextblockid = String.format("%02d",
									(currno + 1));
							int nextblockNo = Integer.parseInt(currstationId
									+ "0" + nextblockid + "0");

							Link link = new Link();
							link.setNextBlockNo(nextblockNo);
							link.setPriority(1);
							block.upLinks.add(link);

						}
						if (startkm1FieldList.size() > 1) {
							if (i == 0) {
								String blockId = String.format("%07d", blockNo);
								int currno = Integer.parseInt(blockId
										.substring(4, 6));
								String currstationId = blockId.substring(0, 3);
								String nextblockid = String.format("%02d",
										(currno + 1));
								int nextblockNo = Integer
										.parseInt(currstationId + "0"
												+ nextblockid + "0");

								Link link = new Link();
								link.setNextBlockNo(nextblockNo);
								link.setPriority(1);
								block.upLinks.add(link);

							}
						}

						sectioninputdialog.addBlock(block);
						resultLabel.setText("Added blocks between selected stations");

					}



				} catch (Exception ex) {
					resultLabel.setText("Check all fields");
				}
			}

	}
	};

	ActionListener Submit2ButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultLabel.setText("");
			int count = 0;
			SectionInputDialog sectioninputdialog2 = GlobalVar
					.getSectionInputDialog();
			ArrayList<Block> blockList = sectioninputdialog2.blockList;
			for (Block block : blockList) {
				String blockId = String.format("%07d", block.blockNo);
				int blockstationId = Integer.parseInt(blockId.substring(0, 3)) - 1;
				if (blockstationId == stationno.getSelectedIndex()) {
					int blockType = Integer.parseInt(blockId.substring(3, 5));
					if(blockType==20){//check for existing common block
					count++;
					}
				}
			}
			if (count == 0)

			{
				try {
					for (int i = 0; i < startkm3FieldList.size(); i++) {
						SectionInputDialog sectioninputdialog = GlobalVar
								.getSectionInputDialog();

						Block block = sectioninputdialog
								.getBlockByBlockNo(CommonBlockIdList.get(i));

						block = new Block();

						block.startMilePost = Double
								.parseDouble(startkm3FieldList.get(i).getText());
						block.endMilePost = Double.parseDouble(endkm3FieldList
								.get(i).getText());

						int blockNo = CommonBlockIdList.get(i);
						block.blockNo = blockNo;
						block.maximumPossibleSpeed = Double
								.parseDouble(BlockVelocity3FieldList.get(i)
										.getText());
						//sneha: some how block.setDirection was not setting up direction value properly.
						//check implementation.As of now set direction by assigning value directly
						block.direction = GlobalVar.COMMON_DIRECTION;
						if (!Speedrestrictionstartkm3FieldList.get(i).getText()
								.isEmpty()
								&& !Speedrestrictionendkm3FieldList.get(i)
										.getText().isEmpty()
								&& !Speedrestrictionmaxspeed3FieldList.get(i)
										.getText().isEmpty())

						{
							double speedLimit = Double
									.parseDouble(Speedrestrictionmaxspeed3FieldList
											.get(i).getText());
							double startKm = Double
									.parseDouble(Speedrestrictionstartkm3FieldList
											.get(i).getText());
							double endKm = Double
									.parseDouble(Speedrestrictionendkm3FieldList
											.get(i).getText());
							SpeedRestriction speedRestrictionFormat = new SpeedRestriction(
									speedLimit, startKm, endKm);
							block.getSpeedRestrictionList().add(speedRestrictionFormat);

						}
						if (i == 0 || i == startkm3FieldList.size() - 1) {
							String blockId = String.format("%07d", blockNo);
							int blockstationId = Integer.parseInt(blockId
									.substring(0, 3));

							if (i == 0) {
								for (Loop loop : sectioninputdialog.loopList) {
									int loopno = loop.getBlockNo();
									
									String loopId = String.format("%07d",
											loopno);

									int loopstationId = Integer.parseInt(loopId
											.substring(0, 3));

									if (loopstationId == blockstationId) {

										int loopIdcheck = Integer
												.parseInt(loopId
														.substring(3, 4));

										if (loopIdcheck != 1) {

											Link link = new Link();
											link.setNextBlockNo(blockNo);
											link.setPriority(1);

											loop.upLinks.add(link);
										}

										if (loopIdcheck != 0) {

											Link link = new Link();
											link.nextBlockNo = loopno;
											int loopPriorityCheck = Integer
													.parseInt(loopId.substring(
															3, 5));
											if (loopPriorityCheck == 10
													|| loopPriorityCheck == 20)
												link.priority = 1;
											else if (loopPriorityCheck == 11)
												link.priority = 2;
											else if (loopPriorityCheck == 23)
												link.priority = 3;
											else if (loopPriorityCheck == 21)
												link.priority = 4;
											else if (loopPriorityCheck == 22)
												link.priority = 5;

											block.downLinks.add(link);

										}

									}
								}
							}
							if (i == startkm3FieldList.size() - 1) {
								for (Loop loop : sectioninputdialog.loopList) {
									int loopno = loop.blockNo;
									String loopId = String.format("%07d",
											loopno);
									int loopstationId = Integer.parseInt(loopId
											.substring(0, 3));

									if (loopstationId == blockstationId + 1)

									{
										int loopIdcheck = Integer
												.parseInt(loopId
														.substring(3, 4));

										if (loopIdcheck != 0) {

											Link link = new Link();
											link.nextBlockNo = blockNo;
											link.priority = 1;

											loop.downLinks.add(link);
										}

										if (loopIdcheck != 1) {

											Link link = new Link();
											link.nextBlockNo = loopno;
											int loopPriorityCheck = Integer
													.parseInt(loopId.substring(
															3, 5));
											if (loopPriorityCheck == 00
													|| loopPriorityCheck == 20)
												link.priority = 1;
											else if (loopPriorityCheck == 01)
												link.priority = 2;
											else if (loopPriorityCheck == 22)
												link.priority = 3;
											else if (loopPriorityCheck == 21)
												link.priority = 4;
											else if (loopPriorityCheck == 23)
												link.priority = 5;

											block.upLinks.add(link);

										}

									}

								}
							}
						} else {
							String blockId = String.format("%07d", blockNo);
							int currno = Integer.parseInt(blockId.substring(4,
									6));
							String currstationId = blockId.substring(0, 3);
							String nextblockid = String.format("%02d",
									(currno + 1));
							String prevblockid = String.format("%02d",
									(currno - 1));
							int nextblockNo = Integer.parseInt(currstationId
									+ "2" + nextblockid + "0");
							int prevblockNo = Integer.parseInt(currstationId
									+ "2" + prevblockid + "0");
							Link linkup = new Link();
							linkup.nextBlockNo = nextblockNo;
							linkup.priority = 1;
							block.upLinks.add(linkup);

							Link linkdown = new Link();
							linkdown.nextBlockNo = prevblockNo;
							linkdown.priority = 1;
							block.downLinks.add(linkdown);
						}
						if (startkm3FieldList.size() > 1) {

							if (i == 0) {
								String blockId = String.format("%07d", blockNo);
								int currno = Integer.parseInt(blockId
										.substring(4, 6));
								String currstationId = blockId.substring(0, 3);

								String nextblockid = String.format("%02d",
										(currno + 1));
								int nextblockNo = Integer
										.parseInt(currstationId + "2"
												+ nextblockid + "0");
								Link linkup = new Link();
								linkup.nextBlockNo = nextblockNo;
								linkup.priority = 1;
								block.upLinks.add(linkup);
							}

							else if (i == startkm3FieldList.size() - 1) {
								String blockId = String.format("%07d", blockNo);
								int currno = Integer.parseInt(blockId
										.substring(4, 6));
								String currstationId = blockId.substring(0,3);

								String prevblockid = String.format("%02d",
										(currno - 1));
								int prevblockNo = Integer
										.parseInt(currstationId + "2"
												+ prevblockid + "0");

								Link linkdown = new Link();
								linkdown.nextBlockNo = prevblockNo;
								linkdown.priority = 1;
								block.downLinks.add(linkdown);
							}
						}
						sectioninputdialog.addBlock(block);
						resultLabel.setText("Added blocks between selected stations");
						
					}
				} catch (Exception ex) {
					resultLabel.setText("Check all fields");
				}

			}
		}
	};

	ActionListener Submit3ButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultLabel.setText("");
			int count = 0;
			SectionInputDialog sectioninputdialog1 = GlobalVar
					.getSectionInputDialog();
			ArrayList<Block> blockList = sectioninputdialog1.blockList;
			for (Block block : blockList) {
				String blockId = String.format("%07d", block.getBlockNo());
				int blockstationId = Integer.parseInt(blockId.substring(0, 3)) - 1;
				if (blockstationId == stationno.getSelectedIndex()) {
					int blockType = Integer.parseInt(blockId.substring(3, 5));
					if(blockType==10){//check for existing down block
					count++;
					}

				}
			}
			if (count == 0)

			{
				try {

					for (int i = 0; i < startkm2FieldList.size(); i++) {
						SectionInputDialog sectioninputdialog = GlobalVar
								.getSectionInputDialog();

						Block block = sectioninputdialog
								.getBlockByBlockNo(DownBlockIdList.get(i));
						block = new Block();

						block.setStartMilePost(Double
								.parseDouble(startkm2FieldList.get(i).getText()));
						block.setEndMilePost(Double.parseDouble(endkm2FieldList
								.get(i).getText()));
						int blockNo = DownBlockIdList.get(i);
						block.setBlockNo(blockNo);
						block.setMaximumPossibleSpeed(Double
								.parseDouble(BlockVelocity2FieldList.get(i)
										.getText()));
						//Sneha: Some how setDirection() method is not working properly so as of now set direction by assigning 
						//value directly.Check implementation of setDirection() method 
						//block.setDirection(GlobalVar.DOWN_DIRECTION);
						block.direction=GlobalVar.DOWN_DIRECTION;

						if (!Speedrestrictionstartkm2FieldList.get(i).getText()
								.isEmpty()
								&& !Speedrestrictionendkm2FieldList.get(i)
										.getText().isEmpty()
								&& !Speedrestrictionmaxspeed2FieldList.get(i)
										.getText().isEmpty()) {
							double speedLimit = Double
									.parseDouble(Speedrestrictionmaxspeed2FieldList
											.get(i).getText());
							double startKm = Double
									.parseDouble(Speedrestrictionstartkm2FieldList
											.get(i).getText());
							double endKm = Double
									.parseDouble(Speedrestrictionendkm2FieldList
											.get(i).getText());
							SpeedRestriction speedRestrictionFormat = new SpeedRestriction(
									speedLimit, startKm, endKm);
							block.getSpeedRestrictionList().add(speedRestrictionFormat);

						}
						if (i == 0 || i == startkm2FieldList.size() - 1) {
							String blockId = String.format("%07d", blockNo);
							int blockstationId = Integer.parseInt(blockId
									.substring(0, 3));

							if (i == startkm2FieldList.size() - 1) {
								for (Loop loop : sectioninputdialog.loopList) {
									int loopno = loop.getBlockNo();
									String loopId = String.format("%07d",
											loopno);
									int loopstationId = Integer.parseInt(loopId
											.substring(0, 3));
									if (loopstationId == blockstationId + 1) {

										int loopIdcheck = Integer
												.parseInt(loopId
														.substring(3, 4));

										if (loopIdcheck != 0) {

											Link link = new Link();
											link.setNextBlockNo(blockNo);
											link.setPriority(1);

											int loopcrossovercheck = Integer
													.parseInt(loopId.substring(
															4, 6));

											if (loopcrossovercheck == 22) {
												link.crossoverIds
														.add(DownBlockIdList
																.get(i));

											}
											loop.downLinks.add(link);
										}
									}
								}
							}
							if (i == 0) {
								for (Loop loop : sectioninputdialog.loopList) {
									int loopno = loop.blockNo;
									String loopId = String.format("%07d",
											loopno);
									int loopstationId = Integer.parseInt(loopId
											.substring(0, 3));

									if (loopstationId == blockstationId)

									{
										int loopIdcheck = Integer
												.parseInt(loopId
														.substring(3, 4));
										if (loopIdcheck != 0) {

											Link link = new Link();
											link.nextBlockNo = loopno;
											int loopPriorityCheck = Integer
													.parseInt(loopId.substring(
															3, 5));
											if (loopPriorityCheck == 10
													|| loopPriorityCheck == 20)
												link.priority = 1;
											else if (loopPriorityCheck == 11)
												link.priority = 2;
											else if (loopPriorityCheck == 23)
												link.priority = 3;
											else if (loopPriorityCheck == 21)
												link.priority = 4;
											else if (loopPriorityCheck == 22)
												link.priority = 5;

											int loopcrossovercheck = Integer
													.parseInt(loopId.substring(
															3, 5));

											if (loopcrossovercheck == 22)

											{

												link.crossoverIds
														.add(DownBlockIdList
																.get(i));

											}
											block.downLinks.add(link);

										}

									}
								}
							}
						} else {
							String blockId = String.format("%07d", blockNo);
							int currno = Integer.parseInt(blockId.substring(4,
									6));
							String currstationId = blockId.substring(0, 3);
							String nextblockid = String.format("%02d",
									(currno - 1));
							int nextblockNo = Integer.parseInt(currstationId
									+ "1" + nextblockid + "0");

							Link link = new Link();
							link.nextBlockNo = nextblockNo;
							link.priority = 1;
							block.downLinks.add(link);

						}
						if (startkm2FieldList.size() > 1) {
							if (i == startkm2FieldList.size() - 1) {
								String blockId = String.format("%07d", blockNo);
								int currno = Integer.parseInt(blockId
										.substring(4, 6));
								String currstationId = blockId.substring(0, 3);
								String nextblockid = String.format("%02d",
										(currno - 1));
								int nextblockNo = Integer
										.parseInt(currstationId + "1"
												+ nextblockid + "0");

								Link link = new Link();
								link.nextBlockNo = nextblockNo;
								link.priority = 1;
								block.downLinks.add(link);

							}
						}

						sectioninputdialog.addBlock(block);
						resultLabel.setText("Added blocks between selected stations");
					}

				} catch (Exception ex) {
					resultLabel.setText("Check all fields");
				}
			}

		}
	};

	ActionListener viewAllBlockActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultLabel.setText("");
			AllBlockData1 allBlockData = new AllBlockData1();
		}
	};

	ActionListener uploopsActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultLabel.setText("");
			String x = String.valueOf(uploops.getSelectedItem());
			int foo = Integer.parseInt(x);

			upblockgenerator(foo, StationName, NextStationName);

		}
	};

	ActionListener downloopsActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultLabel.setText("");
			String x = String.valueOf(downloops.getSelectedItem());
			int foo = Integer.parseInt(x);

			downblockgenerator(foo, StationName, NextStationName);

		}
	};

	ActionListener commonloopsActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			resultLabel.setText("");
			String x = String.valueOf(commonloops.getSelectedItem());
			int foo = Integer.parseInt(x);

			commonblockgenerator(foo, StationName, NextStationName);

		}
	};

	//sneha: added following check to test to check if both left and right station have up loop
    boolean testAllowedUpBlock(String StationName,String NextStationName){
    	boolean uploopLeftStation=false;
    	boolean uploopRightStation=false;
    	boolean allowed=false;
    	


		  SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();  	
		  ArrayList<Loop> temploopList = sectioninputdialog.loopList;		  

		  for (Loop loop : temploopList) {
				if( loop.stationName.equals(StationName) ){
				if(loop.direction==GlobalVar.UP_DIRECTION)uploopLeftStation=true;
				if(loop.direction==GlobalVar.COMMON_DIRECTION)uploopLeftStation=true;
				}
				
				if( loop.stationName.equals(NextStationName )){
					if(loop.direction==GlobalVar.UP_DIRECTION)uploopRightStation=true;
					if(loop.direction==GlobalVar.COMMON_DIRECTION)uploopRightStation=true;}
		  }
		if(uploopLeftStation && uploopRightStation)allowed=true;
			return ( allowed);
	}
    

	//sneha: added following check to test to check if both left and right station have down loop
    boolean testAllowedDownBlock(String StationName,String NextStationName){
    	boolean downloopLeftStation=false;
    	boolean downloopRightStation=false;
    	boolean allowed=false;
    	

		  SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();  	
		  ArrayList<Loop> temploopList = sectioninputdialog.loopList;		  
		
		   	
		  for (Loop loop : temploopList) {
				if( loop.stationName.equals(StationName) ){
				if(loop.direction==GlobalVar.DOWN_DIRECTION)downloopLeftStation=true;
				if(loop.direction==GlobalVar.COMMON_DIRECTION)downloopLeftStation=true;
				}			
				
				if( loop.stationName.equals(NextStationName)){
				if(loop.direction==GlobalVar.DOWN_DIRECTION)downloopRightStation=true;
				if(loop.direction==GlobalVar.COMMON_DIRECTION)downloopRightStation=true;
				}
		  }
		  
		if(downloopLeftStation && downloopRightStation) allowed=true;
    	return ( allowed);
	}
    

	//sneha: added following check to test to check if both left and right station have common loop
    boolean testAllowedcommonBlock(String StationName,String NextStationName){
    	boolean commonloopLeftStation=false;
    	boolean commonloopRightStation=false;
    	boolean allowed=false;
    	

		  SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();  	
		  ArrayList<Loop> temploopList = sectioninputdialog.loopList;		

		//Sneha: check this condition again when should be common block allowed as of now allow common always
		  for (Loop loop : temploopList) {
			if( loop.stationName.equals(StationName) ){
				commonloopLeftStation=true;
				//if(loop.direction==GlobalVar.COMMON_DIRECTION)commonloopLeftStation=true;
				//if((loop.direction==GlobalVar.UP_DIRECTION)&&(loop.direction==GlobalVar.DOWN_DIRECTION))commonloopLeftStation=true;
				}
				
			if( loop.stationName.equals(NextStationName ))	{
				commonloopRightStation=true;
				//if(loop.direction==GlobalVar.COMMON_DIRECTION)commonloopRightStation=true;
				//if((loop.direction==GlobalVar.UP_DIRECTION)&&(loop.direction==GlobalVar.DOWN_DIRECTION))commonloopRightStation=true;
				}
		  }
		if(commonloopLeftStation && commonloopRightStation) allowed=true;
    	return ( allowed);
	}

	private void setBlockLinks(Block block) {

	}


	void upblockgenerator(int x, String stationname, String nextstationname) {
		/**********************************************************/
		   KeyListener keyListener = new KeyListener() {
			      public void keyPressed(KeyEvent keyEvent) {
			        printIt("Pressed", keyEvent);
			      }

			      public void keyReleased(KeyEvent keyEvent) {
			        printIt("Released", keyEvent);
			      }

			      public void keyTyped(KeyEvent keyEvent) {
			        printIt("Typed", keyEvent);
			      }

			      private void printIt(String title, KeyEvent keyEvent) {
			        int keyCode = keyEvent.getKeyCode();
			        
			        //String keyText = KeyEvent.getKeyText(keyCode);
			        //System.out.println(title + " : " + keyText + " / " + keyEvent.getKeyChar());
			        for(int i=0;i<(x-1);i++)
			        	{
			        	
			        	startkm1FieldList.get(i+1).setText(endkm1FieldList.get(i).getText());
			        	}
			        }
			      
			    };
		/**********************************************************/

		SectionInputDialog sectioninputdialog = GlobalVar
				.getSectionInputDialog();
		Station intialstation = sectioninputdialog
				.getStationByStationName(stationname);
		Station finalstation = sectioninputdialog
				.getStationByStationName(nextstationname);
		String endmilepost = String.valueOf(intialstation.endMilePost);
		String startmilepost = String.valueOf(finalstation.startMilePost);
		int count = 1;
		String stationId = null;

		for (Station station : sectioninputdialog.stationList) {

			if (station.stationName == stationname)

				stationId = String.format("%03d", count);
			count++;
		}

		for (int i = 0; i < startkm1FieldList.size(); i++) {
			startkm1Field = startkm1FieldList.get(i);
			endkm1Field = endkm1FieldList.get(i);
			BlockVelocity1Field = BlockVelocity1FieldList.get(i);
			Speedrestrictionstartkm1Field = Speedrestrictionstartkm1FieldList
					.get(i);
			Speedrestrictionendkm1Field = Speedrestrictionendkm1FieldList
					.get(i);
			Speedrestrictionmaxspeed1Field = Speedrestrictionmaxspeed1FieldList
					.get(i);

			jpanel.remove(startkm1Field);
			jpanel.remove(endkm1Field);
			jpanel.remove(BlockVelocity1Field);
			jpanel.remove(Speedrestrictionstartkm1Field);
			jpanel.remove(Speedrestrictionendkm1Field);
			jpanel.remove(Speedrestrictionmaxspeed1Field);
			jpanel.revalidate();
			jpanel.repaint();

		}
		startkm1FieldList.clear();
		endkm1FieldList.clear();
		BlockVelocity1FieldList.clear();
		Speedrestrictionstartkm1FieldList.clear();
		Speedrestrictionendkm1FieldList.clear();
		Speedrestrictionmaxspeed1FieldList.clear();
		UpBlockIdList.clear();

		JTextField[][] upField = new JTextField[6][x];
		int k = x1 * 6 / 4;

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < x; j++) {

				upField[i][j] = new JTextField(null);

				if (i == 0 && j == 0)
					upField[i][j].setText(endmilepost);

				else if (i == 1 && j > x - 2){
					upField[i][j].setText(startmilepost);
					//sneha: added following line
					//upField[i][j].setEnabled(false);
				}
				else if (i == 2)
					upField[i][j].setText("100");

				upField[i][j].setBounds(k, 370 + j * 25, fieldWidth,
						fieldHeight);
				//sneha: added following if block
				/*if((i==0)){
					upField[i][j].setEnabled(false);
					
				}*/
				

				if (i < 1) {
					startkm1FieldList.add(upField[i][j]);
					UpBlockIdList.add(Integer.parseInt(stationId + "0"
							+ String.format("%02d", (j + 1)) + "0"));
				} else if (i < 2) {
					endkm1FieldList.add(upField[i][j]);
					endkm1FieldList.get(j).addKeyListener(keyListener);

				} else if (i < 3) {
					BlockVelocity1FieldList.add(upField[i][j]);
				}

				else if (i < 4) {
					Speedrestrictionstartkm1FieldList.add(upField[i][j]);
				}

				else if (i < 5) {
					Speedrestrictionendkm1FieldList.add(upField[i][j]);

				}

				else {
					Speedrestrictionmaxspeed1FieldList.add(upField[i][j]);

				}

		
				jpanel.add(upField[i][j]);

				jpanel.revalidate();
				jpanel.repaint();
				jpanel.setLayout(null);
			}
			k = k + (35 * x2 / 100);
		} 
		
		

	}


	void commonblockgenerator(int x, String stationname, String nextstationname) {
		
		/**********************************************************/
		   KeyListener keyListener = new KeyListener() {
			      public void keyPressed(KeyEvent keyEvent) {
			        printIt("Pressed", keyEvent);
			      }

			      public void keyReleased(KeyEvent keyEvent) {
			        printIt("Released", keyEvent);
			      }

			      public void keyTyped(KeyEvent keyEvent) {
			        printIt("Typed", keyEvent);
			      }

			      private void printIt(String title, KeyEvent keyEvent) {
			        int keyCode = keyEvent.getKeyCode();
			        
			        //String keyText = KeyEvent.getKeyText(keyCode);
			        //System.out.println(title + " : " + keyText + " / " + keyEvent.getKeyChar());
			        for(int i=0;i<(x-1);i++)
			        	{
			        	
			        	startkm3FieldList.get(i+1).setText(endkm3FieldList.get(i).getText());
			        	}
			        }
			      
			    };
		/**********************************************************/

		SectionInputDialog sectioninputdialog = GlobalVar
				.getSectionInputDialog();
		Station intialstation = sectioninputdialog
				.getStationByStationName(stationname);
		Station finalstation = sectioninputdialog
				.getStationByStationName(nextstationname);
		String endmilepost = String.valueOf(intialstation.endMilePost);
		String startmilepost = String.valueOf(finalstation.startMilePost);
		int count = 1;
		String stationId = null;

		for (Station station : sectioninputdialog.stationList) {

			if (station.stationName == stationname)

				stationId = String.format("%03d", count);
			count++;
		}

		for (int i = 0; i < startkm3FieldList.size(); i++) {
			startkm3Field = startkm3FieldList.get(i);
			endkm3Field = endkm3FieldList.get(i);
			BlockVelocity3Field = BlockVelocity3FieldList.get(i);
			Speedrestrictionstartkm3Field = Speedrestrictionstartkm3FieldList
					.get(i);
			Speedrestrictionendkm3Field = Speedrestrictionendkm3FieldList
					.get(i);
			Speedrestrictionmaxspeed3Field = Speedrestrictionmaxspeed3FieldList
					.get(i);

			jpanel.remove(Speedrestrictionstartkm3Field);
			jpanel.remove(Speedrestrictionendkm3Field);
			jpanel.remove(Speedrestrictionmaxspeed3Field);
			jpanel.remove(startkm3Field);
			jpanel.remove(endkm3Field);
			jpanel.remove(BlockVelocity3Field);
			jpanel.revalidate();
			jpanel.repaint();

		}
		Speedrestrictionstartkm3FieldList.clear();
		Speedrestrictionendkm3FieldList.clear();
		Speedrestrictionmaxspeed3FieldList.clear();

		startkm3FieldList.clear();
		endkm3FieldList.clear();
		BlockVelocity3FieldList.clear();
		CommonBlockIdList.clear();

		JTextField[][] CommonField = new JTextField[6][x];
		int k = (5 * x2) / 4;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < x; j++) {
				CommonField[i][j] = new JTextField(null);

				if (i == 0 && j == 0)
					CommonField[i][j].setText(endmilepost);

				else if (i == 1 && j > x - 2){
					CommonField[i][j].setText(startmilepost);
					//sneha: added following line
					//CommonField[i][j].setEnabled(false);
				}
				

				else if (i == 2)
					CommonField[i][j].setText("100");
				
				//sneha: added following if block
				/*if((i==0)){
					CommonField[i][j].setEnabled(false);
					
				}*/


				CommonField[i][j].setBounds(k, 370 + j * 25, fieldWidth,
						fieldHeight);
				jpanel.add(CommonField[i][j]);



				if (i < 1) {
					startkm3FieldList.add(CommonField[i][j]);
					//sneha: following line was outside if test before test
					//added here to make implementation same as upBlockGenrator and downBlockGenrator
					//string.format(%02d",j) is replaced with string.format(%02d",j+) 
					//as commonblock id was starting from 00 instead of 10
//					CommonBlockIdList.add(Integer.parseInt(stationId + "2"	+ String.format("%02d", j) + "0"));
					CommonBlockIdList.add(Integer.parseInt(stationId + "2"
							+ String.format("%02d", j+1) + "0"));

				} else if (i < 2) {
					endkm3FieldList.add(CommonField[i][j]);
					endkm3FieldList.get(j).addKeyListener(keyListener);
				} else if (i < 3) {
					BlockVelocity3FieldList.add(CommonField[i][j]);
				} else if (i < 4) {
					Speedrestrictionstartkm3FieldList.add(CommonField[i][j]);
				}

				else if (i < 5) {
					Speedrestrictionendkm3FieldList.add(CommonField[i][j]);

				}

				else {
					Speedrestrictionmaxspeed3FieldList.add(CommonField[i][j]);

				}

				jpanel.revalidate();
				jpanel.repaint();
				jpanel.setLayout(null);

			}
			k = k + (35 * x2 / 100);
			;
		}
	}

	void downblockgenerator(int x, String stationname, String nextstationname) {
		
		/**********************************************************/
		   KeyListener keyListener = new KeyListener() {
			      public void keyPressed(KeyEvent keyEvent) {
			        printIt("Pressed", keyEvent);
			      }

			      public void keyReleased(KeyEvent keyEvent) {
			        printIt("Released", keyEvent);
			      }

			      public void keyTyped(KeyEvent keyEvent) {
			        printIt("Typed", keyEvent);
			      }

			      private void printIt(String title, KeyEvent keyEvent) {
			        int keyCode = keyEvent.getKeyCode();
			        
			        //String keyText = KeyEvent.getKeyText(keyCode);
			        //System.out.println(title + " : " + keyText + " / " + keyEvent.getKeyChar());
			        for(int i=0;i<(x-1);i++)
			        	{
			        	
			        	startkm2FieldList.get(i+1).setText(endkm2FieldList.get(i).getText());
			        	}
			        }
			      
			    };
		/**********************************************************/

		SectionInputDialog sectioninputdialog = GlobalVar
				.getSectionInputDialog();
		Station intialstation = sectioninputdialog
				.getStationByStationName(stationname);
		Station finalstation = sectioninputdialog
				.getStationByStationName(nextstationname);
		String endmilepost = String.valueOf(intialstation.endMilePost);
		String startmilepost = String.valueOf(finalstation.startMilePost);
		int count = 1;
		String stationId = null;
		for (Station station : sectioninputdialog.stationList) {

			if (station.stationName == stationname)

				stationId = String.format("%03d", count);
			count++;
		}

		for (int i = 0; i < startkm2FieldList.size(); i++) {
			startkm2Field = startkm2FieldList.get(i);
			endkm2Field = endkm2FieldList.get(i);
			BlockVelocity2Field = BlockVelocity2FieldList.get(i);

			jpanel.remove(startkm2Field);
			jpanel.remove(endkm2Field);
			jpanel.remove(BlockVelocity2Field);
			Speedrestrictionstartkm2Field = Speedrestrictionstartkm2FieldList
					.get(i);
			Speedrestrictionendkm2Field = Speedrestrictionendkm2FieldList
					.get(i);
			Speedrestrictionmaxspeed2Field = Speedrestrictionmaxspeed2FieldList
					.get(i);

			jpanel.remove(Speedrestrictionstartkm2Field);
			jpanel.remove(Speedrestrictionendkm2Field);
			jpanel.remove(Speedrestrictionmaxspeed2Field);

			jpanel.revalidate();
			jpanel.repaint();

		}
		startkm2FieldList.clear();
		endkm2FieldList.clear();
		BlockVelocity2FieldList.clear();
		DownBlockIdList.clear();

		Speedrestrictionstartkm2FieldList.clear();
		Speedrestrictionendkm2FieldList.clear();
		Speedrestrictionmaxspeed2FieldList.clear();

		JTextField[][] downField = new JTextField[6][x];
		int k = 3 * x1 / 2 + 5 * x2 / 2;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < x; j++) {
				downField[i][j] = new JTextField(null);
				downField[i][j].setBounds(k, 370 + j * 25, fieldWidth,
						fieldHeight);

				if (i == 0 && j == 0)
					downField[i][j].setText(endmilepost);

				else if (i == 1 && j > x - 2){
					downField[i][j].setText(startmilepost);
					//sneha: added following line
					//downField[i][j].setEnabled(false);
				}
				else if (i == 2)
					downField[i][j].setText("100");
				
				//sneha: added following if block
				/*if((i==0)){
					downField[i][j].setEnabled(false);
					
				}*/


				downField[i][j].setBounds(k, 370 + j * 25, fieldWidth,
						fieldHeight);

				jpanel.add(downField[i][j]);

				if (i < 1) {
					startkm2FieldList.add(downField[i][j]);

					DownBlockIdList.add(Integer.parseInt(stationId + "1"
							+ String.format("%02d", (j + 1)) + "0"));

				} else if (i < 2) {
					endkm2FieldList.add(downField[i][j]);
					endkm2FieldList.get(j).addKeyListener(keyListener);
				} else if (i < 3) {
					BlockVelocity2FieldList.add(downField[i][j]);
				} else if (i < 4) {
					Speedrestrictionstartkm2FieldList.add(downField[i][j]);
				}

				else if (i < 5) {
					Speedrestrictionendkm2FieldList.add(downField[i][j]);

				}

				else {
					Speedrestrictionmaxspeed2FieldList.add(downField[i][j]);

				}

				jpanel.revalidate();
				jpanel.repaint();
				jpanel.setLayout(null);

			}
			k = k + 35 * x2 / 100;
		}
	}

	public static void main(String args[]) {
		BlockInput blockInput = GlobalVar.getBlockInput();
	}

	public void writeBlocks(ArrayList<Block> blockList) throws IOException {
		String blockFileName = FileNames.getBlockFileName();
		BufferedWriter bw = new BufferedWriter(new FileWriter(blockFileName));
		String formatString = "/*Block Number	Direction	Starting Mile Post	Ending Mile Post	Block Velocity (kmph)	Uplinks	Up Link Length	Priority	Cross-over	Down link	Down Link Length	Priority	Cross-over	Speed Restriction	Starting Mile Post	Ending Mile Post*/\n\n";
		bw.write(formatString);

		for (Block block : blockList) {
			bw.write(String.valueOf(block.blockNo));
			bw.write(" ");

			int blockDirection = block.direction;
			String directionString = GlobalVar
					.getDirectionStringFromDirection(blockDirection);
			bw.write(directionString);

			bw.write(" ");
			bw.write(String.valueOf(block.startMilePost));
			bw.write(" ");
			bw.write(String.valueOf(block.endMilePost));
			bw.write(" ");

			writeCommonBlockLoopAttributes(bw, block);

			bw.write("\n");
		}
		bw.close();
	}

	public void writeLoops(ArrayList<Loop> loopList) throws IOException {
		String loopFileName = FileNames.getLoopFileName();
		BufferedWriter bw = new BufferedWriter(new FileWriter(loopFileName));
		String formatString = "/*Loop Number	Direction	Type 	Station Code Type-of-Trains-allowed	Loop Velocity	Uplinks	Uplink lengths	Priority	Cross-over	Down link	Downlink Lengths	Priority	Cross-over	Speed Restriction	SMP	EMP*/\n\n";
		bw.write(formatString);
		for (Loop loop : loopList) {
			bw.write(String.valueOf(loop.blockNo));
			bw.write(" ");

			int direction = loop.direction;
			String directionString = GlobalVar
					.getDirectionStringFromDirection(direction);
			bw.write(directionString);
			bw.write(" ");

			String loopType = "ml";
			if (loop.whetherMainLine == 1)
				loopType = "loop";
			bw.write(loopType);
			bw.write(" ");

			bw.write("\"" + loop.stationName + "\"");
			bw.write(" ");
			
			bw.write(loop.getLoopTrainType());
			bw.write(" ");

			writeCommonBlockLoopAttributes(bw, loop);

			bw.write("\n");
		}
		bw.close();
	}

	private void writeCommonBlockLoopAttributes(BufferedWriter bw, Block block)
			throws IOException {
		bw.write(" ");
		bw.write(String.valueOf((int) (block.maximumPossibleSpeed)));
		bw.write(" ");

		writeLinkStrings(bw, GlobalVar.UP_DIRECTION, block);
		writeLinkStrings(bw, GlobalVar.DOWN_DIRECTION, block);
		writeSpeedLimitStrings(bw, block);
	}

	private void writeSpeedLimitStrings(BufferedWriter bw, Block block)
			throws IOException {
		bw.write("\"");
		String speedLimitString = block.getSpeedLimitsString();
		bw.write(speedLimitString);
		bw.write("\" ");

		bw.write("\"");
		String speedLimitStartMilePostsString = block
				.getSpeedLimitStartMilePostsString();
		bw.write(speedLimitStartMilePostsString);
		bw.write("\" ");

		bw.write("\"");
		String speedLimitEndMilePostsString = block
				.getSpeedLimitEndMilePostsString();
		bw.write(speedLimitEndMilePostsString);
		bw.write("\" ");
	}

	private void writeLinkStrings(BufferedWriter bw, int direction, Block block)
			throws IOException {
		bw.write("\"");
		String linkString = block.getLinkListString(direction);
		bw.write(linkString);
		bw.write("\" ");

		bw.write("\"");
		String linkLengthString = block.getLinkLengthsString(direction);
		bw.write(linkLengthString);
		bw.write("\" ");

		bw.write("\"");
		String linkPrioritiesString = block.getLinkPrioritiesString(direction);
		bw.write(linkPrioritiesString);
		bw.write("\" ");

		bw.write("\"");
		String linkCrossoversString = block.getLinkCrossoversString(direction);
		bw.write(linkCrossoversString);
		bw.write("\" ");
	}

	public void readBlocks(ArrayList<Block> blockList) throws IOException {
		String blockFileName = FileNames.getBlockFileName();
		StreamTokenizer streamTokenizer = new StreamTokenizer(new FileReader(
				blockFileName));
		streamTokenizer.slashSlashComments(true);
		streamTokenizer.slashStarComments(true);

		while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			Block block = new Block();
			int blockId = (int) streamTokenizer.nval;


			streamTokenizer.nextToken();
			String directionString = streamTokenizer.sval;

			streamTokenizer.nextToken();
			double startKM = streamTokenizer.nval;

			streamTokenizer.nextToken();
			double endKM = streamTokenizer.nval;

			block.blockNo = blockId;
			block.direction = GlobalVar
					.getDirectionFromDirectionString(directionString);
			block.startMilePost = startKM;
			block.endMilePost = endKM;

			block.readCommonBlockLoopProperties(streamTokenizer);
			blockList.add(block);
		}
	}

	public void readLoops(ArrayList<Loop> loopList) throws IOException {
		String loopFileName = FileNames.getLoopFileName();
		StreamTokenizer streamTokenizer = new StreamTokenizer(new FileReader(
				loopFileName));
		streamTokenizer.slashSlashComments(true);
		streamTokenizer.slashStarComments(true);

		while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
			Loop loop = new Loop();
			int blockId = (int) streamTokenizer.nval;

			streamTokenizer.nextToken();
			String directionString = streamTokenizer.sval;

			streamTokenizer.nextToken();
			String loopType = streamTokenizer.sval;

			streamTokenizer.nextToken();
			String stationName = streamTokenizer.sval;
			
			streamTokenizer.nextToken();
			String loopTrainType = streamTokenizer.sval;

			loop.blockNo = blockId;
			loop.direction = GlobalVar
					.getDirectionFromDirectionString(directionString);
			loop.whetherMainLine = Loop.getWhetherMainLineFromType(loopType);
			loop.stationName = stationName;
			loop.setLoopTrainType(loopTrainType);

			loop.readCommonBlockLoopProperties(streamTokenizer);
			loopList.add(loop);
		}
	}

}

class AllBlockData1 extends JFrame implements TableModelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable blockDataTable;

	public AllBlockData1() {
		super("List of blocks");

		blockDataTable = new JTable(new AbstractTableModel() {

			/**
			 * 
			 */
			int i=0;
			private static final long serialVersionUID = 1L;
			String[] columnNames = { "Id", "Direction", "Start Km", "End Km",
					"Maximum Speed", "Up Links", "Priorities", "Crossovers",
					"Down Links", "Priorities", "Crossovers", "Speed Limits",
					"SL - Start Km", "SL - End Km" };
			SectionInputDialog sectioninputdialog = GlobalVar
					.getSectionInputDialog();
			
			ArrayList<Block> blockList = sectioninputdialog.blockList;
			

			
			
			public String getColumnName(int col) {
				return columnNames[col];
			}

			public int getRowCount() {
			
				return blockList.size();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				
				
				switch (col) {
				case 0:
					return blockList.get(row).blockNo;
				case 1:{
					
					//Sneha: Some how getDirection method is not working properly and so
					//using variable direction to assign values.
					//otherwise view results in "direction" "common" for all blocks
					//int direction = blockList.get(row).getDirection();
					int direction = blockList.get(row).direction;
					String directionString = GlobalVar
							.getDirectionStringFromDirection(direction);
					return directionString;
				}
				case 2:
					return blockList.get(row).startMilePost;
				case 3:
					return blockList.get(row).endMilePost;
				case 4:
					return blockList.get(row).maximumPossibleSpeed;
				case 5:
					return blockList.get(row).getLinkListString(
							GlobalVar.UP_DIRECTION);
				case 6:
					return blockList.get(row).getLinkPrioritiesString(
							GlobalVar.UP_DIRECTION);
				case 7:
					return blockList.get(row).getLinkCrossoversString(
							GlobalVar.UP_DIRECTION);
				case 8:
					return blockList.get(row).getLinkListString(
							GlobalVar.DOWN_DIRECTION);
				case 9:
					return blockList.get(row).getLinkPrioritiesString(
							GlobalVar.DOWN_DIRECTION);
				case 10:
					return blockList.get(row).getLinkCrossoversString(
							GlobalVar.DOWN_DIRECTION);
				case 11:
					return blockList.get(row).getSpeedLimitsString();
				case 12:
					return blockList.get(row)
							.getSpeedLimitStartMilePostsString();
				case 13:
					return blockList.get(row).getSpeedLimitEndMilePostsString();

				}
				return null;
			}
		});

		JScrollPane scrollPane = new JScrollPane(blockDataTable);
		blockDataTable
				.setPreferredScrollableViewportSize(new Dimension(500, 70));
		getContentPane().add(scrollPane);
		setBounds(70, 70, 900, 400);
		setVisible(true);
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
	}

	void j() {
	}
	

}



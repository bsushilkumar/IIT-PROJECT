package input;
//shashank
import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Link;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntities.trackProperties.SpeedRestriction;
import gui.entities.sectionEntities.trackProperties.BlockDirectionInInterval;
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
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
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

public  class Maintblock extends InputDialog {

	private static final long serialVersionUID = 1L;
	public JComboBox blocknames = null;
	
	// ArrayList<String> blocknlooplist;
	String [] actualblocknlooplist =null;
String Z;
	public JComboBox maintblocks = null;
	Integer[] maintlist = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	public JLabel blocknumberLabel = new JLabel("Block/Loop number");
	public JLabel blocknumbersLabel = new JLabel("Number of blocks");
	public JLabel starttimeLabel = new JLabel("Start time.");
	public JLabel endtimeLabel = new JLabel("End time");
	public JLabel directionLabel = new JLabel("Allowed direction (up/down/none)");
	public JButton submitButton = new JButton("Submit");
	public JButton doneButton = new JButton("Done");
	DefaultComboBoxModel blocknlooplist = new DefaultComboBoxModel(); 
	public ArrayList<JTextField> starttimeFieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> endtimeFieldList = new ArrayList<JTextField>();
	public ArrayList<JTextField> directionFieldList = new ArrayList<JTextField>();
	

	public JTextField starttimeField = new JTextField("");
	public JTextField endtimeField = new JTextField("");
	public JTextField directionField = new JTextField("none");
	//santhosh
	public JButton editButton=new JButton("Edit");
	public JButton viewButton=new JButton("Edit");
	public boolean editFlag=false;
	public JLabel addMessage=new JLabel("Maintenance block added");
	public Maintblock() {
		x = 100;
		y = 2;
		width = 800;
		height = 600;
		int blockno;
		String stationname;
		int loopno;
		SectionInputDialog sectioninputdialog = GlobalVar
				.getSectionInputDialog();
		
		ArrayList<Block> blocklist = sectioninputdialog.blockList;
		ArrayList<Loop> looplist = sectioninputdialog.loopList;
		// blocknlooplist=new ArrayList<String>();
		for  ( int i =0 ;i < blocklist.size(); i ++ )
		{
		
			blockno = blocklist.get(i).blockNo;
			blocknlooplist.addElement(blockno);
		}
				
		for  ( int i =0 ;i < looplist.size(); i ++ )
		{
			loopno = looplist.get(i).blockNo;
		stationname = looplist.get(i).stationName;
		blocknlooplist.addElement(loopno);
	}
	
		
		
		
		blocknames = new JComboBox(blocknlooplist);
		maintblocks = new JComboBox<Integer>(maintlist);
		
		
		// cards = new JPanel(new CardLayout());

		this.setBounds(x, y, width, height);
		setComponentActionListeners();
		setComponentBounds();
		addComponents();

		this.add(jpanel);
		this.setTitle("Maintenance block details");
		// this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// this.setVisible(true);
	}

	public void addComponents() {

		this.add(blocknames);
		this.add(blocknumberLabel);
		this.add(blocknumbersLabel);
		blocknumbersLabel.setVisible(false);
		this.add(starttimeLabel);
		
		starttimeLabel.setVisible(false);
		this.add(endtimeLabel);
		endtimeLabel.setVisible(false);
		this.add(directionLabel);
		directionLabel.setVisible(false);
		this.add(submitButton);

		this.add(maintblocks);
		maintblocks.setVisible(false);
	 this.add(submitButton);
	 submitButton.setVisible(false);
		this.add(doneButton);
		doneButton.setVisible(false);
		this.add(starttimeField);
		this.add(endtimeField);
		this.add(directionField);
		//santhosh
		this.add(addMessage);
		addMessage.setVisible(false);
	}
	
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
		blocknumberLabel.setBounds(x1 +x2 / 2 , y1, labelWidth, 
				labelHeight);
		blocknames.setBounds(x1 + 2*x2  , y1, labelWidth, 
				labelHeight);

		y1 += 3 * yDifference;
		blocknumbersLabel.setBounds(x1 +x2 / 2 , y1, 3 * labelWidth/2, 
				labelHeight);
		
		maintblocks.setBounds(x1  + 2*x2, y1, 3 * fieldWidth / 2,
				fieldHeight);

		y1 += 2 * yDifference;
		
		starttimeLabel.setBounds(5* x1, y1, 2 * fieldWidth, 2 * fieldHeight);
		
		endtimeLabel.setBounds(5*x1+ x2,
				y1, 2 * fieldWidth, 2 * fieldHeight);
		
		directionLabel.setBounds(5*x1 + 3*x2/2 ,
				y1 +yDifference/2, 6*fieldWidth, fieldHeight);
	
		y1 += 14 * yDifference;
		
		submitButton.setBounds((2*x1 + x2/2) ,
				y1, 3* fieldWidth, fieldHeight);
	
		doneButton.setBounds((2*x1 + 3*x2/2) ,
				y1, 3* fieldWidth, fieldHeight);
		
		y1 += yDifference/4;
		addMessage.setBounds(5* x1, y1, 500, 2 * fieldHeight);
	}
	
	public void setComponentActionListeners() {
		
		blocknames.addActionListener(blocknamesActionListener);
		maintblocks.addActionListener(maintblocksActionListener);
		submitButton.addActionListener(submitButtonActionListener);
		doneButton.addActionListener(doneButtonActionListener);
		
	}
	
	ActionListener blocknamesActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			{		
				addMessage.setVisible(false);
               Z  = String.valueOf(blocknlooplist.getSelectedItem());
               SectionInputDialog sectioninputdialog = GlobalVar
       				.getSectionInputDialog();
               if(sectioninputdialog.blockloopList.contains(Z)){
            	   for (int i = 0; i < starttimeFieldList.size(); i++) {
           			starttimeField = starttimeFieldList.get(i);
           			endtimeField = endtimeFieldList.get(i);
           			directionField = directionFieldList.get(i);

           			jpanel.remove(starttimeField);
           			jpanel.remove(endtimeField);
           			jpanel.remove(directionField);
           			jpanel.revalidate();
           			jpanel.repaint();

           		}
           		starttimeFieldList.clear();
           		endtimeFieldList.clear();
           		directionFieldList.clear();

       			int index=sectioninputdialog.blockloopList.indexOf(Z);
       			String startTimeString=sectioninputdialog.starttimeList.get(index);
       			String[] startTimeArray=startTimeString.split(",");
       			String endTimeString=sectioninputdialog.endtimeList.get(index);
       			String[] endTimeArray=endTimeString.split(",");
       			String directionString=sectioninputdialog.directionList.get(index);
       			String[] directionArray=directionString.split(",");
       			int noOfRows=startTimeArray.length;
       			editFlag=true;
       			maintblocks.setSelectedIndex(noOfRows-1);
       			editFlag=false;
       			JTextField[][] maintblockField = new JTextField[3][noOfRows];
       			int k = 5*x1;

       			for (int i = 0; i < 3; i++) {
       				for (int j = 0; j < noOfRows; j++) {

       					maintblockField[i][j] = new JTextField(startTimeArray[j]);
       					maintblockField[i][j].setBounds(k, 200 + j * 25, fieldWidth,
       							fieldHeight);

       					if (i ==0) {
       						maintblockField[i][j] = new JTextField(startTimeArray[j]);
       						maintblockField[i][j].setBounds(k, 200 + j * 25, fieldWidth,
       								fieldHeight);
       						starttimeFieldList.add(maintblockField[i][j]);

       						
       					} else if (i ==1) {
       						maintblockField[i][j] = new JTextField(endTimeArray[j]);
       						maintblockField[i][j].setBounds(k, 200 + j * 25, fieldWidth,
       								fieldHeight);
       						endtimeFieldList.add(maintblockField[i][j]);

       					} else {
       						maintblockField[i][j] = new JTextField(directionArray[j]);
       						maintblockField[i][j].setBounds(k, 200 + j * 25, fieldWidth,
       								fieldHeight);
       						directionFieldList.add(maintblockField[i][j]);
       					}

       					

       					jpanel.add(maintblockField[i][j]);

       					jpanel.revalidate();
       					jpanel.repaint();
       					jpanel.setLayout(null);
       				}
       				k = k + x2 ;
       			}
       		}else{
       			editFlag=false;
				starttimeLabel.setVisible(true);
				endtimeLabel.setVisible(true);
				directionLabel.setVisible(true);
				blocknumbersLabel.setVisible(true);
			maintblocks.setVisible(true);
			 maintblocks.setSelectedIndex(0);
			 submitButton.setVisible(true);
			 doneButton.setVisible(true);
				jpanel.revalidate();
		jpanel.repaint();
		
       		}
			
			}
		}

	};
	ActionListener maintblocksActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			{
				String x = String.valueOf(maintblocks.getSelectedItem());
				int foo = Integer.parseInt(x);
				addMessage.setVisible(false);
				maintblockgenerator(foo);
		}
		}

	};	
		void maintblockgenerator(int x) {

		SectionInputDialog sectioninputdialog = GlobalVar
				.getSectionInputDialog();
 if(!editFlag){
		for (int i = 0; i < starttimeFieldList.size(); i++) {
			starttimeField = starttimeFieldList.get(i);
			endtimeField = endtimeFieldList.get(i);
			directionField = directionFieldList.get(i);

			jpanel.remove(starttimeField);
			jpanel.remove(endtimeField);
			jpanel.remove(directionField);
			jpanel.revalidate();
			jpanel.repaint();

		}
		starttimeFieldList.clear();
		endtimeFieldList.clear();
		directionFieldList.clear();
	
	//santhosh
		
		
		JTextField[][] maintblockField = new JTextField[6][x];
		int k = 5*x1;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < x; j++) {

				maintblockField[i][j] = new JTextField(null);

			

				maintblockField[i][j].setBounds(k, 200 + j * 25, fieldWidth,
						fieldHeight);

				if (i < 1) {
					starttimeFieldList.add(maintblockField[i][j]);

					
				} else if (i < 2) {
					endtimeFieldList.add(maintblockField[i][j]);

				} else {
					directionFieldList.add(maintblockField[i][j]);
				}

				

				jpanel.add(maintblockField[i][j]);

				jpanel.revalidate();
				jpanel.repaint();
				jpanel.setLayout(null);
			}
			k = k + x2 ;
		}
 }
	}
	
	ActionListener submitButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			{
				SectionInputDialog sectioninputdialog = GlobalVar
						.getSectionInputDialog();
				
				String startTimeListString = "";
				for (int i = 0; i < starttimeFieldList.size(); i++) {
			if (startTimeListString == "")
			{
				startTimeListString = starttimeFieldList.get(i).getText();
			}
			else {	
			startTimeListString = startTimeListString +"," +starttimeFieldList.get(i).getText();	
			}
				}
				
				String endTimeListString = "";
				for (int i = 0; i < endtimeFieldList.size(); i++) {
					if (endTimeListString == "")
					{
						endTimeListString = endtimeFieldList.get(i).getText();
					}
					
					else{
					endTimeListString = endTimeListString +"," +endtimeFieldList.get(i).getText();	
					}
				}
				
				String directionListString = "";
				for (int i = 0; i < directionFieldList.size(); i++) {
					if (directionListString == "")
					{
						directionListString = directionFieldList.get(i).getText();
					}
					else{		
					directionListString = directionListString +"," +directionFieldList.get(i).getText();	
					}
			 
			 
			 
				}
				
				
				if(sectioninputdialog.blockloopList.contains(Z)){
					int index=sectioninputdialog.blockloopList.indexOf(Z);
					sectioninputdialog.blockloopList.remove(index);
					sectioninputdialog.starttimeList.remove(index);
					sectioninputdialog.endtimeList.remove(index);
					sectioninputdialog.directionList.remove(index);
				}
				
				sectioninputdialog.blockloopList.add(Z);
				sectioninputdialog.starttimeList.add(startTimeListString)	;
				sectioninputdialog.endtimeList.add(endTimeListString)	;
				sectioninputdialog.directionList.add(directionListString)	;
				addMessage.setVisible(true);
			
				
				
			}
		}

	};	
	
	
	
	ActionListener doneButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			{
				dispose();
			}
	
		}

	};	


	public void writemaintblocks(ArrayList<String> blockloopList,ArrayList<String> starttimeList, ArrayList<String> endtimeList, ArrayList<String> directiontimeList ) throws IOException {

		String maintblockFileName = FileNames.getBlockDirectionFileName();
		BufferedWriter bw = new BufferedWriter(new FileWriter(maintblockFileName));
		String formatString = "/*Loop/Block Number	Start-time	End-time 	Direction	*/\n\n";
		bw.write(formatString);
		
		for (int i =0; i < blockloopList.size(); i++) {
			
			bw.write(String.valueOf(blockloopList.get(i)));
			bw.write(" ");

			
			bw.write("\""+starttimeList.get(i)+ "\"");
			bw.write(" ");

		
		
			bw.write("\""+endtimeList.get(i)+ "\"");
			bw.write(" ");

			
			bw.write("\""+directiontimeList.get(i)+ "\"");
			bw.write(" ");

	
		
			bw.write("\n");
		}
		
		bw.close();
		
	} 


}



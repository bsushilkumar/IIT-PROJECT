package input;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.trackEntities.Loop;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

public class LoopInputDialog extends InputDialog{

	
	public JLabel loopTypeLabel = new JLabel("Type of Line");
	public JRadioButton mainLineRadioButton = new JRadioButton("Mainline");
	public JRadioButton loopLineRadioButton = new JRadioButton("Loop line");
	public ButtonGroup loopTypeButtonGroup = new ButtonGroup();
	public JLabel directionLabel = new JLabel("Direction");
	public JRadioButton upRadioButton = new JRadioButton("Up");
	public JRadioButton downRadioButton = new JRadioButton("Down");
	public JRadioButton commonupRadioButton = new JRadioButton("Common on up side");
	public JRadioButton commondownRadioButton = new JRadioButton("Common on down side");
	public JRadioButton commonmiddleRadioButton = new JRadioButton("Common in middle");
	public JRadioButton commonRadioButton = new JRadioButton("Common");
	public String Station_ID = null;
	public ButtonGroup directionButtonGroup = new ButtonGroup();
	public JButton addloopbutton = new JButton("Add new loop");
	public JButton Viewallbutton = new JButton("View All");
	public JTextField LoopIdEditDeletefield = new JTextField();
	public JButton editButton = new JButton("Edit");
	public JButton updateButton = new JButton("Update");
	public JButton deleteButton = new JButton("Delete");
	public JButton doneButton = new JButton("Done");
	public JLabel editdeletelabel = new JLabel("Edit/delete");
	public JLabel looptraintypelabel = new JLabel("Type of Trains allowed");
	public JLabel looplengthlabel = new JLabel("Loop Length");
	public JLabel maximumspeedLabel = new JLabel("Maximum Loop Entry Velocity");
	public JTextField maximumspeedField = new JTextField();
	public JComboBox looptraintype = new JComboBox();
	public JComboBox looplength = new JComboBox();
	public JLabel resultLabel = new JLabel("");
	public ArrayList<Loop> loopList = new ArrayList<Loop>();

	
	
	
	private static String [] filename = {"Standard -686m ","Other"};
	private static String [] looptraintypestring = {"All", "Scheduled", "Freight"};
	public int counter00 ;
	public int counter10 ;
	public int counter20 ;
	public int counter01 ;
	public int counter11 ;
	public int counter21 ;
	public int counter22 ;
	public int counter23 ;
	
	Loop loopToBeEdited = null; 
	public LoopInputDialog(String stationName) {
				
		x = 10;
		y = 100;
		width = 600;
		height = 600;
		this.setBounds(x, y, width, height);
		  SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();  	
		  ArrayList<Loop> temploopList = sectioninputdialog.loopList;		  
		  counter00 = 0;
		  counter10 = 0;
		  counter20 = 0;
		  counter01 = 0;
		  counter11 = 0;
		  counter21 = 0;
		  counter22 = 0;
		  counter23 = 0;
			
		  for (Loop loop : temploopList) {
			if( loop.stationName == stationName )
			{
				
				Integer looptest =Integer.parseInt(String.format("%06d",loop.blockNo).substring(2,4));
				
				if (looptest == 
						0)
			counter00++ ;
else if (looptest == 10)
			counter10++ ;
else if (looptest ==20)
	counter20++ ;
else if (looptest ==01)
	counter01++ ;
else if (looptest ==11)
	counter11++ ;
else if (looptest ==21)
	counter21++ ;
else if (looptest ==22)
	counter22++ ;
else if (looptest ==23)
	counter23++ ;
									
				
			}
			



			}
		  
		  
		  int count = 1;
			String stationId=null  ;
		  for (Station station : sectioninputdialog.stationList) {
				
				if( station.stationName == stationName)
					
					stationId = 	 String.format("%02d",count);
				count ++;
		  }
		  
	
		  	
		  	Station_ID = stationId;
	looptraintype = new JComboBox(looptraintypestring);
	looplength = new JComboBox(filename) ; 
		setComponentActionListeners();
		setComponentBounds();
		addComponents();
		 
			
		this.add(jpanel);

		this.setTitle("Loop Input"+ " for "+stationName);
		// this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// this.setVisible(true);

	}
	public LoopInputDialog(String station_Id, int z) {
		
		// TODO Auto-generated constructor stub
		
		switch (z) {
		case 0:
			
				
		case 1:
		
	}
		}
	// 0 is for up direction, 1 for down,2 common
	//also improve the interface for mainloop line options
	// station_id ,x,y make up the block id here
	
	
	ItemListener LooptypeItemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			boolean loopRadioButtonSelected = loopLineRadioButton.isSelected();
			upRadioButton.setSelected(true);
			commonupRadioButton.setVisible(loopRadioButtonSelected);
			if(loopRadioButtonSelected)
			maximumspeedField.setText("15");
			else
				maximumspeedField.setText("100");
			commondownRadioButton.setVisible(loopRadioButtonSelected);
			commonmiddleRadioButton.setVisible(loopRadioButtonSelected);
			commonRadioButton.setVisible(!loopRadioButtonSelected);
					}
	};

	
	int setBlockidforLoop(){
	int blockid = 0;	
	String blockidstring;
		if (upRadioButton.isSelected() && mainLineRadioButton.isSelected()) {
if (counter00 < 1)			
{	counter00 ++ ;
String temp00 = String.format("%02d",counter00); 			
blockidstring = Station_ID+"00"+temp00;
blockid = Integer.parseInt(blockidstring);
}
	
		
		} else if (downRadioButton.isSelected() && mainLineRadioButton.isSelected())
				{

			if (counter10 < 1)			
			{	counter10++ ;
String temp10 = String.format("%02d",counter10); 			
blockidstring = (Station_ID+"10"+temp10);
blockid = Integer.parseInt(blockidstring); 
}
			
		} else if(commonRadioButton.isSelected() && mainLineRadioButton.isSelected()) 
		 {
if(counter20 <1)
			{
				counter20++ ;
String temp20 = String.format("%02d",counter20); 			
blockidstring = (Station_ID+"20"+temp20);
blockid = Integer.parseInt(blockidstring);
		 }
		 
		 
		 }
		else if(upRadioButton.isSelected() && loopLineRadioButton.isSelected()) 
			{

			counter01++ ;
String temp01 = String.format("%02d",counter01); 			
blockidstring = (Station_ID+"01"+temp01);
blockid = Integer.parseInt(blockidstring);

			
			}
		
		else if(downRadioButton.isSelected() && loopLineRadioButton.isSelected()) 
		{

			counter11++ ;
String temp11 = String.format("%02d",counter11); 			
blockidstring = (Station_ID+"11"+temp11);
blockid = Integer.parseInt(blockidstring);

			
		
		
		}
	
		else if(commonmiddleRadioButton.isSelected() && loopLineRadioButton.isSelected())
		{
			

			counter21++ ;
String temp21 = String.format("%02d",counter21); 			
blockidstring = (Station_ID+"21"+temp21);
blockid = Integer.parseInt(blockidstring);

			
		
		}
		
		else if(commonupRadioButton.isSelected() && loopLineRadioButton.isSelected())
		{

			counter22++ ;
String temp22 = String.format("%02d",counter22); 			
blockidstring = (Station_ID+"22"+temp22);
blockid = Integer.parseInt(blockidstring);

		}
		else if(commondownRadioButton.isSelected() && loopLineRadioButton.isSelected())
		{
			counter23++ ;
String temp23 = String.format("%02d",counter23); 			
blockidstring = (Station_ID+"23"+temp23);
blockid = Integer.parseInt(blockidstring);
			
		}
		
		 return blockid;			
	}
	
	
	@Override
	public void addComponents() {
		this.add(resultLabel);

		upRadioButton.setSelected(true);
		this.add(directionLabel);
		this.add(upRadioButton);
		this.add(downRadioButton);
		this.add(commonupRadioButton);
		this.add(commonRadioButton);
		this.add(commondownRadioButton);
		this.add(commonmiddleRadioButton);
		this.add(looptraintype);
		this.add(looptraintypelabel);
        this.add(looplength);
        this.add(looplengthlabel);
		directionButtonGroup.add(upRadioButton);
		directionButtonGroup.add(downRadioButton);
		directionButtonGroup.add(commonupRadioButton);
		directionButtonGroup.add(commondownRadioButton);
		directionButtonGroup.add(commonmiddleRadioButton);
		directionButtonGroup.add(commonRadioButton);
		this.add(loopTypeLabel);
		this.add(loopLineRadioButton);
		this.add(mainLineRadioButton);
		mainLineRadioButton.setSelected(true);
		loopTypeButtonGroup.add(mainLineRadioButton);
		loopTypeButtonGroup.add(loopLineRadioButton);
		
		
		this.add(addloopbutton);
		this.add(editButton);
		this.add(updateButton);
		this.add(deleteButton);
		this.add(addloopbutton);
		this.add(Viewallbutton);
		this.add(editdeletelabel);
		this.add(doneButton);
		this.add(maximumspeedField);
		this.add(LoopIdEditDeletefield);	
this.add(maximumspeedLabel);
	}

	
	



@Override
public void setComponentBounds() {
	int yDifference = 25;
	labelWidth += 40;
	x2 += 40;

	
	
	
	y1 += yDifference;
	loopTypeLabel.setBounds(x1, y1, labelWidth, labelHeight);
	mainLineRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);

	y1 += 20;
	loopLineRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);

	y1 += yDifference;
	directionLabel.setBounds(x1, y1, labelWidth, labelHeight);
	upRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
	y1 += 20;
	downRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
	y1 += 20;
	commonupRadioButton.setBounds(x2, y1, 2*fieldWidth, fieldHeight);
	commonRadioButton.setBounds(x2, y1, 2*fieldWidth, fieldHeight);
	y1 += 20;
	commondownRadioButton.setBounds(x2, y1, 2*fieldWidth, fieldHeight);
	y1 += 20;
	commonmiddleRadioButton.setBounds(x2, y1, 2*fieldWidth, fieldHeight);
	
	y1 += 30;
	looptraintypelabel.setBounds(x1, y1, 2*fieldWidth, fieldHeight);
	looptraintype.setBounds(x2, y1, fieldWidth, fieldHeight);

	
	y1 += 30;
	looplengthlabel.setBounds(x1, y1, fieldWidth, fieldHeight);
	looplength.setBounds(x2, y1, fieldWidth, fieldHeight);

	y1 += yDifference;
	maximumspeedLabel.setBounds(x1, y1, labelWidth, labelHeight);
	maximumspeedField.setBounds(x2, y1, fieldWidth, fieldHeight);
	
	
	
	y1 += 3 * yDifference;
	addloopbutton.setBounds(x1, y1, labelWidth, labelHeight);
	Viewallbutton.setBounds(x2, y1, labelWidth, labelHeight);

	y1 += 2 * yDifference;
	editdeletelabel.setBounds(x1, y1, labelWidth, labelHeight);
	LoopIdEditDeletefield.setBounds(x2, y1, labelWidth, labelHeight);
	
	
	y1 += 2*yDifference;
	editButton.setBounds(x1, y1, 65, buttonHeight);
	updateButton.setBounds(x1 + 70, y1, 90, buttonHeight);
	deleteButton.setBounds(x1 + 170, y1, 90, buttonHeight);
	doneButton.setBounds(x1 + 270, y1, 90, buttonHeight);

	y1 += yDifference;
	resultLabel.setBounds(x1, y1, 600, labelHeight);
}


@Override
public void setComponentActionListeners() {

addloopbutton.addActionListener(addloopbuttonButtonActionListener);
Viewallbutton.addActionListener(ViewallbuttonButtonActionListener);
editButton.addActionListener(editbuttonButtonActionListener);
editButton.addActionListener(editbuttonButtonActionListener);
updateButton.addActionListener(updatebuttonActionListener);
deleteButton.addActionListener(deletebuttonActionListener);
mainLineRadioButton.addItemListener(LooptypeItemListener);
loopLineRadioButton.addItemListener(LooptypeItemListener);
doneButton.addActionListener(doneButtonActionListener);
}

ActionListener editbuttonButtonActionListener = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (LoopIdEditDeletefield.getText().isEmpty()) {
			resultLabel.setText("Enter a block id to edit or delete");
			return;
		}

		int blockNo = Integer.parseInt(LoopIdEditDeletefield.getText());
		SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
			Loop loop = sectioninputdialog.getLoopByBlockNo(blockNo);

		 if (loop != null) {
			setLoopFields(loop);
			loopToBeEdited = loop;
			resultLabel.setText("Loop " + blockNo + " is shown");
		} else {
			resultLabel.setText("No  loop with no " + blockNo
					+ " exists");
		}

	}
};
ActionListener deletebuttonActionListener = new ActionListener() {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (LoopIdEditDeletefield.getText().isEmpty()) {
			resultLabel.setText("Enter a loop id to edit or delete");
			return;
		}

		int loopNo = Integer.parseInt(LoopIdEditDeletefield.getText());
		
		SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
	    Loop loop = sectioninputdialog.getLoopByBlockNo(loopNo);
	    
	    //Sneha : do not allow deletion of loop if selected loop is the only available to given station
	    int count=0x0;
      if (loop != null) {

		 ArrayList<Loop> temploopList = sectioninputdialog.loopList;
				
					for (Loop temploop : temploopList) {
						if( temploop.stationName.equals(loop.stationName))
							{
							count++;
						    }
					}
					if(count>1)
					{
						sectioninputdialog.removeLoop(loop);
						resultLabel.setText("Loop " + loopNo + " deleted");
					}
					else
					{
						resultLabel.setText("Can not delete loop " + loopNo +  " this is the only available with "+ loop.stationName );
					}
		} else {
			resultLabel.setText("No block or loop with no " + loopNo + " exists");
		}

	}
};

ActionListener updatebuttonActionListener = new ActionListener() {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if ( loopToBeEdited == null) {
			resultLabel.setText("Choose a loop to edit");
			return;
		}
				int LoopNo = setBlockidforLoop();

		SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
		
					Loop temploop = loopToBeEdited;
					int x = Integer.parseInt(Station_ID);
					sectioninputdialog.removeLoop(temploop);
					Loop loop= new Loop();
					String stationName = sectioninputdialog.getStationNameByStationId(x) ;
				if (mainLineRadioButton.isSelected()) {
					loop.whetherMainLine = 0;
				} else
					loop.whetherMainLine = 1;
				loop.stationName = stationName;
				loop.upLinks.clear();
				loop.downLinks.clear();
				loop.speedRestrictionList.clear();
				
		      	loop.blockNo = LoopNo;
		    	loop.maximumPossibleSpeed = Double.parseDouble(maximumspeedField
		    			.getText()); // Double.parseDouble(maximumSpeedField
		    			//.getText());

		    	if (upRadioButton.isSelected()) {
		    		loop.direction = GlobalVar.UP_DIRECTION;
		    	} else if (downRadioButton.isSelected()) {
		    		loop.direction = GlobalVar.DOWN_DIRECTION;
		    	} else
		    		loop.direction = GlobalVar.COMMON_DIRECTION;

		    	  String typeoftrain = (String)looptraintype.getSelectedItem();
		    			   
		          loop.setLoopTrainType(typeoftrain);
		          
		          sectioninputdialog.addLoop(loop);
				
				resultLabel.setText("Loop " + loop.blockNo + " updated");
		

		
		loopToBeEdited = null;
	}
};

private void setLoopFields(Loop loop) {
	

	if (loop.whetherMainLine == 0)
		mainLineRadioButton.setSelected(true);
	else
		loopLineRadioButton.setSelected(true);
//	setCommonBlockLoopFields(loop);
	
	maximumspeedField.setText(String.valueOf(loop.maximumPossibleSpeed));
	switch (loop.direction) {
	case GlobalVar.UP_DIRECTION:
		upRadioButton.setSelected(true);
		break;
	case GlobalVar.DOWN_DIRECTION:
		downRadioButton.setSelected(true);
		break;
	case GlobalVar.COMMON_DIRECTION:
	int loopno = loop.blockNo;
	 String loopId = String.format("%06d", loopno);         
		int loopIdcheck = Integer.parseInt(loopId.substring(2, 4));
		if (loopIdcheck == 20)
		commonRadioButton.setSelected(true);
		else if (loopIdcheck == 21)
			commonmiddleRadioButton.setSelected(true);
		else if (loopIdcheck ==22 )
			commonupRadioButton.setSelected(true);
		else if (loopIdcheck ==23 )
			commondownRadioButton.setSelected(true);
		
		break;
	}
	
}


ActionListener addloopbuttonButtonActionListener = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent arg0) {
	SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
	//System.out.println("addlooActionListener from LoopinputDialog called ");
	int LoopNo = setBlockidforLoop();
//	System.out.println(LoopNo);

if(LoopNo !=0)
{
	Loop loop = sectioninputdialog.getLoopByBlockNo(LoopNo);
	if (loop != null) {
		resultLabel.setText("Loop " + LoopNo + " already exists");
		return;
	}
	loop = new Loop();
	int x = Integer.parseInt(Station_ID);
	String stationName = sectioninputdialog.getStationNameByStationId(x) ;
	if (mainLineRadioButton.isSelected()) {
		loop.whetherMainLine = 0;
	} else
	loop.whetherMainLine = 1;
	
	loop.stationName = stationName;
    loop.blockNo = LoopNo;
    String typeoftrain = (String) looptraintype.getSelectedItem();
    loop.setLoopTrainType(typeoftrain);
    loop.maximumPossibleSpeed = Double.parseDouble(maximumspeedField
    .getText()); // Double.parseDouble(maximumSpeedField
    //.getText());
   	
    if (upRadioButton.isSelected()) {
    	loop.direction = GlobalVar.UP_DIRECTION;
    } else if (downRadioButton.isSelected()) {
    loop.direction = GlobalVar.DOWN_DIRECTION;
    } else
	loop.direction = GlobalVar.COMMON_DIRECTION;

    sectioninputdialog.addLoop(loop);
	resultLabel.setText("Loop " + loop.blockNo + " added");
}
else {
	resultLabel.setText("Two main lines of same type not possible");
	}

			

	}		
				
	};

	

ActionListener ViewallbuttonButtonActionListener = new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent arg0) {
				AllLoopData allLoopData = new AllLoopData();
			
		

	}
};

ActionListener doneButtonActionListener = new ActionListener(){
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//Sneha : check if user has added any loop for station
		//if not then keep prompting as station must have at least one loop entry
		int x = Integer.parseInt(Station_ID);
		SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
		String stationName = sectioninputdialog.getStationNameByStationId(x) ;
		ArrayList<Loop> temploopList = sectioninputdialog.loopList;
			int foundcount=0x0;
			for (Loop loop : temploopList) {
					
				if( loop.stationName.equals(stationName))
					{
					foundcount++;
				    }
			}
			if(foundcount!=0){
			dispose();
			
			//sneha : make loopInputDialog null so that in section input dialogbox visibility of 
			//station input dialog box decided accordingly
			GlobalVar.loopInputDialog=null;
			
			//sneha : make station input dialog box visible 
			StationInputDialog stationinputdialog = GlobalVar.getStationInputDialog();
			stationinputdialog.setVisible(true);
			}
			else
			{
				resultLabel.setText("Station Must have atleast one loop entry. Add loop for station " + stationName + ".");
			}
			

}
};

class AllLoopData extends JFrame implements TableModelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable loopDataTable;
	
	public AllLoopData() {

		
		

		SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
		ArrayList<Loop> TotalloopList = sectioninputdialog.loopList ;

int x = Integer.parseInt(Station_ID);
		
		String stationName = sectioninputdialog.getStationNameByStationId(x) ;		
	
		this.setTitle("List of "+ stationName +" loops");
		
		loopList.clear();
		int count = 0;
		for (Loop loop : TotalloopList) 
		{
			if (stationName == TotalloopList.get(count).stationName)
			{
			loopList.add(loop)	;
			}
			count++;
		}
		
		
		

		loopDataTable = new JTable(new AbstractTableModel() {

			/**
			 * 
			 */
		private static final long serialVersionUID = 1L;
			String[] columnNames = { "Id", "Direction", "Station Name", "Type",
					"Type of Trains Allowed", "Maximum Speed" };
			
			
			public String getColumnName(int col) {
				return columnNames[col];
			}

			public int getRowCount() {
				return loopList.size();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return loopList.get(row).blockNo;
				case 1:
					int direction = loopList.get(row).direction;
					String directionString = GlobalVar
							.getDirectionStringFromDirection(direction);
					return directionString;
				case 2:
					return loopList.get(row).stationName;
				case 3:
					String loopType = "mainLine";
					if (loopList.get(row).whetherMainLine != 0)
						loopType = "loop";
					return loopType;
				case 4:	
					return loopList.get(row).getLoopTrainType();
				case 5:
					return loopList.get(row).maximumPossibleSpeed;
		}

				return null;
			}

		});

		JScrollPane scrollPane = new JScrollPane(loopDataTable);
		loopDataTable
				.setPreferredScrollableViewportSize(new Dimension(500, 70));
		getContentPane().add(scrollPane);
		setBounds(70, 70, 900, 400);
		setVisible(true);
	}
	@Override
	public void tableChanged(TableModelEvent arg0) {
	}

}


public void writeLoops(ArrayList<Loop> loopList) throws IOException {
	String loopFileName = FileNames.getLoopFileName();
	BufferedWriter bw = new BufferedWriter(new FileWriter(loopFileName));
	String formatString = "/*Loop Number	Direction	Type 	Station Code  Type-of-Trains-allowed	 Loop Velocity	Uplinks	Uplink lengths	Priority	Cross-over	Down link	Downlink Lengths	Priority	Cross-over	Speed Restriction	SMP	EMP*/\n\n";
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

	//	writeCommonBlockLoopAttributes(bw, loop);

		bw.write("\n");
	}
	bw.close();
}

/*private void writeCommonBlockLoopAttributes(BufferedWriter bw, Block block)
		throws IOException {
	bw.write(" ");
	bw.write(String.valueOf((int) (block.maximumPossibleSpeed)));
	bw.write(" ");

	writeLinkStrings(bw, GlobalVar.UP_DIRECTION, block);
	writeLinkStrings(bw, GlobalVar.DOWN_DIRECTION, block);
	writeSpeedLimitStrings(bw, block);
}
private void setLoopFields(Loop loop) {
	loopRadioButton.setSelected(true);
	stationNameComboBox.setSelectedItem(loop.stationName);
	if (loop.whetherMainLine == 0)
		mainLineRadioButton.setSelected(true);
	else
		loopLineRadioButton.setSelected(true);
	setCommonBlockLoopFields(loop);
}

*/
}
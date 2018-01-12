package input;

import globalVariables.FileNames;
import globalVariables.GlobalVar;
import gui.entities.sectionEntities.Station;
import gui.entities.sectionEntities.trackEntities.Loop;
import gui.entities.sectionEntityList.BlockList;
import gui.entities.sectionEntities.trackEntities.Block;
import gui.entities.sectionEntities.trackEntities.Link;
import gui.entities.trainEntities.ScheduledTrain;
import gui.entities.trainEntities.Train;
import sun.net.TelnetProtocolException;

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
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class LoopInputDialog extends InputDialog{

	
	public JLabel loopTypeLabel = new JLabel("Type of Line");
	public ButtonGroup loopTypeButtonGroup = new ButtonGroup();
	public JLabel directionLabel = new JLabel("Direction");
	public JRadioButton upRadioButton = new JRadioButton("Up");
	public JRadioButton downRadioButton = new JRadioButton("Down");
	public JRadioButton commonupRadioButton = new JRadioButton("Common on up side");
	public JRadioButton commondownRadioButton = new JRadioButton("Common on down side");
	public JRadioButton commonmiddleRadioButton = new JRadioButton("Common in middle");
	public String Station_ID = null;
	public JButton addloopbutton = new JButton("Add new loop");
	public JButton Viewallbutton = new JButton("View All");
//	public JTextField LoopIdEditDeletefield = new JTextField();
	public JButton deleteButton = new JButton("Delete");
	public JButton doneButton = new JButton("Done");
	public JLabel editdeletelabel = new JLabel("Select Loop to be deleted");
	public JLabel looptraintypelabel = new JLabel("Type of Trains allowed");
	public JLabel looplengthlabel = new JLabel("Loop Length");
	public JLabel maximumspeedLabel = new JLabel("Maximum Loop Entry Velocity");
	public JTextField maximumspeedField = new JTextField();
	public JComboBox looptraintype = new JComboBox();
	public JComboBox looplength = new JComboBox();
	public JComboBox selectLoop= new JComboBox<>();
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

	public int lastid01=0 ;
	public int lastid10=0 ;
	public int lastid11=0 ;
	public int lastid21=0 ;
	public int lastid22=0 ;
	public int lastid23=0 ;
	
	
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
			  if( loop.stationName.equals(stationName) ){
				  Integer looptest =Integer.parseInt(String.format("%07d",loop.blockNo).substring(3,5));
				  if (looptest ==0)	counter00++ ;
				  else if (looptest == 10) counter10++ ;
				  else if (looptest ==20) counter20++ ;
				  else if (looptest ==01) counter01++ ;
				  else if (looptest ==11) counter11++ ;
				  else if (looptest ==21) counter21++ ;
				  else if (looptest ==22)counter22++ ;
				  else if (looptest ==23)counter23++ ;
				  }
			}
		  

		  for (Loop loop : temploopList) {
			  if( loop.stationName.equals(stationName) ){
				  Integer looptest =Integer.parseInt(String.format("%07d",loop.blockNo).substring(3,5));
			
				  if (looptest ==01)
				  {
					  int tempid=loop.getBlockNo();
					  String tempstring = String.format("%07d",tempid); 
					  lastid01 = Integer.parseInt(tempstring.substring(5));
					  selectLoop.addItem(tempstring);

				  }
				  else if (looptest ==11) {
					  int tempid=loop.getBlockNo();
					  String tempstring = String.format("%07d",tempid); 
					  lastid11 = Integer.parseInt(tempstring.substring(5));
					  selectLoop.addItem(tempstring);
				  }
				  else if (looptest ==21) {
					  int tempid=loop.getBlockNo();
					  String tempstring = String.format("%07d",tempid); 
					  lastid21 = Integer.parseInt(tempstring.substring(5));
					  selectLoop.addItem(tempstring);
				  }
				  else if (looptest ==22){
					  int tempid=loop.getBlockNo();
					  String tempstring = String.format("%07d",tempid); 
					  lastid22 = Integer.parseInt(tempstring.substring(5));
					  selectLoop.addItem(tempstring);
				  }
				  else if (looptest ==23){
					  int tempid=loop.getBlockNo();
					  String tempstring = String.format("%07d",tempid); 
					  lastid23 = Integer.parseInt(tempstring.substring(5));
					  selectLoop.addItem(tempstring);
				  }
				  }
			}
		  
		  

		  
		  	int count = 1;
			String stationId=null  ;
			for (Station station : GlobalVar.sectionInputDialog.stationList) {
			if( station.stationName.equals(stationName))	stationId =String.format("%03d",count);
			count ++;
		  }
		  
			Station_ID = stationId;
		  	looptraintype = new JComboBox(looptraintypestring);
		  	looplength = new JComboBox(filename) ; 
		  	
		  	setComponentActionListeners();
		  	setComponentBounds();
		  	addComponents();
		  	maximumspeedField.setText("15");
		 	this.add(jpanel);
		 	this.setTitle("Loop Input"+ " for "+stationName);
		 	resultLabel.setText("");

	}
	
	public int setBlockidforLoop(){
		int blockid = 0;	
		String blockidstring;
		if(upRadioButton.isSelected() ){
			counter01++ ;
			String temp01 = String.format("%02d",counter01); 			
			blockidstring = (Station_ID+"01"+temp01);
			blockid = Integer.parseInt(blockidstring);
		}
		else if(downRadioButton.isSelected()  ){
			counter11++ ;
			String temp11 = String.format("%02d",counter11); 			
			blockidstring = (Station_ID+"11"+temp11);
			blockid = Integer.parseInt(blockidstring);
		}
		else if(commonmiddleRadioButton.isSelected() ){
			counter21++ ;
			String temp21 = String.format("%02d",counter21); 			
			blockidstring = (Station_ID+"21"+temp21);
			blockid = Integer.parseInt(blockidstring);
		}
		else if(commonupRadioButton.isSelected() ){
			counter22++ ;
			String temp22 = String.format("%02d",counter22); 			
			blockidstring = (Station_ID+"22"+temp22);
			blockid = Integer.parseInt(blockidstring);
		}
		else if(commondownRadioButton.isSelected() ){
			counter23++ ;
			String temp23 = String.format("%02d",counter23); 			
			blockidstring = (Station_ID+"23"+temp23);
			blockid = Integer.parseInt(blockidstring);
		}
		 return blockid;			
	}

	public int getBlockidforLoop(){
		int blockid = 0;	
		String blockidstring;
		if(upRadioButton.isSelected() ){
					  if (lastid01!= 00){
						  lastid01++;
						  	String temp = String.format("%02d",(lastid01));
							blockidstring = (Station_ID+"01"+ temp);
							blockid = Integer.parseInt(blockidstring);
							}
					  else	{
						  	lastid01++;
						  	String temp23 = String.format("%02d",(lastid01));
							blockidstring = (Station_ID+"01"+ temp23);
							blockid = Integer.parseInt(blockidstring);
							}
		}
		else if(downRadioButton.isSelected()  ){
			  		if (lastid11!= 00){
			  			 lastid11++;
			  			 String temp = String.format("%02d",(lastid11));
			  			 blockidstring = (Station_ID+"11"+ temp);
			  			 blockid = Integer.parseInt(blockidstring);
			  			 }
			  		else {
			  			 lastid11++;
			  			 String temp = String.format("%02d",(lastid11));
			  			 blockidstring = (Station_ID+"11"+ temp);
			  			 blockid = Integer.parseInt(blockidstring);
			  			 }
		}
		else if(commonmiddleRadioButton.isSelected() ){
			  		if (lastid21!= 00){
			  			lastid21++;
			  			String temp = String.format("%02d",(lastid21));
			  			blockidstring = (Station_ID+"21"+ temp);
			  			blockid = Integer.parseInt(blockidstring);
			  			}
			  	   else	{
			  		   	lastid21++;
			  		   	String temp = String.format("%02d",(lastid21));
			  		   	blockidstring = (Station_ID+"21"+ temp);
			  		   	blockid = Integer.parseInt(blockidstring);
			  		   	}
		}
		else if(commonupRadioButton.isSelected() ){
			  if (lastid22!= 00){
				  		lastid22++;
				  		String temp = String.format("%02d",(lastid22));
				  		blockidstring = (Station_ID+"22"+ temp);
				  		blockid = Integer.parseInt(blockidstring);
				  		}
			  	   else {
			  		   	lastid22++;
			  		   	String temp = String.format("%02d",(lastid22));
			  		   	blockidstring = (Station_ID+"22"+ temp);
			  		   	blockid = Integer.parseInt(blockidstring);
			  		   	}
		}
		else if(commondownRadioButton.isSelected() ){
				if (lastid23!= 00){
						lastid23++;
						String temp = String.format("%02d",(lastid23));
						blockidstring = (Station_ID+"23"+ temp);
						blockid = Integer.parseInt(blockidstring);
						}
				   else	{
					   lastid23++;
					   String temp = String.format("%02d",(lastid23));
					   blockidstring = (Station_ID+"23"+ temp);
					   blockid = Integer.parseInt(blockidstring);
				   		}
		}

		 return blockid;			
	}

	public int getLoopLineCount(){
		  int loopCount=0;
		  loopCount=counter01+counter11+counter21+counter22+counter23;
 
		return loopCount;
	}
	
	@Override
	public void addComponents() {
		this.add(resultLabel);
		add(directionLabel);
		add(upRadioButton);
		add(downRadioButton);
		add(commonupRadioButton);
		add(commondownRadioButton);
		add(commonmiddleRadioButton);
		add(looptraintype);
		add(selectLoop);
		add(looptraintypelabel);
        add(looplength);
        add(looplengthlabel);
		loopTypeButtonGroup.add(upRadioButton);
		loopTypeButtonGroup.add(downRadioButton);
		loopTypeButtonGroup.add(commonupRadioButton);
		loopTypeButtonGroup.add(commondownRadioButton);
		loopTypeButtonGroup.add(commonmiddleRadioButton);
		this.add(addloopbutton);
		this.add(deleteButton);
		this.add(addloopbutton);
		this.add(Viewallbutton);
		this.add(editdeletelabel);
		this.add(doneButton);
		this.add(maximumspeedField);

		this.add(maximumspeedLabel);
		
		directionLabel.setVisible(false);
		upRadioButton.setVisible(false);
		downRadioButton.setVisible(false);
		commonupRadioButton.setVisible(false);
		commondownRadioButton .setVisible(false);
		commonmiddleRadioButton.setVisible(false);
		selectLoop.setVisible(false);
		looptraintype.setVisible(false);
		looptraintypelabel.setVisible(false);
        looplength.setVisible(false);
        looplengthlabel.setVisible(false);
		addloopbutton.setVisible(false);
		deleteButton.setVisible(false);
		addloopbutton.setVisible(false);
		editdeletelabel.setVisible(false);
		maximumspeedField.setVisible(false);

		maximumspeedLabel.setVisible(false);

	}

@Override
public void setComponentBounds() {
	int yDifference = 25;
	labelWidth += 40;
	x2 += 40;
	
	y1 += yDifference;
	loopTypeLabel.setBounds(x1, y1, labelWidth, labelHeight);
	y1 += 20;
	directionLabel.setBounds(x1, y1, labelWidth, labelHeight);
	upRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
	y1 += 20;
	downRadioButton.setBounds(x2, y1, fieldWidth, fieldHeight);
	y1 += 20;
	commonupRadioButton.setBounds(x2, y1, 2*fieldWidth, fieldHeight);
	y1 += 20;
	commondownRadioButton.setBounds(x2, y1, 2*fieldWidth, fieldHeight);
	y1 += 20;
	commonmiddleRadioButton.setBounds(x2, y1, 2*fieldWidth, fieldHeight);
	
	y1 += 30;
	looptraintypelabel.setBounds(x1, y1,fieldWidth, fieldHeight);
	looptraintype.setBounds(x2, y1, fieldWidth, fieldHeight);
	
	y1 += 30;
	looplengthlabel.setBounds(x1, y1, fieldWidth, fieldHeight);
	looplength.setBounds(x2, y1, fieldWidth, fieldHeight);
	
	y1 += 30;
	maximumspeedLabel.setBounds(x1, y1, labelWidth, labelHeight);
	maximumspeedField.setBounds(x2, y1, fieldWidth, fieldHeight);
	y1 += 3 * yDifference;
	addloopbutton.setBounds(x1, y1, labelWidth, labelHeight);
	Viewallbutton.setBounds(x2, y1, labelWidth, labelHeight);

	y1 += 2 * yDifference;
	editdeletelabel.setBounds(x1, y1, labelWidth, labelHeight);
	selectLoop.setBounds(x2, y1, labelWidth, labelHeight);
	y1 += 2*yDifference;


	deleteButton.setBounds(x1 + 170, y1, 90, buttonHeight);
	doneButton.setBounds(x1 + 270, y1, 90, buttonHeight);
	y1 += yDifference;
	resultLabel.setBounds(x1, y1, 600, labelHeight);
}


@Override
public void setComponentActionListeners() {


addloopbutton.addActionListener(addloopbuttonButtonActionListener);
Viewallbutton.addActionListener(ViewallbuttonButtonActionListener);

deleteButton.addActionListener(deletebuttonActionListener);
loopTypeButtonGroup.add(upRadioButton);
loopTypeButtonGroup.add(downRadioButton);
loopTypeButtonGroup.add(commonupRadioButton);
loopTypeButtonGroup.add(commondownRadioButton);
loopTypeButtonGroup.add(commonmiddleRadioButton);
doneButton.addActionListener(doneButtonActionListener);
}


ActionListener deletebuttonActionListener = new ActionListener() {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		resultLabel.setText("");
		int loopNo=0;
		if(selectLoop.getSelectedIndex()!=-1){
		loopNo=Integer.parseInt(selectLoop.getSelectedItem().toString());
		SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
	    Loop loop = sectioninputdialog.getLoopByBlockNo(loopNo);
		ArrayList<Train> trainList = sectioninputdialog.getTrainList(true);
		String trainsHalting=null;
		for(Train tempTrain:trainList){
			if(ifTrainHaltAtThisLoop(tempTrain, loop))
			{
				if(trainsHalting==null){
					trainsHalting=" " + Integer.toString(tempTrain.getTrainNo());
				}
				else{
					trainsHalting=trainsHalting + "," + Integer.toString(tempTrain.getTrainNo());
				}
			}
		}

			if(trainsHalting!=null){
				String msg= "Do ypu want to delete selected loop? " + " This will delete halt of the trains " + trainsHalting ;
				int editResponse = JOptionPane.showConfirmDialog(null,msg, "Confirm",
				        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(editResponse==JOptionPane.YES_NO_OPTION){
					
				for(Train tempTrain:trainList){
					if(ifTrainHaltAtThisLoop(tempTrain, loop))
					{
						removeHalt(tempTrain,loop.blockNo);
					}
					
				}
				}
				else{
					return;
				}
					
			}
		

		String loopId = String.format("%07d",loop.blockNo);						
		int loopstationId = Integer.parseInt(loopId.substring(0, 3));
		 Integer looptest =Integer.parseInt(String.format("%07d",loop.blockNo).substring(3,5));
		 		for (Block block : sectioninputdialog.blockList){
						String blockId=String.format("%07d", block.blockNo);
						int blockstationId = Integer.parseInt(blockId.substring(0, 3));
						Integer blocktest =Integer.parseInt(String.format("%07d",block.blockNo).substring(3,5));
					if(blockstationId==(loopstationId-1)){
						if((looptest==01 )|| (looptest==21)||(looptest==22)||looptest==23){
							if(blocktest==00){
							//edit block.uplink
								if(block.upLinks.size()>0){
									ArrayList<Link> tempLinkList = block.upLinks;
									for (Link link:tempLinkList){
										//System.out.println("in uplinkloop " + " loopno " + loop.blockNo +" " + link.nextBlockNo + " " + block.blockNo);
										if(link.nextBlockNo==loop.blockNo){
											block.upLinks.remove(link);
											break;
										}
											
									}

								}
							}
							if(blocktest==20){
							//edit block.uplink
								if(block.upLinks.size()>0){
									ArrayList<Link> tempLinkList = block.upLinks;
										for (Link link:tempLinkList){
											//System.out.println("in uplinkloop " + " loopno " + loop.blockNo +" " + link.nextBlockNo + " " + block.blockNo);
											if(link.nextBlockNo==loop.blockNo){
												block.upLinks.remove(link);
												break;
											}
											
										}
									}
								}
							}
								
						}
							
					if(blockstationId==loopstationId){
						if((looptest==11 )|| (looptest==21)||(looptest==22)||looptest==23){
							if(blocktest==10){
							//edit block.downlink
								if(block.downLinks.size()>0){
									ArrayList<Link> tempLinkList = block.downLinks;
									for (Link link:tempLinkList){
										//System.out.println("in downlinkloop " + " loopno " + loop.blockNo +" " + link.nextBlockNo + " " + block.blockNo);
										if(link.nextBlockNo==loop.blockNo){
											block.downLinks.remove(link);
											break;
										}
									} 
								}
							}
						if(blocktest==20){
							//edit block.downlink
							if(block.downLinks.size()>0){
								ArrayList<Link> tempLinkList = block.downLinks;
									for (Link link:tempLinkList){
										//System.out.println("in downlinkloop " + " loopno " + loop.blockNo +" " + link.nextBlockNo + " " + block.blockNo);
										if(link.nextBlockNo==loop.blockNo){
											block.downLinks.remove(link);
											break;
										}
									} 
								}
							}
						}								
								
					}
							
				}
				
				sectioninputdialog.removeLoop(loop);						
				resultLabel.setText("Loop " + loopNo + " deleted");
				selectLoop.removeItemAt(selectLoop.getSelectedIndex());
				selectLoop.setSelectedItem(-1);
		}
		else{resultLabel.setText("Select Loop to be deleted");}
	}
};


private void setLoopFields(Loop loop) {
	


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
	 String loopId = String.format("%07d", loopno);         
		int loopIdcheck = Integer.parseInt(loopId.substring(3, 5));

		if (loopIdcheck == 21)
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
		resultLabel.setText("");
		boolean uploopline=upRadioButton.isSelected();
		boolean downloopline=downRadioButton.isSelected();
		boolean commonUploopline=commonupRadioButton.isSelected();
		boolean commondownloopline=commondownRadioButton.isSelected();
		boolean commonmiddleloopline=commonmiddleRadioButton.isSelected();
		
		SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
		int LoopNo = getBlockidforLoop();
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
			loop.whetherMainLine = 1;
			loop.stationName = stationName;
			loop.blockNo = LoopNo;
			String typeoftrain = (String) looptraintype.getSelectedItem();
			loop.setLoopTrainType(typeoftrain);
			loop.maximumPossibleSpeed = Double.parseDouble(maximumspeedField.getText()); // Double.parseDouble(maximumSpeedField
			
			String loopIdString = String.format("%07d",loop.blockNo);
			int loopstationId = Integer.parseInt(loopIdString.substring(0, 3));
			int uplinkLinkPriority=0;
			int downlinkLinkPriority=0;
			
			int loopPriorityCheck = Integer.parseInt(loopIdString.substring(3, 5));
			if (loopPriorityCheck == 00	|| loopPriorityCheck == 20)
				uplinkLinkPriority=1;
			else if (loopPriorityCheck == 01)
				uplinkLinkPriority=2;
			else if (loopPriorityCheck == 22)
				uplinkLinkPriority=3;
			else if (loopPriorityCheck == 21)
				uplinkLinkPriority=4;
			else if (loopPriorityCheck == 23)
				uplinkLinkPriority=5;
			
			
			if (loopPriorityCheck == 10
					|| loopPriorityCheck == 20)
				downlinkLinkPriority = 1;
			else if (loopPriorityCheck == 11)
				downlinkLinkPriority = 2;
			else if (loopPriorityCheck == 23)
				downlinkLinkPriority = 3;
			else if (loopPriorityCheck == 21)
				downlinkLinkPriority = 4;
			else if (loopPriorityCheck == 22)
				downlinkLinkPriority = 5;
			
   	
			if (upRadioButton.isSelected()) loop.direction = GlobalVar.UP_DIRECTION;
			else if (downRadioButton.isSelected())loop.direction = GlobalVar.DOWN_DIRECTION;
			else loop.direction = GlobalVar.COMMON_DIRECTION;
			
			//start-----------------------if up loop line is beiing added--------------
			if(uploopline||downloopline||commonUploopline||commondownloopline||commonmiddleloopline){
				

				for (Block block : sectioninputdialog.blockList){
				
					String blockId=String.format("%07d", block.blockNo);
					int blockstationId = Integer.parseInt(blockId.substring(0, 3));
					int blockSerialNo =Integer.parseInt(blockId.substring(5));


					if ((loopstationId == blockstationId) && (blockSerialNo == 10)){
						if  (block.direction==GlobalVar.UP_DIRECTION){
							if(uploopline||commonUploopline||commondownloopline||commonmiddleloopline){
							Link link = new Link();
							link.setNextBlockNo(block.blockNo);
							link.setPriority(1);
							loop.upLinks.add(link);
							//System.out.println("---Block Up--loop common/up " + loop.blockNo + " " + block.blockNo);
							}
						}
						if(block.direction==GlobalVar.COMMON_DIRECTION) {
							if(uploopline){
								Link link = new Link();
								link.setNextBlockNo(block.blockNo);
								link.setPriority(1);
								loop.upLinks.add(link);
								//System.out.println("---Block common--loop up " + loop.blockNo + " " + block.blockNo);
							}
							if(downloopline){
								Link linkforBlock = new Link();
								linkforBlock.setNextBlockNo(loop.blockNo);
								linkforBlock.setPriority(downlinkLinkPriority);
								block.downLinks.add(linkforBlock);
								//System.out.println("---Block common--loop down " + loop.blockNo + " " + block.blockNo);
							}
							if(commonUploopline||commondownloopline||commonmiddleloopline){
								Link linkforLoop = new Link();
								linkforLoop.setNextBlockNo(block.blockNo);
								linkforLoop.setPriority(1);
								loop.upLinks.add(linkforLoop);
								
								//System.out.println("---Block common--loop coomon " + loop.blockNo + " " + block.blockNo);
								
								Link linkforBlock = new Link();
								linkforBlock.setNextBlockNo(loop.blockNo);
								linkforBlock.setPriority(downlinkLinkPriority);
								block.downLinks.add(linkforBlock);
								//System.out.println("---Block common--loop coomon " + block.blockNo + " " + loop.blockNo);
							}
						

						}
						if(block.direction==GlobalVar.DOWN_DIRECTION){
							if(downloopline||commonUploopline||commondownloopline||commonmiddleloopline){
								Link linkforBlock = new Link();
								linkforBlock.setNextBlockNo(loop.blockNo);
								linkforBlock.setPriority(downlinkLinkPriority);
								block.downLinks.add(linkforBlock);
								//System.out.println("Block down--loop down/common " + block.blockNo + " " + loop.blockNo);

							}
						
						}

					}
					
				}
			}

			//start-----------------------if down loopline is added--------------
			if(uploopline||downloopline||commonUploopline||commondownloopline||commonmiddleloopline){
				Block lastblockInCommon=null;
				Block lastblockInDown=null;
				Block lastblockInUp=null;
				for (Block block : sectioninputdialog.blockList){
					String blockId=String.format("%07d", block.blockNo);
					int blockstationId = Integer.parseInt(blockId.substring(0, 3));

					
					if ((loopstationId-1) == blockstationId){
						if(block.direction==GlobalVar.DOWN_DIRECTION){
							lastblockInDown=block;
						}
						if(block.direction==GlobalVar.COMMON_DIRECTION) {
							lastblockInCommon=block;
						}
						if(block.direction==GlobalVar.UP_DIRECTION) {
							lastblockInUp=block;
						}
					}
					
				}
				
				if(downloopline){
					if(lastblockInDown!=null){
						Link linkforloop=new Link();
						linkforloop.setNextBlockNo(lastblockInDown.blockNo);
						linkforloop.setPriority(1);
						loop.downLinks.add(linkforloop);
						//System.out.println("---Block down--loop down " + loop.blockNo + " " + lastblockInDown.blockNo);

						}
					if(lastblockInCommon!=null){
						Link linkforloop=new Link();
						linkforloop.setNextBlockNo(lastblockInCommon.blockNo);
						linkforloop.setPriority(1);
						loop.downLinks.add(linkforloop);
						//System.out.println("---Block common--loop down " + loop.blockNo + " " + lastblockInCommon.blockNo);
					}
				}
				if(commonUploopline||commondownloopline||commonmiddleloopline){
					if(lastblockInDown!=null){
						Link linkforloop=new Link();
						linkforloop.setNextBlockNo(lastblockInDown.blockNo);
						linkforloop.setPriority(1);
						loop.downLinks.add(linkforloop);
						//System.out.println("---Block down--loop common " + loop.blockNo + " " + lastblockInDown.blockNo);
		
					}
					if(lastblockInUp!=null){
						Link linkforblock=new Link();
						linkforblock.setNextBlockNo(loop.blockNo);
						linkforblock.setPriority(uplinkLinkPriority);
						lastblockInUp.upLinks.add(linkforblock);
						//System.out.println("---Block up--loop common " + lastblockInUp.blockNo + " " + loop.blockNo);
					}
					if(lastblockInCommon!=null){
						Link linkforloop=new Link();
						linkforloop.setNextBlockNo(lastblockInCommon.blockNo);
						linkforloop.setPriority(1);
						loop.downLinks.add(linkforloop);
						//System.out.println("---Block common--loop common " + loop.blockNo + " " + lastblockInCommon.blockNo);
						
						Link linkforblock=new Link();
						linkforblock.setNextBlockNo(loop.blockNo);
						linkforblock.setPriority(uplinkLinkPriority);
						lastblockInCommon.upLinks.add(linkforblock);
						//System.out.println("---Block common--loop common " + lastblockInCommon.blockNo + " " + loop.blockNo);

					}
				}
				if(uploopline){
					if(lastblockInUp!=null){
						Link linkforblock=new Link();
						linkforblock.setNextBlockNo(loop.blockNo);
						linkforblock.setPriority(uplinkLinkPriority);
						lastblockInUp.upLinks.add(linkforblock);
						//System.out.println("---Block up--loop up " + lastblockInUp.blockNo + " " + loop.blockNo);
					}
					if(lastblockInCommon!=null){
						Link linkforblock=new Link();
						linkforblock.setNextBlockNo(loop.blockNo);
						linkforblock.setPriority(uplinkLinkPriority);
						lastblockInUp.upLinks.add(linkforblock);
						//System.out.println("---Block common--loop up " + lastblockInUp.blockNo + " " + loop.blockNo);

					}

						
				}
				
				
				
			}


			sectioninputdialog.addLoop(loop);
			resultLabel.setText("Loop " + loop.blockNo + " added");
			}
		else {
			resultLabel.setText("Select one of Loopline loop to be added");
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
			resultLabel.setText("");
			dispose();

}
};

public void printBlockLinkage(){
	
	
	SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
    	for (Block block : sectioninputdialog.blockList){
						Integer blocktest =Integer.parseInt(String.format("%07d",block.blockNo).substring(3,5));
						
						System.out.println("-------- ");
						if(blocktest==00){
							//edit block.uplink
							System.out.print(block.blockNo + " up block");
								if(block.upLinks.size()>0){
									ArrayList<Link> tempLinkList = block.upLinks;
									for (Link link:tempLinkList){
										System.out.print(" " + link.nextBlockNo + " ");
										}
								}
						}
						
						
						if(blocktest==20){
							//edit block.uplink
							System.out.println("-------- ");
							System.out.print(block.blockNo + " common block");
								if(block.upLinks.size()>0){
									ArrayList<Link> tempLinkList = block.upLinks;
									for (Link link:tempLinkList){
										System.out.print(" " + link.nextBlockNo + " ");
										}
								}
						}

						if(blocktest==10){
							//edit block.uplink
							System.out.print(block.blockNo + " downblock");
								if(block.upLinks.size()>0){
									ArrayList<Link> tempLinkList = block.upLinks;
									for (Link link:tempLinkList){
										System.out.print(" " + link.nextBlockNo + " ");
										}
										
								}
						
						}
						
	
}
    	System.out.println();
}

boolean ifTrainHaltAtThisLoop( Train tempTrain, Loop loop){

		if(tempTrain.scheduled){
		for(int i=0;i<tempTrain.numberofhalts;i++)
			if(tempTrain.refTables.get(i).refLoopNo==loop.blockNo){
			return true;	
			}
		}
		return false;
}

void removeHalt(Train tempTrain,int loopId){
	
	for(int i=0;i<tempTrain.getNumberofHalts();i++){
		if(tempTrain.refTables.get(i).refLoopNo==loopId){
//			System.out.println("i the item removed " + i);
			tempTrain.refTables.remove(i);
			tempTrain.setNumberofHalts((tempTrain.getNumberofHalts())-1);
			return;
		}
	}
		
	
}

class AllLoopData extends JFrame implements TableModelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTable loopDataTable;
	
	public AllLoopData() {

		
		this.setTitle("List of all loops");



		loopDataTable = new JTable(new AbstractTableModel() {
			
			SectionInputDialog sectioninputdialog = GlobalVar.getSectionInputDialog();
			ArrayList<Loop> TotalloopList = sectioninputdialog.loopList ;


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
				return TotalloopList.size();
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return TotalloopList.get(row).blockNo;
				case 1:
					int direction = TotalloopList.get(row).direction;
					String directionString = GlobalVar
							.getDirectionStringFromDirection(direction);
					return directionString;
				case 2:
					return TotalloopList.get(row).stationName;
				case 3:
					String loopType = "mainLine";
					if (TotalloopList.get(row).whetherMainLine != 0)
						loopType = "loop";
					return loopType;
				case 4:	
					return TotalloopList.get(row).getLoopTrainType();
				case 5:
					return TotalloopList.get(row).maximumPossibleSpeed;
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
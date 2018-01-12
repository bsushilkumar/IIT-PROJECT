package simulator.outputFeatures;

import globalVariables.GlobalVar;
import input.FreightTrainInputDialog;
import input.SectionInputDialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import simulator.input.SimulationInstance;
import simulator.scheduler.SimulationStart;
import simulator.ui.StartUpScreen;
import simulator.util.Debug;

/**
 * 
 */
@SuppressWarnings("serial")
public class MainInterface extends JFrame {
	/**
	 * mainMenuBar
	 */
	JMenuBar mainMenuBar = new JMenuBar();
	/**
	 * jMenu1
	 */
	JMenu jMenu1 = new JMenu();
	/**
	 * jMnItSelect
	 */
	JMenuItem jMnItGUI = new JMenuItem("GUI");

	
	JMenuItem jMnItSelect = new JMenuItem();
	/**
	 * jMnItRun
	 */
	JMenuItem jMnItRun = new JMenuItem();
	//santhosh
	JMenuItem jMnItCapacity= new JMenuItem();
	JMenuItem jMnItRawCapacity=new JMenuItem();
	/**
	 * jMnItExit
	 */
	JMenuItem jMnItExit = new JMenuItem();
	/**
	 * jMenu2
	 */
	JMenu jMenu2 = new JMenu();
	/**
	 * jMnRunTime
	 */
	JMenuItem jMnRunTime = new JMenuItem();
	/**
	 * borderLayout1
	 */
	BorderLayout borderLayout1 = new BorderLayout();
	/**
	 * jMenu3
	 */
	JMenu jMenu3 = new JMenu();
	/**
	 * jChkMnuDebug
	 */
	JCheckBoxMenuItem jChkMnuDebug = new JCheckBoxMenuItem();

	/**
 * 
 */
	public MainInterface() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
 */
	private void jbInit() {
		this.getContentPane().setLayout(borderLayout1);
		ImageIcon img = new ImageIcon("C:\\Users\\Soumya\\Desktop\\images.jpg");
		this.setIconImage(img.getImage());
		  JLabel background=new JLabel(new ImageIcon("C:\\Users\\Soumya\\Desktop\\logo.png"));
		    add(background);//adding images by Soumya
		jMenu1.setText("Simulation");
		this.setLayout(null);
		JLabel lbl1 = new JLabel("SIMULATOR FOR MIXED TRAFFIC ON SECTIONS");
		JLabel lbl2 = new JLabel("Tool developed for RDSO - Lucknow by IIT Bombay");
		JLabel lbl3 = new JLabel("Jar-file version date: July 2016");
		JLabel lbl4 = new JLabel("Contributors - Faculty, students and project staff at IIT Bombay");
		JLabel lbl5 = new JLabel("Earlier support from Indian Railways (IRISET, Secunderabadand other agencies), and technical");
		JLabel contact=new JLabel("Contact person:");
		JLabel name=new JLabel("Prof. Narayan Rangaraj,");
		JLabel dept=new JLabel("Industrial Engineering and Operations Research,");
		JLabel insti=new JLabel(" IIT Bombay, Powai, Mumbai");
		String str="ww";
		JLabel link = new JLabel("<html><a href=\" " + str + "\"> narayan.rangaraj@iitb.ac.in </a></html>");
		JLabel email=new JLabel("Email:");
		//JLabel present=new JLabel("For current version of the tool, for demonstration and trial, please see");
		JLabel ackcont=new JLabel("contribution of ITSPE, Hyderabad to the software is acknowledged");
		this.add(lbl1);
		this.add(lbl2);
		this.add(lbl3);
		this.add(lbl4);
		this.add(lbl5);this.add(ackcont);
        this.add(contact);
        this.add(name);this.add(dept);this.add(insti);this.add(email);
        //this.add(present);
        this.add(link);
		lbl1.setLocation(200, 200);
		lbl2.setLocation(190, 220);
		lbl3.setLocation(0, 400);
		lbl4.setLocation(0, 425);
		lbl5.setLocation(0, 450);
        contact.setLocation(0,500);
        ackcont.setLocation(0,465);
        
        name.setLocation(10,525);dept.setLocation(10,540);insti.setLocation(10,555);
        email.setLocation(10,570);//present.setLocation(0,590);
        link.setLocation(45,570);
		lbl1.setSize(800, 30);
		lbl2.setSize(800, 30);
		lbl3.setSize(800, 30);
		lbl4.setSize(800, 30);ackcont.setSize(800,30);
		lbl5.setSize(2000, 30);link.setSize(200,30);
	    contact.setSize(800,30);//present.setSize(800,30);
	    dept.setSize(800,30);name.setSize(800,30);insti.setSize(800,30);email.setSize(800,30);
		  //JLabel background=new JLabel(new ImageIcon("C:\\Users\\Soumya\\Desktop\\logo.png"));
		    //add(background);//adding images by Soumya
		jMnItGUI.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inputMenuItem_actionPerformed(e);
			}

			private void inputMenuItem_actionPerformed(ActionEvent e) {
				SectionInputDialog  sectioninputdialog = GlobalVar.getSectionInputDialog();
			sectioninputdialog.setVisible(true);
			
			}
		});
		
		jMnItSelect.setText("Select");
		jMnItSelect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMnItSelect_actionPerformed(e);
			}
		});
		jMnItRun.setText("Run");
		jMnItRun.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runMenuActionPerformed(e);
			}
		});
		//santhosh - capacity added
		jMnItCapacity.setText("Capacity");
		jMnItCapacity.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				capacityMenuActionPerformed(e);
			}
		});
		
		jMnItRawCapacity.setText("Raw Capacity");
		jMnItRawCapacity.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rawCapacityMenuActionPerformed(e);
			}
		});
		jMnItExit.setText("Exit");
		jMnItExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});
		this.setJMenuBar(mainMenuBar);
		this.setTitle("Simulator developed by IIT Bombay");
		jMenu2.setText("Report");
		jMnRunTime.setText("Running Time");
		jMnRunTime.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMnRunTime_actionPerformed(e);
			}
		});
		jMenu3.setText("Options");
		jChkMnuDebug.setText("Debug");
		jChkMnuDebug.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jChkMnuDebug_actionPerformed(e);
			}
		});
		mainMenuBar.add(jMenu1);
		mainMenuBar.add(jMenu2);
		mainMenuBar.add(jMenu3);
		
		jMenu1.add(jMnItGUI);
		jMenu1.add(jMnItSelect);
		jMenu1.add(jMnItRun);
		jMenu1.add(jMnItCapacity);
		jMenu1.add(jMnItRawCapacity);
		jMenu1.addSeparator();
		jMenu1.add(jMnItExit);
		jMenu2.add(jMnRunTime);
		jMenu3.add(jChkMnuDebug);
	}

	/**
	 * @param e
	 */
	void runMenuActionPerformed(ActionEvent e) {
		System.out.println("Boom");
		GlobalVar.capacitySelected=false;
		GlobalVar.rawCapacitySelected=false;
		SimulationStart simulationStart = new SimulationStart();
		System.out.println("sneha Simulation start called");
		simulationStart.start();
		System.out.println("sneha Simulation start returned");
	}
	//santhosh
	void capacityMenuActionPerformed(ActionEvent e) {
		GlobalVar.rawCapacitySelected=false;
		GlobalVar.capacitySelected=true;
		FreightTrainInputDialog freightTrainInputDialog= new FreightTrainInputDialog();
		freightTrainInputDialog.setVisible(true);
	}
	void rawCapacityMenuActionPerformed(ActionEvent e) {
		GlobalVar.capacitySelected=false;
		GlobalVar.rawCapacitySelected=true;
		FreightTrainInputDialog freightTrainInputDialog= new FreightTrainInputDialog();
		
		freightTrainInputDialog.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String args[]) {

		@SuppressWarnings("unused")
//		Debug debug = new Debug();
		StartUpScreen startUp = new StartUpScreen();
		startUp.setVisible(true);
		startUp.setBounds(10, 10, 200, 200);
		
		MainInterface mi = new MainInterface();
		mi.setBounds(10, 10, 700, 700);
		mi.setVisible(true);
		startUp.dispose();
	}

	/**
	 * @param e
	 */
	void this_windowClosing(WindowEvent e) {
		System.exit(0);
	}

	/**
	 * @param e
	 */
	void jMnItSelect_actionPerformed(ActionEvent e) {
		Debug.print("Selecte selected");
		//santhosh
		SimulationInstance simulationInstance=new SimulationInstance();
		GlobalVar.simulationInstance=simulationInstance;
		SelectFiles selectFiles = new SelectFiles(simulationInstance);
		//santhosh
		//SelectFiles selectFiles = new SelectFiles(null);//SelectFiles.getInterface();
		selectFiles.setBounds(100, 100, 500, 700);
		selectFiles.setVisible(true);
	}

	/**
	 * @param e
	 */
	@SuppressWarnings( { "unused" })
	void jMnRunTime_actionPerformed(ActionEvent e) {
		TrainRunTime trnFrame = new TrainRunTime();
	}

	/**
	 * @param e
	 */
	void jChkMnuDebug_actionPerformed(ActionEvent e) {
		if (jChkMnuDebug.getState() == true) {
			Debug.debug = true;
		} else {
			Debug.debug = false;
		}
	}
}
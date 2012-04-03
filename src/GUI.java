
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.zip.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.event.*;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.io.*;
import java.beans.*;

import com.toedter.calendar.*;

public class GUI extends JFrame implements FocusListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private Logic lg;
	
	private ExportImportButtonsListener exportImportButtonsListener;
	private JFileChooser fileChooser; 
	private File currEventFileDirectory;
	private JTabbedPane jtp;
	
	//Menu Bar objects
	private JMenu mnFile;
	private JMenuBar menuBar;
	private JMenuItem mntmCreateNewEvent;
	private JMenuItem mntmLoadEvent;
	private JMenuItem mntmSave;
	private JMenuItem mntmSaveAs;
	
	//GUI0 objects
	private JLabel lbl0_Step1;
	private JLabel lbl0_Step2;
	private JLabel lbl0_Step3;
	private JLabel lbl0_Step4;
	private JLabel lbl0_Step5;
	private JLabel lbl0_Step6;
	private JTextPane textPane0_EventName;
	private JButton btn0_Load;
	private JButton btn0_New;
	
	//GUI1 objects
	private JPanel panel1;
	private JTextField textField1_EventName;
	private JTextField textField1_budget;
	private JComboBox<String> comboBox1_EventType;
	private JDateChooser dateChooser1_StartDate;
	private JSpinner spinner1_StartTimeH;
	private JSpinner spinner1_StartTimeM;
	private JDateChooser dateChooser1_EndDate;
	private JSpinner spinner1_EndTimeH;
	private JSpinner spinner1_EndTimeM;
	private JComboBox<String> comboBox1_MealDates;
	private JTextArea textArea1_EventDescription;
	private JRadioButton rdbtn1_Lunch;
	private JRadioButton rdbtn1_Dinner;
	
	//GUI2 objects
	private JPanel panel2;
	private JButton btn2_Export;
	private JButton btn2_Load;
	private JButton btn2_Next;
	//private JButton btn2_Search;
	private JButton btn2_AddGuest;
	private JButton btn2_DeleteGuest;
	private JTable table2;
	private JScrollPane scrollPane2;
	private Vector<String> guestCols;
	//private JTextField textField2_Search;
	private JCheckBox chckbx2_GuestListFinalised;
	
	//GUI3 objects
	private JPanel panel3;
	private JButton btn3_AddEntry;
	private JButton btn3_DeleteEntry;
	private JButton btn3_Export;
	private JButton btn3_Next;
	private JTable table3;
	private JScrollPane scrollPane3;
	private Vector<String> programmeCols;
	private JCheckBox chckbx3_ProgrammeScheduleFinalised;
	private JComboBox<String> comboBox_Date;
	
	//GUI4 objects
	private JButton btn4_Suggest;
	private JButton btn4_Next;
	private JTextPane textPane4_HotelDetails;
	private JList<String> list4;
	private JSlider slider4;
	private JTextPane textPane4_Guests;
	private JTextPane textPane4_Budget;
	private JCheckBox chckbx4_3Star;
	private JCheckBox chckbx4_4Star;
	private JCheckBox chckbx4_5Star;
	private final ButtonGroup buttonGroup1 = new ButtonGroup();
	
	//GUI5 objects
	private JPanel panel5;
	private JRadioButton rdbtn5_Random;
	private JRadioButton rdbtn5_ByGroup;
	private JComboBox<String> comboBox5_arrangerGroupType;
	private JButton btn5_Generate;
	private JTable table5;
	private JScrollPane scrollPane5;
	private Vector<String> tableCols;
	private JButton btn5_Next;
	private ProgressMonitor progressMonitor5;
	
	//GUI6 objects
	private JPanel panel6;
	private JButton btn6_AddExpense;
	private JButton btn6_DeleteExpense;
	private JTextPane textPane6_TotalBudget;
	private JTextPane textPane6_Spent;
	private JTextPane textPane6_Remaining;
	private JTable table6;
	private JScrollPane scrollPane6;
	private Vector<String> expensesCols;
	private JCheckBox chckbx6_ExpensesFinalised;
	private JTextPane textPane6_CostPerHead;
	private JButton btn6_Export;
	private JButton btn6_ViewSummary;
	private final ButtonGroup buttonGroup5 = new ButtonGroup();
	
	//GUI7 objects
	private JTextPane textPane7_Summary;

	public GUI(final Logic lg) throws IOException {
		this.lg = lg;
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int X = (screen.width / 2) - 400; // Center horizontally.
		int Y = (screen.height / 2) - 300; // Center vertically.
		setBounds(X, Y, 800, 600);
    	
        setTitle("Manage It Right! v0.2");
        jtp = new JTabbedPane();
        jtp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtp.setTabPlacement(JTabbedPane.LEFT);
        getContentPane().add(jtp, BorderLayout.CENTER);
              
        try {
     		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
     	} catch (Exception e) {
     		System.out.println("Unable to load Windows look and feel");
     	}
        
        fileChooser = new JFileChooser(".");
        exportImportButtonsListener = new ExportImportButtonsListener();
        
        // when closing the program, prompt user to save if there are unsaved changes.
        addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent winEvt) {
		    	if(!lg.getSavedStatus()){
        			int choice = JOptionPane.showConfirmDialog(new JFrame(), "There are unsaved changes. Do you want to save the current event?");
        			switch(choice){
        			case 0: // yes: save event & close program.
        				if(currEventFileDirectory == null){
		    				fileChooser = new JFileChooser(".");
		    				fileChooser.setFileFilter(new mirFilter());
		    				
		    				int fileChooserChoice = fileChooser.showSaveDialog(new JFrame());
		    				
		    				if(fileChooserChoice == JFileChooser.APPROVE_OPTION){
		    					File file = fileChooser.getSelectedFile();
		        				if(file == null)
		        					return;
		        				lg.saveEvent(file);
		    				}
        				}
        				else
        					lg.saveEvent(currEventFileDirectory);
        				
        				if(lg.getSavedStatus()){
        					setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        					dispose();
        					System.exit(0);
        				}
        				break;
        				
        			case 1: // no: close program.
        				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        				dispose();
        				System.exit(0);
        				break;
        				
        			case 2: // cancel: do not close program.
        				break;
        			}
        		}
		    	
		    	else {
		    		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    				dispose();
    				System.exit(0);
		    	}
		    }
		});
        
        /*
         * Drawing of individual tabs
         */
    	GUI0();
    	GUI1();
    	GUI2();
    	GUI3();
    	GUI4();
    	
    	GUI5();
    	GUI6();
    	GUI7();
        
    	/*
    	 * Menu Bar
    	 */
    	menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        mnFile = new JMenu("File");
        mnFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuBar.add(mnFile);
        mnFile.setMnemonic(KeyEvent.VK_F);
        
        mntmCreateNewEvent = new JMenuItem("Create New Event");
        mnFile.add(mntmCreateNewEvent);
        mntmCreateNewEvent.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		if(!lg.getSavedStatus()){
        			int choice = JOptionPane.showConfirmDialog(new JFrame(), "Do you want to save the current event?");
        			switch(choice){
        			case 0:
        				if(currEventFileDirectory == null){
		    				fileChooser = new JFileChooser(".");
		    				fileChooser.setFileFilter(new mirFilter());
		    				
		    				int fileChooserChoice = fileChooser.showSaveDialog(new JFrame());
		    				
		    				if(fileChooserChoice == JFileChooser.APPROVE_OPTION){
		    					File file = fileChooser.getSelectedFile();
		        				if(file == null)
		        					return;
		        				lg.saveEvent(file);
		    				}
        				}
        				else
        					lg.saveEvent(currEventFileDirectory);
        				lg.createNewEvent();
        				updateAll();
        				jtp.setSelectedIndex(1);
        				break;
        			case 1:
        				currEventFileDirectory = null;
        				lg.createNewEvent();
        				updateAll();
        				jtp.setSelectedIndex(1);
        				break;
        			case 2:
        				break;
        			}
        		}
        		else jtp.setSelectedIndex(1); 	//TODO if there already is an existing event, we need to clear all the fields
        	}
        });
        
        
        mntmLoadEvent = new JMenuItem("Load Event");
        mnFile.add(mntmLoadEvent);
        mntmLoadEvent.addActionListener(exportImportButtonsListener);
        
        mntmSave = new JMenuItem("Save");
        mnFile.add(mntmSave);
        mntmSave.addActionListener(exportImportButtonsListener);
        
        mntmSaveAs = new JMenuItem("Save As...");
        mnFile.add(mntmSaveAs);
        mntmSaveAs.addActionListener(exportImportButtonsListener);
        
        /*
        JSeparator separator = new JSeparator();
        mnFile.add(separator);
        
        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		System.exit(0);
        	}
        });
        mnFile.add(mntmExit);
        */
        
        jtp.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				Object obj = e.getSource();
				JTabbedPane pane = (JTabbedPane)obj;
				switch(pane.getSelectedIndex()){
				case 0:
					updateStep0();
					break;
				case 1:
					updateStep1();
					break;
				case 2:
					updateStep2();
					break;
				case 3:
					break;
				case 4:
					updateStep4();				
					break;
				case 5:
					updateStep5();
					break;
				case 6:
					break;
				case 7:
					updateStep7();
					break;
				default:
					break;
				}
				
			}
        	
        });
    }
	
	/*
	 * Draw everything in Overview tab
	 */
	private void GUI0() throws IOException{
		JPanel panel0 = new JPanel();
		jtp.addTab("<html><body marginwidth=15 marginheight=15><b>Overview</b></body></html>", null, panel0, null);
		
		JLabel lbl0_Overview = new JLabel("Overview");
		lbl0_Overview.setBounds(10, 11, 76, 30);
		lbl0_Overview.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		ImageIcon myPicture = new ImageIcon(this.getClass().getResource("MIR_logo.jpg"));
		JLabel lbl0_Logo = new JLabel(myPicture);
        lbl0_Logo.setBounds(351, 52, 300, 115);
        panel0.add(lbl0_Logo);
		
		
		 btn0_New = new JButton("New");        
		
		 btn0_New.setBounds(460, 490, 80, 30);
		 btn0_New.setFont(new Font("Tahoma", Font.BOLD, 12));
		 btn0_New.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseClicked(MouseEvent arg0) {
		 		if(!lg.getSavedStatus()){
        			int choice = JOptionPane.showConfirmDialog(new JFrame(), "Do you want to save the current event?");
        			switch(choice){
        			case 0:
        				if(currEventFileDirectory == null){
		    				fileChooser = new JFileChooser(".");
		    				fileChooser.setFileFilter(new mirFilter());
		    				
		    				int fileChooserChoice = fileChooser.showSaveDialog(new JFrame());
		    				
		    				if(fileChooserChoice == JFileChooser.APPROVE_OPTION){
		    					File file = fileChooser.getSelectedFile();
		        				if(file == null)
		        					return;
		        				lg.saveEvent(file);
		    				}
        				}
        				else
        					lg.saveEvent(currEventFileDirectory);
        				lg.createNewEvent();
        				updateAll();
        				jtp.setSelectedIndex(1);
        				break;
        			case 1:
        				currEventFileDirectory = null;
        				lg.createNewEvent();
        				updateAll();
        				jtp.setSelectedIndex(1);
        				break;
        			case 2:
        				break;
        			}
        		}
        		else jtp.setSelectedIndex(1);		//TODO if there already is an existing event, we need to clear all the fields
		 	}
		 });
		 
		 btn0_Load = new JButton("Load");
		 btn0_Load.setBounds(560, 490, 80, 30);
		 btn0_Load.setFont(new Font("Tahoma", Font.BOLD, 12));
		 btn0_Load.addActionListener(exportImportButtonsListener);
		 
		 panel0.setLayout(null);
		 panel0.add(lbl0_Overview);
        
        textPane0_EventName = new JTextPane();
        textPane0_EventName.setEditable(false);
        textPane0_EventName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        textPane0_EventName.setBounds(136, 11, 504, 30);
        panel0.add(textPane0_EventName);
        
        lbl0_Step1 = new JLabel();
        lbl0_Step1.setText("  Event Details are empty.");
        lbl0_Step1.setOpaque(true);
        lbl0_Step1.setBackground(Color.PINK);
        lbl0_Step1.setForeground(Color.BLACK);
        lbl0_Step1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl0_Step1.setBounds(10, 66, 310, 30);
        panel0.add(lbl0_Step1);
        
        lbl0_Step2 = new JLabel("  Guest List is empty.");
        lbl0_Step2.setOpaque(true);
        lbl0_Step2.setBackground(Color.PINK);
        lbl0_Step2.setForeground(Color.BLACK);
        lbl0_Step2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl0_Step2.setBounds(10, 132, 310, 30);
        panel0.add(lbl0_Step2);
        
        lbl0_Step3 = new JLabel("  Programme Schedule is empty.");
        lbl0_Step3.setOpaque(true);
        lbl0_Step3.setBackground(Color.PINK);
        lbl0_Step3.setForeground(Color.BLACK);
        lbl0_Step3.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl0_Step3.setBounds(10, 198, 310, 30);
        panel0.add(lbl0_Step3);
        
        lbl0_Step4 = new JLabel("  Location not chosen.");
        lbl0_Step4.setOpaque(true);
        lbl0_Step4.setBackground(Color.PINK);
        lbl0_Step4.setForeground(Color.BLACK);
        lbl0_Step4.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl0_Step4.setBounds(10, 264, 310, 30);
        panel0.add(lbl0_Step4);
        
        lbl0_Step5 = new JLabel("  Guests are not assigned to tables.");
        lbl0_Step5.setOpaque(true);
        lbl0_Step5.setBackground(Color.PINK);
        lbl0_Step5.setForeground(Color.BLACK);
        lbl0_Step5.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl0_Step5.setBounds(10, 332, 310, 30);
        panel0.add(lbl0_Step5);
        
        lbl0_Step6 = new JLabel("  Expenses List is empty.");
        lbl0_Step6.setOpaque(true);
        lbl0_Step6.setBackground(Color.PINK);
        lbl0_Step6.setForeground(Color.BLACK);
        lbl0_Step6.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl0_Step6.setBounds(10, 400, 310, 30);
        panel0.add(lbl0_Step6);
        
        panel0.add(btn0_New);
        panel0.add(btn0_Load);   
	}
	
	/*
	 * Draws everything in Step 1
	 */
	private void GUI1(){
		
		panel1 = new JPanel();
        jtp.addTab("<html><body marginwidth=15 marginheight=15><b>Step 1:</b><br>Event Details</body></html>", null, panel1, "Manage the event details.");
        panel1.setLayout(null);
        
        JLabel lbl1_EventDetails = new JLabel("Event Details");
        lbl1_EventDetails.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl1_EventDetails.setBounds(10, 10, 126, 30);
        panel1.add(lbl1_EventDetails);
        
        JLabel lbl1_EventType = new JLabel("Event Type:");
        lbl1_EventType.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_EventType.setBounds(10, 59, 100, 14);
        panel1.add(lbl1_EventType);
                  
        comboBox1_EventType = new JComboBox<String>();
        comboBox1_EventType.setBounds(120, 56, 195, 20);
        panel1.add(comboBox1_EventType);
        comboBox1_EventType.addItem("Select Event Type...");
        comboBox1_EventType.addItem("Anniversary");
        comboBox1_EventType.addItem("Award Ceremony");
        comboBox1_EventType.addItem("Birthday Party");
        comboBox1_EventType.addItem("Dinner and Dance");
        comboBox1_EventType.addItem("Festive Party");
        comboBox1_EventType.addItem("Seminar");
        comboBox1_EventType.addItem("Social Event");
        comboBox1_EventType.addItem("Talk / Speech");
        comboBox1_EventType.addItem("Wedding");
        comboBox1_EventType.addItem("Workshop");
        comboBox1_EventType.addActionListener(new ComboBox1_EventType_Listener());
        
        JLabel lbl1_EventName = new JLabel("Event Name:");
        lbl1_EventName.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_EventName.setBounds(10, 100, 100, 14);
        panel1.add(lbl1_EventName);
        
        textField1_EventName = new JTextField();
        textField1_EventName.setBounds(120, 97, 435, 20);
        panel1.add(textField1_EventName);
        textField1_EventName.setColumns(10);
        textField1_EventName.addFocusListener(this);
        
        /*
        comboBox1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {				
				String str = (String)comboBox1.getSelectedItem();
				textField1_EventName.setText(str);	// For testing purposes to display chosen event type 
			}
        });
       */
        JLabel lbl1_EventDate = new JLabel("Start Date:");
        lbl1_EventDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_EventDate.setBounds(10, 142, 73, 14);
        panel1.add(lbl1_EventDate);
        
        dateChooser1_StartDate = new JDateChooser();    
        dateChooser1_StartDate.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
        		if (dateChooser1_StartDate.getDate()!= null) {
        			Date StartDate = dateChooser1_StartDate.getDate();
//        	        String dateString = String.format("%1$td/%1$tm/%1$tY", StartDate); 
        			lg.setEventStartDate(StartDate);       
        			
        			if (lg.checkDate() == false) {
        				JOptionPane.showMessageDialog(new JFrame(), "Start Date cannot " +
        						"be after End Date!", "Start Date Error", JOptionPane.ERROR_MESSAGE );
        				dateChooser1_StartDate.setDate(dateChooser1_EndDate.getDate());
        			}
        			else {
        				if (lg.getEventEndDate() != null) {
            					lg.setDateList(lg.getEventStartDate(), lg.getEventEndDate());
            					lg.checkProgrammeDates();
            					updateStep3();
            			}
        			}
       			
        		}
        		
        		if (!lg.getDateList().isEmpty()) {
        			comboBox1_MealDates.removeAllItems();
                	Iterator<String> itr = lg.getDateList().iterator();
                	while(itr.hasNext()) {
                		comboBox1_MealDates.addItem(itr.next());
                	}
                }
        		
        		enableMealRaidoButtons();
        	}
        });
 
        
        dateChooser1_StartDate.setBounds(120, 142, 117, 20);
        panel1.add(dateChooser1_StartDate);
     
        JLabel lbl1_StartTime = new JLabel("Start Time:");
        lbl1_StartTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_StartTime.setBounds(264, 142, 68, 14);
        panel1.add(lbl1_StartTime);
        
        JLabel lbl1_StartTimeH = new JLabel("H");
        lbl1_StartTimeH.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_StartTimeH.setBounds(342, 142, 14, 14);
        panel1.add(lbl1_StartTimeH);
        
        spinner1_StartTimeH = new JSpinner();
        spinner1_StartTimeH.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        spinner1_StartTimeH.setBounds(357, 140, 40, 20);
        panel1.add(spinner1_StartTimeH);
        spinner1_StartTimeH.addChangeListener(new SpinnerListeners());
        
        
        JLabel lbl1_StartTimeM = new JLabel("M");
        lbl1_StartTimeM.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_StartTimeM.setBounds(401, 143, 14, 14);
        panel1.add(lbl1_StartTimeM);
        
        spinner1_StartTimeM = new JSpinner();
        spinner1_StartTimeM.setModel(new SpinnerNumberModel(0, 0, 59, 1));
        spinner1_StartTimeM.setBounds(417, 140, 40, 20);
        panel1.add(spinner1_StartTimeM);
        spinner1_StartTimeM.addChangeListener(new SpinnerListeners());
        
        JLabel lbl1_EndDate = new JLabel("End Date:");
        lbl1_EndDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_EndDate.setBounds(10, 182, 60, 14);
        panel1.add(lbl1_EndDate);
        
        dateChooser1_EndDate = new JDateChooser();
        dateChooser1_EndDate.addPropertyChangeListener(new PropertyChangeListener() {
        	public void propertyChange(PropertyChangeEvent evt) {
        		if (dateChooser1_EndDate.getDate()!= null) {
        			Date EndDate = dateChooser1_EndDate.getDate();
//        	        String dateString = String.format("%1$td/%1$tm/%1$tY", StartDate); 
        			        			
        			lg.setEventEndDate(EndDate);        		 	
					         			
        			if (lg.checkDate() == false) {
        				JOptionPane.showMessageDialog(new JFrame(), "End Date cannot " +
        						"be before Start Date!", "End Date Error", JOptionPane.ERROR_MESSAGE );
        				dateChooser1_EndDate.setDate(dateChooser1_StartDate.getDate());
        			}
        			
        			else {
        				if (lg.getEventStartDate() != null) {       			
        					lg.setDateList(lg.getEventStartDate(), lg.getEventEndDate());  
        					lg.checkProgrammeDates();
        					updateStep3();
        				}
        			}
        		}
        		
        		if (!lg.getDateList().isEmpty()) {
        			comboBox1_MealDates.removeAllItems();
                	Iterator<String> itr = lg.getDateList().iterator();
                	while(itr.hasNext()) {
                		comboBox1_MealDates.addItem(itr.next());
                	}
                }
        		
        		enableMealRaidoButtons();
        	}
        });
        dateChooser1_EndDate.setBounds(120, 182, 117, 20);
        panel1.add(dateChooser1_EndDate);

        
        
        JLabel lbl1_EndTime = new JLabel("End Time:");
        lbl1_EndTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_EndTime.setBounds(264, 183, 60, 14);
        panel1.add(lbl1_EndTime);
        
        JLabel label1_EndTimeH = new JLabel("H");
        label1_EndTimeH.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label1_EndTimeH.setBounds(342, 184, 14, 14);
        panel1.add(label1_EndTimeH);
        
        spinner1_EndTimeH = new JSpinner();
        spinner1_EndTimeH.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        spinner1_EndTimeH.setBounds(357, 182, 40, 20);
        panel1.add(spinner1_EndTimeH);
        spinner1_EndTimeH.addChangeListener(new SpinnerListeners());
        
        JLabel label1_EndTimeM = new JLabel("M");
        label1_EndTimeM.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label1_EndTimeM.setBounds(401, 185, 14, 14);
        panel1.add(label1_EndTimeM);
        
        spinner1_EndTimeM = new JSpinner();
        spinner1_EndTimeM.setModel(new SpinnerNumberModel(0, 0, 59, 1));
        spinner1_EndTimeM.setBounds(417, 182, 40, 20);
        panel1.add(spinner1_EndTimeM);
        spinner1_EndTimeM.addChangeListener(new SpinnerListeners());
        
        JLabel lbl1_EventDescription = new JLabel("Event Description:");
        lbl1_EventDescription.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_EventDescription.setBounds(10, 264, 100, 14);
        panel1.add(lbl1_EventDescription);
        
        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBounds(120, 260, 435, 171);
        panel1.add(scrollPane1);
        
        textArea1_EventDescription = new JTextArea();
        scrollPane1.setViewportView(textArea1_EventDescription);
        textArea1_EventDescription.setDragEnabled(true);
        textArea1_EventDescription.setLineWrap(true);
        textArea1_EventDescription.setWrapStyleWord(true);
        textArea1_EventDescription.addFocusListener(this);
        
        JLabel lbl1_Budget = new JLabel("Budget:          $");
        lbl1_Budget.setFont(new Font("Tahoma", Font.BOLD, 12));
        lbl1_Budget.setBounds(10, 451, 100, 14);
        panel1.add(lbl1_Budget);
        
        textField1_budget = new JTextField();
        textField1_budget.setToolTipText("");
        textField1_budget.setColumns(10);
        textField1_budget.setBounds(120, 449, 117, 20);
        panel1.add(textField1_budget);
        textField1_budget.addFocusListener(this);
        
        JButton btn1_Next = new JButton("Next");
        btn1_Next.addMouseListener(this);
        btn1_Next.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn1_Next.setBounds(560, 490, 80, 30);
        panel1.add(btn1_Next);
        
        JLabel lbl1_MealDate = new JLabel("Meal Date:");
        lbl1_MealDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_MealDate.setBounds(10, 224, 70, 14);
        panel1.add(lbl1_MealDate);
        
        comboBox1_MealDates = new JComboBox<String>();
        comboBox1_MealDates.setBounds(120, 222, 117, 20);
        panel1.add(comboBox1_MealDates);
        comboBox1_MealDates.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
            	enableMealRaidoButtons();
            	lg.setMealDateSelected(comboBox1_MealDates.getSelectedIndex());
            	System.out.println(lg.getMealDateSelected()); // for testing
            }
        });
        
        JLabel lbl1_MealType = new JLabel("Meal Type:");
        lbl1_MealType.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_MealType.setBounds(264, 224, 68, 14);
        panel1.add(lbl1_MealType);
        
        rdbtn1_Lunch = new JRadioButton("Lunch");
        rdbtn1_Lunch.setEnabled(false);
        lg.setMealRadioButtons(0, false);
        rdbtn1_Lunch.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		lg.setMealType(0);
        	}
        });
        buttonGroup1.add(rdbtn1_Lunch);
        rdbtn1_Lunch.setBounds(342, 221, 55, 23);
        panel1.add(rdbtn1_Lunch);
        
        rdbtn1_Dinner = new JRadioButton("Dinner");
        rdbtn1_Dinner.setEnabled(false);
        lg.setMealRadioButtons(1, false);
        rdbtn1_Dinner.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		lg.setMealType(1);
        	}
        });
        buttonGroup1.add(rdbtn1_Dinner);
        rdbtn1_Dinner.setBounds(417, 221, 60, 23);
        panel1.add(rdbtn1_Dinner);
	}
	
	public void enableMealRaidoButtons() {
		// 0 event days
		if(lg.getDateList().isEmpty()) {
			buttonGroup1.clearSelection();
			lg.setMealType(-1);
			rdbtn1_Lunch.setEnabled(false);
			lg.setMealRadioButtons(0, false);
			rdbtn1_Dinner.setEnabled(false);
			lg.setMealRadioButtons(1, false);
		}
		// 1 day
		else if(lg.getDateList().size() == 1) {
			// checks if the event time frame falls within Lunch or Dinner timings.
			// Lunch: 1100 - 1459 hrs
			// Dinner:  1900 - 2259 hrs
			if((lg.getStartTimeH() <= 14) && (11 <= lg.getEndTimeH())) {
				rdbtn1_Lunch.setEnabled(true);
				lg.setMealRadioButtons(0, true);
			}
			else {
				rdbtn1_Lunch.setEnabled(false);
				lg.setMealRadioButtons(0, false);
				if (lg.getMealType() == 0) {
					buttonGroup1.clearSelection();
					lg.setMealType(-1);
				}
			}

			if((lg.getStartTimeH() <= 22) && (19 <= lg.getEndTimeH())) {
				rdbtn1_Dinner.setEnabled(true);
				lg.setMealRadioButtons(1, true);
			}
			else {
				rdbtn1_Dinner.setEnabled(false);
				lg.setMealRadioButtons(1, false);
				if (lg.getMealType() == 1) {
					buttonGroup1.clearSelection();
					lg.setMealType(-1);
				}
			} // end of checking meal times
		}
		// more than 1 day
		else {
			// first day selected
			if(comboBox1_MealDates.getSelectedIndex() == 0) {
				if(lg.getStartTimeH() <= 14) {
					rdbtn1_Lunch.setEnabled(true);
					lg.setMealRadioButtons(0, true);
				}
				else {
					rdbtn1_Lunch.setEnabled(false);
					lg.setMealRadioButtons(0, false);
					if (lg.getMealType() == 0) {
						buttonGroup1.clearSelection();
						lg.setMealType(-1);
					}
				}

				if(lg.getStartTimeH() <= 22) {
					rdbtn1_Dinner.setEnabled(true);
					lg.setMealRadioButtons(1, true);
				}
				else {
					rdbtn1_Dinner.setEnabled(false);
					lg.setMealRadioButtons(1, false);
					if (lg.getMealType() == 1) {
						buttonGroup1.clearSelection();
						lg.setMealType(-1);
					}
				}
			}
			// last day selected
			else if(comboBox1_MealDates.getSelectedIndex() == lg.getDateList().size() - 1){
				if(11 <= lg.getEndTimeH()) {
					rdbtn1_Lunch.setEnabled(true);
					lg.setMealRadioButtons(0, true);
				}
				else {
					rdbtn1_Lunch.setEnabled(false);
					lg.setMealRadioButtons(0, false);
					if (lg.getMealType() == 0) {
						buttonGroup1.clearSelection();
						lg.setMealType(-1);
					}
				}

				if(19 <= lg.getEndTimeH()) {
					rdbtn1_Dinner.setEnabled(true);
					lg.setMealRadioButtons(1, true);
				}
				else {
					rdbtn1_Dinner.setEnabled(false);
					lg.setMealRadioButtons(1, false);
					if (lg.getMealType() == 1) {
						buttonGroup1.clearSelection();
						lg.setMealType(-1);
					}
				}
			}
			// in between 1st and last day selected.
			else {
				buttonGroup1.clearSelection();
				lg.setMealType(-1);
				rdbtn1_Lunch.setEnabled(true);
				lg.setMealRadioButtons(0, true);
				rdbtn1_Dinner.setEnabled(true);
				lg.setMealRadioButtons(1, true);
			}
		}
	}
	
	/*
	 * Drawing of everything in Step 2
	 */
	private void GUI2(){
			panel2 = new JPanel();
	        jtp.addTab("<html><body marginwidth=15 marginheight=15><b>Step 2:</b><br>Guest List&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</body></html>", null, panel2, "Manage the list of guests attending the event.");
	        panel2.setLayout(null);
	        
	        JLabel lbl2_GuestList = new JLabel("Guest List");
	        lbl2_GuestList.setFont(new Font("Tahoma", Font.BOLD, 16));
	        lbl2_GuestList.setBounds(10, 10, 81, 30);
	        panel2.add(lbl2_GuestList);
	        
	        btn2_AddGuest = new JButton("Add Guest");
	        btn2_AddGuest.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent arg0) {
	        	}
	        });
	        
	        btn2_AddGuest.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        btn2_AddGuest.setBounds(10, 56, 110, 25);
	        panel2.add(btn2_AddGuest);
	        
	        btn2_DeleteGuest = new JButton("Delete Guest");	        
	        btn2_DeleteGuest.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent arg0) {
	        	}
	        });
	        btn2_DeleteGuest.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        btn2_DeleteGuest.setBounds(140, 56, 110, 25);
	        panel2.add(btn2_DeleteGuest);
	        
	       
	        /*
	        textField2_Search = new JTextField();
	        textField2_Search.setToolTipText("Search Guest List");
	        textField2_Search.setBounds(351, 60, 175, 20);
	        panel2.add(textField2_Search);
	        textField2_Search.setColumns(10);
	        
	        btn2_Search = new JButton("Search");
	        btn2_Search.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        btn2_Search.setBounds(536, 58, 77, 23);
	        panel2.add(btn2_Search);
	        */
	        
	        guestCols = new Vector<String>();
	        guestCols.add("Name");
	        guestCols.add("Gender");
	        //guestCols.add("Age");
	        guestCols.add("Group");
	        guestCols.add("Email");
	        guestCols.add("Contact Number");
	        guestCols.add("Description");
	        
	        createTable2(new Vector<Vector<String>>(), guestCols);	        
	        
	        chckbx2_GuestListFinalised = new JCheckBox("Guest List Finalised");
	        chckbx2_GuestListFinalised.setBounds(10, 463, 140, 23);
	        panel2.add(chckbx2_GuestListFinalised);
	        chckbx2_GuestListFinalised.setEnabled(false);
	        chckbx2_GuestListFinalised.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					lg.setGuestListFinalised(chckbx2_GuestListFinalised.isSelected());
				}
	        });
	        
	        btn2_Export = new JButton("Export");
	        btn2_Export.setFont(new Font("Tahoma", Font.BOLD, 12));
	        btn2_Export.setBounds(460, 490, 80, 30);
	        panel2.add(btn2_Export);
	        btn2_Export.addActionListener(exportImportButtonsListener);
	        
	        btn2_Load = new JButton("Load");
	        btn2_Load.setFont(new Font("Tahoma", Font.BOLD, 12));
	        btn2_Load.setBounds(11, 490, 80, 30);
	        panel2.add(btn2_Load);
	        btn2_Load.addActionListener(exportImportButtonsListener);
	        
	        btn2_Next = new JButton("Next");
	        btn2_Next.addMouseListener(this);
	        btn2_Next.setFont(new Font("Tahoma", Font.BOLD, 12));
	        btn2_Next.setBounds(560, 490, 80, 30);
	        panel2.add(btn2_Next);
	}
	
	
	/*
	 * Drawing of everything in Step 3
	 */
	private void GUI3(){
		panel3 = new JPanel();
        jtp.addTab("<html><body marginwidth=15 marginheight=15><b>Step 3:</b><br>Programme&nbsp;</body></html>", null, panel3, "Manage the flow of events.");
        panel3.setLayout(null);
        
        JLabel lbl3_ProgrammeSchedule = new JLabel("Programme Schedule");
        lbl3_ProgrammeSchedule.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl3_ProgrammeSchedule.setBounds(10, 10, 191, 30);
        panel3.add(lbl3_ProgrammeSchedule);
        
        btn3_AddEntry = new JButton("Add Entry");
        btn3_AddEntry.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btn3_AddEntry.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        	}
        });
        btn3_AddEntry.setBounds(10, 56, 110, 25);
        panel3.add(btn3_AddEntry);
        
        btn3_DeleteEntry = new JButton("Delete Entry");
        btn3_DeleteEntry.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btn3_DeleteEntry.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        	}
        });
        btn3_DeleteEntry.setBounds(140, 56, 110, 25);
        panel3.add(btn3_DeleteEntry);
        
        programmeCols = new Vector<String>();
        programmeCols.add("Date");
        programmeCols.add("Start Time");
        programmeCols.add("End Time");
        programmeCols.add("Programme");
        programmeCols.add("In-Charge");
        
        createTable3(new Vector<Vector<String>>(), programmeCols);
        
        chckbx3_ProgrammeScheduleFinalised = new JCheckBox("Programme Schedule Finalised");
        chckbx3_ProgrammeScheduleFinalised.setBounds(10, 463, 217, 23);
        panel3.add(chckbx3_ProgrammeScheduleFinalised);
        chckbx3_ProgrammeScheduleFinalised.setEnabled(false);
        chckbx3_ProgrammeScheduleFinalised.addActionListener(new ActionListener(){
			@Override	//change here
			public void actionPerformed(ActionEvent e) {
				lg.setProgrammeScheduleFinalised(chckbx3_ProgrammeScheduleFinalised.isSelected());
			}
        });
      
        btn3_Export = new JButton("Export");
        btn3_Export.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn3_Export.setBounds(460, 490, 80, 30);
        panel3.add(btn3_Export);
        btn3_Export.addActionListener(exportImportButtonsListener);
        
        
        btn3_Next = new JButton("Next");
        btn3_Next.addMouseListener(this);
        btn3_Next.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn3_Next.setBounds(560, 490, 80, 30);
        panel3.add(btn3_Next);
	}
	
	/*
	 * Drawing of everything in Step 4
	 */
	private void GUI4(){
		JPanel panel4 = new JPanel();
        jtp.addTab("<html><body marginwidth=15 marginheight=8><b>Step 4:</b><br>Hotel<br>Suggestions&nbsp;</body></html>", null, panel4, "View hotel suggestions");
        panel4.setLayout(null);
        
        JLabel lbl4_LocationSelection = new JLabel("Hotel Selection");
        lbl4_LocationSelection.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl4_LocationSelection.setBounds(10, 10, 159, 30);
        panel4.add(lbl4_LocationSelection);
        
        JLabel lbl4_Preference = new JLabel("Preference: ");
        lbl4_Preference.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl4_Preference.setBounds(10, 42, 82, 20);
        panel4.add(lbl4_Preference);
        
        chckbx4_5Star = new JCheckBox("5 Star");
        chckbx4_5Star.setSelected(true);
        chckbx4_5Star.setFont(new Font("Tahoma", Font.PLAIN, 12));
        chckbx4_5Star.setBounds(102, 42, 65, 20);
        panel4.add(chckbx4_5Star);
        
        chckbx4_4Star = new JCheckBox("4 Star");
        chckbx4_4Star.setSelected(true);
        chckbx4_4Star.setFont(new Font("Tahoma", Font.PLAIN, 12));
        chckbx4_4Star.setBounds(202, 42, 65, 20);
        panel4.add(chckbx4_4Star);
        
        chckbx4_3Star = new JCheckBox("3 Star");
        chckbx4_3Star.setSelected(true);
        chckbx4_3Star.setFont(new Font("Tahoma", Font.PLAIN, 12));
        chckbx4_3Star.setBounds(302, 42, 65, 20);
        panel4.add(chckbx4_3Star);
        
        textPane4_Guests = new JTextPane();
        textPane4_Guests.setEditable(false);
        textPane4_Guests.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane4_Guests.setBounds(533, 42, 107, 20);
        panel4.add(textPane4_Guests);
        
        JLabel lbl4_BudgetRatio = new JLabel("Budget Ratio: ");
        lbl4_BudgetRatio.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl4_BudgetRatio.setBounds(10, 109, 82, 20);
        panel4.add(lbl4_BudgetRatio);
        
        slider4 = new JSlider();
        slider4.setPaintLabels(true);
        slider4.setPaintTicks(true);
        slider4.setMinorTickSpacing(5);
        slider4.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        slider4.setSnapToTicks(true);
        slider4.setMajorTickSpacing(10);
        slider4.setBounds(102, 101, 421, 45);
        panel4.add(slider4);
        
        slider4.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {        		
        		lg.setBudgetRatio(slider4.getValue());
        		double hotelBudgetSpent= lg.calculateHotelBudget();
        		textPane4_Budget.setText(String.valueOf("$"+ hotelBudgetSpent));       		
        		updateStep6();
        	}
        });
        
        textPane4_Budget = new JTextPane();
        textPane4_Budget.setEditable(false);
        textPane4_Budget.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane4_Budget.setBounds(533, 109, 107, 20);
        panel4.add(textPane4_Budget);
        
     
        btn4_Suggest = new JButton("Suggest");
        btn4_Suggest.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn4_Suggest.setBounds(550, 167, 90, 30);
        panel4.add(btn4_Suggest);
        btn4_Suggest.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		lg.clearHotelSuggestions();
        		boolean checkbox3Star = chckbx4_3Star.isSelected();
        		boolean checkbox4Star = chckbx4_4Star.isSelected();
        		boolean checkbox5Star = chckbx4_5Star.isSelected();
        		boolean[] checkbox = new boolean[3];
        		int budgetRatio = slider4.getValue();
        		try{
	        		if(checkbox3Star == true){
        				lg.hotelSuggest(3, budgetRatio);
	        		}
	        		else {
	        			checkbox3Star = false;
	        		}
	        		if(checkbox4Star == true){
	        			lg.hotelSuggest(4, budgetRatio);
	        		}
	        		else {
	        			checkbox4Star = false;
	        		}
	        		
	        		if(checkbox5Star == true){
	        			lg.hotelSuggest(5, budgetRatio);
	        		}
	        		else {
	        			checkbox5Star = false;
	        		}
	        			        		
	        		checkbox[0] = checkbox3Star;
	        		checkbox[1] = checkbox4Star;
	        		checkbox[2] = checkbox5Star;
	        		lg.setcheckBox(checkbox);
	        		
        		} catch(IOException io){
        			JOptionPane.showMessageDialog(new JFrame(), "Hotel data file could not be found. Please ensure 'hotelData' is found in the Data directory in your program folder.");
        		} catch(Exception ex){
        			JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
        		}
        		//display
        		list4.removeAll();
        		textPane4_HotelDetails.setText("");
        		list4.setListData(lg.getSuggestedHotelsNames());
        	}
        });
        
        JPanel panel4_SuggestedList = new JPanel();
        panel4_SuggestedList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Suggested List of Hotels", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel4_SuggestedList.setBounds(10, 220, 246, 250);
        panel4.add(panel4_SuggestedList);
        panel4_SuggestedList.setLayout(null);
        
        JScrollPane scrollPane4 = new JScrollPane();
        scrollPane4.setBounds(6, 16, 234, 226);
        panel4_SuggestedList.add(scrollPane4);
        
        list4 = new JList<String>();
        list4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane4.setViewportView(list4);
        list4.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Object obj = e.getSource();
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>)obj;
				Object selectedObj = list.getSelectedValue();
				if(selectedObj == null)
					return;
				if(((String)selectedObj).equals("No hotels match your criteria!")){
					list.clearSelection();
					return;
				}
				textPane4_HotelDetails.setText(lg.getHotelInformation(list.getSelectedIndex()));
				lg.setSelectedHotelIdx(list4.getSelectedIndex()); 
				//lg.setHotelBudgetSpent(lg.getSelectedHotelPrice(lg.getSelectedHotelIdx()));				
				updateStep6();
			}
        });
        
        textPane4_HotelDetails = new JTextPane();
        textPane4_HotelDetails.setEditable(false);
        textPane4_HotelDetails.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane4_HotelDetails.setBounds(266, 220, 374, 250);
        panel4.add(textPane4_HotelDetails);
        
        JLabel lbl4_NoOfGuest = new JLabel("No. of Guests");
        lbl4_NoOfGuest.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl4_NoOfGuest.setBounds(533, 20, 82, 20);
        panel4.add(lbl4_NoOfGuest);
        
        btn4_Next = new JButton("Next");
        btn4_Next.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn4_Next.setBounds(560, 490, 80, 30);
        panel4.add(btn4_Next);
        btn4_Next.addMouseListener(this);
	}
	
	/*
	 * Drawing of everything in Step 5
	 */
	private void GUI5() {
		panel5 = new JPanel();
		jtp.addTab("<html><body marginwidth=15 marginheight=8><b>Step 5:</b><br>Table<br>Assignment&nbsp;</body></html>", null, panel5, "Assign guests to tables of 10.");
		panel5.setLayout(null);
        
		JLabel lbl5_TableAssignment = new JLabel("Table Assignment");
		lbl5_TableAssignment.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbl5_TableAssignment.setBounds(10, 10, 218, 30);
		panel5.add(lbl5_TableAssignment);
		
		JLabel lbl5_ArrangeType = new JLabel("Select Assignment Type: ");
		lbl5_ArrangeType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbl5_ArrangeType.setBounds(10, 42, 154, 20);
        panel5.add(lbl5_ArrangeType);
		
		tableCols = new Vector<String>();
		
		// To get table assignment here.
		// GUI needs to get the tableCols (ie. "Table 1", "Table 2", ... (a variable number of tables))
		// GUI needs to get the list of Guests assigned to the different tables --> 10 rows(guests) per column(table)
		
		createTable5 (new Vector<Vector<String>>(), tableCols);
        
        rdbtn5_Random = new JRadioButton("Random");
        buttonGroup5.add(rdbtn5_Random);
        rdbtn5_Random.setBounds(163, 42, 65, 23);
        panel5.add(rdbtn5_Random);
        rdbtn5_Random.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		comboBox5_arrangerGroupType.setEnabled(false);
        	}
        });
        
        rdbtn5_ByGroup = new JRadioButton("By Group");
        buttonGroup5.add(rdbtn5_ByGroup);
        rdbtn5_ByGroup.setBounds(240, 42, 69, 23);
        panel5.add(rdbtn5_ByGroup);
        rdbtn5_ByGroup.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		comboBox5_arrangerGroupType.setEnabled(true);
        	}
        });
        
        comboBox5_arrangerGroupType = new JComboBox<String>();
        comboBox5_arrangerGroupType.setBounds(330, 43, 137, 20);
        comboBox5_arrangerGroupType.setEnabled(false);
        comboBox5_arrangerGroupType.addItem("Fast (poorer results)");
        comboBox5_arrangerGroupType.addItem("Normal (default)");
        comboBox5_arrangerGroupType.addItem("Slow (better results)");
        comboBox5_arrangerGroupType.setSelectedIndex(1);
        panel5.add(comboBox5_arrangerGroupType);
        
        btn5_Generate = new JButton("Generate");
        btn5_Generate.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn5_Generate.setBounds(510, 42, 100, 30);
        panel5.add(btn5_Generate);
        btn5_Generate.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//send relevant settings to tableAssigner
				int tableArrangerSetting = 0;
				
				if(rdbtn5_Random.isSelected()){
					tableArrangerSetting = -1;
				}
				else if(rdbtn5_ByGroup.isSelected()){
					tableArrangerSetting = comboBox5_arrangerGroupType.getSelectedIndex();
				} 
				else{		//none selected
					JOptionPane.showMessageDialog(new JFrame(), "Please select Assignment Type.");
					return;
				}
				
				progressMonitor5 = new ProgressMonitor(GUI.this, "Generating Arrangement...", "", 0, 100);
				progressMonitor5.setProgress(0);
				progressMonitor5.setMillisToPopup(0);
				progressMonitor5.setMillisToDecideToPopup(0);
				
				//disable button
				btn5_Generate.setEnabled(false);
				GenerateArrangementTask generateTask = new GenerateArrangementTask(tableArrangerSetting);
				generateTask.addPropertyChangeListener(new ArrangementPropertyListener(generateTask));
				generateTask.execute();
			}
        });
        
        btn5_Next = new JButton("Next");
        btn5_Next.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn5_Next.setBounds(560, 490, 80, 30);
        panel5.add(btn5_Next);
        btn5_Next.addMouseListener(this);
	}
	
	/**************************************************************************************************
	 * PropertyListener for SwingTask. This listens for any changes happening during execution of task.
	 **************************************************************************************************/
	class ArrangementPropertyListener implements PropertyChangeListener{
		private GenerateArrangementTask generateTask;
		
		public ArrangementPropertyListener(GenerateArrangementTask generateTask){
			this.generateTask = generateTask;
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if("progress" == evt.getPropertyName()){
				int progress = (Integer) evt.getNewValue();
				progressMonitor5.setProgress(progress);
				
				if(progressMonitor5.isCanceled()){
					generateTask.cancel(true);
					
				}
			}
		}
		
	};
	
	/**************************************************************************************************
	 * A subclass of SwingWorker. This allows threading to do the table assigner.
	 **************************************************************************************************/
	class GenerateArrangementTask extends SwingWorker<Vector<Vector<String>>, Void>{
		int tableArrangerSetting;
		int counter;
		TableAssigner tableAssigner;
		
		public GenerateArrangementTask(int setting){
			this.tableArrangerSetting = setting;
			tableAssigner = new TableAssigner();
		}
		
		@Override
		protected Vector<Vector<String>> doInBackground(){	
			setProgress(0);
			
			tableAssigner.addEventListener(new ProgressListener(){
				@Override
				public void progressEventOccured(ProgressEvent evt) {
					setProgress(evt.getProgress());
				}
			});
			
			switch(tableArrangerSetting){
			case -1:
				tableAssigner.setRandom(true);
				break;
			case 0:
				tableAssigner.setRandom(false);
				tableAssigner.setPopulationSize(40);
				tableAssigner.setNumberOfGenerations(800);
				break;
			case 1:
				tableAssigner.setRandom(false);
				tableAssigner.setPopulationSize(50);
				tableAssigner.setNumberOfGenerations(2000);
				break;
			case 2:
				tableAssigner.setRandom(false);
				tableAssigner.setPopulationSize(60);
				tableAssigner.setNumberOfGenerations(3500);
				break;
			}
			
			Vector<Integer> arrangementIndex = tableAssigner.generateArrangement(lg.getGuestList(), 10);
			Vector<Integer> seatsPerTable = tableAssigner.getSeatsPerTable();
			
			lg.setArrangement(arrangementIndex, seatsPerTable);
			Vector<Vector<String>> arrangement = lg.getArrangement();
			
			setProgress(100);
			return arrangement;
		}
		
		@Override
		public void done(){
			btn5_Generate.setEnabled(true);
			
			Vector<Vector<String>> arrangement;
			try {
				arrangement = this.get();
				
				//generate table cols
				tableCols = new Vector<String>();
				for(int i = 1; i <= arrangement.get(0).size(); i++){
					tableCols.add("Table " + i);
				}
				
				panel5.remove(scrollPane5);
				createTable5(arrangement, tableCols);
				
			} catch(CancellationException ex){
				
			} catch (InterruptedException ex){
				System.out.println("here");
			} catch (ExecutionException ex){
				System.out.println("here");
			}
		}
	}
	
	/*
	 * Drawing of everything in Step 6
	 */
	private void GUI6() {
		panel6 = new JPanel();
		jtp.addTab("<html><body marginwidth=15 marginheight=15><b>Step 6:</b><br>Expenses&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</body></html>", null, panel6, "Manage the expenses for this event.");
		panel6.setLayout(null);
        
		JLabel lbl6_Expenses = new JLabel("Expenses");
		lbl6_Expenses.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbl6_Expenses.setBounds(10, 10, 166, 30);
		panel6.add(lbl6_Expenses);
        
        btn6_AddExpense = new JButton("Add Entry");
        btn6_AddExpense.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        	}
        });
        btn6_AddExpense.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btn6_AddExpense.setBounds(10, 56, 110, 25);
        panel6.add(btn6_AddExpense);
        
        btn6_DeleteExpense = new JButton("Delete Entry");
        btn6_DeleteExpense.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        	}
        });
        btn6_DeleteExpense.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btn6_DeleteExpense.setBounds(140, 56, 110, 25);
        panel6.add(btn6_DeleteExpense);
        
        JLabel lbl6_TotalBudget = new JLabel("Total Budget");
        lbl6_TotalBudget.setHorizontalAlignment(SwingConstants.CENTER);
        lbl6_TotalBudget.setBounds(271, 35, 100, 14);
        panel6.add(lbl6_TotalBudget);
        
        textPane6_TotalBudget = new JTextPane();
        textPane6_TotalBudget.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane6_TotalBudget.setEditable(false);
        textPane6_TotalBudget.setFont(new Font("Tahoma", Font.PLAIN, 14));
        textPane6_TotalBudget.setBounds(271, 53, 100, 25);
        panel6.add(textPane6_TotalBudget);
        
        JLabel lbl6_Spent = new JLabel("Total Spent");
        lbl6_Spent.setHorizontalAlignment(SwingConstants.CENTER);
        lbl6_Spent.setBounds(391, 35, 100, 14);
        panel6.add(lbl6_Spent);
        
        textPane6_Spent = new JTextPane();
        textPane6_Spent.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane6_Spent.setEditable(false);
        textPane6_Spent.setFont(new Font("Tahoma", Font.PLAIN, 14));
        textPane6_Spent.setBounds(391, 53, 100, 25);
        panel6.add(textPane6_Spent);
        
        JLabel lbl6_Remaining = new JLabel("Remaining Budget");
        lbl6_Remaining.setHorizontalAlignment(SwingConstants.CENTER);
        lbl6_Remaining.setBounds(511, 35, 100, 14);
        panel6.add(lbl6_Remaining);
        
        textPane6_Remaining = new JTextPane();
        textPane6_Remaining.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane6_Remaining.setEditable(false);
        textPane6_Remaining.setFont(new Font("Tahoma", Font.PLAIN, 14));
        textPane6_Remaining.setBounds(511, 53, 100, 25);
        panel6.add(textPane6_Remaining);
		
		expensesCols = new Vector<String>();
		expensesCols.add("Item Name");
		expensesCols.add("Unit Cost");
		expensesCols.add("Quantity");
		expensesCols.add("Total Cost");
        
        createTable6(new Vector<Vector<String>>(), expensesCols);
        
        chckbx6_ExpensesFinalised = new JCheckBox("Expenses Finalised");
        chckbx6_ExpensesFinalised.setBounds(10, 463, 217, 23);
        panel6.add(chckbx6_ExpensesFinalised);
        chckbx6_ExpensesFinalised.setEnabled(false);
        chckbx6_ExpensesFinalised.addActionListener(new ActionListener(){
			@Override	//change here
			public void actionPerformed(ActionEvent e) {
				lg.setExpenseFinalised(chckbx6_ExpensesFinalised.isSelected());
			}
        });
        
        JLabel lbl6_CostPerHead = new JLabel("Cost per head estimate");
        lbl6_CostPerHead.setBounds(10, 493, 120, 14);
        panel6.add(lbl6_CostPerHead);
        
        textPane6_CostPerHead = new JTextPane();
        textPane6_CostPerHead.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane6_CostPerHead.setEditable(false);
        textPane6_CostPerHead.setFont(new Font("Tahoma", Font.PLAIN, 14));
        textPane6_CostPerHead.setBounds(127, 487, 150, 25);
        panel6.add(textPane6_CostPerHead);
        
        btn6_Export = new JButton("Export");
        btn6_Export.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn6_Export.setBounds(410, 490, 80, 30);
        panel6.add(btn6_Export);
        btn6_Export.addActionListener(exportImportButtonsListener);
		
		btn6_ViewSummary = new JButton("View Summary");
		btn6_ViewSummary.setFont(new Font("Tahoma", Font.BOLD, 12));
		btn6_ViewSummary.setBounds(510, 490, 130, 30);
        panel6.add(btn6_ViewSummary);        
        btn6_ViewSummary.addMouseListener(this);
	}
	
	/*
	 * Drawing of everything in Summary
	 */
	private void GUI7() {
		JPanel panel7 = new JPanel();
		jtp.addTab("<html><body marginwidth=15 marginheight=15><b>Summary</b></body></html>", null, panel7, null);
		panel7.setLayout(null);
		
		JLabel lbl7_Summary = new JLabel("Summary");
		lbl7_Summary.setBounds(10, 11, 76, 30);
		lbl7_Summary.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel7.add(lbl7_Summary);
		
		textPane7_Summary = new JTextPane();
		textPane7_Summary.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textPane7_Summary.setEditable(false);
		textPane7_Summary.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		textPane7_Summary.setBounds(10, 56, 600, 400);
        panel7.add(textPane7_Summary);
	}
	
	/***************************************************************************
	 * 
	 * Method for creation and initilisation of table2 (Guest List) in Step 2
	 * 
	 ***************************************************************************/
	void createTable2(Vector<Vector<String>> data, Vector<String> cols){
		table2 = new JTable(data, cols);
		table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table2.getTableHeader().setReorderingAllowed(false);            
        scrollPane2 = new JScrollPane(table2);
        scrollPane2.setBounds(10, 92, 600, 370);
        panel2.add(scrollPane2);        
        scrollPane2.setViewportView(table2);
        table2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table2.setPreferredScrollableViewportSize(new Dimension(600,370));
        table2.setFillsViewportHeight(true);
        table2.setColumnSelectionAllowed(true);
        table2.setCellSelectionEnabled(true);
        table2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table2.setAutoCreateRowSorter(true);
        
        table2.getColumnModel().getColumn(0).setPreferredWidth(200);
        //table2.getColumnModel().getColumn(2).setPreferredWidth(30);
        table2.getColumnModel().getColumn(2).setPreferredWidth(110);
        table2.getColumnModel().getColumn(3).setPreferredWidth(200);
        table2.getColumnModel().getColumn(4).setPreferredWidth(110);
        table2.getColumnModel().getColumn(5).setPreferredWidth(250);
        
        TableColumn genderCol = table2.getColumnModel().getColumn(1);
        
        JComboBox<String> comboBox_Gender = new JComboBox<String>();        
        comboBox_Gender.addItem("Select");
        comboBox_Gender.addItem("Male");
        comboBox_Gender.addItem("Female");
        genderCol.setCellEditor(new DefaultCellEditor(comboBox_Gender));
        
        final DefaultTableModel modelGuest = (DefaultTableModel)table2.getModel();
        
        MouseListener [] listeners = btn2_AddGuest.getMouseListeners();
        if(listeners.length != 0){
        	for(int i = 0; i < listeners.length; i++){
        		btn2_AddGuest.removeMouseListener(listeners[i]);
        	}
        }       
        
        btn2_AddGuest.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if (modelGuest.getRowCount() == 0 || table2.getSelectedRowCount() == 0) {
        			lg.addGuest();      			
            		modelGuest.addRow(new Vector<String>(6));        		
            		modelGuest.setValueAt("Select", modelGuest.getRowCount()-1, 1);       		
            		textPane4_Guests.setText(String.valueOf(lg.getGuestList().size()));
            		chckbx2_GuestListFinalised.setEnabled(false);
        		}
        		else {
        			int rowIndex = table2.getSelectedRow();
        			lg.addGuest(rowIndex+1);
	        		modelGuest.insertRow(rowIndex+1, new Vector<String>(6));
	        		modelGuest.setValueAt("Select", rowIndex+1, 1);
	        		textPane4_Guests.setText(String.valueOf(lg.getGuestList().size()));
	        		chckbx2_GuestListFinalised.setEnabled(false); 
        		}
        		
        	}
        });
        
        MouseListener [] listeners2 = btn2_DeleteGuest.getMouseListeners();
        if(listeners2.length != 0){
        	for(int i = 0; i < listeners2.length; i++){
        		btn2_DeleteGuest.removeMouseListener(listeners2[i]);
        	}
        }       
        
        btn2_DeleteGuest.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {       		
/*        		int row = table2.getSelectedRow();
        		if(row == -1)
        			return;
        		lg.removeGuest(row);
        		modelGuest.removeRow(row);     		
        		textPane4_Guests.setText(String.valueOf(lg.getGuestList().size()));
        		chckbx2_GuestListFinalised.setEnabled(false);
*/
        		int[] rowIndices = table2.getSelectedRows();        			
      			
    			for (int i = 0; i < rowIndices.length; i++) {       			
    				modelGuest.removeRow(rowIndices[0]);
    				lg.removeGuest(rowIndices[0]);
    			}
    			textPane4_Guests.setText(String.valueOf(lg.getGuestList().size()));
    			if(chckbx2_GuestListFinalised.isSelected())
    				chckbx2_GuestListFinalised.setSelected(false);
    			chckbx2_GuestListFinalised.setEnabled(lg.completedGuestFields());
        		
        		calcColumnWidths(table2);
        		
        	}
        });
        
        table2.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		if (e.getKeyCode()==KeyEvent.VK_TAB) {
        			table2.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0, false), "selectNextColumnCell"); 
        			chckbx2_GuestListFinalised.setEnabled(lg.completedGuestFields());
        		} 
        		if (e.getKeyCode()== KeyEvent.VK_INSERT) {
        			lg.addGuest();
            		modelGuest.addRow(new Vector<String>(6));
            		modelGuest.setValueAt("Select", modelGuest.getRowCount()-1, 1);
            		textPane4_Guests.setText(String.valueOf(lg.getGuestList().size()));
            		chckbx2_GuestListFinalised.setEnabled(false);
        		}
        		
/*        		if (e.getKeyCode()==KeyEvent.VK_DELETE){
        			int[] rowIndices = table2.getSelectedRows();        			
        		              			
        			for (int i = 0; i < rowIndices.length; i++) {       			
        				modelGuest.removeRow(rowIndices[0]);
        				lg.removeGuest(rowIndices[0]);
        			}
        			textPane4_Guests.setText(String.valueOf(lg.getGuestList().size()));
        			if(chckbx2_GuestListFinalised.isSelected())
        				chckbx2_GuestListFinalised.setSelected(false);
        			chckbx2_GuestListFinalised.setEnabled(lg.completedGuestFields());
        		}
*/        		
        		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
        			int[] rowIndices2 = table2.getSelectedRows();
        			int[] columnIndices = table2.getSelectedColumns();
        			       			
        			for (int nRow = rowIndices2[0] ; nRow <= rowIndices2[rowIndices2.length-1] ; nRow++)
        				for (int nColumn = columnIndices[0] ; nColumn <= columnIndices[columnIndices.length-1] ; nColumn++)
        						modelGuest.setValueAt("", nRow, nColumn);
        		}
        		
        	}
        	@Override
        	public void keyReleased(KeyEvent e) {
        	}
        	@Override
        	public void keyTyped(KeyEvent e) {
        	}
        });
        
        table2.getModel().addTableModelListener(new TableModelListener(){
        	@Override
        	public void tableChanged(TableModelEvent e){
        		//get information about change
        		int row = e.getFirstRow();
        		int column = e.getColumn();
        		TableModel model = (TableModel)e.getSource();
        		String columnName = model.getColumnName(column);
        		if(row == -1 || column == -1)
        			return;
        		String data = (String)model.getValueAt(row, column);
        		
        		//pass to logic to save
        		lg.setGuestInfo(row, columnName, data);
        		
        		//if there exist 1 guest, and all fields are updated, chckbx2_GuestListFinalised should be enabled.
        		chckbx2_GuestListFinalised.setEnabled(lg.completedGuestFields());
    			
        		//if chckBx is checked, it shld be unchecked
        		if(chckbx2_GuestListFinalised.isSelected()){
        			chckbx2_GuestListFinalised.setSelected(false);
        			lg.setGuestListFinalised(false);
        		} 
        		//calcColumnWidths(table2);
        	}
        });                 
	}
	
	
	/***************************************************************************************
	 * 
	 * Method for creation and initlialisation of table3 (Programme Schedule) in Step 3
	 * 
	 ***************************************************************************************/
	void createTable3(Vector<Vector<String>> data, Vector<String> cols){		
		table3 = new JTable(data, cols);
		table3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table3.getTableHeader().setReorderingAllowed(false);
              
        scrollPane3 = new JScrollPane(table3);
        scrollPane3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane3.setBounds(10, 92, 600, 370);
        panel3.add(scrollPane3);
        
        scrollPane3.setViewportView(table3);
        table3.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table3.setPreferredScrollableViewportSize(new Dimension(600,370));
        table3.setFillsViewportHeight(true);
        table3.setColumnSelectionAllowed(true);
        table3.setCellSelectionEnabled(true);
        table3.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table3.setAutoCreateRowSorter(true);
        
        table3.getColumnModel().getColumn(0).setPreferredWidth(90);
        table3.getColumnModel().getColumn(1).setPreferredWidth(75);
        table3.getColumnModel().getColumn(2).setPreferredWidth(75);
        table3.getColumnModel().getColumn(3).setPreferredWidth(239);
        table3.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        TableColumn dateCol = table3.getColumnModel().getColumn(0);
        
        
        /*****************************
         * COMBO BOX ADDING CODE HERE
         *****************************/
        comboBox_Date = new JComboBox<String>();

        if (!lg.getDateList().isEmpty()) {
        	Iterator<String> itr = lg.getDateList().iterator();
        	while(itr.hasNext()) {
        		comboBox_Date.addItem(itr.next());
        	}
        }       
        dateCol.setCellEditor(new DefaultCellEditor(comboBox_Date));
        
        final DefaultTableModel modelProgramme = (DefaultTableModel)table3.getModel();
 
       		
      
        /***********************************************
         * Mouse Listeners for Add and Delete buttons
         ***********************************************/   
        //When creating a new table for the second time, we need to remove the previous table's mouse listener
        MouseListener [] listeners = btn3_AddEntry.getMouseListeners();
        if(listeners.length != 0){
        	for(int i = 0; i < listeners.length; i++){
        		btn3_AddEntry.removeMouseListener(listeners[i]);
        	}
        }       
        
        btn3_AddEntry.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if (modelProgramme.getRowCount() == 0 || table3.getSelectedRowCount() == 0) {
        			lg.addProgramme();
            		modelProgramme.addRow(new Vector<String>(5));      		
            		chckbx3_ProgrammeScheduleFinalised.setEnabled(false); 
        		}
        		else {
        			int rowIndex = table3.getSelectedRow();
        			lg.addProgramme(rowIndex+1);	        			        	
	        		modelProgramme.insertRow(rowIndex+1, new Vector<String>(5));		
	        		chckbx3_ProgrammeScheduleFinalised.setEnabled(false); 
        		}
        		      	
        	}
        });
        
        //When creating a new table for the second time, we need to remove the previous table's mouse listener        
        MouseListener [] listeners2 = btn3_DeleteEntry.getMouseListeners();
        if(listeners2.length != 0){
        	for(int i = 0; i < listeners2.length; i++){
        		btn3_DeleteEntry.removeMouseListener(listeners2[i]);
        	}
        }       
        
        btn3_DeleteEntry.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {       		
        		int[] rowIndices = table3.getSelectedRows();        			
      			
    			for (int i = 0; i < rowIndices.length; i++) {       			
    				modelProgramme.removeRow(rowIndices[0]);
    				lg.removeProgramme(rowIndices[0]);
    			}
    			if(chckbx3_ProgrammeScheduleFinalised.isSelected())
    				chckbx3_ProgrammeScheduleFinalised.setSelected(false);
    			chckbx3_ProgrammeScheduleFinalised.setEnabled(lg.completedProgrammeFields());
        	}
        });
        
        
        /****************************
         * Keyboard Listeners here
         ****************************/
        table3.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		if (e.getKeyCode()==KeyEvent.VK_TAB) {
        			table3.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0, false), "selectNextColumnCell");
        			chckbx3_ProgrammeScheduleFinalised.setEnabled(lg.completedProgrammeFields());
        		} 
        		if (e.getKeyCode()== KeyEvent.VK_INSERT) {
        			modelProgramme.addRow(new Vector<Object>(5));  
        			lg.addProgramme();
        			chckbx3_ProgrammeScheduleFinalised.setEnabled(false);
        		}
/*        		if (e.getKeyCode()==KeyEvent.VK_DELETE){
        			int[] rowIndices = table3.getSelectedRows();        			
        		              			
        			for (int i = 0; i < rowIndices.length; i++) {       			
        				modelProgramme.removeRow(rowIndices[0]);
        				lg.removeProgramme(rowIndices[0]);
        			}
        			if(chckbx3_ProgrammeScheduleFinalised.isSelected())
        				chckbx3_ProgrammeScheduleFinalised.setSelected(false);
        			chckbx3_ProgrammeScheduleFinalised.setEnabled(lg.completedProgrammeFields());
        		}
*/        		
        		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
        			int[] rowIndices2 = table3.getSelectedRows();
        			int[] columnIndices = table3.getSelectedColumns();
        			       			
        			for (int nRow = rowIndices2[0] ; nRow <= rowIndices2[rowIndices2.length-1] ; nRow++)
        				for (int nColumn = columnIndices[0] ; nColumn <= columnIndices[columnIndices.length-1] ; nColumn++)
        						modelProgramme.setValueAt("", nRow, nColumn);
        		}
        		
        	}
        	@Override
        	public void keyReleased(KeyEvent e) {
        	}
        	@Override
        	public void keyTyped(KeyEvent e) {
        	}
        });
        
        
        // Wraps the text for the column "programme"
        table3.getColumnModel().getColumn(3).setCellRenderer(new TableCellWrapText());
        
        /****************************
         * Table Change Listener Here
         *****************************/
        table3.getModel().addTableModelListener(new TableModelListener(){
        	@Override
        	public void tableChanged(TableModelEvent e){
        		//get information about change
        		int row = e.getFirstRow();        	
        		int column = e.getColumn();
        		TableModel model = (TableModel)e.getSource();
        		String columnName = model.getColumnName(column);
        		if(row == -1 || column == -1)	//empty table
        			return;
        		String data = (String)model.getValueAt(row, column);
        		
        		//if column == 1 or 2 means event start time and end time
        		if(column == 1 || column == 2){
        			Integer time;
        			Integer compareTime;	//time to be compared to
        		
        			try{
        				time = Integer.parseInt(data);
        				if(time<0 || time>2359 || (time%100 > 59)){
        					data = "0";
        					model.setValueAt("0", row, column);
        				}
        			} catch(NumberFormatException ex){
        				data = "0";
        				model.setValueAt("0", row, column);
        				return;
        			}     			   			
        			
        			
        			switch (column){
        			case 1:	//if startTime was modified, compare it with endTime for error
        				if(model.getValueAt(row,2) != null){
        					compareTime = Integer.parseInt((String)model.getValueAt(row, 2));
        					if (time > compareTime){
        						data = "0";
        						model.setValueAt(Integer.toString(compareTime), row, column);
        					}
        				}
        				break;

        			case 2:	//if endTime was modified, compare it with startTime for error
        				if(model.getValueAt(row,1) != null){
        					compareTime = Integer.parseInt((String)model.getValueAt(row, 1));
        					if (time < compareTime){
        						data = "0";
        						model.setValueAt(Integer.toString(compareTime), row, column);
        					}
        				}

        				break;
        			}
        			
        		}
        		
        		lg.setProgrammeInfo(row, columnName, data);
        		
        		//if there exist 1 guest, and all fields are updated, chckBx3_ProgrammeScheduleFinalised should be enabled.
        		chckbx3_ProgrammeScheduleFinalised.setEnabled(lg.completedProgrammeFields());

        		//if chckBx is checked, it shld be unchecked
        		if(chckbx3_ProgrammeScheduleFinalised.isSelected()){
        			chckbx3_ProgrammeScheduleFinalised.setSelected(false);
        			lg.setProgrammeScheduleFinalised(false);
        		}
        		
        	}
        });
	}
	
	/*********************************************************************************
	 * 
	 * Method for creation and initlialisation of table5 (Table Assignment) in Step 5
	 * 
	 *********************************************************************************/
	void createTable5(Vector<Vector<String>> data, Vector<String> cols){
		table5 = new JTable(data, cols);
		
		table5.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table5.setPreferredScrollableViewportSize(new Dimension(600,370));
		table5.getTableHeader().setReorderingAllowed(false);
		table5.setFillsViewportHeight(true);
		
        scrollPane5 = new JScrollPane(table5);
        scrollPane5.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane5.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane5.setBounds(10, 92, 600, 370);
        panel5.add(scrollPane5);     
        scrollPane5.setViewportView(table5);
        table5.setFont(new Font("Tahoma", Font.PLAIN, 12));    
        
        table5.setColumnSelectionAllowed(true);
        table5.setCellSelectionEnabled(true);        
        table5.setAutoCreateRowSorter(false);
        
        //for drag and drop
        table5.setDragEnabled(true);
        table5.setDropMode(DropMode.USE_SELECTION);
        table5.setTransferHandler(new TransferHandler(){
        	private int oldCol;
        	private int oldRow;
        	private String oldData;
        	
            public int getSourceActions(JComponent c) {
                  return DnDConstants.ACTION_COPY_OR_MOVE;
              }
   
              public Transferable createTransferable(JComponent comp)
              {
                  JTable table=(JTable)comp;
                  oldRow=table.getSelectedRow();
                  oldCol=table.getSelectedColumn();
   
                  String value = (String)table.getModel().getValueAt(oldRow,oldCol);
                  StringSelection transferable = new StringSelection(value);
                  //table.getModel().setValueAt(null,oldRow,oldCol);
                  return transferable;
              }
              
              public boolean canImport(TransferHandler.TransferSupport info){
                  if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)){
                      return false;
                  }
   
                  return true;
              }
   
              public boolean importData(TransferSupport support) {
   
                  if (!support.isDrop()) {
                      return false;
                  }
   
                  if (!canImport(support)) {
                      return false;
                  }
   
                  JTable table=(JTable)support.getComponent();
                  DefaultTableModel tableModel=(DefaultTableModel)table.getModel();
                  
                  JTable.DropLocation dl = (JTable.DropLocation)support.getDropLocation();
   
                  int row = dl.getRow();
                  int col=dl.getColumn();
                  oldData = (String)tableModel.getValueAt(row, col);
                  
                  String data;
                  try {
                      data = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                  } catch (UnsupportedFlavorException e) {
                      return false;
                  } catch (IOException e) {
                      return false;
                  }
   
                  tableModel.setValueAt(data, row, col);
                  tableModel.setValueAt(oldData, oldRow, oldCol);
   
                  lg.updateArrangementIndex(oldRow, oldCol, row, col);
                  return true;
              }
   
          });
      
      //Calls the method to adjust column width to fit the longest string in the column
  		calcColumnWidths(table5);
  		table5.getModel().addTableModelListener(new TableModelListener() {			
		    public void tableChanged(TableModelEvent e) {
		    	calcColumnWidths(table5);
		    }			
  		});
  		}
	/*
	 * Method for creation and initlialisation of table6 (Expenses) in Step 6
	 */
	void createTable6(Vector<Vector<String>> data, Vector<String> cols){
		table6 = new JTable(data,cols);
/*		table6 = new JTable(data, cols) {
			public boolean isCellEditable(int row, int column) {
        		if (column == 3)
        			return false;
        		else 
        			return true;
        	}
		};
*/
		table6.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);        
		table6.getTableHeader().setReorderingAllowed(false);
        scrollPane6 = new JScrollPane(table6);
        scrollPane6.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane6.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane6.setBounds(10, 92, 600, 370);
        panel6.add(scrollPane6);
        
        scrollPane6.setViewportView(table6);
        table6.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table6.setPreferredScrollableViewportSize(new Dimension(600,370));
        table6.setFillsViewportHeight(true);
        table6.setColumnSelectionAllowed(true);
        table6.setCellSelectionEnabled(true);
        table6.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table6.setAutoCreateRowSorter(true);
        
        table6.getColumnModel().getColumn(0).setPreferredWidth(324);
        table6.getColumnModel().getColumn(1).setPreferredWidth(100);
        table6.getColumnModel().getColumn(2).setPreferredWidth(55);
        table6.getColumnModel().getColumn(3).setPreferredWidth(120);

       
        final DefaultTableModel modelExpense = (DefaultTableModel)table6.getModel() ;
       

        /***********************************************
         * Mouse Listeners for Add and Delete buttons
         ***********************************************/   
        //When creating a new table for the second time, we need to remove the previous table's mouse listener
        MouseListener [] listeners = btn6_AddExpense.getMouseListeners();
        if(listeners.length != 0){
        	for(int i = 0; i < listeners.length; i++){
        		btn6_AddExpense.removeMouseListener(listeners[i]);
        	}
        }       
        
        btn6_AddExpense.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		
        		if (modelExpense.getRowCount() == 0 || table6.getSelectedRowCount() == 0) {
	        		lg.addExpense();
	        		Vector<String> newRow= new Vector<String>();
	        		newRow.add("");
	        		newRow.add("0");
	        		newRow.add("0");
	        		newRow.add("0");
	        		modelExpense.addRow(newRow);      		
	        		chckbx6_ExpensesFinalised.setEnabled(false);      	
        		}
        		else {
        			int rowIndex = table6.getSelectedRow();
        			lg.addExpense(rowIndex+1);
	        		Vector<String> newRow= new Vector<String>();
	        		newRow.add("");
	        		newRow.add("0");
	        		newRow.add("0");
	        		newRow.add("0");
	        		modelExpense.insertRow(rowIndex+1, newRow);		
	        		chckbx6_ExpensesFinalised.setEnabled(false);  	
        		}
        			
        		
        	
        		
        	}
        });
        
        //When creating a new table for the second time, we need to remove the previous table's mouse listener        
        MouseListener [] listeners2 = btn6_DeleteExpense.getMouseListeners();
        if(listeners2.length != 0){
        	for(int i = 0; i < listeners2.length; i++){
        		btn6_DeleteExpense.removeMouseListener(listeners2[i]);
        	}
        }       
        
        btn6_DeleteExpense.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {       		
        		int[] rowIndices = table6.getSelectedRows();     			
      			
      			for (int i = 0; i < rowIndices.length; i++) {       			    				
    				double delete_cost = Double.parseDouble(lg.getExpenseList().get(rowIndices[0]).get(3));     				
    				lg.setExpenseSpent(lg.getExpenseSpent() - delete_cost);
    				updateStep6();    				
    				modelExpense.removeRow(rowIndices[0]);
    				lg.removeExpense(rowIndices[0]);
    			}
    			
    			if(chckbx6_ExpensesFinalised.isSelected())
    				chckbx6_ExpensesFinalised.setSelected(false);
    				chckbx6_ExpensesFinalised.setEnabled(lg.completedExpenseFields());
        	}
        });
        	

        /****************************
         * Keyboard Listeners here
         ****************************/	
        table6.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		if (e.getKeyCode()==KeyEvent.VK_TAB) {
        			table6.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0, false), "selectNextColumnCell");
        			chckbx6_ExpensesFinalised.setEnabled(lg.completedExpenseFields());
        		} 
        		if (e.getKeyCode()== KeyEvent.VK_INSERT) {
        			lg.addExpense();
            		Vector<String> newRow= new Vector<String>();
            		newRow.add("");
            		newRow.add("0");
            		newRow.add("0");
            		newRow.add("0");
            		modelExpense.addRow(newRow);      		
            		chckbx6_ExpensesFinalised.setEnabled(false);    
        		}
/*        		if (e.getKeyCode()==KeyEvent.VK_DELETE){
        			int[] rowIndices = table6.getSelectedRows();        			
        		              			
        			for (int i = 0; i < rowIndices.length; i++) {       			
        				modelExpense.removeRow(rowIndices[0]);
        				lg.removeExpense(rowIndices[0]);
        			}
        			if(chckbx6_ExpensesFinalised.isSelected())
        				chckbx6_ExpensesFinalised.setSelected(false);
        				chckbx6_ExpensesFinalised.setEnabled(lg.completedExpenseFields());
        		}
*/        		      		
        	}
        	@Override
        	public void keyReleased(KeyEvent e) {
        	}
        	@Override
        	public void keyTyped(KeyEvent e) {
        	}
        });
        
        /****************************
         * Table Change Listener Here
         *****************************/
       
        table6.getModel().addTableModelListener(new TableModelListener(){
        	@Override
        	public void tableChanged(TableModelEvent e){
        		//get information about change
        		int row = e.getFirstRow();
        		int column = e.getColumn();
        		TableModel model = (TableModel)e.getSource();
        		String columnName = model.getColumnName(column);
        		if(row == -1 || column == -1)
        			return;
        		String data = (String)model.getValueAt(row, column);
        		
        		switch(column){
        		case 0:			//refers to column Item name
        			lg.setExpenseInfo(row, columnName, data);
        			break;
        		case 1:			//refers to column Unit Price
        			try{
        				Double value = Double.parseDouble(data);
        				if (value < 0.0){
        					data = "0.0";
            				model.setValueAt("0	", row, column);
        				}
        				
        				//by right this parseInt should have no bad things happening 
        				Integer correspondingQuantity = Integer.parseInt(lg.getExpenseList().get(row).get(2));
        				Double correspondingTotalCost = value * correspondingQuantity;
        				String totalCostStr = correspondingTotalCost.toString();
        				
        				double totalBudget = lg.getBudget();
                		double totalExpenses = lg.getHotelBudgetSpent() + lg.getExpenseSpent();
        				if ((totalExpenses + correspondingTotalCost) > totalBudget) {
        					JOptionPane.showMessageDialog(new JFrame(), "Adding this expense will exceed the Event budget"
            						, "Exceed Budget Error", JOptionPane.WARNING_MESSAGE );
            				dateChooser1_StartDate.setDate(dateChooser1_EndDate.getDate());
        				}
                		
                		lg.setExpenseInfo(row, columnName, data);
        				lg.setExpenseInfo(row, "Total Cost", totalCostStr);
        				model.setValueAt(totalCostStr, row, 3);
        			} catch(NumberFormatException ex){
        				data = "0.0";
        				model.setValueAt("0", row, column);
        				return;
        			}
        			break;
        		case 2:		//refers to column Quantity
        			try{
        				Integer value = Integer.parseInt(data);
        				if (value < 0){
        					data = "0";
            				model.setValueAt("0", row, column);
        				}
        				try{
        					Double correspondingCost = Double.parseDouble(lg.getExpenseList().get(row).get(1));       					
            				Double correspondingTotalCost = value * correspondingCost;
            				String totalCostStr = correspondingTotalCost.toString();
            				
            				double totalBudget = lg.getBudget();
                    		double totalExpenses = lg.getHotelBudgetSpent() + lg.getExpenseSpent();
            				if ((totalExpenses + correspondingTotalCost) > totalBudget) {
            					JOptionPane.showMessageDialog(new JFrame(), "Adding this expense will exceed the Event budget"
                						, "Exceed Budget Error", JOptionPane.WARNING_MESSAGE );
                				dateChooser1_StartDate.setDate(dateChooser1_EndDate.getDate());
            				}
            				
                    		lg.setExpenseInfo(row, columnName, data);
            				lg.setExpenseInfo(row, "Total Cost", totalCostStr);
            				model.setValueAt(totalCostStr, row, 3);
        				} catch(NumberFormatException ex){
        					ex.printStackTrace();
        				}
        			} catch(NumberFormatException ex){
        				data = "0";
        				model.setValueAt("0", row, column);
        				return;
        			}
        			break;
        		case 3:		//refers to column Total Cost
    				String currValue = lg.getExpenseList().get(row).get(3);
        			
        			if(currValue.equals(data));
        			else
        				model.setValueAt(currValue, row, 3);
        			break;
        		}
  
        		updateStep6();
        		
/*        		double totalBudget = lg.getBudget();
        		double totalExpenses = lg.getHotelBudgetSpent() + lg.getExpenseSpent();
        		double remainingBudget = totalBudget - totalExpenses;
        		
        		textPane6_TotalBudget.setText(String.valueOf("$"+totalBudget));
        		textPane6_Spent.setText(String.valueOf("$"+ totalExpenses));		
        		textPane6_Remaining.setText(String.valueOf("$"+remainingBudget));
*/        	
        		chckbx6_ExpensesFinalised.setEnabled(lg.completedExpenseFields());

        		//if chckBx is checked, it shld be unchecked
        		if(chckbx6_ExpensesFinalised.isSelected()){
        			chckbx6_ExpensesFinalised.setSelected(false);
        			lg.setExpenseFinalised(false);
        		}
        	}      	
        });
	}
	
	
	/**********************************
	 * 
	 * Update of GUI Starts Here
	 * 
	 **********************************/
	void updateAll(){
		updateStep1();
		updateStep2();
		updateStep3();
		updateStep4();
		updateStep5();
		updateStep6();
		updateStep7();
		updateStep0();
	}
		
	void updateStep0() {
		
		switch(lg.step1Status()) {
			case 0:
				textPane0_EventName.setText(lg.getEventName());
				lbl0_Step1.setText("  Event Details are empty.");
				lbl0_Step1.setBackground(Color.PINK);
				break;
			case 1:
				lbl0_Step1.setText("  Event Details are incomplete.");
				lbl0_Step1.setBackground(Color.PINK);
				break;
			case 2:
				lbl0_Step1.setText("  Event Details are completed.");
				lbl0_Step1.setBackground(new Color(152, 251, 152));
				break;
		}
		
		switch(lg.step2Status()){
			case 0:
				lbl0_Step2.setText("  Guest list is empty.");
				lbl0_Step2.setBackground(Color.PINK);
				break;
			case 1:
				lbl0_Step2.setText("  Guest list contains missing details.");
				lbl0_Step2.setBackground(Color.PINK);
				break;
			case 2:
				lbl0_Step2.setText("  Guest list is not finalised.");
				lbl0_Step2.setBackground(Color.PINK);
				break;
			case 3:
				lbl0_Step2.setText("  Guest list is finalised.");
				lbl0_Step2.setBackground(new Color(152, 251, 152));
				break;
		}
		
		switch(lg.step3Status()){
			case 0:
				lbl0_Step3.setText("  Programme Schedule is empty.");
				lbl0_Step3.setBackground(Color.PINK);
				break;
			case 1:
				lbl0_Step3.setText("  Programme Schedule contains missing details.");
				lbl0_Step3.setBackground(Color.PINK);
				break;
			case 2:
				lbl0_Step3.setText("  Programme Schedule is not finalised.");
				lbl0_Step3.setBackground(Color.PINK);
				break;
			case 3:
				lbl0_Step3.setText("  Programme Schedule is finalised.");
				lbl0_Step3.setBackground(new Color(152, 251, 152));
				break;
		}
		switch(lg.step4Status()){
			case 0:
				lbl0_Step4.setText("  Location not selected.");
				lbl0_Step4.setBackground(Color.PINK);
				break;
			case 1:
				lbl0_Step4.setText("  Location has been selected.");
				lbl0_Step4.setBackground(new Color(152, 251, 152));
				break;
		}
	}
	
	void updateStep1(){
		comboBox1_EventType.setSelectedIndex(lg.getEventType() + 1);
		textField1_EventName.setText(lg.getEventName());
		spinner1_StartTimeH.setValue(lg.getStartTimeH());
		spinner1_StartTimeM.setValue(lg.getStartTimeM());
		spinner1_EndTimeH.setValue(lg.getEndTimeH());
		spinner1_EndTimeM.setValue(lg.getEndTimeM());
		textArea1_EventDescription.setText(lg.getEventDes());
		textField1_budget.setText(String.valueOf(lg.getBudget()));
		dateChooser1_StartDate.setDate(lg.getEventStartDate());
		dateChooser1_EndDate.setDate(lg.getEventEndDate());
		if(lg.getMealType()==0) {
			rdbtn1_Lunch.setSelected(true);
			rdbtn1_Dinner.setSelected(false);
		}
		else if(lg.getMealType()==1) {
			rdbtn1_Lunch.setSelected(false);
			rdbtn1_Dinner.setSelected(true);
		}
		
		if (!lg.getDateList().isEmpty()) {
			comboBox1_MealDates.removeAllItems();
        	Iterator<String> itr = lg.getDateList().iterator();
        	while(itr.hasNext()) {
        		comboBox1_MealDates.addItem(itr.next());
        	}
        	comboBox1_MealDates.setSelectedIndex(lg.getMealDateSelected());
        }
		rdbtn1_Lunch.setEnabled(lg.getMealRadioButtons(0));
		rdbtn1_Dinner.setEnabled(lg.getMealRadioButtons(1));
		
	}
	
	void updateStep2(){	
		panel2.remove(scrollPane2);
		createTable2(lg.getGuestNameList(), guestCols);
		//calcColumnWidths(table2);
		if(lg.completedGuestFields())
			chckbx2_GuestListFinalised.setEnabled(true);
		if(lg.getGuestListFinalised() == true)
			chckbx2_GuestListFinalised.setSelected(true);
	}
	
	void updateStep3(){
		panel3.remove(scrollPane3);
		createTable3(lg.getProgrammeSchedule(), programmeCols);
		if(lg.completedProgrammeFields())
			chckbx3_ProgrammeScheduleFinalised.setEnabled(true);
		if(lg.getProgrammeScheduleFinalised() == true)
			chckbx3_ProgrammeScheduleFinalised.setSelected(true);
	}
	
	void updateStep4(){
		textPane4_Guests.setText(Integer.toString(lg.getGuestList().size()));
		textPane4_Budget.setText(String.valueOf("$"+lg.calculateHotelBudget()));	
		list4.setListData(lg.getSuggestedHotelsNames());
		list4.setSelectedIndex(lg.getSelectedHotelIdx());
		slider4.setValue(lg.getBudgetRatio());
		boolean[] checkbox = lg.getcheckBox();
		if (checkbox[0] == true )
			chckbx4_3Star.setSelected(true);
		else chckbx4_3Star.setSelected(false);
		
		if (checkbox[1] == true )
			chckbx4_4Star.setSelected(true);
		else chckbx4_4Star.setSelected(false);
		
		if (checkbox[2] == true )
			chckbx4_5Star.setSelected(true);
		else chckbx4_5Star.setSelected(false);
	}
	
	void updateStep5(){
		panel5.remove(scrollPane5);
		Vector<Vector<String>> arrangement = lg.getArrangement();
		tableCols = new Vector<String>();
		for(int i = 1; i <= arrangement.get(0).size(); i++){
			tableCols.add("Table " + i);
		}
		
		createTable5(arrangement, tableCols);
	}
	
	void updateStep6(){
		double totalBudget = lg.getBudget();
		double totalExpenses = lg.getHotelBudgetSpent() + lg.getExpenseSpent();
		double remainingBudget = totalBudget - totalExpenses;
		
		textPane6_TotalBudget.setText(String.valueOf("$"+totalBudget));
		textPane6_Spent.setText(String.valueOf("$"+ totalExpenses));		
		textPane6_Remaining.setText(String.valueOf("$"+remainingBudget));

		if (lg.getGuestList().size()!= 0) {
			lg.setCostPerHead();
			DecimalFormat df = new DecimalFormat("0.00");
			df.setGroupingUsed(false);
			double costPerHead = lg.getCostPerHead();
			String costPerHeadStr = df.format(costPerHead);
			textPane6_CostPerHead.setText(String.valueOf("$"+ costPerHeadStr));
		}
		
		//TODO plus create table6
	}
	
	@SuppressWarnings("deprecation")
	void updateStep7(){
		String startdate = null;
		String enddate = null;
		
		if ( lg.getEventStartDate() !=null && lg.getEventEndDate() !=null ) {
		Date startDate = lg.getEventStartDate();
		startdate = Integer.toString(startDate.getDate()) + "/" +  Integer.toString(startDate.getMonth()+1) 
				+ "/" + Integer.toString(startDate.getYear()+1900);
		Date endDate = lg.getEventEndDate();
		enddate = Integer.toString(endDate.getDate()) + "/" + Integer.toString(endDate.getMonth()+1)
				+ "/" + Integer.toString(endDate.getYear()+1900);
		}
		
		textPane7_Summary.setText("\n" +
								  "  Name\t\t" + lg.getEventName() + "\n\n" + 
								  "  Description\t" + lg.getEventDes() + "\n\n" +
								  "  Where\t\t" + lg.getHotelName(lg.getSelectedHotelIdx()) + "\n\n" +
								  "  From\t\t" + startdate + "\n\n" +
								  "  To\t\t" + enddate + "\n\n\n" +
								  
								  "  No. of Guests\t" + Integer.toString(lg.getGuestList().size()) + "\n\n" +
								  "  Total expenses\t" + (lg.getExpenseSpent()+lg.getHotelBudgetSpent()) );
	}
	
	
	/*******************************
	 * 
	 * Listeners Starts Here
	 * 
	 *******************************/
	class ComboBox1_EventType_Listener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			@SuppressWarnings("rawtypes")
			int selectedEventType = ((JComboBox)e.getSource()).getSelectedIndex();
			lg.setEventType(selectedEventType);
			//System.out.println(comboBox1.getItemAt(lg.getEventType()));
		}
	}
	
	class SpinnerListeners implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			Object obj = e.getSource();
			if(obj == spinner1_StartTimeH){
				lg.setStartTimeH((int)spinner1_StartTimeH.getValue());
				//System.out.println(lg.getStartTimeH());
			}
			else if(obj == spinner1_StartTimeM){
				lg.setStartTimeM((int)spinner1_StartTimeM.getValue());
				//System.out.println(lg.getStartTimeM());
			}
			else if(obj == spinner1_EndTimeH){
				lg.setEndTimeH((int)spinner1_EndTimeH.getValue());
				//System.out.println(lg.getEndTimeH());
			}
			else if(obj == spinner1_EndTimeM){
				lg.setEndTimeM((int)spinner1_EndTimeM.getValue());
				//System.out.println(lg.getEndTimeM());
			}
			
			// checks if the event time frame falls within Lunch or Dinner timings.
			// Lunch: 1100 - 1459 hrs
			// Dinner:  1900 - 2259 hrs
			enableMealRaidoButtons();
		}
	}
	
	/*
	 * Overrides for all export and import buttons in GUI
	 */
	class ExportImportButtonsListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			JFrame frame = new JFrame();
			
			if(obj == btn2_Load){
				fileChooser = new JFileChooser(".");
				fileChooser.setFileFilter(new csvFilter());
				int choice = fileChooser.showOpenDialog(frame);
				
				switch(choice){
				case JFileChooser.APPROVE_OPTION:
					File file = fileChooser.getSelectedFile();
					if(file == null)
						return;
					try{
						lg.importGuest(file);
					} catch(DataFormatException ex){
						JOptionPane.showMessageDialog(new JFrame(), "Error importing CSV file. Make sure file is properly formatted");
					}
					break;
				case JFileChooser.CANCEL_OPTION:
					break;
				case JFileChooser.ERROR_OPTION:
					System.out.println("fileChooser error");
					break;
				}
				
				panel2.remove(scrollPane2);
				createTable2(lg.getGuestNameList(), guestCols);
				calcColumnWidths(table2);
				if(lg.completedGuestFields())
					chckbx2_GuestListFinalised.setEnabled(true);
				if(lg.getGuestListFinalised() == true)
					chckbx2_GuestListFinalised.setSelected(true);
			}
			else if(obj == btn2_Export){
				fileChooser = new JFileChooser(".");
				fileChooser.setFileFilter(new csvFilter());
				int choice = fileChooser.showSaveDialog(frame);
				
				switch(choice){
				case JFileChooser.APPROVE_OPTION:
					File file = fileChooser.getSelectedFile();
					if(file == null)
						return;
					lg.exportGuest(file);
					break;
				case JFileChooser.CANCEL_OPTION:
					break;
				case JFileChooser.ERROR_OPTION:
					System.out.println("fileChooser error");
					break;
				}
			}
			else if(obj == btn3_Export){
				fileChooser = new JFileChooser(".");
				fileChooser.setFileFilter(new csvFilter());
				int choice = fileChooser.showSaveDialog(frame);
				
				switch(choice){
				case JFileChooser.APPROVE_OPTION:
					File file = fileChooser.getSelectedFile();
					if(file == null)
						return;
					lg.exportProgramme(file);
					break;
				case JFileChooser.CANCEL_OPTION:
					break;
				case JFileChooser.ERROR_OPTION:
					System.out.println("fileChooser error");
					break;
				}
			}
			else if(obj == btn6_Export){
				fileChooser = new JFileChooser(".");
				fileChooser.setFileFilter(new csvFilter());
				int choice = fileChooser.showSaveDialog(frame);
				
				switch(choice){
				case JFileChooser.APPROVE_OPTION:
					File file = fileChooser.getSelectedFile();
					if(file == null)
						return;
					lg.exportExpense(file);
					break;
				case JFileChooser.CANCEL_OPTION:
					break;
				case JFileChooser.ERROR_OPTION:
					System.out.println("fileChooser error");
					break;
				}
			}
			else if(obj == mntmLoadEvent || obj == btn0_Load){
				//checks if current event needs to be saved, if so go on to save file
				if(!lg.getSavedStatus()){
					int ans = JOptionPane.showConfirmDialog(null, "Do you want to save your current event?");
					
					if(ans == 0){
						fileChooser = new JFileChooser(".");
						fileChooser.setFileFilter(new mirFilter());
						int choice = fileChooser.showSaveDialog(frame);
						
						switch(choice){
						case JFileChooser.APPROVE_OPTION:
							File file = fileChooser.getSelectedFile();
							if(file == null)
								return;
							lg.saveEvent(file);
							break;
						case JFileChooser.CANCEL_OPTION:
							break;
						case JFileChooser.ERROR_OPTION:
							System.out.println("fileChooser error");
							break;
						}
						
					}
					else if(ans == 2)
						return;
				}
				
				//do actual loading here
				fileChooser = new JFileChooser(".");
				fileChooser.setFileFilter(new mirFilter());
				int choice = fileChooser.showOpenDialog(frame);
				
				switch(choice){
				case JFileChooser.APPROVE_OPTION:
					File file = fileChooser.getSelectedFile();
					
					if(file == null)
						return;
					try{
						currEventFileDirectory = file;
						lg.loadEvent(file);
					} catch (Exception ex){
						JOptionPane.showMessageDialog(new JFrame(), "Error importing Event file. Make sure file is valid.");
					}
					break;
				case JFileChooser.CANCEL_OPTION:
					break;
				case JFileChooser.ERROR_OPTION:
					System.out.println("fileChooser error");
					break;
				}
				
				updateAll();
				lg.setSavedStatus(true);
			}
			else if(obj == mntmSave){
				//first check if there's existing save directory
				//if not use save as
				//else just save
				
				if(currEventFileDirectory == null){
					fileChooser = new JFileChooser(".");
					fileChooser.setFileFilter(new mirFilter());
					int choice = fileChooser.showSaveDialog(frame);
					
					switch(choice){
					case JFileChooser.APPROVE_OPTION:
						File file = fileChooser.getSelectedFile();
						if(file == null)
							return;
						
						currEventFileDirectory = file;
						lg.saveEvent(file);
						break;
					case JFileChooser.CANCEL_OPTION:
						break;
					case JFileChooser.ERROR_OPTION:
						System.out.println("fileChooser error");
						break;
					}
				}
				else
					lg.saveEvent(currEventFileDirectory);
			}
			else if (obj == mntmSaveAs){
				fileChooser = new JFileChooser(".");
				fileChooser.setFileFilter(new mirFilter());
				int choice = fileChooser.showSaveDialog(frame);
				
				switch(choice){
				case JFileChooser.APPROVE_OPTION:
					File file = fileChooser.getSelectedFile();
					if(file == null)
						return;
					
					currEventFileDirectory = file;
					lg.saveEvent(file);
					break;
				case JFileChooser.CANCEL_OPTION:
					break;
				case JFileChooser.ERROR_OPTION:
					System.out.println("fileChooser error");
					break;
				}
			}
		}
	};
	
	/*
	 * MouseListener
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		jtp.setSelectedIndex(jtp.getSelectedIndex()+1);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	/*
	 * FocusListener Override
	 */
	@Override
	public void focusGained(FocusEvent e){
		
	}
	@Override
	public void focusLost(FocusEvent e){
		Object obj = e.getSource();
		if(obj == textField1_EventName){
			lg.setEventName(textField1_EventName.getText());
			//System.out.println(lg.getEventName());
		}
		else if(obj == textArea1_EventDescription){
			lg.setEventDes(textArea1_EventDescription.getText());
			//System.out.println(lg.getEventDes());
		}
		else if(obj == textField1_budget){
			try{
				lg.setBudget(Double.parseDouble(textField1_budget.getText()));
				textPane4_Budget.setText(String.valueOf("$"+lg.calculateHotelBudget()));				
				updateStep6();
			} catch(NumberFormatException ex){
				textField1_budget.setText("0");
				textPane4_Budget.setText("0");
				textPane6_TotalBudget.setText("0");
			}
			//System.out.println(lg.getBudget());
		}
	
	}
	
	
	/******************************************
	 * 
	 * FileFilters start here
	 * 
	 ******************************************/
	class csvFilter extends javax.swing.filechooser.FileFilter{
		@Override
		public boolean accept(File file) {
			if(file.getName().toUpperCase().endsWith(".CSV")){
				return true;
			}
			if(file.isDirectory())
				return true;
			return false;
		}

		@Override
		public String getDescription() {
			return "*.csv";
		}
	};
	
	class mirFilter extends javax.swing.filechooser.FileFilter{
		@Override
		public boolean accept(File file){
			if(file.getName().toUpperCase().endsWith(".MIR")){
				return true;
			}
			if(file.isDirectory())
				return true;
			return false;
		}
		
		@Override
		public String getDescription(){
			return "*.mir";
		}
	};
	
	public class TableCellWrapText extends JTextArea implements TableCellRenderer{  
		  
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override  
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {  
	        this.setText((String)value);  
	        this.setWrapStyleWord(true);  
	        this.setLineWrap(true);   
	        this.setFont(new Font("Tahoma", Font.PLAIN, 11));
	  
	        //set the JTextArea to the width of the table column  
	        setSize(table.getColumnModel().getColumn(column).getWidth(),getPreferredSize().height);  
	        if (table.getRowHeight(row) != getPreferredSize().height) {  
	            //set the height of the table row to the calculated height of the JTextArea  
	            table.setRowHeight(row, getPreferredSize().height);  
	        }  
	  
	        return this;  
	    }  
	  
	}
	
	//Method to adjust column width to fit the longest string in the column
	public static void calcColumnWidths(JTable table) {
	    JTableHeader header = table.getTableHeader();

	    TableCellRenderer defaultHeaderRenderer = null;

	    if (header != null)
	        defaultHeaderRenderer = header.getDefaultRenderer();

	    TableColumnModel columns = table.getColumnModel();
	    TableModel data = table.getModel();

	    int margin = columns.getColumnMargin(); // only JDK1.3

	    int rowCount = data.getRowCount();

	    int totalWidth = 0;

	    for (int i = columns.getColumnCount() - 1; i >= 0; --i)
	    {
	        TableColumn column = columns.getColumn(i);
	            
	        int columnIndex = column.getModelIndex();
	            
	        int width = -1; 

	        TableCellRenderer h = column.getHeaderRenderer();
	          
	        if (h == null)
	            h = defaultHeaderRenderer;
	            
	        if (h != null) // Not explicitly impossible
	        {
	            Component c = h.getTableCellRendererComponent
	                   (table, column.getHeaderValue(),
	                    false, false, -1, i);
	                    
	            width = c.getPreferredSize().width;
	        }
	       
	        for (int row = rowCount - 1; row >= 0; --row)
	        {
	            TableCellRenderer r = table.getCellRenderer(row, i);
	                 
	            Component c = r.getTableCellRendererComponent
	               (table,
	                data.getValueAt(row, columnIndex),
	                false, false, row, i);
	        
	                width = Math.max(width, c.getPreferredSize().width);
	        }

	        if (width >= 0)
	            column.setPreferredWidth(width + margin); 
	        else
	            ; 
	            
	        totalWidth += column.getPreferredWidth();
	    }

	}

	
}
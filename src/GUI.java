import java.util.*;
import java.util.zip.*;
import java.awt.*;
import java.awt.event.*;
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
	
	private JFileChooser fileChooser = new JFileChooser();
	private JTabbedPane jtp;
	
	private JMenu mnFile;
	private JMenuBar menuBar;
	private JMenuItem mntmCreateNewEvent;
	private JMenuItem mntmLoadEvent;
	private JMenuItem mntmSaveEvent;
	
	private Vector<String> guestCols;
	private Vector<String> programmeCols;
	
	//GUI0 objects
	private JLabel lbl0_Step1;
	private JLabel lbl0_Step2;
	private JLabel lbl0_Step3;
	private JLabel lbl0_Step4;
	private JTextPane textPane0_EventName;
	private JButton btn0_Load;
	private JButton btn0_New;
	
	//GUI1 objects
	private JPanel panel1;
	private JTextField textField1_EventName;
	private JTextField textField1_budget;
	private JComboBox<String> comboBox1;
	private JDateChooser dateChooser1_StartDate;
	private JSpinner spinner1_StartTimeH;
	private JSpinner spinner1_StartTimeM;
	private JDateChooser dateChooser1_EndDate;
	private JSpinner spinner1_EndTimeH;
	private JSpinner spinner1_EndTimeM;
	private JTextArea textArea1_EventDescription;
	private JRadioButton rdbtn1_Lunch;
	private JRadioButton rdbtn1_Dinner;
	
	//GUI2 objects
	private JPanel panel2;
	private JScrollPane scrollPane2;
	private JButton btn2_Export;
	private JButton btn2_Load;
	private JButton btn2_Next;
	//private JButton btn2_Search;
	private JButton btn2_AddContact;
	private JTable table2;
	//private JTextField textField2_Search;
	private JCheckBox chckbx2_GuestListFinalised;
	
	//GUI3 objects
	private JPanel panel3;
	private JButton btn3_Export;
	private JButton btn3_Next;
	private JTable table3;
	private JScrollPane scrollPane3;
	private JCheckBox chckbx3_ProgrammeScheduleFinalised;
	
	//GUI4 objects
	private JButton btn4_Suggest;
	//private JButton btn4_Next;
	private JTextPane textPane4_HotelDetails;
	private JList<String> list4;
	private JSlider slider4;
	private JTextPane textPane4_Guests;
	private JTextPane textPane4_Budget;
	private JCheckBox chckbx4_3Star;
	private JCheckBox chckbx4_4Star;
	private JCheckBox chckbx4_5Star;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	/*
	 * Constructor
	 */
	public GUI(final Logic lg) {
		this.lg = lg;
    	
        setTitle("Manage It Right! v0.1");
        jtp = new JTabbedPane();
        jtp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtp.setTabPlacement(JTabbedPane.LEFT);
        getContentPane().add(jtp, BorderLayout.CENTER);
        
        /*
         * Drawing of individual tabs
         */
    	GUI0();
    	GUI1();
    	GUI2();
    	GUI3();
    	GUI4();
        
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
        				fileChooser.showSaveDialog(new JFrame());
        				File file = fileChooser.getSelectedFile();
        				if(file == null)
        					return;
        				lg.saveEvent(file);
        				lg.createNewEvent();
        				updateAll();
        				break;
        			case 1:
        				lg.createNewEvent();
        				updateAll();
        				jtp.setSelectedIndex(1);
        				break;
        			case 2:
        				break;
        			}
        		}
        	}
        });
        
        mntmLoadEvent = new JMenuItem("Load Event");
        mnFile.add(mntmLoadEvent);
        mntmLoadEvent.addActionListener(new ExportImportButtonsListener());
        
        mntmSaveEvent = new JMenuItem("Save Event");
        mnFile.add(mntmSaveEvent);
        mntmSaveEvent.addActionListener(new ExportImportButtonsListener());
        
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
					//updateStep1();
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					//updateStep4();
					textPane4_Guests.setText(Integer.toString(lg.getGuestList().size()));
					break;
				}
				
			}
        	
        });
    }
	
	/*
	 * Draw everything in Overview tab
	 */
	private void GUI0(){
		JPanel panel0 = new JPanel();
        jtp.addTab("<html><body marginwidth=15 marginheight=15>Overview</body></html>", null, panel0, null);
        
        JLabel lbl0_Overview = new JLabel("Overview");
        lbl0_Overview.setBounds(10, 11, 76, 30);
        lbl0_Overview.setFont(new Font("Tahoma", Font.BOLD, 16));
        
        btn0_New = new JButton("New");
        btn0_New.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		if(!lg.getSavedStatus()){
        			int choice = JOptionPane.showConfirmDialog(new JFrame(), "Do you want to save the current event?");
        			switch(choice){
        			case 0:
        				fileChooser.showSaveDialog(new JFrame());
        				File file = fileChooser.getSelectedFile();
        				if(file == null)
        					return;
        				lg.saveEvent(file);
        				lg.createNewEvent();
        				updateAll();
        				break;
        			case 1:
        				lg.createNewEvent();
        				updateAll();
        				jtp.setSelectedIndex(1);
        				break;
        			case 2:
        				break;
        			}
        		}
        	}
        });
        btn0_New.setBounds(350, 490, 80, 30);
        btn0_New.setFont(new Font("Tahoma", Font.BOLD, 12));
        
        btn0_Load = new JButton("Load");
        btn0_Load.setBounds(450, 490, 80, 30);
        btn0_Load.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn0_Load.addActionListener(new ExportImportButtonsListener());
        
        JButton btn0_Continue = new JButton("Continue");
        btn0_Continue.addMouseListener(this);
        btn0_Continue.setBounds(550, 490, 90, 30);
        btn0_Continue.setFont(new Font("Tahoma", Font.BOLD, 12));
        panel0.setLayout(null);
        panel0.add(lbl0_Overview);
        
        textPane0_EventName = new JTextPane();
        textPane0_EventName.setEditable(false);
        textPane0_EventName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        textPane0_EventName.setBounds(136, 11, 504, 30);
        panel0.add(textPane0_EventName);
        
        lbl0_Step1 = new JLabel();
        lbl0_Step1.setText("Event Details are empty.");
		lbl0_Step1.setForeground(Color.RED);
        lbl0_Step1.setBounds(10, 52, 630, 30);
        panel0.add(lbl0_Step1);
        
        lbl0_Step2 = new JLabel("Guest List is empty.");
        lbl0_Step2.setForeground(Color.RED);
        lbl0_Step2.setBounds(10, 116, 630, 30);
        panel0.add(lbl0_Step2);
        
        lbl0_Step3 = new JLabel("Programme Schedule is not finalised.");
        lbl0_Step3.setForeground(Color.RED);
        lbl0_Step3.setBounds(10, 181, 630, 30);
        panel0.add(lbl0_Step3);
        
        lbl0_Step4 = new JLabel("Location not chosen.");
        lbl0_Step4.setForeground(Color.RED);
        lbl0_Step4.setBounds(10, 249, 630, 30);
        panel0.add(lbl0_Step4);
        panel0.add(btn0_New);
        panel0.add(btn0_Load);
        panel0.add(btn0_Continue);
	}
	
	/*
	 * Draws everything in Step 1
	 */
	private void GUI1(){
		panel1 = new JPanel();
        jtp.addTab("<html><body marginwidth=15 marginheight=15>Step 1:<br>Event Details</body></html>", null, panel1, "Manage the event details.");
        panel1.setLayout(null);
        
        JLabel lbl1_EventDetails = new JLabel("Event Details");
        lbl1_EventDetails.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl1_EventDetails.setBounds(10, 10, 126, 30);
        panel1.add(lbl1_EventDetails);
        
        JLabel lbl1_EventType = new JLabel("Event Type:");
        lbl1_EventType.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_EventType.setBounds(10, 59, 100, 14);
        panel1.add(lbl1_EventType);
                  
        comboBox1 = new JComboBox<String>();
        comboBox1.setBounds(120, 56, 195, 20);
        panel1.add(comboBox1);
        comboBox1.addItem("Select Event Type...");
        comboBox1.addItem("Anniversary");
        comboBox1.addItem("Award Ceremony");
        comboBox1.addItem("Birthday Party");
        comboBox1.addItem("Dinner and Dance");
        comboBox1.addItem("Festive Party");
        comboBox1.addItem("Seminar");
        comboBox1.addItem("Social Event");
        comboBox1.addItem("Talk / Speech");
        comboBox1.addItem("Wedding");
        comboBox1.addItem("Workshop");
        comboBox1.addActionListener(new ComboBox1Listener());
        
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
//					System.out.println(StartDate);      	        
        		}	
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
//					System.out.println(StartDate);    
				}
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
        
        JLabel lblMeal = new JLabel("Meal:");
        lblMeal.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblMeal.setBounds(10, 224, 46, 14);
        panel1.add(lblMeal);
        
        rdbtn1_Lunch = new JRadioButton("Lunch");
        rdbtn1_Lunch.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		lg.setMealType(0);
        	}
        });
        buttonGroup.add(rdbtn1_Lunch);
        rdbtn1_Lunch.setBounds(120, 221, 109, 23);
        panel1.add(rdbtn1_Lunch);
        
        rdbtn1_Dinner = new JRadioButton("Dinner");
        rdbtn1_Dinner.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		lg.setMealType(1);
        	}
        });
        buttonGroup.add(rdbtn1_Dinner);
        rdbtn1_Dinner.setBounds(264, 221, 109, 23);
        panel1.add(rdbtn1_Dinner);
	}
	
	/*
	 * Drawing of everything in Step 2
	 */
	private void GUI2(){
			panel2 = new JPanel();
	        jtp.addTab("<html><body marginwidth=15 marginheight=15>Step 2:<br>Guest List</body></html>", null, panel2, "Manage the list of guests attending the event.");
	        panel2.setLayout(null);
	        
	        JLabel lbl2_GuestList = new JLabel("Guest List");
	        lbl2_GuestList.setFont(new Font("Tahoma", Font.BOLD, 16));
	        lbl2_GuestList.setBounds(10, 10, 81, 30);
	        panel2.add(lbl2_GuestList);
	        
	        btn2_AddContact = new JButton("Add Contact");
	        btn2_AddContact.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent arg0) {
	        	}
	        });
	        
	        btn2_AddContact.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        btn2_AddContact.setBounds(10, 58, 119, 23);
	        panel2.add(btn2_AddContact);
	        
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
	        btn2_Export.setBounds(11, 490, 80, 30);
	        panel2.add(btn2_Export);
	        btn2_Export.addActionListener(new ExportImportButtonsListener());
	        
	        btn2_Load = new JButton("Load");
	        btn2_Load.setFont(new Font("Tahoma", Font.BOLD, 12));
	        btn2_Load.setBounds(460, 490, 80, 30);
	        panel2.add(btn2_Load);
	        btn2_Load.addActionListener(new ExportImportButtonsListener());
	        
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
        jtp.addTab("<html><body marginwidth=15 marginheight=15>Step 3:<br>Programme</body></html>", null, panel3, "Manage the flow of events.");
        panel3.setLayout(null);
        
        JLabel lbl3_ProgrammeSchedule = new JLabel("Programme Schedule");
        lbl3_ProgrammeSchedule.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl3_ProgrammeSchedule.setBounds(10, 10, 191, 30);
        panel3.add(lbl3_ProgrammeSchedule);
        
        programmeCols = new Vector<String>();
        programmeCols.add("Start Time");
        programmeCols.add("End Time");
        programmeCols.add("Programme");
        programmeCols.add("In-Charge");
        
        createTable3(new Vector<Vector<String>>(), programmeCols);
        
        chckbx3_ProgrammeScheduleFinalised = new JCheckBox("Programme Schedule Finalised");
        chckbx3_ProgrammeScheduleFinalised.setBounds(10, 447, 217, 23);
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
        btn3_Export.addActionListener(new ExportImportButtonsListener());
        
        
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
        jtp.addTab("<html><body marginwidth=15 marginheight=8>Step 4:<br>Hotel<br>Suggestions</body></html>", null, panel4, "View hotel suggestions");
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
        
        
        textPane4_Budget = new JTextPane();
        textPane4_Budget.setEditable(false);
        textPane4_Budget.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane4_Budget.setBounds(533, 109, 107, 20);
        panel4.add(textPane4_Budget);
        
        
        slider4.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {        		
        		textPane4_Budget.setText(Double.toString(lg.calculateHotelBudget()));
        		lg.setBudgetRatio(slider4.getValue());
        	}
        });
        
        
        btn4_Suggest = new JButton("Suggest");
        btn4_Suggest.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn4_Suggest.setBounds(550, 167, 90, 30);
        panel4.add(btn4_Suggest);
        btn4_Suggest.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		lg.clearHotelSuggestions();
        		int budgetRatio = slider4.getValue();
        		try{
	        		if(chckbx4_3Star.isSelected()){
	        			lg.hotelSuggest(3, budgetRatio);
	        		}
	        		if(chckbx4_4Star.isSelected()){
	        			lg.hotelSuggest(4, budgetRatio);
	        		}
	        		
	        		if(chckbx4_5Star.isSelected()){
	        			lg.hotelSuggest(5, budgetRatio);
        		}
        		} catch(IOException io){
        			JOptionPane.showMessageDialog(new JFrame(), "Hotel data file could not be found. Please ensure 'hotelData' is found in the Data directory in your program folder.");
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
			}
        });
        
        textPane4_HotelDetails = new JTextPane();
        textPane4_HotelDetails.setEditable(false);
        textPane4_HotelDetails.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane4_HotelDetails.setBounds(266, 220, 374, 250);
        panel4.add(textPane4_HotelDetails);
        
        /* Next button for v0.2
        btn4_Next = new JButton("Next");
        btn4_Next.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn4_Next.setBounds(560, 490, 80, 30);
        panel4.add(btn4_Next);
        */
	}
	
	/*
	 * Method for creation and initlialisation of table3, found in 
	 */
	void createTable3(Vector<Vector<String>> data, Vector<String> cols){
		table3 = new JTable(data, cols);
		table3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table3.getTableHeader().setReorderingAllowed(false);
        scrollPane3 = new JScrollPane(table3);
        scrollPane3.setBounds(10, 70, 600, 370);
        panel3.add(scrollPane3);
        
        scrollPane3.setViewportView(table3);
        table3.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table3.setPreferredScrollableViewportSize(new Dimension(600,370));
        table3.setFillsViewportHeight(true);
        table3.setColumnSelectionAllowed(true);
        table3.setCellSelectionEnabled(true);
        table3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table3.setAutoCreateRowSorter(true);
        
        table3.getColumnModel().getColumn(0).setPreferredWidth(100);
        table3.getColumnModel().getColumn(1).setPreferredWidth(100);
        table3.getColumnModel().getColumn(2).setPreferredWidth(250);
        table3.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        //table3.setDefaultRenderer(Object.class, new LineWrapCellRenderer());
        final DefaultTableModel modelProgramme = (DefaultTableModel)table3.getModel();
        
        table3.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		if (e.getKeyCode()==KeyEvent.VK_TAB) {
        			table3.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0, false), "selectNextColumnCell");        		     			
        		} 
        		if (e.getKeyCode()== KeyEvent.VK_INSERT || e.getKeyCode()== KeyEvent.VK_ENTER ) {
        			modelProgramme.addRow(new Vector<Object>(4));  
        			lg.addProgramme();
        		}
        		if (e.getKeyCode()==KeyEvent.VK_DELETE){
        			int rowNumber = table3.getSelectedRow();
        			modelProgramme.removeRow(rowNumber);
        			lg.removeProgramme(rowNumber);
        			if(lg.getProgrammeSchedule().size() == 0)
        				chckbx3_ProgrammeScheduleFinalised.setEnabled(false);
        		}
        	}
        	@Override
        	public void keyReleased(KeyEvent e) {
        	}
        	@Override
        	public void keyTyped(KeyEvent e) {
        	}
        });
        
        table3.getModel().addTableModelListener(new TableModelListener(){
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
        		if(column == 0 || column == 1){
        			try{
        				Integer.parseInt(data);
        			} catch(NumberFormatException ex){
        				data = "0";
        				model.setValueAt("0", row, column);
        				return;
        			}
        		}
        		lg.setProgrammeInfo(row, columnName, data);
        		
        		//if there exist 1 guest, and all fields are updated, chckBx3_ProgrammeScheduleFinalised should be enabled.
        		if(lg.completedProgrammeFields(row)){
        			if(chckbx3_ProgrammeScheduleFinalised == null)
        				return;
        			chckbx3_ProgrammeScheduleFinalised.setEnabled(true);
        		}

        		//if chckBx is checked, it shld be unchecked
        		if(chckbx3_ProgrammeScheduleFinalised.isSelected()){
        			chckbx3_ProgrammeScheduleFinalised.setSelected(false);
        			lg.setProgrammeScheduleFinalised(false);
        		}
        		
        	}
        });
	}
	
	/*
	 * Method for creation and initilisation of table2 used in Step 2
	 */
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
        table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        
        MouseListener [] listeners = btn2_AddContact.getMouseListeners();
        if(listeners.length != 0){
        	for(int i = 0; i < listeners.length; i++){
        		btn2_AddContact.removeMouseListener(listeners[i]);
        	}
        }
        
        btn2_AddContact.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		lg.addGuest();
        		modelGuest.addRow(new Vector<String>(6));
        		modelGuest.setValueAt("Select", modelGuest.getRowCount()-1, 1);       		
        		textPane4_Guests.setText(String.valueOf(lg.getGuestList().size()));
        	}
        });
        
        table2.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		if (e.getKeyCode()==KeyEvent.VK_TAB) {
        			table2.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0, false), "selectNextColumnCell");        		     			
        		} 
        		if (e.getKeyCode()== KeyEvent.VK_INSERT || e.getKeyCode()== KeyEvent.VK_ENTER ) {
        			lg.addGuest();
            		modelGuest.addRow(new Vector<String>(6));
            		modelGuest.setValueAt("Select", modelGuest.getRowCount()-1, 1);
            		textPane4_Guests.setText(String.valueOf(lg.getGuestList().size()));
        		}
        		if (e.getKeyCode()==KeyEvent.VK_DELETE){
        			int rowNumber = table2.getSelectedRow();
        			if(rowNumber == -1)
        				return;
        			modelGuest.removeRow(rowNumber);
        			lg.removeGuest(rowNumber);
        			if(lg.getGuestList().size() == 0)
        				chckbx2_GuestListFinalised.setEnabled(false);
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
        		if(lg.completedGuestFields(row)){
        			if(chckbx2_GuestListFinalised == null)
        				return;
        			chckbx2_GuestListFinalised.setEnabled(true);
        		}
    			
        		//if chckBx is checked, it shld be unchecked
        		if(chckbx2_GuestListFinalised.isSelected()){
        			chckbx2_GuestListFinalised.setSelected(false);
        			lg.setGuestListFinalised(false);
        		}
        			
        		
        	}
        });
	}
	
	//refresh all fields
	void updateAll(){
		updateStep1();
		updateStep2();
		updateStep3();
		updateStep4();
		updateStep0();
	}
	
	void updateStep0() {
		
		switch(lg.step1Status()) {
			case 0:
				textPane0_EventName.setText(lg.getEventName());
				lbl0_Step1.setText("Event Details are empty.");
				lbl0_Step1.setForeground(Color.RED);
				break;
			case 1:
				lbl0_Step1.setText("Event Details are incomplete.");
				lbl0_Step1.setForeground(Color.RED);
				break;
			case 2:
				lbl0_Step1.setText("Event Details are completed.");
				lbl0_Step1.setForeground(Color.GREEN);
				break;
		}
		
		switch(lg.step2Status()){
			case 0:
				lbl0_Step2.setText("Guest list is empty.");
				lbl0_Step2.setForeground(Color.RED);
				break;
			case 1:
				lbl0_Step2.setText("Guest list contains missing details.");
				lbl0_Step2.setForeground(Color.RED);
				break;
			case 2:
				lbl0_Step2.setText("Guest list is not finalised.");
				lbl0_Step2.setForeground(Color.RED);
				break;
			case 3:
				lbl0_Step2.setText("Guest list is finalised.");
				lbl0_Step2.setForeground(Color.GREEN);
				break;
		}
		
		switch(lg.step3Status()){
			case 0:
				lbl0_Step3.setText("Programme Schedule is empty.");
				lbl0_Step3.setForeground(Color.RED);
				break;
			case 1:
				lbl0_Step3.setText("Programme Schedule contains missing details.");
				lbl0_Step3.setForeground(Color.RED);
				break;
			case 2:
				lbl0_Step3.setText("Programme Schedule is not finalised.");
				lbl0_Step3.setForeground(Color.RED);
				break;
			case 3:
				lbl0_Step3.setText("Programme Schedule is finalised.");
				lbl0_Step3.setForeground(Color.GREEN);
				break;
		}
		switch(lg.step4Status()){
			case 0:
				lbl0_Step4.setText("Location not selected.");
				lbl0_Step4.setForeground(Color.RED);
				break;
			case 1:
				lbl0_Step4.setText("Location has been selected.");
				lbl0_Step4.setForeground(Color.GREEN);
				break;
		}
	}
	
	void updateStep1(){
		//step 1
		comboBox1.setSelectedIndex(lg.getEventType() + 1);
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
		
	}
	
	void updateStep2(){	
		panel2.remove(scrollPane2);
		createTable2(lg.getGuestList(), guestCols);
		if(lg.getGuestList().size() >= 1)
			chckbx2_GuestListFinalised.setEnabled(true);
		if(lg.getGuestListFinalised() == true)
			chckbx2_GuestListFinalised.setSelected(true);
	}
	
	void updateStep3(){
		panel3.remove(scrollPane3);
		createTable3(lg.getProgrammeSchedule(), programmeCols);
		if(lg.getProgrammeSchedule().size() >= 1)
			chckbx3_ProgrammeScheduleFinalised.setEnabled(true);
		if(lg.getProgrammeScheduleFinalised() == true)
			chckbx3_ProgrammeScheduleFinalised.setSelected(true);
	}
	
	void updateStep4(){
		textPane4_Guests.setText(Integer.toString(lg.getGuestList().size()));
		textPane4_Budget.setText(Double.toString(lg.calculateHotelBudget()));	
		list4.setListData(lg.getSuggestedHotelsNames());
		list4.setSelectedIndex(lg.getSelectedHotelIdx());
		slider4.setValue(lg.getBudgetRatio());
	}
	
	/*
	 * Overrides for ComboBox1
	 */
	class ComboBox1Listener implements ActionListener{
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
				fileChooser.showOpenDialog(frame);
				File file = fileChooser.getSelectedFile();
				if(file == null)
					return;
				try{
					lg.importGuest(file);
				} catch(DataFormatException ex){
					JOptionPane.showMessageDialog(new JFrame(), "Error importing CSV file. Make sure file is properly formatted");
				}
				
				updateStep2();
			}
			else if(obj == btn2_Export){
				fileChooser.showSaveDialog(frame);
				File file = fileChooser.getSelectedFile();
				if(file == null)
					return;
				lg.exportGuest(file);
			}
			else if(obj == btn3_Export){
				fileChooser.showSaveDialog(frame);
				File file = fileChooser.getSelectedFile();
				if(file == null)
					return;
				lg.exportProgramme(file);
			}
			else if(obj == mntmLoadEvent || obj == btn0_Load){
				if(!lg.getSavedStatus()){
					int ans = JOptionPane.showConfirmDialog(null, "Do you want to save your current event?");
					if(ans == 0){
						fileChooser.showSaveDialog(frame);
						File file = fileChooser.getSelectedFile();
						if(file == null)
							return;
						lg.saveEvent(file);
					}
					else if(ans == 2)
						return;
				}
				fileChooser.showOpenDialog(frame);
				File file = fileChooser.getSelectedFile();
				if(file == null)
					return;
				try{
					lg.loadEvent(file);
				} catch (Exception ex){
					JOptionPane.showMessageDialog(new JFrame(), "Error importing Event file. Make sure file is valid.");
				}
				updateAll();
				lg.setSavedStatus(true);
			}
			else if (obj == mntmSaveEvent){
				fileChooser.showSaveDialog(frame);
				File file = fileChooser.getSelectedFile();
				if(file == null)	//no file selected
					return;
				lg.saveEvent(file);
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
				textPane4_Budget.setText(String.valueOf(lg.calculateHotelBudget()));
			} catch(NumberFormatException ex){
				textField1_budget.setText("0");
				textPane4_Budget.setText("0");
			}
			//System.out.println(lg.getBudget());
		}
	}
	
	
	/*
    private int getPreferredRowHeight(JTable table, int r, int margin) {
		return 0;
	}
    
    /*
     *Custom renderer for wrapping text in cell
     */
    /*
    class LineWrapCellRenderer extends JTextArea implements TableCellRenderer  {
    	int rowHeight = 0;  // current max row height for this scan           	
    	
		public Component getTableCellRendererComponent(
    	        JTable table,
    	        Object value,
    	        boolean isSelected,
    	        boolean hasFocus,
    	        int row,
    	        int column)
    	{
				    
			setText((String) value);
    	    setWrapStyleWord(true);
    	    setLineWrap(true);        	           	         	                  
            
    	    int colWidth = table.getColumnModel().getColumn(column).getWidth();

    	    
    	    setSize(new Dimension(colWidth, 1)); 

    	    // get the text area preferred height and add the row margin
    	    int height = getPreferredSize().height + table.getRowMargin();

    	    // ensure the row height fits the cell with most lines
    	    if (column == 0 || height > rowHeight) {
    	        table.setRowHeight(row, height);
    	        rowHeight = height;
    	    }
    	    return this;
    	}
	
    }
    */
}
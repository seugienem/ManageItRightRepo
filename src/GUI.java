import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.JMenu;
import javax.swing.JSeparator;

import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Choice;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JList;
import javax.swing.JRadioButton;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.UIManager;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import java.awt.Cursor;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JTextArea;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.toedter.calendar.JDateChooser;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//test 1

public class GUI extends JFrame {
	private JTextField textField1_EventName;
	private JTable table2;
	private JTextField textField1_budget;
	private JTextField textField2_Search;
	private JTable table3;
	
	public GUI() { // GUI
    	
        setTitle("Manage It Right! v0.1");
        final JTabbedPane jtp = new JTabbedPane();
        jtp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jtp.setTabPlacement(JTabbedPane.LEFT);
        getContentPane().add(jtp, BorderLayout.CENTER);
    	
// Overview Tab
        JPanel panel0 = new JPanel();
        jtp.addTab("<html><body marginwidth=15 marginheight=15>Overview</body></html>", null, panel0, null);
        
        JLabel lbl0_Overview = new JLabel("Overview");
        lbl0_Overview.setBounds(10, 11, 76, 30);
        lbl0_Overview.setFont(new Font("Tahoma", Font.BOLD, 16));
        
        JButton btn0_New = new JButton("New");
        btn0_New.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		jtp.setSelectedIndex(jtp.getSelectedIndex()+1);
        	}
        });
        btn0_New.setBounds(350, 490, 80, 30);
        btn0_New.setFont(new Font("Tahoma", Font.BOLD, 12));
        
        JButton btn0_Load = new JButton("Load");
        btn0_Load.setBounds(450, 490, 80, 30);
        btn0_Load.setFont(new Font("Tahoma", Font.BOLD, 12));
        
        JButton btn0_Continue = new JButton("Continue");
        btn0_Continue.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent arg0) {
        		jtp.setSelectedIndex(jtp.getSelectedIndex()+1);
        	}
        });
        btn0_Continue.setBounds(550, 490, 90, 30);
        btn0_Continue.setFont(new Font("Tahoma", Font.BOLD, 12));
        panel0.setLayout(null);
        panel0.add(lbl0_Overview);
        
        JTextPane textPane0_EventName = new JTextPane();
        textPane0_EventName.setEditable(false);
        textPane0_EventName.setText("Event Name");
        textPane0_EventName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        textPane0_EventName.setBounds(136, 11, 504, 30);
        panel0.add(textPane0_EventName);
        
        JLabel lbl0_Step1 = new JLabel("Event details are not completed.");
        lbl0_Step1.setForeground(Color.RED);
        lbl0_Step1.setBounds(10, 52, 630, 30);
        panel0.add(lbl0_Step1);
        
        JLabel lbl0_Step2 = new JLabel("Guest List is empty.");
        lbl0_Step2.setForeground(Color.RED);
        lbl0_Step2.setBounds(10, 116, 630, 30);
        panel0.add(lbl0_Step2);
        
        JLabel lbl0_Step3 = new JLabel("Programme Schedule is not finalised.");
        lbl0_Step3.setForeground(Color.RED);
        lbl0_Step3.setBounds(10, 181, 630, 30);
        panel0.add(lbl0_Step3);
        
        JLabel lbl0_Step4 = new JLabel("Location not chosen.");
        lbl0_Step4.setForeground(Color.RED);
        lbl0_Step4.setBounds(10, 249, 630, 30);
        panel0.add(lbl0_Step4);
        panel0.add(btn0_New);
        panel0.add(btn0_Load);
        panel0.add(btn0_Continue);
        
// Step 1: Event Details
        JPanel panel1 = new JPanel();
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
                  
        final JComboBox<String> comboBox1 = new JComboBox<String>();
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
        
        
        JLabel lbl1_EventName = new JLabel("Event Name:");
        lbl1_EventName.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_EventName.setBounds(10, 100, 100, 14);
        panel1.add(lbl1_EventName);
        
        textField1_EventName = new JTextField();
        textField1_EventName.setBounds(120, 97, 435, 20);
        panel1.add(textField1_EventName);
        textField1_EventName.setColumns(10);        
        
        comboBox1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {				
				String str = (String)comboBox1.getSelectedItem();
				textField1_EventName.setText(str);	// For testing purposes to display chosen event type 
			}
        });
       
        JLabel lbl1_EventDate = new JLabel("Start Date:");
        lbl1_EventDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_EventDate.setBounds(10, 142, 73, 14);
        panel1.add(lbl1_EventDate);
        
        JDateChooser dateChooser1_StartDate = new JDateChooser();
        dateChooser1_StartDate.setDateFormatString("d MMM, yyyy");
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
        
        JSpinner spinner1_StartTimeH = new JSpinner();
        spinner1_StartTimeH.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        spinner1_StartTimeH.setBounds(357, 140, 40, 20);
        panel1.add(spinner1_StartTimeH);
        
        JLabel lbl1_StartTimeM = new JLabel("M");
        lbl1_StartTimeM.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_StartTimeM.setBounds(401, 143, 14, 14);
        panel1.add(lbl1_StartTimeM);
        
        JSpinner spinner1_StartTimeM = new JSpinner();
        spinner1_StartTimeM.setModel(new SpinnerNumberModel(0, 0, 59, 1));
        spinner1_StartTimeM.setBounds(417, 140, 40, 20);
        panel1.add(spinner1_StartTimeM);
        
        JLabel lbl1_EndDate = new JLabel("End Date:");
        lbl1_EndDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_EndDate.setBounds(10, 182, 60, 14);
        panel1.add(lbl1_EndDate);
        
        JDateChooser dateChooser1_EndDate = new JDateChooser();
        dateChooser1_EndDate.setDateFormatString("d MMM, yyyy");
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
        
        JSpinner spinner1_EndTimeH = new JSpinner();
        spinner1_EndTimeH.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        spinner1_EndTimeH.setBounds(357, 182, 40, 20);
        panel1.add(spinner1_EndTimeH);
        
        JLabel label1_EndTimeM = new JLabel("M");
        label1_EndTimeM.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label1_EndTimeM.setBounds(401, 185, 14, 14);
        panel1.add(label1_EndTimeM);
        
        JSpinner spinner1_EndTimeM = new JSpinner();
        spinner1_EndTimeM.setModel(new SpinnerNumberModel(0, 0, 59, 1));
        spinner1_EndTimeM.setBounds(417, 182, 40, 20);
        panel1.add(spinner1_EndTimeM);
        
        JLabel lbl1_EventDescription = new JLabel("Event Description:");
        lbl1_EventDescription.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl1_EventDescription.setBounds(10, 224, 100, 14);
        panel1.add(lbl1_EventDescription);
        
        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBounds(120, 220, 435, 171);
        panel1.add(scrollPane1);
        
        JTextArea textArea1_EventDescription = new JTextArea();
        scrollPane1.setViewportView(textArea1_EventDescription);
        textArea1_EventDescription.setDragEnabled(true);
        textArea1_EventDescription.setLineWrap(true);
        textArea1_EventDescription.setWrapStyleWord(true);
        
        JLabel lbl1_Budget = new JLabel("Budget:          $");
        lbl1_Budget.setFont(new Font("Tahoma", Font.BOLD, 12));
        lbl1_Budget.setBounds(10, 411, 100, 14);
        panel1.add(lbl1_Budget);
        
        textField1_budget = new JTextField();
        textField1_budget.setToolTipText("DD/MM/YYYY");
        textField1_budget.setColumns(10);
        textField1_budget.setBounds(120, 409, 117, 20);
        panel1.add(textField1_budget);
        
        JButton btn1_Next = new JButton("Next");
        btn1_Next.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		jtp.setSelectedIndex(jtp.getSelectedIndex()+1);
        	}
        });
        btn1_Next.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn1_Next.setBounds(560, 490, 80, 30);
        panel1.add(btn1_Next);
        
// Step 2: Guest List
        JPanel panel2 = new JPanel();
        jtp.addTab("<html><body marginwidth=15 marginheight=15>Step 2:<br>Guest List</body></html>", null, panel2, "Manage the list of guests attending the event.");
        panel2.setLayout(null);
        
        JLabel lbl2_GuestList = new JLabel("Guest List");
        lbl2_GuestList.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl2_GuestList.setBounds(10, 10, 81, 30);
        panel2.add(lbl2_GuestList);
        
        JButton btn2_AddContact = new JButton("Add Contact");
        btn2_AddContact.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btn2_AddContact.setBounds(10, 58, 119, 23);
        panel2.add(btn2_AddContact);
        
        textField2_Search = new JTextField();
        textField2_Search.setToolTipText("Search Guest List");
        textField2_Search.setBounds(351, 60, 175, 20);
        panel2.add(textField2_Search);
        textField2_Search.setColumns(10);
        
        JButton btn2_Search = new JButton("Search");
        btn2_Search.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btn2_Search.setBounds(536, 58, 77, 23);
        panel2.add(btn2_Search);
        
        table2 = new JTable();
        table2.setBounds(10, 92, 603, 353);
        panel2.add(table2);
        
        JCheckBox chckbx2_GuestListFinalised = new JCheckBox("Guest List Finalised");
        chckbx2_GuestListFinalised.setBounds(10, 452, 140, 23);
        panel2.add(chckbx2_GuestListFinalised);
        
        JButton btn2_Export = new JButton("Export");
        btn2_Export.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn2_Export.setBounds(11, 490, 80, 30);
        panel2.add(btn2_Export);
        
        JButton btn2_Load = new JButton("Load");
        btn2_Load.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn2_Load.setBounds(460, 490, 80, 30);
        panel2.add(btn2_Load);
        
        JButton btn2_Next = new JButton("Next");
        btn2_Next.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		jtp.setSelectedIndex(jtp.getSelectedIndex()+1);
        	}
        });
        btn2_Next.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn2_Next.setBounds(560, 490, 80, 30);
        panel2.add(btn2_Next);
        
// Step 3: Programme
        JPanel panel3 = new JPanel();
        jtp.addTab("<html><body marginwidth=15 marginheight=15>Step 3:<br>Programme</body></html>", null, panel3, "Manage the flow of events.");
        panel3.setLayout(null);
        
        JLabel lbl3_ProgrammeSchedule = new JLabel("Programme Schedule");
        lbl3_ProgrammeSchedule.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl3_ProgrammeSchedule.setBounds(10, 10, 191, 30);
        panel3.add(lbl3_ProgrammeSchedule);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 70, 600, 370);
        panel3.add(scrollPane);
        
        Vector<String> columnNames = new Vector<String>(4);
        columnNames.add("Start Time");
        columnNames.add("End Time");
        columnNames.add("Programme");
        columnNames.add("In-Charge");
        
        
        
        Vector < Vector <Object> > data =  new Vector < Vector <Object> >();
        data.add(new Vector<Object>(4));
        
        /*
        data.get(0).add(new Integer(1800));
        data.get(0).add(new Integer(1900));
        data.get(0).add("Arrival of Guests");
        data.get(0).add("Taeyeon");
        for (int i=0; i<15; i++)
        	data.add(new Vector<Object>(4));
        */
        
        table3 = new JTable(data,columnNames);
        table3.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table3.setPreferredScrollableViewportSize(new Dimension(600,370));
        table3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table3.setFillsViewportHeight(true);
        scrollPane.setViewportView(table3);
        table3.setColumnSelectionAllowed(true);
        table3.setCellSelectionEnabled(true);
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = table3.getColumnModel().getColumn(i);         
            if (i == 2) {
                column.setPreferredWidth(250); //third column is bigger
            } else {
                column.setPreferredWidth(15);
            }
        }
        
        
        
        table3.setRowHeight(25);
        for (int i = 0; i < table3.getModel().getRowCount(); i++) {
        	   for (int j = 0; j < table3.getModel().getColumnCount(); j++) {
        	      DefaultTableCellRenderer renderer =
        	         (DefaultTableCellRenderer)table3.getCellRenderer(i, j);
        	      renderer.setHorizontalAlignment(JTextField.CENTER);
        	   } 
        	} 
        
        JCheckBox chckbx3_ProgrammeScheduleFinalised = new JCheckBox("Programme Schedule Finalised");
        chckbx3_ProgrammeScheduleFinalised.setBounds(10, 447, 217, 23);
        panel3.add(chckbx3_ProgrammeScheduleFinalised);
        
        
        
        
        
        
        JButton btn3_Export = new JButton("Export");
        btn3_Export.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn3_Export.setBounds(460, 490, 80, 30);
        panel3.add(btn3_Export);
        
        JButton btn3_Next = new JButton("Next");
        btn3_Next.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		jtp.setSelectedIndex(jtp.getSelectedIndex()+1);
        	}
        });
        btn3_Next.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn3_Next.setBounds(560, 490, 80, 30);
        panel3.add(btn3_Next);
        
// Step 4: Location Suggestions
        JPanel panel4 = new JPanel();
        jtp.addTab("<html><body marginwidth=15 marginheight=8>Step 4:<br>Location<br>Suggestions</body></html>", null, panel4, "View hotel suggestions");
        panel4.setLayout(null);
        
        JLabel lbl4_LocationSelection = new JLabel("Location Selection");
        lbl4_LocationSelection.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl4_LocationSelection.setBounds(10, 10, 159, 30);
        panel4.add(lbl4_LocationSelection);
        
        JLabel lbl4_Preference = new JLabel("Preference: ");
        lbl4_Preference.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl4_Preference.setBounds(10, 42, 82, 20);
        panel4.add(lbl4_Preference);
        
        JCheckBox chckbx4_6Star = new JCheckBox("6 Star");
        chckbx4_6Star.setFont(new Font("Tahoma", Font.PLAIN, 12));
        chckbx4_6Star.setBounds(89, 42, 65, 20);
        panel4.add(chckbx4_6Star);
        
        JCheckBox chckbx4_5Star = new JCheckBox("5 Star");
        chckbx4_5Star.setFont(new Font("Tahoma", Font.PLAIN, 12));
        chckbx4_5Star.setBounds(171, 42, 65, 20);
        panel4.add(chckbx4_5Star);
        
        JCheckBox chckbx4_4Star = new JCheckBox("4 Star");
        chckbx4_4Star.setFont(new Font("Tahoma", Font.PLAIN, 12));
        chckbx4_4Star.setBounds(248, 43, 65, 20);
        panel4.add(chckbx4_4Star);
        
        JCheckBox chckbx4_3Star = new JCheckBox("3 Star");
        chckbx4_3Star.setFont(new Font("Tahoma", Font.PLAIN, 12));
        chckbx4_3Star.setBounds(329, 42, 65, 20);
        panel4.add(chckbx4_3Star);
        
        JTextPane textPane4_Guests = new JTextPane();
        textPane4_Guests.setEditable(false);
        textPane4_Guests.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane4_Guests.setBounds(533, 42, 107, 20);
        panel4.add(textPane4_Guests);
        
        JLabel lbl4_BudgetRatio = new JLabel("Budget Ratio: ");
        lbl4_BudgetRatio.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbl4_BudgetRatio.setBounds(10, 109, 82, 20);
        panel4.add(lbl4_BudgetRatio);
        
        JSlider slider4 = new JSlider();
        slider4.setPaintLabels(true);
        slider4.setPaintTicks(true);
        slider4.setMinorTickSpacing(5);
        slider4.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        slider4.setSnapToTicks(true);
        slider4.setMajorTickSpacing(10);
        slider4.setBounds(102, 101, 421, 45);
        panel4.add(slider4);
        
        JTextPane textPane4_Budget = new JTextPane();
        textPane4_Budget.setEditable(false);
        textPane4_Budget.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane4_Budget.setBounds(533, 109, 107, 20);
        panel4.add(textPane4_Budget);
        
        JButton btn4_Suggest = new JButton("Suggest");
        btn4_Suggest.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn4_Suggest.setBounds(550, 167, 90, 30);
        panel4.add(btn4_Suggest);
        
        JPanel panel4_SuggestedList = new JPanel();
        panel4_SuggestedList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Suggested List of Hotels", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel4_SuggestedList.setBounds(10, 220, 226, 250);
        panel4.add(panel4_SuggestedList);
        panel4_SuggestedList.setLayout(null);
        
        JScrollPane scrollPane4 = new JScrollPane();
        scrollPane4.setBounds(6, 16, 216, 226);
        panel4_SuggestedList.add(scrollPane4);
        
        JList list4 = new JList();
        list4.setModel(new AbstractListModel() {
        	String[] values = new String[] {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        	public int getSize() {
        		return values.length;
        	}
        	public Object getElementAt(int index) {
        		return values[index];
        	}
        });
        list4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane4.setViewportView(list4);
        
        JTextPane textPane4_HotelDetails = new JTextPane();
        textPane4_HotelDetails.setEditable(false);
        textPane4_HotelDetails.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        textPane4_HotelDetails.setBounds(250, 220, 379, 250);
        panel4.add(textPane4_HotelDetails);
        
        JButton btn4_Next = new JButton("Next");
        btn4_Next.setFont(new Font("Tahoma", Font.BOLD, 12));
        btn4_Next.setBounds(560, 490, 80, 30);
        panel4.add(btn4_Next);
        
// Menu Bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu mnFile = new JMenu("File");
        mnFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuBar.add(mnFile);
        mnFile.setMnemonic(KeyEvent.VK_F);
        
        JMenuItem mntmCreateNewEvent = new JMenuItem("Create New Event");
        mnFile.add(mntmCreateNewEvent);
        
        JMenuItem mntmLoadEvent = new JMenuItem("Load Event");
        mnFile.add(mntmLoadEvent);
        
        JMenuItem mntmSaveEvent = new JMenuItem("Save Event");
        mnFile.add(mntmSaveEvent);
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
        
        
    }
	
    private int getPreferredRowHeight(JTable table, int r, int margin) {
		// TODO Auto-generated method stub
		return 0;
	}
    
	public static void main(String[] args) {
        
        GUI tp = new GUI();
        tp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tp.setSize(800, 600);
        tp.setMinimumSize(new Dimension(800, 600));
        tp.setMaximumSize(new Dimension(800, 600));
        tp.setVisible(true);
        
    }
}
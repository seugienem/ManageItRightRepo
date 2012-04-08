/*
 * Event.java - Collection of all the fields required of a event.
 * Authors: Team MIR
 * 
 * Description: Event.java contains all the fields that are required for a event.
 * It also includes the various settings that users set, such as the checkboxes.
 * 
 * Event.java is serializable, providing facitilies for Manage It Right! to save
 * and load back the exact same event.
 * 
 */

import java.util.*;
import java.io.*;

public class Event implements Serializable{
	private static final long serialVersionUID = 1;
	private String eventName;
	private MyCalendar startDateAndTime;
	private MyCalendar endDateAndTime;
	private Vector<Guest> guestList;
	private Vector<Hotel> suggestedHotels;
	private int selectedHotelIdx;
	private Vector<Programme> programmeSchedule;
	private EventType eventType;
	private int mealDateSelected;
	private MealType mealType;
	private boolean[] mealRadioButtonsEnabled;
	private double eventBudget;
	private int budgetRatio;
	private String eventDescription;	
	private boolean guestListFinalised;
	private boolean programmeScheduleFinalised;
	private boolean[] hotelCheckBox;
	private Vector<Integer> seatsPerTable; 
	private Vector<Integer> seatingArrangementIndex;
	private Vector<String> dateList;
	private Vector<MyCalendar> dateListStoredAsMyCalendarObject;
	private Vector<Expense> expenseList;
	private boolean expenseFinalised;
	private double hotelBudgetSpent;
	private double expenseSpent;
	private double remainingBudget;
	private double costPerHead;

	public Event(){
		eventName = "";
		startDateAndTime = new MyCalendar();
		endDateAndTime = new MyCalendar();
		eventType = null;
		mealDateSelected = -1;
		mealType = null;
		mealRadioButtonsEnabled = new boolean[2];
		eventBudget = 0.0;
		eventDescription = "";
		suggestedHotels = new Vector<Hotel>();
		selectedHotelIdx = -1;
		guestList = new Vector<Guest>();
		programmeSchedule = new Vector<Programme>();
		budgetRatio = 50;
		guestListFinalised = false;
		programmeScheduleFinalised = false;
		hotelCheckBox = new boolean[3];	
		for(int i = 0; i < 3; i++){
			hotelCheckBox[i] = true;
		}
		seatsPerTable = new Vector<Integer>();
		seatingArrangementIndex = new Vector<Integer>();
		dateList = new Vector<String>();
		dateListStoredAsMyCalendarObject = new Vector<MyCalendar>();
		expenseList = new Vector<Expense>();
		expenseFinalised = false;
		hotelBudgetSpent = 0.0;
		expenseSpent = 0.0;
		remainingBudget = 0.0;
		costPerHead = 0.0;
	}
	
	public Vector<Integer> getSeatingArrangementIndex(){
		return seatingArrangementIndex;
	}
	
	public void setSeatingArrangementIndex(Vector<Integer> seatingArrangementIndex){
		this.seatingArrangementIndex = seatingArrangementIndex;
	}
	
	public Vector<Integer> getSeatsPerTable(){
		return seatsPerTable;
	}
	
	public void setSeatsPerTable(Vector<Integer> seatsPerTable){
		this.seatsPerTable = seatsPerTable;
	}
	
	public EventType getEventType(){
		return eventType;
	}
	
	public void setEventType(EventType eventType){
		this.eventType = eventType;
	}
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public MyCalendar getStartDateAndTime() {
		return startDateAndTime;
	}

	public void setStartDateAndTime(MyCalendar startDateAndTime) {
		this.startDateAndTime = startDateAndTime;
	}

	public MyCalendar getEndDateAndTime() {
		return endDateAndTime;
	}

	public void setEndDateAndTime(MyCalendar endDateAndTime) {
		this.endDateAndTime = endDateAndTime;
	}
	
	public void setDateList(Vector<String> dateList) {
		this.dateList = dateList;
	}
	
	public void setDateListStoredAsMyCalendarObject(Vector<MyCalendar> dateListStoredAsMyCalendarObject) {
		this.dateListStoredAsMyCalendarObject = dateListStoredAsMyCalendarObject;
	}
	
	public Vector<String> getDateList(){
		return this.dateList;
	}
	
	public Vector<MyCalendar> getDateListStoredAsMyCalendarObject() {
		return this.dateListStoredAsMyCalendarObject;
	}
	
	public void setMealDateSelected(int idx) {
		mealDateSelected = idx;
	}
	
	public int getMealDateSelected() {
		return mealDateSelected;
	}
	
	public void setMealType(MealType mealType) {
		this.mealType = mealType;
	}
	
	public MealType getMealType() {
		return mealType;
	}
	
	public void setMealRadioButtons(int index, boolean value) {
		mealRadioButtonsEnabled[index] = value;
	}
	
	public boolean getMealRadioButtons(int index) {
		return mealRadioButtonsEnabled[index];
	}

	public Vector<Guest> getGuestList() {
		return guestList;
	}

	public void setGuestList(Vector<Guest> guestList) {
		this.guestList = guestList;
	}

	public void mergeWithExistingHotels(Vector<Hotel> newList){
		suggestedHotels.addAll(newList);
	}
	
	public Vector<Hotel> getSuggestedHotels() {
		return suggestedHotels;
	}

	public void setSuggestedHotels(Vector<Hotel> suggestedHotels) {
		this.suggestedHotels = suggestedHotels;
	}
	
	public int getSelectedHotelIdx() {
		return selectedHotelIdx;
	}
	
	public void setSelectedHotelIdx(int idx) {
		this.selectedHotelIdx = idx;
	}
	
	public Vector<Programme> getProgrammeSchedule(){
		return programmeSchedule;
	}
	
	public void setProgrammeSchedule(Vector<Programme> programmeSchedule) {
		this.programmeSchedule = programmeSchedule;
	}

	public double getEventBudget() {
		return eventBudget;
	}

	public void setEventBudget(double eventBudget) {
		this.eventBudget = eventBudget;
	}
	
	public int getBudgetRatio() {
		return budgetRatio;
	}
	
	public void setBudgetRatio(int budgetRatio) {
		this.budgetRatio = budgetRatio;
	}
	
	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	
	public void setGuestListFinalised(boolean value){
		this.guestListFinalised = value;
	}
	
	public boolean getGuestListFinalised(){
		return guestListFinalised;
	}
	
	public void setProgrammeScheduleFinalised(boolean value){
		this.programmeScheduleFinalised = value;
	}
	
	public boolean getProgrammeScheduleFinalised(){
		return programmeScheduleFinalised;
	}
	
	public void setcheckBox(boolean[] value) {
		this.hotelCheckBox = value;
	}
	
	public boolean[] getcheckBox() {
		return hotelCheckBox;
	}

	public Vector<Expense> getExpense() {
		return expenseList;
	}
	
	public boolean getExpenseFinalised(){
		return expenseFinalised;
	}
	
	public void setExpenseFinalised(boolean value){
		this.expenseFinalised = value;
	}
	
	public double getHotelBudgetSpent() {
		return hotelBudgetSpent;
	}
	
	public void setHotelBudgetSpent(double hotelBudgetSpent) {
		this.hotelBudgetSpent = hotelBudgetSpent;
	}
	
	public double getExpenseSpent() {
		return expenseSpent;		
	}
		
	public void setExpenseSpent(double expenseSpent) {
		this.expenseSpent = expenseSpent;
	}
	
	public double getRemainingBudget() {
		return remainingBudget;		
	}
		
	public void setRemainingBudget(double remainingBudget) {
		this.remainingBudget = remainingBudget;
	}
	
	public double getCostPerHead() {
		return costPerHead;		
	}
		
	public void setCostPerHead(double costPerHead) {
		this.costPerHead = costPerHead;
	}
	
}

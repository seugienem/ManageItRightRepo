import java.util.*;
import java.io.*;

//test edit
// new test

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
	private MealType mealType;
	private double eventBudget;
	private int budgetRatio;
	private String eventDescription;	
	private boolean guestListFinalised;
	private boolean programmeScheduleFinalised;
	private boolean[] hotelCheckBox;
	private Vector<Integer> seatsPerTable; 	//TODO
	private Vector<Vector<String>> seatingArrangement; //TODO
	private Vector<String> dateList;
	private Vector<Expense> expenseList;
	private boolean expenseFinalised;
	private double hotelBudgetSpent;
	private double expenseSpent;
	private double remainingBudget;
	private double costPerHead;
	
	
	public Event(){
		//init all variables here
		eventName = "";
		startDateAndTime = new MyCalendar();
		endDateAndTime = new MyCalendar();
		eventType = null;
		mealType = null;
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
		seatsPerTable = new Vector<Integer>();
		seatingArrangement = new Vector<Vector<String>>();
		dateList = new Vector<String>();
		expenseList = new Vector<Expense>();
		hotelBudgetSpent = 0.0;
		expenseSpent = 0.0;
		remainingBudget = 0.0;
		costPerHead = 0.0;
	}
	
/*	public Event(String eventName, Calendar start, Calendar end, float budget, Vector<Guest> guestList, Vector<Hotel> suggestedHotels,
			Vector<Programme> programmeSchedule, EventType eventType, String eventDescription){
		this.eventName = eventName; this.startDateAndTime = start; this.endDateAndTime = end; this.eventBudget = budget;
		this.guestList = guestList; this.suggestedHotels = suggestedHotels; this.programmeSchedule = programmeSchedule;
		this.eventType = eventType;
		this.eventDescription = eventDescription;
	}
	
	public Event(String eventName, String start, String end, float budget, Vector<Guest> guestList, Vector<Hotel> suggestedHotels,
			Vector<Programme> programmeSchedule, EventType eventType, String eventDescription){
		this.eventName = eventName; this.startDateAndTime = Parser.parseStringToCalendar(start); this.endDateAndTime = Parser.parseStringToCalendar(end); 
		this.eventBudget = budget; this.guestList = guestList; this.suggestedHotels = suggestedHotels; this.programmeSchedule = programmeSchedule;
		this.eventType = eventType;
		this.eventDescription = eventDescription;
	}
*/
	
	public Vector<Integer> getSeatsPerTable(){
		return seatsPerTable;
	}
	
	public void setSeatsPerTable(Vector<Integer> seatsPerTable){
		this.seatsPerTable = seatsPerTable;
	}
	
	public Vector<Vector<String>> getSeatingArrangement(){
		return seatingArrangement;
	}
	
	public void setSeatingArrangement(Vector<Vector<String>> seatingArrangement){
		this.seatingArrangement = seatingArrangement;
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
	
/*	public void setStartDateAndTime(String startDateAndTime){
		this.startDateAndTime = Parser.parseStringToCalendar(startDateAndTime);
	}
*/
	public MyCalendar getEndDateAndTime() {
		return endDateAndTime;
	}

	public void setEndDateAndTime(MyCalendar endDateAndTime) {
		this.endDateAndTime = endDateAndTime;
	}
	
/*	public void setEndDateAndTime(String endDateAndTime){
		this.endDateAndTime = Parser.parseStringToCalendar(endDateAndTime);
	}
*/	
	public void setDateList(Vector<String> dateList) {
		this.dateList = dateList;
	}
	
	public Vector<String> getDateList(){
		return this.dateList;
	}
	public void setMealType(MealType mealType) {
		this.mealType = mealType;
	}
	
	public MealType getMealType() {
		return mealType;
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

/*	public void addExpense(Expense expense) {
		expenseList.add(expense);
	}
	
	public void removeExpense(Expense expense) {
		expenseList.remove(expense);
	}
*/	
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

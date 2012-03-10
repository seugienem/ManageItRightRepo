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
}

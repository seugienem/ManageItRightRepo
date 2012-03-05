import java.util.*;

//test edit
// new test

public class Event {
	private String eventName;
	private Calendar startDateAndTime;
	private Calendar endDateAndTime;
	private Vector<Guest> guestList;
	private Vector<Hotel> suggestedHotels;
	private Vector<Programme> programmeSchedule;
	private EventType eventType;
	private double eventBudget;
	private String eventDescription;
	
	public Event(){
	}
	
	public Event(String eventName, Calendar start, Calendar end, float budget, Vector<Guest> guestList, Vector<Hotel> suggestedHotels,
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

	public Calendar getStartDateAndTime() {
		return startDateAndTime;
	}

	public void setStartDateAndTime(Calendar startDateAndTime) {
		this.startDateAndTime = startDateAndTime;
	}
	
	public void setStartDateAndTime(String startDateAndTime){
		this.startDateAndTime = Parser.parseStringToCalendar(startDateAndTime);
	}

	public Calendar getEndDateAndTime() {
		return endDateAndTime;
	}

	public void setEndDateAndTime(Calendar endDateAndTime) {
		this.endDateAndTime = endDateAndTime;
	}
	
	public void setEndDateAndTime(String endDateAndTime){
		this.endDateAndTime = Parser.parseStringToCalendar(endDateAndTime);
	}

	public Vector<Guest> getGuestList() {
		return guestList;
	}

	public void setGuestList(Vector<Guest> guestList) {
		this.guestList = guestList;
	}

	public Vector<Hotel> getSuggestedHotels() {
		return suggestedHotels;
	}

	public void setSuggestedHotels(Vector<Hotel> suggestedHotels) {
		this.suggestedHotels = suggestedHotels;
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

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
}

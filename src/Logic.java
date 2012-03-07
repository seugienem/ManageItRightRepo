import java.io.File;
import java.util.*;

public class Logic {

	private Event event;

	private DataManager dm;
	private boolean saved;
	private HotelSuggest hotelSuggester;
	private DataManager dataM;


	public Logic(Event event, DataManager dm){ //logic instance has a Event and DataManager parameter
		this.event = event;
		this.dm = dm;
		saved = true;
		hotelSuggester = new HotelSuggest();
	}

	boolean getSavedStatus(){
		return saved;
	}
	
	void saveEvent(File out){
		saved = true;
		dm.save(out, event);
	}
	
	void loadEvent(File in){
		saved = true;
		dm.load(in);
	}
	
	////OVERVIEW TAB\\\\

	//Get Event Name
	String getEventName(){
		return event.getEventName();
	}

	//Get Step 1 status
	int step1Status(){
		if (event.getEventType() != null &&
				event.getEventName() != null &&
				event.getStartDateAndTime() != null &&
				event.getEndDateAndTime() != null &&
				event.getEventDescription() != null &&
				event.getEventBudget() != 0.0)
			return 2;	//if Step 1 fields are completed

		else if (event.getEventType() == null &&
				event.getEventName() == null &&
				event.getStartDateAndTime() == null &&
				event.getEndDateAndTime() == null &&
				event.getEventDescription() == null &&
				event.getEventBudget() == 0)
			return 0;	//if Step 1 fields are blank

		else return 1;	//if Step 1 fields are partially filled		
	}

	//Get Step 2 status
	int step2Status(){
		if (event.getGuestList() == null)
			return 0;	//if no guest list is found

		Guest g;
		for (int i=0; i<event.getGuestList().size(); ++i){
			g = event.getGuestList().get(i);
			if (g.getName() == null ||
					g.getEmailAddress() == null ||
					g.getDescription() == null ||
					g.getGroup() == null ||
					g.getContactNumber() == null ||
					g.getGender() == null)
				return 1;	//if there is missing guest detail(s)
		}

		return 2;	//if guest list is finalised
	}

	//Get Step 3 status
	int step3Status(){
		if (event.getProgrammeSchedule() == null)
			return 0; //if no programme schedule is found

		Programme p;
		for (int i=0; i<event.getProgrammeSchedule().size(); ++i){
			p = event.getProgrammeSchedule().get(i);
			if (p.getStartTime() == null ||
					p.getEndTime() == null ||
					p.getTitle() == null ||
					p.getInCharge() == null)
				return 1; //if there is missing programme detail(s)
		}

		return 2; //if programme schedule is finalised


	}

	//Get Step 4 status
	int step4Status(){
		if (event.getSuggestedHotels().size() == 0) //if no suitable hotel(s) exist for the user criteria
			return 0;
		else 
			return 1; //assume that user has selected a suggested hotel
	}
	
	////STEP 1: EVENT DETAILS TAB\\\\
	
	void setEventType(int eventType){
		saved = false;
		EventType eType = null;
		
		switch(eventType){
		case 0:
			eType = EventType.ANNIVERSARY;
			break;
		case 1:
			eType = EventType.AWARD_CEREMONY;
			break;
		case 2:
			eType = EventType.BIRTHDAY_PARTY;
			break;
		case 3:
			eType = EventType.DINNER_DANCE;
			break;
		case 4:
			eType = EventType.FESTIVE_PARTY;
			break;
		case 5:
			eType = EventType.SEMINAR;
			break;
		case 6:
			eType = EventType.SOCIAL_EVENT;
			break;
		case 7:
			eType = EventType.TALK_SPEECH;
			break;
		case 8:
			eType = EventType.WEDDING;
			break;
		case 9:
			eType = EventType.WORKSHOP;
			break;
		}
		
		event.setEventType(eType);
	}

	int getEventType(){
		return event.getEventType().ordinal();
	}
	
	void setEventName(String name){
		saved = false;
		event.setEventName(name);
	}
	
	void setEventStartDate(Date date){
		saved = false;
		Calendar startCal = event.getStartDateAndTime();
		
		startCal.set(date.getYear(), date.getMonth(), date.getDate());
		event.setStartDateAndTime(startCal);
	}
	
	Date getEventStartDate(){
		saved = false;
		Calendar startCal = event.getEndDateAndTime();
		Date startDate = new Date();
		
		startDate.setYear(startCal.get(Calendar.YEAR));
		startDate.setMonth(startCal.get(Calendar.MONTH));
		startDate.setDate(startCal.get(Calendar.DATE));
		
		return startDate;
	}
	
	void setEventEndDate(Date date){
		saved = false;
		Calendar endCal = event.getEndDateAndTime();
		
		endCal.set(date.getYear(), date.getMonth(), date.getDate());
		event.setEndDateAndTime(endCal);
	}
	
	Date getEventEndDate(){
		Calendar endCal = event.getEndDateAndTime();
		Date endDate = new Date();
		
		endDate.setYear(endCal.get(Calendar.YEAR));
		endDate.setMonth(endCal.get(Calendar.MONTH));
		endDate.setDate(endCal.get(Calendar.DATE));
		
		return endDate;
	}
	
	void setStartTimeH(int startH){
		saved = false;
		Calendar startCal = event.getStartDateAndTime();
		
		startCal.set(Calendar.HOUR_OF_DAY, startH);
		event.setStartDateAndTime(startCal);
	}
	
	int getStartTimeH(){
		return event.getStartDateAndTime().get(Calendar.HOUR_OF_DAY);
	}

	void setStartTimeM(int startM){
		saved = false;
		Calendar startCal = event.getStartDateAndTime();

		startCal.set(Calendar.MINUTE, startM);
		event.setStartDateAndTime(startCal);
	}
	
	int getStartTimeM(){
		return event.getStartDateAndTime().get(Calendar.MINUTE);// return: event start time - minutes
	}

	void setEndTimeH(int endH){
		saved = false;
		Calendar endCal = event.getEndDateAndTime();

		endCal.set(Calendar.HOUR_OF_DAY, endH);
		event.setEndDateAndTime(endCal);
	}
	
	int getEndTimeH() {
		return event.getEndDateAndTime().get(Calendar.HOUR_OF_DAY);// return: event start time - minutes
	}
	
	void setEndTimeM(int endM){
		saved = false;
		Calendar endCal = event.getEndDateAndTime();

		endCal.set(Calendar.MINUTE, endM);
		event.setEndDateAndTime(endCal);
	}
	
	int getEndTimeM(){
		return event.getEndDateAndTime().get(Calendar.MINUTE);
	}

	void setEventDes(String des){
		saved = false;
		event.setEventDescription(des);
	}
	
	String getEventDes(){
		return event.getEventDescription();
	}
	
	void setBudget(double budget){
		saved = false;
		event.setEventBudget(budget);
	}
	
	double getBudget(){
		return event.getEventBudget();
	}
	
	void clearHotelSuggestions(){
		event.setSuggestedHotels(new Vector<Hotel>());
	}
	
	void hotelSuggest(int stars, int ratio){
		hotelSuggester.setStars(stars);
		hotelSuggester.setEventBudget(event.getEventBudget());
		hotelSuggester.setBudgetRatio(ratio);
		hotelSuggester.setStartDate(event.getStartDateAndTime());
		hotelSuggester.setNumberOfGuests(event.getGuestList().size());
		Vector<Hotel> hotelList = hotelSuggester.suggest(dm);
		event.mergeWithExistingHotels(hotelList);
	}
	
	Vector<String> getSuggestedHotelsNames(){
		Vector<String> names = new Vector<String>();
		for(Hotel item : event.getSuggestedHotels()){
			names.add(item.getName());
		}
		return names;
	}
	
	Vector<Vector<String>> getGuestList(){
		Vector<Guest> guestList = event.getGuestList();
		Vector<Vector<String>> guestVector = new Vector<Vector<String>>();
		Vector<String> guestDetail;
		Guest currGuest;
		String guestGender = null;
		
		for (int i=0; i<guestList.size(); ++i){
			//Get guest from event guest list
			currGuest = guestList.get(i);
			
			//Parse gender to String
			switch (currGuest.getGender().ordinal()){
			case 0:
				guestGender = "MALE";
				break;
			case 1:
				guestGender = "FEMALE";
				break;
			case 2:
				guestGender = "UNKNOWN";
				break;
			}

			//Prepare guestDetail to add to guestVector
			guestDetail = new Vector<String>();
			guestDetail.add(currGuest.getName());
			guestDetail.add(currGuest.getEmailAddress());
			guestDetail.add(currGuest.getDescription());
			guestDetail.add(currGuest.getGroup());
			guestDetail.add(currGuest.getContactNumber());
			guestDetail.add(guestGender);
			
			//Add guestDetail:vector to guestVector:vector of vector
			guestVector.add(guestDetail);
		}
		
		return guestVector;
	}
}	



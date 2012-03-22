import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;

public class Logic {

	private Event event;

	private DataManager dm;
	private boolean saved;
	private HotelSuggest hotelSuggester;

	public Logic(Event event, DataManager dm){ //logic instance has a Event and DataManager parameter
		this.event = event;
		this.dm = dm;
		saved = true;
		hotelSuggester = new HotelSuggest();
	}

	boolean getSavedStatus(){
		return saved;
	}
	
	void setSavedStatus(boolean value){
		saved = value;
	}
	
	void saveEvent(File out){
		saved = true;
		dm.save(out, event);
	}
	
	void loadEvent(File in) throws Exception{
		saved = true;
		try{
			event = dm.load(in);
		} catch(Exception ex){
			throw ex;
		}
	}
	
	void importGuest(File in) throws DataFormatException{
		try{
			saved = false;
			Vector<Guest> guestIn = dm.importGuest(in);
			event.setGuestList(guestIn);
		} catch(DataFormatException ex){
			throw new DataFormatException();
		}
	}
	
	void exportGuest(File out){
		dm.exportGuest(out, event.getGuestList());
	}
	
	void exportProgramme(File out){
		dm.exportProgramme(out, event.getProgrammeSchedule());
	}
	
	void createNewEvent(){
		this.event = new Event();
	}
	
	////OVERVIEW TAB\\\\

	//Get Event Name
	String getEventName(){
		return event.getEventName();
	}

	//Get Step 1 status
	int step1Status(){
		if (event.getEventType() != null &&
				!event.getEventName().isEmpty() &&
				!(event.getStartDateAndTime().getYear() == 0) && //!(event.getStartDateAndTime().getHour() == 0) &&
				!(event.getEndDateAndTime().getYear() == 0) && //!(event.getEndDateAndTime().getHour() == 0) &&
				event.getMealType() != null &&
				!event.getEventDescription().isEmpty() &&
				event.getEventBudget() != 0.0)
			return 2;	//if Step 1 fields are completed

		else if (event.getEventType() == null &&
				event.getEventName().isEmpty() &&
				event.getStartDateAndTime().getYear() == 0 && //event.getStartDateAndTime().getHour() == 0 &&
				event.getEndDateAndTime().getYear() == 0 && //event.getEndDateAndTime().getHour() == 0 &&
				event.getMealType() == null &&
				event.getEventDescription().isEmpty() &&
				event.getEventBudget() == 0.0)
			return 0;	//if Step 1 fields are blank

		else return 1;	//if Step 1 fields are partially filled		
	}

	//Get Step 2 status
	int step2Status(){
		if (event.getGuestList().isEmpty())
			return 0;	//if no guest list is found

		if(!completedGuestFields())
			return 1;
		
		if(event.getGuestListFinalised())
			return 3;
		else
			return 2;	//not finalised, but details are in
	}

	//Get Step 3 status
	int step3Status(){
		if (event.getProgrammeSchedule().isEmpty())
			return 0; //if no programme schedule is found

		if(!completedProgrammeFields())
			return 1;

		if(event.getProgrammeScheduleFinalised())
			return 3;
		else
			return 2;
	}

	//Get Step 4 status
	int step4Status(){
		/* OLD CODE
		if (event.getSuggestedHotels().size() == 0) //if no suitable hotel(s) exist for the user criteria
			return 0;
		else 
			return 1; //assume that user has selected a suggested hotel
		*/
		
		if(event.getSelectedHotelIdx() > -1) return 1;
		else return 0;
	}
	
	////STEP 1: EVENT DETAILS TAB\\\\
	void setEventType(int eventType){
		saved = false;
		EventType eType = null;
		
		switch(eventType){
		case 0:
			eType = null;
			break;
		case 1:
			eType = EventType.ANNIVERSARY;
			break;
		case 2:
			eType = EventType.AWARD_CEREMONY;
			break;
		case 3:
			eType = EventType.BIRTHDAY_PARTY;
			break;
		case 4:
			eType = EventType.DINNER_DANCE;
			break;
		case 5:
			eType = EventType.FESTIVE_PARTY;
			break;
		case 6:
			eType = EventType.SEMINAR;
			break;
		case 7:
			eType = EventType.SOCIAL_EVENT;
			break;
		case 8:
			eType = EventType.TALK_SPEECH;
			break;
		case 9:
			eType = EventType.WEDDING;
			break;
		case 10:
			eType = EventType.WORKSHOP;
			break;
		}
		
		event.setEventType(eType);
	}

	int getEventType(){
		if(event.getEventType() == null)
			return -1;
		else
		{
			//System.out.println("Ordinal: " + event.getEventType().ordinal());
			return event.getEventType().ordinal();
		}
			
	}
	
	void setEventName(String name){
		saved = false;
		event.setEventName(name);
	}
	
	@SuppressWarnings("deprecation")
	void setEventStartDate(Date date){
		saved = false;
		MyCalendar startCal = event.getStartDateAndTime();
		
		startCal.setDayOfTheWeek(date.getDay());
		startCal.setDate(date.getDate());
		startCal.setMonth(date.getMonth()+1);
		startCal.setYear(date.getYear()+1900);
		event.setStartDateAndTime(startCal);
	}
	
	@SuppressWarnings("deprecation")
	Date getEventStartDate(){
		saved = false;
		MyCalendar startCal = event.getStartDateAndTime();
		if(startCal.getDate() == 0 && startCal.getMonth() == 0 && startCal.getYear() == 0){
			return null;
		}
		Date startDate = new Date();
		
		startDate.setYear(startCal.getYear());
		startDate.setMonth(startCal.getMonth());
		startDate.setDate(startCal.getDate());
		
		return startDate;
	}
	
	@SuppressWarnings("deprecation")
	void setEventEndDate(Date date){
		saved = false;
		MyCalendar endCal = event.getEndDateAndTime();
		
		endCal.setDayOfTheWeek(date.getDay());
		endCal.setDate(date.getDate());
		endCal.setMonth(date.getMonth()+1);
		endCal.setYear(date.getYear()+1900);
		event.setEndDateAndTime(endCal);
	}
	
	@SuppressWarnings("deprecation")
	Date getEventEndDate(){
		MyCalendar endCal = event.getEndDateAndTime();
		Date endDate = new Date();
		if(endCal.getDate() == 0 && endCal.getMonth() == 0 && endCal.getYear() == 0){
			return null;
		}
		
		endDate.setYear(endCal.getYear());
		endDate.setMonth(endCal.getMonth());
		endDate.setDate(endCal.getDate());
		
		return endDate;
	}
	
	boolean checkDate(){
		MyCalendar startCal = event.getStartDateAndTime();
		MyCalendar endCal = event.getEndDateAndTime();
		
		if ( endCal.getYear() == 0  || endCal.getMonth() == 0 || endCal.getDate() == 0)
			return true;
		
		if ( endCal.getYear() < startCal.getYear() || endCal.getMonth() < startCal.getMonth() || endCal.getDate() < startCal.getDate() )
			return false;
		else
			return true;
	}
	
	@SuppressWarnings("deprecation")
	void setDateList(Date StartDate, Date EndDate){
		
		Vector<String> dateList = event.getDateList();		
		      
		int startDate = StartDate.getDate();
        int endDate = EndDate.getDate();
        
        
        for (int i=startDate;i<=endDate;i++) {       	
        	String dateString = Integer.toString(i) + "/" + Integer.toString(StartDate.getMonth()) + "/" + Integer.toString(StartDate.getYear());
        	dateList.add(dateString); 
        }               
        
        event.setDateList(dateList);
	}
	
	Vector<String> getDateList(){
		return event.getDateList();
	}
	
	void setStartTimeH(int startH){
		saved = false;
		MyCalendar startCal = event.getStartDateAndTime();
		
		startCal.setHour(startH);
		event.setStartDateAndTime(startCal);
	}
	
	int getStartTimeH(){
		return event.getStartDateAndTime().getHour();
	}

	void setStartTimeM(int startM){
		saved = false;
		MyCalendar startCal = event.getStartDateAndTime();

		startCal.setMin(startM);		
		event.setStartDateAndTime(startCal);
	}
	
	int getStartTimeM(){
		return event.getStartDateAndTime().getMin();// return: event start time - minutes
	}

	void setEndTimeH(int endH){
		saved = false;
		MyCalendar endCal = event.getEndDateAndTime();

		endCal.setHour(endH);
		event.setEndDateAndTime(endCal);
	}
	
	int getEndTimeH() {
		return event.getEndDateAndTime().getHour();// return: event start time - minutes
	}
	
	void setEndTimeM(int endM){
		saved = false;
		MyCalendar endCal = event.getEndDateAndTime();

		endCal.setMin(endM);
		event.setEndDateAndTime(endCal);
	}
	
	int getEndTimeM(){
		return event.getEndDateAndTime().getMin();
	}
	
	public void setMealType(int i) {
		if (i == 0) event.setMealType(MealType.LUNCH);
		else if (i == 1) event.setMealType(MealType.DINNER);
		
	}
	
	public int getMealType() {
		if(event.getMealType() == null)
			return -1;
		return event.getMealType().ordinal();
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
	
	///STEP 2: GUEST LIST\\\\
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
				guestGender = "Male";
				break;
			case 1:
				guestGender = "Female";
				break;
			case 2:
				guestGender = "Select";
				break;
			}

			//Prepare guestDetail to add to guestVector	
			guestDetail = new Vector<String>();
			guestDetail.add(currGuest.getName());
			guestDetail.add(guestGender);
			guestDetail.add(currGuest.getGroup());
			guestDetail.add(currGuest.getEmailAddress());
			guestDetail.add(currGuest.getContactNumber());
			guestDetail.add(currGuest.getDescription());
			

			//Add guestDetail:vector to guestVector:vector of vector
			guestVector.add(guestDetail);
		}

		return guestVector;
	}
	
	void addGuest(){
		event.getGuestList().add(new Guest());
	}
	
	void removeGuest(int index){
		event.getGuestList().remove(index);
	}
	
	void setGuestInfo(int index, String field, String data){
		saved = false;
		switch(field){
			case "Name":
				event.getGuestList().get(index).setName(data);
				break;
			case "Gender":
				//convert to upper case
				data = data.toUpperCase();
				try{
					event.getGuestList().get(index).setGender(Enum.valueOf(Gender.class, data));
				} catch(Exception ex){
					event.getGuestList().get(index).setGender(Gender.UNKNOWN);
				}
				break;
			case "Group":
				event.getGuestList().get(index).setGroup(data);
				break;
			case "Email":
				event.getGuestList().get(index).setEmailAddress(data);
				break;
			case "Contact Number":
				event.getGuestList().get(index).setContactNumber(data);
				break;
			case "Description":
				event.getGuestList().get(index).setDescription(data);
				break;
		}
	}
	
	boolean completedGuestFields(){
		if(event.getGuestList().size() == 0)
			return false;
		
		for(Guest guestCheck : event.getGuestList()){
			if(guestCheck.getName().equals("") || guestCheck.getGroup().equals("")
					|| guestCheck.getEmailAddress().equals("") || guestCheck.getDescription().equals("")
					|| guestCheck.getContactNumber().equals("") || guestCheck.getGender().ordinal() == 2)
			return false;
		}
		return true;
	}
	
	void setGuestListFinalised(boolean value){
		saved = false;
		event.setGuestListFinalised(value);
	}
	
	boolean getGuestListFinalised(){
		return event.getGuestListFinalised();
	}
	////STEP 3: PROGRAMME TAB\\\\
	Vector<Vector<String>> getProgrammeSchedule(){
		Vector<Programme> programmeList = event.getProgrammeSchedule();
		Vector<Vector<String>> programmeVector = new Vector<Vector<String>>();
		Vector<String> programmeDetail;
		Programme currProgramme;
		//Integer hourInt, minInt;
		String timeStr;
		
		for (int i=0; i<programmeList.size(); ++i){
			programmeDetail = new Vector<String>();
			
			//Get programme from event programme list
			currProgramme = programmeList.get(i);
			
			//Parse startTime to String
			/*
			hourInt = currProgramme.getStartTime();
			minInt = currProgramme.getStartTime();			
			timeStr = String.format("%02d", hourInt.toString()) + ":" + String.format("%02d", minInt.toString());
			*/
			timeStr = String.valueOf(currProgramme.getStartTime());
			
			//Add start time to programme detail
			programmeDetail = new Vector<String>();
			programmeDetail.add(timeStr);
			
			//Parse endTime to String
			/*
			hourInt = currProgramme.getEndTime();
			minInt = currProgramme.getEndTime();			
			timeStr = String.format("%02d", hourInt.toString()) + ":" + String.format("%02d", minInt.toString());
			*/
			timeStr = String.valueOf(currProgramme.getStartTime());
			
			//Add end time to programme detail
			programmeDetail.add(timeStr);
			
			//Add title and inCharge to programme detail
			programmeDetail.add(currProgramme.getTitle());
			programmeDetail.add(currProgramme.getInCharge());
			
			//Add programme detail to programme vector
			programmeVector.add(programmeDetail);
		}
		
		return programmeVector;
	}
	
	void addProgramme(){
		saved = false;
		event.getProgrammeSchedule().add(new Programme());
	}
	
	void removeProgramme(int index){
		event.getProgrammeSchedule().remove(index);
	}
	
	void setProgrammeInfo(int index, String field, String data){
		saved = false;
		switch(field){
		case "Start Time":
			event.getProgrammeSchedule().get(index).setStartTime(Integer.parseInt(data));
			break;
		case "End Time":
			event.getProgrammeSchedule().get(index).setEndTime(Integer.parseInt(data));
			break;
		case "Programme":
			event.getProgrammeSchedule().get(index).setTitle(data);
			break;
		case "In-Charge":
			event.getProgrammeSchedule().get(index).setInCharge(data);
			break;
		}
	}
	
	boolean completedProgrammeFields(){
		if(event.getProgrammeSchedule().size() == 0)
			return false;
		for(Programme programmeCheck : event.getProgrammeSchedule()){
			if(programmeCheck.getStartTime() == -1 || programmeCheck.getEndTime() == -1
					|| programmeCheck.getTitle().equals("") || programmeCheck.getInCharge().equals(""))
				return false;
		}
		return true;
	}
	
	boolean getProgrammeScheduleFinalised(){
		return event.getProgrammeScheduleFinalised();
	}
	
	void setProgrammeScheduleFinalised(boolean value){
		saved = false;
		event.setProgrammeScheduleFinalised(value);
	}
	
	boolean[] getcheckBox() {
		return event.getcheckBox();
	}
	
	void setcheckBox(boolean[] value) {
		event.setcheckBox(value);
	}
	
	
	////STEP 4: HOTEL SUGGESTIONS TAB\\\\
	double calculateHotelBudget(){
		return (event.getEventBudget() * event.getBudgetRatio()/100.0);
	}
	
	void setBudgetRatio(int budgetRatio) {
		event.setBudgetRatio(budgetRatio);
	}
	
	int getBudgetRatio() {
		return event.getBudgetRatio();
	}
	
	void hotelSuggest(int stars, int ratio) throws IOException{
		hotelSuggester.setStars(stars);
		hotelSuggester.setEventBudget(event.getEventBudget());
		hotelSuggester.setBudgetRatio(event.getBudgetRatio());
		hotelSuggester.setStartDate(event.getStartDateAndTime());
		hotelSuggester.setEventMealType(event.getMealType());
		hotelSuggester.setNumberOfGuests(event.getGuestList().size());
		try{
			Vector<Hotel> hotelList = hotelSuggester.suggest(dm);
			event.mergeWithExistingHotels(hotelList);
			event.setSelectedHotelIdx(-1);
		} catch(IOException ioEx){
			throw ioEx;
		}
	}
	
	Vector<String> getSuggestedHotelsNames(){
		Vector<String> names = new Vector<String>();
		for(Hotel item : event.getSuggestedHotels()){
			names.add("[" + item.getStars() + "] " + item.getName());
		}
		if(names.size() == 0)
			names.add("No hotels match your criteria!");
		
		return names;
	}
	
	String getHotelInformation(int index){
		String hotelDetails = "";
		if (index > -1) {
			Hotel item = event.getSuggestedHotels().get(index);
			Vector<Menu> menu = item.getMenuList();
				
			hotelDetails = hotelDetails + item.getName() + " (" + item.getStars() + "-star)\n";
			hotelDetails = hotelDetails + "Address: " + item.getAddress() + "\n";
			hotelDetails = hotelDetails + "Tel: " + item.getContact() + "\n";
			hotelDetails = hotelDetails + "Website: " + item.getWebsite() + "\n";
			hotelDetails = hotelDetails + "Email: " + item.getEmail() + "\n";
				
			hotelDetails = hotelDetails + "\nPrice(s):" + '\n';
				
			for(Menu menuItem : menu){
				if(menuItem.getMealType() == MealType.LUNCH)
					hotelDetails = hotelDetails + "Lunch ";
				else
					hotelDetails = hotelDetails + "Dinner ";
					
				if(menuItem.getMenuType() == MenuType.HIGH)
					hotelDetails = hotelDetails + "highest price: $" + menuItem.getPricePerTable() + " per table of 10.\n";
				else
					hotelDetails = hotelDetails + "lowest price: $" + menuItem.getPricePerTable() + " per table of 10.\n";
			}
				
				return hotelDetails;
			}
		return hotelDetails;
	}
	
	int getSelectedHotelIdx() {
		System.out.println(event.getSelectedHotelIdx());
		return event.getSelectedHotelIdx();
	}
	
	void setSelectedHotelIdx(int idx) {
		saved = false;
		//System.out.println("Set index: " + idx);
		event.setSelectedHotelIdx(idx);
	}
	
}	


/*
 * Logic.java - Provides functions for GUI to interact with the other objects
 * Authors: Team MIR
 * 
 * 
 * Description: This class acts as a layer separating GUI from the various objects 
 * (Event etc.) It provides functions for GUI to get or set the various attributes,
 * and also aids in translating some objects into a type that the GUI methods can 
 * accept.
 */

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;

public class Logic {
	private Event event;

	private DataManager dm;
	private boolean saved;
	private HotelSuggest hotelSuggester;

	public Logic(Event event, DataManager dm){
		this.event = event;
		this.dm = dm;
		saved = true;
		hotelSuggester = new HotelSuggest();
	}

	
	/***********************************************************************
	 * 
	 * Misc. functions here
	 * 
	 ************************************************************************/
	boolean getSavedStatus(){
		return saved;
	}
	
	void setSavedStatus(boolean value){
		saved = value;
	}
	
	void saveEvent(File out){
		saved = true;
		
		//enforce file extension
		if(!out.toString().toUpperCase().endsWith(".MIR")){
			out = new File(out.toString().concat(".MIR"));
		}
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
		if(!out.toString().toUpperCase().endsWith(".CSV")){
			out = new File(out.toString().concat(".CSV"));
		}
		dm.exportGuest(out, event.getGuestList());
	}
	
	void exportProgramme(File out){
		if(!out.toString().toUpperCase().endsWith(".CSV")){
			out = new File(out.toString().concat(".CSV"));
		}
		dm.exportProgramme(out, event.getProgrammeSchedule());
	}
	
	void exportTableAssignment(File out) {
		if(!out.toString().toUpperCase().endsWith(".CSV")){
			out = new File(out.toString().concat(".CSV"));
		}
		dm.exportTableAssigner(out, getArrangement());
	}
	
	void exportExpense(File out) {
		if(!out.toString().toUpperCase().endsWith(".CSV")){
			out = new File(out.toString().concat(".CSV"));
		}
		dm.exportExpense(out, event.getExpense());
	}
	
	void createNewEvent(){
		this.event = new Event();
	}
	
	
	/**************************************************************************
	 * 
	 * Overview Tab Here
	 * 
	 **************************************************************************/
	String getEventName(){
		return event.getEventName();
	}

	int step1Status(){
		if (event.getEventType() != null &&
				!event.getEventName().isEmpty() &&
				!(event.getStartDateAndTime().getYear() == 0) && 
				!(event.getEndDateAndTime().getYear() == 0) && 
				event.getMealType() != null &&
				!event.getEventDescription().isEmpty() &&
				event.getEventBudget() != 0.0)
			return 2;	//if Step 1 fields are completed

		else if (event.getEventType() == null &&
				event.getEventName().isEmpty() &&
				event.getStartDateAndTime().getYear() == 0 &&
				event.getEndDateAndTime().getYear() == 0 && 
				event.getMealType() == null &&
				event.getEventDescription().isEmpty() &&
				event.getEventBudget() == 0.0)
			return 0;	//if Step 1 fields are blank

		else return 1;	//if Step 1 fields are partially filled		
	}

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

	int step4Status(){		
		if(event.getSelectedHotelIdx() > -1) return 1;
		else return 0;
	}
	
	int step5Status() {
		if(event.getSeatsPerTable().isEmpty())
			return 0;
		else
			return 1;
	}
	
	int step6Status() {
		if(event.getExpense().isEmpty())
			return 0; // empty list
		if(event.getExpenseFinalised())
			return 2; // finalised
		else
			return 1; // not finalised
	}
	
	//Method searches for any MIR files in the program directory and returns them
	//in a string array.
	String[] getSavedEvents() {
		File dir = new File(".");
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".MIR");
		    }
		};
		
		String[] children = dir.list(filter);
		if (children.length == 0) {	
			String[] noEvent = new String[1];
			noEvent[0] = "No saved events are found";
			return noEvent;
		}
		
		return children;
	}
	
	
	/*********************************************************************************
	 * 
	 * Step 1 Functions here
	 * 
	 *********************************************************************************/
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
		else{
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
		startCal.setMonth(date.getMonth());
		startCal.setYear(date.getYear());
		
		event.setStartDateAndTime(startCal);
	}
	
	@SuppressWarnings("deprecation")
	Date getEventStartDate() {
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
		endCal.setMonth(date.getMonth());
		endCal.setYear(date.getYear());
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
	
	//Method checks if start date is before end date
	boolean checkDate(){
		MyCalendar startCal = event.getStartDateAndTime();
		MyCalendar endCal = event.getEndDateAndTime();
		
		if ( endCal.getYear() == 0  || endCal.getMonth() == 0 || endCal.getDate() == 0)
			return true;
		
		if(endCal.getYear() < startCal.getYear())
			return false;
		else if(endCal.getYear() > startCal.getYear())
			return true;
		
		//If code reaches here, Year is the same
		if(endCal.getMonth() < startCal.getMonth())
			return false;
		else if(endCal.getMonth() > startCal.getMonth())
			return true;
		
		//If code reaches here, Year and Month are the same
		if(endCal.getDate() < startCal.getDate())
			return false;
		else if(endCal.getDate() >= startCal.getDate())
			return true; 
		
		return true;
	}
	
	//Generates and sets the list of dates between start and end date
	@SuppressWarnings("deprecation")
	void setDateList(Date startDate, Date endDate){
		
		Vector<String> dateList = new Vector<String>();
		Vector<MyCalendar> dateListStoredAsMyCalendarObject = new Vector<MyCalendar>();
		
		long interval = 24* 60 * 60 * 1000; //1 day in milliseconds
		long endTime = endDate.getTime();	//end
		long startTime = startDate.getTime();
		
		while(startTime <= endTime){
			MyCalendar newCal = new MyCalendar();
			Date newDate = new Date(startTime);
			newCal.setDayOfTheWeek(newDate.getDay());
			newCal.setDate(newDate.getDate());
			newCal.setMonth(newDate.getMonth());
			newCal.setYear(newDate.getYear());
			
			String dateStr = newDate.toLocaleString();
			dateStr = dateStr.substring(0, 12);		//get rid of time
			
			dateStr = dateStr.trim(); //trim trailing whitespace
			dateList.add(dateStr);
			dateListStoredAsMyCalendarObject.add(newCal);
			startTime += interval;
		}           
        event.setDateList(dateList);
        event.setDateListStoredAsMyCalendarObject(dateListStoredAsMyCalendarObject);
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
		// return: event start time - minutes
		return event.getStartDateAndTime().getMin();
	}

	void setEndTimeH(int endH){
		saved = false;
		MyCalendar endCal = event.getEndDateAndTime();

		endCal.setHour(endH);
		event.setEndDateAndTime(endCal);
	}
	
	int getEndTimeH() {
		// return: event start time - minutes
		return event.getEndDateAndTime().getHour();
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
	
	public void setMealDateSelected(int idx) {
		saved = false;
		event.setMealDateSelected(idx);
	}
	
	public int getMealDateSelected() {
		return event.getMealDateSelected();
	}
	
	public void setMealType(int i) {
		saved = false;
		if (i == 0) 
			event.setMealType(MealType.LUNCH);
		else if(i == 1) 
			event.setMealType(MealType.DINNER);
		else 
			event.setMealType(null);
		
	}
	
	public int getMealType() {
		if(event.getMealType() == null)
			return -1;
		return event.getMealType().ordinal();
	}
	
	public void setMealRadioButtons(int index, boolean value) {
		saved = false;
		event.setMealRadioButtons(index, value);
	}
	
	public boolean getMealRadioButtons(int index) {
		return event.getMealRadioButtons (index);
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
	

	
	/**************************************************************************
	 * 
	 * Step 2 Functions here
	 * 
	 **************************************************************************/
	//Method gets the Guest List in Vector<Guest> form and translates to 
	//Vector<Vector<String>> form.
	Vector<Vector<String>> getGuestNameList(){
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
		saved = false;
		event.getGuestList().add(new Guest());
	}
	
	void addGuest(int index) {
		saved = false;
		event.getGuestList().add(index, new Guest());
	}
	
	void removeGuest(int index){
		saved = false;
		event.getGuestList().remove(index);
	}
	
	//Method searches the corresponding index and field in the 
	//guestList and sets it with the new data.
	void setGuestInfo(int index, String field, String data){
		saved = false;
		switch(field){
			case "Name":
				event.getGuestList().get(index).setName(data);
				break;
			case "Gender":
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
	
	//Method checks if all the guest fields are completed.
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
	
	Vector<Guest> getGuestList(){
		return event.getGuestList();
	}
	
	int numberOfTables(){
		int numberOfTables;
		int numberOfGuests = getGuestList().size();
		if(numberOfGuests<10 && numberOfGuests > 0)
			numberOfTables = 1;
		else if(numberOfGuests%10 != 0)
			numberOfTables = numberOfGuests/10 + 1;
		else 
			numberOfTables = numberOfGuests/10;
		
		return numberOfTables;
	}
	
	
	void setGuestListFinalised(boolean value){
		saved = false;
		event.setGuestListFinalised(value);
	}
	
	boolean getGuestListFinalised(){
		return event.getGuestListFinalised();
	}


	/**************************************************************************
	 * 
	 * Step 3 Functions here
	 * 
	 **************************************************************************/
	//Method gets the Programme Schedule in Vector<Programme> form and translates to 
	//Vector<Vector<String>> form.
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
			
			//Add programmeDate
			programmeDetail.add(currProgramme.getProgrammeDate());
			
			//Add start time to programme detail
			timeStr = String.valueOf(currProgramme.getStartTime());
			if(timeStr.equals("-1"))
				programmeDetail.add("");
			else
				programmeDetail.add(timeStr);

			//Add end time to programme detail
			timeStr = String.valueOf(currProgramme.getEndTime());
			if(timeStr.equals("-1"))
				programmeDetail.add("");
			else
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
	
	void addProgramme(int index) {
		saved = false;
		event.getProgrammeSchedule().add(index, new Programme());
	}
	
	void removeProgramme(int index){
		saved = false;
		event.getProgrammeSchedule().remove(index);
	}
	
	//Method searches the corresponding index and field in the 
	//programmeSchedule and sets it with the new data.
	void setProgrammeInfo(int index, String field, String data){
		saved = false;
		switch(field){
		case "Date":
			event.getProgrammeSchedule().get(index).setProgrammeDate(data);
			break;
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
	
	//Method checks if all the programme fields are completed.
	boolean completedProgrammeFields(){
		if(event.getProgrammeSchedule().size() == 0)
			return false;
		for(Programme programmeCheck : event.getProgrammeSchedule()){
			if(programmeCheck.getProgrammeDate() == "" || programmeCheck.getStartTime() == -1 || programmeCheck.getEndTime() == -1
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
	
	//Method goes through each entry in programme and check if programme date 
	//still falls between start and end date
	void checkProgrammeDates(){
		Vector<Programme> programme = event.getProgrammeSchedule();
		Vector<String> possibleDates = event.getDateList();
		
		for(int i = 0; i < programme.size(); i++){
			String date = programme.get(i).getProgrammeDate();
			if(!possibleDates.contains(date)){
				programme.get(i).setProgrammeDate("");
			}
		}
	}
	
	
	/**************************************************************************
	 * 
	 * Step 4 Functions here
	 * 
	 **************************************************************************/
	double calculateHotelBudget(){
		return (event.getEventBudget() * event.getBudgetRatio()/100.0);
	}
	
	void setBudgetRatio(int budgetRatio) {
		saved = false;
		event.setBudgetRatio(budgetRatio);
	}
	
	int getBudgetRatio() {
		return event.getBudgetRatio();
	}
	
	//Method runs the hotel suggest algorithm and sets the generated hotel list
	//in the event. Since hotelSuggest only considers 1 type of star rating,
	//this method merges with the existing hotel list. Therefore to 
	//generate a new hotel list, one needs to clear the existing one first.
	void hotelSuggest(int stars) throws IOException, Exception{
		saved = false;
		hotelSuggester.setStars(stars);
		hotelSuggester.setEventBudget(event.getEventBudget());
		hotelSuggester.setBudgetRatio(event.getBudgetRatio());
		hotelSuggester.setMealDate(event.getDateListStoredAsMyCalendarObject().get(event.getMealDateSelected()));
		hotelSuggester.setEventMealType(event.getMealType());
		hotelSuggester.setNumberOfGuests(event.getGuestList().size());
		hotelSuggester.setNumberOfTables(numberOfTables());
		try{
			Vector<Hotel> hotelList = hotelSuggester.suggest(dm);
			event.mergeWithExistingHotels(hotelList);
			event.setSelectedHotelIdx(-1);
		} catch(IOException ioEx){
			throw ioEx;
		} catch(Exception ex){
			throw ex;
		}
	}
	
	//Methods returns suggested hotel names in the format: [x] hotelname
	//where x is the number of stars.
	Vector<String> getSuggestedHotelsNames(){
		Vector<String> names = new Vector<String>();
		for(Hotel item : event.getSuggestedHotels()){
			names.add("[" + item.getStars() + "] " + item.getName());
		}
		if(names.size() == 0)
			names.add("No hotels match your criteria!");
		
		return names;
	}
	
	//Method returns the corresponding hotel information in a formatted string.
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

				if(menuItem.getMenuType() == MenuType.HIGH){
					if ( ( ( (menuItem.getPricePerTable()*100) %100) %10) == 0){
						hotelDetails = hotelDetails + "highest price: $" + menuItem.getPricePerTable() + "0" + " per table of 10.\n";
					}
					else
						hotelDetails = hotelDetails + "highest price: $" + menuItem.getPricePerTable() + " per table of 10.\n";
				}
				else{
					if ( ( ( (menuItem.getPricePerTable()*100) %100) %10) == 0){
						hotelDetails = hotelDetails + "lowest price: $" + menuItem.getPricePerTable() + "0" + " per table of 10.\n";
					}
					else
						hotelDetails = hotelDetails + "lowest price: $" + menuItem.getPricePerTable() + " per table of 10.\n";
				}
			}

			return hotelDetails;
		}
		return hotelDetails;
	}
	
	String getHotelName(int index) {
		String hotelName = "";
		if (index > -1) {
			Hotel item = event.getSuggestedHotels().get(index);				
			hotelName = item.getName() + " (" + item.getStars() + "-star)\n";						
		}
		return hotelName;
	}
	
	//Method gets the selected hotel price. This is calculated
	//by searching through the hotel's menu, and using the highest
	//priced menu as the price per table.
	double getSelectedHotelPrice(int index) {
		double PricePerTable = 0;
		if (index > -1) {
			Hotel item = event.getSuggestedHotels().get(index);
			Vector<Menu> menu = item.getMenuList();
				
			for(Menu menuItem : menu){
				if(menuItem.getPricePerTable() > PricePerTable)
					PricePerTable = menuItem.getPricePerTable();
			}
			return PricePerTable * numberOfTables();
		}
		else		//selected index = -1, no hotel selected.
			return 0;
	}
	
	int getSelectedHotelIdx() {
		return event.getSelectedHotelIdx();
	}
	
	void setSelectedHotelIdx(int idx) {
		saved = false;
		event.setSelectedHotelIdx(idx);
		
		//set curr hotel price
		if(idx == -1) {
			event.setHotelBudgetSpent(0.0);	
		}
		else {
			event.setHotelBudgetSpent(getSelectedHotelPrice(idx));			
		}
	}
	
	boolean[] getcheckBox() {
		return event.getcheckBox();
	}
	
	void setcheckBox(boolean[] value) {
		saved = false;
		event.setcheckBox(value);
	}
	
	/**************************************************************************
	 * 
	 * Step 5 Functions here
	 * 
	 **************************************************************************/
	Vector<Integer> getSeatsPerTable(){
		return event.getSeatsPerTable();
	}
	
	void setArrangement(Vector<Integer> indexes, Vector<Integer> seatsPerTable){
		saved = false;
		event.setSeatingArrangementIndex(indexes);
		event.setSeatsPerTable(seatsPerTable);
	}
	
	//Method takes in the rows and columns of 2 guests and swaps them in the 
	//arrangement index.
	void updateArrangementIndex(int oldRow, int oldCol, int newRow, int newCol){
		saved = false;
		Vector<Integer> seatsPerTable = event.getSeatsPerTable();
		Vector<Integer> arrangementIndex = event.getSeatingArrangementIndex();
		
		int indexOfOld = 0;
		for(int i = 0; i < oldCol; i++){
			indexOfOld += seatsPerTable.get(i);
		}
		indexOfOld += oldRow;
		
		int indexOfNew = 0;
		for(int i = 0; i < newCol; i++){
			indexOfNew += seatsPerTable.get(i);
		}
		indexOfNew += newRow;
		
		//swap
		int temp = arrangementIndex.get(indexOfOld);
		arrangementIndex.set(indexOfOld, arrangementIndex.get(indexOfNew));
		arrangementIndex.set(indexOfNew, temp);
	}
	
	//Method gets the seating arrangement in Vector<Vector<String>> form
	//by getting information from arrangementIndex and seatsPerTable.
	Vector<Vector<String>> getArrangement(){
		Vector<Vector<String>> seatingList = new Vector<Vector<String>>();
		
		Vector<Guest> guests = event.getGuestList();
		Vector<Integer> arrangementIndex = event.getSeatingArrangementIndex();
		Vector<Integer> seatsPerTable = event.getSeatsPerTable();
		
		//this returns Tables in columns 
		for(int i = 0; i < 10; i++){
			seatingList.add(new Vector<String>());
		}
		int index = 0;
		for(int i = 0; i < seatsPerTable.size(); i++){
			int currTableSize = seatsPerTable.get(i);
			
			for(int j = 0; j < currTableSize; j++){
				int currGuestIndex = arrangementIndex.get(index+j);
				seatingList.get(j).add("[" + guests.get(currGuestIndex).getGroup() + "]"  + guests.get(currGuestIndex).getName());
			}
			index += currTableSize;
		}
		return seatingList;
	}
	

	/**************************************************************************
	 * 
	 * Step 6 Functions here
	 * 
	 **************************************************************************/
	//Methods translates the Vector<Expense> expenselist into Vector<Vector<String>>
	//form
	Vector<Vector<String>> getExpenseList(){
		Vector<Expense> expenseList;
		Vector<String> expenseDetail=null;
		Vector<Vector<String>> expenseVector = new Vector<Vector<String>>();
		Expense expense;
		String quantityStr, unitCostStr, totalCostStr;


		//Get expenseList from event
		expenseList = event.getExpense();

		for (int i=0; i<expenseList.size(); ++i){
			//expenseDetail points to a new Vector<String> for each expense iteration
			expenseDetail = new Vector<String>();

			//Get current expense item
			expense = expenseList.get(i);

			//Add Item Name
			expenseDetail.add(expense.getItemName());

			//Add Unit Cost
			unitCostStr = Double.toString(expense.getUnitCost());
			expenseDetail.add(unitCostStr);

			//Add Quantity
			quantityStr = Integer.toString(expense.getQuantity());
			expenseDetail.add(quantityStr);

			//Add Total Cost
			totalCostStr = Double.toString(expense.getTotalCost());
			expenseDetail.add(totalCostStr);

			//Add expenseDetail to expenseVector
			expenseVector.add(expenseDetail);
		}
		
		return expenseVector;
	}
	
	//Method searches the corresponding index and field in the 
	//expenses and sets it with the new data.
	void setExpenseInfo(int index, String field, String data){
		saved = false;
		switch(field){
		case "Item Name":
			event.getExpense().get(index).setItemName(data);
			break;
		case "Unit Cost":
			event.getExpense().get(index).setUnitCost(Double.parseDouble(data));
			break;
		case "Quantity":
			event.getExpense().get(index).setQuantity(Integer.parseInt(data));
			break;
		case "Total Cost":
			event.getExpense().get(index).setTotalCost();
			break;
		}
		
		Vector<Expense> expenses = event.getExpense();
		double totalExpenses = 0.0;
		
		for(int i = 0; i < expenses.size(); i++){
			totalExpenses += expenses.get(i).getTotalCost();
		}
		event.setExpenseSpent(totalExpenses);
	}
	
	//Method checks that all fields in expenses are completed.
	boolean completedExpenseFields(){
		if(event.getExpense().size() == 0){
			return false;
		}
		for (Expense expenseCheck : event.getExpense()){
			if(expenseCheck.getItemName().equals("") || expenseCheck.getQuantity() == 0
					|| expenseCheck.getUnitCost() == 0.0 || expenseCheck.getTotalCost() == 0.0)
				return false;
		}
		return true;
	}
	
	boolean getExpenseFinalised(){
		return event.getExpenseFinalised();
	}
	
	void setExpenseFinalised(boolean value){
		saved = false;
		event.setExpenseFinalised(value);
	}
	
	Vector<Expense> getExpense(){
		return event.getExpense();
	}
	
	void addExpense(){
		saved = false;
		event.getExpense().add(new Expense());
	}
	
	void addExpense(int index) {
		saved =false;
		event.getExpense().add(index, new Expense());
	}
	
	void removeExpense(int index){
		saved = false;
		event.getExpense().remove(index);
		
		Vector<Expense> expenses = event.getExpense();
		double totalExpenses = 0.0;
		
		for(int i = 0; i < expenses.size(); i++){
			totalExpenses += expenses.get(i).getTotalCost();
		}
		event.setExpenseSpent(totalExpenses);
	}
			
	double getHotelBudgetSpent() {
		return event.getHotelBudgetSpent();
	}
	
	double getExpenseSpent() {
		return event.getExpenseSpent();
	}

	double getCostPerHead() {
		return event.getCostPerHead();
	}

	void setCostPerHead() {
		double hotelBudgetSpent = event.getHotelBudgetSpent();
		double expenseSpent = event.getExpenseSpent();
		int numberOfGuest = event.getGuestList().size();
		
		double costPerHead = (hotelBudgetSpent + expenseSpent) / numberOfGuest;
		
		event.setCostPerHead(costPerHead);
	}
}	

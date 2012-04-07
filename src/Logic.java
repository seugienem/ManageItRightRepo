import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		startCal.setMonth(date.getMonth());
		startCal.setYear(date.getYear());
		
		event.setStartDateAndTime(startCal);
	}
	
	@SuppressWarnings("deprecation")
	Date getEventStartDate() {
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
			String test = newDate.toGMTString();
			test = test.substring(0, 11);;		//get rid of time
						
			dateList.add(test);
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
	
	public void setMealDateSelected(int idx) {
		event.setMealDateSelected(idx);
	}
	
	public int getMealDateSelected() {
		return event.getMealDateSelected();
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
	
	public void setMealRadioButtons(int index, boolean value) {
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
	
	Vector<Guest> getGuestList(){
		return event.getGuestList();
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
		event.getProgrammeSchedule().remove(index);
	}
	
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
	
	void checkProgrammeDates(){
		
	}
	
	boolean getProgrammeScheduleFinalised(){
		return event.getProgrammeScheduleFinalised();
	}
	
	void setProgrammeScheduleFinalised(boolean value){
		saved = false;
		event.setProgrammeScheduleFinalised(value);
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
		event.setBudgetRatio(budgetRatio);
	}
	
	int getBudgetRatio() {
		return event.getBudgetRatio();
	}
	
	void hotelSuggest(int stars, int ratio) throws IOException, Exception{
		hotelSuggester.setStars(stars);
		hotelSuggester.setEventBudget(event.getEventBudget());
		hotelSuggester.setBudgetRatio(event.getBudgetRatio());
		hotelSuggester.setMealDate(event.getDateListStoredAsMyCalendarObject().get(event.getMealDateSelected()));
		hotelSuggester.setEventMealType(event.getMealType());
		hotelSuggester.setNumberOfGuests(event.getGuestList().size());
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
	 
	double getSelectedHotelPrice(int index) {
		double PricePerTable = 0;
		if (index > -1) {
			Hotel item = event.getSuggestedHotels().get(index);
			Vector<Menu> menu = item.getMenuList();
				
			for(Menu menuItem : menu){									
				if(menuItem.getMenuType() == MenuType.HIGH)
					PricePerTable = menuItem.getPricePerTable();
				else
					PricePerTable = menuItem.getPricePerTable();
								
			}
				
				return PricePerTable * ((getGuestList().size()/10) + 1);
			}
		return PricePerTable * ((getGuestList().size()/10) + 1);
	}
	
	
	
	int getSelectedHotelIdx() {
		//System.out.println(event.getSelectedHotelIdx());
		return event.getSelectedHotelIdx();
	}
	
	void setSelectedHotelIdx(int idx) {
		saved = false;
		//System.out.println("Set index: " + idx);
		event.setSelectedHotelIdx(idx);
		
		//set curr hotel price
		if(idx == -1)
			event.setHotelBudgetSpent(0.0);
		else
			event.setHotelBudgetSpent(getSelectedHotelPrice(idx));
	}
	
	boolean[] getcheckBox() {
		return event.getcheckBox();
	}
	
	void setcheckBox(boolean[] value) {
		event.setcheckBox(value);
	}
	
	/**************************************************************************
	 * 
	 * Step 5 Functions here
	 * 
	 **************************************************************************/
	/*
	//not actually required now. this has been handled in the gui
	Vector<Vector<String>> generateArrangement(int setting){
		switch(setting){
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
		
		return tableAssigner.generateArrangement(event.getGuestList(), 10);
	}
	*/
	
	Vector<Integer> getSeatsPerTable(){
		return event.getSeatsPerTable();
	}
	
	void setArrangement(Vector<Integer> indexes, Vector<Integer> seatsPerTable){
		event.setSeatingArrangementIndex(indexes);
		event.setSeatsPerTable(seatsPerTable);
	}
	
	void updateArrangementIndex(int oldRow, int oldCol, int newRow, int newCol){
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
		event.getExpense().remove(index);
	}
			
	double getHotelBudgetSpent() {
		return event.getHotelBudgetSpent();
	}
	
	/*
	void setHotelBudgetSpent(double hotelBudget) {		
		event.setHotelBudgetSpent(hotelBudget);
	}
	*/
	double getExpenseSpent() {
		return event.getExpenseSpent();
	}
	
	void setExpenseSpent(double expenseSpent) {
		event.setExpenseSpent(expenseSpent);
	}
	
	/*
	double getRemainingBudget() {
		return event.getRemainingBudget();
	}
	*/
	
	/*
	void setRemainingBudget() {
		event.setRemainingBudget(event.getEventBudget()-event.getHotelBudgetSpent()-event.getExpenseSpent());
	}
	*/

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

import java.util.*;
import java.io.*;

public class HotelSuggest {
	
	private Vector<Hotel> hotelList = null;
	private int stars;
	private double eventBudget;
	private int budgetRatio;
	private MyCalendar mealDate;
	private MealType eventMealType;
	private int numberOfGuests; //assume tables of 10pax each
	private int numberOfTables;
	
	//HotelSuggest constructor
	public HotelSuggest(Vector<Hotel> hotelList, int stars, double eventBudget,	int budgetRatio, MyCalendar mealDate, MealType eventMealType, int numberOfGuests, int numberOfTables){
		this.hotelList = hotelList;
		this.stars = stars;
		this.eventBudget = eventBudget;
		this.budgetRatio = budgetRatio;
		this.mealDate = mealDate;
		this.eventMealType = eventMealType;
		this.numberOfGuests = numberOfGuests;
		this.numberOfTables = numberOfTables;
	}
	
	public HotelSuggest() {
		stars = 0;
		eventBudget = 0;
		budgetRatio = 0;
		mealDate = new MyCalendar();
		eventMealType = MealType.DINNER;	
		numberOfGuests = 0;
	}

	//HotelSuggest Getters and Setters
	public Vector<Hotel> getHotelList() {
		return hotelList;
	}

	public void setHotelList(Vector<Hotel> hotelList) {
		this.hotelList = hotelList;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
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

	public MyCalendar getMealDate() {
		return mealDate;
	}

	public void setMealDate(MyCalendar myCalendar) {
		this.mealDate = myCalendar;
	}

	public MealType getEventMealType() {
		return eventMealType;
	}

	public void setEventMealType(MealType eventMealType) {
		this.eventMealType = eventMealType;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}
	
	public void setNumberOfTables(int numberOfTables) {
		this.numberOfTables = numberOfTables;
	}
	
	public int getNumberOfTables() {
		return numberOfTables;
	}
	
	private DayType getDayType(MyCalendar startDate2){
		int day = startDate2.getDayOfTheWeek(); 
		DayType daytype = null;
		
		switch(day){
		case -1: 
			daytype = null;
			break;	
		case 0:
		case 5:
		case 6:
			daytype =  DayType.FRI_SUN; //FRI THRU TO SUNDAY
			break;
		case 1:							//MONDAY 
		case 2:							//THRU 
		case 3:							//TO
		case 4: 						//THURSDAY
			daytype = DayType.NORMAL;
			break;
		}

		return daytype;
	}

	public Vector<Hotel> suggest(DataManager dataM) throws IOException, Exception{		
		//Get day of week from start date of event
		DayType eventDayType = getDayType(mealDate);
		String exceptionErrorMessage = "Please ensure you have:\n";
		
		if (eventDayType == null)
			exceptionErrorMessage = exceptionErrorMessage + "-Start Date\n";
		
		//Read hotelList from DataManager for the first time only
		if(hotelList == null){
			String path = System.getProperty("user.dir");
			File hotelFile = new File(path + File.separator + "Data" + File.separator + "hotelData");
			try{
				this.setHotelList(dataM.importHotels(hotelFile));
			} catch(IOException ioEx){
				throw ioEx;
			}
		}
		
		//Calculate hotel budget
		double hotelBudget = (eventBudget * budgetRatio/100.0);
		if(hotelBudget == 0)
			exceptionErrorMessage = exceptionErrorMessage + "-Hotel budget\n";
				
		if(numberOfGuests == 0)
			exceptionErrorMessage = exceptionErrorMessage + "-Guests\n";
		
		if(eventMealType == null)
			exceptionErrorMessage = exceptionErrorMessage + "-Meal Type\n";
		
		if(!exceptionErrorMessage.equals("Please ensure you have:\n")){
			throw new Exception(exceptionErrorMessage);
		}
		
		//Generate suggested hotels
		Vector<Hotel> suggestedHotels = new Vector<Hotel>();
		Hotel tryHotel;
		Vector<Menu> tryMenu;
		Menu currentMenu;
	
		for (int i=0; i<hotelList.size(); ++i){ //scan through hotel list
			Vector<Menu> selectedMenu = new Vector<Menu>();
			
			if (hotelList.get(i).getStars() == stars){ //if hotel matches preferred star rating
				tryHotel = hotelList.get(i);
				tryMenu = tryHotel.getMenuList();
				
				for (int j=0; j<tryMenu.size(); ++j){ //scan through menu list of current hotel
					currentMenu = tryMenu.get(j);
					if (currentMenu.getDayType() == eventDayType && currentMenu.getMealType() == eventMealType){ //if required day and required meal types match offerings
						if (currentMenu.getPricePerTable()*numberOfTables <= hotelBudget){	//if menu falls within hotel budget
							selectedMenu.add(currentMenu); 								 	//add to selected menu list
						}
					}
				}
				
				Hotel toBeAdded = new Hotel(tryHotel.getName(), tryHotel.getAddress(), tryHotel.getContact(), tryHotel.getEmail(), tryHotel.getWebsite(), selectedMenu, tryHotel.getStars());
				
				if (!toBeAdded.getMenuList().isEmpty()){	//if hotel possesses at least one affordable menu item
					suggestedHotels.add(toBeAdded);		//add hotel to suggested list
				}
			}
		}
		
		return suggestedHotels;
	}
}
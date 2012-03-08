import java.util.*;
import java.io.*;

public class HotelSuggest {
	
	private Vector<Hotel> hotelList = null;
	private int stars;
	private double eventBudget;
	private int budgetRatio;
	private Calendar startDate;
	private MealType eventMealType;
	private int numberOfGuests; //assume tables of 10pax each
	
	//HotelSuggest constructor
	public HotelSuggest(Vector<Hotel> hotelList, int stars, double eventBudget,	int budgetRatio, Calendar startDate, MealType eventMealType, int numberOfGuests){
		this.hotelList = hotelList;
		this.stars = stars;
		this.eventBudget = eventBudget;
		this.budgetRatio = budgetRatio;
		this.startDate = startDate;
		this.eventMealType = eventMealType;
		this.numberOfGuests = numberOfGuests;
	}
	
	public HotelSuggest() {
		stars = 0;
		eventBudget = 0;
		budgetRatio = 0;
		startDate = Calendar.getInstance();
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

	public double getBudgetRatio() {
		return budgetRatio;
	}

	public void setBudgetRatio(int budgetRatio) {
		this.budgetRatio = budgetRatio;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
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
	
	private MealType getMealType(){
		if(startDate.get(Calendar.HOUR) > 15){
			return MealType.LUNCH;
		}
		else
			return MealType.DINNER;
	}
	
	private DayType getDayType(Calendar startDate){
		int day = startDate.get(Calendar.DAY_OF_WEEK); 
		DayType daytype = null;
		
		switch(day){
		case 1:
		case 6:
		case 7:
			daytype =  DayType.FRI_SUN; //FRI THRU TO SUNDAY
			break;
		case 2:							//MONDAY 
		case 3:							//THRU 
		case 4:							//TO
		case 5: 						//THURSDAY
			daytype = DayType.NORMAL;
			break;
		}

		return daytype;
	}

	public Vector<Hotel> suggest(DataManager dataM){
		//Get day of week from start date of event
		DayType eventDayType = getDayType(startDate);
		
		//Get expected meal type based on start time of event
		this.eventMealType = getMealType();
		
		
		//Read hotelList from DataManager for the first time only
		if(hotelList == null){
			String path = System.getProperty("user.dir");
			File hotelFile = new File(path + File.separator + "Data" + File.separator + "hotelData");
			this.setHotelList(dataM.importHotels(hotelFile));
		}
		
		//Calculate hotel budget
		double hotelBudget = (eventBudget * budgetRatio/100.0);
		if(hotelBudget == 0){
			return new Vector<Hotel>();
		}
		
		//Calculate number of tables required 
		int numberOfTables;
		if(numberOfGuests%10 != 0)
			numberOfTables = numberOfGuests/10 + 1;
		else 
			numberOfTables = numberOfGuests/10;
		
		if(numberOfTables == 0){
			//return new Vector<Hotel>();
		}
		
		//Generate suggested hotels
		Vector<Hotel> suggestedHotels = new Vector<Hotel>();
		Hotel tryHotel;
		Vector<Menu> tryMenu;
		Vector<Menu> selectedMenu = new Vector<Menu>();
		
		Menu currentMenu;
	
		for (int i=0; i<hotelList.size(); ++i){ //scan through hotel list
			
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
			
				tryHotel.setMenuList(selectedMenu); 
				
				if (!tryHotel.getMenuList().isEmpty())	//if hotel possesses at least one affordable menu item
					suggestedHotels.add(tryHotel);		//add hotel to suggested list
			}
		}
		
		return suggestedHotels;
	}
	
}

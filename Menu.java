
public class Menu {

	private DayType dayType;
	private MealType mealType;
	private MenuType menuType;
	private double pricePerTable; //NEW
	
	//Menu constructor
	public Menu (DayType dayType, MealType mealType, MenuType menuType, double pricePerTable){
		this.dayType = dayType;
		this.mealType = mealType;
		this.menuType = menuType;
		this.pricePerTable = pricePerTable;
	}

	//Menu Getters and Setters
	public DayType getDayType() {
		return dayType;
	}

	public void setDayType(DayType dayType) {
		this.dayType = dayType;
	}

	public MealType getMealType() {
		return mealType;
	}

	public void setMealType(MealType mealType) {
		this.mealType = mealType;
	}

	public MenuType getMenuType() {
		return menuType;
	}

	public void setMenuType(MenuType menuType) {
		this.menuType = menuType;
	}
	
	public double getPricePerTable() {
		return pricePerTable;
	}

	public void setPricePerTable(double pricePerTable) {
		this.pricePerTable = pricePerTable;
	}
	
}
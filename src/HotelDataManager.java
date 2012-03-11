import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

/*
 * Notes about HotelDataManger
 * 1) The variable File out passed into saveHotels() must be valid, otherwise exception will be thrown.
 * 2) The variable File in passed into loadHotels() must be valid, otherwise exception will be thrown.
 * 3) The variable File in passed into loadHotels() must be a properly formatted CSV, otherwise 
 */

public class HotelDataManager {
	
	public void saveHotels(File out, Vector<Hotel> hotels){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(out));
			
			for(Hotel item:hotels){
				writer.write(item.getName() + ",");
				writer.write(item.getStars() + ",");
				
				Vector<Menu> menuItem = item.getMenuList();
				for(Menu menu:menuItem){
					writer.write(menu.getDayType() + ",");
					writer.write(menu.getMealType() + ",");
					writer.write(menu.getMenuType().toString() + ",");
					writer.write(Double.toString(menu.getPricePerTable()) + ",");
				}
				writer.write("\n");
			}
			
			writer.close();
		} catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public Vector<Hotel> loadHotels(File in) throws DataFormatException{
		Vector<Hotel> hotel = new Vector<Hotel>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(in));
			
			String line;
			while((line = reader.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line, ",");
				
				hotel.add(new Hotel());
				hotel.lastElement().setName(st.nextToken());
				hotel.lastElement().setStars(Integer.parseInt(st.nextToken()));
				
				Vector<Menu> menu = new Vector<Menu>();
				
				while(st.hasMoreTokens()){
					try{
						DayType dayType = Enum.valueOf(DayType.class, st.nextToken());
						MealType mealType = Enum.valueOf(MealType.class, st.nextToken());
						MenuType menuType = Enum.valueOf(MenuType.class, st.nextToken());
						double budget = Double.parseDouble(st.nextToken());
						menu.add(new Menu(dayType, mealType, menuType,budget));
					} catch(IllegalArgumentException illegalEx){
						throw new DataFormatException();
					}
				}
				
				hotel.lastElement().setMenuList(menu);
			}
			
			reader.close();
		} catch(IOException ex){
			ex.printStackTrace();
		}
		return hotel;
	}
}

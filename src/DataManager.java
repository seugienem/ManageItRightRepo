import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//test edit
//to do list
//in saveEvent, consider checking if directory exist
//in saveEvent, check for existing files
//send error to gui in all exceptions

public class DataManager {
	public void saveEvent(File out, Event event) throws Exception{
		//creating a directory
		String path = out.toString() + File.separator + event.getEventName();
		Path dir = Paths.get(path);
		
		if(Files.exists(dir)){
			System.out.println("directory exists");
			Path main = Paths.get(path + File.separator + "main");
			if(Files.exists(main)){
				//check whether to overwrite
				//shld this be handled here?
				throw new IOException();
			}
		}
		
		try{
			Files.createDirectories(Paths.get(path));
		} catch(IOException ex){
			System.out.println("Error: DataManager.saveEvent():Files.createDirectoryFailed.");
		}
		
		//filenames
		String timeStamp = Long.toString(System.currentTimeMillis());
		String main = dir + File.separator + "main";
		String programmeFileName = dir + File.separator + "programme" + timeStamp;
		String guestFileName = dir + File.separator + "guests" + timeStamp;
		String suggestFileName = dir + File.separator + "suggest" + timeStamp;
		
		//2 date strings
		String startDateAndTime = event.getStartDateAndTime().get(Calendar.DATE) + "/" + event.getStartDateAndTime().get(Calendar.MONTH) + "/"
				+ event.getStartDateAndTime().get(Calendar.YEAR) + " " + event.getStartDateAndTime().get(Calendar.HOUR_OF_DAY) + ":" 
				+ event.getStartDateAndTime().get(Calendar.MINUTE);
		String endDateAndTime = event.getEndDateAndTime().get(Calendar.DATE) + "/" + event.getEndDateAndTime().get(Calendar.MONTH) + "/"
				+ event.getEndDateAndTime().get(Calendar.YEAR) + " " + event.getEndDateAndTime().get(Calendar.HOUR_OF_DAY) + ":" 
				+ event.getEndDateAndTime().get(Calendar.MINUTE);
				
		//main file
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(main)));
			
			writer.write(event.getEventName() + "\n");
			writer.write(startDateAndTime + "\n");
			writer.write(endDateAndTime + "\n");
			writer.write(event.getEventBudget() + "\n");
			writer.write(event.getEventType().toString() + "\n");
			writer.write(event.getEventDescription() + "\n");
			
			writer.write("programme" + timeStamp + "\n");
			writer.write("guests" + timeStamp + "\n");
			writer.write("suggest" + timeStamp + "\n");
			
			writer.close();
		} catch(IOException ex) {
			throw ex;
		}
		
		//need to use try code?
		saveProgramme(new File(programmeFileName), event.getProgrammeSchedule());
		saveGuestList(new File(guestFileName), event.getGuestList());
		saveHotels(new File(suggestFileName), event.getSuggestedHotels());
	}
	
	public void saveProgramme(File out, Vector<Programme> programme){ 
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(out));
			
			for(Programme item:programme){
				writer.write(item.getStartTime().get(Calendar.HOUR_OF_DAY) + ":" + item.getStartTime().get(Calendar.MINUTE) + ",");
				writer.write(item.getEndTime().get(Calendar.HOUR_OF_DAY) + ":" + item.getEndTime().get(Calendar.MINUTE) + ",");
				writer.write(item.getTitle() + ",");
				writer.write(item.getInCharge() + "\n");
			}
			
			writer.close();
		} catch(IOException ex) {
			System.out.println("Error: DataManager.saveProgramme() failed.");
			ex.printStackTrace();
		}
	}
	
	public void saveGuestList(File out, Vector<Guest> guestList){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(out));
			
			for(Guest item:guestList){
				writer.write(item.getName() + ",");
				writer.write(item.getGender() + ",");
				writer.write(item.getGroup() + ",");
				writer.write(item.getContactNumber() + ",");
				writer.write(item.getEmailAddress() + ",");
				writer.write(item.getDescription() + "\n");
			}
			
			writer.close();
		} catch(IOException ex) {
			System.out.println("Error: DataManager.saveGuestList() failed.");
		}
	}
	
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
			System.out.println("Error: DataManager.saveSuggestedHotels() failed.");
		}
	}
	
	public Event loadEvent(File in){
		Path dir = Paths.get(in.toString());
		Calendar startDate = new GregorianCalendar();	//needs to be changed
		Calendar endDate = new GregorianCalendar();
		String main = dir + File.separator + "main";
		String eventName = "";
		String eventDescription = "";
		String programmeFileName = "";
		String guestFileName = "";
		String suggestFileName = "";
		float eventBudget = 0;
		EventType eventType = null;

		try{
			BufferedReader reader = new BufferedReader(new FileReader(new File(main)));
			
			eventName = reader.readLine();
			startDate = Parser.parseStringToCalendar(reader.readLine());
			endDate = Parser.parseStringToCalendar(reader.readLine());
			eventBudget = Float.parseFloat(reader.readLine());
			eventType = Enum.valueOf(EventType.class, reader.readLine());
			eventDescription = reader.readLine();
			
			//read filenames
			programmeFileName = reader.readLine();
			guestFileName = reader.readLine();
			suggestFileName = reader.readLine();
			
			reader.close();
		} catch(IOException ex){
			System.out.println("Error: DataManager.loadEvent() failed.");
		}
		
		Vector<Programme> eventProgramme = loadProgramme(new File(dir + File.separator + programmeFileName));
		Vector<Guest> eventGuests = loadGuestList(new File(dir + File.separator + guestFileName));
		Vector<Hotel> suggest = loadHotels(new File(dir + File.separator + suggestFileName));
		
		Event event = new Event(eventName, startDate, endDate, eventBudget, eventGuests, suggest, eventProgramme, eventType, eventDescription);
		
		return event;
	}
	
	public Vector<Programme> loadProgramme(File in){
		Vector<Programme> programme = new Vector<Programme>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(in));
			String line;
			while((line = reader.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line, ",");
				
				programme.add(new Programme());
				programme.lastElement().setStartTime(st.nextToken());
				programme.lastElement().setEndTime(st.nextToken());
				programme.lastElement().setTitle(st.nextToken());
				programme.lastElement().setInCharge(st.nextToken());
			}
			
			reader.close();
		} catch(IOException ex){
			System.out.println("Error: DataManager.loadProgramme() failed.");
		}
		
		return programme;
	}
	
	public Vector<Guest> loadGuestList(File in){
		Vector<Guest> guestList = new Vector<Guest>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(in));
			String line;
			while((line = reader.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line, ",");
				
				guestList.add(new Guest());
				guestList.lastElement().setName(st.nextToken());
				guestList.lastElement().setGender(Enum.valueOf(Gender.class, st.nextToken()));
				guestList.lastElement().setGroup(st.nextToken());
				guestList.lastElement().setContactNumber(st.nextToken());
				guestList.lastElement().setEmailAddress(st.nextToken());
				guestList.lastElement().setDescription(st.nextToken());
			}
			
			reader.close();
		} catch(IOException ex){
			System.out.println("Error: DataManager.loadGuestList() failed.");
		}
		return guestList;
	}
	
	public Vector<Hotel> loadHotels(File in){
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
					DayType dayType = Enum.valueOf(DayType.class, st.nextToken());
					MealType mealType = Enum.valueOf(MealType.class, st.nextToken());
					MenuType menuType = Enum.valueOf(MenuType.class, st.nextToken());
					double budget = Double.parseDouble(st.nextToken());
					menu.add(new Menu(dayType, mealType, menuType,budget));
				}
				
				hotel.lastElement().setMenuList(menu);
			}
			
			reader.close();
		} catch(IOException ex){
			System.out.println("Error: DataManager.loadSuggestedHotel() failed.");
		}
		return hotel;
	}
}

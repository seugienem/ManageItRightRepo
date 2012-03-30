import java.io.*;
import java.util.zip.DataFormatException;
import java.util.*;

/*
 * Notes about using DataManger:
 * 1) Simply a collection of various data managers
 * 2) works as a interface
 * 3) see various dataManagers for more info
 */

public class DataManager{
	EventDataManager eventDataManager;
	ProgrammeDataManager programmeDataManager;
	HotelDataManager hotelDataManager;
	GuestDataManager guestDataManager;
	ExpenseDataManager expenseDataManager;
	
	public DataManager(){
		eventDataManager = new EventDataManager();
		programmeDataManager = new ProgrammeDataManager();
		hotelDataManager = new HotelDataManager();
		guestDataManager = new GuestDataManager();
		expenseDataManager = new ExpenseDataManager();
	}
	
	
	//methods
	public void save(File out, Event eventOut){
		eventDataManager.serializeEvent(out, eventOut);
	}
	
	public Event load(File in) throws Exception{
		try{
			Event eventIn = eventDataManager.deserializeEvent(in);
			return eventIn;
		} catch(Exception ex){
			throw ex;
		}
	}
	
	public void exportGuest(File out, Vector<Guest> guestOut){
		guestDataManager.saveGuestList(out, guestOut);
	}
	
	public Vector<Guest> importGuest(File in) throws DataFormatException{
		try{
			Vector<Guest> guestIn = guestDataManager.loadGuestList(in);
			return guestIn;
		} catch(DataFormatException dataEx){
			throw new DataFormatException();
		}
	}
	
	public void exportHotels(File out, Vector<Hotel> hotelOut){
		hotelDataManager.saveHotels(out, hotelOut);
	}
	
	public Vector<Hotel> importHotels(File in) throws IOException{
		try{
			Vector<Hotel> hotelIn = hotelDataManager.loadHotels(in);
			return hotelIn;
		} catch(DataFormatException dataEx){
			dataEx.printStackTrace();
			return new Vector<Hotel>();
		} catch(IOException ioEx){
			throw ioEx;
		}
	}
	
	public void exportProgramme(File out, Vector<Programme> programmeOut){
		programmeDataManager.saveProgramme(out, programmeOut);
	}
	
	public Vector<Programme> importProgramme(File in){
		try{
			Vector<Programme> programmeIn = programmeDataManager.loadProgramme(in);
			return programmeIn;
		} catch(DataFormatException dataEx){
			dataEx.printStackTrace();
			return new Vector<Programme>();
		}
	}
	
	public void exportExpense(File out, Vector<Expense> expenseOut){
		expenseDataManager.saveExpense(out, expenseOut);
	}
	
	public Vector<Expense> importExpense(File in){
		try{
			Vector<Expense> expenseIn = expenseDataManager.loadExpense(in);
			return expenseIn;
		} catch(DataFormatException dataEx){
			dataEx.printStackTrace();
			return new Vector<Expense>();
		}
	}
}

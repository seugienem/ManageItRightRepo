import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

/*
 * Notes about GuestDataManager
 * 1) The variable File out must be a valid directory otherwise exception will be thrown
 * 2) The variable File in must be a properly formatted CSV file, otherwise 
 * 3) The variable File in must be a valid directory, otherwise exception will be thrown
 */

public class GuestDataManager {
	final int numberOfTokens = 6;
	
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
			ex.printStackTrace();
		}
	}
	
	public Vector<Guest> loadGuestList(File in) throws DataFormatException{
		Vector<Guest> guestList = new Vector<Guest>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(in));
			String line;
			while((line = reader.readLine()) != null){
				String [] guestData = line.split(",", numberOfTokens);
				if(guestData.length != numberOfTokens)
					throw new DataFormatException("numberOfTokens is " + guestData.length + " instead of " + numberOfTokens);
				
				guestList.add(new Guest());
				guestList.lastElement().setName(guestData[0]);
				try{
					guestList.lastElement().setGender(Enum.valueOf(Gender.class, guestData[1]));
				} catch(IllegalArgumentException ex){
					throw new DataFormatException("Error getting enum value of gender.");
				}
				
				guestList.lastElement().setGroup(guestData[2]);
				guestList.lastElement().setEmailAddress(guestData[3]);
				guestList.lastElement().setContactNumber(guestData[4]);
				guestList.lastElement().setDescription(guestData[5]);
			}
			
			reader.close();
		} catch(IOException ex){
			ex.printStackTrace();
		}
		return guestList;
	}
}

import java.io.BufferedReader;
import java.util.zip.DataFormatException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

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
				StringTokenizer st = new StringTokenizer(line, ",");
				
				if(st.countTokens() != numberOfTokens){
					throw new DataFormatException();
				}
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
			ex.printStackTrace();
		}
		return guestList;
	}
}

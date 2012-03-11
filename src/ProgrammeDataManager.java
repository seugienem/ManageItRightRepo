import java.io.*;
import java.util.zip.DataFormatException;
import java.util.StringTokenizer;
import java.util.Vector;

/*
 * Notes about ProgrammeDataManager
 * 1) The variable File out passed in to saveProgamme() must be valid, otherwise exception will be thrown
 * 2) The variable File in passed in to loadProgramme() must be a properly formatted CSV file, otherwise the Vector<Programme> returned will be wrong/exception will be thrown
 * 3) The variable File in passed into loadProgramme() must be a valid directory, otherwise an exception will be thrown
 */

public class ProgrammeDataManager {	
	final int numberOfTokens = 4;
	
	public void saveProgramme(File out, Vector<Programme> programme){ 
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(out));
			
			for(Programme item:programme){
				writer.write(item.getStartTime() + ",");
				writer.write(item.getEndTime() + ",");
				writer.write(item.getTitle() + ",");
				writer.write(item.getInCharge() + "\n");
			}
			
			writer.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public Vector<Programme> loadProgramme(File in) throws DataFormatException{
		Vector<Programme> programme = new Vector<Programme>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(in));
			String line;
			while((line = reader.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line, ",");
				
				//perhaps add in check here, if st does not have enough token, thrown exception
				if(st.countTokens() != numberOfTokens){
					throw new DataFormatException();
				}
				programme.add(new Programme());
				programme.lastElement().setStartTime(Integer.parseInt(st.nextToken()));
				programme.lastElement().setEndTime(Integer.parseInt(st.nextToken()));
				programme.lastElement().setTitle(st.nextToken());
				programme.lastElement().setInCharge(st.nextToken());
			}
			
			reader.close();
		} catch(IOException ex){
			ex.printStackTrace();
		}
		
		return programme;
	}
}

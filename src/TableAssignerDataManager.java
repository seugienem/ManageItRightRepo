import java.io.*;
import java.util.*;

/*
 * Notes about TableAssignerDataManager
 * 1) The variable File out must be a valid directory otherwise exception will be thrown
 */

public class TableAssignerDataManager {
	
	
	public void saveTableAssignmentList(File out, Vector<Vector<String>> tableAssignmentList){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(out));
			
			//Writes the table numbers first
			for(int i = 1; i <= tableAssignmentList.get(0).size(); i++){
				writer.write("Table " + i + ",");
			}
			writer.write("\n");
			
			//Writes the remaining entries
			
			for (int i = 0; i < 10 ; i++) {
				Iterator<String> ite = tableAssignmentList.get(i).iterator();
				while(ite.hasNext()) 
					writer.write(ite.next() + ",");
				writer.write("\n");
			}
			
			writer.close();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
}

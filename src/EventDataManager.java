import java.io.*;

/*
 * Notes on EventDataManager
 * 1) Exceptions still need to be handled properly. Currently, only the stack trace is printed.
 * 2) In future, exceptions should be passed on to dataManager, who will pass it on to whoever called it.
 * 3) Possible failed scenarios include bad serialize file input, bad directory. 
 */

public class EventDataManager {
	public void serializeEvent(File out, Event eventOut){
		try{
			//need to change the directory
			FileOutputStream fs = new FileOutputStream(out);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(eventOut);
			os.close();
			
		} catch(FileNotFoundException fileEx){	//if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
			fileEx.printStackTrace();
		} catch(IOException IOEx){	//if an I/O error occurs while writing stream header, generated by os
			IOEx.printStackTrace();
		} 
	}
	
	public Event deserializeEvent(File in) throws Exception{
		Event eventIn = new Event();
		try{
			FileInputStream fs = new FileInputStream(in);
			ObjectInputStream is = new ObjectInputStream(fs);
			Object obj = is.readObject();
			eventIn = (Event)obj;
			
			is.close();
		} catch(FileNotFoundException fileEx){
			throw fileEx;
		} catch (IOException IOEx){
			throw IOEx;
		} catch(ClassNotFoundException classEx){	//Class of a serialized object cannot be found.
			throw classEx;
		} 
		
		return eventIn;
	}
}

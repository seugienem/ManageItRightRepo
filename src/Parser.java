import java.text.ParseException;
import java.util.*;
import java.text.*;

public class Parser {
	public static Calendar parseStringToCalendar(String dateAndTime){
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		
		try{
			Date d1 = dateTime.parse(dateAndTime);
			cal.setTime(d1);
			return cal;
		} catch (ParseException pe){
			System.out.println("Error: Event.parseStringToCalendar() failed.");
		}
		return cal;
	}

	public static Calendar parseStringToCalendarTime(String timeStr){
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat time = new SimpleDateFormat("HH:mm");
		
		try{
			Date d1 = time.parse(timeStr);
			cal.setTime(d1);
			return cal;
		} catch(ParseException pe){
			System.out.println("Error: Event.parseStringToCalendarTime() failed.");
		}
		return cal;
	}
}

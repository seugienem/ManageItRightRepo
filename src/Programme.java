import java.io.*;
import java.util.Date;


public class Programme implements Serializable{
	private static final long serialVersionUID = 1L;
	private String programmeDate;
	private int startTime;
	private int endTime;
	private String title;
	private String inCharge;
	
	public Programme(){
		programmeDate = "";
		startTime = 0;
		endTime = 0;
		title = "";
		inCharge = "";
	}
	
	public String getDate() {
		return programmeDate;
	}
	
	public void setDate(String date) {
		this.programmeDate = date;
	}
	
	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	
/*	public void setStartTime(String startTime){
		this.startTime = Parser.parseStringToCalendarTime(startTime);
	}
*/
	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	
/*	public void setEndTime(String endTime){
		this.endTime = Parser.parseStringToCalendarTime(endTime);
	}
*/
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInCharge() {
		return inCharge;
	}

	public void setInCharge(String inCharge) {
		this.inCharge = inCharge;
	}
	
}

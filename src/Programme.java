import java.io.*;


public class Programme implements Serializable{
	private static final long serialVersionUID = 1L;
	private String programmeDate;
	private int startTime;
	private int endTime;
	private String title;
	private String inCharge;
	
	public Programme(){
		programmeDate = "";
		startTime = -1;
		endTime = -1;
		title = "";
		inCharge = "";
	}
	
	public Programme(String programmeDate, int startTime, int endTime, String title, String inCharge) {
		this.programmeDate = programmeDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.title = title;
		this.inCharge = inCharge;
	}


	public void setProgrammeDate(String date) {
		this.programmeDate = date;
	}
	
	public String getProgrammeDate(){
		return programmeDate;
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

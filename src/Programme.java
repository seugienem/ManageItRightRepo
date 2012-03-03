import java.util.*;

public class Programme {

	private Calendar startTime;
	private Calendar endTime;
	private String title;
	private String inCharge;
	
	public Programme(){
		
	}
	
	public Programme(Calendar startTime, Calendar endTime, String title, String inCharge) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.title = title;
		this.inCharge = inCharge;
	}
	
	public Programme(String startTime, String endTime, String title, String inCharge){
		this.startTime = Parser.parseStringToCalendarTime(startTime);
		this.endTime = Parser.parseStringToCalendarTime(endTime);
		this.title = title;
		this.inCharge = inCharge;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	
	public void setStartTime(String startTime){
		this.startTime = Parser.parseStringToCalendarTime(startTime);
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
	
	public void setEndTime(String endTime){
		this.endTime = Parser.parseStringToCalendarTime(endTime);
	}

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

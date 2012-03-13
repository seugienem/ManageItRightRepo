import java.io.*;

public class MyCalendar implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int dayOfTheWeek;
	private int date;
	private int month;
	private int year;
	private int hour;
	private int min;
	
	
	public MyCalendar(){
		this.dayOfTheWeek = 0;
		this.date = 0;
		this.month = 0;
		this.year = 0;
		this.hour = 0;
		this.min = 0;
	}
	
	public void setDayOfTheWeek(int day) {
		this.dayOfTheWeek = day;
	}
	
	public int getDayOfTheWeek() {
		return this.dayOfTheWeek;
	}
	
	public void setDate(int date) {
		this.date = date;		
	}
	
	public int getDate() {
		return this.date;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public int getMonth() {
		return this.month;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getYear() {
		return this.year;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public int getHour() {
		return this.hour;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public int getMin() {
		return this.min;
	}
}
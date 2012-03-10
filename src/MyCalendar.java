import java.io.*;

public class MyCalendar implements Serializable{
	private int dayOfTheWeek;
	private int date;
	private int month;
	private int year;
	private int hour;
	private int min;
	
	
	public MyCalendar(){
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
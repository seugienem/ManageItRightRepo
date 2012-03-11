import java.util.*;
import java.io.*;

public class Hotel implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String address;	//NEW
	private String contact;	//NEW
	private String email;	//NEW
	private String website;	//NEW
	private Vector<Menu> menuList;
	private int stars;
	
	public Hotel(){
		
	}
	//Hotel constructor
	public Hotel(String name, String address, String contact, String email, String website, Vector<Menu> menuList, int stars) {
		this.name = name;
		this.address = address;
		this.contact = contact;
		this.email = email;
		this.website = website;
		this.menuList = menuList;
		this.stars = stars;
	}

	//Hotel Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Vector<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(Vector<Menu> menuList) {
		this.menuList = menuList;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}
		
}

import java.util.*;

public class Hotel {
	
	private String name;
	private String address;	//NEW
	private int contact;	//NEW
	private String email;	//NEW
	private String website;	//NEW
	private Vector<Menu> menuList;
	private int stars;
	
	public Hotel(){
		
	}
	
	public Hotel(String name, int stars, Vector<Menu> menuList){
		this.name = name;
		this.stars = stars;
		this.menuList = menuList;
	}
	//Hotel constructor
	public Hotel(String name, String address, int contact, String email, String website, Vector<Menu> menuList, int stars) {
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

	public int getContact() {
		return contact;
	}

	public void setContact(int contact) {
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

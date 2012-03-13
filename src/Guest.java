import java.io.*;

public class Guest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String emailAddress;
	private String description;
	private String group;
	private String contactNumber;
	private Gender gender;
	
	public Guest(){
		this.name = "";
		this.emailAddress = "";
		this.description = "";
		this.group = "";
		this.contactNumber = "";
		this.gender = Gender.UNKNOWN;
	}
	
	public Guest(String name, Gender gender, String emailAddress, String group, String contactNumber){
		this.name = name; this.gender = gender; this.emailAddress = emailAddress;
		this.group = group; this.contactNumber = contactNumber;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setEmailAddress(String emailAddress){
		this.emailAddress = emailAddress;
	}
	
	public String getEmailAddress(){
		return emailAddress;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setGroup(String group){
		this.group = group;
	}
	
	public String getGroup(){
		return group;
	}
	
	public void setContactNumber(String contactNumber){
		this.contactNumber = contactNumber;
	}
	
	public String getContactNumber(){
		return contactNumber;
	}
	
	public void setGender(Gender gender){
		this.gender = gender;
	}
	
	public Gender getGender(){
		return gender;
	}
	
}

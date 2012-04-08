import static org.junit.Assert.*;

import java.util.Date;
import java.util.Vector;

import org.junit.Test;


public class LogicTest {
	@Test
	public void testStep1Status() {
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());
		assertEquals("Result", 0, tester.step1Status());

		event.setEventName("event name");
		assertEquals("Result", 1, tester.step1Status());

		MyCalendar start = new MyCalendar();
		start.setYear(2012);
		event.setStartDateAndTime(start);
		assertEquals("Result", 1, tester.step1Status());

		MyCalendar end = new MyCalendar();
		end.setYear(2012);
		event.setEndDateAndTime(end);
		assertEquals("Result", 1, tester.step1Status());

		event.setEventType(EventType.ANNIVERSARY);
		assertEquals("Result", 1, tester.step1Status());

		event.setMealType(MealType.DINNER);
		assertEquals("Result", 1, tester.step1Status());

		event.setEventDescription("event description");
		assertEquals("Result", 1, tester.step1Status());

		event.setEventBudget(1000);
		assertEquals("Result", 2, tester.step1Status());
	}

	@Test
	public void testStep2Status(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());
		assertEquals("Result", 0, tester.step2Status());

		event.setGuestList(new Vector<Guest>());
		assertEquals("Result", 0, tester.step2Status());

		event.getGuestList().add(new Guest());
		assertEquals("Result", 1, tester.step2Status());

		event.getGuestList().get(0).setName("name");
		event.getGuestList().get(0).setGroup("group");
		event.getGuestList().get(0).setGender(Gender.MALE);
		event.getGuestList().get(0).setEmailAddress("email");
		event.getGuestList().get(0).setDescription("description");
		event.getGuestList().get(0).setContactNumber("12323");
		event.setGuestListFinalised(true);
		assertEquals("Result", 3, tester.step2Status());
	}

	@Test
	public void testStep3Status(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());
		assertEquals("Result", 0, tester.step3Status());

		event.setProgrammeSchedule(new Vector<Programme>());
		assertEquals("Result", 0, tester.step3Status());

		event.getProgrammeSchedule().add(new Programme());
		assertEquals("Result", 1, tester.step3Status());

		event.getProgrammeSchedule().get(0).setStartTime(1000);
		event.getProgrammeSchedule().get(0).setEndTime(1200);
		event.getProgrammeSchedule().get(0).setInCharge("person");
		event.getProgrammeSchedule().get(0).setTitle("title");
		event.getProgrammeSchedule().get(0).setProgrammeDate("date");
		event.setProgrammeScheduleFinalised(true);
		assertEquals("Result", 3, tester.step3Status());
	}

	@Test
	public void testStep4Status(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());
		assertEquals("Result", 0, tester.step4Status());

		event.setSelectedHotelIdx(1);
		assertEquals("Result", 1, tester.step4Status());
	}

	@Test
	public void testSetEventType(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		tester.setEventType(-1);
		assertEquals("Result", null, event.getEventType());

		tester.setEventType(11);
		assertEquals("Result", null, event.getEventType());

		tester.setEventType(2);
		assertEquals("Result", EventType.AWARD_CEREMONY, event.getEventType());
	}

	@Test
	public void testGetEventType(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		event.setEventType(EventType.ANNIVERSARY);
		assertEquals("Result", 0, tester.getEventType());

		event.setEventType(null);
		assertEquals("Result", -1, tester.getEventType());
	}

	@Test
	public void testSetEventName(){
		//trivial
	}

	/*
	 * If we only call setEventStartDate by passing on a Date object that is generated by JCalendar, 
	 * we can be sure no garbage data is passed in.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testSetEventStartDate(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		Date date = new Date(2012, 11, 12);
		tester.setEventStartDate(date);
		assertEquals("Result", 2012, event.getStartDateAndTime().getYear());
		assertEquals("Result", 11, event.getStartDateAndTime().getMonth());
		assertEquals("Result", 12, event.getStartDateAndTime().getDate());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetEventStartDate(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		MyCalendar calendar = new MyCalendar();
		calendar.setYear(2012);
		calendar.setMonth(11);
		calendar.setDate(30);
		event.setStartDateAndTime(calendar);
		assertEquals("Result", 2012, tester.getEventStartDate().getYear());
		assertEquals("Result", 11, tester.getEventStartDate().getMonth());
		assertEquals("Result", 30, tester.getEventStartDate().getDate());

		event.setStartDateAndTime(new MyCalendar());
		assertEquals("Result", null, tester.getEventStartDate());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSetEventEndDate(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		Date date = new Date(2012, 11, 12);
		tester.setEventEndDate(date);
		assertEquals("Result", 2012, event.getEndDateAndTime().getYear());
		assertEquals("Result", 11, event.getEndDateAndTime().getMonth());
		assertEquals("Result", 12, event.getEndDateAndTime().getDate());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetEventEndDate(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		MyCalendar calendar = new MyCalendar();
		calendar.setYear(2012);
		calendar.setMonth(11);
		calendar.setDate(30);
		event.setEndDateAndTime(calendar);
		assertEquals("Result", 2012, tester.getEventEndDate().getYear());
		assertEquals("Result", 11, tester.getEventEndDate().getMonth());
		assertEquals("Result", 30, tester.getEventEndDate().getDate());

		event.setEndDateAndTime(new MyCalendar());
		assertEquals("Result", null, tester.getEventEndDate());
	}

	@Test
	public void testCheckDate(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		//start before end
		MyCalendar start = new MyCalendar();
		start.setYear(2012);
		start.setMonth(4);
		start.setDate(3);
		event.setStartDateAndTime(start);

		MyCalendar end = new MyCalendar();
		end.setYear(2012);
		end.setMonth(4);
		end.setDate(4);
		event.setEndDateAndTime(end);

		assertEquals("Result", true, tester.checkDate());

		end.setDate(1);
		end.setMonth(5);
		assertEquals("Result", true, tester.checkDate());

		end.setMonth(3);
		end.setYear(2013);
		assertEquals("Result", true, tester.checkDate());

		//start same as end
		end.setYear(2012);
		end.setMonth(4);
		end.setDate(3);
		assertEquals("Result", true, tester.checkDate());

		//start after end
		start.setDate(4);
		assertEquals("Result", false, tester.checkDate());

		start.setMonth(5);
		assertEquals("Result", false, tester.checkDate());

		start.setYear(2013);
		assertEquals("Result", false, tester.checkDate());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSetDateList(){
		//precondition, start date is before end date
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		tester.setDateList(new Date(112, 10, 2), new Date(112, 10, 2));
		assertEquals("Results", 1, event.getDateList().size());
		assertEquals("Results", 1, event.getDateListStoredAsMyCalendarObject().size());
		assertEquals("Results", "1 Nov 2012 ", event.getDateList().get(0));	//Java Date stores 2012 as 2012 - 1900 etc.
		assertEquals("Results", 112, event.getDateListStoredAsMyCalendarObject().get(0).getYear());
		assertEquals("Results", 10, event.getDateListStoredAsMyCalendarObject().get(0).getMonth());
		assertEquals("Results", 2, event.getDateListStoredAsMyCalendarObject().get(0).getDate());	
	}

	@Test
	public void testGetGuestNameList(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		//Add guest
		event.getGuestList().add(new Guest("Sally", Gender.FEMALE, "FINANCE", "sally@email.com", "12345678", "Chief Financial Officer"));

		//Test 
		Vector<Vector<String>> testerGuestList = tester.getGuestNameList();
		assertEquals("Results", "Sally", testerGuestList.get(0).get(0));
		assertEquals("Results", "Female", testerGuestList.get(0).get(1));
		assertEquals("Results", "FINANCE", testerGuestList.get(0).get(2));
		assertEquals("Results", "sally@email.com", testerGuestList.get(0).get(3));
		assertEquals("Results", "12345678", testerGuestList.get(0).get(4));
		assertEquals("Results", "Chief Financial Officer", testerGuestList.get(0).get(5));
	}

	@Test
	public void testSetGuestInfo(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		//Add guest
		event.getGuestList().add(new Guest("Sally", Gender.FEMALE, "FINANCE", "sally@email.com", "12345678", "Chief Financial Officer"));

		//Test
		tester.setGuestInfo(0, "Name", "Kevin");
		assertEquals("Results", "Kevin", event.getGuestList().get(0).getName());
		tester.setGuestInfo(0, "Gender", "male");
		assertEquals("Results", Gender.MALE, event.getGuestList().get(0).getGender());
		tester.setGuestInfo(0, "Group", "IT");
		assertEquals("Results", "IT", event.getGuestList().get(0).getGroup());
		tester.setGuestInfo(0, "Email", "kevin@email.com");
		assertEquals("Results", "kevin@email.com", event.getGuestList().get(0).getEmailAddress());
		tester.setGuestInfo(0, "Contact Number", "87654321");
		assertEquals("Results", "87654321", event.getGuestList().get(0).getContactNumber());
		tester.setGuestInfo(0, "Description", "Product Development Manager");
		assertEquals("Results", "Product Development Manager", event.getGuestList().get(0).getDescription());
	}

	@Test
	public void testCompletedGuestFields(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		//Test for initially empty guest list
		assertEquals("Results", false, tester.completedGuestFields());

		//Test for completed guest fields
		event.getGuestList().add(new Guest("Sally", Gender.FEMALE, "FINANCE", "sally@email.com", "12345678", "Chief Financial Officer"));
		assertEquals("Results", true, tester.completedGuestFields());

		//Test for incomplete guest fields
		event.getGuestList().add(new Guest("", Gender.FEMALE, "FINANCE", "sally@email.com", "12345678", "Chief Financial Officer"));
		assertEquals("Results", false, tester.completedGuestFields());
		event.getGuestList().add(new Guest("Sally", Gender.UNKNOWN, "FINANCE", "sally@email.com", "12345678", "Chief Financial Officer"));
		assertEquals("Results", false, tester.completedGuestFields());
		event.getGuestList().add(new Guest("Sally", Gender.FEMALE, "", "sally@email.com", "12345678", "Chief Financial Officer"));
		assertEquals("Results", false, tester.completedGuestFields());
		event.getGuestList().add(new Guest("Sally", Gender.FEMALE, "FINANCE", "", "12345678", "Chief Financial Officer"));
		assertEquals("Results", false, tester.completedGuestFields());
		event.getGuestList().add(new Guest("Sally", Gender.FEMALE, "FINANCE", "sally@email.com", "", "Chief Financial Officer"));
		assertEquals("Results", false, tester.completedGuestFields());
		event.getGuestList().add(new Guest("Sally", Gender.FEMALE, "FINANCE", "sally@email.com", "12345678", ""));
		assertEquals("Results", false, tester.completedGuestFields());
	}

	@Test
	public void testGetProgrammeSchedule(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		//Add an activity to programme schedule
		event.getProgrammeSchedule().add(new Programme("1 Nov 2012", 1300, 1400, "Cocktail Party", "Jonathan"));

		//Test
		Vector<Vector<String>> testerProgrammeSchedule = tester.getProgrammeSchedule();
		assertEquals("Results", "1 Nov 2012", testerProgrammeSchedule.get(0).get(0));
		assertEquals("Results", "1300", testerProgrammeSchedule.get(0).get(1));
		assertEquals("Results", "1400", testerProgrammeSchedule.get(0).get(2));
		assertEquals("Results", "Cocktail Party", testerProgrammeSchedule.get(0).get(3));
		assertEquals("Results", "Jonathan", testerProgrammeSchedule.get(0).get(4));
	}

	@Test
	public void testSetProgrammeInfo(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());

		//Add guest
		event.getProgrammeSchedule().add(new Programme("1 Nov 2012", 1300, 1400, "Cocktail Party", "Jonathan"));

		//Test
		tester.setProgrammeInfo(0, "Date", "25 December 2012");
		assertEquals("Results", "25 December 2012", event.getProgrammeSchedule().get(0).getProgrammeDate());
		tester.setProgrammeInfo(0, "Start Time", "2000");
		assertEquals("Results", 2000, event.getProgrammeSchedule().get(0).getStartTime());
		tester.setProgrammeInfo(0, "End Time", "2130");
		assertEquals("Results", 2130, event.getProgrammeSchedule().get(0).getEndTime());
		tester.setProgrammeInfo(0, "Programme", "Christmas Party");
		assertEquals("Results", "Christmas Party", event.getProgrammeSchedule().get(0).getTitle());
		tester.setProgrammeInfo(0, "In-Charge", "Michelle");
		assertEquals("Results", "Michelle", event.getProgrammeSchedule().get(0).getInCharge());
	}

	@Test
	public void testCompletedProgrammeFields(){
		Event event = new Event();
		Logic tester = new Logic(event, new DataManager());
		
		//Test for initially empty programme schedule
		assertEquals("Results", false, tester.completedProgrammeFields());
		
		//Add an activity to programme schedule
		event.getProgrammeSchedule().add(new Programme("1 Nov 2012", 1300, 1400, "Cocktail Party", "Jonathan"));
		
		//Test
		event.getProgrammeSchedule().add(new Programme("", 1300, 1400, "Cocktail Party", "Jonathan"));
		assertEquals("Results", false, tester.completedProgrammeFields());
		event.getProgrammeSchedule().add(new Programme("1 Nov 2012", -1, 1400, "Cocktail Party", "Jonathan"));
		assertEquals("Results", false, tester.completedProgrammeFields());
		event.getProgrammeSchedule().add(new Programme("1 Nov 2012", 1300, -1, "Cocktail Party", "Jonathan"));
		assertEquals("Results", false, tester.completedProgrammeFields());
		event.getProgrammeSchedule().add(new Programme("1 Nov 2012", 1300, 1400, "", "Jonathan"));
		assertEquals("Results", false, tester.completedProgrammeFields());
		event.getProgrammeSchedule().add(new Programme("1 Nov 2012", 1300, 1400, "Cocktail Party", ""));
		assertEquals("Results", false, tester.completedProgrammeFields());
	}

	@Test
	public void testHotelSuggest(){
		
	}

	@Test
	public void testGuestSuggestedHOtelsNames(){

	}

	@Test
	public void testGetHotelInformation(){

	}

	@Test
	public void testGetSelectedHotelPrice(){

	}

	@Test
	public void testUpdateArrangementIndex(){

	}

	@Test
	public void testGetArrangement(){

	}

	@Test
	public void testGetExpenseList(){

	}

	@Test
	public void testSetExpenseInfo(){

	}

	@Test
	public void testCompletedExpenseFields(){

	}
}
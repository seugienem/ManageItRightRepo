import static org.junit.Assert.*;

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
}

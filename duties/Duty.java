package duties;

public class Duty {
	private String location; //where the duty is, ex atrium, 8th floor
	private Time time; // when the duty is ex morning, first break, lunch, after school.
	int pointsDeducted;
	
	public Duty() {} //empty constructor
	
	public Duty(String location, Time t) { // constructor with Time
		this.setLocation(location);
		this.setTime(t.getHours(), t.getMinutes());
	}
	
	public Duty (String location, int hours, int minutes) { // constructor with hours and minutes
		this.setLocation(location);
		this.setTime(hours, minutes);
	}
	public String getLocation() { //return the location
		return location;
	}
	public void setLocation(String location) { // set the location
		this.location = location;
	}
	public String getTime() { //return the time in a string
		return time.toString();
	}
	public void setTime(int hours, int minutes) { // set the time
		time.setTime(hours, minutes);
	}
	
	public String toString() { //return the duty in a string
		return "Location of Duty: " + this.location + " Time of duty: " + this.time;
	}
	
	
}

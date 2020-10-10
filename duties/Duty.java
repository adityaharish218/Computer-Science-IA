package duties;

public class Duty {
	private String location; //where the duty is, ex atrium, 8th floor
	private String time; // when the duty is ex morning, first break, lunch, after school.
	
	public Duty() {}
	
	public Duty(String location, String time) {
		this.setLocation(location);
		this.setTime(time);
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}

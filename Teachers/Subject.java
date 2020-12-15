package Teachers;

public class Subject {
	private String name; //Name of subject
	private String meetingDay; //Day teachers meet
	//Setters and Getters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMeetingDay() {
		return meetingDay;
	}
	public void setMeetingDay(String meetingDay) {
		this.meetingDay = meetingDay;
	}
	public Subject(String name, String meetingDay) {
		this.name = name;
		this.meetingDay = meetingDay;
	}
	public String toString() {
		return " Name : " + this.getName() + " Meeting Day " + this.getMeetingDay() + " ";
	}
	 
	
}

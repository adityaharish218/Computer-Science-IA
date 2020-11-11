package duties;

public class Duty {
	private String name; //name of duty
	private Time startTime; //Start time of duty
	private Time endTime; //End time of duty
	private String dayOfTheWeek; //day of the week the duty is
	private int pointsToDeduct; //points to deduct from teacher if duty is assigned
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime.setHours(startTime.getHours()); //Setter in time Class
		this.startTime.setMinutes(startTime.getMinutes()); //Setter in Time Class
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime.setHours(endTime.getHours()); //Setter in Time Class
		this.endTime.setMinutes(endTime.getMinutes()); //Setter in Time class
	}
	public String getDayOfTheWeek() {
		return dayOfTheWeek;
	}
	public void setDayOfTheWeek(String dayOfTheWeek) {
		this.dayOfTheWeek = dayOfTheWeek;
	}

	public void setPointsToDeduct() {
		
	}
}

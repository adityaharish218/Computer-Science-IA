package duties;

public class Duty {
	private String name; //name of duty
	private Time startTime; //Start time of duty
	private Time endTime; //End time of duty
	private String dayOfTheWeek; //day of the week the duty is
	private int pointsToDeduct; //points to deduct from teacher if duty is assigned
	private boolean hasBeenAssigned = false; //if duty has been assigned or not
	public boolean isHasBeenAssigned() {
		return hasBeenAssigned;
	}
	public void setHasBeenAssigned(boolean hasBeenAssigned) {
		this.hasBeenAssigned = hasBeenAssigned;
	}
	//setters and getters
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
	
	public String toString() { //to string method 
		String k = "";
		k = k + this.dayOfTheWeek + " ";
		k = k + this.startTime.toString() + " ";
		k = k + this.endTime.toString() + " ";
		k = k + this.name;
		return k;
	}
	
	
	public Duty(String name, Time startTime, Time endTime, String dayOfTheWeek) { //constructor 
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.dayOfTheWeek = dayOfTheWeek;
	}
}

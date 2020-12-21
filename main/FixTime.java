package main;

import duties.Time;

public class FixTime { //class to fix the problem with importing duties 
	private String dayOfTheWeek; //day of the week
	private Time startTime; //start time
	private Time endTime; //end time
	private int upTo; //how many duties 
	
	public FixTime(String dayOfTheWeek, Time startTime, Time endTime, int upTo) {
		this.dayOfTheWeek = dayOfTheWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.upTo = upTo;
	}
	//Setters and Getters
	public String getDayOfTheWeek() {
		return dayOfTheWeek;
	}
	public void setDayOfTheWeek(String dayOfTheWeek) {
		this.dayOfTheWeek = dayOfTheWeek;
	}
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	public int getUpTo() {
		return upTo;
	}
	public void setUpTo(int upTo) {
		this.upTo = upTo;
	}
	
}

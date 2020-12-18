package main;

import Teachers.Teacher;
import duties.Duty;

public class AssignedDuty {
	private Duty duty; //Duty assigned
	private Teacher teacher; //Teacher duty is assigned to
	public AssignedDuty() {}//Empty constructor
	public AssignedDuty(Duty duty, Teacher teacher) { //constructor 
		this.duty = duty;
		this.teacher = teacher;
	}
	public Duty getDuty() {
		return duty;
	}
	public void setDuty(Duty duty) {
		this.duty = duty;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	public String toStringWithCommas() { //method for returning duty to store in CSV file 
		String k = "";
		k = k + this.getDuty().getDayOfTheWeek() + ","; //add a comma to the end of each string. Used to seperate values in final CSV file
		k = k + this.getDuty().getName() + ",";
		k = k + this.getDuty().getStartTime().toString() + ",";
		k = k + this.getDuty().getEndTime().toString() + ",";
		k = k + this.getTeacher().getName() + ",";
		k = k + this.getTeacher().getId();
		return k;
	}
}

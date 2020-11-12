package main;

import Teachers.Teacher;
import duties.Duty;

public class AssignedDuty {
	private Duty duty; //Duty assigned
	private Teacher teacher; //Teacher duty is assigned to
	public AssignedDuty() {}//Empty constructor
	public AssignedDuty(Duty duty, Teacher teacher) {
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
}
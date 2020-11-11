package main;

import java.util.ArrayList;

import Teachers.Subject;
import Teachers.Teacher;
import duties.Duty;

public class top_level {
	public ArrayList<Teacher> teachers; //List of teachers
	public ArrayList<Duty> duties; //list of duties
	public ArrayList<AssignedDuty> assignedDuties; //list of duties with teachers assigned

	public static void main(String[] args) {
	
	
	

	}
	
	public void assignTeachers() {
		for (int i = 0; i < duties.size(); i++) { //going through all duties
			Duty assigned = duties.get(i); //accessing duty
			for(int j = 0; j < teachers.size(); j++) { //going through all teachers
				Teacher teacher = teachers.get(i); //accessing teacher
				String dayOfTheWeek = assigned.getDayOfTheWeek(); //accessing the duty's day of the week
				for(int k = 0; k < teacher.getSubject().length; k++ ) { // going through all of the teachers lessons
					Subject [] teacherSubjects = teacher.getSubject(); // accessing teachers subjects
					if(dayOfTheWeek.equals(teacherSubjects[k].getMeetingDay())) { //check if the subject 
						break; //break out of loop as teacher cannot be allocated this duty on this day
					}
				}
			}
		}
	}

}

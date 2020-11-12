package main;

import java.util.ArrayList;

import Teachers.Lesson;
import Teachers.Subject;
import Teachers.Teacher;
import duties.Duty;
import duties.Time;

public class top_level {
	public ArrayList<Teacher> teachers; // List of teachers
	public ArrayList<Duty> duties; // list of duties
	public ArrayList<AssignedDuty> assignedDuties; // list of duties with teachers assigned
	public String[] daysOfTheWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
	

	public static void main(String[] args) {

	}
	
	public int searchForDay(String day) {
		for(int i = 0; i < 5; i ++) { //Going through all days of the week
			if(day.equalsIgnoreCase(daysOfTheWeek[i])) { //if the day of the week is found
				return i; //return the index of that
			}
		}
		return -1; // default return - 1
	}

	public void assignTeachers() {
		for (int i = 0; i < duties.size(); i++) { // going through all duties
			Duty assigned = duties.get(i); // accessing duty
			Time dutyStartTime = assigned.getStartTime();// get the start time of the duty
			for (int j = 0; j < teachers.size(); j++) { // going through all teachers
				boolean canAssign = true; //Assume teacher can be assigned duty
				Teacher teacher = teachers.get(j); // accessing teacher
				
				if (dutyStartTime.getHours() == 8) { // Check if the duty begins at before school, Begins at 8:00
					for (int k = 0; k < teacher.getSubject().length; k++) { // going through all of the teachers 
						Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
						int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
						if(teacherLessons[index].one == true) { //Check if teacher has lesson on period 1
							canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
							break;
						}
						
						if(teacher.getDutiesAssigned() == teacher.getDutiesToBeAssigned()) { //check if the teacher can have more duties
							canAssign = false; //Teacher cannot be assigned duty if the duties assinged are greater are greater
							break;
						}
					}
				}
				if (dutyStartTime.getHours() == 11) { // Check if the duty begins at break, break begins at 11 hence hours = 11
					for (int k = 0; k < teacher.getSubject().length; k++) { // going through all of the teachers 
						Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
						int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
						if(teacherLessons[index].two == true || teacherLessons[index].three == true) { //Check if teacher has lesson on period 1
							canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
							break;
						}
						
						if(teacher.getDutiesAssigned() == teacher.getDutiesToBeAssigned()) { //check if the teacher can have more duties
							canAssign = false; //Teacher cannot be assigned duty if the duties assinged are greater are greater
							break;
						}
					}
				}
				
				
				if (dutyStartTime.getHours() == 13) { // Check if the duty begins at lunch time, Lunch begins at 1:25 pm so 13 hours
					String dayOfTheWeek = assigned.getDayOfTheWeek(); // accessing the duty's day of the week
					for (int k = 0; k < teacher.getSubject().length; k++) { // going through all of the teachers lessons
						Subject[] teacherSubjects = teacher.getSubject(); // accessing teachers subjects
						if (dayOfTheWeek.equals(teacherSubjects[k].getMeetingDay())) { // check if the subject
							canAssign = false; // teacher has a subject so cannot assign them a lunch duty
							break; // break out of loop as teacher cannot be allocated this duty on this day
						}
						Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
						int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
						if(teacherLessons[index].four == true || teacherLessons[index].five == true) { //Check if teacher has lesson on  period 4 or 5
							canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
							break;
						}
						
						if(teacher.getDutiesAssigned() == teacher.getDutiesToBeAssigned()) { //check if the teacher can have more duties
							canAssign = false; //Teacher cannot be assigned duty if they are greater
							break;
						}
						
							
						}
					}
				if (dutyStartTime.getHours() >= 14) { // Check if the duty begins after school. The after school duty is varied between 15:30 and 17:00 hence check if the hour is greater than 14
					for (int k = 0; k < teacher.getSubject().length; k++) { // going through all of the teachers 
						Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
						int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
						if(teacherLessons[index].six == true) { //Check if teacher has lesson on period 6
							canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
							break;
						}
						
						if(teacher.getDutiesAssigned() == teacher.getDutiesToBeAssigned()) { //check if the teacher can have more duties
							canAssign = false; //Teacher cannot be assigned duty if the duties assinged are greater are greater
							break;
						}
					}
				}
				if(canAssign) { //check if the teacher can be assigned
					AssignedDuty a = new AssignedDuty(assigned,teacher); //create a new assigned duty with the teacher
					assignedDuties.add(a); //add this duty along with assinged teacher
					teacher.setDutiesAssigned(teacher.getDutiesAssigned() + 1); //increase this teacher's number of duties assigned by 1
					teachers.set(i,teacher); // set this teacher into the teachers arraylist
					
				}
				}
			}
		}

	}

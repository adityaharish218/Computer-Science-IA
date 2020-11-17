package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Teachers.Lesson;
import Teachers.Subject;
import Teachers.Teacher;
import duties.Duty;
import duties.Time;

public class top_level {
	public ArrayList<Teacher> teachers; // List of teachers
	public ArrayList<Duty> duties; // list of duties
	public ArrayList<AssignedDuty> assignedDuties; // list of duties with teachers assigned
	public ArrayList<Integer> adminIds; //list of adminIds
	public String[] daysOfTheWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
	public Duty prevduty = duties.get(0); //find the first duty
	public String line = "";//create an empty string (Will be used when importing teachers and duties)
	public int noOfAdmins = 0; // the number of admins 
	public int dutiesAssignedToAdmins = 0; // the number of duties that have been assigned to admins
	

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
	
	public void newDay() { //change all teachers to not have duty on day 
		for (int i = 0; i < teachers.size(); i++) { // go through all teachers 
			Teacher teacher = teachers.get(i); // make a copy
			teacher.setAssignedToday(false); //set the copy to false
			teachers.set(i, teacher); //put the copy in place of the real one
		}
	}
	
	public boolean setLesson(String s) { // method to set lessons 
		if(s.equals(null)) { //check if the string equals null
			return false; //if yes return false
		}
		return true; //if no, return true and assume teacher has lesson
	}
	
	public void setTeachersDutiesToBeAssigned() { //function to set the duties assigned of all teachers
		for(int i = 0; i < teachers.size(); i++) { //goes through each teacher
			Teacher teacher = teachers.get(i); //gets the teacher
			teacher.setDutiesToBeAssigned(); //calls the setDutiesToBeAssigned function which sets the number of duties each teacher can have based on conditions
			teachers.set(i, teacher); // sets the teacher back into the list
		}
	}

	public void assignTeachers() {
		for (int i = 0; i < duties.size(); i++) { // going through all duties
			Duty assigned = duties.get(i); // accessing duty
			if(!(prevduty.getDayOfTheWeek().equalsIgnoreCase(assigned.getDayOfTheWeek()))){ //check if this new duty's day of the week is the same as the previous one
				newDay(); //if it is not, that means it is a new day and so set it such that all teachers can be assigned
			}
			Time dutyStartTime = assigned.getStartTime();// get the start time of the duty
			for (int j = 0; j < teachers.size(); j++) { // going through all teachers
				boolean canAssign = true; //Assume teacher can be assigned duty
				Teacher teacher = teachers.get(j); // accessing teacher
				
				if (dutyStartTime.getHours() == 8) { // Check if the duty begins at before school, Begins at 8:00
						if(!(teacher.isAdmin()) && dutiesAssignedToAdmins < noOfAdmins) { //check if teacher is admin or not, my client usually gives admin the early morning duties. Also check if there are any admins still left to be assigned duties. 
							canAssign = false; // cannot assign duty because it needs to be admin
						}
						if(teacher.isHomebase() == true) { //Check if teacher has a homebase or not
							canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
						}
						
						if(teacher.getDutiesAssigned() == teacher.getDutiesToBeAssigned()) { //check if the teacher can have more duties
							canAssign = false; //Teacher cannot be assigned duty if the duties assigned are greater are greater
							
						}
					
				}
				if (dutyStartTime.getHours() == 11) { // Check if the duty begins at break, break begins at 11 hence hours = 11
						Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
						int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
						if(teacherLessons[index].two == true || teacherLessons[index].three == true) { //Check if teacher has lesson on period 1
							canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
							
						}
						
						if(teacher.getDutiesAssigned() == teacher.getDutiesToBeAssigned()) { //check if the teacher can have more duties
							canAssign = false; //Teacher cannot be assigned duty if the duties assigned are greater are greater
						
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
					}
						Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
						int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
						if(teacherLessons[index].four == true || teacherLessons[index].five == true) { //Check if teacher has lesson on  period 4 or 5
							canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
							
						}
						
						if(teacher.getDutiesAssigned() == teacher.getDutiesToBeAssigned()) { //check if the teacher can have more duties
							canAssign = false; //Teacher cannot be assigned duty if they are greater
							
						}
						
							
					}
				if (dutyStartTime.getHours() >= 14) { // Check if the duty begins after school. The after school duty is varied between 15:30 and 17:00 hence check if the hour is greater than 14
					
						Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
						int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
						if(teacherLessons[index].six == true) { //Check if teacher has lesson on period 6
							canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
							
						}
						
						if(teacher.getDutiesAssigned() == teacher.getDutiesToBeAssigned()) { //check if the teacher can have more duties
							canAssign = false; //Teacher cannot be assigned duty if the duties assigned are greater are greater
							
						}
					
				}
				if(canAssign && !(teacher.getAssignedToday())) { //check if the teacher can be assigned
					AssignedDuty a = new AssignedDuty(assigned,teacher); //create a new assigned duty with the teacher
					assignedDuties.add(a); //add this duty along with assigned teacher
					teacher.setDutiesAssigned(teacher.getDutiesAssigned() + 1); //increase this teacher's number of duties assigned by 1
					teacher.setAssignedToday(true); // the teacher has been assigned a duty today so cannot be assigned again
					teachers.set(i,teacher); // set this teacher into the teachers arraylist
					if(teacher.isAdmin()) { // check if the teacher being assigned is admin
						dutiesAssignedToAdmins = dutiesAssignedToAdmins + 1; //if they are, increase it by one
					}
					break; //break out of loop as teacher has been assigned 
					
				}
				}
			prevduty = assigned; //make this duty the previous duty
			}
		}
	public boolean importTeachers() { //returns a boolean, if true, then the teachers have been imported properly, if false means there is error
		System.out.println("Enter path of file (Option + Right Click then select 'Copy as Pathname' "); // Ask user to input the filepath
		Scanner in = new Scanner(System.in); //create a new scanner so that it can read path name
		String path = in.next(); //create a new string called path which stores the filepath
		try {
			BufferedReader br = new BufferedReader(new FileReader(path)); //create a new buffered reader which reads the path
			int i = 0; //counter variable
			while ((line = br.readLine()) != null) { //while line != null, keep on running 
				String [] values = line.split(",");
				Teacher t = new Teacher(); //create a new teacher
				t.setId(Integer.parseInt(values[0])); //first element is ID, set that to the teacher
				t.setName(values[1]);// second element is Name so set the teachers name to be that 
				t.setLessonsPerWeek(Integer.parseInt(values[3])); //third element is the number of lessons the teacher has
				boolean homebase = setLesson(values[4]);
				t.setHomebase(homebase);
				for(int j = 0; j < 5; j++) { //going through all the teacher days
					
				}
				br.close();
				teachers.add(t);
				i++;
			}
			
		} catch (FileNotFoundException e) { //catch the file not found exception
			// TODO Auto-generated catch block
			e.printStackTrace();
			in.close();
			return false;
		} catch (IOException e) { //catch the IO exception caused by br.readLine()
			// TODO Auto-generated catch block
			e.printStackTrace();
			in.close();
			return false;
		}
		in.close();
		
		return true;
	}
	public boolean importAdmins(String path) { //importing all the admins 
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path)); //create a new BufferedReader
			while((line = br.readLine())!= null) { //While the next line is not null;
			String[] values = line.split(","); //create an array which has values with split
			int id = Integer.parseInt(values[0]);//first element is the ID
			adminIds.add(id);//add the ID to the list
			
			}
			br.close(); //close the bufferedreader
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int k = 0; k < adminIds.size(); k++) { //going through all the adminIds
			int id = adminIds.get(k); //get the adminId
			boolean adminfound = false; //boolean to find the admin, assume not found
		for(int i = 0; i < teachers.size(); i++) { //going through all the teachers
			Teacher t = teachers.get(i); //get the teacher
			if(t.getId() == id ) { //check if teachers id matches the adminId
				t.setAdmin(true); //set the teacher to be an admin
				teachers.set(i, t); //set that teacher into the arraylist
				noOfAdmins = noOfAdmins + 1; //increase no of admins by 1
				adminfound = true; //admin is found, change boolean to true
				break; //break out of loop 
			}
		}
		if(!adminfound) { //if admin is not found 
			System.out.println("Error, admin id" + id + "not found"); //output error message and where it occurred
			return false; // exit the function with false
		}
		}
		setTeachersDutiesToBeAssigned(); //change the teacher duties 
		return true; //import successful
		
	}
	}


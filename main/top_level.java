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
	public static ArrayList<Teacher> teachers = new ArrayList<Teacher>(); // List of teachers
	public static ArrayList<Duty> duties = new ArrayList<Duty>(); // list of duties
	public static ArrayList<AssignedDuty> assignedDuties = new ArrayList<AssignedDuty>(); // list of duties with teachers assigned
	public static ArrayList<Integer> adminIds = new ArrayList<Integer>(); //list of adminIds
	public static String[] daysOfTheWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};	
	public static Duty prevduty; //create a duty
	public static int noOfAdmins = 0; // the number of admins 
	public static int dutiesAssignedToAdmins = 0; // the number of duties that have been assigned to admins
	public static Lesson allFalse = new Lesson(false,false,false,false,false,false,false); //Creating an lesson with all lessons being false
	public static Scanner in = new Scanner(System.in); //public scanner that is used by all methods
	public static String line = "";


	public static void main(String[] args) {
		boolean b = importTeachers();
		System.out.println(teachers.get(0).toStringWithLessons());
		System.out.println(teachers.get(1).toStringWithLessons());
		
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

	public static boolean setLesson(String s) { // method to set lessons 
		
		if(s.length() > 0) {
			return true; //if the length is greater than 0, return true
		}
		return false;  //else assume length is 0 and return false
	}

	public void setTeachersDutiesToBeAssigned() { //function to set the duties assigned of all teachers
		for(int i = 0; i < teachers.size(); i++) { //goes through each teacher
			Teacher teacher = teachers.get(i); //gets the teacher
			teacher.setDutiesToBeAssigned(); //calls the setDutiesToBeAssigned function which sets the number of duties each teacher can have based on conditions
			teachers.set(i, teacher); // sets the teacher back into the list
		}
	}

	public void assignTeachers() {
		prevduty = duties.get(0); //get the first duty
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
					if(teacherLessons[index].four == true || teacherLessons[index].five == true || teacherLessons[index].lunch == true) { //Check if teacher has lesson on period 4, lunch or 5
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

	public static boolean importTeachers() { //returns a boolean, if true, then the teachers have been imported properly, if false means there is error
		System.out.println("Enter path of file (Option + Right Click then select 'Copy as Pathname' "); // Ask user to input the filepath
	 
		String path = in.next(); //create a new string called path which stores the filepath
		in.close(); //close the scanner
		try {
			BufferedReader br = new BufferedReader(new FileReader(path)); //create a new buffered reader which reads the path
			int i = 0; //counter variable
			br.readLine(); //going through first line because it doesn't matter (days of the week)
			while (br.ready()) {//check if the the bufferedreader is ready
				line = br.readLine(); // read the next line 
				String [] values = new String[34]; //initialize a values array with 33 elements (Max number of elements that a row can have (discounting period 6))
				String [] read = line.split(","); //store the values of line into a new array called read;
				for (int b = 0; b < values.length; b++) { //loop through the whole of the values array 
					if (b < read.length) { //check if the value of b is less than the length of the read array
						values[b] = read[b]; // if so add the value of read[b] into the values[b]
					} else {
						values[b] = ""; //otherwise set the values of value[b] to be empty
					}
				}
				//Going through the values array and seeing what it stores
				for(int z = 0; z < values.length; z++) {
					System.out.println(values[z] + setLesson(values[z]));
				}
				
				int Id = Integer.parseInt(values[0]); //first element is ID
				String name = values[1];// second element is Name  
				int lessonsPerWeek = Integer.parseInt(values[2]); //third element is the number of lessons the teacher has
				boolean homebase = setLesson(values[3]); //fourth element is the teacher's homebase
				//Setting all the temp lessons to false to avoid NullPointer exception
				Lesson tempLessons [] = {allFalse, allFalse, allFalse, allFalse, allFalse};

				for(int j = 0; j < 5; j++) { //going through all the days in the week 
					boolean one = setLesson(values[(j*6) + 4]); //get lesson one 
					boolean two = setLesson(values[(j*6) + 5]); //get lesson two
					boolean three = setLesson(values[(j*6) + 6]); //get lesson three
					boolean four = setLesson(values[(j*6) + 7]); //get lesson four
					boolean lunch = setLesson(values[(j*6) + 8]); //get lunch lesson
					boolean five = setLesson(values[(j*6) + 9]); // get lesson 5
					Lesson temp = new Lesson(one, two, three, four, lunch, five, false); //create a new lesson (Lesson 6 is false for now)
					tempLessons[j] = temp; //set the day to be this
					/* the above is a calculation that will help me get each lesson on each day of the week. There are 6 lessons (Omitting lesson 6 and including lunch)
					 * that occur in each day. All of them will be stored in values. The calculation will help me get a specific lesson on one day. For example, lesson
					 * 3 on thursday will be the teacher's 21st lesson. In the values array that will be stored in index no 24. Using this calculation we will also get
					 * index no 24 (thursday means j = 3 so 3*6 + 6 = 24)  
					 */

				} 

					// set the teacher's lessons
				br.readLine(); //read the next line because the line does not have important information  either (Teacher rooms, not important for my code)
				Teacher temp = new Teacher(name, Id, lessonsPerWeek, homebase, tempLessons); //create a new teacher with the attributes
				teachers.add(temp);//add the teacher into the arraylist
				
				i++;
			}
			br.close(); //Close the bufferedreader

		} catch (FileNotFoundException e) { //catch the file not found exception
			// TODO Auto-generated catch block
			e.printStackTrace();
			in.close(); //close the scanner.
			System.out.println("Error, File not found"); //output error message. 
			return false; //return false as there is an error that has occurred
		} catch (IOException e) { //catch the IO exception caused by br.readLine()
			// TODO Auto-generated catch block
			e.printStackTrace(); 
			in.close(); //close the scanner
			return false; //return false as there is an error that has occurred 
		}
		in.close(); //close the scanner

		return true; //Import successful
	}
	public boolean importAdmins() { //importing all the admins 
		System.out.println("Enter List of Admins");
		String path = in.next();
		in.close(); //close the scanner
		try {
			//First add all admin IDs to an arraylist
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
		//Check adminID against all teachers and then set admin if found
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
	public boolean importSubjects() { //import the teacher subjects 
	
		System.out.println("Enter file name for teaching departments"); // ask user to input path name
		String path = in.next(); //store path name 
		in.close(); //close the scanner

		try {
			BufferedReader br = new BufferedReader(new FileReader (path)); //create a new bufferedreader
			br.readLine(); //read the first line as it is not important
			 
			while(br.ready()) { //while the bufferedreader is ready
				line = br.readLine(); //read the next line 
				String [] values = line.split(","); //split it by commas 
				int i = 0;
				Teacher t = teachers.get(i); //get the first teacher 
				i++; //counter variable
				while(t.getId() != Integer.parseInt(values[0])) {// check if the id at the first index of values is not equal to the teacher
					t = teachers.get(i); //move on to the next teacher 
					i++; //increase i 
				}
				String [] k = values[3].split(","); //create an array for the subjects as there might be more than one (multiple subjects in the CSV file are shown as "Maths, Physics"
				k[0] = k[0].replace('"', ' '); //replace the " in the beginning with a space 
				k[k.length - 1] = k[k.length - 1].replace('"', ' '); //replace the " in the end with a space //Try '\"' if it doesn't work
				Subject [] temp = new Subject[k.length];
				for(int index = 0; index < k.length; index++) { //go through the subject array
					temp[index].setName(k[index]);  //set the name of the subject to be the array
				}
				t.setSubject(temp);
				teachers.set(i, t);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return true;// import is successful return true 
	}
	
	public boolean importSubjectMeetindDays() {
		ArrayList<Subject> SubjectsWithDays = new ArrayList<Subject>(); //create a new array with 
		System.out.println("Enter list of subjects and meeting days"); 
		String path = in.next(); //read the filepath
		in.close(); //close the scanner 
		try {
			BufferedReader br = new BufferedReader(new FileReader(path)); //create a new buffered reader 
			br.readLine(); //read first line as it is not important
			
			while(br.ready()) { //loop while it is ready 
				line = br.readLine(); //read the line 
				String [] values = line.split(","); //split and store into array 
				Subject s = new Subject(values[0],values[1]); //first value is the name and second is meetingday
				SubjectsWithDays.add(s); //store into the array list
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i < teachers.size(); i++) { //loop through teachers 
			Teacher t = teachers.get(i); //get the teacher at index
			for(int j = 0; j < t.getSubject().length; j++ ) { //loop until the number of the subjects of the teacher 
				for(int k = 0; k< SubjectsWithDays.size(); k++) { //loop until the number of subjects
					if(t.getSubject()[j].getName() == SubjectsWithDays.get(k).getName()) { //check if teacher subject name is equal to SubjectWithDays subject name
						t.getSubject()[j] = SubjectsWithDays.get(k); //repalce the subject to include the day
						break; //break out of the loop 
					}
				}
			}
			teachers.set(i, t); //replace the teacher with t (Teacher with subjects and meeting days) 
		}
		return true; //import successful 
	}
}



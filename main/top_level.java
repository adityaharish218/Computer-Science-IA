package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import Teachers.AssignedTimes;
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
	public static ArrayList<Subject> SubjectsWithDays = new ArrayList<Subject>(); 
	public static final String[] daysOfTheWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};	
	public static final boolean[] falseOnAllDays = {false,false,false,false,false}; //array to say that on all days it is false
	public static int noOfAdmins = 0; // the number of admins 
	public static int dutiesAssignedToAdmins = 0; // the number of duties that have been assigned to admins
	public static Lesson allFalse = new Lesson(false,false,false,false,false,false,false); //Creating an lesson with all lessons being false
	public static Scanner in = new Scanner(System.in); //public scanner that is used by all methods
	public static String line = "";
	public static final char[] numbers = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '-', ':', ' ', '0'}; //array for checking of time
	public static final char[] IdNumbers = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}; //array for validation of ID
	public static final char[] NotAllowedForDutyName = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'E', '+'}; //array for validation of duty name
	public static final char[] allowedChars = {'(', ')', ' ', '.', '/', ',', '-'};

	public static void main(String[] args) {
		boolean a = importDutiesTakeTwo(); //function imports duties and returns true if all duties have been imported properly
		while(!a) {  //it returns false meaning that there is some problem importing duties
			duties.clear(); //clear the arrayList
			a = importDutiesTakeTwo(); //method will output error message, user will fix and then re enter path
		}
		
		boolean b = importTeachers(); //function imports all of the teachers
		while(!b) { //it returns false if there is some problem in importing the teachers
			teachers.clear(); //clear the arrayList
			b = importTeachers(); //method will output error message, user will fix and then re enter path 
		}
		
		boolean c = importSubjectMeetingDays();  //function imports all of the subjects with meeting days
		while(!c) { //method will return false if there is an error
			SubjectsWithDays.clear(); //clear the arrayList
			c = importSubjectMeetingDays(); // method will output error message, user will fix and then re-enter path
		}
		boolean d = importSubjects(); //method to import the subjects each teacher has
		while(!d) { //method will return false if there is an error
			d = importSubjects(); //method will output error message, user will fix and then re-enter path. Just call method again. No need to clear because it is based on ID.	
		}
		boolean e = importPeriodSix();//method to import all the teachers who have period 6
		while(!e) { //method will return false if there is a problem
			e = importPeriodSix(); //method will output error message, user will fix and then re-enter path. Just call method again. No need to clear because it is based on ID.
		}
		boolean f = importAdmins(); //method to import all of the admins and SAL's who only get one duty
		while(!f) {
			adminIds.clear(); //clear the adminIds list
			f = importAdmins(); // method will output error message, user will fix and then re-enter path.
		}
		//output messages to see whether user wants to use custom or default
		System.out.println("Number of duties : " + duties.size());
		System.out.println("Number of teachers : " + teachers.size());
		System.out.println("Number of admins : " + noOfAdmins);
		
		System.out.println("Do you want to use custom setup? (Yes/No)"); //output whether user wants to use custom arrangement or not
		String decision = in.next(); //get the user's input
//		boolean g = custom(decision);
//		while(!g) { //method will return false if there is an issue with input
//			decision = in.next();
//			g = custom(decision);
//		}
		custom("no");
		AdminsFirst(); //first sort the admins
		assignPossibleTeachers(); //assign possible teachers
		sortByPossibleTeachers(); //sort by possible teachers
		assignTheTeacher(); //assign the teacher
		assignUnAssignedDuties(); //assign all of the unassigned duties by shifting teachers
		System.out.println(assignedDuties.size());
		boolean h = ensureAllDutiesAssigned(); //make sure all duties are assigned
		if(h) { //if all duties are assigned proceed to output
			sortForFinalOutput();
			generateCSV();
		}
		while(!h) { //if they are not assigned, ask user if they wish to try with custom
			System.out.println("Error, not all duties assigned."); //error message
			System.out.println("Number of duties assigned : " + assignedDuties.size());
			System.out.println("Number of duties : " + duties.size());
			System.out.println("Do you wish to try again with a custom setup or output all duties assigned?");
			String yes = "yes";
			String no = "no";
			decision = in.next(); //get the user input
			decision = decision.trim(); //remove any unnecessary spaces
			while(decision.equals(yes) == false || decision.equals(no)) { //while they are not yes or no
				System.out.println("Error, please enter a valid option");
			}
			if(decision.equals(yes)){ //if it equals yes
				custom(yes); //go ahead and ask user for custom setup
				
			}
			else { //go ahead and output all duties that have been assigned
				sortForFinalOutput();
				generateCSV();
			}
		}
		
	}
	
	public static void clearForCustom() { //method if user wishes to redo the duties with a custom setup
		assignedDuties.clear(); //clear assignedDuties list
		for(int i = 0; i < duties.size(); i++) { //go through duties and remove all possible teachers
			duties.get(i).getPossibleTeachers().clear();
		}
		for(int i = 0; i < teachers.size(); i++) {
			
		}
	}
	
	public static void printTeacherByID(int id) { //method to print teacher by id
		int index = findTeacherByID(id);
		System.out.println(teachers.get(index).toStringWithLessons());
	}
	public static int searchForDay(String day) {
		day = day.trim(); //remove any unnecessary spaces;
		for(int i = 0; i < 5; i ++) { //Going through all days of the week
			if(day.equalsIgnoreCase(daysOfTheWeek[i])) { //if the day of the week is found
				return i; //return the index of that
			}
		}
		return -1; // default return - 1
	}
	public static boolean searchFor(char [] charArray, char k) { //method for searching for character in array
		for(int i = 0; i < charArray.length; i++) { //loop through array
			if(charArray[i] == k) { //check if the character matches 
				return true; //return true 
			}
		}
		return false; // return false as character has not been found 
	}
	
	public static void AdminsFirst() {
		Collections.sort(teachers, new Comparator<Teacher>(){ // call sort method from collections class (Learnt from https://www.youtube.com/watch?v=wzWFQTLn8hI) 	
			
			public int compare(Teacher t1, Teacher t2) { //create a compare method for two teachers 
				return Integer.valueOf(t1.CompareForSort()).compareTo(t2.CompareForSort()); //compare t1's how many duties can be assigned to t2's and return the value
			}
		});
	}
	public static int toASCII(char ch) { //method to convert character to ASCII
		int k = ch; //converting character to ASCII value
		return k; // returning the ASCII value
	}
	public static boolean isValidID(String id) { //validation for ID
		//Presence Check 
		if (id == null) { //check if id is null 
			return false; //Invalid ID
			}
		id = id.trim(); //remove unnecessary spaces
		//Length check 
		if(id.length() != 5) { //check if length of ID is not 6
			return false; //Invalid ID
		}
		//Format check (All characters are numbers)
		for(int i = 0; i < id.length(); i++) {  //loop through all characters of string
			char k = id.charAt(i); //get character at index
			if(searchFor(IdNumbers, k) == false) { //try and find character in array
				return false; //return false as character has not been found in the array thus Invalid ID
			}
		}
		//checking ID is unique
		int identity = Integer.parseInt(id); //getting the ID as an integer. Should be given that all characters are numbers
		for(int z = 0; z < teachers.size(); z++) { //looping through teachers arrayList so far
			if(identity == teachers.get(z).getId()) { //check if ID matches that of list
				System.out.println("ID already taken by " + teachers.get(z).getName()); //print out error message indicating that it's not a unique ID
				return false; //Invalid ID as it is not unique
			}
		}
		return true; //Valid ID
	}
	public static boolean isValidName(String name) { //validation for Name
		//Presence Check 
		if(name == null) { //check if it null
			return false; //Invalid Name
		}
		name = name.trim(); //remove unnecessary spaces
		//Length Check
		if(name.length() <= 1) { //check if length is less than or equal to 1
			 return false; //Invalid Name
		 }
		//Format check (All alphabets and some special characters only)
		for(int i = 0; i < name.length(); i++) { //loop through length of name
			char k = name.charAt(i); //get the character at the index
			int ASCII = toASCII(k); //convert to ASCII value
				if(ASCII < 65 || ASCII > 90) {//check if it is not a capital letter
					if(ASCII < 96 || ASCII > 122) { //check if it is not a letter
						if(searchFor(allowedChars, k) == false) { //check if it is not an allowed character like '(' or ')'
							return false; //Invalid ID
						}
					}
				} 
		}
		return true; //Valid Name
	}
	public static boolean isValidLessonsPerWeek(String lessonsPerWeek) { //validation for LessonsPerWeek
		//Presence check
		if(lessonsPerWeek == null || lessonsPerWeek.length() == 0) { // check if it is null
			return false; //invalid LessonsPerWeek
		}
		//Length check
		if(lessonsPerWeek.length() == 0) { //check if it's length is 0
			return false; //invalid LessonsPerWeek+
		}
		//Format Check (All characters are numbers only)
		lessonsPerWeek = lessonsPerWeek.trim(); //remove any unnecessary spaces
		for(int i = 0; i < lessonsPerWeek.length(); i++) { //loop through length of array
			char k = lessonsPerWeek.charAt(i); //get the character at the index
			if(searchFor(IdNumbers,k) == false) { //check if the character is not a number
				return false; // invalid LessonsPerWeek
			}
		}
		//Range check
		int week = Integer.parseInt(lessonsPerWeek); //get the integer
		if (week > 30 || week < 0) { //check if week is greater than 30 or less than 0
			return false; //invalid lessonsPerWeek
		}
		return true; //Valid Lessons per week
	}
	
	public static boolean isValidDutyName(String duty) { //validation for duty name
		 //Presence Check 
		if(duty == null ) { //check if it is null
			return false; //Invalid duty name
		}
		duty = duty.trim(); //remove any unnecessary spaces
		
		if(duty.length() <= 1) { //check if it is less than or equal to 1
			return false; //Invalid duty name
		}
		//Format check (Alphabets + numbers)
		boolean allNumbers = true; //assume the duty is all numbers
		for(int i = 0; i < duty.length(); i++) {
			char k = duty.charAt(i); //get the character at the index
			if(searchFor(NotAllowedForDutyName,k) == false) { //try to find the character in the numbers array and if it can't it means that it is not a number
				allNumbers = false; //duty name is not all numbers
				break; //break out of loop
			}
		} 
		if(allNumbers) { //check if it's still all numbers
			return false; //Invalid duty name
		}
		return true; //Valid duty name
	}
	
	public static boolean validDayOfTheWeek(String day) { //checking for valid day of the week
		day = day.trim(); //remove any unneccessary spaces
		return searchForDay(day) != -1; //use the searchForDay method. This returns an index. If it returns -1, it means day is not a day of the week and thus it will return false
	}
	
	public static boolean validTimes(Time startTime, Time endTime) {
		return startTime.compareTo(endTime) < 0; //use the CompareTo method in the time class. This returns a negative value if the another time is greater than this time. 
	}
	
	public static boolean validSubjectName(String sName) {
		sName = sName.trim(); //remove any unnecessary spaces
		return isValidName(sName); //Subject name has the same validation as name. 
	}
	
	public static boolean searchNumbers(char k) { //method to find if a certain character is a integer or - (Used for time) 
		 
		for(int i = 0; i < numbers.length; i++) { //loop through the whole numbers array
			if(numbers[i] == k) { //check if k is in the numbers array
				return true; //return true 
			}
		}
		return false; //return false
	}
	public static boolean isInteger(String s) { //method to find out if it is time or not 
		
		for(int i = 0; i < s.length(); i++) { //loop through whole string
			char k = s.charAt(i); //get the char at that particular index 
			if (searchNumbers(k) == false) { //try to find the character in the Numbers array
				return false; // if false that means the particular character is not
			}
		}
		
		return true; //assume it is true
	}
	public static boolean setLesson(String s) { // method to set lessons 
		
		if(s.length() > 0) {
			return true; //if the length is greater than 0, return true
		}
		return false;  //else assume length is 0 and return false
	}

	public static void setTeachersDutiesToBeAssigned() { //function to set the duties assigned of all teachers
		for(int i = 0; i < teachers.size(); i++) { //goes through each teacher
			teachers.get(i).setDutiesToBeAssigned(); //sets the number of assigned duties
		}
	}

	
	
	public static void assignPossibleTeachers() {
		for (int i = 0; i < duties.size(); i++) { // going through all duties
			Duty assigned = duties.get(i); // accessing duty
			Time dutyStartTime = assigned.getStartTime();// get the start time of the duty
			ArrayList<Teacher> possibleTeachers = new ArrayList<Teacher>(); //create a new array used to find probable teachers for the duty
			for (int j = 0; j < teachers.size(); j++) { // going through all teachers
				boolean canAssign = true; //Assume teacher can be assigned duty
				Teacher teacher = teachers.get(j); // accessing teacher
				if(teacher.getDutiesToBeAssigned() == 0) { //check if the teacher cannot do any duties
					canAssign = false; //cannot assign duty because teacher cannot have any duties
				}
				if (dutyStartTime.getHours() == 8) { // Check if the duty begins  before school. So begins before 9:00
					Lesson[] teacherLessons = teacher.getLessons();
					int index = searchForDay(assigned.getDayOfTheWeek());
					if(teacher.isHomebase() == true || teacherLessons[index].one == true) { //Check if teacher has a homebase or if they have lesson 1
						canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
					}


				}
				if (dutyStartTime.getHours() == 11) { // Check if the duty begins at break, break begins at 11 hence hours = 11
					Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
					int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
					if(teacherLessons[index].two == true && teacherLessons[index].three == true) { //Check if teacher has lesson on period 2 and three
						canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
					}
				}


				if (dutyStartTime.getHours() == 13) { // Check if the duty begins at lunch time, Lunch begins at 1:25 pm so 13 hours
					String dayOfTheWeek = assigned.getDayOfTheWeek(); // accessing the duty's day of the week
					for (int k = 0; k < teacher.getSubject().length; k++) { // going through all of the teachers lessons
						Subject[] teacherSubjects = teacher.getSubject(); // accessing teachers subjects
						if(teacherSubjects[k] == null) { //check if it is equal to null
							canAssign = false; //teacher cannot be assigned as they are likely admin which means they cannot be assigned in lunch
							break; //break out of loop
						}
						
						if (dayOfTheWeek.equals(teacherSubjects[k].getMeetingDay())) { // check if the subject's meeting day is the same as the duty's day
							canAssign = false; // teacher has a subject so cannot assign them a lunch duty
							break; // break out of loop as teacher cannot be allocated this duty on this day
						}
					}
					Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
					int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
					if(teacherLessons[index].four == true &&  teacherLessons[index].five == true) { //Check if teacher has a lesson on period 4 and 5
						canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
					}
					if(teacherLessons[index].lunch == true) { //check if the teacher has a lesson on lunch
						canAssign = false; //teacher cannot be assigned the duty as he/she is preoccupied during lunch
					}
				}
				if (dutyStartTime.getHours() >= 14) { // Check if the duty begins after school. The after school duty is varied between 15:30 and 17:00 hence check if the hour is greater than 14

					Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
					int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
					if(teacherLessons[index].six == true) { //Check if teacher has lesson on period 6
						canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty

					}
				}
				
				Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
				int index = searchForDay(assigned.getDayOfTheWeek()); //access the day of the week as index
				if(teacherLessons[index].getLesosnsToday() >= 5) { //check if the teacher has more than 4 lessons on the day
					System.out.println(assigned.getDayOfTheWeek());
					System.out.println(teacher.toStringWithLessonCount());
					System.out.println("Condition max lessons on day");
					canAssign = false; //can assign = false
				}
				if(canAssign) { //check if the teacher can be assigned
					possibleTeachers.add(teachers.get(j)); //add this teacher to the duty's possible teachers
					teachers.get(j).setHowManyDutiescanBeAssigned(teachers.get(j).getHowManyDutiescanBeAssigned() + 1); //increase the number of duties this teacher can do by 1
				}
			}
			duties.get(i).setPossibleTeachers(possibleTeachers); //set the duties possible teachers
		}
	}
	
	public static void sortByPossibleTeachers() { //method to sort duties based on the number of possible teachers who can do the duty
		Collections.sort(duties, new Comparator<Duty>() { //call sort method from collections class (Learnt from https://www.youtube.com/watch?v=wzWFQTLn8hI)
			public int compare(Duty d1, Duty d2) { //create a compare method for duties
				Time t1 = d1.getStartTime();
				Time t2 = d2.getStartTime();
				int timeCompare = t1.compareTo(t2);
				if(timeCompare != 0 ) {
					return timeCompare;
				}
				return Integer.valueOf(d1.getPossibleTeachers().size()).compareTo(d2.getPossibleTeachers().size()); //compare the size of the possibleTeachers arraylist for both duties and return the value
			}
		});
	}
	
	public static void sortByPossibleDuties() { //method to sort teachers by number of duties they can do
		Collections.sort(teachers, new Comparator<Teacher>(){ // call sort method from collections class (Learnt from https://www.youtube.com/watch?v=wzWFQTLn8hI) 	
		
			public int compare(Teacher t1, Teacher t2) { //create a compare method for two teachers 
				return Integer.valueOf(t1.getHowManyDutiescanBeAssigned()).compareTo(t2.getHowManyDutiescanBeAssigned()); //compare t1's how many duties can be assigned to t2's and return the value
			}
		});
	}
	
	public static boolean assignTheTeacher() { //assign the teacher
		for(int i = 0; i < duties.size(); i++) { //loop through the whole teachers array
			while(i < duties.size() && duties.get(i).isHasBeenAssigned()) { //check if the duty has been assigned and that i < duties.size() (Avoid index out of bounds exception) 
				i = i + 1; //increase i and move on to the next duty
			}
			if(i == duties.size()) {
				return true; //all duties have been assigned
			}
			System.out.println("Trying to assign duty:  " + duties.get(i).toString());
			System.out.println("Number of teachers that can be assigned " + duties.get(i).getPossibleTeachers().size());
			for(int j = 0; j < duties.get(i).getPossibleTeachers().size(); j++) { //loop through the duty's possible teachers
				
				System.out.println("Trying to assign teacher " + duties.get(i).getPossibleTeachers().get(j));
				
				boolean canAssign = true; //assume teacher can be assigned the duty
				int index = findTeacherByID(duties.get(i).getPossibleTeachers().get(j).getId()); //find the teacher in the teachers array List
				System.out.println("In main arrayList" + teachers.get(index).toString());
				if(teachers.get(index).getDutiesToBeAssigned() == teachers.get(index).getDutiesAssigned()) { //check if the number of duties already assigned to teacher is equal to the number of duties to be assigned to the teacher
					canAssign = false; //teacher cannot be assigned the duty;
					System.out.println("Condition max assigned");
				}
				for(int z = 0; z < teachers.get(index).getAssignedDuties().size(); z++) { //loop through the teacher's duties that have been assigned
					if(teachers.get(index).getAssignedDuties().get(z).getDayOfTheWeek().equals(duties.get(i).getDayOfTheWeek()) && teachers.get(index).getAssignedDuties().get(z).getStartTime().equals(duties.get(i).getStartTime())) { //check if the duty trying to be assigned's day of the week and start time is the same as one of the teacher's assigned duties
						canAssign = false; //teacher cannot be assigned as he/she already has a duty on that day of the week
					System.out.println("Condition already assigned on this day on same time");
					}
				}
				
				
				if(duties.get(i).getStartTime().getHours() == 8) { //check if the duty is in the morning
					if(teachers.get(index).isAdmin() == true && dutiesAssignedToAdmins < noOfAdmins) { //check if the teacher is an admin and that the number of duties assigned to admins is < the number of admins
						canAssign = false; //cannot assign as admins can still be assigned the duty
						System.out.println("Condition not admin");
					}
				}
				if(duties.get(i).getStartTime().getHours() >= 14) { //check if the duty begins after school
					if(teachers.get(index).isAssignedAfterSchool()) { //check if the teacher has already been assigned a duty
						canAssign = false;
						System.out.println("Condition already assigned afterschool");
					}
				}
				if(duties.get(i).isHasBeenAssigned()) { //check if the duty has been assigned 
					canAssign = false;
					System.out.println("Condition has already been assigned");
				}

				if(canAssign) { //if the duty can be assigned
					AssignedDuty a = new AssignedDuty(duties.get(i),teachers.get(index)); //create a new assigned duty with the teacher
					assignedDuties.add(a); //add this duty along with assigned teacher
					teachers.get(index).setDutiesAssigned(teachers.get(index).getDutiesAssigned() + 1); //increase the teachers number of duties by 1
					if(duties.get(i).getStartTime().getHours() >= 14) { //check if the duty is after school
						teachers.get(index).setAssignedAfterSchool(true); //the teacher has already been assigned an afterschool duty
					}
					
					if(teachers.get(index).isAdmin()) { // check if the teacher being assigned is admin
						dutiesAssignedToAdmins = dutiesAssignedToAdmins + 1; //if they are, increase it by one
					}
					duties.get(i).setHasBeenAssigned(true); //duty has been assigned. 
					teachers.get(index).getAssignedDuties().add(duties.get(i)); //add this duty to the list of duties assigned to teachers
					System.out.println("Assigned successfully");
					break; //break out of loop as teacher has been assigned 
				}
				System.out.println("Not assigned");
			}
		}
		
		
		return false; //assume all duties have not been assigned
	}
	
	public static int findTeacherByID(int id) { //method to find teacher by ID. I do not need to worry about validation as the data has already been validated
		int i = 0; //counter variable
		while(teachers.get(i).getId() != id) { //while the teacher's ID does not equal the id we need to find 
			i++; //increase I 
		}
		return i; //return i as teacher has been found
	}
	
	public static boolean checkTeacher(Teacher t, Duty d) { //method that returns if a teacher can fit a duty or not
		int index = findTeacherByID(t.getId()); //access the index of the teacher in the main array;
		System.out.println(t.toString());
		System.out.println(teachers.get(index).toString());
		if(teachers.get(index).getDutiesAssigned() == teachers.get(index).getDutiesToBeAssigned()) { //check if the teacher has already been assigned the max number of duties
			System.out.println("Condition Max assigned");
			return false; //return false as teacher cannot be assigned
		}
		if(d.getStartTime().getHours() >= 14) {//check if the duty is after school
			if(teachers.get(index).isAssignedAfterSchool()) { //check if the teacher has been assigned after school
				System.out.println("Condition already Assigned");
				return false; //return false as teacher cannot be assigned
			}
		}
		for(int i = 0; i < teachers.get(index).getAssignedDuties().size(); i++) { //go through all of the teachers assigned duties
			int c = 0; //conditions variable
			if(teachers.get(index).getAssignedDuties().get(i).getDayOfTheWeek().equals(d.getDayOfTheWeek())) { // check if the teacher has been assigned on this day of the week
				c = c + 1; //increase condition
			}
			if(teachers.get(index).getAssignedDuties().get(i).getStartTime().equals(d.getStartTime())) { //check if the teacher has been assigned on this time
				c = c + 1; //increase condition
			}
			
			if(c == 2) { //if teacher has been assigned on this day and already on this time
				return false; //teacher cannot be assigned
			}
		}
		
		return true; //teacher can be assigned this duty
	}
	
	public static int findDutyById(Duty d) {
		int i = 0;
		while(duties.get(i).getId() != d.getId()) { //while the id is not the same as in the main arrayList
			i = i + 1; //increase i
		}
		
		return i;
	}
	public static void assignUnAssignedDuties() {
		ArrayList<Duty> notAssigned = new ArrayList<Duty>(); //create a new arrayList of unassigned duties
		for(int i = 0; i < duties.size(); i++) { //loop through all duties
			if(duties.get(i).isHasBeenAssigned() == false) { //check if a duty has not been assigned
				notAssigned.add(duties.get(i)); //add this to the arrayList
			}
		}
		
		for(int k = 0; k < notAssigned.size(); k++) { //loop through all duties that are not assigned
			boolean hasBeenAssigned = false; //assume duty has not been assigned
			Duty unAssigned = notAssigned.get(k); //access the duty
			for(int j = 0; j < unAssigned.getPossibleTeachers().size(); j++) { //go through the duties possible teacher
				int indexInMainList = findTeacherByID(unAssigned.getPossibleTeachers().get(j).getId()); //find the teacher in the main arrayList
				Teacher reAssign = teachers.get(indexInMainList); //access the teacher 
				for(int p = 0; p < reAssign.getAssignedDuties().size(); p++) { //go through the teachers assignedDuties
					int indexForDutyInMainList = findDutyById(reAssign.getAssignedDuties().get(p)); //find the duty in the main list
					Duty dReAssign = duties.get(indexForDutyInMainList); //access the duty from the main list
					for(int o = 0; o < dReAssign.getPossibleTeachers().size(); o++) { //go through that duty's possible teachers
						Teacher toBeReassigned = dReAssign.getPossibleTeachers().get(o); //access the teacher
						boolean canBeAssigned = checkTeacher(toBeReassigned,dReAssign);
						System.out.println(canBeAssigned);
						if(canBeAssigned) {
							System.out.println(toBeReassigned.toString());
						}
						if(canBeAssigned && toBeReassigned.getId() != reAssign.getId()) { //check if another teacher from the duty's possible teachers can be assigned
							int indexForAssignedDuty = findAssignedDutyByID(dReAssign); //get this duty's index in the AssignedDuties list
							assignedDuties.get(indexForAssignedDuty).setTeacher(dReAssign.getPossibleTeachers().get(o)); //change it so that this teacher is made available
							AssignedDuty a = new AssignedDuty(unAssigned,reAssign); //create a new assigned duty with this unassigned duty and this teacher
							assignedDuties.add(a); //add the assigned duty to the list
							p = reAssign.getAssignedDuties().size(); //change p to be the max value such that it will break out
							j = unAssigned.getPossibleTeachers().size(); //change j to be the max value such that it will break out
							hasBeenAssigned = true; //duty has been assigned
							break; //break out of the loop
						}
					}
				}
			}
			if(hasBeenAssigned) {
			int indexForReplacing = findDutyById(unAssigned); //find the duties index as it has been assigned
			duties.get(indexForReplacing).setHasBeenAssigned(true); //change it such that the duty has been assigned
			}
		}
		
	}
	
	public static boolean ensureAllDutiesAssigned(){
		for(int i = 0; i < duties.size(); i++) {
			if(duties.get(i).isHasBeenAssigned() == false) { //check if a duty has not been assigned
				System.out.println(duties.get(i));
				return false; //return false
			}
		}
		return true;
	}
	public static void sortForFinalOutput() { //sort method for final output. Sort by ID's as they have been made such that when each duty is added, a new ID is generated
		Collections.sort(assignedDuties, new Comparator<AssignedDuty>() { //use collections.sort method
			public int compare(AssignedDuty a, AssignedDuty b) { //create new compare method 
				return a.getDuty().getId() - b.getDuty().getId(); //compare the ID's and return
				
			}
		});

	
	}
	
	public static int findAssignedDutyByID(AssignedDuty a) { //method to search for assignedDuties with another assignedDuty
		int i = 0; //counter variable
		while(assignedDuties.get(i).getDuty().getId() != a.getDuty().getId()) { //while the ID has not been found
			i = i + 1; //move on to next index
			if(i == assignedDuties.size()) { //check if it equal to the size
				return i; //return the size as the name has not been found
			}
		}
		return i; //return the index
	}
	public static int findAssignedDutyByID(Duty a) { //method to search for assignedDuties with a duty
		int i = 0; //counter variable
		while(assignedDuties.get(i).getDuty().getId() != a.getId()) { //while the ID has not been found
			i = i + 1; //move on to next index
			if(i == assignedDuties.size()) { //check if it equal to the size
				return i; //return the size as the name has not been found
			}
		}
		return i; //return the index
	}
	public static boolean importPeriodSix() { //import teachers who have period six
	System.out.println("Enter file path for list of teachers with period 6"); //output message 
	String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/Period_6.csv"; //read the path
	try {
			BufferedReader br = new BufferedReader(new FileReader(path)); //create a new bufferedReader
			br.readLine(); //read the first Line as it is not important
			while(br.ready()) { //while the BufferedReader is ready
				line = br.readLine(); //read the line
				String values[] = line.split(","); //split it and store in array 
				int Id = Integer.parseInt(values[0]); //get the Id which is the first element 
				String [] tempDaysOfTheWeek = new String[values.length - 2]; //create a new array called days of the week with a size two less than values (First two elements are name and ID)
				for (int q = 0; q < tempDaysOfTheWeek.length; q++) { //loop through subjects array
					tempDaysOfTheWeek[q] = values[q + 2]; //store the subject in the array
				}
				boolean teacherFound = false; //flag variable
				for(int i = 0; i < teachers.size(); i++) { //go through the whole teachers arrayList
					
					if(teachers.get(i).getId() == Id) { //check if the Id matches of the teacher in array list to Id in period 6 list
						teacherFound = true; //teacher found so set true
						for(int k = 0; k < tempDaysOfTheWeek.length; k++) { //go through array and populate teacher
							int index = searchForDay(tempDaysOfTheWeek[k]); //find the index of the day of the week (I.e if monday then 0, tuesday than 1 etc)
							Lesson [] tempLessons = teachers.get(i).getLessons();//get the teachers lessons
							tempLessons[index].six = true; //change period six on that day to be true
							teachers.get(i).setLessons(tempLessons); 
							
						}
					}
					if(teacherFound) { //if the teacher has been found and populated, break out of the loop
						break; //break out of the for loop and move onto the next teacher who has period 6
					}
				}
				if(!teacherFound) {
					System.out.println("Error, Id: " + Id + " not found. Please fix");
					return false;
				}
			} 
				
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	for(int k = 0; k < teachers.size(); k++) {
		teachers.get(k).setNoOfLessonsOnEachDay();
	}
		return true;
	}
	public static boolean searchFor(char[] k, String name) { //method to validate strings
		for(int i = 0; i < name.length(); i++) { //go through whole string
			char p = name.charAt(i); //get the string at the index
			if(searchFor(k,p) == false) { //check if it is not in the array
				return false; //return false
			}
		}
		return true;
	}
	public static boolean custom(String decision) { //method to create a custom set of no of duties to be assigned to each teacher. Useful if one year there are many duties and less teachers requiring teachers to maybe do more than usual
		decision = decision.trim(); //remove any unwanted spaces
		String yes = "yes"; //if client chooses yes
		String no = "no"; //if client chooses no
		
		if(decision.equalsIgnoreCase(yes) == false && decision.equalsIgnoreCase(no) == false) { //if the input is neither yes or two
			System.out.println("Error, not an option"); //tell user it is not an option
			return false; //return false
		}
		else if(decision.equals(no)) { //if the user chooses no
			setTeachersDutiesToBeAssigned(); //method that sets using the default method
			
		}
		else {
			System.out.println("Enter how many hours for teacher to not be assigned any duties");
			String hforNone = in.next(); //get the input
			while(searchFor(IdNumbers,hforNone) == false) { //if it is not a number
				System.out.println("Error, please enter a number"); //output error message
				hforNone = in.next(); //get the input again
			}
			System.out.println("Enter how many hours for teacher to be assigned one duty");
			String hforOne = in.next();
			while(searchFor(IdNumbers,hforOne) == false) { //if it is not a number
				System.out.println("Error, please enter a number"); //output error message
				hforOne = in.next(); //get the input again
			}
			System.out.println("Enter how many duties for admin");
			String fAdmin = in.next();
			while(searchFor(IdNumbers,fAdmin) == false) { //if it is not a number
				System.out.println("Error, please enter a number"); //output error message
				fAdmin = in.next(); //get the input again
			}
			System.out.println("Enter how many hours for teacher to be considedered part time");
			String hPartTime = in.next(); //get the input
			while(searchFor(IdNumbers,hPartTime) == false) { //if it is not a number
				System.out.println("Error, please enter a number"); //output error message
				hPartTime = in.next(); //get the input again
			}
			System.out.println("Enter how many duties to be assigned to part timers");
			String fPartTime = in.next();
			while(searchFor(IdNumbers,fPartTime) == false) { //if it is not a number
				System.out.println("Error, please enter a number");
				fPartTime = in.next(); //get the input again
			}
			System.out.println("Enter how many duties to be assigned to normal teachers");
			String fNormal = in.next();
			while(searchFor(IdNumbers,fNormal) == false) { //if it is not a number
				System.out.println("Error, please enter a number");
				fNormal = in.next(); //get the input again
			}
			//extract the information as integers
			int hoursForNone = Integer.parseInt(hforNone);
			int hoursForOne = Integer.parseInt(hforOne);
			int forAdmin = Integer.parseInt(fAdmin);
			int hoursForPartTime = Integer.parseInt(hPartTime);
			int forPartTimers = Integer.parseInt(fPartTime);
			int forNormal = Integer.parseInt(fNormal);
			
			for(int i = 0; i < teachers.size(); i++) { //loop through teachers array
				teachers.get(i).setDutiesToBeAssigned(hoursForNone, hoursForOne, forAdmin, hoursForPartTime, forPartTimers, forNormal); //go through all teachers and set their number of duties using this method
			}
					
		}
		return true; //return true as the function has accepted the inputs
	}
	public static boolean importTeachers() { //returns a boolean, if true, then the teachers have been imported properly, if false means there is error
	System.out.println("Enter file path for teachers (Option + Right Click then select 'Copy as Pathname' "); // Ask user to input the filepath
	 
		String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/HS_TT_summary_23Oct20.csv"; //create a new string called path which stores the filepath
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
				if(isValidID(values[0]) == false) { //check if it is a valid ID.
					System.out.println("Error. ID " + values[0] + " of teacher " + values[1] + " is invalid. Please fix and re-enter"); //output error message
					return false; //return false as import is not successful. 
				}
				int Id = Integer.parseInt(values[0]); //first element is ID
				if(isValidName(values[1]) == false) { //check if the name is valid
					System.out.println("Error. Name " + values[1] + " of teacher with ID " + values[0] + " is invalid. Please fix and re-enter"); //output error message
					return false;
				}
				String name = values[1];// second element is Name  
				if(isValidLessonsPerWeek(values[2]) == false) { //check if the lessons per week is not valid+
					System.out.println("Error, Lessons per Week for " + values[1] + " with ID" + values[0] + " is not valid, please fix and re-enter"); //output error message
					return false;
				}
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
			 //close the scanner.
			System.out.println("Error, File not found"); //output error message. 
			return false; //return false as there is an error that has occurred
		} catch (IOException e) { //catch the IO exception caused by br.readLine()
			// TODO Auto-generated catch block
			e.printStackTrace(); 
			//close the scanner
			return false; //return false as there is an error that has occurred 
		}
		

		return true; //Import successful
	}
	public static boolean importAdmins() { //importing all the admins 
		System.out.println("Enter file path for List of Admins");
	String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/Teachers_Admin_List.csv";
	try {
			//First add all admin IDs to an arraylist
			BufferedReader br = new BufferedReader(new FileReader(path));//create a new BufferedReader
			br.readLine(); //read the first line as it is not important
			while(br.ready()) { //While the bufferedReader is ready
				line = br.readLine(); //read the first line
				String[] values = line.split(","); //create an array which has values with split
				if(searchFor(IdNumbers,values[1]) == false) { //check to make sure it is an ID
					System.out.println("Error, ID : " + values[1] + " of teacher" + values[0] + " is not valid. Please fix and re enter"); //output message
					return false;
				}
				int id = Integer.parseInt(values[1]);// Second element is the ID
				adminIds.add(id);//add the ID to the list
			}
			br.close(); //close the bufferedreader
		} catch (FileNotFoundException e) {
			System.out.println("Error, File not found");
			return false;
		} catch (IOException e) {
			System.out.println("Error, input output is wrong. Please refer to guide");
			return false;
		}
		//Check adminID against all teachers and then set admin if found
		for (int k = 0; k < adminIds.size(); k++) { //going through all the adminIds
			int id = adminIds.get(k); //get the adminId
			boolean adminfound = false; //boolean to find the admin, assume not found
			for(int i = 0; i < teachers.size(); i++) { //going through all the teachers
				if(teachers.get(i).getId() == id ) { //check if teachers id matches the adminId
					teachers.get(i).setAdmin(true); //set the teacher to be an admin
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
		return true; //import successful

	}
	public static boolean importSubjects() { //import the teacher subjects 
	
		System.out.println("Enter file path for teaching departments"); // ask user to input path name
		String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/HS_Tchng_Depts.csv"; //store path name 

	try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			br.readLine(); //read the first line as it is not important
			int i = 0; //counter variable
			while(br.ready()) { //while it is ready
				line = br.readLine();
				String [] values = line.split(","); //split and store into array
				int Id = Integer.parseInt(values[0]); //get the teachers id
				String [] Subjects = new String[values.length - 3]; //create a new array with size 3 less than the length of values (First 3 elements are ID, Last name and name)
				for (int q = 0; q < Subjects.length; q++) { //loop through the subjects
					Subjects[q] = values[q + 3]; //store the subject into the array
				}
				Subjects[0] = Subjects[0].replace('\"', ' '); //replace the quotation mark with a space
				Subjects[Subjects.length - 1] = Subjects[Subjects.length - 1].replace('\"', ' '); //replace the last " again with a space
				//for printing and testing
				Teacher temp = teachers.get(i); //get the teacher
				if(Id != temp.getId()) { //check if ID does not match with ID stored in ArrayList (it should because it is in alphabetical order but just incase
					i++; //increase i
					temp = teachers.get(i); //move onto next teacher
				} 
					boolean SubjectFound = false; //assume subject has not yet been found 
					Subject [] tempSubjects = new Subject[Subjects.length]; //create a new array the lenght of the subjects array
					for(int j = 0; j < tempSubjects.length; j++) { //loop through all of the teachers subjects
						String sName = Subjects[j]; //get the name of the teacher's subject
						sName = sName.trim(); //trim it so that all the spaces are removed
						for (int k = 0; k < SubjectsWithDays.size(); k++) { //loop through arrayList with subjects and meeting days
							String sNameInList = SubjectsWithDays.get(k).getName(); //get the name of the subject in the list
							if(sName.equals(sNameInList)) { //check if names math
								tempSubjects[j] = SubjectsWithDays.get(k); //if they do then set that to the tempSubjects array
								SubjectFound = true; //subject has been found
								break; //break out of the loop
							}
						}
						if(!SubjectFound) { //if subject is not found
							System.out.println("Error, Teacher : " + teachers.get(i).getName() + " with subject " + sName + " not found. Please fix and re enter"); //output error message
							return false; //return false as subject was not found
						}
					SubjectFound = false; //set back to false as moving on to next subject. 
					}
					temp.setSubject(tempSubjects); //set the teacher subjects to be this
				
				teachers.set(i, temp); //add the new teacher to the teachers arraylist with the subjects
				i++; // increase and move on to the next teacher
				
				
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
	
	public static boolean importSubjectMeetingDays() { //method to import subjects with meeting days
			System.out.println("Enter file path for list of subjects and meeting days"); 
			String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/Subject_Area_Meeting_Days.csv"; //read the filepath
	try {
			BufferedReader br = new BufferedReader(new FileReader(path)); //create a new buffered reader 
			br.readLine(); //read first line as it is not important
			
			while(br.ready()) { //loop while it is ready 
				line = br.readLine(); //read the line 
				String [] values = line.split(","); //split and store into array 
				if(validSubjectName(values[0]) == false) { //check if the name is not valid
					System.out.println("Error, Subject name " + values[0] + " is not a valid name. Please fix and re-enter"); //output error message
					return false; //import not successful. 
				}
				if(validDayOfTheWeek(values[1]) == false) { //check if the day of the week is not valid
					System.out.println("Error, Subject name " + values[1] + " is not a valid day of the week. Please fix and re-enter"); //output error message
					return false; //import not successful.
				}
				Subject s = new Subject(values[0].trim(),values[1]); //first value is the name and second is meetingday
				SubjectsWithDays.add(s); //store into the array list
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true; //import successful 
	}
	
	public static boolean importDutiesTakeTwo() {
		System.out.println("Enter path for list of duties");
		String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/HS_Duties_2020.csv";
		int id = 0;
		Time oldStartTime = new Time(0,0); //create a new oldStart Time with 0 0 
		try {
			BufferedReader br = new BufferedReader(new FileReader(path)); //create a new buffered reader
			br.readLine(); //read the first line as it is not important
			while(br.ready()) { //while the buffered reader is ready 
				line = br.readLine(); //read the line
				String [] values = line.split(","); //split the line by commas and store it into a new array
				String name = values[0]; //first element is the name of the duty
				name = name.trim(); //remove any unnecessary spaces if present
				if(isValidDutyName(name) == false) { //check if the duty name is not valid 
					System.out.println("Error. Duty name " + name + " is not valid. Please fix and re enter");
					return false;
				}
				String timeInString = values[1]; //second element is the times 
				String [] times = timeInString.split("-"); //split the time in terms of "-", there are two times one being the starting time and the second being the ending time
				String [] forStartTime = times[0].split(":"); //now for the start time which is the first value stored in values array, split it by the ":". So if the value was like 11:35, this will give me an array with the values of 11 and 35
				//trim to get rid of any unnecessary spaces 
				for(int z = 0; z < forStartTime.length; z++) {
					forStartTime[z] = forStartTime[z].trim();
				}
				for(int z = 0; z < forStartTime.length; z++) {
					if(searchFor(IdNumbers,forStartTime[z]) == false) {
						System.out.println("Error, Time " + forStartTime[z] + " for duty '" + name +  "' is not valid. Please fix and re-enter");
						return false;
					}
					String temp = forStartTime[z]; //get as string
					if(temp.length() == 0 ) { //if it's length is 0
						System.out.println("Error, Time " + forStartTime[z] + " for duty '" + name +  "' is not valid. Please fix and re-enter");
						return false;
					}
				}
				int startHours = Integer.parseInt(forStartTime[0].trim()); //first element is the hours
				int starMin = Integer.parseInt(forStartTime[1].trim()); //second element is the minutes
				Time startTime = new Time(startHours, starMin); //create a new time with those parameters
				//repeat process for endTime
				String[] forEndTime = times[1].split(":");
				for(int z = 0; z < forEndTime.length; z++) {
					forEndTime[z] = forEndTime[z].trim();
				}
				for(int z = 0; z < forEndTime.length; z++) {
					if(searchFor(IdNumbers,forEndTime[z]) == false) {
						System.out.println("condition 1, end time");
						System.out.println("Error, Time " + forEndTime[z] + " for duty '" + name +  "' is not valid. Please fix and re-enter");
						return false;
					}
					
				}
				int endHours = Integer.parseInt(forEndTime[0].trim()); //first element is the hours
				int endMin = Integer.parseInt(forEndTime[1].trim()); //second element is the minutes
				Time endTime = new Time(endHours, endMin); 
				if(validTimes(startTime,endTime) == false) { //check if the times are not valid
					System.out.println("Error, start time " + startTime.toString() + " and end time " + endTime.toString() + " for duty: '" + name + "' are not valid. Please fix and re enter"); //output message
					return false; //import not successful
				}
				if(oldStartTime.equals(startTime) == false) { //check if new there is a new startTime
					AssignedTimes a = new AssignedTimes(startTime, falseOnAllDays); //create a new assigned Times with false indicating teacher has not been assigned yet
	
					oldStartTime = startTime; //change the oldStartTime to the startTime
				}
				for(int i = 0; i < values.length - 2; i++) { //loop through the length of the duties array - the first two elements
					if(values[i + 2].length() > 0 && searchForDay(values[i+2]) != -1) { //check if it it's length is longer than 0 and it is a day of the week
					String dayofTheWeek = values[i + 2]; //get the day of the week
					Duty temp = new Duty(name,startTime,endTime,dayofTheWeek,id); //create a new duty
					id = id + 1; //increase id
					duties.add(temp); //add duty into array list
					}
				}
			}
			//exceptions with appropriate error messages
		} catch (FileNotFoundException e) {
			System.out.println("Error, File not found");
			return false;
		} catch (IOException e) {
			System.out.println("Error, input does not match system. Please check handout to see how input should be");
			return false;
		} 

		return true; //import successful 
	}
	
	public static boolean generateCSV() {
		System.out.println("Enter file path to where you wish to save final list"); //get the location client wishes to store final list 
		String path = in.next(); //store the location
		System.out.println("Enter name of file"); // get the name of the file
		String name = in.next(); //store the name of the file 
		try {
			PrintWriter pw = new PrintWriter(new File(path + "/" + name + ".csv")); //create a new PrintWriter. Add a "/" after path (Normal way to save files on a computer) and a ".csv" (The file format of my file) 
			StringBuilder sb = new StringBuilder(); //create a new string builder which will store the actual values
			sb.append("Day of the week "); //first element is the day of the week
			sb.append(","); //comma to separate the values 
			sb.append("Duty name"); //second element is the duty name
			sb.append(",");
			sb.append("Start time"); //third element is the start time
			sb.append(",");
			sb.append("End time"); //fourth element is the end time
			sb.append(",");
			sb.append("Teacher name"); // fifth element is the name of the teacher
			sb.append(",");
			sb.append("Teacher ID"); //sixth element is the teacher ID
			sb.append("\r\n"); //moves on to the next line
			for(int i = 0; i < assignedDuties.size(); i++) { //loop through all assigned duties 
				sb.append(assignedDuties.get(i).toStringWithCommas()); // use with commas method in AssignedDuty class (Puts in an element and then separates with a ",")
				sb.append("\r\n"); //move on to the next line after adding each teacher
			}
			pw.write(sb.toString()); // Write the string into the file
			pw.close(); //close the pw
			System.out.println("Finished"); //output message
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Error, File path not found");
			return false;
		}
		return true; //generated successfully
	}
}

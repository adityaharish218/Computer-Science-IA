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
	public static ArrayList<FixTime> fixTimes = new ArrayList<FixTime>();
	public static ArrayList<Duty> dutiesYetToBeAssigned = new ArrayList<Duty>(); //arraylist for duties that still have to be assigned
	public static ArrayList<Teacher> teachersStillCanBeAssigned = new ArrayList<Teacher>(); //arraylist for teachers that can still be assigned duties
	public static final String[] daysOfTheWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};	
	public static int noOfAdmins = 0; // the number of admins 
	public static int dutiesAssignedToAdmins = 0; // the number of duties that have been assigned to admins
	public static Lesson allFalse = new Lesson(false,false,false,false,false,false,false); //Creating an lesson with all lessons being false
	public static Scanner in = new Scanner(System.in); //public scanner that is used by all methods
	public static String line = "";
	public static final char[] numbers = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '-', ':', ' ', '0'}; //array for checking of time
	public static final char[] IdNumbers = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}; //array for validation of ID
	public static final char[] allowedChars = {'(', ')', ' ', '.', '/', ',', '-'};

	public static void main(String[] args) {
		boolean b = importTeachers();
		boolean c = importSubjectMeetingDays(); 
		boolean d = importSubjects();
		boolean e = importAdmins();
		boolean f = importPeriodSix();
		boolean g = importDutiesTakeTwo(); 
		assignPossibleTeachers();
		sortByPossibleTeachers();
		sortByPossibleDuties();
		
		System.out.println("Teachers by no of different duties they can do ");
		for(int i = 0; i < teachers.size(); i++) {
			System.out.println(teachers.get(i).toStringWithNoOfDuties());
		}
		System.out.println("Duties by number of possible teachers ");
		for(int k = 0; k < duties.size(); k++) {
			System.out.println(duties.get(k).toString() + " teachers who can do this duty, " + duties.get(k).getPossibleTeachers().size());
		}
		
		
		boolean k = assignTeachers();
			while(k == false) {
				k = assignTeachers();
			}
		
		generateCSV();
		
		System.out.println(duties.size());
		in.close();
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
		for(int z = 0; z < teachers.size(); z++) { //looping through teachers arrayList so far
			String compare = "" + teachers.get(z).getId(); //convert to string by getting teacher ID
			compare = compare.trim(); //remove unnecessary spaces
			if(id.equals(compare)) { //check if ID matches that of list
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
			if(searchFor(numbers,k) == false) { //try to find the character in the numbers array and if it can't it means that it is not a number
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
			Teacher teacher = teachers.get(i); //gets the teacher
			teacher.setDutiesToBeAssigned(); //calls the setDutiesToBeAssigned function which sets the number of duties each teacher can have based on conditions
			teachers.set(i, teacher); // sets the teacher back into the list
		}
	}

	public static boolean assignTeachers() {
		for (int i = 0; i < duties.size(); i++) { // going through all duties
			while( i < duties.size() && duties.get(i).isHasBeenAssigned() == true ) { //check if the duty has been assigned
				i = i + 1; //move on to the next duty
			}
			if(i == duties.size()) { //check if i = to the size
				break; //break out of the for loop
			}
			Duty assigned = duties.get(i); // accessing duty
			Time dutyStartTime = assigned.getStartTime();// get the start time of the duty
			for (int j = 0; j < teachers.size(); j++) { // going through all teachers
				boolean canAssign = true; //Assume teacher can be assigned duty
				Teacher teacher = teachers.get(j); // accessing teacher
				if(teacher.assignedDays[searchForDay(assigned.getDayOfTheWeek())]) { //check if teacher has already been assigned a duty on that day
					canAssign = false; //teacher cannot be assigned duty. 
				}
				if(assigned.isHasBeenAssigned()) { //check if duty has already been assigned 
					canAssign = false; //cannot assign as duty has already been assigned. 
				}
				if(teacher.getDutiesAssigned() == teacher.getDutiesToBeAssigned()) { //check if the teacher can have more duties
					canAssign = false; //Teacher cannot be assigned duty if the duties assigned are greater are greater
				}
				
				if(teacher.isAssignedAfterSchool()) { //check if the teacher has already been assigned an afterSchool duty
					canAssign = false;
				}
				if (dutyStartTime.getHours() == 8) { // Check if the duty begins at before school, Begins at 8:00
					
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
						canAssign = false; //Teacher cannot be assigned duty as they have already been assigned the max 

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

					

				}
			
				if(canAssign) { //check if the teacher can be assigned
					AssignedDuty a = new AssignedDuty(assigned,teacher); //create a new assigned duty with the teacher
					assignedDuties.add(a); //add this duty along with assigned teacher
					teachers.get(j).setDutiesAssigned(teachers.get(j).getDutiesAssigned() + 1); //increase the teachers number of duties by 1
					teachers.get(j).assignedDays[searchForDay(assigned.getDayOfTheWeek())] = true; //change it such that teacher has been assigned today 
					if(dutyStartTime.getHours() >= 14) { //check if the duty is after school
						teachers.get(j).setAssignedAfterSchool(true); //the teacher has already been assigned an afterschool duty
					}
					
					if(teacher.isAdmin()) { // check if the teacher being assigned is admin
						dutiesAssignedToAdmins = dutiesAssignedToAdmins + 1; //if they are, increase it by one
					}
					duties.get(i).setHasBeenAssigned(true); //duty has been assigned. 
					break; //break out of loop as teacher has been assigned 
				}
			}
		}
		for(int k = 0; k < duties.size(); k++) {
			if(duties.get(k).isHasBeenAssigned() == false) { // check if a duty has not yet been assigned 
				
				return false; //not all duties have been assigned 
			}
		}
	return true; //all duties have been assigned;
		
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
				if (dutyStartTime.getHours() == 8) { // Check if the duty begins at before school, Begins at 8:00
					if(teacher.isAdmin() == false ) { //check if teacher is admin or not, my client usually gives admin the early morning duties. Also check if there are any admins still left to be assigned duties. 
						canAssign = false; // cannot assign duty because it needs to be admin
					}
					if(teacher.isHomebase() == true) { //Check if teacher has a homebase or not
						canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty
					}


				}
				if (dutyStartTime.getHours() == 11) { // Check if the duty begins at break, break begins at 11 hence hours = 11
					Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
					int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
					if(teacherLessons[index].two == true || teacherLessons[index].three == true) { //Check if teacher has lesson on period 1
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
					if(teacherLessons[index].four == true || teacherLessons[index].five == true || teacherLessons[index].lunch == true) { //Check if teacher has lesson on period 4, lunch or 5
						canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty

					}
				}
				if (dutyStartTime.getHours() >= 14) { // Check if the duty begins after school. The after school duty is varied between 15:30 and 17:00 hence check if the hour is greater than 14

					Lesson [] teacherLessons = teacher.getLessons(); //access teacher lessons
					int index = searchForDay(assigned.getDayOfTheWeek());// access the day of the week as index
					if(teacherLessons[index].six == true) { //Check if teacher has lesson on period 6
						canAssign = false; //Set can assign to false as the teacher cannot be assigned the duty

					}

					

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
		Collections.sort(duties, new Comparator<Duty>() { //call sorth method from collections class (Learnt from https://www.youtube.com/watch?v=wzWFQTLn8hI)
			public int compare(Duty d1, Duty d2) { //create a compare method for duties
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
	public static void assignTheTeacher() { //assign the teacher
		for(int i = 0; i < duties.size(); i++) { //loop through the whole teachers array
			for(int j = 0; j < duties.get(i).getPossibleTeachers().size(); j++) { //loop through the duty's possible teacher
				boolean canAssign = true; //assume teacher can be assigned the duty
			}
		}
	}
	
	public static int findTeacherByID(int id) { //method to find teacher by ID. I do not need to worry about validation as the data has already been validated
		int i = 0; //counter variable
		while(teachers.get(i).getId() != id) { //while the teacher's ID does not equal the id we need to find 
			i++; //increase I 
		}
		return i; //return i as teacher has been found
	}
	
	public static boolean importPeriodSix() { //import teachers who have period six
	//System.out.println("Enter file path for list of teachers with period 6"); //output message 
	//	String path = in.next(); //read the path
		System.out.println("Importing period six");
		String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/Period_6.csv";
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
		
		return true;
	}
	public static boolean importTeachers() { //returns a boolean, if true, then the teachers have been imported properly, if false means there is error
	//System.out.println("Enter file path for teachers (Option + Right Click then select 'Copy as Pathname' "); // Ask user to input the filepath
//	 
		//String path = in.next(); //create a new string called path which stores the filepath
	System.out.println("Importing Teachers");
		String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/HS_TT_summary_23Oct20.csv";
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
					System.out.println("Error. Name " + values[1] + " of teacher with ID " + values[0] + " is invalid. Please fix and re-enter");
					return false;
				}
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
			 //close the scanner.
			System.out.println("Error, File not found"); //output error message. 
			return false; //return false as there is an error that has occurred
		} catch (IOException e) { //catch the IO exception caused by br.readLine()
			// TODO Auto-generated catch block
			e.printStackTrace(); 
			//close the scanner
			return false; //return false as there is an error that has occurred 
		}
		//close the scanner

		return true; //Import successful
	}
	public static boolean importAdmins() { //importing all the admins 
//		System.out.println("Enter file path for List of Admins");
//	String path = in.next();
		System.out.println("Importing admins");
		String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/Teachers_Admin_List.csv";
		try {
			//First add all admin IDs to an arraylist
			BufferedReader br = new BufferedReader(new FileReader(path));//create a new BufferedReader
			br.readLine(); //read the first line as it is not important
			while(br.ready()) { //While the bufferedReader is ready
				line = br.readLine(); //read the first line
				String[] values = line.split(","); //create an array which has values with split
				int id = Integer.parseInt(values[1]);// Second element is the ID
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
		setTeachersDutiesToBeAssigned(); //change the teacher duties 
		return true; //import successful

	}
	public static boolean importSubjects() { //import the teacher subjects 
	
		//System.out.println("Enter file path for teaching departments"); // ask user to input path name
		//String path = in.next(); //store path name 

		System.out.println("Importing Subjects");
		String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/HS_Tchng_Depts.csv";
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
				if(Id != temp.getId()) { //check if ID does not match with ID stored in ArrayList (it should because it is alphabetical order
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
		//	System.out.println("Enter file path for list of subjects and meeting days"); 
		//	String path = in.next(); //read the filepath
		System.out.println("Import Subjects with Meeting days");
		String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/Subject_Area_Meeting_Days.csv";
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
	
	public static boolean importDuties() {
		System.out.println("Enter file path for duties"); //Prompt output of file path 
	//	String path = in.next(); //store path
		String path = "/Users/adityaharish/Documents/Documents/Subjects/CompSci/G11/Computer-Science-IA/Files/HS_Duties_2020-1.csv";
	String dayOfTheWeek = "Monday"; //create a day of the week and set it to monday which is the first duty
		Time startTime = new Time(0,0); //create a new start time
		Time endTime = new Time(0,0); //create a new end time 
		int index = 0;
		int indexforfixTimes = 0;
		int counter = 0;
		System.out.println("In arrayList immediately after I've added the Duty ");
		try {
			BufferedReader br = new BufferedReader(new FileReader(path)); //create a new bufferedReader
			br.readLine(); //read the first line as it is not important (It is monday but that is already stored)
			boolean [] allDaysofTheWeek = {true,false,false,false,false}; //an array to ensure that all days of the week have been covered. Monday is true 
			while(br.ready()) { //while the bufferedReader is ready
			line = br.readLine(); //read the line and store into the string line
			line = line.trim(); //remove all uncessary spaces 
			if(line == null) { //check if it's null
				line = ""; //change it to empty string (Avoid null pointer exception) 
			}
			
			// System.out.println(">>>" + searchForDay(line) + " " + isInteger(line) + line);
			
			if(searchForDay(line.trim()) > -1 ) { //use search for day on line, this returns an index and the default is -1, if it is not -1 that means that this is a day of the week and check if it is not null or empty ("")
				dayOfTheWeek = line; //day of the week is now line
				allDaysofTheWeek[searchForDay(line)] = true; //set the boolean to true as that particular day of the week has been found
			}
			else if (isInteger(line.trim()) && setLesson(line)) { //check if line is likely to be a time which means that all it's characters are in the numbers array. Also make sure that it's length is greater than 0
				String [] values = line.split("-"); //split the values in terms of "-", the time is like "11:35 - 12:00". So by splitting it in terms of -, it will give me two values
				String [] forStartTime = values[0].split(":"); //now for the start time which is the first value stored in values array, split it by the ":". So if the value was like 11:35, this will give me an array with the values of 11 and 35
				
				int starHour = Integer.parseInt(forStartTime[0]); //get the integer in the first value which will be the hour. Trim to remove the spaces
				int starMin = Integer.parseInt(forStartTime[1].trim()); //get the integer in the second value which will be the minute. Trim to remove the spaces 
				startTime.setTime(starHour, starMin);
				//repeat the process for the ending time 
				String [] forEndTime = values[1].split(":");
				int endHour = Integer.parseInt(forEndTime[0].trim());
				int endMin = Integer.parseInt(forEndTime[1].trim());
				endTime.setTime(endHour, endMin);
				if(validTimes(startTime, endTime) == false) { //check if times are not valid
					System.out.println("Error," + startTime.toString() + " and " + endTime.toString() + " are not valid. Please ensure that end time is later than start time. Please fix and re-enter");
					return false; //import unsuccessful. 
				}
				FixTime temp = new FixTime(dayOfTheWeek,startTime,endTime,counter); //create a new FixTime object
				fixTimes.add(temp); //add it to the ArrayList
				fixTimes.set(indexforfixTimes,temp);
				indexforfixTimes = indexforfixTimes + 1;
			}
			else if(setLesson(line)) { //Check if the length of line is greater than 0;  The duties between days are separated by an empty row which means that it is not a duty Name and it's length is 0
				if(isValidDutyName(line) == false) { //check if it is not a valid name
					System.out.println("Error, Duty name " + line + " is not valid. Please fix and re-enter"); //output error message
					return false; //import not successful
				}
				String dutyName = line; // if it isn't the other two, then it is the name of the duty. Store that as a string
				Duty temp = new Duty(dutyName, startTime, endTime, dayOfTheWeek); //create a new duty with these parameters
				temp.setStartTime(startTime);
				temp.setEndTime(endTime);
				duties.add(temp); //add it to the duties array list. 
				counter++; //increase the counter for number of duties 
				//printing and testing 
				
					System.out.println("Duty no " + (index + 1));
					System.out.println("Day of the week " + duties.get(index).getDayOfTheWeek());
					System.out.println("Name " + duties.get(index).getName());
					System.out.println("Start time " + duties.get(index).getStartTime().toString());
					System.out.println("End time " + duties.get(index).getEndTime().toString());
					System.out.println();
				
				index++;
			}
			}
			for(int i = 0; i < allDaysofTheWeek.length; i++) { //loop through all days of the week
				if(allDaysofTheWeek[i] == false) { //if you find one false which means one day has not been covered
					System.out.println("Error, " + daysOfTheWeek[i] + " not found. Please fix and re-enter");
					return false; //import not successful
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Error, file not found");
			return false;
		} 
		catch (IOException e) {
			
			//e.printStackTrace();
			return false;
		}
		
		return true; //import successful
	}
	
	public static void fixTimes() {
		int startIndex = 0; //starting index
		for(int i = 0; i < fixTimes.size(); i++) { //loop through all fixtimes arrayList
			int endIndex = fixTimes.get(i).getUpTo(); //get the last index of which the duties times need to be changed
			for(int j = startIndex; j < endIndex; j++) { //loop from start index to end index indicating the number of duties 
				duties.get(j).setStartTime(fixTimes.get(i).getStartTime()); //set the duties start time to that of the fixtimes start time at the index
				duties.get(j).setEndTime(fixTimes.get(i).getEndTime()); //set the duties end time to that of the fixTimes end time at the index
			}
			startIndex = endIndex; 
		}
		int prevprint = 0;
		for(int k = 0; k < fixTimes.size(); k++) {
			System.out.println("For duty no " + (prevprint + 1) + " to duty no " + fixTimes.get(k).getUpTo() + ", the start time is " + fixTimes.get(k).getStartTime().toString() + " and the end time is " + fixTimes.get(k).getEndTime().toString());
			prevprint = fixTimes.get(k).getUpTo();
		}
		
	}
	
	public static boolean importDutiesTakeTwo() {
		System.out.println("Enter path for list of duties");
		String path = in.next();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path)); //create a new buffered reader
			br.readLine(); //read the first line as it is not important
			while(br.ready()) { //while the buffered reader is ready 
				line = br.readLine(); //read the line
				String [] values = line.split(","); //split the line by commas and store it into a new array
				String name = values[0]; //first element is the name of the duty
				String timeInString = values[1]; //second element is the times 
				String [] times = timeInString.split("-"); //split the time in terms of "-", there are two times one being the starting time and the second being the ending time
				String [] forStartTime = times[0].split(":"); //now for the start time which is the first value stored in values array, split it by the ":". So if the value was like 11:35, this will give me an array with the values of 11 and 35
				//trim to get rid of any unnecessary spaces 
				int startHours = Integer.parseInt(forStartTime[0].trim()); //first element is the hours
				int starMin = Integer.parseInt(forStartTime[1].trim()); //second element is the minutes
				Time startTime = new Time(startHours, starMin); //create a new time with those parameters
				//repeat process for endTime
				String[] forEndTime = times[1].split(":");
				int endHours = Integer.parseInt(forEndTime[0].trim()); //first element is the hours
				int endMin = Integer.parseInt(forEndTime[1].trim()); //second element is the minutes
				Time endTime = new Time(endHours, endMin); 
				for(int i = 0; i < values.length - 2; i++) { //loop through the length of the duties array - the first two elements
					if(values[i + 2].length() > 0 && searchForDay(values[i+2]) != -1) { //check if it it's length is longer than 0 and it is a day of the week
					String dayofTheWeek = values[i + 2]; //get the day of the week
					Duty temp = new Duty(name,startTime,endTime,dayofTheWeek); //create a new duty
					duties.add(temp); //add duty into array list
					}
				}
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
			pw.close();
			System.out.println("Finished");
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
